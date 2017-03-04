package com.shange.mobilesave.service;

import java.util.List;

import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.AndroidProcess;
import com.jaredrummler.android.processes.models.AndroidProcesses;
import com.shange.mobilesave.activity.EnterPsdActivity;
import com.shange.mobilesave.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewDebug.FlagToString;

public class WatchDogService extends Service {

	protected static final String tag = "WatchDogService";
	private List<String> lockList;
	boolean isWatch ;
	private String skipPackageName;
	private AppLockDao mDao;
	private MyObserver myObserver;
	private InnerReceiver mInnerReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isWatch = true;
		mDao = AppLockDao.getInstance(getApplicationContext());
		//����һ��ѭ��,���ӳ���
		watch();
		//ע��һ���㲥������
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.SKIP");
		mInnerReceiver = new InnerReceiver();
		//�ô���ע��㲥,��Ҫ�ô�������
		registerReceiver(mInnerReceiver, intentFilter);

		//ע��,��Ϊ��Ա����,����ȡ��
		myObserver = new MyObserver(new Handler());

		getContentResolver().registerContentObserver(Uri.parse("content://applock/change"), true,myObserver);//ontent://applock/change

	}
	class MyObserver extends ContentObserver{

		public MyObserver(Handler handler) {
			super(handler);

		}
		@Override
		public void onChange(boolean selfChange) {
			// �����ݿⷢ���ı�ʱ,����
			new Thread(){
				@Override
				public void run() {
					//���»�ȡ����Ӧ�õİ���
					Log.i(tag, "change");
					lockList = mDao.findAll();
				}
			}.start();


			super.onChange(selfChange);
		}

	}


	class InnerReceiver extends BroadcastReceiver{



		@Override
		public void onReceive(Context context, Intent intent) {
			//��ȡ���͹㲥�����д��ݹ����İ���,�����˴ΰ���������

			skipPackageName = intent.getStringExtra("packageName");

		}	
	}
	private void watch(){
		//1.���߳���,����һ���ɿ���ѭ��
		new Thread(){
			/* (non-Javadoc)
			 * @see java.lang.Thread#run()
			 */
			public void run() {
				//1.���Ȼ�ȡ�������ĳ���
				lockList = mDao.findAll();
				while(isWatch){
					//2.�����������ڿ�����Ӧ��,����ջ
					//3.��ȡactivity�������
					/*	ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					//4.��ȡ������������ջ�ķ���
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					RunningTaskInfo runningTaskInfo = runningTasks.get(0);
					//5.��ȡջ����activity,Ȼ���ڻ�ȡ��activity����Ӧ�õİ���
					String packageName = runningTaskInfo.topActivity.getPackageName();*/
					List<AndroidAppProcess> runningForegroundApps = AndroidProcesses.getRunningForegroundApps(getApplicationContext());
					//�����������ǌ�
					for (AndroidAppProcess androidAppProcess : runningForegroundApps) {
						Log.i(tag, 1+"");
						if(lockList.contains(androidAppProcess.getPackageName())){//����˵��������
							//���
							if(!androidAppProcess.getPackageName().equals(skipPackageName)){//������˵��������ȷ,����Ҫ�ڼ�����
								Log.i(tag, 2+"");
								//7.�������ؽ���
								Intent intent = new Intent(getApplicationContext(),EnterPsdActivity.class);
								intent.putExtra("packageName",androidAppProcess.getPackageName());
								//�����п���activity��Ҫ��������ջ
								//����һ���µ�����ջ
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}

						}
						//˯��һ��,ʱ��Ƭ��ת
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {		
		super.onDestroy();
		//ֹͣ���Ź�ѭ��
		isWatch = false;
		//ע���㲥������
		if(mInnerReceiver!=null){
			unregisterReceiver(mInnerReceiver);
		}
		//ע�����ݹ۲���
		if(myObserver!=null){
			getContentResolver().unregisterContentObserver(myObserver);
		}

	}

}

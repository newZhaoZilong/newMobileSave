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
		//创建一个循环,监视程序
		watch();
		//注册一个广播接受者
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.SKIP");
		mInnerReceiver = new InnerReceiver();
		//用代码注册广播,需要用代码销毁
		registerReceiver(mInnerReceiver, intentFilter);

		//注册,改为成员变量,方便取消
		myObserver = new MyObserver(new Handler());

		getContentResolver().registerContentObserver(Uri.parse("content://applock/change"), true,myObserver);//ontent://applock/change

	}
	class MyObserver extends ContentObserver{

		public MyObserver(Handler handler) {
			super(handler);

		}
		@Override
		public void onChange(boolean selfChange) {
			// 当数据库发生改变时,调用
			new Thread(){
				@Override
				public void run() {
					//重新获取锁定应用的包名
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
			//获取发送广播过程中传递过来的包名,跳过此次包名检测过程

			skipPackageName = intent.getStringExtra("packageName");

		}	
	}
	private void watch(){
		//1.子线程中,开启一个可控死循环
		new Thread(){
			/* (non-Javadoc)
			 * @see java.lang.Thread#run()
			 */
			public void run() {
				//1.首先获取已锁定的程序
				lockList = mDao.findAll();
				while(isWatch){
					//2.监视现在正在开启的应用,任务栈
					//3.获取activity管理对象
					/*	ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					//4.获取真正运行任务栈的方法
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					RunningTaskInfo runningTaskInfo = runningTasks.get(0);
					//5.获取栈顶的activity,然后在获取此activity所在应用的包名
					String packageName = runningTaskInfo.topActivity.getPackageName();*/
					List<AndroidAppProcess> runningForegroundApps = AndroidProcesses.getRunningForegroundApps(getApplicationContext());
					//这个工具类就是屌
					for (AndroidAppProcess androidAppProcess : runningForegroundApps) {
						Log.i(tag, 1+"");
						if(lockList.contains(androidAppProcess.getPackageName())){//包含说明上锁了
							//如果
							if(!androidAppProcess.getPackageName().equals(skipPackageName)){//如果相等说明密码正确,不需要在加锁了
								Log.i(tag, 2+"");
								//7.弹出拦截界面
								Intent intent = new Intent(getApplicationContext(),EnterPsdActivity.class);
								intent.putExtra("packageName",androidAppProcess.getPackageName());
								//服务中开启activity需要创建任务栈
								//开启一个新的任务栈
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}

						}
						//睡眠一下,时间片轮转
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
		//停止看门狗循环
		isWatch = false;
		//注销广播接受者
		if(mInnerReceiver!=null){
			unregisterReceiver(mInnerReceiver);
		}
		//注销内容观察者
		if(myObserver!=null){
			getContentResolver().unregisterContentObserver(myObserver);
		}

	}

}

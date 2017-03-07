package com.shange.mobilesave.activity;

import java.lang.reflect.Method;
import java.util.List;

import com.shange.mobilesave.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CacheClearActivity extends Activity {

	/**
	 * ��ȡ������Ϣ����״̬��
	 */
	protected static final int UPDATE_CACHE_APP = 100;
	protected static final int CHECK_CACHE_APP = 101;
	protected static final int CHECK_FINISH = 102;
	protected static final int CLEAR_CACHE = 104;
	protected static final String tag = "CacheClearActivity";
	private Button bt_clear;
	private ProgressBar pb_bar;
	private TextView tv_name;
	private LinearLayout ll_add_text;
	private PackageManager mPM;
	private int mIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cacheclear);

		initUI();

		initData();
	}
	private Handler mHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_CACHE_APP:
				final CacheInfo cacheInfo = (CacheInfo) msg.obj;
				//8.�����Բ�������ӻ���Ӧ����Ŀ
				View view = View.inflate(getApplicationContext(), R.layout.item_linearlayout_cache, null);
				TextView tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
				TextView tv_cache_info = (TextView) view.findViewById(R.id.tv_cache_info);
				ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
				//��ֵ
				tv_app_name.setText(cacheInfo.name);
				tv_cache_info.setText(Formatter.formatFileSize(getApplicationContext(), cacheInfo.cacheSize) );
				iv_icon.setImageDrawable(cacheInfo.icon);
				
				//�����Ŀ
				ll_add_text.addView(view, 0);
				iv_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//ͨ���鿴ϵͳ��־,��ȡ����������activity��action��data
						Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
						
						intent.setData(Uri.parse("package:"+cacheInfo.packagename));
						startActivity(intent);
						
					}
				});
				
				
				break;
			case CHECK_CACHE_APP:
				tv_name.setText((String)msg.obj);
								
				break;
			case CHECK_FINISH:
				tv_name.setText("ɨ�����");
								
				break;
			case CLEAR_CACHE:
				//���listview
				ll_add_text.removeAllViews();				
				
				break;

			}
			
		};
		
	};



	/**
	 * ��ʼ��UI
	 */
	private void initUI() {
		bt_clear = (Button) findViewById(R.id.bt_clear);
		pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		ll_add_text = (LinearLayout) findViewById(R.id.ll_add_text);
		bt_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//1.��ȡָ������ֽ����ļ�
				try {
					Class<?> clazz = Class.forName("android.content.pm.PackageManager");
					//2.��ȡ���÷�������
					Method method = clazz.getMethod("freeStorageAndNotify", long.class,IPackageDataObserver.class);
					//3.��ȡ������÷���
					method.invoke(mPM, Long.MAX_VALUE,new IPackageDataObserver.Stub() {
						@Override
						public void onRemoveCompleted(String packageName, boolean succeeded)
								throws RemoteException {
							//���������ɺ���õķ���(����Ȩ��)
							Message msg = Message.obtain();
							msg.what = CLEAR_CACHE;
							mHandler.sendMessage(msg);
						}
					});
				} catch (Exception e) {
					Message msg = Message.obtain();
					msg.what = CLEAR_CACHE;
					mHandler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		});
		

	}

	/**
	 * �����ֻ�����Ӧ��,��ȡ�л����Ӧ��
	 */
	private void initData() {
		new Thread(){
			@Override
			public void run() {
				mPM = getPackageManager();
				//��ȡ���а�װ��Ӧ��
				List<PackageInfo> installedPackages = mPM.getInstalledPackages(0);
				//���������������ֵ
				pb_bar.setMax(installedPackages.size());
				//4.����ÿһ��Ӧ��,��ȡ�л����Ӧ����Ϣ(Ӧ������,ͼ��,�����С,����)
				for (PackageInfo packageInfo : installedPackages) {
					String packageName = packageInfo.packageName;					

					getPackageCache(packageName);
					mIndex++;
					pb_bar.setProgress(mIndex);
					//ÿѭ��һ�ν�������Ӧ����ʾ��textview��
					Message msg = Message.obtain();
					msg.what = CHECK_CACHE_APP;
					String name = null;
					try {
						name = mPM.getApplicationInfo(packageName, 0).loadLabel(mPM).toString();
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					msg.obj = name;
					mHandler.sendMessage(msg);
				}
				Message msg = Message.obtain();
				msg.what = CHECK_FINISH;
				mHandler.sendMessage(msg);
			}
		}.start();
	}


	class CacheInfo{
		public String name;
		public Drawable icon;
		public String packagename;
		public long cacheSize;	
	}

	/**
	 * ͨ��������ȡ�ΰ���ָ��Ӧ�õĻ�����Ϣ
	 * @param packageName Ӧ�ð���
	 */
	protected void getPackageCache(final String packageName) {
		//1.��ȡָ������ֽ����ļ�
		IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {

			public void onGetStatsCompleted(PackageStats stats,
					boolean succeeded) {
				//���߳��з���,�õ���Ϣ����
			// �����С�Ĺ���,���߳��д���,����ȥ����UI
				long cacheSize = stats.cacheSize;
				//5.�жϻ����С�Ƿ����0
				CacheInfo cacheInfo = null;
				if(cacheSize>0){//����0�Ž��в���
					
					//6.��֪���̸߳���UI
					Message msg = Message.obtain();

					msg.what = UPDATE_CACHE_APP;
					//7.ά���л���Ӧ�õ�javabean
					cacheInfo = new CacheInfo();
					cacheInfo.cacheSize = cacheSize;
					cacheInfo.packagename = stats.packageName;
					try {
						cacheInfo.name = mPM.getApplicationInfo(stats.packageName, 0).loadLabel(mPM).toString();
						cacheInfo.icon = mPM.getApplicationInfo(stats.packageName, 0).loadIcon(mPM);
						
						Log.i(tag, "Ӧ����"+ cacheInfo.name+"  �����С:"+stats.cacheSize) ;
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msg.obj = cacheInfo;
					//������Ϣ
					mHandler.sendMessage(msg);
					
				}


			}
		};
		//1.��ȡָ������ֽ����ļ�
		try{
			Class<?> clazz = Class.forName("android.content.pm.PackageManager");
			//2��ȡ���÷�������
			Method method = clazz.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
			//3.���÷���
			method.invoke(mPM,packageName,mStatsObserver);

		}catch(Exception e){

			e.printStackTrace();
		}

	}
}

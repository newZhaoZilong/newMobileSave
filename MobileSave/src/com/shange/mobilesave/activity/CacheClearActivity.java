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
	 * 获取缓存信息对象状态码
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
				//8.在线性布局中添加缓存应用条目
				View view = View.inflate(getApplicationContext(), R.layout.item_linearlayout_cache, null);
				TextView tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
				TextView tv_cache_info = (TextView) view.findViewById(R.id.tv_cache_info);
				ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
				//赋值
				tv_app_name.setText(cacheInfo.name);
				tv_cache_info.setText(Formatter.formatFileSize(getApplicationContext(), cacheInfo.cacheSize) );
				iv_icon.setImageDrawable(cacheInfo.icon);
				
				//添加条目
				ll_add_text.addView(view, 0);
				iv_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//通过查看系统日志,获取开启清理缓存activity中action和data
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
				tv_name.setText("扫描完成");
								
				break;
			case CLEAR_CACHE:
				//清空listview
				ll_add_text.removeAllViews();				
				
				break;

			}
			
		};
		
	};



	/**
	 * 初始化UI
	 */
	private void initUI() {
		bt_clear = (Button) findViewById(R.id.bt_clear);
		pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		ll_add_text = (LinearLayout) findViewById(R.id.ll_add_text);
		bt_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//1.获取指定类的字节码文件
				try {
					Class<?> clazz = Class.forName("android.content.pm.PackageManager");
					//2.获取调用方法对象
					Method method = clazz.getMethod("freeStorageAndNotify", long.class,IPackageDataObserver.class);
					//3.获取对象调用方法
					method.invoke(mPM, Long.MAX_VALUE,new IPackageDataObserver.Stub() {
						@Override
						public void onRemoveCompleted(String packageName, boolean succeeded)
								throws RemoteException {
							//清除缓存完成后调用的方法(考虑权限)
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
	 * 遍历手机所有应用,获取有缓存的应用
	 */
	private void initData() {
		new Thread(){
			@Override
			public void run() {
				mPM = getPackageManager();
				//获取所有安装的应用
				List<PackageInfo> installedPackages = mPM.getInstalledPackages(0);
				//给进度条设置最大值
				pb_bar.setMax(installedPackages.size());
				//4.遍历每一个应用,获取有缓存的应用信息(应用名称,图标,缓存大小,包名)
				for (PackageInfo packageInfo : installedPackages) {
					String packageName = packageInfo.packageName;					

					getPackageCache(packageName);
					mIndex++;
					pb_bar.setProgress(mIndex);
					//每循环一次将遍历的应用显示在textview上
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
	 * 通过包名获取次包名指向应用的缓存信息
	 * @param packageName 应用包名
	 */
	protected void getPackageCache(final String packageName) {
		//1.获取指定类的字节码文件
		IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {

			public void onGetStatsCompleted(PackageStats stats,
					boolean succeeded) {
				//子线程中方法,用到消息机制
			// 缓存大小的过程,子线程中代码,不能去处理UI
				long cacheSize = stats.cacheSize;
				//5.判断缓存大小是否大于0
				CacheInfo cacheInfo = null;
				if(cacheSize>0){//大于0才进行操作
					
					//6.告知主线程更新UI
					Message msg = Message.obtain();

					msg.what = UPDATE_CACHE_APP;
					//7.维护有缓存应用的javabean
					cacheInfo = new CacheInfo();
					cacheInfo.cacheSize = cacheSize;
					cacheInfo.packagename = stats.packageName;
					try {
						cacheInfo.name = mPM.getApplicationInfo(stats.packageName, 0).loadLabel(mPM).toString();
						cacheInfo.icon = mPM.getApplicationInfo(stats.packageName, 0).loadIcon(mPM);
						
						Log.i(tag, "应用名"+ cacheInfo.name+"  缓存大小:"+stats.cacheSize) ;
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msg.obj = cacheInfo;
					//发送消息
					mHandler.sendMessage(msg);
					
				}


			}
		};
		//1.获取指定类的字节码文件
		try{
			Class<?> clazz = Class.forName("android.content.pm.PackageManager");
			//2获取调用方法对象
			Method method = clazz.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
			//3.调用方法
			method.invoke(mPM,packageName,mStatsObserver);

		}catch(Exception e){

			e.printStackTrace();
		}

	}
}

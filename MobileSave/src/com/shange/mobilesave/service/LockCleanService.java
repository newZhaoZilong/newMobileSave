package com.shange.mobilesave.service;

import com.shange.mobilesave.engine.ProcessInfoProvider;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockCleanService extends Service {

	private InnerReceiver innerReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		
		super.onCreate();
		//锁屏action
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);//监听屏幕关闭的动作
		
		innerReceiver = new InnerReceiver();
		//注册广播接受者
		registerReceiver(innerReceiver, intentFilter);
		System.out.println("服务已开启");
		
	}
	@Override
	public void onDestroy() {
		
		if(innerReceiver!=null){
			unregisterReceiver(innerReceiver);
		}
		super.onDestroy();
		System.out.println("服务已关闭");
	}
	class InnerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//清理手机正在运行的进程
			ProcessInfoProvider.killAll(context);
			System.out.println("清理");
		}
		
	}

}

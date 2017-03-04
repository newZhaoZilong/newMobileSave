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
		//����action
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);//������Ļ�رյĶ���
		
		innerReceiver = new InnerReceiver();
		//ע��㲥������
		registerReceiver(innerReceiver, intentFilter);
		System.out.println("�����ѿ���");
		
	}
	@Override
	public void onDestroy() {
		
		if(innerReceiver!=null){
			unregisterReceiver(innerReceiver);
		}
		super.onDestroy();
		System.out.println("�����ѹر�");
	}
	class InnerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//�����ֻ��������еĽ���
			ProcessInfoProvider.killAll(context);
			System.out.println("����");
		}
		
	}

}

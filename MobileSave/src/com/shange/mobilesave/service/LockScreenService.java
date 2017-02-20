package com.shange.mobilesave.service;





import com.shange.mobilesave.receiver.DeviceAdminSampleReceiver;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class LockScreenService extends Service {

	private ComponentName mDeviceAdminSample;
	private DevicePolicyManager mDPM;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mDeviceAdminSample = new ComponentName(getApplicationContext(), DeviceAdminSampleReceiver.class);
		mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
		//是否开启的判断
				if(mDPM.isAdminActive(mDeviceAdminSample)){//设备是激活的
					mDPM.lockNow();
					mDPM.resetPassword("123", 0);//锁屏同时去设置密码,没用
				}else{
					Toast.makeText(this, "请先激活", 0).show();
				}
	}
}

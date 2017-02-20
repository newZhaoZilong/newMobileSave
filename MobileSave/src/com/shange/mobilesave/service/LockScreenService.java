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
		//�Ƿ������ж�
				if(mDPM.isAdminActive(mDeviceAdminSample)){//�豸�Ǽ����
					mDPM.lockNow();
					mDPM.resetPassword("123", 0);//����ͬʱȥ��������,û��
				}else{
					Toast.makeText(this, "���ȼ���", 0).show();
				}
	}
}

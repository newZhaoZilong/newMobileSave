package com.shange.mobilesave.service;

import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//������λ����
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		//�����ŵķ�ʽ��ȡ��γ������
		Criteria criteria = new Criteria();
		//������
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//ָ����ȡ��γ�ȵľ�ȷ��
		String bestProvider = lm.getBestProvider(criteria, true);
		//3,��һ��ʱ����,�ƶ�һ��������ȡ��γ������
		lm.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				//��ȡ��γ��
				double longitude = location.getLongitude();
				double latitude = location.getLatitude();
				//4,���Ͷ���
				
				SmsManager smsManager = SmsManager.getDefault();
				String phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
				smsManager.sendTextMessage(phone, null,"����longtitude:"+longitude+"γ��latitude:"+latitude, null,null);
				
			}
		});
	}
	
}

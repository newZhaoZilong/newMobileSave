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
		//开启定位服务
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		//以最优的方式获取经纬度坐标
		Criteria criteria = new Criteria();
		//允许花费
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//指定获取经纬度的精确度
		String bestProvider = lm.getBestProvider(criteria, true);
		//3,在一定时间间隔,移动一定距离后获取经纬度坐标
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
				//获取经纬度
				double longitude = location.getLongitude();
				double latitude = location.getLatitude();
				//4,发送短信
				
				SmsManager smsManager = SmsManager.getDefault();
				String phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
				smsManager.sendTextMessage(phone, null,"经度longtitude:"+longitude+"纬度latitude:"+latitude, null,null);
				
			}
		});
	}
	
}

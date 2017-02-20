package com.shange.mobilesave.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.text.TextUtils;

public class ServiceUtil {

	/**
	 * @param ctx
	 * @param serviceName �ж��Ƿ��������еķ���
	 * @return true ���� false û������
	 */
	public static boolean isRunning(Context ctx,String serviceName){
		
		//1,��ȡactiivtyManager�����߶���,����ȥ��ȡ��ǰ�ֻ��������еķ���
		ActivityManager am = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);
		//����һ����ǰ������Ϣ�ļ���
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		//����list
		for(RunningServiceInfo serviceinfo : runningServices){
			//�ж��Ƿ���sercieName��ͬ,�Ӷ��ó��Ƿ���Ҫ�ҵ��ĳ���
			if(serviceName!=null&&serviceName.equals(serviceinfo.service.getClassName())){
				return true ;
			}
			
			
		}
		//����,����false
		return false;
	}
}

package com.shange.mobilesave.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.text.TextUtils;

public class ServiceUtil {

	/**
	 * @param ctx
	 * @param serviceName 判断是否正在运行的服务
	 * @return true 运行 false 没有运行
	 */
	public static boolean isRunning(Context ctx,String serviceName){
		
		//1,获取actiivtyManager管理者对象,可以去获取当前手机正在运行的服务
		ActivityManager am = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);
		//这是一个当前服务信息的集合
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		//遍历list
		for(RunningServiceInfo serviceinfo : runningServices){
			//判断是否与sercieName相同,从而得出是否是要找到的程序
			if(serviceName!=null&&serviceName.equals(serviceinfo.service.getClassName())){
				return true ;
			}
			
			
		}
		//否则,返回false
		return false;
	}
}

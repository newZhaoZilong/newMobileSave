package com.shange.mobilesave.engine;

import java.util.ArrayList;
import java.util.List;

import com.shange.mobilesave.db.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppEngine {

	/**
	 * 获取系统中所有应用程序的信息
	 *获取系统中所有应用程序的信息
	 */
	public static List<AppInfo> getAppInfos(Context context){
		
		List<AppInfo> arrayList = new ArrayList<AppInfo>();
		//需要管理者
		//这个packageManager就直接获取了
		PackageManager pm = context.getPackageManager();
		//要获取所有,所以参数选0
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		/*
		 * 	//名称
	private String name;
	//图标
	private Drawable icon;
	//包名
	private String packageName;
	//版本号
	private String versionName;
	//是否安装到SD卡
	private boolean isSD;
	//是否是用户程序
	private boolean isUser;
		 * */
		//遍历集合
		for (PackageInfo packageInfo : packages) {
			//获取packageInfo对象
			String packageName = packageInfo.packageName;
			
			String versionName = packageInfo.versionName;
			//获取应用信息
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			//获取图标
			Drawable icon = applicationInfo.loadIcon(pm);
			//获取版本名称
			String name = (String) applicationInfo.loadLabel(pm);
			//判断是否是用户程序
			//获取应用程序中相关信息,是否是系统程序和是否安装到SD卡
			boolean isUser;
			
			int flags = applicationInfo.flags;
			
			if((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM){
				//等于ture说明是系统程序
				isUser = false;
			}else{
				//用户程序
				isUser = true;
			}
			//是否安装到SD卡
			
			boolean isSD;
			
			if((applicationInfo.FLAG_EXTERNAL_STORAGE & flags) == applicationInfo.FLAG_EXTERNAL_STORAGE){
				//安装到sd卡
				isSD = true;
			}else{
				//安装位置在手机内存
				isSD = false;
			}
			//添加到bean中
			AppInfo appInfo = new AppInfo(name, icon, packageName, versionName, isSD, isUser);
			
			//将bean存档到list集合
			arrayList.add(appInfo);
			
		}
		
		return arrayList;	
	}
}

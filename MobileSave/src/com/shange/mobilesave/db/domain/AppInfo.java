package com.shange.mobilesave.db.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	
	
	//名称
	public String name;
	//图标
	public Drawable icon;
	//包名
	public String packageName;
	//版本号
	public String versionName;
	//是否安装到SD卡
	public boolean isSD;
	//是否是用户程序
	public boolean isUser;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public boolean isSD() {
		return isSD;
	}
	public void setSD(boolean isSD) {
		this.isSD = isSD;
	}
	public boolean isUser() {
		return isUser;
	}
	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}
	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", icon=" + icon + ", packageName="
				+ packageName + ", versionName=" + versionName + ", isSD="
				+ isSD + ", isUser=" + isUser + "]";
	}
	public AppInfo() {
		super();//这个super什么意思
	}
	public AppInfo(String name, Drawable icon, String packageName,
			String versionName, boolean isSD, boolean isUser) {
		super();
		this.name = name;
		this.icon = icon;
		this.packageName = packageName;
		this.versionName = versionName;
		this.isSD = isSD;
		this.isUser = isUser;
	}
	
	
	
	
}

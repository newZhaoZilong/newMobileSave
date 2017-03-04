package com.shange.mobilesave.db.domain;

import android.graphics.drawable.Drawable;

public class ProcessInfo {

	public String name;//应用名称
	public Drawable icon;//应用图标
	public long memSize;//应用已使用的内存数
	public boolean isCheck;//是否被选中
	public boolean isSystem;//是否为系统应用,否则是用户应用
	public String packageName;//如果进程没有名称,则将其所在应用的包名作为名称
	
	public int pid;
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
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
	public long getMemeSize() {
		return memSize;
	}
	public void setMemeSize(long memeSize) {
		this.memSize = memeSize;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	
}

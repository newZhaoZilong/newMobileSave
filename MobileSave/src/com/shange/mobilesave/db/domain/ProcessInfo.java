package com.shange.mobilesave.db.domain;

import android.graphics.drawable.Drawable;

public class ProcessInfo {

	public String name;//Ӧ������
	public Drawable icon;//Ӧ��ͼ��
	public long memSize;//Ӧ����ʹ�õ��ڴ���
	public boolean isCheck;//�Ƿ�ѡ��
	public boolean isSystem;//�Ƿ�ΪϵͳӦ��,�������û�Ӧ��
	public String packageName;//�������û������,��������Ӧ�õİ�����Ϊ����
	
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

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
	 * ��ȡϵͳ������Ӧ�ó������Ϣ
	 *��ȡϵͳ������Ӧ�ó������Ϣ
	 */
	public static List<AppInfo> getAppInfos(Context context){
		
		List<AppInfo> arrayList = new ArrayList<AppInfo>();
		//��Ҫ������
		//���packageManager��ֱ�ӻ�ȡ��
		PackageManager pm = context.getPackageManager();
		//Ҫ��ȡ����,���Բ���ѡ0
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		/*
		 * 	//����
	private String name;
	//ͼ��
	private Drawable icon;
	//����
	private String packageName;
	//�汾��
	private String versionName;
	//�Ƿ�װ��SD��
	private boolean isSD;
	//�Ƿ����û�����
	private boolean isUser;
		 * */
		//��������
		for (PackageInfo packageInfo : packages) {
			//��ȡpackageInfo����
			String packageName = packageInfo.packageName;
			
			String versionName = packageInfo.versionName;
			//��ȡӦ����Ϣ
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			//��ȡͼ��
			Drawable icon = applicationInfo.loadIcon(pm);
			//��ȡ�汾����
			String name = (String) applicationInfo.loadLabel(pm);
			//�ж��Ƿ����û�����
			//��ȡӦ�ó����������Ϣ,�Ƿ���ϵͳ������Ƿ�װ��SD��
			boolean isUser;
			
			int flags = applicationInfo.flags;
			
			if((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM){
				//����ture˵����ϵͳ����
				isUser = false;
			}else{
				//�û�����
				isUser = true;
			}
			//�Ƿ�װ��SD��
			
			boolean isSD;
			
			if((applicationInfo.FLAG_EXTERNAL_STORAGE & flags) == applicationInfo.FLAG_EXTERNAL_STORAGE){
				//��װ��sd��
				isSD = true;
			}else{
				//��װλ�����ֻ��ڴ�
				isSD = false;
			}
			//��ӵ�bean��
			AppInfo appInfo = new AppInfo(name, icon, packageName, versionName, isSD, isUser);
			
			//��bean�浵��list����
			arrayList.add(appInfo);
			
		}
		
		return arrayList;	
	}
}

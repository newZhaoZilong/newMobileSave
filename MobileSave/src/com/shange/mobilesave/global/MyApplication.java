package com.shange.mobilesave.global;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

public class MyApplication extends Application {

	
	protected static final String tag = "MyApplication";

	@Override
	public void onCreate() {
		// 
		super.onCreate();
		//����ȫ��(Ӧ������ģ��)�쳣
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				//�������ȡ����Ϊ������쳣��,����ķ���
				ex.printStackTrace();
				Log.i(tag,"������һ��������쳣");
				//�����񵽵��쳣�洢��sd����
				String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"error100.log";
				File file = new File(path);
				
				PrintWriter printWriter;
				try {
					printWriter = new PrintWriter(file);

					ex.printStackTrace(printWriter);
					
					printWriter.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//�ϴ���˾������
				//����Ӧ��
				System.exit(0);
			}
		});
		
		
		
	}
	
}

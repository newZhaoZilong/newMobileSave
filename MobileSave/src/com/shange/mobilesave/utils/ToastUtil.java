package com.shange.mobilesave.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	/**
	 * @param �����Ļ���
	 * @param ��ӡ�ı�����
	 */
	public static void show(Context applicationContext, String string) {
		
		Toast.makeText(applicationContext, string, 0).show();
	}	
}

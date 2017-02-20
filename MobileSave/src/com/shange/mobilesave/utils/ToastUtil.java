package com.shange.mobilesave.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	/**
	 * @param 上下文环境
	 * @param 打印文本内容
	 */
	public static void show(Context applicationContext, String string) {
		
		Toast.makeText(applicationContext, string, 0).show();
	}	
}

package com.shange.mobilesave.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class FocusTextView extends TextView {
//ʹ�����ù�java���봴������
	public FocusTextView(Context context) {
		super(context);
		//
	}
	//��ϵͳ����(������+�����Ĺ��췽��)
	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 
	}
	//��ϵͳ����(������+�����Ļ������췽��+�����ļ��ж�����ʽ���췽��)
	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//
	}
	//��д��ȡ����ķ���,��ϵͳ����,���õ�ʱ��Ĭ�Ͼ��ܻ�ȡ����
	@Override
		@ExportedProperty(category = "focus")
		public boolean isFocused() {
			
			return true;
		}
}

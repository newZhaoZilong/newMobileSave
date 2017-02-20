package com.shange.mobilesave.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class FocusTextView extends TextView {
//使用在用过java代码创建控制
	public FocusTextView(Context context) {
		super(context);
		//
	}
	//有系统调用(带属性+上下文构造方法)
	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 
	}
	//有系统调用(带属性+上下文环境构造方法+布局文件中定义样式构造方法)
	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//
	}
	//重写获取焦点的方法,由系统调用,调用的时候默认就能获取焦点
	@Override
		@ExportedProperty(category = "focus")
		public boolean isFocused() {
			
			return true;
		}
}

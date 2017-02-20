package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class Setup1Activity extends BaseSetupActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		
	}
	
	/**
	 * 跳转到下一界面
	 * @param view点击的那个控件,为啥这方法必须是public
	 */
	
	//实现滑动到下一页的方法
	//1.监听屏幕上响应的事件类型(按下(1次),移动(多次),抬起(1次)
	

	@Override
	public void showPrePage() {
		//
		
	}

	@Override
	protected void showNextPage() {
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		//开启了一个新的界面后,关闭功能列表界面
		
		finish();
		//开启平移动画
		overridePendingTransition(R.anim.next_in, R.anim.next_current_out);
		
	}
	
}

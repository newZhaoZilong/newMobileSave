package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity {

	private GestureDetector gestureDetector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		//2.创建手势管理的对象,用作管理在onTouchEvent(event)传递过来的手势动作
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
			//e1起始位置,e2结束位置
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//监听手势的移动
				if(e1.getX()>e2.getX()){
					
					
					showNextPage();
				}
				if(e1.getX()<e2.getX()){
					//由左向右,移动到上一页
					showPrePage();
				}
				
				return super.onFling(e1, e2, velocityX, velocityY);
				
			}
		});
	}

	public abstract void showPrePage();
	//就是因为这个view对象不知道怎么办,属于nextPage特有的,才造成需要将nextPage中的方法包装起来
	protected abstract void showNextPage();

	/**
	 * 跳转到下一界面
	 * @param view点击的那个控件,为啥这方法必须是public
	 */
	public void nextPage(View view){
		showNextPage();//调用
	}
	public void prePage(View view){
		showPrePage();//调用
	}
	//实现滑动到下一页的方法
	//1.监听屏幕上响应的事件类型(按下(1次),移动(多次),抬起(1次)
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//通过手势处理类,接收多种类型的事件,用作处理
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
}

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
		//2.�������ƹ���Ķ���,����������onTouchEvent(event)���ݹ��������ƶ���
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
			//e1��ʼλ��,e2����λ��
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//�������Ƶ��ƶ�
				if(e1.getX()>e2.getX()){
					
					
					showNextPage();
				}
				if(e1.getX()<e2.getX()){
					//��������,�ƶ�����һҳ
					showPrePage();
				}
				
				return super.onFling(e1, e2, velocityX, velocityY);
				
			}
		});
	}

	public abstract void showPrePage();
	//������Ϊ���view����֪����ô��,����nextPage���е�,�������Ҫ��nextPage�еķ�����װ����
	protected abstract void showNextPage();

	/**
	 * ��ת����һ����
	 * @param view������Ǹ��ؼ�,Ϊɶ�ⷽ��������public
	 */
	public void nextPage(View view){
		showNextPage();//����
	}
	public void prePage(View view){
		showPrePage();//����
	}
	//ʵ�ֻ�������һҳ�ķ���
	//1.������Ļ����Ӧ���¼�����(����(1��),�ƶ�(���),̧��(1��)
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//ͨ�����ƴ�����,���ն������͵��¼�,��������
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
}

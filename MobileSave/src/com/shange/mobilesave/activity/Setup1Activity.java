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
	 * ��ת����һ����
	 * @param view������Ǹ��ؼ�,Ϊɶ�ⷽ��������public
	 */
	
	//ʵ�ֻ�������һҳ�ķ���
	//1.������Ļ����Ӧ���¼�����(����(1��),�ƶ�(���),̧��(1��)
	

	@Override
	public void showPrePage() {
		//
		
	}

	@Override
	protected void showNextPage() {
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		//������һ���µĽ����,�رչ����б����
		
		finish();
		//����ƽ�ƶ���
		overridePendingTransition(R.anim.next_in, R.anim.next_current_out);
		
	}
	
}

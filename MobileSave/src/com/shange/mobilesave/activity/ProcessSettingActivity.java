package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;
import com.shange.mobilesave.service.LockCleanService;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.ServiceUtil;
import com.shange.mobilesave.utils.SpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ProcessSettingActivity extends Activity {

	private CheckBox  cb_show_system,cb_lock_clear;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processsetting);
		
		initUI();
		
		initSystem();
		
		initLockClean();
	}







	/**
	 * ��ʼ��UI
	 */
	private void initUI() {
		cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
		
		cb_lock_clear = (CheckBox) findViewById(R.id.cb_lock_clear);	
	}
	/**
	 * ��ʾϵͳ���̵ļ����¼�
	 */
	private void initSystem() {
		//�ȴ�sp�л�ȡ״̬
		boolean state = SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, true);
		
		//���ݴ洢��״̬,�ж��Ƿ���Ҫ��ʾ����
		if(state){//��ǰ״̬����isChecked
			cb_show_system.setText("��ʾϵͳ����");
		}else{
			cb_show_system.setText("����ϵͳ����");
		}
		cb_show_system.setChecked(state);
		//��ѡ��ı�ʱ���ø÷���
		
		cb_show_system.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//��ȡ��ǰ״̬
				if(isChecked){//��ǰ״̬����isChecked
					cb_show_system.setText("��ʾϵͳ����");
				}else{
					cb_show_system.setText("����ϵͳ����");
				}
		//		cb_show_system.setChecked(isChecked);,��仰����д,˵��checkbox�ǿ��Ա������
				//��״̬�洢��sp��
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, isChecked);				
			}
		});
		
	}
	/**
	 * �Ƿ�����������
	 */
	private void initLockClean() {
		
		//�жϷ����Ƿ���
		boolean running = ServiceUtil.isRunning(getApplicationContext(), "com.shange.mobilesave.service.LockCleanService");
		
		//���ݴ洢��״̬,�ж��Ƿ���Ҫ��ʾ����
		if(running){//��ǰ״̬����isChecked
			cb_lock_clear.setText("���������ѿ���");
		}else{
			cb_lock_clear.setText("���������ѹر�");
		}
		cb_lock_clear.setChecked(running);
		//��ѡ��ı�ʱ���ø÷���
		
		cb_lock_clear.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//��ȡ��ǰ״̬
				if(isChecked){//��ǰ״̬����isChecked
					cb_lock_clear.setText("���������ѿ���");
					//������������ķ���,Ϊʲô��ֱ�Ӵ�����broadcast����
					Intent intent = new Intent(getApplicationContext(),LockCleanService.class);
					startService(intent);
					
				}else{
					cb_lock_clear.setText("���������ѹر�");
					//�ر���������ķ���
					Intent intent = new Intent(getApplicationContext(),LockCleanService.class);
					stopService(intent);
				}
		//		cb_show_system.setChecked(isChecked);,��仰����д,˵��checkbox�ǿ��Ա������
				//��״̬�洢��sp��
		//		SpUtil.putBoolean(getApplicationContext(), ConstantValue.LOCK_CLEAN, isChecked);				
			}
		});
		
	}
}

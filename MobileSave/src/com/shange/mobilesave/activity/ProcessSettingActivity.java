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
	 * 初始化UI
	 */
	private void initUI() {
		cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
		
		cb_lock_clear = (CheckBox) findViewById(R.id.cb_lock_clear);	
	}
	/**
	 * 显示系统进程的监听事件
	 */
	private void initSystem() {
		//先从sp中获取状态
		boolean state = SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, true);
		
		//根据存储的状态,判断是否需要显示进程
		if(state){//当前状态就是isChecked
			cb_show_system.setText("显示系统进程");
		}else{
			cb_show_system.setText("隐藏系统进程");
		}
		cb_show_system.setChecked(state);
		//当选择改变时调用该方法
		
		cb_show_system.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//获取当前状态
				if(isChecked){//当前状态就是isChecked
					cb_show_system.setText("显示系统进程");
				}else{
					cb_show_system.setText("隐藏系统进程");
				}
		//		cb_show_system.setChecked(isChecked);,这句话不用写,说明checkbox是可以被点击的
				//将状态存储到sp中
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, isChecked);				
			}
		});
		
	}
	/**
	 * 是否开启锁屏清理
	 */
	private void initLockClean() {
		
		//判断服务是否开启
		boolean running = ServiceUtil.isRunning(getApplicationContext(), "com.shange.mobilesave.service.LockCleanService");
		
		//根据存储的状态,判断是否需要显示进程
		if(running){//当前状态就是isChecked
			cb_lock_clear.setText("锁屏清理已开启");
		}else{
			cb_lock_clear.setText("锁屏清理已关闭");
		}
		cb_lock_clear.setChecked(running);
		//当选择改变时调用该方法
		
		cb_lock_clear.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//获取当前状态
				if(isChecked){//当前状态就是isChecked
					cb_lock_clear.setText("锁屏清理已开启");
					//开启锁屏清理的服务,为什么不直接创建个broadcast对象
					Intent intent = new Intent(getApplicationContext(),LockCleanService.class);
					startService(intent);
					
				}else{
					cb_lock_clear.setText("锁屏清理已关闭");
					//关闭锁屏清理的服务
					Intent intent = new Intent(getApplicationContext(),LockCleanService.class);
					stopService(intent);
				}
		//		cb_show_system.setChecked(isChecked);,这句话不用写,说明checkbox是可以被点击的
				//将状态存储到sp中
		//		SpUtil.putBoolean(getApplicationContext(), ConstantValue.LOCK_CLEAN, isChecked);				
			}
		});
		
	}
}

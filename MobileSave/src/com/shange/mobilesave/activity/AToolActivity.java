package com.shange.mobilesave.activity;

import java.io.File;

import com.shange.mobilesave.R;
import com.shange.mobilesave.engine.SmsBackUp;
import com.shange.mobilesave.engine.SmsBackUp.CallBack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AToolActivity extends Activity {

	private static final String tag = "AToolActivity";
	private TextView tv_sms_backup;
	private ProgressBar pd_bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
		//电话归属地查询方法
		initPhoneAddress();
		//短信备份方法
		initSmsBackUp();
		//常用号码查询
		initCommonNumberQuery();
		//锁定app应用
		initAppLock();
		Log.i(tag, Environment.getExternalStorageDirectory()
				+File.separator+"sms.xml");
	}


	/**
	 * 电话归属地查询
	 */
	private void initPhoneAddress() {
		TextView tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);

		tv_query_phone_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//点击跳转到下一界面
				Intent intent = new Intent(getApplicationContext(),QueryAddressActivity.class);

				startActivity(intent);
			}
		});

	}

	private void initSmsBackUp() {
		tv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);

		pd_bar = (ProgressBar) findViewById(R.id.pb_bar);

		tv_sms_backup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showSmsBackUpDialog();
			}
		});
	}

	/**
	 * 短信备份
	 */
	protected void showSmsBackUpDialog() {
		//1,创建一个带进度条的对话框
		final ProgressDialog progressDialog = new ProgressDialog(this);

		progressDialog.setIcon(R.drawable.ic_launcher1);

		progressDialog.setTitle("短信备份");
		//2,指定进度条的样式为水平
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//3,展示进度条
		progressDialog.show();
		//4,直接调用备份短信方法即可
		new Thread(){

			public void run(){
				//备份短信
				SmsBackUp.backup(getApplicationContext(), Environment.getExternalStorageDirectory()
						+File.separator+"sms.xml", new CallBack() {

					@Override
					public void setProgress(int progress) {
						//实现这两个方法就行
						//		pd_bar.setProgress(progress);
						progressDialog.setProgress(progress);
					}

					@Override
					public void setMax(int max) {

						//		pd_bar.setMax(max);
						progressDialog.setMax(max);
					}
				});
				//备份完成后,取消对话框,可以在子线程中更新UI
				progressDialog.dismiss();//02-23 09:39:07.906: I/AToolActivity(1508): /storage/emulated/0/sms.xml

			}

		}.start();
	}
	/**
	 * 常用号码查询
	 */
	private void initCommonNumberQuery() {
		
		TextView tv_common_numberquery = (TextView) findViewById(R.id.tv_common_numberquery);
		
		tv_common_numberquery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击跳转到新页面
				Intent intent = new Intent(getApplicationContext(),CommonNumberQueryActivity.class);
				
				startActivity(intent);
				
			}
		});
		
	}
	
	/**
	 * 锁定app
	 */
	private void initAppLock() {
		//找到控件
		TextView tv_app_lock = (TextView) findViewById(R.id.tv_app_lock);
		//设置点击事件
		tv_app_lock.setOnClickListener(new OnClickListener() {
			
			//点击跳转的新的activity
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(),AppLockActivity.class);
				startActivity(intent);
				
			}
		});
		
	}


}

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
		//�绰�����ز�ѯ����
		initPhoneAddress();
		//���ű��ݷ���
		initSmsBackUp();
		//���ú����ѯ
		initCommonNumberQuery();
		//����appӦ��
		initAppLock();
		Log.i(tag, Environment.getExternalStorageDirectory()
				+File.separator+"sms.xml");
	}


	/**
	 * �绰�����ز�ѯ
	 */
	private void initPhoneAddress() {
		TextView tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);

		tv_query_phone_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//�����ת����һ����
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
	 * ���ű���
	 */
	protected void showSmsBackUpDialog() {
		//1,����һ�����������ĶԻ���
		final ProgressDialog progressDialog = new ProgressDialog(this);

		progressDialog.setIcon(R.drawable.ic_launcher1);

		progressDialog.setTitle("���ű���");
		//2,ָ������������ʽΪˮƽ
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//3,չʾ������
		progressDialog.show();
		//4,ֱ�ӵ��ñ��ݶ��ŷ�������
		new Thread(){

			public void run(){
				//���ݶ���
				SmsBackUp.backup(getApplicationContext(), Environment.getExternalStorageDirectory()
						+File.separator+"sms.xml", new CallBack() {

					@Override
					public void setProgress(int progress) {
						//ʵ����������������
						//		pd_bar.setProgress(progress);
						progressDialog.setProgress(progress);
					}

					@Override
					public void setMax(int max) {

						//		pd_bar.setMax(max);
						progressDialog.setMax(max);
					}
				});
				//������ɺ�,ȡ���Ի���,���������߳��и���UI
				progressDialog.dismiss();//02-23 09:39:07.906: I/AToolActivity(1508): /storage/emulated/0/sms.xml

			}

		}.start();
	}
	/**
	 * ���ú����ѯ
	 */
	private void initCommonNumberQuery() {
		
		TextView tv_common_numberquery = (TextView) findViewById(R.id.tv_common_numberquery);
		
		tv_common_numberquery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�����ת����ҳ��
				Intent intent = new Intent(getApplicationContext(),CommonNumberQueryActivity.class);
				
				startActivity(intent);
				
			}
		});
		
	}
	
	/**
	 * ����app
	 */
	private void initAppLock() {
		//�ҵ��ؼ�
		TextView tv_app_lock = (TextView) findViewById(R.id.tv_app_lock);
		//���õ���¼�
		tv_app_lock.setOnClickListener(new OnClickListener() {
			
			//�����ת���µ�activity
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(),AppLockActivity.class);
				startActivity(intent);
				
			}
		});
		
	}


}

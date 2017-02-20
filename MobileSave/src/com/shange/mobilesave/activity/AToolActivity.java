package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AToolActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
		initUI();
	}

	private void initUI() {
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
}

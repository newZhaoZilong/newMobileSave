package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SetupOverActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
		if(setup_over){
			//密码输入成功,并且四个导航界面设置完成----->停留在设置完成功能列表界面
			setContentView(R.layout.activity_setup_over);
			//初始化UI
			initUI();
		}else{
			//密码输入成功,四个导航界面没有设置完成----->跳转到导航界面第一个
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			//开启了一个新的界面后,关闭功能列表界面
			finish();
		}
	}

	private void initUI() {
		
		TextView tv_reset_setup = (TextView) findViewById(R.id.tv_reset_setup);
		//设置点击事件
		TextView tv_setover_phone = (TextView) findViewById(R.id.tv_setover_phone);
		//首先获取电话号码
		String setup_over_phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
		//赋值
		tv_setover_phone.setText(setup_over_phone);
		
		tv_reset_setup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//如果想要重新设置,跳转到setup1界面
				Intent intent = new Intent(getApplicationContext(),Setup1Activity.class);
				startActivity(intent);
				
				finish();
				
			}
		});
	}

}

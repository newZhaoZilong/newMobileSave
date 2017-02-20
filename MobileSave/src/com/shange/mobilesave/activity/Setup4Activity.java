package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		//初始化UI
		initUI();
	}

	private void initUI() {
		final CheckBox cb_box = (CheckBox) findViewById(R.id.cb_open_security);
//		//从sp中获取状态,回显
		boolean b = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
		//设置给checkbox,回显
		cb_box.setChecked(b);
		if(b){//为true
			cb_box.setText("防盗保护已开启");
		}else{
			cb_box.setText("请开启防盗保护");
		}
		//设置点击事件
		cb_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//当check点击时调用该方法
				//存储到sp中
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
				if(isChecked){//为true
					cb_box.setText("防盗保护已开启");
				}else{
					cb_box.setText("请开启防盗保护");
				}
			}
		});
	}

	

	@Override
	public void showPrePage() {
		
		//
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		//开启了一个新的界面后,关闭功能列表界面
		finish();
		//开启平移动画
		overridePendingTransition(R.anim.pre_in, R.anim.pre_current_out);
	}

	@Override
	protected void showNextPage() {
		if(SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false)){
			//设置完成,将结果存入sp中
			SpUtil.putBoolean(this, ConstantValue.SETUP_OVER, true);
			Intent intent = new Intent(this,SetupOverActivity.class);
			startActivity(intent);
			
			
			//开启了一个新的界面后,关闭功能列表界面
			finish();
			overridePendingTransition(R.anim.next_in, R.anim.next_current_out);
		}else{
			ToastUtil.show(this, "请开启防盗保护设置");
		}
		
	}
}

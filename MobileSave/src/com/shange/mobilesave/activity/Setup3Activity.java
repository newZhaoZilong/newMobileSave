package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;

public class Setup3Activity extends BaseSetupActivity {
	private EditText et_phone_number;
	private Button bt_select_number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		//初始化UI
		initUI();
	}

	private void initUI() {
		//显示号码
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
		et_phone_number.setText(phone);//没有就是空,有就赋上
		
		//跳转到选择联系人界面
		bt_select_number = (Button) findViewById(R.id.bt_select_number);
		bt_select_number.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 点击按钮,跳转到选择联系人界面
				Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
				//当调用这个方法时,说明还要回到该界面,所以不需要关闭该界面
				startActivityForResult(intent, 1);//设置请求码
				
				
			}
		});
		
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 获取数据
		//首先判断根据结果码
		if(resultCode == 2){
			String phone = data.getStringExtra("phone").trim();
			//将特殊字符过滤
			phone = phone.replace("-", "").replace(" ", "").trim();
			et_phone_number.setText(phone);
			//将号码存入sp中
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void showPrePage() {
		String phone = et_phone_number.getText().toString().trim();
		SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		//开启了一个新的界面后,关闭功能列表界面
		finish();
		//开启平移动画
		overridePendingTransition(R.anim.pre_in, R.anim.pre_current_out);
		
	}

	@Override
	protected void showNextPage() {
		//点击按钮以后,需要获取输入框中的联系人,在做下一些操作
		String phone = et_phone_number.getText().toString().trim();
		SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
		if(!TextUtils.isEmpty(phone)){
			Intent intent = new Intent(this,Setup4Activity.class);
			startActivity(intent);
			//开启了一个新的界面后,关闭功能列表界面
			finish();
			overridePendingTransition(R.anim.next_in, R.anim.next_current_out);
		}else{
			ToastUtil.show(getApplicationContext(), "请选择联系人");
		}
		
	}
}

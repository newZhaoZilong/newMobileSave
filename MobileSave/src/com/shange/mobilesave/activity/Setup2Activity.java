package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;
import com.shange.mobilesave.utils.ToastUtil;
import com.shange.mobilesave.view.SettingItemView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Setup2Activity extends BaseSetupActivity {

	private static final String tag = "Setup2Activity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		initUI();
	}

	private void initUI() {
		final SettingItemView siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
		
		String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
		
		//将值赋给
		siv_sim_bound.setChecked(sim_number);
		
		siv_sim_bound.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//3,获取原有的状态
				boolean check = siv_sim_bound.isCheck();
				//点一下转变一下状态
				siv_sim_bound.setChecked(!check);
				if(!check){
					//绑定sim卡
					TelephonyManager telephony_service = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					//获取序列化卡号
					String simSerialNumber = telephony_service.getSimSerialNumber();
					//将卡号存入sp中
					SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
				}else{
					SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
				}
				
			}
		});
		
		
	}

	/**
	 * 跳转到下一界面
	 * @param view点击的那个控件,为啥这方法必须是public
	 */
	
	/**跳转到之前的界面
	 * @param view 点击的那个控件
	 */
	

	@Override
	public void showPrePage() {
		//
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		//开启了一个新的界面后,关闭功能列表界面
		finish();
		//开启平移动画
		overridePendingTransition(R.anim.pre_in, R.anim.pre_current_out);
		
	}

	@Override
	protected void showNextPage() {
		String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
		if(!TextUtils.isEmpty(sim_number)){//如果不为空,跳转到下一页
			Intent intent = new Intent(this,Setup3Activity.class);
			startActivity(intent);
			//开启了一个新的界面后,关闭功能列表界面
			finish();
			//开启平移动画
			overridePendingTransition(R.anim.next_in, R.anim.next_current_out);
		}else{
			ToastUtil.show(this, "请绑定sim卡");
		}
		
		
	}
}

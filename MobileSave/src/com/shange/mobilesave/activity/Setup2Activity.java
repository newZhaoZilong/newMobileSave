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
		
		//��ֵ����
		siv_sim_bound.setChecked(sim_number);
		
		siv_sim_bound.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//3,��ȡԭ�е�״̬
				boolean check = siv_sim_bound.isCheck();
				//��һ��ת��һ��״̬
				siv_sim_bound.setChecked(!check);
				if(!check){
					//��sim��
					TelephonyManager telephony_service = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					//��ȡ���л�����
					String simSerialNumber = telephony_service.getSimSerialNumber();
					//�����Ŵ���sp��
					SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
				}else{
					SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
				}
				
			}
		});
		
		
	}

	/**
	 * ��ת����һ����
	 * @param view������Ǹ��ؼ�,Ϊɶ�ⷽ��������public
	 */
	
	/**��ת��֮ǰ�Ľ���
	 * @param view ������Ǹ��ؼ�
	 */
	

	@Override
	public void showPrePage() {
		//
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		//������һ���µĽ����,�رչ����б����
		finish();
		//����ƽ�ƶ���
		overridePendingTransition(R.anim.pre_in, R.anim.pre_current_out);
		
	}

	@Override
	protected void showNextPage() {
		String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
		if(!TextUtils.isEmpty(sim_number)){//�����Ϊ��,��ת����һҳ
			Intent intent = new Intent(this,Setup3Activity.class);
			startActivity(intent);
			//������һ���µĽ����,�رչ����б����
			finish();
			//����ƽ�ƶ���
			overridePendingTransition(R.anim.next_in, R.anim.next_current_out);
		}else{
			ToastUtil.show(this, "���sim��");
		}
		
		
	}
}

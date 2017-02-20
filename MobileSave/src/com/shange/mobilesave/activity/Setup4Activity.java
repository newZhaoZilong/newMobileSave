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
		//��ʼ��UI
		initUI();
	}

	private void initUI() {
		final CheckBox cb_box = (CheckBox) findViewById(R.id.cb_open_security);
//		//��sp�л�ȡ״̬,����
		boolean b = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
		//���ø�checkbox,����
		cb_box.setChecked(b);
		if(b){//Ϊtrue
			cb_box.setText("���������ѿ���");
		}else{
			cb_box.setText("�뿪����������");
		}
		//���õ���¼�
		cb_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//��check���ʱ���ø÷���
				//�洢��sp��
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
				if(isChecked){//Ϊtrue
					cb_box.setText("���������ѿ���");
				}else{
					cb_box.setText("�뿪����������");
				}
			}
		});
	}

	

	@Override
	public void showPrePage() {
		
		//
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		//������һ���µĽ����,�رչ����б����
		finish();
		//����ƽ�ƶ���
		overridePendingTransition(R.anim.pre_in, R.anim.pre_current_out);
	}

	@Override
	protected void showNextPage() {
		if(SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false)){
			//�������,���������sp��
			SpUtil.putBoolean(this, ConstantValue.SETUP_OVER, true);
			Intent intent = new Intent(this,SetupOverActivity.class);
			startActivity(intent);
			
			
			//������һ���µĽ����,�رչ����б����
			finish();
			overridePendingTransition(R.anim.next_in, R.anim.next_current_out);
		}else{
			ToastUtil.show(this, "�뿪��������������");
		}
		
	}
}

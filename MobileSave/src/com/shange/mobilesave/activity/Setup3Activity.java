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
		//��ʼ��UI
		initUI();
	}

	private void initUI() {
		//��ʾ����
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
		et_phone_number.setText(phone);//û�о��ǿ�,�о͸���
		
		//��ת��ѡ����ϵ�˽���
		bt_select_number = (Button) findViewById(R.id.bt_select_number);
		bt_select_number.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// �����ť,��ת��ѡ����ϵ�˽���
				Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
				//�������������ʱ,˵����Ҫ�ص��ý���,���Բ���Ҫ�رոý���
				startActivityForResult(intent, 1);//����������
				
				
			}
		});
		
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ��ȡ����
		//�����жϸ��ݽ����
		if(resultCode == 2){
			String phone = data.getStringExtra("phone").trim();
			//�������ַ�����
			phone = phone.replace("-", "").replace(" ", "").trim();
			et_phone_number.setText(phone);
			//���������sp��
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void showPrePage() {
		String phone = et_phone_number.getText().toString().trim();
		SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		//������һ���µĽ����,�رչ����б����
		finish();
		//����ƽ�ƶ���
		overridePendingTransition(R.anim.pre_in, R.anim.pre_current_out);
		
	}

	@Override
	protected void showNextPage() {
		//�����ť�Ժ�,��Ҫ��ȡ������е���ϵ��,������һЩ����
		String phone = et_phone_number.getText().toString().trim();
		SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
		if(!TextUtils.isEmpty(phone)){
			Intent intent = new Intent(this,Setup4Activity.class);
			startActivity(intent);
			//������һ���µĽ����,�رչ����б����
			finish();
			overridePendingTransition(R.anim.next_in, R.anim.next_current_out);
		}else{
			ToastUtil.show(getApplicationContext(), "��ѡ����ϵ��");
		}
		
	}
}

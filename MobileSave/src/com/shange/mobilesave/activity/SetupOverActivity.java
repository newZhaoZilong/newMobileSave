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
			//��������ɹ�,�����ĸ����������������----->ͣ����������ɹ����б����
			setContentView(R.layout.activity_setup_over);
			//��ʼ��UI
			initUI();
		}else{
			//��������ɹ�,�ĸ���������û���������----->��ת�����������һ��
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			//������һ���µĽ����,�رչ����б����
			finish();
		}
	}

	private void initUI() {
		
		TextView tv_reset_setup = (TextView) findViewById(R.id.tv_reset_setup);
		//���õ���¼�
		TextView tv_setover_phone = (TextView) findViewById(R.id.tv_setover_phone);
		//���Ȼ�ȡ�绰����
		String setup_over_phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
		//��ֵ
		tv_setover_phone.setText(setup_over_phone);
		
		tv_reset_setup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�����Ҫ��������,��ת��setup1����
				Intent intent = new Intent(getApplicationContext(),Setup1Activity.class);
				startActivity(intent);
				
				finish();
				
			}
		});
	}

}

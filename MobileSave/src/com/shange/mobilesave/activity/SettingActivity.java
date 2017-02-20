package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;
import com.shange.mobilesave.service.AddressService;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.ServiceUtil;
import com.shange.mobilesave.utils.SpUtil;
import com.shange.mobilesave.view.SettingClickView;
import com.shange.mobilesave.view.SettingItemView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	protected static final String tag = "SettingActivity";
	private String[] mToastStyles;
	private int toast_style;
	private SettingClickView scv_toast_style;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//��activity��һ������ʱ����
		setContentView(R.layout.activity_setting);
		//
		initUpdate();
		//����address
		initAddress();
		//��˾��ʽ
		initToastStyle();
		//��˾����
		initLocation();
	}

	private void initLocation() {
		
		SettingClickView scv_location = (SettingClickView) findViewById(R.id.scv_location);
		//���ñ���
		scv_location.setTitle("��������ʾ���λ��");
		
		scv_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(getApplicationContext(),ToastLocationActivity.class));
			}
		});
		
	}

	private void initToastStyle() {
		scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
		//���ñ���
		scv_toast_style.setTitle("���ù�������ʾ���");
		//������ɫ����
		mToastStyles = new String[]{"͸��","��ɫ","��ɫ","��ɫ","��ɫ"};
		
		toast_style = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
		//3,ͨ������,��ȡ�ַ��������е�����,��ʾ��������������
		scv_toast_style.setDes(mToastStyles[toast_style]);
		//����ؼ�,������ѡ�Ի���
		scv_toast_style.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//������ѡ�Ի���
				showToastStyleDialog();
				Log.i(tag,"�����Ի���");
			}
		});
		
	}

	protected void showToastStyleDialog() {
		//������ѡ�Ի���,
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("��ѡ���������ʽ");
		//Ҫʱʱ����
		toast_style = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
		builder.setSingleChoiceItems(mToastStyles, toast_style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//���������Ŀʱ,��ȡ�Ķ���Ŀ��id
				
				//�洢����
				SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_STYLE, which);
				//�رնԻ���//�Զ���Ի�����Ҫ��builder,��AlertDialog����
				dialog.dismiss();
				//�ǵø�ֵ
				scv_toast_style.setDes(mToastStyles[which]);
				
			}
		});
		builder.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		} );
		//��tm����show��
		builder.show();
	}

	private void initAddress() {
		
		//�ҵ��ؼ�,�ؼ�����������
				final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
				//��ȡ�洢�Ľڵ�ֵ,�жϷ����Ƿ���
				boolean isrunning = ServiceUtil.isRunning(this, "com.shange.mobilesave.service.AddressService");
				//�Ƿ�ѡ��,������һ�δ洢�Ľ��ȥ������
				siv_address.setChecked(isrunning);//����ĸ��������������
				//���øÿؼ��ĵ���¼�
				siv_address.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// �����ʱ�����������
						//���Ȼ�ȡ��ǰ״̬
						boolean isCheck = siv_address.isCheck();//�ϱ߸�����,���ھ�ȥ
						//���һ��,ת��һ��״̬
						siv_address.setChecked(!isCheck);
						//�洢����״̬	
						if(!isCheck){//Ϊture��������
							//��������
							Intent intent = new Intent(getApplicationContext(),AddressService.class);
							startService(intent);
						}else{
							//�رշ���
							Intent intent = new Intent(getApplicationContext(),AddressService.class);
							stopService(intent);//�رշ���û��
						}
					}
				});
	}

	/**
	 * ��������
	 */
	private void initUpdate() {
		//�ҵ��ؼ�,�ؼ�����������
		final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
		//��ȡ�洢�Ľڵ�ֵ
		boolean open_update = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false);//��ȡֵ,���������,Ĭ��false
		//�Ƿ�ѡ��,������һ�δ洢�Ľ��ȥ������
		siv_update.setChecked(open_update);//����ĸ��������������
		//���øÿؼ��ĵ���¼�
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// �����ʱ�����������
				//���Ȼ�ȡ��ǰ״̬
				boolean isCheck = siv_update.isCheck();//�ϱ߸�����,���ھ�ȥ
				//���һ��,ת��һ��״̬
				siv_update.setChecked(!isCheck);
				//�洢����״̬
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isCheck);
				//û���һ��,�洢һ��,����Ĵ����Ծ��Ǻ�
			}
		});
	}
}
package com.shange.mobilesave.activity;


import com.lidroid.xutils.cache.MD5FileNameGenerator;
import com.shange.mobilesave.R;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.Md5Util;
import com.shange.mobilesave.utils.SpUtil;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	protected static final String tag = "HomeActivity";
	private String[] mTitleStrs;
	private int[] mDrawableIds;
	private GridView gv_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//���ò����ļ�
		setContentView(R.layout.activity_home);

		initUI();
		//��ʼ�����ݵķ���
		initData();

	}
	/**
	 *��ʼ���ؼ� 
	 */
	private void initUI() {
		gv_home = (GridView) findViewById(R.id.gv_home);

	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		mTitleStrs = new String[]{
				"�ֻ�����","ͨ����ʿ","�������","���̹���","����ͳ��","�ֻ�ɱ��","��������","�߼�����","��������"
		};
		mDrawableIds = new int[]{
				R.drawable.home_safe,R.drawable.home_callmsgsafe,
				R.drawable.home_apps,R.drawable.home_taskmanager,
				R.drawable.home_netmanager,R.drawable.home_trojan,
				R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
		};
		//�Ź���ؼ���������������(��ͬListView����������)
		gv_home.setAdapter(new MyAdapter());
		//����������û��Ӧ,������Ҫ������
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			//parent����GridView,�̳�adapterview, view:�������Ŀ�ϵ��Ǹ�view����  position:��Ŀ��λ�� id:��Ŀ��id
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// �����ʱ���ø÷���
				//ͨ��position���ֵ��������һ������,դ��

				Log.i(tag,"position:"+position);
				Log.i(tag,"id:"+id);
				switch (position) {
				case 0:
					showDialog();
					break;
				
				case 1:
					//��ת��ͨ����ʿģ��
					startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
					break;
				case 2:
					Intent intent2 = new Intent(getApplicationContext(), SoftManagerActivity.class);
					//��������
					startActivity(intent2);
					break;
				case 3:
					Intent intent3 = new Intent(getApplicationContext(), ProcessManagerActivity.class);
					//��������
					startActivity(intent3);
					break;
					
				case 7:
					//�������������ʱ��һ������
					Intent intent7 = new Intent(getApplicationContext(), AToolActivity.class);
					//��������
					startActivity(intent7);
					break;

				case 8:
					//�������������ʱ��һ������
					Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
					//��������
					startActivity(intent);
					break;

				default:
					break;
				}

			}
		});

	}

	private void showDialog() {
		//�жϱ����Ƿ��д洢����
		String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
		//1,��ʼ��������Ի���
		if(TextUtils.isEmpty(psd)){
			//���Ϊ��,�򵯳���������Ի���
			showSetPsdDialog();
		}else{
			//2,ȷ������Ի���
			showConfirPsdDialog();
		}
		
		
	}

	/**
	 * ȷ������Ի���
	 */
	private void showConfirPsdDialog() {
		//����������
				Builder builder = new AlertDialog.Builder(this);//֮ǰ��builder�Ϳ�������ȷ��,ȡ����ť 
				//�����Ի���
				final AlertDialog dialog = builder.create();
				//����view����
				final View view = View.inflate(this,R.layout.dialog_confirm_psd , null);
				//
				dialog.setView(view, 0, 0, 0, 0);
				
				dialog.show();
				//���õ���¼�
				Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
				Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
				//���õ���¼�
				bt_submit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//���ȷ��ʱ���ø÷���
						//����Ҫ�ж������Ƿ�Ϊ��
						EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
						
						String set_psd = et_set_psd.getText().toString().trim();
						//��sheredpreference�л�ȡ����
						
						
						if(!TextUtils.isEmpty(set_psd)){
							//���洢��sp��32λ������,��ȡ����,Ȼ�����������ͬ������md5,Ȼ����sp�д洢����ȶ�
							String psd = SpUtil.getString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD , "");
							//�����Ϊ��,���ж��Ƿ���ͬ
							String md5_set_psd = Md5Util.encoder(set_psd);
							if(md5_set_psd.equals(psd)){
								//���������ͬ,������½���
								//������ͼ
							Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
							//��������
							startActivity(intent);
							//���������sheredPreference
							
							
							//dismiss
							dialog.dismiss();
							}else{
								ToastUtil.show(getApplicationContext(), "ȷ���������");
							}
						}else{
							//������ʾ
							ToastUtil.show(getApplicationContext(), "���벻��Ϊ��");
						}
					}
				});
				//����ȡ���ĵ���¼�
				bt_cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// ȡ���Ļ�,ֱ�ӽ����Ի���
						dialog.dismiss();
						
					}
				});
	
	}
	/**
	 * ��������Ի���
	 */
	private void showSetPsdDialog() {
		//����������
		Builder builder = new AlertDialog.Builder(this);//֮ǰ��builder�Ϳ�������ȷ��,ȡ����ť 
		//�����Ի���
		final AlertDialog dialog = builder.create();
		//����view����
		final View view = View.inflate(this,R.layout.dialog_set_psd , null);
		//
		dialog.setView(view, 0, 0, 0, 0);
		
		dialog.show();
		//���õ���¼�
		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		//���õ���¼�
		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//���ȷ��ʱ���ø÷���
				//����Ҫ�ж������Ƿ�Ϊ��
				EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
				EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
				String set_psd = et_set_psd.getText().toString().trim();
				String confirm_psd = et_confirm_psd.getText().toString().trim();
				
				if(!TextUtils.isEmpty(set_psd)&&!TextUtils.isEmpty(confirm_psd)){
					//�������Ϊ��,���ж��Ƿ���ͬ
					if(set_psd.equals(confirm_psd)){
						//���������ͬ,������½���
						//������ͼ
					Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
					//��������
					startActivity(intent);
					//���������sheredPreference
					SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD,Md5Util.encoder(set_psd));
					
					//dismiss
					dialog.dismiss();
					}else{
						ToastUtil.show(getApplicationContext(), "ȷ���������");
					}
				}else{
					//������ʾ
					ToastUtil.show(getApplicationContext(), "���벻��Ϊ��");
				}
			}
		});
		//����ȡ���ĵ���¼�
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ȡ���Ļ�,ֱ�ӽ����Ի���
				dialog.dismiss();
				
			}
		});
		
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// ��Ŀ������  �������� == ͼƬ����
			return mTitleStrs.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mTitleStrs[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//������ת��Ϊview����,root,�����ص�view
			View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
			//ͨ��view�����ҵ��ؼ�
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			//���ؼ���ֵ
			iv_icon.setBackgroundResource(mDrawableIds[position]);
			tv_title.setText(mTitleStrs[position]);
			return view;
		}

	}

}

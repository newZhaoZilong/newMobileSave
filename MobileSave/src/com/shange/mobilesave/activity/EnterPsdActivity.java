package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EnterPsdActivity extends Activity {

	private static final String tag = "EnterPsdActivity";
	private TextView tv_app_name;
	private ImageView iv_app_icon;
	private EditText et_psd;
	private Button bt_submit;
	private String packageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_psd);
		packageName = getIntent().getStringExtra("packageName");
		
		initUI();
		initData();
	}

	private void initUI() {
		tv_app_name = (TextView) findViewById(R.id.tv_app_name);
		iv_app_icon = (ImageView) findViewById(R.id.iv_app_icon);
		et_psd = (EditText) findViewById(R.id.et_psd);
		bt_submit = (Button) findViewById(R.id.bt_submit);
		
		//���ð�ť����¼�
		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String psd = et_psd.getText().toString();
				if(!TextUtils.isEmpty(psd)){
					if(psd.equals("123")){
						//����,����Ӧ��,��֪���Ź���Ҫ��ȥ�����Լ�������Ӧ��
						//,���͹㲥
						Intent intent = new Intent("android.intent.action.SKIP");
						intent.putExtra("packageName", packageName);
						sendBroadcast(intent);
						
						finish();
					}else{
						ToastUtil.show(getApplicationContext(),"�������");
					}
				}else{
					ToastUtil.show(getApplicationContext(), "����������");
				}
				
				
			}
		});

	}

	private void initData() {
		//ͨ�����ݹ����İ�����ȡ����Ӧ�õ�ͼ���Լ�����
		PackageManager pm = getPackageManager();
		
		try {
			//Ĭ��0,������Ҫ��Ż��õ�
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
			
			Drawable icon = applicationInfo.loadIcon(pm);
			
			iv_app_icon.setBackgroundDrawable(icon);
			//Ӧ������ֵ
			tv_app_name.setText(applicationInfo.loadLabel(pm));
			
			Log.i(tag, "name:"+applicationInfo.name);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	//�����˼���ʱ����ø÷���
	@Override
	public void onBackPressed() {
		// ��ת������
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		
		startActivity(intent);
		super.onBackPressed();
	}
	
}

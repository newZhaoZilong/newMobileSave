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
		
		//设置按钮点击事件
		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String psd = et_psd.getText().toString();
				if(!TextUtils.isEmpty(psd)){
					if(psd.equals("123")){
						//解锁,进入应用,告知看门狗不要再去监听以及解锁的应用
						//,发送广播
						Intent intent = new Intent("android.intent.action.SKIP");
						intent.putExtra("packageName", packageName);
						sendBroadcast(intent);
						
						finish();
					}else{
						ToastUtil.show(getApplicationContext(),"密码错误");
					}
				}else{
					ToastUtil.show(getApplicationContext(), "请输入密码");
				}
				
				
			}
		});

	}

	private void initData() {
		//通过传递过来的包名获取拦截应用的图标以及名称
		PackageManager pm = getPackageManager();
		
		try {
			//默认0,有特殊要求才会用到
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
			
			Drawable icon = applicationInfo.loadIcon(pm);
			
			iv_app_icon.setBackgroundDrawable(icon);
			//应用名赋值
			tv_app_name.setText(applicationInfo.loadLabel(pm));
			
			Log.i(tag, "name:"+applicationInfo.name);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	//按后退键的时候调用该方法
	@Override
	public void onBackPressed() {
		// 跳转到桌面
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		
		startActivity(intent);
		super.onBackPressed();
	}
	
}

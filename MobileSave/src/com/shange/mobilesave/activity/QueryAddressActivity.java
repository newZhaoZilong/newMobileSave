package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;
import com.shange.mobilesave.engine.AddressDao;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryAddressActivity extends Activity {

	protected static final String tag = "QueryAddressActivity";
	private EditText et_input_phone;
	private Button bt_query_phone_address;
	private TextView tv_phone_address;
	private String mAddress;
	private String input_phone;

	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//收到空消息,给控件赋值
			tv_phone_address.setText(mAddress);
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_address);

		initUI();


	}
	private void initUI() {
		//输入电话号码
		et_input_phone = (EditText) findViewById(R.id.et_input_phone);
		et_input_phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				//每次文本变化后,都要查询
				input_phone = et_input_phone.getText().toString();
				query(input_phone);
			}
		});
		bt_query_phone_address = (Button) findViewById(R.id.bt_query_phone_address);
		bt_query_phone_address.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				//点击按钮
				//获取电话号码
				
				input_phone = et_input_phone.getText().toString();
				if(!TextUtils.isEmpty(input_phone)){
					Log.i(tag,"input_phone:"+input_phone);
					//查询地址
					//2查询是耗时操作,开启子线程
					query(input_phone);
				}else{
					//抖动,插补器
					Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
					//自定义插补器
					shake.setInterpolator(new Interpolator() {
						
						@Override
						public float getInterpolation(float input) {
							// TODO Auto-generated method stub
							return (float)(Math.sin(2 * 7 * Math.PI * input));//不就位移和加速度吗/
						}
					});
					//CycleInterpolator
					et_input_phone.startAnimation(shake);
				}
				
				
				
			}
		});
	
		tv_phone_address = (TextView) findViewById(R.id.tv_phone_address);
	}
	/**耗时操作
	 * 获取电话号码归属地
	 * @param input_phone 查询电话号码
	 */
	protected void query(final String input_phone) {
		new Thread(){
			

			public void run(){
				
				mAddress = AddressDao.getAddress(input_phone);
				//3,消息机制,告知主线程查询结束,可以去使用查询结果
				//发送一条空消息
				mhandler.sendEmptyMessage(0);
			};
			
		}.start();
		
	}
	
	
}

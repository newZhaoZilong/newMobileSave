package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;
import com.shange.mobilesave.service.AddressService;
import com.shange.mobilesave.service.BlackNumberService;
import com.shange.mobilesave.service.WatchDogService;
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
		//当activity第一次启动时调用
		setContentView(R.layout.activity_setting);
		//
		initUpdate();
		//更新address
		initAddress();
		//吐司样式
		initToastStyle();
		//吐司界面
		initLocation();
		//开启黑名单服务
		initBlackNumber();
		//初始化程序锁的方法
		initAppLock();
	}



	/**
	 * 开启黑名单服务
	 */
	private void initBlackNumber() {
		//找到控件,控件名就是类名
		final SettingItemView siv_blackNumber = (SettingItemView) findViewById(R.id.siv_blackNumber);
		//获取存储的节点值
		boolean isrunning = ServiceUtil.isRunning(this, "com.shange.mobilesave.service.BlackNumberService");
		//是否选中,根据上一次存储的结果去做决定
		siv_blackNumber.setChecked(isrunning);//代码的复用性真是妙不可言
		//设置该控件的点击事件
		siv_blackNumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 当点击时调用这个方法
				//首先获取当前状态
				boolean isCheck = siv_blackNumber.isCheck();//上边刚设置,现在就去
				//点击一次,转变一次状态
				siv_blackNumber.setChecked(!isCheck);
				if(!isCheck){//为ture开启服务
					//开启服务
					Intent intent = new Intent(getApplicationContext(),BlackNumberService.class);
					startService(intent);
				}else{
					//关闭服务
					Intent intent = new Intent(getApplicationContext(),BlackNumberService.class);
					stopService(intent);//
				}
			}
		});
		
		
	}

	private void initLocation() {
		
		SettingClickView scv_location = (SettingClickView) findViewById(R.id.scv_location);
		//设置标题
		scv_location.setTitle("归属地提示框的位置");
		scv_location.setDes("设置归属地提示框的位置");
		
		scv_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(getApplicationContext(),ToastLocationActivity.class));
			}
		});
		
	}

	private void initToastStyle() {
		scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
		//设置标题
		scv_toast_style.setTitle("设置归属地显示风格");
		//设置颜色数组
		mToastStyles = new String[]{"透明","橙色","蓝色","灰色","绿色"};
		
		toast_style = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
		//3,通过索引,获取字符串数组中的文字,显示给描述内容软件
		scv_toast_style.setDes(mToastStyles[toast_style]);
		//点击控件,弹出单选对话框
		scv_toast_style.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//弹出单选对话框
				showToastStyleDialog();
				Log.i(tag,"弹出对话框");
			}
		});
		
	}

	protected void showToastStyleDialog() {
		//创建单选对话框,
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher1);
		builder.setTitle("请选择归属地样式");
		//要时时更新
		toast_style = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
		builder.setSingleChoiceItems(mToastStyles, toast_style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//点击哪条条目时,获取哪儿条目的id
				
				//存储起来
				SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_STYLE, which);
				//关闭对话框//自定义对话框不需要用builder,用AlertDialog就行
				dialog.dismiss();
				//记得赋值
				scv_toast_style.setDes(mToastStyles[which]);
				
			}
		});
		builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		} );
		//又tm忘记show了
		builder.show();
	}

	private void initAddress() {
		
		//找到控件,控件名就是类名
				final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
				//获取存储的节点值,判断服务是否开启
				boolean isrunning = ServiceUtil.isRunning(this, "com.shange.mobilesave.service.AddressService");
				//是否选中,根据上一次存储的结果去做决定
				siv_address.setChecked(isrunning);//代码的复用性真是妙不可言
				//设置该控件的点击事件
				siv_address.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// 当点击时调用这个方法
						//首先获取当前状态
						boolean isCheck = siv_address.isCheck();//上边刚设置,现在就去
						//点击一次,转变一次状态
						siv_address.setChecked(!isCheck);
						//存储设置状态	
						if(!isCheck){//为ture开启服务
							//开启服务
							Intent intent = new Intent(getApplicationContext(),AddressService.class);
							startService(intent);
						}else{
							//关闭服务
							Intent intent = new Intent(getApplicationContext(),AddressService.class);
							stopService(intent);//关闭服务没用,还要remove(view);
						}
					}
				});
	}

	/**
	 * 更新设置
	 */
	private void initUpdate() {
		//找到控件,控件名就是类名
		final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
		//获取存储的节点值
		boolean open_update = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false);//获取值,如果不存在,默认false
		//是否选中,根据上一次存储的结果去做决定
		siv_update.setChecked(open_update);//代码的复用性真是妙不可言
		//设置该控件的点击事件
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 当点击时调用这个方法
				//首先获取当前状态
				boolean isCheck = siv_update.isCheck();//上边刚设置,现在就去
				//点击一次,转变一次状态
				siv_update.setChecked(!isCheck);
				//存储设置状态
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isCheck);
				//没点击一次,存储一次,代码的创造性就是好
			}
		});
	}
	/**
	 * 初始化程序锁方法
	 */
	private void initAppLock() {
		//找到控件,控件名就是类名
		final SettingItemView siv_app_lock = (SettingItemView) findViewById(R.id.siv_app_lock);
		//获取存储的节点值
		boolean isrunning = ServiceUtil.isRunning(this, "com.shange.mobilesave.service.WatchDogService");
		//是否选中,根据上一次存储的结果去做决定
		siv_app_lock.setChecked(isrunning);//代码的复用性真是妙不可言
		//设置该控件的点击事件
		siv_app_lock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 当点击时调用这个方法
				//首先获取当前状态
				boolean isCheck = siv_app_lock.isCheck();//上边刚设置,现在就去
				//点击一次,转变一次状态
				siv_app_lock.setChecked(!isCheck);
				if(!isCheck){//为ture开启服务
					//开启服务
					Intent intent = new Intent(getApplicationContext(),WatchDogService.class);
					
					startService(intent);
				}else{
					//关闭服务
					Intent intent = new Intent(getApplicationContext(),WatchDogService.class);
					stopService(intent);//
				}
			}
		});
	
	}
}

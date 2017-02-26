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
		//设置布局文件
		setContentView(R.layout.activity_home);

		initUI();
		//初始化数据的方法
		initData();

	}
	/**
	 *初始化控件 
	 */
	private void initUI() {
		gv_home = (GridView) findViewById(R.id.gv_home);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTitleStrs = new String[]{
				"手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"
		};
		mDrawableIds = new int[]{
				R.drawable.home_safe,R.drawable.home_callmsgsafe,
				R.drawable.home_apps,R.drawable.home_taskmanager,
				R.drawable.home_netmanager,R.drawable.home_trojan,
				R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
		};
		//九宫格控件设置数据适配器(等同ListView数据适配器)
		gv_home.setAdapter(new MyAdapter());
		//点设置中心没反应,所以需要监听器
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			//parent代表GridView,继承adapterview, view:点击的条目上的那个view对象  position:条目的位置 id:条目的id
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 当点击时调用该方法
				//通过position区分点击的是哪一个网格,栅格

				Log.i(tag,"position:"+position);
				Log.i(tag,"id:"+id);
				switch (position) {
				case 0:
					showDialog();
					break;
				
				case 1:
					//跳转到通信卫士模块
					startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
					break;
				case 2:
					Intent intent2 = new Intent(getApplicationContext(), SoftManagerActivity.class);
					//开启界面
					startActivity(intent2);
					break;
				case 3:
					Intent intent3 = new Intent(getApplicationContext(), ProcessManagerActivity.class);
					//开启界面
					startActivity(intent3);
					break;
					
				case 7:
					//当点击设置中心时打开一个界面
					Intent intent7 = new Intent(getApplicationContext(), AToolActivity.class);
					//开启界面
					startActivity(intent7);
					break;

				case 8:
					//当点击设置中心时打开一个界面
					Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
					//开启界面
					startActivity(intent);
					break;

				default:
					break;
				}

			}
		});

	}

	private void showDialog() {
		//判断本地是否有存储密码
		String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
		//1,初始设置密码对话框
		if(TextUtils.isEmpty(psd)){
			//如果为空,则弹出设置密码对话框
			showSetPsdDialog();
		}else{
			//2,确认密码对话框
			showConfirPsdDialog();
		}
		
		
	}

	/**
	 * 确认密码对话框
	 */
	private void showConfirPsdDialog() {
		//创建建造器
				Builder builder = new AlertDialog.Builder(this);//之前用builder就可以设置确定,取消按钮 
				//创建对话框
				final AlertDialog dialog = builder.create();
				//创建view对象
				final View view = View.inflate(this,R.layout.dialog_confirm_psd , null);
				//
				dialog.setView(view, 0, 0, 0, 0);
				
				dialog.show();
				//设置点击事件
				Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
				Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
				//设置点击事件
				bt_submit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//点击确定时调用该方法
						//首先要判断密码是否为空
						EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
						
						String set_psd = et_set_psd.getText().toString().trim();
						//从sheredpreference中获取密码
						
						
						if(!TextUtils.isEmpty(set_psd)){
							//将存储在sp中32位的密码,获取出来,然后将输入的密码同样进行md5,然后与sp中存储密码比对
							String psd = SpUtil.getString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD , "");
							//如果不为空,就判断是否相同
							String md5_set_psd = Md5Util.encoder(set_psd);
							if(md5_set_psd.equals(psd)){
								//如果密码相同,则进入新界面
								//创建意图
							Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
							//开启界面
							startActivity(intent);
							//将密码存入sheredPreference
							
							
							//dismiss
							dialog.dismiss();
							}else{
								ToastUtil.show(getApplicationContext(), "确认密码错误");
							}
						}else{
							//否则提示
							ToastUtil.show(getApplicationContext(), "密码不能为空");
						}
					}
				});
				//设置取消的点击事件
				bt_cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// 取消的话,直接结束对话框
						dialog.dismiss();
						
					}
				});
	
	}
	/**
	 * 设置密码对话框
	 */
	private void showSetPsdDialog() {
		//创建建造器
		Builder builder = new AlertDialog.Builder(this);//之前用builder就可以设置确定,取消按钮 
		//创建对话框
		final AlertDialog dialog = builder.create();
		//创建view对象
		final View view = View.inflate(this,R.layout.dialog_set_psd , null);
		//
		dialog.setView(view, 0, 0, 0, 0);
		
		dialog.show();
		//设置点击事件
		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		//设置点击事件
		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击确定时调用该方法
				//首先要判断密码是否为空
				EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
				EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
				String set_psd = et_set_psd.getText().toString().trim();
				String confirm_psd = et_confirm_psd.getText().toString().trim();
				
				if(!TextUtils.isEmpty(set_psd)&&!TextUtils.isEmpty(confirm_psd)){
					//如果都不为空,就判断是否相同
					if(set_psd.equals(confirm_psd)){
						//如果密码相同,则进入新界面
						//创建意图
					Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
					//开启界面
					startActivity(intent);
					//将密码存入sheredPreference
					SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD,Md5Util.encoder(set_psd));
					
					//dismiss
					dialog.dismiss();
					}else{
						ToastUtil.show(getApplicationContext(), "确认密码错误");
					}
				}else{
					//否则提示
					ToastUtil.show(getApplicationContext(), "密码不能为空");
				}
			}
		});
		//设置取消的点击事件
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 取消的话,直接结束对话框
				dialog.dismiss();
				
			}
		});
		
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// 条目的总数  文字组数 == 图片张数
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
			//将布局转化为view对象,root,所挂载的view
			View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
			//通过view对象找到控件
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			//给控件赋值
			iv_icon.setBackgroundResource(mDrawableIds[position]);
			tv_title.setText(mTitleStrs[position]);
			return view;
		}

	}

}

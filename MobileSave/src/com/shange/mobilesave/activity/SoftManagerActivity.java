package com.shange.mobilesave.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.shange.mobilesave.R;
import com.shange.mobilesave.db.domain.AppInfo;
import com.shange.mobilesave.engine.AppEngine;
import com.shange.mobilesave.utils.AppUtil;
import com.shange.mobilesave.utils.MyAsyncTask;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 山哥
 *用于管理软件的类
 */
public class SoftManagerActivity extends Activity implements OnClickListener {

	private static final String tag = "SoftManagerActivity";
	private ListView lv_softmanager_application;
	private ProgressBar loading;
	private MyAdapter myAdapter;
	private List<AppInfo> appInfos;
	//用户程序集合
	private List<AppInfo> userappinfo;
	//系统程序集合
	private List<AppInfo> systemappinfo;
	private TextView tv_softmanager_userorsystem;
	private PopupWindow popupWindow;
	private AppInfo appInfo1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmanager);
		//初始化UI
		initUI();
		//初始化数据
		initData();
	}

	/**
	 * 初始化UI
	 */


	private void initUI() {
		
		TextView tv_softmanager_rom = (TextView) findViewById(R.id.tv_softmanager_rom);
		
		TextView tv_softmanager_sd = (TextView) findViewById(R.id.tv_softmanager_sd);
		
		//获取内存可用空间和sd卡可用空间
		
		long availableROM = AppUtil.getAvailableROM();
		
		long availableSD = AppUtil.getAvailableSD();
		
		//数据转化
		
		String romSize = Formatter.formatFileSize(getApplicationContext(), availableROM);
		
		String sdSize = Formatter.formatFileSize(getApplicationContext(), availableSD);
		
		//赋值
		
		tv_softmanager_rom.setText("SD卡可用:"+romSize);
		
		tv_softmanager_sd.setText("内存可用:"+sdSize);
		
		lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);

		loading = (ProgressBar) findViewById(R.id.loading);

		tv_softmanager_userorsystem = (TextView) findViewById(R.id.tv_softmanager_userorsystem);

		lv_softmanager_application.setOnScrollListener(new OnScrollListener() {

			//滚动状态改变

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {


			}
			//滚动中
			//view:listview
			//firstVisibleItem: 界面第一个显示条目
			//visibleItemCount: 显示条目总个数
			//totallItemCount: 条目的总个数
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//判断第一个条目是否大于,出现空指针异常,说明userappinfo是空的
				hidePopupwindow();
				//listview在初始化的时候就会调用onScroll方法
				if(userappinfo!=null&&systemappinfo!=null){
					/*if(firstVisibleItem == 0 || firstVisibleItem == userappinfo.size()+1){
						tv_softmanager_userorsystem.setVisibility(View.INVISIBLE);
					}else{*/
						tv_softmanager_userorsystem.setVisibility(View.VISIBLE);
						if(firstVisibleItem>=(userappinfo.size()+1)){

							tv_softmanager_userorsystem.setText("系统程序("+systemappinfo.size()+")");
						}else{
							tv_softmanager_userorsystem.setText("用户程序("+userappinfo.size()+")");
						}
					//}
					
				}
				
			}
		});
		lv_softmanager_application.setOnItemClickListener(new OnItemClickListener() {



			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//每次点击前要清除之前的popupwindow

				//弹出气泡
				//1.屏蔽用户程序和系统程序(...个)弹出气泡
				if(position == 0 || position == userappinfo.size()+1){
					return;
				}
				//2.获取条目所对应的应用程序的信息
				
				//数据要从userappinfo和systemappinfo中获取
				if(position<userappinfo.size()+1){

					appInfo1 = userappinfo.get(position-1);
				}else{
					appInfo1 = systemappinfo.get(position-userappinfo.size()-2);
				}
				//隐藏气泡
				hidePopupwindow();
				//3,弹出气泡
				/*TextView textView = new TextView(getApplicationContext());
				textView.setText("我是popuwindow的textview的控件");
				textView.setBackgroundColor(Color.RED);*/
				View popup_view = View.inflate(getApplicationContext(), R.layout.popup_window, null);
				
				
				//初始化控件
				LinearLayout ll_pupupwindow_uninstall = (LinearLayout) popup_view.findViewById(R.id.ll_pupupwindow_uninstall);
				LinearLayout ll_pupupwindow_start = (LinearLayout) popup_view.findViewById(R.id.ll_pupupwindow_start);
				LinearLayout ll_pupupwindow_share = (LinearLayout) popup_view.findViewById(R.id.ll_pupupwindow_share);
				LinearLayout ll_pupupwindow_detail = (LinearLayout) popup_view.findViewById(R.id.ll_pupupwindow_detail);
				
				//设置监听事件
				ll_pupupwindow_uninstall.setOnClickListener(SoftManagerActivity.this);
				ll_pupupwindow_start.setOnClickListener(SoftManagerActivity.this);
				ll_pupupwindow_share.setOnClickListener(SoftManagerActivity.this);
				ll_pupupwindow_detail.setOnClickListener(SoftManagerActivity.this);
				
				popupWindow = new PopupWindow(popup_view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);


				popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				//PopupWindow popupWindow = new PopupWindow(contentView, width, height);
				//4.获取条目的位置,让气泡显示在相应的条目,主要是y轴
				int[] location = new int[2];
				
				view.getLocationInWindow(location);//获取条目x,y的坐标,同时保存到int[],c程序员的写法
				//因为是获取条目的view,位置,条目的view对象就是view
				//初始化控件
				
			
				
				//parent:要挂载在那个控件上
				//gravity,x,y:控制popupwindow显示的位置
				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, location[0]+300, location[1]);
				//6.设置动画
				//缩放动画
				//前四个:控制控件由没有变到有, 动画0:没有    1:整个控件
				//后四个:控制控件是按照自身还是父控件进行变化,
				//RELATIVE_TO_SELF :以自身变化
				//RELATIVE_TO_SELF	:以父控件变化
				//0.5要加f,不然会认为是double类型
				ScaleAnimation scaleAnimation = new ScaleAnimation
						(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
				scaleAnimation.setDuration(300);
				//创建个透明动画
				AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
				alphaAnimation.setDuration(300);
				//组合动画
				//shareInterpolator:是否使用相同的动画插补器 true:共享 false:各自使用各自的
				AnimationSet animationSet = new AnimationSet(true);
				//添加动画
				animationSet.addAnimation(scaleAnimation);
				animationSet.addAnimation(alphaAnimation);

				//执行动画
				popup_view.startAnimation(animationSet);

				
			}

			


		});
	}		
	
	//设置popupwindow的点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_pupupwindow_uninstall:
			System.out.println("ll_pupupwindow_uninstall");
			uninstall();
			break;
		case R.id.ll_pupupwindow_start:
			System.out.println("ll_pupupwindow_start");
			start();
			break;
		case R.id.ll_pupupwindow_share:
			System.out.println("ll_pupupwindow_share");
			share();
			break;
		case R.id.ll_pupupwindow_detail:
			System.out.println("ll_pupupwindow_detail");
			detail();
			break;
			//
			
			//为啥一般都不要这玩意儿
		}	
		//隐藏气泡
		hidePopupwindow();
	}
	/**
	 * 卸载点击的软件
	 */
	private void uninstall() {
		if(appInfo1.isUser()){
			if(!appInfo1.getPackageName().equals(getPackageName())){
				Intent intent = new Intent();
				intent.setAction("android.intent.action.DELETE");
				intent.addCategory("android.intent.category.DEFAULT");
				//为什么没有空指针异常呢?
				
				intent.setData(Uri.parse("package:"+appInfo1.getPackageName()));
				startActivityForResult(intent, 0);//默认都是0,写了跟每写一样

			}else{
				ToastUtil.show(this, "文明社会,杜绝自杀");
			}
		}else{
			ToastUtil.show(this, "要想卸载程序,请先root");
		}
		
			}
	/**
	 * 启动该应用
	 */
	private void start(){
		PackageManager packageManager = getPackageManager();
		//获取应用程序的启动意图
		Intent intent = packageManager.getLaunchIntentForPackage(appInfo1.getPackageName());
		if(intent!=null){
			startActivity(intent);
		}else{
			ToastUtil.show(this, "系统核心程序,无法启动");
		}
		
	}
	/**
	 * 分享软件
	 */
	private void share(){
		
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "发现一个很牛x软件"+appInfo1.getName()+"下载地址:www.baidu.com,自己去搜");
		startActivity(intent);
	}
	/**
	 * 获取软件详情
	 */
	private void detail(){
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:"+appInfo1.getPackageName()));
		startActivity(intent);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//重新初始化数据
		initData();
		//initData();
	}
	/**
	 * 初始化数据
	 * 异步加载框架
	 */
	private void initData() {

		new MyAsyncTask() {

			@Override
			public void preTask() {
				//设置能见度为可见;
				loading.setVisibility(View.VISIBLE);//子线程不能更新UI
			}
			//在子线程中执行的操作
			@Override
			public void doinBack() {
				
				//获取集合
				appInfos = AppEngine.getAppInfos(getApplicationContext());

				userappinfo = new ArrayList<AppInfo>();

				systemappinfo = new ArrayList<AppInfo>();
				//将总集合分为用户应用和系统应用
				//遍历集合
				for (AppInfo appInfo : appInfos) {
					
					//判断
					if(appInfo.isUser()){
						//是用户应用就添加到用户集合中
						userappinfo.add(appInfo);

					}else{
						//不是就添加到系统应用中
						systemappinfo.add(appInfo);
					}
				}
			}
			//在线程后执行的操作,(子线程之外,在handlermessage()里执行的操作
			@Override
			public void postTask() {
				//设置能见度为不可见的
				loading.setVisibility(View.INVISIBLE);

				if(myAdapter==null){
					//为空,创建对象
					myAdapter = new MyAdapter();
					lv_softmanager_application.setAdapter(myAdapter);
				}else{
					//通知刷新数据
					myAdapter.notifyDataSetChanged();
				}
				

			}


		}.execute();//执行方法,怎么和线程的start()方法很像
	}
	class MyAdapter extends BaseAdapter{

		

		@Override
		public int getCount() {

			//长度为两个集合的长度
			return userappinfo.size()+systemappinfo.size()+2;
		}

		@Override
		public AppInfo getItem(int position) {
			// 
			return appInfo1;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// listView的优化,重点
			if(position==0){
				//添加用用户程序的textView
				TextView textView = new TextView(getApplicationContext());
				textView.setBackgroundColor(Color.GRAY);
				textView.setTextColor(Color.WHITE);
				textView.setText("用户程序("+userappinfo.size()+")");
				return textView;
			}
			if(position==(userappinfo.size()+1)){
				//添加系统程序的textView
				TextView textView = new TextView(getApplicationContext());
				textView.setBackgroundColor(Color.GRAY);
				textView.setTextColor(Color.WHITE);
				textView.setText("系统程序("+systemappinfo.size()+")");
				return textView;
			}
			ViewHolder holder;
			//复用的时候需要判断一下,是否是RelativeLayout才能复用,否则就可能出错
			if(convertView != null&& convertView instanceof RelativeLayout){

				//复用的时候要判断一下是不是textview{

				//重新获取holder
				holder = (ViewHolder) convertView.getTag();

			}else{
				convertView = View.inflate(getApplicationContext(), R.layout.item_softmanager, null);

				holder = new ViewHolder();
				//写这个好慢啊
				holder.iv_item_softmanager_icon	 = (ImageView) convertView.findViewById(R.id.iv_item_softmanager_icon);

				holder.tv__item_softmanager_name = (TextView) convertView.findViewById(R.id.tv__item_softmanager_name);

				holder.tv__item_softmanager_issd	 = (TextView) convertView.findViewById(R.id.tv__item_softmanager_issd);

				holder.tv__item_softmanager_version	 = (TextView) convertView.findViewById(R.id.tv__item_softmanager_version);
				//存起来
				convertView.setTag(holder);
			}
			if(position<userappinfo.size()+1){

				appInfo1 = userappinfo.get(position-1);
			}else{
				appInfo1 = systemappinfo.get(position-userappinfo.size()-2);
			}
			//给控件赋值
			holder.iv_item_softmanager_icon.setImageDrawable(appInfo1.getIcon());

			holder.tv__item_softmanager_name.setText(appInfo1.getName());

			boolean sd = appInfo1.isSD();

			if(sd){
				holder.tv__item_softmanager_issd.setText("SD卡");
			}else{
				holder.tv__item_softmanager_issd.setText("手机内存");
			}
			holder.tv__item_softmanager_version.setText(appInfo1.getVersionName());

			return convertView;
		}

	}
	static class ViewHolder{
		ImageView iv_item_softmanager_icon ;

		TextView tv__item_softmanager_name ,tv__item_softmanager_issd ,tv__item_softmanager_version;

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hidePopupwindow();
		Log.i(tag, "有调用这方法吗");
	}
	public void hidePopupwindow() {
		if(popupWindow!=null){
			//先清除之前的气泡
			popupWindow.dismiss();
		}
	}
	
}

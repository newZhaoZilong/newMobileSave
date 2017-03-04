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
 * @author ɽ��
 *���ڹ����������
 */
public class SoftManagerActivity extends Activity implements OnClickListener {

	private static final String tag = "SoftManagerActivity";
	private ListView lv_softmanager_application;
	private ProgressBar loading;
	private MyAdapter myAdapter;
	private List<AppInfo> appInfos;
	//�û����򼯺�
	private List<AppInfo> userappinfo;
	//ϵͳ���򼯺�
	private List<AppInfo> systemappinfo;
	private TextView tv_softmanager_userorsystem;
	private PopupWindow popupWindow;
	private AppInfo appInfo1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmanager);
		//��ʼ��UI
		initUI();
		//��ʼ������
		initData();
	}

	/**
	 * ��ʼ��UI
	 */


	private void initUI() {
		
		TextView tv_softmanager_rom = (TextView) findViewById(R.id.tv_softmanager_rom);
		
		TextView tv_softmanager_sd = (TextView) findViewById(R.id.tv_softmanager_sd);
		
		//��ȡ�ڴ���ÿռ��sd�����ÿռ�
		
		long availableROM = AppUtil.getAvailableROM();
		
		long availableSD = AppUtil.getAvailableSD();
		
		//����ת��
		
		String romSize = Formatter.formatFileSize(getApplicationContext(), availableROM);
		
		String sdSize = Formatter.formatFileSize(getApplicationContext(), availableSD);
		
		//��ֵ
		
		tv_softmanager_rom.setText("SD������:"+romSize);
		
		tv_softmanager_sd.setText("�ڴ����:"+sdSize);
		
		lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);

		loading = (ProgressBar) findViewById(R.id.loading);

		tv_softmanager_userorsystem = (TextView) findViewById(R.id.tv_softmanager_userorsystem);

		lv_softmanager_application.setOnScrollListener(new OnScrollListener() {

			//����״̬�ı�

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {


			}
			//������
			//view:listview
			//firstVisibleItem: �����һ����ʾ��Ŀ
			//visibleItemCount: ��ʾ��Ŀ�ܸ���
			//totallItemCount: ��Ŀ���ܸ���
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//�жϵ�һ����Ŀ�Ƿ����,���ֿ�ָ���쳣,˵��userappinfo�ǿյ�
				hidePopupwindow();
				//listview�ڳ�ʼ����ʱ��ͻ����onScroll����
				if(userappinfo!=null&&systemappinfo!=null){
					/*if(firstVisibleItem == 0 || firstVisibleItem == userappinfo.size()+1){
						tv_softmanager_userorsystem.setVisibility(View.INVISIBLE);
					}else{*/
						tv_softmanager_userorsystem.setVisibility(View.VISIBLE);
						if(firstVisibleItem>=(userappinfo.size()+1)){

							tv_softmanager_userorsystem.setText("ϵͳ����("+systemappinfo.size()+")");
						}else{
							tv_softmanager_userorsystem.setText("�û�����("+userappinfo.size()+")");
						}
					//}
					
				}
				
			}
		});
		lv_softmanager_application.setOnItemClickListener(new OnItemClickListener() {



			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//ÿ�ε��ǰҪ���֮ǰ��popupwindow

				//��������
				//1.�����û������ϵͳ����(...��)��������
				if(position == 0 || position == userappinfo.size()+1){
					return;
				}
				//2.��ȡ��Ŀ����Ӧ��Ӧ�ó������Ϣ
				
				//����Ҫ��userappinfo��systemappinfo�л�ȡ
				if(position<userappinfo.size()+1){

					appInfo1 = userappinfo.get(position-1);
				}else{
					appInfo1 = systemappinfo.get(position-userappinfo.size()-2);
				}
				//��������
				hidePopupwindow();
				//3,��������
				/*TextView textView = new TextView(getApplicationContext());
				textView.setText("����popuwindow��textview�Ŀؼ�");
				textView.setBackgroundColor(Color.RED);*/
				View popup_view = View.inflate(getApplicationContext(), R.layout.popup_window, null);
				
				
				//��ʼ���ؼ�
				LinearLayout ll_pupupwindow_uninstall = (LinearLayout) popup_view.findViewById(R.id.ll_pupupwindow_uninstall);
				LinearLayout ll_pupupwindow_start = (LinearLayout) popup_view.findViewById(R.id.ll_pupupwindow_start);
				LinearLayout ll_pupupwindow_share = (LinearLayout) popup_view.findViewById(R.id.ll_pupupwindow_share);
				LinearLayout ll_pupupwindow_detail = (LinearLayout) popup_view.findViewById(R.id.ll_pupupwindow_detail);
				
				//���ü����¼�
				ll_pupupwindow_uninstall.setOnClickListener(SoftManagerActivity.this);
				ll_pupupwindow_start.setOnClickListener(SoftManagerActivity.this);
				ll_pupupwindow_share.setOnClickListener(SoftManagerActivity.this);
				ll_pupupwindow_detail.setOnClickListener(SoftManagerActivity.this);
				
				popupWindow = new PopupWindow(popup_view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);


				popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				//PopupWindow popupWindow = new PopupWindow(contentView, width, height);
				//4.��ȡ��Ŀ��λ��,��������ʾ����Ӧ����Ŀ,��Ҫ��y��
				int[] location = new int[2];
				
				view.getLocationInWindow(location);//��ȡ��Ŀx,y������,ͬʱ���浽int[],c����Ա��д��
				//��Ϊ�ǻ�ȡ��Ŀ��view,λ��,��Ŀ��view�������view
				//��ʼ���ؼ�
				
			
				
				//parent:Ҫ�������Ǹ��ؼ���
				//gravity,x,y:����popupwindow��ʾ��λ��
				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, location[0]+300, location[1]);
				//6.���ö���
				//���Ŷ���
				//ǰ�ĸ�:���ƿؼ���û�б䵽��, ����0:û��    1:�����ؼ�
				//���ĸ�:���ƿؼ��ǰ��������Ǹ��ؼ����б仯,
				//RELATIVE_TO_SELF :������仯
				//RELATIVE_TO_SELF	:�Ը��ؼ��仯
				//0.5Ҫ��f,��Ȼ����Ϊ��double����
				ScaleAnimation scaleAnimation = new ScaleAnimation
						(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
				scaleAnimation.setDuration(300);
				//������͸������
				AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
				alphaAnimation.setDuration(300);
				//��϶���
				//shareInterpolator:�Ƿ�ʹ����ͬ�Ķ����岹�� true:���� false:����ʹ�ø��Ե�
				AnimationSet animationSet = new AnimationSet(true);
				//��Ӷ���
				animationSet.addAnimation(scaleAnimation);
				animationSet.addAnimation(alphaAnimation);

				//ִ�ж���
				popup_view.startAnimation(animationSet);

				
			}

			


		});
	}		
	
	//����popupwindow�ĵ���¼�
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
			
			//Ϊɶһ�㶼��Ҫ�������
		}	
		//��������
		hidePopupwindow();
	}
	/**
	 * ж�ص�������
	 */
	private void uninstall() {
		if(appInfo1.isUser()){
			if(!appInfo1.getPackageName().equals(getPackageName())){
				Intent intent = new Intent();
				intent.setAction("android.intent.action.DELETE");
				intent.addCategory("android.intent.category.DEFAULT");
				//Ϊʲôû�п�ָ���쳣��?
				
				intent.setData(Uri.parse("package:"+appInfo1.getPackageName()));
				startActivityForResult(intent, 0);//Ĭ�϶���0,д�˸�ÿдһ��

			}else{
				ToastUtil.show(this, "�������,�ž���ɱ");
			}
		}else{
			ToastUtil.show(this, "Ҫ��ж�س���,����root");
		}
		
			}
	/**
	 * ������Ӧ��
	 */
	private void start(){
		PackageManager packageManager = getPackageManager();
		//��ȡӦ�ó����������ͼ
		Intent intent = packageManager.getLaunchIntentForPackage(appInfo1.getPackageName());
		if(intent!=null){
			startActivity(intent);
		}else{
			ToastUtil.show(this, "ϵͳ���ĳ���,�޷�����");
		}
		
	}
	/**
	 * �������
	 */
	private void share(){
		
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "����һ����ţx���"+appInfo1.getName()+"���ص�ַ:www.baidu.com,�Լ�ȥ��");
		startActivity(intent);
	}
	/**
	 * ��ȡ�������
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
		//���³�ʼ������
		initData();
		//initData();
	}
	/**
	 * ��ʼ������
	 * �첽���ؿ��
	 */
	private void initData() {

		new MyAsyncTask() {

			@Override
			public void preTask() {
				//�����ܼ���Ϊ�ɼ�;
				loading.setVisibility(View.VISIBLE);//���̲߳��ܸ���UI
			}
			//�����߳���ִ�еĲ���
			@Override
			public void doinBack() {
				
				//��ȡ����
				appInfos = AppEngine.getAppInfos(getApplicationContext());

				userappinfo = new ArrayList<AppInfo>();

				systemappinfo = new ArrayList<AppInfo>();
				//���ܼ��Ϸ�Ϊ�û�Ӧ�ú�ϵͳӦ��
				//��������
				for (AppInfo appInfo : appInfos) {
					
					//�ж�
					if(appInfo.isUser()){
						//���û�Ӧ�þ���ӵ��û�������
						userappinfo.add(appInfo);

					}else{
						//���Ǿ���ӵ�ϵͳӦ����
						systemappinfo.add(appInfo);
					}
				}
			}
			//���̺߳�ִ�еĲ���,(���߳�֮��,��handlermessage()��ִ�еĲ���
			@Override
			public void postTask() {
				//�����ܼ���Ϊ���ɼ���
				loading.setVisibility(View.INVISIBLE);

				if(myAdapter==null){
					//Ϊ��,��������
					myAdapter = new MyAdapter();
					lv_softmanager_application.setAdapter(myAdapter);
				}else{
					//֪ͨˢ������
					myAdapter.notifyDataSetChanged();
				}
				

			}


		}.execute();//ִ�з���,��ô���̵߳�start()��������
	}
	class MyAdapter extends BaseAdapter{

		

		@Override
		public int getCount() {

			//����Ϊ�������ϵĳ���
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
			// listView���Ż�,�ص�
			if(position==0){
				//������û������textView
				TextView textView = new TextView(getApplicationContext());
				textView.setBackgroundColor(Color.GRAY);
				textView.setTextColor(Color.WHITE);
				textView.setText("�û�����("+userappinfo.size()+")");
				return textView;
			}
			if(position==(userappinfo.size()+1)){
				//���ϵͳ�����textView
				TextView textView = new TextView(getApplicationContext());
				textView.setBackgroundColor(Color.GRAY);
				textView.setTextColor(Color.WHITE);
				textView.setText("ϵͳ����("+systemappinfo.size()+")");
				return textView;
			}
			ViewHolder holder;
			//���õ�ʱ����Ҫ�ж�һ��,�Ƿ���RelativeLayout���ܸ���,����Ϳ��ܳ���
			if(convertView != null&& convertView instanceof RelativeLayout){

				//���õ�ʱ��Ҫ�ж�һ���ǲ���textview{

				//���»�ȡholder
				holder = (ViewHolder) convertView.getTag();

			}else{
				convertView = View.inflate(getApplicationContext(), R.layout.item_softmanager, null);

				holder = new ViewHolder();
				//д���������
				holder.iv_item_softmanager_icon	 = (ImageView) convertView.findViewById(R.id.iv_item_softmanager_icon);

				holder.tv__item_softmanager_name = (TextView) convertView.findViewById(R.id.tv__item_softmanager_name);

				holder.tv__item_softmanager_issd	 = (TextView) convertView.findViewById(R.id.tv__item_softmanager_issd);

				holder.tv__item_softmanager_version	 = (TextView) convertView.findViewById(R.id.tv__item_softmanager_version);
				//������
				convertView.setTag(holder);
			}
			if(position<userappinfo.size()+1){

				appInfo1 = userappinfo.get(position-1);
			}else{
				appInfo1 = systemappinfo.get(position-userappinfo.size()-2);
			}
			//���ؼ���ֵ
			holder.iv_item_softmanager_icon.setImageDrawable(appInfo1.getIcon());

			holder.tv__item_softmanager_name.setText(appInfo1.getName());

			boolean sd = appInfo1.isSD();

			if(sd){
				holder.tv__item_softmanager_issd.setText("SD��");
			}else{
				holder.tv__item_softmanager_issd.setText("�ֻ��ڴ�");
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
		Log.i(tag, "�е����ⷽ����");
	}
	public void hidePopupwindow() {
		if(popupWindow!=null){
			//�����֮ǰ������
			popupWindow.dismiss();
		}
	}
	
}

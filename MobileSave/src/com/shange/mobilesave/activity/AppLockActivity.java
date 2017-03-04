package com.shange.mobilesave.activity;

import java.util.ArrayList;
import java.util.List;

import com.shange.mobilesave.R;
import com.shange.mobilesave.db.dao.AppLockDao;
import com.shange.mobilesave.db.domain.AppInfo;
import com.shange.mobilesave.engine.AppEngine;
import com.shange.mobilesave.utils.MyAsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AppLockActivity extends Activity {

	private Button bt_unlock,bt_lock;
	private LinearLayout ll_unlock,ll_lock;
	private TextView tv_unlock,tv_lock;
	private ListView lv_unlock,lv_lock;
	private List<AppInfo> appInfos,mUnLockList,mLockList;
	private List<String> lockNameList;
	private MyAdapter mLockAdapter,mUnLockAdapter;
	private AppLockDao mDao;
	private TranslateAnimation mTranslateAnimation;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);

		initUI();
		initData();
		initAnimation();

	}







	/**
	 * ��ʼ��UI
	 */
	private void initUI() {

		bt_unlock = (Button) findViewById(R.id.bt_unlock);
		bt_lock = (Button) findViewById(R.id.bt_lock);

		ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
		ll_lock = (LinearLayout) findViewById(R.id.ll_lock);

		tv_unlock = (TextView) findViewById(R.id.tv_unlock);
		tv_lock = (TextView) findViewById(R.id.tv_lock);

		lv_unlock = (ListView) findViewById(R.id.lv_unlock);
		lv_lock = (ListView) findViewById(R.id.lv_lock);
		//���ð�ť�ĵ���¼�
		bt_unlock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//1.�Ѽ����б�����,δ�����б���ʾ
				ll_lock.setVisibility(View.GONE);
				ll_unlock.setVisibility(View.VISIBLE);
				//2.δ���������ɫͼƬ,�Ѽ������ǳɫͼƬ
				bt_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
				bt_lock.setBackgroundResource(R.drawable.tab_right_default);


			}
		});
		//���ð�ť�ĵ���¼�
		bt_lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//1.�Ѽ����б���ʾ,δ�����б�����
				ll_lock.setVisibility(View.VISIBLE);
				ll_unlock.setVisibility(View.GONE);
				//2.δ�������ǳɫͼƬ,�Ѽ��������ɫͼƬ
				bt_unlock.setBackgroundResource(R.drawable.tab_left_default);
				bt_lock.setBackgroundResource(R.drawable.tab_right_pressed);


			}
		});

	}
	/**
	 * ��ʼ������
	 */
	private void initData() {
		new MyAsyncTask(){

			

			@Override
			public void preTask() {


			}

			@Override
			public void doinBack() {

				appInfos = AppEngine.getAppInfos(getApplicationContext());
				mDao = AppLockDao.getInstance(getApplicationContext());
				lockNameList = AppLockDao.getInstance(getApplicationContext()).findAll();
				mUnLockList = new ArrayList<AppInfo>();
				mLockList = new ArrayList<AppInfo>();
				//������������,һ��δ��������,һ����������
				for (AppInfo appInfo : appInfos) {
					//�������˵����������
					if(lockNameList.contains(appInfo.getPackageName())){
						mLockList.add(appInfo);
					}else{
						mUnLockList.add(appInfo);
					}					
				}								

			}

			@Override
			public void postTask() {
				//����һ�������������������
				mLockAdapter = new MyAdapter(true);
				lv_lock.setAdapter(mLockAdapter);

				mUnLockAdapter = new MyAdapter(false);
				lv_unlock.setAdapter(mUnLockAdapter);

			}

		}.execute();	
	}
	/**
	 * ��ʼ��ƽ�ƶ����ķ���(ƽ�������һ����ȴ�С
	 */
	private void initAnimation() {
		mTranslateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 1,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0);
		mTranslateAnimation.setDuration(500);
	}
	//��������������
	class MyAdapter extends BaseAdapter{

		//����������Ҫ��������,δ��������״̬
		//������Ҫ��flag
		private boolean isLock;
		public MyAdapter(boolean islock) {

			this.isLock = islock;
		}
		@Override
		public int getCount() {
			if(isLock){//��������
				tv_lock.setText("�Ѽ���Ӧ��:"+mLockList.size());
				return mLockList.size();
			}else{
				tv_unlock.setText("δ����Ӧ��:"+mUnLockList.size());
				return mUnLockList.size();
			}
		}

		@Override
		public AppInfo getItem(int position) {
			if(isLock){
				return mLockList.get(position);
			}else{
				return mUnLockList.get(position);
			}
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = View.inflate(getApplicationContext(), R.layout.listview_islock_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final AppInfo appInfo = getItem(position);//��ȡӦ�ó�����Ϣ����
			final View animationView = convertView;//������Ϊ�˱�����������?
			//���ؼ���ֵ
			holder.iv_icon.setBackgroundDrawable(appInfo.getIcon());//����ͼƬ
			holder.tv_name.setText(appInfo.getName());//Ӧ������
			//����һ����ʱ����ִ�ж���
			
			
			if(isLock){
				holder.iv_lock.setBackgroundResource(R.drawable.lock);
			}else{
				holder.iv_lock.setBackgroundResource(R.drawable.unlock);
			}
			//����ͼƬ�ĵ���¼�,���ͼƬʵ�ֶ���
			holder.iv_lock.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//��Ӷ���Ч��
					animationView.startAnimation(mTranslateAnimation);//500����
					//�Զ���ִ�й������¼�����,����������ִ����ɺ�,��ȥ�Ƴ������е�����,
					//�������ݿ�,ˢ�½���
					mTranslateAnimation.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// 
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							//
							
						}
						//����ִ�����,���еķ���
						@Override
						public void onAnimationEnd(Animation animation) {
							if(isLock){
								//��������
								
								//�Ѽ���----->δ��������
								//1.�Ѽ�������ɾ��һ��,δ�����������һ��,�������getItem������ȡ�Ķ���
								mLockList.remove(appInfo);
								mUnLockList.add(appInfo);
								//2.���Ѽ��������ݿ���ɾ��һ������,ͬ�����ݿ�
								//���û�ŵ����߳�,˵������ʱ
								mDao.delete(appInfo.getPackageName());
								//3.ˢ������������
								mLockAdapter.notifyDataSetChanged();
							}else{
								//ִ��λ�ƶ���
					//			animationView.startAnimation(mTranslateAnimation);
								//δ����=====>�Ѽ�������
								//1.δ��������ɾ��һ��,�Ѽ����������һ��,�������getItem������ȡ�Ķ���
								mLockList.add(appInfo);
								mUnLockList.remove(appInfo);
								
								//2.���Ѽ��������ݿ��в���һ������,ͬ�����ݿ�
								//���û�ŵ����߳�,˵������ʱ
								
								mDao.insert(appInfo.getPackageName());
								//3.ˢ������������
								//һ��û��ˢ��,Ӧ�þ����жϳ���,���������������
								mUnLockAdapter.notifyDataSetChanged();
							}
							
						}
					});
					
					
				}
			});

			return convertView;
		}


	}
	static class ViewHolder{
		ImageView iv_icon;
		TextView tv_name;
		ImageView iv_lock;

	}
}

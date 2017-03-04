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
	 * 初始化UI
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
		//设置按钮的点击事件
		bt_unlock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//1.已加锁列表隐藏,未加锁列表显示
				ll_lock.setVisibility(View.GONE);
				ll_unlock.setVisibility(View.VISIBLE);
				//2.未加锁变成深色图片,已加锁变成浅色图片
				bt_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
				bt_lock.setBackgroundResource(R.drawable.tab_right_default);


			}
		});
		//设置按钮的点击事件
		bt_lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//1.已加锁列表显示,未加锁列表隐藏
				ll_lock.setVisibility(View.VISIBLE);
				ll_unlock.setVisibility(View.GONE);
				//2.未加锁变成浅色图片,已加锁变成深色图片
				bt_unlock.setBackgroundResource(R.drawable.tab_left_default);
				bt_lock.setBackgroundResource(R.drawable.tab_right_pressed);


			}
		});

	}
	/**
	 * 初始化数据
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
				//创建两个集合,一个未锁定集合,一个锁定集合
				for (AppInfo appInfo : appInfos) {
					//如果包含说明是锁定的
					if(lockNameList.contains(appInfo.getPackageName())){
						mLockList.add(appInfo);
					}else{
						mUnLockList.add(appInfo);
					}					
				}								

			}

			@Override
			public void postTask() {
				//这里一般的做的是设置适配器
				mLockAdapter = new MyAdapter(true);
				lv_lock.setAdapter(mLockAdapter);

				mUnLockAdapter = new MyAdapter(false);
				lv_unlock.setAdapter(mUnLockAdapter);

			}

		}.execute();	
	}
	/**
	 * 初始化平移动画的方法(平移自身的一个宽度大小
	 */
	private void initAnimation() {
		mTranslateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 1,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0);
		mTranslateAnimation.setDuration(500);
	}
	//创建数据适配器
	class MyAdapter extends BaseAdapter{

		//由于适配器要区分锁定,未锁定两种状态
		//所以需要个flag
		private boolean isLock;
		public MyAdapter(boolean islock) {

			this.isLock = islock;
		}
		@Override
		public int getCount() {
			if(isLock){//设置数量
				tv_lock.setText("已加锁应用:"+mLockList.size());
				return mLockList.size();
			}else{
				tv_unlock.setText("未加锁应用:"+mUnLockList.size());
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
			final AppInfo appInfo = getItem(position);//获取应用程序信息对象
			final View animationView = convertView;//仅仅是为了避免编译错误吗?
			//给控件赋值
			holder.iv_icon.setBackgroundDrawable(appInfo.getIcon());//附上图片
			holder.tv_name.setText(appInfo.getName());//应用名称
			//创建一个临时变量执行动画
			
			
			if(isLock){
				holder.iv_lock.setBackgroundResource(R.drawable.lock);
			}else{
				holder.iv_lock.setBackgroundResource(R.drawable.unlock);
			}
			//设置图片的点击事件,点击图片实现动画
			holder.iv_lock.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//添加动画效果
					animationView.startAnimation(mTranslateAnimation);//500毫秒
					//对动画执行过程做事件监听,监听到动画执行完成后,再去移除集合中的数据,
					//操作数据库,刷新界面
					mTranslateAnimation.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// 
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							//
							
						}
						//动画执行完后,进行的方法
						@Override
						public void onAnimationEnd(Animation animation) {
							if(isLock){
								//开启动画
								
								//已加锁----->未加锁过程
								//1.已加锁集合删除一个,未加锁集合添加一个,对象就是getItem方法获取的对象
								mLockList.remove(appInfo);
								mUnLockList.add(appInfo);
								//2.从已加锁的数据库中删除一条数据,同步数据库
								//这个没放到子线程,说明不耗时
								mDao.delete(appInfo.getPackageName());
								//3.刷新数据适配器
								mLockAdapter.notifyDataSetChanged();
							}else{
								//执行位移动画
					//			animationView.startAnimation(mTranslateAnimation);
								//未加锁=====>已加锁过程
								//1.未加锁集合删除一个,已加锁集合添加一个,对象就是getItem方法获取的对象
								mLockList.add(appInfo);
								mUnLockList.remove(appInfo);
								
								//2.从已加锁的数据库中插入一条数据,同步数据库
								//这个没放到子线程,说明不耗时
								
								mDao.insert(appInfo.getPackageName());
								//3.刷新数据适配器
								//一看没有刷新,应该就能判断出来,就是这个方法出错
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

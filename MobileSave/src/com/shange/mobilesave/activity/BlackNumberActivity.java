package com.shange.mobilesave.activity;
//ListView的优化(重点)
//1.复用converView
//2.对findViewById次数的优化,使用ViewHolder
//3,将ViewHolder定义成静态,不会去创建多个对象
//4,listView如果有多个条目的时候,可以做分页算法,每一次加载20条,逆序返回

import java.util.ArrayList;

import com.shange.mobilesave.R;
import com.shange.mobilesave.db.dao.BlackNumberDao;
import com.shange.mobilesave.db.domain.BlackNumberInfo;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class BlackNumberActivity extends Activity {

	protected static final String tag = "BlackNumberActivity";
	private Button bt_add_black_number;
	private ListView lv_blacknumber;
	private BlackNumberDao mBlackNumberDao;
	private ArrayList<BlackNumberInfo> mBlackNumberList;

	private MyAdapter mAdapter;
	private int mode = 1;
	private boolean mIsLoad = false;
	private int mCount;

	private Handler mHandler = new Handler(){


		public void handleMessage(android.os.Message msg) {
			//4.告知listView可以去设置数据适配器
			if(mAdapter == null){
				mAdapter = new MyAdapter();
				//必须写在这里,否则就从第一条开始配置
				lv_blacknumber.setAdapter(mAdapter);
			}else{
				//如果存在,通知刷新数据,后不能再配置给listview
				//否则就会变成从第一条开始
				mAdapter.notifyDataSetChanged();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		//初始化UI
		initUI();
		//初始化数据
		initData();

	}

	private void initData() {
		//获取数据库中的所有电话号码
		new Thread(){		


			@Override
			public void run() {
				mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());
				mBlackNumberList = mBlackNumberDao.find(0);
				//获取总条目数
				mCount = mBlackNumberDao.getCount();
				//发送消息,同adapter可以更新了
				//3,通过消息机制告知主线程可以去使用包含数据的集合
				mHandler.sendEmptyMessage(0);

			}
		}.start();

	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		bt_add_black_number = (Button) findViewById(R.id.bt_add_black_number);

		lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);



		bt_add_black_number.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//点击添加按钮,弹出对话框
				showDialog();
			}
		});	
		lv_blacknumber.setOnScrollListener(new OnScrollListener() {
			
			//1,滚动到最底部,最后一个listView的条目可见
			//2.滚动状态发生改变, 滚动=======>停止(空闲)
			//3.监听状态改变
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//当滚动状态发生改变的时候
				//条件一:滚动到停止状态
				//条件二:最后一个条目可见(最后一个条目的索引值>=数据适配器中集合的总条目个数-1
				if(scrollState== OnScrollListener.SCROLL_STATE_IDLE&&lv_blacknumber.getLastVisiblePosition()
						>=mBlackNumberList.size()-1&&!mIsLoad){
				/*mIsLoad防止重复加载的变量
				 * 如果当前正在加载mIsLoad就会为true,本次加载完毕后,再将mIsLoad改为false
				 * 如果下一次加载需要去做执行的时候,会判断上述mIsLoad变量,是否为false,如果
				 * 为true,就需要等待上一次加载完成,将其值改为false后再去加载
				 * 
				 */
					
					if(mCount>mBlackNumberList.size()){
						//加载下一页数据
						new Thread(){		

							@Override
							public void run() {
								mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());
								ArrayList<BlackNumberInfo> mBlackNumberList2 = mBlackNumberDao.find(mBlackNumberList.size());
								//添加集合
								mBlackNumberList.addAll(mBlackNumberList2);
								//3,通过消息机制告知主线程可以去使用包含数据的集合
								mHandler.sendEmptyMessage(0);

							}
						}.start();
					}
					
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				
			}
		});

	}

	protected void showDialog() {
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknumber, null);
		//设置视野
		dialog.setView(view,0,0,0,0);

		final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
		RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);

		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

		//设置按钮的点击事件
		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			//这个checkedId是radiogroup控件中的ID
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				//点击的哪个id就是几
				switch (checkedId) {
				case R.id.rb_sms:
					mode = 1;
					break;
				case R.id.rb_phone:
					//拦截电话
					mode = 2;
					Log.i(tag, "mode:"+2);
					break;
				case R.id.rb_all:
					//拦截所有
					mode = 3;
					break;
				}				
			}
		});
		bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设置点击事件,添加黑名单
				String phone = et_phone.getText().toString().trim();
				if(!TextUtils.isEmpty(phone)){
					//添加到数据库中
					mBlackNumberDao.insert(phone, mode+"");

					//填充数据
					BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
					blackNumberInfo.phone = phone;
					blackNumberInfo.mode =  mode+"";
					//添加到list集合中去
					mBlackNumberList.add(0, blackNumberInfo);
					//添加完后,更新adapter
					if(mAdapter!=null){
						mAdapter.notifyDataSetChanged();
					}					
					//关闭对话框
					dialog.dismiss();

				}else{
					ToastUtil.show(getApplicationContext(), "请输入拦截号码");
				}			
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {

			return mBlackNumberList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mBlackNumberList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			//直接就用convertView,为空,inflate
			ViewHolder viewHolder = null;
			if(convertView==null){
				convertView = View.inflate(getApplicationContext(), R.layout.listview_blacknumber_item, null);
				//
				viewHolder = new ViewHolder();
				viewHolder.tv_phone	= (TextView) convertView.findViewById(R.id.tv_number);
				viewHolder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
				viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
				convertView.setTag(viewHolder);
			}else{

				viewHolder = (ViewHolder) convertView.getTag();

			}
			viewHolder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//删除数据库
					mBlackNumberDao.delete(mBlackNumberList.get(position).phone);
					//删除集合中
					mBlackNumberList.remove(position);

					if(mAdapter!=null){
						mAdapter.notifyDataSetChanged();
					}								
				}
			});
			viewHolder.tv_phone.setText(mBlackNumberList.get(position).phone);
			int mode2 = Integer.parseInt(mBlackNumberList.get(position).mode);
			switch (mode2) {
			case 1:
				viewHolder.tv_mode.setText("拦截短信");
				break;
			case 2:
				viewHolder.tv_mode.setText("拦截电话");
				break;
			case 3:
				viewHolder.tv_mode.setText("拦截所有");
				break;

			}	

			return convertView;
		}


	}
	//创建一个javaBean;javaBean感觉就是一个存储一连串有关系数据的类
	static class ViewHolder{

		TextView tv_phone ;
		TextView tv_mode ;
		ImageView iv_delete;

	}
}

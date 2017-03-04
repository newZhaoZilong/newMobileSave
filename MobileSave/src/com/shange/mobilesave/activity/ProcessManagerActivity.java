package com.shange.mobilesave.activity;

import java.util.ArrayList;
import java.util.List;

import com.shange.mobilesave.R;
import com.shange.mobilesave.activity.SoftManagerActivity.MyAdapter;
import com.shange.mobilesave.activity.SoftManagerActivity.ViewHolder;
import com.shange.mobilesave.db.domain.AppInfo;
import com.shange.mobilesave.db.domain.ProcessInfo;
import com.shange.mobilesave.engine.ProcessInfoProvider;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.MyAsyncTask;
import com.shange.mobilesave.utils.SpUtil;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProcessManagerActivity extends Activity implements OnClickListener {

	protected static final String tag = "ProcessManagerActivity";
	private Context mContext;
	private TextView tv_process_count,tv_memory_info,tv_des;
	private ListView mLv_process_list;
	private Button bt_all,bt_reverse,bt_clean,bt_setting;
	private ProgressBar loading;
	private List<ProcessInfo> mProcessInfoList,mSystemList,mCustomerList;
	private MyAdapter myAdapter;
	private ProcessInfo mProcessInfo;
	private long mAvailSpace;
	private String mStr_totalSpace;
	private int mProcessCount;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processmanager);		
		mContext = this;
		//初始化UI
		initUI();
		//初始化标题数据
		initTitleData();
		//初始化listdata;
		initListData();
	}
	/**
	 * 初始化UI
	 */
	private void initUI() {
		//获取进程总数
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		//获取剩余总共控件
		tv_memory_info = (TextView) findViewById(R.id.tv_memory_info);

		tv_des = (TextView) findViewById(R.id.tv_des);
		//找到listview
		mLv_process_list = (ListView) findViewById(R.id.lv_process_list);
		//找到4个按钮
		bt_all = (Button) findViewById(R.id.bt_all);
		bt_reverse = (Button) findViewById(R.id.bt_reverse);
		bt_clean = (Button) findViewById(R.id.bt_clean);
		bt_setting = (Button) findViewById(R.id.bt_setting);
		
		//设置按钮的点击事件
		bt_all.setOnClickListener(this);
		bt_reverse.setOnClickListener(this);
		bt_clean.setOnClickListener(this);
		bt_setting.setOnClickListener(this);

		loading = (ProgressBar) findViewById(R.id.loading);

		//设置listview滑动监听事件
		mLv_process_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {


			}
			//在滚动时调用
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(mCustomerList!=null&&mSystemList!=null){
					if(firstVisibleItem>=mCustomerList.size()+1){
						//滚动到了系统目录
						tv_des.setText("系统进程("+mSystemList.size()+")");
					}else{
						//滚动到了用户应用条目
						tv_des.setText("用户进程("+mCustomerList.size()+")");
					}
				}
			}
		});
		//设置listview点击事件
		mLv_process_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0 || position == mCustomerList.size()+1){
					return;
				}else{
					if(position<mCustomerList.size()+1){
						mProcessInfo = mCustomerList.get(position-1);
					}else{
						//返回系统应用对应条目的对象
						mProcessInfo = mSystemList.get(position-mCustomerList.size()-2);
					}
					if(mProcessInfo!=null){
						if(!mProcessInfo.packageName.equals(getPackageName())){
							//选中条目指向的对象和本应用的报名不一致,才需要去状态取反和设置单选框状态
							//状态取反
							mProcessInfo.isCheck = !mProcessInfo.isCheck;
							//checkbox显示状态切换
							//通过选中条目的view对象,findViewById找到此条目指向的cb_box,然后切换其状态
							CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
							
							cb_box.setChecked(mProcessInfo.isCheck);
							
							Log.i(tag, "状态:"+mProcessInfo.isCheck);
						}
					}
				}
			}
		});
	}

	/**
	 * 初始化标题数据
	 */
	private void initTitleData() {
		mProcessCount = ProcessInfoProvider.getProcessCount(mContext);

		tv_process_count.setText("进程总数:"+mProcessCount);
		mAvailSpace = ProcessInfoProvider.getAvailSpace(mContext);

		String str_availSpace = Formatter.formatFileSize(mContext, mAvailSpace);
		//获取运行内存总控件
		long totalSpace = ProcessInfoProvider.getTotalSpace(mContext);

		mStr_totalSpace = Formatter.formatFileSize(mContext, totalSpace);
		//赋值
		tv_memory_info.setText("剩余/总共:"+str_availSpace+"/"+mStr_totalSpace);


	}
	/**
	 * 初始化listview数据
	 * 用异步加载框架做
	 */
	private void initListData() {

		new MyAsyncTask() {


			//线程之前
			@Override
			public void preTask() {
				//设置进度条可见
				loading.setVisibility(View.VISIBLE);


			}

			//子线程中要做的事
			@Override
			public void doinBack() {
				//这步是关键,获取所有数据
				mProcessInfoList = ProcessInfoProvider.getProcessInfo(mContext);

				mSystemList = new ArrayList<ProcessInfo>();

				mCustomerList = new ArrayList<ProcessInfo>();
				//将系统进程和用户进程区分开来
				for (ProcessInfo info : mProcessInfoList) {
					if(info.isSystem){
						//系统进程集合里有数据了
						mSystemList.add(info);
					}else{
						//用户进程集合里有数据了
						mCustomerList.add(info);
					}

				}
				//有数据后,就该创建adapter传给listview了
			}
			//子线程之后,一般是创建adapter
			@Override
			public void postTask() {
				//设置进度条不可见
				loading.setVisibility(View.INVISIBLE);//因为在framelayout中,因为是叠加,有没有空间没区别
				if(myAdapter==null){
					myAdapter = new MyAdapter();
					mLv_process_list.setAdapter(myAdapter);
				}else{
					//通知刷新数据
					myAdapter.notifyDataSetChanged();
				}
				//其实这一步实际上不需要写
				if(tv_des!=null&&mCustomerList!=null){
					tv_des.setText("用户进程("+mCustomerList.size()+")");
				}

			}
		}.execute();

	}
	class MyAdapter extends BaseAdapter{
		//获取数据适配器中条目类型的总数,修改成两种(纯文本,图片+文字)
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}

		//指定索引指向的条目类型,条目类型状态码指定(0(复用系统),1)
		@Override
		public int getItemViewType(int position) {
			if(position == 0 || position == mCustomerList.size()+1){
				//返回0,代表纯文本条目的状态码
				return 0;
			}else{
				//返回1,代表图片+文本条目状态码
				return 1;
			}
		}

		//listView中添加两个描述条目
		@Override
		public int getCount() {
			//在这里做判断,如果根据是否需要显示系统进程而改变
			//listview的长度
			boolean state = SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, true);
			if(state){//为true说明需要显示
				return mCustomerList.size()+mSystemList.size()+2;
			}else{//为false说明不需要显示,长度变短
				return mCustomerList.size()+1;
			}			
			
		}

		@Override
		public ProcessInfo getItem(int position) {
			if(position == 0 || position == mCustomerList.size()+1){
				return null;
			}else{
				if(position<mCustomerList.size()+1){
					return mCustomerList.get(position-1);
				}else{
					//返回系统进程对应条目的对象
					return mSystemList.get(position - mCustomerList.size()-2);
				}
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);

			if(type == 0){
				//展示灰色纯文本条目
				ViewTitleHolder holder = null;
				if(convertView == null){
					convertView = View.inflate(getApplicationContext(), R.layout.listview_app_item_title, null);
					holder = new ViewTitleHolder();
					holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
					convertView.setTag(holder);
				}else{
					holder = (ViewTitleHolder) convertView.getTag();
				}
				if(position == 0){
					holder.tv_title.setText("用户进程("+mCustomerList.size()+")");
				}else{
					holder.tv_title.setText("系统进程("+mSystemList.size()+")");
				}
				return convertView;
			}else{
				//展示图片+文字条目
				ViewHolder holder = null;
				if(convertView == null){
					convertView = View.inflate(getApplicationContext(), R.layout.listview_process_item, null);
					holder = new ViewHolder();
					holder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
					holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
					holder.tv_memory_info = (TextView) convertView.findViewById(R.id.tv_memory_info);
					holder.cb_box = (CheckBox) convertView.findViewById(R.id.cb_box);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				//赋值
				holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
				holder.tv_name.setText(getItem(position).name);
				String strSize = Formatter.formatFileSize(getApplicationContext(), getItem(position).memSize);
				holder.tv_memory_info.setText(strSize);

				//本进程不能被选中,所以先将checkbox隐藏掉
				if(getItem(position).packageName.equals(getPackageName())){
					holder.cb_box.setVisibility(View.GONE);//控件隐藏并不保留空间
				}else{
					holder.cb_box.setVisibility(View.VISIBLE);
				}

				holder.cb_box.setChecked(getItem(position).isCheck);

				return convertView;
			}
		}
	}

	static class ViewHolder{
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_memory_info;
		CheckBox cb_box;
	}

	static class ViewTitleHolder{
		TextView tv_title;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 设置按钮的点击事件
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.bt_all:
			
			selectAll();
			break;
		case R.id.bt_reverse:
			reverseAll();
			break;
		case R.id.bt_clean:
			cleanAll();
			break;
		case R.id.bt_setting:
			setting();
			break;

		}
	}
	


	/**
	 * 全部选中
	 */
	private void selectAll() {
		//1.将所有的集合中的对象上isCheck字段设置为true,代表全选,排除当前应用
		for(ProcessInfo processInfo:mCustomerList){
			if(processInfo.getPackageName().equals(getPackageName())){
				continue;
			}
			processInfo.isCheck = true;
		}
		for(ProcessInfo processInfo: mSystemList){
			processInfo.isCheck = true;
		}
		//2,通知数据适配器刷新
		if(myAdapter!=null){
			myAdapter.notifyDataSetChanged();
		}
		
	}
	/**
	 * 反选
	 */
	private void reverseAll() {
		
		for(ProcessInfo processInfo:mCustomerList){
			if(processInfo.getPackageName().equals(getPackageName())){
				continue;//这个continue用的真是棒
			}
			processInfo.isCheck = !processInfo.isCheck;
		}
		for(ProcessInfo processInfo: mSystemList){
			processInfo.isCheck = !processInfo.isCheck;
		}
		//2,通知数据适配器刷新
		if(myAdapter!=null){
			myAdapter.notifyDataSetChanged();
		}
		
	}
	/**
	 * 清理所有选中的进程
	 */
	private void cleanAll() {
		//1,获取选中进程
		//2,创建一个记录需要杀死的进程的集合
		//3.记录需要杀死的用户进程
		//4.记录需要杀死的系统进程
		//5.循环遍历killProcessList,然后去移除mCustomerList和mSystemList中的对象
		//6.判断当前进程在哪个集合中,从所在集合中移除
		//7.杀死记录在killProcessList中的进程
		//8.在集合改变后,需要通知数据适配器刷新
		//9.进程总数的更新
		//10,更新可用剩余空间(释放空间+原有剩余空间== 当前剩余空间)
		//11,更新进程总数和剩余空间大小
		//12,通过吐司告知用户,释放了多少空间,杀死了几个进程
		
		//1,获取选中进程
		//2,创建一个记录需要杀死的进程的集合
		List<ProcessInfo> killProcessList = new ArrayList<ProcessInfo>();
		for(ProcessInfo processInfo:mCustomerList){
			if(processInfo.getPackageName().equals(getPackageName())){
				continue;
			}
			if(processInfo.isCheck){
				//不能在集合循环过程中去移除集合中的对象
//				mCustomerList.remove(processInfo);
				//3,记录需要杀死的用户进程
				killProcessList.add(processInfo);
			}
		}
		
		for(ProcessInfo processInfo:mSystemList){
			if(processInfo.isCheck){
				//4,记录需要杀死的系统进程
				killProcessList.add(processInfo);
			}
		}
		//5,循环遍历killProcessList,然后去移除mCustomerList和mSystemList中的对象
		long totalReleaseSpace = 0;
		for (ProcessInfo processInfo : killProcessList) {
			//6,判断当前进程在那个集合中,从所在集合中移除
			if(mCustomerList.contains(processInfo)){
				mCustomerList.remove(processInfo);
			}
			
			if(mSystemList.contains(processInfo)){
				mSystemList.remove(processInfo);
			}
			//7,杀死记录在killProcessList中的进程
			ProcessInfoProvider.killProcess(this,processInfo);
			
			//记录释放空间的总大小
			totalReleaseSpace += processInfo.memSize;
		}
		//8,在集合改变后,需要通知数据适配器刷新
		if(myAdapter!=null){
			myAdapter.notifyDataSetChanged();
		}
		//9,进程总数的更新
		mProcessCount -= killProcessList.size();
		//10,更新可用剩余空间(释放空间+原有剩余空间 == 当前剩余空间)
		mAvailSpace += totalReleaseSpace;
		//11,根据进程总数和剩余空间大小
		tv_process_count.setText("进程总数:"+mProcessCount);
		tv_memory_info.setText("剩余/总共"+Formatter.formatFileSize(this, mAvailSpace)+"/"+mStr_totalSpace);
		//12,通过吐司告知用户,释放了多少空间,杀死了几个进程,
		String totalRelease = Formatter.formatFileSize(this, totalReleaseSpace);
//		ToastUtil.show(getApplicationContext(), "杀死了"+killProcessList.size()+"个进程,释放了"+totalRelease+"空间");
		
//		jni  java--c   c---java
		//占位符指定数据%d代表整数占位符,%s代表字符串占位符
		ToastUtil.show(getApplicationContext(), 
				String.format("杀死了%d进程,释放了%s空间", killProcessList.size(),totalRelease));
	}
	/**
	 * 设置界面
	 */
	private void setting() {
		//开启一个新的界面,并且需要新的界面的信息
		Intent intent = new Intent(mContext,ProcessSettingActivity.class);
		
		startActivityForResult(intent, 0);//因为就一个,写个默认值
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//更新adapter
		if(myAdapter!=null){
			myAdapter.notifyDataSetChanged();//通知数据更新
		}
	}

}

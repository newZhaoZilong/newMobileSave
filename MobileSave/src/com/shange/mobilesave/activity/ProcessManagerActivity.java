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
		//��ʼ��UI
		initUI();
		//��ʼ����������
		initTitleData();
		//��ʼ��listdata;
		initListData();
	}
	/**
	 * ��ʼ��UI
	 */
	private void initUI() {
		//��ȡ��������
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		//��ȡʣ���ܹ��ؼ�
		tv_memory_info = (TextView) findViewById(R.id.tv_memory_info);

		tv_des = (TextView) findViewById(R.id.tv_des);
		//�ҵ�listview
		mLv_process_list = (ListView) findViewById(R.id.lv_process_list);
		//�ҵ�4����ť
		bt_all = (Button) findViewById(R.id.bt_all);
		bt_reverse = (Button) findViewById(R.id.bt_reverse);
		bt_clean = (Button) findViewById(R.id.bt_clean);
		bt_setting = (Button) findViewById(R.id.bt_setting);
		
		//���ð�ť�ĵ���¼�
		bt_all.setOnClickListener(this);
		bt_reverse.setOnClickListener(this);
		bt_clean.setOnClickListener(this);
		bt_setting.setOnClickListener(this);

		loading = (ProgressBar) findViewById(R.id.loading);

		//����listview���������¼�
		mLv_process_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {


			}
			//�ڹ���ʱ����
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(mCustomerList!=null&&mSystemList!=null){
					if(firstVisibleItem>=mCustomerList.size()+1){
						//��������ϵͳĿ¼
						tv_des.setText("ϵͳ����("+mSystemList.size()+")");
					}else{
						//���������û�Ӧ����Ŀ
						tv_des.setText("�û�����("+mCustomerList.size()+")");
					}
				}
			}
		});
		//����listview����¼�
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
						//����ϵͳӦ�ö�Ӧ��Ŀ�Ķ���
						mProcessInfo = mSystemList.get(position-mCustomerList.size()-2);
					}
					if(mProcessInfo!=null){
						if(!mProcessInfo.packageName.equals(getPackageName())){
							//ѡ����Ŀָ��Ķ���ͱ�Ӧ�õı�����һ��,����Ҫȥ״̬ȡ�������õ�ѡ��״̬
							//״̬ȡ��
							mProcessInfo.isCheck = !mProcessInfo.isCheck;
							//checkbox��ʾ״̬�л�
							//ͨ��ѡ����Ŀ��view����,findViewById�ҵ�����Ŀָ���cb_box,Ȼ���л���״̬
							CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
							
							cb_box.setChecked(mProcessInfo.isCheck);
							
							Log.i(tag, "״̬:"+mProcessInfo.isCheck);
						}
					}
				}
			}
		});
	}

	/**
	 * ��ʼ����������
	 */
	private void initTitleData() {
		mProcessCount = ProcessInfoProvider.getProcessCount(mContext);

		tv_process_count.setText("��������:"+mProcessCount);
		mAvailSpace = ProcessInfoProvider.getAvailSpace(mContext);

		String str_availSpace = Formatter.formatFileSize(mContext, mAvailSpace);
		//��ȡ�����ڴ��ܿؼ�
		long totalSpace = ProcessInfoProvider.getTotalSpace(mContext);

		mStr_totalSpace = Formatter.formatFileSize(mContext, totalSpace);
		//��ֵ
		tv_memory_info.setText("ʣ��/�ܹ�:"+str_availSpace+"/"+mStr_totalSpace);


	}
	/**
	 * ��ʼ��listview����
	 * ���첽���ؿ����
	 */
	private void initListData() {

		new MyAsyncTask() {


			//�߳�֮ǰ
			@Override
			public void preTask() {
				//���ý������ɼ�
				loading.setVisibility(View.VISIBLE);


			}

			//���߳���Ҫ������
			@Override
			public void doinBack() {
				//�ⲽ�ǹؼ�,��ȡ��������
				mProcessInfoList = ProcessInfoProvider.getProcessInfo(mContext);

				mSystemList = new ArrayList<ProcessInfo>();

				mCustomerList = new ArrayList<ProcessInfo>();
				//��ϵͳ���̺��û��������ֿ���
				for (ProcessInfo info : mProcessInfoList) {
					if(info.isSystem){
						//ϵͳ���̼�������������
						mSystemList.add(info);
					}else{
						//�û����̼�������������
						mCustomerList.add(info);
					}

				}
				//�����ݺ�,�͸ô���adapter����listview��
			}
			//���߳�֮��,һ���Ǵ���adapter
			@Override
			public void postTask() {
				//���ý��������ɼ�
				loading.setVisibility(View.INVISIBLE);//��Ϊ��framelayout��,��Ϊ�ǵ���,��û�пռ�û����
				if(myAdapter==null){
					myAdapter = new MyAdapter();
					mLv_process_list.setAdapter(myAdapter);
				}else{
					//֪ͨˢ������
					myAdapter.notifyDataSetChanged();
				}
				//��ʵ��һ��ʵ���ϲ���Ҫд
				if(tv_des!=null&&mCustomerList!=null){
					tv_des.setText("�û�����("+mCustomerList.size()+")");
				}

			}
		}.execute();

	}
	class MyAdapter extends BaseAdapter{
		//��ȡ��������������Ŀ���͵�����,�޸ĳ�����(���ı�,ͼƬ+����)
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}

		//ָ������ָ�����Ŀ����,��Ŀ����״̬��ָ��(0(����ϵͳ),1)
		@Override
		public int getItemViewType(int position) {
			if(position == 0 || position == mCustomerList.size()+1){
				//����0,�����ı���Ŀ��״̬��
				return 0;
			}else{
				//����1,����ͼƬ+�ı���Ŀ״̬��
				return 1;
			}
		}

		//listView���������������Ŀ
		@Override
		public int getCount() {
			//���������ж�,��������Ƿ���Ҫ��ʾϵͳ���̶��ı�
			//listview�ĳ���
			boolean state = SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, true);
			if(state){//Ϊtrue˵����Ҫ��ʾ
				return mCustomerList.size()+mSystemList.size()+2;
			}else{//Ϊfalse˵������Ҫ��ʾ,���ȱ��
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
					//����ϵͳ���̶�Ӧ��Ŀ�Ķ���
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
				//չʾ��ɫ���ı���Ŀ
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
					holder.tv_title.setText("�û�����("+mCustomerList.size()+")");
				}else{
					holder.tv_title.setText("ϵͳ����("+mSystemList.size()+")");
				}
				return convertView;
			}else{
				//չʾͼƬ+������Ŀ
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
				//��ֵ
				holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
				holder.tv_name.setText(getItem(position).name);
				String strSize = Formatter.formatFileSize(getApplicationContext(), getItem(position).memSize);
				holder.tv_memory_info.setText(strSize);

				//�����̲��ܱ�ѡ��,�����Ƚ�checkbox���ص�
				if(getItem(position).packageName.equals(getPackageName())){
					holder.cb_box.setVisibility(View.GONE);//�ؼ����ز��������ռ�
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
	 * ���ð�ť�ĵ���¼�
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
	 * ȫ��ѡ��
	 */
	private void selectAll() {
		//1.�����еļ����еĶ�����isCheck�ֶ�����Ϊtrue,����ȫѡ,�ų���ǰӦ��
		for(ProcessInfo processInfo:mCustomerList){
			if(processInfo.getPackageName().equals(getPackageName())){
				continue;
			}
			processInfo.isCheck = true;
		}
		for(ProcessInfo processInfo: mSystemList){
			processInfo.isCheck = true;
		}
		//2,֪ͨ����������ˢ��
		if(myAdapter!=null){
			myAdapter.notifyDataSetChanged();
		}
		
	}
	/**
	 * ��ѡ
	 */
	private void reverseAll() {
		
		for(ProcessInfo processInfo:mCustomerList){
			if(processInfo.getPackageName().equals(getPackageName())){
				continue;//���continue�õ����ǰ�
			}
			processInfo.isCheck = !processInfo.isCheck;
		}
		for(ProcessInfo processInfo: mSystemList){
			processInfo.isCheck = !processInfo.isCheck;
		}
		//2,֪ͨ����������ˢ��
		if(myAdapter!=null){
			myAdapter.notifyDataSetChanged();
		}
		
	}
	/**
	 * ��������ѡ�еĽ���
	 */
	private void cleanAll() {
		//1,��ȡѡ�н���
		//2,����һ����¼��Ҫɱ���Ľ��̵ļ���
		//3.��¼��Ҫɱ�����û�����
		//4.��¼��Ҫɱ����ϵͳ����
		//5.ѭ������killProcessList,Ȼ��ȥ�Ƴ�mCustomerList��mSystemList�еĶ���
		//6.�жϵ�ǰ�������ĸ�������,�����ڼ������Ƴ�
		//7.ɱ����¼��killProcessList�еĽ���
		//8.�ڼ��ϸı��,��Ҫ֪ͨ����������ˢ��
		//9.���������ĸ���
		//10,���¿���ʣ��ռ�(�ͷſռ�+ԭ��ʣ��ռ�== ��ǰʣ��ռ�)
		//11,���½���������ʣ��ռ��С
		//12,ͨ����˾��֪�û�,�ͷ��˶��ٿռ�,ɱ���˼�������
		
		//1,��ȡѡ�н���
		//2,����һ����¼��Ҫɱ���Ľ��̵ļ���
		List<ProcessInfo> killProcessList = new ArrayList<ProcessInfo>();
		for(ProcessInfo processInfo:mCustomerList){
			if(processInfo.getPackageName().equals(getPackageName())){
				continue;
			}
			if(processInfo.isCheck){
				//�����ڼ���ѭ��������ȥ�Ƴ������еĶ���
//				mCustomerList.remove(processInfo);
				//3,��¼��Ҫɱ�����û�����
				killProcessList.add(processInfo);
			}
		}
		
		for(ProcessInfo processInfo:mSystemList){
			if(processInfo.isCheck){
				//4,��¼��Ҫɱ����ϵͳ����
				killProcessList.add(processInfo);
			}
		}
		//5,ѭ������killProcessList,Ȼ��ȥ�Ƴ�mCustomerList��mSystemList�еĶ���
		long totalReleaseSpace = 0;
		for (ProcessInfo processInfo : killProcessList) {
			//6,�жϵ�ǰ�������Ǹ�������,�����ڼ������Ƴ�
			if(mCustomerList.contains(processInfo)){
				mCustomerList.remove(processInfo);
			}
			
			if(mSystemList.contains(processInfo)){
				mSystemList.remove(processInfo);
			}
			//7,ɱ����¼��killProcessList�еĽ���
			ProcessInfoProvider.killProcess(this,processInfo);
			
			//��¼�ͷſռ���ܴ�С
			totalReleaseSpace += processInfo.memSize;
		}
		//8,�ڼ��ϸı��,��Ҫ֪ͨ����������ˢ��
		if(myAdapter!=null){
			myAdapter.notifyDataSetChanged();
		}
		//9,���������ĸ���
		mProcessCount -= killProcessList.size();
		//10,���¿���ʣ��ռ�(�ͷſռ�+ԭ��ʣ��ռ� == ��ǰʣ��ռ�)
		mAvailSpace += totalReleaseSpace;
		//11,���ݽ���������ʣ��ռ��С
		tv_process_count.setText("��������:"+mProcessCount);
		tv_memory_info.setText("ʣ��/�ܹ�"+Formatter.formatFileSize(this, mAvailSpace)+"/"+mStr_totalSpace);
		//12,ͨ����˾��֪�û�,�ͷ��˶��ٿռ�,ɱ���˼�������,
		String totalRelease = Formatter.formatFileSize(this, totalReleaseSpace);
//		ToastUtil.show(getApplicationContext(), "ɱ����"+killProcessList.size()+"������,�ͷ���"+totalRelease+"�ռ�");
		
//		jni  java--c   c---java
		//ռλ��ָ������%d��������ռλ��,%s�����ַ���ռλ��
		ToastUtil.show(getApplicationContext(), 
				String.format("ɱ����%d����,�ͷ���%s�ռ�", killProcessList.size(),totalRelease));
	}
	/**
	 * ���ý���
	 */
	private void setting() {
		//����һ���µĽ���,������Ҫ�µĽ������Ϣ
		Intent intent = new Intent(mContext,ProcessSettingActivity.class);
		
		startActivityForResult(intent, 0);//��Ϊ��һ��,д��Ĭ��ֵ
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//����adapter
		if(myAdapter!=null){
			myAdapter.notifyDataSetChanged();//֪ͨ���ݸ���
		}
	}

}

package com.shange.mobilesave.activity;
//ListView���Ż�(�ص�)
//1.����converView
//2.��findViewById�������Ż�,ʹ��ViewHolder
//3,��ViewHolder����ɾ�̬,����ȥ�����������
//4,listView����ж����Ŀ��ʱ��,��������ҳ�㷨,ÿһ�μ���20��,���򷵻�

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
			//4.��֪listView����ȥ��������������
			if(mAdapter == null){
				mAdapter = new MyAdapter();
				//����д������,����ʹӵ�һ����ʼ����
				lv_blacknumber.setAdapter(mAdapter);
			}else{
				//�������,֪ͨˢ������,���������ø�listview
				//����ͻ��ɴӵ�һ����ʼ
				mAdapter.notifyDataSetChanged();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		//��ʼ��UI
		initUI();
		//��ʼ������
		initData();

	}

	private void initData() {
		//��ȡ���ݿ��е����е绰����
		new Thread(){		


			@Override
			public void run() {
				mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());
				mBlackNumberList = mBlackNumberDao.find(0);
				//��ȡ����Ŀ��
				mCount = mBlackNumberDao.getCount();
				//������Ϣ,ͬadapter���Ը�����
				//3,ͨ����Ϣ���Ƹ�֪���߳̿���ȥʹ�ð������ݵļ���
				mHandler.sendEmptyMessage(0);

			}
		}.start();

	}

	/**
	 * ��ʼ��UI
	 */
	private void initUI() {
		bt_add_black_number = (Button) findViewById(R.id.bt_add_black_number);

		lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);



		bt_add_black_number.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//�����Ӱ�ť,�����Ի���
				showDialog();
			}
		});	
		lv_blacknumber.setOnScrollListener(new OnScrollListener() {
			
			//1,��������ײ�,���һ��listView����Ŀ�ɼ�
			//2.����״̬�����ı�, ����=======>ֹͣ(����)
			//3.����״̬�ı�
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//������״̬�����ı��ʱ��
				//����һ:������ֹͣ״̬
				//������:���һ����Ŀ�ɼ�(���һ����Ŀ������ֵ>=�����������м��ϵ�����Ŀ����-1
				if(scrollState== OnScrollListener.SCROLL_STATE_IDLE&&lv_blacknumber.getLastVisiblePosition()
						>=mBlackNumberList.size()-1&&!mIsLoad){
				/*mIsLoad��ֹ�ظ����صı���
				 * �����ǰ���ڼ���mIsLoad�ͻ�Ϊtrue,���μ�����Ϻ�,�ٽ�mIsLoad��Ϊfalse
				 * �����һ�μ�����Ҫȥ��ִ�е�ʱ��,���ж�����mIsLoad����,�Ƿ�Ϊfalse,���
				 * Ϊtrue,����Ҫ�ȴ���һ�μ������,����ֵ��Ϊfalse����ȥ����
				 * 
				 */
					
					if(mCount>mBlackNumberList.size()){
						//������һҳ����
						new Thread(){		

							@Override
							public void run() {
								mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());
								ArrayList<BlackNumberInfo> mBlackNumberList2 = mBlackNumberDao.find(mBlackNumberList.size());
								//��Ӽ���
								mBlackNumberList.addAll(mBlackNumberList2);
								//3,ͨ����Ϣ���Ƹ�֪���߳̿���ȥʹ�ð������ݵļ���
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
		//������Ұ
		dialog.setView(view,0,0,0,0);

		final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
		RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);

		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

		//���ð�ť�ĵ���¼�
		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			//���checkedId��radiogroup�ؼ��е�ID
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				//������ĸ�id���Ǽ�
				switch (checkedId) {
				case R.id.rb_sms:
					mode = 1;
					break;
				case R.id.rb_phone:
					//���ص绰
					mode = 2;
					Log.i(tag, "mode:"+2);
					break;
				case R.id.rb_all:
					//��������
					mode = 3;
					break;
				}				
			}
		});
		bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ���õ���¼�,��Ӻ�����
				String phone = et_phone.getText().toString().trim();
				if(!TextUtils.isEmpty(phone)){
					//��ӵ����ݿ���
					mBlackNumberDao.insert(phone, mode+"");

					//�������
					BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
					blackNumberInfo.phone = phone;
					blackNumberInfo.mode =  mode+"";
					//��ӵ�list������ȥ
					mBlackNumberList.add(0, blackNumberInfo);
					//������,����adapter
					if(mAdapter!=null){
						mAdapter.notifyDataSetChanged();
					}					
					//�رնԻ���
					dialog.dismiss();

				}else{
					ToastUtil.show(getApplicationContext(), "���������غ���");
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
			//ֱ�Ӿ���convertView,Ϊ��,inflate
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
					//ɾ�����ݿ�
					mBlackNumberDao.delete(mBlackNumberList.get(position).phone);
					//ɾ��������
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
				viewHolder.tv_mode.setText("���ض���");
				break;
			case 2:
				viewHolder.tv_mode.setText("���ص绰");
				break;
			case 3:
				viewHolder.tv_mode.setText("��������");
				break;

			}	

			return convertView;
		}


	}
	//����һ��javaBean;javaBean�о�����һ���洢һ�����й�ϵ���ݵ���
	static class ViewHolder{

		TextView tv_phone ;
		TextView tv_mode ;
		ImageView iv_delete;

	}
}

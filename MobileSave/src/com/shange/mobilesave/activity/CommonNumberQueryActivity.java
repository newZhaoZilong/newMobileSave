package com.shange.mobilesave.activity;

import java.util.List;

import com.shange.mobilesave.R;
import com.shange.mobilesave.engine.CommonnumDao;
import com.shange.mobilesave.engine.CommonnumDao.Child;
import com.shange.mobilesave.engine.CommonnumDao.Group;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class CommonNumberQueryActivity extends Activity {

	private ExpandableListView el_common_numberlist;
	private List<Group> mGroup;
	private MyAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commonquery);
		
		initUI();
		initData();
	}


	/**
	 * ��ʼ��UI
	 */
	private void initUI() {
		
		el_common_numberlist = (ExpandableListView) findViewById(R.id.el_common_numberlist);
	}
	/**
	 * ��ʼ������
	 */
	private void initData() {
		CommonnumDao commonnumDao = new CommonnumDao();
		mGroup = commonnumDao.getGroup();
		
		myAdapter = new MyAdapter();
		
		el_common_numberlist.setAdapter(myAdapter);
		
		//������Ŀ����¼�
		el_common_numberlist.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//�����Ŀ,��ת����绰����
				//����绰����
				String number = mGroup.get(groupPosition).childList.get(childPosition).number;
				phone(number);
				
				return false;
			}
		});
		
	}
	/**
	 * ��绰
	 */
	protected void phone(String number) {
		 Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +number));
         //֪ͨactivtity�������call����
        startActivity(intent);
		
	}
	class MyAdapter extends BaseExpandableListAdapter{

		//group������,group�������м���������Ǽ�
		@Override
		public int getGroupCount() {
			
			return mGroup.size();//
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// ÿ��group�еĺ��Ӽ���
			return mGroup.get(groupPosition).childList.size();
		}

		@Override
		public Group getGroup(int groupPosition) {
			//���ݽǱ��ȡgroup����
			return mGroup.get(groupPosition);
		}

		@Override
		public Child getChild(int groupPosition, int childPosition) {
			// ���ݽǱ��ȡchild����
			return mGroup.get(groupPosition).childList.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			//�����ôд
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			//
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// ���view������ʾgroupView
			TextView textView = new TextView(getApplicationContext());
			//�����ĸ���
			textView.setText("			"+getGroup(groupPosition).name);
			textView.setTextColor(Color.RED);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);//dip����dp
			textView.setPadding(40, 20, 20, 20);
			return textView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// 
			View view = View.inflate(getApplicationContext(), R.layout.elv_child_item, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
			//��ֵ
			tv_name.setText(getChild(groupPosition, childPosition).name);
			tv_number.setText(getChild(groupPosition, childPosition).number);
			return view;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			//�ǲ�����ĺ��ӽڵ��ܷ�ѡ��
			//���Ҫ���õ���¼�,��Ȼ��Ҫ��ѡ��,��ѡ�оͲ��ܱ����
			return true;
		}
		
	}

}

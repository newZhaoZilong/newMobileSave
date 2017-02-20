package com.shange.mobilesave.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.shange.mobilesave.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListActivity extends Activity {
	
	protected static final String tag = "ContactListActivity";
	private ListView lv_contact;
	private ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();//ΪʲôҪ���������,����Ϊ��Ϣ������?
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//8,�������������
			//������,Ӧ����ֻҪ������Ϣ,�ͻ�����������,ֻ��һ������,���Բ���Ҫ�ж�
			MyAdapter myAdapter = new MyAdapter();
			
			Log.i(tag,"handler������һ��");
			lv_contact.setAdapter(myAdapter);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		//���ʻ�UI
		initUI();
		//��ʼ������
		initData();
		
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public HashMap<String,String> getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//����view����
			View  view= null;
			if(convertView!=null){
				view = convertView;//����
			}else{
				view = View.inflate(getApplicationContext(), R.layout.listview_contact_item, null);
			}
			
			//�ҵ��ؼ�
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_phone = (TextView) view.findViewById(R.id.tv_setover_phone);
			
			//���ؼ���ֵ
			tv_name.setText(arrayList.get(position).get("name"));
			tv_phone.setText(arrayList.get(position).get("phone"));
			return view;
		}
		
	}

	private void initData() {
		
		//�������ṩ���л�ȡ����
		new Thread(){
			

			public void run(){
				
				//1,��ȡ���ݽ���������
				ContentResolver contentResolver = getContentResolver();
				//2,����ѯ��ϵ�����ݿ�����(��ȡ��ϵ��Ȩ��)
				//uri:��ַprojection:��ѯ������ selection:��ѯ����, selectionArgs:��ѯ�����Ĳ���, sortOrder:������
				/*Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
						new String[]{"contact_id"}, null, null, null);*/
				//����cursor
				//3.ѭ���α�,֪��û������Ϊֹ
			//	while (cursor.moveToNext()) {//�ƶ�����һ��
			//		String id = cursor.getString(0);
				//	Log.i(tag,"id ="+id);//����˵raw_contacts���е�contact_id��data���е�raw_contacts_id��һһ��Ӧ��
					//��ѯdata��
					//4,�����û�Ψһ��idֵ,��ѯdata���mimetype�����ɵ���ͼ,��ȡdata�Լ�mimetype�ֶ�
					/*Cursor cursor2 = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
							new String[]{"data1","mimetype"} , "raw_contact_id = ?", new String[]{id}, null);//���ݶ�Ӧ��raw_contact_id��ȡ����
					//5.ѭ����ȡÿһ����ϵ�˵ĵ绰�����Լ�����,��������
					while(cursor2.moveToNext()){
						Log.i(tag,"data = "+cursor2.getString(0));
						Log.i(tag,"mimetype = "+cursor2.getString(1));
						
					}*/
				Cursor cursor1 = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
						new String[]{"raw_contact_id"} , null, null, null);//���ݶ�Ӧ��ra
				
				HashSet<String> hashSet = new HashSet<String>();
				while(cursor1.moveToNext()){
					String raw_contact_id = cursor1.getString(0);
					hashSet.add(raw_contact_id);
				}
				cursor1.close();//cursor�ǵ�Ҫ��ʱ�ر�
				arrayList.clear();//���List; 
				
				for(String id : hashSet){
					Cursor cursor2 = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
							new String[]{"data1","mimetype"} , "raw_contact_id = ?", new String[]{id}, null);//���ݶ�Ӧ��raw_contact_id��ȡ����
					//5.ѭ����ȡÿһ����ϵ�˵ĵ绰�����Լ�����,��������
					HashMap<String,String> hashMap = new HashMap<String, String>();//ûѭ��һ��id�ʹ���һ��HashMap
					while(cursor2.moveToNext()){
						String data = cursor2.getString(0);				
						String type = cursor2.getString(1);
						//02-17 01:15:50.670: I/ContactListActivity(28641): raw_contact_id = 56==> data1 = 4001809660==> mimetype = vnd.android.cursor.item/phone_v2

						if(type.equals("vnd.android.cursor.item/phone_v2")){
							if(!TextUtils.isEmpty(data)){
								hashMap.put("phone", data);
							}
						}else if(type.equals("vnd.android.cursor.item/name")){
							if(!TextUtils.isEmpty(data)){
								hashMap.put("name", data);
							}
						}
					}
					//��HashMap��ӵ�ArrayList��ȥ
					arrayList.add(hashMap);
					//System.out.println(hashMap.toString());
					cursor2.close();//cursor�ǵ�Ҫ��ʱ�ر�
				}//forѭ������,˵��������,�������̲߳��ܸ���UI,������Ҫ�õ���Ϣ����
				//7,����һ���յ���Ϣ,��֪���߳̿���ȥʹ�����߳��Ѿ����õ�����
			mHandler.sendEmptyMessage(0);
					
				}
			//	cursor.close();
		//	}
		}.start();
		
	}

	private void initUI() {
		lv_contact = (ListView) findViewById(R.id.lv_contact);
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//��ȡ������Ŀ������ָ���еĶ���
				//1������ȡ�þʹ��Ķ�����
				if(arrayList.size()>0){
					String phone = arrayList.get(position).get("phone");
					//2�˵绰��Ҫ����������������ʹ��
					//4,�ڽ����˽���ص�ǰһ�����������ʱ��,��Ҫ�����ݷ��ع�ȥ
					Intent intent = new Intent();
					intent.putExtra("phone", phone);
					setResult(2,intent);
					finish();
				}
				
				
				
			}
			
			
		});
		
	}
	
}

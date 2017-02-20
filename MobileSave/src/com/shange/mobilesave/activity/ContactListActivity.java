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
	private ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();//为什么要在这儿创建,是因为消息机制吗?
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//8,填充数据适配器
			//明白了,应该是只要发了消息,就会调用这个方法,只有一钟需求,所以不需要判断
			MyAdapter myAdapter = new MyAdapter();
			
			Log.i(tag,"handler到了这一步");
			lv_contact.setAdapter(myAdapter);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		//舒适化UI
		initUI();
		//初始化数据
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
			//创建view对象
			View  view= null;
			if(convertView!=null){
				view = convertView;//复用
			}else{
				view = View.inflate(getApplicationContext(), R.layout.listview_contact_item, null);
			}
			
			//找到控件
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_phone = (TextView) view.findViewById(R.id.tv_setover_phone);
			
			//给控件赋值
			tv_name.setText(arrayList.get(position).get("name"));
			tv_phone.setText(arrayList.get(position).get("phone"));
			return view;
		}
		
	}

	private void initData() {
		
		//从内容提供者中获取数据
		new Thread(){
			

			public void run(){
				
				//1,获取内容解析器对象
				ContentResolver contentResolver = getContentResolver();
				//2,做查询联系人数据库表过程(读取联系人权限)
				//uri:地址projection:查询的列名 selection:查询条件, selectionArgs:查询条件的参数, sortOrder:正序倒序
				/*Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
						new String[]{"contact_id"}, null, null, null);*/
				//遍历cursor
				//3.循环游标,知道没有数据为止
			//	while (cursor.moveToNext()) {//移动到下一行
			//		String id = cursor.getString(0);
				//	Log.i(tag,"id ="+id);//按理说raw_contacts表中的contact_id和data表中的raw_contacts_id是一一对应的
					//查询data表
					//4,根据用户唯一性id值,查询data表和mimetype表生成的视图,获取data以及mimetype字段
					/*Cursor cursor2 = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
							new String[]{"data1","mimetype"} , "raw_contact_id = ?", new String[]{id}, null);//根据对应的raw_contact_id获取两列
					//5.循环获取每一个联系人的电话号码以及姓名,数据类型
					while(cursor2.moveToNext()){
						Log.i(tag,"data = "+cursor2.getString(0));
						Log.i(tag,"mimetype = "+cursor2.getString(1));
						
					}*/
				Cursor cursor1 = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
						new String[]{"raw_contact_id"} , null, null, null);//根据对应的ra
				
				HashSet<String> hashSet = new HashSet<String>();
				while(cursor1.moveToNext()){
					String raw_contact_id = cursor1.getString(0);
					hashSet.add(raw_contact_id);
				}
				cursor1.close();//cursor记得要及时关闭
				arrayList.clear();//清空List; 
				
				for(String id : hashSet){
					Cursor cursor2 = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
							new String[]{"data1","mimetype"} , "raw_contact_id = ?", new String[]{id}, null);//根据对应的raw_contact_id获取两列
					//5.循环获取每一个联系人的电话号码以及姓名,数据类型
					HashMap<String,String> hashMap = new HashMap<String, String>();//没循环一个id就创建一个HashMap
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
					//将HashMap添加到ArrayList中去
					arrayList.add(hashMap);
					//System.out.println(hashMap.toString());
					cursor2.close();//cursor记得要及时关闭
				}//for循环结束,说明填充完毕,但是子线程不能更新UI,所以需要用到消息机制
				//7,发送一个空的消息,告知主线程可以去使用子线程已经填充好的数据
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
				//获取点中条目的索引指向集中的对象
				//1从哪里取得就从哪儿里获得
				if(arrayList.size()>0){
					String phone = arrayList.get(position).get("phone");
					//2此电话需要给第三个导航界面使用
					//4,在结束此界面回到前一个导航界面的时候,需要将数据返回过去
					Intent intent = new Intent();
					intent.putExtra("phone", phone);
					setResult(2,intent);
					finish();
				}
				
				
				
			}
			
			
		});
		
	}
	
}

package com.shange.mobilesave.db.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shange.mobilesave.db.BlackNumberOpenHelper;
import com.shange.mobilesave.db.domain.BlackNumberInfo;

public class BlackNumberDao {
	private static final String TABLENAME = "blacknumber";
	//BlackNumberDao����ģʽ
	//1,˽�л����췽��
	private BlackNumberOpenHelper blackNumberOpenHelper ;
	//2,����һ����ǰ��Ķ���

	private BlackNumberDao(Context context){
		//�������ݿ��Լ����ṹ
		blackNumberOpenHelper = new BlackNumberOpenHelper(context);

	}
	private static BlackNumberDao blackNumberDao = null;
	//3,�ṩһ������,�����ǰ��Ķ���Ϊ��,����һ���µ�
	public static BlackNumberDao getInstance(Context context){
		if(blackNumberDao == null){
			blackNumberDao = new BlackNumberDao(context);
		}
		return blackNumberDao;
	}
	//����ģʽ��ʲô��?

	/**����һ����Ŀ
	 * @param phone ���صĵ绰����
	 * @param mode ��������(1:����  2:�绰  3:��������(����+�绰))
	 */
	public void insert(String phone,String mode){
		//1,�������ݿ�,�汻��д�����
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put("phone", phone);
		values.put("mode", mode);
		/*//
		int iphone = Integer.parseInt(phone);
		//ֱ�Ӳ�����100��
		for(int i=0 ;i <100 ; i++){
			
			values.put("phone", (iphone+i)+"");
			db.insert(TABLENAME, null, values);
		}*/
		db.insert(TABLENAME, null, values);
		db.close();

	}
	/**
	 * @param phone Ҫɾ���ĵ绰����
	 */
	public void delete(String phone){

		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		//Ҫɾ����Ȼ������ɾ����,ɵ��
		db.delete(TABLENAME, "phone = ?", new String[]{phone});

		db.close();

	}
	/**���ݵ绰,��������ģʽ
	 * @param phone Ҫ��������ģʽ�ĵ绰����
	 * @param mode Ҫ���µ�ģʽ(1:����  2:�绰  3��������(����+�绰)
	 */
	public void update(String phone,String mode){

		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);

		db.update(TABLENAME, values, "phone = ?", new String[]{phone});
		db.close();
	}
	/**
	 * ��ѯ����
	 * @param phone
	 */
	public ArrayList<BlackNumberInfo> findAll(){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		
		Cursor cursor = db.query(TABLENAME, new String[]{"phone", "mode"}, null, null, null,null, "_id desc");
		
		ArrayList<BlackNumberInfo> blackNumberList = new ArrayList<BlackNumberInfo>();
		while(cursor.moveToNext()){
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.phone = cursor.getString(0);
			blackNumberInfo.mode = cursor.getString(1);
			
			blackNumberList.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		
		return blackNumberList;
	}
	/**
	 * ÿ�β�ѯ20������
	 * @param index ��ѯ������ֵ
	 * @return ���鼯��
	 */
	public ArrayList<BlackNumberInfo> find(int index){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("select phone,mode from blacknumber order by _id desc limit ?,20;", new String[]{index+""});
		ArrayList<BlackNumberInfo> blackNumberList = new ArrayList<BlackNumberInfo>();
		while(cursor.moveToNext()){
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.phone = cursor.getString(0);
			blackNumberInfo.mode = cursor.getString(1);
			
			blackNumberList.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		
		return blackNumberList;
	}
	
	/**��ȡ����Ŀ��
	 * @return ��������Ŀ��
	 */
	public int getCount(){
	SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		int count = 0;
		while(cursor.moveToNext()){
			
			count = cursor.getInt(0);
			
		}
		cursor.close();
		db.close();
		
		return count;
	}
	/**
	 * ��ѯ�����������ģʽ
	 * @param phone Ҫ��ѯ�ĺ���
	 * @return	����ģʽ,1,���� 2,�绰,3���� 0,û�в鵽,���Ǻ�����
	 */
	public int getMode(String phone){
	SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		
	Cursor cursor = db.query(TABLENAME, new String[]{"mode"}, "phone = ?", new String[]{phone}, null,null, null);
	int mode = 0;
		if(cursor.moveToNext()){
			mode = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		
		return mode;
	}
}

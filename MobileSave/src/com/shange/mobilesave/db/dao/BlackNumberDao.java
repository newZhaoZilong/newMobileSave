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
	//BlackNumberDao单例模式
	//1,私有化构造方法
	private BlackNumberOpenHelper blackNumberOpenHelper ;
	//2,声明一个当前类的对象

	private BlackNumberDao(Context context){
		//创建数据库以及其表结构
		blackNumberOpenHelper = new BlackNumberOpenHelper(context);

	}
	private static BlackNumberDao blackNumberDao = null;
	//3,提供一个方法,如果当前类的对象为空,创建一个新的
	public static BlackNumberDao getInstance(Context context){
		if(blackNumberDao == null){
			blackNumberDao = new BlackNumberDao(context);
		}
		return blackNumberDao;
	}
	//单例模式有什么用?

	/**增加一个条目
	 * @param phone 拦截的电话号码
	 * @param mode 拦截类型(1:短信  2:电话  3:拦截所有(短信+电话))
	 */
	public void insert(String phone,String mode){
		//1,开启数据库,真被做写入操作
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put("phone", phone);
		values.put("mode", mode);
		/*//
		int iphone = Integer.parseInt(phone);
		//直接插它个100条
		for(int i=0 ;i <100 ; i++){
			
			values.put("phone", (iphone+i)+"");
			db.insert(TABLENAME, null, values);
		}*/
		db.insert(TABLENAME, null, values);
		db.close();

	}
	/**
	 * @param phone 要删除的电话号码
	 */
	public void delete(String phone){

		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		//要删除当然是整行删除了,傻吊
		db.delete(TABLENAME, "phone = ?", new String[]{phone});

		db.close();

	}
	/**根据电话,更新拦截模式
	 * @param phone 要更新拦截模式的电话号码
	 * @param mode 要更新的模式(1:短信  2:电话  3拦截所有(短信+电话)
	 */
	public void update(String phone,String mode){

		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);

		db.update(TABLENAME, values, "phone = ?", new String[]{phone});
		db.close();
	}
	/**
	 * 查询所有
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
	 * 每次查询20条数据
	 * @param index 查询的索引值
	 * @return 数组集合
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
	
	/**获取总条目数
	 * @return 返回总条目数
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
	 * 查询黑名单号码的模式
	 * @param phone 要查询的号码
	 * @return	返回模式,1,短信 2,电话,3所有 0,没有查到,不是黑名单
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

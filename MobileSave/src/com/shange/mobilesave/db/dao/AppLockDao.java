package com.shange.mobilesave.db.dao;



import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.shange.mobilesave.db.AppLockOpenHelper;
import com.shange.mobilesave.db.BlackNumberOpenHelper;


public class AppLockDao {
	//将表名常量化,懒得敲
	private static final String TABLENAME = "applock";
	//BlackNumberDao单例模式
	//1,私有化构造方法
	private AppLockOpenHelper appLockOpenHelper ;
	//2,声明一个当前类的对象
	private Context context;
	private AppLockDao(Context context){
		//创建数据库以及其表结构
		this.context = context;
		appLockOpenHelper = new AppLockOpenHelper(context);

	}
	private static AppLockDao appLockDao = null;
	//3,提供一个方法,如果当前类的对象为空,创建一个新的
	public static AppLockDao getInstance(Context context){
		if(appLockDao == null){
			appLockDao = new AppLockDao(context);
		}
		return appLockDao;
	}
	//单例模式有什么用?	
	//插入方法
	/**
	 * 根据包名插入锁定的应用包名
	 * @param packagename
	 */
	public void insert(String packagename){
		SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();

		contentValues.put("packagename", packagename);

		db.insert(TABLENAME, null, contentValues);

		db.close();
		//注册一条内容观察者,必须是cotent://开头,不然会出错,后边的名字随便,那么解析器是根据什么判断applock表发生变化的呢?
		//写在这里就有效,其他地方无效,必须有数据库操作才有效
		context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);//ontent://applock/change
	}
	//删除方法
	/**
	 * 根据包名删除锁定的程序
	 * @param packagename
	 */
	public void delete(String packagename){

		SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();

		contentValues.put(TABLENAME, packagename);

		db.delete(TABLENAME, "packagename = ?", new String[]{packagename});

		db.close();
		//设置一条通知,执行了就会发送通知给内容观察者
		context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);//ontent://applock/change
	}
	//查询所有
	/**
	 * 查询所有锁定的应用包名
	 * @return 返回lock的packagename集合
	 */
	public List<String> findAll(){

		SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();

		Cursor cursor = db.query(TABLENAME,new String[]{"packagename"},null,null,null,null,null);

		ArrayList<String> lockPackageList = new ArrayList<String>();

		while(cursor.moveToNext()){

			lockPackageList.add(cursor.getString(0));

		}
		cursor.close();

		db.close();

		return lockPackageList;

	}


}

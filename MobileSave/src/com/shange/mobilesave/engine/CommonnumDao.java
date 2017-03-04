package com.shange.mobilesave.engine;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonnumDao {

	//1.指定访问数据库的路径
	public static String path = "data/data/com.shange.mobilesave/files/commonnum.db" ;
	//2,开启数据(组)
	public List<Group> getGroup(){

		ArrayList<Group> list = new ArrayList<Group>();
		//获得数据库对象
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		//查询所有
		Cursor cursor = db.query("classlist", null, null, null, null, null, null);

		while(cursor.moveToNext()){
			Group group = new Group();
			group.name = cursor.getString(0);
			group.idx = cursor.getString(1);
			group.childList = getChild(db,group.idx);
			list.add(group);
		}
		cursor.close();
		db.close();
		return list;
	}
	/**
	 * @param idx 表名的后缀数字
	 * 获取每一个组中孩子节点的数据
	 */
	public List<Child> getChild(SQLiteDatabase db, String idx){

		ArrayList<Child> list = new ArrayList<Child>();
		//获得数据库对象
		//	SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		//查询所有
		//	Cursor cursor = db.query(tablename, null, null, null, null, null, null);
		Cursor cursor = db.rawQuery("select * from table"+idx+";", null);

		while(cursor.moveToNext()){
			Child child = new Child();
			child._id = cursor.getString(0);//这里明明是integer类型的
			child.number = cursor.getString(1);
			child.name = cursor.getString(2);
			list.add(child);

		}
		cursor.close();
		//	db.close();
		return list;
	}


	/**
	 * @author 山哥
	 *javabean
	 */
	public class Group{
		public	String name;
		public	String idx;//数据库中一般都是varChar类型
		public	List<Child> childList;

	}
	public	class Child{

		public	String _id;
		public	String number;
		public	String name;

	}

}

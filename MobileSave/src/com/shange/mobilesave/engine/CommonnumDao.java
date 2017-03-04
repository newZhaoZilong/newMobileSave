package com.shange.mobilesave.engine;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonnumDao {

	//1.ָ���������ݿ��·��
	public static String path = "data/data/com.shange.mobilesave/files/commonnum.db" ;
	//2,��������(��)
	public List<Group> getGroup(){

		ArrayList<Group> list = new ArrayList<Group>();
		//������ݿ����
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		//��ѯ����
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
	 * @param idx �����ĺ�׺����
	 * ��ȡÿһ�����к��ӽڵ������
	 */
	public List<Child> getChild(SQLiteDatabase db, String idx){

		ArrayList<Child> list = new ArrayList<Child>();
		//������ݿ����
		//	SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		//��ѯ����
		//	Cursor cursor = db.query(tablename, null, null, null, null, null, null);
		Cursor cursor = db.rawQuery("select * from table"+idx+";", null);

		while(cursor.moveToNext()){
			Child child = new Child();
			child._id = cursor.getString(0);//����������integer���͵�
			child.number = cursor.getString(1);
			child.name = cursor.getString(2);
			list.add(child);

		}
		cursor.close();
		//	db.close();
		return list;
	}


	/**
	 * @author ɽ��
	 *javabean
	 */
	public class Group{
		public	String name;
		public	String idx;//���ݿ���һ�㶼��varChar����
		public	List<Child> childList;

	}
	public	class Child{

		public	String _id;
		public	String number;
		public	String name;

	}

}

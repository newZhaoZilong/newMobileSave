package com.shange.mobilesave.db;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class BlackNumberOpenHelper extends SQLiteOpenHelper {

	public BlackNumberOpenHelper(Context context) {
		super(context, Environment.getExternalStorageDirectory()+File.separator+"blacknumber.db", null, 1);
		System.out.println( Environment.getExternalStorageDirectory()+File.separator+"blacknumber.db");
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//�������ݿ��б�ķ���
		db.execSQL("create table blacknumber(_id integer primary key autoincrement,phone varchar(20),mode varchar(5));");
		System.out.println("�������ݿⷽ��");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

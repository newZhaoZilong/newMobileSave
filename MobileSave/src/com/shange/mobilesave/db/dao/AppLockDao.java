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
	//������������,������
	private static final String TABLENAME = "applock";
	//BlackNumberDao����ģʽ
	//1,˽�л����췽��
	private AppLockOpenHelper appLockOpenHelper ;
	//2,����һ����ǰ��Ķ���
	private Context context;
	private AppLockDao(Context context){
		//�������ݿ��Լ����ṹ
		this.context = context;
		appLockOpenHelper = new AppLockOpenHelper(context);

	}
	private static AppLockDao appLockDao = null;
	//3,�ṩһ������,�����ǰ��Ķ���Ϊ��,����һ���µ�
	public static AppLockDao getInstance(Context context){
		if(appLockDao == null){
			appLockDao = new AppLockDao(context);
		}
		return appLockDao;
	}
	//����ģʽ��ʲô��?	
	//���뷽��
	/**
	 * ���ݰ�������������Ӧ�ð���
	 * @param packagename
	 */
	public void insert(String packagename){
		SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();

		contentValues.put("packagename", packagename);

		db.insert(TABLENAME, null, contentValues);

		db.close();
		//ע��һ�����ݹ۲���,������cotent://��ͷ,��Ȼ�����,��ߵ��������,��ô�������Ǹ���ʲô�ж�applock�����仯����?
		//д���������Ч,�����ط���Ч,���������ݿ��������Ч
		context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);//ontent://applock/change
	}
	//ɾ������
	/**
	 * ���ݰ���ɾ�������ĳ���
	 * @param packagename
	 */
	public void delete(String packagename){

		SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();

		contentValues.put(TABLENAME, packagename);

		db.delete(TABLENAME, "packagename = ?", new String[]{packagename});

		db.close();
		//����һ��֪ͨ,ִ���˾ͻᷢ��֪ͨ�����ݹ۲���
		context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);//ontent://applock/change
	}
	//��ѯ����
	/**
	 * ��ѯ����������Ӧ�ð���
	 * @return ����lock��packagename����
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

package com.shange.mobilesave.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class AddressDao {
	//1,ָ���������ݿ��·��
	public static String path = "data/data/com.shange.mobilesave/files/address.db";
	public static String mAddress = "δ֪����";
	

	/**
	 * ����һ���绰����,�������ݿ�����,���з���,����һ��������
	 * @param phone ��ѯ�绰����
	 */
	public static String getAddress(String phone){
		//�����ݿ�,����Ϊֻ��,�������Ҫ����,����opendatabase,��Ҫ�������ݿ�,����Ҫdatabaseopenhelper
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		mAddress =  "δ֪����";
		//������ʽ
		String regularExpression = "^1[3-8]\\d{9}";
		if(phone.matches(regularExpression)){//�ж�11λƥ�䲻ƥ��
			phone = phone.substring(0,7);
			

			//3,���ݲ�ѯ
			//�Ȳ�data1���и���id��outkey
			Cursor outkey_cursor = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
			if(outkey_cursor.moveToNext()){//id��Ψһ��,����Ҫôû��,Ҫôֻ��һ��
				//�����ǲ鵽Ϊֹ,������if
				String outkey = outkey_cursor.getString(0);
				//��õ�outkey��,��outkey����id��ѯ��data2�е�location
				Cursor location_cursor = db.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);

				if(location_cursor.moveToNext()){
					//��ȡ��ַ
					String location = location_cursor.getString(0);
					//���ص�ַ
					mAddress = location;
				}else{
					mAddress =  "δ֪����";
				}
				location_cursor.close();
			}
			outkey_cursor.close();
		}else{
			int length = phone.length();
			switch (length) {
			case 3:
				mAddress =  "�����绰";
				break;
			case 4:
				mAddress =  "ģ����";
				break;
			case 5:
				mAddress =  "����绰";
				break;
			case 7:
				mAddress =  "���ص绰";
				break;
			case 8:
				mAddress =  "���ص绰";
				break;
			case 11:
				//(3+8)����+��������(���),��ѯdata2
				String area = phone.substring(1,3);
				Cursor location_cursor = db.query("data2", new String[]{"location"}, "id = ?", new String[]{area}, null, null, null);
				if(location_cursor.moveToNext()){
					//��ȡ��ַ
					String location = location_cursor.getString(0);
					//���ص�ַ
					mAddress =  location;
				}else{
					mAddress =  "δ֪����";
				}
				location_cursor.close();
				break;
			case 12:
				String area2 = phone.substring(1,4);
				Cursor location_cursor2 = db.query("data2", new String[]{"location"}, "id = ?", new String[]{area2}, null, null, null);
				if(location_cursor2.moveToNext()){
					//��ȡ��ַ
					String location = location_cursor2.getString(0);
					//���ص�ַ
					mAddress =  location;
				}else{
					mAddress =  "δ֪����";
				}
				location_cursor2.close();
				break;

			default:
				break;
			}
		}
		return mAddress;
	}

}

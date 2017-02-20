package com.shange.mobilesave.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class AddressDao {
	//1,指定访问数据库的路径
	public static String path = "data/data/com.shange.mobilesave/files/address.db";
	public static String mAddress = "未知号码";
	

	/**
	 * 传递一个电话号码,开启数据库连接,进行访问,返回一个归属地
	 * @param phone 查询电话号码
	 */
	public static String getAddress(String phone){
		//打开数据库,设置为只读,如果不需要创建,就用opendatabase,需要创建数据库,就需要databaseopenhelper
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		mAddress =  "未知号码";
		//正则表达式
		String regularExpression = "^1[3-8]\\d{9}";
		if(phone.matches(regularExpression)){//判断11位匹配不匹配
			phone = phone.substring(0,7);
			

			//3,数据查询
			//先查data1表中根据id查outkey
			Cursor outkey_cursor = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
			if(outkey_cursor.moveToNext()){//id是唯一的,所以要么没有,要么只有一行
				//而且是查到为止,所以用if
				String outkey = outkey_cursor.getString(0);
				//获得到outkey中,将outkey当做id查询表data2中的location
				Cursor location_cursor = db.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);

				if(location_cursor.moveToNext()){
					//获取地址
					String location = location_cursor.getString(0);
					//返回地址
					mAddress = location;
				}else{
					mAddress =  "未知号码";
				}
				location_cursor.close();
			}
			outkey_cursor.close();
		}else{
			int length = phone.length();
			switch (length) {
			case 3:
				mAddress =  "报警电话";
				break;
			case 4:
				mAddress =  "模拟器";
				break;
			case 5:
				mAddress =  "服务电话";
				break;
			case 7:
				mAddress =  "本地电话";
				break;
			case 8:
				mAddress =  "本地电话";
				break;
			case 11:
				//(3+8)区号+座机号码(外地),查询data2
				String area = phone.substring(1,3);
				Cursor location_cursor = db.query("data2", new String[]{"location"}, "id = ?", new String[]{area}, null, null, null);
				if(location_cursor.moveToNext()){
					//获取地址
					String location = location_cursor.getString(0);
					//返回地址
					mAddress =  location;
				}else{
					mAddress =  "未知号码";
				}
				location_cursor.close();
				break;
			case 12:
				String area2 = phone.substring(1,4);
				Cursor location_cursor2 = db.query("data2", new String[]{"location"}, "id = ?", new String[]{area2}, null, null, null);
				if(location_cursor2.moveToNext()){
					//获取地址
					String location = location_cursor2.getString(0);
					//返回地址
					mAddress =  location;
				}else{
					mAddress =  "未知号码";
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

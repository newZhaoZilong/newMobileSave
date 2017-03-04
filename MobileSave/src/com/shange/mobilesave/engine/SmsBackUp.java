package com.shange.mobilesave.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.shange.mobilesave.db.dao.BlackNumberDao;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import android.widget.ProgressBar;

public class SmsBackUp {

	//备份短信方法
	//ProgressDialog
	public static void backup(Context context,String path,CallBack pd){
		//需要用到的对象上下文环境,备份文件夹路径,进度条所在的对话框对象用于备份过程中进度的更新
		//如何备份短信呢
		//1.先创建个File文件对象,用来备份短信


		File file = new File(path);//Environment.getExternalStorageDirectory(), "sms.xml"

		//然后呢,短信从数据库中
		//2获取内容解析器
		ContentResolver contentResolver = context.getContentResolver();

		Cursor cursor = contentResolver.query(Uri.parse("content://sms/"),
				new String[]{"address","date","type","body"}, null, null,null );
		//设置短信数量为进度条最大值
		if(pd!=null){
			pd.setMax(cursor.getCount());
		}		

		int index = 0;
		FileOutputStream fos = null;
		try {
			//3,文件相应的输出流
			fos = new FileOutputStream(file);
			//4,序列化数据库中读取的数据,放置到xml中
			XmlSerializer newSerializer = Xml.newSerializer();
			//设置输出流
			newSerializer.setOutput(fos, "utf-8");
			//DTD(xml规范)
			newSerializer.startDocument("utf-8", true);
			//根节点
			newSerializer.startTag(null, "smss");


			while(cursor.moveToNext()){
				//开始创建短信了
				newSerializer.startTag(null, "sms");

				newSerializer.startTag(null, "address");				
				newSerializer.text(cursor.getString(0));
				newSerializer.endTag(null, "address");

				newSerializer.startTag(null, "date");				
				newSerializer.text(cursor.getString(1));
				newSerializer.endTag(null, "date");

				newSerializer.startTag(null, "type");				
				newSerializer.text(cursor.getString(2));
				newSerializer.endTag(null, "type");

				newSerializer.startTag(null, "body");				
				newSerializer.text(cursor.getString(3));
				newSerializer.endTag(null, "body");

				newSerializer.endTag(null, "sms");
				//每备份一条短信就+1
				index++;

				Thread.sleep(20);
				//更新进度条,设置进度
				if(pd!=null){
					pd.setProgress(index);
				}				
			}

			newSerializer.endTag(null, "smss");
			//结束文件
			newSerializer.endDocument();


		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(cursor!=null){
				cursor.close();
			}

		}
	}
	/**
	 * @author 山哥
	 *定义了一个接口,需要实现setMax,setProgress两个方法
	 *适用于progressBar和progressDialog
	 */
	public interface CallBack{
		//短信总数设置为实现方法(有自己决定是否用对话框.setMax(max)还是用 进度条.setMax(max));
		public void setMax(int max);
		//备份过程中短信百分比更新(由自己决定是用对话框.setProgress(max) 还是用 进度条.setProgress(max))
		public void setProgress(int progress);
	}

}


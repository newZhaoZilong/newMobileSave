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

	//���ݶ��ŷ���
	//ProgressDialog
	public static void backup(Context context,String path,CallBack pd){
		//��Ҫ�õ��Ķ��������Ļ���,�����ļ���·��,���������ڵĶԻ���������ڱ��ݹ����н��ȵĸ���
		//��α��ݶ�����
		//1.�ȴ�����File�ļ�����,�������ݶ���


		File file = new File(path);//Environment.getExternalStorageDirectory(), "sms.xml"

		//Ȼ����,���Ŵ����ݿ���
		//2��ȡ���ݽ�����
		ContentResolver contentResolver = context.getContentResolver();

		Cursor cursor = contentResolver.query(Uri.parse("content://sms/"),
				new String[]{"address","date","type","body"}, null, null,null );
		//���ö�������Ϊ���������ֵ
		if(pd!=null){
			pd.setMax(cursor.getCount());
		}		

		int index = 0;
		FileOutputStream fos = null;
		try {
			//3,�ļ���Ӧ�������
			fos = new FileOutputStream(file);
			//4,���л����ݿ��ж�ȡ������,���õ�xml��
			XmlSerializer newSerializer = Xml.newSerializer();
			//���������
			newSerializer.setOutput(fos, "utf-8");
			//DTD(xml�淶)
			newSerializer.startDocument("utf-8", true);
			//���ڵ�
			newSerializer.startTag(null, "smss");


			while(cursor.moveToNext()){
				//��ʼ����������
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
				//ÿ����һ�����ž�+1
				index++;

				Thread.sleep(20);
				//���½�����,���ý���
				if(pd!=null){
					pd.setProgress(index);
				}				
			}

			newSerializer.endTag(null, "smss");
			//�����ļ�
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
	 * @author ɽ��
	 *������һ���ӿ�,��Ҫʵ��setMax,setProgress��������
	 *������progressBar��progressDialog
	 */
	public interface CallBack{
		//������������Ϊʵ�ַ���(���Լ������Ƿ��öԻ���.setMax(max)������ ������.setMax(max));
		public void setMax(int max);
		//���ݹ����ж��Űٷֱȸ���(���Լ��������öԻ���.setProgress(max) ������ ������.setProgress(max))
		public void setProgress(int progress);
	}

}


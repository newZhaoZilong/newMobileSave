package com.shange.mobilesave.utils;

import android.os.AsyncTask;
import android.os.Handler;

//����:������չ��
//����1:���߳�ִ��������Ĳ���
//����2:ִ�еĽ���
//����3:���߳�ִ���з��صĽ��
//��ʵ�Ͼ�ִ�����������߳̽��е�,ִ��ǰ�ͺ��������߳�
//�ٶ�������:����ִ�е����ٸ�������ʱ��ͺ�newһ���߳�û������,5��
/*public void show(){
		new AsyncTask<String, Integer, String>() {

			//���߳�֮ǰִ�еķ���
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			//�����߳�֮��ִ�еķ���
			@Override
			protected String doInBackground(String... params) {

				return null;
			}
			//�����߳�֮��ִ�еķ���
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}
			//��ʾ��ǰ���صĽ���
			@Override
			protected void onProgressUpdate(Integer... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
			}

		};

	}*/

public abstract class MyAsyncTask {
	
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			//֮��
			postTask();
		};
	};
	/**
	 * �����߳�֮ǰִ�еķ���
	 */
	public abstract void preTask();
	/**
	 * �����߳�֮��ִ�еķ���
	 */
	public abstract void doinBack();
	/**
	 * �����߳�֮��ִ�еķ���
	 */
	public abstract void postTask();

	
	public void execute(){
		//֮ǰ
		preTask();
		
		new Thread(){
			
			public void run() {
				//֮��	
				doinBack();				
				//������Ϣ
				handler.sendEmptyMessage(0);
			};
			
		}.start();
	}	
}

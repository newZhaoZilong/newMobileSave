package com.shange.mobilesave.utils;

import android.os.AsyncTask;
import android.os.Handler;

//参数:调高扩展性
//参数1:子线程执行中所需的参数
//参数2:执行的进度
//参数3:子线程执行中返回的结果
//事实上就执行中是在子线程进行的,执行前和后都是在主线程
//百度面试题:当他执行到多少个操作的时候就和new一个线程没区别了,5个
/*public void show(){
		new AsyncTask<String, Integer, String>() {

			//子线程之前执行的方法
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			//在子线程之中执行的方法
			@Override
			protected String doInBackground(String... params) {

				return null;
			}
			//在子线程之后执行的方法
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}
			//显示当前加载的进度
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
			//之后
			postTask();
		};
	};
	/**
	 * 在子线程之前执行的方法
	 */
	public abstract void preTask();
	/**
	 * 在子线程之中执行的方法
	 */
	public abstract void doinBack();
	/**
	 * 在子线程之后执行的方法
	 */
	public abstract void postTask();

	
	public void execute(){
		//之前
		preTask();
		
		new Thread(){
			
			public void run() {
				//之中	
				doinBack();				
				//发送消息
				handler.sendEmptyMessage(0);
			};
			
		}.start();
	}	
}

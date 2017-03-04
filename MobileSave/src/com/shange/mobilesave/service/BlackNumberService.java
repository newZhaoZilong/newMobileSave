package com.shange.mobilesave.service;


import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.shange.mobilesave.db.dao.BlackNumberDao;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BlackNumberService extends Service {

	private static final String tag = "BlackNumberService";
	private TelephonyManager mTM;
	private MyPhoneStateListener myPhoneStateListener;
	private BlackNumberReceiver blackNumberReceiver;
	private BlackNumberDao blackNumberDao;
	private MyContentObserver myContentObserver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		blackNumberDao = BlackNumberDao.getInstance(this);
		//1,首先屏蔽短息
		Log.i(tag, "开启blacknumber服务");
		//注册一个广播接受者,然后优先级提到,最高,接收到广播后,abort终止广播
		IntentFilter intentFilter = new IntentFilter();
		//设置优先级
		intentFilter.setPriority(Integer.MAX_VALUE);//我tm是最大值
		//添加事件
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		blackNumberReceiver = new BlackNumberReceiver();
		//注册广播
		registerReceiver(blackNumberReceiver, intentFilter);
		//监听来电状态
		mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		myPhoneStateListener = new MyPhoneStateListener();
		//监听
		mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);//监听状态

	}
	class MyPhoneStateListener extends PhoneStateListener{


		//需要重写呼叫状态改变的方法
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			//
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				//空闲状态,什么时候添加的读取电话状态,不管了

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//接起电话
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//来电话了,根据号码,判断mode,为2或3直接挂断
				endCall(incomingNumber);

				break;
			}
		}

	}
	public void endCall(String phone) {
		int phone_mode = blackNumberDao.getMode(phone);
		if(phone_mode==2||phone_mode==3){
			//挂断电话
			//现在需要获取ServiceManager,要用到反射,只要获取ServiceManager.getService方法就行了
			//首先根据包名获取字节码文件
			try {
				Class<?> clazz = Class.forName("android.os.ServiceManager");
				//获取方法对象
				Method method = clazz.getMethod("getService", String.class);
				//调用方法
				IBinder ibinder = (IBinder) method.invoke(clazz, Context.TELEPHONY_SERVICE);
				//获取ITelephony对象
				ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
				//结束电话
				iTelephony.endCall();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 					

			//	ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
			//6,在内容解析器上,去注册内容观察者,通过内容观察者,观察数据库(Uri决定那张表那个库)的变化
			myContentObserver = new MyContentObserver(new Handler(), phone);
			//通过内容解析器,注册观察者,能匹配的上 ,传true,匹配不上 ,传false
			getContentResolver().registerContentObserver
			(Uri.parse("content://call_log/calls"), true, myContentObserver);
		}	

	}
	class MyContentObserver extends ContentObserver{

		private String phone;
		public MyContentObserver(Handler handler,String phone) {
			super(handler);
			this.phone = phone;
		}
		//数据库中指定calls表发生改变的时候会去调用方法
		@Override
		public void onChange(boolean selfChange) {
			//清除数据库中的数据
			getContentResolver().delete
			(Uri.parse("content://call_log/calls"), "number = ?",new String[]{phone} );

			super.onChange(selfChange);
		}

	}
	class BlackNumberReceiver extends BroadcastReceiver{


		@Override
		public void onReceive(Context context, Intent intent) {
			//判断手机号码,查询mode,有就屏蔽
			//2.获取短信内容居然是从intent中获取的数据
			Object[] object = (Object[]) intent.getExtras().get("pdus");


			//3.循环遍历短信过程,为什么要循环遍历,不就一条短信,难道能一次接受很多条短信吗
			for(Object ob : object){
				//4,获取短信对象
				SmsMessage sms = SmsMessage.createFromPdu((byte[])ob);
				//5,获取短信对象的基本信息
				String originatingAddress = sms.getOriginatingAddress();//发信地址


				//
				int mode = blackNumberDao.getMode(originatingAddress);

				if(mode==1||mode==3){//说明是黑名单,需要屏蔽短信
					//终止广播
					abortBroadcast();
				}

			}	
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(tag, "关闭blacknumber服务");
		//关闭广播
		if(blackNumberReceiver!=null){
			unregisterReceiver(blackNumberReceiver);
		}
		//注销内容观察者
		if(myContentObserver!=null){
			getContentResolver().unregisterContentObserver(myContentObserver);
		}
		//关闭监听器
		if(mTM!=null&&myPhoneStateListener!=null){
			mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}

	}


}

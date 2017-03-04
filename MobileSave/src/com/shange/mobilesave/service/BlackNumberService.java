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
		//1,�������ζ�Ϣ
		Log.i(tag, "����blacknumber����");
		//ע��һ���㲥������,Ȼ�����ȼ��ᵽ,���,���յ��㲥��,abort��ֹ�㲥
		IntentFilter intentFilter = new IntentFilter();
		//�������ȼ�
		intentFilter.setPriority(Integer.MAX_VALUE);//��tm�����ֵ
		//����¼�
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		blackNumberReceiver = new BlackNumberReceiver();
		//ע��㲥
		registerReceiver(blackNumberReceiver, intentFilter);
		//��������״̬
		mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		myPhoneStateListener = new MyPhoneStateListener();
		//����
		mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);//����״̬

	}
	class MyPhoneStateListener extends PhoneStateListener{


		//��Ҫ��д����״̬�ı�ķ���
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			//
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				//����״̬,ʲôʱ����ӵĶ�ȡ�绰״̬,������

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//����绰
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//���绰��,���ݺ���,�ж�mode,Ϊ2��3ֱ�ӹҶ�
				endCall(incomingNumber);

				break;
			}
		}

	}
	public void endCall(String phone) {
		int phone_mode = blackNumberDao.getMode(phone);
		if(phone_mode==2||phone_mode==3){
			//�Ҷϵ绰
			//������Ҫ��ȡServiceManager,Ҫ�õ�����,ֻҪ��ȡServiceManager.getService����������
			//���ȸ��ݰ�����ȡ�ֽ����ļ�
			try {
				Class<?> clazz = Class.forName("android.os.ServiceManager");
				//��ȡ��������
				Method method = clazz.getMethod("getService", String.class);
				//���÷���
				IBinder ibinder = (IBinder) method.invoke(clazz, Context.TELEPHONY_SERVICE);
				//��ȡITelephony����
				ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
				//�����绰
				iTelephony.endCall();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 					

			//	ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
			//6,�����ݽ�������,ȥע�����ݹ۲���,ͨ�����ݹ۲���,�۲����ݿ�(Uri�������ű��Ǹ���)�ı仯
			myContentObserver = new MyContentObserver(new Handler(), phone);
			//ͨ�����ݽ�����,ע��۲���,��ƥ����� ,��true,ƥ�䲻�� ,��false
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
		//���ݿ���ָ��calls�����ı��ʱ���ȥ���÷���
		@Override
		public void onChange(boolean selfChange) {
			//������ݿ��е�����
			getContentResolver().delete
			(Uri.parse("content://call_log/calls"), "number = ?",new String[]{phone} );

			super.onChange(selfChange);
		}

	}
	class BlackNumberReceiver extends BroadcastReceiver{


		@Override
		public void onReceive(Context context, Intent intent) {
			//�ж��ֻ�����,��ѯmode,�о�����
			//2.��ȡ�������ݾ�Ȼ�Ǵ�intent�л�ȡ������
			Object[] object = (Object[]) intent.getExtras().get("pdus");


			//3.ѭ���������Ź���,ΪʲôҪѭ������,����һ������,�ѵ���һ�ν��ܺܶ���������
			for(Object ob : object){
				//4,��ȡ���Ŷ���
				SmsMessage sms = SmsMessage.createFromPdu((byte[])ob);
				//5,��ȡ���Ŷ���Ļ�����Ϣ
				String originatingAddress = sms.getOriginatingAddress();//���ŵ�ַ


				//
				int mode = blackNumberDao.getMode(originatingAddress);

				if(mode==1||mode==3){//˵���Ǻ�����,��Ҫ���ζ���
					//��ֹ�㲥
					abortBroadcast();
				}

			}	
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(tag, "�ر�blacknumber����");
		//�رչ㲥
		if(blackNumberReceiver!=null){
			unregisterReceiver(blackNumberReceiver);
		}
		//ע�����ݹ۲���
		if(myContentObserver!=null){
			getContentResolver().unregisterContentObserver(myContentObserver);
		}
		//�رռ�����
		if(mTM!=null&&myPhoneStateListener!=null){
			mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}

	}


}

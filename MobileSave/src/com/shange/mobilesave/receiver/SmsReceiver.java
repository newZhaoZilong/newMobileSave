package com.shange.mobilesave.receiver;

import com.shange.mobilesave.R;
import com.shange.mobilesave.service.LocationService;
import com.shange.mobilesave.service.LockScreenService;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String tag = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		//1.�ж��ֻ������Ƿ���
		boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
		
		if(open_security){//���������,�Ͳ�������
			//2.��ȡ�������ݾ�Ȼ�Ǵ�intent�л�ȡ������
			Object[] object = (Object[]) intent.getExtras().get("pdus");
			
			
			//3.ѭ���������Ź���
			for(Object ob : object){
				//4,��ȡ���Ŷ���
				SmsMessage sms = SmsMessage.createFromPdu((byte[])ob);
				//5,��ȡ���Ŷ���Ļ�����Ϣ
				String originatingAddress = sms.getOriginatingAddress();//���ŵ�ַ
				String messageBody = sms.getMessageBody();
				//6.�ж��Ƿ�����������ֵĹؼ���
				if(messageBody.contains("#*alarm*#")){
					//7.��������(׼������,MediaPlayer)
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);//�������֪��R�ļ�������
					mediaPlayer.setLooping(true);//����Ϊѭ������
					mediaPlayer.start();//��������
				}
				if(messageBody.contains("#*location*#")){
					//������λ����
					Intent intent2 = new Intent(context,LocationService.class);
					context.startService(intent2);
				}
				if(true){//messageBody.contains("#*lockscreen*#")
					//����Զ����������
					Intent intent3 = new Intent(context,LockScreenService.class);
					context.startService(intent3);
				}
			}
		}
	}

}

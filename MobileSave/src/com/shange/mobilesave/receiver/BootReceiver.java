package com.shange.mobilesave.receiver;

import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private static final String tag = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(tag,"�����ֻ��ɹ�");
		//1.��ȡ�������ֻ���sim�����к�
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//2.��ȡ�ֻ����к�
		String simSerialNumber = tm.getSimSerialNumber();
		//��ȡ�洢�����к�
		String sim_number = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "");
		//3.�ԱȲ�һ��
		if(!simSerialNumber.equals(sim_number)){
			//4,���Ͷ��Ÿ�ѡ����ϵ�˺���
			
			SmsManager smsManager = SmsManager.getDefault();
			String phone = SpUtil.getString(context, ConstantValue.CONTACT_PHONE, "");
			smsManager.sendTextMessage(phone, null,"sim change!!!", null,null);
		}
	}

}

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
		Log.i(tag,"重启手机成功");
		//1.获取开机后手机的sim卡序列号
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//2.获取手机序列号
		String simSerialNumber = tm.getSimSerialNumber();
		//获取存储的序列号
		String sim_number = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "");
		//3.对比不一致
		if(!simSerialNumber.equals(sim_number)){
			//4,发送短信给选中联系人号码
			
			SmsManager smsManager = SmsManager.getDefault();
			String phone = SpUtil.getString(context, ConstantValue.CONTACT_PHONE, "");
			smsManager.sendTextMessage(phone, null,"sim change!!!", null,null);
		}
	}

}

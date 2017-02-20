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
		//1.判断手机防盗是否开启
		boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
		
		if(open_security){//如果开启了,就播放音乐
			//2.获取短信内容居然是从intent中获取的数据
			Object[] object = (Object[]) intent.getExtras().get("pdus");
			
			
			//3.循环遍历短信过程
			for(Object ob : object){
				//4,获取短信对象
				SmsMessage sms = SmsMessage.createFromPdu((byte[])ob);
				//5,获取短信对象的基本信息
				String originatingAddress = sms.getOriginatingAddress();//发信地址
				String messageBody = sms.getMessageBody();
				//6.判断是否包含播放音乐的关键字
				if(messageBody.contains("#*alarm*#")){
					//7.播放音乐(准备音乐,MediaPlayer)
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);//不用想就知道R文件倒错了
					mediaPlayer.setLooping(true);//设置为循环播放
					mediaPlayer.start();//开启音乐
				}
				if(messageBody.contains("#*location*#")){
					//开启定位服务
					Intent intent2 = new Intent(context,LocationService.class);
					context.startService(intent2);
				}
				if(true){//messageBody.contains("#*lockscreen*#")
					//开启远程锁屏服务
					Intent intent3 = new Intent(context,LockScreenService.class);
					context.startService(intent3);
				}
			}
		}
	}

}

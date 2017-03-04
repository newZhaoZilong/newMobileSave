package com.shange.mobilesave.receiver;





import com.shange.mobilesave.engine.ProcessInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillProcessReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		
		ProcessInfoProvider.killAll(context);
		
	}
}

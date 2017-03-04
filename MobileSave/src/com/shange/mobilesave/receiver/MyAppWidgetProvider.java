package com.shange.mobilesave.receiver;




import com.shange.mobilesave.service.UpdateWidgetService;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

@SuppressLint("NewApi")
public class MyAppWidgetProvider extends AppWidgetProvider {
	private static final String tag = "MyAppWidgetProvider";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(tag, "onReceive............");
		super.onReceive(context, intent);
	}
	@Override
	public void onEnabled(Context context) {
		
		
		
		context.startService(new Intent(context, UpdateWidgetService.class));
		super.onEnabled(context);
	}
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
	
		context.startService(new Intent(context, UpdateWidgetService.class));
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
	
		context.startService(new Intent(context, UpdateWidgetService.class));
	
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onDisabled(Context context) {
		
	
		context.stopService(new Intent(context, UpdateWidgetService.class));
		super.onDisabled(context);
	}
}

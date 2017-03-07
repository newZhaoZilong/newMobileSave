package com.shange.mobilesave.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class SDCacheClearActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(getApplicationContext());
		textView.setText("¹þ¹þ");
		textView.setTextColor(Color.BLACK);
		
		setContentView(textView);
		
	}
}

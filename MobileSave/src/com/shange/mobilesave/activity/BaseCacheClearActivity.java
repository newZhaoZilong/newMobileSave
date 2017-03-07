package com.shange.mobilesave.activity;

import com.shange.mobilesave.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost.TabSpec;

public class BaseCacheClearActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_clearcache);
		
		//1.生成选项卡1
		TabSpec tab1 = getTabHost().newTabSpec("clear_cache").setIndicator("缓存清理");
		//2生成选项卡2
		TabSpec tab2 = getTabHost().newTabSpec("sd_clear_cache").setIndicator("sd卡清理");
		
		//3告知点中选项卡后继续操作
		
		tab1.setContent(new Intent(getApplicationContext(),CacheClearActivity.class));//点击跳转到一个界面
		tab2.setContent(new Intent(getApplicationContext(),SDCacheClearActivity.class));//点击跳转到一个界面

		//4.将此两个选项卡维护host(选项卡宿主)中去
		getTabHost().addTab(tab1);
		getTabHost().addTab(tab2);
	
	}
	
}

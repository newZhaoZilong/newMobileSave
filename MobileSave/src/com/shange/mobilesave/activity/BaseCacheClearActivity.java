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
		
		//1.����ѡ�1
		TabSpec tab1 = getTabHost().newTabSpec("clear_cache").setIndicator("��������");
		//2����ѡ�2
		TabSpec tab2 = getTabHost().newTabSpec("sd_clear_cache").setIndicator("sd������");
		
		//3��֪����ѡ����������
		
		tab1.setContent(new Intent(getApplicationContext(),CacheClearActivity.class));//�����ת��һ������
		tab2.setContent(new Intent(getApplicationContext(),SDCacheClearActivity.class));//�����ת��һ������

		//4.��������ѡ�ά��host(ѡ�����)��ȥ
		getTabHost().addTab(tab1);
		getTabHost().addTab(tab2);
	
	}
	
}

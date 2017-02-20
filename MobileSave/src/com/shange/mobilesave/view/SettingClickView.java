package com.shange.mobilesave.view;

import com.shange.mobilesave.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
//为什么继承relativelayout,是方便挂载吗,继承线性布局也可以,不继承好像不行,没法挂载,
public class SettingClickView extends RelativeLayout {


	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.shange.mobilesave";
	private static final String tag = "SettingItemView";
	private CheckBox cb_box;
	private TextView tv_des;
	private String mDestitle;
	private String mDesoff;
	private String mDeson;
	private TextView tv_setting_title;
	public SettingClickView(Context context) {
		this(context, null);
		// 
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		this(context, attrs,0);

	}

	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//这样就可以确保调用的是这个方法,将布局挂载到这个类里面,这个布局是子布局,挂载relativeLayout上,多用了一个布局,浪费
		View view = View.inflate(getContext(), R.layout.setting_click_view, this);
		tv_setting_title = (TextView) findViewById(R.id.tv_setting_title);
		tv_des = (TextView) findViewById(R.id.tv_des);


	}

	/**
	 * @param title 设置标题内容
	 */
	public void setTitle(String title){
		tv_setting_title.setText(title);
		
	}
	/**
	 * 设置描述内容
	 */
	public void setDes(String des) {
		tv_des.setText(des);

	}

	//把这个控件,当设置成checkbox一样,点击一下,有勾了,已开启,再点击一下,没勾了,已关闭,就像一个大号的checkbox
	//那么就应该有ischeck(),和setcheck(),这两个方法
	/**
	 * @return 判断选中状态
	 */
	public boolean isCheck(){

		//这是一个大号的Checkbox,那么调用这个方法,实际上调用的是
		//获取当前状态,需要从sheredPreference中获取
		return cb_box.isChecked();
	}
	/**
	 * @param isCheckd 为true,打钩,已开启,为false,没勾,已关闭
	 */
	public void setChecked(boolean checked){
		cb_box.setChecked(checked);//将checkbox的状态设置的和大条目一样
		if(checked){
			tv_des.setText(mDeson);
		}else{
			tv_des.setText(mDesoff);
		}
	}
	public void setChecked(String checked){
		if(!TextUtils.isEmpty(checked)){
			setChecked(true);//如果不为空,则设置为true
		}else{
			setChecked(false);
		}			
	}

}

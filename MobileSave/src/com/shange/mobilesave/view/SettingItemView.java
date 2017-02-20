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
public class SettingItemView extends RelativeLayout {

	
	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.shange.mobilesave";
	private static final String tag = "SettingItemView";
	private CheckBox cb_box;
	private TextView tv_des;
	private String mDestitle;
	private String mDesoff;
	private String mDeson;
	public SettingItemView(Context context) {
		this(context, null);
		// 
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//这样就可以确保调用的是这个方法,将布局挂载到这个类里面,这个布局是子布局,挂载relativeLayout上,多用了一个布局,浪费
		View view = View.inflate(getContext(), R.layout.setting_item_view, this);
		//自定义组合空间中的标题描述
		TextView tv_setting_title = (TextView) findViewById(R.id.tv_setting_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		cb_box = (CheckBox) findViewById(R.id.cb_box);
		//获取自定义以及原生属性的操作,写在此处,AttributeSet attrs对象中获取
		initAttrs(attrs);
		//获取布局文件中定义的字符串,赋值给自定义组件
		tv_setting_title.setText(mDestitle);
		
	}
	private void initAttrs(AttributeSet attrs) {
		mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
		mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
		mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
		
	//	Log.i(tag,mDestitle);
	//	Log.i(tag,mDesoff);
	//	Log.i(tag,mDeson);
		
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

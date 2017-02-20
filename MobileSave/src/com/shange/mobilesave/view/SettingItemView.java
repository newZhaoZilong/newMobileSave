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
//Ϊʲô�̳�relativelayout,�Ƿ��������,�̳����Բ���Ҳ����,���̳к�����,û������,
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
		//�����Ϳ���ȷ�����õ����������,�����ֹ��ص����������,����������Ӳ���,����relativeLayout��,������һ������,�˷�
		View view = View.inflate(getContext(), R.layout.setting_item_view, this);
		//�Զ�����Ͽռ��еı�������
		TextView tv_setting_title = (TextView) findViewById(R.id.tv_setting_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		cb_box = (CheckBox) findViewById(R.id.cb_box);
		//��ȡ�Զ����Լ�ԭ�����ԵĲ���,д�ڴ˴�,AttributeSet attrs�����л�ȡ
		initAttrs(attrs);
		//��ȡ�����ļ��ж�����ַ���,��ֵ���Զ������
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

	//������ؼ�,�����ó�checkboxһ��,���һ��,�й���,�ѿ���,�ٵ��һ��,û����,�ѹر�,����һ����ŵ�checkbox
	//��ô��Ӧ����ischeck(),��setcheck(),����������
	/**
	 * @return �ж�ѡ��״̬
	 */
	public boolean isCheck(){
		
		//����һ����ŵ�Checkbox,��ô�����������,ʵ���ϵ��õ���
		//��ȡ��ǰ״̬,��Ҫ��sheredPreference�л�ȡ
		return cb_box.isChecked();
	}
	/**
	 * @param isCheckd Ϊtrue,��,�ѿ���,Ϊfalse,û��,�ѹر�
	 */
	public void setChecked(boolean checked){
		cb_box.setChecked(checked);//��checkbox��״̬���õĺʹ���Ŀһ��
		if(checked){
			tv_des.setText(mDeson);
		}else{
			tv_des.setText(mDesoff);
		}
	}
	public void setChecked(String checked){
		if(!TextUtils.isEmpty(checked)){
			setChecked(true);//�����Ϊ��,������Ϊtrue
		}else{
			setChecked(false);
		}			
	}

}

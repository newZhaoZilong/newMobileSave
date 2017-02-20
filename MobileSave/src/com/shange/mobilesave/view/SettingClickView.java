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
		//�����Ϳ���ȷ�����õ����������,�����ֹ��ص����������,����������Ӳ���,����relativeLayout��,������һ������,�˷�
		View view = View.inflate(getContext(), R.layout.setting_click_view, this);
		tv_setting_title = (TextView) findViewById(R.id.tv_setting_title);
		tv_des = (TextView) findViewById(R.id.tv_des);


	}

	/**
	 * @param title ���ñ�������
	 */
	public void setTitle(String title){
		tv_setting_title.setText(title);
		
	}
	/**
	 * ������������
	 */
	public void setDes(String des) {
		tv_des.setText(des);

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

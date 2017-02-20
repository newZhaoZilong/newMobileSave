package com.shange.mobilesave.service;

import com.shange.mobilesave.R;
import com.shange.mobilesave.engine.AddressDao;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {

	public static final String tag = "AddressService";
	private TelephonyManager mTM;
	private MyPhoneStateListener myPhoneStateListener;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private WindowManager wm;
	private View mViewToast;
	private String address;

	//������Ϣ����
	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//˵����ȡ���˵�ַ,��ֵ��text
			
			
			tv_toast.setText(address);
		};
	};
	private TextView tv_toast;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		
		mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		myPhoneStateListener = new MyPhoneStateListener();
			//����
		mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);//����״̬
		//��ȡ�������
		
		
		
		//tm.listen(listener, events);
		
	}
	class MyPhoneStateListener extends PhoneStateListener{
		//��Ҫ��д����״̬�ı�ķ���
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			//
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				//����״̬,ʲôʱ����ӵĶ�ȡ�绰״̬,������
				//ȡ������
				Log.i(tag, "����״̬");
				showToast(incomingNumber);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//����绰
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//����״̬,����һ����˾
				Log.i(tag, "����״̬");
				if(wm!=null&&mViewToast!=null){
					//����״̬�Ǻ��п���Ϊ�յ�
					wm.removeView(mViewToast);
				}
				
				break;

			default:
				break;
			}
			
		}
	}
	/**
	 * չʾ��˾
	 */
	public void showToast( String incomingNumber) {
		// 
	
		//�Զ���Toast
		   final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
          //      | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ,���ܴ���ȥ��
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
     //   params.windowAnimations = com.android.internal.R.style.Animation_Toast;,����ȥ��
        //���ô�绰����
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        //ָ����˾������λ��
        params.gravity = Gravity.LEFT+Gravity.TOP;
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);
        //��ȡ��ɫid
        int toast_style_id = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        //ͼƬ����
        int[] drawables_id = new int[]{
        		R.drawable.call_locate_white,
        		R.drawable.call_locate_orange,
        		R.drawable.call_locate_blue,
        		R.drawable.call_locate_gray,
        		R.drawable.call_locate_green,    		
        };
        //��ʱ�ı䱳��ͼƬ
        tv_toast.setBackgroundResource(drawables_id[toast_style_id]);
        //�ڴ����Ϲ���һ��view(Ȩ��);
        wm.addView(mViewToast, params);
        //��ȡ�����������,��Ҫ����������ѯ
        query(incomingNumber);
	}
	private void query(final String incomingNumber) {
		//��ѯ�Ǻ�ʱ�Ĳ�����Ҫ�������߳�
		new Thread(){
		
			public void run() {
				
				address = AddressDao.getAddress(incomingNumber);
				//������Ϣ
				mhandler.sendEmptyMessage(0);
				
			};
			
		}.start();
		
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//���������,����ø÷���,�����ڸ÷�����,ȡ��������,����
		//�ж��Ƿ�Ϊ��
		if(mTM!=null&&myPhoneStateListener!=null){
			mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);//ȡ������,��ȡ���Ļ�,�رշ����,��绰�������
		}
		
		
	}

}

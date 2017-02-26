package com.shange.mobilesave.service;

import com.shange.mobilesave.R;
import com.shange.mobilesave.engine.AddressDao;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {

	public static final String tag = "AddressService";
	private TelephonyManager mTM;
	private MyPhoneStateListener myPhoneStateListener;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

	private View mViewToast;
	private String address;
	private WindowManager mWM;
	private int mScreenHeight;
	private int mScreenWidth;
	private TextView tv_toast;
	private InnerOutCallReceiver innerOutCallReceiver;

	//������Ϣ����
	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//˵����ȡ���˵�ַ,��ֵ��text
			tv_toast.setText(address);
		};
	};
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();


		mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		myPhoneStateListener = new MyPhoneStateListener();
		//����
		mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);//����״̬
		//��ȡ�������
		
		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		mScreenHeight = mWM.getDefaultDisplay().getHeight();
		mScreenWidth = mWM.getDefaultDisplay().getWidth();
		IntentFilter intentFilter = new IntentFilter();
		//tm.listen(listener, events);
		//����¼�����,�ڲ��Ĺ㲥,����Ҫ���嵥�ļ�������?
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		innerOutCallReceiver = new InnerOutCallReceiver();
		//ע��һ���㲥������,Ҳ����˵����Ҫ���嵥�ļ���ע����
		registerReceiver(innerOutCallReceiver, intentFilter);
		//����ע���ԭ����,������˾�ȷ�������


	}
	class InnerOutCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//���յ��˹㲥��,��Ҫ��ʾ�Զ������˾,��ʾ���������غ���
			//����Ǻ���?
			//��ȡ�����绰������ַ���
			String phone = getResultData();
			showToast(phone);

		}	
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
				if(mWM!=null&&mViewToast!=null){
					//����״̬�Ǻ��п���Ϊ�յ�
					mWM.removeView(mViewToast);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//����绰
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//����״̬,����һ����˾
			
				showToast(incomingNumber);
				Log.i(tag, "����״̬");
		
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
		mViewToast.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;
				case MotionEvent.ACTION_MOVE:
					int moveX = (int) event.getRawX();
					int moveY = (int) event.getRawY();

					int disX = moveX-startX;
					int disY = moveY-startY;

					//Toast��ֻ������
					params.x = params.x+disX;
					params.y = params.y+disY;

					//�ݴ���(iv_drag������ק���ֻ���Ļ)
					//���Ե���ܳ�����Ļ
					if(params.x<0){
						params.x=0;
						//return true;
					}

					//�ұ߱�Ե���ܳ�����Ļ
					if(params.x>(mScreenWidth-mViewToast.getWidth())){
						params.x = mScreenWidth-mViewToast.getWidth();
						//		return true;
					}

					//�ϱ�Ե���ܳ�����Ļ����ʵ����
					if(params.y<0){
						params.y=0;
						//	return true;
					}

					//�±�Ե(��Ļ�ĸ߶�-22 = �ױ�Ե��ʾ���ֵ)
					if(params.y>(mScreenHeight-22-mViewToast.getHeight())){
						params.y = mScreenHeight-22-mViewToast.getHeight();
						//return true;
					}


					//2,��֪�ƶ��Ŀؼ�,���������������ȥ��չʾ
					mWM.updateViewLayout(mViewToast, params);

					//3,����һ����ʵ����
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, params.x);
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, params.y);
					break;


				}
				//true��Ӧ��ק�Ĵ����¼�
				return true;
			}
		});
		//��ȡλ����Ϣ
		params.x = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
		params.y = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);

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
		mWM.addView(mViewToast, params);




		//wm.updateViewLayout(tv_toast, params);
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
		if(innerOutCallReceiver!=null){
			unregisterReceiver(innerOutCallReceiver);
		}

	}

}

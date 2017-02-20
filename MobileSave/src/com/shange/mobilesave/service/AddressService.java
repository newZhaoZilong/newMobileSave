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

	//创建消息机制
	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//说明获取到了地址,赋值给text
			
			
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
			//监听
		mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);//监听状态
		//获取窗体对象
		
		
		
		//tm.listen(listener, events);
		
	}
	class MyPhoneStateListener extends PhoneStateListener{
		//需要重写呼叫状态改变的方法
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			//
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				//空闲状态,什么时候添加的读取电话状态,不管了
				//取消监听
				Log.i(tag, "空闲状态");
				showToast(incomingNumber);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//接起电话
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//响铃状态,发送一个吐司
				Log.i(tag, "响铃状态");
				if(wm!=null&&mViewToast!=null){
					//空闲状态是很有可能为空的
					wm.removeView(mViewToast);
				}
				
				break;

			default:
				break;
			}
			
		}
	}
	/**
	 * 展示吐司
	 */
	public void showToast( String incomingNumber) {
		// 
	
		//自定义Toast
		   final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
          //      | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ,不能触摸去掉
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
     //   params.windowAnimations = com.android.internal.R.style.Animation_Toast;,动画去掉
        //设置打电话类型
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        //指定吐司的所在位置
        params.gravity = Gravity.LEFT+Gravity.TOP;
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);
        //获取颜色id
        int toast_style_id = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        //图片数组
        int[] drawables_id = new int[]{
        		R.drawable.call_locate_white,
        		R.drawable.call_locate_orange,
        		R.drawable.call_locate_blue,
        		R.drawable.call_locate_gray,
        		R.drawable.call_locate_green,    		
        };
        //随时改变背景图片
        tv_toast.setBackgroundResource(drawables_id[toast_style_id]);
        //在窗体上挂在一个view(权限);
        wm.addView(mViewToast, params);
        //获取到了来电号码,需要做来电号码查询
        query(incomingNumber);
	}
	private void query(final String incomingNumber) {
		//查询是耗时的操作需要放在子线程
		new Thread(){
		
			public void run() {
				
				address = AddressDao.getAddress(incomingNumber);
				//发送消息
				mhandler.sendEmptyMessage(0);
				
			};
			
		}.start();
		
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//服务结束后,会调用该方法,所以在该方法里,取消掉监听,完美
		//判断是否为空
		if(mTM!=null&&myPhoneStateListener!=null){
			mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);//取消监听,不取消的话,关闭服务后,打电话还会调用
		}
		
		
	}

}

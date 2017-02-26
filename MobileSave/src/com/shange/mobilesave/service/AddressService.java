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

	//创建消息机制
	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//说明获取到了地址,赋值给text
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
		//监听
		mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);//监听状态
		//获取窗体对象
		
		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		mScreenHeight = mWM.getDefaultDisplay().getHeight();
		mScreenWidth = mWM.getDefaultDisplay().getWidth();
		IntentFilter intentFilter = new IntentFilter();
		//tm.listen(listener, events);
		//添加事件名称,内部的广播,不需要在清单文件里配置?
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		innerOutCallReceiver = new InnerOutCallReceiver();
		//注册一个广播接受者,也就是说不需要在清单文件里注册了
		registerReceiver(innerOutCallReceiver, intentFilter);
		//这里注册的原因是,调用吐司等方法方便


	}
	class InnerOutCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//接收到此广播后,需要显示自定义的吐司,显示播出归属地号码
			//如何那号码?
			//获取播出电话号码的字符串
			String phone = getResultData();
			showToast(phone);

		}	
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
				if(mWM!=null&&mViewToast!=null){
					//空闲状态是很有可能为空的
					mWM.removeView(mViewToast);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//接起电话
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//响铃状态,发送一个吐司
			
				showToast(incomingNumber);
				Log.i(tag, "响铃状态");
		
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

					//Toast就只能这样
					params.x = params.x+disX;
					params.y = params.y+disY;

					//容错处理(iv_drag不能拖拽出手机屏幕)
					//左边缘不能超出屏幕
					if(params.x<0){
						params.x=0;
						//return true;
					}

					//右边边缘不能超出屏幕
					if(params.x>(mScreenWidth-mViewToast.getWidth())){
						params.x = mScreenWidth-mViewToast.getWidth();
						//		return true;
					}

					//上边缘不能超出屏幕可现实区域
					if(params.y<0){
						params.y=0;
						//	return true;
					}

					//下边缘(屏幕的高度-22 = 底边缘显示最大值)
					if(params.y>(mScreenHeight-22-mViewToast.getHeight())){
						params.y = mScreenHeight-22-mViewToast.getHeight();
						//return true;
					}


					//2,告知移动的控件,按计算出来的坐标去做展示
					mWM.updateViewLayout(mViewToast, params);

					//3,重置一次其实坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, params.x);
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, params.y);
					break;


				}
				//true相应拖拽的触发事件
				return true;
			}
		});
		//获取位置信息
		params.x = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
		params.y = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);

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
		mWM.addView(mViewToast, params);




		//wm.updateViewLayout(tv_toast, params);
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
		if(innerOutCallReceiver!=null){
			unregisterReceiver(innerOutCallReceiver);
		}

	}

}

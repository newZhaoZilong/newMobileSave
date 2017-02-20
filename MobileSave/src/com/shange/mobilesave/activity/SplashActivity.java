package com.shange.mobilesave.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.shange.mobilesave.R;
import com.shange.mobilesave.utils.ConstantValue;
import com.shange.mobilesave.utils.SpUtil;
import com.shange.mobilesave.utils.StreamUtil;
import com.shange.mobilesave.utils.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class SplashActivity extends Activity {
	
	/**
	 * 版本描述
	 */
	private String versionDes;
	
	/**
	 * 下载路径
	 */
	private String downloadUrl;
	
	private static final String tag = "SplashActivity";
	/**
	 * 更新新版本的状态码
	 */
	protected static final int UPDATE_VERSION = 100;
	/**
	 * 进入应用程序主界面的状态码
	 */
	protected static final int ENTER_HOME = 101;
	/**
	 * URL地址出错状态码
	 */
	protected static final int URL_ERROR = 102;
	/**
	 * IO异常状态码
	 */
	protected static final int IO_ERROR = 103;
	/**
	 * JSON异常状态码
	 */
	protected static final int JSON_ERROR = 104;
	private int mLocalVersionCode = 0;
	private TextView tv_version_name;
	
	private Handler mHandler = new Handler(){
		//因为要重写handlerMessage这个方法,所以创建子类对象的方式,重写方法比较方便
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_VERSION:
				//弹出对话框,提示用户更新
				showUpdateDialog();
				break;
			case ENTER_HOME:
				//进入应用程序主界面,activity跳转过程,太快了,所以要sleep
				enterHome();
				break;
			case URL_ERROR:
				ToastUtil.show(getApplicationContext(),"URL异常");
				enterHome();
				break;
			case IO_ERROR:
				ToastUtil.show(getApplicationContext(),"读取异常");
				enterHome();
				break;
			case JSON_ERROR:
				ToastUtil.show(getApplicationContext(),"JSON解析异常");
				enterHome();
				break;

			default:
				break;
			}
		};
	};

	private RelativeLayout rl_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* //去除掉当前acitvity头
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        setContentView(R.layout.activity_splash);
        //初始化UI
        initUI();
        //初始化数据
        initData();
        //初始化动画
        initAnimation();
        //舒适化数据库
        initDB();
        
    }

    
	private void initDB() {
		//1,归属地数据拷贝过程
		
		
		initAddressDB("address.db");
		
	}


	/**拷贝数据库到files文件夹下
	 * @param dbName 数据库的名称
	 */
	private void initAddressDB(String dbName) {
		
		File filesDir = getFilesDir();//获取目录
		//创建数据库
		File file = new File(filesDir,dbName);
		//数据库文件创建好了,需要将其传入files文件下
		if(file.exists()){
			return;//如果存在,就不需要在导入了
		}
		//2,读取第三方资产目录下的文件,这是资产目录的好处
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			inputStream = getAssets().open(dbName);
			//3.将读取的内容写入到指定文件夹的文件中去
			fileOutputStream = new FileOutputStream(file);
			//4,每次的读取内容太小
			byte[] bs = new byte[1024];
			int temp = -1;
			while((temp=inputStream.read(bs))!=-1){
				//写入到指定位置
				fileOutputStream.write(bs,0,temp);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(inputStream!=null&&fileOutputStream!=null){
				try {
					inputStream.close();
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}


	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		//创建alphaAnimation对象
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		//设置时常
		alphaAnimation.setDuration(3000);
		//启动动画,需要找到控件
		rl_splash.startAnimation(alphaAnimation);
		
		
	}


	/**
	 * 弹出对话框,提示用户更新
	 */
	protected void showUpdateDialog() {
		//对话框,是依赖于activity存在的
		Builder builder = new AlertDialog.Builder(this);
		
		//设置左上角图标
		builder.setIcon(R.drawable.ic_launcher);
		//设置描述内容
		builder.setTitle("版本更新:");
		
		builder.setMessage(versionDes);
		//积极按钮,立即更新
		builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载apk,apk链接地址,downloadUrl
				//判断sd卡是否挂载
				downloadApk();
				
			}

			

			
		});
		//消极按钮,进入主界面
		builder.setNegativeButton("稍后再说",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//取消对话框,进入主界面
				enterHome();
				
			}
		});
			//设置一个监听器,监听取消对话框
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// 进入界面
				enterHome();
				
			}
		});
			//取消对话框,进入主界面
		builder.show();
		
	}


	protected void downloadApk() {
		//apk下载链接地址,放置apk的所在路径
		//1.判断sd卡是否可用,是否挂载上
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//2.获取sd路径
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"mobilesafe.apk";
			//3创建HttpUtils对象
			HttpUtils httpUtils = new HttpUtils();
			//4.下载apk,(下载路径,下载地址,
			
			httpUtils.download(downloadUrl, path, new RequestCallBack<File>() {
				
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					//下载成功(
					Log.i(tag,"下载成功");
					//下载成功后要安装apk
					File file = arg0.result ;
					//提示用户安装
					installApk(file);
					Log.i(tag,"文件路径"+file.toString());
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// 下载失败
					Log.i(tag,"下载失败");
				}
				@Override
				public void onStart() {
					// 开始下载
					Log.i(tag,"开始下载");
					super.onStart();
				}
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					// 正在下载,total,总长度,current,当前进度
					Log.i(tag,"正在下载:total:"+total+"当前进度:current"+current);
					super.onLoading(total, current, isUploading);
				}
				
			});
		}
		
	}


	
	/**
	 * 安装对应apk
	 * @param file 安装文件路径
	 */
	protected void installApk(File file) {
		// 系统应用界面,源码,安装apk入口
		Intent intent = new Intent("android.intent.action.VIEW");
		//设置种类
		intent.addCategory("android.intent.category.DEFAULT");
		//文件作为数据源
		//设置数据和类型
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive" );
		//开启安装界面
		startActivityForResult(intent, 1);
		
	}


	/**
	 * 进入应用程序主界面
	 */
	protected void enterHome() {
		//创建意图
		Intent intent = new Intent(this,HomeActivity.class);
		//用过意图开启界面
		startActivity(intent);
		//在开启一个新的界面后,将导航界面关闭
		finish();
		
	}


	/**
	 * 获取数据方法
	 */
	private void initData() {
		// 
		//1.应用版本名称
		
		tv_version_name.setText("版本名称  "+getVersionName());
		//2.检测(本地版本号和服务器版本号)是否有更新,如果有更新,提示用户下载
		mLocalVersionCode = getVersionCode();
		
		Log.e(tag, mLocalVersionCode+"");
		//3 获取服务器版本号(客户端发请求,服务端给相应,(json,xml))
		//http://www.oxxx.com/update74.json?key=value 返回200 请求成功,流的方式将读取出来
		//json总内容包含:
		/*更新版本的版本名称
		 * 新版本的描述信息
		 * 服务器版本号
		 * 新版本apk下载地址
		 * 
		 */
		if(SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false)){
			//检测版本更新
			checkVersion();
		}else{
			//消息机制
			//在发送消息4庙后去处理,ENTER_HOME指向的消息
			//mHandler.sendMessageDelayed(msg, delayMillis),这个发送的是message
			//发送的仅是message.what的值
			mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
		}
		
		
		
		
	}	
	
	
	/**
	 * 检测版本号
	 */
	private void checkVersion() {
		//创建一个子线程
		new Thread(new Runnable() {//后边加大括号代表是子类对象,对于接口是实现类对象
			
			

			

			@Override
			public void run() {
				//请求json
				//创建URL对象
				//obtain可以节约资源,写在这里是因为catch中也要用到message
				//测试网络请求的时间
				
				long start = System.currentTimeMillis();
				Message msg = Message.obtain();
				try {
					URL url = new URL("http://192.168.1.107:8080/versionInfo.json");
					//打开连接
					HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
					//设置超时时间
					openConnection.setConnectTimeout(2000);
					//设置读取资源时间
					openConnection.setReadTimeout(2000);
					
					//判断请求码,是否为200,不是200,连接失败
					int responseCode = openConnection.getResponseCode();
					Log.i(tag, "响应码是:"+responseCode);
					if(responseCode == 200){
						//如果为200说明连接成功
						//连接成功就可以获取流
						//获取输入流,提取信息当然是输入流,输出流是发送信息
						InputStream inputStream = openConnection.getInputStream();
						//将流转化为字符串
						String versionInfo = StreamUtil.StreamToString(inputStream);
						//解析json字符串
						JSONObject jsonObject = new JSONObject(versionInfo);
						
						downloadUrl = jsonObject.getString("downloadUrl");
						String versionCode = jsonObject.getString("versionCode");
						versionDes = jsonObject.getString("versionDes");
						String versionName = jsonObject.getString("versionName");
						
						Log.i(tag, versionInfo);
						Log.i(tag, downloadUrl);
						Log.i(tag, versionCode);
						Log.i(tag, versionDes);
						Log.i(tag, versionName);
						
						//数据都获取到了,主线程不能做耗时的操作,子线程不能更新UI
						//8,比对版本号(服务器版本号>本地版本号,提示用户更新)
						if(mLocalVersionCode<Integer.parseInt(versionCode)){
							//提示用户更新,弹出对话框
							msg.what = UPDATE_VERSION;
						}else{
							//进入应用程序主界面
							msg.what = ENTER_HOME;
						}
						
					}
					
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
					msg.what = URL_ERROR ;
				} catch (IOException e) {
					
					e.printStackTrace();
					msg.what = IO_ERROR;
				} catch (JSONException e) {
					
					e.printStackTrace();
					msg.what = JSON_ERROR;
				}finally{
					//指定睡眠时间,请求网络的时长超过4秒则不作处理
					//请求网络的时长小于4秒,强制让其睡眠满4秒钟
					long end = System.currentTimeMillis();
					long time = end - start ;
					if(time < 4000){//单位是毫秒
						
							try {
								Thread.sleep(4000-(time));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}					
					}
					mHandler.sendMessage(msg);				
				}			
			}
		}).start();		
	}


	/**
	 * 获取版本号:清单文件中
	 * @return 版本号 非0 则代表获取成功
	 */
	private int getVersionCode() {
		//获取包管理者
		PackageManager pm = getPackageManager();
		
		try {
			//传0则代表获取基本信息
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			//返回版本号
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}


	/**
	 * 获取版本名称:清单文件中
	 * @return 版本名称 应用版本名称 返回null代表异常
	 */
	private String getVersionName() {
		//1.包管理者对象packageManager
		PackageManager pm = getPackageManager();
		//2,从包的管理者对象中,获取制定包名的基本信息(版本名称,版本号),传0代表获取基本信息
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			//通过管理者获取版本名
			return packageInfo.versionName ;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 初始化控件
	 */
	private void initUI() {
		//获取textview控件 
		tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 这个方法会在打开界面结束后执行
		Log.i(tag, "请求码;"+requestCode+"结果码:"+resultCode);
		if(requestCode == 1){
			enterHome();
		}
			
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
   
}

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
	 * �汾����
	 */
	private String versionDes;
	
	/**
	 * ����·��
	 */
	private String downloadUrl;
	
	private static final String tag = "SplashActivity";
	/**
	 * �����°汾��״̬��
	 */
	protected static final int UPDATE_VERSION = 100;
	/**
	 * ����Ӧ�ó����������״̬��
	 */
	protected static final int ENTER_HOME = 101;
	/**
	 * URL��ַ����״̬��
	 */
	protected static final int URL_ERROR = 102;
	/**
	 * IO�쳣״̬��
	 */
	protected static final int IO_ERROR = 103;
	/**
	 * JSON�쳣״̬��
	 */
	protected static final int JSON_ERROR = 104;
	private int mLocalVersionCode = 0;
	private TextView tv_version_name;
	
	private Handler mHandler = new Handler(){
		//��ΪҪ��дhandlerMessage�������,���Դ����������ķ�ʽ,��д�����ȽϷ���
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_VERSION:
				//�����Ի���,��ʾ�û�����
				showUpdateDialog();
				break;
			case ENTER_HOME:
				//����Ӧ�ó���������,activity��ת����,̫����,����Ҫsleep
				enterHome();
				break;
			case URL_ERROR:
				ToastUtil.show(getApplicationContext(),"URL�쳣");
				enterHome();
				break;
			case IO_ERROR:
				ToastUtil.show(getApplicationContext(),"��ȡ�쳣");
				enterHome();
				break;
			case JSON_ERROR:
				ToastUtil.show(getApplicationContext(),"JSON�����쳣");
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
       /* //ȥ������ǰacitvityͷ
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        setContentView(R.layout.activity_splash);
        //��ʼ��UI
        initUI();
        //��ʼ������
        initData();
        //��ʼ������
        initAnimation();
        //���ʻ����ݿ�
        initDB();
        
    }

    
	private void initDB() {
		//1,���������ݿ�������
		
		
		initAddressDB("address.db");
		
	}


	/**�������ݿ⵽files�ļ�����
	 * @param dbName ���ݿ������
	 */
	private void initAddressDB(String dbName) {
		
		File filesDir = getFilesDir();//��ȡĿ¼
		//�������ݿ�
		File file = new File(filesDir,dbName);
		//���ݿ��ļ���������,��Ҫ���䴫��files�ļ���
		if(file.exists()){
			return;//�������,�Ͳ���Ҫ�ڵ�����
		}
		//2,��ȡ�������ʲ�Ŀ¼�µ��ļ�,�����ʲ�Ŀ¼�ĺô�
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			inputStream = getAssets().open(dbName);
			//3.����ȡ������д�뵽ָ���ļ��е��ļ���ȥ
			fileOutputStream = new FileOutputStream(file);
			//4,ÿ�εĶ�ȡ����̫С
			byte[] bs = new byte[1024];
			int temp = -1;
			while((temp=inputStream.read(bs))!=-1){
				//д�뵽ָ��λ��
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
	 * ��ʼ������
	 */
	private void initAnimation() {
		//����alphaAnimation����
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		//����ʱ��
		alphaAnimation.setDuration(3000);
		//��������,��Ҫ�ҵ��ؼ�
		rl_splash.startAnimation(alphaAnimation);
		
		
	}


	/**
	 * �����Ի���,��ʾ�û�����
	 */
	protected void showUpdateDialog() {
		//�Ի���,��������activity���ڵ�
		Builder builder = new AlertDialog.Builder(this);
		
		//�������Ͻ�ͼ��
		builder.setIcon(R.drawable.ic_launcher);
		//������������
		builder.setTitle("�汾����:");
		
		builder.setMessage(versionDes);
		//������ť,��������
		builder.setPositiveButton("��������", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//����apk,apk���ӵ�ַ,downloadUrl
				//�ж�sd���Ƿ����
				downloadApk();
				
			}

			

			
		});
		//������ť,����������
		builder.setNegativeButton("�Ժ���˵",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ȡ���Ի���,����������
				enterHome();
				
			}
		});
			//����һ��������,����ȡ���Ի���
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// �������
				enterHome();
				
			}
		});
			//ȡ���Ի���,����������
		builder.show();
		
	}


	protected void downloadApk() {
		//apk�������ӵ�ַ,����apk������·��
		//1.�ж�sd���Ƿ����,�Ƿ������
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//2.��ȡsd·��
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"mobilesafe.apk";
			//3����HttpUtils����
			HttpUtils httpUtils = new HttpUtils();
			//4.����apk,(����·��,���ص�ַ,
			
			httpUtils.download(downloadUrl, path, new RequestCallBack<File>() {
				
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					//���سɹ�(
					Log.i(tag,"���سɹ�");
					//���سɹ���Ҫ��װapk
					File file = arg0.result ;
					//��ʾ�û���װ
					installApk(file);
					Log.i(tag,"�ļ�·��"+file.toString());
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// ����ʧ��
					Log.i(tag,"����ʧ��");
				}
				@Override
				public void onStart() {
					// ��ʼ����
					Log.i(tag,"��ʼ����");
					super.onStart();
				}
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					// ��������,total,�ܳ���,current,��ǰ����
					Log.i(tag,"��������:total:"+total+"��ǰ����:current"+current);
					super.onLoading(total, current, isUploading);
				}
				
			});
		}
		
	}


	
	/**
	 * ��װ��Ӧapk
	 * @param file ��װ�ļ�·��
	 */
	protected void installApk(File file) {
		// ϵͳӦ�ý���,Դ��,��װapk���
		Intent intent = new Intent("android.intent.action.VIEW");
		//��������
		intent.addCategory("android.intent.category.DEFAULT");
		//�ļ���Ϊ����Դ
		//�������ݺ�����
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive" );
		//������װ����
		startActivityForResult(intent, 1);
		
	}


	/**
	 * ����Ӧ�ó���������
	 */
	protected void enterHome() {
		//������ͼ
		Intent intent = new Intent(this,HomeActivity.class);
		//�ù���ͼ��������
		startActivity(intent);
		//�ڿ���һ���µĽ����,����������ر�
		finish();
		
	}


	/**
	 * ��ȡ���ݷ���
	 */
	private void initData() {
		// 
		//1.Ӧ�ð汾����
		
		tv_version_name.setText("�汾����  "+getVersionName());
		//2.���(���ذ汾�źͷ������汾��)�Ƿ��и���,����и���,��ʾ�û�����
		mLocalVersionCode = getVersionCode();
		
		Log.e(tag, mLocalVersionCode+"");
		//3 ��ȡ�������汾��(�ͻ��˷�����,����˸���Ӧ,(json,xml))
		//http://www.oxxx.com/update74.json?key=value ����200 ����ɹ�,���ķ�ʽ����ȡ����
		//json�����ݰ���:
		/*���°汾�İ汾����
		 * �°汾��������Ϣ
		 * �������汾��
		 * �°汾apk���ص�ַ
		 * 
		 */
		if(SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false)){
			//���汾����
			checkVersion();
		}else{
			//��Ϣ����
			//�ڷ�����Ϣ4���ȥ����,ENTER_HOMEָ�����Ϣ
			//mHandler.sendMessageDelayed(msg, delayMillis),������͵���message
			//���͵Ľ���message.what��ֵ
			mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
		}
		
		
		
		
	}	
	
	
	/**
	 * ���汾��
	 */
	private void checkVersion() {
		//����һ�����߳�
		new Thread(new Runnable() {//��߼Ӵ����Ŵ������������,���ڽӿ���ʵ�������
			
			

			

			@Override
			public void run() {
				//����json
				//����URL����
				//obtain���Խ�Լ��Դ,д����������Ϊcatch��ҲҪ�õ�message
				//�������������ʱ��
				
				long start = System.currentTimeMillis();
				Message msg = Message.obtain();
				try {
					URL url = new URL("http://192.168.1.107:8080/versionInfo.json");
					//������
					HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
					//���ó�ʱʱ��
					openConnection.setConnectTimeout(2000);
					//���ö�ȡ��Դʱ��
					openConnection.setReadTimeout(2000);
					
					//�ж�������,�Ƿ�Ϊ200,����200,����ʧ��
					int responseCode = openConnection.getResponseCode();
					Log.i(tag, "��Ӧ����:"+responseCode);
					if(responseCode == 200){
						//���Ϊ200˵�����ӳɹ�
						//���ӳɹ��Ϳ��Ի�ȡ��
						//��ȡ������,��ȡ��Ϣ��Ȼ��������,������Ƿ�����Ϣ
						InputStream inputStream = openConnection.getInputStream();
						//����ת��Ϊ�ַ���
						String versionInfo = StreamUtil.StreamToString(inputStream);
						//����json�ַ���
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
						
						//���ݶ���ȡ����,���̲߳�������ʱ�Ĳ���,���̲߳��ܸ���UI
						//8,�ȶ԰汾��(�������汾��>���ذ汾��,��ʾ�û�����)
						if(mLocalVersionCode<Integer.parseInt(versionCode)){
							//��ʾ�û�����,�����Ի���
							msg.what = UPDATE_VERSION;
						}else{
							//����Ӧ�ó���������
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
					//ָ��˯��ʱ��,���������ʱ������4����������
					//���������ʱ��С��4��,ǿ������˯����4����
					long end = System.currentTimeMillis();
					long time = end - start ;
					if(time < 4000){//��λ�Ǻ���
						
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
	 * ��ȡ�汾��:�嵥�ļ���
	 * @return �汾�� ��0 ������ȡ�ɹ�
	 */
	private int getVersionCode() {
		//��ȡ��������
		PackageManager pm = getPackageManager();
		
		try {
			//��0������ȡ������Ϣ
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			//���ذ汾��
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}


	/**
	 * ��ȡ�汾����:�嵥�ļ���
	 * @return �汾���� Ӧ�ð汾���� ����null�����쳣
	 */
	private String getVersionName() {
		//1.�������߶���packageManager
		PackageManager pm = getPackageManager();
		//2,�Ӱ��Ĺ����߶�����,��ȡ�ƶ������Ļ�����Ϣ(�汾����,�汾��),��0�����ȡ������Ϣ
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			//ͨ�������߻�ȡ�汾��
			return packageInfo.versionName ;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * ��ʼ���ؼ�
	 */
	private void initUI() {
		//��ȡtextview�ؼ� 
		tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ����������ڴ򿪽��������ִ��
		Log.i(tag, "������;"+requestCode+"�����:"+resultCode);
		if(requestCode == 1){
			enterHome();
		}
			
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
   
}

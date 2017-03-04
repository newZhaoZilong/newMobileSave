package com.shange.mobilesave.engine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.AndroidProcess;
import com.jaredrummler.android.processes.models.AndroidProcesses;
import com.shange.mobilesave.R;
import com.shange.mobilesave.db.domain.ProcessInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Process;
import android.util.Log;


//��ȡ���������ķ���
/**
 * @author ɽ��
 *���̹�������
 */

public class ProcessInfoProvider {
	private static final String tag = "ProcessInfoProvider";
	//1.��ȡactivityManager
	/**
	 * @param ctx
	 * @return �����������еĽ�����
	 */
	public static int getProcessCount(Context ctx){
		//1,��ȡactivityManager
//		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2.��ȡ�������еĽ��̼���
	//	List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		
		List<AndroidAppProcess> runningAppProcesses = AndroidProcesses.getRunningAppProcesses();
		
		
	/*	List<RecentTaskInfo> recentTasks = am.getRecentTasks(100, ActivityManager.RECENT_WITH_EXCLUDED);
		
		List<RunningTaskInfo> runningTasks = am.getRunningTasks(100);
		
		Log.i(tag, "������:"+runningTasks.size());*/
		//3,��ȡ��������
		return runningAppProcesses.size();
	}	
	/**
	 * @param ctx �����Ķ���
	 * @return ���ؿ��õ��ڴ��� bytes
	 */
	public static long getAvailSpace(Context ctx){

		//1,��ȡactivityManager
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2,�����洢�����ڴ�Ķ���
		MemoryInfo memoryInfo = new MemoryInfo();
		//3,��memoryInfo����(�����ڴ�)ֵ
		am.getMemoryInfo(memoryInfo);
		//��ȡ�����ڴ�
		long availMem = memoryInfo.availMem;

		return availMem;

	}
	/**
	 * @param ctx
	 * @return ���ؿ��õ��ڴ��� ��λΪbytes ����0˵���쳣
	 */
	public static long getTotalSpace(Context ctx){

		/*//1,��ȡactivityManager
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2,�����洢�����ڴ�Ķ���
		 MemoryInfo memoryInfo = new MemoryInfo();
		//3,��memoryInfo����(�����ڴ�)ֵ
		 am.getMemoryInfo(memoryInfo);
		 //��ȡ�����ڴ�
		 long totalMem = memoryInfo.totalMem;

		return totalMem;*/
		//���ڲ��ö�ȡ�ķ���
		File file = new File("proc/meminfo");

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			//������ȡ��
			fileReader = new FileReader(file);
			//������ȡ��������
			bufferedReader = new BufferedReader(fileReader);
			//��һ��
			String readLine = bufferedReader.readLine();

			Log.i(tag, "����һ��:"+readLine);

			String[] split = readLine.split(":");
			String[] split2 = split[1].split("k");
			String trim = split2[0].trim();
			Log.i(tag, "����һ��:"+trim);
			long parseLong = Long.parseLong(trim);
			System.out.println(parseLong);
			/*char[] charArray = readLine.toCharArray();
			StringBuilder stringBuilder = new StringBuilder();
			for(char c: charArray){
				if(c>='0' && c<='9'){
					stringBuilder.append(c);
				}
			}
			return Long.parseLong(stringBuilder.toString())*1024;*/
			return parseLong*1024;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fileReader!=null&&bufferedReader!=null){
				try {
					fileReader.close();
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}		
		return 0;
	}
	/**
	 * @param ctx �����Ļ���
	 * @return ��ǰ�ֻ��������еĽ��̵������Ϣ
	 */
	public static List<ProcessInfo> getProcessInfo(Context ctx) {

		List<ProcessInfo> arrayList = new ArrayList<ProcessInfo>();
		//1,���Ȼ�ȡ�������еĽ���
		//1,��ȡactivityManager
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		
		
		PackageManager pm = ctx.getPackageManager();
		//2.��ȡ�������еĽ��̼���
	//	List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		List<AndroidAppProcess> runningAppProcesses = AndroidProcesses.getRunningAppProcesses();
		//3,��������������еĽ���,��ȡ���������Ϣ(����,����,ͼ��,ʹ���ڴ�Ĵ�С,�Ƿ�Ϊϵͳ����,�Ƿ�ѡ��)
		for (AndroidAppProcess androidAppProcess : runningAppProcesses) {
			ProcessInfo processInfo = new ProcessInfo();
			//4,��ȡӦ�õİ���
			processInfo.packageName = androidAppProcess.getPackageName();
			
			
			//5,��ȡ����ռ�õ��ڴ��С(����һ�����̶�Ӧ��pid����)
			//ÿһ�����̶���һ��Ψһ��pid,process id
			int pid = androidAppProcess.pid;
			processInfo.pid = pid;
			int[] pids = new int[]{pid};
			android.os.Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(pids);//���id�ͻ�ȡ���������ڴ���Ϣ����
			//6.��������������λ��Ϊ0�Ķ���,Ϊ��ǰ���̵��ڴ���Ϣ�Ķ���
			android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
			//7,��ȡ��ʹ�õĴ�С
			int totalPrivateDirty = memoryInfo.getTotalPrivateDirty();

			processInfo.memSize = totalPrivateDirty*1024;
			//8.��ȡӦ�õ�����
			try {
				//8.��ȡӦ�õ�����
				ApplicationInfo applicationInfo = pm.getApplicationInfo(androidAppProcess.getPackageName(), 0);

				processInfo.name = applicationInfo.loadLabel(pm).toString();
				
				//9.��ȡӦ�õ�ͼ��
				processInfo.icon = applicationInfo.loadIcon(pm);

				int flags = applicationInfo.flags;

				if((ApplicationInfo.FLAG_SYSTEM & flags) == ApplicationInfo.FLAG_SYSTEM){
					//����ture˵����ϵͳ����
					processInfo.isSystem = true;
				}else{
					//�������û�����
					processInfo.isSystem = false;
				}

			} catch (NameNotFoundException e) {
				// ���û�ҵ�˵����ϵͳ����,���������ݴ���
				processInfo.name = androidAppProcess.name;
				//9.��ȡӦ�õ�ͼ��
				processInfo.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher);
				//�ò������ƿ϶���ϵͳ����
				processInfo.isSystem = true;
				e.printStackTrace();
			}
			arrayList.add(processInfo);

		}

		return arrayList;
		}
		
		/*for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			ProcessInfo processInfo = new ProcessInfo();
			//4,��ȡ���̵����� == Ӧ�õİ���
			processInfo.packageName = runningAppProcessInfo.processName;//
			//5,��ȡ����ռ�õ��ڴ��С(����һ�����̶�Ӧ��pid����)
			//ÿһ�����̶���һ��Ψһ��pid,process id
			int pid = runningAppProcessInfo.pid;
			int[] pids = new int[]{pid};
			android.os.Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(pids);//���id�ͻ�ȡ���������ڴ���Ϣ����
			//6.��������������λ��Ϊ0�Ķ���,Ϊ��ǰ���̵��ڴ���Ϣ�Ķ���
			android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
			//7,��ȡ��ʹ�õĴ�С
			int totalPrivateDirty = memoryInfo.getTotalPrivateDirty();

			processInfo.memSize = totalPrivateDirty*1024;
			//8.��ȡӦ�õ�����
			try {
				//8.��ȡӦ�õ�����
				ApplicationInfo applicationInfo = pm.getApplicationInfo(runningAppProcessInfo.processName, 0);

				processInfo.name = applicationInfo.loadLabel(pm).toString();
				
				//9.��ȡӦ�õ�ͼ��
				processInfo.icon = applicationInfo.loadIcon(pm);

				int flags = applicationInfo.flags;

				if((ApplicationInfo.FLAG_SYSTEM & flags) == ApplicationInfo.FLAG_SYSTEM){
					//����ture˵����ϵͳ����
					processInfo.isSystem = true;
				}else{
					//�������û�����
					processInfo.isSystem = false;
				}

			} catch (NameNotFoundException e) {
				// ���û�ҵ�˵����ϵͳ����,���������ݴ���
				processInfo.name = runningAppProcessInfo.processName;
				//9.��ȡӦ�õ�ͼ��
				processInfo.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher);
				//�ò������ƿ϶���ϵͳ����
				processInfo.isSystem = true;
				e.printStackTrace();
			}
			arrayList.add(processInfo);

		}

		return arrayList;
	}*/
	
	/**
	 * ɱ���̷���
	 * @param ctx �����Ļ���
	 * @param processInfo ɱ���������ڵ�javabean����
	 */
	public  static void killProcess(Context ctx,ProcessInfo processInfo) {
		//1,��ȡactivityManager
	/*	ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2,ɱ��ָ����������(Ȩ��)
		am.killBackgroundProcesses(processInfo.packageName);	*/
	//	ActivityManager sd = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		
	/*	ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        Method forceStopPackage;
           try {
                   forceStopPackage = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage",String.class);
                   forceStopPackage.setAccessible(true);
               forceStopPackage.invoke(am, processInfo);
           } catch (SecurityException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
           } catch (Exception e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
           }*/
           //Process.killProcess(pid)
		/*java.lang.Process sh = null;
		    DataOutputStream os = null;
		    try {
		        sh = Runtime.getRuntime().exec("su");
		        os = new DataOutputStream(sh.getOutputStream());
		        final String Command = "kill -9 " + processInfo.pid + "\n";
		        os.writeBytes(Command);
		        os.flush();

		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }

		    try {
		        sh.waitFor();
		    } catch (InterruptedException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }*/
	}
	/**
	 * ɱ�����н���
	 * @param ctx �����Ļ���
	 */
	public static void killAll(Context ctx){
		//1,��ȡactivityManager
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2.��ȡ�������еĽ��̵ļ���
		List<AndroidAppProcess> runningAppProcesses = AndroidProcesses.getRunningAppProcesses();
		//3,ѭ���������еĽ���,����ɱ��
		for (AndroidAppProcess androidAppProcess : runningAppProcesses) {
			//4,�����ֻ���ʿ����,�����Ľ��̶�Ҫȥɱ��
			if(androidAppProcess.getPackageName().equals(ctx.getPackageName())){
				//���ƥ�������ֻ���ʿ,����Ҫ��������ѭ��,������һ��ѭ��,����ɱ������
				continue;
			}
			am.killBackgroundProcesses(androidAppProcess.getPackageName());
			
		}
		
	}
}

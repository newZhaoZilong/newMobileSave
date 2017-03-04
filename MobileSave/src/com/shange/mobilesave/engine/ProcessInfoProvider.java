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


//获取进程总数的方法
/**
 * @author 山哥
 *进程管理工具类
 */

public class ProcessInfoProvider {
	private static final String tag = "ProcessInfoProvider";
	//1.获取activityManager
	/**
	 * @param ctx
	 * @return 返回正在运行的进程数
	 */
	public static int getProcessCount(Context ctx){
		//1,获取activityManager
//		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2.获取正在运行的进程集合
	//	List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		
		List<AndroidAppProcess> runningAppProcesses = AndroidProcesses.getRunningAppProcesses();
		
		
	/*	List<RecentTaskInfo> recentTasks = am.getRecentTasks(100, ActivityManager.RECENT_WITH_EXCLUDED);
		
		List<RunningTaskInfo> runningTasks = am.getRunningTasks(100);
		
		Log.i(tag, "进程数:"+runningTasks.size());*/
		//3,获取进程数量
		return runningAppProcesses.size();
	}	
	/**
	 * @param ctx 上下文对象
	 * @return 返回可用的内存数 bytes
	 */
	public static long getAvailSpace(Context ctx){

		//1,获取activityManager
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2,构建存储可用内存的对象
		MemoryInfo memoryInfo = new MemoryInfo();
		//3,给memoryInfo对象赋(可用内存)值
		am.getMemoryInfo(memoryInfo);
		//获取可用内存
		long availMem = memoryInfo.availMem;

		return availMem;

	}
	/**
	 * @param ctx
	 * @return 返回可用的内存数 单位为bytes 返回0说明异常
	 */
	public static long getTotalSpace(Context ctx){

		/*//1,获取activityManager
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2,构建存储可用内存的对象
		 MemoryInfo memoryInfo = new MemoryInfo();
		//3,给memoryInfo对象赋(可用内存)值
		 am.getMemoryInfo(memoryInfo);
		 //获取可用内存
		 long totalMem = memoryInfo.totalMem;

		return totalMem;*/
		//现在采用读取的方法
		File file = new File("proc/meminfo");

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			//创建读取流
			fileReader = new FileReader(file);
			//创建读取流缓冲区
			bufferedReader = new BufferedReader(fileReader);
			//读一行
			String readLine = bufferedReader.readLine();

			Log.i(tag, "读的一行:"+readLine);

			String[] split = readLine.split(":");
			String[] split2 = split[1].split("k");
			String trim = split2[0].trim();
			Log.i(tag, "读的一行:"+trim);
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
	 * @param ctx 上下文环境
	 * @return 当前手机正在运行的进程的相关信息
	 */
	public static List<ProcessInfo> getProcessInfo(Context ctx) {

		List<ProcessInfo> arrayList = new ArrayList<ProcessInfo>();
		//1,首先获取正在运行的进程
		//1,获取activityManager
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		
		
		PackageManager pm = ctx.getPackageManager();
		//2.获取正在运行的进程集合
	//	List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		List<AndroidAppProcess> runningAppProcesses = AndroidProcesses.getRunningAppProcesses();
		//3,遍历获得正在运行的进程,获取进程相关信息(名称,包名,图标,使用内存的大小,是否为系统进程,是否被选中)
		for (AndroidAppProcess androidAppProcess : runningAppProcesses) {
			ProcessInfo processInfo = new ProcessInfo();
			//4,获取应用的包名
			processInfo.packageName = androidAppProcess.getPackageName();
			
			
			//5,获取进程占用的内存大小(传递一个进程对应的pid数组)
			//每一个进程都有一个唯一的pid,process id
			int pid = androidAppProcess.pid;
			processInfo.pid = pid;
			int[] pids = new int[]{pid};
			android.os.Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(pids);//填几个id就获取几个进程内存信息对象
			//6.返回数据中索引位置为0的对象,为当前进程的内存信息的对象
			android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
			//7,获取已使用的大小
			int totalPrivateDirty = memoryInfo.getTotalPrivateDirty();

			processInfo.memSize = totalPrivateDirty*1024;
			//8.获取应用的名称
			try {
				//8.获取应用的名称
				ApplicationInfo applicationInfo = pm.getApplicationInfo(androidAppProcess.getPackageName(), 0);

				processInfo.name = applicationInfo.loadLabel(pm).toString();
				
				//9.获取应用的图标
				processInfo.icon = applicationInfo.loadIcon(pm);

				int flags = applicationInfo.flags;

				if((ApplicationInfo.FLAG_SYSTEM & flags) == ApplicationInfo.FLAG_SYSTEM){
					//等于ture说明是系统程序
					processInfo.isSystem = true;
				}else{
					//否则是用户程序
					processInfo.isSystem = false;
				}

			} catch (NameNotFoundException e) {
				// 如果没找到说明是系统程序,在这里做容错处理
				processInfo.name = androidAppProcess.name;
				//9.获取应用的图标
				processInfo.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher);
				//拿不到名称肯定是系统进程
				processInfo.isSystem = true;
				e.printStackTrace();
			}
			arrayList.add(processInfo);

		}

		return arrayList;
		}
		
		/*for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			ProcessInfo processInfo = new ProcessInfo();
			//4,获取进程的名称 == 应用的包名
			processInfo.packageName = runningAppProcessInfo.processName;//
			//5,获取进程占用的内存大小(传递一个进程对应的pid数组)
			//每一个进程都有一个唯一的pid,process id
			int pid = runningAppProcessInfo.pid;
			int[] pids = new int[]{pid};
			android.os.Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(pids);//填几个id就获取几个进程内存信息对象
			//6.返回数据中索引位置为0的对象,为当前进程的内存信息的对象
			android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
			//7,获取已使用的大小
			int totalPrivateDirty = memoryInfo.getTotalPrivateDirty();

			processInfo.memSize = totalPrivateDirty*1024;
			//8.获取应用的名称
			try {
				//8.获取应用的名称
				ApplicationInfo applicationInfo = pm.getApplicationInfo(runningAppProcessInfo.processName, 0);

				processInfo.name = applicationInfo.loadLabel(pm).toString();
				
				//9.获取应用的图标
				processInfo.icon = applicationInfo.loadIcon(pm);

				int flags = applicationInfo.flags;

				if((ApplicationInfo.FLAG_SYSTEM & flags) == ApplicationInfo.FLAG_SYSTEM){
					//等于ture说明是系统程序
					processInfo.isSystem = true;
				}else{
					//否则是用户程序
					processInfo.isSystem = false;
				}

			} catch (NameNotFoundException e) {
				// 如果没找到说明是系统程序,在这里做容错处理
				processInfo.name = runningAppProcessInfo.processName;
				//9.获取应用的图标
				processInfo.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher);
				//拿不到名称肯定是系统进程
				processInfo.isSystem = true;
				e.printStackTrace();
			}
			arrayList.add(processInfo);

		}

		return arrayList;
	}*/
	
	/**
	 * 杀进程方法
	 * @param ctx 上下文环境
	 * @param processInfo 杀死进程所在的javabean对象
	 */
	public  static void killProcess(Context ctx,ProcessInfo processInfo) {
		//1,获取activityManager
	/*	ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2,杀死指定包名进程(权限)
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
	 * 杀死所有进程
	 * @param ctx 上下文环境
	 */
	public static void killAll(Context ctx){
		//1,获取activityManager
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2.获取正在运行的进程的集合
		List<AndroidAppProcess> runningAppProcesses = AndroidProcesses.getRunningAppProcesses();
		//3,循环遍历所有的进程,并且杀死
		for (AndroidAppProcess androidAppProcess : runningAppProcesses) {
			//4,除了手机卫士以外,其他的进程都要去杀死
			if(androidAppProcess.getPackageName().equals(ctx.getPackageName())){
				//如果匹配上了手机卫士,则需要跳出本次循环,进行下一次循环,继续杀死进程
				continue;
			}
			am.killBackgroundProcesses(androidAppProcess.getPackageName());
			
		}
		
	}
}

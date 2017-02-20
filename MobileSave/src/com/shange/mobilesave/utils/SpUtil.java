package com.shange.mobilesave.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode.Mode;

public class SpUtil {
	//写
	
	private static SharedPreferences sharedPreferences;
	/**
	 * 写入boolean变量值sp中
	 * @param ctx 上下文环境
	 * @param key	存储节点名称
	 * @param value	存储节点的值boolean
	 */
	public static void putBoolean(Context ctx,String key,boolean value){
		//首先判断该文件存在
		createFile(ctx);
		//存放数据需要edit,取出数据不要,
		sharedPreferences.edit().putBoolean(key, value).commit();//这是存储节点
		
		
	}
	//读
	/**
	 * 读取boolean标识从sp中
	 * @param ctx 上下文环境
	 * @param key 存储节点名称
	 * @param value	没有此节点默认值
	 * @return	默认值或者此节点读取到的结果
	 */
	public static boolean getBoolean(Context ctx,String key,boolean value){
		
		createFile(ctx);
		return sharedPreferences.getBoolean(key, value);//取出节点,如果节点不存在,有默认值
	}
	/**
	 * 写入boolean变量值sp中
	 * @param ctx 上下文环境
	 * @param key	存储节点名称
	 * @param value	存储节点的值boolean
	 */
	public static void putString(Context ctx,String key,String value){
		//首先判断该文件存在
		createFile(ctx);
		//存放数据需要edit,取出数据不要,
		sharedPreferences.edit().putString(key, value).commit();//这是存储节点
		
		
	}
	//读
	/**
	 * 读取boolean标识从sp中
	 * @param ctx 上下文环境
	 * @param key 存储节点名称
	 * @param value	没有此节点默认值
	 * @return	默认值或者此节点读取到的结果
	 */
	public static String getString(Context ctx,String key,String value){
		createFile(ctx);
		return sharedPreferences.getString(key, value);//取出节点,如果节点不存在,有默认值
	}
	/**
	 * 将节点从sp中移除
	 * @param ctx	上下文对象
	 * @param key 需要移除的节点
	 */
	public static void remove(Context ctx,String key) {
		//首先判断该文件存在
			createFile(ctx);
				//存放数据需要edit,取出数据不要,
				sharedPreferences.edit().remove(key).commit();//移除节点,居然也tm要提交
		
	}
	public static void createFile(Context ctx){
		//判断该文件存在
		if(sharedPreferences==null){
			sharedPreferences = ctx.getSharedPreferences("config", ctx.MODE_PRIVATE);//这是创建一个xml文件,
			//保存在shared_prefs文件中
			
		}
	}
	public static int getInt(Context ctx,String key,int value) {
		createFile(ctx);
		return sharedPreferences.getInt(key, value);//取出节点,如果节点不存在,有默认值
		
	}
	public static void putInt(Context ctx,String key,int value){
		//首先判断该文件存在
		createFile(ctx);
		//存放数据需要edit,取出数据不要,
		sharedPreferences.edit().putInt(key, value).commit();//这是存储节点
		
		
	}
}

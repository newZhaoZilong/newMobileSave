package com.shange.mobilesave.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	/**
	 * 这是一个将流转换为字符串的方法
	 * @param 输入流
	 * @return 流转换成的字符串,返回null代表异常
	 */
	public static String StreamToString(InputStream inputStream) {
		//可以用byteArrayOutputStream,可以存储流的输出流,一般流转字符串要先写在硬盘上,再读取出来,太麻烦,输出流都要写一个输出地址
		//通过byteArrayOutputStream,可以存储在流里面,然后读取出来
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		//把byteArrayOutputStream当普通的输出流操作就可以
		//创建一个字节数组,用于运输字节的小车
		byte[] by = new byte[1024];
		//创建一个记录一次运输多少字节的数字
		int number = -1;
		try {
			while((number=inputStream.read(by))!=-1){//一次读一个字节数组,返回读取的字节数,如果读到末端则为-1
				//写入字节数组流
				byteArrayOutputStream.write(by, 0, number);				
				
			}
			//返回读取数据
			return byteArrayOutputStream.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
				byteArrayOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}

}

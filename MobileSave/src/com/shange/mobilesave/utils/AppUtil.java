package com.shange.mobilesave.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;
import android.sax.StartElementListener;

public class AppUtil {

	/**
	 * ��ȡSD�����ÿռ�
	 */
	public static long getAvailableSD(){
		 //��ȡSD��·��
		 File path = Environment.getExternalStorageDirectory();
		 //Ӳ�̵�API����
         StatFs stat = new StatFs(path.getPath());
         long blockSize = stat.getBlockSize();//��ȡÿ��Ĵ�С
         long totalBlocks = stat.getBlockCount();//��ȡ�ܿ���
         long availableBlocks = stat.getAvailableBlocks();//��ȡ���õĿ���
         return availableBlocks*blockSize;
	}
	/**
	 *��ȡ�ڴ���ÿռ�
	 * @return
	 */
	public static long getAvailableROM(){
		//��ȡ�ڴ�·��
		File path = Environment.getDataDirectory();
		//Ӳ�̵�API����
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();//��ȡÿ��Ĵ�С
        long totalBlocks = stat.getBlockCount();//��ȡ�ܿ���
        long availableBlocks = stat.getAvailableBlocks();//��ȡ���õĿ���
        return availableBlocks*blockSize;
	}
	
}

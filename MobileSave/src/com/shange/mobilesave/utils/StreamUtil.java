package com.shange.mobilesave.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	/**
	 * ����һ������ת��Ϊ�ַ����ķ���
	 * @param ������
	 * @return ��ת���ɵ��ַ���,����null�����쳣
	 */
	public static String StreamToString(InputStream inputStream) {
		//������byteArrayOutputStream,���Դ洢���������,һ����ת�ַ���Ҫ��д��Ӳ����,�ٶ�ȡ����,̫�鷳,�������Ҫдһ�������ַ
		//ͨ��byteArrayOutputStream,���Դ洢��������,Ȼ���ȡ����
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		//��byteArrayOutputStream����ͨ������������Ϳ���
		//����һ���ֽ�����,���������ֽڵ�С��
		byte[] by = new byte[1024];
		//����һ����¼һ����������ֽڵ�����
		int number = -1;
		try {
			while((number=inputStream.read(by))!=-1){//һ�ζ�һ���ֽ�����,���ض�ȡ���ֽ���,�������ĩ����Ϊ-1
				//д���ֽ�������
				byteArrayOutputStream.write(by, 0, number);				
				
			}
			//���ض�ȡ����
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

package com.shange.mobilesave.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Md5Util {
	
	public static void main(String[] args){
		String psd = "123";//
		
		encoder(psd);
	}
	
	
	/**��ָ���ַ�������md5�㷨ȥ����
	 * @param psd ��Ҫ���ܵ�����,���˼��δ���
	 * @return md5����ַ���
	 */
	public static String encoder(String psd) {
		//����
		psd = psd+"shangeshimengnan";//
		//1,ָ�������㷨����
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//2,����Ҫ���ܵ��ַ�����ת����byte���͵�����,Ȼ����������ϣ����
			byte[] bs = digest.digest(psd.getBytes());
			System.out.println(bs.length);
			//3,ѭ������bs,Ȼ����������32Ϊ�ַ���,�̶�д��
			//4,ƴ���ַ���
			StringBuilder stringBuilder = new StringBuilder();
			for(byte b : bs){
				//��֤ת��16���ƺ󲻳�����λ
				int i = b & 0xff;
				//int���͵�i��Ҫת����16�����ַ�
				String hexString = Integer.toHexString(i);
				System.out.println(hexString);
				if(hexString.length()<2){//���ֻ��1λ,����0,�����λ
					hexString = "0"+hexString;
				}
				//ѭ��һ��,���һ��
				stringBuilder.append(hexString);
			}
			//5��ӡ����
			return stringBuilder.toString();
		} catch (NoSuchAlgorithmException e) {
			// 
			e.printStackTrace();
		}
		return "";
	}
}

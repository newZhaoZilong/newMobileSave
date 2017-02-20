package com.shange.mobilesave.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Md5Util {
	
	public static void main(String[] args){
		String psd = "123";//
		
		encoder(psd);
	}
	
	
	/**给指定字符串按照md5算法去加密
	 * @param psd 需要加密的密码,做了加盐处理
	 * @return md5后的字符串
	 */
	public static String encoder(String psd) {
		//加盐
		psd = psd+"shangeshimengnan";//
		//1,指定加密算法类型
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//2,将需要加密的字符串中转换成byte类型的数组,然后进行随机哈希过程
			byte[] bs = digest.digest(psd.getBytes());
			System.out.println(bs.length);
			//3,循环遍历bs,然后让其生成32为字符串,固定写法
			//4,拼接字符串
			StringBuilder stringBuilder = new StringBuilder();
			for(byte b : bs){
				//保证转成16进制后不超过两位
				int i = b & 0xff;
				//int类型的i需要转换成16进制字符
				String hexString = Integer.toHexString(i);
				System.out.println(hexString);
				if(hexString.length()<2){//如果只有1位,补个0,变成两位
					hexString = "0"+hexString;
				}
				//循环一次,添加一次
				stringBuilder.append(hexString);
			}
			//5打印测试
			return stringBuilder.toString();
		} catch (NoSuchAlgorithmException e) {
			// 
			e.printStackTrace();
		}
		return "";
	}
}

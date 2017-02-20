package com.shange.mobilesave.utils;

import java.util.HashSet;

public class Contacts2Util {

	
	public static HashSet getContactId(String[] st){
		
		//创建hashSet,
		HashSet<String> hashSet = new HashSet<String>();
		//遍历数组
		for(String s : st){
			hashSet.add(s);
		}
		
		return hashSet;
		
	}
	/*public static void main(String[] args){
		String[] st = {"2","1","1","3","1","1"};
		HashSet contactId = getContactId(st);
		
		System.out.println(contactId.toString());
	}*/
}

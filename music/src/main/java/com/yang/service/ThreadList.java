package com.yang.service;

import java.util.ArrayList;

final public class ThreadList {

	private static ArrayList<Thread> list;

	static{
		if (list==null) {
			list=new ArrayList<Thread>();
		}
	}
	
	public static void add(Thread thread){
		list.add(thread);
	}
	
	public static ArrayList<Thread> getList() {
		return list;
	}
}

package com.xycode.threadlocal;

public class Main {

	public static void main(String[] args) {
		//����Client��Ӧ�����߳�,Ҳ�Ͷ�������TSLog
		new Client("Alice").start();
		new Client("Bob").start();
		new Client("Charis").start();
	}

}

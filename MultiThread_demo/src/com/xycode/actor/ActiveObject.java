package com.xycode.actor;

import java.util.concurrent.Future;

public interface ActiveObject {
	//�ӿ����涨��ķ���Ĭ����abstract��
	public Future<String> makeStr(int cnt,char fillChar);
	public void displayStr(String str);
	public void shutdown();
	
}

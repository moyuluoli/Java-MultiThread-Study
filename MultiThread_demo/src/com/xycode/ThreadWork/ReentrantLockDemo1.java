package com.xycode.ThreadWork;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo1 implements Runnable{
	//ReentrantLock(boolean fair),Ĭ�Ϸǹ�ƽ,fair==trueʱ,����ƽ��,��������ȷǹ�ƽ���ϲ�
	public static ReentrantLock lock=new ReentrantLock(false);//������,unlock��lock�Ĵ���Ҫһ��
	public static int i;
	@Override
	public void run() {
		for(int j=0;j<1e6;++j) {
			try {
				lock.lock();
				lock.lock();//����
				++i;
			}finally {
				lock.unlock();//�ͷ���
				lock.unlock();
			}

		}
		
	}
	public static void main(String[] args) throws InterruptedException {
		Thread t1=new Thread(new ReentrantLockDemo1());
		Thread t2=new Thread(new ReentrantLockDemo1());
		t1.start();t2.start();
		t1.join();t2.join();//join����start֮��
		
		System.out.println(i);
		
	}


}

package com.xycode.ThreadWork;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo2 implements Runnable{
	static ReentrantLock lock1=new ReentrantLock();
	static ReentrantLock lock2=new ReentrantLock();
	int lock_flag;
	
	public ReentrantLockDemo2(int lock_flag) {
		super();
		this.lock_flag = lock_flag;
	}

	@Override
	public void run() {
		if(lock_flag==1) {
			try {
				lock1.lockInterruptibly();//lock,������Ӧ�ж�,����ͨ�������ж��ź����������
				Thread.sleep(500);//����sleep,ʹ�̸߳����ײ�������
				lock2.lockInterruptibly();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else {
			try {
				lock2.lockInterruptibly();
				Thread.sleep(500);
				lock1.lockInterruptibly();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(lock1.isHeldByCurrentThread()) lock1.unlock();
		if(lock2.isHeldByCurrentThread()) lock2.unlock();
		System.out.println("Thread-"+Thread.currentThread().getId()+" exit!");
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1=new Thread(new ReentrantLockDemo2(1));
		Thread t2=new Thread(new ReentrantLockDemo2(2));
		t1.start();t2.start();
		Thread.sleep(1000);
		t2.interrupt();//t2�̱߳��ж�,t2�����������������,�����ͷ��ѳ��е���
	}

}

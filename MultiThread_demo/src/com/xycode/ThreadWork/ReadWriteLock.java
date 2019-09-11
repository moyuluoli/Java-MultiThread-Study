package com.xycode.ThreadWork;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/*
 * 1.ReentrantReadWriteLock(��д(����)��)��ʹ�������������֮�䲢��,��Ϊ�������������֮��û������һ���Ե�����,
 * ��������д����,д������д����֮���ܱ���ͬ��,��ʱ��д�����൱����ͨ����,
 * 2.�ڶ������Ĵ���Զ����д������,��д���ܴ����������������.
 * 3.ʵ��֤��,join()��ʹ������ʧ��....
 */
public class ReadWriteLock {
	static Lock lock=new ReentrantLock();
	static ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();
	static Lock readLock=readWriteLock.readLock();
	static Lock writeLock=readWriteLock.writeLock();
	int val;
	public Object handleRead(Lock lock) {
		try {
			lock.lock();
			Thread.sleep(1000);//ģ��һ����ʱ�Ķ�����
			System.out.println(val);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return val;
	}
	
	public void handleWrite(Lock lock,int data) {
		
		try {
			lock.lock();
			Thread.sleep(1000);//ģ��һ����ʱ��д����
			val=data;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		final ReadWriteLock demo=new ReadWriteLock();
		Runnable readRunnable=new Runnable() {
			@Override
			public void run() {
				demo.handleRead(readLock);
//				demo.handleRead(lock);
			}
		};
		Runnable writeRunnable=new Runnable() {
			@Override
			public void run() {
				demo.handleWrite(writeLock,new Random().nextInt(100));
//				demo.handleRead(lock,new Random().nextInt(100));
			}
		};
//		long begin=System.currentTimeMillis();
//		for(int i=0;i<10;++i) {
//			if(i<8) {
//				Thread t=new Thread(readRunnable);
//				t.start();
//				t.join();//main�߳�Ҫ�ȴ�t�߳�ִ�н���
//			}
//			else {
//				Thread t=new Thread(writeRunnable);
//				t.start();
//				t.join();
//			}
//		}
//		System.out.println("consume time: "+(System.currentTimeMillis()-begin)/1000+"s");
		
		for(int i=0;i<2;++i) {
			Thread t=new Thread(writeRunnable);
			t.start();
		}
		
		for(int i=0;i<18;++i) {
			Thread t=new Thread(readRunnable);
			t.start();
		}

		
	}

}

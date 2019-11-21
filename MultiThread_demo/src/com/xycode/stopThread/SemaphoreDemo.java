package com.xycode.stopThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
 * �ź���Semaphore,�������߳�ͬʱ����
 */
public class SemaphoreDemo implements Runnable{
	final Semaphore semp=new Semaphore(5);//�������5���߳�ͬʱ����
	@Override
	public void run() {
		try {
			semp.acquire();
			Thread.sleep(1000);//���һ�����
			System.out.println("Thread-"+Thread.currentThread().getId()+" done!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			semp.release();//�ͷ�һ�����
		}
	}
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec=Executors.newFixedThreadPool(10);
//		ExecutorService exec=Executors.newCachedThreadPool();
		final SemaphoreDemo demo=new SemaphoreDemo();
		for(int i=0;i<20;++i) {
			exec.submit(new Thread(demo));
		}
		exec.awaitTermination(10, TimeUnit.SECONDS);//�̳߳صȴ�ָ��ʱ��(�ȴ�ʱ����ó����̳߳�������ִ�е�ʱ��)
		System.out.println("Aloha!");
		exec.shutdown();//�ܾ������µ�����,���ҵȴ��̳߳��е�����ִ����Ϻ���˳�.
		//exec.shutdownNow();//�̳߳ؾܾ��������ύ������ͬʱ����ر��̳߳أ��̳߳����������ִ��ֱ���˳�,����!!!
	}

}

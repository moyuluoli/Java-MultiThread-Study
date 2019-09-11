package com.xycode.ThreadWork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRejectDemo {
	static class MyTask implements Runnable{

		@Override
		public void run() {
			System.out.println(System.currentTimeMillis()/1000+" : Thread-"+Thread.currentThread().getId());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService es=new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(10),
				new RejectedExecutionHandler() {//�Զ���ܾ�����
					
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						System.out.println(r.toString()+" is discard.");
					}
				});
		for(int i=0;i<10e5;++i) {
			es.submit(new MyTask());
			Thread.sleep(10);//10ms�ύһ������,����100ms����ִ����һ������,���ձؽ������̳߳صĴ�������,�Ӷ�ִ��reject����.
		}
	}

}

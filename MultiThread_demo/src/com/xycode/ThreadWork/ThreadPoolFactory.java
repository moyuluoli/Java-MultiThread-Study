package com.xycode.ThreadWork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolFactory {
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
		ExecutorService es=new ThreadPoolExecutor(5,//corePoolSize
				10,//maximumPoolSize
				0,//�̳߳����߳���������corePoolSize��С��maximumPoolSize,������̱߳��ִ���ʱ��
				TimeUnit.SECONDS,//���ʱ���ʱ�䵥λ
				new SynchronousQueue<>(),//�������,��ű��ύ��δִ�е�����
				new ThreadFactory() {//�����̵߳Ĺ���
					
					@Override
					public Thread newThread(Runnable r) {
						Thread t=new Thread(r);
						t.setDaemon(true);
						System.out.println("create Thread-"+t.getId());
						return t;
					}
				},
			new RejectedExecutionHandler() {//�Զ���ܾ�����,���̳߳����߳���������maximumPoolSizeʱִ�еľܾ�����
				
				@Override
				public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
					System.out.println("Thread-"+new Thread(r).getId()+" is rejected.");
				}
			}
		);
		
		for(int i=0;i<20;++i) {
			es.submit(new MyTask());
		}
		//��Ϊ�̳߳��е��̶߳�����Ϊ�ػ��ֳ�,�����ڷ��ػ��߳�mainû�н���֮ǰ,�̳߳�Ҳһֱ�����˳�
		//�̳߳صȴ�main�߳�����3s֮����˳�
		Thread.sleep(3000);
	}

}

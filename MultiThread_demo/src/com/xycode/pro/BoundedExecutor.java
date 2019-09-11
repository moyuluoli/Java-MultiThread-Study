package com.xycode.pro;
/*
 * ʹ���ź���(Semaphore)�����������ύ������
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

public class BoundedExecutor {
	final ExecutorService exec;
	final Semaphore semp;
	
	public BoundedExecutor(ExecutorService exec, Semaphore semp) {
		super();
		this.exec = exec;
		this.semp = semp;
	}
	
	public void shutdown(){
		exec.shutdown();
	}

	public void submitTask(final Runnable task) throws InterruptedException {
		semp.acquire();
		try {
			exec.execute(new Runnable() {//��task���°�װ,Ƕ���ź�������
				@Override
				public void run() {
					try {
						task.run();
					}finally {
						semp.release();
					}
				}
			});
		}catch(RejectedExecutionException e) {
			semp.release();
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		BoundedExecutor tasks=new BoundedExecutor(Executors.newCachedThreadPool(), new Semaphore(10));
		for(int i=0;i<100;++i) {
			try {
				tasks.submitTask(new Runnable() {
					
					@Override
					public void run() {
						System.out.println("Task-"+Thread.currentThread().getId()+" is Running.");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//�ܷ������Եļ��ʽ������һ������(һ��10��)
		
		tasks.shutdown();
		
	}
	
}

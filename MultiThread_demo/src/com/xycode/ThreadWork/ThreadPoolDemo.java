package com.xycode.ThreadWork;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * ScheduledThreadPool:��Լƻ�����
 */
public class ThreadPoolDemo {

	public static void main(String[] args) {
		ScheduledExecutorService ses=Executors.newScheduledThreadPool(10);//poolSize=10
		ses.scheduleAtFixedRate(new Runnable() {
			//scheduleAtFixedRate:
			//2sһ������,������ʱΪ0,
			//������ִ��ʱ�䳬��������,�t��һ����������������ִ��,��������ʱ.
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					System.out.println(System.currentTimeMillis()/1000%1009);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}, 0, 2, TimeUnit.SECONDS);
		
//		ses.schedule(new Runnable() {
//			//schedule:
//			//ָ����ʱ,Ȼ��ִ��
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(3000);
//					System.out.println(System.currentTimeMillis()/1000%1009);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				
//			}
//		}, 3, TimeUnit.SECONDS);
		
//		ses.scheduleWithFixedDelay(new Runnable() {
//			//scheduleWithFixedDelay:
//			//2sһ������,������ʱΪ0,�����ǵ�����������,ʵ��������ÿ��������������һ����ʱ(period)
//			//������ִ��ʱ�䳬��������,�t��һ������������Ի������ʱ(period),��������ʱ.
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(3000);
//					System.out.println(System.currentTimeMillis()/1000%1009);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				
//			}
//		}, 0, 2, TimeUnit.SECONDS);
		
		//�̳߳�����shutdown,�����������˳�
		//ses.shutdown();//�ܾ������µ�����,���ҵȴ��̳߳��е�����ִ����Ϻ���˳�.
		//ses.shutdownNow();//�̳߳ؾܾ��������ύ������ͬʱ����ر��̳߳أ��̳߳����������ִ��ֱ���˳�,����!!!
		
		
	}

}

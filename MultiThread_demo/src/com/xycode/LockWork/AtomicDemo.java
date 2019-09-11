package com.xycode.LockWork;
/**
 * AtomicData��,�ײ�ԭ����CAS,�ھ�������̫���ҵ������,�������ڼ����ķ�ʽ
 * ���߼��Ļ���AtomicReference<Class>,���һ�������з�װ,����Ҫ�ֶ��Ľ���CAS����,CompareAndSet
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class AtomicDemo implements Runnable{
	static AtomicInteger i=new AtomicInteger(0);//ԭ��Integer��,��ֵ��Ϊ0,�ײ�ԭ����CAS
	static int k=0;
	int mode;
	public static ReentrantLock lock=new ReentrantLock(false);
	
	public AtomicDemo(int mode) {
		super();
		this.mode = mode;
	}

	@Override
	public void run() {
		if(mode==0) {
			//1900
			for(int j=0;j<10000000;++j) {
				i.incrementAndGet();//�൱��++i,AtomicInteger��֧������++i,i++�Ĳ���,����ͨ��api
			}
		}else {
			//10ms
			lock.lock();
			for(int j=0;j<10000000;++j) {
				++k;
				
			}
			lock.unlock();
			
			//2000ms
			//AtomicInteger�ķ������ܱ���������Ժ�һЩ,Զ�����������ָ��ŵļ�����ʽ(���ֻ�)
//			for(int j=0;j<10000000;++j) {
//				lock.lock();
//				++k;
//				lock.unlock();
//			}
		}

	}

	public static void main(String[] args) {
//		long b=System.currentTimeMillis();
//		ExecutorService es=new ThreadPoolExecutor(10,10,0,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>()) {
//			@Override
//			protected void beforeExecute(Thread t, Runnable r) {
//				super.beforeExecute(t, r);//Ĭ���ǿյķ���
//				
//			}
//			@Override
//			protected void afterExecute(Runnable r, Throwable t) {
//				super.afterExecute(r, t);
//
//			}
//			@Override
//			protected void terminated() {
//				super.terminated();//Ĭ���ǿյķ���
//				System.out.println(i);
//				System.out.println("ThreadPool exit!");
//				System.out.println("AtomicInteger total cost time: "+(System.currentTimeMillis()-b)+"ms.");
//			}
//		};
//		
//		for(int i=0;i<10;++i) {
//			es.submit(new AtomicDemo(0));
//		}
//		es.shutdown();
		
		
		long b=System.currentTimeMillis();
		ExecutorService es=new ThreadPoolExecutor(10,10,0,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>()) {
			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				super.beforeExecute(t, r);//Ĭ���ǿյķ���
				
			}
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);

			}
			@Override
			protected void terminated() {
				super.terminated();//Ĭ���ǿյķ���
				System.out.println(k);
				System.out.println("ThreadPool exit!");
				System.out.println("ReentrantLock total cost time: "+(System.currentTimeMillis()-b)+"ms.");
			}
		};
		
		for(int i=0;i<10;++i) {
			es.submit(new AtomicDemo(1));
		}
		es.shutdown();
		
		
	}
}

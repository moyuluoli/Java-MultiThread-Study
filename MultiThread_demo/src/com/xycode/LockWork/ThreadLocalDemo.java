package com.xycode.LockWork;
/**
 * ThreadLocal,һ��ͨ����ÿ���߳����ɾֲ�����������ȫ��������Դ�����ķ���.
 * ʵ��֤��,��ͳ��ֱ�������������ֲ������ݳ�ԱҲ�ܴﵽ��ͬ��Ч��,����Ҳ����һ��.
 * ����˵,ThreadLocal��ȴ�ͳ�ķ�������������˽����ݽ��������.
 * 
 */
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadLocalDemo{
	static final int GEN_COUNT=10000000;
	static final int THREAD_COUNT=4;
	static ExecutorService es=Executors.newFixedThreadPool(THREAD_COUNT);
	static Random r=new Random(1234);
	
	static ThreadLocal<Random> tr=new ThreadLocal<>() {//ThreadLocal model,Ϊÿ���߳�����һ����������,����������
		protected Random initialValue() {
			return new Random(1234);
		}
	};
	
	static class MyTask implements Callable<Long>{
		int mode;
		Random my_r;
		public MyTask(int mode) {
			super();
			this.mode = mode;
			this.my_r=new Random(1234);
		}

		@Override
		public Long call(){
			long b=System.currentTimeMillis();
			for(int i=0;i<GEN_COUNT;++i) {
				if(mode==0) {
					r.nextInt();
				}else if(mode==1){
					tr.get().nextInt();
				}else {
					my_r.nextInt();
				}
			}
			long e=System.currentTimeMillis();
			System.out.println("Thread-"+Thread.currentThread().getId()+" costs "+(e-b)+"ms.");
			return e-b;
		}
		
	}
	public static void main(String[] args) {
		Future<Long>[] f=new Future[THREAD_COUNT];//future model
		for(int i=0;i<THREAD_COUNT;++i) {
			f[i]=es.submit(new MyTask(0));
		}
		long total_time=0;
		for(int i=0;i<THREAD_COUNT;++i) {
			try {
				total_time+=f[i].get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("GlobalData's total time: "+total_time+"ms.");
	
		for(int i=0;i<THREAD_COUNT;++i) {
			f[i]=es.submit(new MyTask(1));
		}
		total_time=0;
		for(int i=0;i<THREAD_COUNT;++i) {
			try {
				total_time+=f[i].get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("ThreadLocal's total time: "+total_time+"ms.");
		
		for(int i=0;i<THREAD_COUNT;++i) {
			f[i]=es.submit(new MyTask(2));
		}
		total_time=0;
		for(int i=0;i<THREAD_COUNT;++i) {
			try {
				total_time+=f[i].get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("ClassLocalData's total time: "+total_time+"ms.");
	}


}

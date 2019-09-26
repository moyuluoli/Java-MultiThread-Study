package com.xycode.pro;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CacheTest {
	/*
	 * ʹ���̰߳�ȫ��ConcurrentHashMap�����л���,Ч����HashMap+synchronized��һЩ,
	 * Ȼ��,�������̼߳�������ʱ��Ͼ�ʱ,���п�������ظ�����
	 */
	static class Memorizer_1<R,A> implements Computable<R,A>{
		final Map<A,R> cache=new ConcurrentHashMap<>();
		
		@Override
		public R compute(final A arg) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return (R)arg;
		}
		
		@Override
		public R get(final A arg) {
			R result=cache.get(arg);
			if(result==null) {
				result=compute(arg);
				cache.put(arg, result);
			}
			return result;
		}
		
	}
	/*
	 * 
	 * ʹ�ò�����Future��ConcurrentHashMap,
	 * ��Ϊfuture�������õ�,���Բ�����Ϊ����ʱ�����������ظ�����
	 * ����ʹ��ConcurrentHashMap::putIfAbsent(K,V),��Ϊ�÷�����ԭ���Ե�,�ʶž����ظ����future�Ŀ�����
	 */
	static class Memorizer_2<R,A> implements Computable<R,A>{
		final Map<A,Future<R>> cache=new ConcurrentHashMap<>();
		
		@Override
		public R compute(final A arg) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return (R)arg;
		}
		
		@Override
		public R get(final A arg) {
			while(true) {
				Future<R> f=cache.get(arg);
				if(f==null) {
					FutureTask<R> ft=new FutureTask<>(new Callable<R>() {

						@Override
						public R call() throws Exception {
							return compute(arg);
						}
						
					});
					
					f=cache.putIfAbsent(arg, ft);
					if(f==null) {//putIfAbsent����null,˵����ǰֵ��null(������˵����ǰ���������ӳ��,�����ǵ�һ�����)
						f=ft;
						ft.run();//�����call(),���jdkԴ��
					}
				}
				
				try {
					return f.get();//�����ȴ�(��û��õĻ�),�����������ؽ��
				}catch(CancellationException e) {
					e.printStackTrace();
					cache.remove(arg,f);//��ֹ������Ⱦ
				}catch(RuntimeException e){
					e.printStackTrace();
					cache.remove(arg,f);//��ֹ������Ⱦ
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	static CountDownLatch gate;
	static class Task<R,A> implements Runnable{
		Computable<R,A> m;
		A arg;
		public Task(Computable<R, A> m, A arg) {
			super();
			this.m = m;
			this.arg = arg;
		}
		
		@Override
		public void run()  {
			try {
				m.get(arg);
			}finally {
				gate.countDown();//������һ
			}
			//System.out.println(m.get(arg));
		}
		
	}
	
	
	public static void test(ExecutorService es,Computable<Integer, Integer> m) {
		gate=new CountDownLatch(1000);//ʹ��CountDownLatch��������ʱ
		Random r=new Random();
		long begin=System.currentTimeMillis();
		for(int i=0;i<1000;++i) {
			es.submit(new Task<Integer, Integer>(m,r.nextInt(50)));//�������ظ���Խ��,�����Ч��Խ��
		}
		try {
			gate.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end=System.currentTimeMillis();
		if(m instanceof Memorizer_1)//instanceofʶ�����ĸ���
			System.out.println("Memorizer_1's total time: "+(end-begin)/1000+" s.");
		else
			System.out.println("Memorizer_2's total time: "+(end-begin)/1000+" s.");
		es.shutdown();
	}
	
	public static void main(String[] args) {
		
		//��һ�ַ�ʽ�����Ҫ6s ~ 8s
//		ExecutorService es_1=new ThreadPoolExecutor(10,20,0,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>()) {
//			@Override
//			protected void terminated() {
//				System.out.println(System.currentTimeMillis()/1000%1000);
//				System.out.println("ThreadPool exit!");
//				System.out.println();
//			}
//		};
//		
//		test(es_1,new Memorizer_1<Integer,Integer>());
//		System.out.println(System.currentTimeMillis()/1000%1000);
		
		
		//�ڶ��ַ�ʽ�����Ҫ3s ~ 4s,�����ǵ�һ�ֵ�������
//		ExecutorService es_2=new ThreadPoolExecutor(10,20,0,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>()) {
//			@Override
//			protected void terminated() {
//				System.out.println(System.currentTimeMillis()/1000%1000);
//				System.out.println("ThreadPool exit!");
//			}
//		};
//		test(es_2,new Memorizer_2<Integer,Integer>());
//		System.out.println(System.currentTimeMillis()/1000%1000);
		
		
		
//		ExecutorService es_1=Executors.newFixedThreadPool(20);
//		test(es_1,new Memorizer_1<Integer,Integer>());//5s
//		
//		ExecutorService es_2=Executors.newFixedThreadPool(20);
//		test(es_2,new Memorizer_2<Integer,Integer>());//2s,��������2��+�Ĳ��
		
		
		ExecutorService es_1=new TimingThreadPool
				(10, 20, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
		ExecutorService es_2=new TimingThreadPool
				(10, 20, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
		test(es_1,new Memorizer_1<Integer,Integer>());//����ȷ��,�������ƽ������ʱ��Ҳ�ɿ������ܵĲ��
		test(es_2,new Memorizer_2<Integer,Integer>());

	}

}

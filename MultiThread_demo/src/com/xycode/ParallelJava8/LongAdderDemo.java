package com.xycode.ParallelJava8;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

public class LongAdderDemo {
	static final int MAX_THREAD=10;
	static final int TASK_COUNT=10;
	static final int TARGET_COUNT=10000000;
	ReentrantLock lock=new ReentrantLock();
	long count=0;//��ͨ�ļ���ͬ��
	AtomicLong acount=new AtomicLong(0);//ԭ��ͬ��
	LongAdder lacount=new LongAdder();//LongAdderͬ��(ԭ��:����ԭ��ͬ�����ȵ����)
	
	
	static CountDownLatch cdlsync=new CountDownLatch(TASK_COUNT);//�ȴ�ָ�����߳����(��������)
	static CountDownLatch cdlatomic=new CountDownLatch(TASK_COUNT);
	static CountDownLatch cdladder=new CountDownLatch(TASK_COUNT);
	
	static class SyncThread implements Runnable{
		String name;
		long start;
		LongAdderDemo out;
		
		public SyncThread(String name, long start, LongAdderDemo out) {
			super();
			this.name = name;
			this.start = start;
			this.out = out;
		}

		@Override
		public void run() {
			
			for(int i=0;i<TARGET_COUNT;++i) {
				out.lock.lock();//������Ϊ�˱Ƚ�����,���Բ�ȥ�������Ⱥ�ϸ�ļ�������,ʵ������forѭ��������������ܽ�����õ�
				++out.count;
				out.lock.unlock();
			}
			
			cdlsync.countDown();//����һ���߳��������
			
		}
		
	}
	
	public void testSync() throws InterruptedException {
		ExecutorService es=Executors.newFixedThreadPool(MAX_THREAD);
		SyncThread sync=new SyncThread("SyncThread", System.currentTimeMillis(), this);
		for(int i=0;i<TASK_COUNT;++i) {
			es.submit(sync);
		}
		cdlsync.await();
		System.out.println(count);
		System.out.println(sync.name+" spend "+(System.currentTimeMillis()-sync.start)+" ms");
		es.shutdown();
	}
	
	static class AtomicThread implements Runnable{
		String name;
		long start;
		LongAdderDemo out;
		
		public AtomicThread(String name, long start, LongAdderDemo out) {
			super();
			this.name = name;
			this.start = start;
			this.out = out;
		}

		@Override
		public void run() {
			for(int i=0;i<TARGET_COUNT;++i) {
				out.acount.incrementAndGet();
			}
			cdlatomic.countDown();
		}
		
	}
	
	
	public void testAtomic() throws InterruptedException {
		ExecutorService es=Executors.newFixedThreadPool(MAX_THREAD);
		AtomicThread atom=new AtomicThread("AtomicThread", System.currentTimeMillis(), this);
		for(int i=0;i<TASK_COUNT;++i) {
			es.submit(atom);
		}
		cdlatomic.await();
		System.out.println(acount);
		System.out.println(atom.name+" spend "+(System.currentTimeMillis()-atom.start)+" ms");
		es.shutdown();
	}
	
	static class AdderThread implements Runnable{
		String name;
		long start;
		LongAdderDemo out;
		
		public AdderThread(String name, long start, LongAdderDemo out) {
			super();
			this.name = name;
			this.start = start;
			this.out = out;
		}

		@Override
		public void run() {
			for(int i=0;i<TARGET_COUNT;++i) {
				out.lacount.increment();//�����ȵ����ļ�
			}
			cdladder.countDown();
		}
		
	}
	
	
	public void testAdder() throws InterruptedException {
		ExecutorService es=Executors.newFixedThreadPool(MAX_THREAD);
		AdderThread adder=new AdderThread("AdderThread", System.currentTimeMillis(), this);
		for(int i=0;i<TASK_COUNT;++i) {
			es.submit(adder);
		}
		cdladder.await();//�ȴ�����ȫ�����
		System.out.println(lacount.sum());//��Ϊ���ȵ����,����Ҫsum���е�cell,���ܵõ����յ�ֵ
		System.out.println(adder.name+" spend "+(System.currentTimeMillis()-adder.start)+" ms");
		es.shutdown();
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		LongAdderDemo demo=new LongAdderDemo();
		demo.testSync();
		demo.testAtomic();
		demo.testAdder();
	}

}

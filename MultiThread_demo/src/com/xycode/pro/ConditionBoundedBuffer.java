package com.xycode.pro;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
/**
 * ����ReentrantLock��Condition���н绷�λ���(Ӧ�Զ��̻߳���)
 */
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionBoundedBuffer<T> {
	private final Lock lock=new ReentrantLock();
	private final Condition notFull=lock.newCondition();
	private final Condition notEmpty=lock.newCondition();
	private final T[] items;
	private int head,tail,count;
	
	public ConditionBoundedBuffer(int buf_size) {
		items=(T[])new Object[buf_size];
		head=0;
		tail=0;
		count=0;
	}
	
	public void put(T item) throws InterruptedException {
		lock.lock();//lock��֤ĳһʱ��ֻ����һ���߳��޸Ļ���
		try {
			while(count==items.length) {//��������
				notFull.await();//����notFull(��Ҫ���put����),��ʱ�����������,��һֱ���������
			}
			items[tail++]=item;
			if(tail==items.length) {//���λ���
				tail=0;
			}
			++count;
			notEmpty.signal();//����ͨ��notEmpy(��Ҫ���take����),��ʱ������������,����take
		}finally {
			lock.unlock();
		}
	}
	
	
	public T take() throws InterruptedException {
		lock.lock();//lock��֤ĳһʱ��ֻ����һ���߳��޸Ļ���
		try {
			while(count==0) {
				notEmpty.await();//����notEmpty,��Ϊ�����Ѿ�����,������take
			}
			T item=items[head];
			items[head]=null;
			if(++head==items.length) {//���λ���
				head=0;
			}
			--count;
			notFull.signal();//����ͨ��notFull(��Ҫ���put����),��ʱ����put
			return item;
		}finally {
			lock.unlock();
		}
	}
	
	static final Random r=new Random();
	static class Task<T> implements Runnable{
		ConditionBoundedBuffer<T> cb;
		int mode;
		public Task(ConditionBoundedBuffer<T> cb,int mode) {
			super();
			this.cb = cb;
			this.mode=mode;
		}
		
		@Override
		public void run() {
			if(mode==1) {//mode==1,take...
				for(int i=0;i<1000;++i) {
					try {
						T item=cb.take();
//						System.out.println("take "+item);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}else {//else,put...
				for(int i=0;i<1000;++i) {
					Object o=r.nextInt(1000);
					//java�ķ����ǻ������Ͳ�����(���Ǳ��Object����)
					//���Է�������ֻ��ǿ��ת��Object����
					try {
						cb.put((T)o);
//						System.out.println("put "+o);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("Thread-"+Thread.currentThread().getId()+", has finished Task!");
			
		}
	}
	
	public static void main(String[] args) {
		ConditionBoundedBuffer<Integer> cb=new ConditionBoundedBuffer<>(1000);
		ExecutorService es=new TimingThreadPool
				(10, 20, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
		for(int i=0;i<1000;++i) {
			es.submit(new Task(cb, 1));
			es.submit(new Task(cb, 0));
		}
		
		es.shutdown();
		
	}

}

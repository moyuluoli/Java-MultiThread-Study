package com.xycode.LockWork;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/*
 * ֱ�Ӷ���ͨ�������Atomic��װ
 * AtomicIntegerFieldUpdate
 * AtomicLongFieldUpdate
 * AtomicReferenctFieldUpdate
 */
public class AtomicDemo2 {
	static class Candidate{
		int id;
		volatile int score;
		public Candidate(int id, int score) {
			super();
			this.id = id;
			this.score = score;
		}
		
	}
	
	static final AtomicIntegerFieldUpdater<Candidate> scoreUpdater
	=AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");//�������Candidate.score
	static AtomicInteger total_score=new AtomicInteger(0);
	
	public static void main(String[] args) throws InterruptedException {
		final Candidate stu=new Candidate(1,0);
		Thread[] t=new Thread[10000];
		for(int i=0;i<10000;++i) {
			t[i]=new Thread() {
				public void run() {
					if(Math.random()>0.4) {
						scoreUpdater.incrementAndGet(stu);
						total_score.incrementAndGet();
					}
				}
			};
			t[i].start();//����start�������ǵ��ø÷���ֻ��׼���̲߳�������������
		}
		for(int i=0;i<10000;++i) t[i].join();
		System.out.println("score = "+stu.score);
		System.out.println("total score = "+total_score);
		
	}

}

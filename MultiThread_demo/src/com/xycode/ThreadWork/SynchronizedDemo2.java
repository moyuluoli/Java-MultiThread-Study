package com.xycode.ThreadWork;

public class SynchronizedDemo2 implements Runnable{

	public static Integer i=0;
	public static SynchronizedDemo2 o=new SynchronizedDemo2();
	@Override
	public void run() {
		for(int j=0;j<10e6;++j) {
			synchronized (o) {
				++i;
			}
			
//			synchronized (i) {//���������,��ΪInteger�����ǲ����,++i����i��ָ�򲻶ϸı�,��˻������һ�µ�����,���ܼ����ӵ��˲�һ���Ķ���ʵ����
//				++i;
//			}
			
		}
	
	}
	public static void main(String[] args) throws InterruptedException {
		Thread t1=new Thread(o);
		Thread t2=new Thread(o);
		t1.start();t2.start();
		t1.join();t2.join();//join����start֮��
		
		System.out.println(i);
	}

}

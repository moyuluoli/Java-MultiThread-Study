package com.xycode.ThreadWork;
/*
 * synchronized(object):��ָ���������
 * synchronized returnType function(argv):��ʵ����������,�൱�ڸ�ʵ���������(synchronized(this){}����ʵ�����󻥳�)
 * synchronized static returnType function(argv):����̬��������,�൱�ڸ������(synchronized(object.class){})(��ͬ�಻ͬ��ʵ������Ҳ�ǻ����)
 */
public class SynchronizedDemo implements Runnable{
	public static int i=0;
	static SynchronizedDemo instance=new SynchronizedDemo();
	public static synchronized void inc() {//��ʵ����������
		++i;
	}
//	public synchronized void inc() {//����̬ʵ����������
//		++i;
//	}
	@Override
	public void run() {
		for(int j=0;j<10e6;++j) {
//			synchronized (instance) {
//				++i;
//			}
			
//			synchronized (this) {
//				++i;
//			}
			
			//static synchronized methodʵ������synchronized(object.class)�ȼ�
			inc();
//			synchronized (SynchronizedDemo.class) {
//				++i;
//			}
			
		}
	
	}
	public static void main(String[] args) throws InterruptedException {
		SynchronizedDemo s=new SynchronizedDemo();
		Thread t1=new Thread(s);
		Thread t2=new Thread(s);
		t1.start();t2.start();
		t1.join();t2.join();//join����start֮��
		
		System.out.println(i);
	}

}

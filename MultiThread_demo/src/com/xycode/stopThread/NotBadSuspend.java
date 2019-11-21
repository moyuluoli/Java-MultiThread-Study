package com.xycode.stopThread;
/*
 * ͨ����־�������˳�
 * ȱ��:���߳�����ʱ,������Զ���޷�ִ�е��жϱ�־���������д���,Ҳ���޷��˳���
 * (����:LinkedBlockingQueue<V>����������ʱ,���������������,��ʱ�߳̾ͻ�����)
 */
public class NotBadSuspend {
	public static Object o=new Object();
	public static class WriteThread extends Thread{
		volatile boolean suspendme=false;
		public volatile boolean flag=true;
		public void suspendMe() {
			suspendme=true;
		}
		
		public void resumeMe() {
			suspendme=false;
			synchronized (o) {
				o.notify();//notify��,�̲߳��������ͷ���
				try {
					System.out.println("WriteThread sleep 2s.(maintaining o's lock)");
					this.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}


		}
		@Override
		public void run() {
			while(true) {
				synchronized(o){
					while(suspendme) {
						try {
							o.wait();//wait,���ͷ���o����,suspendme==trueʱ,�߳̾�ͣ�������
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(flag) {//suspendme==falseʱ,�߳̾�����ִ��,��֮ǰwait��,����Ҫnotify�������߳�
						System.out.println("in WriteThread.");
						//flag=false;
					}
				}

			}

		}
	}
	
	public static class ReadThread extends Thread{
		public volatile boolean flag=true;
		@Override
		public void run() {
			while(true) {
				synchronized(o){
					if(flag) {
						System.out.println("in ReadThread.");
						flag=false;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		WriteThread t1=new WriteThread();
		ReadThread t2=new ReadThread();
		t1.start();
		t2.start();
		Thread.sleep(1000);
		t1.suspendMe();
		System.out.println("WriteThread suspend 2s.");
		Thread.sleep(2000);
		System.out.println("WriteThread resume.");
		t1.resumeMe();//resume��ȫִ����Ϻ�Ż��ͷ�o����
		
	}

}

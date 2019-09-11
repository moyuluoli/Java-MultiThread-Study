package com.xycode.stopThread;
/*
 * wait,notifyһ���Ǻ�synchronized����ʹ��
 * ���߳�T1��,object.wait(),T1�����wait���object,����T1���ͷ�object����
 * ������object.notify(),�̱߳����Ȼ��object��������notify
 * wait��notify������ĳ�����������õ�,���Ե��õĵ�ǰ�߳�������
 */

public class WaitAndNotify {
	final static Object o=new Object();
	public static class T1 implements Runnable{

		@Override
		public void run() {
			synchronized (o) {
				System.out.println(System.currentTimeMillis()+": T1 start.");
				try {
					System.out.println(System.currentTimeMillis()+": T1 wait for o.");
					o.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(System.currentTimeMillis()+": T1 end.");
			}
		}
		
	}
	
	public static class T2 implements Runnable{

		@Override
		public void run() {
			synchronized (o) {
				System.out.println(System.currentTimeMillis()+": T2 start,notify T1.");
				o.notify();
				System.out.println(System.currentTimeMillis()+": T2 end.");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
	public static void main(String[] args) {
		new Thread(new T1()).start();
		new Thread(new T2()).start();
		//T1���ӳ�2s��end,��ΪT2:o.notify()��,û���ͷ�o����,����sleep��2s,2s֮��T1�ŵõ�o����,��ʱT1���ܼ���ִ�в�end
	}

}

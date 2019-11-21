package com.xycode.ThreadWork;
/*
 * ��ν�ػ��̱߳����б��ػ����̲߳Ż�����,��û�б��ػ��߳�(���ػ��̼߳���)ʱ,�ػ��̱߳�����.
 */
public class DaemonDemo implements Runnable{
	public static volatile int i=0;
	@Override
	public void run() {
		while(true) {
			System.out.println("I am alive.");
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		for(i=0;i<10e7;++i);
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t=new Thread(new DaemonDemo());
		//�˴�main�߳�Ϊ�û�����,��t�����ػ�����,t����Զ������ȥ
		t.setDaemon(true);//setDaemon������start֮ǰ����,t���ػ��߳�,�˴������ػ�main�߳�
		//���Ե�main�߳�2s�������,�����ػ��߳�Ҳ��֮����,����t���ػ��̵߳Ļ�,main�̼߳�ʹ�˳���,t�߳�Ҳ��Զ����ִ��
		t.start();
		Thread.sleep(2000);
		
//		Thread t=new Thread(new DaemonDemo());
//		
//		t.start();
//		t.join();//join����start֮������,t.join��ʾmain�߳�Ը��ȴ��߳�tִ�����,����i�����С,���������ӵ���ֵmain�߳̾ͽ�����
//		System.out.println(i);
		
	}
	
}
package com.xycode.parallelModeAndAlgorithm;
/*
 * ����ģʽ
 * 
 */


public class Singleton {
	private Singleton() {//private�Ĺ��췽��,�����ⲿ���ö�δ���
		System.out.println("Singleton is created.");
	}
	/**
	 * ��instance��static,getInstanceҲ��static,
	 * ��ôִ��getInstance��ǰ���Ǳ�����һ��Singleton�Ķ�������,�����ɲ��������,
	 * ���µĺ����������ԶҲ�޷�����������ʵ��
	 * 
	 * ʵ����instance��ʱ�����ǲ�һ����,�����ǵ�һ��GetInstance()ʱ,���ߵ�һ��Ӧ��static����ʱ(�������STATUS)
	 */
	public static int STATUS=1;
	private static Singleton instance=new Singleton();
	public static Singleton GetInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
//		System.out.println(Singleton.STATUS);//���Կ���,��created,Ȼ���ӡ����STATUS
//		System.out.println(Singleton.GetInstance());//�����ֻ����instance,��������
		
		//��֤����ģʽȷʵ��Ч
		Thread[] t=new Thread[100];
		for(int i=0;i<100;++i) {
			t[i]=new Thread() {
				public void run() {
					try {
						System.out.println(Singleton.GetInstance());
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			};
			t[i].start();
		}
		for(int i=0;i<100;++i) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("end!");
	}
}

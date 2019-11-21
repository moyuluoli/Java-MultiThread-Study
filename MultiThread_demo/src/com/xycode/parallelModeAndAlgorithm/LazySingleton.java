package com.xycode.parallelModeAndAlgorithm;

public class LazySingleton {

	private LazySingleton() {//private�Ĺ��췽��,�����ⲿ���ö�δ���
		System.out.println("LazySingleton is created.");
	}
	/**
	 * ��instance��static,getInstanceҲ��static,
	 * ��ôִ��getInstance��ǰ���Ǳ�����һ��Singleton�Ķ�������,�����ɲ��������,
	 * ���µĺ����������ԶҲ�޷�����������ʵ��
	 * 
	 * ����ķ�ʽ�Ǳ�֤�ǵ�һ��GetInstance()ʱ��������,synchronized��ֹͬʱ�������,����Ҳ�������½���.
	 */
	private static LazySingleton instance=null;
	/**
	 * ʵ��֤��,��ȥ��synchronized,���̱߳Ƚ��ܼ�ʱ,���δ���
	 * 
	 */
	public synchronized static LazySingleton GetInstance() {
		if(instance==null) return (instance=new LazySingleton());
		else return instance;
	}
	
	public static void main(String[] args) {
//		System.out.println(LazySingleton.GetInstance());
		
		//��֤����ģʽȷʵ��Ч
		Thread[] t=new Thread[100];
		for(int i=0;i<100;++i) {
			t[i]=new Thread() {
				public void run() {
					try {
						System.out.println(LazySingleton.GetInstance());
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

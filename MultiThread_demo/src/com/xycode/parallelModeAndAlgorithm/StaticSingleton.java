package com.xycode.parallelModeAndAlgorithm;

public class StaticSingleton {
	private StaticSingleton() {//private�Ĺ��췽��,�����ⲿ���ö�δ���
		System.out.println("StaticSingleton is created.");
	}
	/**
	 * ��instance��static,getInstanceҲ��static,
	 * ��ôִ��getInstance��ǰ���Ǳ�����һ��Singleton�Ķ�������,�����ɲ��������,
	 * ���µĺ����������ԶҲ�޷�����������ʵ��
	 * 
	 * ʵ����instance��ʱ�����ǲ�һ����,�����ǵ�һ��GetInstance()ʱ,���ߵ�һ��Ӧ��static����ʱ,
	 * �������ַ�ʽ��������������ȱ����ʵ��ֻ�ڵ�һ��GetInstance()ʱ����
	 */
	public static int STATUS=1;
	private static class SingletonHolder{//private,ʹ�ⲿ�޷�����,������������������ڲ�����
		private static StaticSingleton instance=new StaticSingleton();
	}
	
	public static StaticSingleton GetInstance() {
		return SingletonHolder.instance;
	}
	public static void main(String[] args) {
		System.out.println(StaticSingleton.STATUS);//ֻ��ӡ��STATUS,֤����ʱû�д�������
//		System.out.println(StaticSingleton.GetInstance());//��ʱ��ʹ���������
		
		//��֤����ģʽȷʵ��Ч
		Thread[] t=new Thread[100];
		for(int i=0;i<100;++i) {
			t[i]=new Thread() {
				public void run() {
					try {
						System.out.println(StaticSingleton.GetInstance());
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

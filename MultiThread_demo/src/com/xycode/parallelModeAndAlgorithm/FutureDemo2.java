package com.xycode.parallelModeAndAlgorithm;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


public class FutureDemo2 {
	static class RealData implements Callable<String>{//jdk֧�ֵ�futureģʽ
		String para;
		public RealData(String para) {
			super();
			this.para = para;
		}

		@Override
		public String call() throws Exception {
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<10;++i) {
				sb.append(para);
				Thread.sleep(100);
			}
			return sb.toString();
		}
		
	}
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		FutureTask<String> future1=new FutureTask<>(new RealData("xycode "));//jdk֧�ֵ�futureģʽ
		FutureTask<String> future2=new FutureTask<>(new RealData("xycodec "));
		ExecutorService es=Executors.newCachedThreadPool();
		es.submit(future1);
		es.submit(future2);
		System.out.println("Request Finished!");
		
		//������Դ�������ҵ���߼�
		System.out.println("Handle Others...");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Handle Others Finished!");
		
		//jdk�ṩ��future���ģʽ������,FutureTask<V> ,Callable<V>
		System.out.println("Data = "+future1.get());//�����ݻ�û���,�������һֱ����,ֱ������׼�����
		System.out.println("Data = "+future2.get());

	}

}

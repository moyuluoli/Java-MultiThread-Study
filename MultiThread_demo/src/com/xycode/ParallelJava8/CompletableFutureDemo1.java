package com.xycode.ParallelJava8;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class CompletableFutureDemo1 {
	static class AskThread implements Runnable{
		CompletableFuture<Integer> re=null;
		
		public AskThread(CompletableFuture<Integer> re) {
			super();
			this.re = re;
		}

		@Override
		public void run() {
			int result=0;
			try {
				result=re.get()*re.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			System.out.println(result);
		}
	}
	
	public static int calc(int para) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return para*para;
	}
	
	public static int div(int a,int b) {
		return a/b;
	}
	
	public static void f() {
		;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		/*
//		 * 1.����һ��CompletableFutureʵ��
//		 * 2.�½�һ���߳�,��CompletableFutureʵ����Ϊ����,Ȼ��ִ��(run()������ҪFuture.get())
//		 */
//		final CompletableFuture<Integer> fu=new CompletableFuture<>();
//		new Thread(new AskThread(fu)).start();
//		System.out.println("sleep...");
//		Thread.sleep(2000);
//		fu.complete(100);//�������(��Ӧget())
////		fu.complete(200);
		
		
//		//������д��
//		final CompletableFuture<Integer> fu=CompletableFuture.supplyAsync(()->calc(100));
//		/*
//		 * CompletableFuture<U> supplyAsync(Supplier<U> supplier)
//		 * Supplier��һ�������ӿ�
//		 * 
//		 * @FunctionalInterface
//			public interface Supplier<T> {
//    			T get();
//			}
//			Functional Interface: ����һ�����ܽ��棬��������lambda���ʽ�򷽷����õĸ�ֵ���� 
//		 */
//		System.out.println("calculating...");
//		System.out.println(fu.get());
		
		
		//CompletableFuture���쳣����,��ʽ����
		CompletableFuture<Void> fu=CompletableFuture//��Ȼdiv�з���ֵ,���Ǿ���һϵ�е���ʽ���ú�,��û�з���ֵ��
				.supplyAsync(()->div(12,0))
				.exceptionally(exception->{
					exception.printStackTrace();
					return 0;
				})
				.thenApply(i->Integer.toString(i))
				.thenApply(str->("\""+str+"\""))
				.thenAccept(System.out::println);
		System.out.println(fu.get());//fu.get()����null,ʵ���Ϸ���ֵΪvoid(���߽��޷���ֵ)�ǲ��ܴ�ӡ��
				
	}

}

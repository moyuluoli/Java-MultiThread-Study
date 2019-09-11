package com.xycode.parallelModeAndAlgorithm;
/**
 * ��������,�Ƚ����зֶ�,ÿ�η���һ���߳�ȥ����
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


public class ParallelSearch {
	static AtomicInteger result=new AtomicInteger(-1);//ԭ����
	static final int Thread_num=10;
	static ExecutorService es=Executors.newCachedThreadPool();
	static int[] arr;
	static class SearchTask implements Callable<Integer>{
		int begin,end,searchValue;

		public SearchTask(int searchValue,int begin, int end) {
			super();
			this.begin = begin;
			this.end = end;
			this.searchValue = searchValue;
		}

		@Override
		public Integer call() throws Exception {
			return search(searchValue, begin, end);
		}
		
	}
	public static int search(int searchValue,int begin,int end) {//����ֵ���ڵ�����
		for(int i=begin;i<end;++i) {
			if(result.get()>=0) return result.get();
			if(arr[i]==searchValue) {
				if(result.compareAndSet(-1, i)==false) {//CASģʽ����
					return result.get();
				}else {
					return i;
				}
			}
		}
		return -1;//û�ҵ�,�ͷ���-1
	}
	
	public static int pSearch(int searchValue) throws InterruptedException, ExecutionException {
		int subSize=arr.length/Thread_num+1;
		List<Future<Integer>> re=new ArrayList<>();//������ķ���ֵ��ƾ֤,Futureģʽ
		for(int i=0;i<arr.length;i+=subSize) {
			int end=i+subSize;
			if(end>=arr.length) end=arr.length;
			//submit�з���ֵ,ΪFuture<V>,�ύ���̳߳���ȥ����
			re.add(es.submit(new SearchTask(searchValue,i, end)));
		}
		for(Future<Integer> fu:re) {
			if(fu.get()>=0) return fu.get();//get(),Futureģʽ,������ǰ��ƾ֤ȥ�������Ľ��,��û�����,�ͻ�һֱ�ȴ�
		}
		return -1;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Random r=new Random();
		arr=new int[2000000];
		for(int i=0;i<2000000;++i) {
			arr[i]=r.nextInt(2000000);
		}
		for(int i=0;i<100;++i) {
			int searchValue=r.nextInt(2000000);
			System.out.println("searchValue = "+searchValue+", pos = "+pSearch(searchValue));
			result=new AtomicInteger(-1);//����һ�κ�Ҫ��ʱ���״̬
		}
	}

}

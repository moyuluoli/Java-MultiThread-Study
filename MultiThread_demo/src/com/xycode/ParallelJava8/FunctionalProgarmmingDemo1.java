package com.xycode.ParallelJava8;

import java.util.Arrays;

public class FunctionalProgarmmingDemo1 {

	public static void main(String[] args) {
		int[] arr= {1,2,3,4,5};
		Arrays.stream(arr).map(x->x+1).forEach(System.out::print);
		System.out.println();
		Arrays.stream(arr).forEach(System.out::print);//����ʽ��̼�������ı������ݵĶ���,����Ȼ�ʺϲ��л�
	}

}

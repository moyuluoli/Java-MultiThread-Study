package com.xycode.DH;

import java.util.Random;

public class Constant {
	public final static int a=5;
	public final static int p=23;
	public static Random r=new Random();
	public static int get(int private_key) {//�����Կ��������е��м���
		return ((int)Math.pow(a,private_key))%p;
	}

	public static int get(int f, int s) {//�������յ���Կ
		return ((int)Math.pow(f,s))%p;
	}
}

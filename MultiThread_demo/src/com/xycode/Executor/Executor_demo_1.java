package com.xycode.Executor;
/**
 * ʵ������Ĵ���(����)�������ִ�з���
 * @author xycode
 *
 */
public class Executor_demo_1 {

	public static void main(String[] args) {
		MyChannel channel=new MyChannel(10);
		channel.start();
		new MyClient("Alice",channel).start();
		new MyClient("Bob",channel).start();
		new MyClient("Charis",channel).start();
		
	}

}

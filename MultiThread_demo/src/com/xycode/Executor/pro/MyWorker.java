package com.xycode.Executor.pro;

public class MyWorker extends Thread{
	private final MyChannel channel;
	public MyWorker(String name,MyChannel channel) {
		super(name);
		this.channel = channel;
	}

	public void cancel() {
		this.interrupt();
	}
	
	@Override
	public void run() {
		while(true) {
			if(Thread.currentThread().isInterrupted()) {//�ж�ʽ��ֹͣ,��Ϊ��ȫ
				System.out.println(Thread.currentThread().getName()+" interrupted, exit!");
				break;
			}
			Request request=channel.takeRequest();
			request.task();
		}
	}
	
}

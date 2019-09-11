package com.xycode.stopThread;

/*
 * �����жϻ�������ȫ���˳��߳�,��Ҫע����ǵ�һ���߳��յ�һ���ж��ź�ʱ,�̲߳����������˳�,ֻ�Ǳ���ϣ������߳��˳���,
 * �����˳����,���ɸ��߳����о���
 */
public class StopThreadSafe {
	public static class User{
		private int id;
		private String name;
		public User() {
			id=0;
			name="0";
		}
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "id="+id+", name="+name;
		}
	
	}
	public static User u=new User();
	
	public static class WriteThread extends Thread{
		@Override
		public void run() {
			while(true) {
				if(this.isInterrupted()) {//�жϵ�ǰ�߳��Ƿ��ж�,interrupted()�����ж��Ƿ��ж�,���������ǰ�ն�״̬
					System.out.println("Thread-"+this.currentThread().getId()+" interruted!");
					break;
				}
				synchronized(u){
					int v=(int)(System.currentTimeMillis()/1000);
					u.setId(v);
//					try {
//						Thread.sleep(100);
//					}catch (Exception e) {
//						e.printStackTrace();
//					}
					for(int i=0;i<10e8;++i);
					u.setName(String.valueOf(v));
				}
				this.yield();//�̼߳��һ��ǫ�û���
			}

		}
	}
		
		
	public static class ReadThread implements Runnable{
		@Override
		public void run() {
			while(true) {
				synchronized(u){
					if(u.getId()!=Integer.parseInt(u.getName())) {
						System.out.println(u);
					}
				}
				Thread.yield();//�̼߳��һ��ǫ�û���
			}

		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		(new Thread(new ReadThread())).start();
		while(true) {
			Thread t=new WriteThread();
			t.start();
			Thread.sleep(200);
			t.interrupt();//main�̸߳��߳�t���ж��ź�,ϣ����ֹͣ����
		}

	}
	
}

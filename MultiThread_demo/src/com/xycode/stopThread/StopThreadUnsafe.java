package com.xycode.stopThread;
/* Thread.stop()�ǲ���ȫ��,���ܻ��������ݲ�һ�µ����
 * extends Thread��implements Runnable����Ҫ�����ǣ�
 * 1.Thread���࣬Runnable�ǽӿڣ���һ���߳������extends Thread�ķ�ʽ��ʵ�֣�����java��֧�ֶ�̳У�����߳��ཫ�޷��ڼ̳������ࡣ
 * ��implements Runnable��û����������
 * 2.extends Thread�ķ�ʽ����ʹ���߳���ֱ��ʹ��Thread��һЩ������this.xxx����
 */
public class StopThreadUnsafe{
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
				synchronized(u){
					int v=(int)(System.currentTimeMillis()/1000);
					u.setId(v);
					try {
						this.sleep(100);
					}catch (Exception e) {
						e.printStackTrace();
					}
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
			t.stop();//һ�ַ����ķ���,��Ϊ�˳��Ļ��Ʋ���ȫ,���ܳ������ݲ�һ��.
		}

	}

}

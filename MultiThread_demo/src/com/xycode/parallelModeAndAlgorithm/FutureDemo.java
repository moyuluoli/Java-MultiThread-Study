package com.xycode.parallelModeAndAlgorithm;
/**
 * Futureģʽ��ʵ��
 * @author xycode
 *
 */
public class FutureDemo {
	static interface Data{
		public String getResult();
	}
	static class RealData implements Data{
		String result;		
		public RealData(String para,int count) {//��ʵ����,���ɵ��ٶȽ���
			if(count<0) {
				throw new NegativeArraySizeException(String.valueOf(count));
			}
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<count;++i) {
				sb.append(para);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			result=sb.toString();
		}
		@Override
		public String getResult() {
			return result;
		}
		
	}
	static class FutureData implements Data{
		RealData realData=null;
		boolean isReady=false;
		public synchronized void setRealData(RealData realData) {//�ȼ���synchronized(this){}
			if(isReady) return; //�Ѿ�׼����������,�Ͳ����ټ�����,ʵ��������һ�ֱ���,Ҳ���̰߳�ȫ��
			this.realData=realData;
			isReady=true;
			this.notifyAll();
//			synchronized(this) {
//				this.notifyAll();
//			}
			
		}
		@Override
		public String getResult() {
			while(!isReady) {
				try {
					synchronized(this) {
						this.wait();//this����ֻ�����ڵ�ǰ���õĵ���ʵ��
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return realData.result;
		}
		
	}
	
	static class Client{
		FutureData future=new FutureData();
		public Data request(final String queryStr,final int count) {
			new Thread() {
				@Override
				public void run() {
					try {
						RealData realData=new RealData(queryStr,count);//�����ļ������
						future.setRealData(realData);//������˾ͽ�������õ�FutureData	
					}catch (Exception e) {
						System.err.println("illegal negative size!");
						e.printStackTrace();
					}
			
				}
			}.start();
			return future;//���ﷵ�صĲ�������ʵ������,����һ��ƾ֤
		}
	}
	
	public static void main(String[] args) {
		Client client1=new Client();
		Client client2=new Client();

		Data data1=client1.request("xycode ",10);//��������������һ��Futureƾ֤
		Data data2=client2.request("xycodec ",20);//��������������һ��Futureƾ֤

		System.out.println("Request Finished!");
		//������Դ�������ҵ���߼�
		System.out.println("Handle Others...");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Handle Others Finished!");
		System.out.println("RealData = "+data1.getResult());//�������ȷ����ʵ�����Ѿ��������,����ͻ�һֱ������ȴ�,֪����ʵ�����������
		System.out.println("RealData = "+data2.getResult());//�ܿ���,����������ǲ��в�����
	}

}

package com.xycode.threadlocal;
/**
 * ThreadLocalģʽ,Ϊÿ���̷߳���TSLog,������
 * @author xycode
 *
 */
public class LogProxy {
	private static final ThreadLocal<TSLog> tslogCollection=new ThreadLocal<>();
	
	private static TSLog getTSLog() {
		TSLog tsLog=tslogCollection.get();//����ݵ�ǰ�߳���������Ϣ����ȡ���ݶ���
		if(tsLog==null) {//֮ǰû�з���TSLog�Ļ��ͷ���һ��,�����ֱ�ӷ��ؽ��
			tsLog=new TSLog(Thread.currentThread().getName()+"-log.txt");
			tslogCollection.set(tsLog);
		}
		return tsLog;
	}
	
	public static void println(String s) {
		getTSLog().println(s);
	}
	
	public static void close() {
		getTSLog().close();
	}
}

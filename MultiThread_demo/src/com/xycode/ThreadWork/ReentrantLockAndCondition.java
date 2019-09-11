package com.xycode.ThreadWork;
/*
 * Condition[await(),signal()]һ���ReentrantLock[lock(),unlock()]����ʹ��
 * await(),signal()������������wait(),notify()
 */
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockAndCondition implements Runnable{
	static ReentrantLock lock=new ReentrantLock();
	static Condition condition=lock.newCondition();
	
	@Override
	public void run() {
		
		try {
			lock.lock();
			System.out.println("Thread-"+Thread.currentThread().getId()+" is waitting...");
			condition.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
			System.out.println("Thread-"+Thread.currentThread().getId()+" exit!");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1=new Thread(new ReentrantLockAndCondition());
		t1.start();
		t1.sleep(2000);
		
		lock.lock();
		condition.signal();//���������(��condition������lock)�ſ���signal
		lock.unlock();
	}

}

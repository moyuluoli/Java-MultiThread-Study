package com.xycode.parallelModeAndAlgorithm;

import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
public class DisruptorDemo {
	static class PCData{
		long value;

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}
		
	}
	
	static class PCDataFactory implements EventFactory<PCData>{
		@Override
		public PCData newInstance() {
			return new PCData();
		}
		
	}
	
	static class Producer{
		RingBuffer<PCData> ringBuffer;

		public Producer(RingBuffer<PCData> ringBuffer) {
			super();
			this.ringBuffer = ringBuffer;
		}
		
		public void pushData(ByteBuffer bb) {
			long sequence=ringBuffer.next();//Increment and return the next sequence for the ring buffer
			PCData event=ringBuffer.get(sequence);
			event.setValue(bb.getLong(0));//index=0,��ʼ��ȡ������
			ringBuffer.publish(sequence);
		}
		
	}
	
	static class Comsumer implements WorkHandler<PCData>{

		@Override
		public void onEvent(PCData event) throws Exception {
			System.out.print("Consumer-Thread-"+Thread.currentThread().getId()+" start, ");
			System.out.println(MessageFormat.format("Compute {0}*{1}={2}",
					event.getValue(),event.getValue(),event.getValue()*event.getValue()));
		}
		
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService es=Executors.newCachedThreadPool();
		PCDataFactory factory=new PCDataFactory();
		int bufferSize=2048;
		Disruptor<PCData> disruptor=new Disruptor<>(factory, bufferSize, es);
		disruptor.handleEventsWithWorkerPool(
				new Comsumer(),new Comsumer(),new Comsumer(),new Comsumer());
		disruptor.start();
		
		RingBuffer<PCData> ringBuffer=disruptor.getRingBuffer();//disruptor����Consumer�߳�
		ByteBuffer bb=ByteBuffer.allocate(8);//8���ֽ�,�պ���Long������ռ���ֽڳ���
		Producer producer=new Producer(ringBuffer);//ringBuffer��Producer��Consumer��������
		for(int i=0;i<100;++i) {
			System.out.println("Producer add data "+i);
			bb.putLong(0,i);//��ByteBuffer�����long���͵�����(ֵΪi),longռ8 byte
			producer.pushData(bb);
			Thread.sleep(50);
		}
		
	}

}

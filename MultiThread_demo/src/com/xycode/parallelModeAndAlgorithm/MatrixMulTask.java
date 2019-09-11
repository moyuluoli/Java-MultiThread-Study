package com.xycode.parallelModeAndAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import org.jmatrices.dbl.Matrix;
import org.jmatrices.dbl.MatrixFactory;
import org.jmatrices.dbl.operator.MatrixOperator;

public class MatrixMulTask extends RecursiveTask<Matrix>{//ForkAndJoin���,RecursiveTask<V>���з���ֵ��
	Matrix m1,m2;
	String pos;
	static final int Mul_granularity=8;
	public MatrixMulTask(Matrix m1, Matrix m2, String pos) {
		super();
		this.m1 = m1;
		this.m2 = m2;
		this.pos = pos;
	}

	@Override
	protected Matrix compute() {
		//���ϵؽ���ľ���ֽ�ΪС�ľ������,��Ϊÿ��С�������forkһ���߳�������
		if(m1.rows()<=Mul_granularity||m2.cols()<=Mul_granularity) {
			return MatrixOperator.multiply(m1, m2);
		}else {
			//����rows,����cols
			Matrix m11=m1.getSubMatrix(1, 1, m1.rows()/2, m1.cols());//����m1���ϰ벿��
			Matrix m12=m1.getSubMatrix(m1.rows()/2+1, 1, m1.rows(), m1.cols());//����m1���°벿��
			
			Matrix m21=m2.getSubMatrix(1, 1, m2.rows(), m2.cols()/2);//����m2����벿��
			Matrix m22=m2.getSubMatrix(1, m2.cols()/2+1, m2.rows(), m2.cols());//����m2����벿��
			
			ArrayList<MatrixMulTask> subTasks=new ArrayList<>();
			subTasks.add(new MatrixMulTask(m11, m21, "m1"));
			subTasks.add(new MatrixMulTask(m11, m22, "m2"));
			subTasks.add(new MatrixMulTask(m12, m21, "m3"));
			subTasks.add(new MatrixMulTask(m12, m22, "m4"));
			
			for(MatrixMulTask t:subTasks) {
				t.fork();//forkһ���߳�������
			}
			
			Map<String,Matrix> matrix_result=new HashMap<>();//Stringԭ��֧��Hash��equals,����������
			for(MatrixMulTask t:subTasks) {
				matrix_result.put(t.pos, t.join());//join���ؾ�����,����pos��ȷ�����ǿ������
			}
			
			Matrix tmp1=MatrixOperator.horizontalConcatenation(matrix_result.get("m1"), matrix_result.get("m2"));//ˮƽ����ϲ�����
			Matrix tmp2=MatrixOperator.horizontalConcatenation(matrix_result.get("m3"), matrix_result.get("m4"));
			
			Matrix result=MatrixOperator.verticalConcatenation(tmp1, tmp2);//��ֱ����ϲ�����,�õ������Ľ��
			return result;
		}
	}
	
	public static void main(String[] args) {
		ForkJoinPool pool=new ForkJoinPool();
		Matrix m1=MatrixFactory.getRandomMatrix(512, 256, null);
		Matrix m2=MatrixFactory.getRandomMatrix(256, 512, null);
		MatrixMulTask task=new MatrixMulTask(m1,m2,null);
		ForkJoinTask<Matrix> result=pool.submit(task);//futureģʽ
		try {
			System.out.println(result.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
	}

}

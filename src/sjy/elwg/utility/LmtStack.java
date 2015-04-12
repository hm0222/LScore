package sjy.elwg.utility;

import java.util.Deque;
import java.util.LinkedList;

/**
 * �����������Ƶ�ջ
 * ѹջʱ���ջ�����ﵽ���ޣ���ջ�����ϵ�Ԫ�ؼ���ջ�����ѹջһ���ܳɹ�.
 * ��ջʱ���ջΪ�գ��򷵻�null.
 * @author jingyuan.sun
 *
 * @param <T>
 */
public class LmtStack<T> {
	
	private static final int CAPACITY = 10; //ջ����
	
	private Deque<T> deque = new LinkedList<T>();
	
	private int capacity = CAPACITY;
	
	public LmtStack(){
		
	}
	
	public LmtStack(int capacity){
		this.capacity = capacity;
	}
	
	public void push(T obj){
		deque.offerLast(obj);
		while(deque.size() > capacity){
			deque.pollFirst();
		}
	}
	
	public T poll(){
		return deque.pollLast();
	}
	
	public int size(){
		return deque.size();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
}

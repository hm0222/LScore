package sjy.elwg.notation.musicBeans.symbolLines;

import java.util.ArrayList;

/**
 * ���зֽӿڣ�
 * ʵ���˸ýӿڵ�������зֹ��ܣ�����һ��ʵ���з�Ϊ�����໥������ʵ��. 
 * һ������ʹ��Ϊ�����е���������,ÿ���������Ź����������������Щ�������ڲ�ͬ�е�ʱ��ÿ�����Żᱻ�з�Ϊ����Ƭ�Σ������ڲ�ͬ������.
 * @author jingyuan.sun
 *
 */
public interface Splitable {
	
	/**
	 * �����з�
	 * @param num �зֵ���Ŀ
	 * @return
	 */
	public ArrayList<AbstractLine> split(int num);

}

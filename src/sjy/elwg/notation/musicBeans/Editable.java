package sjy.elwg.notation.musicBeans;

/**
 * �ɱ༭�ӿ�
 * �������пɱ༭����ʵ�ָýӿ�.�ýӿ�Ҫ����ʵ�ֵ�����ָ�����벻ͬ״̬��ִ�еĲ���.
 * @author jingyuan.sun
 *
 */
public interface Editable {
	
	/**
	 * ״̬����,������ͼ״̬�ͱ༭״̬
	 */
	public static final int VIEW_MODE = 0;
	public static final int EDIT_MODE = 1;
	
	/**
	 * ����༭״̬
	 */
	public void editMode();
	
	/**
	 * ����۲�״̬
	 */
	public void viewMode();

}

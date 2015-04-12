package sjy.elwg.notation.musicBeans;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * ����
 * @author sjy
 *
 */
public class Stem extends JPanel {
	
	/**
	 * ���˵����ͳ���
	 */
	public static final int NORMAL = 10;
	public static final int GRACE = 11;
	/**
	 * �߶ȳ���
	 */
	public static final int NORMAL_HEIGHT = 35;
	public static final int GRACE_HEIGHT = 22;
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 9L;
	
	/**
	 * ����
	 */
	private int type;
	
	/**
	 * ���˿��
	 */
	private int stemWidth = 2;
	
	/**
	 * �չ��캯��
	 */
	public Stem(){
		super();
		type = NORMAL;
		setSize(stemWidth, NORMAL_HEIGHT);
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	/**
	 * ���캯��
	 * @param type ��������
	 */
	public Stem(int type){
		super();
		this.type = type;
		adjustHeight();
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	/**
	 * �����߶�
	 */
	public void adjustHeight(){
		if(type == NORMAL)
			setSize(2, NORMAL_HEIGHT);
		else if(type == GRACE)
			setSize(2, GRACE_HEIGHT);
	}
	
	public void paintComponent(Graphics g){
		g.drawLine(0, 0, 0, getHeight());
	}
	
	/**
	 * ���ص�ǰ���˵�Ĭ�ϳ���
	 * @return
	 */
	public int getDefaultHeight(){
		if(type == GRACE)
			return GRACE_HEIGHT;
		else if(type == NORMAL)
			return NORMAL_HEIGHT;
		return -1;
	}

}

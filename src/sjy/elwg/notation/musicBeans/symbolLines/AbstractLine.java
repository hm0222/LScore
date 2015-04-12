package sjy.elwg.notation.musicBeans.symbolLines;

import javax.swing.JPanel;

/**
 * �������������ڵ��������ŵĸ���.
 * �̳��Ը����������е��ص㣺�ɱ��з֣������ſ������ڻ��е��������һ��Ϊ��.
 * @author sjy
 *
 */
public abstract class AbstractLine extends JPanel implements Splitable{
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = -7290983329762122391L;
	/**
	 * �ֱ�Ϊ�༭��ѡ����ͨ��״ֵ̬.
	 */
	public static final int EDIT_STATUS = 0;
	public static final int SELECT_STATUS = 1;
	public static final int NORMAL_STATUS = 2;
	/**
	 * �����������ŵ�����
	 */
	public static final int TIE = 0;
	public static final int SLUR = 1;
	public static final int VIB = 2;
	public static final int OCTAVE_UP = 3;
	public static final int OCTAVE_DOWN = 4;
	public static final int CRE = 5;
	public static final int DIM = 6;
	public static final int PEDAL = 7;
	public static final int DIMC = 8;
	public static final int CRESC = 9;
	public static final int REPEATENDING = 10;
	
	
	/**
	 * ��ǰ״ֵ̬
	 */
	protected int status = NORMAL_STATUS;
	
	/**
	 * �зֺ���ŵ�ǰһ���֣�û���зֻ���Ϊ�зֺ�ĵ�һ���� ��Ϊnull.
	 */
	protected AbstractLine preSymbolLine;
	
	/**
	 * �зֺ���ŵ���һ���֣�û���зֻ���Ϊ�зֺ�����һ���� ��Ϊnull.
	 */
	protected AbstractLine nextSymbolLine;
	
	/**
	 * ���캯��
	 */
	public AbstractLine(){
		super();
		setOpaque(false);
		setLayout(null);
	}
	
	/**
	 * ������״���С
	 */
	public abstract void reShape();
	
	/**
	 * ���󷽷������ݿ�ȵ������Ŵ�С.
	 * ��Ȳ����Ƿ�����������������(���߿�)֮��ı�׼��ȣ�������ű��û��ֶ��ı��С��
	 * ��������д�÷���ʱ��Ҫ����С�ñ������ǽ�ȥ.
	 * @param x
	 */
	public abstract void reSize(int x);
	
	/**
	 * ���󷽷�,������������
	 * @param line ��
	 * @param measureIndex �ױ�ID
	 */
	public abstract void reLocate();

	/**
	 * ���ǰһ������
	 * @return
	 */
	public AbstractLine getPreSymbolLine() {
		return preSymbolLine;
	}

	/**
	 * ����ǰһ������
	 * @param preSymbolLine
	 */
	public void setPreSymbolLine(AbstractLine preSymbolLine) {
		this.preSymbolLine = preSymbolLine;
	}

	/**
	 * ��ú�һ������
	 * @return
	 */
	public AbstractLine getNextSymbolLine() {
		return nextSymbolLine;
	}

	/**
	 * ���ú�һ������
	 * @param nextSymbolLine
	 */
	public void setNextSymbolLine(AbstractLine nextSymbolLine) {
		this.nextSymbolLine = nextSymbolLine;
	}

	/**
	 * ��õ�ǰ״̬
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * ���õ�ǰ״̬
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}

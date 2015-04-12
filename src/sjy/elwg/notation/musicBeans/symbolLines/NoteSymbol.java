package sjy.elwg.notation.musicBeans.symbolLines;

import javax.swing.JPanel;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Selectable;
/**
 * �����࣬��������,��������������װ�η��ţ����ȷ��ŵ�.
 * ʵ����װ���������ȷ��ŵȷ����������ƣ������ǵ�ʹ��ϰ�ߣ������Ƿ�Ϊ��ͬ����.
 * ������Ϊ��Щ����ĸ���.
 * ֮���Բ��ó����࣬��Ϊ�˱���Ӹ���ֱ�Ӵ������Ŷ���.
 * @author jingyuan.sun
 *
 */
public abstract class NoteSymbol extends JPanel implements Selectable{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = -2626826446968113902L;
	/**
	 * �Ƿ�ѡ��
	 */
	protected boolean selected;
	/**
	 * ������������.
	 */
	protected String symbolType;
	
	/**
	 * ��÷��������������
	 */
	protected AbstractNote note;
	
	/**
	 * ���û����϶�����x�����y����
	 * �ڽ��з��ŷ���ʱ����λ����Ĭ��λ�����϶�λ��֮��
	 */
	protected int draggedX = 0;
	protected int draggedY = 0;
	
	/**
	 * Ĭ�Ϲ��캯��
	 * @param type
	 */
	public NoteSymbol(String type){
		super();
		this.symbolType = type;
		setOpaque(false);
		adjustSize();
//		repaint();
	}
	
	/**
    /**
	 * ���󷽷���������С
	 */
	public abstract void adjustSize();


	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getSymbolType() {
		return symbolType;
	}

	public void setSymbolType(String symbolType) {
		this.symbolType = symbolType;
	}

	public AbstractNote getNote() {
		return note;
	}

	public void setNote(AbstractNote note) {
		this.note = note;
	}

	public int getDraggedY() {
		// TODO Auto-generated method stub
		return draggedY;
	}
	
	public int getDraggedX() {
		return draggedX;
	}
	
}

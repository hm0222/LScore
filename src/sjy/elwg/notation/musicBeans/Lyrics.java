package sjy.elwg.notation.musicBeans;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.event.DocumentListener;


/**
 * ���
 * @author jingyuan.sun
 *
 */
public class Lyrics extends FreeTextField implements Editable, DocumentListener,MouseMotionListener,MouseListener,Selectable{
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = -6275980176199532122L;
	
	/**
	 * �������������
	 */
	private AbstractNote note;
	
	/**
	 * ���캯��
	 *
	 */
	public Lyrics(){
		super();
	}
	
	/**
	 * ���캯��
	 * @param str
	 */
	public Lyrics(String str){
		super(str);
	}
	
	/**
	 * ������������
	 */
	public void reSize(){	
		if(getText() == null || getText().equalsIgnoreCase("")){
			setSize(Note.NORMAL_HEAD_WIDTH/2, LYRIC_FONT.getSize() + 4);
		}
		else{
			int width = getFontMetrics(getFont()).stringWidth(getText())+5;
			int height = getFont().getSize() + 4;
			setSize(width , height);
		}
	}

	/**
	 * ���ظ�����ڵ����� 
	 * @return
	 */
	public AbstractNote getNote() {
		return note;
	}

	/**
	 * ��������
	 * @param note
	 */
	public void setNote(AbstractNote note) {
		this.note = note;
	}

}

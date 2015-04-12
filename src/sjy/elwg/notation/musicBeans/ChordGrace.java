package sjy.elwg.notation.musicBeans;

public class ChordGrace extends ChordNote{

	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 26979727663461315L;
	
	/**
	 * �Ƿ����б�ߣ����Ƿ��Ƕ�����
	 */
	private boolean hasSlash;
	
	/**
	 * ��������������
	 */
	private AbstractNote note;
	
	/**
	 * ���캯��
	 * @param note
	 */
	public ChordGrace(Note note, boolean hasSlash) {
		super(note);
		this.hasSlash = hasSlash;
		// TODO Auto-generated constructor stub
	}

	/**
	 * �Ƿ��Ƕ�����
	 * @return
	 */
	public boolean isHasSlash() {
		return hasSlash;
	}

	/**
	 * ���ö�����
	 * @param hasSlash
	 */
	public void setHasSlash(boolean hasSlash) {
		this.hasSlash = hasSlash;
	}

	/**
	 * �����������
	 * @return
	 */
	public AbstractNote getNote() {
		return note;
	}

	/**
	 * ������������
	 * @param note
	 */
	public void setNote(AbstractNote note) {
		this.note = note;
	}

}

package sjy.elwg.utility;

/**
 * ����ĳ��ʱ��ʵ������࣬��������ʱ�Ĳ���
 * @author jingyuan.sun
 *
 */
public class NoteLineStrategy {
	
	/**
	 * С������Ʋ���
	 */
	private MeaPartStrategy mpst;
	/**
	 * �����Ƶ��й���ϡ��ʱ���Ƿ�����������һ�еĲ���С����
	 */
	private boolean isDrawBack;
	
	/**
	 * ���캯��
	 * @param mpst С������Ʋ���
	 * @param isDrawBack �Ƿ�����������һ��
	 */
	public NoteLineStrategy(MeaPartStrategy mpst, boolean isDrawBack){
		this.mpst = mpst;
		this.isDrawBack = isDrawBack;
	}
	
	public NoteLineStrategy(NoteLineStrategy nls){
		this.mpst = new MeaPartStrategy(nls.getMpst());
		this.isDrawBack = nls.isDrawBack();
	}

	/**
	 * ���С������Ʋ���
	 * @return
	 */
	public MeaPartStrategy getMpst() {
		return mpst;
	}

	/**
	 * ����С�������
	 * @param mpst
	 */
	public void setMpst(MeaPartStrategy mpst) {
		this.mpst = mpst;
	}

	/**
	 * ����Ƿ���������
	 * @return
	 */
	public boolean isDrawBack() {
		return isDrawBack;
	}

	/**
	 * ������������
	 * @param isDrawBack
	 */
	public void setDrawBack(boolean isDrawBack) {
		this.isDrawBack = isDrawBack;
	}
	
}

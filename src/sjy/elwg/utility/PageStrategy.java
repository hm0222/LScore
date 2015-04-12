package sjy.elwg.utility;

/**
 * ����ĳҳʱ�Ĳ�����,��������ʵ�����
 * @author jingyuan.sun
 *
 */
public class PageStrategy {
	/**
	 * ���л��������õ��в���
	 */
	private NoteLineStrategy nls;
	/**
	 * ��ҳ���������ʣ��հף��Ƿ�����������һҳ�Ĳ���Ԫ��
	 */
	private boolean isDrawBack;
	
	/**
	 * ���캯��
	 * @param nls
	 * @param isDrawBack
	 */
	public PageStrategy(NoteLineStrategy nls, boolean isDrawBack){
		this.nls = nls;
		this.isDrawBack = isDrawBack;
	}
	
	/**
	 * ���캯��
	 * @param ps
	 */
	public PageStrategy(PageStrategy ps){
		this.nls = ps.getNls();
		this.isDrawBack = ps.isDrawBack();
	}

	/**
	 * ����в���
	 * @return
	 */
	public NoteLineStrategy getNls() {
		return nls;
	}

	/**
	 * �����в���
	 * @param nls
	 */
	public void setNls(NoteLineStrategy nls) {
		this.nls = nls;
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

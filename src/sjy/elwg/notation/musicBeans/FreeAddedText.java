package sjy.elwg.notation.musicBeans;

/**
 * ���ɱ�ע�ı�
 * ���ı������û������׽��е������ı���ע����Щ�������߼��ϲ������κ����ֶ���������ǡ�Ʈ�㡱������
 * ���档�ڱ���ʱ��¼���ǵľ���λ�á�
 * 
 * ����ĳ����ĳЩ���ֶ���ı�ע����Annotation����
 * 
 * @author jingyuan.sun
 *
 */
public class FreeAddedText extends FreeTextArea{

	private static final long serialVersionUID = -8178873619209620520L;
	
	private int pageIndex; //���ڵ�ҳ�룬��0��ʼ����

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
}

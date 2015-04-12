package sjy.elwg.utility;

/**
 * �����ǻ���С����ʱ�Ĳ���ʵ���࣬��������С����ı�Ҫ����.
 * @author jingyuan.sun
 *
 */
public class MeaPartStrategy {
	
	/**
	 * ������ļ��
	 */
	private int noteDist;
	/**
	 * �Ƿ�����ʱ����.�ñ�����ҪӰ���ػ�С����ʱС���ߵķ���.�������ʱ���У�����ƽ���ʱ������С���ߣ����������.
	 * ͨ����Ϊtrue.
	 */
	private boolean isTemp;
	
	/**
	 * ���캯��
	 * @param noteDist �������
	 * @param isTemp �Ƿ�����ʱ
	 */
	public MeaPartStrategy(int noteDist, boolean isTemp){
		this.noteDist = noteDist;
		this.isTemp = isTemp;
	}
	
	public MeaPartStrategy(MeaPartStrategy mps){
		this.noteDist = mps.getNoteDist();
		this.isTemp = mps.isTemp();
	}

	/**
	 * ����������
	 * @return
	 */
	public int getNoteDist() {
		return noteDist;
	}

	/**
	 * �����������
	 * @param noteDist
	 */
	public void setNoteDist(int noteDist) {
		this.noteDist = noteDist;
	}

	/**
	 * ����Ƿ�����ʱ����
	 * @return
	 */
	public boolean isTemp() {
		return isTemp;
	}

	/**
	 * �����Ƿ�����ʱ����
	 * @param isTemp
	 */
	public void setTemp(boolean isTemp) {
		this.isTemp = isTemp;
	}

}

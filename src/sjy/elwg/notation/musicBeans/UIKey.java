package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
/**
 * ����ʵ��
 * ע����С�������洢���ַ����͵������ͱ�������С�ڵ�ʵ�ʵ���ʵ��ĵ������Ϳ��ܲ�һ��������ʵ��ȡ����С���������е�λ��.
 * @author sjy
 *
 */
public class UIKey extends JPanel{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = -8113634949273674746L;

	/**
	 * ���õ���ʱ��ǰһ��UI����ļ��
	 */
	public static final int KEY_GAP = 3;
	
	/**
	 * ��ɵ��ŵĵ������ŵĿ��
	 */
	public static final int SINGLEWIDTH = 8;
	
	/**
	 * ���ţ������Լ��ָ��ŵĵ�������
	 */
	public static final String FONT_SHARP = "\uE10E";
	public static final String FONT_FLAT = "\uE114";
	public static final String FONT_NATURAL = "\uE113";
	
	/**
	 * ����ֵ. ȡֵ��Χ��-7��7.����Ϊ0.����Ϊ���ţ�����Ϊ����.
	 */
	private int keyValue;
	
	/**
	 * �Ƿ��ǻָ���������
	 */
	private boolean restoreNatural;
	
	/**
	 * �׺�����.��Ϊ����ʵ�����״���Ӧ���׺��й�.
	 */
	private String clefType;
	
	/**
	 * �Ƿ�ѡ��
	 */
	private boolean selected;
	
	/**
	 * ���캯��
	 * @param keyValue
	 * @param clefType
	 */
	public UIKey(int keyValue, String clefType){
		super();
		this.keyValue = keyValue;
		this.clefType = clefType;
		selected = false;
		adjustSize();
		setOpaque(false);
		//setBackground(Color.red);
	}
	
	/**
	 * ������С
	 */
	public void adjustSize(){
		if(keyValue == 1 || keyValue == -1){
			setSize(NoteCanvas.LINE_GAP+2, NoteCanvas.LINE_GAP*3 + 5);
		}else if(keyValue == 2 || keyValue == -2){
			setSize(2*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*4 + 5);
		}else if(keyValue == 3 || keyValue == -3){
			setSize(3*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*5 + 5);
		}else if(keyValue == 4 || keyValue == -4){
			setSize(4*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*5 + 5);
		}else if(keyValue == 5 || keyValue == -5){
			setSize(5*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*6 + 5);
		}else if(keyValue == 6 || keyValue == -6){
			setSize(6*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*6 + 5);
		}else if(keyValue == 7 || keyValue == -7){
			setSize(7*SINGLEWIDTH+2, NoteCanvas.LINE_GAP*6 + 5);
		}
	}
	
	/**
	 * ����Ƿ�ָ�������
	 * @return
	 */
	public boolean isRestoreNatural() {
		return restoreNatural;
	}

	/**
	 * ���ûָ�������
	 * @param restoreNatural
	 */
	public void setRestoreNatural(boolean restoreNatural) {
		this.restoreNatural = restoreNatural;
	}

	/**
	 * ��õ���ֵ��
	 * @return
	 */
	public int getKeyValue() {
		return keyValue;
	}

	/**
	 * ���õ���ֵ
	 * @param keyValue
	 */
	public void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * ����׺�����
	 * @return
	 */
	public String getClefType() {
		return clefType;
	}

	/**
	 * �����׺�����.
	 * @param clefType
	 */
	public void setClefType(String clefType) {
		this.clefType = clefType;
	}

	/**
	 * ����Ƿ�ѡ��
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * ���ñ�ѡ��
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
//		g.setFont(new Font("mscore", Font.PLAIN, 35));
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
		if(selected) g.setColor(Color.red);
		else g.setColor(Color.black);
		
		String fontToUse = null;
		if(keyValue > 0){
			if(restoreNatural) fontToUse = FONT_NATURAL;
			else fontToUse = FONT_SHARP;
		}else if(keyValue < 0){
			if(restoreNatural) fontToUse = FONT_NATURAL;
			else fontToUse = FONT_FLAT;
		}
		if(clefType.equalsIgnoreCase("g/2") || clefType.equalsIgnoreCase("f/4") || clefType.equalsIgnoreCase("c/3")){
			if(keyValue == 1){
				g.drawString(fontToUse, 1, 15);
			}else if(keyValue == 2){
				g.drawString(fontToUse, 1, 15);
				g.drawString(fontToUse, SINGLEWIDTH, 30);
			}else if(keyValue == 3){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
			}else if(keyValue == 4){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
			}else if(keyValue == 5){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 45);
			}else if(keyValue == 6){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 5*SINGLEWIDTH, 25);
			}else if(keyValue == 7){
				g.drawString(fontToUse, 1, 20);
				g.drawString(fontToUse, SINGLEWIDTH, 35);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 15);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 5*SINGLEWIDTH, 25);
				g.drawString(fontToUse, 6*SINGLEWIDTH, 40);
			}
			if(keyValue == -1){
				g.drawString(fontToUse, 1, 25);
			}else if(keyValue == -2){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
			}else if(keyValue == -3){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
			}else if(keyValue == -4){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
			}else if(keyValue == -5){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 50);
			}else if(keyValue == -6){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 50);
				g.drawString(fontToUse, 5*SINGLEWIDTH, 35);
			}else if(keyValue == -7){
				g.drawString(fontToUse, 1, 40);
				g.drawString(fontToUse, SINGLEWIDTH, 25);
				g.drawString(fontToUse, 2*SINGLEWIDTH, 45);
				g.drawString(fontToUse, 3*SINGLEWIDTH, 30);
				g.drawString(fontToUse, 4*SINGLEWIDTH, 50);
				g.drawString(fontToUse, 5*SINGLEWIDTH, 35);
				g.drawString(fontToUse, 6*SINGLEWIDTH, 55);
			}
		}
	}
	
	/**
	 * ���ݵ������׺����ͣ����ط��õ���ʱ���ŵ�y�������������С��y����Ĳ�ֵ,���������С�ڵ�y����ֵ
	 * @return
	 */
	public int getPositionOffset(){
		if(keyValue == 0){
			System.err.println("����ʵ���keyValueֵ����Ϊ0");
			return 0;
		}
		if(clefType.equalsIgnoreCase("g/2")){
			if(keyValue == 1 || keyValue == 2){
				return -15;
			}else if(keyValue > 0){
				return -20;
			}else if(keyValue == -1){
				return -5;
			}else if(keyValue < 0){
				return -20;
			}
		}else if(clefType.equalsIgnoreCase("f/4")){
			if(keyValue == 1 || keyValue == 2){
				return -5;
			}else if(keyValue > 0){
				return -10;
			}else if(keyValue == -1){
				return 5;
			}else if(keyValue < 0){
				return -10;
			}
		}else if(clefType.equalsIgnoreCase("c/3")){
			if(keyValue == 1 || keyValue == 2){
				return -10;
			}else if(keyValue > 0){
				return -15;
			}else if(keyValue == -1){
				return 0;
			}else if(keyValue < 0){
				return -15;
			}
		}
		return 0;
	}

}

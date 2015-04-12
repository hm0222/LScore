package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
/**
 * �ĺ�ʵ��
 * @author sjy
 *
 */

/**
 * @author tequila
 *
 */
public class UITime extends JPanel{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = -2974244396106756250L;

	/**
	 * �����ĺ�ʱ��ǰһ��UI����ļ��
	 */
	public static final int TIME_GAP = 2;
	
	/**
	 * ��ͨ�ĺ�����
	 */
	private int beats = 4;
	
	/**
	 * ����ĺ�����
	 */
	private int[] beat = new int[4];
	
	
	
	/**
	 * ����
	 */
	private int beatType = 4;
	/**
	 * �Ƿ�ѡ��
	 */
	private boolean selected = false;
	
	/**
	 * ���캯��1
	 * @param beats
	 * @param beatType
	 */
	public UITime(int beats, int beatType){
		super();
		this.beats = beats;
		this.beatType = beatType;
		setSize((int)(1.6*NoteCanvas.LINE_GAP), Measure.MEASURE_HEIGHT);
		setOpaque(false);
	}
	/**
	 * ���캯��2
	 * @param beat[]
	 * @param beatType
	 */
	public UITime(int beat[], int beatType){
		super();
		this.beat= beat;
		this.beatType = beatType;
		adjustSize();
		setOpaque(false);
	}
	
	/**
	 * ���캯��3
	 * @param time
	 */
	public UITime(Time time){
		super();
		this.beat = time.getBeat();
		this.beats = time.getBeats();
		this.beatType = time.getBeatType();
		adjustSize();
		setOpaque(false);
	}

	/**
	 * �������
	 * @return
	 */
	public int getBeats() {
		return beats;
	}

	/**
	 * ��������
	 * @param beats
	 */
	public void setBeats(int beats) {
		this.beats = beats;
	}

	public int[] getBeat() {
		return beat;
	}
	public void setBeat(int[] beat) {
		this.beat = beat;
	}
	/**
	 * ��ý�������
	 * @return
	 */
	public int getBeatType() {
		return beatType;
	}

	/**
	 * ���ý�������
	 * @param beatType
	 */
	public void setBeatType(int beatType) {
		this.beatType = beatType;
	}

	/**
	 * �Ƿ�ѡ��
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
	
	/**
	 * �������ʹ������������ַ�����ʾ
	 * @param beats
	 * @return
	 */
	public String getNumString(int beats){
		String beatsString = "\u0034";
		switch (beats) {
		case 1:
			beatsString = "\u0031";
			break;
		case 2:
			beatsString = "\u0032";
			break;
		case 3:
			beatsString = "\u0033";
			break;
		case 4:
			beatsString = "\u0034";
			break;
		case 5:
			beatsString = "\u0035";
			break;
		case 6:
			beatsString = "\u0036";
			break;
		case 7:
			beatsString = "\u0037";
			break;
		case 8:
			beatsString = "\u0038";
			break;
		case 9:
			beatsString = "\u0039";
			break;
		}
		return beatsString;
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
		if(selected) g.setColor(Color.red);
		else g.setColor(Color.black);
		if(beat[0] == 0){
			
			if(beats / 10 < 1){
				String beatsString = getNumString(beats);
				g.drawString(beatsString, 3, 20);
			}else{
				int beat1 = beats / 10;
				int beat2 = beats % 10;
				setSize(24, Measure.MEASURE_HEIGHT);
				String beatString1 = getNumString(beat1);
				String beatString2 = getNumString(beat2);
				g.drawString(beatString1,1,20);
				g.drawString(beatString2,10,20);			
			}
			if(beatType / 10 < 1){
				String beatTypeString = getNumString(beatType);
				g.drawString(beatTypeString, getWidth()/2-5, 38);
			}else{
				int beatType1 = beatType / 10;
				int beatType2 = beatType % 10;
				String beatTypeString1 = getNumString(beatType1);
				String beatTypeString2 = getNumString(beatType2);
				g.drawString(beatTypeString1, 1, 38);
				g.drawString(beatTypeString2, 10, 38);
			}
		}else{		
			int width = 2;
			if(beat[0] / 10 < 1){
				String beatsString = getNumString(beat[0]);
				g.drawString(beatsString,width,20);
				width += 11;
			}else{
				int beat1 = beat[0] / 10;
				int beat2 = beat[0] % 10;
				String beatString1 = getNumString(beat1);
				String beatString2 = getNumString(beat2);
				g.drawString(beatString1, width,20);
				width += 11;
				g.drawString(beatString2, width, 20);
				width += 11;
			}
			for(int i = 1; i < 4; i++){
				if(beat[i] != 0){
					g.drawString("+",width+2,20);
					width += 11;
					if(beat[i] / 10 < 1){
						String beatsString = getNumString(beat[i]);
						g.drawString(beatsString,width,20);
						width += 11;
					}else{
						int beat1 = beat[i] / 10;
						int beat2 = beat[i] % 10;
						String beatString1 = getNumString(beat1);
						String beatString2 = getNumString(beat2);
						g.drawString(beatString1, width,20);
						width += 11;
						g.drawString(beatString2, width, 20);
						width += 11;
					}
				}		
			}	
			if(beatType / 10 < 1){
				String beatTypeString = getNumString(beatType);
				g.drawString(beatTypeString, getWidth()/2-8, 38);
			}else{
				int beatType1 = beatType / 10;
				int beatType2 = beatType % 10;
				String beatTypeString1 = getNumString(beatType1);
				String beatTypeString2 = getNumString(beatType2);
				if(getWidth() > 20){
					g.drawString(beatTypeString1, getWidth()/2-13, 38);
					g.drawString(beatTypeString2, getWidth()/2-2, 38);
				}else{
					setSize(24, Measure.MEASURE_HEIGHT);
					g.drawString(beatTypeString1, 2, 38);
					g.drawString(beatTypeString2, 11, 38);
				}

			}
		}

		
	}
	public void adjustSize() {
		if(beat[0] != 0){
			int j = 0;
			int k = 0;
			for (int i = 0; i < 4; i++) {
				if (beat[i] != 0) {
					if (beat[i] / 10 < 1) {
						j++;
					}else{
						j++;
						j++;
					}
					k++;
				}
			}
			setSize((j + k)*10,Measure.MEASURE_HEIGHT );
		}else{
			if(beats / 10 < 1 && beatType / 10 < 1){
				setSize((int)(1.6*NoteCanvas.LINE_GAP), Measure.MEASURE_HEIGHT);
			}else{
				setSize((int)(2.5*NoteCanvas.LINE_GAP), Measure.MEASURE_HEIGHT);
			}
		}
	
	}
    
	/**
	 * �����ڷ����ĺ�ʱ���ĺ�����ĺ����ڵ�С�ڵ�y�����yֵ
	 * @return
	 */
	public int getPositionOffset(){
		return 0;
	}

}

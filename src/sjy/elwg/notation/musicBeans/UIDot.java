package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;

/**
 * ����ʵ��
 * @author sjy
 *
 */
public class UIDot extends JPanel{
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 6887314525369158735L;
	/**
	 * �������
	 */
	private int dotNum;
	
	/**
	 * ���캯��
	 * @param dotNum
	 */
	public UIDot(int dotNum){
		super();
		this.dotNum = dotNum;
		adjustSize();
		setOpaque(false);
		setBackground(Color.red);
		repaint();
	}
	
	/**
	 * ������С
	 */
	public void adjustSize(){
		if(dotNum == 1){
			setSize(6, 12);
		}else if(dotNum ==2){
			setSize(12, 15);
		}
	}

	public void paintComponent(Graphics gg){
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        	renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        	g.setRenderingHints(renderHints);
//		g.setFont(new Font("mscore1", Font.PLAIN, 30));
		g.setFont(NoteCanvas.MSCORE1_FONT.deriveFont(30f));
		if(dotNum == 1){
			g.drawString("\uE10A", 2, 10);
		}else if(dotNum == 2){
			g.drawString("\uE10A", 2, 10);
			g.drawString("\uE10A", 7, 10);
		}
	}

	/**
	 * ��ø������
	 * @return
	 */
	public int getDotNum() {
		return dotNum;
	}

	/**
	 * ���ø������ 
	 * @param dotNum
	 */
	public void setDotNum(int dotNum) {
		this.dotNum = dotNum;
	}

}

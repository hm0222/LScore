package sjy.elwg.notation.musicBeans;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;

/**
 * ��β
 * @author sjy
 *
 */
public class Tail extends JPanel {
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 9L;
	/**
	 * ��β����Ӧ������ʱ��,��Ϊ��ͬʱ����������Ӧ��ͬ�ķ�β
	 */
	private int duration;
	/**
	 * ��β������Чֵ��"up"��"down".
	 */
	private String upOrDown;
	/**
	 * ����
	 */
	private int type;
	
	/**
	 * ���캯��
	 * @param durDiv
	 * @param direction
	 */
	public Tail(int durDiv, String direction){
		this.duration = durDiv;
		upOrDown = direction;
		type = Stem.NORMAL;
		switch(duration){
		case 256/1: 
		case 256/2: 
		case 256/4: setSize(1, 1); break;
		default: setSize(10, 45);
		}
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	public Tail(int durDiv, String direction, int type){
		this.duration = durDiv;
		upOrDown = direction;
		this.type = type;
		switch(duration){
		case 256/1: 
		case 256/2: 
		case 256/4: setSize(1, 1); break;
		default: setSize(10, 45);
		}
		setOpaque(false);
		setLayout(null);
		if(type == Stem.GRACE)
			setSize(getWidth()*3/4, getHeight()*3/4);
		repaint();
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        if(type == Stem.NORMAL)
        	g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
        else
        	g.setFont(NoteCanvas.MCORE_FONT.deriveFont(25f));
		String str = "";
		if(upOrDown.equalsIgnoreCase("up")){
			switch(duration){
			case 256/8: str = "\uE190"; break;
			case 256/16: str = "\uE191"; break;
			case 256/32: str = "\uE192"; break;
			case 256/64: str = "\uE193"; break;
			default: str = ""; 
			}
			g.drawString(str, 0, 0);
		}else if(upOrDown.equalsIgnoreCase("down")){
			switch(duration){
			case 256/8: str = "\uE194"; break;
			case 256/16: str = "\uE197"; break;
			case 256/32: str = "\uE198"; break;
			case 256/64: str = "\uE199"; break;
			default: str = ""; 
			}
			g.drawString(str, 0, getHeight());
		}
	}

}

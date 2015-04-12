package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;

/**
 * С����
 * @author jingyuan.sun
 *
 */
public class Barline extends JPanel implements Selectable{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 4L;

	/**
	 * С����������С����
	 */
	private MeasurePart meaPart;
	
	/**
	 * �Ƿ�ѡ��
	 */
	private boolean isSelected;
	
	/**
	 * С�������ͣ���Чֵ�У�regular,light-light,heavy,light-heavy,backward,forward,backward-forward
	 */
	private String type;
	
	/**
	 * ���캯��
	 */
	public Barline(String type){
		super();
		this.type = type;
		isSelected = false;
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	public Barline(MeasurePart meaPart,String type){
		super();
		this.type = type;
		this.meaPart = meaPart;
		meaPart.setBarline(this);
		isSelected = false;
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	/**
	 * ��������С���������С
	 */
	public void adjustSize(){
	//	setSize(10, meaPart.getHeight());
		if(type.equalsIgnoreCase("regular"))
			setSize(2,meaPart.getHeight());
		if(type.equalsIgnoreCase("light-light"))
			setSize(5,meaPart.getHeight());
		if(type.equalsIgnoreCase("heavy"))
			setSize(10,meaPart.getHeight());
		if(type.equalsIgnoreCase("light-heavy"))
			setSize(10,meaPart.getHeight());
		if(type.equalsIgnoreCase("forward"))
			setSize(10,meaPart.getHeight());
		if(type.equalsIgnoreCase("backward"))
			setSize(10,meaPart.getHeight());
		if(type.equalsIgnoreCase("backward-forward"))
		setSize(20,meaPart.getHeight());
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
		g.setFont(NoteCanvas.MUSICAL_FONT.deriveFont(29f));
		if(isSelected){
			g.setColor(Color.blue);
		}else{
			g.setColor(Color.black);
		}

		if(type.equalsIgnoreCase("regular")){
			g.drawLine(0, 0, 0, getHeight());
			g.drawLine(1, 0, 1, getHeight());
		}else if(type.equalsIgnoreCase("light-light")){
			g.drawLine(0, 0, 0, getHeight());
			g.drawLine(3, 0, 3, getHeight());
		}else if(type.equalsIgnoreCase("heavy")){
			g.drawLine(0, 0, 0, getHeight());
			g.drawLine(1, 0, 1, getHeight());
			g.drawLine(2, 0, 2, getHeight());
		}else if(type.equalsIgnoreCase("light-heavy")){
			g.drawLine(0, 0, 0, getHeight());
			g.drawLine(2, 0, 2, getHeight());
			g.drawLine(3, 0, 3, getHeight());
			g.drawLine(4, 0, 4, getHeight());
		}else if(type.equalsIgnoreCase("forward")){
			g.drawString("\uf07B", 0, 35);
			g.drawLine(5, 0, 5, getHeight());
			g.drawLine(7, 0, 7, getHeight());
			g.drawLine(8, 0, 8, getHeight());
			g.drawLine(9, 0, 9, getHeight());
		}else if(type.equalsIgnoreCase("backward")){
			g.drawLine(0, 0, 0, getHeight());
			g.drawLine(1, 0, 1, getHeight());
			g.drawLine(2, 0, 2, getHeight());
			g.drawLine(4, 0, 4, getHeight());
			g.drawString("\uf07B", 7, 35);
		}
		else if(type.equalsIgnoreCase("backward-forward")){
			g.drawString("\uf07B", 0, 35);
			g.drawLine(5, 0, 5, getHeight());
			g.drawLine(7, 0, 7, getHeight());
			g.drawLine(8, 0, 8, getHeight());
			g.drawLine(9, 0, 9, getHeight());
			g.drawLine(11, 0, 11, getHeight());
			g.drawString("\uf07B", 15, 35);
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * ���ô�С
	 */
	public void setSize(int x, int y){
		super.setSize(x, y);
		repaint();
	}

	/**
	 * �������С����
	 * @return
	 */
	public MeasurePart getMeaPart() {
		return meaPart;
	}

	/**
	 * ����С����
	 * @param meaPart
	 */
	public void setMeaPart(MeasurePart meaPart) {
		this.meaPart = meaPart;
	}

	/**
	 * �����Ƿ�ѡ��
	 * @return
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * ���ñ�ѡ��
	 * @param isSelected
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public void beSelected() {
		// TODO Auto-generated method stub
		isSelected = true;
		repaint();
	}

	@Override
	public void cancleSelected() {
		// TODO Auto-generated method stub
		isSelected = false;
		repaint();
	}
	
	public void setBarlineLocation(int x, int y){
		if(type.equalsIgnoreCase("regular")){
			setLocation(x, y);
		}else if(type.equalsIgnoreCase("forward")){
			setLocation(x-10, y);
		}else {
			setLocation(x-3, y);
		}
		
	}
	
	/**
	 * ������С�������ͺϲ�Ϊһ��
	 * ��ΪmusicXML�ļ���С������ͨ�����С�ں��ұ�С������С�ڵ������ȷ���ġ�
	 * @param type1
	 * @param type2
	 */
	public static String genBarlineStyle(String type1, String type2){
		String result = type2;
		if(type1.equalsIgnoreCase("backward") && type2.equalsIgnoreCase("forward"))
			result = "backward-forward";
		return result;
	}

}

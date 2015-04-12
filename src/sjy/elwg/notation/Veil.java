package sjy.elwg.notation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
/**
 * �����ڻ����ϲ��͸����.��Ҫ�����ڱ༭ʱ����������ȫ�����꣬�Ӷ���λ������༭������.
 * ͬʱҲ�������ڸ����ߵĻ���.
 * @author jingyuan.sun
 *
 */
public class Veil extends JPanel{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 4001953959805952510L;

	public static BasicStroke dashedLineStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
			0, new float[]{6}, 0);
	
	/**
	 * �����ǵĻ���
	 */
	private NoteCanvas canvas;
	
	public Veil(NoteCanvas canvas){
		super();
		this.canvas = canvas;
		setSize(canvas.getWidth(), canvas.getHeight());
		setLocation(0, 0);
		setOpaque(false);
		setVisible(false);
		setLayout(null);
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D)gg;
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	    g.setRenderingHints(renderHints);
	    g.setColor(Color.red);
		int x1 = (int) canvas.getConnBeginPoint().getX();
		int y1 = (int) canvas.getConnBeginPoint().getY();
		int x2 = (int) canvas.getConnEndPoint().getX();
		int y2 = (int) canvas.getConnEndPoint().getY();
		g.setStroke(dashedLineStroke);
		g.drawLine(x1, y1, x2, y2);
	}

}

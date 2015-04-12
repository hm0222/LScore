package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 * �û���ע���
 * @author wenxi.lu
 *
 */
public class Annotation extends JPanel implements Editable, MouseListener, MouseMotionListener{
	
	/**
	 * ��ע��������״̬�������رպʹ�
	 */
	public static int CLOSE = 0;
	public static int OPEN = 1;
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 20L;
	
	/**
	 * ��ñ����صĶ��󼯺�
	 */
	private ArrayList<Selectable> relatedObjts; 
	
	/**
	 * �ı�����
	 */
	private JTextArea textArea;
	
	/**
	 * ͼ�����
	 */
	private ImagePanel imagePanel;
	
	private Border border = BorderFactory.createLineBorder(Color.red);
	
	/**
	 * �����Ļ������
	 */
	private Point screenPoint = new Point();
	/**
	 * ���û����϶�����x�����y����
	 * �ڽ��з��ŷ���ʱ����λ����Ĭ��λ�����϶�λ��֮��
	 */
	private int draggedX = 0;
	private int draggedY = 0;
	
	/**
	 * ��ע���ĵ�ǰģʽ
	 */
	private int mode = CLOSE;
	
	/**
	 * ���캯��
	 */
	public Annotation() {
		super();
		setSize(23, 27);
		relatedObjts = new ArrayList<Selectable>();
		setBackground(Color.WHITE);
		setLayout(null);
		setBorder(border);
		
		textArea = new JTextArea();
		textArea.setSize(190, 80);
		textArea.setLineWrap(true);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setSize(190, 80);
		scrollPane.setLocation(5, 28);
		add(scrollPane);
		
		imagePanel = new ImagePanel();
		imagePanel.setAt(this);
		imagePanel.setLocation(0, 0);
		add(imagePanel);
		imagePanel.addMouseListener(this);
		imagePanel.addMouseMotionListener(this);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public Annotation(String text){
		super();
		setSize(23, 27);
		relatedObjts = new ArrayList<Selectable>();
		setBackground(Color.WHITE);
		setLayout(null);
		setBorder(border);
		
		textArea = new JTextArea(text);
		textArea.setSize(190, 80);
		textArea.setLineWrap(true);
		
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setSize(190, 80);
		scrollPane.setLocation(5, 28);
		add(scrollPane);
		
		imagePanel = new ImagePanel();
		imagePanel.setAt(this);
		imagePanel.setLocation(0, 0);
		add(imagePanel);
		imagePanel.addMouseListener(this);
		imagePanel.addMouseMotionListener(this);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * ��������
	 */
	public void resize() {
		// TODO Auto-generated method stub
		if (mode == OPEN) {
			setSize(23, 27);
			mode = CLOSE;
			for(Selectable s : relatedObjts){
				s.cancleSelected();
			}
		} else if (mode == CLOSE) {
			setSize(200, 110);
			mode = OPEN;
			for(Selectable s : relatedObjts){
				s.beSelected();
			}
		}
	}

	public void editMode() {
		// TODO Auto-generated method stub

	}

	public void viewMode() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * ��ø�ע����ע�͵Ķ��󼯺�
	 * @return
	 */
	public ArrayList<Selectable> getRelatedObjts() {
		return relatedObjts;
	}
	
	/**
	 * ���ע������
	 * @return
	 */
	public String getText(){
		return textArea.getText();
	}
	
	/**
	 * ����ע������
	 * @param str
	 */
	public void setText(String str){
		textArea.setText(str);
	}
	
	/**
	 * ��õ�ǰģʽ
	 * @return
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * ����ģʽ
	 * @param mode
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	/**
	 * ���ͼ�����
	 * @return
	 */
    public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public int getDraggedX() {
		return draggedX;
	}

	public void setDraggedX(int draggedX) {
		this.draggedX = draggedX;
	}

	public int getDraggedY() {
		return draggedY;
	}

	public void setDraggedY(int draggedY) {
		this.draggedY = draggedY;
	}



	/************************************************************************************/	
	/**
	 * ʢ��ͼƬ��panel
	 * @author wenxi.lu
	 *
	 */
	public class ImagePanel extends JPanel implements Selectable{
		
		private static final long serialVersionUID = -3253274741258205429L;
		
		private Annotation at;
		/**
		 * ��ѡ��û��ѡʱ�ı߽�
		 */
		private Border sborder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		private Border cborder = getBorder();

		public ImagePanel() {
			setSize(23, 27);
		}

		public Annotation getAt() {
			return at;
		}

		public void setAt(Annotation at) {
			this.at = at;
		}

		public void paintComponent(Graphics g) {
			try {
				InputStream input = new FileInputStream("pic/Write document.png");
				g.drawImage(ImageIO.read(input), 0, 0, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void beSelected() {
			// TODO Auto-generated method stub
			setBorder(sborder);
		}

		@Override
		public void cancleSelected() {
			// TODO Auto-generated method stub
			setBorder(cborder);
		}

	}
/******************************************************************************************/


	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == imagePanel ){
			resize();
		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this || e.getSource() == imagePanel){
			for(Selectable s : relatedObjts){
				s.beSelected();
			}
			screenPoint.setLocation(e.getXOnScreen(), e.getYOnScreen());
		}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this || e.getSource() == imagePanel){
			if(mode == CLOSE){
				for(Selectable s : relatedObjts){
					s.cancleSelected();
				}
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		int curX = e.getXOnScreen();
		int curY = e.getYOnScreen();
		int deltax = curX - (int)screenPoint.getX();
		int deltay = curY - (int)screenPoint.getY();
		setLocation(getX() + deltax, getY() + deltay);
		screenPoint.setLocation(curX, curY);
		draggedX += deltax;
    	draggedY += deltay;
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

   

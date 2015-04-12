package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import sjy.elwg.notation.FontChooser;

/**
 * ������ʾ��������.
 * @author jingyuan.sun
 *
 */
public class TitlePanel extends JTextArea implements DocumentListener, MouseListener, FocusListener, Editable, MouseMotionListener, Selectable{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 6121241395955101376L;
	public static Font TITLE_FONT = new Font("΢���ź�", Font.PLAIN, 40);
	

	/**
	 * ��ǰ״̬
	 */
	protected int mode = EDIT_MODE;
	
	/**
	 * �߽�
	 */
	private Border border = BorderFactory.createLineBorder(Color.blue, 1);
	
	/**
	 * ���û�ʲô��û������ʱ����ʾ��Ĭ������
	 */
	private String defaultString = " ";

	/**
	 * ���û����϶�����x�����y����
	 * �ڽ��з��ŷ���ʱ����λ����Ĭ��λ�����϶�λ��֮��
	 */
	protected int draggedX = 0;
	protected int draggedY = 0;

	/**
	 * ����¼������Ļ������
	 */
	protected Point screenPoint = new Point(); 
	
	/**
	 * Ĭ����ɫ
	 */
	protected Color defaultColor = Color.BLACK;

	
	
	/**
	 * ���캯��
	 */
	public TitlePanel(){
		super();
		setOpaque(false);
		getDocument().addDocumentListener(this);
		viewMode();
		setText(defaultString);
		setForeground(Color.LIGHT_GRAY);
		reSize();
		addMouseListener(this);
		addMouseMotionListener(this);
		addFocusListener(this);
	}
	
	public TitlePanel(String str){
		super();
		this.defaultString = str;
		setOpaque(false);
		getDocument().addDocumentListener(this);
		viewMode();
		setText(defaultString);
		setForeground(Color.LIGHT_GRAY);
		reSize();
		addMouseListener(this);
		addMouseMotionListener(this);
		addFocusListener(this);
	}
	
	public void viewMode(){
		mode = VIEW_MODE;
		setEditable(false);
		setDragEnabled(true);
		setBorder(null);
		revalidate();
	}
	
	public void editMode(){
		mode = EDIT_MODE;
		reSize();
		setDragEnabled(false);
		setEditable(true);
		setOpaque(true);
		setCaretColor(Color.blue);
		setFocusable(true);
		setForeground(Color.blue);
		setBorder(border);
		getCaret().setVisible(true);
		revalidate();
	}

	public void beSelected(){
		setForeground(Color.blue);
		repaint();
	}
	
	public void cancleSelected(){
		viewMode();
		setForeground(defaultColor);
		repaint();
	}

	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		reSize();
	    if(getParent() instanceof Page.TitleBox){
	    	Page.TitleBox pt = (Page.TitleBox)getParent();
			pt.reLocateComponents();
	    }
	}

	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		reSize();
		if(getParent() instanceof Page.TitleBox){
	    	Page.TitleBox pt = (Page.TitleBox)getParent();
			pt.reLocateComponents();
	    }
	}
	
	/**
	 * �����������ô�С
	 */
	public void reSize(){
		
		int height = getLineCount() * getFont().getSize();
		//�����е������
		int maxWidth = 0;
		int offset = 0;
		for(int i = 0; i < getLineCount(); i++){
			try {
				String subStr = getText().substring(offset, getLineEndOffset(i));
				int tempWidth = getFontMetrics(getFont()).stringWidth(subStr);
				if(tempWidth > maxWidth)
					maxWidth = tempWidth;
				offset = getLineEndOffset(i);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(maxWidth == 0)
			setSize(10, (height + 7));
		else
			setSize((int)(maxWidth + 3), (height + 7));
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this && e.getClickCount() == 2){
			editMode();
			requestFocus();
			repaint();
		}
		else if(e.getButton() == MouseEvent.BUTTON3){
			TextPopMenu tm = new TextPopMenu();
			tm.show((Component)(e.getSource()), e.getX(), e.getY());
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//�۲�״̬�±�ѡ��
		screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
		int x = e.getXOnScreen();
    		int y = e.getYOnScreen();
    	
	    	int deltax = x - (int)screenPoint.getX();
    		int deltay = y - (int)screenPoint.getY();
    	
    		screenPoint.setLocation(x, y);
    		draggedX += deltax;
    		draggedY += deltay;
    	
    		int curX = getX();
    		int curY = getY();
    		setLocation(curX + deltax, curY + deltay);
	}


	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		viewMode();
		setForeground(defaultColor);
		if(getText() == null || getText().equalsIgnoreCase("")){
			setText(defaultString);
			setForeground(Color.LIGHT_GRAY);
		}
		reSize();
	}

	/**
	 * ���Ĭ������
	 * @return
	 */
	public String getDefaultString() {
		return defaultString;
	}

	/**
	 * ����Ĭ������
	 */
	public void setDefaultString(String defaultString) {
		this.defaultString = defaultString;
	}

	/**
	 * ��õ�ǰ״̬
	 * @return
	 */
	public int getMode() {
		return mode;
	}

	public Color getDefaultColor(){
		return defaultColor;
	}
	
	public void setDefaultColor(Color color){
		this.defaultColor = color;
	}


	/**
	 * ����״̬
	 * @param mode
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	/**
	 * �����˵� 
	 *
	 */
	class TextPopMenu extends JPopupMenu{
		private static final long serialVersionUID = 41816163850165945L;
		private JMenuItem menuFont;
		private JMenuItem menuColor;
		public TextPopMenu(){
			super();
			install();
		}
		private void install(){
			menuFont = new JMenuItem("����");
			this.add(menuFont);
			menuColor = new JMenuItem("��ɫ");
			this.add(menuColor);
			addMouseListener(TitlePanel.this);
			menuFont.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Font font = FontChooser.showDialog(null, null, TitlePanel.this.getFont());
					TitlePanel.this.setFont(font);
					TitlePanel.this.revalidate();
				}
			});
			menuColor.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Color color = JColorChooser.showDialog(TitlePanel.this, "Color", Color.BLACK);
					TitlePanel.this.setForeground(color);
					TitlePanel.this.defaultColor = color;
					TitlePanel.this.repaint();
				}
			});
		}
	}

}

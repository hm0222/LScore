package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sjy.elwg.notation.FontChooser;

/**
 * �����ɽ��б༭����ק����������Ȳ�����field�ı�;��֮���Ƽ�FreeTextArea.
 * ������
 * @author jingyuan.sun
 *
 */
public abstract class FreeTextField extends JTextField implements Editable, DocumentListener,MouseMotionListener,MouseListener,Selectable{
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = -6275980176134532122L;

	/**
	 * Ĭ������
	 */
	public static Font LYRIC_FONT = new Font("����", Font.PLAIN, 20);
	/**
	 * ��ʱ߿�߶�
	 */
	protected int fontHeight = LYRIC_FONT.getSize() + 4;
	
	/**
	 * ��ǰ״ֵ̬
	 */
	protected int mode = VIEW_MODE;
	
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
	 * �༭ģʽ���ı��ı߿�
	 */
	protected Border border = BorderFactory.createLineBorder(Color.BLUE);

	/**
	 * Ĭ����ɫ
	 */
	protected Color defaultColor = Color.BLACK;

	/**
	 * ���캯��
	 *
	 */
	public FreeTextField(){
		super();
		setFont(LYRIC_FONT);		
		reSize();
		editMode();
		mode = EDIT_MODE;
		getDocument().addDocumentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * ���캯��
	 * @param str
	 */
	public FreeTextField(String str){
		super(str);
		setFont(LYRIC_FONT);
		setOpaque(false);
		setText(str);
		reSize();
		viewMode();
		getDocument().addDocumentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * ������������
	 */
	public void reSize(){	
		if(getText() == null || getText().equalsIgnoreCase("")){
			setSize(Note.NORMAL_HEAD_WIDTH/2, LYRIC_FONT.getSize() + 4);
		}
		else{
			int width = getFontMetrics(getFont()).stringWidth(getText())+5;
			int height = getFont().getSize() + 4;
			setSize(width , height);
		}
	}



	public void changedUpdate(DocumentEvent e) {
		reSize();
	}

	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		reSize();
	}

	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		reSize();
	}

	@Override
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

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == MouseEvent.BUTTON3){
			TextPopMenu tm = new TextPopMenu();
			tm.show((Component)(e.getSource()), e.getX(), e.getY());
		}
		else if (e.getClickCount() == 2) {
			editMode();
			requestFocus();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//�۲�״̬�±�ѡ��
		screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
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

	public Color getDefaultColor(){
		return defaultColor;
	}
	
	public void setDefaultColor(Color color){
		this.defaultColor = color;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
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

	@Override
	public void editMode() {
		// TODO Auto-generated method stub
		mode = EDIT_MODE;
		reSize();
		setDragEnabled(false);
		setEditable(true);
		setOpaque(true);
		setCaretColor(Color.blue);
		setFocusable(true);
		setForeground(Color.blue);
		setBorder(border);
	}

	@Override
	public void viewMode() {
		// TODO Auto-generated method stub
		mode =VIEW_MODE;
		setDragEnabled(true);
		setEditable(false);
		setOpaque(false);
		setFocusable(false);
		setBorder(null);
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
			addMouseListener(FreeTextField.this);
			menuFont.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Font font = FontChooser.showDialog(null, null, FreeTextField.this.getFont());
					FreeTextField.this.setFont(font);
					FreeTextField.this.revalidate();
				}
			});
			menuColor.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Color color = JColorChooser.showDialog(FreeTextField.this, "Color", Color.BLACK);
					FreeTextField.this.setForeground(color);
					FreeTextField.this.defaultColor = color;
					FreeTextField.this.repaint();
				}
			});
		}
	}
}

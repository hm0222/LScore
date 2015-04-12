package sjy.elwg.notation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * ����ĺŵĶԻ����ṩ��ͨ�ĺŵ�ѡ��Ҳ�������������ĺ�
 *
 */
public class TimeDialog extends JDialog implements ActionListener{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = -3055734169543347586L;
	/**
	 * ���ڴ�С
	 */
	private static final int DIALOG_WIDTH = 450;
	private static final int DIALOG_HEIGHT = 160;
	/**
	 * ��ť��С
	 */
	private static final int BT_WIDTH = 40;
	private static final int BT_HEIGHT = 30;
	

	/**
	 * ��ͨ�ĺŰ�ť�ϵ�ͼ��
	 */
	ImageIcon iconTime22 = new ImageIcon(("pic/time22.png"));
	ImageIcon iconTime24 = new ImageIcon(("pic/time24.png"));
	ImageIcon iconTime34 = new ImageIcon(("pic/time34.png"));
	ImageIcon iconTime38 = new ImageIcon(("pic/time38.png"));
	ImageIcon iconTime44 = new ImageIcon(("pic/time44.png"));
	ImageIcon iconTime54 = new ImageIcon(("pic/time54.png"));
	ImageIcon iconTime64 = new ImageIcon(("pic/time64.png"));
	ImageIcon iconTime68 = new ImageIcon(("pic/time68.png"));
	ImageIcon iconTime98 = new ImageIcon(("pic/time98.png"));
	ImageIcon iconTime128 = new ImageIcon(("pic/time128.png"));

	
	/**
	 * ��ͨ�ĺŰ�ť
	 */
	private JButton bt22 = new JButton(iconTime22);
	private JButton bt24 = new JButton(iconTime24);
	private JButton bt34 = new JButton(iconTime34);
	private JButton bt38 = new JButton(iconTime38);
	private JButton bt44 = new JButton(iconTime44);
	private JButton bt54 = new JButton(iconTime54);
	private JButton bt64 = new JButton(iconTime64);
	private JButton bt68 = new JButton(iconTime68);
	private JButton bt98 = new JButton(iconTime98);
	private JButton bt128 = new JButton(iconTime128);
	
	/**
	 * ��������ĺţ���������ĺţ������
	 */
	private JPanel panel;
	
	/**
	 * ����ĺ���������Ĭ���ĺ�Ϊ4/4��
	 */
	private JTextField beatTypeField = new JTextField("4");
	private JTextField beatField1 = new JTextField("4");
	private JTextField beatField2 = new JTextField("0");
	private JTextField beatField3 = new JTextField("0");
	private JTextField beatField4 = new JTextField("0");
	
		
	
	/**
	 * �����ĺ���Ӱ�ť��ͼ��
	 */
	ImageIcon iconNewBeatAdd = new ImageIcon(("pic/newBeatAdd.png"));
	
	/**
	 * �����ĺ���Ӱ�ť
	 */
	private JButton btNewBeatAdd= new JButton(iconNewBeatAdd);
	
	
	/**
	 * ��ǰ��ѡ����ĺŵ����ԣ���ͨ�ĺŵ�beat[]�е�ֵΪ0 
	 */
	private int beats = -1;
	private int beatType = -1;
	private int[] beat = new int[4];
	
	
	/**
	 * ���캯��
	 */
	public TimeDialog(){
		super();
		setLayout(null);
		setModal(true);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();
	}
	



	/**
	 * ��ʼ����ť
	 */
	public void initComponents(){
		bt22.setSize(BT_WIDTH, BT_HEIGHT);
		bt24.setSize(BT_WIDTH, BT_HEIGHT);
		bt34.setSize(BT_WIDTH, BT_HEIGHT);
		bt38.setSize(BT_WIDTH, BT_HEIGHT);
		bt44.setSize(BT_WIDTH, BT_HEIGHT);
		bt54.setSize(BT_WIDTH, BT_HEIGHT);
		bt64.setSize(BT_WIDTH, BT_HEIGHT);
		bt68.setSize(BT_WIDTH, BT_HEIGHT);
		bt98.setSize(BT_WIDTH, BT_HEIGHT);
		bt128.setSize(BT_WIDTH, BT_HEIGHT);
		bt44.setSize(BT_WIDTH, BT_HEIGHT);
		
		int x = 10; 
		int y = 22;
		bt22.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt24.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt34.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt38.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt44.setLocation(x, y);
		
		x = 10; y = 67;
		bt54.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt64.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt68.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt98.setLocation(x, y);
		x += BT_WIDTH + 10;
		bt128.setLocation(x, y);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(bt22);
		bg.add(bt24);
		bg.add(bt34);
		bg.add(bt38);
		bg.add(bt44);
		bg.add(bt54);
		bg.add(bt64);
		bg.add(bt68);
		bg.add(bt98);
		bg.add(bt128);

		add(bt22);
		add(bt24);
		add(bt34);
		add(bt38);
		add(bt44);
		add(bt54);
		add(bt64);
		add(bt68);
		add(bt98);
		add(bt128);
		
		//��ɫ�߿�
		Border border = BorderFactory.createLineBorder(Color.GRAY,1);
		
		panel = new JPanel(){
			/**
			 * ���к�
			 */
			private static final long serialVersionUID = 7332697206410248188L;

			public void paintComponent(Graphics gg){
				Graphics2D g = (Graphics2D) gg;	
				RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		        g.setRenderingHints(renderHints);
		        g.setFont(getFont().deriveFont(Font.BOLD, 15f));
		        g.drawString("�����ĺ�:", 5, 20);
		        g.setFont(NoteCanvas.MCORE_FONT.deriveFont(30f));
		        g.drawString("+", 43, 45);
		        g.drawString("+", 78, 45);
		        g.drawString("+", 113, 45);        
		        g.setColor(Color.gray);
		        g.drawLine(15,52,148,52);
		        g.drawLine(15,53,148,53);
		     
			}
		};
		panel.setBorder(border);
		panel.setBounds(260,5,165,110);
		panel.setLayout(null);
		
		/**
		 * Ϊÿһ���ı�������ı��ĵ�����ı��ĵ����ĵļ�����,
		 * ���ӵĵ�һ���ı����е����ֺͷ�ĸ���ı�������ֱ���
		 * Ϊ�������������ı�������ֿ���Ϊ0
		 */
		
		beatField1.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			  }
			  public void removeUpdate(DocumentEvent e) {
			  }
			  public void insertUpdate(DocumentEvent e) {
			    warn();
			  }
	
			  public void warn() {						  
				  try{
					  	//����һ���ڵ�����Ϊ���������ǲ�Ϊ��������ʱ���������
					     if (Integer.parseInt(beatField1.getText()) <= 0){
						       JOptionPane.showMessageDialog(beatField1,
						          "�������󣬵�һ�������������0������", "����",
						          JOptionPane.ERROR_MESSAGE);
						       
						     }
				  }catch(NumberFormatException  nef){
					  	//��׽���벻Ϊ�������쳣�������쳣�������������
					  	System.out.println("���벻Ϊ����");
					       JOptionPane.showMessageDialog(beatField1,
					    		   "�������󣬵�һ�������������0������", "����",
							          JOptionPane.ERROR_MESSAGE);
				  }
			  }
			});
		
		
		beatField2.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			  }
			  public void removeUpdate(DocumentEvent e) {
			  }
			  public void insertUpdate(DocumentEvent e) {
			    warn();
			  }

			  public void warn() {		
				  
				  try{
					     if (Integer.parseInt(beatField2.getText()) < 0){
						       JOptionPane.showMessageDialog(beatField2,
						          "�������󣬸ø�������ӦΪ���ڻ����0������", "����",
						          JOptionPane.ERROR_MESSAGE);
						     }
				  }catch(NumberFormatException  nef){
					  	System.out.println("���벻Ϊ����");
					       JOptionPane.showMessageDialog(beatField2,
					    		   "�������󣬸ø�������ӦΪ���ڻ����0������", "����",
							          JOptionPane.ERROR_MESSAGE);
				  }

			  }
			});
		
		beatField3.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			  }
			  public void removeUpdate(DocumentEvent e) {
			  }
			  public void insertUpdate(DocumentEvent e) {
			    warn();
			  }

			  public void warn() {		
				  
				  try{
					     if (Integer.parseInt(beatField3.getText()) < 0){
						       JOptionPane.showMessageDialog(beatField3,
						          "�������󣬸ø�������ӦΪ���ڻ����0������", "����",
						          JOptionPane.ERROR_MESSAGE);
						     }
				  }catch(NumberFormatException  nef){
					  	System.out.println("���벻Ϊ����");
					       JOptionPane.showMessageDialog(beatField3,
					    		   "�������󣬸ø�������ӦΪ���ڻ����0������", "����",
							          JOptionPane.ERROR_MESSAGE);
				  }
			  }
			});
		
		beatField4.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			  }
			  public void removeUpdate(DocumentEvent e) {

			  }
			  public void insertUpdate(DocumentEvent e) {
			    warn();
			  }

			  public void warn() {		
				  
				  try{
					     if (Integer.parseInt(beatField4.getText()) < 0){
						       JOptionPane.showMessageDialog(beatField4,
						          "�������󣬸ø�������ӦΪ���ڻ����0������", "����",
						          JOptionPane.ERROR_MESSAGE);
						
						     }
				  }catch(NumberFormatException  nef){
					  	System.out.println("���벻Ϊ����");
					       JOptionPane.showMessageDialog(beatField4,
					    		   "�������󣬸ø�������ӦΪ���ڻ����0������", "����",
							          JOptionPane.ERROR_MESSAGE);
					   
				  }

			  }
			});
		
		beatTypeField.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			  }
			  public void removeUpdate(DocumentEvent e) {
			  }
			  public void insertUpdate(DocumentEvent e) {
			    warn();
			  }

			  public void warn() {		
				  try{
					     if (Integer.parseInt(beatTypeField.getText()) <= 0){
						       JOptionPane.showMessageDialog(beatTypeField,
						          "���������ĺ�ӦΪ����0������", "����",
						          JOptionPane.ERROR_MESSAGE);
						     }
				  }catch(NumberFormatException  nef){
					  	System.out.println("���벻Ϊ����");
					       JOptionPane.showMessageDialog(beatTypeField,
					    		   "������������ӦΪ����0������", "����",
							          JOptionPane.ERROR_MESSAGE);
				  }
			  }
			});

		beatField1.setBounds(20, 30, 20, 18);
		beatField2.setBounds(55, 30, 20, 18);
		beatField3.setBounds(90, 30, 20, 18);
		beatField4.setBounds(125, 30, 20, 18);
		beatTypeField.setBounds(72, 60, 20, 18);
		panel.add(beatField1);
		panel.add(beatField2);
		panel.add(beatField3);
		panel.add(beatField4);
		panel.add(beatTypeField);
			
		btNewBeatAdd.setBounds(110,80,45,20);
		panel.add(btNewBeatAdd);		
		add(panel);
		btNewBeatAdd.addActionListener(this);
			
		bt22.addActionListener(this);
		bt24.addActionListener(this);
		bt34.addActionListener(this);
		bt38.addActionListener(this);
		bt44.addActionListener(this);
		bt54.addActionListener(this);
		bt64.addActionListener(this);
		bt68.addActionListener(this);
		bt98.addActionListener(this);
		bt128.addActionListener(this);

	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == bt22 ){
			beats = 2;
			beatType = 2;
			dispose();
		}else if(e.getSource() == bt24 ){
			beats = 2;
			beatType = 4;
			dispose();
		}else if(e.getSource() == bt34 ){
			beats = 3;
			beatType = 4;
			dispose();
		}else if(e.getSource() == bt38 ){
			beats = 3;
			beatType = 8;
			dispose();
		}else if(e.getSource() == bt44 ){
			beats = 4;
			beatType = 4;
			dispose();
		}else if(e.getSource() == bt54 ){
			beats = 5;
			beatType = 4;
			dispose();
		}else if(e.getSource() == bt64 ){
			beats = 6;
			beatType = 4;
			dispose();
		}else if(e.getSource() == bt68 ){
			beats = 6;
			beatType = 8;
			dispose();
		}else if(e.getSource() == bt98 ){
			beats = 9;
			beatType = 8;
			dispose();
		}else if(e.getSource() == bt128 ){			
			beats = 12;
			beatType = 8;
			dispose();
		}
		else if(e.getSource() == btNewBeatAdd){	
			System.out.println("actually");
			beat[0] = Integer.parseInt(beatField1.getText());
			beat[1] = Integer.parseInt(beatField2.getText());
			beat[2] = Integer.parseInt(beatField3.getText());
			beat[3] = Integer.parseInt(beatField4.getText());
			beatType = Integer.parseInt(beatTypeField.getText());
			dispose();
		}
	}


	public int getBeats() {
		return beats;
	}


	public void setBeats(int beats) {
		this.beats = beats;
	}


	public int getBeatType() {
		return beatType;
	}


	public void setBeatType(int beatType) {
		this.beatType = beatType;
	}

	public JTextField getBeatTypeField() {
		return beatTypeField;
	}

	public void setBeatTypeField(JTextField beatTypeField) {
		this.beatTypeField = beatTypeField;
	}

	public JTextField getBeatField1() {
		return beatField1;
	}

	public void setBeatField1(JTextField beatField1) {
		this.beatField1 = beatField1;
	}

	public JTextField getBeatField2() {
		return beatField2;
	}

	public void setBeatField2(JTextField beatField2) {
		this.beatField2 = beatField2;
	}

	public JTextField getBeatField3() {
		return beatField3;
	}


	public void setBeatField3(JTextField beatField3) {
		this.beatField3 = beatField3;
	}

	public JTextField getBeatField4() {
		return beatField4;
	}

	public void setBeatField4(JTextField beatField4) {
		this.beatField4 = beatField4;
	}

	public JButton getBtNewBeatAdd() {
		return btNewBeatAdd;
	}

	public void setBtNewBeatAdd(JButton btNewBeatAdd) {
		this.btNewBeatAdd = btNewBeatAdd;
	}

	public int[] getBeat() {
		return beat;
	}

	public void setBeat(int[] beat) {
		this.beat = beat;
	}

}

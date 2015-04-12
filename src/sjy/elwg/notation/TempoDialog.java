package sjy.elwg.notation;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TempoDialog extends JDialog implements ActionListener, MouseListener{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 6364402042373824938L;
	/**
	 * ���ڴ�С
	 */
	private static final int DIALOG_WIDTH = 325;
	private static final int DIALOG_HEIGHT = 300;
	public static final int LIST_WIDTH = 120;
	public static final int LIST_HEIGHT = 180;
	
	/**
	 * ȷ��ȡ����ť
	 */
	private JButton btYes;
	private JButton btNo;
	
	/**
	 * �ٶ������б�
	 */
	private JList tempoList;
	
	/**
	 * ��ѡ��ť
	 */
	private JCheckBox checkBox;
	
	/**
	 * �������ٶȵ������
	 */
	private JTextField fname;
	private JTextField fnum;
	
	/**
	 * �������ٶȵ�˵��
	 */
	private JLabel lname;
	private JLabel lnum;
	
	/**
	 * �Ƿ�����ȷ��
	 */
	private boolean yesPressed = false;
	/**
	 * �ٶ�ֵ
	 */
	private int number = 120;
	
	/**
	 * ���캯��
	 */
	public TempoDialog(){
		super();
		setModal(true);
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();
	}
	
	/**
	 * ��ʼ�����
	 */
	public void initComponents(){
		btYes = new JButton("ȷ��");
		btNo = new JButton("ȡ��");
		btYes.setSize(60, 20);
		btYes.setPreferredSize(new Dimension(60, 20));
		btNo.setSize(60, 20);
		btNo.setPreferredSize(new Dimension(60, 20));
		btYes.addActionListener(this);
		btNo.addActionListener(this);
		
		String[] data = {"Adagio", "Allegro", "Allegretto", "Andante", "Con brio", "Con moto", "Grave", "Largo", "Lento",
				"Maestoso", "Moderato", "Prestissimo", "Presto", "Vivace", "Vivo", "Ballad", "Fast", "Lively", "Moderate",
				"Slow", "Very slow", "With movement", "Entrainant", "Latent", "Rapide", "Regulier", "Vif", "Vite",
				"Vivement", "Bewegt", "Langsam", "Lebhaft", "Schnell"};
		tempoList = new JList(data);
		tempoList.setSelectedIndex(0);
		tempoList.addMouseListener(this);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(tempoList);
		scrollPane.setSize(LIST_WIDTH + 15, LIST_HEIGHT + 15);
		scrollPane.setPreferredSize(new Dimension(LIST_WIDTH + 15, LIST_HEIGHT + 15));
		
		fname = new JTextField(data[0]);
		fnum = new JTextField("120");
		lname = new JLabel("����");
		lnum = new JLabel("��/����");
		checkBox = new JCheckBox("��ʾ����", true);
		fname.setSize(LIST_WIDTH - 40, 15);
		fname.setPreferredSize(new Dimension(LIST_WIDTH - 40, 15));
		fnum.setSize(LIST_WIDTH - 40, 15);
		fnum.setPreferredSize(new Dimension(LIST_WIDTH - 40, 15));
		lname.setSize(60, 15);
		lname.setPreferredSize(new Dimension(40, 15));
		lnum.setSize(60, 15);
		lnum.setPreferredSize(new Dimension(40, 15));
		checkBox.setSize(100, 15);
		
		getContentPane().add(btYes);
		getContentPane().add(btNo);
		getContentPane().add(scrollPane);
		getContentPane().add(fname);
		getContentPane().add(fnum);
		getContentPane().add(lname);
		getContentPane().add(lnum);
		getContentPane().add(checkBox);
		
		scrollPane.setLocation(10, 10);
		lname.setLocation(scrollPane.getX() + scrollPane.getWidth() + 10, DIALOG_HEIGHT/3);
		lnum.setLocation(lname.getX(), lname.getY() + lname.getHeight()+ 5);
		fname.setLocation(lname.getX() + lname.getWidth() + 5, lname.getY());
		fnum.setLocation(fname.getX(), lnum.getY());
		checkBox.setLocation(lname.getX(), lnum.getY() + lnum.getHeight() + 10);
		btYes.setLocation(DIALOG_WIDTH/4, DIALOG_HEIGHT - 70);
		btNo.setLocation(btYes.getX() + btYes.getWidth() + 30, btYes.getY());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btYes){
			yesPressed = true;
			dispose();
		}
		else if(e.getSource() == btNo){
			dispose();
		}
		else if(e.getSource() == tempoList){
			String name = (String)tempoList.getSelectedValue();
			fname.setText(name);
		}
	}
	
	/**
	 * ��ý�������
	 * @return
	 */
	public String getText(){
		return yesPressed ? fname.getText() : null;
	}
	
	/**
	 * ����ٶ�ֵ
	 * @return
	 */
	public int getNumber(){
		return yesPressed ? number : -1;
	}
	
	/**
	 * �Ƿ���ʾ����
	 * @return
	 */
	public boolean displayTempo(){
		return checkBox.isSelected();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		if(e.getSource() == tempoList){
			String name = (String)tempoList.getSelectedValue();
			fname.setText(name);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

package sjy.elwg.notation;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class InstrumentDialog extends JDialog implements ActionListener{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 8390190872587846802L;
	/**
	 * ���ڿ����߶�
	 */
	public static final int DIALOG_WIDTH = 320;
	public static final int DIALOG_HEIGHT = 300;
	public static final int LIST_WIDTH = 120;
	public static final int LIST_HEIGHT = 180;
	
	/**
	 * ȷ��ȡ����ť
	 */
	private JButton btYes;
	private JButton btNo;
	
	/**
	 * �����б�
	 */
	private JList instrumentList;
	
	/**
	 * ������������д�������
	 */
	private JTextField fname;
	private JTextField fabbre;
	
	/**
	 * ������������д��˵��
	 */
	private JLabel lname;
	private JLabel labbre;
	
	/**
	 * �û����ȷ������ȡ��
	 */
	private boolean yes = false;
	
	
	/**
	 * ���캯��
	 * @param o ����������ݽ����Ķ���
	 */
	public InstrumentDialog(){
		super();
		setModal(true);
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
		initComponents();
		setVisible(true);
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
		
		String[] data = {"����", "��˹", "С����", "������", "������", "����", "��"};
		instrumentList = new JList(data);
		instrumentList.setSelectedIndex(0);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(instrumentList);
		instrumentList.setPreferredSize(new Dimension(LIST_WIDTH, LIST_HEIGHT));
		scrollPane.setSize(LIST_WIDTH + 15, LIST_HEIGHT + 15);
		scrollPane.setPreferredSize(new Dimension(LIST_WIDTH + 15, LIST_HEIGHT + 15));
		
		fname = new JTextField();
		fabbre = new JTextField();
		lname = new JLabel("����");
		labbre = new JLabel("��д");
		fname.setSize(LIST_WIDTH - 40, 15);
		fname.setPreferredSize(new Dimension(LIST_WIDTH - 40, 15));
		fabbre.setSize(LIST_WIDTH - 40, 15);
		fabbre.setPreferredSize(new Dimension(LIST_WIDTH - 40, 15));
		lname.setSize(30, 15);
		lname.setPreferredSize(new Dimension(30, 15));
		labbre.setSize(30, 15);
		labbre.setPreferredSize(new Dimension(30, 15));
		
		getContentPane().add(btYes);
		getContentPane().add(btNo);
		getContentPane().add(scrollPane);
		getContentPane().add(fname);
		getContentPane().add(fabbre);
		getContentPane().add(lname);
		getContentPane().add(labbre);
		
		scrollPane.setLocation(10, 10);
		lname.setLocation(scrollPane.getX() + scrollPane.getWidth() + 10, DIALOG_HEIGHT/2);
		labbre.setLocation(lname.getX(), lname.getY() + lname.getHeight()+ 5);
		fname.setLocation(lname.getX() + lname.getWidth() + 5, lname.getY());
		fabbre.setLocation(fname.getX(), labbre.getY());
		btYes.setLocation(DIALOG_WIDTH/3, DIALOG_HEIGHT - 70);
		btNo.setLocation(btYes.getX() + btYes.getWidth() + 30, btYes.getY());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btYes){
			yes = true;
			dispose();
		}
		else if(e.getSource() == btNo){
			dispose();
		}
	}
	
	/**
	 * �����û���ѡ�Ĳ���
	 * ����û����ȡ�����򷵻�null
	 * @return
	 */
	public HashMap<String, String> getParameters(){
		if(yes){
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("instrument", (String)getSelectedInstrument());
			result.put("name", fname.getText());
			result.put("abbre", fabbre.getText());
			return result;
		}
		else 
			return null;
	}
	
	/**
	 * ������ѡ�������
	 * @return
	 */
	private String getSelectedInstrument(){
		String result = null;
		String name = (String)instrumentList.getSelectedValue();
		if(name.equals("����"))
			result = "1";
		else if(name.equals("��˹"))
			result = "33";
		else if(name.equals("С����"))
			result = "41";
		else if(name.equals("������"))
			result = "42";
		else if(name.equals("������"))
			result = "43";
		else if(name.equals("����"))
			result = "25";
		else if(name.equals("��"))
			result = "117";
		
		return result;
	}

}

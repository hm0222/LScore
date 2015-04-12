package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JRadioButton;

import sjy.elwg.notation.musicBeans.Score;

public class NewScoreDialog extends JDialog implements ActionListener{

	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 865529529543655345L;
	/**
	 * ���ڰ�ť�����߶�
	 */
	public static final int DIALOG_WIDTH = 300;
	public static final int DIALOG_HEIGHT = 200;
	public static final int BT_WIDTH = 100;
	public static final int BT_HEIGHT = 50;
	
	/**
	 * ȷ��ȡ����ť
	 */
	private JButton btYes;
	private JButton btNo;
	
	private JRadioButton btNormal = new JRadioButton("��ͨģʽ", true);
	private JRadioButton btUnlmted = new JRadioButton("�����ƽ���ģʽ", false);
	private JCheckBox checkBox = new JCheckBox("��ʾ����");
	/**
	 * �û���ѡ����������
	 */
	private int scoreType = Score.SCORE_NORMAL;
	private int realScoreType = -1;
	
	/**
	 * ���캯��
	 */
	public NewScoreDialog(){
		super();
		setLayout(null);
		setModal(true);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();
		setAlwaysOnTop(true);
		//setVisible(true);
	}
	
	/**
	 * ��ʼ������
	 */
	public void initComponents(){
		btYes = new JButton("ȷ��");
		btNo = new JButton("ȡ��");
		btYes.setSize(60, 20);
		btNo.setSize(60, 20);
		btYes.addActionListener(this);
		btNo.addActionListener(this);
		
		ButtonGroup btGroup = new ButtonGroup();
		btGroup.add(btNormal);
		btGroup.add(btUnlmted);
		btNormal.addActionListener(this);
		btUnlmted.addActionListener(this);
		
		btNormal.setSize(BT_WIDTH, BT_HEIGHT);
		btUnlmted.setSize(BT_WIDTH + 20, BT_HEIGHT);
		btNormal.setLocation(DIALOG_WIDTH / 2 - 120, DIALOG_HEIGHT / 7);
		btUnlmted.setLocation(btNormal.getX() + btNormal.getWidth() , btNormal.getY());
		btYes.setLocation(DIALOG_WIDTH / 3, DIALOG_HEIGHT * 3 / 5);
		btNo.setLocation(btYes.getX() + btYes.getWidth() + 50, btYes.getY());
		
		checkBox.setSize(BT_WIDTH, BT_HEIGHT);
		checkBox.setLocation((btNormal.getX() + btUnlmted.getX())/2, btNormal.getY() + btNormal.getHeight()*2/3);
		
		add(btYes);
		add(btNo);
		add(btNormal);
		add(btUnlmted);
		add(checkBox);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btNormal){
			scoreType = Score.SCORE_NORMAL;
		}
		else if(e.getSource() == btUnlmted){
			scoreType = Score.SCORE_UNLMTED;
		}
		else if(e.getSource() == btYes){
			realScoreType = scoreType;
			dispose();
		}
		else if(e.getSource() == btNo){
			scoreType = -1;
			dispose();
		}
	}

	/**
	 * ����û���ѡ����������
	 * @return
	 */
	public int getScoreType() {
		return realScoreType;
	}
	
	
	public boolean hasTitle(){
		return checkBox.isSelected();
	}
}

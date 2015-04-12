package sjy.elwg.notation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddMeasureDialog extends JDialog implements ActionListener{

	/**
	 * ���к�
	 */
	private static final long serialVersionUID = -2796002169639595637L;
	
	/**
	 * ���ڴ�С
	 */
	private static final int DIALOG_WIDTH = 200;
	private static final int DIALOG_HEIGHT = 120;
	/**
	 * ��ť��С
	 */
	private static final int BT_WIDTH = 45;
	private static final int BT_HEIGHT = 22;
	
	/**
	 * ��ťͼ��
	 */
	ImageIcon iconConfirm = new ImageIcon(("pic/confirm.png"));
	ImageIcon iconCancel = new ImageIcon(("pic/cancel.png"));
	
	/**
	 * ȷ����ȡ����ť
	 */
	private JButton btYes = new JButton(iconConfirm);
	private JButton btCancle = new JButton(iconCancel);
	
	/**
	 * �û���д��
	 */
	private JTextField textField = new JTextField("3");
	
	private int measureNum = -1;
	
	
	/**
	 * ���캯��
	 */
	public AddMeasureDialog(){
		super();
		setLayout(null);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		initComponents();
		setModal(true);
		setVisible(true);
	}
	
	/**
	 * ��ʼ�����
	 */
	public void initComponents(){
		JLabel lb = new JLabel("С�ڸ���:");
		lb.setSize(60, 20);
		textField.setSize(30, 20);
		btYes.setSize(BT_WIDTH, BT_HEIGHT);
		btCancle.setSize(BT_WIDTH, BT_HEIGHT);
		
		lb.setLocation(50, 10);
		textField.setLocation(lb.getX() + lb.getWidth(), lb.getY());
		btYes.setLocation(getWidth()/5, 50);
		btCancle.setLocation(btYes.getX() + btYes.getWidth() + 20, btYes.getY());
		
		add(lb);
		add(btYes);
		add(btCancle);
		add(textField);
		
		btYes.addActionListener(this);
		btCancle.addActionListener(this);
	}

	/**
	 * �����ӵ�С�ڸ���
	 * @return
	 */
	public int getMeasureNum() {
		return measureNum;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//���ȷ��
		if(e.getSource() == btYes){
			int num = -1;
			try{
				num = Integer.parseInt(textField.getText());
			}catch(NumberFormatException ex){
				ex.printStackTrace();
				measureNum = -1;
				dispose();
				return;
			}
			measureNum = num;
			dispose();
		}
		//���ȡ��
		else if(e.getSource() == btCancle){
			measureNum = -1;
			dispose();
		}
	}

}

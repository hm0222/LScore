package sjy.elwg.notation;

import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 9114669846195416260L;
	/**
	 * ������
	 */
	private JProgressBar progressBar;
	
	/**
	 * �չ��캯��
	 */
	public ProgressDialog(){
		super();
		progressBar = new JProgressBar();
		add(progressBar);
		
		getContentPane().setLayout(null);
		setSize(500, 50);
		progressBar.setSize(500, 10);
		progressBar.setLocation(0, 0);
		
		setAlwaysOnTop(true);
		setVisible(true);
	}
	
	/**
	 * ���캯��
	 * @param min ��Сֵ
	 * @param max ���ֵ
	 */
	public ProgressDialog(int min, int max){
		super();
		progressBar = new JProgressBar(min, max);
		add(progressBar);
		
		getContentPane().setLayout(null);
		setSize(500, 55);
		progressBar.setSize(500, 10);
		progressBar.setLocation(0, 0);
		
		setAlwaysOnTop(true);
		setVisible(true);
	}
	
	/**
	 * ���õ�ǰֵ
	 * @param n
	 */
	public void setValue(int n){
		progressBar.setValue(n);
	}
	
	/**
	 * ��õ�ǰֵ
	 * @return
	 */
	public int getValue(){
		return progressBar.getValue();
	}
	
	/**
	 * �����Сֵ
	 * @return
	 */
	public int getMin(){
		return progressBar.getMinimum();
	}
	
	/**
	 * ������Сֵ
	 * @param n
	 */
	public void setMin(int n){
		progressBar.setMinimum(n);
	}
	
	/**
	 * ������ֵ
	 * @return
	 */
	public int getMax(){
		return progressBar.getMaximum();
	}
	
	/**
	 * ������Сֵ
	 * @param n
	 */
	public void setMax(int n){
		progressBar.setMaximum(n);
	}
	
	public void refresh(){
		int x = progressBar.getWidth();
		int y = progressBar.getHeight();
		progressBar.paintImmediately(new Rectangle(x, y));
		//progressBar.updateUI();
	}

}

package sjy.elwg.notation;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * �˵����ڵĶ������.
 * @author jingyuan.sun
 *
 */
public class TopPanel extends JPanel{

	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 12L;
	
	/**
	 * ʢ�Ų˵���ť
	 */
	private JMenuBar menuBar;
	
	JMenu menuFile = new JMenu("�ļ�");
	JMenu menuStaff = new JMenu("����");
	JMenu menuLayout = new JMenu("ҳ��");
	
	JMenuItem menuAutoLayout = new JMenuItem("�Զ�����");
	JMenuItem menuNew = new JMenuItem("�½�����");
	JMenuItem menuImport = new JMenuItem("����");
	JMenu menuExport = new JMenu("����");
	JMenuItem menuXML = new JMenuItem("MusicXML�ĵ�");
	JMenuItem menuPicture = new JMenuItem("ͼƬ");
	JMenuItem menuPrint = new JMenuItem("��ӡ");
	
	JMenuItem addPartDown = new JMenuItem("��֮ǰ�������");
	JMenuItem addPartUp = new JMenuItem("��֮���������");
	JMenuItem removeStaff = new JMenuItem("ɾ������");
	JMenuItem changeTime = new JMenuItem("�ı��ĺ�");
	JMenuItem changeKey = new JMenuItem("�ı����");
	JMenuItem changeClef = new JMenuItem("�ı��׺�");
	JMenu addTuplet = new JMenu("����������");
	JMenuItem addMea = new JMenuItem("���С��");
	JMenuItem delMea = new JMenuItem("�Ƴ�С��");
	
	public TopPanel(){
		super();
		setLayout(null);
		initComponents();
		setSize(80, 25);
	}
	
	public void initComponents(){
		menuBar = new JMenuBar();
		menuBar.setSize(120, 25);
		
		menuLayout.add(menuAutoLayout);
		
		menuExport.add(menuXML);
		menuExport.add(menuPicture);
		menuFile.add(menuNew);
		menuFile.add(menuImport);
		menuFile.add(menuExport);
		menuFile.add(menuPrint);
		
		menuStaff.add(addPartDown);
		menuStaff.add(addPartUp);
		menuStaff.add(removeStaff);
		menuStaff.addSeparator();
		menuStaff.add(changeTime);
		menuStaff.add(changeKey);
		menuStaff.add(changeClef);
		menuStaff.addSeparator();
		menuStaff.add(addTuplet);
		menuStaff.addSeparator();
		menuStaff.add(addMea);
		menuStaff.add(delMea);
		
		menuBar.add(menuFile);
		menuBar.add(menuStaff);
		menuBar.add(menuLayout);
		add(menuBar);
		
		int x = 0;
		int y = 5;
		menuFile.setLocation(x, y);
		x += menuFile.getWidth();
		menuLayout.setLocation(x, y);
	}
	
	/**
	 * ���ô�С
	 */
	public void setSize(int x, int y){
		super.setSize(x, y);
//		menuBar.setLocation(getWidth() - menuBar.getWidth() - 10, getHeight() - menuBar.getHeight());
		menuBar.setLocation(0, getHeight() - menuBar.getHeight());
	}
	
	/**
	 * ��������ײ˵�������
	 * @param l
	 */
	public void addStaffListener(ActionListener l){
		addPartDown.addActionListener(l);
		addPartUp.addActionListener(l);
		removeStaff.addActionListener(l);
		changeTime.addActionListener(l);
		changeKey.addActionListener(l);
		changeClef.addActionListener(l);
		addMea.addActionListener(l);
		delMea.addActionListener(l);
	}
	
	/**
	 * ����ļ�������
	 * @param listener
	 */
	public void addFileListener(ActionListener listener){
		menuNew.addActionListener(listener);
		menuImport.addActionListener(listener);
		menuXML.addActionListener(listener);
		menuPicture.addActionListener(listener);
		menuPrint.addActionListener(listener);
	}
	
	/**
	 * ��Ӳ��ֲ˵�������
	 * @param listener
	 */
	public void addLayoutListenern(ActionListener listener){
		menuAutoLayout.addActionListener(listener);
	}
	
	public void addFunctionListener(ActionListener l){
		this.addStaffListener(l);
		this.addFileListener(l);
		this.addLayoutListenern(l);
	}

	public JMenu getMenuFile() {
		return menuFile;
	}

	public JMenu getMenuStaff() {
		return menuStaff;
	}

	public JMenu getMenuLayout() {
		return menuLayout;
	}

	public JMenuItem getMenuAutoLayout() {
		return menuAutoLayout;
	}

	public JMenuItem getMenuNew() {
		return menuNew;
	}

	public JMenuItem getMenuImport() {
		return menuImport;
	}

	public JMenuItem getMenuPrint(){
		return menuPrint;
	}

	public JMenu getMenuExport() {
		return menuExport;
	}

	public JMenuItem getMenuXML() {
		return menuXML;
	}

	public JMenuItem getMenuPicture() {
		return menuPicture;
	}

	public JMenuItem getAddPartDown() {
		return addPartDown;
	}

	public JMenuItem getAddPartUp() {
		return addPartUp;
	}

	public JMenuItem getRemoveStaff() {
		return removeStaff;
	}

	public JMenuItem getChangeTime() {
		return changeTime;
	}

	public JMenuItem getChangeKey() {
		return changeKey;
	}

	public JMenuItem getChangeClef() {
		return changeClef;
	}

	public JMenu getAddTuplet() {
		return addTuplet;
	}

	public JMenuItem getAddMea() {
		return addMea;
	}

	public JMenuItem getDelMea() {
		return delMea;
	}

}

package sjy.elwg.notation.musicBeans;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * ����
 * @author jingyuan.sun
 *
 */
public class Beam extends JPanel {
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 5L;
	/**
	 * ����ܹ���������
	 */
	private ArrayList<AbstractNote> uiNoteList;
	/**
	 * ������������λ�ã��ϻ�����
	 */
	private String upOrDown;
	/**
	 * ������б��ʽ����ߣ��Ҹߣ�����ˮƽ
	 */
	private String highNode;
	/**
	 * ���ܲ�����ȡ�����������
	 */
	private int beamNum;
	
	private int noteWidth = 12;
	/**
	 * ������бб��
	 */
	private double ratio = 0.15;
	private int beamWidth = 4;
	private int beamGap = 2;
	
	/**
	 * ���캯��
	 * @param notes
	 */
	public Beam(ArrayList<AbstractNote> notes){
		super();
		int num = notes.size();
		uiNoteList = new ArrayList<AbstractNote>();
		for(int i = 0; i < notes.size(); i++){
			AbstractNote anote = notes.get(i);
			uiNoteList.add(anote);
			anote.setBeam(this);
			if(i != 0 && i < num - 1){
				anote.setBeamType("continue");
			}
		}
		beamNum = 1;
		noteWidth = notes.get(0).getHighestNote().getWidth()-1;
		determineShape();
		adjustSize();
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	/**
	 * ���캯��
	 * @param notes
	 * @param upOrDown ���ܳ���
	 */
	public Beam(ArrayList<AbstractNote> notes, String upOrDown){
		super();
		int num = notes.size();
		uiNoteList = new ArrayList<AbstractNote>();
		for(int i = 0; i < notes.size(); i++){
			AbstractNote anote = notes.get(i);
			uiNoteList.add(anote);
			anote.setBeam(this);
			if(i != 0 && i < num - 1){
				anote.setBeamType("continue");
			}
		}
		beamNum = 1;
		noteWidth = notes.get(0).getHighestNote().getWidth()-1;
		this.upOrDown = upOrDown;
		determineHighNode();
		adjustSize();
		setOpaque(false);
		setLayout(null);
		repaint();
	}
	
	/**
	 * �ж��������е���б��ʽ����ʽ
	 */
	public void determineShape(){
		determineUpOrDown();
		determineHighNode();
	}
	
	/**
	 * �������ܵĳ���
	 */
	private void determineUpOrDown(){
		upOrDown = Controller.beamUpOrDown(uiNoteList);
	}
	
	/**
	 * �������ܵ���б��ʽ
	 */
	private void determineHighNode(){
		highNode = Controller.highBeamNode(uiNoteList);
	}

	/**
	 * ��÷���б��
	 * @return
	 */
	public double getRatio() {
		return ratio;
	}

	/**
	 * ����б��
	 * @param ratio
	 */
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	/**
	 * ���ط���λ��
	 * @return
	 */
	public String getUpOrDown() {
		return upOrDown;
	}

	/**
	 * ���÷���λ��
	 * @param upOrDown
	 */
	public void setUpOrDown(String upOrDown) {
		this.upOrDown = upOrDown;
	}

	/**
	 * �����б��ʽ
	 * @return
	 */
	public String getHighNode() {
		return highNode;
	}

	/**
	 * ������б��ʽ
	 * @param highNode
	 */
	public void setHighNode(String highNode) {
		this.highNode = highNode;
	}

	/**
	 * ����������Ϣ������С
	 */
	public void adjustSize(){
		//�������ˮƽ
		int div = MusicMath.shortestDur(uiNoteList);
		switch(div){
		case 256/8: beamNum = 1; break;
		case 256/16: beamNum = 2; break;
		case 256/32: beamNum = 3; break;
		case 256/64: beamNum = 4; break;
		}
		int width = 0;
		for(int i = uiNoteList.size()-1; i >= 0; i--){
			if(uiNoteList.get(i) instanceof Note){
			width = uiNoteList.get(i).getX() - uiNoteList.get(0).getX()+10;
			break;	
			}else if(uiNoteList.get(i) instanceof ChordNote){
				if(uiNoteList.get(i).getWidth() == ((ChordNote)uiNoteList.get(i)).getNote(0).getWidth()){
					width = uiNoteList.get(i).getX() - uiNoteList.get(0).getX()+10;
					break;		
				}else{
					width = uiNoteList.get(i).getX() + ((ChordNote) uiNoteList.get(i)).getNote(0).getWidth()- uiNoteList.get(0).getX();
					break;		
				}
			}
			
		}
		int height = beamNum * beamWidth + (beamNum - 1) * beamGap;
		//������ܲ�ˮƽ�����Ӹ߶�
		if(!highNode.equalsIgnoreCase("flat")) height += width * ratio;
		setSize(width, height);
	}
	
	/**
	 * �����������
	 * @return
	 */
	public ArrayList<AbstractNote> getUiNoteList() {
		return uiNoteList;
	}

	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        
		if(highNode.equalsIgnoreCase("flat")){
			ratio = 0.0;
		}
		//��������������
		if(upOrDown.equalsIgnoreCase("up")){
			//�ұ߸�
			if(highNode.equalsIgnoreCase("right") || highNode.equalsIgnoreCase("flat")){
				int width = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(0).getX();
				Polygon p = new Polygon();
				p.addPoint(0, (int)(width*ratio));
				p.addPoint(0, (int)(width*ratio)+4);
				p.addPoint(width, 4);
				p.addPoint(width, 0);
				g.fillPolygon(p); 
				//��һ������,8�ַ���
				/***************************************************/
				ArrayList<AbstractNote> list = new ArrayList<AbstractNote>();
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 16){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							int y = (int)(deltax*ratio) + beamWidth + beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							y = (int)(deltax*ratio) + beamWidth + beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = (int)(deltax*ratio) + beamWidth + beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
					int y = (int)(deltax*ratio) + beamWidth + beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
					y = (int)(deltax*ratio) + beamWidth + beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = (int)(deltax*ratio) + beamWidth + beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//�ڶ������ܣ�16������
				/**************************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/32){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							int y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
					int y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
					y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = (int)(deltax*ratio) + 2*beamWidth + 2*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//����������,32������
				/*******************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/64){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							int y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
					int y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
					y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = (int)(deltax*ratio) + 3*beamWidth + 3*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//���������ܣ�64������
			}
			//////////////////////////////////////////////////////////////////////////////////////////////////////////
			//��߸�
			else{
				int width = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(0).getX();
				Polygon p = new Polygon();
				p.addPoint(width, (int)(width*ratio));
				p.addPoint(width, (int)(width*ratio)+4);
				p.addPoint(0, 4);
				p.addPoint(0, 0);
				g.fillPolygon(p); 
				//��һ������,8�ַ���
				/***************************************************/
				ArrayList<AbstractNote> list = new ArrayList<AbstractNote>();
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 16){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + beamWidth + beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							y = (int)((x)*ratio) + beamWidth + beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + beamWidth + beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y +(int)(noteWidth*ratio);//lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + beamWidth + beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					y = (int)(x*ratio) + beamWidth + beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + beamWidth + beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//�ڶ������ܣ�16������
				/**************************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/32){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth+50;
							y = y + (int)(noteWidth*ratio);//lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + 2*beamWidth + 2*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y -(int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//����������,32������
				/*******************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/64){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							int y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y + (int)(noteWidth*ratio); //lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
					y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = (int)(x*ratio) + 3*beamWidth + 3*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//���������ܣ�64������
			}
		}
		/********************************************��������������*************************************************/
		//��������������
		else if(upOrDown.equalsIgnoreCase("down")){
			//�ұ߸�
			if(highNode.equalsIgnoreCase("right") || highNode.equalsIgnoreCase("flat")){
				int width = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(0).getX();
				int height = getHeight();
				
				Polygon p = new Polygon();
				if(uiNoteList.get(0) instanceof Note){
					p.addPoint(0, height-beamWidth);
					p.addPoint(0, height);
				}else if(uiNoteList.get(0) instanceof ChordNote){
					int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
					if(uiNoteList.get(0).getWidth() == cNoteWidth){
						p.addPoint(0, height-beamWidth);
						p.addPoint(0, height);
					}else{
						p.addPoint(cNoteWidth, height-beamWidth);
						p.addPoint(cNoteWidth, height);
					}
				}
				if(uiNoteList.get(uiNoteList.size()-1) instanceof Note){
					p.addPoint(width, height-(int)(ratio*width));
					p.addPoint(width, height-(int)(ratio*width)-beamWidth);
				}else if(uiNoteList.get(uiNoteList.size()-1) instanceof ChordNote){
					int cNoteWidth = ((ChordNote) uiNoteList.get(uiNoteList.size()-1)).getNote(0).getWidth();
					if(uiNoteList.get(uiNoteList.size()-1).getWidth() == cNoteWidth){
						p.addPoint(width, height-(int)(ratio*width));
						p.addPoint(width, height-(int)(ratio*width)-beamWidth);
					}else{
						p.addPoint(width + cNoteWidth, height-(int)(ratio*width));
						p.addPoint(width + cNoteWidth, height-(int)(ratio*width)-beamWidth);
					}
				}
				
				g.fillPolygon(p); 
				//��һ������,8�ַ���
				/***************************************************/
				ArrayList<AbstractNote> list = new ArrayList<AbstractNote>();
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 16){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = startNote.getX() - uiNoteList.get(0).getX();
								yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									xStart = startNote.getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = startNote.getX() + startNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;							
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = endNote.getX() - uiNoteList.get(0).getX();
								yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = endNote.getX() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = endNote.getX() + endNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
							
							g.fillPolygon(poly);
							list.clear();							
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX() + noteWidth;
							int y = height - (int)(x*ratio) - 2*beamWidth - beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = startNote.getX() - uiNoteList.get(0).getX();
						yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = startNote.getX() - uiNoteList.get(0).getX() ;
							yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = startNote.getX() + startNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
							yStart =  height - (int)(xStart*ratio) - 2*beamWidth - beamGap;							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = endNote.getX() - uiNoteList.get(0).getX();
						yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
						poly.addPoint(xEnd,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = endNote.getX() - uiNoteList.get(0).getX() ;
							yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = endNote.getX() + endNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(xEnd*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = height - (int)(x*ratio) - 2*beamWidth - beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//�ڶ������ܣ�16������
				/**************************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/32){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
//								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
								yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;						
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
//								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
								yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
									yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int y = height - (int)(x*ratio) - 3*beamWidth - 2*beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							y = height - (int)(x*ratio) - 3*beamWidth - 2*beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX() + noteWidth;
							int y = height - (int)(x*ratio) - 3*beamWidth - 2*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
//						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
						yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
							yStart =  height - (int)(xStart*ratio) - 3*beamWidth - 2*beamGap;						
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
//						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
						yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
							yEnd =  height - (int)(xEnd*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int y = height - (int)(x*ratio) - 3*beamWidth - 2*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//����������,32������
				/*******************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/64){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
//								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
								yStart =  height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
									yStart =   height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;						
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
//								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
								yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
									yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int y = height - (int)(x*ratio) - 4*beamWidth - 3*beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							y = height - (int)(x*ratio) - 4*beamWidth - 3*beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX() + noteWidth;
							int y = height - (int)(x*ratio) - 4*beamWidth - 3*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y - (int)(noteWidth*ratio);
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
//						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
						yStart =  height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							yStart =  height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
							yStart =   height - (int)(xStart*ratio) - 4*beamWidth - 3*beamGap;						
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
//						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
						yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
							yEnd =  height - (int)(xEnd*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX() + noteWidth;
					int y = height - (int)(x*ratio) - 4*beamWidth - 3*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y + (int)(noteWidth*ratio);
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//���������ܣ�64������
			}
			//////////////////////////////////////////////////////////////////////////////////////////////////////////
			//��߸�
			else if(highNode.equalsIgnoreCase("left")){
				int width = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(0).getX();
				int height = getHeight();
				Polygon p = new Polygon();
				if(uiNoteList.get(0) instanceof Note){
					p.addPoint(0,height-(int)(ratio*width)-beamWidth );
					p.addPoint(0, height-(int)(ratio*width));
				}else if(uiNoteList.get(0) instanceof ChordNote){
					int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
					if(uiNoteList.get(0).getWidth() == cNoteWidth){
						p.addPoint(0, height-(int)(ratio*width)-beamWidth);
						p.addPoint(0, height-(int)(ratio*width));
					}else{
						p.addPoint(cNoteWidth, height-(int)(ratio*width)-beamWidth);
						p.addPoint(cNoteWidth, height-(int)(ratio*width));
					}
				}
				if(uiNoteList.get(uiNoteList.size()-1) instanceof Note){
					p.addPoint(width, height);
					p.addPoint(width, height-beamWidth);
				}else if(uiNoteList.get(uiNoteList.size()-1) instanceof ChordNote){
					int cNoteWidth = ((ChordNote) uiNoteList.get(uiNoteList.size()-1)).getNote(0).getWidth();
					if(uiNoteList.get(uiNoteList.size()-1).getWidth() == cNoteWidth){
						p.addPoint(width, height);
						p.addPoint(width, height-beamWidth);
					}else{
						p.addPoint(width + cNoteWidth, height);
						p.addPoint(width + cNoteWidth, height-beamWidth);
					}
				}
//				p.addPoint(width, getHeight()-4);
//				p.addPoint(width, getHeight());
//				p.addPoint(0, getHeight()-(int)(width*ratio));
//				p.addPoint(0, getHeight()-(int)(width*ratio)-4);
				g.fillPolygon(p); 
				//��һ������,8�ַ���
				/***************************************************/
				ArrayList<AbstractNote> list = new ArrayList<AbstractNote>();
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 16){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();	
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = startNote.getX() - uiNoteList.get(0).getX();
								yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									xStart = startNote.getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									xStart = startNote.getX() + startNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
									yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;							
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = endNote.getX() - uiNoteList.get(0).getX();
								yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = endNote.getX() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = endNote.getX() + endNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
									yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}							
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
//							int y = getHeight() - (int)(deltax*ratio) - 2*beamWidth - beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
//							y = getHeight() - (int)(deltax*ratio) - 2*beamWidth - beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = 0;
							if(uiNoteList.get(0) instanceof Note){
								x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							}else if(uiNoteList.get(0) instanceof ChordNote){
								int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
								if(uiNoteList.get(0).getWidth() == cNoteWidth){
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
								}else{
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX()+noteWidth;
								}
							}
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = getHeight() - (int)(deltax*ratio) - 2*beamWidth - beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y + (int)(noteWidth*ratio); //lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = startNote.getX() - uiNoteList.get(0).getX();
						yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							xStart = startNote.getX() - uiNoteList.get(0).getX();
							yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							xStart = startNote.getX() + startNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
							yStart =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;							
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = endNote.getX() - uiNoteList.get(0).getX();
						yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = endNote.getX() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = endNote.getX() + endNote.getNote(0).getWidth() - uiNoteList.get(0).getX();
							yEnd =  height - (int)(deltax*ratio) - 2*beamWidth - beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}	
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = getHeight() - (int)(deltax*ratio) - 2*beamWidth - beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//�ڶ������ܣ�16������
				/**************************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/32){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
//								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
								yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
									yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;						
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
//								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
								yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
//							int y = getHeight() - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
//							y = getHeight() - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = 0;
							if(uiNoteList.get(0) instanceof Note){
								x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							}else if(uiNoteList.get(0) instanceof ChordNote){
								int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
								if(uiNoteList.get(0).getWidth() == cNoteWidth){
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
								}else{
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX()+noteWidth;
								}
							}
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = getHeight() - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y + (int)(noteWidth*ratio);//lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
//						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
						yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							yStart =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;						
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
//						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
						yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							yEnd =  height - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = getHeight() - (int)(deltax*ratio) - 3*beamWidth - 2*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//����������,32������
				/*******************************************************/
				for(int i = 0; i < uiNoteList.size(); i++){
					if(uiNoteList.get(i).getDuration() <= 256/64){
						list.add(uiNoteList.get(i));
					}else{
						//�����ֹһ������
						if(!list.isEmpty() && list.size()!= 1){
							int startIndex = uiNoteList.indexOf(list.get(0));
							int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
							Polygon poly = new Polygon();
							int xStart = 0;
							int yStart = 0;
							int xEnd = 0;
							int yEnd = 0;
							if(uiNoteList.get(startIndex) instanceof Note){
//								Note startNote = (Note) uiNoteList.get(startIndex);
								xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
								yStart =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
								poly.addPoint(xStart,yStart);
								poly.addPoint(xStart,yStart + beamWidth);
							}else if(uiNoteList.get(startIndex) instanceof ChordNote){
								ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
								if(startNote.getWidth() == startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yStart =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
									xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
									yStart =   height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;						
									
									poly.addPoint(xStart,yStart);
									poly.addPoint(xStart,yStart + beamWidth);
								}
							
							}
							if(uiNoteList.get(endIndex) instanceof Note){
//								Note endNote = (Note) uiNoteList.get(endIndex);
								xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
								int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
								yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
								poly.addPoint(xEnd ,yEnd + beamWidth);
								poly.addPoint(xEnd ,yEnd);
								
							}else if(uiNoteList.get(endIndex) instanceof ChordNote){
								ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
								if(endNote.getWidth() == endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
									yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xEnd ,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
									xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
									int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
									yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
									poly.addPoint(xEnd,yEnd + beamWidth);
									poly.addPoint(xEnd,yEnd);
									
								}
							  
							}
//							int x = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
//							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
//							int y = getHeight() - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
//							poly.addPoint(x, y);
//							poly.addPoint(x, y+beamWidth);
//							x = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
//							deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
//							y = getHeight() - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
//							poly.addPoint(x, y+beamWidth);
//							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}//ֻ��һ������
						else if(!list.isEmpty()){
							int index = uiNoteList.indexOf(list.get(0));
							int x = 0;
							if(uiNoteList.get(0) instanceof Note){
								x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
							}else if(uiNoteList.get(0) instanceof ChordNote){
								int cNoteWidth = ((ChordNote) uiNoteList.get(0)).getNote(0).getWidth();
								if(uiNoteList.get(0).getWidth() == cNoteWidth){
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
								}else{
									x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX()+noteWidth;
								}
							}
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
							int y = getHeight() - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
							Polygon poly = new Polygon();
							poly.addPoint(x, y);
							poly.addPoint(x, y+beamWidth);
							x = x + noteWidth;
							y = y + (int)(noteWidth*ratio); //lwx
							poly.addPoint(x, y+beamWidth);
							poly.addPoint(x, y);
							g.fillPolygon(poly);
							list.clear();
						}
					}
				}
				//�������һ������
				if(!list.isEmpty() && list.size()!= 1){
					int startIndex = uiNoteList.indexOf(list.get(0));
					int endIndex = uiNoteList.indexOf(list.get(list.size()-1));
					Polygon poly = new Polygon();
					int xStart = 0;
					int yStart = 0;
					int xEnd = 0;
					int yEnd = 0;
					if(uiNoteList.get(startIndex) instanceof Note){
//						Note startNote = (Note) uiNoteList.get(startIndex);
						xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
						yStart =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
						poly.addPoint(xStart,yStart);
						poly.addPoint(xStart,yStart + beamWidth);
					}else if(uiNoteList.get(startIndex) instanceof ChordNote){
						ChordNote startNote = (ChordNote) uiNoteList.get(startIndex);						
						if(startNote.getWidth() == startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							yStart =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}else if(startNote.getWidth() == 2 * startNote.getNote(0).getWidth()){
							xStart = uiNoteList.get(startIndex).getX() + startNote.getNote(0).getWidth()- uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(startIndex).getX();
							yStart =   height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;						
							
							poly.addPoint(xStart,yStart);
							poly.addPoint(xStart,yStart + beamWidth);
						}
					
					}
					if(uiNoteList.get(endIndex) instanceof Note){
//						Note endNote = (Note) uiNoteList.get(endIndex);
						xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
						int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
						yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
						poly.addPoint(xEnd ,yEnd + beamWidth);
						poly.addPoint(xEnd ,yEnd);
						
					}else if(uiNoteList.get(endIndex) instanceof ChordNote){
						ChordNote endNote = (ChordNote) uiNoteList.get(endIndex);						
						if(endNote.getWidth() == endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xEnd ,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}else if(endNote.getWidth() == 2 * endNote.getNote(0).getWidth()){
							xEnd = uiNoteList.get(endIndex).getX() - uiNoteList.get(0).getX()+ endNote.getNote(0).getWidth();
							int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(endIndex).getX();
							yEnd =  height - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
							poly.addPoint(xEnd,yEnd + beamWidth);
							poly.addPoint(xEnd,yEnd);
							
						}
					  
					}
					g.fillPolygon(poly);
					list.clear();
				}//ֻ�����һ������
				else if(!list.isEmpty()){
					int index = uiNoteList.indexOf(list.get(0));
					int x = uiNoteList.get(index).getX() - uiNoteList.get(0).getX();
					int deltax = uiNoteList.get(uiNoteList.size()-1).getX() - uiNoteList.get(index).getX();
					int y = getHeight() - (int)(deltax*ratio) - 4*beamWidth - 3*beamGap;
					Polygon poly = new Polygon();
					poly.addPoint(x, y);
					poly.addPoint(x, y+beamWidth);
					x = x - noteWidth;
					y = y - (int)(noteWidth*ratio); //lwx
					poly.addPoint(x, y+beamWidth);
					poly.addPoint(x, y);
					g.fillPolygon(poly);
					list.clear();
				}
				//���������ܣ�64������
			}
		}
	}
	
	/**
	 * ���÷���
	 */
	public void locate(){
		int stemLength = NoteCanvas.STEM_LENGTH;
		int noteWidth = Note.NORMAL_HEAD_WIDTH;
		if(uiNoteList.get(0).getHighestNote() instanceof Grace){
			stemLength = Stem.GRACE_HEIGHT;
			noteWidth = Gracable.GRACE_WIDTH;
		}
		if(upOrDown.equalsIgnoreCase("up")){
			int x = uiNoteList.get(0).getX() + noteWidth;
			int y = MusicMath.getHighestNote(uiNoteList).getY() - stemLength;
			setLocation(x, y);
		}else if(upOrDown.equalsIgnoreCase("down")){
			int x = uiNoteList.get(0).getX();
			int y = MusicMath.getLowestNote(uiNoteList).getY() + stemLength - getHeight();
			setLocation(x, y);
		}
	}
	
	/**
	 * ����ָ��λ�õķ�������Ӧ������ʱ��
	 * @param index
	 * @return
	 */
	public static int getDurByBeamIndex(int index){
		switch(index){
		case 0:
			return 32;
		case 1:
			return 16;
		case 2:
			return 8;
		case 3:
			return 4;
		default:
			return -1;
		}
	}

}

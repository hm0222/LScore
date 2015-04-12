package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Selectable;
import sjy.elwg.notation.musicBeans.Stem;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * ������
 * @author sjy
 *
 */
public class Tie extends AbstractLine implements MouseListener,Selectable{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 7834196443431064912L;
	/**
	 * ��ʵ����
	 */
	private Note startNote;
	/**
	 * ��������
	 */
	private Note endNote;
	/**
	 * �Ƿ�ѡ��
	 */
	private boolean selected;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * �����ߵĻ��ȷ�����Чֵ"up"��"down".
	 * "up"��ʾ������Բ�ε����沿�֣����߷��������������������ֵĻ��ȷ��� "down"���෴.
	 */
	protected String upOrDown = "up";
	
	
	/**
	 * �չ��캯��
	 */
	public Tie(){
		super();
		addMouseListener(this);
			
	}
	
	/**
	 * ���캯��
	 * ͨ������XML����ʱ
	 * @param note
	 */
	public Tie(Note note){
		super();
		startNote = note;
		if(startNote != null)
			startNote.addTie(this);
		addMouseListener(this);
	}
	
	/**
	 * ���캯��
	 * ͨ������XML����ʱ
	 * @param note
	 * @param direction
	 */
	public Tie(Note note, String direction){
		super();
		startNote = note;
		upOrDown = direction;
		repaint();
		if(startNote != null)
			startNote.addTie(this);
		addMouseListener(this);
	}
	
	/**
	 * ���캯��
	 * ͨ�������û�������ӷ���
	 * @param startNote
	 * @param endNote
	 */
	public Tie(Note startNote, Note endNote){
		super();
		this.startNote = startNote;
		this.endNote = endNote;
		startNote.addTie(this);
		endNote.addTie(this);
		reShape();
		addMouseListener(this);
	}

	@Override
	public void reLocate() {
		// TODO Auto-generated method stub
		Controller.locateLine(this);
	}

	@Override
	public void reSize(int x) {
		// TODO Auto-generated method stub
		setSize(x, Slur.SLUR_HEIGHT);
	}

	@Override
	public void reShape() {
		// TODO Auto-generated method stub
		/*
		 * ��ʼ�����������������Ϊ��
		 */
		if(startNote != null && endNote != null){
			//�����ж�
			determineUpOrDown();
			
			NoteLine startLine = startNote.getMeasure().getMeasurePart().getNoteLine();
			NoteLine endLine = endNote.getMeasure().getMeasurePart().getNoteLine();
			
			//��������ͬһ��
			if(startLine == endLine){
				reSize(endNote.getX() - startNote.getX());
				Page page = startLine.getPage();
				if(getParent() != page) 
					page.add(this);
				//���֮ǰ���з֣�������Ĳ���ɾ��
				Tie nxtSlur = (Tie)nextSymbolLine;
				nextSymbolLine = null;
				while(nxtSlur != null){
					nxtSlur.setPreSymbolLine(null);
					if(nxtSlur.getParent() != null)
						((JPanel)nxtSlur.getParent()).remove(nxtSlur);
					if(nxtSlur.getEndNote() != null){
						nxtSlur.getEndNote().removeTie(nxtSlur);
					}
					nxtSlur = (Tie)nxtSlur.getNextSymbolLine();
				}
				
				reLocate();
			}
			//����������ͬһ�У�ͨ�����Ǹյ������ݵ�������������ڱ༭����.
			else{
				int lineDiff = MusicMath.NoteLineDiffs(startLine, endLine);
				ArrayList<AbstractLine> pieces = split(lineDiff + 1);
				NoteLine nxtLine = startLine;
				for(int i = 0, n = pieces.size(); i < n; i++){
					Tie sl = (Tie)pieces.get(i);
					sl.reShape();
					sl.reLocate();
					if(sl.getParent() != nxtLine.getPage())
						nxtLine.getPage().add(sl);
					nxtLine = MusicMath.nxtLine(nxtLine);
				}
			}
		}
		
		/*
		 * ��ʼ������Ϊ�գ���������Ϊ�գ����зֺ�ĵ�һ����
		 */
		else if(startNote != null && endNote == null){
			//�����ж�
			determineUpOrDown();
			
			//��֮��Ĳ�����ͬһ��,����кϲ�
			Tie nxtTie = nextSymbolLine == null ? null : (Tie)nextSymbolLine;
			NoteLine line = startNote.getMeasure().getMeasurePart().getNoteLine();
			if(nxtTie != null &&  nxtTie.getEndNote() != null){
				NoteLine nxtLine = nxtTie.getEndNote().getMeasure().getMeasurePart().getNoteLine();
				if(nxtLine == line){
					nextSymbolLine = null;
					nxtTie.setPreSymbolLine(null);
					line.getPage().remove(nxtTie);
					endNote = nxtTie.getEndNote();
					endNote.removeTie(nxtTie);
					endNote.addTie(this);
					
					reSize(endNote.getX() - startNote.getX());
					reLocate();
					return;
				}
			}
			reSize(NoteCanvas.lineWidth + NoteCanvas.xStart - startNote.getX());
			reLocate();
		}
		
		/*
		 * ��ʼ����Ϊ�գ�����������Ϊ�գ����зֺ�����һ���� 
		 */
		else if(startNote == null && endNote != null){
			NoteLine line = endNote.getMeasure().getMeasurePart().getNoteLine();
			MeasurePart firstPart = line.getMeaPartList().get(0);
			reSize(endNote.getX() + endNote.getWidth()/2 - NoteCanvas.xStart - firstPart.maxAttrWidth());
			reLocate();
		}
		
	}

	public ArrayList<AbstractLine> split(int num) {
		// TODO Auto-generated method stub
		ArrayList<AbstractLine> pieces = new ArrayList<AbstractLine>();
//		if(num != 2){
//			System.err.println("������ֻ�ܱ��з�Ϊ�����֣�����Ϊ��"+num);
//			return null;
//		}
		
		Note endNote = this.endNote;
		this.endNote = null;
		
		pieces.add(this);
		
		Tie sl = new Tie(null, this.upOrDown);
		sl.setEndNote(endNote);
		endNote.removeTie(this);
		endNote.addTie(sl);
		pieces.add(sl);
		nextSymbolLine = sl;
		sl.setPreSymbolLine(this);
		
		return pieces;
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHints(renderHints);
        
        if(selected)
        	g.setColor(Color.blue);
        else 
        	g.setColor(Color.black);
        
        if(upOrDown.equalsIgnoreCase("up"))
    		g.drawArc(0, 2, getWidth(), (int)(getHeight()*1.5), 15, 150);
    	else if(upOrDown.equalsIgnoreCase("down"))
    		g.drawArc(0, -10, getWidth(), (int)(getHeight()*1.5-1), -15, -150);
	}
	
	public void setSize(int width, int height){
		if(width < 30){
			super.setSize(width, 11);
			repaint();
			return;
		}
		else if(width >= 30){
			super.setSize(width, 20);
			repaint();
			return;
		}
		super.setSize(width, (int)(width * 0.2));
		repaint();
	}

	/**
	 * �ж������߻��ȷ���
	 */
	public void determineUpOrDown(){
		//�����ж�
		Stem stem = startNote.getChordNote() == null ? 
				startNote.getStem() : startNote.getChordNote().getStem();
		if(stem != null && 
				stem.getY() < startNote.getHighestNote().getY()){
			upOrDown = "down";
			repaint();
		}else{
			upOrDown = "up";
			repaint();
		}
	}

	public Note getStartNote() {
		return startNote;
	}

	public void setStartNote(Note startNote) {
		this.startNote = startNote;
	}

	public Note getEndNote() {
		return endNote;
	}

	public void setEndNote(Note endNote) {
		this.endNote = endNote;
	}

	public String getUpOrDown() {
		return upOrDown;
	}

	public void setUpOrDown(String upOrDown) {
		this.upOrDown = upOrDown;
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
//	JComponent p = (JComponent)getStartNote().getParent().getParent();
//		
//		((NoteCanvas) p).cancleAllSelected();
//		selected = true;
//		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
//		selected = false;
//		repaint();
	}


	public void beSelected() {
		// TODO Auto-generated method stub
		if(!selected){
			selected = true;
			repaint();
		}
	}

	public void cancleSelected() {
		// TODO Auto-generated method stub
		if(selected){
			selected = false;
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==this){
			if(!selected)
			selected = true;
			repaint();
		}				
	}




}

package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComponent;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Selectable;
import sjy.elwg.utility.MusicMath;

/**
 * ������Ϊ������λ���������ŵĸ��࣬�������е�������������һ��������Ϊ��㡢һ��������Ϊ�յ�.
 * ���������������������Բ�𣬸��಻����������. ��������һ���̳���AbstractSymbolLine.
 * @author jingyuan.sun
 *
 */
public abstract class SymbolLine extends AbstractLine implements Selectable, MouseListener, MouseMotionListener{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = -6855117194689288517L;
	/**
	 * ��ʼ����
	 */
	protected AbstractNote startNote;
	/**
	 * ��������
	 */
	protected AbstractNote endNote;
	/**
	 * ����¼������Ļ������
	 */
	protected Point screenPoint = new Point();
	/**
	 * ���û����϶�����x�����y����
	 * �ڽ��з��ŷ���ʱ����λ����Ĭ��λ�����϶�λ��֮��
	 */
	protected int draggedX = 0;
	protected int draggedY = 0;
	
	/**
	 * ���캯��
	 */
	public SymbolLine(){
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * ������ʼ���������������������С��λ��
	 */
	public void reShape(){
		repaint();
		
		/*
		 * ��ʼ�����������������Ϊ��
		 */
		if(startNote != null && endNote != null){
			
			NoteLine startLine = startNote.getMeasure().getMeasurePart().getNoteLine();
			NoteLine endLine = endNote.getMeasure().getMeasurePart().getNoteLine();
			
			//��������ͬһ��
			if(startLine == endLine){
				reSize(endNote.getX() - startNote.getX());
				Page page = startLine.getPage();
				if(getParent() != page) 
					page.add(this);
				//���֮ǰ���з֣�������Ĳ���ɾ��
				SymbolLine nxtSlur = (SymbolLine)nextSymbolLine;
				nextSymbolLine = null;
				while(nxtSlur != null){
					nxtSlur.setPreSymbolLine(null);
					if(nxtSlur.getParent() != null)
						((JComponent)nxtSlur.getParent()).remove(nxtSlur);
					if(nxtSlur.getEndNote() != null){
						nxtSlur.getEndNote().getSymbolLines().remove(nxtSlur);
					} 
					
					nxtSlur = (SymbolLine)nxtSlur.getNextSymbolLine();
				}
				reLocate();
			}
			//����������ͬһ�У�ͨ�����Ǹյ������ݵ�������������ڱ༭����.
			else{
				int lineDiff = MusicMath.NoteLineDiffs(startLine, endLine);
				ArrayList<AbstractLine> pieces = split(lineDiff + 1);
				NoteLine nxtLine = startLine;
				for(int i = 0, n = pieces.size(); i < n; i++){
					SymbolLine sl = (SymbolLine)pieces.get(i);
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
			
			//��֮��Ĳ�����ͬһ��,����кϲ�
			SymbolLine nxtTie = nextSymbolLine == null ? null : (SymbolLine)nextSymbolLine;
			NoteLine line = startNote.getMeasure().getMeasurePart().getNoteLine();
			if(nxtTie != null &&  nxtTie.getEndNote() != null){
				NoteLine nxtLine = nxtTie.getEndNote().getMeasure().getMeasurePart().getNoteLine();
				if(nxtLine == line){
					nextSymbolLine = null;
					nxtTie.setPreSymbolLine(null);
					line.getPage().remove(nxtTie);
					endNote = nxtTie.getEndNote();
					endNote.getSymbolLines().remove(nxtTie);
					endNote.getSymbolLines().add(this);
				}
			}
			
			reSize(NoteCanvas.lineWidth + NoteCanvas.xStart - startNote.getX());
			reLocate();
		}
		
		/*
		 * ��ʼ����Ϊ�գ�����������Ϊ�գ����зֺ�����һ���� 
		 */
		else if(startNote == null && endNote != null){
			NoteLine startLine = null;
			if(preSymbolLine != null){
				NoteLine preLine = (NoteLine)MusicMath.getNoteLineBySymbolLine((SymbolLine)preSymbolLine).get(0);
				startLine = MusicMath.nxtLine(preLine);
			}
			NoteLine line = endNote.getMeasure().getMeasurePart().getNoteLine();
			//�������
			if(startLine == line){
				MeasurePart firstPart = line.getMeaPartList().get(0);
				reSize(endNote.getX() + endNote.getWidth()/2 - NoteCanvas.xStart - firstPart.maxAttrWidth());
				//���֮ǰ���з֣�������Ĳ���ɾ��
				SymbolLine nxtSlur = (SymbolLine)nextSymbolLine;
				nextSymbolLine = null;
				while(nxtSlur != null){
					nxtSlur.setPreSymbolLine(null);
					if(nxtSlur.getParent() != null)
						nxtSlur.getParent().remove(nxtSlur);
					if(nxtSlur.getEndNote() != null){
						nxtSlur.getEndNote().getSymbolLines().remove(nxtSlur);
					}
					nxtSlur = (SymbolLine)nxtSlur.getNextSymbolLine();
				}
				reLocate();
			}
			//��Ҫ�з�
			else{
				ArrayList<AbstractLine> pieces = split(2);
				NoteLine nxtLine = startLine;
				for(int i = 0, n = pieces.size(); i < n; i++){
					SymbolLine sl = (SymbolLine)pieces.get(i);
					sl.reShape();
					sl.reLocate();
					if(sl.getParent() != nxtLine.getPage())
						nxtLine.getPage().add(sl);
					nxtLine = MusicMath.nxtLine(nxtLine);
				}
			}
		}
		
		/*
		 * ��ʼ���������������Ϊ�գ����зֺ���м䲿��
		 */
		else{
			NoteLine curLine = (NoteLine)MusicMath.getNoteLineBySymbolLine(this).get(0);
			int attrWidth = curLine.getMeaPartList().get(0).maxAttrWidth(); 
			reSize(NoteCanvas.lineWidth - attrWidth);
			reLocate();
		}
	}
	
	/**
	 * �����������ŵ�һ����ʵ��.
	 * ���巵�صĶ����������������.
	 * @return
	 */
	public abstract SymbolLine getSymbolLineInstance();
	
	/**
	 * ���������Ľ�������
	 * @param note
	 */
	public void shiftEndNote(AbstractNote note){
		//ɾ��ԭ�����������Ը÷��ŵ�����
		if(this.endNote != null && this.endNote.getSymbolLines().size() > 0){
			this.endNote.getSymbolLines().remove(this);
		}
		//���½�������
		this.endNote = note;
		//����½�������������
		endNote.getSymbolLines().add(this);
		reShape();
	}
	
	/**
	 * ���������Ž����з�.
	 */
	public ArrayList<AbstractLine> split(int num){
		if(num < 2)
			return null;
		
		AbstractNote endNote = this.endNote;
		this.endNote = null;
		
		ArrayList<AbstractLine> pieces = new ArrayList<AbstractLine>();
		pieces.add(this);
		
		for(int i = 1; i < num; i++){
			SymbolLine sbl = getSymbolLineInstance();
			if(i < num - 1)
				sbl.setEndNote(null);
			else {
				sbl.setEndNote(endNote);
				if(endNote != null){
					endNote.getSymbolLines().remove(this);
					endNote.getSymbolLines().add(sbl);
				}
			}
			pieces.add(sbl);
			sbl.setPreSymbolLine(pieces.get(i - 1));
			pieces.get(i - 1).setNextSymbolLine(sbl);
			sbl.repaint();//����������֮���ػ�һ��
		}
		
		return pieces;
	}
	

	public AbstractNote getStartNote() {
		return startNote;
	}

	public void setStartNote(AbstractNote startNote) {
		this.startNote = startNote;
	}

	public AbstractNote getEndNote() {
		return endNote;
	}

	public void setEndNote(AbstractNote endNote) {
		this.endNote = endNote;
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

	public void beSelected(){
		if(status == NORMAL_STATUS){
			status = SELECT_STATUS;
			repaint();
		}
	}
	
	public void cancleSelected(){
		if(status != NORMAL_STATUS){
			status = NORMAL_STATUS;
			repaint();
		}
	}
	
	public void mouseClicked(MouseEvent e){
		
	}
	
    public void mousePressed(MouseEvent e){
		screenPoint.setLocation((int)e.getXOnScreen(), (int)e.getYOnScreen());
	}
    
    public void mouseEntered(MouseEvent e){
		
	}
    
    public void mouseExited(MouseEvent e){
		
	}
    
    public void mouseReleased(MouseEvent e){
    	
    }
    
    public void mouseMoved(MouseEvent e){
    	
    }
    
    public void mouseDragged(MouseEvent e){
    	if(status == SELECT_STATUS){
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
	else if(status == EDIT_STATUS){
    		int x = e.getXOnScreen();
        	int y = e.getYOnScreen();
        	int deltax = x - (int)screenPoint.getX();
        	screenPoint.setLocation(x, y);	

		setSize(getWidth() + deltax, getHeight());
		repaint();
    	}

    }

}

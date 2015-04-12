package sjy.elwg.notation.musicBeans.symbolLines;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Selectable;
import sjy.elwg.utility.MusicMath;

/**
 * ���ӼǺŵĸ���
 * ���������һ��С�ڵĿ���Ϊ��㣬���Ը�С�ڻ���ĳһ��С�ڵĽ���Ϊ�յ�
 * @author tequila
 */
public abstract class RepeatLine extends AbstractLine implements Selectable, MouseListener, MouseMotionListener{
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ��ʼС����
	 */
	protected MeasurePart startMeasurePart; 
	
	/**
	 * ��ֹС����
	 */
	protected MeasurePart endMeasurePart;
	
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
	
	public RepeatLine(){
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void reShape() {
		
		// TODO Auto-generated method stub
		if(startMeasurePart != null && endMeasurePart != null){
			
			NoteLine startLine = startMeasurePart.getNoteLine();
			NoteLine endLine = endMeasurePart.getNoteLine();
			
			/**
			 * ��ʼС�ںͽ���С��Ϊͬһ��
			 */
				if(startMeasurePart == endMeasurePart){
					reSize(startMeasurePart.getWidth());
					reLocate();
				}	
				
			/**
			 * ��ʼС�ںͽ���С�ھ���Ϊ��
			 */
				//����С����һ��
				else if(startLine == endLine){
					reSize(endMeasurePart.getX() - startMeasurePart.getX() + endMeasurePart.getWidth());
					Page page = startLine.getPage();
					if(getParent() != page){
						page.add(this);
					}
					//���֮ǰ���з֣�������Ĳ���ɾ��
					RepeatLine nxtRepeatLine = (RepeatLine)nextSymbolLine;
					nextSymbolLine = null;
					while(nxtRepeatLine != null){
						nxtRepeatLine.setPreSymbolLine(null);
						if(nxtRepeatLine.getParent() != null)
							((JComponent)nxtRepeatLine.getParent()).remove(nxtRepeatLine);
						if(nxtRepeatLine.getEndMeasurePart() != null){
							nxtRepeatLine.getEndMeasurePart().getRepeatLines().remove(nxtRepeatLine);
						}
						nxtRepeatLine = (RepeatLine)nxtRepeatLine.getNextSymbolLine();
					}
					reLocate();
				}
				
				//����С�ڲ���һ��
				else if(startLine != endLine){

					int lineDiff = MusicMath.NoteLineDiffs(startLine, endLine);
					ArrayList<AbstractLine> pieces = split(lineDiff + 1);
					NoteLine nxtLine = startLine;
					for(int i = 0, n = pieces.size(); i < n; i++){
						RepeatLine sl = (RepeatLine)pieces.get(i);
						sl.reShape();
						sl.reLocate();
						if(sl.getParent() != nxtLine.getPage())
							nxtLine.getPage().add(sl);
						nxtLine = MusicMath.nxtLine(nxtLine);
					}
				}

			}

			/**
			 * ��ʼС�ڲ�Ϊ�գ�����С��Ϊ�գ��зֺ�ĵ�һ����
			 */
			else if(startMeasurePart != null && endMeasurePart == null){
				//��֮��Ĳ�����ͬһ��,����кϲ�
				RepeatLine nxtRepeatLine = nextSymbolLine == null ? null: (RepeatLine)nextSymbolLine;
				NoteLine line = startMeasurePart.getNoteLine();
				if(nxtRepeatLine != null && nxtRepeatLine.getEndMeasurePart() != null){
					NoteLine nxtLine = nxtRepeatLine.getEndMeasurePart().getNoteLine();
					if(nxtLine == line){
						nextSymbolLine = null;
						nxtRepeatLine.setPreSymbolLine(null);
						line.getPage().remove(nxtRepeatLine);
						endMeasurePart = nxtRepeatLine.getEndMeasurePart();
						endMeasurePart.getRepeatLines().add(this);
						
					}
				}	
				reSize(NoteCanvas.lineWidth + NoteCanvas.xStart - startMeasurePart.getX() );
				reLocate();
			}
		
			/**
			 * ��ʼС��Ϊ�գ�����С�ڲ�Ϊ�գ��зֺ�ĵڶ�����
			 */
			else if(startMeasurePart == null && endMeasurePart != null){
				NoteLine line = endMeasurePart.getNoteLine();
				MeasurePart firstMeasurePart = line.getMeaPartList().get(0);
				reSize(endMeasurePart.getX() - firstMeasurePart.getX() + endMeasurePart.getWidth());
				//���֮ǰ���з֣�������Ĳ���ɾ��
				RepeatLine nxtRepeatLine = (RepeatLine)nextSymbolLine;
				nextSymbolLine = null;
				while(nxtRepeatLine != null){
					nxtRepeatLine.setPreSymbolLine(null);
					if(nxtRepeatLine.getParent() != null)
						((JPanel)nxtRepeatLine.getParent()).remove(nxtRepeatLine);
					if(nxtRepeatLine.getEndMeasurePart() != null){
						nxtRepeatLine.setEndMeasurePart(null);
					}
//				//	reSize(NoteCanvas.lineWidth + NoteCanvas.xStart - startMeasurePart.getX() );
//					reSize(50);
					reLocate();
					
				}
			}
//			/**
//			 * ��ʼС�ںͽ���С�ھ�Ϊ�գ��зֺ���м䲿��
//			 */

		
		
	}

	
	/**
	 * �����������ŵ�һ����ʵ��.
	 * ���巵�صĶ����������������.
	 * @return
	 */
	public abstract RepeatLine getRepeatLineInstance();


	@Override
	public ArrayList<AbstractLine> split(int num) {
		// TODO Auto-generated method stub
		if(num < 2)
			return null;
		MeasurePart endMeasurePart = this.endMeasurePart;
		this.endMeasurePart = null;
		

		
		ArrayList<AbstractLine> pieces = new ArrayList<AbstractLine>();
		pieces.add(this);
		
		for(int i = 1; i < num; i++){
			RepeatLine rl = getRepeatLineInstance();
			if(i < num - 1)
				rl.setEndMeasurePart(null);
			
			else {
				rl.setEndMeasurePart(endMeasurePart);
				endMeasurePart.getRepeatLines().add(rl);
			}
			pieces.add(rl);
			rl.setPreSymbolLine(pieces.get(i - 1));
			pieces.get(i - 1).setNextSymbolLine(rl);
		}
		
		return pieces;
	}

	public void shiftEndMeasure(MeasurePart measurePart){
		//ɾ��ԭ������С�ڶԸ÷��ŵ�����
		if(this.endMeasurePart != null && this.endMeasurePart.getRepeatLines().size() > 0){
			this.endMeasurePart.getRepeatLines().remove(this);
		}
		//���½���С��
		this.endMeasurePart = measurePart;
		//����½�������������
		endMeasurePart.getRepeatLines().add(this);
		reShape();
	}

	@Override
	public void beSelected() {
		// TODO Auto-generated method stub
		if(status == NORMAL_STATUS){
			status = SELECT_STATUS;
			repaint();
		}
	}

	

	@Override
	public void cancleSelected() {
		// TODO Auto-generated method stub
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
    	}else if(status == EDIT_STATUS){
    		int x = e.getXOnScreen();
        	int x1 = this.getX();
        	
        	if((x - x1) > 30){
        		reSize(x - x1);		
        	}	
    	}

    }


	public MeasurePart getStartMeasurePart() {
		return startMeasurePart;
	}


	public void setStartMeasurePart(MeasurePart startMeasurePart) {
		this.startMeasurePart = startMeasurePart;
	}


	public MeasurePart getEndMeasurePart() {
		return endMeasurePart;
	}


	public void setEndMeasurePart(MeasurePart endMeasurePart) {
		this.endMeasurePart = endMeasurePart;
	}

}

package sjy.elwg.utility;


import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.action.AddChordAction;
import sjy.elwg.notation.action.ChangeMeaTimeAction;
import sjy.elwg.notation.action.ChangeMultiTimeAction;
import sjy.elwg.notation.action.ChangeRythmAction;
import sjy.elwg.notation.action.ClearMeasureAction;
import sjy.elwg.notation.action.DelNoteAction;
import sjy.elwg.notation.action.RemoveChordAction;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Annotation;
import sjy.elwg.notation.musicBeans.Barline;
import sjy.elwg.notation.musicBeans.Beam;
import sjy.elwg.notation.musicBeans.ChordGrace;
import sjy.elwg.notation.musicBeans.ChordNote;
import sjy.elwg.notation.musicBeans.FreeAddedText;
import sjy.elwg.notation.musicBeans.Gracable;
import sjy.elwg.notation.musicBeans.Grace;
import sjy.elwg.notation.musicBeans.GraceSymbol;
import sjy.elwg.notation.musicBeans.Lyrics;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.RepeatSymbol;
import sjy.elwg.notation.musicBeans.Score;
import sjy.elwg.notation.musicBeans.Selectable;
import sjy.elwg.notation.musicBeans.SharpOrFlat;
import sjy.elwg.notation.musicBeans.Stem;
import sjy.elwg.notation.musicBeans.Tail;
import sjy.elwg.notation.musicBeans.TempoText;
import sjy.elwg.notation.musicBeans.Time;
import sjy.elwg.notation.musicBeans.TremoloBeam;
import sjy.elwg.notation.musicBeans.Tuplet;
import sjy.elwg.notation.musicBeans.UIClef;
import sjy.elwg.notation.musicBeans.UIDot;
import sjy.elwg.notation.musicBeans.UIKey;
import sjy.elwg.notation.musicBeans.UITime;
import sjy.elwg.notation.musicBeans.symbolLines.AbstractLine;
import sjy.elwg.notation.musicBeans.symbolLines.Breath;
import sjy.elwg.notation.musicBeans.symbolLines.Cre;
import sjy.elwg.notation.musicBeans.symbolLines.Cresc;
import sjy.elwg.notation.musicBeans.symbolLines.Dim;
import sjy.elwg.notation.musicBeans.symbolLines.Dimc;
import sjy.elwg.notation.musicBeans.symbolLines.Dynamic;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.OctaveDown;
import sjy.elwg.notation.musicBeans.symbolLines.OctaveUp;
import sjy.elwg.notation.musicBeans.symbolLines.Ornament;
import sjy.elwg.notation.musicBeans.symbolLines.Pedal;
import sjy.elwg.notation.musicBeans.symbolLines.PerformanceSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.RepeatLine;
import sjy.elwg.notation.musicBeans.symbolLines.Slur;
import sjy.elwg.notation.musicBeans.symbolLines.SymbolLine;
import sjy.elwg.notation.musicBeans.symbolLines.Tie;
import sjy.elwg.notation.musicBeans.symbolLines.Vibrato;
/**
 * ���������.����������в��ֵȲ���
 * @author sjy
 *
 */
public class Controller {
	
	
	/**
	 * ����
	 */
	private NoteCanvas canvas;
	
	/**
	 * ����������
	 */
	private ActionHistoryController actionController = ActionHistoryController.getInstance();
	
	public Controller(NoteCanvas canvas){
		this.canvas = canvas;
	}
	
	/**
	 * ��û���
	 * @return
	 */
	public NoteCanvas getCanvas() {
		return canvas;
	}

	/**
	 * ������������
	 * @param tie
	 */
	public static void locateLine(AbstractLine sbl){
		//������
		int draggedX = 0;
		int draggedY = 0;
		if(sbl instanceof SymbolLine){
			draggedX = ((SymbolLine)sbl).getDraggedX();
			draggedY = ((SymbolLine)sbl).getDraggedY();
		}
		if(sbl instanceof SymbolLine){
			SymbolLine sl = (SymbolLine)sbl;
			if(sl.getStartNote() != null){
				//�������������SymbolLine���ŵķ��ã�����������β����������β���������
				AbstractNote startNote = sl.getStartNote();
//				Measure measure = startNote.getMeasure();
				if(sl instanceof Cre || sl instanceof Dim || sl instanceof Vibrato || sl instanceof Dimc || sl instanceof Cresc){
					sl.setLocation(startNote.getX()  + draggedX-2+12, startNote.getMeasure().getY() - sl.getHeight() + draggedY);
				}else if(sl instanceof OctaveUp){
					sl.setLocation(startNote.getX()  + draggedX-2, startNote.getMeasure().getY() - sl.getHeight() + draggedY);
				}

				else if(sl instanceof OctaveDown ){
					sl.setLocation(startNote.getX()  + draggedX-2, startNote.getY() + startNote.getHeight()  + draggedY);
				}
				if(sl instanceof Slur){
						//����β��������β�����������
						if(sl.getEndNote() != null){
							
							//��������β�������з���
							if(startNote.getStem() != null && sl.getEndNote().getStem() != null){
								//�����������ϣ�β������������
								if(startNote.isStemUp() && !sl.getEndNote().isStemUp()){
									int y = Math.max(startNote.getStem().getY(), sl.getEndNote().getY());
									sl.setLocation(startNote.getX() + 2 + draggedX,  y - sl.getHeight() + draggedY );
								}
								//�������������ϣ�β������������
								else if(startNote.isStemUp() && sl.getEndNote().isStemUp() ){
									int y = Math.min(startNote.getLowestNote().getY() + 10, sl.getEndNote().getLowestNote().getY() + 10);
									sl.setLocation(startNote.getX() + 2 + draggedX,  y  + draggedY );
								}
								
								//�������������£�β����������
								else if(!startNote.isStemUp() && sl.getEndNote().isStemUp()){
									int y = Math.max(startNote.getY(), sl.getEndNote().getStem().getY());
									sl.setLocation(startNote.getX() + 2 + draggedX,  y  - sl.getHeight() + draggedY );
								}
								
								//�������������£�β����������
								else if(!startNote.isStemUp() && !sl.getEndNote().isStemUp() ){
									int y = Math.max(startNote.getHighestNote().getY(), sl.getEndNote().getHighestNote().getY());
									sl.setLocation(startNote.getX() + 2 + draggedX,  y  - sl.getHeight() + draggedY );
								}						
							}
							//���з��ˣ�β��
							else if(startNote.getStem() != null && sl.getEndNote().getStem() == null){			
//								int yS = startNote.getStem().getY() < startNote.getY() ? startNote.getStem().getY() : startNote.getY();
//								//ȡ���������˺ͷ�ͷ��y�������ֵ��β�����Ƚ�

								if(startNote.isStemUp()){
									int y= Math.min(sl.getEndNote().getY(),startNote.getLowestNote().getY());
									if( sl.getEndNote().getDuration() <= 64){
										sl.setLocation(startNote.getX() + 2 + draggedX, y + sl.getEndNote().getHeight()/2 + 5 + draggedY);	
									}else{
										sl.setLocation(startNote.getX() + 2 + draggedX, y + sl.getEndNote().getHeight() / 2 + draggedY);			
									}							
								}else{
									int y= Math.max(sl.getEndNote().getY() ,startNote.getHighestNote().getY());
									sl.setLocation(startNote.getX() + 2 + draggedX, y - sl.getHeight() + draggedY);		
								}
								
							}
							//���ޣ�β��
							else if(startNote.getStem() == null && sl.getEndNote().getStem() != null){	
								if(sl.getEndNote().isStemUp()){
									
									int y= Math.min(sl.getStartNote().getY(),sl.getEndNote().getLowestNote().getY());
//									if( sl.getEndNote().getDuration() <= 64){
//										sl.setLocation(startNote.getX() + 2 + draggedX, y + sl.getEndNote().getHeight()/2 + 5 + draggedY);	
//									}else{
//										sl.setLocation(startNote.getX() + 2 + draggedX, y + sl.getEndNote().getHeight() / 2 + draggedY);			
//									}	
//									
									sl.setLocation(startNote.getX() + 2 + draggedX, y + 10 + draggedY);
								}else{
									int y= Math.max(sl.getStartNote().getY(),sl.getEndNote().getHighestNote().getY());	
									sl.setLocation(startNote.getX() + 2 + draggedX, y - sl.getHeight() + draggedY);
								}
								
							}
							//���ޣ�β��
							else{
								AbstractNote eNote = (AbstractNote) sl.getEndNote();
								if(startNote.isRest()){
									if(eNote.isRest()){
										int y = Math.max(startNote.getY(), sl.getEndNote().getY());
										sl.setLocation(startNote.getX() + 2 + draggedX, y- sl.getHeight() + draggedY);
									}else{
										if(eNote.getHighestNote().getPitch() >= 4){
											int y = Math.max(startNote.getY(), sl.getEndNote().getHighestNote().getY());
											sl.setLocation(startNote.getX() + 2 + draggedX, y- sl.getHeight() + draggedY);
										}else{
											int y = Math.min(startNote.getY(), sl.getEndNote().getLowestNote().getY());
											sl.setLocation(startNote.getX() + 2 + draggedX, y + draggedY);
										}
									}
								}else{
									if(startNote.getHighestNote().getPitch() >= 4){
										int y = Math.max(startNote.getHighestNote().getY(), sl.getEndNote().getHighestNote().getY());
										sl.setLocation(startNote.getX() + 2 + draggedX, y- sl.getHeight() + draggedY);
									}else{
										int y = Math.min(startNote.getLowestNote().getY(), sl.getEndNote().getLowestNote().getY());
										sl.setLocation(startNote.getX() + 2 + draggedX, y + 12 + draggedY);
									}
								}
					
							}
							
						}
						//��β����
						else{
							//����������з���
							if(startNote.getStem() != null){
								if(startNote.isStemUp()){
									sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY() + draggedY);
								}else{
									sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY()- sl.getHeight() + draggedY);
								}
							}
							//�������޷���
							else{
								if(startNote.getHighestNote().getPitch() >= 4){
									
									sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY()- sl.getHeight() + draggedY);
								}else{
									sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY() + draggedY);
								}
							}
							sl.setLocation(startNote.getX() + 2 + draggedX, startNote.getY() - sl.getHeight() + draggedY);
							}
//						}
					//slur����
//					else{
//					
//						if(sl.getEndNote() != null){
//							int y = startNote.getY() < sl.getEndNote().getY() ? startNote.getY() + startNote.getHeight() - 5: sl.getEndNote().getY() + sl.getEndNote().getHeight() - 5;
//							sl.setLocation(startNote.getX() + 4 + draggedX, y + draggedY);
//						}else{
//							sl.setLocation(startNote.getX() + 4 + draggedX, startNote.getY() + startNote.getHeight() - 5 + draggedY);
//						}
//						
//					}
				}
			}
			else if(sl.getEndNote() != null){
				
				AbstractNote endNote = sl.getEndNote();
//				Measure measure = endNote.getMeasure();
				if(sl instanceof Cre || sl instanceof Dim || sl instanceof OctaveUp || sl instanceof Vibrato|| sl instanceof Dimc || sl instanceof Cresc){
					sl.setLocation(endNote.getX() - sl.getWidth() + 3 + draggedX, endNote.getMeasure().getY() - sl.getHeight() + draggedY);
				}
				else if(sl instanceof OctaveDown ){
					sl.setLocation(endNote.getX() - sl.getWidth() + 3 + draggedX, endNote.getY() + endNote.getHeight() + Measure.MEASURE_HEIGHT + draggedY);
				}
				if(sl instanceof Slur){
					if(((Slur)sl).getUpOrDown().equalsIgnoreCase("up"))
						sl.setLocation(endNote.getX() - sl.getWidth() + 3 + draggedX, endNote.getY() - sl.getHeight() + draggedY);
					else
						sl.setLocation(endNote.getX() - sl.getWidth() + 8 + draggedX, endNote.getY() + endNote.getHeight()+draggedY);
				}
			}
			else{
				NoteLine line = (NoteLine)MusicMath.getNoteLineBySymbolLine(sl).get(0);
				int meaIndex = (Integer)MusicMath.getNoteLineBySymbolLine(sl).get(1);
				int notex = line.getMeaPartList().get(0).maxAttrWidth() + NoteCanvas.xStart;
				int measurey = line.getMeaPartList().get(0).getMeasure(meaIndex).getY();
				if(sl instanceof Cre || sl instanceof Dim || sl instanceof OctaveUp || sl instanceof Vibrato){
					sl.setLocation(notex + draggedX, measurey - sl.getHeight() + draggedY);
				}
				else if(sl instanceof OctaveDown){
					sl.setLocation(notex + draggedX, measurey + Measure.MEASURE_HEIGHT + draggedY);
				}
				if(sl instanceof Slur){
					if(((Slur)sl).getUpOrDown().equalsIgnoreCase("up"))
						sl.setLocation(notex + draggedX, measurey - 2 + draggedY);
					else
						sl.setLocation(notex + draggedX, measurey + Measure.MEASURE_HEIGHT + draggedY);
				}
			}
		}
		//������
		else if(sbl instanceof Tie){
			Tie tie = (Tie)sbl;
			if(tie.getStartNote() != null){
				Note startNote = tie.getStartNote();
				if(tie.getUpOrDown().equalsIgnoreCase("up"))
					tie.setLocation(startNote.getX() + 2, startNote.getY() - tie.getHeight()/2);
				else
					tie.setLocation(startNote.getX() + 3, startNote.getY() + startNote.getHeight()-9);
			}
			else if(tie.getEndNote() != null){
				Note endNote = tie.getEndNote();
				if(tie.getUpOrDown().equalsIgnoreCase("up"))
					tie.setLocation(endNote.getX() - tie.getWidth() + 3, endNote.getY() - tie.getHeight()/2);
				else
					tie.setLocation(endNote.getX() - tie.getWidth() + 8, endNote.getY() + endNote.getHeight()-9);
			}
		}
		//���ӼǺ�
		else if(sbl instanceof RepeatLine){
			RepeatLine repeatLine = (RepeatLine)sbl;
			if(repeatLine.getStartMeasurePart() != null){
				MeasurePart measurePart = repeatLine.getStartMeasurePart();
				repeatLine.setLocation(measurePart.getX(), measurePart.getY()-30);
				//repeatLine.setLocation(50, 50);
			}
			else if(repeatLine.getStartMeasurePart() == null && repeatLine.getEndMeasurePart() != null){
				MeasurePart measurePart = repeatLine.getEndMeasurePart();
				repeatLine.setLocation(NoteCanvas.xStart , measurePart.getY() - 30);
			}
		}
	}
	
	/**
	 * �����޷��������ķ����ͷ�β
	 * @param note
	 */
	public static void locateNoteStemAndTail(AbstractNote note){
		int noteWidth = note.getHighestNote().getWidth()-1;
		//�޷���
		if(note.getBeam() == null){
			Stem stem = note.getStem();
			Tail tail = note.getTail();
			//��ͨ����
			if(note instanceof Note){
				Note nnote = (Note)note;
				//��������
				if(nnote.getPitch() < 4){
					if(stem != null){
						stem.setLocation(nnote.getX() + noteWidth, nnote.getY() + nnote.getHeight()/2 - stem.getHeight() + 0);
					}
				}//��������
				else{
					if(stem != null){
						stem.setLocation(nnote.getX(), nnote.getY() + nnote.getHeight()/2);
					}
				}
			}
			//��������
			else if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				Note snote = cnote.getNote(0);
				int highPitch = cnote.getHighestNote().getPitch();
				int lowPitch = cnote.getLowestNote().getPitch();
				//��������
				if(Math.abs(highPitch-4) < Math.abs(lowPitch-4)){
					if(stem != null){
						stem.setLocation(cnote.getX() + noteWidth, cnote.getLowestNote().getY() + snote.getHeight()/2 - stem.getHeight());
					}
				}//��������
				else{
					if(stem != null){
						int notex = cnote.getWidth() > Note.NORMAL_HEAD_WIDTH ? 
								cnote.getX()+Note.NORMAL_HEAD_WIDTH : cnote.getX();
						stem.setLocation(notex, cnote.getHighestNote().getY() + snote.getHeight()/2 );
					}
				}
			}
			
			//��β
			if(tail != null){
				if(stem.getY() > note.getY() - 1){
					tail.setLocation(stem.getX(), stem.getY()+stem.getHeight() - 2 - stem.getDefaultHeight());
				}
				else{
					tail.setLocation(stem.getX(), stem.getY() - 3 );
				}
			}
		}
	}
	
	public static void locateNoteStemAndTail(AbstractNote note, String upOrDown){
		int noteWidth = note.getHighestNote().getWidth()-1;
		//�޷���
		if(note.getBeam() == null){
			Stem stem = note.getStem();
			Tail tail = note.getTail();
			//��ͨ����
			if(note instanceof Note){
				Note nnote = (Note)note;
				//��������
				if(upOrDown.equalsIgnoreCase("up")){
					if(stem != null){
						stem.setLocation(nnote.getX() + noteWidth, nnote.getY() + nnote.getHeight()/2 - stem.getHeight() + 0);
					}
				}//��������
				else{
					if(stem != null){
						stem.setLocation(nnote.getX(), nnote.getY() + nnote.getHeight()/2);
					}
				}
			}
			//��������
			else if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				Note snote = cnote.getNote(0);
				//��������
				if(upOrDown.equalsIgnoreCase("up")){
					if(stem != null){
						stem.setLocation(cnote.getX() + noteWidth, cnote.getLowestNote().getY() + snote.getHeight()/2 - stem.getHeight());
					}
				}//��������
				else{
					if(stem != null){
						int notex = cnote.getWidth() > Note.NORMAL_HEAD_WIDTH ? 
								cnote.getX()+Note.NORMAL_HEAD_WIDTH : cnote.getX();
						stem.setLocation(notex, cnote.getHighestNote().getY() + snote.getHeight()/2 );
					}
				}
			}
			
			//��β
			if(tail != null){
				if(stem.getY() > note.getY() - 1){
					tail.setLocation(stem.getX(), stem.getY()+stem.getHeight() - 2 - stem.getDefaultHeight());
				}
				else{
					tail.setLocation(stem.getX(), stem.getY() - 3 );
				}
			}
		}
	}
	
	/**
	 * ����С�����ڵı��ע��
	 * @param measurePart
	 */
	public static void locateAnnotations(MeasurePart measurePart){
		for(int i = 0, n = measurePart.getMeasureNum(); i < n; i++){
			Measure measure = measurePart.getMeasure(i);
			//С�ڱ�ע
			if(!measure.getAnnotations().isEmpty()){
				for(int j = 0; j < measure.getAnnotations().size(); j++){
					Annotation an = measure.getAnnotations().get(j);
					if(an.getRelatedObjts().indexOf(measure) == 0)
						an.setLocation(measure.getX() + NoteCanvas.LINE_GAP + an.getDraggedX(), 
								measure.getY() - an.getHeight() - NoteCanvas.LINE_GAP + an.getDraggedY());
				}
			}
			//������ע
			for(int v = 0; v < measure.getVoiceNum(); v++){
				for(int j = 0, jn = measure.getNoteNum(v); j < jn; j++){
					AbstractNote note = measure.getNote(j, v);
					if(note instanceof Note){
						Note nnote = (Note)note;
						for(Annotation an : nnote.getAnnotations()){
							if(an.getRelatedObjts().indexOf(note) == 0)
								an.setLocation(nnote.getX()+nnote.getWidth() + an.getDraggedX(),
										measure.getY() - an.getHeight() - NoteCanvas.LINE_GAP + an.getDraggedY());
						}
					}
					else if(note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						for(int k = 0; k < cnote.getNoteNum(); k++){
							Note nnote = cnote.getNote(k);
							for(Annotation an : nnote.getAnnotations()){
								if(an.getRelatedObjts().indexOf(note) == 0)
									an.setLocation(nnote.getX()+nnote.getWidth() + an.getDraggedX(),
									    measure.getY() - an.getHeight() - NoteCanvas.LINE_GAP + an.getDraggedY());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * �����������׵ı�עע��
	 * !!!!!!(ע�⣬�÷�����ʱ����ʹ�ã���������ı�ע�ķ��÷����ڶ����ڲ�)
	 * @param score
	 */
	public static void locateAnnotations(Score score){
		for(Page page : score.getPageList()){
			for(NoteLine line : page.getNoteLines()){
				for(MeasurePart meaPart : line.getMeaPartList()){
					locateAnnotations(meaPart);
				}
			}
		}
	}
	
	/**
	 * Ϊ�������л�����ʱ���������ϻ�������.
	 * @param notes
	 * @return "up"��"down"����ֵ
	 */
	public static String beamUpOrDown(ArrayList<AbstractNote> notes){
		String result = "up";
		int upNum = 0; 
		int downNum = 0;
		for(int i = 0; i < notes.size(); i++){
			if(notes.get(i) instanceof Note){
				Note nnote = (Note)notes.get(i);
				if(nnote.getPitch() < 4) upNum++;
				else if(nnote.getPitch() > 4) downNum++;
			}
			else if(notes.get(i) instanceof ChordNote){
				ChordNote cnote = (ChordNote)notes.get(i);
				for(int j = 0, n = cnote.getNoteNum(); j < n; j++){
					Note cnnote = cnote.getNote(j);
					if(cnnote.getPitch() < 4) upNum++;
					else if(cnnote.getPitch() > 4) downNum++;
				}
			}
		}
		if(upNum > downNum) result = "up";
		else result = "down";
		return result;
	}
	
	/**
	 * ������ʱ��������б�ķ�ʽ. ����ߡ��Ҹߡ�ˮƽ����ֵ.
	 * @return "flat","left","right"����ֵ
	 */
	public static String highBeamNode(ArrayList<AbstractNote> notes){
		String result = "flat";
		
		if(notes.size() == 2){
			if(notes.get(0).getHighestNote().getPitch() > notes.get(1).getHighestNote().getPitch()){
				result = "left";
			}else if(notes.get(0).getHighestNote().getPitch() < notes.get(1).getHighestNote().getPitch()){
				result = "right";
			}
		}
		else{
			boolean rise = true;
			boolean down = true;
			boolean flat = true;
			for(int i = 1, n = notes.size() - 1 ; i < n; i++){
				int curPitch = notes.get(i).getHighestNote().getPitch();
				int prePitch = notes.get(i-1).getHighestNote().getPitch();
				int nxtPitch = notes.get(i+1).getHighestNote().getPitch();
				if((curPitch > prePitch && curPitch > nxtPitch) ||
						(curPitch < prePitch && curPitch < nxtPitch)){
					result = "flat";
					return result;
				}
				if(curPitch == prePitch && curPitch == nxtPitch && flat){
					result = "flat";
				}else if(curPitch >= prePitch && curPitch <= nxtPitch && rise){
					result = "right";
					down = false;
					flat = false;
				}else if(curPitch <= prePitch && curPitch >= nxtPitch && down){
					result = "left";
					rise = false;
					flat = false;
				}else{
					result = "flat";
					return result;
				}
			}
		}
		
		if(!result.equalsIgnoreCase("flat")){
			int deltay = Math.abs(notes.get(0).getHighestNote().getY() - notes.get(notes.size()-1).getHighestNote().getY());
			int deltax = Math.abs(notes.get(notes.size()-1).getX() - notes.get(0).getX());
			double ratio = (double)deltay / deltax;
			if(ratio < 0.15)
				result = "flat";
		}
		
		return result;
	}
	
	/**
	 * ����С�����������������С���
	 * @param measurePart Ŀ��С����
	 * @return
	 */
	public static int shortestMeaPartWidth(MeasurePart measurePart){
		return shortestMeaPartWidth(measurePart, NoteCanvas.X_MIN_DIST);
	}
	
	/**
	 * �Ը��������������ʱ��������С������
	 * @param measurePart С����
	 * @param dst ָ���ļ��
	 * @return
	 */
	public static int shortestMeaPartWidth(MeasurePart measurePart, int ddist){
		if(measurePart.getMeasure(0).isUnlimited() && measurePart.getMeasure(0).totalNoteNum() == 0)
			return Note.NORMAL_HEAD_WIDTH;
		
		int x = measurePart.getX();
//		int y = measurePart.getY();
		int meaNum = measurePart.getMeasureNum();
		//���һ��������С����֮��ľ���
		int lastDist = ddist + Note.NORMAL_HEAD_WIDTH;
		//����С��ǰһ������
		ArrayList<MeasurePart.NListWithMeaIndex> preNotes = new ArrayList<MeasurePart.NListWithMeaIndex>();
		for(int i = 0; i <meaNum; i++){
			preNotes.add(null);
		}
		
		//�׺ţ����ţ��ĺŵ�x����.
		int clefx = x;
		int keyx = clefx;
		int timex = keyx;
		//������ʼλ��x����.
		int notex = timex;
		for(int i = 0, n = measurePart.getMeasureNum(); i < n; i++){
			Measure measure = measurePart.getMeasure(i);
			if(measure.getUiClef() != null){
				clefx = x + UIClef.CLEF_GAP;
				keyx = clefx + measure.getUiClef().getWidth();
				timex = timex > keyx ? timex : keyx;
				notex = timex ;
			}
			if(measure.getUiKey() != null){
                keyx = keyx + UIKey.KEY_GAP;
                timex = timex > keyx ? timex : keyx + measure.getUiKey().getWidth();
				notex = timex;
			}
			if(measure.getUiTime() != null){
				notex = timex + measure.getUiTime().getWidth();
			}
		}
		
		List<List<MeasurePart.NListWithMeaIndex>> noteStamp = measurePart.getNotesByTimeSlot();  //..........!!!!!!!!
		
		//����ʱ����������ĺ����꣬����ʱ���������һ����ʼֵ����С�����xֵ
		ArrayList<Integer> xlocations = new ArrayList<Integer>();
		xlocations.add(notex);
		//��ǰʱ��۵ĺ�����
		int tempx = notex;
		//����С��ǰһ������������
		int[] prex = new int[meaNum];
		for(int i = 0; i < noteStamp.size(); i++){
			List<MeasurePart.NListWithMeaIndex> tslot = noteStamp.get(i);
			//��ͬһС�ڵ�ǰ�����������ļ�����޶��ĵ�ǰʱ�������������Сxֵ
			int[] shortestDistX = new int[tslot.size()];
			for(int j = 0; j < tslot.size(); j++){
				MeasurePart.NListWithMeaIndex mslot = tslot.get(j);
				int meaIndex = mslot.getMeaIndex();
				int shortDist = MusicMath.shortestDist(preNotes.get(meaIndex), mslot, true);
				shortestDistX[j] = preNotes.get(meaIndex)==null ? 
						notex+shortDist : prex[meaIndex]+shortDist;
				preNotes.set(meaIndex, mslot);
			}
			int maxShortX = MusicMath.maxValue(shortestDistX);
			
			if(i == 0){
				if(maxShortX > xlocations.get(i) + ddist){
					tempx = maxShortX;
				}else{
					tempx = xlocations.get(i) + ddist;
				}
			}
			else{
				if(maxShortX > xlocations.get(i) + Note.NORMAL_HEAD_WIDTH + ddist){
					tempx = maxShortX;
				}else{
					tempx = xlocations.get(i) + Note.NORMAL_HEAD_WIDTH + ddist;
				}
			}
			
			for(int j = 0; j < tslot.size(); j++){
				int meaIndex = tslot.get(j).getMeaIndex();
				prex[meaIndex] = tempx;
			}
			xlocations.add(tempx);
		}
		//ɾ��xlocations�е��׸���ʼֵ
		xlocations.remove(0);
		return tempx + lastDist - x;
	}
	
	/**
	 * �����ܣ�ָ�����ܳ���
	 * @param comp
	 * @param slist
	 * @param upordown ���ܷ���,"up" "down"
	 */
	private static void drawBeam(JComponent comp, List<AbstractNote> slist, String upordown){
		ArrayList<ArrayList<AbstractNote>> commonBeamNotes = MusicMath.pickNoteTeams(slist);
		//����
		for(int i = 0, n = commonBeamNotes.size(); i < n; i++){
			ArrayList<AbstractNote> cbNotes = commonBeamNotes.get(i);
			if(cbNotes.size() > 1){
				Beam beam = new Beam(cbNotes, upordown);
				beam.locate();
				comp.add(beam);
			}
		}
	}
	
	/**
	 * �����ܣ�Ϊָ�����ܳ���
	 * @param comp
	 * @param slist
	 */
	private static void drawBeam(JComponent comp, List<AbstractNote> slist){
		ArrayList<ArrayList<AbstractNote>> commonBeamNotes = MusicMath.pickNoteTeams(slist);
		//����
		for(int i = 0, n = commonBeamNotes.size(); i < n; i++){
			ArrayList<AbstractNote> cbNotes = commonBeamNotes.get(i);
			if(cbNotes.size() > 1){
				Beam beam = new Beam(cbNotes);
				beam.locate();
				comp.add(beam);
			}
		}
	}
	
	/**
	 * �����ˡ�������ָ������
	 * ע�⣺�÷���������drawBeam()֮�����
	 * @param comp
	 * @param slist
	 * @param upordown
	 */
	private static void drawStem(JComponent comp, List<AbstractNote> slist, String upordown){
		if(slist.size() == 0)
			return;
		
		boolean isGrace = false;
		if(slist.get(0) instanceof Grace || slist.get(0) instanceof ChordGrace)
			isGrace = true;
		int stemType = isGrace ? Stem.GRACE : Stem.NORMAL;
		int noteWidth = isGrace ? Gracable.GRACE_WIDTH : Note.NORMAL_HEAD_WIDTH;
		
		//������β
		for(int i = 0, n = slist.size(); i < n; i++){
			AbstractNote note = slist.get(i);
			if(note.getDuration() < 256){
				if(note instanceof Note){
					Note nnote = (Note)note;
					if(nnote.isRest())
						continue;
				}
			
				Stem stem = new Stem(stemType);
				note.setStem(stem);
				comp.add(stem);
				
				if(note instanceof Note && !(note instanceof Grace)){
					Note nnote = (Note)note;
					int pitch = nnote.getPitch();
					if(pitch >= 11 || pitch <= -3){
						int stemHeight = Math.abs(nnote.getPitch() - 4) * NoteCanvas.LINE_GAP / 2;
						stem.setSize(stem.getWidth(), stemHeight);
						stem.repaint();
					}
				}else if(note instanceof ChordNote && !(note instanceof ChordGrace)){
					ChordNote cnote = (ChordNote) note;
					int pitchLow = cnote.getLowestNote().getPitch();
					int pitchHigh = cnote.getHighestNote().getPitch();
					if(pitchLow >= 11){
						stem.setSize(stem.getWidth(),(pitchLow - 4) * NoteCanvas.LINE_GAP / 2);
						stem.repaint();
					}else if(pitchHigh <= -3){
						stem.setSize(stem.getWidth(),(4 - pitchHigh) * NoteCanvas.LINE_GAP / 2);
						stem.repaint();
					}
				}
				//������з���
				if(note.getBeam() != null){
					Beam beam = note.getBeam();
					int noteHeight = isGrace ? Gracable.GRACE_HEIGHT : Note.HEAD_HEIGHT;         //�����߶�
					//��������
					if(beam.getUpOrDown().equalsIgnoreCase("up")){
						double ratio = beam.getRatio();
						ratio = beam.getHighNode().equalsIgnoreCase("flat") ? 0.0 : ratio;
						int listSize = beam.getUiNoteList().size();
						int deltax;
						if(beam.getHighNode().equalsIgnoreCase("left"))
							deltax = note.getX() - beam.getUiNoteList().get(0).getX();
						else
							deltax = beam.getUiNoteList().get(listSize-1).getX() - note.getX();
						int deltay = (int)(deltax * ratio);
						int yy = 0;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							yy = cnote.getLowestNote().getY() + noteHeight/2 - beam.getY() - deltay;
						}else
							yy = note.getY() + noteHeight/2 - beam.getY() - deltay;
						stem.setSize(stem.getWidth(), yy);
						stem.repaint();
						stem.setLocation(note.getX()+noteWidth-1, beam.getY() + deltay );
					}//��������
					else if(beam.getUpOrDown().equalsIgnoreCase("down")){
						double ratio = beam.getRatio();
						ratio = beam.getHighNode().equalsIgnoreCase("flat") ? 0.0 : ratio;
						int listSize = beam.getUiNoteList().size();
						int deltax;
						if(beam.getHighNode().equalsIgnoreCase("left"))
						    deltax = beam.getUiNoteList().get(listSize-1).getX() - note.getX();
						else
							deltax = note.getX() - beam.getUiNoteList().get(0).getX();
						int deltay = (int)(deltax * ratio);
						int yy = 0;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							yy = beam.getY() + beam.getHeight() - noteHeight/2 
							       - cnote.getHighestNote().getY() - deltay;
						}else
							yy = beam.getY() + beam.getHeight() - noteHeight/2 - note.getY() - deltay;
						stem.setSize(stem.getWidth(), yy);
						stem.repaint();
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							int x = cnote.getWidth();
							if(x == cnote.getNote(0).getWidth()){
								stem.setLocation(note.getX(), cnote.getHighestNote().getY() + noteHeight/2);
							}else if(x == 2 * cnote.getNote(0).getWidth()){
								stem.setLocation(note.getX()+note.getWidth()/2, cnote.getHighestNote().getY() + noteHeight/2);
							}
							
						}else
							stem.setLocation(note.getX(), note.getY() + noteHeight/2);
					}
				}//�����з���
				else{
					if(note.getStem() != null && note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						int deltay = cnote.getLowestNote().getY() - cnote.getHighestNote().getY();
						stem.setSize(stem.getWidth(), stem.getHeight() + deltay);
					}
					if(note.getDuration() < 64){
						Tail tail = new Tail(note.getDuration(), upordown, stemType);
						note.setTail(tail);
						comp.add(tail);
					}
					locateNoteStemAndTail(note, upordown);
				}
			}
		}
		if(isGrace){
			boolean hasSlash = slist.get(0) instanceof Grace ? ((Grace)slist.get(0)).isHasSlash() : ((ChordGrace)slist.get(0)).isHasSlash();
			Grace grace = (Grace)slist.get(0).getHighestNote();
			if(hasSlash && slist.get(0).getTremoloBeam() == null){
				TremoloBeam tm = new TremoloBeam("tremoloBeam1", Gracable.GRACE);
				slist.get(0).setTremoloBeam(tm);
				grace.getParent().add(tm);
			}
		}
	}
	
	/**
	 * �����ˡ�����. ��ָ������
	 * ע�⣺�÷��������ڵ���drawBeam()����֮�����.
	 * @param comp
	 * @param slist
	 */
	private static void drawStem(JComponent comp, List<AbstractNote> slist){
		if(slist.size() == 0)
			return;
		
		boolean isGrace = false;
		if(slist.get(0) instanceof Grace || slist.get(0) instanceof ChordGrace)
			isGrace = true;
		int stemType = isGrace ? Stem.GRACE : Stem.NORMAL;
		int noteWidth = isGrace ? Gracable.GRACE_WIDTH : Note.NORMAL_HEAD_WIDTH;
		
		//������β
		for(int i = 0, n = slist.size(); i < n; i++){
			AbstractNote note = slist.get(i);
			if(note.getDuration() < 256){
				if(note instanceof Note){
					Note nnote = (Note)note;
					if(nnote.isRest())
						continue;
				}
			
				Stem stem = new Stem(stemType);
				note.setStem(stem);
				comp.add(stem);
				
				if(note instanceof Note && !(note instanceof Grace)){
					Note nnote = (Note)note;
					int pitch = nnote.getPitch();
					if(pitch >= 11 || pitch <= -3){
						int stemHeight = Math.abs(nnote.getPitch() - 4) * NoteCanvas.LINE_GAP / 2;
						stem.setSize(stem.getWidth(), stemHeight);
						stem.repaint();
					}
				}else if(note instanceof ChordNote && !(note instanceof ChordGrace)){
					ChordNote cnote = (ChordNote) note;
					int pitchLow = cnote.getLowestNote().getPitch();
					int pitchHigh = cnote.getHighestNote().getPitch();
					if(pitchLow >= 11){
						stem.setSize(stem.getWidth(),(pitchLow - 4) * NoteCanvas.LINE_GAP / 2);
						stem.repaint();
					}else if(pitchHigh <= -3){
						stem.setSize(stem.getWidth(),(4 - pitchHigh) * NoteCanvas.LINE_GAP / 2);
						stem.repaint();
					}
				}
				//������з���
				if(note.getBeam() != null){
					Beam beam = note.getBeam();
					int noteHeight = isGrace ? Gracable.GRACE_HEIGHT : Note.HEAD_HEIGHT;         //�����߶�
					//��������
					if(beam.getUpOrDown().equalsIgnoreCase("up")){
						double ratio = beam.getRatio();
						ratio = beam.getHighNode().equalsIgnoreCase("flat") ? 0.0 : ratio;
						int listSize = beam.getUiNoteList().size();
						int deltax;
						if(beam.getHighNode().equalsIgnoreCase("left"))
							deltax = note.getX() - beam.getUiNoteList().get(0).getX();
						else
							deltax = beam.getUiNoteList().get(listSize-1).getX() - note.getX();
						int deltay = (int)(deltax * ratio);
						int yy = 0;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							yy = cnote.getLowestNote().getY() + noteHeight/2 - beam.getY() - deltay;
						}else
							yy = note.getY() + noteHeight/2 - beam.getY() - deltay;
						stem.setSize(stem.getWidth(), yy);
						stem.repaint();
						stem.setLocation(note.getX()+noteWidth-1, beam.getY() + deltay );
					}//��������
					else if(beam.getUpOrDown().equalsIgnoreCase("down")){
						double ratio = beam.getRatio();
						ratio = beam.getHighNode().equalsIgnoreCase("flat") ? 0.0 : ratio;
						int listSize = beam.getUiNoteList().size();
						int deltax;
						if(beam.getHighNode().equalsIgnoreCase("left"))
						    deltax = beam.getUiNoteList().get(listSize-1).getX() - note.getX();
						else
							deltax = note.getX() - beam.getUiNoteList().get(0).getX();
						int deltay = (int)(deltax * ratio);
						int yy = 0;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							yy = beam.getY() + beam.getHeight() - noteHeight/2 
							       - cnote.getHighestNote().getY() - deltay;
						}else
							yy = beam.getY() + beam.getHeight() - noteHeight/2 - note.getY() - deltay;
						stem.setSize(stem.getWidth(), yy);
						stem.repaint();
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							int x = cnote.getWidth();
							if(x == cnote.getNote(0).getWidth()){
								stem.setLocation(note.getX(), cnote.getHighestNote().getY() + noteHeight/2);
							}else if(x == 2 * cnote.getNote(0).getWidth()){
								stem.setLocation(note.getX()+note.getWidth()/2, cnote.getHighestNote().getY() + noteHeight/2);
							}
							
						}else
							stem.setLocation(note.getX(), note.getY() + noteHeight/2);
					}
				}//�����з���
				else{
					if(note.getStem() != null && note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						int deltay = cnote.getLowestNote().getY() - cnote.getHighestNote().getY();
						stem.setSize(stem.getWidth(), stem.getHeight() + deltay);
					}
					if(note.getDuration() < 64){
						String upOrDown;
						if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							int highPitch = cnote.getHighestNote().getPitch();
							int lowPitch = cnote.getLowestNote().getPitch();
							if(Math.abs(highPitch-4) < Math.abs(lowPitch-4))
								upOrDown = "up";
							else 
								upOrDown = "down";
						}else{
							Note nnote = (Note)note;
							upOrDown = nnote.getPitch() >= 4 ? "down" : "up";
						}
						Tail tail = new Tail(note.getDuration(), upOrDown, stemType);
						note.setTail(tail);
						comp.add(tail);
					}
					locateNoteStemAndTail(note);
				}
			}
		}
		if(isGrace){
			boolean hasSlash = slist.get(0) instanceof Grace ? ((Grace)slist.get(0)).isHasSlash() : ((ChordGrace)slist.get(0)).isHasSlash();
			Grace grace = (Grace)slist.get(0).getHighestNote();
			if(hasSlash && slist.get(0).getTremoloBeam() == null){
				TremoloBeam tm = new TremoloBeam("tremoloBeam1", Gracable.GRACE);
				slist.get(0).setTremoloBeam(tm);
				grace.getParent().add(tm);
			}
		}
	}
	
	
	
	/**
	 * Ϊָ�����������л����˺ͷ��ܣ�ָ���˷��ܷ���
	 * @param slist ��������
	 * @param comp ʢ�ŷ��ˡ����ܵ�����
	 * @param upordown ���ܵĳ���,��Чֵ"up","down".
	 */
	public static void drawBeamAndStem(JComponent comp, List<AbstractNote> slist, String upordown){
		drawBeam(comp, slist, upordown);
		drawStem(comp, slist, upordown);
	}
	
	/**
	 * Ϊָ�����������л����˺ͷ��ܣ�Ϊָ�����ܷ���
	 * @param comp
	 * @param slist
	 */
	public static void drawBeamAndStem(JComponent comp, List<AbstractNote> slist){
		drawBeam(comp, slist);
		drawStem(comp, slist);
	}
	
	/**
	 * ɾ�����������и������ķ��ܡ����ˡ�����
	 * @param slist
	 */
	public static void deleteBeamAndStem(List<AbstractNote> slist){
		for(int i = 0, n = slist.size(); i < n; i++){
			AbstractNote note = slist.get(i);
			if(note.getBeam() != null){
				Beam beam = note.getBeam();
				for(int j = 0, jn = beam.getUiNoteList().size(); j < jn; j++){
					AbstractNote nnote = beam.getUiNoteList().get(j);
					nnote.setBeam(null);
				}
				beam.getUiNoteList().clear();
				if(beam.getParent() != null)
					beam.getParent().remove(beam);
			}
			if(note.getStem() != null){
				Stem stem = note.getStem();
				note.setStem(null);
				if(stem.getParent() != null)
					stem.getParent().remove(stem);
			}
			if(note.getTail() != null){
				Tail tail = note.getTail();
				note.setTail(null);
				if(tail.getParent() != null)
					tail.getParent().remove(tail);
			}
		}
	}
	
	/**
	 * Ϊ�˵���Ӽ�����
	 * @param l
	 */
	public static void addMenuListener(JMenu menu, ActionListener l){
		for(int i = 0, n = menu.getMenuComponentCount(); i < n; i++){
			Component comp = menu.getMenuComponent(i);
			if(comp instanceof JMenu){
				addMenuListener(((JMenu)comp), l);
			}
			else if(comp instanceof JMenuItem){
				((JMenuItem)comp).addActionListener(l);
			}
		}
	}
	
	/**
	 * ��һ�������ķ���ת�Ƶ���һ������
	 * ͨ���ǽ���������ת�Ƶ���ͨ���������߷�������
	 * @param cnote
	 * @param onlyNote
	 */
	public static void transferSymbols(AbstractNote cnote, AbstractNote onlyNote){
		
		for(int i = 0; i < cnote.getLyricsNum(); i++){
			onlyNote.addLyrics(cnote.getLyrics(i));
			cnote.getLyrics(i).setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getOrnamentsNum(); i++){
			onlyNote.addOrnament(cnote.getOrnament(i));
			cnote.getOrnament(i).setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getDynamicsNum(); i++){
			onlyNote.addDynamics(cnote.getDynamics(i));
			cnote.getDynamics(i).setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getPerformanceSymbolsNum(); i++){
			onlyNote.addPerformanceSymbols(cnote.getPerformanceSymbols(i));
			cnote.getPerformanceSymbols(i).setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getSymbolLines().size(); i++){
			SymbolLine sl = cnote.getSymbolLines().get(i);
			onlyNote.getSymbolLines().add(sl);
			if(sl.getStartNote() == cnote)
				sl.setStartNote(onlyNote);
			else if(sl.getEndNote() == cnote)
				sl.setEndNote(onlyNote);
		}
		if(cnote.getTremoloBeam() != null){
			onlyNote.setTremoloBeam(cnote.getTremoloBeam());
			cnote.getTremoloBeam().setNote(onlyNote);
		}
		
		if(cnote.getBreath() != null){
			onlyNote.setBreath(cnote.getBreath());
			cnote.getBreath().setNote(onlyNote);
		}
		
		if(cnote.getPedal() != null){
			onlyNote.setPedal(cnote.getPedal());
			cnote.getPedal().setNote(onlyNote);
		}
		
		if(cnote.getTempoText() != null){
			onlyNote.setTempoText(cnote.getTempoText());
			cnote.getTempoText().setNote(onlyNote);
		}
		
		for(int i = 0; i < cnote.getGraceSymbolNum(); i++){
			onlyNote.addGraceSymbol(cnote.getGraceSymbols(i));
		}
		onlyNote.setTremoloBeam(cnote.getTremoloBeam());

		onlyNote.setBreath(cnote.getBreath());
		
		if(cnote.getTuplet() != null){
			Tuplet tuplet = cnote.getTuplet();
			int index = tuplet.getNoteList().indexOf(cnote);
			onlyNote.setTuplet(tuplet);
			tuplet.getNoteList().set(index, onlyNote);
		}
		
		onlyNote.getLeftGraces().clear();
		onlyNote.getRightGraces().clear();
		for(int i = 0; i < cnote.getLeftGraces().size(); i++){
			AbstractNote note = cnote.getLeftGraces().get(i);
			onlyNote.addLeftGrace(note);
		}
		for(int i = 0;i < cnote.getRightGraces().size(); i++){
			AbstractNote note = cnote.getRightGraces().get(i);
			onlyNote.addRightGrace(note);
		}
		cnote.getLeftGraces().clear();
		cnote.getRightGraces().clear();
		
		//ɾ��Դ����������
		cnote.clearLyrics();
		cnote.clearDynamics();
		cnote.clearPerformanceSymbol();
		cnote.clearOrnaments();
		if(cnote instanceof ChordNote)
			((ChordNote)cnote).clearNoteList();
		cnote.getSymbolLines().clear();
		cnote.setTremoloBeam(null);


		cnote.setTempoText(null);
		cnote.setBreath(null);
		cnote.setTuplet(null);
	}
	
	/**
	 * ���нṹ���Ƴ�ĳ��С����
	 * ע�⣺�����ӵ�ǰ�еĽṹ���Ƴ�С���飬��δ��������
	 * @param measurePart ���Ƴ�С����
	 * @return ����Ƴ�С���������һ��Ϊ�գ�����true, ���򷵻�false.
	 */
	public boolean removeMeasurePartFromLine(MeasurePart measurePart){
		NoteLine line = measurePart.getNoteLine();
		line.getMeaPartList().remove(measurePart);
		if(line.getMeaPartList().isEmpty()){
			line.deleteMarkers();
			line.deleteBrackets();
			line.deleteFrontLine();
			Page page = line.getPage();
			page.getNoteLines().remove(line);
			if(page.getNoteLines().isEmpty()){
				page.getScore().getPageList().remove(page);
				page.setScore(null);
				canvas.remove(page);
			}
			//�����������
			for(int i = 0, n = page.getNoteLines().size(); i < n; i++){
				NoteLine tline = page.getNoteLines().get(i);
				tline.determineLocation();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * ��ҳ��ṹ��ɾ��ĳһ��
	 * ע�⣺��δ��������
	 * @param line
	 */
	public void removeNoteLineFromPage(NoteLine line){
		Page page = line.getPage();
		page.getNoteLines().remove(line);
		if(page.getNoteLines().isEmpty()){
			Score score = page.getScore();
			int pageIndex = score.getPageList().indexOf(page);
			score.getPageList().remove(page);
			canvas.remove(page);
			canvas.rearangePages(pageIndex);
		}
	}
	
	/**
	 * �����׽�β������һ��
	 * @param line ��������
	 */
	public void addLine(NoteLine line){
		Score score = canvas.getScore();
		int pageNum = score.getPageList().size();
		Page lastPage = score.getPageList().get(pageNum-1);
		lastPage.getNoteLines().add(line);
		line.setPage(lastPage);
		line.determineLocation();
	}
	
	/**
	 * ��С�����и�С�ڣ��Լ�С���е����������ַ��ŵ�UI������ӵ�һ��ҳ��������.
	 * �����ЩUI����֮ǰ�Ѿ�����һ�������У���ᱻ�Զ�Ų����Ŀ��ҳ������
	 * @param measurePart ��ҪŲ����С����
	 * @param page Ŀ��ҳ��
	 */
	public void addUIEntityToPage(MeasurePart measurePart, Page page) {

		for (int i = 0, n = measurePart.getMeasureNum(); i < n; i++) {
			Measure measure = measurePart.getMeasure(i);
			page.add(measure);
			//�׺ŵ�С�ڷ���
			if(measure.getUiClef() != null){
				page.add(measure.getUiClef());
			}
			if(measure.getUiKey() != null){
				page.add(measure.getUiKey());
			}
			if(measure.getUiTime() != null){
				page.add(measure.getUiTime());
			}
			//С�ڱ��ע��,��ӵ������϶�����ҳ����
			if(!measure.getAnnotations().isEmpty()){
				for(Annotation an : measure.getAnnotations()){
					if(an.getParent() != page)
						page.add(an, JLayeredPane.DRAG_LAYER);
					System.out.println("AN!!!!!!!!");
				}
			}
			for(int v = 0; v < measure.getVoiceNum(); v++){
				for (int j = 0, nj = measure.getNoteNum(v); j < nj; j++) {
					AbstractNote note = measure.getNote(j, v);
					page.add(note);
					
					//���ܣ���������β��
					if(note.getBeam() != null){
						Beam beam = note.getBeam();
						page.add(beam);
					}
					if(note.getStem() != null){
						Stem stem = note.getStem();
						page.add(stem);
					}
					if(note.getTail() != null){
						Tail tail = note.getTail();
						page.add(tail);
					}
					//����
					if(note.getDotNum() != 0){
						if(note instanceof Note){
							UIDot dot = ((Note)note).getUiDot();
							page.add(dot);
						}else if(note instanceof ChordNote){
							ChordNote cnote = (ChordNote)note;
							for(int k = 0; k < cnote.getNoteNum(); k++){
								UIDot dot = cnote.getNote(k).getUiDot();
								page.add(dot);
							}
						}
					}
					//������
					if(note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						for(int k = 0, nk = cnote.getNoteNum(); k < nk; k++){
							Note nnote = cnote.getNote(k);
							if(nnote.getSharpOrFlat() != null){
								page.add(nnote.getSharpOrFlat());
							}
						}
					}else if(note instanceof Note){
						Note nnote = (Note)note;
						if(nnote.getSharpOrFlat() != null){
							page.add(nnote.getSharpOrFlat());
						}
					}
					//������
					if(note instanceof Note && ((Note)note).getTieNum() != 0){
						for(int k = 0; k < ((Note)note).getTieNum(); k++){
							Tie tie = ((Note)note).getTie(k);
							page.add(tie);
						}
					}else if(note instanceof ChordNote){
						for(int k = 0; k < ((ChordNote)note).getNoteNum(); k++){
							Note nnote = ((ChordNote)note).getNote(k);
							if(nnote.getTieNum() != 0){
								for(int m = 0; m < nnote.getTieNum(); m++){
									page.add(nnote.getTie(m));
								}
							}
						}
					}
					//������
					if(note.getTuplet() != null && note.getTuplet().getParent() != page){
						page.add(note.getTuplet());
					}
					//������������
					for(int k = 0; k < note.getSymbolLines().size(); k++){
						SymbolLine symbolLine = note.getSymbolLines().get(k);
						page.add(symbolLine);
					}
					//��������
					for(int k = 0, kn = note.getOrnamentsNum(); k < kn; k++){
						NoteSymbol nsl = note.getOrnament(k);
						page.add(nsl);
					}
					if( note.getPedal() != null){
						NoteSymbol nsl = note.getPedal();
						page.add(nsl);
					}
					for(int k = 0, kn = note.getDynamicsNum(); k < kn; k++){
						NoteSymbol nsl = note.getDynamics(k);
						page.add(nsl);
					}
					for(int k = 0, kn = note.getPerformanceSymbolsNum(); k < kn; k++){
						NoteSymbol nsl = note.getPerformanceSymbols(k);
						page.add(nsl);
					}
					for(int k = 0, kn = note.getGraceSymbolNum(); k < kn; k++){
						NoteSymbol nsl = note.getGraceSymbols(k);
						page.add(nsl);
					}
					for(int k = 0, kn = note.getLeftGraces().size(); k < kn; k++){
						AbstractNote grace = note.getLeftGraces().get(k);
						page.add(grace);
					}
					for(int k = 0, kn = note.getRightGraces().size(); k < kn; k++){
						AbstractNote grace = note.getRightGraces().get(k);
						page.add(grace);
					}
					if(note.getTremoloBeam() != null)
						page.add(note.getTremoloBeam());
					if(note.getTempoText() != null)
						page.add(note.getTempoText());
					if(note.getBreath() != null)
						page.add(note.getBreath());
					
					//���
					for(int k = 0, kn = note.getLyricsNum(); k < kn; k++){
						Lyrics lyric = note.getLyrics(k);
						if(lyric != null)
							page.add(lyric);
					}
					//�������ע��
					if(note instanceof Note){
						Note nnote = (Note)note;
						if(!nnote.getAnnotations().isEmpty()){
							for(Annotation an : nnote.getAnnotations()){
								if(an.getParent() != page)
									page.add(an);
							}
						}
					}
					else if(note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						for(int k = 0, kn = cnote.getNoteNum(); k < kn; k++){
							Note nnote = cnote.getNote(k);
							if(!nnote.getAnnotations().isEmpty()){
								for(Annotation an : nnote.getAnnotations()){
									if(an.getParent() != page)
										page.add(an);
								}
							}
						}
					}
				}
			}
		}
		//С����
		page.add(measurePart.getBarline());
		//С���鷴���Ǻ�
		for(int i = 0, n = measurePart.getRepeatSymbol().size(); i < n; i++){
			NoteSymbol nsl = measurePart.getRepeatSymbol().get(i);
			page.add(nsl);
		}
		//С���ظ������Ǻţ����ӼǺŵȣ�
		for(int i = 0; i < measurePart.getRepeatLines().size(); i++){
			RepeatLine repeatLine = measurePart.getRepeatLines().get(i);
			page.add(repeatLine);
		}
	}
	
	/**
	 * ��ȡС��������,�Ӷ���������.
	 * �÷��������˳���������ҳ�Ļ��֣�����������������. ҳ�����UI����ʵ�壬��δ���������벼��.
	 * @param list
	 * @return
	 */
	public Score makeScoreByMeasureParts(ArrayList<MeasurePart> list, List<FreeAddedText> tlist){
		Score score = new Score();
		Page page = new Page();
		page.addMouseListener(canvas);
		
		score.getPageList().add(page);
		page.setScore(score);
		NoteLine line = new NoteLine();
		page.getNoteLines().add(line);
		line.setPage(page);
		//����ʱ��۵ĸ���
		int slotNum = 0;
		for(int i = 0, n = list.size(); i < n; i++){
			MeasurePart measurePart = list.get(i);
			
	        slotNum += measurePart.getNotesByTimeSlot().size();
	        //һ����С����������ܳ���5��, ʱ��۲�����36��.
			if(slotNum >= 36 || line.getMeaPartList().size() > 5){
				line = new NoteLine();
				line.getMeaPartList().add(measurePart);
				measurePart.setNoteLine(line);
				measurePart.getBarline().addMouseListener(canvas);
				measurePart.getBarline().addMouseMotionListener(canvas);
				
				if(page.getBlankHeight() <= line.getLineGap()*2 + line.getHeight()){
					page = new Page();
					page.addMouseListener(canvas);
					score.getPageList().add(page);
					page.setScore(score);
				}
				page.getNoteLines().add(line);
				line.setPage(page);
				slotNum = measurePart.getNotesByTimeSlot().size();
			}
			
			else{
				line.getMeaPartList().add(measurePart);
				measurePart.setNoteLine(line);
				measurePart.getBarline().addMouseListener(canvas);
				measurePart.getBarline().addMouseMotionListener(canvas);
			}
			
			//���UI����ʵ��
			addUIEntityToPage(measurePart, page);
		}
		
		//����ҳ���С
//		canvas.adjustSize(score);
		
		//Ϊ�������ɸ������ţ�ǰ�ߣ����ţ�ˮƽ��ʶ����
		for(int i = 0; i < score.getPageList().size(); i++){
			Page p = score.getPageList().get(i);
			for(int j = 0; j < p.getNoteLines().size(); j++){
				NoteLine lline = p.getNoteLines().get(j);
				lline.generateMarkers();
				lline.addMarkerListener(canvas);
				lline.generateBrackets();
				lline.generateFrontBarlineLine();
			}
		}
		
		//��������ı�
		for(int i = 0; i < tlist.size(); i++){
			FreeAddedText txt = tlist.get(i);
			txt.addMouseListener(canvas);
			score.addFreeText(txt);
		}
		return score;
	}
	
	/**
	 * ����ĳ�и�С�ڵ����������׺ţ����ţ��ĺŵ�UIʵ��.
	 * �÷���ͨ����redrawLine()�����е���.
	 * @param line ��
	 */
	public void genMeaAttrInLine(NoteLine line){
		int scoreType = canvas.getScore().getScoreType();
		
		Page page = line.getPage();
		int meaNum = line.getMeaPartList().get(0).getMeasureNum();
		
		//ǰһ��С�������
		Time preTime = null;
		//ǰһ��С�����С���׺�
		ArrayList<String> preClefType = new ArrayList<String>();
		for(int i = 0; i < meaNum; i++){
			preClefType.add(null);
		}
		//ǰһ��С�����С�ڵ���
		ArrayList<Integer> preKeyValue = new ArrayList<Integer>();
		for(int i = 0; i < meaNum; i++){
			preKeyValue.add(null);
		}
		for(int i = 0, n = line.getMeaPartList().size(); i < n; i++){
			MeasurePart measurePart = line.getMeaPartList().get(i);
			for(int j = 0, jn = measurePart.getMeasureNum(); j < jn; j++){
				Measure measure = measurePart.getMeasure(j);
				
				String clefType = measure.getClefType();
				Time time = measure.getTime();
				int keyValue = measure.getKeyValue();
				
				/*
				 * �׺�
				 */
				if(i == 0 || !clefType.equalsIgnoreCase(preClefType.get(j))){
					if(measure.getUiClef() == null){
						UIClef uiClef = new UIClef(clefType);
						page.add(uiClef);
						measure.setUiClef(uiClef);
					}else{
						measure.getUiClef().setClefType(clefType);
						measure.getUiClef().adjustSize();
						measure.getUiClef().repaint();
					}
				}
				//ɾ�������е��׺�
				else if(measure.getUiClef() != null){
					UIClef uclef = measure.getUiClef();
					page.remove(uclef);
					measure.setUiClef(null);
				}
				
				/*
				 * �ĺţ��ĺ�ֻ������������ͨ����ʱ���жϣ���Ϊ�����ƽ������Ͳ������׺Ÿ���
				 */
				if(scoreType == Score.SCORE_NORMAL){
					if(i == 0){
						if(MusicMath.preMeasurePart(canvas.getScore(), measurePart) != null){
	                        MeasurePart prePart = MusicMath.preMeasurePart(canvas.getScore(), measurePart);
	                        //ǰһ���ĺ��뵱ǰ�����
	                        if(!prePart.getMeasure(j).getTime().equals(measure.getTime())){
	                        	if(measure.getUiTime() == null){
	                        		UITime uiTime = new UITime(time);
	        						page.add(uiTime);
	        						measure.setUiTime(uiTime);
	                        	}else{
	                        		measure.getUiTime().setBeats(time.getBeats());
	                        		measure.getUiTime().setBeatType(time.getBeatType());
	                        		measure.getUiTime().repaint();
	                        	}
	                        }
	                        else{
	                        	//�Ѳ����е��ĺ�ɾ��
	                        	if(measure.getUiTime() != null){
	                        		UITime utime = measure.getUiTime();
	                        		page.remove(utime);
	                        		measure.setUiTime(null);
	                        	}
	                        }
						}
						//�����׵�һ��С����
						else{
							if(measure.getUiTime() == null){
	                    		UITime uiTime = new UITime(time);
	    						page.add(uiTime);
	    						measure.setUiTime(uiTime);
	                    	}else{
	                    		UITime uiTime = measure.getUiTime();
	                    		page.remove(uiTime);
	                    		measure.setUiTime(null);
	                    		UITime utime = new UITime(time);
	    						page.add(utime);
	    						measure.setUiTime(utime);
//	                    		measure.getUiTime().setBeat(time.getBeat());                 
//	                    		measure.getUiTime().setBeatType(time.getBeatType());
//	                    		measure.getUiTime().repaint();
	                    	}
						}
					}
					else{
						if(!time.equals(preTime)){
							if(measure.getUiTime() == null){
								UITime uiTime = new UITime(time);
								page.add(uiTime);
								measure.setUiTime(uiTime);
							}
							else{
								UITime utime = measure.getUiTime();
								utime.setBeats(time.getBeats());
								utime.setBeatType(time.getBeatType());
								utime.repaint();
							}
						}
						//ɾ�������е��ĺ�
						else if(measure.getUiTime() != null){
							UITime utime = measure.getUiTime();
	                		page.remove(utime);
	                		measure.setUiTime(null);
						}
					}
				}
				
				/*
				 * ����
				 */
				if(i == 0){
					MeasurePart prePart = MusicMath.preMeasurePart(canvas.getScore(), measurePart);
					//���׵�һ��С����
					if(prePart == null){
						//ɾ�������еĵ���
						if(keyValue == 0 && measure.getUiKey() != null){
							UIKey ukey = measure.getUiKey();
							page.remove(ukey);
							measure.setUiKey(null);
						}
						else if(keyValue != 0){
							if(measure.getUiKey() != null){
								UIKey ukey = measure.getUiKey();
								ukey.setKeyValue(keyValue);
								ukey.adjustSize();
								ukey.repaint();
							}
							else{
								UIKey ukey = new UIKey(keyValue, clefType);
								page.add(ukey);
								measure.setUiKey(ukey);
							}
						}
					}
					//����һ�����һ�������
					else if(prePart.getMeasure(j).getKeyValue() != keyValue){
						UIKey newKey = measure.getUiKey() == null ? 
								new UIKey(1, clefType) : measure.getUiKey();
						if(keyValue == 0){
							newKey.setKeyValue(prePart.getMeasure(j).getKeyValue());
							newKey.setRestoreNatural(true);
							newKey.adjustSize();
							newKey.repaint();
						}
						else{
							newKey.setKeyValue(keyValue);
							newKey.setRestoreNatural(false);
							newKey.adjustSize();
							newKey.repaint();
						}
						if(measure.getUiKey() == null){
							page.add(newKey);
							measure.setUiKey(newKey);
						}
					}
					//����һ�����һ�����
					else if(prePart.getMeasure(j).getKeyValue() == keyValue){
						if(keyValue != 0){
							if(measure.getUiKey() != null){
								measure.getUiKey().setKeyValue(keyValue);
								measure.getUiKey().setRestoreNatural(false);
								measure.getUiKey().adjustSize();
								measure.getUiKey().repaint();
							}else{
								UIKey ukey = new UIKey(keyValue, clefType);
								page.add(ukey);
								measure.setUiKey(ukey);
							}
						}
						//ɾ�������еĵ���
						else if (keyValue == 0 && measure.getUiKey() != null) {
							UIKey ukey = measure.getUiKey();
							page.remove(ukey);
							measure.setUiKey(null);
						}
					}
					//ɾ����Ӧ���еĵ���
					else if(measure.getUiKey() != null){
						UIKey ukey = measure.getUiKey();
						page.remove(ukey);
						measure.setUiKey(null);
					}
				}
				else{
					if(keyValue != preKeyValue.get(j)){
						UIKey newKey = measure.getUiKey() == null ? 
								new UIKey(1, clefType) : measure.getUiKey();
						if(keyValue == 0){
							newKey.setKeyValue(preKeyValue.get(j));
							newKey.setRestoreNatural(true);
							newKey.adjustSize();
							newKey.repaint();
						}
						else{
							newKey.setKeyValue(keyValue);
							newKey.setRestoreNatural(false);
							newKey.adjustSize();
							newKey.repaint();
						}
						if(measure.getUiKey() == null){
							page.add(newKey);
							measure.setUiKey(newKey);
						}
					}
					//ɾ����Ӧ���еĵ���
					else if(measure.getUiKey() != null){
						UIKey ukey = measure.getUiKey();
						page.remove(ukey);
						measure.setUiKey(null);
					}
				}
				
				//���±���
				preClefType.set(j, clefType);
				if(j == jn - 1) 
					preTime = time;
				preKeyValue.set(j, keyValue);
			}
		}
		page.revalidate();
		page.updateUI();
	}
	
	/**
	 * �������ڱ༭��ҳ���
	 * @param x �����veil�ϵ�x���꣬Ҳ�������ȫ��x����
	 * @param y �����veil�ϵ�Y���꣬Ҳ�������ȫ��y����
	 * @return
	 */
	public int getEditingPageIndex(int x, int y){
		int pageNum = y / (Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP);
		return pageNum;
	}
	
	/**
	 * �������ڱ༭�������
	 * @param x ҳ�ھֲ�x����
	 * @param y ҳ�ھֲ�y����
	 * @return
	 */
	public int getEditingLineIndex(Page page, int x, int y){
		int lineNum = page.getNoteLines().size();
		NoteLine firstLine = page.getNoteLines().get(0);
		NoteLine lastLine = page.getNoteLines().get(lineNum-1);
		if(y <= firstLine.getY())
			return 0;
		if(y >= lastLine.getY())
			return lineNum-1;
		//�ӵ�һ�п�ʼһֱ�������ڶ���
		for(int i = 0, n =lineNum - 1; i < n; i++){
			NoteLine curLine = page.getNoteLines().get(i);
			NoteLine nxtLine = page.getNoteLines().get(i+1);
			if(curLine.getY() <= y && nxtLine.getY() >= y){
				int stdy = curLine.getY() + curLine.getHeight() + curLine.getLineGap()/2;
				if(y < stdy)
					return i;
				else 
					return i+1;
			}
		}
		return -1;
	}
	
	/**
	 * �������ڱ༭��С�������
	 * @param x ҳ�ھֲ�x����
	 * @param y ҳ�ھֲ�y����
	 * @return
	 */
	public int getEditingMeaPartIndex(NoteLine line, int x, int yy){
		int meaPartNum = line.getMeaPartList().size();
		MeasurePart firstMeaPart = line.getMeaPartList().get(0);
		MeasurePart lastMeaPart = line.getMeaPartList().get(meaPartNum-1);
		if(x <= firstMeaPart.getX())
			return 0;
		if(x >= lastMeaPart.getX())
			return meaPartNum-1;
		//�ӵ�һ��С����һֱ�������ڶ���
		for(int i = 0, n = meaPartNum-1; i < n; i++){
			MeasurePart curPart = line.getMeaPartList().get(i);
			MeasurePart nxtPart = line.getMeaPartList().get(i+1);
			if(x >= curPart.getX() && x <= nxtPart.getX())
				return i;
		}
		return -1;
	}
	
	/**
	 * �������ڱ༭��С�����
	 * @param measurePart С����
	 * @param x ҳ�ھֲ�x����
	 * @param y ҳ�ھֲ�y����
	 * @return
	 */
	public int getEditingMeasureIndex(MeasurePart measurePart, int x, int y){
		NoteLine line = measurePart.getNoteLine();
		int meaNum = measurePart.getMeasureNum();
		Measure firstMeasure = measurePart.getMeasure(0);
		Measure lastMeasure = measurePart.getMeasure(meaNum-1);
		if(y <= firstMeasure.getY())
			return 0;
		if(y >= lastMeasure.getY())
			return meaNum-1;
		//�ӵ�һ��С��һֱ�������ڶ���
		for(int i = 0, n = meaNum-1; i < n; i++){
			Measure curMeasure = measurePart.getMeasure(i);
			Measure nxtMeasure = measurePart.getMeasure(i+1);
			if(y >= curMeasure.getY() && y <= nxtMeasure.getY()){
				int stdy = curMeasure.getY() + curMeasure.getHeight() + line.getMeasureGaps().get(i)/2;
				if(y < stdy)
					return i;
				else 
					return i+1;
			}
		}
		return -1;
	}
	
	/**
	 * ����С�������ڱ༭���������
	 * 
	 * ���С��������С�ڣ��򱻱༭����һ����С���ڵ�ĳ������������ֹ������ʱ���ر��༭���������
	 * ���С���������ƽ���С�ڣ��򱻱༭���������ǿյģ�����С��ĩβ�����������ʱ���صĽ��Ϊ���һ���������+1.
	 * @param measure С��
	 * @param x ҳ�ھֲ�x����
	 * @param y ҳ�ھֲ�y����
	 * @return
	 */
	public int getEditingNoteIndex(Measure measure, int x, int y, int voice){
		int noteNum = measure.getNoteNum(voice);
		if(noteNum == 0){
			return 0;
		}
		AbstractNote firstNote = measure.getNote(0, voice);
		AbstractNote lastNote = measure.getNote(noteNum-1, voice);
		if(x <= firstNote.getX())
			return 0;
		if(x >= lastNote.getX()){
			//����С��
			if(!measure.isUnlimited())
				return noteNum-1;
			//�����ƽ���С��
			else{
				if(x <= lastNote.getX() + lastNote.getWidth() + 2*NoteCanvas.LINE_GAP)
					return noteNum-1;
				else
					return noteNum;
			}
		}	
		for(int i = 0, n = noteNum-1; i < n; i++){
			AbstractNote curNote = measure.getNote(i, voice);
			AbstractNote nxtNote = measure.getNote(i+1, voice);
			if(x >= curNote.getX() && x <= nxtNote.getX()){
				int stdx = (curNote.getX() + nxtNote.getX()) / 2 + curNote.getWidth() / 2;
				if(x <= stdx)
					return i;
				else 
					return i+1;
			}
		}
		return -1;
	}
	
	/**
	 * �������ڱ༭������
	 * @param x �����veil�ϵ�x���꣬Ҳ�������ȫ��x����
	 * @param y �����veil�ϵ�Y���꣬Ҳ�������ȫ��y����
	 * @return
	 */
	public AbstractNote getEditingNote(int x, int y, int voice){
		int pageIndex = getEditingPageIndex(x, y);
		Page page = canvas.getScore().getPageList().get(pageIndex);
		//�ֲ�y����
		int yy = y - (Page.PAGE_HEIGHT + NoteCanvas.PAGE_GAP) * pageIndex;
		int lineIndex = getEditingLineIndex(page, x, yy);
		NoteLine line = page.getNoteLines().get(lineIndex);
		int meaPartIndex = getEditingMeaPartIndex(line, x, yy);
		MeasurePart meaPart = line.getMeaPartList().get(meaPartIndex);
		int meaIndex = getEditingMeasureIndex(meaPart, x, yy);
		Measure measure = meaPart.getMeasure(meaIndex);
		int noteIndex = getEditingNoteIndex(measure, x, yy, voice);
		if(noteIndex >= measure.getNoteNum(voice))
			return null;
		AbstractNote note = measure.getNote(noteIndex, voice);
		return note;
	}
	
	/**
	 * ��ָ��С�ڵ�ָ�����������һ���µ�����,���������߼�����ֲ�ı仯
	 * @param measure ָ��С��
	 * @param noteIndex ָ���������
	 * @param newNote �µ�����
	 * 
	 * �������༭�ɹ�ʱ����true, ���򷵻�false
	 * 
	 */
	
	public boolean changeMeasureRythm(Measure measure, int noteIndex, Note newNote, int voice){
		Page page = (Page)measure.getParent();
		
		/*
		 * ������С��
		 */
		if(measure.isUnlimited()){
			if(noteIndex >= measure.getNoteNum(voice)){
				measure.addNote(newNote, voice);
				page.add(newNote);
				newNote.addMouseListener(canvas);
				newNote.addMouseMotionListener(canvas);
				return true;
			}
			else{
				AbstractNote note = measure.getNote(noteIndex, voice);
				//��ǰ������ʱ�����ڵ��ڴ��������
				if(note.getDurationWithDot() >= newNote.getDurationWithDot()){
					handleShortNote(measure, noteIndex, newNote, voice);
					return true;
				}
				//��ǰ����ʱ���϶̣���û��������
				else if(note.getTuplet() == null){
					measure.removeNote(note);
					page.deleteNote(note, false);
					note.removeAllSymbols(false);
					measure.addNote(noteIndex, newNote, voice);
					page.add(newNote);
					if(newNote.getUiDot() != null)
						page.add(newNote.getUiDot());
					newNote.addMouseListener(canvas);
					newNote.addMouseMotionListener(canvas);
					return true;
				}
			}
		}
		
		/*
		 * ��ͨС�ڣ�Ϊ�ڶ�������ӵ�һ������
		 */
		if(noteIndex == 0 && noteIndex >= measure.getNoteNum(voice) && voice > 0){
			if(newNote.getDurationWithDot() > MusicMath.getMeasureDuration(measure))
				return false;
			ChangeRythmAction action = new ChangeRythmAction(measure, voice, this);  
			actionController.pushAction(action);    //����ѹջ
			measure.addNote(noteIndex, newNote, voice);
			page.add(newNote);
			action.addCurNote(newNote);
			newNote.addMouseListener(canvas);
			newNote.addMouseMotionListener(canvas);
			
			int rest = MusicMath.getMeasureDuration(measure) - newNote.getDurationWithDot();
			ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
			int n = splited.size();
			for(int i = 0; i < splited.size(); i++){
				Note note = splited.get(n-1-i);
				measure.addNote(i + 1, note, voice);
				page.add(note);
				action.addCurNote(note);
				note.addMouseListener(canvas);
				note.addMouseMotionListener(canvas);
			}
			//�����ɵ�����Ĭ��Ϊѡ��״̬
			canvas.cancleAllSelected();
			newNote.beSelected();
			canvas.getSelectedObjects().add(newNote);
			return true;
		}
		
		AbstractNote curNote = measure.getNote(noteIndex, voice);
		//��ǰ������ʱ�����ڵ��ڴ��������
		if(curNote.getDurationWithDot() >= newNote.getDurationWithDot()){
			handleShortNote(measure, noteIndex, newNote, voice);
			return true;
		}
		else{
			//��ǰ����������������
			if(curNote.getTuplet() == null){
				//���õ�ʣ��ʱ��
				int totalRestDur = 0;
				float tempDur = 0.0f;
				for(int i = noteIndex; i < measure.getNoteNum(voice); i++){
					AbstractNote note = measure.getNote(i, voice);
					tempDur += note.getRealDuration();
				}
				totalRestDur = Math.round(tempDur);
				
				//ʣ��ʱ���㹻
				if(totalRestDur >= newNote.getDurationWithDot()){
					ChangeRythmAction action = new ChangeRythmAction(measure, voice, this);  
					actionController.pushAction(action);    //����ѹջ
					action.addPreNote(curNote);
					int curDur = curNote.getDurationWithDot();
					measure.removeNote(curNote);
					page.deleteNote(curNote, false);
					//curNote.removeAllSymbols(false);
					//�ϲ���������ֱ���㹻ʢ��������
					while(curDur < newNote.getDurationWithDot()){
						AbstractNote nxtNote = measure.getNote(noteIndex, voice);
						if(nxtNote.getTuplet() != null){
							curDur += nxtNote.getTuplet().getRealDuration();
							measure.remove(nxtNote);
							page.deleteNote(nxtNote, true);
							//nxtNote.removeAllSymbols(true);
						}
						else{
							measure.removeNote(nxtNote);
							page.deleteNote(nxtNote, false);
							//nxtNote.removeAllSymbols(false);
							curDur += nxtNote.getDurationWithDot();
						}
						action.addPreNote(nxtNote);
					}
					//���ϲ���Ķ����������з�.
					int rest = curDur - newNote.getDurationWithDot();
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
					//�����������зֺ�����������С��
					measure.addNote(noteIndex, newNote, voice);
					page.add(newNote);
					if(newNote.getUiDot() != null)
						page.add(newNote.getUiDot());
					newNote.addMouseListener(canvas);
					newNote.addMouseMotionListener(canvas);
					action.addCurNote(newNote); 
					int n = splited.size();
					for(int i = 0; i < splited.size(); i++){
						Note note = splited.get(n-1-i);
						measure.addNote(noteIndex + i + 1, note, voice);
						page.add(note);
						note.addMouseListener(canvas);
						note.addMouseMotionListener(canvas);
						action.addCurNote(note);
					}
					
					//�����ɵ�����Ĭ��Ϊѡ��״̬
					canvas.cancleAllSelected();
					newNote.beSelected();
					canvas.getSelectedObjects().add(newNote);
					
					return true;
				}
				else 
					return false;
			}
			
			//��ǰ��������������
			else{
				Tuplet tup = curNote.getTuplet();
				int index = tup.getNoteList().indexOf(curNote);
				//���������ڲ���ʣ��ʱ��
				int restDur = 0;
				for(int i = index; i < tup.getNoteList().size(); i++){
					restDur += tup.getNoteList().get(i).getDurationWithDot();
				}
				//ʱ���㹻
				if(restDur >= newNote.getDurationWithDot()){
					int curDur = curNote.getDurationWithDot();
					measure.removeNote(curNote);
					page.deleteNote(curNote, false);
					//curNote.removeAllSymbols(false);
					tup.getNoteList().remove(curNote);
					//�ϲ���������ֱ���㹻ʢ��������
					while(curDur < newNote.getDurationWithDot()){
						AbstractNote nxtNote = measure.getNote(noteIndex, voice);
						measure.removeNote(nxtNote);
						page.deleteNote(nxtNote, false);
						//nxtNote.removeAllSymbols(false);
						tup.getNoteList().remove(nxtNote);
						curDur += nxtNote.getDurationWithDot();
					}
					//���ϲ���Ķ����������з�.
					int rest = curDur - newNote.getDurationWithDot();
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
					//�����������зֺ�����������С��
					measure.addNote(noteIndex, newNote, voice);
					page.add(newNote);
					tup.getNoteList().add(index, newNote);
					newNote.setTuplet(tup);
					if(newNote.getUiDot() != null)
						page.add(newNote.getUiDot());
					newNote.addMouseListener(canvas);
					newNote.addMouseMotionListener(canvas);
					int n = splited.size();
					for(int i = 0; i < splited.size(); i++){
						Note note = splited.get(n-1-i);
						measure.addNote(noteIndex + i + 1, note, voice);
						page.add(note);
						tup.getNoteList().add(index + 1 + i, note);
						note.setTuplet(tup);
						note.addMouseListener(canvas);
						note.addMouseMotionListener(canvas);
					}
					
					//�����ɵ�����Ĭ��Ϊѡ��״̬
					canvas.cancleAllSelected();
					newNote.beSelected();
					canvas.getSelectedObjects().add(newNote);
					
					return true;
				}
				else 
					return false;
			}
		}
	}
	
	/**
	 * ����༭���༭����������ֹ��ʱ�����ڵ��ڴ��������
	 * @param measure
	 * @param noteIndex
	 * @param newNote
	 */
	public void handleShortNote(Measure measure, int noteIndex, Note newNote, int voice){
		
		AbstractNote curNote = measure.getNote(noteIndex, voice);
		newNote.addMouseListener(canvas);
		newNote.addMouseMotionListener(canvas);
		//�Ƿ����������
		boolean hasTup = curNote.getTuplet() == null ? false : true;
		
		//��ǰ��������ֹ��������������ʱ�����
		if(curNote.getDurationWithDot() == newNote.getDurationWithDot() && 
				curNote instanceof Note && ((Note)curNote).isRest()){
			ChangeRythmAction action = new ChangeRythmAction(measure, voice, this);  
			actionController.pushAction(action);    //����ѹջ
			
			measure.removeNote(curNote);
			Page page = (Page)measure.getParent();
			page.deleteNote(curNote, false);
			//curNote.removeAllSymbols(false);
			action.addPreNote(curNote);
			measure.addNote(noteIndex, newNote, voice);
			page.add(newNote);
			action.addCurNote(newNote);
			if(newNote.getUiDot() != null)
				page.add(newNote.getUiDot());
			//�����������
			if(curNote.getTuplet() != null){
				Tuplet tup = curNote.getTuplet();
				int index = tup.getNoteList().indexOf(curNote);
				tup.getNoteList().remove(curNote);
				tup.getNoteList().add(index, newNote);
				newNote.setTuplet(tup);
			}
		}
		
		//��ǰ����ʱ��������������
		else if(curNote.getDurationWithDot() > newNote.getDurationWithDot()){
			ChangeRythmAction action = new ChangeRythmAction(measure, voice, this);  
			actionController.pushAction(action);    //����ѹջ
			
			//�зֶ����ʱ��
			int rest = curNote.getDurationWithDot() - newNote.getDurationWithDot();
			ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
			//ɾ��ԭ������
			Page page = (Page)measure.getParent();
			measure.removeNote(curNote);
			page.deleteNote(curNote, false);
			//curNote.removeAllSymbols(false);
			action.addPreNote(curNote);
			//���������
			measure.addNote(noteIndex, newNote, voice);
			page.add(newNote);
			action.addCurNote(newNote);
			if(newNote.getUiDot() != null)
				page.add(newNote.getUiDot());
			//������
			if(hasTup){
				Tuplet tup = curNote.getTuplet();
				int index = tup.getNoteList().indexOf(curNote);
				tup.getNoteList().remove(curNote);
				tup.getNoteList().add(index, newNote);
				newNote.setTuplet(tup);
			}
			int n = splited.size();
			for(int i = 0; i < splited.size(); i++){
				measure.addNote(noteIndex + i + 1, splited.get(n-1-i), voice);
				page.add(splited.get(i));
				if(hasTup){
					Tuplet tup = newNote.getTuplet();
					int index = tup.getNoteList().indexOf(newNote);
					tup.getNoteList().add(index + i + 1, splited.get(n-1-i));
					splited.get(n-1-i).setTuplet(tup);
				}
				splited.get(n-1-i).addMouseListener(canvas);
				splited.get(n-1-i).addMouseMotionListener(canvas);
				action.addCurNote(splited.get(n-1-i));
			}
		}
		
		//��ǰ��������ֹ��������������ʱ�����
		else if((curNote instanceof ChordNote || (curNote instanceof Note && !((Note)curNote).isRest())) 
				&& curNote.getDurationWithDot() == newNote.getDurationWithDot()){
			
			//�����������غϣ��򷵻�
			if(curNote instanceof ChordNote && ((ChordNote)curNote).getNoteWithPitch(newNote.getPitch()) != null)
				return;
			else if(curNote instanceof Note && ((Note)curNote).getPitch() == newNote.getPitch())
				return;
			
			//û���غϣ��򹹳ɺ���
			AddChordAction action = new AddChordAction(this);
			actionController.pushAction(action);
			action.setPreNote(curNote);
			action.setNote(newNote);
			ChordNote cnote = addChord(curNote, newNote, noteIndex, voice);
			action.setCurNote(cnote);
	    }
		//�����ɵ�����Ĭ��Ϊѡ��״̬
		canvas.cancleAllSelected();
		newNote.beSelected();
		canvas.getSelectedObjects().add(newNote);
		NoteLine line = measure.getMeasurePart().getNoteLine();
		canvas.redrawLine(line);
	}
	
	/**
	 * ��Ӻ���
	 * @param curNote
	 * @param newNote
	 */
	public ChordNote addChord(AbstractNote curNote, Note newNote, int noteIndex, int voice){
		boolean hasTup = curNote.getTuplet() == null ? false : true; //�Ƿ����������
		Measure measure = curNote.getMeasure();
		if(measure == null){
			return null;
		}
		ChordNote result = null;
		Page page = (Page)measure.getParent();
		//֮ǰ���Ǻ���
		if(curNote instanceof Note){
			Note nnote = (Note)curNote;
			ChordNote cnote = new ChordNote(nnote);
			if(newNote.getPitch() != nnote.getPitch()){
				cnote.addNote(newNote);
			    cnote.addNote(nnote);
			    //�ú����滻��ԭ������
			    measure.removeNote(curNote);
			    page.deleteNote(curNote, false);
			    page.add(curNote);
			    page.add(newNote);
			    measure.addNote(noteIndex, cnote, voice);
			    if(hasTup){
			    	Tuplet tup = curNote.getTuplet();
			    	int index = tup.getNoteList().indexOf(curNote);
			    	tup.getNoteList().remove(curNote);
			    	tup.getNoteList().add(index, cnote);
			    	cnote.setTuplet(tup);
			    }
			}
			result = cnote;
		}
		//֮ǰ�Ѿ��Ǻ���
		else if(curNote instanceof ChordNote){
			ChordNote cnote = (ChordNote)curNote;
			ArrayList<Integer> List = new ArrayList<Integer>();
            for(int i = 0; i < cnote.getNoteNum(); i++){
            	List.add(cnote.getNote(i).getPitch());
            }
            if(List.contains(newNote.getPitch())){
            	return cnote;
            }else{
            	cnote.addNote(newNote);
		        page.add(newNote);
			if(newNote.getUiDot() != null)
				page.add(newNote.getUiDot());
            }
            result = cnote;
	    }
		return result;
	}
	
	/**
	 * �������ķ��ܡ���������β��ʵ��ɾ��
	 * @param note
	 */
	public void deleteNoteBeamStemTail(AbstractNote note){
		Page page = null;
		if(note instanceof Note){
			Note nnote = (Note)note;
			page = (Page)nnote.getParent();
		}
		else if(note instanceof ChordNote){
			ChordNote cnote = (ChordNote)note;
			page = (Page)cnote.getNote(0).getParent();
		}
		if(note.getStem() != null)
			page.remove(note.getStem());
		if(note.getBeam() != null)
			page.remove(note.getBeam());
		if(note.getTail() != null)
			page.remove(note.getTail());
	}

	
	/**
	 * Ϊ�������ø���
	 * @param note ����
	 * @param dotNum �����õĸ������
	 * @return
	 */
	public boolean addDotForNote(Note note, int dotNum){
		Measure measure = note.getMeasure();
		int noteIndex = measure.noteIndex(note);
		Page page = (Page)note.getParent();
		AbstractNote anote = note.getChordNote() == null ? note : note.getChordNote();
		int voice = anote.getVoice();
		
		//�����ƽ���С��,��û��������
		if(measure.isUnlimited() && note.getTuplet() == null){
			note.setDotNum(dotNum);
			note.generateUIDot();
			((JComponent)page).add(note.getUiDot());
			NoteLine line = measure.getMeasurePart().getNoteLine();
			while(canvas.redrawLine(line)){
				line = MusicMath.nxtLine(line);
			}
		}
		
		//ԭ���������
		int formerDotNum = note.getDotNum();
		if(formerDotNum == dotNum) 
			return false;
		
		//�µĸ���ʱ��
		int dotDuration = MusicMath.dotDuration(note.getDuration(), dotNum);
		//�ϸ���ʱ��
		int oldDotDuration = MusicMath.dotDuration(note.getDuration(), formerDotNum);
		
		//������������������
		if(note.getTuplet() == null){
			int curDuration = oldDotDuration;
			float tempDur = 0.0f;
			for(int i = noteIndex + 1; i < measure.getNoteNum(voice); i++){
				AbstractNote nt = measure.getNote(i, voice);
				tempDur += nt.getRealDuration();
			}
			curDuration = Math.round(tempDur);
			
			//ʣ���ʱ���㹻
			if(curDuration >= dotDuration){
				curDuration = oldDotDuration;
				note.setDotNum(dotNum);
				note.generateUIDot();
				if(note.getUiDot().getParent() != page)
					page.add(note.getUiDot());
				//�ϲ�������ֱ��ʱ���㹻
				while(curDuration < dotDuration){
					AbstractNote tempNote = measure.getNote(noteIndex + 1, voice);
					//ĳ��������������
					if(tempNote.getTuplet() != null){
						Tuplet tup = tempNote.getTuplet();
						curDuration += tup.getRealDuration();
						for(AbstractNote an : tup.getNoteList()){
							measure.removeNote(an);
						}
						page.deleteNote(tempNote, true);
						tempNote.removeAllSymbols(true);
					}
					//������������
					else{
						measure.removeNote(tempNote);
						page.deleteNote(tempNote, false);
						tempNote.removeAllSymbols(false);
						curDuration += tempNote.getDurationWithDot();
					}
				}
				//�ϲ�֮���ж��࣬�򽫶���Ľ����з�
				if(curDuration > dotDuration){
					int rest = curDuration - dotDuration;
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
					for(int i = 0, n = splited.size(); i < n; i++){
						Note nt = splited.get(n - 1 - i);
						measure.addNote(noteIndex + i + 1, nt, voice);
						page.add(nt);
						nt.addMouseListener(canvas);
					}
				}
				return true;
			}
		}
		
		//����������������
		else{
			Tuplet tuplet = note.getTuplet();
			int index = tuplet.getNoteList().indexOf(note);
			int curDuration = oldDotDuration;
			for(int i = index + 1; i < tuplet.getNoteList().size(); i++){
				AbstractNote nt = tuplet.getNoteList().get(i);
				curDuration += nt.getDurationWithDot();
			}
			
			//ʣ���ʱ���㹻
			if(curDuration >= dotDuration){
				curDuration = oldDotDuration;
				note.setDotNum(dotNum);
				note.generateUIDot();
				if(note.getUiDot().getParent() != page)
					page.add(note.getUiDot());
				//�ϲ�������ֱ��ʱ���㹻
				while(curDuration < dotDuration){
					AbstractNote tempNote = measure.getNote(noteIndex + 1, voice);
					measure.removeNote(tempNote);
					page.deleteNote(tempNote, false);
					tempNote.removeAllSymbols(false);
					tuplet.getNoteList().remove(tempNote);
					curDuration += tempNote.getDurationWithDot();
				}
				//�ϲ�֮���ж��࣬�򽫶���Ľ����з�
				if(curDuration > dotDuration){
					int rest = curDuration - dotDuration;
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
					for(int i = 0; i < splited.size(); i++){
						Note nt = splited.get(i);
						measure.addNote(noteIndex + i + 1, nt, voice);
						page.add(nt);
						tuplet.getNoteList().add(index + i + 1, nt);
						nt.setTuplet(tuplet);
						nt.addMouseListener(canvas);
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ɾ�������ĸ��㣬ɾ���ɹ��򷵻�true�����򷵻�false.
	 * @param note
	 * @return
	 */
	public boolean removeDotFromNote(Note note){
		if(note.getUiDot() == null || note.getChordNote() != null)
			return false;
		
		Measure measure = note.getMeasure();
		Page page = measure.getMeasurePart().getNoteLine().getPage();
		AbstractNote anote = note.getChordNote() == null ? note : note.getChordNote();
		int voice = anote.getVoice();
		int noteIndex = measure.noteIndex(note);
		int dotDur = MusicMath.dotDuration(note.getDuration(), note.getDotNum());
		//ɾ������
		note.setDotNum(0);
		page.remove(note.getUiDot());
		note.setUiDot(null);
//		note.getDotNum();
		
		ArrayList<Note> splited = MusicMath.splitDurIntoRests(dotDur);
		for(int i = 0; i < splited.size(); i++){
			Note n = splited.get(i);
			n.addMouseListener(canvas);
			n.addMouseMotionListener(canvas);
			measure.addNote(noteIndex + i + 1, n, voice);
			page.add(n);
		}
		
		return true;
	}
	
	/**
	 * ɾ��������ĳ������
	 * @param o
	 */
	public void deleteObject(Object o){
		//ɾ������
		if(o instanceof Note){
			Note note = (Note)o;
			note.deletePosLines();
			
			//����
			if(note instanceof Grace){
				Grace grace = (Grace)note;
				ChordGrace cg = ((Grace)note).getChordNote() == null ? null : (ChordGrace)((Grace)note).getChordNote();
				if(cg == null){
					AbstractNote anote = ((Grace)note).getNote();
					anote.removeGrace((Grace)note);
					Page page = (Page)grace.getParent();
					page.deleteNote(grace, false);
					//grace.removeAllSymbols(false);
					NoteLine line = anote.getMeasure().getMeasurePart().getNoteLine();
					while(canvas.redrawLine(line)){
						line = MusicMath.nxtLine(line);
					}
				}else{
					AbstractNote bnote = cg.getNote();
					NoteLine line = cg.getMeasure().getMeasurePart().getNoteLine();
					
					cg.removeNote(grace);
					Page page = (Page)grace.getParent();
					page.deleteNote(grace, false);
					//grace.removeAllSymbols(false);
					if(cg.getNoteNum() < 2){
						Grace onlyGrace = (Grace)cg.getNote(0);
						boolean left = grace.getX() < bnote.getX();
						if(left){
							int index = bnote.getLeftGraces().indexOf(cg);
							bnote.getLeftGraces().remove(index);
							bnote.addLeftGrace(index, onlyGrace);
						}
						else{
							int index = bnote.getRightGraces().indexOf(cg);
							bnote.getRightGraces().remove(index);
							bnote.addRightGrace(index, onlyGrace);
						}
						cg.clearNoteList();
						//ת�Ʒ���
						transferSymbols(cg, onlyGrace);
						page.deleteNote(cg, false);
					}
					
					while(canvas.redrawLine(line)){
						line = MusicMath.nxtLine(line);
					}
				}
				return;
			}
			
			Measure measure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
			NoteLine line = measure.getMeasurePart().getNoteLine();
			Page page = line.getPage();
			
			//�Ǻ��ң�����ֹ��
			if(note.getChordNote() == null && !note.isRest()){
				//��ͨ����
				if(canvas.getScore().getScoreType() == Score.SCORE_NORMAL){
					if(note.getDotNum() != 0){
//						int noteIndex = measure.noteIndex(note);
						ArrayList<Note> splited = MusicMath.splitDurIntoRests(note.getDurationWithDot()-note.getDuration());
						for(int i = 0; i < splited.size(); i++){
							Note n = splited.get(i);
							n.addMouseListener(canvas);
							n.addMouseMotionListener(canvas);
							//measure.addNote(noteIndex, n, note.getVoice());
							//page.add(n);
						}
					}
					DelNoteAction action = new DelNoteAction(note, this);
					actionController.pushAction(action);
					page.deleteNote(note, false);
					note.setRest(true);
					note.setPitch(0);
					//note.removeAllSymbols(false);
					page.add(note);
				}
				//�����ƽ�������
				else{
					page.deleteNote(note, false);
					//note.removeAllSymbols(false);
					measure.removeNote(note);
					canvas.redrawLine(line);
				}
			}//����
			else if(note.getChordNote() != null){
				RemoveChordAction action = new RemoveChordAction(this);
				actionController.pushAction(action);
				ChordNote cnote = note.getChordNote();
				action.setPreNote(cnote);
				action.setNote(note);
				AbstractNote afterNote = removeChord(cnote, note);
				action.setCurNote(afterNote);
			}
			//��������ֹ��������
			else if(note.isRest()){
				note.setHidden(true);
				note.repaint();
			}
			canvas.redrawLine(line);
			JComponent pr = (JComponent)canvas.getParent();
			pr.revalidate();
			pr.updateUI();
			canvas.revalidate();
			canvas.updateUI();
		}
		else if(o instanceof SharpOrFlat){
			SharpOrFlat sof = (SharpOrFlat)o;
			Note note = sof.getNote();
			note.setSharpOrFlat(null);
			sof.setNote(null);
			sof.getParent().remove(sof);
			JComponent pr = (JComponent) canvas.getParent();
			Measure measure = note.getChordNote() == null ? note.getMeasure() : note.getChordNote().getMeasure();
	        	NoteLine line = measure.getMeasurePart().getNoteLine();
	        	while(canvas.redrawLine(line)){
	        		line = MusicMath.nxtLine(line);
	        	}
			pr.updateUI();
		}
		//ɾ����������
		else if(o instanceof SymbolLine){
			//��ǰƬ��
			SymbolLine cur = (SymbolLine)o;
			AbstractNote startNote = cur.getStartNote();
			AbstractNote endNote = cur.getEndNote();
			//ɾ����������
			if(startNote != null){
				cur.setStartNote(null);
				startNote.getSymbolLines().remove(cur);
			}
			if(endNote != null){
				cur.setEndNote(null);
				endNote.getSymbolLines().remove(cur);
			}
			//ɾ��ʵ��
			JComponent parent = (JComponent)cur.getParent();
			parent.remove(cur);
			
			//֮ǰ��Ƭ��
			SymbolLine pre = (SymbolLine)cur.getPreSymbolLine();
			while(pre != null){
				AbstractNote snote = pre.getStartNote();
				//ɾ����������
				if(snote != null){
					pre.setStartNote(null);
					snote.getSymbolLines().remove(pre);
				}
				//ɾ��ʵ��
				JComponent p = (JComponent)pre.getParent();
				p.remove(pre);
				pre.setNextSymbolLine(null);
				pre = (SymbolLine)pre.getPreSymbolLine();
			}
			
			//֮���Ƭ��
			SymbolLine nxt = (SymbolLine)cur.getNextSymbolLine();
			while(nxt != null){
				AbstractNote snote = nxt.getEndNote();
				//ɾ����������
				if(snote != null){
					nxt.setEndNote(null);
					snote.getSymbolLines().remove(nxt);
				}
				//ɾ��ʵ��
				JComponent p = (JComponent)nxt.getParent();
				p.remove(nxt);
				nxt.setPreSymbolLine(null);
				nxt = (SymbolLine)nxt.getNextSymbolLine();
			}
			
			JComponent pr = (JComponent)canvas.getParent();
			pr.revalidate();
			pr.updateUI();
			
			canvas.getSelectedObjects().remove(o);
		}
		//ɾ��tie����
		else if (o instanceof Tie){
			Tie tie = (Tie)o;
			AbstractNote startNote = tie.getStartNote();
			AbstractNote endNote = tie.getEndNote();
			//ɾ����������
			if(startNote != null){
				
			//	startNote.getTies().remove(tie);
				tie.setStartNote(null);
			}
			if(endNote != null){
				
			//	endNote.getTies().remove(tie);
				tie.setEndNote(null);
			}
			//ɾ��ʵ��
			JComponent parent = (JComponent)tie.getParent();
			parent.remove(tie);					
			JComponent pr = (JComponent)canvas.getParent();
			pr.revalidate();
			pr.updateUI();
			
			canvas.getSelectedObjects().remove(o);
			
		}
		//ɾ��ornament����
		else if(o instanceof Ornament){
			Ornament ornament = (Ornament)o;
			AbstractNote note = ornament.getNote();
			note.removeOrnament(ornament);
			note.locateNoteSymbols();
			ornament.getParent().remove(ornament);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		//ɾ��graceSymbol
		else if(o instanceof GraceSymbol){
			GraceSymbol graceSymbol = (GraceSymbol)o;
			AbstractNote note = graceSymbol.getNote();
			if(note != null){
				note.removeGraceSymbol(graceSymbol);
				note.locateNoteSymbols();
			}
			graceSymbol.getParent().remove(graceSymbol);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		//ɾ����������
		else if(o instanceof TremoloBeam){
			TremoloBeam tremoloBeam = (TremoloBeam)o;
			AbstractNote note = tremoloBeam.getNote();
			note.setTremoloBeam(null);
			tremoloBeam.setNote(null);
			tremoloBeam.getParent().remove(tremoloBeam);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		//ɾ��Dynamic����
		else if (o instanceof Dynamic) {
			Dynamic dynamic = (Dynamic) o;
			AbstractNote note = dynamic.getNote();
			note.removeDynamics(dynamic);
			note.locateNoteSymbols();
			dynamic.getParent().remove(dynamic);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		} 
		//ɾ����������
		else if (o instanceof PerformanceSymbol) {
			PerformanceSymbol performanceSymbol = (PerformanceSymbol) o;
			AbstractNote note = performanceSymbol.getNote();
			note.removePerformanceSymbols(performanceSymbol);
			note.locateNoteSymbols();
			performanceSymbol.getParent().remove(performanceSymbol);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		} 
		//ɾ���ظ��Ǻ�
		else if(o instanceof RepeatSymbol){
			RepeatSymbol rs  = (RepeatSymbol) o;
			MeasurePart measurePart = rs.getMeasurePart();
			measurePart.removeRepeatSymbol(rs);
			rs.getParent().remove(rs);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		
		//ɾ�����ӼǺ�
		else if(o instanceof RepeatLine){	
			//��ǰƬ��
			RepeatLine cur = (RepeatLine)o;
			MeasurePart startMeasurePart = cur.getStartMeasurePart();
			MeasurePart endMeasurePart = cur.getEndMeasurePart();
			
			//ɾ��С�ڹ���
			if(startMeasurePart != null){
				cur.setStartMeasurePart(null);
				startMeasurePart.getRepeatLines().remove(cur);
			}
			if(endMeasurePart != null){
				cur.setEndMeasurePart(null);
				endMeasurePart.getRepeatLines().remove(cur);
			}
			
			//ɾ��ʵ��
			JComponent parent = (JComponent)cur.getParent();
			parent.remove(cur);
			
			//֮ǰ��Ƭ��
			RepeatLine pre = (RepeatLine)cur.getPreSymbolLine();
			
			while(pre != null){
				MeasurePart sMeasurePart = pre.getStartMeasurePart();
				//ɾ��С�ڹ���
				if(sMeasurePart != null){
					pre.setStartMeasurePart(null);
					sMeasurePart.getRepeatLines().remove(pre);
				}
				//ɾ��ʵ��
				JComponent p = (JComponent)pre.getParent();
				p.remove(pre);
				pre.setNextSymbolLine(null);
				pre = (RepeatLine)pre.getPreSymbolLine();
				
			}
			
			//֮���Ƭ��
			RepeatLine nxt = (RepeatLine)cur.getNextSymbolLine();
			while(nxt != null){
				MeasurePart eMeasurePart = nxt.getEndMeasurePart();
				//ɾ��С�ڹ���
				if(eMeasurePart != null){
					nxt.setEndMeasurePart(null);
					eMeasurePart.getRepeatLines().remove(nxt);
				}
				
				//ɾ��ʵ��
				JComponent p = (JComponent)pre.getParent();
				p.remove(pre);
				pre.setNextSymbolLine(null);
				pre = (RepeatLine)pre.getPreSymbolLine();
				
			}
			
			JComponent pr = (JComponent)canvas.getParent();
			pr.revalidate();
			pr.updateUI();
			
			canvas.getSelectedObjects().remove(o);
		}
		
		else if(o instanceof Breath){
			Breath breath  = (Breath) o;
			AbstractNote note = breath.getNote();
			note.removeBreath();
		//	note.locateNoteSymbols();
		//	note.setBreath(null);
			breath.getParent().remove(breath);
			note.locateNoteSymbols();
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		
		else if(o instanceof Pedal){
			Pedal pedal  = (Pedal) o;
			AbstractNote note = pedal.getNote();
			note.removeBreath();
		//	note.locateNoteSymbols();
		//	note.setBreath(null);
			pedal.getParent().remove(pedal);
			note.locateNoteSymbols();
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		
		//ɾ�����
		else if (o instanceof Lyrics) {
			Lyrics lyric = (Lyrics) o;
			AbstractNote note = lyric.getNote();
			note.removeLyrics(lyric);
			lyric.getParent().remove(lyric);
			note.locateNoteSymbols();
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		//�����ı�
		else if(o instanceof FreeAddedText){
			FreeAddedText txt = (FreeAddedText)o;
			canvas.getScore().removeFreeText(txt);
			txt.getParent().remove(txt);
			JComponent pr = (JComponent) canvas.getParent();
			pr.revalidate();
			pr.updateUI();
		}
		
		//���С��������
		else if(o instanceof Measure){
			Measure measure = (Measure)o;
			ClearMeasureAction action = new ClearMeasureAction(this, measure);
			actionController.pushAction(action);
			clearMeasure(measure);
		}
		
		//ɾ��ע��
		else if(o instanceof Annotation.ImagePanel){
			Annotation.ImagePanel imgPanel = (Annotation.ImagePanel)o;
			Annotation ant = imgPanel.getAt();
			ant.getParent().remove(ant);
			for(Selectable s : ant.getRelatedObjts()){
				s.cancleSelected();
				if(s instanceof Measure){
					Measure mea = (Measure)s;
					mea.getAnnotations().remove(ant);
				}
				else if(s instanceof Note){
					Note note = (Note)s;
					note.getAnnotations().remove(ant);
				}
			}
			((JComponent)canvas.getParent()).updateUI();
			canvas.getVeil().setVisible(false);
		}
		
		//ɾ���ٶȼǺ�
		else if(o instanceof TempoText || o instanceof TempoText.EditField){
			TempoText tt = o instanceof TempoText? (TempoText)o : (TempoText)((TempoText.EditField)o).getParent();
			AbstractNote note = tt.getNote();
			note.setTempoText(null);
			tt.setNote(null);
			tt.getParent().remove(tt);
			canvas.repaint();
		}
	}
	
	/**
	 * ���С���ڵ�����
	 * @param measure
	 */
	public void clearMeasure(Measure measure){
		NoteLine line = measure.getMeasurePart().getNoteLine();
		Page page = line.getPage();
		int voiceNum = measure.getVoiceNum();
		for(int i = 0; i < voiceNum; i++){
			while(measure.getNoteNum(i) > 0){
				AbstractNote note = measure.getNote(0, i);
				page.deleteNote(note, true);
				measure.removeNote(note);
//				note.removeAllSymbols(true);
			}
		}
		if(!measure.isUnlimited()){
			Note rest = new Note(MusicMath.getMeasureDuration(measure), true);
			measure.addNote(rest, 0);
			page.add(rest);
			rest.addMouseListener(canvas);
			rest.addMouseMotionListener(canvas);
		}
		canvas.redrawLine(line);
	}
	
	public AbstractNote removeChord(ChordNote cnote, Note note){
		if(note.getChordNote() != cnote){
			return null;
		}
		AbstractNote result = cnote;
		Measure measure = cnote.getMeasure();
		Page page = (Page)measure.getParent();
		cnote.removeNote(note);
		int voice = cnote.getVoice();
		if(cnote.getNoteNum() < 2){
			int noteIndex = measure.noteIndex(cnote);
			Note onlyNote = cnote.getNote(0);
			result = onlyNote;
			onlyNote.setChordNote(null);
			measure.removeNote(cnote);
			measure.addNote(noteIndex, onlyNote, voice);
			//ת�Ʒ���
			transferSymbols(cnote, onlyNote);
			
			page.deleteNote(cnote, false);	
		}
		page.deleteNote(note, false);
		note.removeAllSymbols(false);		
		canvas.getSelectedObjects().remove(note);
		return result;
	}
	
	/**
	 * ����ָ��������֮�����һ��������
	 * @param instrIndex ָ�����������
	 * @param newInstrument ��������MIDI��ֵ
	 * @param direction ��ӵ�λ��,��Чֵ��:"up"��"down"����
	 */
	public void addInstrument(int instrIndex, int newInstrument, String direction){
		Score score = canvas.getScore();
		MeasurePart fmeaPart = score.getPageList().get(0).getNoteLines().get(0).getMeaPartList().get(0);
		int meaIndex = -1;
		if(direction.equalsIgnoreCase("up"))
			meaIndex = fmeaPart.getMeaIndxByInstrIndx(instrIndex);
		else
			meaIndex = fmeaPart.getMeaIndxByInstrIndx(instrIndex + 1);
		//���С�ڱ����λ����-1,����ӵ�������
		meaIndex = meaIndex == -1 ? fmeaPart.getMeasureNum() : meaIndex;
		
		for(int i = 0, in = score.getPageList().size(); i < in; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart measurePart = line.getMeaPartList().get(k);
					//ԭ����С��
					Measure oldMeasure = measurePart.getMeasure(0);
					
					for(int p = 0 ; p < MusicMath.getPartNumOfInstr(newInstrument); p++){
						String clefType = MusicMath.getClefsOfInstrument(newInstrument).get(p);
						int keyValue = oldMeasure.getKeyValue();
						Time time = new Time(oldMeasure.getTime());
						//С��
						Measure newMeasure = new Measure(clefType, keyValue, time);
						measurePart.addMeasure(meaIndex + p, newMeasure);
						newMeasure.addMouseListener(canvas);
						if(p == 0)
							newMeasure.setInstrument(String.valueOf(newInstrument));
						//ȫ��ֹ��
						Note note = new Note(MusicMath.getMeasureDuration(newMeasure), true);
						newMeasure.addNote(note, 0);
						note.addMouseListener(canvas);
						//���ʵ��
						page.add(newMeasure);
						page.add(note);
					}
				}
				line.generateBrackets();
				line.generateMarkers();
			}
		}
	}
	
	/**
	 * ɾ��ĳ������.
	 * @param meaIndex ������ţ���һ��С������С�����е����
 	 */
	public void removePart(int meaIndex){
		Score score = canvas.getScore();
		
		//ֻ��һ������������ɾ��
		MeasurePart fmeaPart = score.getPageList().get(0).getNoteLines().get(0).getMeaPartList().get(0);
		if(fmeaPart.getMeasureNum() == 1)
			return;
		
		for(int i = 0, in = score.getPageList().size(); i < in; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart measurePart = line.getMeaPartList().get(k);
					Measure dmeasure = measurePart.getMeasure(meaIndex);
					Measure nxtMeasure = null;
					if(meaIndex < measurePart.getMeasureNum() - 1)
						nxtMeasure = measurePart.getMeasure(meaIndex + 1);
					
					//�Ƴ�С�ڡ���������������
					measurePart.removeMeasure(dmeasure);
					for(int m = 0; m < dmeasure.getVoiceNum(); m++){
						while(dmeasure.getNoteNum(m) > 0){
							AbstractNote note = dmeasure.getNote(0, m);
							if(note.getTuplet() != null){
								for(AbstractNote tnt : note.getTuplet().getNoteList()){
									dmeasure.removeNote(tnt);
								}
							}
							page.deleteNote(note, true);
							note.removeAllSymbols(true);
							dmeasure.removeNote(note);
						}
					}
					page.remove(dmeasure);
					dmeasure.deleteMeaAttr();
					
					//���ɾ����һ�������������ĵ�һ������
					if(nxtMeasure != null && dmeasure.getInstrument() != null){
						if(nxtMeasure.getInstrument() == null)
							nxtMeasure.setInstrument(dmeasure.getInstrument());
					}
				}
				line.generateBrackets();
				page.remove(line.getMarkers().get(meaIndex));
				line.getMarkers().remove(meaIndex);
			}
		}
	}
	
	/**
	 * ��ָ��С���鿪ʼ������֮���С������ĺŶ���Ϊָ�����ĺ�ֵ
	 * @param meaPart ָ������ʼС����
	 * @param time ָ�������ĺ�
	 * @param return �����ĺŵ����һ�У����������û�а���ָ��С���飬�򷵻�null
	 */
	public NoteLine changeTime(MeasurePart meaPart, Time time){
		//��ʾ�Ƿ񵽴���ʼС����
		boolean flag = false;
		//��ʼС������ĺ�
		Time oldTime = meaPart.getMeasure(0).getTime();
		Score score = canvas.getScore();
		ChangeMultiTimeAction action = new ChangeMultiTimeAction(meaPart, this);
		for(int i = 0, n = score.getPageList().size(); i < n; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart measurePart = line.getMeaPartList().get(k);
					if(measurePart == meaPart)
						flag = true;
					if(flag){
						for(int m = 0, mn = measurePart.getMeasureNum(); m < mn; m++){
							Measure measure = measurePart.getMeasure(m);
							if(measure.getTime().equals(oldTime)){
								ChangeMeaTimeAction aaction = changeMeasureTime(measure, time);
								action.addTimeAction(aaction);
							}
							else
								return line;
						}
					}
				}
			}
		}
		if(action.singleActionSize() != 0){
			actionController.pushAction(action); //����ѹջ
		}
		if(flag == false)
			return null;
		else{
			Page lastPage = score.getPageList().get(score.getPageList().size()-1);
			NoteLine lastLine = lastPage.getNoteLines().get(lastPage.getNoteLines().size()-1);
			return lastLine;
		}
	}
	
	/**
	 * ��ĳ��С�ڵ��ĺ���Ϊָ��ֵ
	 * @param measure
	 * @param time
	 */
	public ChangeMeaTimeAction changeMeasureTime(Measure measure, Time time){
		//�����ĺ���ͬ
		if(measure.getTime().equals(time) && MusicMath.getMeasureDuration(measure) == MusicMath.getMeasureDuration(time))
			return null;
		
		ChangeMeaTimeAction action = new ChangeMeaTimeAction(this, measure, measure.getTime(), time);
		Page page = measure.getMeasurePart().getNoteLine().getPage();
		
		for(int v = 0; v < measure.getVoiceNum(); v++){
			List<AbstractNote> preNoteList = new ArrayList<AbstractNote>();
			List<AbstractNote> curNoteList = new ArrayList<AbstractNote>();
			action.addPreNoteList(preNoteList);
			action.addCurNoteList(curNoteList);
			
			//������ȫ��ֹ��,�����޸ĸ���ֹ����ʱ������
			if(measure.getNoteNum(v) == 1){
				AbstractNote note = measure.getNote(0, v);
				if(note instanceof Note && ((Note)note).isRest() && ((Note)note).isFull()){
					page.deleteNote(note, false);
					measure.removeNote(note);
					Note nnote = new Note();
					nnote.setRest(true);
					nnote.setFull(true);
					nnote.setDuration(MusicMath.getMeasureDuration(time));
					measure.addNote(nnote, v);
					page.add(nnote);
//					note.setDuration(MusicMath.getMeasureDuration(time));
					preNoteList.add(note);
					curNoteList.add(nnote);
					continue;
				}
			}
			
			//���ĺŸ���
			if(MusicMath.getMeasureDuration(time) > MusicMath.getMeasureDuration(measure)){
				int rest = MusicMath.getMeasureDuration(time) - MusicMath.getMeasureDuration(measure);
				ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
				for(int i = splited.size() - 1; i >= 0; i--){
					AbstractNote note = splited.get(i);
					measure.addNote(note, v);
					page.add(note);
					note.addMouseListener(canvas);
					note.addMouseMotionListener(canvas);
					curNoteList.add(note);
				}
			}
			
			//���ĺűȾ��ĺŶ�
			else if(MusicMath.getMeasureDuration(time) < MusicMath.getMeasureDuration(measure)){
				int diff = MusicMath.getMeasureDuration(measure) - MusicMath.getMeasureDuration(time);
				//��ǰ��ȥ��������ʱ���ܺ�
				int accum = 0;
				for(int i = measure.getNoteNum(v) - 1; i >= 0; i--){
					//ȥ����ʱ���Ѿ��㹻
					if(accum >= diff){
						break;
					}
					
					AbstractNote note = measure.getNote(measure.getNoteNum(v)-1, v);
					//����û��������
					if(note.getTuplet() == null){
						measure.removeNote(note);
						page.deleteNote(note, false);
//						note.removeAllSymbols(false);
						accum += note.getDurationWithDot();
						preNoteList.add(note);
					}
					//����������
					else{
						Tuplet tup = note.getTuplet();
						accum += tup.getRealDuration();
						for(AbstractNote anote : tup.getNoteList()){
							measure.removeNote(anote);
							preNoteList.add(anote);
						}
						page.deleteNote(note, true);
//						note.removeAllSymbols(true);
					}
				}
				//ʵ��ȥ���ı���Ҫȥ���Ķ࣬�򽫶�������з�
				if(accum > diff){
					ArrayList<Note> splited = MusicMath.splitDurIntoRests(accum - diff);
					for(int j = 0, n = splited.size(); j < n; j++){
						Note nnote = splited.get(n - j - 1);
						measure.addNote(nnote, v);
						page.add(nnote);
						nnote.addMouseListener(canvas);
						nnote.addMouseMotionListener(canvas);
						curNoteList.add(nnote);
					}
				}
			}
		}
		measure.setTime(time);
		return action;
	}
	
	/**
	 * ��ָ��С���鿪ʼ��������С�����ָ��λ�õ�С�ڵ��׺�����Ϊָ��ֵ
	 * @param measurePart ��ʼС����
	 * @param meaIndex С��λ��
	 * @param clefType ָ���׺�
	 */
	public void changeClef(MeasurePart measurePart, int meaIndex, String clefType){
		Measure measure = measurePart.getMeasure(meaIndex);
		String oldClef = measure.getClefType();
		Score score = canvas.getScore();
		boolean flag = false;
		for(int i = 0, in = score.getPageList().size(); i < in; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart meaPart = line.getMeaPartList().get(k);
					if(meaPart == measurePart)
						flag = true;
					if(flag){
						Measure mea = meaPart.getMeasure(meaIndex);
						//��ָ��С���鿪ʼ��һֱ����һ���׺ű仯��Ϊֹ
						if(!mea.getClefType().equalsIgnoreCase(oldClef)){
							return;
						}
						for(int v = 0; v < mea.getVoiceNum(); v++){
							for(int n = 0, nn = mea.getNoteNum(v); n < nn; n++){
								AbstractNote note = mea.getNote(n, v);
								if(note instanceof Note){
									Note nnote = (Note)note;
									if(!nnote.isRest())
										nnote.determinePitchByRealPitch(clefType);
								}
								else if(note instanceof ChordNote){
									ChordNote cnote = (ChordNote)note;
									for(int p = 0; p < cnote.getNoteNum(); p++){
										Note nt = cnote.getNote(p);
										nt.determinePitchByRealPitch(clefType);
									}
								}
							}
						}
						mea.setClefType(clefType);
					}
				}
			}
		}
	}
	
	/**
	 * ��ָ����С���鿪ʼ����ÿ��С������ָ��λ�õ�С�ڵĵ���ֵ����Ϊָ��ֵ
	 * @param measurePart ��ʼС����
	 * @param meaIndex С��λ��
	 * @param key ָ������ֵ
	 */
	public void changeKey(MeasurePart measurePart, int meaIndex, int key){
		Measure measure = measurePart.getMeasure(meaIndex);
		int oldKey = measure.getKeyValue();
		Score score = canvas.getScore();
		boolean flag = false;
		for(int i = 0, in = score.getPageList().size(); i < in; i++){
			Page page = score.getPageList().get(i);
			for(int j = 0, jn = page.getNoteLines().size(); j < jn; j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0, kn = line.getMeaPartList().size(); k < kn; k++){
					MeasurePart meaPart = line.getMeaPartList().get(k);
					if(meaPart == measurePart)
						flag = true;
					if(flag){
						Measure mea = meaPart.getMeasure(meaIndex);
						//��ָ��С���鿪ʼ��һֱ����һ�����ű仯��Ϊֹ
						if(mea.getKeyValue() != oldKey){
							return;
						}
						mea.setKeyValue(key);
					}
				}
				
			}
		}
	}
	
	/**
	 * ���������������
	 * @param note
	 * @param modification
	 * @param normal
	 */
	public void addTuplet(Note note, int modification, int normal){
		AbstractNote curNote = note.getChordNote() == null ? note : note.getChordNote();
		int voice = curNote.getVoice();
		
		//������Ƕ������
		if(curNote.getTuplet() != null)
			return;
		
		Measure measure = curNote.getMeasure();
		int curDur = note.getDurationWithDot();
		int index = measure.noteIndex(curNote);
		Page page = (Page)note.getParent();
		
		//����������ƽ������ͣ�ֱ�����
		if(canvas.getScore().getScoreType() == Score.SCORE_UNLMTED){
			//�����ɵ�����������
			Tuplet tuplet = new Tuplet(curNote, modification, normal);
			page.add(tuplet);
			//��ǰ�����������λ��
			int curIndex = index + 1;
			for(int i = 1; i < modification; i++){
				Note rest = new Note(note.getDuration(), false);
				tuplet.getNoteList().add(rest);
				rest.setTuplet(tuplet);
				measure.addNote(curIndex++, rest, voice);
				page.add(rest);
				rest.addMouseListener(canvas);
				rest.addMouseMotionListener(canvas);
			}
			return;
		}
		
		int restDur = MusicMath.restDuration(measure, note);
		int durNeeded = note.getDuration() * normal;
		//С��ʱ������
		if(restDur < durNeeded)
			return;
		
		//�ϲ�ֱ��ʱ���㹻
		for(int i = index + 1; index + 1 < measure.getNoteNum(voice) && curDur < durNeeded; i++){
			AbstractNote anote = measure.getNote(index + 1, voice);
			if(curDur < durNeeded){
				if(anote.getTuplet() == null){
					page.deleteNote(anote, false);
					anote.removeAllSymbols(false);
					measure.removeNote(anote);
					curDur += anote.getDurationWithDot();
				}
				else{
					Tuplet tuplet = anote.getTuplet();
					for(AbstractNote an : tuplet.getNoteList()){
						measure.removeNote(an);
					}
					page.deleteNote(anote, true);
					anote.removeAllSymbols(true);
					curDur += tuplet.getRealDuration();
				}
			}
		}
		
		//�����ɵ�����������
		Tuplet tuplet = new Tuplet(curNote, modification, normal);
		page.add(tuplet);
		//��ǰ�����������λ��
		int curIndex = index + 1;
		for(int i = 1; i < modification; i++){
			Note rest = new Note(note.getDuration(), false);
			tuplet.getNoteList().add(rest);
			rest.setTuplet(tuplet);
			measure.addNote(curIndex++, rest, voice);
			page.add(rest);
			rest.addMouseListener(canvas);
			rest.addMouseMotionListener(canvas);
		}
		
		//�����ʱ���з�Ϊ��ֹ��
		if(curDur > durNeeded){
			int rest = curDur - durNeeded;
			ArrayList<Note> splited = MusicMath.splitDurIntoRests(rest);
			for(int i = splited.size() - 1; i >= 0; i--){
				Note n = splited.get(i);
				measure.addNote(curIndex++, n, voice);
				page.add(n);
				n.addMouseListener(canvas);
				n.addMouseMotionListener(canvas);
			}
		}	
	}
	
	/**
	 * ��ָ����С�������׷��С����
	 * @param measurePart ָ��С����
	 * @param num ��ӵ�С�������
	 * �����С�ڵ�ʱ����Ҫ���µ�С��������������򵼳���MusicXML�ĵ��������
	 */
	public void addMeasure(MeasurePart measurePart, int num){
		int meaNum = measurePart.getMeasureNum();
		NoteLine line = measurePart.getNoteLine();
		int index = line.getMeaPartList().indexOf(measurePart);
		JComponent parent = (JComponent)measurePart.getMeasure(0).getParent();
		
		for(int i = 0; i < num ; i++){
			MeasurePart meaPart = new MeasurePart();
			for(int j = 0; j < meaNum; j++){
				Time time = null;
				Measure measure = null;
				String instrument = measurePart.getMeasure(j).getInstrument();
				//����ģʽ
				if(canvas.getScore().getScoreType() == Score.SCORE_NORMAL){
					time = new Time(measurePart.getMeasure(j).getTime());
					measure = new Measure(measurePart.getMeasure(j).getClefType(), 
							measurePart.getMeasure(j).getKeyValue(), time);
					Note note = new Note(MusicMath.getMeasureDuration(measure), true);
					note.addMouseListener(canvas);
					note.addMouseMotionListener(canvas);
					parent.add(note);
					measure.addNote(note, 0);
					measure.setInstrument(instrument);
				}
				//��ϰģʽ��С�������ƽ���
				else{
					measure = new Measure(measurePart.getMeasure(j).getClefType(), 
							measurePart.getMeasure(j).getKeyValue());
					measure.setInstrument(instrument);
				}
				measure.addMouseListener(canvas);			
				//ʵ��
				parent.add(measure);
				//�߼���ϵ
				meaPart.addMeasure(measure);
			}
			Barline barline = new Barline(meaPart, "regular");
			parent.add(barline);
			barline.addMouseListener(canvas);
			barline.addMouseMotionListener(canvas);
			line.getMeaPartList().add(index + 1, meaPart);
			meaPart.setNoteLine(line);	
		}	
		//���׷���ڸ���ĩβ
		if(line.getMeaPartList().indexOf(measurePart) == line.getMeaPartList().size() - 1 - num){
			//���measurePart�����һ��С���ߵ����ͣ���ԭ�������һ��С������Ϊregular����Ӻ�����һ��С������Ϊ֮ǰ��״̬
			String barlineType = measurePart.getBarline().getType();
			measurePart.getBarline().setType("regular");	
			line.getMeaPartList().get(line.getMeaPartList().size() - 1).getBarline().setType(barlineType);
		}
	}
	
	/**
	 * �����Ƴ�ĳ��С����,�������ڲ��߼���ϵ
	 * @param meaPart
	 * @return ���ɾ��֮���¸���Ϊ�գ��򷵻�true, ���򷵻�false
	 */
	public boolean deleteMeasure(MeasurePart meaPart){
		return deleteMeasure(meaPart, true);
	}

	/**
	 * ����ɾ��ĳ��С����
	 * @param meaPart 
	 * @param split �Ƿ��С������н��塣������壬��ɾ��С������С�ڣ�С��������֮��Ĺ�����ϵ������ֻɾ��С���������������й������������ڲ����ж����߼���ϵ��
	 *
	 *
	 */
	public boolean deleteMeasure(MeasurePart meaPart, boolean split){
		Page page = meaPart.getNoteLine().getPage();
		for(int j = meaPart.getMeasureNum() - 1; j >= 0; j--){
			Measure measure = meaPart.getMeasure(j);
			page.remove(measure);
			if(split)
				meaPart.removeMeasure(measure);
			for(int i = 0, in = measure.getVoiceNum(); i < in; i++){
				for(int x = measure.getNoteNum(i) - 1; x >= 0 ; x--){
					AbstractNote note = measure.getNote(x, i);
					page.deleteNote(note, true);
					if(split)
						measure.removeNote(note);
				}
			}
			measure.deleteMeaAttr();
		}
		page.remove(meaPart.getBarline());
		if(split)
			meaPart.setBarline(null);
		return removeMeasurePartFromLine(meaPart);
	}

	/**
	 * ����
	 */
	public void undo(){
		actionController.undo();
	}
	
	/**
	 * �س�
	 */
	public void redo(){
		actionController.redo();
	}
}

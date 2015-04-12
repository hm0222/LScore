package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.Tie;
import sjy.elwg.utility.MusicMath;

/**
 * ��ͨ������
 * ���������е���ֹ��������������������
 * Ϊ��������λ�ô����㣬ͨ������������λ������. �������б�������������ʵ����RealPitch.
 * �����Ĳ��������������
 * 1. �û��༭���������ɵ���������λ������.����������ʵ����,��Ϊ����ʵ���ߵ�ȷ����Ҫ������С�ڵ��׺ŵ�����.
 *    ��������ӵ�ĳ��С��ʱ��������measure.addNote(note)ʱ���������������ӵ�С��ȷ������ʵ����.��Ҳ
 *    �ǽ�Measure��noteList��װ�����ģ��������û�ֱ�Ӳ�����ԭ���û���С���������ֻ��ͨ��measure.addNote()
 *    ������������ͨ��measure.getNoteList().add(note).
 * 2. ���ݵ������, �����MusicXML�ļ��а��������ľ������ߣ����������캯��������λ�����ߣ������ʱ��Ҫ��
 *    ����������ת��Ϊλ������.
 * @author jingyuan.sun
 *
 */
public class Note extends AbstractNote implements Equalable{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 7L;
	
	public final static int FULL_HEAD_WIDTH = 17;
	public final static int NORMAL_HEAD_WIDTH = 12;
	public final static int HEAD_HEIGHT = 16;
	public final static int REST1_WIDTH = 12;
	public final static int REST1_HEIGHT = 18;
	public final static int REST2_WIDTH = 12; 
	public final static int REST2_HEIGHT = 16;
	public final static int REST4_WIDTH = 10; 
	public final static int REST4_HEIGHT = 40;
	public final static int REST8_WIDTH = 10; 
	public final static int REST8_HEIGHT = 40;
	public final static int REST16_WIDTH = 15;
	public final static int REST16_HEIGHT = 40;
	public final static int REST32_WIDTH = 15;
	public final static int REST32_HEIGHT = 40;
	public final static int REST64_WIDTH = 15;
	public final static int REST64_HEIGHT = 40;
	public final static int REST128_WIDTH = 15;
	public final static int REST128_HEIGHT = 40;
	public final static int ORNAMENT_HEIGHT = 10;
	
	/**
	 * ����,������������һ������Ϊ0,ÿ�����������1���������ڵ�һ������Ϊ8.
	 */
	protected int pitch;
	/**
	 * �Ƿ���ֹ��
	 */
	protected boolean isRest;
	/**
	 * �Ƿ�ȫ����.ͨ����isRest����ʹ�����ж��Ƿ���ȫ��ֹ��.
	 */
	protected boolean isFull;
	/**
	 * ��������ʵ��
	 */
	protected SharpOrFlat sharpOrFlat;

	/**
	 * ��ʵ����
	 */
	protected RealPitch realPitch;
	/**
	 * ������λ�ó���������ʱ����������ָʾ����λ�õĶ���.
	 * ��list���浱ǰ�����Ķ��ż���.
	 */
	private ArrayList<JPanel> positionLines = new ArrayList<JPanel>();
	/**
	 * �����������ĺ�������,û����Ϊ��
	 */
	private ChordNote chordNote;
	/**
	 * ����ʵ��
	 */
	private UIDot uiDot;
	/**
	 * ������. �������������ص����������ߣ�ͨ��������������һ���Ը�������Ϊ��㣬��һ���Ը�������Ϊ�յ�.
	 */
	private ArrayList<Tie> ties = new ArrayList<Tie>();
	/**
	 * �Ƿ�ѡ��
	 */
	protected boolean selected;
	/**
	 * �Ƿ����أ���ҪӦ���ڸ�������ֹ��.
	 * ���ҽ���������Ϊ��ֹ�����Ҳ�����С�ڵ���������ͬʱ������Ϊtrueʱ�������ᱻ����.
	 * �����׵��߼�����ʱ����������Ȼ�ᱻ��������
	 */
	private boolean isHidden;
	/**
	 * ��ע���ϣ����жԸ��������еı�ע
	 */
	private ArrayList<Annotation> annotations = new ArrayList<Annotation>();
	
	/**
	 * �չ��캯��.
	 * ע�⣺������XML�ļ�֮�⣬�����κ�ʱ��Ӧ�ñ���ʹ�ÿչ��캯����
	 */
	public Note(){
		super(64);
		setOpaque(false);
		setLayout(null);
		adjSize();
		setFocusable(true);
	}
	
	/**
	 * ��ͨ�������������캯��
	 * @param duration ʱ��
	 * @param pitch ����
	 * @param dotNum �������
	 */
	public Note(int duration, int pitch, int dotNum){
		super(duration);
		this.pitch = pitch;
		this.dotNum = dotNum;
		beamType = duration < 64 ? "default" : "none";
		setOpaque(false);
		setLayout(null);
		adjSize();
		repaint();
		setFocusable(true);
		generateUIDot();
	}
	
	/**
	 * ��ͨ���������������캯��
	 * @param duration ʱ��
	 * @param pitch ����
	 */
	public Note(int duration, int pitch){
		super(duration);
		this.pitch = pitch;
		beamType = duration < 64 ? "default" : "none";
		setOpaque(false);
		setLayout(null);
		adjSize();
		repaint();
		setFocusable(true);
	}
	
	/**
	 * ��������ֹ�����캯��
	 * @param duration ʱ��
	 * @param dotNum �������
	 * @param isFull �Ƿ�ȫ��ֹ��
	 */
	public Note(int duration, int dotNum, boolean isFull){
		super(duration);
		this.dotNum = dotNum;
		this.isFull = isFull;
		isRest = true;
		beamType = "none";
		setOpaque(false);
		setLayout(null);
		adjSize();
		repaint();
		setFocusable(true);
		generateUIDot();
	}
	
	/**
	 * ����������ֹ�����캯��
	 * @param duration ʱ��
	 * @param isFull �Ƿ�ȫ��ֹ��
	 */
	public Note(int duration, boolean isFull){
		super(duration);
		this.isFull = isFull;
		isRest = true;
		beamType = "none";
		setOpaque(false);
		setLayout(null);
		adjSize();
		repaint();
		setFocusable(true);
	}
	
	/**
	 * ����ʱ��.
	 * ����ʱ����˵�����丸�� AbstractNote
	 */
	public void setDuration(int duration){
		super.setDuration(duration);
		adjSize();
		repaint();
	}
	
	/**
	 * ���λ������
	 * @return
	 */
	public int getPitch() {
		return pitch;
	}

	/**
	 * ����λ������
	 * @param pitch
	 */
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	/**
	 * �Ƿ�����ֹ��
	 * @return
	 */
	public boolean isRest() {
		return isRest;
	}

	/**
	 * ������ֹ��
	 * @param isRest
	 */
	public void setRest(boolean isRest) {
		this.isRest = isRest;
		beamType = "none";
		adjSize();
		repaint();
	}

	/**
	 * �����Ƿ�ȫ��ֹ��
	 * @return
	 */
	public boolean isFull() {
		return isFull;
	}

	/**
	 * ����ȫ��ֹ��
	 * @param isFull
	 */
	public void setFull(boolean isFull) {
		this.isFull = isFull;
		adjSize();
		repaint();
	}

	/**
	 * ����������С
	 */
	public void adjSize(){
		if(!isRest){
			if(duration == 256) 
				setSize(FULL_HEAD_WIDTH, HEAD_HEIGHT);
			else setSize(NORMAL_HEAD_WIDTH, HEAD_HEIGHT);
		}else{
			switch(duration)
			{
			case 256: setSize(REST1_WIDTH, REST1_HEIGHT); break;
			case 128: setSize(REST2_WIDTH, REST2_HEIGHT); break;
			case 64: setSize(REST4_WIDTH, REST4_HEIGHT); break;
			case 32: setSize(REST8_WIDTH, REST8_HEIGHT); break;
			case 16: setSize(REST16_WIDTH, REST16_HEIGHT); break;
			case 8: setSize(REST32_WIDTH, REST32_HEIGHT); break;
			case 4: setSize(REST64_WIDTH, REST64_HEIGHT); break;
			case 2: setSize(REST128_WIDTH, REST128_HEIGHT); break;
			default:
				setSize(REST1_WIDTH, REST1_HEIGHT);
				System.err.println("Invalid durDiv!");
			}
		}
		if(dotNum == 1){
			setSize(getWidth()+4, getHeight());
		}else if(dotNum == 2){
			setSize(getWidth()+6, getHeight());
		}
	}
	
	public void paintComponent(Graphics gg){
		super.paintComponent(gg);
		//������
		if(voice > 0 && isHidden && isRest){
			return;
		}
		Graphics2D g = (Graphics2D) gg;	
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    	renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g.setRenderingHints(renderHints);
		g.setFont(NoteCanvas.MCORE_FONT.deriveFont(35f));
		int voice = chordNote == null ? this.voice : chordNote.getVoice();
		if(selected && voice == 0) 
			g.setColor(Color.blue);
		else if(selected && voice == 1)
			g.setColor(Color.green);
		else 
			g.setColor(Color.black);
		if(!isRest){
			if(duration == 256){
				g.drawString("\uE12B", 0, 8);
			}else if(duration == 128){
				g.drawString("\uE12C", 0, 8);
			}else{
				g.drawString("\uE12D", 0, 8);
			}
		}else{
			if(isFull){
				g.drawString("\uE101", 0, 5);
			}else if(duration == 128){
				g.drawString("\uE100", 0, 4);
			}else if(duration == 64){
				g.drawString("\uE107", 0, 20);
			}else if(duration == 32){
				g.drawString("\uE109", 0, 20);
			}else if(duration == 16){
				g.drawString("\uE10A", 3, 20);
			}else if(duration == 8){
				g.drawString("\uE10B", 3, 20);
			}else if(duration == 4){
				g.drawString("\uE10C", 3, 20);
			}else if(duration == 2){
				g.drawString("\uE10D", 3, 20);
			}else{
				g.drawString("\uE101", 0, 5);
			}
		}
		/*if(dotNum > 0){
			if(dotNum == 1){
				g.fillPolygon(new int[]{getWidth()-3, getWidth()-2, getWidth()-1, getWidth()-1, getWidth()-2, getWidth()-3}, new int[]{getHeight()/2-2, getHeight()/2-2, getHeight()/2-2, getHeight()/2, getHeight()/2, getHeight()/2}, 6);
			}
			else if(dotNum == 2){
				g.fillPolygon(new int[]{getWidth()-6, getWidth()-5, getWidth()-4, getWidth()-4, getWidth()-5, getWidth()-6}, new int[]{getHeight()/2-2, getHeight()/2-2, getHeight()/2-2, getHeight()/2, getHeight()/2, getHeight()/2}, 6);
				g.fillPolygon(new int[]{getWidth()-3, getWidth()-2, getWidth()-1, getWidth()-1, getWidth()-2, getWidth()-3}, new int[]{getHeight()/2-2, getHeight()/2-2, getHeight()/2-2, getHeight()/2, getHeight()/2, getHeight()/2}, 6);
			}
		}*/
	}
	
	/**
	 * ���߼���ɾ�������������еķ���
	 * @param withTup �Ƿ�ɾ��������
	 */
	public void removeAllSymbols(boolean withTup){
		super.removeAllSymbols(withTup);
		
		if(sharpOrFlat != null){
			sharpOrFlat.setNote(null);
			sharpOrFlat = null;
		}

		
		if(!ties.isEmpty()){
			for(int i = 0; i < ties.size(); i++){
				Tie tie = ties.get(i);
				
				Note startNote = tie.getStartNote();
				Note endNote = tie.getEndNote();
				if(startNote != null){
					startNote.removeTie(tie);
					tie.setStartNote(null);
				}
				if(endNote != null){
					endNote.removeTie(tie);
					tie.setEndNote(null);
				}
				//ǰһ��Ƭ��
				Tie preTie = (Tie)tie.getPreSymbolLine();
				if(preTie != null){
					Note snote = preTie.getStartNote();
					Note enote = preTie.getEndNote();
					if(snote != null){
						snote.removeTie(preTie);
						preTie.setStartNote(null);
					}
					if(enote != null){
						enote.removeTie(preTie);
						preTie.setEndNote(null);
					}
					preTie.setNextSymbolLine(null);
					tie.setPreSymbolLine(null);
				}
				//��һ��Ƭ��
				Tie nxtTie = (Tie)tie.getNextSymbolLine();
				if(nxtTie != null){
					Note snote = nxtTie.getStartNote();
					Note enote = nxtTie.getEndNote();
					if(snote != null){
						snote.removeTie(nxtTie);
						nxtTie.setStartNote(null);
					}
					if(enote != null){
						enote.removeTie(nxtTie);
						nxtTie.setEndNote(null);
					}
					nxtTie.setPreSymbolLine(null);
					tie.setNextSymbolLine(null);
				}
			}
		}
	}
     
	
	@Override
	public void locateNote(int x) {
		// TODO Auto-generated method stub
		if(isRest){
//			setLocation(x, measure.getY() + 2*NoteCanvas.LINE_GAP - getHeight()/2);
			setLocation(x, measure.getY() + 2*NoteCanvas.LINE_GAP - 
					NoteCanvas.LINE_GAP*pitch/2 - getHeight()/2);
		}else{
			setLocation(x, measure.getY() + 4*NoteCanvas.LINE_GAP - 
					NoteCanvas.LINE_GAP*pitch/2 - getHeight()/2);
		}
		refreshPosLines();
	}
	
	public int sharpOrFlatWidth(){
		return sharpOrFlat == null ? 0 : sharpOrFlat.getWidth();
	}
	
	public int dotWidth(){
		return uiDot == null ? 0 : uiDot.getWidth();
	}
	
	public boolean absHigherThan(AbstractNote note){
		if(note instanceof Note){
			Note nnote = (Note)note;
			return pitch > nnote.getPitch();
		}
		else if(note instanceof ChordNote){
			ChordNote cnote = (ChordNote)note;
			for(Note n : cnote.getNoteList()){
				if(pitch <= n.getPitch())
					return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean absLowerThan(AbstractNote note){
		if(note instanceof Note){
			Note nnote = (Note)note;
			return pitch < nnote.getPitch();
		}
		else if(note instanceof ChordNote){
			ChordNote cnote = (ChordNote)note;
			for(Note n : cnote.getNoteList()){
				if(pitch >= n.getPitch())
					return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * ������������ʵ���ߣ�����λ������
	 */
	public void determinePitchByRealPitch(String clefType){
		int pitch = MusicMath.getNotePitchByRealPitch(realPitch.getOctave(), realPitch.getStep(), clefType);
		this.pitch = pitch;
	}
	
	/**
	 * ��������λ�������Լ�����С�ڵ���Ϣȷ��������ʵ������
	 * �÷�����������������ӽ�С��֮�����
	 */
	public void determineRealPitch(){
		Measure tmeasure = chordNote == null ? measure : chordNote.getMeasure();
		if(tmeasure == null)
			return;
		
		int octave = MusicMath.getOctave(this, tmeasure);
		int alter = MusicMath.getAlter(this, tmeasure);
		String step = MusicMath.getStep(this, tmeasure);
		
		if(realPitch == null){
			realPitch = new RealPitch(octave, step, alter);
		}else{
			realPitch.setAlter(alter);
			realPitch.setOctave(octave);
			realPitch.setStep(step);
		}
	}
	
	/**
	 * ����С�ڻ����������������ԣ�����������ʵ�����е�Alterֵ
	 */
	public void resetAlter(){
		Measure tmeasure = chordNote == null ? measure : chordNote.getMeasure();
		int alter = MusicMath.getAlter(this, tmeasure);
		if(realPitch != null){
			realPitch.setAlter(alter);
		}
	}

	/**
	 * �����������ʵ������Ϣ�������˶ȡ����׼����
	 * @return
	 */
	public RealPitch getRealPitch() {
		return realPitch;
	}

	/**
	 * ������ʵ����,�����˶ȡ����׼����
	 * @param realPitch
	 */
	public void setRealPitch(RealPitch realPitch) {
		this.realPitch = realPitch;
	}

	/**
	 * �����������ʵ��
	 * @return
	 */
	public SharpOrFlat getSharpOrFlat() {
		return sharpOrFlat;
	}

	/**
	 * ���������ŵ�ʵ��
	 * @param sharpOrFlat
	 */
	public void setSharpOrFlat(SharpOrFlat sharpOrFlat) {
		this.sharpOrFlat = sharpOrFlat;
	}
	
	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	/**
	 * ���������ĸ��ַ���
	 */
	@Override
	public void locateNoteSymbols() {
		// TODO Auto-generated method stub
		super.locateNoteSymbols();
		// ������
		if (sharpOrFlat != null) {
			sharpOrFlat.setLocation(getX() - sharpOrFlat.getWidth() + 2, getY()
					- sharpOrFlat.getHeight() / 3);
		}
		
		// ����Ǻ���װ����
		if(ornaments.size() != 0){
			//�������˵�������
			int sy = getStem() == null ? getY() : getStem().getY();
			int uy1 = getY() - 2 - (8 - pitch) * 5;
			int dy1 = getY() + NoteCanvas.LINE_GAP + 2 + (pitch - 1) * 5;
			int uy2 = getY() - 3 - (8 - pitch) * 5;
			int dy2 = getY() + NoteCanvas.LINE_GAP + 1 + pitch * 5;
			int uy3 = getY() - 10;
			int dy3 = getY() + 10 + 2;
			for (int i = 0, n = ornaments.size(); i < n; i++) {
				NoteSymbol nsl = ornaments.get(i);
				int dragY = nsl.getDraggedY();
				// ��������2��6֮��ʱ������staccato����ķ��ų������������Ϸ����·��Ĺ̶�λ��
				if (pitch >= 2 && pitch <= 6) {
					// ��������3��5֮��ʱ�����������������������ϵ�λ�õ�ȷ����staccato���ž�����������Ҫ��������
					//�������˵ķ���Ҳ��������ŵķ���
					if (pitch >= 3 && pitch <= 5) {
	                     //��staccato����ʱ���÷���Ӧ�÷�����������
						if (nsl.getSymbolType().equals("staccato")) {
							if (sy > getY()) {
								nsl.setLocation(getX(), this.getMeasure().getY() + dragY);
							} else if (sy < getY()) {
								nsl.setLocation(getX(), this.getMeasure().getY() + 30 + dragY);
							}
						}else if(nsl.getSymbolType().equals("staccatoTenutoUp")||nsl.getSymbolType().equals("staccatoTenutoDown")){
							if (sy > getY()) {
								nsl.setLocation(getX(), uy1 + dragY - 7);
								uy1 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX(), dy1  + dragY);
									dy1 += 10;
							}
						}else if(nsl.getSymbolType().equals("fermata")){
							if (sy > getY()) {
								nsl.setLocation(getX() - 3, uy1 + dragY - 9);
								uy1 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX() - 3, dy1  + dragY);
									dy1 += 10;
							}
						}else {
							if (sy > getY()) {
								nsl.setLocation(getX(), uy1 + dragY);
								uy1 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX(), dy1  + dragY);
									dy1 += 10;
							}
						}
					} else {
						//����Ϊ2����6ʱ
						if(nsl.getSymbolType().equals("staccatoTenutoUp")||nsl.getSymbolType().equals("staccatoTenutoDown")){
							if (sy > getY()) {
								nsl.setLocation(getX(), uy2 + dragY - 7);
								uy2 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX(), dy2 + dragY);
									dy2 += 10;
							}
						}else if(nsl.getSymbolType().equals("fermata")){
							if (sy > getY()) {
								nsl.setLocation(getX() - 3, uy2 + dragY - 9);
								uy2 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX() - 3, dy2 + dragY);
									dy2 += 10;
							}	
						}else{
							if (sy > getY()) {
								nsl.setLocation(getX(), uy2 + dragY);
								uy2 -= 10;
							} else if (sy < getY()) {
									nsl.setLocation(getX(), dy2 + dragY);
									dy2 += 10;
							}
						}
					}
				}else{
					//����С��2���ߴ���1ʱ
					if(nsl.getSymbolType().equals("staccatoTenutoUp")||nsl.getSymbolType().equals("staccatoTenutoDown")){
						if (sy > getY()) {
							nsl.setLocation(getX(), uy3 - 7);
							uy3 -= 10;
						} else if (sy < getY()) {
							nsl.setLocation(getX(), dy3+ 2);
							dy3 += 10;							
						}
					}else if(nsl.getSymbolType().equals("fermata")){
						if (sy > getY()) {
							nsl.setLocation(getX() - 3, uy3 - 9);
							uy3 -= 10;
						} else if (sy < getY()) {
							nsl.setLocation(getX() - 3, dy3+ 2);
							dy3 += 10;							
						}
					}else{
						if (sy > getY()) {
							nsl.setLocation(getX(), uy3);
							uy3 -= 10;
						} else if (sy < getY()) {
							nsl.setLocation(getX(), dy3+ 2);
							dy3 += 10;							
						}
					} 
				}
			}
		}


		//���ȼǺ�
		if(dynamics.size() != 0){
			
			for(int i = 0, n = dynamics.size(); i < n; i++){
				NoteSymbol nsl = dynamics.get(i);
				int dragX = nsl.getDraggedX();
				int dragY = nsl.getDraggedY();
				if(getPitch() >= 10){
					nsl.setLocation(getX() + dragX -25,
							getY()- 15 + dragY);
				}else{
					nsl.setLocation(getX() + dragX - 10,
							measure.getY() - 30 + dragY);
				}
			}
		}
		//��������
		if(performanceSymbols.size() != 0){
			for(int i = 0, n = performanceSymbols.size(); i < n; i++){
				NoteSymbol nsl = performanceSymbols.get(i);
				int dragX = nsl.getDraggedX();
				int dragY = nsl.getDraggedY();
				if(getPitch() >= 10){
					nsl.setLocation(getX() + dragX -25,
							getY()- 15 + dragY);
				}else{
					nsl.setLocation(getX() + dragX - 10,
							measure.getY() - 30 + dragY);
				}
			}
		}
		//װ��������
		if(graceSymbols.size() != 0){	
			for(int i = 0, n = graceSymbols.size(); i < n; i++){
				NoteSymbol nsl = graceSymbols.get(i);
				int dragY = nsl.getDraggedY();
				String gType = nsl.getSymbolType();
				if(gType.equalsIgnoreCase("trill-sharp")||gType.equalsIgnoreCase("trill-flat")||gType.equalsIgnoreCase("trill-natural")){
					nsl.setLocation(getX()- 2, this.getMeasure().getY() - 30 + dragY);
				}else{
					nsl.setLocation(getX()- 2, this.getMeasure().getY() - 15 + dragY);
				}
			
//					} else if (getStem().getY()< getY()) {
//						nsl.setLocation(getX()- 2, this.getMeasure().getY() - 10 + dragY);
//					}
//				}else{
//					if (getStem().getY() > getY()) {
//						nsl.setLocation(getX()- 2, getY()-10 + dragY);
//					} else if (getStem().getY()< getY()) {
//						nsl.setLocation(getX()- 2, getY() + 10 + dragY);
//					}
//				}
			}
		}
		
		//�����Ǻ�
		if(breath != null){
			int dragX = breath.getDraggedX();
			int dragY = breath.getDraggedY();
			if(this.getY()<this.getMeasure().getY()){
				breath.setLocation(getX()+12+ dragX, getY()-25+ dragY);
			}else{
				breath.setLocation(getX()+12+ dragX,this.getMeasure().getY()-29+ dragY);
			}
		
	
		}
		
		if(pedal != null){
			int dragX = pedal.getDraggedX();
			int dragY = pedal.getDraggedY();
			if(this.getPitch() > 2){
				pedal.setLocation(getX()+ dragX, this.getMeasure().getY()+40+ dragY);
			}else{
				pedal.setLocation(getX()+ dragX,getY()+15+ dragY);
			}
		}
		//����
		if(uiDot != null){
			if(pitch % 2 == 0)
				uiDot.setLocation(getX() + getWidth(), getY());
			else 
				uiDot.setLocation(getX() + getWidth(), getY() + NoteCanvas.LINE_GAP/2);
		}
		//���
		if(lyrics.size() != 0){
			Measure measure = chordNote == null ? this.measure : chordNote.getMeasure();		
			int yy = measure.getY() + Measure.MEASURE_HEIGHT + NoteLine.MEASURE_GAP/3;
			for(int i = 0; i < lyrics.size(); i++){
				if(lyrics.get(i) != null){
					int xx = getX() - (lyrics.get(i).getWidth() - getWidth()) / 2;
					lyrics.get(i).setLocation(xx + lyrics.get(i).draggedX, yy + lyrics.get(i).draggedY);
				}
				yy += Lyrics.LYRIC_FONT.getSize() + 2;
			}
		}
		//������
		if(tuplet != null){
			tuplet.locateTuplet();
		}
		//ע��
		if(!annotations.isEmpty()){
			for(Annotation an : annotations){
				if(an.getRelatedObjts().get(0) == this){
					an.setLocation(getX() + getWidth() + an.getDraggedX(),
							getY() - an.getHeight() - NoteCanvas.LINE_GAP + an.getDraggedY());
				}
			}
		}
		//�ڷ����ϵĲ���
		if (tremoloBeam != null) {
			if (this.getDuration() == 256) {
				tremoloBeam.setLocation(getX() + 3, getY() - 10);
			} else {
				if (getStem().getY() > getY()) {
					if (tremoloBeam.getSymbolType().equals("tremoloBeam1")) {
						tremoloBeam.setLocation(getX() - getWidth() / 2 + 2, getStem().getY() + getStem().getHeight() / 2);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam2")) {
						tremoloBeam.setLocation(getX() - getWidth() / 2 + 2, getStem().getY() + getStem().getHeight() / 2 - 3);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam3")) {
						tremoloBeam.setLocation(getX() - getWidth() / 2 + 2, getStem().getY() + getStem().getHeight() / 2 - 6);
					}
				} else if (getStem().getY() < getY()) {
					if (tremoloBeam.getSymbolType().equals("tremoloBeam1")) {tremoloBeam.setLocation(getX() + getWidth() / 2 + 1,
								getStem().getY() + getStem().getHeight() / 2);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam2")) {
						tremoloBeam.setLocation(getX() + getWidth() / 2 + 1,
								getStem().getY() + getStem().getHeight() / 2 - 3);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam3")) {
						tremoloBeam.setLocation(getX() + getWidth() / 2 + 1, getStem().getY() + getStem().getHeight() / 2 - 6);
					}
				}
			}
		}

	}
	
	/**
	 * �Ƿ�����һ��������ͬ
	 */
	public boolean equalsWith(Object o){
		if(!(o instanceof Note)){
			System.out.println("��������");
			return false;
		}
		Note note = (Note)o;
		boolean basicSame1 = note.getPitch()==pitch && note.getDuration()==duration && note.isRest()==isRest &&
		      note.getDotNum()==dotNum && note.hasSharpOrFlat()==hasSharpOrFlat();
		if(basicSame1){
			boolean basicSame2 = false;
			if(note.hasSharpOrFlat()){
				basicSame2 = note.getSharpOrFlat().getType().equals(sharpOrFlat.getType());
				if(!basicSame2){
					System.out.println("���������Ͳ�һ��");
					return false;
				}
			}
			return true;
		}
		System.out.println(note.getPitch() + " " + note.getDuration() + " " + note.isRest());
		System.out.println("basicSame1Ϊfalse");
		return false;
	}
	

	/**
	 * �Ƿ����������
	 * @return
	 */
	public boolean hasSharpOrFlat(){
		return !(sharpOrFlat == null);
	}
	
	/**
	 * ˢ�������ĸ�����
	 */
	public void refreshPosLines(){
		Measure tmeasure = chordNote == null ? measure : chordNote.getMeasure();
		refreshPosLines(tmeasure);
	}
	
	/**
	 * ˢ������������
	 * @param measure
	 */
	public void refreshPosLines(Measure measure){
		//���ָ��С�ڵ�����
		int pitch = Math.round((measure.getY() + Measure.MEASURE_HEIGHT - (getY() + getHeight()/2)) * 2 / NoteCanvas.LINE_GAP);
		
		if((pitch <= 8 && pitch >= 0 && positionLines.isEmpty()) || isRest)
			return;
		
		if(!positionLines.isEmpty()){
			for(JPanel line : positionLines){
				JComponent parent = (JComponent)line.getParent();
				if(parent != null)
					parent.remove(line);
			}
			positionLines.clear();
		}
		
		if(pitch < 0 || pitch > 8){
			int num = pitch > 8 ? (pitch - 8)/2 : (0 - pitch)/2 + 1;
			boolean up = pitch > 8 ? true : false;
			for(int i = 0; i < num; i++){
				JPanel line = new ShortLine();
				positionLines.add(line);
				if(getParent() != null){
					((JComponent)getParent()).add(line);
				}
				if(measure == null)
					return;
				if(up)
					line.setLocation(getX() - 2, measure.getY() - (i + 1) * NoteCanvas.LINE_GAP);
				else
					line.setLocation(getX() - 2, measure.getY() + Measure.MEASURE_HEIGHT + (i + 1) * NoteCanvas.LINE_GAP - 1);
			}
		}
		
		((JComponent)getParent()).revalidate();
		((JComponent)getParent()).updateUI();
	}
	
	/**
	 * ɾ��������λ������
	 */
	public void deletePosLines(){
		if(!positionLines.isEmpty()){
			JComponent parent = (JComponent)positionLines.get(0).getParent();
			
			for(JPanel line : positionLines){
				if(parent != null)
					parent.remove(line);
			}
			positionLines.clear();
			parent.revalidate();
		}
	}
	
	
	
	/********************************************************************************/
	
	/**
	 * ����ʵ���࣬���ڶ����������ߵĿ͹�����.
	 * �����˶ȣ����ף����
	 * @author jingyuan.sun
	 *
	 */
	public class RealPitch{
		/**
		 * ����,��ЧֵΪ��C, D, E, F, G, A, B
		 */
		String step;
		/**
		 * ���,������ʾ����������ʾ��
		 */
		int alter;
		/**
		 * �˶�
		 */
		int octave;
		
		public RealPitch(int octave, String step, int alter){
			this.octave = octave;
			this.step = step;
			this.alter = alter;
		}
		
		public RealPitch(){
			octave = 4;
			step = "G";
			alter = 0;
		}

		/**
		 * �������
		 * @return
		 */
		public String getStep() {
			return step;
		}

		/**
		 * ��������
		 * @param step
		 */
		public void setStep(String step) {
			this.step = step;
		}

		/**
		 * ��ñ��
		 * @return
		 */
		public int getAlter() {
			return alter;
		}

		/**
		 * ���ñ��
		 * @param alter
		 */
		public void setAlter(int alter) {
			this.alter = alter;
		}

		/**
		 * ��ð˶�
		 * @return
		 */
		public int getOctave() {
			return octave;
		}

		/**
		 * ���ð˶�
		 * @param octave
		 */
		public void setOctave(int octave) {
			this.octave = octave;
		}
	}
	
	/******************************************************************/
	
	/**
	 * ָʾ����λ�õĶ�����
	 */
	private class ShortLine extends JPanel{
		
		/**
		 * ���к�
		 */
		private static final long serialVersionUID = -3408943048696549781L;

		public ShortLine(){
			super();
			setSize(Note.this.getWidth() + 4, 2);
			setOpaque(false);
			repaint();
		}
		
		public void paintComponent(Graphics gg){
			gg.drawLine(0, 0, getWidth(), 0);
		}
	}

	@Override
	public Note getHighestNote() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Note getLowestNote() {
		// TODO Auto-generated method stub
		return this;
	}
	
	public Note getNoteWithPitch(int pitch){
		if(!isRest && this.pitch == pitch)
			return this;
		return null;
	}
	
	/**
	 * ��д��ͨ�����Ļ��С�ڷ���
	 */
	public Measure getMeasure(){
		if(chordNote != null){
			return chordNote.getMeasure();
		}
		else{
			return super.getMeasure();
		}
	}

	/**
	 * ����������ں�������,���û�У��򷵻�null
	 * @return
	 */
	public ChordNote getChordNote() {
		return chordNote;
	}

	/**
	 * ���ú���
	 * @param chordNote
	 */
	public void setChordNote(ChordNote chordNote) {
		this.chordNote = chordNote;
	}
	
	/**
	 * ��ø���ʵ��
	 * @return
	 */
	public UIDot getUiDot() {
		return uiDot;
	}
	
	/**
	 * ���ø���
	 * @param uiDot
	 */
	public void setUiDot(UIDot uiDot) {
		this.uiDot = uiDot;
	}

	/**
	 * ���λ�ö��߼���
	 * @return
	 */
	public ArrayList<JPanel> getPositionLines() {
		return positionLines;
	}

	/**
	 * ��������
	 */
	public void generateUIDot(){
		if(dotNum != 0){
			if(this.uiDot == null){
				uiDot = new UIDot(dotNum);
			}
			else{
				uiDot.setDotNum(dotNum);
				uiDot.adjustSize();
				uiDot.repaint();
			}
		}
	}

	/**
	 * ���ر�ѡ��
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * ���ñ�ѡ��
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * ����û���ע
	 * @return
	 */
	public ArrayList<Annotation> getAnnotations() {
		return annotations;
	}

	/************************ ��ties���з�װ  **************************/
	/**
	 * ���������
	 */
	public void addTie(Tie tie){
		if(!ties.contains(tie)){
			ties.add(tie);
		}
	}
	
	/**
	 * ɾ��������
	 * @param tie
	 */
	public void removeTie(Tie tie){
		ties.remove(tie);
	}
	
	/**
	 * �������������(ͨ�����Ϊ2)
	 * @return
	 */
	public int getTieNum(){
		return ties.size();
	}
	
	/**
	 * ���������
	 * @param index
	 * @return
	 */
	public Tie getTie(int index){
		return ties.get(index);
	}
	
	/*
	 * �����߽������жϡ��ǵĻ�����ʱ������
	 */
	public boolean isEndOfTie() {
		if (ties.size() == 0) return false;
		for (Tie t : ties) {
			if (t.getEndNote() == this) return true;
		}
		return false;
	}
	/*****************************************************************/
	
//	public Stem getStem(){
//		if(chordNote == null){
//			return super.getStem();
//		}
//		else{
//			return chordNote.getStem();
//		}
//	}

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

}



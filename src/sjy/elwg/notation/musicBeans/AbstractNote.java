package sjy.elwg.notation.musicBeans;

import java.util.ArrayList;
import java.util.List;

import javax.rmi.CORBA.Tie;
import javax.swing.JComponent;
import javax.swing.JPanel;

import sjy.elwg.notation.musicBeans.symbolLines.Breath;
import sjy.elwg.notation.musicBeans.symbolLines.Dynamic;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.Ornament;
import sjy.elwg.notation.musicBeans.symbolLines.Pedal;
import sjy.elwg.notation.musicBeans.symbolLines.PerformanceSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.SymbolLine;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * �����ĸ���
 * ������Note����ͨ����������ң�ChordNote��
 * @author jingyuan.sun
 *
 */
public abstract class AbstractNote extends JPanel implements Selectable, Equalable{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 14L;
	/**
	 * ����ʱ��. ȫ����Ϊ256�� 2��Ϊ128�� 4��Ϊ64�� ��������...
	 */
	protected int duration = 64;
	/**
	 * �������
	 */
	protected int dotNum;
	/**
	 * ������������. ��"begin", "continue", "none", "default"����.
	 * Ĭ�϶���ʱ�����ڵ����ķ�����ʱ�����������Լ�������ֹ����ֵΪ"none".
	 * ����������Ĭ��ֵΪ"default".   "begin"��"continue"�����û��ֶ�����ʱ����.
	 */
	protected String beamType = "default";
	/**
	 * ����С���ڵ�����.
	 * �����ڱ���ӽ�С��ʱ���ñ������趨.
	 * Ŀǰ�������Ե��ױ�֧�����������������Чֵ��0����1
	 */
	protected int voice = -1;
	/**
	 * ��������С��
	 */
	protected Measure measure;
	/**
	 * ��������
	 */
	protected Beam beam;
	/**
	 * ��������
	 */
	protected Stem stem;
	/**
	 * ������β
	 */
	protected Tail tail;
	/**
	 * ���
	 */
	protected ArrayList<Lyrics> lyrics;
	/**
	 * ��������йص��������ż���
	 */
	protected ArrayList<SymbolLine> symbolLines;
	/**
	 * ���������������ż���.
	 */
	protected ArrayList<NoteSymbol> ornaments;
	/**
	 * ������ص����ȼǺ�
	 */
	protected ArrayList<NoteSymbol> dynamics;
	/**
	 * ������ص�װ�����Ǻţ�����������
	 */
	protected ArrayList<NoteSymbol> graceSymbols;
	/**
	 * ��������
	 */
	protected ArrayList<NoteSymbol> performanceSymbols;
	
	
	/**
	 * �ڷ����ϵĲ����Ǻ�
	 */
	protected TremoloBeam tremoloBeam;
	
	/**
	 * �����Ǻ�
	 */
	protected Breath breath;
	
	/**
	 * ̤��Ǻ�
	 */
	protected Pedal pedal;
	
	/**
	 * �����Ǻ�
	 */
	protected Tremolo tremolo;
	/**
	 * tie����
	 */
	protected ArrayList<Tie> ties;
	/**
	 * ������
	 */
	protected Tuplet tuplet;
	/**
	 * ������ߵ�����
	 */
	protected List<AbstractNote> leftGraces;
	/**
	 * �����ұߵ�����
	 */
	protected List<AbstractNote> rightGraces;
	/**
	 * �ٶȼǺ�
	 */
	protected TempoText tempoText;


	/**
	 * ���캯��
	 */
	public AbstractNote(int duration){
		super();
		this.duration = duration;
		symbolLines = new ArrayList<SymbolLine>();
		ornaments = new ArrayList<NoteSymbol>();
		dynamics = new ArrayList<NoteSymbol>();
		performanceSymbols = new ArrayList<NoteSymbol>();
		lyrics = new ArrayList<Lyrics>();
		leftGraces = new ArrayList<AbstractNote>();
		rightGraces = new ArrayList<AbstractNote>();
		graceSymbols = new ArrayList<NoteSymbol>();
	//	breath = new Breath();
	//	pedal = new Pedal(type)
	//	tremoloBeam = new TremoloBeam("no");
	}
	
	/**
	 * ���󷽷�
	 * �������ĺ������������
	 * @param x
	 */
	public abstract void locateNote(int x);
	
	/**
	 * �����ſ��.
	 * ���û���򷵻�0
	 * @return
	 */
	public abstract int sharpOrFlatWidth();
	
	/**
	 * ������
	 * ���û���򷵻�0
	 * @return
	 */
	public abstract int dotWidth();
	
	/**
	 * �����Ƿ���Ը�����һ������
	 * ��ν���Ը��ڣ���ָ��������ÿһ����������������һ������������һ��������
	 * @param note
	 * @return
	 */
	public abstract boolean absHigherThan(AbstractNote note);
	
	/**
	 * �����Ƿ���Ե�����һ������
	 * ��ν���Ե��ڣ���ָ��������ÿһ����������������һ������������һ��������
	 * @param note
	 * @return
	 */
	public abstract boolean absLowerThan(AbstractNote note);
	
	/**
	 * ������������
	 * ����ͨ����Ҫ��д�÷���
	 */
	public void locateNoteSymbols(){
		//����
		if(!leftGraces.isEmpty()){
			int x = getX();
			AbstractNote curNote = this;
			for(int i = leftGraces.size()-1; i >= 0; i--){
				AbstractNote grace = leftGraces.get(i);
				x -= MusicMath.shortestDistNeeded(grace, curNote, false);
				grace.locateNote(x);
				curNote = grace;
			}
			Controller.deleteBeamAndStem(leftGraces);
			Controller.drawBeamAndStem((JComponent)getHighestNote().getParent(), leftGraces);
			for(int i = 0; i < leftGraces.size(); i++){
				AbstractNote grace = leftGraces.get(i);
				grace.locateNoteSymbols();
			}
		}
		//�ٶȼǺ�
		if(tempoText != null){
			int y = measure.getY() - tempoText.getHeight() - 5;
			y = getHighestNote().getY() <= y ? getHighestNote().getY() - 10 : y;
			tempoText.setLocation(getX()+getWidth()/2-tempoText.getWidth()/2, y);
		}
	}
	

	/**
	 * ���󷽷�
	 * ����������͵�����.
	 * �������ͨ���������ر���������ң�����������͵�����
	 * @return
	 */
	public abstract Note getLowestNote();
	
	/**
	 * ���󷽷�
	 * ����������ߵ�����.
	 * �������ͨ���������ر���������ң�����������ߵ�����
	 * @return
	 */
	public abstract Note getHighestNote();
	
	/**
	 * ���ؾ���ָ�����ߵ�����
	 * �������ͨ��������������������ָ��ֵ��ͬ���򷵻���������
	 * ����Ǻ����������Һ����а���ָ�����ߵ��������򷵻ظ��ض�������
	 * ���򷵻�0.
	 * @param pitch
	 * @return
	 */
	public abstract Note getNoteWithPitch(int pitch);
	
	/**
	 * ���󷽷�
	 * ���ɸ���ʵ��
	 */
	public abstract void generateUIDot();
	
	public abstract boolean isRest();
	
	
	/**
	 * ���ؿ��Ƿ���ʱ������y���꣬������y�����y����Сֵ
	 * @return
	 */
	public int getYWithStem(){
		if(stem == null)
			return getHighestNote().getY();
		else{
			int ny = getHighestNote().getY();
			int sy = stem.getY();
			return ny > sy ? sy : ny;
		}
	}
	
	/**
	 * �����������ʵʱ�����������㡢��������
	 * @return
	 */
	public float getRealDuration(){
		if(tuplet == null)
			return (float)getDurationWithDot();
		else{
			int mod = tuplet.getModification();
			int nor = tuplet.getNormal();
			return (float)getDurationWithDot() * nor / mod;
		}
	}
	

	
	/**
	 * ���߼���ɾ��������ӵ�е����з���
	 * @param withTup �Ƿ����������
	 */
    public void removeAllSymbols(boolean withTup){
    	//���ݼǺ�
		for(NoteSymbol ns : ornaments){
			ns.setNote(null);
		}
		ornaments.clear();
		
		//���ȷ���
		for(NoteSymbol ns : dynamics){
			ns.setNote(null);
		}
		dynamics.clear();
		
		//��������
		for(NoteSymbol ns : performanceSymbols ){
			ns.setNote(null);
		}
		performanceSymbols.clear();
		//���
		for(Lyrics ly : lyrics){
			ly.setNote(null);
		}
		lyrics.clear();
		
		//�����Ǻ�
		
		if(tremoloBeam != null){
			tremoloBeam.setNote(null);
			tremoloBeam = null;
		}
		
		//װ�η���
		
		if(graceSymbols != null){
			for(NoteSymbol ns : graceSymbols){
				ns.setNote(null);
			}
			graceSymbols.clear();
		}
		
		//��������
		for(int i = symbolLines.size() - 1; i >= 0; i--){
			SymbolLine sl = symbolLines.get(i);
			//֮ǰ��Ƭ������
			SymbolLine preLine = (SymbolLine)sl.getPreSymbolLine();
			//֮���Ƭ������
			SymbolLine nxtLine = (SymbolLine)sl.getNextSymbolLine();
			
			//��ʼ����
			AbstractNote startNote = sl.getStartNote();
			AbstractNote endNote = sl.getEndNote();
			if(startNote == this){
				if(startNote != null){
					startNote.getSymbolLines().remove(sl);
					sl.setStartNote(null);
				}
				if(endNote != null){
					endNote.getSymbolLines().remove(sl);
					sl.setEndNote(null);
				}
				while(preLine != null){
					AbstractNote snote = preLine.getStartNote();
					AbstractNote enote = preLine.getEndNote();
					
				
						if(snote != null){
							snote.getSymbolLines().remove(preLine);
							preLine.setStartNote(null);
						}
						if(enote != null){
							enote.getSymbolLines().remove(preLine);
							preLine.setEndNote(null);
						}
					

					SymbolLine templine = (SymbolLine)preLine.getPreSymbolLine();
					if(templine != null){
						preLine.setPreSymbolLine(null);
						templine.setNextSymbolLine(null);
					}
					preLine = templine;
				}
				while(nxtLine != null){
					AbstractNote snote = nxtLine.getStartNote();
					AbstractNote enote = nxtLine.getEndNote();
			
						if(snote != null){
							snote.getSymbolLines().remove(nxtLine);
							nxtLine.setStartNote(null);
						}
						if(enote != null){
							enote.getSymbolLines().remove(nxtLine);
							nxtLine.setEndNote(null);
						}
					

					SymbolLine templine = (SymbolLine)nxtLine.getNextSymbolLine();
					if(templine != null){
						nxtLine.setNextSymbolLine(null);
						templine.setPreSymbolLine(null);
					}
					nxtLine = templine;
				}
			}



		}
		
		//������
		if(withTup && tuplet != null){
			for(int i = 0; i < tuplet.getNoteList().size(); i++){
				AbstractNote note = tuplet.getNoteList().get(i);
				if(note != this){
					tuplet.getNoteList().remove(note);
					note.setTuplet(null);
					note.removeAllSymbols(false);
				}
			}
		}
    }
	
	/**
	 * ������ǰ��������������.
	 * @param nsl
	 */
    public void addNoteSymbol(NoteSymbol nsl){
    	if(nsl instanceof Ornament){
    		ornaments.add(nsl);
    	}else if(nsl instanceof Dynamic){
    		dynamics.add(nsl);
    	}else if(nsl instanceof PerformanceSymbol){
    		performanceSymbols.add(nsl);
    	}else if(nsl instanceof TremoloBeam){
    		tremoloBeam = (TremoloBeam)nsl;
    	}else if(nsl instanceof GraceSymbol){
    		graceSymbols.add(nsl);
    	}else if(nsl instanceof TempoText){
    		tempoText = (TempoText)nsl;
    	}else if(nsl instanceof Breath){
    		breath = (Breath)nsl;
    	}else if(nsl instanceof Pedal){
    		pedal = (Pedal)nsl;
    	}
    	nsl.setNote(this);
    }
    
    /**
     * �Ƿ������ַ�������ָ����װ����/����Ǻ�
     * @param str �ַ�������
     * @return
     */
    public boolean hasOrnament(String str){
    	for(NoteSymbol nsl : ornaments){
    		if(nsl.getSymbolType().equalsIgnoreCase(str))
    			return true;
    	}
    	return false;
    }
    
	
    /**
     * ���ʱ��
     * @return
     */
	public int getDuration() {
		return duration;
	}

	/**
	 * ����ʱ��
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * ��ø������
	 * @return
	 */
	public int getDotNum() {
		return dotNum;
	}

	/**
	 * ���ø������
	 * @param dotNum
	 */
	public void setDotNum(int dotNum) {
		this.dotNum = dotNum;
	}

	/**
	 * ��÷�������
	 * @return
	 */
	public String getBeamType() {
		return beamType;
	}

	/**
	 * ���÷�������
	 * @param beamType
	 */
	public void setBeamType(String beamType) {
		this.beamType = beamType;
	}

	/**
	 * ���������С��
	 * @return
	 */
	public Measure getMeasure() {
		return measure;
	}

	/**
	 * ����С��
	 * @param measure
	 */
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	/**
	 * ��÷���ʵ��
	 * @return
	 */
	public Beam getBeam() {
		return beam;
	}

	/**
	 * ���÷���
	 * @param beam
	 */
	public void setBeam(Beam beam) {
		this.beam = beam;
	}

	/**
	 * ��÷���
	 * @return
	 */
	public Stem getStem() {
		return stem;
	}
	/**
	 * ��ø���
	 * @return
	 */

	/**
	 * ���÷���
	 * @param stem
	 */
	public void setStem(Stem stem) {
		this.stem = stem;
	}
	
	/**
	 * �����з��ˣ������Ƿ��ϣ��������Ϸ�ͷ���£�
	 */
	public boolean isStemUp(){
		if(getStem() != null){
			if(getStem().getY() < getY()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * ��÷�β
	 * @return
	 */
	public Tail getTail() {
		return tail;
	}

	/**
	 * ���÷�β
	 * @param tail
	 */
	public void setTail(Tail tail) {
		this.tail = tail;
	}
	
	/**
	 * ����������ż���
	 * @return
	 */
	public ArrayList<SymbolLine> getSymbolLines() {
		return symbolLines;
	}

	/**
	 * ���װ�η��ż���
	 * @return
	 */
	public ArrayList<NoteSymbol> getOrnaments() {
		return ornaments;
	}
	/**
	 * ���tie����
	 */	
	public ArrayList<Tie> getTies() {
		return ties;
	}
	/**
	 * ����tie����
	 */
	public void setTies(ArrayList<Tie> ties) {
		this.ties = ties;
	}

	/**
	 * ������ȼǺŵ����Ʒ���
	 * @return
	 */
//	public ArrayList<NoteSymbol> getDynamics() {
//		return dynamics;
//	}

	/**
	 * ��ø��
	 * @return
	 */
//	public ArrayList<Lyrics> getLyrics() {
//		return lyrics;
//	}
	
	/**
	 * ���������
	 */
	public Tuplet getTuplet() {
		return tuplet;
	}

	/**
	 * ����������
	 * @param tuplet
	 */
	public void setTuplet(Tuplet tuplet) {
		this.tuplet = tuplet;
	}
	
	/**
	 * �����ڷ����ϵĲ���
	 * @param tuplet
	 */
	public void setTremoloBeam(TremoloBeam tB) {
		this.tremoloBeam = tB;
	}
	
	/**
	 * ���ò���
	 * @param tuplet
	 */	
	public void setTremolo(Tremolo t) {
		this.tremolo = t;
	}
	
	


	/**
	 * ���������
	 * @return
	 */
	public List<AbstractNote> getLeftGraces() {
		return leftGraces;
	}

	/**
	 * ���������
	 * @return
	 */
	public List<AbstractNote> getRightGraces() {
		return rightGraces;
	}

	/**************************���������š����ȼǺš���ʵȽ��з�װ****************************/
	
	public void addOrnament(NoteSymbol ns){
		ornaments.add(ns);
		ns.setNote(this);
	}
	
	public void addDynamics(NoteSymbol ns){
		dynamics.add(ns);
		ns.setNote(this);
	}
	
	public void addPerformanceSymbols(NoteSymbol ns){
		performanceSymbols.add(ns);
		ns.setNote(this);
	}
	
	public void addBreath(Breath breath){
		this.breath = breath;
		breath.setNote(this);
	}
	
	public void addPedal(Pedal pedal){
		this.pedal = pedal;
	}
	
	public void addTremoloBeam(TremoloBeam tB){
		this.tremoloBeam = tB;
	}
	
	public void addGraceSymbol(NoteSymbol gs){
		graceSymbols.add(gs);
		gs.setNote(this);
	}
	
	public void addLyrics(Lyrics l){
		lyrics.add(l);
		if(l != null)
			l.setNote(this);
	}
	
	public void removeOrnament(NoteSymbol ns){
		ornaments.remove(ns);
		ns.setNote(null);
	}
	
	public void removeDynamics(NoteSymbol ns){
		dynamics.remove(ns);
		ns.setNote(null);
	}
	
	public void removePerformanceSymbols(NoteSymbol ns){
		performanceSymbols.remove(ns);
		ns.setNote(null);
	}
	
	public void removeTremoloBeam(){
		if(tremoloBeam != null){
			tremoloBeam.setNote(null);
			tremoloBeam = null;
		}
	}
	
	public void removeBreath(){
		if(breath != null){
			breath.setNote(null);
			breath = null;
		}
	}
	
	public void removePedal(){
		if(pedal != null){
			pedal.setNote(null);
			pedal = null;
		}
	}
	
	public void removeGraceSymbol(NoteSymbol ns){
		graceSymbols.remove(ns);
		ns.setNote(null);
	}
	
	public void removeLyrics(Lyrics l){
		//���һ�����
		int index = lyrics.indexOf(l);
		if(index == lyrics.size() - 1)
			lyrics.remove(l);
		else
			lyrics.set(index, null);
		l.setNote(null);
	}
	
	public int getOrnamentsNum(){
		return ornaments.size();
	}
	
	public int getDynamicsNum(){
		return dynamics.size();
	}
	
	public int getPerformanceSymbolsNum(){
		return performanceSymbols.size();
	}
	
	public int getGraceSymbolNum(){
		return graceSymbols.size();
	}
	
	
	public int getLyricsNum(){
		return lyrics.size();
	}
	
	public NoteSymbol getOrnament(int index){
		return ornaments.get(index);
	}
	
	public TremoloBeam getTremoloBeam(){
		return tremoloBeam;
	}
	
	public NoteSymbol getDynamics(int index){
		return dynamics.get(index);
	}
	
	public NoteSymbol getPerformanceSymbols(int index){
		return performanceSymbols.get(index);
	}
	
	public NoteSymbol getGraceSymbols(int index){
		return graceSymbols.get(index);
	}
	
	public Lyrics getLyrics(int index){
		return lyrics.get(index);
	}
	
	public void clearOrnaments(){
		ornaments.clear();
	}
	
	public void clearDynamics(){
		dynamics.clear();
	}
	
	public void clearPerformanceSymbol(){
		performanceSymbols.clear();
	}
	
	public void clearLyrics(){
		lyrics.clear();
	}
	
	public void setLyrics(int index, Lyrics ly){
		lyrics.set(index, ly);
	}
	
	/******************************************************************************************/

	
    /********************************�����������еĲ������з�װ*******************************/
	
	/**
	 * ���������
	 */
	public void addLeftGrace(AbstractNote g){
		leftGraces.add(g);
		g.setMeasure(measure);
		if(g instanceof Grace){
			((Grace)g).setNote(this);
			((Grace)g).determineRealPitch();
		}
		if(g instanceof ChordGrace){
			ChordGrace cgrace = (ChordGrace)g;
			cgrace.setNote(this);
			for(int i = 0; i < cgrace.getNoteNum(); i++){
				Note nnote = cgrace.getNote(i);
				nnote.determineRealPitch();
			}
		}
	}
	
	/**
	 * ���������
	 * @param index
	 * @param g
	 */
	public void addLeftGrace(int index, AbstractNote g){
		leftGraces.add(index, g);
		g.setMeasure(measure);
		if(g instanceof Grace){
			((Grace)g).setNote(this);
		}
		if(g instanceof ChordGrace){
			ChordGrace cgrace = (ChordGrace)g;
			cgrace.setNote(this);
			for(int i = 0; i < cgrace.getNoteNum(); i++){
				Note nnote = cgrace.getNote(i);
				nnote.determineRealPitch();
			}
		}
	}
	
	/**
	 * ���������
	 */
	public void addRightGrace(AbstractNote g){
		rightGraces.add(g);
		g.setMeasure(measure);
		if(g instanceof Grace){
			((Grace)g).setNote(this);
			((Grace)g).determineRealPitch();
		}
		if(g instanceof ChordGrace){
			ChordGrace cgrace = (ChordGrace)g;
			cgrace.setNote(this);
			for(int i = 0; i < cgrace.getNoteNum(); i++){
				Note nnote = cgrace.getNote(i);
				nnote.determineRealPitch();
			}
		}
		
	}
	
	/**
	 * ���������
	 * @param index
	 * @param g
	 */
	public void addRightGrace(int index, AbstractNote g){
		rightGraces.add(index, g);
		g.setMeasure(measure);
		if(g instanceof Grace){
			((Grace)g).setNote(this);
		}
		if(g instanceof ChordGrace){
			ChordGrace cgrace = (ChordGrace)g;
			cgrace.setNote(this);
			for(int i = 0; i < cgrace.getNoteNum(); i++){
				Note nnote = cgrace.getNote(i);
				nnote.determineRealPitch();
			}
		}
	}
	
	/**
	 * ɾ������
	 * @param g
	 */
	public void removeGrace(AbstractNote g){
		if(leftGraces.contains(g)){
			leftGraces.remove(g);
			if(g instanceof Grace)
				((Grace)g).setNote(null);
			else if(g instanceof ChordGrace)
				((ChordGrace)g).setNote(null);
			g.setMeasure(null);
			return;
		}
		else if(rightGraces.contains(g)){
			rightGraces.remove(g);
			if(g instanceof Grace)
				((Grace)g).setNote(null);
			else if(g instanceof ChordGrace)
				((ChordGrace)g).setNote(null);
			g.setMeasure(null);
			return;
		}
	}
	
	/******************************************************************************************/
	
	
	/**
	 * ���ϸ���֮�������ʱ��
	 * @return
	 */
	public int getDurationWithDot(){
		int dur = duration;
		if(dotNum ==1) 
			return dur + dur/2;
		else if(dotNum ==2)
			return dur + dur/2 + dur/4;
		else
			return duration;
	}
	
	/**
	 * ���ظ����ռ������,���û�и�ʣ��򷵻�0
	 * @return
	 */
	public int maxLyricWidth(){
		int max = 0;
		for(int i = 0; i < lyrics.size(); i++){
			Lyrics ly = lyrics.get(i);
			if(ly != null && ly.getWidth() > max)
				max = ly.getWidth();
		}
		return max;
	}
	
	/**
	 * ����������������������ռ�Ŀ��
	 * @param direction ����λ�ã���Чֵ�У�"left","right".�ֱ������������������
	 * @return
	 */
	public int shortestGraceWidth(String direction){
		List<AbstractNote> list = null;
		if(direction.equalsIgnoreCase("left"))
			list = leftGraces;
		else if(direction.equalsIgnoreCase("right"))
			list = rightGraces;
		if(list != null){
			int result = 0;
			for(int i = 0; i < list.size()-1; i++){
				AbstractNote grace = list.get(i);
				result += MusicMath.shortestDistNeeded(grace, list.get(i+1), false);
			}
			AbstractNote firstGrace = list.get(0);
			AbstractNote lastGrace = list.get(list.size()-1);
			result += firstGrace.sharpOrFlatWidth();
			result += lastGrace.dotWidth();
			return result;
		}
		return 0;
	}

	/**
	 * ����ٶȼǺ�
	 * @return
	 */
	public TempoText getTempoText() {
		return tempoText;
	}

	/**
	 * �����ٶȼǺ�
	 * @param tempoText
	 */
	public void setTempoText(TempoText tempoText) {
		this.tempoText = tempoText;
	}

	/**
	 * ��ú����Ǻ�
	 * @return
	 */
	public Breath getBreath() {
		return breath;
	}

	/**
	 * ���ú����Ǻ�
	 * @param breath
	 */
	public void setBreath(Breath breath) {
		this.breath = breath;
	}

	public Pedal getPedal() {
		return pedal;
	}

	public void setPedal(Pedal pedal) {
		this.pedal = pedal;
	}

	public int getVoice() {
		return voice;
	}

	public void setVoice(int voice) {
		this.voice = voice;
	}

}

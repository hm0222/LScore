package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * С��.�����ס������ĺŵ�����.
 * �������׽ṹû���������������������˶�������������ͨ��ÿ��С�ڵ���������������.������д��MIDI������Ϊ����.
 * ÿ��С�����еĶ��С�ڣ��������ڲ�ͬ������.���ΪС����������������ֶΡ�
 * ��������С������ͬ��һ�������������������ٵ��������������������´����Ե�һ�����������������ƣ����ڶ�������
 * ��Ϊnull. �������û�����֣���Ϊ"none"��������Ϊnull����������Ϊ����һ�������ķǵ�һ����.
 * 
 * С�ڷ�Ϊ���֣�����С���������ƽ���С��. ���ǵ�Ψһ��ͬ�����������ƽ���С��û���ĺŵĸ����С���ڿ���ʢ�ŵ���
 * ������û�����ơ������͵�С��ͨ����������ģʽ.
 * 
 * һ��С�����֧����������(voices)��һ����������һ�����������ֱ�Ϊ����1������2��������һ����Ϊ�գ�����������Ϊ��
 * ����������¶�Ϊ�գ�.�����������ͬʱ���ڣ�����˷����෴��Ĭ�����������ϣ����������¡�
 * 
 * ��С�����ɾ������������:measure.addNote(note)��measure.remove(note)
 * 
 * @author jingyuan.sun
 *
 */
public class Measure extends JPanel implements Selectable,Equalable{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 6L;
	/**
	 * С�ڸ߶�
	 */
	public static final int MEASURE_HEIGHT = 41;
	/**
	 * ����С��֧�ֵ���������
	 */
	public static final int VOICE_NUM = 2;
	/**
	 * ��������������
	 */
	private ArrayList<AbstractNote> noteList;
	/**
	 * ��������������
	 */
	private V2NoteList v2NoteList;
	/**
	 * ������С����
	 */
	private MeasurePart measurePart;
	
	/**
	 * �׺�����.��Чֵ�У�"g/2", "f/4",�ֱ��ʾ�����͵����׺�
	 */
	private String clefType;
	
	/**
	 * ����ֵ��������ʾ�������͡���ֵ��Χ��-7��7.������ʾ���ţ�������ʾ����.0��ʾ��ͨ��ʽ
	 */
	private int keyValue;
	
	/**
	 * �ĺ�����
	 */
	private Time time;
	
	/**
	 * �׺ţ��������ĺŵ�UIʵ��.
	 * ע�⣺UIʵ�岢��һ����С����Ӧ������ֵһһ��Ӧ.
	 */
	private UIClef uiClef;
	private UIKey uiKey;
	private UITime uiTime;
	
	/**
	 * �������֡���д��MIDI����.
	 * MIDI������MIDI�淶�����涨������Ƶ��ֵ��ͨ���ǰ��������֣������ȡ�ַ�����ʽ.
	 */
	private String partName;
	private String partAbrre;
	private String instrument;
	
	/**
	 * �Ƿ�ѡ��
	 */
	private boolean selected;
	
	/**
	 * С�ڱ�ѡ��ʱ�ķ���
	 */
	private JPanel selector;
	
	/**
	 * ��ע����
	 */
	private ArrayList<Annotation> annotations;
	
	
	/**
	 * ��ͨС�ڹ��캯��
	 */
	public Measure(String clefType, int keyValue, Time time){
		super();
		this.clefType = clefType;
		this.keyValue = keyValue;
		this.time = new Time(time.getBeats(), time.getBeatType());
		noteList = new ArrayList<AbstractNote>();
		v2NoteList = new V2NoteList();
		annotations = new ArrayList<Annotation>();
		setOpaque(false);
		setBackground(Color.yellow);
		setLayout(null);
	}
	
	/**
	 * ��ͨС����ͨ�ĺŹ��캯��
	 * @param clefType
	 * @param keyValue
	 * @param beats
	 * @param beatType
	 */
	public Measure(String clefType, int keyValue, int beats,int beatType){
		super();
		this.clefType = clefType;
		this.keyValue = keyValue;
		this.time = new Time(beats, beatType);
		noteList = new ArrayList<AbstractNote>();
		v2NoteList = new V2NoteList();
		annotations = new ArrayList<Annotation>();
		setOpaque(false);
		setBackground(Color.yellow);
		setLayout(null);
	}
	/**
	 * ��ͨС�ڻ���ĺŹ��캯��
	 * @param clefType
	 * @param keyValue
	 * @param beat
	 * @param beatType
	 */
	public Measure(String clefType, int keyValue, int[] beat,int beatType){
		super();
		this.clefType = clefType;
		this.keyValue = keyValue;
		this.time = new Time(beat, beatType);
		noteList = new ArrayList<AbstractNote>();
		v2NoteList = new V2NoteList();
		annotations = new ArrayList<Annotation>();
		setOpaque(false);
		setBackground(Color.yellow);
		setLayout(null);
	}
	

	/**
	 * �����ƽ���С�ڹ��캯��
	 * @param clefType
	 * @param keyValue
	 */
	public Measure(String clefType, int keyValue){
		super();
		this.clefType = clefType;
		this.keyValue = keyValue;
		noteList = new ArrayList<AbstractNote>();
		v2NoteList = new V2NoteList();
		annotations = new ArrayList<Annotation>();
		setOpaque(false);
		setBackground(Color.yellow);
		setLayout(null);
	}
	
	/**
	 * ����С�����ڵ�С����
	 * @return
	 */
	public MeasurePart getMeasurePart() {
		return measurePart;
	}

	/**
	 * ����С����
	 * @param measurePart
	 */
	public void setMeasurePart(MeasurePart measurePart) {
		this.measurePart = measurePart;
	}
	
	/**
	 * ����С�ڵ�ע�ͼ���
	 * @return
	 */
	public ArrayList<Annotation> getAnnotations() {
		return annotations;
	}
	
	/**
	 * ��ָ�������������
	 * @param note
	 * @param voiceNum
	 */
	private void addVoiceNote(AbstractNote note, int voiceNum){
		if(voiceNum == 0){
			noteList.add(note);
			note.setVoice(0);
		}
		else if(voiceNum == 1){
			v2NoteList.list.add(note);
			note.setVoice(1);
		}
		else{
			System.err.println("�޷��������,��������쳣: " + voiceNum);
			return;
		}
		note.setMeasure(this);
		makeRealPitch(note);
//		System.out.println("note voice: " + voiceNum);
	}
	
	/**
	 * ��ָ��������������
	 * @param index
	 * @param note
	 * @param voiceNum
	 */
	private void addVoiceNote(int index, AbstractNote note, int voiceNum){
		if(voiceNum == 0){
			noteList.add(index, note);
			note.setVoice(0);
		}
		else if(voiceNum == 1){
			v2NoteList.list.add(index, note);
			note.setVoice(1);
		}
		else{
			System.err.println("�޷��������,��������쳣: " + voiceNum);
			return;
		}
		note.setMeasure(this);
		makeRealPitch(note);
	}
	
	/**
	 * ����������������.
	 * ע�⣺�÷�������addVoiceNote()�����ڲ�����
	 * @param note
	 */
	private void makeRealPitch(AbstractNote note){
		if(note instanceof Note){
			Note nnote = (Note)note;
			if(!nnote.isRest())
				nnote.determineRealPitch();
		}
		else if(note instanceof ChordNote){
			ChordNote cnote = (ChordNote)note;
			for(int i = 0, n = cnote.getNoteNum(); i < n; i++){
				Note nnote = cnote.getNote(i);
				nnote.determineRealPitch();
			}
		}
	}
	
	/************  ���·�����noteList�ķ��������˷�װ,��������measure����ֱ�ӻ��noteList������  ****************/

	/**
	 * ��С��ָ������׷������
	 * @param note
	 */
	public void addNote(AbstractNote note, int voice){
		addVoiceNote(note, voice);
	}
	
	/**
	 * ��ָ������ָ��λ�ò�������
	 * @param index
	 * @param note
	 */
	public void addNote(int index, AbstractNote note, int voice){
		addVoiceNote(index, note, voice);
	}
	
	/**
	 * ����ָ��λ�õ�����
	 * @param index
	 * @return
	 */
	public AbstractNote getNote(int index, int voice){
		AbstractNote note = voice == 0 ? noteList.get(index) : v2NoteList.list.get(index);
		return note;
	}
	
	/**
	 * ɾ������
	 * @param index
	 */
	public void removeNote(int index, int voice){
		if(voice == 0){
			noteList.get(index).setMeasure(null);
			noteList.get(index).setVoice(-1);
			noteList.remove(index);
		}
		else if(voice == 1){
			v2NoteList.list.remove(index);
			v2NoteList.list.get(index).setVoice(-1);
			v2NoteList.list.get(index).setMeasure(null);
		}
	}
	
	/**
	 * ɾ������
	 * @param note
	 */
	public void removeNote(AbstractNote note){
		if(note.getVoice() == 0){
			note.setMeasure(null);
			noteList.remove(note);
			note.setVoice(-1);
		}else if(note.getVoice() == 1){
			note.setMeasure(null);
			v2NoteList.list.remove(note);
			note.setVoice(-1);
		}
	}
	
	/**
	 * ����ָ����������������
	 * ���ָ�������������ڣ��򷵻�-1.
	 * @return
	 */
	public int getNoteNum(int voice){
		return noteNumInVoice(voice);
	}
	
	/**
	 * ������������������Ŀ
	 * @return
	 */
	public int totalNoteNum(){
		return noteList.size() + v2NoteList.list.size();
	}
	
	/**
	 * ����ָ����������������
	 * ���ָ�������쳣���򷵻�-1.
	 * @param index
	 * @return
	 */
	private int noteNumInVoice(int index){
		return index == 0 ? noteList.size() : (index == 1 ? v2NoteList.list.size() : -1);
	}
	
	/**
	 * ����С��������λ��,������ָ�������򷵻�-1.
	 * @param note
	 * @return
	 */
	public int noteIndex(AbstractNote note){
		if(note.getVoice() == 0 || noteList.contains(note))
			return noteList.indexOf(note);
		else if(note.getVoice() == 1 || v2NoteList.list.contains(note))
			return v2NoteList.list.indexOf(note);
		else 
			return -1;
	}
	
	/****************************************************************************************/
	
	/**
	 * ��С�ڶ����������������ʱ������У�ÿ�����а������voice������������voiceΪС����������.
	 */
	public List<List<AbstractNote>> getNotesByTimeSlot(){
		List<List<AbstractNote>> result = new ArrayList<List<AbstractNote>>();
		//���������ۻ�ʱ��
		int voiceNum = v2NoteList.list.size() == 0 ? 1 : 2;
		int[] accumDur = new int[voiceNum];
		float[] faccumDur = new float[voiceNum];
		//��������ǰ�������
		int noteIndex1 = 0;
		int noteIndex2 = 0;
		while(true){
			List<AbstractNote> slot = new ArrayList<AbstractNote>();
			int accum = MusicMath.minValue(accumDur);
			if(accumDur[0] == accum){
				if(noteIndex1 < noteList.size()){
					AbstractNote note = noteList.get(noteIndex1++);
					slot.add(note);
					faccumDur[0] += note.getRealDuration();
					accumDur[0] = Math.round(faccumDur[0]);
				}
			}
			if(v2NoteList.list.size() != 0 && accumDur[1] == accum){
				if(noteIndex2 < v2NoteList.list.size()){
					AbstractNote note = v2NoteList.list.get(noteIndex2++);
					slot.add(note);
					faccumDur[1] += note.getRealDuration();
					accumDur[1] = Math.round(faccumDur[1]);
				}
			}
			if(!slot.isEmpty())
				result.add(slot);
			else if(noteIndex1 < noteList.size()){
				AbstractNote note = noteList.get(noteIndex1++);
				slot.add(note);
				faccumDur[0] += note.getRealDuration();
				accumDur[0] = Math.round(faccumDur[0]);
				result.add(slot);
			}
			else{
				return result;
			}
		}
	}
	
	/**
	 * ��getNotesByTimeSlot()�������ƣ�ֻ�ǰ�����Ԫ�����������仯
	 * @return
	 */
	public List<MeasurePart.NListWithMeaIndex> getMeasureSlots(){
		List<MeasurePart.NListWithMeaIndex> result = new ArrayList<MeasurePart.NListWithMeaIndex>();
		int meaIndex = measurePart.getMeasureList().indexOf(this);
		//���������ۻ�ʱ��
		int voiceNum = v2NoteList.list.size() == 0 ? 1 : 2;
		int[] accumDur = new int[voiceNum];
		float[] faccumDur = new float[voiceNum];
		//��������ǰ�������
		int noteIndex1 = 0;
		int noteIndex2 = 0;
		while(true){
			List<AbstractNote> slot = new ArrayList<AbstractNote>();
			int accum = MusicMath.minValue(accumDur);
			if(accumDur[0] == accum){
				if(noteIndex1 < noteList.size()){
					AbstractNote note = noteList.get(noteIndex1++);
					slot.add(note);
					faccumDur[0] += note.getRealDuration();
					accumDur[0] = Math.round(faccumDur[0]);
				}
			}
			if(v2NoteList.list.size() != 0 && accumDur[1] == accum){
				if(noteIndex2 < v2NoteList.list.size()){
					AbstractNote note = v2NoteList.list.get(noteIndex2++);
					slot.add(note);
					faccumDur[1] += note.getRealDuration();
					accumDur[1] = Math.round(faccumDur[1]);
				}
			}
			if(!slot.isEmpty()){
				MeasurePart.NListWithMeaIndex mn = measurePart.new NListWithMeaIndex(slot, meaIndex);
				result.add(mn);
			}
			else if(noteIndex1 < noteList.size()){
				AbstractNote note = noteList.get(noteIndex1++);;
				slot.add(note);
				faccumDur[1] += note.getRealDuration();
				accumDur[1] = Math.round(faccumDur[1]);
				MeasurePart.NListWithMeaIndex mn = measurePart.new NListWithMeaIndex(slot, meaIndex);
				result.add(mn);
			}else{
				return result;
			}
		}
	}
	
	
	/**
	 * ��ͼ
	 */
	public void paintComponent(Graphics g){
		for(int i = 0; i < 5; i++){
			g.drawLine(0, i * NoteCanvas.LINE_GAP, getWidth(), i * NoteCanvas.LINE_GAP);
		}
	}
	
	/**
	 * ����С���׺�
	 * @param x
	 */
	public void locateClef(int x){
		if(uiClef != null){
			uiClef.setLocation(x, getY() + uiClef.getPositionOffset());
		}
	}
	

	
	/**
	 * ����С���ĺ�
	 * @param x
	 */
	public void locateTime(int x){
		if(uiTime != null){
			uiTime.setLocation(x, getY() + uiTime.getPositionOffset());
		}
	}
	
	/**
	 * ����С�ڵ���
	 * @param x
	 */
	public void locateKey(int x){
		if(uiKey != null){
			uiKey.setLocation(x, getY() + uiKey.getPositionOffset());
		}
	}

	/**
	 * �����׺�����
	 * @return
	 */
	public String getClefType() {
		return clefType;
	}

	/**
	 * �����׺�����
	 * @param clefType
	 */
	public void setClefType(String clefType) {
		this.clefType = clefType;
	}

	/**
	 * ���ص�������
	 * @return
	 */
	public int getKeyValue() {
		return keyValue;
	}

	/**
	 * ���õ�������
	 * @param keyValue
	 */
	public void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * �����ĺ�ʵ��
	 * @return
	 */
	public Time getTime() {
		return time;
	}

	/**
	 * �����ĺ�ʵ��
	 * @param time
	 */
	public void setTime(Time time) {
		this.time = time;
	}

	/**
	 * �����׺�ʵ��
	 * @return
	 */
	public UIClef getUiClef() {
		return uiClef;
	}

	/**
	 * �����׺�ʵ��
	 * @param uiClef
	 */
	public void setUiClef(UIClef uiClef) {
		this.uiClef = uiClef;
	}

	/**
	 * ��õ���ʵ��
	 * @return
	 */
	public UIKey getUiKey() {
		return uiKey;
	}

	/**
	 * ���õ���ʵ��
	 * @param uiKey
	 */
	public void setUiKey(UIKey uiKey) {
		this.uiKey = uiKey;
	}

	/**
	 * �����ĺ�ʵ��
	 * @return
	 */
	public UITime getUiTime() {
		return uiTime;
	}

	/**
	 * �����ĺ�ʵ��
	 * @param uiTime
	 */
	public void setUiTime(UITime uiTime) {
		this.uiTime = uiTime;
	}

	/**
	 * ����С�����ڵ���������
	 * @return
	 */
	public String getPartName() {
		return partName;
	}

	/**
	 * ����С�����ڵ���������0
	 * @param partName
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}

	/**
	 * ����������д
	 * @return
	 */
	public String getPartAbrre() {
		return partAbrre;
	}

	/**
	 * ����������д
	 * @param partAbrre
	 */
	public void setPartAbrre(String partAbrre) {
		this.partAbrre = partAbrre;
	}

	/**
	 * ����MIDI����
	 * @return
	 */
	public String getInstrument() {
		return instrument;
	}

	/**
	 * ����MIDI����
	 * @param instrument
	 */
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	/**
	 * ����С�ڱ�ѡ��ʱ��ѡ���
	 * @return
	 */
	public JPanel getSelector() {
		return selector;
	}

	/**
	 * ����С��ѡ���
	 * @param selector
	 */
	public void setSelector(JPanel selector) {
		this.selector = selector;
	}

	/**
	 * ����С���Ƿ�ѡ��
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * ����С��ѡ��
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * �����׺ţ����ź��ĺŵĿ��֮��.
	 * @return
	 */
	public int getAttrWidth(){
		//�׺ţ����ţ��ĺŵļ��
		int result = 0;
		int deltax = uiClef == null ? 0 : uiClef.getWidth() + UIClef.CLEF_GAP;
		result += deltax;
		deltax = uiKey == null ? 0 : uiKey.getWidth() + UIKey.KEY_GAP;
		result += deltax;
		deltax = uiTime == null ? 0 : uiTime.getWidth() + UITime.TIME_GAP;
		result += deltax;
		
		return result;
	}
	
	/**
	 * ���ɱ�ѡ��ʱ�ķ���
	 */
	public void generateSelector(){
		if(selector == null){
			selector = new JPanel();
			selector.setOpaque(false);
			selector.setSize(getWidth() + NoteCanvas.LINE_GAP, getHeight() + 2 * NoteCanvas.LINE_GAP);
			Border bd = BorderFactory.createLineBorder(Color.blue, 2);
			selector.setBorder(bd);
			selector.setLocation(getX() - NoteCanvas.LINE_GAP/2, getY() - NoteCanvas.LINE_GAP);
			((JLayeredPane)getParent()).add(selector, JLayeredPane.DRAG_LAYER);
		}
	}
	
	/**
	 * ȥ��ѡ����
	 */
	public void removeSelector(){
		if(selector != null && getParent() != null){
			Page page = (Page)getParent();
			page.remove(selector);
			page.revalidate();
			page.updateUI();
			((JComponent)(page.getParent().getParent())).updateUI();
			selector = null;
		}
		selector = null;
	}

	public void cancleSelected() {
		// TODO Auto-generated method stub
		selected = false;
		removeSelector();
		for(AbstractNote note : noteList){
			note.cancleSelected();
		}
		for(AbstractNote note : v2NoteList.list){
			note.cancleSelected();
		}
	}

	public void beSelected() {
		// TODO Auto-generated method stub
		selected = true;
		if(selector == null){
			generateSelector();
		}
		for(AbstractNote note : noteList){
			note.beSelected();
		}
		for(AbstractNote note : v2NoteList.list){
			note.beSelected();
		}
	}
	
	 /**
	  * ɾ��С���׺š��ĺŵ�ʵ��
	  */
	public void deleteMeaAttr(){
		if(uiClef != null){
			JComponent parent = (JComponent)uiClef.getParent();
			parent.remove(uiClef);
		}
		if(uiKey != null){
			JComponent parent = (JComponent)uiKey.getParent();
			parent.remove(uiKey);
		}
		if(uiTime != null){
			JComponent parent = (JComponent)uiTime.getParent();
			parent.remove(uiTime);
		}
	}


	/**
	 * ����ע��
	 */
	public void locateAnnotations(){
		for(Annotation an : annotations){
			if(an.getRelatedObjts().get(0) == this){
				an.setLocation(getX() + NoteCanvas.LINE_GAP + an.getDraggedX(),
						getY() - an.getHeight() - 2*NoteCanvas.LINE_GAP + an.getDraggedY());
			}
		}
	}
	
	/**
	 * С���Ƿ��������ƽ��ĵ�
	 * @return
	 */
	public boolean isUnlimited(){
		return time == null;
	}
	
	/**
	 * ������������
	 * @return
	 */
	public ArrayList<AbstractNote> getNoteList(){
		return noteList;
	}

	@Override
	public boolean equalsWith(Object o) {
		// TODO Auto-generated method stub
		if(!(o instanceof Measure))
			return false;
		Measure measure = (Measure)o;
		if(measure.getNoteNum(0) != noteList.size() || measure.getNoteNum(1) != v2NoteList.list.size())
			return false;
		int noteNum = noteList.size();
		AbstractNote[] list1 = new AbstractNote[noteNum];
		AbstractNote[] list2 = new AbstractNote[noteNum];
		
		return MusicMath.equals(noteList.toArray(list1), measure.getNoteList().toArray(list2));
	}
	
	/**
	 * �������Ƿ�������
	 * �÷����������������˵Ļ��ƣ������Ҫ�ڻ��Ʒ���ʱ����
	 * @return
	 */
	public boolean isVoice2Down(){
		return v2NoteList.direction.equals("down");
	}
	
	/**
	 * ����������������
	 * @param note
	 * @return
	 */
	public int getVoice(AbstractNote note){
		if(note.getVoice() != -1)
			return note.getVoice();
		return noteList.contains(note) ? 1 : (v2NoteList.list.contains(note) ? 2 : -1);
	}
	
	/**
	 * ����С����������
	 * ���ҽ����ڶ�����Ϊ��ʱ������1�����򷵻�2
	 * @return
	 */
	public int getVoiceNum(){
		int n = v2NoteList.list.size() == 0 ? 1 : 2;
		return n;
	}
	
	/**
	 * �����������ķ�װ��
	 * @author sjy
	 *
	 */
	class V2NoteList{
		/**
		 * ����������λ�ã���Чֵ"up"��"down".
		 */
		String direction = "down";
		/**
		 * ��������
		 */
		List<AbstractNote> list = new ArrayList<AbstractNote>();
	}
	
	/**
	 * �����ܷ��˷���
	 */
	public void drawBeamAndStem(){
		JComponent page = (JComponent)getParent();
		if(getVoiceNum() == 1)
			Controller.drawBeamAndStem(page, noteList);
		else{
			if(v2NoteList.direction.equalsIgnoreCase("down")){
				Controller.drawBeamAndStem(page, noteList, "up");
				Controller.drawBeamAndStem(page, v2NoteList.list, "down");
			}
			else if(v2NoteList.direction.equalsIgnoreCase("up")){
				Controller.drawBeamAndStem(page, noteList, "down");
				Controller.drawBeamAndStem(page, v2NoteList.list, "up");
			}
		}
	}

}

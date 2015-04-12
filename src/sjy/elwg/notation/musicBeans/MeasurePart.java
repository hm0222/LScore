package sjy.elwg.notation.musicBeans;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.notation.musicBeans.symbolLines.RepeatLine;
import sjy.elwg.utility.MusicMath;

/**
 * С����,�������ɸ�С��
 * @author jingyuan.sun
 *
 */
public class MeasurePart implements Selectable,Equalable{
	
	/**
	 * С������
	 */
	private ArrayList<Measure> measureList;
	/**
	 * ���ڵ���
	 */
	private NoteLine noteLine;
	/**
	 * С����
	 */
	private Barline barline;
	/**
	 * �����Ǻ�
	 */
	protected ArrayList<NoteSymbol> repeatSymbol;
	
	/**
	 * �����Ǻ�
	 */
	protected ArrayList<RepeatLine> repeatLines;
	/**
	 * X����ֵ
	 */
	private int x;
	/**
	 * y����ֵ
	 */
	private int y;
	/**
	 * ���
	 */
	private int width;
	/**
	 * ��ѡ��ʱ���ֵĿ�
	 */
	private JPanel selector;
	
	/**
	 * ���캯��
	 */
	public MeasurePart(){
		measureList = new ArrayList<Measure>();
		repeatSymbol = new ArrayList<NoteSymbol>();
		repeatLines = new ArrayList<RepeatLine>();
	}
	
	/********  ��measureList�ķ������з�װ�����������ֱ�ӷ���measureList  *********/
	
	/**
	 * ���С��
	 */
	public void addMeasure(Measure measure){
		measureList.add(measure);
		measure.setMeasurePart(this);
	}
	
	/**
	 * ���С��
	 * @param index
	 * @param measure
	 */
	public void addMeasure(int index, Measure measure){
		measureList.add(index, measure);
		measure.setMeasurePart(this);
	}
	
	/**
	 * ɾ��С��
	 * @param measure
	 */
	public void removeMeasure(Measure measure){
		measure.setMeasurePart(null);
		measureList.remove(measure);
	}
	
	/**
	 * ���С��
	 * @param index
	 * @return
	 */
	public Measure getMeasure(int index){
		return measureList.get(index);
	}
	
	/**
	 * С����С�����е�λ��
	 * @param measure
	 * @return
	 */
	public int measureIndex(Measure measure){
		return measureList.indexOf(measure);
	}
	
	/**
	 * С�ڸ���
	 * @return
	 */
	public int getMeasureNum(){
		return measureList.size();
	}
	
	/*******************************************************/

	/**
	 * ���С����
	 */
	public Barline getBarline() {
		return barline;
	}

	/**
	 * ����С����
	 * @param barline
	 */
	public void setBarline(Barline barline) {
		this.barline = barline;
	}
	
	/**
	 * ����X����ֵ,��С�����е�һ��С�ڵ�X����
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * ����X����ֵ
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * ����Y����ֵ
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * ����Y����ֵ
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * ��¼����С�����е�ʵ����,�䵱�ṹ��
	 *
	 */
	public class NoteWithMeaIndex{
		private AbstractNote note;
		private int meaIndex;
		public NoteWithMeaIndex(AbstractNote note, int noteIndex){
			this.note = note;
			this.meaIndex = noteIndex;
		}
		public AbstractNote getNote() {
			return note;
		}
		public int getMeaIndex() {
			return meaIndex;
		}
	}
	
	/**
	 * ĳһʱ�̲�ͬ���������������.
	 * @author sjy
	 *
	 */
	public class NListWithMeaIndex{
		private List<AbstractNote> list;
		private int meaIndex;
		public NListWithMeaIndex(List<AbstractNote> list, int index){
			this.list = list;
			this.meaIndex = index;
		}
		public List<AbstractNote> getList(){
			return list;
		}
		public int getMeaIndex(){
			return meaIndex;
		}
		/**
		 * ���������������Ƿ񽻲�
		 * ������棬����������ʱ�������������������У���������������
		 * @return
		 */
		public boolean isInteracted(){
			if(list.size() < 2)  //������������
				return false;
			if((list.get(0) instanceof Note && ((Note)list.get(0)).isRest()) ||   //��һ������ֹ��
					(list.get(1) instanceof Note && ((Note)list.get(1)).isRest()))
				return false;
			Measure measure = measureList.get(meaIndex);
			AbstractNote note1 = list.get(0);
			AbstractNote note2 = list.get(1);
			if(note1.absHigherThan(note2) && ((note1.getVoice() == 0 && measure.isVoice2Down()) || 
					(note1.getVoice() == 1 && !measure.isVoice2Down())))
				return false;
			if(note2.absHigherThan(note1) && ((note2.getVoice() == 0 && measure.isVoice2Down()) || 
					(note2.getVoice() == 1 && !measure.isVoice2Down())))
				return false;
			return true;
		}
		/**
		 * ������棬���ؽ���ʱ�������ұߵ������������˳��µ�����
		 * ���û�н��棬����null
		 * @return
		 */
		public AbstractNote getRightNote(){
			if(!isInteracted())
				return null;
			Measure measure = measureList.get(meaIndex);
			int voiceIndex = -1;
			voiceIndex = measure.isVoice2Down() ? 1 : 0;
			for(int i = 0; i < list.size(); i++){
				AbstractNote note = list.get(i);
				if(note.getVoice() == voiceIndex)
					return note;
			}
			return null;
		}
		/**
		 * ������棬���ؽ���ʱ��������ߵ������������˳��ϵ�����
		 * ���û�н��棬����null
		 * @return
		 */
		public AbstractNote getLeftNote(){
			if(!isInteracted())
				return null;
			Measure measure = measureList.get(meaIndex);
			int voiceIndex = -1;
			voiceIndex = measure.isVoice2Down() ? 0 : 1;
			for(int i = 0; i < list.size(); i++){
				AbstractNote note = list.get(i);
				if(note.getVoice() == voiceIndex)
					return note;
			}
			return null;
		}
		/**
		 * ���ø�����������
		 * @param x
		 */
		public void locateNotes(int x){
			if(!isInteracted()){
				for(AbstractNote n : list){
					n.locateNote(x);
				}
			}else{
				Measure measure = measureList.get(meaIndex);
				if(measure.isVoice2Down()){
					for(AbstractNote n : list){
						if(n.getVoice() == 1){
							int xx = n.getWidth() > Note.NORMAL_HEAD_WIDTH ? x : x + Note.NORMAL_HEAD_WIDTH + 1;
							n.locateNote(xx);
						}else{
							n.locateNote(x);
						}
					}
				}else{
					for(AbstractNote n : list){
						if(n.getVoice() == 0){
							int xx = n.getWidth() > Note.NORMAL_HEAD_WIDTH ? x : x + Note.NORMAL_HEAD_WIDTH + 1;
							n.locateNote(xx);
						}else{
							n.locateNote(x);
						}
					}
				}
			}
		}
		/**
		 * ��������������ռ�Ŀ��
		 * @return
		 */
		public int getWidth(){
			int w = isInteracted() ? Note.NORMAL_HEAD_WIDTH * 2 + 1 : Note.NORMAL_HEAD_WIDTH;
			return w;
		}
	}
	
	
	/**
	 * ����С���鰴��ʱ������е���������
	 * @return
	 */
	public List<List<NoteWithMeaIndex>> getNoteListByTimeSlot(){
		int meaNum = measureList.size();
		//��С�ڵ��ۻ�ʱ��
		int[] accumDur = new int[meaNum];
		float[] faccumDur = new float[meaNum];
		//��С�ڵ�ǰʱ������
		int[] slotIndex = new int[meaNum];
		
		List<List<List<AbstractNote>>> meaSlots = new ArrayList<List<List<AbstractNote>>>();  //������С�ڵ�ʱ������кϲ�Ϊһ��
		for(int i = 0; i < meaNum; i++){
			Measure measure = measureList.get(i);
			meaSlots.add(measure.getNotesByTimeSlot());
		}
		
		List<List<NoteWithMeaIndex>> result = new ArrayList<List<NoteWithMeaIndex>>();
		Loop:
		while(true){
			List<NoteWithMeaIndex> timeSlot = new ArrayList<NoteWithMeaIndex>();
			int minDur = MusicMath.minValue(accumDur);
			for(int i = 0;i < meaNum; i++){
				if(accumDur[i] == minDur){
					if(slotIndex[i] >= meaSlots.get(i).size())
						break Loop;
					List<AbstractNote> l = meaSlots.get(i).get(slotIndex[i]);
					for(int j = 0; j < l.size(); j++){
						NoteWithMeaIndex nw = new NoteWithMeaIndex(l.get(j), i);
						timeSlot.add(nw);
					}
					faccumDur[i] += l.get(0).getRealDuration();
					slotIndex[i]++;
					accumDur[i] = Math.round(faccumDur[i]);
				}
			}
			result.add(timeSlot);
		}
		return result;
	}
	
	/**
	 * ��getNoteListByTimeSlot()�������ơ���ͬ���ǿ���С����������
	 * @return
	 */
	public List<List<NListWithMeaIndex>> getNotesByTimeSlot(){
		int meaNum = measureList.size();
		//��С�ڵ��ۻ�ʱ��
		int[] accumDur = new int[meaNum];
		float[] faccumDur = new float[meaNum];
		//��С�ڵ�ǰʱ������
		int[] slotIndex = new int[meaNum];
		
		List<List<List<AbstractNote>>> meaSlots = new ArrayList<List<List<AbstractNote>>>();  //������С�ڵ�ʱ������кϲ�Ϊһ��
		for(int i = 0; i < meaNum; i++){
			Measure measure = measureList.get(i);
			meaSlots.add(measure.getNotesByTimeSlot());
		}
		
		List<List<NListWithMeaIndex>> result = new ArrayList<List<NListWithMeaIndex>>();
		Loop:
		while(true){
			List<NListWithMeaIndex> list = new ArrayList<NListWithMeaIndex>();
			int minDur = MusicMath.minValue(accumDur);
			for(int i = 0; i < meaNum; i++){
				if(accumDur[i] == minDur){
					if(slotIndex[i] >= meaSlots.get(i).size())
						break Loop;
					List<AbstractNote> l = meaSlots.get(i).get(slotIndex[i]);
					NListWithMeaIndex nwmi = new NListWithMeaIndex(l, i);
					list.add(nwmi);
					faccumDur[i] += MusicMath.minRealDuration(l);
					slotIndex[i]++;
					accumDur[i] = Math.round(faccumDur[i]);
				}
			}
			result.add(list);
		}
		return result;
	}

	/**
	 * ����С������,������ÿ��С�ڵĿ�ȶ���ͬ
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * ����С������
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
    
	/**
	 * ����С����߶�,��ֵΪ�ӵ�һ��С�ڵĵ�һ���ߵ����һ��С�ڵ����һ���ߵĸ߶Ȳ�
	 * @return
	 */
	public int getHeight() {
		int height = 0;
		for(int i = 0, n = measureList.size() - 1; i < n; i++){
			height += Measure.MEASURE_HEIGHT + noteLine.getMeasureGaps().get(i);
		}
		height += Measure.MEASURE_HEIGHT;
		return height;
	}

	/**
	 * ����С�������ڵ���
	 * @return
	 */
	public NoteLine getNoteLine() {
		return noteLine;
	}

	/**
	 * ������
	 * @param noteLine
	 */
	public void setNoteLine(NoteLine noteLine) {
		this.noteLine = noteLine;
	}
	
	/**
	 * ����С�������С���׺š����š��ĺſ��֮�͵����ֵ
	 * @return
	 */
	public int maxAttrWidth(){
		int result = 0;
		for(int i = 0, n = measureList.size(); i < n; i++){
			Measure measure = measureList.get(i);
			if(measure.getAttrWidth() > result)
				result = measure.getAttrWidth();
		}
		return result;
	}
	
	/**
	 * ������ѡ���
	 */
	public void generateSelector(){
		if(selector == null){
			selector = new JPanel();
			selector.setOpaque(false);
			selector.setSize(getWidth() + NoteCanvas.LINE_GAP, getHeight() + 2 * NoteCanvas.LINE_GAP);
			Border bd = BorderFactory.createLineBorder(Color.blue, 2);
			selector.setBorder(bd);
			selector.setLocation(getX() - NoteCanvas.LINE_GAP/2, getY() - NoteCanvas.LINE_GAP);
			Measure fmeasure = measureList.get(0);
			((JLayeredPane)fmeasure.getParent()).add(selector, JLayeredPane.DRAG_LAYER);
		}
	}
	
	/**
	 * ɾ����ѡ���
	 */
	public void removeSelector(){
		if(selector != null){
			Measure fmeasure = measureList.get(0);
			Page page = (Page)fmeasure.getParent();
			page.remove(selector);
			page.revalidate();
			page.updateUI();
			((JComponent)(page.getParent().getParent())).updateUI();
			selector = null;
		}
	}

	public void beSelected() {
		// TODO Auto-generated method stub
		if(selector == null){
			generateSelector();
			for(Measure measure : measureList){
				for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
					for(int i = 0, n = measure.getNoteNum(v+1); i < n; i++){
						measure.getNote(i, v).beSelected();
					}
				}
			}
		}
	}

	public void cancleSelected() {
		// TODO Auto-generated method stub
		removeSelector();
		for(Measure measure : measureList){
			for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
				for(int i = 0, n = measure.getNoteNum(v+1); i < n; i++){
					measure.getNote(i, v).cancleSelected();
				}
			}
		}
	}
	
	/**
	 * ����С����С�����д��ڵڼ�������.
	 * �����С���鲻������С�ڣ��򷵻�-1
	 * @param measure
	 * @return
	 */
	public int getInstrumentIndex(Measure measure){
		int result = -1;
		for(int i = 0, n = measureList.size(); i < n; i++){
			Measure mea = measureList.get(i);
			if(mea.getInstrument() != null)
				result++;
			if(mea == measure)
				return result;
		}
		return -1;
	}
	
	/**
	 * ����ָ�������ĵ�һ�������������е�����λ��
	 * ���ָ�����������������׵������������򷵻�-1.
	 * @param instrIndex �������
	 * @return
	 */
	public int getMeaIndxByInstrIndx(int instrIndex){
		int instr = -1;
		for(int i = 0, n = measureList.size(); i < n; i++){
			Measure measure = measureList.get(i);
			if(measure.getInstrument() != null)
				instr++;
			if(instr == instrIndex)
				return i;
		}
		return -1;
	}
	
	/**
	 * ����С������
	 * @return
	 */
	public ArrayList<Measure> getMeasureList(){
		return measureList;
	}

	@Override
	public boolean equalsWith(Object o) {
		// TODO Auto-generated method stub
		if(!(o instanceof MeasurePart))
			return false;
		MeasurePart meaPart = (MeasurePart)o;
		if(meaPart.getMeasureNum() != measureList.size())
			return false;
		int num = measureList.size();
		Measure[] list1 = new Measure[num];
		Measure[] list2 = new Measure[num];
		return MusicMath.equals(measureList.toArray(list1), meaPart.getMeasureList().toArray(list2));
	}

	public void addRepeatSymbol(RepeatSymbol rs) {
		// TODO Auto-generated method stub
		repeatSymbol.add(rs);
		rs.setMeasure(this);
	}

	public void removeRepeatSymbol(RepeatSymbol rs) {
		// TODO Auto-generated method stub
		repeatSymbol.remove(rs);
		rs.setMeasurePart(null);
	}

//	public void addRepeatEnding(RepeatEnding rl) {
//		// TODO Auto-generated method stub
//		RepeatEnding = rl;
//		rl.setMeasurePart(this);
//	}




	
	/**
	 * ����С����ĸ��ַ���
	 */
	public void locateSymbols() {
		// TODO Auto-generated method stub
//		if(repeatEnding != null){
//			RepeatEnding.adjustSize();
//			RepeatEnding.setLocation(getX(), getY() - RepeatEnding.getHeight());
//		}
//		if((RepeatEnding)repeatLine != null){
//			System.out.println("yes,has repeat line");
//			repeatLine.setLocation(getX(), getY() - repeatLine.getHeight());
//			repeatLine.setLocation(200, 50);
//		}
		if(repeatSymbol.size() != 0){
			for(int i = 0, n = repeatSymbol.size(); i < n; i++){
				NoteSymbol nsl = repeatSymbol.get(i);
				int dragX = nsl.getDraggedX();
				int dragY = nsl.getDraggedY();
				nsl.setLocation(getX()+dragX, getY()-nsl.getHeight()+dragY);
			}
		}
	}

	/**
	 * ����ظ������Ǻ�
	 * @return
	 */
	public ArrayList<RepeatLine> getRepeatLines() {
		return repeatLines;
	}

	public void setRepeatLines(ArrayList<RepeatLine> repeatLines) {
		this.repeatLines = repeatLines;
	}

	/**
	 * ��÷����Ǻ�
	 * @return
	 */
	public ArrayList<NoteSymbol> getRepeatSymbol() {
		return repeatSymbol;
	}

}

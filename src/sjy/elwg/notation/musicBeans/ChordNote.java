package sjy.elwg.notation.musicBeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.NoteSymbol;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * ��������
 * @author jingyuan.sun
 * 
 * ���������Ĺ�����Ҫһ����ͨ������Ϊ�����������ƴ󲿷ָòο������Ĺ�������.
 * ����������������ͨ����ʱ��������������ƺ��������Ĺ�������.
 *
 */
public class ChordNote extends AbstractNote{
	
	/**
	 * ���к�
	 */
	private static final long serialVersionUID = 15L;
	/**
	 * �����е���������
	 */
	protected ArrayList<Note> noteList;
	
	/**
	 * ���캯��,��Ҫһ����ͨ�������й���.
	 * ע�⣺����ʱ����ͨ��������������Ĺ��з��Ÿ�����ң�ͬʱȡ����ͨ��������Щ���ŵ�����Ȩ��
	 * @param note
	 */
	public ChordNote(Note note){
		super(note.getDuration());
		note.setChordNote(this);
		noteList = new ArrayList<Note>();
		dotNum = note.getDotNum();
		beamType = note.getBeamType();
		Controller.transferSymbols(note, this);
	}

	/**
	 * ��������. ��Ҫ���������������������̫Сʱ������,���ȶԺ����е������������ߵ�����
	 * �ٸ�������֮�����ߵĲ��������
	 * @param x ����������
	 */
	public void locateNote(int x) {
			Comparator<Note> comparator = new Comparator<Note>() {
				public int compare(Note note1, Note note2) {
					return note1.getPitch() - note2.getPitch();
				}
			};
			Collections.sort(noteList, comparator);
			for (int i = 0; i < noteList.size(); i++) {
				Note note = noteList.get(i);
				if (i == 0) {
					note.setLocation(x, measure.getY() + 4
							* NoteCanvas.LINE_GAP - NoteCanvas.LINE_GAP
							* note.getPitch() / 2 - note.getHeight() / 2);
				} else if (i == 1) {
					if (noteList.get(1).getPitch() - noteList.get(0).getPitch() == 1) {
						noteList.get(1).setLocation(
								x + noteList.get(1).getWidth(),
								measure.getY() + 4 * NoteCanvas.LINE_GAP
										- NoteCanvas.LINE_GAP
										* noteList.get(1).getPitch() / 2
										- noteList.get(1).getHeight() / 2);
					} else {
						note.setLocation(x, measure.getY() + 4
								* NoteCanvas.LINE_GAP - NoteCanvas.LINE_GAP
								* note.getPitch() / 2 - note.getHeight() / 2);
					}
				} else {
					if (noteList.get(i).getPitch()
							- noteList.get(i - 1).getPitch() == 1) {
						if (noteList.get(i - 1).getX() == x) {
							noteList.get(i).setLocation(
									x + noteList.get(i).getWidth(),
									measure.getY() + 4 * NoteCanvas.LINE_GAP
											- NoteCanvas.LINE_GAP
											* noteList.get(i).getPitch() / 2
											- noteList.get(i).getHeight() / 2);
						} else {
							noteList.get(i).setLocation(
									x,
									measure.getY() + 4 * NoteCanvas.LINE_GAP
											- NoteCanvas.LINE_GAP
											* noteList.get(i).getPitch() / 2
											- noteList.get(i).getHeight() / 2);
						}

					} else {
						note.setLocation(x, measure.getY() + 4
								* NoteCanvas.LINE_GAP - NoteCanvas.LINE_GAP
								* note.getPitch() / 2 - note.getHeight() / 2);
					}
				}
				note.refreshPosLines();

			}
	}

	/**
	 * ����������͵�����
	 * @return
	 */
	public Note getLowestNote(){
		int pitch = 1000;
		Note result = null;
		for(int i = 0; i < noteList.size(); i++){
			Note note = noteList.get(i);
			if(note.getPitch() < pitch){
				pitch = note.getPitch();
				result = note;
			}
		}
		return result;
	}
	
	/**
	 * ����������ߵ�����
	 * @return
	 */
	public Note getHighestNote(){
		int pitch = -1000;
		Note result = null;
		for(int i = 0; i < noteList.size(); i++){
			Note note = noteList.get(i);
			if(note.getPitch() > pitch){
				pitch = note.getPitch();
				result = note;
			}
		}
		return result;
	}
	
	/**
	 * �������о���ָ�����ߵ�����
	 */
	public Note getNoteWithPitch(int pitch){
		for(Note note : noteList){
			if(!note.isRest() && note.getPitch() == pitch)
				return note;
		}
		return null;
	}
	
	/**
	 * �Ƿ�����һ�������������
	 */
	public boolean equalsWith(Object o){
		if(!(o instanceof ChordNote))
			return false;
		ChordNote cnote = (ChordNote)o;
		if(cnote.getNoteNum() == noteList.size()){
			Note[] list1 = new Note[cnote.getNoteNum()];
			Note[] list2 = new Note[cnote.getNoteNum()];
			return MusicMath.equals(noteList.toArray(list1), cnote.getNoteList().toArray(list2));
		}
		return false;
	}
	
	public int sharpOrFlatWidth(){
		for(int i = 0; i < noteList.size(); i++){
			Note nt = noteList.get(i);
			if(nt.getSharpOrFlat() != null)
				return nt.getSharpOrFlat().getWidth();
		}
		return 0;
	}
	
	public int dotWidth(){
		Note nt = noteList.get(0);
		if (nt.getUiDot() != null)
			return nt.getUiDot().getWidth();

		return 0;
	}
	
	public boolean absHigherThan(AbstractNote note){
		boolean flag = true;
		for(Note n : noteList){
			if(!n.absHigherThan(note))
				flag = false;
		}
		return flag;
	}
	
	public boolean absLowerThan(AbstractNote note){
		boolean flag = true;
		for(Note n : noteList){
			if(!n.absLowerThan(note))
				flag = false;
		}
		return flag;
	}
	
	/**
	 * ���߼���ɾ�������������еķ���
	 */
	public void removeAllSymbols(boolean withTup){
		super.removeAllSymbols(withTup);
		
		for(int i = 0; i < noteList.size(); i++){
			Note note = noteList.get(i);
			note.removeAllSymbols(false);
		}
	}
	
	/**
	 * ���غ�����(����)
	 * @return
	 */
	public int getX(){
		int x = noteList.get(0).getX();
		for(int i = 0, n = noteList.size(); i < n; i++){
			Note note = noteList.get(i);
			if(note.getX() < x)
				x = note.getX();
		}
		return x;
	}
	
	/**
	 * ���������꣬ʹ�ú����������������������������
	 */
	public int getY(){
		return getHighestNote().getY();
	}
	
	/**
	 * ���غ���������ռ�Ŀ��
	 * @return
	 */
	public int getWidth(){
		for(int i = 0, n = noteList.size(); i < n; i++){
			Note note = noteList.get(i);
			int curPitch = note.getPitch();
			for(int j = i + 1; j < n; j++){
				int nxtPitch = noteList.get(j).getPitch();
				if(Math.abs(curPitch - nxtPitch) == 1)
					return 2 * noteList.get(0).getWidth();
			}
		}
		return noteList.get(0).getWidth();
	}

	@Override
	public void locateNoteSymbols() {
		// TODO Auto-generated method stub
		super.locateNoteSymbols();
		for(Note note : noteList){
			note.locateNoteSymbols();
		}
		if (ornaments.size() != 0){
			Stem stem = getStem();
			int sy = stem == null ? getHighestNote().getY() : stem.getY();
			int lx = getLowestNote().getX();
			int ly = getLowestNote().getY();
			int hx = getHighestNote().getX();
			int hy = getHighestNote().getY();
			int y1 = getHighestNote().getY() - (int)4 * NoteCanvas.LINE_GAP;
			
			for(int i = 0, n = ornaments.size(); i < n; i++){
				NoteSymbol nsl = ornaments.get(i);
				int dragY = nsl.getDraggedY();
				//�������ڷ����Ϸ���ʱ���������������
				if(sy < getHighestNote().getY()){
//					int pitch = getLowestNote().getPitch();
					if(nsl.getSymbolType().equals("staccato")){
						nsl.setLocation(lx, getLowestNote().getY() + 15 + dragY);
					}else{
						if(hasOrnament("staccato")){
							if (nsl.getSymbolType().equals("Staccatissimo") || nsl.getSymbolType().equals("strong-accent")) {
								nsl.setLocation(lx, y1);
								y1 -= 10;
							}else{
								nsl.setLocation(lx, ly + 25 + dragY);
								ly += 10;
							}
						}else{
							if(nsl.getSymbolType().equals("staccato")){
								nsl.setLocation(lx, getLowestNote().getY() + 15 + dragY);
							}else{
								if (nsl.getSymbolType().equals("Staccatissimo") || nsl.getSymbolType().equals("strong-accent")) {
									nsl.setLocation(lx, y1);
									y1 -= 10;
								}else{
									nsl.setLocation(lx, ly + 15 + dragY);
									ly += 10;
								}
							}					
						}
					}
				}else{
				    //�������ڷ����·�ʱ��������������
//					int pitch = getHighestNote().getPitch();
					if(nsl.getSymbolType().equals("staccato")){
						nsl.setLocation(hx, getHighestNote().getY() - 5 + dragY);
					}else{
						if(hasOrnament("staccato")){
							nsl.setLocation(hx, hy - 15 + dragY);
							hy -= 10;
						}else{
							if(nsl.getSymbolType().equals("staccato")){
								nsl.setLocation(hx, getHighestNote().getY() - 5 + dragY);
							}else{
								nsl.setLocation(hx, hy - 5 + dragY);
								hy -= 10;
							}						
						}
					}
				}
			}
		
		}

		if(dynamics.size() != 0){
			for(int i = 0, n = dynamics.size(); i < n; i++){
				NoteSymbol nsl = dynamics.get(i);
				int dragX = nsl.getDraggedX();
				int dragY = nsl.getDraggedY();
				if(getHighestNote().getPitch() >= 10){
					nsl.setLocation(getX() + dragX - 25,
							getHighestNote().getY() - 15 + dragY);
				}else{
					nsl.setLocation(getX() + dragX - 10,
							measure.getY() - 30 + dragY);
				}
				
			}
		}
		
		if(performanceSymbols.size() != 0){
			for(int i = 0, n = performanceSymbols.size(); i < n; i++){
				NoteSymbol nsl = performanceSymbols.get(i);
				int dragX = nsl.getDraggedX();
				int dragY = nsl.getDraggedY();
				if(getHighestNote().getPitch() >= 10){
					nsl.setLocation(getX() + dragX - 25,
							getHighestNote().getY() - 15 + dragY);
				}else{
					nsl.setLocation(getX() + dragX - 10,
							measure.getY() - 30 + dragY);
				}
				
			}
		}
		//���
		if(lyrics.size() != 0){
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
		//�ڷ����ϵĲ���
		if (tremoloBeam != null) {
			int highy = getHighestNote().getY();
			int lowy = getLowestNote().getY();
			int restStemLength = getStem().getHeight() - (lowy - highy);
			int singleWidth = getHighestNote().getWidth();
			if (this.getDuration() == 256) {
				tremoloBeam.setLocation(getX() + 3, highy - 10);
			} else {
				//���˳���
				if (getStem().getY() > highy) {
					if (tremoloBeam.getSymbolType().equals("tremoloBeam1")) {
						tremoloBeam.setLocation(getX() - singleWidth / 2 + 2, lowy + restStemLength / 2);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam2")) {
						tremoloBeam.setLocation(getX() - singleWidth / 2 + 2, lowy + restStemLength / 2 - 3);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam3")) {
						tremoloBeam.setLocation(getX() - singleWidth / 2 + 2, lowy + restStemLength / 2 - 6);
					}
				}//���˳��� 
				else if (getStem().getY() < getY()) {
					if (tremoloBeam.getSymbolType().equals("tremoloBeam1")) {tremoloBeam.setLocation(getX() + singleWidth / 2 + 1,
							highy - restStemLength / 2);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam2")) {
						tremoloBeam.setLocation(getX() + singleWidth / 2 + 1,
								highy - restStemLength / 2 - 3);
					} else if (tremoloBeam.getSymbolType().equals("tremoloBeam3")) {
						tremoloBeam.setLocation(getX() + singleWidth / 2 + 1, highy - restStemLength / 2 - 6);
					}
				}
			}
		}
		if(graceSymbols.size() != 0){			
			for(int i = 0, n = graceSymbols.size(); i < n; i++){
				int highy = getHighestNote().getY();
				NoteSymbol nsl = graceSymbols.get(i);
				int dragX = nsl.getDraggedX();
				int dragY = nsl.getDraggedY();
				if(highy > getHighestNote().getMeasure().getY()){
					nsl.setLocation(getX()- 2 + dragX, getHighestNote().getMeasure().getY()-15+dragY);
				}else{
					nsl.setLocation(getX()- 2 + dragX, highy-15+dragY);
				}			
			}
		}
		if(breath != null){		
			int highy = getHighestNote().getY();
			int dragX = breath.getDraggedX();
			int dragY = breath.getDraggedY();
				
				if(highy > getHighestNote().getMeasure().getY()){
					breath.setLocation(getX()+12 + dragX, getHighestNote().getMeasure().getY()-25 + dragY);
	
				}else{
					breath.setLocation(getX()+12+ dragX,getHighestNote().getMeasure().getY()-29+ dragY);
					
				}			
			
		}
		
		if(pedal != null){
			int dragX = pedal.getDraggedX();
			int dragY = pedal.getDraggedY();
			if(this.getLowestNote().getPitch() > 2){
				pedal.setLocation(getX()+ dragX, this.getMeasure().getY()+40+ dragY);
			}else{
				pedal.setLocation(getX()+ dragX,getLowestNote().getY()+15+ dragY);
			}
		}
	}
	
	/**
	 * �Ƿ���������
	 * @return
	 */
	public boolean hasSharpOrFlat(){
		for(Note note : noteList){
			if(note.getSharpOrFlat() != null)
				return true;
		}
		return false;
	}
	
	/**
	 * �����������������ŵ�����ȣ��������������û�������ţ��򷵻�0
	 * @return
	 */
	public int getMaxSharpOrFlatWidth(){
		int result = 0;
		for(Note note : noteList){
			if(note.getSharpOrFlat() != null){
				SharpOrFlat sof = note.getSharpOrFlat();
				if(sof.getWidth() > result)
					result = sof.getWidth();
			}
		}
		return result;
	}
	
	public void generateUIDot(){
		if(dotNum != 0){
			for(Note note : noteList){
				note.generateUIDot();
			}
		}
	}
	
	/**
	 * ������������������
	 * @return
	 */
	public ArrayList<Note> getNoteList(){
		return noteList;
	}
	
	/************  ���·�����noteList���з�װ���������ֱ�ӻ��noteList������  **********/
	
	/**
	 * ��ú����е�ĳ������
	 * @param i
	 * @return
	 */
	public Note getNote(int i){
		return noteList.get(i);
	}
	
	/**
	 * ��������������
	 * @param note
	 */
	public void addNote(Note note){
		noteList.add(note);
		note.setChordNote(this);
		note.setDuration(duration);
		note.setBeamType(beamType);
		note.setDotNum(dotNum);
		if(measure != null){
			note.determineRealPitch();
		}
	}
	
	/**
	 * �Ӻ�����ɾ������
	 * ע�⣺��ɾ������������������Ž����߼��ϵ�ɾ������û�н���UIʵ���ɾ��.
	 * @param note
	 */
	public void removeNote(Note note){
		noteList.remove(note);
		note.setChordNote(null);
	}
	
	/**
	 * ������������
	 * @return
	 */
	public int getNoteNum(){
		return noteList.size();
	}
	
	/**
	 * �����������
	 */
	public void clearNoteList(){
		noteList.clear();
	}
	
	/****************************************************************************/
	
	public void beSelected() {
		// TODO Auto-generated method stub
		for(Note note : noteList){
			note.beSelected();
		}
	}

	public void cancleSelected() {
		// TODO Auto-generated method stub
		for(Note note : noteList){
			note.cancleSelected();
		}
	}

	@Override
	public boolean isRest() {
		return false;
	}

//	@Override
//	public void locateOrnaments() {
//		// TODO Auto-generated method stub
//		
//	}

}

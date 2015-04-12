package sjy.elwg.utility;

import java.util.ArrayList;
import java.util.List;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Beam;
import sjy.elwg.notation.musicBeans.ChordGrace;
import sjy.elwg.notation.musicBeans.ChordNote;
import sjy.elwg.notation.musicBeans.Equalable;
import sjy.elwg.notation.musicBeans.Grace;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Score;
import sjy.elwg.notation.musicBeans.Time;
import sjy.elwg.notation.musicBeans.symbolLines.SymbolLine;
/**
 * ������
 * @author sjy
 *
 */
public class MusicMath {
	
	/**
	 * ��preNote֮�����note,���ڸ��ַ��ŵ����أ��������������������С����
	 * @param preNote ǰһ������
	 * @param note ��ǰ����
	 * @param withGrace �Ƿ�������
	 * @return
	 */
	public static int shortestDistNeeded(AbstractNote preNote, AbstractNote note, boolean withGrace){
		int result = 0;
		
		//ǰһ������Ϊ��. ��noteΪС�ڵ�һ������
		if(preNote == null){
			result = NoteCanvas.LINE_GAP;
			if(note instanceof Note){
				Note nnote = (Note)note;
				if(nnote.getSharpOrFlat() != null){
					result = nnote.getSharpOrFlat().getWidth() + 3 > result ? 
							nnote.getSharpOrFlat().getWidth() + 3 : result;
				}
			}
			else if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				if(cnote.hasSharpOrFlat()){
					result = cnote.getMaxSharpOrFlatWidth() + 3 > result ?
							cnote.getMaxSharpOrFlatWidth() + 3 : result;
				}
			}
			if(withGrace && !note.getLeftGraces().isEmpty()){
				result = note.shortestGraceWidth("left");
			}
			result = note.maxLyricWidth() / 2 + 3 > result ?
					note.maxLyricWidth() / 2 + 3 : result;
		}
		//��һ������Ϊ��. ��preNoteΪС�����һ������
		else if(note == null){
			result = preNote.getWidth() + NoteCanvas.X_MIN_DIST;
			if(preNote instanceof Note){
				Note nnote = (Note)preNote;
				if(nnote.getUiDot() != null){
					result = nnote.getUiDot().getWidth() + 3 > result ?
							nnote.getUiDot().getWidth() + 3 : result;
				}
			}
			else if(preNote instanceof ChordNote && ((ChordNote)preNote).getDotNum() != 0){
				ChordNote cnote = (ChordNote)preNote;
				if(cnote.getDotNum() != 0){
					result = cnote.getNote(0).getUiDot().getWidth() + 3 > result ?
						    cnote.getNote(0).getUiDot().getWidth() + 3 : result;
				}
			}
			if(withGrace && !preNote.getRightGraces().isEmpty()){
				result += preNote.shortestGraceWidth("right");
			}
			result = preNote.maxLyricWidth() / 2 + 3 > result ?
					preNote.maxLyricWidth() / 2 + 3 : result;
		}
		//�������
		else{
			result += preNote.getWidth();
			
			//ǰһ����������
			if(preNote instanceof ChordNote){
				//���������������ɵĶ�����
				ChordNote cn = (ChordNote)preNote;
				int deltax = cn.getWidth() > Note.NORMAL_HEAD_WIDTH ? cn.getNote(0).getWidth() : 0;
				result += deltax;
				
				if(cn.getDotNum() != 0){
					result += cn.getNote(0).getUiDot().getWidth();
				}
			}
			else if(preNote instanceof Note){
				Note nnote = (Note)preNote;
				if(nnote.getUiDot() != null){
					result += nnote.getUiDot().getWidth();
				}
			}
			if(withGrace && !note.getRightGraces().isEmpty()){
				result += note.shortestGraceWidth("right");
			}
			
			
			//��һ������
			if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				result += cnote.getMaxSharpOrFlatWidth();
			}
			else if(note instanceof Note){
				Note nnote = (Note)note;
				if(nnote.getSharpOrFlat() != null){
					result += nnote.getSharpOrFlat().getWidth();
				}
			}
			if(withGrace && !note.getLeftGraces().isEmpty()){
				result += note.shortestGraceWidth("left");
			}
			
			int minDist = NoteCanvas.X_MIN_DIST;
			if(preNote.getHighestNote() instanceof Grace)
				minDist = NoteCanvas.X_MIN_DIST/2;
			result = result > note.getHighestNote().getWidth() + minDist ?
					result : note.getHighestNote().getWidth() + minDist;
			int lyWidth = preNote.maxLyricWidth() / 2 + note.maxLyricWidth() / 2 + 3;
			result = result > lyWidth ? result : lyWidth;
		}
		return result;
	}
	
	/**
	 * �����������е���С����
	 * ������������Ϊͬһ��С���ڣ�ĳ��ʱ���������У�Ϊ��ͬ����������
	 * @param list1
	 * @param list2
	 * @param withGrace �Ƿ�������
	 * @return
	 */
	public static int shortestDist(MeasurePart.NListWithMeaIndex list1, MeasurePart.NListWithMeaIndex list2, 
			boolean withGrace){
		
		if(list1 == null && list2 == null)
			throw new NullPointerException();
		if((list1 == null || list1.getList().size() == 1) && (list2 == null || list2.getList().size() == 1)){
			AbstractNote n1 = list1 == null ? null : list1.getList().get(0);
			AbstractNote n2 = list2 == null ? null : list2.getList().get(0);
			return shortestDistNeeded(n1, n2, withGrace);
		}
		else if(list1 == null || list2 == null){
			if(list1 == null){
				return Math.max(shortestDistNeeded(null, list2.getList().get(0), withGrace), 
						shortestDistNeeded(null, list2.getList().get(1), withGrace));
			}
			else{
				return Math.max(shortestDistNeeded(list1.getList().get(0), null, withGrace), 
						shortestDistNeeded(list1.getList().get(1), null, withGrace));
			}
		}
		else if(list1.getList().size() == 2 && list2.getList().size() == 2){
			return shortestDistNeeded(list1.getList().get(0), list2.getList().get(0), withGrace);
		}
		else if(list1.getList().size() == 2 && (list2 == null || list2.getList().size() == 1)){
			AbstractNote rightNote =  list2 == null ? null : list2.getList().get(0);
			if(list1.isInteracted()){
				AbstractNote note = list1.getRightNote();
				return shortestDistNeeded(note, rightNote, withGrace) + Note.NORMAL_HEAD_WIDTH + 1;
			}else{
				return Math.min(shortestDistNeeded(list1.getList().get(0), rightNote, withGrace), 
						shortestDistNeeded(list1.getList().get(1), rightNote, withGrace));
			}
		}
		else if((list1 == null || list1.getList().size() == 1) && list2.getList().size() == 2){
			AbstractNote leftNote = list1 == null ? null : list1.getList().get(0);
			if(list2.isInteracted()){
				AbstractNote note = list2.getLeftNote();
				return shortestDistNeeded(leftNote, note, withGrace);
			}else{
				return Math.min(shortestDistNeeded(leftNote, list2.getList().get(0), withGrace), 
						shortestDistNeeded(leftNote, list2.getList().get(1), withGrace));
			}
		}
		return -1;
	}
	
	/**
	 * ���������е���Сֵ
	 * @param source Դ����
	 * @return
	 */
	public static int minValue(int[] source){
		int result = source[0];
		for(int i = 0; i < source.length; i++){
			if(source[i] < result)
				result = source[i];
		}
		return result;
	}
	
	/**
	 * �����������ֵ
	 * @param source Դ����
	 * @return
	 */
	public static int maxValue(int[] source){
		int result = source[0];
		for(int i = 0; i < source.length; i++){
			if(source[i] > result)
				result = source[i];
		}
		return result;
	}
	
	/**
	 * ���������о��е����ʵʱ�����������㡢�����ŵ�
	 * @param list
	 * @return
	 */
	public static float maxRealDuration(List<AbstractNote> list){
		float result = list.get(0).getRealDuration();
		for(int i = 0, n = list.size(); i < n; i++){
			float d = list.get(i).getRealDuration();
			if(d > result)
				result = d;
		}
		return result;
	}
	
	/**
	 * ���������о��е������ʵʱ�����������㡢�����ŵ�
	 * @param list
	 * @return
	 */
	public static float minRealDuration(List<AbstractNote> list){
		float result = list.get(0).getRealDuration();
		for(int i = 0, n = list.size(); i < n; i++){
			float d = list.get(i).getRealDuration();
			if(d < result)
				result = d;
		}
		return result;
	}
	
	/**
	 * �����������ŷ��������ڵ��У��Լ��ױ�ID.
	 * �����У��Լ��ױ�����Ⱥ�����һ��list��.
	 * @param sl ��������
	 * @return ��������Լ��ױ���ŵ�list.
	 */
	public static ArrayList<Object> getNoteLineBySymbolLine(SymbolLine sl){
		ArrayList<Object> result = new ArrayList<Object>();
		if(sl.getStartNote() != null){
			Measure measure = sl.getStartNote().getMeasure();
			MeasurePart meaPart = measure.getMeasurePart();
			NoteLine line = meaPart.getNoteLine();
			int meaNum = meaPart.measureIndex(measure);
			result.add(line);
			result.add(meaNum);
		}
		else if(sl.getEndNote() != null){
			Measure measure = sl.getEndNote().getMeasure();
			MeasurePart meaPart = measure.getMeasurePart();
			NoteLine line = meaPart.getNoteLine();
			int meaNum = meaPart.measureIndex(measure);
			result.add(line);
			result.add(meaNum);
		}else{
			SymbolLine preSl = (SymbolLine)sl.getPreSymbolLine();
			int num = 1;
			if(preSl == null){
				return null;
			}
			while(preSl.getStartNote() == null){
				num++;
				preSl = (SymbolLine)preSl.getPreSymbolLine();
			}
			Measure preMea = preSl.getStartNote().getMeasure();
			MeasurePart preMeaPart = preMea.getMeasurePart();
			NoteLine preLine = preMeaPart.getNoteLine();
			int meaIndex = preMeaPart.measureIndex(preMea);
			NoteLine curLine = preLine;
			for(int i = 0; i < num; i++){
				curLine = nxtLine(preLine);
			}
			result.add(curLine);
			result.add(meaIndex);
		}
		return result;
	}
	
	/**
	 * ������ĳһ�е���һ�У�������һ���򷵻ؿ�
	 * @param score ����
	 * @param line ��
	 * @return
	 */
	public static NoteLine nxtLine(NoteLine line){
		Score score = line.getPage().getScore();
		
		NoteLine nxtLine = null;
		Page page = line.getPage();
		int lineIndex = page.getNoteLines().indexOf(line);
		//���Ǳ�ҳ���һ��
		if(lineIndex < page.getNoteLines().size()-1){
			nxtLine = page.getNoteLines().get(lineIndex+1);
		}//�����һ��
		else{
			int pageIndex = score.getPageList().indexOf(page);
			//�������һҳ
			if(pageIndex != score.getPageList().size() - 1){
				Page nxtPage = score.getPageList().get(pageIndex + 1);
				nxtLine = nxtPage.getNoteLines().get(0);
			}
		}
		return nxtLine;
	}
	
	/**
	 * ����ĳ����������һ������������������
	 * û���򷵻�null
	 * @param note
	 * @return
	 */
	public static AbstractNote nxtNote(AbstractNote note){
		return nxtNote(note, false);
	}
	
	/**
	 * ��������һ������
	 * @param note ��ǰ����
	 * @param grace �Ƿ�������
	 * @return
	 */
	public static AbstractNote nxtNote(AbstractNote note, boolean grace){
		//������
		if(note instanceof Grace || note instanceof ChordGrace){
			AbstractNote anote = note instanceof Grace ? ((Grace)note).getNote() : ((ChordGrace)note).getNote();
			//������
			if(note.getX() < anote.getX()){
				if(grace){
					int index = anote.getLeftGraces().indexOf(note);
					AbstractNote re = index == anote.getLeftGraces().size()-1 ? anote : anote.getLeftGraces().get(index+1);
					return re;
				}else{
					return anote;
				}
			}
			//������
			else{
				if(grace){
					int index = anote.getLeftGraces().indexOf(note);
					AbstractNote re = index == anote.getLeftGraces().size()-1 ? nxtNote(anote) : anote.getLeftGraces().get(index+1);
					return re;
				}else{
					return nxtNote(anote);
				}
			}
		}
		//��ͨ����
		else{
			if(grace && !note.getRightGraces().isEmpty()){
				return note.getRightGraces().get(0);
			}
			
			int voice = note.getVoice();
			Measure measure = note.getMeasure();
			int noteIndex = measure.noteIndex(note);
			//����С�����һ������
			if(noteIndex < measure.getNoteNum(voice)-1){
				return measure.getNote(noteIndex + 1, voice);
			}
			else{
				MeasurePart measurePart = measure.getMeasurePart();
				int meaIndex = measurePart.measureIndex(measure);
				MeasurePart nxtPart = MusicMath.nxtMeasurePart(measurePart);
				while(nxtPart != null){
					Measure nxtMeasure = nxtPart.getMeasure(meaIndex);
					if(nxtMeasure.getNoteNum(voice) == 0){
						nxtPart = MusicMath.nxtMeasurePart(nxtPart);
						continue;
					}
					return nxtMeasure.getNote(0, voice);
				}
				return null;
			}
		}
	}
	
	/**
	 * ����һ��������ǰһ������,����������
	 * @param note
	 * @return
	 */
	public static AbstractNote preNote(AbstractNote note){
		return preNote(note, false);
	}
	
	/**
	 * ����һ��������ǰһ������
	 * @param note ��ǰ����
	 * @param grace �Ƿ�������
	 * @return
	 */
	public static AbstractNote preNote(AbstractNote note, boolean grace){
		//������
		if(note instanceof Grace || note instanceof ChordGrace){
			AbstractNote anote = note instanceof Grace ? ((Grace)note).getNote() : ((ChordGrace)note).getNote();
			//������
			if(note.getX() < anote.getX()){
				if(grace){
					int index = anote.getLeftGraces().indexOf(note);
					AbstractNote re = index == 0 ? preNote(anote) : anote.getLeftGraces().get(index-1);
					return re;
				}else{
					return preNote(anote);
				}
			}
			//������
			else{
				if(grace){
					int index = anote.getLeftGraces().indexOf(note);
					AbstractNote re = index == 0 ? anote : anote.getLeftGraces().get(index-1);
					return re;
				}else{
					return anote;
				}
			}
		}
		//��ͨ����
		else{
			if(grace && !note.getLeftGraces().isEmpty()){
				return note.getLeftGraces().get(note.getLeftGraces().size()-1);
			}
			
			Measure measure = note.getMeasure();
			int noteIndex = measure.noteIndex(note);
			int voice = note.getVoice();
			//����С�ڵ�һ������
			if(noteIndex > 0){
				return measure.getNote(noteIndex - 1, voice);
			}
			else{
				MeasurePart measurePart = measure.getMeasurePart();
				int meaIndex = measurePart.measureIndex(measure);
				MeasurePart nxtPart = MusicMath.preMeasurePart(measurePart);
				while(nxtPart != null){
					Measure preMeasure = nxtPart.getMeasure(meaIndex);
					if(preMeasure.getNoteNum(voice) == 0){
						nxtPart = MusicMath.preMeasurePart(nxtPart);
						continue;
					}
					return preMeasure.getNote(preMeasure.getNoteNum(voice)-1, voice);
				}
				return null;
			}
		}
	}
	
	/**
	 * ����С�������һ��С����
	 * û���򷵻�null
	 * @param meaPart
	 * @return
	 */
	public static MeasurePart nxtMeasurePart(MeasurePart meaPart){
		NoteLine line = meaPart.getNoteLine();
		int meaPartIndex = line.getMeaPartList().indexOf(meaPart);
		//�����е����һ��С����
		if(meaPartIndex < line.getMeaPartList().size()-1){
			return line.getMeaPartList().get(meaPartIndex + 1);
		}
		else{
			NoteLine nxtLine = MusicMath.nxtLine(line);
			if(nxtLine != null)
				return nxtLine.getMeaPartList().get(0);
			else 
				return null;
		}
	}
	
	/**
	 * ����һ��С�����ǰһ��С����
	 * @param meaPart
	 * @return
	 */
	public static MeasurePart preMeasurePart(MeasurePart meaPart){
		NoteLine line = meaPart.getNoteLine();
		int meaPartIndex = line.getMeaPartList().indexOf(meaPart);
		//�����еĵ�һ��С����
		if(meaPartIndex > 0){
			return line.getMeaPartList().get(meaPartIndex - 1);
		}
		else{
			NoteLine nxtLine = MusicMath.preLine(line.getPage().getScore(), line);
			if(nxtLine != null)
				return nxtLine.getMeaPartList().get(nxtLine.getMeaPartList().size()-1);
			else 
				return null;
		}
	}
	
	/**
	 * ������ĳһ�е�ǰһ�У�����ǵ�һҳ��һ���򷵻ؿ�.
	 * @param score
	 * @param line
	 * @return
	 */
	public static NoteLine preLine(Score score, NoteLine line){
		NoteLine preLine = null;
		Page page = line.getPage();
		int lineIndex = page.getNoteLines().indexOf(line);
		if(lineIndex > 0){
			preLine = page.getNoteLines().get(lineIndex - 1);
		}
		else{
			int pageIndex = score.getPageList().indexOf(page);
			if(pageIndex > 0){
				Page prePage = score.getPageList().get(pageIndex - 1);
				preLine = prePage.getNoteLines().get(prePage.getNoteLines().size() - 1);
			}
		}
		return preLine;
	}
	
	/**
	 * ����������ĳ��С�����ǰһ��С����
	 * ����ǵ�һҳ�ĵ�һ��С���飬�򷵻ؿ�
	 * @param score
	 * @param measurePart
	 * @return
	 */
	public static MeasurePart preMeasurePart(Score score, MeasurePart measurePart){
		MeasurePart prePart = null;
		
		NoteLine line = measurePart.getNoteLine();
		
		int meaPartIndex = line.getMeaPartList().indexOf(measurePart);
		if(meaPartIndex > 0){
			prePart = line.getMeaPartList().get(meaPartIndex - 1);
		}
		else{
			NoteLine preLine = preLine(score, line);
			if(preLine != null){
				int meaPartNum = preLine.getMeaPartList().size();
				prePart = preLine.getMeaPartList().get(meaPartNum - 1);
			}
		}
		
		return prePart;
	}
	
	/**
	 * ����������ĳҳ����һҳ
	 * @param score
	 * @param page
	 * @return
	 */
	public static Page nxtPage(Score score, Page page){
		int pageIndex = score.getPageList().indexOf(page);
		if(pageIndex >= score.getPageList().size() - 1)
			return null;
		else 
			return score.getPageList().get(pageIndex + 1);
	}
	
	/**
	 * ���������Ƿ���ͬһ����
	 * @param note1
	 * @param note2
	 * @return
	 */
	public static boolean inSameLine(AbstractNote note1, AbstractNote note2){
		if(note1 == note2)
			return true;
		NoteLine line1 = note1.getMeasure().getMeasurePart().getNoteLine();
		NoteLine line2 = note2.getMeasure().getMeasurePart().getNoteLine();
		return line1 == line2;
	}
	
	public static boolean measureInSameLine(MeasurePart mPart1, MeasurePart mPart2){
		if(mPart1 == mPart2)
			return true;
		NoteLine line1 = mPart1.getNoteLine();
		NoteLine line2 = mPart2.getNoteLine();
		return line1 == line2;
	}
	
	/**
	 * �������������ʱ��(�����������������)
	 * @param inputNotes ��������
	 * @return
	 */
	public static int shortestDur(ArrayList<AbstractNote> inputNotes){
		int div = 1024;
		for(int i = 0; i < inputNotes.size(); i++){
			int temp = inputNotes.get(i).getDuration();
			if(temp < div) div = temp;
		}
		return div;
	}
	
	/**
	 * �������������е��������
	 * @param list
	 * @return
	 */
	public static int highestPitch(ArrayList<AbstractNote> list){
		int hpitch = -100;
		for(int i = 0, n = list.size(); i < n; i++){
			AbstractNote note = list.get(i);
			if(note instanceof Note){
				Note nnote = (Note)note;
				if(nnote.getPitch() > hpitch)
					hpitch = nnote.getPitch();
			}
			else if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				int chpitch = cnote.getHighestNote().getPitch();
				if(chpitch > hpitch)
					hpitch = chpitch;
			}
		}
		return hpitch;
	}
	
	/**
	 * ��������������������ߵĵ�����
	 * @param list
	 * @return
	 */
	public static Note getHighestNote(ArrayList<AbstractNote> list){
		Note result = list.get(0).getHighestNote();
		for(int i = 0, n = list.size(); i < n; i++){
			AbstractNote note = list.get(i);
			if(note instanceof Note){
				Note nnote = (Note)note;
				if(nnote.getPitch() > result.getPitch())
					result = nnote;
			}
			else if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				if(cnote.getHighestNote().getPitch() > result.getPitch())
					result = cnote.getHighestNote();
			}
		}
		return result;
	}
	
	/**
	 * ��������������������͵ĵ�����
	 * @param list
	 * @return
	 */
	public static Note getLowestNote(ArrayList<AbstractNote> list){
		Note result = list.get(0).getLowestNote();
		for(int i = 0, n = list.size(); i < n; i++){
			AbstractNote note = list.get(i);
			if(note instanceof Note){
				Note nnote = (Note)note;
				if(nnote.getPitch() < result.getPitch())
					result = nnote;
			}
			else if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				if(cnote.getLowestNote().getPitch() < result.getPitch())
					result = cnote.getLowestNote();
			}
		}
		return result;
	}
	
	/**
	 * �������������е��������
	 * @param list
	 * @return
	 */
	public static int lowestPitch(ArrayList<AbstractNote> list){
		int hpitch = 1000;
		for(int i = 0, n = list.size(); i < n; i++){
			AbstractNote note = list.get(i);
			if(note instanceof Note){
				Note nnote = (Note)note;
				if(nnote.getPitch() < hpitch)
					hpitch = nnote.getPitch();
			}
			else if(note instanceof ChordNote){
				ChordNote cnote = (ChordNote)note;
				int chpitch = cnote.getLowestNote().getPitch();
				if(chpitch < hpitch)
					hpitch = chpitch;
			}
		}
		return hpitch;
	}
	
	/**
	 * �ҳ����������п��ܹ�����ܵ�������
	 * ע�⣺ �÷������ص������鲻������ֹ�����Լ������ܾ��з��ܵ���������4������
	 * @param slist ������������
	 * @return
	 */
	public static ArrayList<ArrayList<AbstractNote>> pickNoteTeams(List<AbstractNote> slist){
		ArrayList<ArrayList<AbstractNote>> list = new ArrayList<ArrayList<AbstractNote>>();
		ArrayList<AbstractNote> temp = new ArrayList<AbstractNote>();
		int accumDur = 0;
		for(int i = 0, n = slist.size(); i < n; i++){
			AbstractNote note = slist.get(i);
			//�������Ա��ֶ�ָ������ָ�
			if(note.getBeamType().equalsIgnoreCase("begin") && !temp.isEmpty()){
				list.add(temp);
				temp = new ArrayList<AbstractNote>();
				temp.add(note);
				accumDur = note.getDurationWithDot();
			}
			//���������ܾ��з��ܵ�����
			else if(note.getBeamType().equalsIgnoreCase("none") || note.getDuration() >= 64){
				if(!temp.isEmpty()){
					list.add(temp);
					temp = new ArrayList<AbstractNote>();
					accumDur = 0;
				}
			}
			else{
				temp.add(note);
				accumDur += note.getDurationWithDot();
			}
			//��������������ָ�
			if(accumDur == 64 || accumDur == 128 || accumDur == 192 || accumDur == 256){
				if(!note.getBeamType().equalsIgnoreCase("continue") && temp.size() > 2){
					list.add(temp);
					temp = new ArrayList<AbstractNote>();
					accumDur = 0;
				}
			}
			//���һ���������һ���������Ϊ��
			if(i == slist.size()-1 && !temp.isEmpty()){
				list.add(temp);
			}
		}
		return list;
	}
	
	/**
     * ͨ�����������ߣ��˶ȣ����ף���С���׺ţ����������ڱ������е����ߣ����������������е�λ��
     * �����׺Ź�ʽΪ: (octave - 4) * 7 + (step - 'c' + 5) + (line - 2) * 2
     * �����׺Ź�ʽΪ: (octave - 2) * 7 + (step - 'c' + 3) + (line - 4) * 2
     * ����lineΪ�׺�"/"֮��Ĳ���
     * @param octave �˶�
     * @param step ����
     * @param clefType �׺�,��ʽ��"high/2"
     * @return
     */
	/**
	 * ��Чֵ�У� "g/2", "g1u/2","g2u/2","g1d/2","g/1","f/4","f1u/4","f2u/4","f1d/4"
	 * "f2d/4", "f/3","f/5","c/3","c/1"	,"c/2","c/4","c/5"
	 * 
	 */
    public static int getNotePitchByRealPitch(int octave, String step, String clefType){
    	String firstPart = clefType.split("/")[0];
    	int secondPart = Integer.parseInt(clefType.split("/")[1]);
    	if(firstPart.equalsIgnoreCase("g")){
    		if(secondPart == 2){
    			int result = (octave - 5) * 7 + (stepDiffInOctave(step, "c") + 5) + (secondPart - 2) * 2;
    			return result;
    		}else if(secondPart == 1){
    			int result = (octave - 5) * 7 + (stepDiffInOctave(step, "c") + 5) + (secondPart - 3) * 2;
    			return result;
    		}
    		
    	}else if(firstPart.equalsIgnoreCase("g1u")){
    		int result = (octave - 6) * 7 + (stepDiffInOctave(step, "c") + 5) + (secondPart - 2) * 2;
    		return result;
    	}else if(firstPart.equalsIgnoreCase("g2u")){
    		int result = (octave - 7) * 7 + (stepDiffInOctave(step, "c") + 5) + (secondPart - 2) * 2;
    		return result;
    	}else if(firstPart.equalsIgnoreCase("g1d")){
    		int result = (octave - 4) * 7 + (stepDiffInOctave(step, "c") + 5) + (secondPart - 2) * 2;
    		return result;
    	}else if(firstPart.equalsIgnoreCase("g")){
    		int result = (octave - 5) * 7 + (stepDiffInOctave(step, "c") + 5) + (secondPart - 2) * 2;
    		return result;
    	}
    	else if(firstPart.equalsIgnoreCase("f")){
    		if(secondPart == 4){
    			int result = (octave - 3) * 7 + (stepDiffInOctave(step, "c") + 3) + (secondPart - 4) * 2;
    			return result;
    		}else if(secondPart == 3){
    			int result = (octave - 3) * 7 + (stepDiffInOctave(step, "c") + 3) + (secondPart - 5) * 2;
    			return result;
    		}else if(secondPart == 5){
    			int result = (octave - 3) * 7 + (stepDiffInOctave(step, "c") + 3) + (secondPart - 3) * 2;
    			return result;
    		}
    	}else if(firstPart.equalsIgnoreCase("f1u/4")){
    		int result = (octave - 4) * 7 + (stepDiffInOctave(step, "c") + 3) + (secondPart - 4) * 2;
    		return result;
    	}else if(firstPart.equalsIgnoreCase("f2u/4")){
    		int result = (octave - 5) * 7 + (stepDiffInOctave(step, "c") + 3) + (secondPart - 4) * 2;
    		return result;
    	}else if(firstPart.equalsIgnoreCase("f1d/4")){
    		int result = (octave - 2) * 7 + (stepDiffInOctave(step, "c") + 3) + (secondPart - 4) * 2;
    		return result;
    	}else if(firstPart.equalsIgnoreCase("f2d/4")){
    		int result = (octave - 1) * 7 + (stepDiffInOctave(step, "c") + 3) + (secondPart - 4) * 2;
    		return result;
    	}
    	else if(firstPart.equalsIgnoreCase("c")){
    		if(secondPart == 3){
        		int result = (octave - 4) * 7 + (stepDiffInOctave(step, "c") + 4) + (secondPart - 3) * 2;
        		return result;
    		}else if(secondPart == 1){
        		int result = (octave - 4) * 7 + (stepDiffInOctave(step, "c") + 4) + (secondPart - 5) * 2;
        		return result;
    		}else if(secondPart == 2){
        		int result = (octave - 4) * 7 + (stepDiffInOctave(step, "c") + 4) + (secondPart - 4) * 2;
        		return result;
    		}else if(secondPart == 4){
        		int result = (octave - 4) * 7 + (stepDiffInOctave(step, "c") + 4) + (secondPart - 2) * 2;
        		return result;
    		}else if(secondPart == 4){
        		int result = (octave - 4) * 7 + (stepDiffInOctave(step, "c") + 4) + (secondPart - 1) * 2;
        		return result;
    		}

    	}
    	return -1;
    }
    
    /**
     * ������ͬһ���˶��ڣ�����a������b�ߵĽ���. 
     * @param a ����a,Ϊ���ַ��ַ���,��ЧֵΪa, b, c, d, e, f, g
     * @param b ����b,Ϊ���ַ��ַ���,��ЧֵΪa, b, c, d, e ,f ,g
     * @return
     */
    public static int stepDiffInOctave(String a, String b){
    	a = a.toLowerCase();
    	b = b.toLowerCase();
    	char cha = a.toCharArray()[0];
    	char chb = b.toCharArray()[0];
    	if(cha == 'a' || cha == 'b')
    		cha += 7;
    	if(chb == 'a' || chb == 'b'){
    		chb += 7;
    	}
    	return cha - chb;
    }
    
    /**
     *�����������ͷ���ʱ��(4������Ϊ64)
     * @param type ��������(MusicXML�ĵ���׼)
     * @return
     */
    public static int noteDurationByType(String type){
    	if(type.equalsIgnoreCase("whole"))
    		return 256;
    	else if(type.equalsIgnoreCase("half"))
    		return 128;
    	else if(type.equalsIgnoreCase("quarter"))
    		return 64;
    	else if(type.equalsIgnoreCase("eighth"))
    		return 32;
    	else if(type.equalsIgnoreCase("16th"))
    		return 16;
    	else if(type.equalsIgnoreCase("32nd"))
    		return 8;
    	else if(type.equalsIgnoreCase("64th"))
    		return 4;
    	else if(type.equalsIgnoreCase("128th"))
    		return 2;
    	else 
    		return -1;
    }
    
    /**
     * ��������ʱ���ж�������
     * @param duration
     * @return
     */
    public static String noteTypeByDuration(int duration){
    	switch(duration){
    	case 256:
    		return "whole";
    	case 128:
    		return "half";
    	case 64:
    		return "quarter";
    	case 32:
    		return "eighth";
    	case 16:
    		return "16th";
    	case 8:
    		return "32nd";
    	case 4:
    		return "64th";
    	case 2:
    		return "128th";
    	default:
    		return null;
    	}
    }
    
    /**
     * ����λ�õĲ��,��line2��line1��������
     * @param line1 ��ʼ��
     * @param line2 ������
     * @return
     */
    public static int NoteLineDiffs(NoteLine line1, NoteLine line2){
    	if(line1 == line2)
    		return 0;
    	
    	int result = 1;
    	NoteLine nxtLine = nxtLine(line1);
    	while(nxtLine != line2){
    		nxtLine = nxtLine(nxtLine);
    		result ++;
    		//��line1�����׽���һֱû�ҵ�line2,��line2��line1֮ǰ
    		if(nxtLine == null)
    			return NoteLineDiffs(line2, line1);
    	}
    	
    	return result;
    }
    
    /**
     * ����С�ڴ��ڵڼ�������
     * @param measure
     * @return
     */
    public static int partIndex(Measure measure){
    	int result = 0;
    	MeasurePart measurePart = measure.getMeasurePart();
    	for(int i = 0, n = measurePart.getMeasureNum(); i < n; i++){
    		Measure mea = measurePart.getMeasure(i);
    		if(mea != measure && mea.getPartName() != null)
    			result++;
    	}
    	return result;
    }
    
    /**
     * ��һ��ʱ���з�Ϊ���ɸ���ֹ������.������˳��Ϊ�Ӵ�С��
     * @param dur
     * @return
     */
    public static ArrayList<Note> splitDurIntoRests(int dur){
    	ArrayList<Note> list = new ArrayList<Note>();
    	int newDur = 256;
    	while(dur > 0 && newDur > 2){
    		if(newDur > dur){
    			newDur /= 2;
    			continue;
    		}
    		Note note = new Note(newDur, false);
    		list.add(note);
    		dur -= newDur;
    		newDur /= 2;
    	}
    	return list;
    }

    /**
     * ����һ��ʱ���������еĸ���ʱ��
     * @param duration
     * @param dotNum
     * @return
     */
    public static int dotDuration(int duration, int dotNum){
    	if(dotNum == 0)
    		return 0;
    	else if(dotNum == 1)
    		return duration / 2;
    	else if(dotNum == 2)
    		return duration /2 + duration / 4;
    	return -1;
    }
    
    /**
     * ���������İ˶�
     * @param note
     * @param measure
     * @return
     */
    public static int getOctave(Note note, Measure measure){
    	int result = 0;
    	if(note.isRest())
    		return -1;
    	else{
    		int pitch = note.getPitch();
    		if(measure.getClefType().equalsIgnoreCase("g/2")){
    			result = (int)Math.floor((35 + pitch - 5) / 7);
    		}
    		else if(measure.getClefType().equalsIgnoreCase("f/4")){
    			result = (int)Math.floor((21 + pitch - 3) / 7);
    		}
    		else if(measure.getClefType().equalsIgnoreCase("c/3")){
    			result = (int)Math.floor((28 + pitch - 4) / 7);
    		}
    	}
    	return result;
    }
    
    /**
     * ��������ı��
     * @param note
     * @param measure
     * @return
     */
    public static int getAlter(Note note, Measure measure){
    	int result = 0;
    	if(note.getSharpOrFlat() != null){
    		String sof = note.getSharpOrFlat().getType();
    		if(sof.equalsIgnoreCase("natural"))
    			result = 0;
    		else if(sof.equalsIgnoreCase("sharp"))
    			result = 1;
    		else if(sof.equalsIgnoreCase("flat"))
    			result = -1;
    		else if(sof.equalsIgnoreCase("double-sharp") || sof.equalsIgnoreCase("sharp-sharp"))
    			result = 2;
    		else if(sof.equalsIgnoreCase("double-flat") || sof.equalsIgnoreCase("flat-flat"))
    			result = -2;
    	}
    	else{
    		String step = getStep(note, measure);
    		int key = measure.getKeyValue();
    		switch(key){
    		case 0:
    			result = 0; 
    			break;
    		case 1:
    			if(step.equalsIgnoreCase("F"))
    				result = 1; 
    			break;
    		case -6:
    			if(step.equalsIgnoreCase("A") || step.equalsIgnoreCase("B") || step.equalsIgnoreCase("C") || 
    					step.equalsIgnoreCase("D") || step.equalsIgnoreCase("E") || step.equalsIgnoreCase("G"))
    				result = -1;
    			break;
    		case -1:
    			if(step.equalsIgnoreCase("B"))
    				result = -1;
    			break;
    		case 6:
    			if(step.equalsIgnoreCase("B") || step.equalsIgnoreCase("F") || step.equalsIgnoreCase("C") || 
    					step.equalsIgnoreCase("D") || step.equalsIgnoreCase("E") || step.equalsIgnoreCase("G"))
    				result = 1;
    			break;
    		case 2:
    			if(step.equalsIgnoreCase("F") || step.equalsIgnoreCase("C"))
    				result = 1; 
    			break;
    		case -5:
    			if(step.equalsIgnoreCase("G") || step.equalsIgnoreCase("A") || step.equalsIgnoreCase("B") || 
    					step.equalsIgnoreCase("D") || step.equalsIgnoreCase("E"))
    				result = -1;
    			break;
    		case -2:
    			if(step.equalsIgnoreCase("E") || step.equalsIgnoreCase("B"))
    				result = -1; 
    			break;
    		case 5:
    			if(step.equalsIgnoreCase("A") || step.equalsIgnoreCase("C") || step.equalsIgnoreCase("D") || 
    					step.equalsIgnoreCase("F") || step.equalsIgnoreCase("G"))
    				result = 1;
    			break;
    		case 3:
    			if(step.equalsIgnoreCase("C") || step.equalsIgnoreCase("F") || step.equalsIgnoreCase("G"))
    				result = 1; 
    			break;
    		case -4:
    			if(step.equalsIgnoreCase("A") || step.equalsIgnoreCase("D") || step.equalsIgnoreCase("E") || 
    					step.equalsIgnoreCase("G"))
    				result = -1;
    			break;
    		case -3:
    			if(step.equalsIgnoreCase("A") || step.equalsIgnoreCase("B") || step.equalsIgnoreCase("E"))
    				result = -1; 
    			break;
    		case 4:
    			if(step.equalsIgnoreCase("C") || step.equalsIgnoreCase("D") || step.equalsIgnoreCase("F") || 
    					step.equalsIgnoreCase("G"))
    				result = 1;
    			break;
    		}
    	}
    	return result;
    }
    
    /**
     * ������������
     * @param note
     * @param measure
     * @return
     */
    public static String getStep(Note note, Measure measure){
    	String clefType = measure.getClefType();
    	int pitch = note.getPitch();
    	int intStep = 0;
    	if(clefType.equalsIgnoreCase("g/2")){
    		intStep = (28+pitch-5) % 7;
    	}else if(clefType.equalsIgnoreCase("f/4")){
    		intStep = (14+pitch-3) % 7;
    	}else if(clefType.equalsIgnoreCase("c/3")){
    		intStep = (21+pitch-4) % 7;
    	}
    	
    	String step = null;
    	switch(intStep){
		case 0: 
			step = "C"; break;
		case 1:
			step = "D"; break;
		case 2:
			step = "E"; break;
		case 3:
			step = "F"; break;
		case 4:
			step = "G"; break;
		case 5:
			step = "A"; break;
		case 6:
			step = "B"; break;
		}
    	return step;
    }
    
    /**
     * ����������MIDI��ֵ������������
     * @param instrument
     * @return
     */
    public static int getPartNumOfInstr(int instrument){
    	switch(instrument){
    	case 0:
    	case 1:
    		return 2;
    	case 33:
    	case 41:
    	case 42:
    	case 43:
    	case 25:
    	case 117:
    		return 1;
    	default:
    		return 1;
    	}
    }
    
    /**
     * ������ָ���������ĸ���������Ĭ���׺�
     * @param instrument
     * @return
     */
    public static ArrayList<String> getClefsOfInstrument(int instrument){
    	ArrayList<String> result = new ArrayList<String>();
    	switch(instrument){
    	case 0:
    	case 1:
    		result.add("g/2");
    		result.add("f/4");
    		break;
    	case 33:
    	case 43:
    		result.add("f/4");
    		break;
    	case 41:
    	case 25:
    		result.add("g/2");
    		break;
    	case 42:
    		result.add("c/3");
    		break;
    	default:
    		result.add("g/2");
    	}
    	return result;
    }
    
    /**
     * ����С�ڵ���ʱ��
     * @param measure
     * @return
     */
    public static int getMeasureDuration(Measure measure){
    	int beats = measure.getTime().getBeats();
    	int beatType = measure.getTime().getBeatType();
    	return 256 * beats / beatType;
    }
    
    public static int getMeasureDuration(Time time){
    	int beats = time.getBeats();
    	int beatType = time.getBeatType();
    	return 256 * beats / beatType;
    }
    
    /**
     * С���ָ��������֮�������е�ʱ���ܺ�(��������������)
     * @param measure
     * @param note
     * @return
     */
    public static int restDuration(Measure measure, AbstractNote note){
    	int index = measure.noteIndex(note);
    	float dur = 0f;
    	int voice = note.getVoice();
    	for(int i = index, n = measure.getNoteNum(voice); i < n; i++){
    		AbstractNote nt = measure.getNote(i, voice);
    		dur += nt.getRealDuration();
    	}
    	return Math.round(dur);
    }
    
    /**
     * ���ؾ���ָ��ʱ�������ķ��ܸ���
     * @param dur
     * @return
     */
    public static int beamNum(int dur){
    	int beamNum = 0;
    	switch(dur){
		case 256/8: beamNum = 1; break;
		case 256/16: beamNum = 2; break;
		case 256/32: beamNum = 3; break;
		case 256/64: beamNum = 4; break;
		}
    	return beamNum;
    }
    
    /**
     * ����һ��list,��list�����������ӵ�һ�������һ�����ܵ�����
     * ��Чֵ�� begin, continue, end, forward hook, backward hook
     * @param note
     * @return
     */
    public static ArrayList<String> getBeamTypes(AbstractNote nnote){
    	ArrayList<String> result = new ArrayList<String>();
    	
    	Beam beam = nnote.getBeam();
    	if(beam == null || !beam.getUiNoteList().contains(nnote))
    		return null;
    	
    	int num = MusicMath.beamNum(nnote.getDuration());
		for(int b = 0; b < num; b++){
			String eleBeam = null;
			
			//����������ĵ�һ��
			if(beam.getUiNoteList().indexOf(nnote) == 0){
				int curIndex = beam.getUiNoteList().indexOf(nnote);
				AbstractNote nxtNote = beam.getUiNoteList().get(curIndex+1);
				if(nxtNote.getDuration() > Beam.getDurByBeamIndex(b))
					eleBeam = "forward hook";
				else
					eleBeam = "begin";
			}
			//��������������һ��
			else if(beam.getUiNoteList().indexOf(nnote) == beam.getUiNoteList().size()-1){
				int curIndex = beam.getUiNoteList().indexOf(nnote);
				AbstractNote preNote = beam.getUiNoteList().get(curIndex-1);
				if(preNote.getDuration() > Beam.getDurByBeamIndex(b))
					eleBeam = "backward hook";
				else
					eleBeam = "end";
			}
			//�Ȳ��ǵ�һ��Ҳ�������һ��
			else{
				int curIndex = beam.getUiNoteList().indexOf(nnote);
				AbstractNote preNote = beam.getUiNoteList().get(curIndex-1);
				AbstractNote nxtNote = beam.getUiNoteList().get(curIndex+1);
				if(preNote.getDuration() > Beam.getDurByBeamIndex(b) && nxtNote.getDuration() > Beam.getDurByBeamIndex(b))
					eleBeam = "forward hook";
				else if(preNote.getDuration() <= Beam.getDurByBeamIndex(b) && nxtNote.getDuration() > Beam.getDurByBeamIndex(b))
					eleBeam = "end";
				else if(preNote.getDuration() > Beam.getDurByBeamIndex(b) && nxtNote.getDuration() <= Beam.getDurByBeamIndex(b))
					eleBeam = "bigin";
				else 
					eleBeam = "continue";
			}
			
			result.add(eleBeam);
		}
		return result;
    }
    
    /**
     * �Ա����������ɱȽ϶��������.
     * @param list1
     * @param list2
     * @return
     */
    public static boolean equals(Equalable[] list1, Equalable[] list2){
    	if(list1.length != list2.length){
    		System.out.println("���鳤�Ȳ�һ��");
    		return false;
    	}
    	int num = list1.length;
    	for(int i = 0; i < num; i++){
    		if(!list1[i].equalsWith(list2[i])){
//    			System.out.println("�����еĵ�λ��һ��" + list1[i].getClass().getName());
    			return false;
    		}
    	}
    	return true;
    }

}

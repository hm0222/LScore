package sjy.elwg.notation.musicBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sjy.elwg.utility.MusicMath;

/**
 * ����
 * @author sjy
 *
 */
public class Score implements Equalable{
	
	/**
	 * ����ģʽ���ֱ�Ϊ��ͨģʽ��������ģʽ��
	 * ��������ģʽ�£�С�ڲ������ĺŵĸ����С���ڿ���ʢ�����������
	 */
	public static int SCORE_NORMAL = 0;
	public static int SCORE_UNLMTED = 1;
	
	
	/**
	 * ҳ�漯��
	 */
	private ArrayList<Page> pageList;
	
	/**
	 * �������Ķ���.
	 * ��Map��keyֵ��ID��valueֵ��һ��list�����б������뵱ǰID����Ӧ�Ŀ�ѡ�����
	 * ��ʵ������ͨ����������̬���õģ��������ײ��ɱ༭ʱ���á������׿ɱ༭ʱ����
	 * �ܻ��޸ĸ�Map�ж��󣬴Ӷ��ﲻ��Ԥ�ڽ��
	 */
	private HashMap<Integer, ArrayList<Selectable>> highlightedObjts;
	
	/**
	 * ��������
	 */
	private int scoreType = SCORE_NORMAL;
	
	/**
	 * �����Ƿ��б���������
	 */
	private boolean hasTitle = false;
	
	/**
	 * ����������ӵ������ı�
	 */
	private List<FreeAddedText> addedTexts = new ArrayList<FreeAddedText>();
	
	
	/**
	 * ���캯��
	 */
	public Score(){
		pageList = new ArrayList<Page>();
		highlightedObjts = new HashMap<Integer, ArrayList<Selectable>>();
	}

	/**
	 * ���ҳ��
	 * @return
	 */
	public ArrayList<Page> getPageList() {
		return pageList;
	}

	/**
	 * ��ø�������Map
	 * @return
	 */
	public HashMap<Integer, ArrayList<Selectable>> getHighlightedObjts() {
		return highlightedObjts;
	}

    /**
	 * ������������
	 * @return
	 */
	public int getScoreType() {
		return scoreType;
	}
	
    /**
	 * ������������
	 * @param mode
	 */
	public void setScoreType(int scoreType) {
		this.scoreType = scoreType;
	}

	/**
	 * �Ƿ��б���
	 * @return
	 */
	public boolean isHasTitle() {
		return hasTitle;
	}

	/**
	 * �����Ƿ��б���
	 * @param hasTitle
	 */
	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}
	
	/**
	 * ������ת��Ϊһ��С��������
	 * @return
	 */
	public ArrayList<MeasurePart> toMeaPartList(){
		ArrayList<MeasurePart> list = new ArrayList<MeasurePart>();
		for(int i = 0; i < pageList.size(); i++){
			Page page = pageList.get(i);
			for(int j = 0; j < page.getNoteLines().size(); j++){
				NoteLine line = page.getNoteLines().get(j);
				for(int k = 0; k < line.getMeaPartList().size(); k++){
					MeasurePart part = line.getMeaPartList().get(k);
					list.add(part);
				}
			}
		}
		return list;
	}

	@Override
	public boolean equalsWith(Object o) {
		// TODO Auto-generated method stub
		if(!(o instanceof Score)){
			return false;
		}
		Score score = (Score)o;
		ArrayList<MeasurePart> parts1 = score.toMeaPartList();
		ArrayList<MeasurePart> parts2 = toMeaPartList();
		if(parts1.size() != parts2.size()){
			System.out.println("score��Ŀ��һ��");
			return false;
		}
		int num = parts1.size();
		MeasurePart[] list1 = new MeasurePart[num];
		MeasurePart[] list2 = new MeasurePart[num];
		
		//��ӡƥ�����
		for(int i = 0; i < parts1.size(); i++){
			MeasurePart part = parts1.get(i);
			for(int j = 0; j < part.getMeasureNum(); j++){
				Measure measure = part.getMeasure(j);
				for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
					for(int k = 0; k < measure.getNoteNum(v); k++){
						AbstractNote anote = measure.getNote(k, v);
						if(!(anote instanceof Note))
							continue;
						Note note = (Note)anote;
						System.out.print(note.getPitch() + " ");
					}
				}
			}
		}
		for(int i = 0; i < parts2.size(); i++){
			MeasurePart part = parts1.get(i);
			for(int j = 0; j < part.getMeasureNum(); j++){
				Measure measure = part.getMeasure(j);
				for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
					for(int k = 0; k < measure.getNoteNum(v); k++){
						AbstractNote anote = measure.getNote(k, v);
						if(!(anote instanceof Note))
							continue;
						Note note = (Note)anote;
						System.out.print(note.getPitch() + " ");
					}
				}
			}
		}
		
		return MusicMath.equals(parts1.toArray(list1), parts2.toArray(list2));
	}

	public List<FreeAddedText> getAddedTexts() {
		return addedTexts;
	}
	
	public void addFreeText(FreeAddedText txt){
		addedTexts.add(txt);
	}
	
	public void removeFreeText(FreeAddedText txt){
		addedTexts.remove(txt);
	}
}

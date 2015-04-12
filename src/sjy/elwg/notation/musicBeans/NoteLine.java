package sjy.elwg.notation.musicBeans;

import java.awt.Container;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JPanel;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.symbolLines.Bracket;
/**
 * ��
 * @author jingyuan.sun
 *
 */
public class NoteLine {
	
	/**
	 * �����ױ�֮���Ĭ�Ͼ���
	 */
	public static final int MEASURE_GAP = 60;
	
	/**
	 * ����һ��֮���Ĭ�Ͼ���
	 */
	public static final int NOTELINE_GAP = 110;
	
	/**
	 * ����ױ������Ϊ10
	 */
	public static final int MAX_STAVES = 10;
	
	/**
	 * С����
	 */
	private ArrayList<MeasurePart> meaPartList;
	/**
	 * �п�ͷ������
	 */
	private Barline frontBarline;
	/**
	 * �����������ż���.������������ͬ��û�����ŵ�����Ϊnull.
	 */
	private ArrayList<Bracket> brackets;
	
	private Page page;
	
	private int x;
	
	private int y;
	
	/**
	 * ���и��ױ��ˮƽ��ʶ��, �������ױ��������С����С�ڸ������
	 */
	private ArrayList<LineMarker> markers;
	
	/**
	 * ���и��ױ���.
	 */
	private ArrayList<Integer> measureGaps;
	
	/**
	 * ����һ��֮��ļ��
	 */
	private int lineGap = NOTELINE_GAP;
	
	/**
	 * ���캯��
	 */
	public NoteLine(){
		meaPartList = new ArrayList<MeasurePart>();
		markers = new ArrayList<LineMarker>();
		measureGaps = new ArrayList<Integer>();
		for(int i = 0; i < MAX_STAVES; i++){
			measureGaps.add(MEASURE_GAP);
		}
		brackets = new ArrayList<Bracket>();
		for(int i = 0; i < MAX_STAVES; i++){
			brackets.add(null);
		}
	}
	
	/**
	 * ����ˮƽ��ʶ��
	 */
	public void locateMarkers(){
		int yy = y;
		for(int i = 0, n = markers.size(); i < n; i++){
			LineMarker marker = markers.get(i);
			marker.setLocation(0, yy);
			yy += measureGaps.get(i) + Measure.MEASURE_HEIGHT;
		}
	}
	
	/**
	 * ����ˮƽ��ʶ��
	 * ��ʶ������Ŀ������������Ŀ��ͬ����������һ��С�����С����Ŀ��ͬ
	 * ��˲�����ʶ����ǰ�������ڲ���Ϊ��.
	 */
	public void generateMarkers(){
		NoteCanvas canvas = (NoteCanvas)page.getParent();
		MeasurePart firstPart = meaPartList.get(0);
		int meaNum = firstPart.getMeasureNum();
		if(markers.size() < meaNum){
			for(int i = 0, n = meaNum - markers.size(); i < n; i++){
				LineMarker marker = new LineMarker();
				markers.add(marker);
				marker.setLine(this);
				page.add(marker);
				marker.addMouseListener(canvas);
				marker.addMouseMotionListener(canvas);
			}
		}
	}
	
	/**
	 * Ϊ�������е��б�ʶ������������ʱ��
	 * @param listener
	 */
	public void addMarkerListener(EventListener listener){
		for(int i = 0, n = markers.size(); i < n; i++){
			LineMarker marker = markers.get(i);
			MouseListener lsn = (MouseListener)listener;
			marker.addMouseListener(lsn);
			MouseMotionListener mlsn = (MouseMotionListener)listener;
			marker.addMouseMotionListener(mlsn);
		}
	}
	
	/**
	 * ɾ��ˮƽ��ʶ��
	 */
	public void deleteMarkers(){
		if(markers.size() == 0)
			return;
		LineMarker firstMarker = markers.get(0);
		Container container = firstMarker.getParent();
		for(int i = 0, n = markers.size(); i < n; i++){
			LineMarker marker = markers.get(i);
			container.remove(marker);
		}
		JPanel panel = (JPanel)firstMarker;
		panel.revalidate();
		panel.updateUI();
		markers.clear();
	}

	/**
	 * ���С���鼯��
	 * @return
	 */
	public ArrayList<MeasurePart> getMeaPartList() {
		return meaPartList;
	}

	/**
	 * ������ڵ�ҳ
	 * @return
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * ����ҳ
	 * @param page
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * ���x����
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * ����x����
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * ���y����
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * ����y����
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * ��ø�������һ��֮��ļ��
	 * @return
	 */
	public int getLineGap() {
		return lineGap;
	}

	/**
	 * ���ø�������һ�е��м��
	 * @param lineGap
	 */
	public void setLineGap(int lineGap) {
		this.lineGap = lineGap;
	}

	/**
	 * ���ˮƽ��ʶ��
	 * @return
	 */
	public ArrayList<LineMarker> getMarkers() {
		return markers;
	}

	/**
	 * �����ױ�������
	 * @return
	 */
	public ArrayList<Integer> getMeasureGaps() {
		return measureGaps;
	}

	/**
	 * ������ż���
	 * @return
	 */
	public ArrayList<Bracket> getBrackets() {
		return brackets;
	}

	/**
	 * �趨��ҳ�е�λ��
	 */
	public void determineLocation(){
		int lineIndex = page.getNoteLines().indexOf(this);
		if(lineIndex == 0){
			setX(NoteCanvas.xStart);
			setY(page.getNoteLineYStart());
		}else{
			NoteLine preLine = page.getNoteLines().get(lineIndex - 1);
			setX(NoteCanvas.xStart);
			setY(preLine.getY() + preLine.getHeight() + preLine.getLineGap());
		}
	}
	
	/**
	 * ��������ռ�ĸ߶�
	 * ע�⣺�ø߶�ָ�Ӹ��е�һ���ױ��˵����һ���ױ�׶˵ĸ߶ȣ����������н���֮������һ��֮��Ŀհ�
	 * @return
	 */
	public int getHeight(){
		int result = 0;
		int meaNum = meaPartList.get(0).getMeasureNum();
		for(int i = 0; i < meaNum - 1; i++){
			result += Measure.MEASURE_HEIGHT + measureGaps.get(i);
		}
		result += Measure.MEASURE_HEIGHT;
		return result;
	}
	
	/**
	 * ���ظ��е���С�߶�.������Ĭ���ױ�����и�
	 * @return
	 */
	public int getMinHeight(){
		int meaNum = meaPartList.get(0).getMeasureNum();
		return meaNum * Measure.MEASURE_HEIGHT + (meaNum-1) * MEASURE_GAP;
	}
	
	/**
	 * ���ظ�������С����ʱ��۵���Ŀ�ܺ�
	 * @return
	 */
	public int getTimeSlotNum(){
		int result = 0;
		for(MeasurePart meaPart : meaPartList){
			int num = meaPart.getNoteListByTimeSlot().size();
			result += num;
		}
		return result;
	}
	
	/**
	 * ��������ʼ����
	 */
	public void generateFrontBarlineLine(){
		if(frontBarline == null){
			frontBarline = new Barline("regular");
		}
		
		if(frontBarline.getParent() != page){
			page.add(frontBarline);
		}
	}
	
	/**
	 * ��������ʼ��
	 */
	public void locateFrontBarline(){
		
		int height = 0;
		int meaNum = meaPartList.get(0).getMeasureNum();
		
		if(frontBarline != null){
			for(int i = 0, n = meaNum; i < n; i++){
				height += measureGaps.get(i) + Measure.MEASURE_HEIGHT;
			}
			height -= measureGaps.get(meaNum - 1);
			frontBarline.setSize(2, height);
			frontBarline.setLocation(x, y);
		}
		
	}
	
	/**
	 * ������������
	 * @return
	 */
	public int getPartNum(){
		MeasurePart fmeaPart = meaPartList.get(0);
		int result = 0;
		for(int i = 0; i < fmeaPart.getMeasureNum(); i++){
			Measure measure = fmeaPart.getMeasure(i);
			if(measure.getInstrument() != null)
				result++;
		}
		return result;
	}
	
	/**
	 * �������������������ױ������
	 * @param partIndex
	 * @return
	 */
	public int getStavesInPart(int partIndex){
		if(partIndex >= getPartNum()){
			return 0;
		}
		MeasurePart fmeaPart = meaPartList.get(0);
		int num = 0;
		int result = 1;
		for(int i = 0; i < fmeaPart.getMeasureNum(); i++){
			Measure measure = fmeaPart.getMeasure(i);
			if(measure.getInstrument() != null)
				num++;
			else if(num == partIndex+1){
				result++;
			}
		}
		return result;
	}
	
	/**
	 * ���ظ�����ĳ���ױ�ĸ�ʶ�������ֵ,��ĳ�����������е����������.
	 * @param meaIndex �ױ�ID
	 * @return
	 */
	public int getMaxLyricNum(int meaIndex){
		int max = 0;
		for(int i = 0, n = meaPartList.size(); i < n; i++){
			MeasurePart meaPart = meaPartList.get(i);
			Measure measure = meaPart.getMeasure(meaIndex);
			for(int v = 0, vn = measure.getVoiceNum(); v < vn; v++){
				for(int j = 0, jn = measure.getNoteNum(v); j < jn; j++){
					AbstractNote anote = measure.getNote(j, v);
					if(anote.getLyricsNum() > max)
						max = anote.getLyricsNum();
				}
			}
		}
		return max;
	}
	
	/**
	 * ָ���ױ�ID�������м��
	 * @param meaIndex ��ʾһ�еĵڼ�������
	 * @param flag �Ƿ��м��Ƚϴ�ʱ���Ƿ������Զ�����
	 */
	public void adjustGap(int meaIndex, boolean flag){
		int meaNum = meaPartList.get(0).getMeasureNum();
		int maxLyric = getMaxLyricNum(meaIndex);
		int deltay = maxLyric * (Lyrics.LYRIC_FONT.getSize() + 3);
		if(meaIndex == meaNum - 1){
			lineGap = lineGap < deltay + NoteCanvas.LINE_GAP ? deltay + NoteCanvas.LINE_GAP : lineGap;
		}else{
			int formerGap = measureGaps.get(meaIndex);
			int newGap;
			if(!flag)
				newGap = formerGap >= MEASURE_GAP + deltay ? formerGap : MEASURE_GAP + deltay;
			else
				newGap = MEASURE_GAP + deltay;
			measureGaps.set(meaIndex, newGap);
		}
	}
	
	/**
	 * ��ÿ���ױ��������
	 * @param flag �Ƿ��м��Ƚϴ�ʱ���Ƿ������Զ�����
	 */
	public void adjustGap(boolean flag){
		int meaNum = meaPartList.get(0).getMeasureNum();
		
		for(int i = 0; i < meaNum; i++){
			adjustGap(i, flag);
		}
	}
	
	/**
	 * ��������
	 */
	public void locateBrackets(){
		MeasurePart fmeaPart = meaPartList.get(0);
		
		int instrIndex = -1;
		for(int i = 0, n = fmeaPart.getMeasureNum(); i < n; i++){
			Measure measure = fmeaPart.getMeasure(i);
			if(measure.getInstrument() != null){
				instrIndex++;
				Bracket bk = brackets.get(instrIndex);
				if(bk != null){
					int num = getStavesInPart(instrIndex);
					int hheight = 0;
					for(int j = 0; j < num - 1; j++){
						hheight += Measure.MEASURE_HEIGHT + measureGaps.get(i + j);
					}
					hheight += Measure.MEASURE_HEIGHT;
					bk.setSize(bk.getWidth(), hheight);
					bk.setLocation(x - bk.getWidth(), measure.getY());
				}
			}
		}
	}
	
	/**
	 * ��������
	 */
	public void generateBrackets(){
		deleteBrackets();
		int partNum = getPartNum();
		for(int i = 0; i < partNum; i++){
			int staffNum = getStavesInPart(i);
			if(staffNum > 1){
				Bracket bk = new Bracket();
				getBrackets().add(bk);
				getPage().add(bk);
			}else{
				getBrackets().add(null);
			}
		}
	}
	
	/**
	 * ɾ������
	 */
	public void deleteBrackets(){
		if(getBrackets().size() != 0){
			for(Bracket bk : getBrackets()){
				if(bk != null)
					getPage().remove(bk);
			}
			getBrackets().clear();
		}
	}
	
	/**
	 * ɾ����ǰ��
	 */
	public void deleteFrontLine(){
		if(frontBarline != null){
			getPage().remove(frontBarline);
		}
	}

	public Barline getFrontBarline() {
		return frontBarline;
	}

}

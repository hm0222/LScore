package sjy.elwg.notation.musicBeans;

import java.util.ArrayList;

/**
 * �ĺ�ʵ�壬������ͨ�ĺź������ĺţ���ͨ�ĺ�ָ�ĺ������ֱ�Ӵ��ڵİ�ť
 * ��ӵ��ĺš������ĺ�ָͨ���ĺ�����ұߵ������ĺ�����������������Ӷ�
 * �γɵ��ĺţ������ĺ��п������ɻ���ĺţ���2+2+3/8���ӣ�Ҳ�������ɺ���
 * ͨ�ĺ��е���������һ����������ͨ�ĺ���û�е��ĺţ�����12/16�ġ�
 * @author sjy
 *
 */
public class Time {
	/**
	 * ��ͨ�ĺŵ�����
	 */
	private int beats;
	/**
	 * ������
	 */
	private int beatType;
	
	/**
	 * �����ĺŵ�����
	 */
	private int[] beat = new int[4];
	
	/**
	 * ��ͨ�ĺŵĹ��캯��
	 * @param beats
	 * @param beatType
	 */
	public Time(int beats, int beatType){
		this.beats = beats;
		this.beatType = beatType;
	}
	/**
	 * �����ĺŵĹ��캯��
	 * @param beat
	 * @param beatType
	 */
	public Time(int[] beat, int beatType){
		this.beat = beat;		
		for(int i = 0; i < 4; i++){
			beats += beat[i];
		}
		this.beatType = beatType;
	}
	
	public Time(Time time){
		this.beat = time.getBeat();
		this.beats = time.getBeats();
		this.beatType = time.getBeatType();
	}
	
	
	

	/**
	 * �������
	 * @return
	 */
	public int getBeats() {
		return beats;
	}

	/**
	 * ��������
	 * @param beats
	 */
	public void setBeats(int beats) {
		this.beats = beats;
	}

	/**
	 * ��ý�������
	 * @return
	 */
	public int getBeatType() {
		return beatType;
	}

	/**
	 * ���ý�������
	 * @param beatType
	 */
	public void setBeatType(int beatType) {
		this.beatType = beatType;
	}
	
	/**
	 * ��дObject��equals����,�������ĺ��߼��Ƚϣ��Ӷ��ж��Ƿ����.
	 * ���������beats��beatType�����.
	 */
	public boolean equals(Object o){
		if(o == this)
			return true;
		if(!(o instanceof Time))
			return false;
		Time time = (Time)o;
		
		//���߾�Ϊ��ͨ�ĺţ��Ƚ�beats��beatType����
		if(beat[0] == 0 && time.getBeat()[0] == 0){
			return time.getBeats() == beats && time.getBeatType() == beatType;
		}
		//��ǰ�ĺ�Ϊ��ͨ�ĺţ���Ƚϵ��ĺ�Ϊ�����ĺţ���������ĺŵĵ�һ�����ӵ�����ͨ�ĺŵ�beats�������������ӵĵڶ������������Ӷ�Ϊ0�����������
		else if(beat[0] == 0 && time.getBeat()[0] != 0){
			if(time.getBeat()[0] == beats && time.getBeatType() == beatType){
				if(time.getBeat()[1] == 0 && time.getBeat()[2] == 0 && time.getBeat()[3] == 0){
					return true;
				}else{
					return false;
				}
				
			}else{
				return false;
			}
		}
		//��ǰ�ĺ�Ϊ�����ĺţ���Ƚϵ��ĺ�Ϊ��ͨ�ĺţ��ȽϷ���ͬ��
		else if(beat[0] != 0 && time.getBeat()[0] == 0){
			if(beat[0] == time.getBeats() && beatType == time.getBeatType()){
				if(beat[1] == 0 && beat[2] == 0 && beat[3] == 0){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}		
		}
		//���߶�Ϊ�����ĺţ��Ƚ�ÿ�������Ƿ����
		else if(beat[0] !=0 && time.getBeat()[0] != 0){

			ArrayList<Integer> arrayList1 = new ArrayList<Integer>();
			ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
			for(int i = 0; i < 4; i++){
				if(beat[i] != 0){
					arrayList1.add(beat[i]);
				}
			}
			for(int i = 0; i < 4; i++){
				if(time.getBeat()[i] != 0){
					arrayList2.add(time.getBeat()[i]);
				}
			}
			if(arrayList1.equals(arrayList2)){
				return true;
			}else{
				return false;
			}
			
 		}else{
			return false;
		}	
	}
	
	/**
	 * ���ڸ�д��equals����������Ҫ��дɢ��ֵ����.
	 */
	public int hashCode(){
		int result = 17;
		result = 37 * result + beats;
		result = 37 * result + beatType;
		return result;
	}

	public int[] getBeat() {
		return beat;
	}

	public void setBeat(int[] beat) {
		this.beat = beat;
	}

}

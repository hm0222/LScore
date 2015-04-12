package sjy.elwg.notation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.ChordNote;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.Score;
import sjy.elwg.utility.MusicMath;

public class MIDIPlayer extends Thread implements MetaEventListener{
	
	/**
	 * ����
	 */
	private NoteCanvas canvas;
	/**
	 * ������
	 */
	private Sequencer sequencer;
	
	/**
	 * ������������ĸ�����Ĭ��Ϊ1
	 */
	private int trackNum = 1;
	
	/**
	 * MIDI�¼����м�.ÿ����Ԫ��ͬ����list,�洢һ���ױ���һ�������MIDI�¼�
	 */
	private ArrayList<ArrayList<MidiEvent>> midiEvents;
	/**
	 * �洢meta�¼�
	 */
	private ArrayList<MidiEvent> metaEvents;
	/**
	 * ����
	 */
	private int volume = 100;
	
	/**
	 * �ķ�����ռ�ĳ���(����)
	 */
	private int QuarterNoteTick = 75;
	
	/**
	 * �����ٶ�
	 */
	private int tempo = 60;
	
	
	/**
	 * ���캯��
	 * @param score
	 */
	public MIDIPlayer(NoteCanvas canvas){
		this.canvas = canvas;
		Score score = canvas.getScore();
		trackNum = score.getPageList().get(0).getNoteLines().get(0).getMeaPartList().get(0).getMeasureNum();
		midiEvents = new ArrayList<ArrayList<MidiEvent>>();
		metaEvents = new ArrayList<MidiEvent>();
		for(int i = 0; i < trackNum; i++){
			ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();
			midiEvents.add(events);
		}
	}
	
	/**
	 * �������׵��������ϡ�
	 * ���ص�list��Ŀ����������������ͬ���䰴��˳��洢��ÿ������������.
	 * @return
	 */
	public ArrayList<Integer> getInstruments(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		Score score = canvas.getScore();
		MeasurePart firstMeaPart = score.getPageList().get(0).getNoteLines().get(0).getMeaPartList().get(0);
		int curInstr = 0;
		for(int i = 0, n = firstMeaPart.getMeasureNum(); i < n; i++){
			Measure measure = firstMeaPart.getMeasure(i);
			if(measure.getInstrument() == null)
				result.add(curInstr);
			else{
				int ins = Integer.valueOf(measure.getInstrument());
				result.add(ins);
				curInstr = ins;
			}
		}
		return result;
	}

	/**
	 * �������ף�����MIDI�¼�,����1
	 */
	public void makeMidi(){
		canvas.genPlayingNoteSequence();
		List<List<MeasurePart.NListWithMeaIndex>> tickList = canvas.getNoteSequence();
		//ʱ���
		float tickPosition = 0f;
		for(int i = 0, in = tickList.size(); i < in; i++){
			List<MeasurePart.NListWithMeaIndex> list = tickList.get(i);
			//����ʱ���
			int intTick = Math.round(tickPosition);
			
			//�����¼�
			for(MeasurePart.NListWithMeaIndex mpw : list){
				int meaIndex = mpw.getMeaIndex();
				for(AbstractNote note : mpw.getList()){
					Measure measure = note.getMeasure();
					//��ͨ����
					if(note instanceof Note){
						Note nnote = (Note)note;
						if(!nnote.isRest() && !nnote.isEndOfTie()){
							int midiPitch = getMidiPitch(nnote, measure);
							setNoteEvent(midiEvents.get(meaIndex), Math.round(nnote.getRealDuration()), midiPitch, intTick);
						}
					}
					//��������
					else if(note instanceof ChordNote){
						ChordNote cnote = (ChordNote)note;
						for(int q = 0, qn = cnote.getNoteNum(); q < qn; q++){
							Note nnote = cnote.getNote(q);
							int midiPitch = getMidiPitch(nnote, measure);
							setNoteEvent(midiEvents.get(meaIndex), Math.round(nnote.getRealDuration()), midiPitch, intTick);
						}
					}
				}
			}
			
			//meta�¼�
			setMetaEvent(metaEvents, 0, intTick);
			
			//��ǰʱ����ĸ���������Сʱ�����Ա������һ��ʱ�����ʱ��λ��
			float shortestDur = 256f;
			for(int j = 0, jn = list.size(); j < jn; j++){
				MeasurePart.NListWithMeaIndex mpw = list.get(j);
				float dur = mpw.getList().get(0).getRealDuration();
				for(int k = 1; k < mpw.getList().size(); k++){
					float tempDur = mpw.getList().get(k).getRealDuration();
					if(tempDur < dur){
						dur = tempDur;
					}
				}
				if(dur < shortestDur)
					shortestDur = dur;
			}
			tickPosition += shortestDur;
		}
	}
	
	/**
	 * �������ף�����MIDI�¼�,����2
	 */
	public void makeMidiMusic(){
		canvas.genPlayingNoteSequence();
		List<MeasurePart> meaPartList = canvas.getScore().toMeaPartList();
		//С����ʱ�����ʱ�����С��Ϊ��λУ׼����ÿ��ʼһ���µ�С���飬����С����������
		//С�ڵ�ʱ�������ͳһ���Է�ֹ�������ȷ��������ʱ���������ۻ���
		int time = 0;
		Set<Integer> metaSet = new HashSet<Integer>(); 
		for(int i = 0, n = meaPartList.size(); i < n; i++){
			MeasurePart measurePart = meaPartList.get(i);
			for(int j = 0, jn = measurePart.getMeasureNum(); j < jn; j++){
				Measure measure = measurePart.getMeasure(j);
				List<MidiEvent> list = midiEvents.get(j);
				makeMeasureMidi(measure, list, time, metaSet);
			}
			time += MusicMath.getMeasureDuration(measurePart.getMeasure(0));
		}
	}
	
	private void makeMeasureMidi(Measure measure, List<MidiEvent> list, int time, Set<Integer> set){
		int timeStamp = time;
		float realStamp = (float)time;
		for(int i = 0; i < measure.getVoiceNum(); i++){
			for(int k = 0; k < measure.getNoteNum(i); k++){
				AbstractNote note = measure.getNote(k, i);
				int dur = Math.round(note.getRealDuration());
				//��ͨ����
				if(note instanceof Note){
					Note nnote = (Note)note;
					if(!nnote.isRest() && !nnote.isEndOfTie()){
						int midiPitch = getMidiPitch(nnote, measure);
						setNoteEvent(list, dur, midiPitch, timeStamp);
					}
				}
				//��������
				else if(note instanceof ChordNote){
					ChordNote cnote = (ChordNote)note;
					for(int q = 0, qn = cnote.getNoteNum(); q < qn; q++){
						Note nnote = cnote.getNote(q);
						if (!nnote.isEndOfTie()) {
							int midiPitch = getMidiPitch(nnote, measure);
							setNoteEvent(list, dur, midiPitch, timeStamp);	
						}
					}
				}
				if(!set.contains(timeStamp)){
//					System.out.println("meta: " + timeStamp);
					setMetaEvent(metaEvents, 0, timeStamp);
					set.add(timeStamp);
				}
				realStamp += note.getRealDuration();
				timeStamp = Math.round(realStamp);
			}
			timeStamp = time;
		}
	}
	
	/**
	 * ��������
	 */
	public void playSequence(){
//        makeMidi();
        makeMidiMusic();
		
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
		} catch (MidiUnavailableException ex) {
			sequencer = null;
			ex.printStackTrace();
		}

		Sequence seq = null;
		try {
			seq = new Sequence(Sequence.PPQ, QuarterNoteTick);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		
		//�ȹ������1�����ڴ洢meta�¼�. meta�¼��洢�ڵ�һ�����
		for(int i = 0; i < trackNum+1; i++){
			seq.createTrack();
		}
		Track[] tracks = seq.getTracks();
		
		//������������¼�
		ArrayList<Integer> instruments = getInstruments();
		for(int i = 1, n = tracks.length; i < n; i++){
			ShortMessage instrumentMsg = new ShortMessage();
			try{
				instrumentMsg.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instruments.get(i-1), 0);
//				instrumentMsg.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 11, 0);
			}catch(Exception e){
				e.printStackTrace();
			}
			MidiEvent ev = new MidiEvent(instrumentMsg, 0);
			tracks[i].add(ev);
		}
		
		//�����������
		for(int i = 1, n = tracks.length; i < n; i++){
			ArrayList<MidiEvent> events = midiEvents.get(i-1);
			for(int j = 0, jn = events.size(); j < jn; j++){
				tracks[i].add(events.get(j));
			}
		}
		
		//���meta�¼�
		for(int i = 0, n = metaEvents.size(); i < n; i++){
			tracks[0].add(metaEvents.get(i));
		}
		System.out.println("mestaEvent size: " + metaEvents.size());
		
		try {
			sequencer.setSequence(seq);
            sequencer.addMetaEventListener(this);
			sequencer.setTempoInBPM(tempo);

			sequencer.startRecording();
			Thread.sleep(sequencer.getMicrosecondLength() / 1000 * 3);
			System.out.println("�߳�����ʱ�䵽");
			sequencer.stop();
			sequencer.close();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} 
		catch(InterruptedException ee){
			sequencer.stop();
			sequencer.close();
		}
	}
	
	
	/**
	 * ���meta�¼�
	 * @param events meta�¼�����
	 * @param type �¼�����
	 * @param time ʱ���
	 */
	public void setMetaEvent(ArrayList<MidiEvent> events, int type, int time){
		MetaMessage metamessage = new MetaMessage();
		byte[] data = new byte[1];
		data[0] = 0;
		try {
			metamessage.setMessage(type, data, 0);
		} catch (InvalidMidiDataException x) {
			x.printStackTrace();
		}
		MidiEvent event = new MidiEvent(metamessage, time);
		events.add(event);
	}
	
	/**
	 * ��ĳһ�������������MIDI�����¼�
	 * 
	 * @param events ����ӵ�MIDI�¼����У�����һ������
	 * @param duration ����ʱ��
	 * @param pitch �������߻����MIDI��׼���ֵ
	 * @param time �¼���ʱ���
	 */
	public void setNoteEvent(List<MidiEvent> events, int duration, int pitch, int time){
		ShortMessage message = new ShortMessage();
		ShortMessage messageOff = new ShortMessage();
		try{
            message.setMessage(ShortMessage.NOTE_ON, 0, pitch, volume);
            messageOff.setMessage(ShortMessage.NOTE_OFF, 0, pitch, volume);    
        }
        catch (InvalidMidiDataException e)
        {
            e.printStackTrace();
        }
        events.add(new MidiEvent(message, time));
        time += duration;
        events.add(new MidiEvent(messageOff, time));
	}
	
	/**
	 * ����������С�ڣ�����������MIDI����ֵ
	 * @param note
	 * @param measure
	 * @return
	 */
	public int getMidiPitch(Note note, Measure measure){
		int octave = MusicMath.getOctave(note, measure);
		String step = MusicMath.getStep(note, measure);
		int alter = MusicMath.getAlter(note, measure);
		
		int stepInt = 0;
		if(step.equals("C"))
        {
            stepInt = 0;
        }else
        if(step.equals("D"))
        {
            stepInt = 2;
        }else
        if(step.equals("E"))
        {
            stepInt = 4;
        }else
        if(step.equals("F"))
        {
            stepInt = 5;
        }else
        if(step.equals("G"))
        {
            stepInt = 7;
        }else
        if(step.equals("A"))
        {
            stepInt = 9;
        }else
        if(step.equals("B"))
        {
            stepInt = 11;
        }
		
		stepInt += alter;
		int resultPitch = (octave+1) * 12 + stepInt;
		
		return resultPitch;
	}
	
	/**
	 * �߳̿�ʼ. ����
	 */
	public void run(){
		playSequence();
	}

	@Override
	public void meta(MetaMessage meta) {
		// TODO Auto-generated method stub
		canvas.shiftPlayingNote();
	}

	public int getQuarterNoteTick() {
		return QuarterNoteTick;
	}

	public void setQuarterNoteTick(int quarterNoteTick) {
		QuarterNoteTick = quarterNoteTick;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

}

package sjy.elwg.notation.action;

import sjy.elwg.notation.musicBeans.AbstractNote;
import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Note;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Score;
import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;
import sjy.elwg.utility.MeaPartStrategy;
import sjy.elwg.utility.NoteLineStrategy;
import sjy.elwg.utility.PageStrategy;

/**
 * ɾ��С�ڲ���
 * @author jingyuan.sun
 */
public class DelMeasureAction extends Action{

	//�ڴ�С��֮��ɾ��С��
	private MeasurePart preMeasurePart;

	//��ǰС��
	private MeasurePart measurePart;

	public DelMeasureAction(Controller controller){
		super(controller);
	}

	public DelMeasureAction(MeasurePart preMeasurePart, MeasurePart measurePart, Controller controller){
		super(controller);
		this.measurePart = measurePart;
		this.preMeasurePart = preMeasurePart;
	}

	public void redo(){
		//UI Obj
		controller.deleteMeasure(measurePart, false);
		//redraw
		NoteCanvas canvas = controller.getCanvas();
		Score score = canvas.getScore();
		NoteLine line = preMeasurePart == null ? score.getPageList().get(0).getNoteLines().get(0) : preMeasurePart.getNoteLine();
		Page page = line.getPage();
		PageStrategy ps = new PageStrategy(new NoteLineStrategy(new MeaPartStrategy(NoteCanvas.X_MIN_DIST, false), false), true);//����������һҳ
		while(canvas.redrawPage(page, ps)){
			page = MusicMath.nxtPage(canvas.getScore(), page);
		}
	}

	public void undo(){
		NoteCanvas canvas = controller.getCanvas();
		Score score = canvas.getScore();
		//���preMeasurePart��null����˵����ɾ���������׵�һ��С��
		NoteLine line = preMeasurePart == null ? score.getPageList().get(0).getNoteLines().get(0) : preMeasurePart.getNoteLine();
		Page page = line.getPage();
		int index = line.getMeaPartList().indexOf(preMeasurePart);
		line.getMeaPartList().add(index + 1, measurePart);
		//UI Objects
		page.addMeasurePartAndSymbols(measurePart);
		PageStrategy ps = new PageStrategy(new NoteLineStrategy(new MeaPartStrategy(NoteCanvas.X_MIN_DIST, false), false), false);
		while(canvas.redrawPage(page, ps)){
			page = MusicMath.nxtPage(canvas.getScore(), page);
		}
	}

}

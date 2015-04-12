package sjy.elwg.notation.action;

import sjy.elwg.notation.musicBeans.Measure;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;
import sjy.elwg.notation.NoteCanvas;

/**
 * �ı���Ų���
 * @author jingyuan.sun
 *
 */
public class ChangeMeaKeyAction extends Action{

	private int oldKey; //�ϵ���

	private int newKey; //�µ���

	private MeasurePart measurePart; //�ı����ʼС����

	private int meaIndex; //С����С������ߵ�index

	public ChangeMeaKeyAction(Controller controller){
		super(controller);
	}

	public ChangeMeaKeyAction(int oldKey, int newKey, MeasurePart measurePart, int meaIndex, Controller controller){
		super(controller);
		this.oldKey = oldKey;
		this.newKey = newKey;
		this.meaIndex = meaIndex;
		this.measurePart = measurePart;
	}

	public void redo(){
		controller.changeKey(measurePart, meaIndex, newKey);
		//�ػ�
		NoteCanvas canvas = controller.getCanvas();
		NoteLine line = measurePart.getNoteLine();
		while(line != null){
			canvas.redrawLine(line);
			line = MusicMath.nxtLine(line);
		}
	}

	public void undo(){
		controller.changeKey(measurePart, meaIndex, oldKey);
		//�ػ�
		NoteCanvas canvas = controller.getCanvas();
		NoteLine line = measurePart.getNoteLine();
		while(line != null){
			canvas.redrawLine(line);
			line = MusicMath.nxtLine(line);
		}
	}

}

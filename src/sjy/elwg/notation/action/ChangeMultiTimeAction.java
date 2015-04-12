package sjy.elwg.notation.action;

import java.util.ArrayList;
import java.util.List;

import sjy.elwg.notation.NoteCanvas;
import sjy.elwg.notation.musicBeans.MeasurePart;
import sjy.elwg.notation.musicBeans.NoteLine;
import sjy.elwg.utility.Controller;
import sjy.elwg.utility.MusicMath;

/**
 * �ı���С���ĺŶ���
 * @author jingyuan.sun
 *
 */
public class ChangeMultiTimeAction extends Action{
	
	private MeasurePart meaPart; //�ı��ĺŵ���ʼС��
	
	private List<ChangeMeaTimeAction> singleActionList = new ArrayList<ChangeMeaTimeAction>();

	public ChangeMultiTimeAction(MeasurePart meaPart, Controller controller) {
		super(controller);
		this.meaPart = meaPart;
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		for(int i = 0, n = singleActionList.size(); i < n; i++){
			ChangeMeaTimeAction a = singleActionList.get(i);
			a.redo();
		}
		redraw();
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		for(int i = 0, n = singleActionList.size(); i < n; i++){
			ChangeMeaTimeAction a = singleActionList.get(i);
			a.undo();
		}
		redraw();
	}
	
	private void redraw(){
		NoteLine line = meaPart.getNoteLine();
		NoteCanvas canvas = controller.getCanvas();
		while(line != null){
			canvas.redrawLine(line);
			line = MusicMath.nxtLine(line);
		}
	}
	
	public void addTimeAction(ChangeMeaTimeAction action){
		singleActionList.add(action);
	}
	
	public int singleActionSize(){
		return singleActionList.size();
	}

}

package sjy.elwg.utility;

import sjy.elwg.notation.action.Action;

/**
 * �û��Ĳ�����ʷ������
 * ���ڳ���/�س�����
 * 
 * @author jingyuan.sun
 *
 */
public class ActionHistoryController {
	
	private LmtStack<Action> undoStack = new LmtStack<Action>();
	
	private LmtStack<Action> redoStack = new LmtStack<Action>();
	
	private volatile static ActionHistoryController controller;
	
	public static ActionHistoryController getInstance(){
		if(controller == null){
			synchronized(ActionHistoryController.class){
				if(controller == null){
					controller = new ActionHistoryController();
				}
			}
		}
		return controller;
	}
	
	/**
	 * ������ѹջ
	 * @param action
	 */
	public void pushAction(Action action){
		undoStack.push(action);
	}
	
	/**
	 * ����
	 */
	public void undo(){
		Action a = undoStack.poll();
		if(a != null){
			redoStack.push(a);
			a.undo();
		}
	}
	
	/**
	 * �س�
	 */
	public void redo(){
		Action a = redoStack.poll();
		if(a != null){
			undoStack.push(a);
			a.redo();
		}
	}

}

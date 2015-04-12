package sjy.elwg.notation.action;

import sjy.elwg.utility.Controller;

/**
 * ���ж������׵Ĳ����ĸ���
 * �ϸ����������������в�����������Ҫ�ṩ����/�س����ܵĲ���
 * @author jingyuan.sun
 *
 */
public abstract class Action {
	
	protected Controller controller;
	
	public Action(Controller controller){
		this.controller = controller;
	}
	
	/**
	 * ����
	 */
	public abstract void undo();
	
	/**
	 * �س�
	 */
	public abstract void redo();

}

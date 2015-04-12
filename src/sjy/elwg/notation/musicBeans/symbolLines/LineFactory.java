package sjy.elwg.notation.musicBeans.symbolLines;


/**
 * �������Ź�����
 * @author sjy
 *
 */
public class LineFactory {
	
	/**
	 * �������ִ����������Ŷ���
	 * @param name 
	 * @return
	 */
	public static SymbolLine generateSymbolLine(int name){
		if(name == AbstractLine.SLUR)
			return new Slur();
		else if(name == AbstractLine.CRE)
			return new Cre(null);
		else if(name == AbstractLine.DIM)
			return new Dim(null);
//		else if(name == AbstractSymbolLine.PEDAL)
//			return new Pedal(null);
		else if(name == AbstractLine.OCTAVE_DOWN)
			return new OctaveDown(null);
		else if(name == AbstractLine.OCTAVE_UP)
			return new OctaveUp(null);
		else if(name == AbstractLine.VIB)
			return new Vibrato(null);
		else if(name == AbstractLine.DIMC)
			return new Dimc(null);
		else if(name == AbstractLine.CRESC)
			return new Cresc(null);
		
		else 
			return null;
	}
	

	
//	public static RepeatLine generateRepeatLine(int name){
//		if(name == AbstractLine.REPEATENDING)
//			return new RepeatEnding(null);
//		
//			
//		else
//			return null;
//	}
}

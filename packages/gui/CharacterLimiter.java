package gui;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class CharacterLimiter extends PlainDocument{
		
	private final int limit;
	
	public CharacterLimiter(int limit){
		this.limit = limit;
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) 
							 throws BadLocationException{
		if(str == null) return;
		
		if(getLength() + str.length() <= limit)
			super.insertString(offs,str,a);		
	}
	
}
package main;

import java.awt.Insets;
import java.awt.Cursor;
import javax.swing.JButton;


//This button is leaving artifacts everytime this
//button is in a panel with transparent background.
//artifacts appear during hover event.
//
//To solve this artifacts problem. override paintComponent
//of this component and enable transparency via setOpaque()
//method.
public class UndecoratedButtonFactory{
	
	private UndecoratedButtonFactory(){}
	
	public static JButton createButton(boolean nowrap, String text, String size){
		JButton btn = null;
		
		if(nowrap)
			btn = new JButton("<html><a style='text-decoration:underline;white-space:nowrap;'>"+
						      text+"</a></html>");
		else
			btn = new JButton("<html><a style='text-decoration:underline;'>"+
						      text+"</a></html>");
		
		btn.setFocusPainted(false);
		btn.setMargin(new Insets(0,0,0,0));
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setOpaque(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		if(size != null)
			btn.setFont(btn.getFont().
		    deriveFont(btn.getFont().getStyle(),Integer.parseInt(size)));
		
		return btn;
	}
}
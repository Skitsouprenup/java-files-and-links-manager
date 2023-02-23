package gui;

import java.awt.Font;

public interface DefaultFont{
	Font font = javax.swing.UIManager.getDefaults().getFont("Label.font");
	
	static String getFontName(){ return font.getFontName(); }
	static int getStyle(){ return font.getStyle(); }
}
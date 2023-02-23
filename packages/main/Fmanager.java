package main;

import java.awt.EventQueue;
import gui.GUIComponents;

public class Fmanager {
	
	public static void main(String[] args){
		new Fmanager();
	}
	
	private Fmanager(){
		
		EventQueue.invokeLater( () -> GUIComponents.instantiate());
	}
}
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//Singleton class
class GUIActions extends GUIEventSource {
	private final ActionListener btnListener;
	private final ActionListener guiBtnListener;
	private final ActionListener settingsMenuListener;
	private final ActionListener editMenuListener;
	private final ActionListener toolsMenuListener;
	
	private static boolean isInstantiated;
	private static GUIActions instance;
	
	private GUIActions(){
		btnListener = new BtnAction();
		guiBtnListener = new GUIBtnAction();
		settingsMenuListener = new SettingsMenuAction();
		editMenuListener = new EditMenuAction();
		toolsMenuListener = new ToolsMenuAction();
	}
	
	static GUIActions instantiate(){
		if(!isInstantiated){
			instance = new GUIActions();
			isInstantiated = true;
		}
		return instance;
	}
	
	ActionListener getBtnListener(){
	    return btnListener;
	}
	
	ActionListener getGuiBtnListener(){
		return guiBtnListener;
	}
	
	ActionListener getSettingsMenuListener(){
		return settingsMenuListener;
	}
	
	ActionListener getEditMenuListener(){
		return editMenuListener;
	}
	
	ActionListener getToolsMenuListener(){
		return toolsMenuListener;
	}
	
	//GUI related events
	//This ActionListener changes the state of GUI components via
	//special button type like radiobutton, checkbox, etc.
	private class GUIBtnAction implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			guiBtnActSource(e);
		}
	}
	
	//program's components related actions
	//These ActionListener listens to actions that
	//commence program's main operations
	//like compress, decompress, file generation, etc.
	private class BtnAction implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		   try{ btnActSource(e); }
		   catch(IOException f){ f.printStackTrace(); }
		}
	}
	
	private class SettingsMenuAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			settingsMenuActSource(e);
		}
	}
	
	private class EditMenuAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			editMenuActSource(e);
		}
	}
	
	private class ToolsMenuAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			toolsMenuActSource(e);
		}
	}
	
}
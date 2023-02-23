package gui.events;

import gui.subpanels.FileViewPanel;

public abstract class FileViewPEvents implements IEventSource{
	
	@Override
	public boolean checkSource(Object source, Object component){
		FileViewPanel fvp = checkPanelType(component);
		
		for(int i = 0 ; i < fvp.getSubPnlBtnsLn(); i++)
			if(source == fvp.getSubPnlBtn(i)) return true;
		return false;
	}
	
	//gui_related_events are unnecessary in this panel
	//That's why this overriding method immediately returns false
	@Override
	public boolean checkGUIEvtSource(Object source, Object component){ return false; }
	
	protected FileViewPanel checkPanelType(Object instance){
		
		if(instance instanceof FileViewPanel)
			return (FileViewPanel)instance;
		else throw new NullPointerException("instance is incompatible with FileViewPanel!");
	}
}
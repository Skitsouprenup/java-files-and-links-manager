package gui.events;

import gui.subpanels.filetree.FileTreePopup;

public abstract class TreePopupEvents implements IEventSource{
	
	@Override
	public boolean checkSource(Object source, Object component){
		FileTreePopup ftp = null;
		if(component instanceof FileTreePopup)
			ftp = (FileTreePopup)component;
		else throw new NullPointerException("instance is incompatible with FileTreePopup!");
		
		for(int i = 0 ; i < ftp.getTreePopupOptionsLn(); i++)
			if(source == ftp.getTreePopupOption(i)) return true;
		return false;
	}
	
	//gui_related_events are unnecessary in this panel
	//That's why this overriding method immediately returns false
	@Override
	public boolean checkGUIEvtSource(Object source, Object component){ return false; }
}
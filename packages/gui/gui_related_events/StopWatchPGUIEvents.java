package gui.gui_related_events;

import gui.subpanels.StopwatchPanel;

public abstract class StopWatchPGUIEvents implements IGUIEvents{
	
	@Override
	public boolean checkSource(Object source,Object component){
		StopwatchPanel swp = checkPanelType(component);
		
		if(source == swp.getSubPnlCb()) return true;
		return false;
	}
	
	@Override
	public void fireEvent(Object source,Object component){}
	
	protected StopwatchPanel checkPanelType(Object instance){
		
		if(instance instanceof StopwatchPanel)
			return (StopwatchPanel)instance;
		else throw new NullPointerException("instance is incompatible with StopwatchPanel!");
	}
	
}
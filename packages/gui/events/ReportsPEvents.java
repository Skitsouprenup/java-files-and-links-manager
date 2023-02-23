package gui.events;

import gui.subpanels.ReportsPanel;

public abstract class ReportsPEvents implements IEventSource{
	
	@Override
	public boolean checkSource(Object source, Object component){
		ReportsPanel rp = checkPanelType(component);
		
		for(int i = 0 ; i < rp.getSubPnlBtnsLn(); i++)
			if(source == rp.getSubPnlBtn(i)) return true;
		return false;
	}
	
	//gui_related_events are unnecessary in this panel
	//That's why this overriding method immediately returns false
	@Override
	public boolean checkGUIEvtSource(Object source, Object component){ return false; }
	
	protected ReportsPanel checkPanelType(Object instance){
		
		if(instance instanceof ReportsPanel)
			return (ReportsPanel)instance;
		else throw new NullPointerException("instance is incompatible with ReportsPanel!");
	}
}
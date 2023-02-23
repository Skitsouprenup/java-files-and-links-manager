package gui.gui_related_events;

import gui.subpanels.LinksPanel;

public abstract class LinksPGUIEvents implements IGUIEvents{
	
	@Override
	public boolean checkSource(Object source,Object component){
		LinksPanel lp = checkPanelType(component);
		
		if(source == lp.getSubPnlRb(0) || 
		   source == lp.getSubPnlRb(1)) return true;
		return false;
	}
	
	@Override
	public void fireEvent(Object source,Object component){
		LinksPanel lp = checkPanelType(component);
		
		if(source == lp.getSubPnlRb(0))
				lp.getSubPnlCb().setVisible(false);
			
		else if(source == lp.getSubPnlRb(1))
				lp.getSubPnlCb().setVisible(true);
	}
	
	protected LinksPanel checkPanelType(Object instance){
		
		if(instance instanceof LinksPanel)
			return (LinksPanel)instance;
		else throw new NullPointerException("instance is incompatible with LinksPanel!");
	}
}
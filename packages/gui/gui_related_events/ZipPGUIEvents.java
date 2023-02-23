package gui.gui_related_events;

import gui.subpanels.ZipPanel;

public abstract class ZipPGUIEvents implements IGUIEvents{
	@Override
	public boolean checkSource(Object source,Object component){
		ZipPanel zp = checkPanelType(component);
		
		if(source == zp.getSubPnlRb(2)|| 
		   source == zp.getSubPnlRb(3)) return true;
		return false;
	}
	
	@Override
	public void fireEvent(Object source,Object component){
		ZipPanel zp = checkPanelType(component);
		
		if(source == zp.getSubPnlRb(2))
			zp.getSpnlCb(1).setVisible(true);
		else if(source == zp.getSubPnlRb(3))
			zp.getSpnlCb(1).setVisible(false);
	}
	
	protected ZipPanel checkPanelType(Object instance){
		
		if(instance instanceof ZipPanel)
			return (ZipPanel)instance;
		else throw new NullPointerException("instance is incompatible with ZipPanel!");
	}
}
package gui.events;

import static main.UtilityClasses.*;

import gui.gui_related_events.ZipPGUIEvents;
import gui.subpanels.ZipPanel;

public abstract class ZipPEvents extends ZipPGUIEvents implements IEventSource{
	
	@Override
	public boolean checkSource(Object source,Object component){
		ZipPanel zp = checkPanelType(component);
		
		for(int i = 0 ; i < zp.getSubPnlBtnsLn(); i++)
			if(source == zp.getSubPnlBtn(i)) return true;
		return false;
	}
	
	@Override
	public boolean checkGUIEvtSource(Object source, Object component){
		return super.checkSource(source,component);
	}
	
	public String createZipMessage(ZipPanel zp, ZipPackRef zType){
		StringBuilder selectedJob = new StringBuilder();
		String selectionType = null;
		   
		if(zp.getSubPnlRb(0).isSelected()){ 
			selectionType = "Per Selected Directory";
		    zType.setValue(ZipPackType.PER_DIR);
		}
		else if(zp.getSubPnlRb(1).isSelected()){ 
		    selectionType = "Single Package";
		    zType.setValue(ZipPackType.SINGLE_PACKAGE);
		}
		selectedJob.append("Compress Files<br>");
		selectedJob.append("<b>Compression Type:</b> " + zp.getSpnlJcb().getSelectedItem().toString() + "<br>");
		selectedJob.append("<b>Selection Type:</b> " + selectionType + "<br>");
		if(zp.getSpnlCb(2).isSelected())
			selectedJob.append("<b>Exclude Collection Directory:</b> Yes");
		else selectedJob.append("<b>Exclude Collection Directory:</b> No");
		selectedJob.append("<br>");
		if(zp.getSpnlCb(0).isSelected())
			selectedJob.append("<b>Include Sub-Directory:</b> Yes");
		else selectedJob.append("<b>Include Sub-Directory:</b> No");
		
		return selectedJob.toString();
	}
	
	public String createUnzipMessage(ZipPanel zp, UnzipPackRef uType){
		StringBuilder selectedJob = new StringBuilder();
		String selectionType = null;
		String includeSub = null;
				
		if(zp.getSubPnlRb(2).isSelected()){ 
		   selectionType = "Directory";
		   uType.setValue(UnzipPackType.DIRECTORY);
		   if(zp.getSpnlCb(1).isSelected())
			   includeSub = "Yes";
		   else includeSub = "No";
		}
		else if(zp.getSubPnlRb(3).isSelected()){ 
		   selectionType = "Zip File";
		   uType.setValue(UnzipPackType.ZIP_FILE);
		}
		selectedJob.append("Decompress Files<br>");
		selectedJob.append("<b>Selection Type:</b> " + selectionType);
		if(includeSub != null) selectedJob.append("<br><b>Include Sub-Directories:</b> " + includeSub);
		
		return selectedJob.toString();
	}
}
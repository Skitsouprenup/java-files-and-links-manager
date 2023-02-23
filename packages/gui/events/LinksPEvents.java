package gui.events;

import static main.UtilityClasses.SelectionType;
import static main.UtilityClasses.SelectionLinkRef;

import fileoperations.LinkOperations;
import gui.gui_related_events.LinksPGUIEvents;
import gui.subpanels.LinksPanel;

import main.PreferencesConfig;

import java.awt.Component;

import javax.swing.JOptionPane;

public abstract class LinksPEvents extends LinksPGUIEvents implements IEventSource{
	
	@Override
	public boolean checkSource(Object source, Object component){
		LinksPanel lp = checkPanelType(component);
		
		for(int i = 0 ; i < lp.getSubPnlBtnsLn(); i++)
			if(source == lp.getSubPnlBtn(i)) return true;
		return false;
	}
	
	@Override
	public boolean checkGUIEvtSource(Object source, Object component){
		return super.checkSource(source,component);
	}
	
	public String createMessage(LinksPanel lp, SelectionLinkRef sType,Component mainFrame){
		StringBuilder msg = new StringBuilder();
		
		if(lp.getSubPnlJta().getText().equals("")){
			JOptionPane.showMessageDialog(mainFrame,"No links in the text area!","No links",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		msg.append("<b>Selected Operation:</b> ");
		msg.append("Put Links");
		
		if(lp.getSubPnlRb(0).isSelected()){ 
			msg.append("<br><b>Selection Type:</b> Single");
			sType.setValue(SelectionType.SINGLE);
		}
		else if(lp.getSubPnlRb(1).isSelected()){ 
			msg.append("<br><b>Selection Type:</b> Multiple");
			sType.setValue(SelectionType.MULTIPLE);
			msg.append("<br>");
			if(lp.getSubPnlCb().isSelected())
			   msg.append("<b>Include Sub-Directories?:</b> Yes");
			else msg.append("<b>Include Sub-Directories?:</b> No");
			
		}
		msg.append("<br><b>Current File Generation Preset:</b> "+
				   PreferencesConfig.getPrefsConfig().getCurrentPresetName());
		msg.append("<br><b>Supported Host Name/s</b><br>");
		java.util.ArrayList<String> suppHosts = 
		PreferencesConfig.getPrefsConfig().getCurrentSupportedHosts();
		for(String s : suppHosts)
			msg.append(s + "<br>");
		
		msg.append("<br>");
		return msg.toString();
	}
	
	public String checkLinks(StringBuilder validLinks, LinksPanel lp, Component mainFrame){
		String[] splitLinks = lp.getSubPnlJta().getText().split("\n");
		
		StringBuilder msg = new StringBuilder();
		StringBuilder invalidLinks = new StringBuilder();
		
		StringBuilder hostnames = new StringBuilder();
		if(!PreferencesConfig.verifyAndGetHostNames(mainFrame, hostnames, null)){
			lp.getSubPnlJta().setText("");
			return null;
		}
		//System.out.println(hostnames.toString());
		
		for(String link : splitLinks){
							
			//ignore a line of text with only new line(\n or \r\n) 
			//I think java is not adding these characters to the total length
			//of a string
			//Note: This solution is not ony limited to \n or \r\n. It can also
			//ignore other "non-character" characters.
			if(link.length() == 0) continue;
								
			if(LinkOperations.validLink(link,hostnames.toString()))
				validLinks.append(link + "<br>");
			else invalidLinks.append(link + "<br>");
		}
		
		msg.append("<div style='white-space:nowrap;'>");		
		if(validLinks.length() != 0){
			msg.append("<b>Valid Links</b><br>");
			msg.append(validLinks.toString());
			msg.append("<br>");
		}
		else{
			JOptionPane.showMessageDialog(mainFrame,"No Valid Links!","No Valid Links",JOptionPane.ERROR_MESSAGE);
			//lp.getSubPnlJta().setText("");
			return null;
		}
		
		if(invalidLinks.length() != 0){
			msg.append("<b>Invalid Links</b><br>");
			msg.append(invalidLinks.toString());
			msg.append("<br>");
		}
		msg.append("</div>");
		
		return msg.toString();
	}
	
}
package gui.events;

import static main.UtilityClasses.Jobs;
import static main.UtilityClasses.JobRef;

import gui.subpanels.FileGenPanel;
import main.PreferencesConfig;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class FileGenPEvents implements IEventSource{
	
	@Override
	public boolean checkSource(Object source, Object component){
		FileGenPanel fgp = checkPanelType(component);
		
		for(int i = 0 ; i < fgp.getSubPnlBtnsLn(); i++)
			if(source == fgp.getSubPnlBtn(i)) return true;
		return false;
	}
	
	//gui_related_events are unnecessary in this panel
	//That's why this overriding method immediately returns false
	@Override
	public boolean checkGUIEvtSource(Object source, Object component){ return false; }
	
	//This method can be used to replace message in the reports panel if the preset changes
	//happens while reports panel has selected "Create post.txt" operation
	public String updateCreatePostMessage(FileGenPanel fgp){
		StringBuilder message = new StringBuilder();
		
		message.append("<b>Selected Operation:</b> ");
		message.append("Create post.txt<br>");
		message.append("<b>Post Type:</b> " + fgp.getSubPnlJcb().getSelectedItem()+"<br>");
			
		if(fgp.getSubPnlCb(0).isSelected())
			message.append("<b>Include Collections?</b> Yes<br>");
		else message.append("<b>Include Collections?</b> No<br>");
			
		if(fgp.getSubPnlCb(1).isSelected())
			message.append("<b>Include Sub-Directories?</b> Yes<br>");
		else message.append("<b>Include Sub-Directories?</b> No<br>");
			
		message.append("<br><b>Current File Generation Preset:</b> "+
				       PreferencesConfig.getPrefsConfig().getCurrentPresetName());
		message.append("<br><b>Supported Host Name/s</b><br>");
		java.util.ArrayList<String> suppHosts = 
		PreferencesConfig.getPrefsConfig().getCurrentSupportedHosts();
		for(String s : suppHosts)
			message.append(s + "<br>");
		
		message.append("<br>");
		return message.toString();
	}
	
	public String createMessage(Object source, FileGenPanel fgp, 
								JFrame mainFrame, JobRef job){
		StringBuilder selectedJob = new StringBuilder();
		StringBuilder subMsg = new StringBuilder();
		
		if(fgp.getSubPnlCb(1).isSelected())
			 subMsg.append("<b>Include Sub-Directories?</b> Yes<br>");
		else subMsg.append("<b>Include Sub-Directories?</b> No<br>");
		
		if(source == fgp.getSubPnlBtn(0)){
			if(!PreferencesConfig.notifyAndLoadPrefsConfig(mainFrame) ||
			   !PreferencesConfig.defaultCurrentPresetVerification(mainFrame,null))
			  return null;
			
			job.setValue(Jobs.CREATE_POST_TXT);
			selectedJob.append(updateCreatePostMessage(fgp));
			
		}
		else if(source == fgp.getSubPnlBtn(1)){
			if(fgp.getSubPnlTf().getText().isEmpty() && 
			   fgp.getSubPnlTf().getText().isBlank()){
			   JOptionPane.showMessageDialog(
					mainFrame,"Threshold is empty!",
					"Empty Textfield",JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			int threshold = 0;
			try{ 
				threshold = Integer.valueOf(fgp.getSubPnlTf().getText());
				if(threshold < 0){
					JOptionPane.showMessageDialog(mainFrame,"Threshold is less than 0!",
											  "Negative Number Not Allowed",JOptionPane.ERROR_MESSAGE);
					fgp.getSubPnlTf().setText("5");
					return null;
				}
			}
			catch(NumberFormatException e){
				//e.printStackTrace();
				JOptionPane.showMessageDialog(mainFrame,"Threshold is not a number!",
											  "Not A Number",JOptionPane.ERROR_MESSAGE);
				fgp.getSubPnlTf().setText("5");
				return null;
			}
			
			job.setValue(Jobs.CREATE_DUMMY_FILE);
			selectedJob.append("<b>Selected Operation:</b> ");
			selectedJob.append("Create dummy_file<br>");
			selectedJob.append("<b>Threshold:</b> " + threshold + " mb");
			selectedJob.append("<br>"+subMsg);
		}
		else if(source == fgp.getSubPnlBtn(2)){ 
			job.setValue(Jobs.DELETE_DUMMY_FILE);
			selectedJob.append("<b>Selected Operation:</b> ");
			selectedJob.append("Delete dummy_file");
			selectedJob.append("<br>"+subMsg);
		}
		
		return selectedJob.toString();
	}
	
	protected FileGenPanel checkPanelType(Object instance){
		
		if(instance instanceof FileGenPanel)
			return (FileGenPanel)instance;
		else throw new NullPointerException("instance is incompatible with FileGenPanel!");
	}
}
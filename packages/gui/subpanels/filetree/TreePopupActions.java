package gui.subpanels.filetree;

import static gui.subpanels.filetree.FileTreePopup.DeleteOption;
import static gui.subpanels.filetree.FileTreePopup.SelectOption;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import gui.subpanels.ReportsPanel;

//I decided to separate this event from GUIActions
//That's why the event design of TreePopupMenu is different 
class TreePopupActions implements ActionListener{
	private final FileTreePopup ftp;
	private final ReportsPanel rp;
	
	private static TreePopupActions instance;
	private static boolean isInstantiated;
	
	private TreePopupActions(FileTreePopup ftp, ReportsPanel rp){
		this.rp = rp;
		this.ftp = ftp;
	}
	
	public static TreePopupActions instantiate(FileTreePopup ftp, ReportsPanel rp){
		if(!isInstantiated){
			instance = new TreePopupActions(ftp, rp);
			isInstantiated = true;
		}
		
		return instance;
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(ftp.checkSource(e.getSource(), ftp)){
			try{
				String opResult = "";
				StringBuilder op = new StringBuilder();
				StringBuilder info = new StringBuilder();
				op.append("File Viewer Operation<br>");
				info.append("<div style='white-space:nowrap;'>");
				if(e.getSource() == ftp.getTreePopupOption(0)){
					ftp.deleteSelectedFiles(DeleteOption.DELETE_ANY, info);
					opResult = "Delete File/s";
				}
				else if(e.getSource() == ftp.getTreePopupOption(1)){
					ftp.deleteSelectedFiles(DeleteOption.DELETE_ZIP, info);
					opResult = "Delete Zip File/s";
				}
				else if(e.getSource() == ftp.getTreePopupOption(2)){
					ftp.deleteSelectedFiles(DeleteOption.DELETE_DUMMY, info);
					opResult = "Delete Dummy File/s";
				}
				else if(e.getSource() == ftp.getTreePopupOption(3)){
					ftp.expandCollapseDirs(true);
					opResult = "Expand Multiple Directories";
				}
				else if(e.getSource() == ftp.getTreePopupOption(4)){
					ftp.expandCollapseDirs(false);
					opResult = "Collapse Multiple Directories";
				}
				else if(e.getSource() == ftp.getTreePopupOption(5)){
					ftp.selectSpecificFiles(SelectOption.ZIP, info);
					opResult = "Select Zip File/s";
				}
				else if(e.getSource() == ftp.getTreePopupOption(6)){
					ftp.createDirsForFiles(info);
					opResult = "Create Directories for Files";
				}
				else if(e.getSource() == ftp.getTreePopupOption(7)){
					opResult = "Replace Text...";
					String selectedOp = "<b>Selected Operation:</b> "+opResult+"<br><br>";
					ftp.replaceTextDialog(
					new StringBuilder(op.toString()+selectedOp), rp);
				}
				info.append("</div>");
				op.append("<b>Selected Operation:</b> "+opResult+"<br>");
				rp.getSubPnlep().setText(op.toString() + info.toString());
			}
			catch(IOException f){ f.printStackTrace(); }
		}
	}
}	
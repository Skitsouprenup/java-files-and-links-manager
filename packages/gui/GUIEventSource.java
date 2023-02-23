package gui;

import static main.UtilityClasses.*;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import fileoperations.approvedopfunctions.*;
import fileoperations.FileOperations;
import main.PreferencesConfig;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public abstract class GUIEventSource{
	//file chooser
	private final JFileChooser fc;
	//holds operations for file generation
	private final FileOperations fo;
	//holds GUI instance
	private static GUIComponents guiC;
	//holds the selected operation of the program
	private final JobRef jb;
	//
	private final SelectionLinkRef sr;
	//
	private final ZipPackRef zpr;
	//
	private final UnzipPackRef upr;
	//
	private static StringBuilder message;
	//
	private StringBuilder validLinks;
	//
	private final FileChooserProperties fcp;
	
	//intialization block #1
	{ 
		fc = new JFileChooser();
		Action detDisp = fc.getActionMap().get("viewTypeDetails");
		detDisp.actionPerformed(null);
		
		Component[] comp = fc.getComponents();
		JTable table = getChooserTable(comp);
		
		if(table != null){
			TableRowSorter<TableModel> trs = new TableRowSorter<>(table.getModel());
		
			trs.setComparator(0, new Comparator<String>(){
			
				@Override
				public int compare(String o1,String o2){
					return o1.compareToIgnoreCase(o2);
				}
			
			});
		
			table.setRowSorter(trs);
		}
		
		fo = FileOperations.instantiate();
	}
	
	//intialization block #2
	{
		fcp = new FileChooserProperties();
		jb = new JobRef();
		sr = new SelectionLinkRef();
		zpr = new ZipPackRef();
		upr = new UnzipPackRef();
		message = new StringBuilder();
		validLinks = new StringBuilder();
	}
	
	//This method will look for the jtable of JFileChooser
	//where the files are sorted. We're looking for jtable
	//that is an anonymous class in FilePane. Anonymous class
	//has this "$" signature and followed by a number.
	void extractChooserTable(Component[] comp, JTable table){
		
		for(Component c: comp){
			if(table != null)
				break;
			
			if(c instanceof JTable)
				table = (JTable)c;
			Container cont = (Container)c;
			extractChooserTable(cont.getComponents(), table);
		}
	}
	
	JTable getChooserTable(Component[] comp) {
		JTable table = null;
		extractChooserTable(comp, table);

		return table;
	}

	static void setGuiC(GUIComponents gc){ guiC = gc; }
	
	public static void refreshMsgBeforeOp(String newMsg){
		message = new StringBuilder(newMsg);
	}
	
	//GUI related events
	//These events changes the state of GUI components via
	//special button type like radiobutton, checkbox, etc.
	protected void guiBtnActSource(ActionEvent e){
		
		if( guiC.linksP().checkGUIEvtSource(e.getSource(), guiC.linksP()) ){
			guiC.linksP().fireEvent(e.getSource(), guiC.linksP());
		}
		else if( guiC.zipP().checkGUIEvtSource(e.getSource(), guiC.zipP()) ) {
			guiC.zipP().fireEvent(e.getSource(), guiC.zipP());
		}
		else if( guiC.stopWatchP().checkGUIEvtSource(e.getSource(), guiC.stopWatchP()) ){
			if(guiC.stopWatchP().getSubPnlCb().isSelected()){
				guiC.stopWatchP().getSubPnlLbl(0).setVisible(false);
				guiC.setEnabledComps(false);
			}
			else{
				guiC.stopWatchP().getSubPnlLbl(0).setVisible(true);
				guiC.setEnabledComps(true);
			}
		}
	}
	
	private void resetReportsPanelState(){
		guiC.setEnabledCompsWithStpWtch(true,false);
		guiC.reportsP().getSubPnlPnl().setVisible(false);
		guiC.linksP().getSubPnlJta().setEditable(true);
		message = new StringBuilder();
		validLinks = new StringBuilder();
		jb.setValue(null);
	}
	
	//program's components related events
	//These events commence program's main operations
	//like compress, decompress, file generation, etc.
	protected void btnActSource(ActionEvent e) throws IOException{
		
		//FileGenPanel button events
		if( guiC.fileGenP().checkSource(e.getSource(), guiC.fileGenP()) ) {
			String msg = guiC.fileGenP().createMessage(e.getSource(), guiC.fileGenP(), 
													   guiC.getMainFrame(), jb);
		  
		  if(msg != null){
			  message.append(msg);
			  guiC.reportsP().getSubPnlep().setText(message.toString());
		 
			  guiC.setEnabledCompsWithStpWtch(false,true);
			  guiC.reportsP().getSubPnlPnl().setVisible(true);
		  }
			
		}//ReportsPanel button events
		else if( guiC.reportsP().checkSource(e.getSource(), guiC.reportsP() )){
			//clear
		    if(e.getSource() == guiC.reportsP().getSubPnlBtn(0)){
				guiC.reportsP().getSubPnlep().setText("No Selected Operation...");
			}
			//commence operation: yes
			else if(e.getSource() == guiC.reportsP().getSubPnlBtn(1)){
				
				//File chooser defaults
				FileChooserProperties.setToDefaults(fc);
				//Type Selection that affects some features of the file chooser
				if(jb.getValue() == Jobs.PUT_LINKS)
					fcp.linksSelectType(fc, sr.getValue());
				else if(jb.getValue() == Jobs.COMPRESS_FILES)
					fcp.zipPackagingType(fc, zpr.getValue());
				else if(jb.getValue() == Jobs.DECOMPRESS_FILES)
					fcp.unZipSelectType(fc, upr.getValue());
				
				int val = fc.showOpenDialog(guiC.getMainFrame());
				StringBuilder msg = new StringBuilder();
				
				if(val == JFileChooser.APPROVE_OPTION){
					message.append("<div style='white-space:nowrap;'>");
					
					//fix JFileChooser selection bug. See method description
					//for more info
					File[] modFiles = FileChooserProperties.fChooserSelectBugFix(fc);
					
					switch(jb.getValue()){
					
						case CREATE_POST_TXT:
						if(!PreferencesConfig.
						   defaultCurrentPresetVerification(guiC.getMainFrame(),null)){
							resetReportsPanelState();
							guiC.reportsP().getSubPnlep().setText("No Selected Operation...");
							return;
						}
						msg.append( fo.createPost(modFiles,guiC.fileGenP().getSubPnlJcb().getSelectedIndex(),
											guiC.fileGenP().getSubPnlCb(1).isSelected(),
											guiC.fileGenP().getSubPnlCb(0).isSelected()) );
						break;
					
						case CREATE_DUMMY_FILE:
						msg.append( fo.createDummy(modFiles,guiC.fileGenP().getSubPnlTf().getText(),
											guiC.fileGenP().getSubPnlCb(1).isSelected()) );
						break;
					  
						case DELETE_DUMMY_FILE:
						msg.append( fo.deleteDummy(modFiles,guiC.fileGenP().getSubPnlCb(1).isSelected()) );
						break;
						
						case PUT_LINKS:
						if(!PreferencesConfig.notifyAndLoadPrefsConfig(guiC.getMainFrame())){
							resetReportsPanelState();
							guiC.reportsP().getSubPnlep().setText("No Selected Operation...");
							return;
						}
						
						if(validLinks.length() != 0){
							StringBuilder suppHosts = new StringBuilder();
							StringBuilder splitRegex = new StringBuilder();
							if(!PreferencesConfig.verifyAndGetHostNames(guiC.getMainFrame(), suppHosts,
							   splitRegex)){
								resetReportsPanelState();
								guiC.reportsP().getSubPnlep().setText("No Selected Operation...");
								return;
							}
							
							String[] hosts = suppHosts.toString().split(splitRegex.toString());
							ArrayList<String> hostsArr = 
							new ArrayList<>(Arrays.asList(hosts));
							
							//these two arrays must have the same size
							//Actually, these two arrays hold the same values.
							//They only differ if there's a host with 
							//backward compatibility issue which is processed
							//in arrangeSupportedHosts() method
							//
							//hostHeaders holds hostnames their alternative with
							//regex patterns and separated with "|"
							//example: hostHeaders[n] = ^rg.to$|^drop.download$
							//hostLinks holds hostnames their alternative names separated with "|"
							//example: hostLinks[n] = rg.to|drop.download
							String[] hostHeaders = new String[hostsArr.size()];
							String[] hostLinks = new String[hostsArr.size()];
							
							boolean isArranged = 
							LinkOpApprovedFunctions.
							arrangeSupportedHosts(hostsArr, hostHeaders, hostLinks);
							
							if(!isArranged)
								msg.append(
								"<span style='color:red;'>No supported hosts found!<br>"+
								"Can't generate links!</span>");
							
							if(hostHeaders.length != 0 && hostLinks.length != 0 &&
							   hostHeaders.length == hostLinks.length && isArranged){
								if(sr.getValue() == SelectionType.SINGLE){
								File selectedFile = fc.getSelectedFile();
								if(selectedFile.isDirectory())
									msg.append( fo.putLinksDir(selectedFile,sr.getValue(),
							                               validLinks,hostHeaders,
														   hostLinks) );
								}
								else if(sr.getValue() == SelectionType.MULTIPLE){
									boolean incSubDirs = guiC.linksP().getSubPnlCb().isSelected();
									msg.append( fo.putLinksSub(modFiles,sr.getValue(),incSubDirs,
															   validLinks,hostHeaders,hostLinks) );
								}
							}
							
						}
						break;
						
						case COMPRESS_FILES:
						msg.append( fo.compressFiles(modFiles,guiC.zipP().getSpnlCb(0).isSelected(),
										 guiC.zipP().getSubPnlRb(0).isSelected(),
										 guiC.zipP().getSubPnlRb(1).isSelected(),
										 guiC.zipP().getSpnlJcb().getSelectedItem().toString(),
										 guiC.zipP().getSpnlCb(2).isSelected(),
										 guiC.zipP().getSpnlCb(3).isSelected()) );
						break;
						
						case DECOMPRESS_FILES:
						msg.append( fo.deCompressFiles(modFiles,guiC.zipP().getSpnlCb(1).isSelected(),
									upr.getValue()) );
						break;
					} 
					message.append(msg.toString());
					message.append("</div>");
					guiC.reportsP().getSubPnlep().setText(message.toString());
				}
				else if(val == JFileChooser.CANCEL_OPTION)
					guiC.reportsP().getSubPnlep().setText("No Selected Operation...");
				
				//enable and reset things
				resetReportsPanelState();
			}
			//commence operation: no
			else if(e.getSource() == guiC.reportsP().getSubPnlBtn(2)){
				resetReportsPanelState();
				guiC.reportsP().getSubPnlep().setText("No Selected Operation...");
			}
		}
		//LinksPanel button events
		else if( guiC.linksP().checkSource(e.getSource(), guiC.linksP()) ){
			if(e.getSource() == guiC.linksP().getSubPnlBtn(0)){
				if(!PreferencesConfig.notifyAndLoadPrefsConfig(guiC.getMainFrame()))
					return;
				
				jb.setValue(Jobs.PUT_LINKS);
				
				StringBuilder msg = new StringBuilder();
				String linksStr = guiC.linksP().createMessage(guiC.linksP(),sr,guiC.getMainFrame());
				if(linksStr != null){
					msg.append(linksStr);
					linksStr = guiC.linksP().checkLinks(validLinks,guiC.linksP(),
															   guiC.getMainFrame());
					if(linksStr != null){
						msg.append(linksStr);
						
						message.append(msg.toString());
						guiC.reportsP().getSubPnlep().setText(message.toString());
				
						guiC.setEnabledCompsWithStpWtch(false,true);
						guiC.setEnabledComps(false);
						guiC.linksP().getSubPnlJta().setEditable(false);
						guiC.reportsP().getSubPnlPnl().setVisible(true);
					}
				}	
			}
			else if(e.getSource() == guiC.linksP().getSubPnlBtn(1))
				guiC.linksP().getSubPnlJta().setText("");
			
		}
		//ZipPanel button events
		else if( guiC.zipP().checkSource(e.getSource(), guiC.zipP()) ){
			
			if(e.getSource() == guiC.zipP().getSubPnlBtn(0)){
				jb.setValue(Jobs.COMPRESS_FILES);
				
				String msg = guiC.zipP().createZipMessage(guiC.zipP(),zpr);
				message.append("<b>Selected Operation:</b> " + msg + "<br><br>");
			}
			else if(e.getSource() == guiC.zipP().getSubPnlBtn(1)){
				jb.setValue(Jobs.DECOMPRESS_FILES);
				
				String msg = guiC.zipP().createUnzipMessage(guiC.zipP(),upr);
				message.append("<b>Selected Operation:</b> " + msg + "<br><br>");
			}
			
			guiC.setEnabledCompsWithStpWtch(false,true);
			guiC.reportsP().getSubPnlPnl().setVisible(true);
			guiC.reportsP().getSubPnlep().setText(message.toString());
		}
		//StopWatchPanel button events
		else if( guiC.stopWatchP().checkSource(e.getSource(), guiC.stopWatchP()) ){
			guiC.stopWatchP().openStopWatch(e.getSource(),guiC.getMainFrame(),
											guiC.stopWatchP());
		}
		//FileViewPanel button events
		else if( guiC.fileViewP().checkSource(e.getSource(),guiC.fileViewP()) ){
			if(e.getSource() == guiC.fileViewP().getSubPnlBtn(0)){
				
				FileChooserProperties.setToDefaults(fc);
				
				int val = fc.showOpenDialog(guiC.getMainFrame());
				
				if(val == JFileChooser.APPROVE_OPTION){
					//fix JFileChooser selection bug. See method description
					//for more info
					File[] modFiles = FileChooserProperties.fChooserSelectBugFix(fc);
				    
					List<File> files = Arrays.asList(modFiles);
					
					Comparator<File> sortType = new SortFiles().createInstance("IgnoreCase");
					Collections.sort(files, sortType);
					
					File[] sortedFiles = new File[files.size()];
					files.toArray(sortedFiles);
					
					guiC.fileViewP().setFileTreeNodes(sortedFiles);
					
					if(!guiC.fileViewP().getTreePaneVisibility())
					guiC.fileViewP().setTreePaneVisibility(true);
				}
				
			}
		}
	}
	
	protected void settingsMenuActSource(ActionEvent e){
		if(e.getSource() == guiC.getSettingsMenuItem(0)){
			guiC.createPrefsDialog(jb, sr);
		}
		else if(e.getSource() == guiC.getSettingsMenuItem(1)){
			System.exit(0);
		}
	}
	
	protected void editMenuActSource(ActionEvent e){
		if(e.getSource() == guiC.getEditMenuItem(0)){
			guiC.createPostFileEditorDialog();
		}
	}
	
	protected void toolsMenuActSource(ActionEvent e){
		if(e.getSource() == guiC.getToolsMenuItem(0)){
			guiC.createRandomizerDialog();
		}
		else if(e.getSource() == guiC.getToolsMenuItem(1)){
			guiC.createLinkCheckerDialog();
		}
	}
	
}
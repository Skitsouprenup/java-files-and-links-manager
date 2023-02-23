package gui;

import static main.UtilityClasses.JobRef;
import static main.UtilityClasses.SelectionLinkRef;

import javax.swing.JMenuItem;
import javax.swing.JComponent;

import main.PreferencesConfig;

import gui.dialogs.*;
import gui.dialogs.linkchecker.LinkCheckerDialog;
import gui.subpanels.*;

//Singleton class
public class GUIComponents extends GUIContainers{
	private final GUIActions act;
	
	//Sub Panels
	private final FileGenPanel fileGenPanel;
	private final ReportsPanel reportsPanel;
	private final LinksPanel linksPanel;
	private final ZipPanel zipPanel;
	private final StopwatchPanel stopwatchPanel;
	private final FileViewPanel fileViewPanel;
	//
	private static boolean isInstantiated;
	private static GUIComponents instance;
	
	private GUIComponents(){
		super();
		act = GUIActions.instantiate();
		fileGenPanel = FileGenPanel.instantiate(subPnls[0],act.getBtnListener(),getMainFrame());
		//
		reportsPanel = ReportsPanel.instantiate(subPnls[1],act.getBtnListener());
		//
		linksPanel = LinksPanel.instantiate(subPnls[2],act.getBtnListener(),
											act.getGuiBtnListener());
		//
		zipPanel = ZipPanel.instantiate(subPnls[3],act.getBtnListener(),
										act.getGuiBtnListener());
		//
		stopwatchPanel = StopwatchPanel.instantiate(subPnls[4],act.getBtnListener(),
													act.getGuiBtnListener());
		//
		fileViewPanel = FileViewPanel.instantiate(subPnls[5],act.getBtnListener(),
												  getMainFrame());
		//Set JFrame's settings menu listener
		for(JMenuItem item : settingsMenuItems)
			item.addActionListener(act.getSettingsMenuListener());
		//Set JFrame's edit menu listener
		for(JMenuItem item : editMenuItems)
			item.addActionListener(act.getEditMenuListener());
		//Set JFrame's tools menu listener
		for(JMenuItem item : toolsMenuItems)
			item.addActionListener(act.getToolsMenuListener());
		//
		calibrateTopContainers();
		
		//initially load prefsconfig
		PreferencesConfig.loadPrefsConfigFile();
	}
	
	public static GUIComponents instantiate(){
		if(!isInstantiated){
			isInstantiated = true;
			instance = new GUIComponents();
			GUIEventSource.setGuiC(instance);
		}
		
		return instance;
	}
	
	//enable/disable core components of this program
	void setEnabledComps(boolean state){
		fileGenPanel.setEnabledMainComps(state);
		reportsPanel.setEnabledMainComps(state);
		linksPanel.setEnabledMainComps(state);
		zipPanel.setEnabledMainComps(state);
		fileViewPanel.setEnabledMainComps(state);
	}
	
	void setEnabledCompsWithStpWtch(boolean state, boolean stopStpWtch){
		
		if(stopStpWtch){
			//if the start/resume/stop panel is visible, it means that
			//the stopwatch is in running or pause state.
			if(stopwatchPanel.getSubPnlPnl2().isVisible()){
				stopwatchPanel.stopWatchStartStop(false);
				stopwatchPanel.getSubPnlTxt(0).setText("3");
				stopwatchPanel.getSubPnlTxt(1).setText("0");
				if(stopwatchPanel.getSubPnlBtn(1).getText().equals("Resume"))
					stopwatchPanel.getSubPnlBtn(1).setText("Pause");
				stopwatchPanel.getStopWatch().stopThread();
			}
			
			setEnabledComps(state);
			stopwatchPanel.setEnabledMainCompsForStpWtch(state);
		}
		else{
			setEnabledComps(state);
			stopwatchPanel.setEnabledMainCompsForStpWtch(state);
		}
	}
	
	static void setEnabledComponents(boolean state,JComponent... comps){
	    for(int i = 0; i < comps.length; i++)
		    comps[i].setEnabled(state);
	}
	
	//
	FileGenPanel fileGenP(){ return fileGenPanel; }
	ReportsPanel reportsP(){ return reportsPanel; }
	LinksPanel linksP(){ return linksPanel; }
	ZipPanel zipP(){ return zipPanel; }
	StopwatchPanel stopWatchP(){ return stopwatchPanel; }
	FileViewPanel fileViewP(){ return fileViewPanel; }
	
	void createPrefsDialog(JobRef jb, SelectionLinkRef sr){ 
		if(!PrefsDialog.isInstantiated())
			new PrefsDialog(jf, fileGenPanel, linksPanel,
							reportsPanel.getSubPnlep(), jb, sr);
		else PrefsDialog.retainFocus();
	}
	
	void createPostFileEditorDialog(){
		System.out.println("Editor");
	}
	
	void createLinkCheckerDialog(){
		if(!LinkCheckerDialog.isInstantiated())
			new LinkCheckerDialog(reportsPanel.getSubPnlep(), jf);
		else LinkCheckerDialog.retainFocus();
	}
	
	void createRandomizerDialog(){
		if(!FileUpRandomizerDialog.isInstantiated())
			new FileUpRandomizerDialog(jf);
		else FileUpRandomizerDialog.retainFocus();
	}
}
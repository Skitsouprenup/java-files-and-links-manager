package gui.dialogs;

import static main.UtilityClasses.JobRef;
import static main.UtilityClasses.SelectionLinkRef;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import gui.dialogs.subpanels.prefs_subpanel.*;
import gui.subpanels.FileGenPanel;
import gui.subpanels.LinksPanel;

public class PrefsDialog{
	private static JDialog staticMainFrame;
	
	private final JDialog mainFrame;
	private final JList<String> prefsList;
	private final JPanel mainPanel, leftSplit;
	private JScrollPane mainPanelScroll;
	private final JSplitPane splitPane;
	
	private final FileGenPanel fgp;
	private final LinksPanel lp;
	private final JEditorPane reportsPane;
	private final JobRef jb;
	private final SelectionLinkRef sr;
	
	public PrefsDialog(Frame rootFrame, FileGenPanel fgp,
					   LinksPanel lp, JEditorPane reportsPane, JobRef jb,
					   SelectionLinkRef sr){
		this.fgp = fgp;
		this.lp = lp;
		this.reportsPane = reportsPane;
		this.jb = jb;
		this.sr = sr;
						   
		mainFrame = new JDialog();
		mainFrame.setTitle("Preferences");
		staticMainFrame = mainFrame;
		prefsList = new JList<>(
		new String[]{"File Generation", "Reports", "Link Operations",
		             "Compress & Decompress", "Stopwatch", "File Viewer"});
		prefsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		prefsList.setSelectedIndex(0);
		mainPanel = new JPanel(new GridBagLayout());
		mainPanelScroll = new JScrollPane(mainPanel);
		leftSplit = new JPanel();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		prefsList.addMouseListener(new MouseAdapter(){
		
			@Override
			public void mousePressed(MouseEvent e){
				if(SwingUtilities.isLeftMouseButton(e))
					showPrefsSubPanel(prefsList.getSelectedIndex());
			}
		
		});
		
		mainFrame.addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e){
				staticMainFrame = null;
			}
		});
		
		leftSplit.add(prefsList);
		splitPane.setLeftComponent(leftSplit);
		showPrefsSubPanel(prefsList.getSelectedIndex());
		
		mainPanel.add(splitPane, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
													   GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
		mainFrame.add(mainPanelScroll);
		
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(rootFrame);
		mainFrame.setVisible(true);
	}
	
	public static boolean isInstantiated(){
		if(staticMainFrame != null)
			return true;
		else return false;
	}
	
	public static void retainFocus(){
		if(staticMainFrame != null)
			staticMainFrame.requestFocus();
	}
	
	private void showPrefsSubPanel(int index){
		switch(index){
			
			//File Generation
			case 0:
			splitPane.setRightComponent(
			new PrefsFileGenSubPnl(mainFrame, fgp, lp, reportsPane, jb, sr)
			.getPanel());
			break;
			
			//Reports
			case 1:
			splitPane.setRightComponent(new ReportsSubPnl().getPanel());
			break;
			
			//Link Operation
			case 2:
			splitPane.setRightComponent(new PrefsLinkOpSubPnl(mainFrame).getPanel());
			break;
			
			//Compress & Decompress
			case 3:
			splitPane.setRightComponent(new CompDeCompSubPnl().getPanel());
			break;
			
			//Stopwatch
			case 4:
			splitPane.setRightComponent(new StopWatchSubPnl().getPanel());
			break;
			
			//File Viewer
			case 5:
			splitPane.setRightComponent(new FileViewerSubPnl().getPanel());
			break;
			
			default:
			System.err.println("Invalid index!");
			break;
		}
		mainFrame.pack();
	}
}
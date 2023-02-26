package gui.dialogs;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.io.File;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

import gui.CharacterLimiter;

import main.UndecoratedButtonFactory;

public class FileUpRandomizerDialog extends FileUpPopupFunctions{
	private static JDialog staticMainFrame;
	
	private Window parent;
	
	private JDialog mainFrame;
	private JPanel mainPanel;
	private JPanel sourceListPnl, destListPnl, recListPnl, destLinkListPnl;
	private JPopupMenu rClickPopup;
	private JLabel[] lbls;
	private JButton[] btns;
	private JButton settingsBtn; 
	private JButton savePresetBtn; 
	private JList<Object> sourceList, destination, recordRefs, destLinkList;
	private JScrollPane mainPanelScroll;
	
	private JMenuItem remove, removeAll;
	
	private JFileChooser fc;
	
	private JTextField randCountTxt;
	
	private LinkedHashSet<String> sourceSet, destSet, recSet, destLinkSet;
	
	private final int btnCount = 5;
	
	public FileUpRandomizerDialog(Window parent){
		this.parent = parent; 
		mainFrame = new JDialog(parent);
		mainFrame.setTitle("File Upload Randomizer");
		staticMainFrame = mainFrame;
		mainPanel = new JPanel(new GridBagLayout());
		mainPanelScroll = new JScrollPane(mainPanel);
		rClickPopup = new JPopupMenu();
		randCountTxt = new JTextField(3);
		randCountTxt.setDocument(new CharacterLimiter(2));
		
		sourceList = new JList<>();
		sourceList.setName(ListType.SOURCE.toString());
		destination = new JList<>();
		destination.setName(ListType.DESTINATION.toString());
		recordRefs = new JList<>();
		recordRefs.setName(ListType.RECORD.toString());
		destLinkList = new JList<>();
		destLinkList.setName(ListType.SYMLINK_DESTINATION.toString());
		
		lbls = new JLabel[5];
		lbls[0] = new JLabel("Source/s(Directory)");
		lbls[1] = new JLabel("Destination(Directory)");
		lbls[2] = new JLabel("Record Reference/s");
		lbls[3] = new JLabel("Destination(Symlink/s)");
		lbls[4] = new JLabel("# of Directories: ");
		
		btns = new JButton[btnCount];
		btns[0] = new JButton("Add Source/s");
		btns[1] = new JButton("Add Destination");
		btns[2] = new JButton("Add Record/s");
		btns[3] = new JButton("Add Link Destination");
		btns[4] = new JButton("Randomize!");
		
		settingsBtn = UndecoratedButtonFactory.createButton(false, "Settings", "13");
		savePresetBtn = UndecoratedButtonFactory.createButton(false, "Save/Delete Preset", "13");
		
		remove = new JMenuItem("Remove");
		removeAll = new JMenuItem("Remove All");
		
		fc = new JFileChooser();
		
		sourceSet = new LinkedHashSet<>();
		destSet = new LinkedHashSet<>();
		recSet = new LinkedHashSet<>();
		destLinkSet = new LinkedHashSet<>();
		
		addComponents();
		addActions();
		
		mainFrame.addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e){
				staticMainFrame = null;
			}
		});
		
	}
	
	private void addComponents(){
		sourceListPnl = new JPanel(new GridBagLayout());
		destListPnl = new JPanel(new GridBagLayout());
		recListPnl = new JPanel(new GridBagLayout());
		destLinkListPnl = new JPanel(new GridBagLayout());
		
		JPanel[] btnHolder = new JPanel[btnCount];
		Dimension dm = new Dimension();
		//get the default height of one button in the btns array 
		//and set a fixed width to create buttons with uniform size
		//while the default height is unchanged
		//each font in the btns array has the same height so it
		//doesn't matter which button height you wanna acquire 
		dm.setSize(200, btns[0].getPreferredSize().getHeight());
		for(int i = 0; i < btnHolder.length; i++){
			btnHolder[i] = new JPanel(new FlowLayout());
			btnHolder[i].add(btns[i]);
			btns[i].setPreferredSize(dm);
		}
		
		rClickPopup.add(remove);
		rClickPopup.add(removeAll);
		
		mainPanel.add(lbls[0], new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
									 GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
									 
		sourceListPnl.add(new JLabel("None"), new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
												  GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
									   
		mainPanel.add(sourceListPnl, new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,
									     GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
										 
		mainPanel.add(btnHolder[0], new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER,
									     GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		mainPanel.add(lbls[1], new GridBagConstraints(0,3,1,1,1,1,GridBagConstraints.CENTER,
									 GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		destListPnl.add(new JLabel("None"), new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
												  GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		
		mainPanel.add(destListPnl, new GridBagConstraints(0,4,1,1,1,1,GridBagConstraints.CENTER,
									   GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
								
		mainPanel.add(btnHolder[1], new GridBagConstraints(0,5,1,1,1,1,GridBagConstraints.CENTER,
									     GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		mainPanel.add(lbls[2], new GridBagConstraints(0,6,1,1,1,1,GridBagConstraints.CENTER,
									 GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		recListPnl.add(new JLabel("None"), new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
												  GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
									
		mainPanel.add(recListPnl, new GridBagConstraints(0,7,1,1,1,1,GridBagConstraints.CENTER,
									  GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
									  
		mainPanel.add(btnHolder[2], new GridBagConstraints(0,8,1,1,1,1,GridBagConstraints.CENTER,
									     GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		mainPanel.add(lbls[3], new GridBagConstraints(0,9,1,1,1,1,GridBagConstraints.CENTER,
									 GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		destLinkListPnl.add(new JLabel("None"), new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
												GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		
		mainPanel.add(destLinkListPnl, new GridBagConstraints(0,10,1,1,1,1,GridBagConstraints.CENTER,
									   GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
		
		mainPanel.add(btnHolder[3], new GridBagConstraints(0,11,1,1,0,0,GridBagConstraints.CENTER,
									     GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		JPanel lblTxtHolder = new JPanel(new GridBagLayout());
		
		lblTxtHolder.add(lbls[4], new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,
									  GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
									   
		lblTxtHolder.add(randCountTxt, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,
									       GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		
		mainPanel.add(lblTxtHolder, new GridBagConstraints(0,12,1,1,0,0,GridBagConstraints.CENTER,
									    GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		mainPanel.add(settingsBtn, new GridBagConstraints(0,13,1,1,0,0,GridBagConstraints.CENTER,
									   GridBagConstraints.NONE,new Insets(1,5,0,5),0,0));
		
		mainPanel.add(savePresetBtn, new GridBagConstraints(0,14,1,1,0,0,GridBagConstraints.CENTER,
									        GridBagConstraints.NONE,new Insets(0,5,0,5),0,0));
		
		mainPanel.add(btnHolder[4], new GridBagConstraints(0,15,1,1,0,0,GridBagConstraints.CENTER,
									    GridBagConstraints.NONE,new Insets(1,5,5,5),0,0));
		
		mainFrame.add(mainPanelScroll);
		
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(parent);
		mainFrame.setVisible(true);
	}
	
	private void addActions(){
		DialogActions da = new DialogActions();
		
		for(JButton btn : btns)
			btn.addActionListener(da);
		settingsBtn.addActionListener(da);
		savePresetBtn.addActionListener(da);
		
		enableListPopup(sourceList);
		enableListPopup(destination);
		enableListPopup(recordRefs);
		enableListPopup(destLinkList);
		
		remove.addActionListener(da);
		removeAll.addActionListener(da);
	}
	
	private void enableListPopup(JList<Object> list){
		list.addMouseListener(new MouseAdapter(){
		
			@Override
			public void mousePressed(MouseEvent e){
				if(SwingUtilities.isRightMouseButton(e))
					if(e.isPopupTrigger()){
						rClickPopup.show(e.getComponent(),e.getX(),e.getY());
					}
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				if(SwingUtilities.isRightMouseButton(e))
					if(e.isPopupTrigger()){
						rClickPopup.show(e.getComponent(),e.getX(),e.getY());
					}
			}
		
		});
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
	
	private class DialogActions implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try{ actionSource(e); }
			catch(IOException f){
				f.printStackTrace();
			}
		}
	}
	
	private void actionSource(ActionEvent e) throws IOException{
		
		if(e.getSource() == btns[0]){
			ArrayList<File> selectedFileList = new ArrayList<>();
			int val = selectFilesThenVerifyPaths(fc, mainFrame, selectedFileList,
					  sourceSet, destSet, recSet, destLinkSet, true, sourceList.getName());
			
			//Add source
			if(val != JFileChooser.CANCEL_OPTION){
				addItemsToJList(selectedFileList, mainFrame, sourceList, sourceListPnl, sourceSet);
				System.out.println("Here");
				mainFrame.setLocationRelativeTo(parent);
			}
		}
		else if(e.getSource() == btns[1]){
			ArrayList<File> selectedFileList = new ArrayList<>();
			int val = selectFilesThenVerifyPaths(fc, mainFrame, selectedFileList,
					  sourceSet, destSet, recSet, destLinkSet, false, destination.getName());
					  
			//Add destination
			if(val != JFileChooser.CANCEL_OPTION){
				addItemsToJList(selectedFileList, mainFrame, destination, destListPnl, destSet);
				mainFrame.setLocationRelativeTo(parent);
			}
		}
		else if(e.getSource() == btns[2]){
			ArrayList<File> selectedFileList = new ArrayList<>();
			int val = selectFilesThenVerifyPaths(fc, mainFrame, selectedFileList,
					  sourceSet, destSet, recSet, destLinkSet, true, recordRefs.getName());
			//add Reference
			if(val != JFileChooser.CANCEL_OPTION){
				addItemsToJList(selectedFileList, mainFrame, recordRefs, recListPnl, recSet);
				mainFrame.setLocationRelativeTo(parent);
			}
		}
		else if(e.getSource() == btns[3]){
			ArrayList<File> selectedFileList = new ArrayList<>();
			int val = selectFilesThenVerifyPaths(fc, mainFrame, selectedFileList,
					  sourceSet, destSet, recSet, destLinkSet, false, destLinkList.getName());
					  
			//add Symlink Destination
			if(val != JFileChooser.CANCEL_OPTION){
				addItemsToJList(selectedFileList, mainFrame, destLinkList, destLinkListPnl, destLinkSet);
				mainFrame.setLocationRelativeTo(parent);
			}
		}
		else if(e.getSource() == btns[4]){
			
			if(randCountTxt.getText().isEmpty() && 
			   randCountTxt.getText().isBlank()){
			   JOptionPane.showMessageDialog(
					mainFrame,"Number of random directories is empty!",
					"Empty Textfield",JOptionPane.ERROR_MESSAGE);
				return;
			}
			   
			int randCount = 0;
			try{
				randCount = Integer.parseInt(randCountTxt.getText());
				if(randCount < 0){
					JOptionPane.showMessageDialog(
						mainFrame,"Number of random directories is less than 0!",
						"Negative Number Not Allowed",JOptionPane.ERROR_MESSAGE);
					randCountTxt.setText("");
					return;
				}
			}
			catch(NumberFormatException f){
				javax.swing.JOptionPane.showMessageDialog(
							mainFrame,"Number of random directories is not a number",
							"Not A Number",javax.swing.JOptionPane.ERROR_MESSAGE);
				randCountTxt.setText("");
				return;
			}
			randomize(mainFrame, randCount, sourceSet, destSet, recSet, destLinkSet);
		}
		else if(e.getSource() == settingsBtn){
			new RandomizerSettingsDialog(mainFrame, this);
		}
		else if(e.getSource() == savePresetBtn){
			new RandomizerSavePresetDialog(mainFrame, this);
		}
		else if(e.getSource() == remove){
			remove(rClickPopup.getInvoker().getName(), RemoveType.REMOVE);
		}
		else if(e.getSource() == removeAll){
			remove(rClickPopup.getInvoker().getName(), RemoveType.REMOVE_ALL);
		}
	}
	
	void loadPreset(RandomizerPreset preset) throws IOException{
		if(preset.getPresetName().equals("None"))
			return;
		
		sourceSet.clear();
		destSet.clear();
		recSet.clear();
		destLinkSet.clear();
		
		ArrayList<String> sourceTemp = new ArrayList<>(preset.getSourceDirs());
		ArrayList<String> destTemp = new ArrayList<>(preset.destDirs());
		ArrayList<String> recTemp = new ArrayList<>(preset.getRecordDirs());
		ArrayList<String> destLinkTemp = new ArrayList<>(preset.getDestSymlinksDirs());
		
		//check the sizes of loaded sets. Destination and symlinks destination
		//must have only one element. Record reference can have multiple elements.
		//If destination and symlinks destination have more than one element,
		//their first elements remain and others are removed.
		if(destTemp.size() > 1)
			for(int i = 1; i < destTemp.size(); i++)
				destTemp.remove(i);
		if(destLinkTemp.size() > 1)
			for(int i = 1; i < recTemp.size(); i++)
				recTemp.remove(i);
		
		//Check cross duplicates between sets
		//Having cross duplicates in a saved preset
		//is impossible unless someone modifies
		//the saved preset externally
		//this algorithm is inefficient
		checkCrossDupsInLoadedPaths(sourceTemp, destTemp, recTemp, destLinkTemp);
		
		ArrayList<File> sourceFiles = new ArrayList<>();
		ArrayList<File> destFiles = new ArrayList<>();
		ArrayList<File> recFiles = new ArrayList<>();
		ArrayList<File> destLinkFiles = new ArrayList<>();
		
		//check if the loaded elements are existing in user's
		//machine. If an element doesn't exists, it won't be
		//added in its respective file list.
		for(String s : sourceTemp){
			File file = new File(s);
			if(file.exists())
				sourceFiles.add(file);
		}
		for(String s : destTemp){
			File file = new File(s);
			if(file.exists())
				destFiles.add(file);
		}
		for(String s : recTemp){
			File file = new File(s);
			if(file.exists())
				recFiles.add(file);
		}
		for(String s : destLinkTemp){
			File file = new File(s);
			if(file.exists())
				destLinkFiles.add(file);
		}
		
		//add loaded elements in JLists
		addItemsToJList(sourceFiles, mainFrame, sourceList, sourceListPnl, sourceSet);
		addItemsToJList(destFiles, mainFrame, destination, destListPnl, destSet);
		addItemsToJList(recFiles, mainFrame, recordRefs, recListPnl, recSet);
		addItemsToJList(destLinkFiles, mainFrame, destLinkList, destLinkListPnl, destLinkSet);
		mainFrame.setLocationRelativeTo(parent);
		
		JOptionPane.showMessageDialog(
		mainFrame,"Preset Loaded!",
		"Load Complete",JOptionPane.INFORMATION_MESSAGE);
	}
	
	LinkedHashSet<String> getSourceSet(){
		return sourceSet;
	}
	
	LinkedHashSet<String> getDestSet(){
		return destSet;
	}
	
	LinkedHashSet<String> getRecSet(){
		return recSet;
	}
	
	LinkedHashSet<String> getDestLinkSet(){
		return destLinkSet;
	}
	
	private void remove(String listName, RemoveType removeType){
		if(listName.equals(ListType.SOURCE.toString())){
			if(removeType == RemoveType.REMOVE)
				removeItems(mainFrame, sourceList, sourceListPnl, sourceSet);
			else if(removeType == RemoveType.REMOVE_ALL)
				removeAllItems(mainFrame, sourceList, sourceListPnl, sourceSet);
			mainFrame.setLocationRelativeTo(parent);
		}
		else if(listName.equals(ListType.DESTINATION.toString())){
			if(removeType == RemoveType.REMOVE)
				removeItems(mainFrame, destination, destListPnl, destSet);
			else if(removeType == RemoveType.REMOVE_ALL)
				removeAllItems(mainFrame, destination, destListPnl, destSet);
			mainFrame.setLocationRelativeTo(parent);
		}
		else if(listName.equals(ListType.RECORD.toString())){
			if(removeType == RemoveType.REMOVE)
				removeItems(mainFrame, recordRefs, recListPnl, recSet);
			else if(removeType == RemoveType.REMOVE_ALL)
				removeAllItems(mainFrame, recordRefs, recListPnl, recSet);
			mainFrame.setLocationRelativeTo(parent);
		}
		else if(listName.equals(ListType.SYMLINK_DESTINATION.toString())){
			if(removeType == RemoveType.REMOVE)
				removeItems(mainFrame, destLinkList, destLinkListPnl, destLinkSet);
			else if(removeType == RemoveType.REMOVE_ALL)
				removeAllItems(mainFrame, destLinkList, destLinkListPnl, destLinkSet);
			mainFrame.setLocationRelativeTo(parent);
		}
	}
	
	private enum RemoveType{
		REMOVE,REMOVE_ALL
	}
}
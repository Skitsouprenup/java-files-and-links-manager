package gui.dialogs;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import java.util.ArrayList;

class RandomizerSettingsDialog{
	
	private Window parent;
	
	private JButton saveBtn;
	private JButton cancelBtn;
	private JButton loadPresetBtn;
	
	private JDialog mainFrame;
	private JPanel mainPanel;
	
	private JLabel randFilesOpLbl;
	private JLabel createdSymLinksOpLbl;
	private JLabel presetLbl;
	
	private JRadioButton[] randFilesOpRbs;
	private JRadioButton[] createdSymLinksOpRbs;
	
	private ButtonGroup randFilesOpRbsGroup;
	private ButtonGroup createdSymLinksOpRbsGroup;
	
	private JComboBox<RandomizerPreset> presetCb;
	
	private FileUpRandomizerDialog randDialogRef;
	
	RandomizerSettingsDialog(Window parent, FileUpRandomizerDialog randDialogRef){
		this.parent = parent;
		this.randDialogRef = randDialogRef;
		mainFrame = new JDialog(parent,"Settings",ModalityType.APPLICATION_MODAL);
		mainPanel = new JPanel(new GridBagLayout());
		
		randFilesOpLbl = new JLabel("Randomed File/s Operation");
		createdSymLinksOpLbl = new JLabel("Created Symlink/s");
		presetLbl = new JLabel("Saved Presets");
		
		randFilesOpRbs = new JRadioButton[2];
		createdSymLinksOpRbs = new JRadioButton[2];
		
		randFilesOpRbs[0] = new JRadioButton("Copy");
		randFilesOpRbs[1] = new JRadioButton("Move");
		randFilesOpRbsGroup = new ButtonGroup();
		for(JRadioButton rb : randFilesOpRbs)
			randFilesOpRbsGroup.add(rb);
		
		createdSymLinksOpRbs[0] = new JRadioButton("From Copy    ");
		createdSymLinksOpRbs[1] = new JRadioButton("From Source");
		createdSymLinksOpRbsGroup = new ButtonGroup();
		for(JRadioButton rb : createdSymLinksOpRbs)
			createdSymLinksOpRbsGroup.add(rb);
		
		saveBtn = new JButton("Save Changes");
		cancelBtn = new JButton("Cancel");
		loadPresetBtn = new JButton("Load Preset");
		
		presetCb = new JComboBox<>();
		
		try{
			loadPresets(presetCb);
		}
		catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		
		if(!loadSettings()){
			randFilesOpRbs[0].setSelected(true);
			createdSymLinksOpRbs[1].setSelected(true);
		}
		else{
			if(randFilesOpRbs[1].isSelected()){
				createdSymLinksOpRbs[1].setEnabled(false);
				createdSymLinksOpRbs[0].setSelected(true);
			}
		}
		
		addActions();
		addComponents();
	}
	
	private void addComponents(){
		JPanel randFilesOpPnl = new JPanel(new GridBagLayout());
		JPanel createdSymLinksOpPnl = new JPanel(new GridBagLayout());
		JPanel buttonPnl = new JPanel(new FlowLayout());
		
		Dimension dm = new Dimension();
		//get the default height of one button in the btns array 
		//and set a fixed width to create buttons with uniform size
		//while the default height is unchanged
		//each font in the btns array has the same height so it
		//doesn't matter which button height you wanna acquire 
		dm.setSize(150, saveBtn.getPreferredSize().getHeight());
		saveBtn.setPreferredSize(dm);
		cancelBtn.setPreferredSize(dm);
		
		mainPanel.add(randFilesOpLbl, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
									      GridBagConstraints.NONE,new Insets(5,5,1,5),0,0));
										  
		randFilesOpPnl.add(randFilesOpRbs[0], new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
									              GridBagConstraints.NONE,new Insets(1,5,5,2),0,0));
		randFilesOpPnl.add(randFilesOpRbs[1], new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,
									              GridBagConstraints.NONE,new Insets(1,1,5,5),0,0));			  			  
		//
		mainPanel.add(randFilesOpPnl, new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,
									      GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		//
		mainPanel.add(createdSymLinksOpLbl, new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER,
									            GridBagConstraints.NONE,new Insets(5,5,1,5),0,0));		
										  
		createdSymLinksOpPnl.add(createdSymLinksOpRbs[0], new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
									                          GridBagConstraints.NONE,new Insets(1,5,1,5),0,0));
		createdSymLinksOpPnl.add(createdSymLinksOpRbs[1], new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,
									                          GridBagConstraints.NONE,new Insets(1,5,5,5),0,0));
		//
		mainPanel.add(createdSymLinksOpPnl, new GridBagConstraints(0,3,1,1,1,1,GridBagConstraints.CENTER,
									            GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		mainPanel.add(presetLbl, new GridBagConstraints(0,4,1,1,1,1,GridBagConstraints.CENTER,
									 GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
									 
		mainPanel.add(presetCb, new GridBagConstraints(0,5,1,1,0,0,GridBagConstraints.CENTER,
									 GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
									 
		mainPanel.add(loadPresetBtn, new GridBagConstraints(0,6,1,1,0,0,GridBagConstraints.CENTER,
									     GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		buttonPnl.add(saveBtn);
		buttonPnl.add(cancelBtn);
		//
		mainPanel.add(buttonPnl, new GridBagConstraints(0,7,1,1,1,1,GridBagConstraints.CENTER,
									 GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		mainFrame.add(mainPanel);
		
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(parent);
		mainFrame.setVisible(true);
	}
	
	//load presets in ComboBox
	private void loadPresets(JComboBox<RandomizerPreset> cb) throws IOException, ClassNotFoundException{
		File file = new File("prefs"+File.separator+"randomizerpresets.cfg");
		
		if(file.exists()){
			cb.removeAllItems();
			
			try(ObjectInputStream ois = 
			new ObjectInputStream(new FileInputStream(file))){
				Object o = ois.readObject();
				
				if(o instanceof ArrayList){
					//This arraylist must be raw type
					ArrayList list = (ArrayList)o;
					
					if(list.isEmpty())
						cb.addItem(new RandomizerPreset("None"));
					else
						for(Object elem : list)
							if(elem instanceof RandomizerPreset)
								cb.addItem((RandomizerPreset)elem);
				}
			}
		}
		else cb.addItem(new RandomizerPreset("None"));
	}
	
	private void saveSettings(){
		File file = new File("prefs"+File.separator+"randomizersettings.cfg");
		
		try(FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos)){
			
			dos.writeBoolean(randFilesOpRbs[0].isSelected());
			dos.writeBoolean(randFilesOpRbs[1].isSelected());
			dos.writeBoolean(createdSymLinksOpRbs[0].isSelected());
			dos.writeBoolean(createdSymLinksOpRbs[1].isSelected());
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(
			mainFrame,
			"<html>Serious Error Occured! Writing settings failed!<br>"+
			"Error Description: "+e.getMessage()+"</html>",
			"IOException",JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(
		mainFrame,
		"<html>All changes are applied!</html>",
		"Save Sucessfully",JOptionPane.INFORMATION_MESSAGE);
	}
	
	private boolean loadSettings(){
		File file = new File("prefs"+File.separator+"randomizersettings.cfg");
		
		if(!file.exists())
			return false;
		
		try(FileInputStream fis = new FileInputStream(file);
            DataInputStream das = new DataInputStream(fis)){
			
			randFilesOpRbs[0].setSelected(das.readBoolean());
			randFilesOpRbs[1].setSelected(das.readBoolean());
			createdSymLinksOpRbs[0].setSelected(das.readBoolean());
			createdSymLinksOpRbs[1].setSelected(das.readBoolean());
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(
					mainFrame,
					"<html>Serious Error Occured! Loading settings failed!<br>"+
					"Error Description: "+e.getMessage()+"</html>",
					"IOException",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	private void addActions(){
		RandomizerSettingsAction rsa = new RandomizerSettingsAction();
		
		for(JRadioButton rb : randFilesOpRbs)
			rb.addActionListener(rsa);
		for(JRadioButton rb : createdSymLinksOpRbs)
			rb.addActionListener(rsa);
		
		saveBtn.addActionListener(rsa);
		loadPresetBtn.addActionListener(rsa);
		cancelBtn.addActionListener(rsa);
	}
	
	private class RandomizerSettingsAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				actionSource(e);
			}
			catch(IOException f){
				f.printStackTrace();
			}
		}
	}
	
	private void actionSource(ActionEvent e) throws IOException{
		
		if(e.getSource() == randFilesOpRbs[0]){
			if(!createdSymLinksOpRbs[1].isEnabled())
				createdSymLinksOpRbs[1].setEnabled(true);
		}
		else if(e.getSource() == randFilesOpRbs[1]){
			createdSymLinksOpRbs[1].setEnabled(false);
			createdSymLinksOpRbs[0].setSelected(true);
		}
		else if(e.getSource() == saveBtn){
			saveSettings();
		}
		else if(e.getSource() == loadPresetBtn){
			randDialogRef.loadPreset((RandomizerPreset)presetCb.getSelectedItem());
		}
		else if(e.getSource() == cancelBtn){
			mainFrame.dispose();
		}
	}
}
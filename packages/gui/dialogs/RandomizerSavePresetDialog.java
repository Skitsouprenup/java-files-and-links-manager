package gui.dialogs;

import gui.CharacterLimiter;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

class RandomizerSavePresetDialog{
	
	private JLabel presetNameLbl;
	private JTextField presetTxt;
	
	private JButton saveBtn;
	private JButton cancelBtn;
	private JButton deleteBtn;
	
	private JPanel mainPanel;
	private JDialog mainFrame;
	
	private Window parent;
	private FileUpRandomizerDialog randDialogRef;
	
	RandomizerSavePresetDialog(Window parent, FileUpRandomizerDialog randDialogRef){
		this.parent = parent;
		this.randDialogRef = randDialogRef;
		presetNameLbl = new JLabel("Preset Name: ");
		presetTxt = new JTextField(15);
		presetTxt.setDocument(new CharacterLimiter(20));
		
		saveBtn = new JButton("Save");
		deleteBtn = new JButton("Delete");
		cancelBtn = new JButton("Cancel");
		
		mainPanel = new JPanel(new GridBagLayout());
		mainFrame = new JDialog(parent, "Save Preset", ModalityType.APPLICATION_MODAL);
		
		//for some reason, addActions() is not invoked when addComponents() is invoked
		//first. Thus, I put addActions() on top of addComponents().
		addActions();
		addComponents();
	}
	
	private void addComponents(){
		JPanel inputPnl = new JPanel(new GridBagLayout());
		JPanel btnPnl = new JPanel(new FlowLayout());
		
		//resize buttons
		Dimension dm = new Dimension();
		dm.setSize(80, saveBtn.getPreferredSize().getHeight());
		saveBtn.setPreferredSize(dm);
		deleteBtn.setPreferredSize(dm);
		cancelBtn.setPreferredSize(dm);
		
		inputPnl.add(presetNameLbl, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
									    GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		inputPnl.add(presetTxt, new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,
									GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		mainPanel.add(inputPnl, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
									GridBagConstraints.NONE,new Insets(5,2,2,2),0,0));
									
		btnPnl.add(saveBtn, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
							    GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		btnPnl.add(deleteBtn, new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,
							    GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		btnPnl.add(cancelBtn, new GridBagConstraints(2,0,1,1,1,1,GridBagConstraints.CENTER,
							      GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		mainPanel.add(btnPnl, new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,
								  GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		mainFrame.add(mainPanel);
		
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(parent);
		mainFrame.setVisible(true);
	}
	
	private void addActions(){
		RandomizerSavePresetActions rspa = new RandomizerSavePresetActions();
		
		saveBtn.addActionListener(rspa);
		deleteBtn.addActionListener(rspa);
		cancelBtn.addActionListener(rspa);
	}
	
	private void savePreset() throws IOException, ClassNotFoundException{
		ArrayList<RandomizerPreset> presets = new ArrayList<>();
		
		if(presetTxt.getText().isEmpty() || presetTxt.getText().isBlank()){
			JOptionPane.showMessageDialog(
			mainFrame,
			"Invalid Preset Name!",
			"Empty Text Field",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		File file = new File("prefs"+File.separator+"randomizerpresets.cfg");
		
		if(file.exists()){
			try(ObjectInputStream ois = 
			new ObjectInputStream(new FileInputStream(file))){
				Object o = ois.readObject();
				
				boolean oneTimeOverwrite = false;
				if(o instanceof ArrayList){
					//This arraylist must be raw type
					ArrayList savedPresets = (ArrayList)o;
					
					for(Object obj: savedPresets){
						RandomizerPreset preset = null;
						boolean overwrite = false;
						
						if(obj instanceof RandomizerPreset){
							preset = (RandomizerPreset)obj;
							
							if(preset.getPresetName().equals(presetTxt.getText())){
								
								if(!oneTimeOverwrite){
									int result = JOptionPane.showConfirmDialog(mainFrame,
									"Preset name already exists in the saved preset list.\n"
									+"Do you wanna override it?", "Preset Already Exists",
									JOptionPane.OK_CANCEL_OPTION);
								
									if(result == JOptionPane.CANCEL_OPTION){
										ois.close();
										return;
									}
									else overwrite = true;
								}
							}
							
							if(overwrite){
								if(!oneTimeOverwrite){
									presets.add(new RandomizerPreset(
									presetTxt.getText(), randDialogRef.getSourceSet(), 
									randDialogRef.getDestSet(), randDialogRef.getRecSet(),
									randDialogRef.getDestLinkSet()));
									oneTimeOverwrite = true;
								}
							}
							else presets.add(preset);
						}
					}
				} else throw new IllegalStateException("Object in saved preset is not ArrayList object!");
			
				if(!oneTimeOverwrite)
					presets.add(new RandomizerPreset(
					presetTxt.getText(), randDialogRef.getSourceSet(), 
					randDialogRef.getDestSet(), randDialogRef.getRecSet(),
					randDialogRef.getDestLinkSet()));
				
				//commit changes
				try(ObjectOutputStream oos = 
				new ObjectOutputStream(new FileOutputStream(file))){
					oos.writeObject(presets);
				}
			}
		}
		else{
			presets.add(new RandomizerPreset(
						presetTxt.getText(), randDialogRef.getSourceSet(), 
						randDialogRef.getDestSet(), randDialogRef.getRecSet(),
						randDialogRef.getDestLinkSet()));
			try(ObjectOutputStream oos = 
			new ObjectOutputStream(new FileOutputStream(file))){
				oos.writeObject(presets);
			}
		}
		
		JOptionPane.showMessageDialog(
		mainFrame,"Preset has been saved!",
		"Save Successful",JOptionPane.INFORMATION_MESSAGE);
		presetTxt.setText("");
	}
	
	private void deletePreset() throws IOException, ClassNotFoundException{
		ArrayList<RandomizerPreset> presets = new ArrayList<>();
		
		if(presetTxt.getText().isEmpty() || presetTxt.getText().isBlank()){
			JOptionPane.showMessageDialog(
			mainFrame,
			"Invalid Preset Name!",
			"Empty Text Field",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		File file = new File("prefs"+File.separator+"randomizerpresets.cfg");
		if(!file.exists()){
			JOptionPane.showMessageDialog(
			mainFrame,
			"No saved presets found!",
			"Saved Presets Can't be Found",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		boolean nameDeleted = false;
		try(ObjectInputStream ois = 
			new ObjectInputStream(new FileInputStream(file))){
			Object o = ois.readObject();
			
			if(o instanceof ArrayList){
				ArrayList savedPresets = (ArrayList)o;
				
				if(savedPresets.isEmpty()){
					JOptionPane.showMessageDialog(
					mainFrame,
					"Saved preset is empty!",
					"Empty Saved Preset",JOptionPane.ERROR_MESSAGE);
					ois.close();
					return;
				}
				
				for(Object obj: savedPresets){
					RandomizerPreset preset = null;
					if(obj instanceof RandomizerPreset){
						preset = (RandomizerPreset)obj;
						if(preset.getPresetName().equals(presetTxt.getText())){
							if(!nameDeleted){
								int result = 
								JOptionPane.showConfirmDialog(mainFrame,
								"Are you sure you wanna delete \""+presetTxt.getText()+
								"\" preset?", "Confirm Deletion",
								JOptionPane.OK_CANCEL_OPTION);
								if(result == JOptionPane.CANCEL_OPTION){
									ois.close();
									return;
								}
								else nameDeleted = true;
							}
						}
						else presets.add(preset);
					}
				}
			}
			else throw new IllegalStateException("Object in saved preset is not ArrayList object!");
			
			if(nameDeleted){
				JOptionPane.showMessageDialog(
				mainFrame,"Preset has been deleted!",
				"Delete Successful",JOptionPane.INFORMATION_MESSAGE);
				presetTxt.setText("");
				
				try(ObjectOutputStream oos = 
				new ObjectOutputStream(new FileOutputStream(file))){
					oos.writeObject(presets);
				}
			}
			else
				JOptionPane.showMessageDialog(
				mainFrame,"Preset Name is not registered in the saved preset list!",
				"Name Not Registered",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void actionSource(ActionEvent e) throws IOException, ClassNotFoundException{
		
		if(e.getSource() == saveBtn){
			savePreset();
		}
		else if(e.getSource() == deleteBtn){
			deletePreset();
		}
		else if(e.getSource() == cancelBtn){
			mainFrame.dispose();
		}
	}
	
	private class RandomizerSavePresetActions implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			try{
				actionSource(e);
			}
			catch(IOException | ClassNotFoundException f){
				f.printStackTrace();
			}
			catch(Exception f){
				f.printStackTrace();
			}
		}
	}
}
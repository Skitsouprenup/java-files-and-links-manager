package gui.dialogs.subpanels.prefs_subpanel;

import main.PreferencesConfig;

import java.awt.Dialog.ModalityType;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import java.util.ArrayList;
import fileoperations.LinkOperations;

class FileGenPresetDialog{
	private ArrayList<Preset> presets;
	private Preset selectedPreset;
	private ArrayList<String> hostList;
	
	private PrefsFileGenSubPnl parentPanel;
	
	private boolean isAdd;
	private JDialog dialog;
	private JPanel panel, hostPanel;
	private JList<Object> suppHosts;
	private JButton addHost, deleteHost;
	private JButton approve, cancel;
	private JTextField presetTxt, hostTxt;
	private JLabel presetLbl, suppHostsLbl, addHostLbl;
	private JLabel noneLbl;
	
	//Add Preset
	FileGenPresetDialog(ArrayList<Preset> presets, PrefsFileGenSubPnl parentPanel){
		this.presets = presets;
		this.parentPanel = parentPanel;
		isAdd = true;
		hostList = new ArrayList<>();
		initializeComponents(parentPanel.getDialog(), "Add Preset");
	}
	
	//Edit Preset
	FileGenPresetDialog(ArrayList<Preset> presets, Preset selectedPreset,
	                    PrefsFileGenSubPnl parentPanel){
		this.presets = presets;
		this.selectedPreset = selectedPreset;
		this.parentPanel = parentPanel;
		isAdd = false;
		hostList = new ArrayList<>(selectedPreset.getSupportedHosts());
		initializeComponents(parentPanel.getDialog(), "Edit Preset");
	}
	
	private void initializeComponents(JDialog rootDialog, String title){
		dialog = new JDialog(rootDialog, title, ModalityType.DOCUMENT_MODAL);
		panel = new JPanel(new GridBagLayout());
		hostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		noneLbl = new JLabel("None");
		
		suppHosts = new JList<>();
		suppHosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if(!hostList.isEmpty()){
			suppHosts.setListData(hostList.toArray());
			hostPanel.add(suppHosts);
		}
		else hostPanel.add(noneLbl);
		//
		addHost = new JButton("Add Host");
		deleteHost = new JButton("Remove Host");
		if(isAdd) approve = new JButton("Add Preset");
		else approve = new JButton("Apply");
		cancel = new JButton("Cancel");
		//
		presetTxt = new JTextField(15);
		if(selectedPreset != null)
			presetTxt.setText(selectedPreset.getPresetName());
		
		hostTxt = new JTextField(15);
		//
		presetLbl = new JLabel("Preset Name: ");
		addHostLbl = new JLabel("Host to Add: ");
		suppHostsLbl = new JLabel("Supported Host/s");
		//
		JPanel presetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		presetPanel.add(presetLbl);
		presetPanel.add(presetTxt);
		//
		JPanel addHostPanel = new JPanel(new GridBagLayout());
		addHostPanel.add(addHostLbl, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,
							   GridBagConstraints.NONE,new Insets(5,14,5,5),0,0));
		addHostPanel.add(hostTxt, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,
							   GridBagConstraints.NONE,new Insets(5,1,5,1),0,0));
		addHostPanel.add(addHost, new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.WEST,
							   GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		//
		JPanel approveCancel = new JPanel(new FlowLayout());
		approveCancel.add(approve);
		approveCancel.add(cancel);
		//
		panel.add(presetPanel, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,
							   GridBagConstraints.NONE,new Insets(5,5,1,5),0,0));
		panel.add(addHostPanel, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,
							   GridBagConstraints.NONE,new Insets(1,5,5,5),0,0));
		panel.add(suppHostsLbl, new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.CENTER,
							    GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		panel.add(hostPanel, new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.CENTER,
							 GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		panel.add(deleteHost, new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.CENTER,
							  GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		panel.add(approveCancel, new GridBagConstraints(0,5,2,1,0,0,GridBagConstraints.CENTER,
						   GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		
		dialog.add(panel);
		addActions();
		
		dialog.pack();
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(rootDialog);
		dialog.setVisible(true);
	}
	
	private void addActions(){
		PresetDialogActions actions = new PresetDialogActions();
		addHost.addActionListener(actions);
		deleteHost.addActionListener(actions);
		approve.addActionListener(actions);
		cancel.addActionListener(actions);
	}
	
	private class PresetDialogActions implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			actionSource(e);
		}
	}
	
	private void actionSource(ActionEvent e){
		
		if(e.getSource() == addHost){
			if(!hostTxt.getText().isBlank() && !hostTxt.getText().isEmpty()){
				if(LinkOperations.findSubSequence(",",hostTxt.getText(),false)){
					JOptionPane.showMessageDialog(dialog,"Comma(,) is not allowed in hostname!",
					"Invalid Preset Name",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				hostList.add(hostTxt.getText());
				suppHosts.setListData(hostList.toArray());
				hostTxt.setText("");
				
				if(hostPanel.getComponent(0) instanceof JLabel){
					hostPanel.removeAll();
					hostPanel.add(suppHosts);
					hostPanel.revalidate();
				}
				dialog.pack();
			}
		}
		else if(e.getSource() == deleteHost){
			int index = suppHosts.getSelectedIndex();
			if(index < hostList.size()){
				hostList.remove(index);
				if(!hostList.isEmpty()){

					suppHosts.setListData(hostList.toArray());
				}
				else{
					hostPanel.removeAll();
					hostPanel.add(noneLbl);
					hostPanel.revalidate();
				}
				dialog.pack();
			}
		}
		else if(e.getSource() == approve){
			if(presetTxt.getText().isEmpty() || presetTxt.getText().isBlank()){
				JOptionPane.showMessageDialog(dialog,"Preset name is not specified.",
				"No Preset Name",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int index = 0;
      //Add Preset Dialog
			if(isAdd){
				for(Preset p : presets){
					if(p.getPresetName().equals(presetTxt.getText())){
						JOptionPane.showMessageDialog(dialog,"Preset name\""
						+presetTxt.getText()+"\" already exists in the Preset record.",
							"Preset Name Already Exists",JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}

				presets.add(new Preset(presetTxt.getText(),hostList));
				index = presets.size()-1;
			}
      //Edit Preset Dialog
			else{
				Preset editPreset = null;
				for(int i = 0; i < presets.size(); i++)
					if(!presets.get(i).getPresetName().equals(selectedPreset.getPresetName())){
						if(presets.get(i).getPresetName().equals(presetTxt.getText())){
							JOptionPane.showMessageDialog(dialog,"Preset name\""
							+presetTxt.getText()+"\" already exists in the Preset record.",
								"Preset Name Already Exists",JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
					else{
						editPreset = presets.get(i);
						index = i;
					}

				editPreset.setPresetName(presetTxt.getText());
				editPreset.setSupportedHosts(hostList);

        ArrayList<String> hosts = editPreset.getSupportedHosts();
				presets.set(index, editPreset);
			}
			parentPanel.createPresetFile();
			parentPanel.loadPresetFile(index);
			parentPanel.getDialog().pack();
			
			if(!isAdd){
				//need to re-assign selected preset 'cause once we load
				//the preset file, current selected preset instance before load
				//and after load are different.
				selectedPreset = presets.get(index);
				PreferencesConfig.getPrefsConfig().setCurrentPreset(selectedPreset);
				parentPanel.createPrefsConfigFile();
				PreferencesConfig.loadPrefsConfigFile();
				parentPanel.updateReportsPaneDueToCurrentPresetChange();
			}
			
      if(isAdd){
        //Select the newly created preset as current preset
        PreferencesConfig.getPrefsConfig().setCurrentPreset(presets.get(index));
        parentPanel.createPrefsConfigFile();
        PreferencesConfig.loadPrefsConfigFile();
        parentPanel.updateReportsPaneDueToCurrentPresetChange();
			}
			
			JOptionPane.showMessageDialog(dialog,"All changes are applied!",
			"Operation Success",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == cancel){
			dialog.dispose();
		}
	}
	
}
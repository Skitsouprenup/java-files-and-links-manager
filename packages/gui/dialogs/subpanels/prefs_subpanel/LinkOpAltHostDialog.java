package gui.dialogs.subpanels.prefs_subpanel;

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

class LinkOpAltHostDialog{
	private ArrayList<AlternativeHost> hostnames;
	private AlternativeHost selectedHost;
	private ArrayList<String> altHostList;
	
	private PrefsLinkOpSubPnl parentDialog;
	
	private boolean isAdd;
	private JDialog dialog;
	private JPanel panel, altHostPanel;
	private JList<Object> altHosts;
	private JButton addHost, deleteHost;
	private JButton approve, cancel;
	private JTextField hostTxt, newAltHostTxt;
	private JLabel hostLbl, newAltHostLbl, altHostLbl;
	private JLabel noneLbl;
	
	//Add Host
	LinkOpAltHostDialog(ArrayList<AlternativeHost> hostnames, PrefsLinkOpSubPnl parentDialog){
		this.hostnames = hostnames;
		this.parentDialog = parentDialog;
		isAdd = true;
		altHostList = new ArrayList<>();
		initializeComponents(parentDialog.getDialog(), "Add Alternative Host");
	}
	
	//Edit Host
	LinkOpAltHostDialog(ArrayList<AlternativeHost> hostnames, AlternativeHost selectedHost,
	                    PrefsLinkOpSubPnl parentDialog){
		this.hostnames = hostnames;
		this.selectedHost = selectedHost;
		this.parentDialog = parentDialog;
		isAdd = false;
		altHostList = new ArrayList<>(selectedHost.getAltHostNames());
		initializeComponents(parentDialog.getDialog(), "Edit Alternative Host");
	}
	
	private void initializeComponents(JDialog rootDialog, String title){
		dialog = new JDialog(rootDialog, title, ModalityType.DOCUMENT_MODAL);
		panel = new JPanel(new GridBagLayout());
		altHostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		noneLbl = new JLabel("None");
		
		altHosts = new JList<>();
		altHosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if(!altHostList.isEmpty()){
			altHosts.setListData(altHostList.toArray());
			altHostPanel.add(altHosts);
		}
		else altHostPanel.add(noneLbl);
		//
		addHost = new JButton("Add Host");
		deleteHost = new JButton("Remove Host");
		if(isAdd) approve = new JButton("Add Host");
		else approve = new JButton("Apply");
		cancel = new JButton("Cancel");
		//
		hostTxt = new JTextField(15);
		if(selectedHost != null)
			hostTxt.setText(selectedHost.getHostName());
		
		newAltHostTxt = new JTextField(15);
		//
		hostLbl = new JLabel("Host Name: ");
		newAltHostLbl = new JLabel("<html><p style='text-align:left;'>Alternative<br /> Host to Add: </p></html>");
		altHostLbl = new JLabel("Alternative Host/s");
		//
		JPanel hostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		hostPanel.add(hostLbl);
		hostPanel.add(hostTxt);
		//
		JPanel addAltHostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		addAltHostPanel.add(newAltHostLbl);
		addAltHostPanel.add(newAltHostTxt);
		addAltHostPanel.add(addHost);
		//
		JPanel approveCancel = new JPanel(new FlowLayout());
		approveCancel.add(approve);
		approveCancel.add(cancel);
		//
		panel.add(hostPanel, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,
							   GridBagConstraints.NONE,new Insets(5,5,1,5),0,0));
		panel.add(addAltHostPanel, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,
							   GridBagConstraints.NONE,new Insets(1,5,5,5),0,0));
		panel.add(altHostLbl, new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.CENTER,
							    GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		panel.add(altHostPanel, new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.CENTER,
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
		AltHostDialogActions actions = new AltHostDialogActions();
		addHost.addActionListener(actions);
		deleteHost.addActionListener(actions);
		approve.addActionListener(actions);
		cancel.addActionListener(actions);
	}
	
	private class AltHostDialogActions implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			actionSource(e);
		}
	}
	
	private void actionSource(ActionEvent e){
		
		if(e.getSource() == addHost){
			if(!newAltHostTxt.getText().isBlank() && !newAltHostTxt.getText().isEmpty()){
				if(LinkOperations.findSubSequence(",",newAltHostTxt.getText(),false)){
					JOptionPane.showMessageDialog(dialog,"Comma(,) is not allowed in alternative hostname!",
					"Invalid Preset Name",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				altHostList.add(newAltHostTxt.getText());
				altHosts.setListData(altHostList.toArray());
				newAltHostTxt.setText("");
				
				if(altHostPanel.getComponent(0) instanceof JLabel){
					altHostPanel.removeAll();
					altHostPanel.add(altHosts);
					altHostPanel.revalidate();
				}
				dialog.pack();
			}
		}
		else if(e.getSource() == deleteHost){
			int index = altHosts.getSelectedIndex();
			if(index >= 0 && index < altHostList.size()){
				altHostList.remove(index);
				if(!altHostList.isEmpty()){
					altHosts.setListData(altHostList.toArray());
				}
				else{
					altHostPanel.removeAll();
					altHostPanel.add(noneLbl);
					altHostPanel.revalidate();
				}
				dialog.pack();
			}
		}
		else if(e.getSource() == approve){
			if(hostTxt.getText().isEmpty() || hostTxt.getText().isBlank()){
				JOptionPane.showMessageDialog(dialog,"Host name is not specified.",
				"No Host Name",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(LinkOperations.findSubSequence(",",hostTxt.getText(),false)){
					JOptionPane.showMessageDialog(dialog,"Comma(,) is not allowed in hostname!",
					"Invalid Preset Name",JOptionPane.ERROR_MESSAGE);
					return;
				}
			
			int index = 0;
			if(isAdd){
				for(AlternativeHost ah : hostnames){
					if(ah.getHostName().equals(hostTxt.getText())){
						JOptionPane.showMessageDialog(dialog,"Host name \""
						+hostTxt.getText()+"\" already exists in the Alternative Host record.",
							"Host Name Already Exists",JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
				hostnames.add(new AlternativeHost(hostTxt.getText(),altHostList));
				index = hostnames.size()-1;
			}
			else{
				AlternativeHost editHost = null;
				for(int i = 0; i < hostnames.size(); i++)
					if(!hostnames.get(i).getHostName().equals(selectedHost.getHostName())){
						if(hostnames.get(i).getHostName().equals(hostTxt.getText())){
							JOptionPane.showMessageDialog(dialog,"Host name \""
							+hostTxt.getText()+"\" already exists in the Alternative Host record.",
								"Host Name Already Exists",JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
					else{
						editHost = hostnames.get(i);
						index = i;
					}
				editHost.setHostName(hostTxt.getText());
				editHost.setAltHostNames(altHostList);
				hostnames.set(index, editHost);
			}
			parentDialog.createPresetFile();
			parentDialog.loadPresetFile(index);
			parentDialog.getDialog().pack();
			
			if(!isAdd){
				//need to re-assign selected host 'cause once we load
				//the Alternative Host file, current selected host instance before load
				//and after load are different.
				selectedHost = hostnames.get(index);
			}
			
			JOptionPane.showMessageDialog(dialog,"All changes are applied!",
			"Operation Success",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == cancel){
			dialog.dispose();
		}
	}
}
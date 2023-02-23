package gui.dialogs.subpanels.prefs_subpanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PrefsLinkOpSubPnl{
	private final SubPnlActions spa;
	
	//Hosts
	private ArrayList<AlternativeHost> hostnames;
	
	private final JLabel[] lbls;
	private JDialog mainDialogRef;
	private final JPanel panel, altHostsPanel;
	private final JComboBox<AlternativeHost> hosts;
	private final JButton addHost, editHost, deleteHost;
	
	public PrefsLinkOpSubPnl(JDialog mainDialogRef){
		lbls = new JLabel[2];
		lbls[0] = new JLabel("Hostname/s: ");
		lbls[1] = new JLabel("Alternative Hostname/s", SwingConstants.LEFT);
		
		hostnames = new ArrayList<>();
		this.mainDialogRef = mainDialogRef;
		
		panel = new JPanel(new GridBagLayout());
		altHostsPanel = new JPanel(new GridBagLayout());
		hosts = new JComboBox<>();
		
		addHost = new JButton("Add Host");
	    editHost = new JButton("Edit Host");
		deleteHost = new JButton("Delete Host");
		
		boolean isLoaded = loadPresetFile(0);
		addComponents(isLoaded);
		
		spa = new SubPnlActions();
		addHost.addActionListener(spa);
		editHost.addActionListener(spa);
		deleteHost.addActionListener(spa);
		hosts.addActionListener(spa);
	}
	
	private void addComponents(boolean isLoaded){
		JPanel hostPanel = new JPanel(new FlowLayout());
		hostPanel.add(lbls[0]);
		hostPanel.add(hosts);
		
		panel.add(hostPanel, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,
								 GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		//
		panel.add(lbls[1], new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,
							   GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		
		if(!isLoaded){
			hosts.addItem(new AlternativeHost("None"));
			altHostsPanel.add(new JLabel("None", SwingConstants.LEFT), 
		    new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,
			GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		}
				
		//
		panel.add(altHostsPanel, new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.WEST,
							      GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		//
		panel.add(addHost, new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.CENTER,
							 GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		//
		panel.add(deleteHost, new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.CENTER,
							 GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		//
		panel.add(editHost, new GridBagConstraints(0,5,1,1,0,0,GridBagConstraints.CENTER,
							 GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
	JDialog getDialog(){
		return mainDialogRef;
	}
	
	void createPresetFile(){
		File file = new File("prefs"+File.separator+"alternativehosts.cfg");
		File parentDir = file.getParentFile();
		
		if(!parentDir.exists())
			if(!parentDir.mkdir()){
				System.err.println("Error Occured: "+parentDir.getName()+
			    " couldn't be created.");
				return;
			}
			
	    try(ObjectOutputStream oos = 
			new ObjectOutputStream(new FileOutputStream(file))){
			oos.writeObject(hostnames);
			oos.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	boolean loadPresetFile(int currHostIndex){
		File file = new File("prefs"+File.separator+"alternativehosts.cfg");
		if(!file.exists())
			return false;
		
		try(ObjectInputStream ois = 
		new ObjectInputStream(new FileInputStream(file))){
			//readObject() returns an Object type. Thus,
			//after the invocation of readObject() we need
			//to convert that Object type to its original
			//type
			ArrayList arrList = (ArrayList)ois.readObject();
			
			//Temporarily remove sitePresets(JComboBox) action listener
			//because removeAllItems() and addItem() automatically
			//fires the action event of hosts
			hosts.removeActionListener(spa);
			hosts.removeAllItems();
			
			if(!arrList.isEmpty()){
				hostnames.clear();
				hostnames.ensureCapacity(arrList.size());
				
				for(Object o : arrList)
					if(o instanceof AlternativeHost)
						hostnames.add((AlternativeHost)o);
					else throw new ClassCastException(
						"Deserialized object can't be converted to AlternativeHost!");
					 
				for(AlternativeHost ah : hostnames)
					hosts.addItem(ah);
				if(currHostIndex != -1)
					hosts.setSelectedIndex(currHostIndex);
				else hosts.setSelectedIndex(0);
			}
			else{
				hosts.addItem(new AlternativeHost("None"));
				
				altHostsPanel.removeAll();
				altHostsPanel.add(new JLabel("None", SwingConstants.LEFT), 
				new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,
				GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
				altHostsPanel.revalidate();
				mainDialogRef.pack();
			}
			reDisplaySuppHosts();
			//re-register action listener with hosts after
			//removeAllItems() and addItem() are invoked
			hosts.addActionListener(spa);
			
		}
		catch(IOException | ClassNotFoundException | ClassCastException e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	private void reDisplaySuppHosts(){
		AlternativeHost ah = (AlternativeHost)hosts.getSelectedItem();
		ArrayList<String> altHosts = ah.getAltHostNames();
		if(!altHosts.isEmpty()){
			altHostsPanel.removeAll();
			for(int i = 0; i < altHosts.size(); i++){
				altHostsPanel.add(new JLabel(altHosts.get(i), SwingConstants.LEFT), 
				new GridBagConstraints(0,i,1,1,1,1,GridBagConstraints.WEST,
				GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
			}
			altHostsPanel.revalidate();
			mainDialogRef.pack();
		}
		else{
			altHostsPanel.removeAll();
			altHostsPanel.add(new JLabel("None", SwingConstants.LEFT), 
			new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,
			GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
			altHostsPanel.revalidate();
			mainDialogRef.pack();
		}
	}
	
	private class SubPnlActions implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			actionSource(e);
		}
	}
	
	private void actionSource(ActionEvent e){
		
		if(e.getSource() == addHost){
			new LinkOpAltHostDialog(hostnames, this);
		}
		else if(e.getSource() == deleteHost){
			if(hostnames.isEmpty()){
				JOptionPane.showMessageDialog(mainDialogRef,"Alternative Hosts record is empty!",
				  "Empty Preset Record",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			int result = JOptionPane.showConfirmDialog(mainDialogRef,
						 "Are you sure you want to delete \n\""
						 +hosts.getSelectedItem()+"\" Alternative Host?", "Delete Alternative Host",
						 JOptionPane.OK_CANCEL_OPTION);
						 
			if(result == JOptionPane.OK_OPTION){
				int index = hosts.getSelectedIndex();
				hostnames.remove(index);
				createPresetFile();
				loadPresetFile((index >= hostnames.size()) ? index-1 : index);
			}
		}
		else if(e.getSource() == editHost){
			if(!hostnames.isEmpty())
				new LinkOpAltHostDialog(hostnames, 
			        (AlternativeHost)hosts.getSelectedItem(), this);
			else
				JOptionPane.showMessageDialog(mainDialogRef,"Alternative Hosts record is empty!",
				  "Empty Preset Record",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == hosts){
			//System.out.println(e.getActionCommand());
			reDisplaySuppHosts();
		}
	}
}
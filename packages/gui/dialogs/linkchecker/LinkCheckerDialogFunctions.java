package gui.dialogs.linkchecker;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;

import gui.dialogs.subpanels.prefs_subpanel.AlternativeHost;
import gui.FileChooserProperties;

public class LinkCheckerDialogFunctions extends LinkCheckerDialogPopupFunctions{
	
	protected ButtonsActionListener bal;
	protected PopupActionListener pal;
	
	protected JLabel suppHostLbl, altHostLbl, searchForLbl;
	protected JButton selectDirBtn, checkLinksBtn, clearLinksBtn, searchForBtn;
	protected JComboBox<AlternativeHost> suppHostCb;
	protected JList<Object> selectedDirList;
	protected JList<Object> searchForList;
	
	protected JDialog mainFrame;
	protected JPanel mainPanel;
	protected JPanel altHostPnl;
	protected JEditorPane ep;
	
	protected JTable linksTable;
	protected DefaultTableModel tableModel;
	
	protected ArrayList<Object> hostArrList;
	protected LinkedHashSet<Object> dirArrList;
	
	protected JFileChooser fc;
	protected Window parent;
	
	protected JScrollPane altHostPane;
	
	protected LinkCheckerDialogFunctions(JEditorPane ep, Window parent){
		this.ep = ep;
		this.parent = parent;
		mainFrame = new JDialog(parent, "Link Checker", ModalityType.MODELESS);
		
		staticMainFrame = mainFrame;
		
		altHostLbl = new JLabel("Alternative Hostname/s");
		suppHostLbl = new JLabel("Supported Hostname/s");
		searchForLbl = new JLabel("Search List");
		
		selectDirBtn = new JButton("Add Directories");
		checkLinksBtn = new JButton("Check Links");
		clearLinksBtn = new JButton("Clear");
		searchForBtn = new JButton("Add to Search");
		
		selectedDirList = new JList<>();
		selectedDirList.setName("Directory Selection");
		
		searchForList = new JList<>();
		searchForList.setName("Search List");
		
		suppHostCb = new JComboBox<>();
		
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setPreferredSize(new Dimension(910, 510));
		
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setMultiSelectionEnabled(true);
		
		tableModel = new DefaultTableModel(new Object[]{"Links","Status"},0){
			@Override
			public boolean isCellEditable(int row, int column) {
				//all cells false
				return false;
			}
		};
		linksTable = new JTable(tableModel);
		linksTable.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		for(int i = 0; i < 30; i++)
			tableModel.addRow(new Object[]{"test element "+i,"test element "+i});
		
		hostArrList = new ArrayList<>();
		dirArrList = new LinkedHashSet<>();
		
		bal = new ButtonsActionListener();
		pal = new PopupActionListener();
		
		mainFrame.addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e){
				staticMainFrame = null;
			}
		});
		
	}
	
	protected boolean loadAltHostFile(){
		File file = new File("prefs\\alternativehosts.cfg");
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
			suppHostCb.removeActionListener(bal);
			suppHostCb.removeAllItems();
			
			if(arrList != null && !arrList.isEmpty()){
				for(Object o : arrList)
					if(o instanceof AlternativeHost)
						suppHostCb.addItem((AlternativeHost)o);
				
				AlternativeHost ah = 
				(AlternativeHost)suppHostCb.getSelectedItem();
				
				ArrayList<String> altHosts = ah.getAltHostNames();
				if(altHosts != null && !altHosts.isEmpty())
					for(int i = 0; i < altHosts.size(); i++){
						JPanel panel = new JPanel(new GridBagLayout());
						panel.add(new JLabel(altHosts.get(i)), 
								  new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,
							          GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
						
						altHostPnl.add(panel, new GridBagConstraints(0,i,1,1,1,1,GridBagConstraints.NORTH,
							                      GridBagConstraints.HORIZONTAL,new Insets(3,3,3,3),0,0));
					}
				
			}else suppHostCb.addItem(new AlternativeHost("None"));
			
			//re-register action listener with hosts after
			//removeAllItems() and addItem() are invoked
			suppHostCb.addActionListener(bal);
		}
		catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		return true;
	}
	
	private void btnActionSource(ActionEvent e){
		
		if(e.getSource() == searchForBtn){
			addToSearchList();
		}
		else if(e.getSource() == selectDirBtn){
			int val = fc.showOpenDialog(mainFrame);
			
			if(val == JFileChooser.APPROVE_OPTION){
				File[] modFiles = FileChooserProperties.fChooserSelectBugFix(fc);
				dirArrList.addAll(java.util.Arrays.asList(modFiles));
				selectedDirList.setListData(dirArrList.toArray());
			}
		}
		else if(e.getSource() == clearLinksBtn){
		}
		else if(e.getSource() == checkLinksBtn){
		}
		else if(e.getSource() == suppHostCb){
			AlternativeHost ah = (AlternativeHost)suppHostCb.getSelectedItem();
			ArrayList<String> altHosts = ah.getAltHostNames();
			
			altHostPnl.removeAll();
			if(altHosts != null){
				for(int i = 0; i < altHosts.size(); i++){
					JPanel panel = new JPanel(new GridBagLayout());
					panel.add(new JLabel(altHosts.get(i)), 
								  new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,
							          GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
						
						altHostPnl.add(panel, new GridBagConstraints(0,i,1,1,1,1,GridBagConstraints.NORTH,
							                      GridBagConstraints.HORIZONTAL,new Insets(3,3,3,3),0,0));
				}
			}
			altHostPnl.repaint();
			altHostPnl.revalidate();
		}
		
	}
	
	private void addToSearchList(){
		String hostAndAlts = "";
			
		if(!suppHostCb.getSelectedItem().equals("None")){
			hostAndAlts = hostAndAlts.concat(suppHostCb.getSelectedItem().toString());
				
			//rapidgator old header backward compatibility
			if(hostAndAlts.startsWith("rapidgator.net") || hostAndAlts.startsWith("rg.to"))
				hostAndAlts = hostAndAlts.concat(", rapidgator, Rapidgator");
				
			AlternativeHost selectedHost = (AlternativeHost)suppHostCb.getSelectedItem();
			ArrayList<String> altHosts = selectedHost.getAltHostNames();
				
			if(altHosts != null && altHosts.size() > 0){
				for(int i = 0; i < altHosts.size(); i++){
					if(i != altHosts.size()-1){
					   if(!altHosts.get(i).startsWith("rapidgator") || 
					   !altHosts.get(i).startsWith("Rapidgator"))
						hostAndAlts = hostAndAlts.concat(", "+altHosts.get(i));
					}
				}
			}
				
			boolean duplicate = false;
			for(Object o : hostArrList)
				if(o instanceof String){
					String s = (String)o;
						
					if(hostAndAlts.equals(s)){
						duplicate = true;
						JOptionPane.showMessageDialog(mainFrame,"Host that you wanna add is already in Search List!",
						"Duplicate Entry",JOptionPane.ERROR_MESSAGE);
						break;
					}
				}
				
			if(!duplicate){
				hostArrList.add(hostAndAlts);
				searchForList.setListData(hostArrList.toArray());
				searchForList.revalidate();
			}
		}
	}
	
	protected class ButtonsActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			btnActionSource(e);
		}
	}
	
	protected class PopupActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			popupActionSource(e, selectedDirList, searchForList, dirArrList, hostArrList);
		}
	}
}
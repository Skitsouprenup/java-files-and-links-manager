package gui.dialogs.subpanels.prefs_subpanel;

import static main.UtilityClasses.JobRef;
import static main.UtilityClasses.Jobs;
import static main.UtilityClasses.SelectionLinkRef;

import gui.subpanels.FileGenPanel;
import gui.subpanels.LinksPanel;

import main.PreferencesConfig;

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
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PrefsFileGenSubPnl{
	private final SubPnlActions spa;
	
	//Presets
	private ArrayList<Preset> presets;
	
	private final JLabel[] lbls;
	private JDialog mainDialogRef;
	private final JPanel panel;
	private JPanel supportedHosts;
	private final JComboBox<Preset> sitePresets;
	private final JButton addPreset, editPreset, deletePreset;
	
	final FileGenPanel fgp;
	final LinksPanel lp;
	final JEditorPane reportsPane;
	final JobRef jb;
	final SelectionLinkRef sr;
	
	public PrefsFileGenSubPnl(JDialog mainDialogRef, FileGenPanel fgp,
					          LinksPanel lp, JEditorPane reportsPane, JobRef jb, SelectionLinkRef sr){
						   
		this.fgp = fgp;
		this.lp = lp;
		this.reportsPane = reportsPane;
		this.jb = jb;
		this.sr = sr;
						   
		lbls = new JLabel[2];
		lbls[0] = new JLabel("Preset/s: ");
		lbls[1] = new JLabel("Supported Host/s", SwingConstants.LEFT);
		
		presets = new ArrayList<>();
		this.mainDialogRef = mainDialogRef;
		
		panel = new JPanel(new GridBagLayout());
		supportedHosts = new JPanel(new GridBagLayout());
		sitePresets = new JComboBox<>();
		addPreset = new JButton("Add Preset");
		editPreset = new JButton("Edit Preset");
		deletePreset = new JButton("Delete Preset");
		
		boolean isLoaded = loadPresetFile(0);
		addComponents(isLoaded);
		
		spa = new SubPnlActions();
		addPreset.addActionListener(spa);
		editPreset.addActionListener(spa);
		deletePreset.addActionListener(spa);
		//sitePresets.addActionListener(spa);
		
		if(PreferencesConfig.loadPrefsConfigFile()){
			if(!presets.isEmpty())
				if(PreferencesConfig.defaultCurrentPresetVerification(mainDialogRef, presets))
					{
            for(int i = 0; i < presets.size(); i++)
              if(presets.get(i).getPresetName().
                  equals(PreferencesConfig.getPrefsConfig().getCurrentPresetName())){
                  sitePresets.removeActionListener(spa);
                  sitePresets.setSelectedIndex(i);
                  sitePresets.addActionListener(spa);
                  break;
                }
            reDisplaySuppHosts();
          }
		}
		else{
			if(!presets.isEmpty())
				PreferencesConfig.setPrefsConfig(new PrefsConfig(presets.get(0)));
			else
				PreferencesConfig.setPrefsConfig(new PrefsConfig(new Preset("")));
			createPrefsConfigFile();
		}
	}
	
	private void addComponents(boolean isLoaded){
		JPanel presetPanel = new JPanel(new FlowLayout());
		presetPanel.add(lbls[0]);
		presetPanel.add(sitePresets);
		
		panel.add(presetPanel, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,
								 GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		//
		panel.add(lbls[1], new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,
							   GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		
		if(!isLoaded){
			sitePresets.addItem(new Preset("None"));
			supportedHosts.add(new JLabel("None", SwingConstants.LEFT), 
		    new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,
			GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		}
				
		//
		panel.add(supportedHosts, new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.WEST,
							      GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		//
		panel.add(addPreset, new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.CENTER,
							 GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		//
		panel.add(deletePreset, new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.CENTER,
							 GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		//
		panel.add(editPreset, new GridBagConstraints(0,5,1,1,0,0,GridBagConstraints.CENTER,
							 GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
	JDialog getDialog(){
		return mainDialogRef;
	}
	
	void createPrefsConfigFile(){
		File file = new File("prefs"+File.separator+"prefsconfig.cfg");
		File parentDir = file.getParentFile();
		
		if(!parentDir.exists())
			if(!parentDir.mkdir()){
				System.err.println("Error Occured: "+parentDir.getName()+
			    " couldn't be created.");
				return;
			}
		
		try(ObjectOutputStream oos = 
			new ObjectOutputStream(new FileOutputStream(file))){
			oos.writeObject(PreferencesConfig.getPrefsConfig());
			oos.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	void createPresetFile(){
		File file = new File("prefs"+File.separator+"sitePresets.cfg");
		File parentDir = file.getParentFile();
		
		if(!parentDir.exists())
			if(!parentDir.mkdir()){
				System.err.println("Error Occured: "+parentDir.getName()+
			    " couldn't be created.");
				return;
			}
			
	    try(ObjectOutputStream oos = 
			new ObjectOutputStream(new FileOutputStream(file))){
			oos.writeObject(presets);
			oos.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	boolean loadPresetFile(int currPresetIndex){
		File file = new File("prefs"+File.separator+"sitePresets.cfg");
		if(!file.exists())
			return false;
		
		try(ObjectInputStream ois = 
		new ObjectInputStream(new FileInputStream(file))){
			
			//readObject() returns an Object type. Thus,
			//after the invocation of readObject() we need
			//to convert that Object type to its original
			//type
			Object deserializedObj = ois.readObject();
			ArrayList arrList = null;
			if(deserializedObj instanceof ArrayList)
				arrList = (ArrayList)deserializedObj;
			else throw new NullPointerException("Deserialized object must be an ArrayList!");
			
			//Temporarily remove sitePresets(JComboBox) action listener
			//because removeAllItems() and addItem() automatically
			//fires the action event of sitePresets
			sitePresets.removeActionListener(spa);
			sitePresets.removeAllItems();
			
			if(!arrList.isEmpty()){
				presets.clear();
				presets.ensureCapacity(arrList.size());
				
				for(Object o : arrList)
					if(o instanceof Preset)
						presets.add((Preset)o);
					else throw new ClassCastException(
						"Deserialized preset/s can't be converted to Preset!");
					 
				for(Preset p : presets)
					sitePresets.addItem(p);
				sitePresets.setSelectedIndex(currPresetIndex);
			}
			else{
				sitePresets.addItem(new Preset("None"));
				
				supportedHosts.removeAll();
				supportedHosts.add(new JLabel("None", SwingConstants.LEFT), 
				new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,
				GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
				supportedHosts.revalidate();
				mainDialogRef.pack();
			}
			reDisplaySuppHosts();
			//re-register action listener with sitePresets after
			//removeAllItems() and addItem() are invoked
			sitePresets.addActionListener(spa);
			
		}
		catch(IOException | ClassNotFoundException | ClassCastException e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	private void reDisplaySuppHosts(){
		Preset p = (Preset)sitePresets.getSelectedItem();
		ArrayList<String> suppHosts = p.getSupportedHosts();
		if(!suppHosts.isEmpty()){
			supportedHosts.removeAll();
			for(int i = 0; i < suppHosts.size(); i++){
				supportedHosts.add(new JLabel(suppHosts.get(i), SwingConstants.LEFT), 
				new GridBagConstraints(0,i,1,1,1,1,GridBagConstraints.WEST,
				GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
			}
			supportedHosts.revalidate();
			mainDialogRef.pack();
		}
		else{
			supportedHosts.removeAll();
			supportedHosts.add(new JLabel("None", SwingConstants.LEFT), 
			new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,
			GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
			supportedHosts.revalidate();
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
		
		if(e.getSource() == addPreset){
			new FileGenPresetDialog(presets, this);
		}
		else if(e.getSource() == deletePreset){
			if(presets.isEmpty()){
				JOptionPane.showMessageDialog(mainDialogRef,"Preset record is empty!",
				  "Empty Preset Record",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int result = JOptionPane.showConfirmDialog(mainDialogRef,
						 "Are you sure you want to delete \n\""
						 +sitePresets.getSelectedItem()+"\" Preset?", "Delete Preset",
						 JOptionPane.OK_CANCEL_OPTION);
						 
			if(result == JOptionPane.OK_OPTION){
				int index = sitePresets.getSelectedIndex();
				presets.remove(index);
				createPresetFile();
				loadPresetFile((index >= presets.size()) ? index-1 : index);
        //For some reason, in this block, revalidate() in loadPresetFile() method is not
        //enough to refresh 'supportedHosts' panel. Thus, repaint() is called
        //here. This problem could only persist on linux. Now sure if this problem
        //persists on windows.
        supportedHosts.repaint();
				if(presets.isEmpty())
					PreferencesConfig.getPrefsConfig().setCurrentPreset(new Preset(""));

        updateFileGenPresetAndReportsPane();
			}
		}
		else if(e.getSource() == editPreset){
			if(!presets.isEmpty()) {
        new FileGenPresetDialog(presets, 
			        (Preset)sitePresets.getSelectedItem(), this);
      }
			else
				JOptionPane.showMessageDialog(mainDialogRef,"Preset record is empty!",
				  "Empty Preset Record",JOptionPane.ERROR_MESSAGE);
		}
		else if(e.getSource() == sitePresets){
			//System.out.println(e.getActionCommand());
      reDisplaySuppHosts();
			updateFileGenPresetAndReportsPane();
		}
	}
	
  void updateFileGenPresetAndReportsPane() {
    if(!presets.isEmpty()){
      PreferencesConfig.getPrefsConfig().setCurrentPreset((Preset)sitePresets.getSelectedItem());
      createPrefsConfigFile();
      PreferencesConfig.loadPrefsConfigFile();
      updateReportsPaneDueToCurrentPresetChange();
    }
  }

	void updateReportsPaneDueToCurrentPresetChange(){
		if(jb.getValue() != null)
			if(jb.getValue() == Jobs.CREATE_POST_TXT){
				String newText = fgp.updateCreatePostMessage(fgp);
				gui.GUIEventSource.refreshMsgBeforeOp(newText);
				reportsPane.setText(newText);
			}
			else if(jb.getValue() == Jobs.PUT_LINKS){
				StringBuilder sb = new StringBuilder();
						
				sb.append(lp.createMessage(lp, sr, mainDialogRef));
				sb.append(lp.checkLinks(new StringBuilder(), lp, mainDialogRef));
				gui.GUIEventSource.refreshMsgBeforeOp(sb.toString());
				reportsPane.setText(sb.toString());
			}
	}
}
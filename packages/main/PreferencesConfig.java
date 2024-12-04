package main;

import java.awt.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import gui.dialogs.subpanels.prefs_subpanel.PrefsConfig;
import gui.dialogs.subpanels.prefs_subpanel.Preset;
import gui.dialogs.subpanels.prefs_subpanel.AlternativeHost;

public class PreferencesConfig{
	private static PrefsConfig prefsConfig;
	
	//private constructor
	private PreferencesConfig(){}
	
	public static PrefsConfig getPrefsConfig(){
		return prefsConfig;
	}
	
	public static void setPrefsConfig(PrefsConfig newConfig){
		prefsConfig = newConfig;
	}
	
	public static boolean loadPrefsConfigFile(){
		File file = new File("prefs"+File.separator+"prefsconfig.cfg");
		if(!file.exists())
			return false;
		
		try(ObjectInputStream ois = 
		new ObjectInputStream(new FileInputStream(file))){
			prefsConfig = (PrefsConfig)ois.readObject();
		}
		catch(IOException | ClassNotFoundException | ClassCastException e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static boolean defaultCurrentPresetVerification(
						  Component rootFrame, 
						  ArrayList<Preset> presets){
		boolean result = true;
		
		if(prefsConfig == null){
			JOptionPane.showMessageDialog(
			rootFrame,
			"Can't find Preferences Config!",
			"Null PrefsConfig",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		ArrayList<Preset> fileGenPresets = presets;
		if(fileGenPresets == null){
			File file = new File("prefs"+File.separator+"sitePresets.cfg");
			if(!file.exists()){
				JOptionPane.showMessageDialog(
				rootFrame,
				"sitePresets.cfg can't be found!\n"+
				"Please go to Settings > Preferences > File Generation\n"+
				"to add new presets",
				"File Not Found",JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			try(ObjectInputStream ois = 
			new ObjectInputStream(new FileInputStream(file))){
				//This arraylist must be raw type
				ArrayList rawArrayList = (ArrayList)ois.readObject();
				fileGenPresets = new ArrayList<>(rawArrayList.size());
				
				for(Object o : rawArrayList)
					if(o instanceof Preset)
						fileGenPresets.add((Preset)o);
					else throw new ClassCastException(
					    "Deserialized object can't be converted to Preset!");
			}
			catch(IOException | ClassNotFoundException | ClassCastException e){
				e.printStackTrace();
			}
		}
		
		switch(prefsConfig.verifyPreset(fileGenPresets)){
				
		case 1:
		JOptionPane.showMessageDialog(
		rootFrame,
		"Current saved preset is invalid!\n"+
		"Current preset has been changed to\n"+
		"the first preset in the Preset record.",
		"Invalid Current Preset",JOptionPane.INFORMATION_MESSAGE);
		break;
				
		case 2:
		JOptionPane.showMessageDialog(
		rootFrame,
		"Preset record is empty!\n"+
		"Can't verify current preset\n"+
		"Go to Settings > Preferences > File Generation to\n"+
		"add, edit and delete presets.",
		"Empty Preset Record",JOptionPane.ERROR_MESSAGE);
		result = false;
		break;
				
		case 3:
		JOptionPane.showMessageDialog(
		rootFrame,
		"Can't find current preset!\n",
		"Null Current Preset",JOptionPane.ERROR_MESSAGE);
		result = false;
		break;
		}
		return result;
	}
	
	public static boolean notifyAndLoadPrefsConfig(Component rootFrame){
		
		File file = new File("prefs"+File.separator+"prefsconfig.cfg");
		if(prefsConfig == null){
			if(!file.exists()){
				JOptionPane.showMessageDialog(
				rootFrame,
				"Can't find Preferences Config\n"+
				"because prefsconfig.cfg doesn't exist!\n"+
				"Go to Settings > Preferences to create\n"+
				"and configure Preferences Configuration.",
				"File Not Found",JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			
			try(ObjectInputStream ois = 
			new ObjectInputStream(new FileInputStream(file))){
				prefsConfig = (PrefsConfig)ois.readObject();
			}
			catch(IOException | ClassNotFoundException | ClassCastException e){
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public static boolean verifyAndGetHostNames(Component rootFrame, StringBuilder container,
												StringBuilder separatorRegex){
		boolean isMatch = false;
		
		File file = new File("prefs"+File.separator+"alternativehosts.cfg");
		if(!file.exists()){
			JOptionPane.showMessageDialog(
			rootFrame,
			"alternativehosts.cfg doesn't exist!"+
			" Can't get alternative host names.\n"+
			"Please go to Settings > Preferences > Link Operation\n"+
			"to configure alternative host names.",
			"File Not Found",JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try(ObjectInputStream ois = 
		new ObjectInputStream(new FileInputStream(file))){
			//This arraylist must be raw type
			ArrayList rawArrayList = (ArrayList)ois.readObject();

			if(prefsConfig != null){
				
				if(!defaultCurrentPresetVerification(rootFrame, null))
					return false;
				
				ArrayList<String> suppHosts = 
				new ArrayList<>(prefsConfig.getCurrentSupportedHosts());
				
				for(int i = 0; i < rawArrayList.size(); i++){
          boolean addHostToContainer = false;
					
					if(rawArrayList.get(i) instanceof AlternativeHost){
						AlternativeHost ah = (AlternativeHost)rawArrayList.get(i);

						if(suppHosts.isEmpty())
							break;

						for(int index = 0; index < suppHosts.size(); index++){

							if(suppHosts.get(index).equals(ah.getHostName())){
								
                addHostToContainer = true;
								container.append(ah.getHostName());
								ArrayList<String> altHostList = ah.getAltHostNames();
								if(!altHostList.isEmpty()){
									container.append("|");
									for(int j = 0; j < altHostList.size(); j++){
										if(j == altHostList.size()-1)
											container.append(altHostList.get(j));
										else container.append(altHostList.get(j) + "|");
									}
								}

								suppHosts.remove(index);
								isMatch = true;
								break;
							}
						}
            
						//creates a pattern that separates
						//supported hosts with its alternative
						//host name/s
            if(suppHosts.size() != 0 && addHostToContainer){
              if(separatorRegex != null) {
                container.append("||");
              }
							else {
                container.append("|");
              }
            }
             
					}
					else{
						JOptionPane.showMessageDialog(
						rootFrame,
						"Host Object in alternativehosts.config\n"+
						"is unverifiable!",
						"Security Error",JOptionPane.ERROR_MESSAGE);
						System.err.println(
						"Host Object in alternativehosts.config\n"+
						"is unverifiable!");
						System.exit(1);
					}
				}
        //System.out.println(container.toString());

				if(!isMatch){
					JOptionPane.showMessageDialog(
					rootFrame,
					"Can't find match with current supported host.\n"+
					"Please go to Settings > Preferences > Link Operation\n"+
					"to configure hosts that Link Operation supports.",
					"Security Error",JOptionPane.ERROR_MESSAGE);
					return false;
				}
				
				if(!suppHosts.isEmpty()){
					String str = "";
					for(int i = 0; i < suppHosts.size(); i++){
						if(i == suppHosts.size()-1)
							str += suppHosts.get(i);
						else
							str += suppHosts.get(i) + "\n";
					}
					JOptionPane.showMessageDialog(
					rootFrame,
					"Some supported hosts in the preset record\n"+
					"are not supported by Link Operation\n"+
					"Please go to Settings > Preferences > Link Operation\n"+
					"to configure hosts that Link Operation supports.\n\n"+
					"Unsupported hosts by Link Operation\n"+str,
					"Unsupported hosts by Link Operation",JOptionPane.ERROR_MESSAGE);
					return false;	
				}
				
				if(separatorRegex != null){
					separatorRegex.setLength(0);
					separatorRegex.append("[|][|]");
				}
				
			} else throw new NullPointerException("prefsConfig is unexpectedly null!");
			
		}
		catch(IOException | ClassNotFoundException | ClassCastException e){
			e.printStackTrace();
		}
		return true;
	}
}
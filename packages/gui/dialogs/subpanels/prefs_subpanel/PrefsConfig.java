package gui.dialogs.subpanels.prefs_subpanel;

import java.util.ArrayList;
import java.io.Serializable;

public class PrefsConfig implements Serializable{
	private static final long SerialVersionUID = 3L;
	
	private Preset currPreset;
	
	PrefsConfig(Preset currPreset){
		this.currPreset = currPreset;
	}
	
	public String getCurrentPresetName(){
		return currPreset.getPresetName();
	}
	
	public ArrayList<String> getCurrentSupportedHosts(){
		return currPreset.getSupportedHosts();
	}
	
	void setCurrentPreset(Preset currPreset){
		this.currPreset = currPreset;
	}
	
	public int verifyPreset(ArrayList<Preset> presets){
		int state = 0;
		if(currPreset == null){
			state = 3;
			return state;
		}
		
		if(!presets.isEmpty()){
			boolean validPreset = false;
			for(Preset p : presets){
				if(currPreset.getPresetName()
				   .equals(p.getPresetName())){
				   validPreset = true;
				   break;
				}
			}
			if(!validPreset){
				currPreset = presets.get(0);
				state = 1;
			}else state = 0;
		}
		else{
			currPreset = new Preset("");
			state = 2;
		}
		
		return state;
	}
}
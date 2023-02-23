package gui.dialogs.subpanels.prefs_subpanel;

import java.io.Serializable;
import java.util.ArrayList;

public class Preset implements Serializable{
	private static final long SerialVersionUID = 1L;
	
	private String presetName;
	private ArrayList<String> supportedHosts;
		
	Preset(String presetName){
		this.presetName = presetName;
		supportedHosts = new ArrayList<>();
	}
	
	Preset(String presetName, ArrayList<String> supportedHosts){
		this.presetName = presetName;
		this.supportedHosts = supportedHosts;
	}
	
	void addHost(String element){
		supportedHosts.add(element);
	}
		
	ArrayList<String> getSupportedHosts(){
		return supportedHosts;
	}
	
	void setSupportedHosts(ArrayList<String> supportedHosts){
		this.supportedHosts = supportedHosts;
	}
	
	String getPresetName(){
		return presetName;
	}
	
	void setPresetName(String presetName){
		this.presetName = presetName;
	}
	
	@Override
	public String toString(){
		return presetName;
	}
}
package gui.dialogs.subpanels.prefs_subpanel;

import java.io.Serializable;
import java.util.ArrayList;

public class AlternativeHost implements Serializable{

	private static final long SerialVersionUID = 2L;
	
	private String hostName;
	private ArrayList<String> altHostNames;
	
	public AlternativeHost(String hostName){
		this.hostName = hostName;
		altHostNames = new ArrayList<>();
	}
	
	AlternativeHost(String hostName, ArrayList<String> altHostNames){
		this.hostName = hostName;
		this.altHostNames = altHostNames;
	}
	
	void addAltHost(String element){
		altHostNames.add(element);
	}
		
	public ArrayList<String> getAltHostNames(){
		return altHostNames;
	}
	
	void setAltHostNames(ArrayList<String> altHostNames){
		this.altHostNames = altHostNames;
	}
	
	public String getHostName(){
		return hostName;
	}
	
	void setHostName(String hostName){
		this.hostName = hostName;
	}
	
	@Override
	public String toString(){
		return hostName;
	}
}	
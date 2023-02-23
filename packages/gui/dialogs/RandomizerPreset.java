package gui.dialogs;

import java.io.Serializable;
import java.util.LinkedHashSet;

class RandomizerPreset implements Serializable{
	private static final long SerialVersionUID = 4L;
	
	private String presetName;
	private LinkedHashSet<String> sourceDirs;
	private LinkedHashSet<String> destDirs;
	private LinkedHashSet<String> recordRefsDirs;
	private LinkedHashSet<String> destSymlinksDirs;
	
	RandomizerPreset(String presetName){
		this.presetName = presetName;
		sourceDirs = new LinkedHashSet<>();
		destDirs = new LinkedHashSet<>();
		recordRefsDirs = new LinkedHashSet<>();
		destSymlinksDirs = new LinkedHashSet<>();
	}
	
	RandomizerPreset(String presetName, LinkedHashSet<String> sourceDirs, LinkedHashSet<String> destDirs,
	                 LinkedHashSet<String> recordRefsDirs, LinkedHashSet<String> destSymlinksDirs){
		this.presetName = presetName;
		this.sourceDirs = sourceDirs;
		this.destDirs = destDirs;
		this.recordRefsDirs = recordRefsDirs;
		this.destSymlinksDirs = destSymlinksDirs;
	}
	
	String getPresetName(){
		return presetName;
	}
	
	LinkedHashSet<String> getSourceDirs(){
		return sourceDirs;
	}
	
	LinkedHashSet<String> destDirs(){
		return destDirs;
	}
	
	LinkedHashSet<String> getRecordDirs(){
		return recordRefsDirs;
	}
	
	LinkedHashSet<String> getDestSymlinksDirs(){
		return destSymlinksDirs;
	}
	
	public String toString(){
		return presetName;
	}
}
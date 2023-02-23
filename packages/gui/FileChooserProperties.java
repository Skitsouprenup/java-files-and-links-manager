package gui;

import static main.UtilityClasses.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class FileChooserProperties{
	
	FileChooserProperties(){}
	
	public static void setToDefaults(JFileChooser fc){	
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(true);
		fc.resetChoosableFileFilters();
		fc.setAcceptAllFileFilterUsed(true);
		fc.rescanCurrentDirectory();
	}
	
	void linksSelectType(JFileChooser fc, SelectionType sType){
		if(sType == SelectionType.SINGLE)
			fc.setMultiSelectionEnabled(false);
		else if(sType == SelectionType.MULTIPLE)
			fc.setMultiSelectionEnabled(true);
	}
	
	void zipPackagingType(JFileChooser fc, ZipPackType zp){
		if(zp == ZipPackType.PER_DIR)
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		else if(zp == ZipPackType.SINGLE_PACKAGE)
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	}
	
	void unZipSelectType(JFileChooser fc, UnzipPackType up){
		if(up == UnzipPackType.ZIP_FILE){
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileFilter(new FileNameExtensionFilter("Zip Files","zip"));
		}
	}
	
	//Fix for jfilechooser bug where the parent folder is still included in
	//the selection when user selects all of its children by clicking CTRL+A
	public static File[] fChooserSelectBugFix(JFileChooser fc){
		File[] files = fc.getSelectedFiles();
		File[] modFiles = null;
		
		if(files.length > 1){
			Comparator<File> sortType = new SortFiles().createInstance("IgnoreCase");
			Arrays.sort(files, sortType);
			if(files[0].getName().equals(files[1].getParentFile().getName()))
				modFiles = Arrays.copyOfRange(files, 1, files.length-1);
			else modFiles = files;
		}else modFiles = files;
		
		return modFiles;
	}
}
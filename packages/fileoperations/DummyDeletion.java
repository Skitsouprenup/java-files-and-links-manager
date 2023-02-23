package fileoperations;

import static main.UtilityClasses.IntObj;

import java.io.File;
import java.io.IOException;

class DummyDeletion extends FileGeneration{
	
	private File[] listSources;
	
	String deleteDummy(File[] selectedFiles, boolean includeSubDirs) throws IOException{
		StringBuilder msg1 = new StringBuilder();
		StringBuilder fMessage = new StringBuilder();
		
		//used in deleteDummy() and deleteDummySubDirs to monitor deleted dummy_file
		IntObj listDirNum = new IntObj(0);
		IntObj dummyDeleted = new IntObj(0);
		
		for(File selectedFile : selectedFiles){
			if(selectedFile.isDirectory()){
				listDirNum.setValue(listDirNum.intValue()+1);
				
				listSources = selectedFile.listFiles();
				if(!includeSubDirs){
					for(File file : listSources){
						if(file.getName().equals("dummy_file") &&
						   file.isFile()){
							file.delete();
							dummyDeleted.setValue(dummyDeleted.intValue()+1);
							msg1.append(selectedFile.getAbsolutePath()+"<br>");
						}
					}
				}
				else{
					for(File file : listSources)
						deleteDummySubDirs(file,msg1,listDirNum,dummyDeleted);
				}
			}
		}
		
		fMessage.append("<b>Directories #:</b> " + listDirNum.intValue() + 
			            "<br><b># of dummy_file that had been deleted:</b> " + dummyDeleted.intValue() +
						"<br><br>");
		if(listDirNum.intValue() == 1)
			fMessage.append("<b>Directory that has had dummy_file</b><br>");
		else if(listDirNum.intValue() > 1)
			fMessage.append("<b>Directories that have had dummy_file</b><br>");
		
		fMessage.append(msg1.toString());
		
		return fMessage.toString();
	}
}
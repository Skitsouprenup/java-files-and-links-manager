package fileoperations;

import static main.UtilityClasses.*;

import java.io.File;
import java.io.IOException;

class DummyCreation extends FileGeneration{
	
	private File[] listSources;
	
	String createDummy(File[] selectedFiles,String thresTxt,
					   boolean includeSubDirs) throws IOException{
		StringBuilder msg1 = new StringBuilder();
		StringBuilder msg2 = new StringBuilder();
		StringBuilder msg3 = new StringBuilder();
		StringBuilder fMessage = new StringBuilder();
		
		//used in createDummy() method to track number of creater dummy files,
		//existing dummy files and directories with high file size.
		IntObj listDirNum = new IntObj(0);
		IntObj dummyCreated = new IntObj(0);
		IntObj dummyExistCount = new IntObj(0);
		IntObj highFileSize = new IntObj(0);
		
		for(File selectedFile : selectedFiles){
			if(selectedFile.isDirectory()){
				listDirNum.setValue(listDirNum.intValue()+1);
				
				long threshold = 0;
				long convThres = 0;
				long subLength = 0;
				
				//convert string to int
				threshold = Integer.valueOf(thresTxt);
				
				//convert mb to byte
				//mb -> kb -> byte base10(decimal) conversion
				//mb * 1000 * 1000
				//mb -> kb -> byte base2(binary) conversion
				//mb * 1024 * 1024
				//Windows OS uses base2 byte value(1.024 per byte)
				if(threshold != 0)
					convThres = threshold*1024L*1024L;
				
				listSources = selectedFile.listFiles();
				if(!includeSubDirs){
					boolean isDummyExist = false;
					for(File file : listSources){
						if(file.getName().equals("dummy_file") &&
						   file.isFile()){
							dummyExistCount.setValue(dummyExistCount.intValue()+1);
							isDummyExist = true;
							msg2.append("<b>dummy_file</b> already exists in "+selectedFile.getAbsolutePath()+"<br>");
							break;
						}
						subLength += file.length();
					}
					
					if(!isDummyExist){
						if(subLength < convThres){
							dummyCreated.setValue(dummyCreated.intValue()+1);
							writeDummy(selectedFile,convThres);
							msg1.append("<b>dummy_file</b> has been created in "+selectedFile.getAbsolutePath()+"<br>");
						}
						else{
							highFileSize.setValue(highFileSize.intValue()+1);
							msg3.append(selectedFile.getAbsolutePath()+"<br>");
						}
					}

				}
				else{
					BoolObj isDummyExist = new BoolObj(false);
					LongObj fileSize = new LongObj(0L);
					for(File file : listSources){
						createDummySubDirs(file,isDummyExist,fileSize,convThres,msg1,msg2,msg3,
										   listDirNum,dummyExistCount,dummyCreated,highFileSize);
					}
					if(!isDummyExist.booleanValue() && (fileSize.longValue() < convThres)){
						writeDummy(selectedFile,convThres);
						dummyCreated.setValue(dummyCreated.intValue()+1);
						msg1.append("<b>dummy_file</b> has been created in "+selectedFile.getAbsolutePath()+"<br>");
					}
					else if(fileSize.longValue() > convThres){
						highFileSize.setValue(highFileSize.intValue()+1);
						msg3.append(selectedFile.getAbsolutePath()+"<br>");
					}
				}
			}
		}
		
		fMessage.append("<b>Directories #:</b> " + listDirNum.intValue() + 
			            "<br><b># of subfolders that have existing dummy_file:</b> " + dummyExistCount.intValue() +
						"<br><b># of newly created dummy_file: </b>"+ dummyCreated.intValue() +
						"<br><b># of directories with file size higher "+
						"than the threshold:</b> " + highFileSize.intValue() + "<br><br>");
		if(dummyCreated.intValue() != 0) fMessage.append(msg1.toString() + "<br>");
		if(dummyExistCount.intValue() != 0) fMessage.append(msg2.toString() + "<br>");
		if(highFileSize.intValue() != 0){
			fMessage.append("<b>directories with file size higher than the threshold</b><br>");
			fMessage.append(msg3.toString());
		}
		
		return fMessage.toString();
	}
	
}
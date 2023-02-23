package fileoperations;

import static main.UtilityClasses.*;

import java.util.zip.ZipOutputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.CRC32;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;

class ZipOperations{
	
	private final CompressMethods cm;
	private final DecompressMethods dm;
	
	ZipOperations(){
		cm = new CompressMethods();
		dm = new DecompressMethods();
	}
	
	String compressFiles(File[] selectedFiles,boolean incSubDirs,
					   boolean op1Selected,boolean op2Selected,
					   String compressType,boolean excludeCol,
					   boolean excludePostTxt) throws IOException{
		StringBuilder fMsg = new StringBuilder();
		
		if(op1Selected){
			IntObj fileCount = new IntObj(0);
			StringBuilder msg = new StringBuilder();
			StringBuilder msg1 = new StringBuilder();
			for(File source: selectedFiles){
				//skip hidden files
				if(source.isHidden()) continue;
				//exclude directories with collection keywords
				if(excludeCol)
					if(LinkOperations.findSpecStr(FileOperations.getKeywordsRegPatt(),
					   source.getName(),true) && source.isDirectory()){
						msg1.append("<b>"+source.getName() + "</b> is a collection directory."+
									"Thus, It's not going to be packed.<br><br>");
						continue;
					}					
				
				String sourceZip = source.getCanonicalPath()+File.separator+
								   source.getName()+".zip";
				File sourceFile = new File(sourceZip);
				//skip directory if sourceFile already exist there
				if(sourceFile.exists()){
					msg1.append("<a style=\"color:red;\"><b>"+sourceFile.getName() +
								"</b> already exists. Can't create zip file in <u>" +
							    sourceFile.getParentFile().getAbsolutePath() + "</u></a><br>");
					continue;
				}
				
				msg1.append("<b>Zip Path: </b>"+sourceFile.getParentFile().getAbsolutePath()+"<br>");
				
				FileOutputStream fos = new FileOutputStream(sourceZip);
				CheckedOutputStream checksum = new CheckedOutputStream(fos,new CRC32());
				ZipOutputStream zos = new ZipOutputStream(checksum);
				cm.setCompressLvl(compressType,zos);
				
				if(!incSubDirs){
					StringBuilder sb = new StringBuilder();
					sb.append( cm.zipCurDir(source,zos,sourceZip,fileCount,excludePostTxt) );
					if(sb.length() != 0){
						msg.append(sb.toString());
						msg.append("<a style=\"color:green;\"><b>"+sourceFile.getName()+
								   "</b> has been created successfully!</a>");
						msg.append("<br><br>");
					}
				}
				else{
					StringBuilder sb = new StringBuilder();
					cm.zipSubDirs(source,source.getName(),zos,sourceZip,false,
							   excludeCol,excludePostTxt,sb,fileCount);
					if(sb.length() != 0){
						msg.append(sb.toString());
						msg.append("<a style=\"color:green;\"><b>"+sourceFile.getName()+
								   "</b> has been created successfully!</a>");
						msg.append("<br><br>");
					}
				}
				msg1.append("<b># of Files:</b> " + fileCount.intValue() + "<br>");
				msg1.append(msg.toString());
				msg.setLength(0);
				fileCount.setValue(0);
				
				zos.close();
				fos.close();
				
			}
			fMsg.append(msg1.toString());
			
		}
		else if(op2Selected)
			fMsg.append( zipInOnePackage(selectedFiles,incSubDirs,compressType,excludeCol,excludePostTxt) );
		
		return fMsg.toString();
	}
	
	private String zipInOnePackage(File[] selectedFiles,boolean incSubDirs,
								 String compressType,boolean excludeCol,boolean excludePostTxt) throws IOException{
		StringBuilder msg = new StringBuilder();
		StringBuilder fMsg = new StringBuilder();
		IntObj fileCount = new IntObj(0);
		//jFileChooser sometimes has selected files even users didn't select anything
		//and that's the default current directory of file chooser or the user-selected
		//current directory. if the user doesn't select any file, The APPROVE_OPTION will do nothing.
		//Thus, the program won't execute this method. So, this code won't throw arrayoutofboundsexeception
		String sourceZip = selectedFiles[0].getParentFile().getCanonicalPath()+File.separator+
						   selectedFiles[0].getParentFile().getName()+".zip";
		File sourceFile = new File(sourceZip);
		if(sourceFile.exists()){
			msg.append("<a style=\"color:red;\"><b>"+sourceFile.getName() +
					   "</b> already exists. Can't create directory in <u>" +
					   sourceFile.getParentFile().getAbsolutePath() + "</u></a><br>");
			return "";
		}
		
		msg.append("<b>Zip Path: </b>"+sourceFile.getParentFile().getAbsolutePath()+"<br>");
		
		FileOutputStream fos = new FileOutputStream(sourceZip);
		CheckedOutputStream checksum = new CheckedOutputStream(fos,new CRC32());
		ZipOutputStream zos = new ZipOutputStream(checksum);
		cm.setCompressLvl(compressType,zos);
		
		for(File source: selectedFiles){
			//skip hidden files
			if(source.isHidden()) continue;
			//don't pack the source file itself
			if(source.getCanonicalPath().equals(sourceZip)) continue;
			//exclude directories with collection keywords
			if(excludeCol)
				if(LinkOperations.findSpecStr(FileOperations.getKeywordsRegPatt(),
				   source.getName(),true) && source.isDirectory()){
					msg.append("<b>"+source.getName() + "</b> is a collection directory."+
								"Thus, It's not going to be packed.<br><br>");
					continue;
				}	
			
			if(!incSubDirs){
				if(excludePostTxt)
					if(source.getName().equals("post.txt") && source.isFile())
						continue;
				
				if(source.isDirectory()){
					
					String fName = null;
					if(source.getCanonicalPath().endsWith(File.separator)){
						fName = source.getName();
						zos.putNextEntry(new ZipEntry(fName));
						zos.closeEntry();
					}
					else{
						fName = source.getName()+File.separator;
						zos.putNextEntry(new ZipEntry(fName));
						zos.closeEntry();
					}
					
					for(File subSource : source.listFiles()){
						if(subSource.isDirectory()) continue;
						
					if(excludePostTxt)
						if(subSource.getName().equals("post.txt") && subSource.isFile())
							continue;
						
						cm.writeFileToZip(subSource,zos,fName+subSource.getName());
						msg.append("<u>"+ fName+subSource.getName() + "</u> is packed.<br>");
						fileCount.setValue(fileCount.intValue()+1);
					}
					continue;
				}
				
				cm.writeFileToZip(source,zos,source.getName());
				msg.append("<u>"+ source.getName() + "</u> is packed.<br>");
				fileCount.setValue(fileCount.intValue()+1);
			}
			else cm.zipSubDirs(source,source.getName(),zos,sourceZip,true,excludeCol,excludePostTxt,msg,fileCount);
		}
		zos.close();
		fos.close();
		
		fMsg.append("<b># of Files:</b> " + fileCount.intValue() + "<br>");
		if(msg.length() != 0)
			fMsg.append(msg.toString());
		fMsg.append("<a style=\"color:green;\"><b>"+sourceFile.getName()+"</b> has been created successfully!</a>");
		fMsg.append("<br><br>");
		
		return fMsg.toString();
	}

	String deCompressFiles(File[] selectedFiles,boolean incSubDirs,
						 UnzipPackType uType) throws IOException{
		StringBuilder msg = new StringBuilder();
		dm.unZipInDirs(selectedFiles,incSubDirs,uType,msg);
		return msg.toString();
	}	
}

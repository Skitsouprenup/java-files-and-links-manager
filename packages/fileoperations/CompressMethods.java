package fileoperations;

import static main.UtilityClasses.IntObj;

import java.util.zip.Deflater;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class CompressMethods{
	
	//This indicates folder hierarchy if there are mulitple folders in a parent folder.
	//0-1: parent folder. I prefer to put the content of the parent in the root entry of
	//a zip file. So, I skip the additional folder creation in the root entry.
	//>1: sub folders.
	private int count;
	
	void setCompressLvl(String compressType,ZipOutputStream zos){
		
		switch(compressType){
			case "Best":
			zos.setLevel(Deflater.BEST_COMPRESSION);
			break;
					
			case "Normal":
			zos.setLevel(Deflater.DEFAULT_COMPRESSION);
			break;
					
			case "Fastest":
			zos.setLevel(Deflater.BEST_SPEED);
			break;
					
			case "Store":
			zos.setLevel(Deflater.NO_COMPRESSION);
			break;
		}
	}
	
	String zipCurDir(File source,ZipOutputStream zos,String zipSource,
							 IntObj fileCount,boolean excludePostTxt) throws IOException {
		StringBuilder msg = new StringBuilder();
		if(source.isDirectory()){
			File[] listSource = source.listFiles();
			for(File file : listSource){
				if(file.isFile()){
					if(file.getCanonicalPath().equals(zipSource)) continue;
					
					if(excludePostTxt)
						if(file.getName().equals("post.txt"))
						   continue;	
					
					writeFileToZip(file,zos,file.getName());
					msg.append("<u>"+ file.getName() + "</u> is packed.<br>");
					fileCount.setValue(fileCount.intValue()+1);
				}
			}
		}
		return msg.toString();
	}
	
	void zipSubDirs(File source,String fName,ZipOutputStream zos,
							String zipSource,boolean isOnePackage,boolean excludeCol,
							boolean excludePostTxt,StringBuilder msg,IntObj fileCount) throws IOException {
		//exclude hidden directories
		if(source.isDirectory()){
			if(source.isHidden()) return;
			
			if(excludeCol)
				if(LinkOperations.findSpecStr(FileOperations.getKeywordsRegPatt(),
				   source.getName(),true)){
					msg.append("<u><b>"+fName+"</b></u> is a collection directory."+
								"Thus, It's not going to be packed.<br>");
					return;
				}
			
			count++;
			//this will include empty directories in the zip package
			if(count > 1 || isOnePackage){
				if(source.getCanonicalPath().endsWith(File.separator)){
					zos.putNextEntry(new ZipEntry(fName));
					zos.closeEntry();
				}
				else{
					zos.putNextEntry(new ZipEntry(fName+File.separator));
					zos.closeEntry();
				}
			}
			
			File[] listSource = source.listFiles();
			for(File file : listSource){
				
				if(excludePostTxt)
					if(file.getName().equals("post.txt") && file.isFile())
						continue;
				
				if(count > 1 || isOnePackage)
					zipSubDirs(file,fName + File.separator + file.getName(),zos,zipSource,
							   isOnePackage,excludeCol,excludePostTxt,msg,fileCount);
				else zipSubDirs(file,file.getName(),zos,zipSource,isOnePackage,
								excludeCol,excludePostTxt,msg,fileCount);
			}
			count--;
			return;
		}
		//This code prevents the program from zipping the zip file
		//that is currently in the process of zipping that can lead
		//to an infinite loop and consumes more space
		if(source.getCanonicalPath().equals(zipSource)) return;
		
		writeFileToZip(source,zos,fName);
		msg.append("<u>"+fName+"</u> is packed.<br>");
		fileCount.setValue(fileCount.intValue()+1);
	}
	
	void writeFileToZip(File source, ZipOutputStream zos, String fName) throws IOException{
		FileInputStream fis = new FileInputStream(source);
		ZipEntry ze = new ZipEntry(fName);
		zos.putNextEntry(ze);
		
		byte[] buffer = new byte[2048];
		int ln = 0;
		while( (ln = fis.read(buffer)) >= 0 )
			zos.write(buffer,0,ln);
		
		fis.close();
	}
}
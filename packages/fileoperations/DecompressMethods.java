package fileoperations;

import static main.UtilityClasses.UnzipPackType;
import static main.UtilityClasses.BoolObj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

class DecompressMethods{
	int fileCount = 0;
	
	void unZipInDirs(File[] selectedFiles,boolean incSubDirs,
							 UnzipPackType uType,StringBuilder msg) throws IOException{
		
		if(uType == UnzipPackType.DIRECTORY){
			if(!incSubDirs){
				for(File source : selectedFiles){
					File[] listSources = source.listFiles();
					
					boolean hasZip = false;
					for(File subSource : listSources){
						if(subSource.getName().endsWith(".zip")){
							readZipEntry(subSource,msg);
							hasZip = true;
						}
					}
					if(!hasZip)
						msg.append("Couldn't find any <b>zip</b> files in <u>" + 
								   source.getAbsolutePath()+"</u><br>");
				}
			}
			else{
				BoolObj hasZip = new BoolObj(false);
				for(File source : selectedFiles){
					if(source.isDirectory()){
						for(File subSource : source.listFiles())
							unZipDirChain(subSource,msg,hasZip);
						if(!hasZip.booleanValue())
							msg.append("Couldn't find any <b>zip</b> files in <u>" + 
									   source.getAbsolutePath()+"</u><br>");
						hasZip.setValue(false);
					}
				}
					
			}
		}
		else if(uType == UnzipPackType.ZIP_FILE){
			for(File source : selectedFiles)
				readZipEntry(source,msg);
		}
		
	}
	
	private void unZipDirChain(File source,StringBuilder msg,BoolObj hasZip) throws IOException{
		
		if(source.isDirectory()){
			File[] listSources = source.listFiles();
			BoolObj has_a_zip = new BoolObj(false);
			for(File subSource : listSources)
				unZipDirChain(subSource,msg,has_a_zip);
			if(!has_a_zip.booleanValue())
				msg.append("Couldn't find any <b>zip</b> files in <u>" + 
						   source.getAbsolutePath()+"</u><br>");
			return;
		}
		
		if(source.getName().endsWith(".zip")){
			readZipEntry(source,msg);
		    hasZip.setValue(true);
		}
	}
	
	private void readZipEntry(File source,StringBuilder message) throws IOException{
		StringBuilder msg = new StringBuilder();
		String fName = source.getName().replaceAll(".zip$","");
		File unZipPath = new File(source.getParentFile().getCanonicalPath() + File.separator + fName);
		
		if(!unZipPath.mkdir()){
			message.append("Directory <b>"+fName+"</b> can't be created. It may be already existed or\n"+
						   "you're not allowed to create directories in <b>"+
						   unZipPath.getParentFile().getAbsolutePath()+"</b><br>");
			return;
		}
		message.append("<b>Zip Source:</b> " + source.getAbsolutePath()+ "<br>");
						
		ZipInputStream zis = new ZipInputStream(new FileInputStream(source));
		ZipEntry zipEntry = zis.getNextEntry();
		
		boolean isSuccess = false;
		isSuccess = unZipFile(unZipPath,zis,zipEntry,msg);
		zis.close();
		if(isSuccess) msg.append("<a style=\"color:green;\"><b>"+source.getName()+
								 "</b> has been extracted successfully!</a><br><br>");
		else msg.append("<a style=\"color:red;\"><b>"+source.getName()+"</b> has been failed" 
						+" to be extracted!</a><br><br>");
		message.append("<b># of Files:</b> "+fileCount + "<br>");
		message.append(msg.toString());
		fileCount = 0;
	}
	
	private boolean unZipFile(File source,ZipInputStream zis,ZipEntry zipEntry,
							  StringBuilder msg) throws IOException{
		while(zipEntry != null){
			File verifiedFile = zipSlipGuard(source,zipEntry);
			boolean isDir = false;
			
			//If the name of the zip entry ends with "/" or "\"
			//then that zip entry is a directory.
			if(zipEntry.getName().endsWith(File.separator)){
				isDir = true;
			}
			
			//if the zip entry is a directory then create a directory and move on to the next entry
			//In this directory verification I use mkdirs() to create directories but we can use mkdir().
			//For some unknown reason, and it's very rare to happen, parent directories might not be
			//successfully created. So, it's better to use mkdirs()
			if(isDir){
				if(!verifiedFile.mkdirs()) {
					msg.append("Directory <b>"+zipEntry.getName()+"</b> can't be created."+
							   "you may not be allowed to create directories in <b>"+
							   verifiedFile.getParentFile().getAbsolutePath()+
							   "\n<a style=\"color:red;\">Process Aborted!</a></b><br>");
					return false;
				}
				zipEntry = zis.getNextEntry();
				continue;
			}
			//if the zip entry is a file then check first if the parent directory of that file does exist before 
			//extracting the file to avoid filenotfoundexception and other related exceptions
			else{
				File parent = verifiedFile.getParentFile();

				if(!parent.exists())
					if(!parent.mkdirs()){
						msg.append("Directory <b>"+parent.getName()+"</b> can't be created."+
						"you may not be allowed to create directories in <b>"+
						parent.getAbsolutePath()+"\n<a style=\"color:red;\">Process Aborted!</a></b><br>");
						return false;
					}
			}
			
			FileOutputStream fos = new FileOutputStream(verifiedFile);
			byte[] buffer = new byte[2048];
			int len;
			while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
			}
			fos.close();
			zipEntry = zis.getNextEntry();
			String name = verifiedFile.getParentFile().getName()+File.separator+verifiedFile.getName();
			msg.append("<u>" + name + "</u> is extracted.<br>");
			fileCount++;
		}
		return true;
	}
	
	private File zipSlipGuard(File targetPath,ZipEntry zipEntry)throws IOException{
		File dstFile = new File(targetPath,zipEntry.getName());
		
		String dirTargetPath = targetPath.getCanonicalPath();
		String filePath = dstFile.getCanonicalPath();
		
		if(!filePath.startsWith(dirTargetPath + File.separator))
			throw new IOException("Bad Entry Path: " + zipEntry.getName());
		
		return dstFile;
	}
}
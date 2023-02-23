package fileoperations;

import static main.UtilityClasses.*;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import fileoperations.approvedopfunctions.LinkOpApprovedFunctions;

abstract class FileGeneration{
	
	private File newFile;
	private FileOutputStream fos;
	private OutputStreamWriter osw;
	private BufferedWriter bw;
	
	protected void createPostSubDirs(File source, BoolObj isPostExist,
								     int index,boolean includeCol,
									 StringBuilder msg1, StringBuilder msg2,
									 IntObj listDirNum,IntObj postExistCount,
									 IntObj noPostCount) throws IOException{
			
		if(source.isDirectory()){
			listDirNum.setValue(listDirNum.intValue()+1);
			File[] files = source.listFiles();
			BoolObj postExist = new BoolObj(false);
			for(File file : files)
				createPostSubDirs(file,postExist,index,includeCol,msg1,msg2,
								  listDirNum,postExistCount,noPostCount);
			if(!postExist.booleanValue()){
				writePost(source,includeCol,index);
				noPostCount.setValue(noPostCount.intValue()+1);
				msg2.append("<b>post.txt</b> has been created in "+source.getAbsolutePath()+"<br>");
			}
			return;
		}
			
		if(source.getName().equals("post.txt") && !isPostExist.booleanValue()){
			isPostExist.setValue(true);
			postExistCount.setValue(postExistCount.intValue()+1);
			msg1.append("<b>post.txt</b> already exists in "+source.getAbsolutePath()+"<br>");
		}
	}
	
	protected void writePost(File source,boolean includeCol,int index) throws IOException{
		newFile = new File(source.getCanonicalPath() + File.separator + "post.txt");

		String postWrite = null;
		switch(index){
			case 0:
			if(includeCol){
				
				if(LinkOperations.findSpecStr(FileOperations.getKeywordsRegPatt(),
											  source.getName(),true))
				   postWrite = PostTypes.generatePostType(PostTypesConstants.POST_COLLECTION);
				else postWrite = PostTypes.generatePostType(PostTypesConstants.POST_EBOOK);
			}
			else postWrite = PostTypes.generatePostType(PostTypesConstants.POST_EBOOK);
			break;
							
			case 1:
			if(includeCol){
				if(LinkOperations.findSpecStr(FileOperations.getKeywordsRegPatt(),
											  source.getName(),true))
				   postWrite = PostTypes.generatePostType(PostTypesConstants.POST_COLLECTION);
				else postWrite = PostTypes.generatePostType(PostTypesConstants.POST_MAGAZINE);
			}
			else postWrite = PostTypes.generatePostType(PostTypesConstants.POST_MAGAZINE);
			break;
							
			case 2:
			postWrite = PostTypes.generatePostType(PostTypesConstants.POST_TUTORIAL);
			break;
		}
						
			fos = new FileOutputStream(newFile);
			osw = new OutputStreamWriter(fos,"UTF-8");
			bw = new BufferedWriter(osw);
					  
			bw.write(postWrite);
			bw.close();
	}
	
	protected void writeDummy(File source,long convThres) throws IOException{
		newFile = new File(source.getCanonicalPath() + File.separator + "dummy_file");
		RandomAccessFile raf = new RandomAccessFile(newFile,"rw");
		raf.setLength(convThres);
		raf.close();
	}
	
	protected void createDummySubDirs(File source, BoolObj isDummyExist, LongObj fileSize, 
									  long convThres, StringBuilder msg1, StringBuilder msg2,
									  StringBuilder msg3,IntObj listDirNum,IntObj dummyExistCount,
									  IntObj dummyCreated,IntObj highFileSize) throws IOException{
										  
		if(source.isDirectory()){
			listDirNum.setValue(listDirNum.intValue()+1);
			File[] files = source.listFiles();
			BoolObj dummyExist = new BoolObj(false);
			LongObj fSize = new LongObj(0L);
			for(File file : files)
				createDummySubDirs(file,dummyExist,fSize,convThres,msg1,msg2,msg3,
								   listDirNum,dummyExistCount,dummyCreated,highFileSize);
			if(!dummyExist.booleanValue() && fSize.longValue() < convThres){
				writeDummy(source,convThres);
				dummyCreated.setValue(dummyCreated.intValue()+1);
				msg1.append("<b>dummy_file</b> has been created in "+source.getAbsolutePath()+"<br>");
			}
			else if(fSize.longValue() > convThres){
				highFileSize.setValue(highFileSize.intValue()+1);
				msg3.append(source.getAbsolutePath()+"<br>");
			}
			return;
		}
		
		if(source.getName().equals("dummy_file") && !isDummyExist.booleanValue()){
			isDummyExist.setValue(true);
			dummyExistCount.setValue(dummyExistCount.intValue()+1);
			msg1.append("<b>dummy_file</b> already exists in "+source.getAbsolutePath()+"<br>");
		}
		//compute total file size of a directory as long as no dummy_file exists yet
		//otherwise, stop because there's a dummy_file already so we're going to assume
		//that the directory was computed already.
		if(!isDummyExist.booleanValue())
			fileSize.setValue(fileSize.longValue() + source.length());
		
	}
	
	protected void deleteDummySubDirs(File source, StringBuilder msg1,
									  IntObj listDirNum,IntObj dummyDeleted){
										  
		if(source.isDirectory()){
			listDirNum.setValue(listDirNum.intValue()+1);
			File[] files = source.listFiles();
			for(File file : files)
				deleteDummySubDirs(file,msg1,listDirNum,dummyDeleted);
			return;
		}
		
		if(source.getName().equals("dummy_file") &&
		   source.isFile()){
				source.delete();
				dummyDeleted.setValue(dummyDeleted.intValue()+1);
				msg1.append(source.getParent()+"<br>");
		}
	}
	
	protected void putLinksSubDirs(File source,File subSource,SelectionType sType,BoolObj linkPostExist,
								   String[] valLnksStr,IntObj linkCounter,ArrayList<File> dirs,
								   ArrayList<File> vDirs,ArrayList<File> uDirs,StringBuilder wPost,
								   ArrayList<File> subDirList,IntObj listDirNum,IntObj linkPostExistCount,
								   IntObj noLinkPostCount,IntObj processedLinks,int ln,int index,
								   String[] hostHeaders, String[] hostLinks,
								   HashMap<HashMap<String[], String[]>, String> missingHosts) throws IOException{
		
		if(subSource.getName().equals("post.txt") &&
		   subSource.isFile() && !linkPostExist.booleanValue()){
			linkPostExistCount.setValue(linkPostExistCount.intValue()+1);
			linkPostExist.setValue(true);
			if(linkCounter.intValue() < valLnksStr.length){
				writeLinks(source,subSource,sType,valLnksStr,
				           hostHeaders,hostLinks,missingHosts,
						   linkCounter);
				linkCounter.setValue(linkCounter.intValue()+1);
				processedLinks.setValue(processedLinks.intValue()+1);
				vDirs.add(source);
			}
			//has post.txt message
			wPost.append(source.getAbsolutePath() + "<br>");
		}
		
		
		if(subSource.isDirectory()){
			listDirNum.setValue(listDirNum.intValue()+1);
			dirs.add(subSource);
			subDirList.add(subSource);
		}
		
		if(index == ln-1){
			
			if(subDirList.size() != 0){
				ArrayList<File> dirList = new ArrayList<File>();
				BoolObj lnkPostExist = new BoolObj(false);
				for(int i = 0; i < subDirList.size();i++){
					
					File dirFile = (File)subDirList.get(i);
					File[] file = dirFile.listFiles();
					for(int j = 0; j < file.length; j++){
						
						putLinksSubDirs(dirFile,file[j],sType,lnkPostExist,valLnksStr,
										linkCounter,dirs,vDirs,uDirs,wPost,dirList,
										listDirNum,linkPostExistCount,noLinkPostCount,
										processedLinks,file.length,j,hostHeaders,
										hostLinks,missingHosts);
					}
					//no post.txt message
					if(!lnkPostExist.booleanValue()){
						uDirs.add(dirFile);
						noLinkPostCount.setValue(noLinkPostCount.intValue()+1);
					}
					dirList.clear();
					lnkPostExist.setValue(false);
				}
			}
		}
	}
	
	protected void writeLinks(File source,File subSource,SelectionType sType,
							  String[] links,String[] hostHeaders,String[] hostLinks,
							  HashMap<HashMap<String[], String[]>, String> missingHosts,IntObj linkCounter)
							  throws IOException,UnsupportedEncodingException{
								  
		FileInputStream fip = new FileInputStream(subSource);
		InputStreamReader isr = new InputStreamReader(fip,"UTF-8");
		BufferedReader br = new BufferedReader(isr);
								
		newFile = new File(source.getCanonicalPath() + File.separator + "post.tmp");
				
		//delete existing post.tmp if there is
		if(newFile.exists())
			newFile.delete();
				
		fos = new FileOutputStream(newFile);
		osw = new OutputStreamWriter(fos,"UTF-8");
		bw = new BufferedWriter(osw);
		
		ArrayList<String> availableHosts =
		new ArrayList<>();
		try{
			
			String lineStr = br.readLine();
			while(lineStr != null){
				bw.write(lineStr + "\r\n");
				
				for(int i = 0; i < hostHeaders.length; i++){
					if(LinkOperations.findSpecStr(hostHeaders[i],lineStr,false) && links.length != 0){
						availableHosts.add(hostLinks[i]);
						
						//If there are existing links in the link section of post.txt then right
						//those first in the new post.txt
						lineStr = br.readLine();
						while(LinkOperations.validLink(lineStr,hostLinks[i])){
							bw.write(lineStr + "\r\n");
							lineStr = br.readLine();
							
						}
				
						if(sType == SelectionType.SINGLE){
							for(String link : links){
								if(LinkOperations.validLink(link,hostLinks[i]))
									bw.write(link + "\r\n");
							}
						}
						else if(sType == SelectionType.MULTIPLE){
							String currLink = links[linkCounter.intValue()];
							if(LinkOperations.validLink(currLink,hostLinks[i]))
								bw.write(currLink + "\r\n");
						}
							
						//add one space to separate sections between
						//host headers with their links
						bw.write(lineStr + "\r\n");
						break;
					}
				}
				lineStr = br.readLine();
			}
			bw.flush();
		}
		finally{
			br.close();
			bw.close();
		}
		
		if(!subSource.delete())
			System.err.println(subSource.getName()+" couldn't be deleted.");
									
		boolean renamed = newFile.renameTo(subSource);
		//if renameTo() is not successful for some reason. Use
		//java.NIO.Paths. requires Java 7 and above
		if(!renamed){
			Path newTxt = Paths.get(newFile.getCanonicalPath());
			Files.move(newTxt, newTxt.resolveSibling("post.txt"));
		}
		
		int counterForMultDirs = (linkCounter != null) ? linkCounter.intValue() : 0;
		LinkOpApprovedFunctions.
		getMissingHostsInTxt(missingHosts, hostLinks, links, sType,
							 availableHosts, subSource.getCanonicalPath(),
							 counterForMultDirs);
	}
}
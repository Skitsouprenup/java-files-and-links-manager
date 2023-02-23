package fileoperations;

import static main.UtilityClasses.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import fileoperations.approvedopfunctions.LinkOpApprovedFunctions;

class LinkInsertion extends FileGeneration{
	
	private File[] listSources;
	
	//This method puts all links in a single directory with existing post.txt
	String putLinksDir(File selectedFile,SelectionType sType,
	                   StringBuilder links, String[] hostHeaders,
					   String[] hostLinks)
	              throws IOException,UnsupportedEncodingException {
					  
		StringBuilder message = new StringBuilder();
		boolean isPostExist = false;
		listSources = selectedFile.listFiles();
		String[] valLnksStr = links.toString().split("<br>");
		
		//hosts that are supported but their headers
		//are not defined in post.txt
		HashMap<HashMap<String[], String[]>, String> missingHosts = new HashMap<>();
		for(File file : listSources){
			if(file.getName().equals("post.txt") &&
			   file.isFile()){
				isPostExist = true;
				
				//Multiple Directories function is not supported in this method
				//that's why the last argument is null
				writeLinks(selectedFile,file,sType,valLnksStr,
				           hostHeaders,hostLinks,missingHosts,null);
				break;
			}
		}
		
		if(isPostExist){
			message.append("<u>" + selectedFile.getAbsolutePath() + "</u> path has <b>post.txt</b><br><br>");
			if(missingHosts.isEmpty())
				message.append("All valid links have been put in the <b>post.txt</b> of "
               				   + selectedFile.getAbsolutePath());
			else
				LinkOpApprovedFunctions.reportLinkOpUnsupportedHostsLinks(message, missingHosts);
		}
		else {
			message.append("<u>" + selectedFile.getAbsolutePath() + "</u> path doesn't have <b>post.txt</b><br><br>");
			message.append("<span style='color:red;'><b>No links have been processed!\nOperation failed!</b></span>");
		}
		
		return message.toString();
	}
	
	//This method distribute one link per directory with post.txt
	//Other distribution processes might be implemented in the future
	String putLinksSub(File[] selectedFiles,SelectionType sType,boolean includeSubDirs,
	                   StringBuilder links, String[] hostHeaders, String[] hostLinks)
					   throws IOException,UnsupportedEncodingException {
						   
		StringBuilder wPost = new StringBuilder();
		StringBuilder message = new StringBuilder();
		String[] valLnksStr = links.toString().split("<br>");
		
		//total directory count
		ArrayList<File> dirs = new ArrayList<File>();
		//total number of processed directories
		ArrayList<File> vDirs = new ArrayList<File>();
		//total number of unprocessed directories due to missing post.txt
		ArrayList<File> uDirs = new ArrayList<File>();
		
		//used in  putLinksSub() and putLinksSubDirs() methods to monitor
		//directories with or without post.txt and processed links.
		IntObj listDirNum = new IntObj(0);
		IntObj linkPostExistCount = new IntObj(0);
		IntObj noLinkPostCount = new IntObj(0);
		IntObj processedLinks = new IntObj(0);
		IntObj linkCounter = new IntObj(0);
		
		//hosts that are supported but their headers
		//are not defined in post.txt
		HashMap<HashMap<String[], String[]>, String> missingHosts = new HashMap<>();
		if(!includeSubDirs){
			
			//Check if the number of links is equal to the
			//number of directories
			if(!linksEqualToDirs(valLnksStr.length,
								 selectedFiles,includeSubDirs)){
				String msg = "<b>Number of links</b> is not equal to "+
				                 "<b>Number of directories with post.txt!</b><br>"+
								 "<span style=\"color:red;\"><b>Operation Aborted!</b></span>";
				return msg;
			}
			
			for(File selectedFile : selectedFiles){
				if(selectedFile.isDirectory()){
					boolean isPostExist = false;
					listDirNum.setValue(listDirNum.intValue()+1);
					dirs.add(selectedFile);
					
					listSources = selectedFile.listFiles();
					for(File file : listSources){
						if(file.getName().equals("post.txt") &&
						file.isFile()){
							isPostExist = true;
							
							if(linkCounter.intValue() < valLnksStr.length){
								writeLinks(selectedFile,file,sType,valLnksStr,
								           hostHeaders,hostLinks,missingHosts,
										   linkCounter);
								linkCounter.setValue(linkCounter.intValue()+1);
								vDirs.add(selectedFile);
							}
							break;
						}
					}
					if(isPostExist){
						wPost.append(selectedFile.getAbsolutePath() + "<br>");
						linkPostExistCount.setValue(linkPostExistCount.intValue()+1);
					}						
					else{
						uDirs.add(selectedFile);
						noLinkPostCount.setValue(noLinkPostCount.intValue()+1);
					}
				}
			}
		}
		else{
			
			if(!linksEqualToDirs(valLnksStr.length,
								 selectedFiles,includeSubDirs)){
				String msg = "<b>Number of valid links</b> is not equal to "+
				                 "<b>Number of directories with post.txt</b>";
				return msg;
			}
			
			ArrayList<File> subDirList = new ArrayList<File>();
			BoolObj linkPostExist = new BoolObj(false);
			for(File selectedFile : selectedFiles){
				if(selectedFile.isDirectory()){
					listDirNum.setValue(listDirNum.intValue()+1);
					dirs.add(selectedFile);
					
					listSources = selectedFile.listFiles();
					for(int i = 0; i < listSources.length; i++){
						
						putLinksSubDirs(selectedFile,listSources[i],sType,linkPostExist,
										valLnksStr,linkCounter,dirs,vDirs,uDirs,wPost,subDirList,
										listDirNum,linkPostExistCount,noLinkPostCount,processedLinks,
										listSources.length,i,hostHeaders,hostLinks,missingHosts);
					}
					if(!linkPostExist.booleanValue()){
						uDirs.add(selectedFile);
						noLinkPostCount.setValue(noLinkPostCount.intValue()+1);
					}
				}
				subDirList.clear();
				linkPostExist.setValue(false);
			}
		}										
		
		message.append("<b>Directories #:</b> " + listDirNum.intValue() + "<br>" +
					   "<b>Links #:</b> " + valLnksStr.length + "<br>" +
			           "<b># of Directories with post.txt:</b> " + linkPostExistCount.intValue() + "<br>" +
					   "<b># of Directories without post.txt:</b> "+ noLinkPostCount.intValue() +
					   "<br><br>");
		
		if(wPost.length() != 0){
			message.append("<b>Directories with post.txt</b><br>");
			message.append(wPost.toString() + "<br>");
		}
		if(uDirs.size() != 0){
			message.append("<b>Directories without post.txt</b><br>");
			uDirs.forEach((p) -> message.append(p + "<br>"));
			message.append("<br>");
		}
		
		//shrinked directory count due to missing post.txt
		int shrinkDir = listDirNum.intValue() - noLinkPostCount.intValue();
		dirs.removeAll(vDirs);
		//if noLinkPostCount is greater than 0 and shrinkDir is less than the
		//total length of valid links then there are unprocessed links due to
		//missing post.txt
		//e.g.
		//links > directories
		//5 links, 4 directories, 2 directories with missing post.txt
		//4 - 2 = 2 -> 5 > 2
		//In this example, one link can't be processed due to lack of directories.
		//and then we include directories with missing post.txt and the overall unprocessed
		//link is 3: 2 due to missing post.txt and 1 due to lack of valid links.
		//the unprocessed links due to missing post.txt will be reported here. The latter
		//will be processed in another method.
		//e.g
		//directories > links
		//4 links, 6 directories, 3 directories with missing post.txt
		//4 < 6 -> 6 - 3 = 3 -> 4 > 3
		//In this example, directories are greater than links. Since directories with
		//missing post.txt are ignored, links can be greater than directories. In the
		//example above directories are greater than links and then I subtracted directories
		//with missing post.txt and directories became less than links. If this happens, then
		//links might be unprocessed due to missing post.txt and those links will be reported
		//here.
		//e.g
		//directories > links
		//4 links 6 directories, 1 directories with missing post.txt
		// 4 < 6 -> 6 - 1 = 5 -> 4 < 5
		//in the example above, links are still less than directories. All links will be
		//processed, one directory won't be processed and one ignored directory due to
		//missing post.txt. In this case, all links are processed so other methods will
		//handle the unprocessed directory and ignored directory
		//Note: This program skips directories but doesn't skip links. It means that, links
		//will be processed as long as there are directories with post.txt. and even 
		//the locations of directories with missing post.txt are arbitrary.
		if(noLinkPostCount.intValue() != 0 && shrinkDir < valLnksStr.length){
			message.append("<b>Unprocessed valid link/s due to missing post.txt</b><br>");
			//if links are greater than the total directory count
			//then there are valid links that can't be processed plus the unprocessed
			//valid links due to directories with missing post.txt
			//e.g.
			//7 links, 5 directories, 2 directories with missing post.txt
			//shrinkDir = 5 - 2 = 3,total # of directories = 5
			//while(3 < 5){while code...}
			//the total unprocessed links in this example is 4 and this method
			//only handles unprocessed links due to missing post.txt. The other
			//two are handled outside
			if(valLnksStr.length > listDirNum.intValue()){
				int index = shrinkDir;
				while( index < listDirNum.intValue() ){
					message.append(valLnksStr[index] + "<br>");
					index++;
				}
			}
			//Otherwise, some valid links can't be processed due to shrinking of total directory count
			//because of directories with missing post.txt
			else{
				//find the number of unprocessed links due to lack of directory due to
				//missing post.txt
				int diff = valLnksStr.length - shrinkDir;
				//find the index of the first unprocessed link
				int ln = valLnksStr.length - diff;
				
				for(int i = 0; i < diff; i++)
					message.append(valLnksStr[ln+i] + "<br>");
				
			}
			message.append("<br>");
		}
		
		//This statement handles unprocessed links due to the lack of directories with post.txt
		if(valLnksStr.length > listDirNum.intValue()){
			int excessValidLinks = Math.abs(valLnksStr.length - (valLnksStr.length - listDirNum.intValue()));
			message.append("<b>Unprocessed valid link/s due to the lack of directories.</b><br>");
			for(int i = excessValidLinks; i < valLnksStr.length; i++)
				message.append(valLnksStr[i] + "<br>");
			message.append("<br>");
		}
		//this statement handles Unprocessed directories due to the lack of valid link/s.
		else{
			dirs.removeAll(uDirs);
			if(!dirs.isEmpty()){
				message.append("<b>Unprocessed directories due to the lack of valid link/s.</b><br>");
				dirs.forEach((p) -> message.append(p + "<br>"));
				message.append("<br>");
			}
		}
		
		if(!missingHosts.isEmpty())
			LinkOpApprovedFunctions.reportLinkOpUnsupportedHostsLinks(message, missingHosts);
		
		return message.toString();
	}
	
	private boolean linksEqualToDirs(int linksLn, File[] selectedFiles, boolean includeSubDirs){
		IntObj dirWithPost = new IntObj(0);
		
		if(!includeSubDirs){
			for(File selectedFile : selectedFiles)
				if(selectedFile.isDirectory())
					for(File file : selectedFile.listFiles())
						if(file.getName().equals("post.txt") &&
						   file.isFile())
						   dirWithPost.setValue(dirWithPost.intValue()+1);
		}
		else{
			for(File selectedFile : selectedFiles)
				if(selectedFile.isDirectory()){
					listSources = selectedFile.listFiles();
					linksEqualToDirsRecursive(listSources,dirWithPost);
				}
		}
		
		return linksLn == dirWithPost.intValue();
	}
	
	//This method is not tested yet
	private void linksEqualToDirsRecursive(File[] files, IntObj dirWithPost){
		
		for(File file : files){
			if(file.getName().equals("post.txt") &&
			   file.isFile()){
			   dirWithPost.setValue(dirWithPost.intValue()+1);
			   continue;
			}
			
			if(file.isDirectory())
				linksEqualToDirsRecursive(file.listFiles(),dirWithPost);
		}
	}
}
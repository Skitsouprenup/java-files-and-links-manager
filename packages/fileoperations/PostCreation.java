package fileoperations;

import static main.UtilityClasses.*;

import java.io.File;
import java.io.IOException;

class PostCreation extends FileGeneration{
	
	private File[] listSources;
	
	String createPost(File[] selectedFiles,int index,boolean includeSubDirs,boolean includeCol) throws IOException{
		StringBuilder msg1 = new StringBuilder();
		StringBuilder msg2 = new StringBuilder();
		StringBuilder fMessage = new StringBuilder();
		//used in createPost() and createPostSubDirs() methods.
		//To keep track of number of directories, existing post.txt in a directory
		//and newly created post.txt
		IntObj listDirNum = new IntObj(0);
		IntObj postExistCount = new IntObj(0);
		IntObj noPostCount = new IntObj(0);
		
		for(File selectedFile : selectedFiles){
			
			if(selectedFile.isDirectory()){
				listDirNum.setValue(listDirNum.intValue()+1);
				listSources = selectedFile.listFiles();
				if(!includeSubDirs){
					boolean isPostExist = false;
					//Check if post.txt already exists in the selected directory
					for(File file : listSources){
						if(file.getName().equals("post.txt") &&
						   file.isFile()){
							postExistCount.setValue(postExistCount.intValue()+1);
							isPostExist = true;
							msg1.append("<b>post.txt</b> already exists in <u>"+selectedFile.getAbsolutePath()+
										"</u><br>");
							break;
						}		
					}
					//if no existing post.txt then create one
					if(!isPostExist){
						noPostCount.setValue(noPostCount.intValue()+1);
						writePost(selectedFile,includeCol,index);
						msg2.append("<b>post.txt</b> has been created in <u>"+selectedFile.getAbsolutePath()+
									"</u><br>");
					}
				} 
				else {
					BoolObj isPostExist = new BoolObj(false);
					for(File file : listSources){
						//listFiles() gives all files in a directory in alphabetical order. So, the loop
						//is gonna be in alphabetical order. Don't be confused with the arrangement of 
						//files here and the arrangement of files in explorer.exe if you're using windows
						//don't use the arrangement of files in explorer.exe as a reference when debugging
						//file loops.
						//System.out.println("e: "+isPostExist.booleanValue()+", "+file.getName());
						createPostSubDirs(file,isPostExist,index,includeCol,msg1,msg2,listDirNum,
										  postExistCount,noPostCount);
					}
					if(!isPostExist.booleanValue()){
						writePost(selectedFile,includeCol,index);
						noPostCount.setValue(noPostCount.intValue()+1);
						msg2.append("<b>post.txt</b> has been created in "+selectedFile.getAbsolutePath()+"<br>");
					}
				}
			}
		}
		
		fMessage.append("<b>Directories #:</b> " + listDirNum.intValue() + 
			            "<br><b># of subfolders that have existing post.txt:</b> " + postExistCount.intValue() +
						"<br><b># of newly created post.txt: </b>"+ noPostCount.intValue() + "<br><br>");
		fMessage.append(msg1.toString() + msg2.toString());
		
		return fMessage.toString();
	}
}
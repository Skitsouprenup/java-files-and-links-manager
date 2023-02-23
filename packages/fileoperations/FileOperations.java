package fileoperations;

import static main.UtilityClasses.*;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

//singleton class
public class FileOperations{
	
	private static Desktop desktop;
	private static boolean isTrashSupported;
	
	private static boolean isInstantiated;
	private static FileOperations instance;
	
	private final static String[] colKeywords;
	
	private final PostCreation postCreation;
	private final DummyCreation dummyCreation;
	private final DummyDeletion dummyDeletion;
	private final LinkInsertion linkInsertion;
	private final ZipOperations zipOperations;
	
	static {
		colKeywords = new String[]{"Collection Pack", "Collection"};
		
		if(Desktop.isDesktopSupported()){
		desktop = Desktop.getDesktop();
			if(desktop.isSupported(Desktop.Action.MOVE_TO_TRASH))
				isTrashSupported = true;
		}
		else {
			System.out.println("Desktop class is not supported in this platform");
			System.exit(-1);
		}
	}
	
	private FileOperations(){
		postCreation = new PostCreation();
		dummyCreation = new DummyCreation();
		dummyDeletion = new DummyDeletion();
		linkInsertion = new LinkInsertion();
		zipOperations = new ZipOperations();
	}
	
	public static FileOperations instantiate(){
		if(!isInstantiated){
			instance = new FileOperations();
			isInstantiated = true;
		}
		
		return instance;
	}
	
	public static boolean getMoveToTrashSupport(){ return isTrashSupported; }
	public static Desktop getDesktop(){ return desktop; }
	private static String[] getColKeywordsList(){ return colKeywords; }
	
	static String getKeywordsRegPatt(){
		StringBuilder keywords = new StringBuilder(".*");
		for(int i = 0; i < getColKeywordsList().length;i++){
			if(i != getColKeywordsList().length-1)
				keywords.append(getColKeywordsList()[i]+".*|.*");
			else keywords.append(getColKeywordsList()[i]+".*");
		}
		return keywords.toString();
	}
	
	public String getColKeywords(){
		StringBuilder keywords = new StringBuilder();
	    keywords.append("If one of these keywords is part of a directory's name\n"+
						"then it's considered as directory with multiple books.\n"+
						"Thus, A different template will be used if the program creates a\n"+
						"post.txt in this directory.\n"+
						"This option only applies to Books Post Types.\n\n"+
						"Keywords\n");
		for(String str : colKeywords)
			keywords.append("\""+str+"\"\n");
		return keywords.toString();
		
	}
	
	public String createPost(File[] selectedFiles,int index,boolean includeSubDirs,boolean includeCol)throws IOException{
		return postCreation.createPost(selectedFiles,index,includeSubDirs,includeCol);
	}
	
	public String createDummy(File[] selectedFiles,String thresTxt,boolean includeSubDirs)throws IOException{
		return dummyCreation.createDummy(selectedFiles,thresTxt,includeSubDirs);
	}
	
	public String deleteDummy(File[] selectedFiles, boolean includeSubDirs)throws IOException{
		return dummyDeletion.deleteDummy(selectedFiles,includeSubDirs);
	}
	
	public String putLinksDir(File selectedFile,SelectionType sType,StringBuilder links,
	                          String[] hostHeaders, String[] hostLinks)
		   throws IOException,UnsupportedEncodingException{
		return linkInsertion.putLinksDir(selectedFile,sType,links,hostHeaders,hostLinks);
	}
	
	public String putLinksSub(File[] selectedFiles,SelectionType sType,
							  boolean includeSubDirs,StringBuilder links,
							  String[] hostHeaders, String[] hostLinks)throws IOException,UnsupportedEncodingException{
		return linkInsertion.putLinksSub(selectedFiles,sType,includeSubDirs,links,
										 hostHeaders,hostLinks);
	}
	
	public String compressFiles(File[] selectedFiles,boolean incSubDirs,
							  boolean op1Selected,boolean op2Selected,
							  String compressType,boolean excludeCol,
							  boolean excludePostTxt) throws IOException{
		return zipOperations.compressFiles(selectedFiles,incSubDirs,op1Selected,op2Selected,compressType,
		                                   excludeCol,excludePostTxt);
	}
	
	public String deCompressFiles(File[] selectedFiles,boolean incSubDirs,UnzipPackType uType)throws IOException{
		return zipOperations.deCompressFiles(selectedFiles,incSubDirs,uType);
	}
}
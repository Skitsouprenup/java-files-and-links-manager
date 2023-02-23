package fileoperations;

import gui.dialogs.subpanels.prefs_subpanel.PrefsConfig;
import main.PreferencesConfig;

class PostTypes{
	
	private static String post_ebook = 
	"[B][/B]\r\n\r\n"+
	"[IMG][/IMG]\r\n\r\n"+
	"[B]By: [/B]\r\n"+ 
    "[B]Genre: [/B]\r\n"+ 
    "[B]Date Published: [/B]\r\n"+ 
    "[B]Pages: [/B]\r\n"+ 
    "[B]Publisher: [/B]\r\n"+ 
    "[B]Format: [/B]\r\n"+ 
    "[B]Language: [/B]English\r\n\r\n"+
    "[QUOTE][/QUOTE]\r\n\r\n";
	
	private static String post_collection =
	"[B][/B]\r\n\r\n"+
    "[B]By: [/B]Multiple Authors\r\n"+
    "[B]Genre: [/B]\r\n"+ 
    "[B]Language: [/B]English\r\n\r\n"+
    "[B]Covers[/B]\r\n"+
    "[IMG][/IMG]\r\n"+
    "[IMG][/IMG]\r\n"+
    "[IMG][/IMG]\r\n\r\n"+
    "[quote]\r\n"+
    "Contents\r\n\r\n"+
    "[/quote]\r\n\r\n";
	
	private static String post_magazine = 
	"[B][/B]\r\n"+
	"[IMG][/IMG]\r\n\r\n"+
	"[B]Category: [/B]\r\n"+
	"[B]Date Published: [/B]\r\n"+
	"[B]Pages: [/B]\r\n"+
	"[B]Language: [/B]English\r\n"+
	"[B]File Format: [/B]PDF\r\n\r\n";
	
	private static String post_tutorial =
	"[B][/B]\r\n\r\n"+
	"[IMG][/IMG]\r\n\r\n"+
	"[B]By: [/B]\r\n"+
	"[B]Genre: [/B]\r\n"+
	"[B]Publisher: [/B]\r\n"+
	"[B]Language: [/B]English\r\n"+
	"[B]Format: [/B]\r\n"+
	"[B]Duration: [/B]\r\n"+
	"[B]Size: [/B]\r\n\r\n"+
	"[QUOTE][/QUOTE]\r\n\r\n"+
	"[QUOTE]\r\n"+
	"contents\r\n\r\n"+
	"[/QUOTE]\r\n\r\n";
	
	static String generatePostType(PostTypesConstants type){
		PrefsConfig prefsConfig = PreferencesConfig.getPrefsConfig();
		if(prefsConfig == null){
			System.err.println("Fatal error: prefsConfig is null!");
			System.err.println("Can't generate post.txt!");
			System.exit(1);
		}
		
		StringBuilder postTxt = new StringBuilder();
		switch(type){
			
			case POST_EBOOK:
			postTxt.append(post_ebook);
			break;
			
			case POST_COLLECTION:
			postTxt.append(post_collection);
			break;
			
			case POST_MAGAZINE:
			postTxt.append(post_magazine);
			break;
			
			case POST_TUTORIAL:
			postTxt.append(post_tutorial);
			break;
		}
		postTxt.append("Download:\r\n\r\n");
		postTxt.append("[CODE]\r\n");
		for(String suppHost : prefsConfig.getCurrentSupportedHosts()){
			postTxt.append(suppHost + "\r\n\r\n");
		}
		postTxt.append("[/CODE]");
		
		return postTxt.toString();
	}
}
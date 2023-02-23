package fileoperations;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class LinkOperations {
	
	private LinkOperations(){}
	
	public static boolean validLink(String text,String host){
		boolean isMatch = false;
		Pattern pattern = Pattern.compile("((https://|http://)((www.)?)("+host+"))");
		Matcher matcher = pattern.matcher(text);
		isMatch = matcher.lookingAt();
		return isMatch;
	}
	
	public static boolean findSpecStr(String regex,String content,boolean isSensitive){
		
		Pattern pattern = null;
		if(isSensitive)
			pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		else pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		return matcher.matches();
	}
	
	public static boolean findSubSequence(String regex,String content,boolean isSensitive){
		
	    Pattern pattern = null;
		if(isSensitive)
			pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		else pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		return matcher.find();
	}
	
	public static int[] getFileExtensionRange(String filename, String regex, boolean isSensitive){
		int startIndex = 0;
		int endIndex = 0;
		boolean endHit = false;
		
		Pattern pattern = null;
		if(isSensitive){
			pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		}
		else pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(filename);
		while(matcher.find())
			if(matcher.hitEnd()){
				endHit = true;
				startIndex = matcher.start();
				endIndex = matcher.end();
			}
		
			
		if(endHit)
			return new int[]{startIndex, endIndex};
		else return null;
		
	}
	
}
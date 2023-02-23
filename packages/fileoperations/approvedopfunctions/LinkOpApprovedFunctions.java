package fileoperations.approvedopfunctions;

import static main.UtilityClasses.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fileoperations.LinkOperations;

//Operations in this class occur if all links are validated and
//# of directories are equal to # of links.
public class LinkOpApprovedFunctions{
	
	private LinkOpApprovedFunctions(){}
	
	public static boolean arrangeSupportedHosts(ArrayList<String> hostsArr,
	                      String[] hostHeaders, String[] hostLinks){
		
		if(!hostsArr.isEmpty()){
			boolean oldHeader = false;
			int oldHeaderIndex = 0;
			String[] suppHosts_with_rg_splitted = null;
			
			for(int i = 0; i < hostsArr.size(); i++){
				hostHeaders[i] = new String("");
				hostLinks[i] = new String("");
									
				String[] split = hostsArr.get(i).split("[|]");
				for(int j = 0; j < split.length; j++){
					if((LinkOperations.findSpecStr("rapidgator[.]net",split[j],false)
						|| LinkOperations.findSpecStr("rg[.]to",split[j],false)) &&
					   oldHeader == false){
					   oldHeader = true;
					   oldHeaderIndex = i;
					   suppHosts_with_rg_splitted = split;
					}
										
					if(j == split.length-1){
						hostHeaders[i] += "^"+split[j]+"$";
						hostLinks[i] += split[j];
					}
					else{
						hostHeaders[i] += "^"+split[j]+"$|";
						hostLinks[i] += split[j]+"|";
					}
										
				}
			}
			
			//Backward compatibility for old post.txt
			//that have rapidgator download header
			//without top-level domain
			//
			//check first if the old headers are already
			//part of supported host and its alternative
			//hostnames
			if(oldHeader){
				boolean oldRgHeaderCapsExist = false;
				boolean oldRgHeaderNoCapsExist = false;
				for(String hosts : suppHosts_with_rg_splitted){
					if(LinkOperations.findSpecStr("|^Rapidgator$",hosts,false))
						oldRgHeaderCapsExist = true;
					else if(LinkOperations.findSpecStr("|^rapidgator$",hosts,false))
						oldRgHeaderNoCapsExist = true;
				}
				
				//add them to supported host if old headers
				//don't exist in the supported host yet.
				if(!oldRgHeaderCapsExist)
					hostHeaders[oldHeaderIndex] += "|^Rapidgator$";
				if(!oldRgHeaderNoCapsExist)
					hostHeaders[oldHeaderIndex] += "|^rapidgator$";
			}
			
			return true;			
		}else return false;
		
	}
	
	public static void getMissingHostsInTxt(HashMap<HashMap<String[], String[]>, String> missingHosts,
	                                        String[] hostHeaders, String[] links, SelectionType sType,
											ArrayList<String> availableHosts, String txtSource,
											int linkCounter){
		ArrayList<String> notAvailableHosts = new ArrayList<>();
		for(int i = 0; i < hostHeaders.length; i++){
			boolean isAvailable = false;
			for(String str : availableHosts)
				if(str.equals(hostHeaders[i])){
					isAvailable = true;
					break;
				}
				
			if(isAvailable)
				continue;
			else
				notAvailableHosts.add(hostHeaders[i]);
		}
		//Nothing is missing. Thus, we can't get
		//any missing hosts in text file(post.txt)
		if(notAvailableHosts.size() == 0)
			return;
		
		String[] combineHosts = new String[notAvailableHosts.size()];
		String[] combineLinks = new String[notAvailableHosts.size()];
		boolean gotLink = false;
		for(int i = 0; i < notAvailableHosts.size(); i++){
			combineHosts[i] = notAvailableHosts.get(i);
			combineLinks[i] = new String("");
			if(sType == SelectionType.SINGLE){
				for(int j = 0; j < links.length; j++){
					if(LinkOperations.validLink(links[j],combineHosts[i])){
						if(j == links.length-1)
							combineLinks[i] += links[j];
						else combineLinks[i] += links[j]+"|";
					}
				}
			}
			else if(sType == SelectionType.MULTIPLE && !gotLink){
				String currLink = links[linkCounter];
				if(LinkOperations.validLink(currLink,combineHosts[i])){
					combineLinks[i] = currLink;
					gotLink = true;
				}
			}
			
		}
		HashMap<String[], String[]> hostlinks = new HashMap<>();
		hostlinks.put(combineHosts, combineLinks);
		missingHosts.put(hostlinks, txtSource);
	}
	
	public static void reportLinkOpUnsupportedHostsLinks(StringBuilder message,
	                   HashMap<HashMap<String[], String[]>, String> missingHosts){
						   
		message.append(
			"<span><b>Some supported hosts and their respective links<br> have been skipped"+
			" because their sections didn't exist.</b></span><br><br>");
			
		//Map.Entry and Hashmap must be raw type
		for(Map.Entry me : missingHosts.entrySet()){
			HashMap missingHostLinks = null;
			Object key = me.getKey();
			if(key instanceof HashMap)
				missingHostLinks = (HashMap)key;
			else 
				throw new NullPointerException("\"key\" variable in LinkInsertion.java must be HashMap!");
					
			String filePath = (String)me.getValue();
			
			message.append("<b>Source File</b><br>"+filePath+"<br>");
			
			//These two must have the same length
			Object[] missHosts = missingHostLinks.keySet().toArray();
			Object[] missLinks = missingHostLinks.values().toArray();
					
			//These two must have the same length
			String[] combineHosts = null;
			String[] combineLinks = null;
					
			for(int i = 0; i < missHosts.length; i++){
				String[] missHostsIn = (String[])missHosts[i];
				String[] missLinksIn = (String[])missLinks[i];
						
				message.append("<b>Skipped Host/s</b><br>");
				for(int j = 0; j < missHostsIn.length; j++){
					combineHosts = missHostsIn[j].split("[|]");
					for(int k = 0; k < combineHosts.length; k++)
						if(k == combineHosts.length-1)
							message.append(combineHosts[k]);
						else
							message.append(combineHosts[k] + ", ");
					message.append("<br>");
							
					String tempLinksArr = missLinksIn[j];
					combineLinks = tempLinksArr.split("[|]");
					message.append("<b>Skipped Link/s</b><br>");
					if(!tempLinksArr.isEmpty()){
						for(String s : combineLinks)
							message.append(s + "<br>");
					}else message.append("None<br>");
					message.append("<br>");
				}
							
			}
		}
	}
}
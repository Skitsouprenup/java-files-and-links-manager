package gui;

import java.io.File;
import java.util.Comparator;

public class SortFiles{
	
	public class SortIgnoreCase implements Comparator<File>{
		
		@Override
		public int compare(File o1,File o2){
			String s1 = o1.getName();
			String s2 = o2.getName();
			
			return s1.compareToIgnoreCase(s2);
		}
	}
	
	public Comparator<File> createInstance(String sortType){
		Comparator<File> type = null;
		
		switch(sortType){
			
			case "IgnoreCase":
			type = new SortIgnoreCase();
			break;
		}
		return type;
	}
}

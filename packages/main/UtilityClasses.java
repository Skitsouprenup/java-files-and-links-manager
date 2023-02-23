package main;

public abstract class UtilityClasses{
	//Note: java class references that follow pass-by-value
	//String, enum, wrapper classes
	
	//default constructor
	private UtilityClasses(){}
	
	//Because Boolean wrapper class still follows pass-by-value method. I created this class to act
	//as a boolean object.
	public static class BoolObj{
		private boolean bool;
		
		public BoolObj(boolean bool){
			this.bool = bool;
		}
		
		public boolean booleanValue(){ return bool; }
		public void setValue(boolean bool){ this.bool = bool; }
	}
	//Because Long wrapper class still follows pass-by-value method. I created this class to act
	//as a long object.
	public static class LongObj{
		private long lng;
		
		public LongObj(long lng){
			this.lng = lng;
		}
		
		public long longValue(){ return lng; }
		public void setValue(long lng){ this.lng = lng; }
	}
	//Because Integer wrapper class still follows pass-by-value method. I created this class to act
	//as an int object.
	public static class IntObj{
		private int integer;
		
		public IntObj(int integer){
			this.integer = integer;
		}
		
		public int intValue(){ return integer; }
		public void setValue(int integer){ this.integer = integer; }
	}
	
	//Because enum references follow pass-by-value method. I created this class to act
	//as a true enum object
	public static class JobRef{
		private Jobs job;
		
		public void setValue(Jobs job){
			this.job = job;
		}
		
		public Jobs getValue(){ return job; }
	}
	//
	public static class SelectionLinkRef{
		private SelectionType selectionType;
		
		public void setValue(SelectionType selectionType){
			this.selectionType = selectionType;
		}
		
		public SelectionType getValue(){ return selectionType; }
	}
	//
	public static class ZipPackRef{
		private ZipPackType zipPackType;
		
		public void setValue(ZipPackType zipPackType){
			this.zipPackType = zipPackType;
		}
		
		public ZipPackType getValue(){ return zipPackType; }
	}
	//
	public static class UnzipPackRef{
		private UnzipPackType unzipPackType;
		
		public void setValue(UnzipPackType unzipPackType){
			this.unzipPackType = unzipPackType;
		}
		
		public UnzipPackType getValue(){ return unzipPackType; }
	}
	
	public enum SelectionType{
		SINGLE, MULTIPLE
	}
	
	public enum ZipPackType{
		PER_DIR, SINGLE_PACKAGE
	}
	
	public enum UnzipPackType{
		DIRECTORY, ZIP_FILE
	}
	
	public enum StopWatchState{
		ONGOING,STOPPED,FINISHED
	}
	
	public enum Jobs {
		CREATE_POST_TXT, CREATE_DUMMY_FILE, DELETE_DUMMY_FILE, PUT_LINKS,
		COMPRESS_FILES, DECOMPRESS_FILES
	}
}
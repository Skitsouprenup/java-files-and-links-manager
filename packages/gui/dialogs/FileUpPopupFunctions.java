package gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import gui.FileChooserProperties;

class FileUpPopupFunctions extends FileUpRandomizeFunctions{
	
	protected FileUpPopupFunctions(){}
	
	protected void removeItems(JDialog parentDialog ,JList<Object> listComponent,
							   JPanel listPanel, LinkedHashSet<String> listSet){
		if(listComponent.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(parentDialog,"No selected item/s!",
				  "Empty Selection",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		List<Object> list = listComponent.getSelectedValuesList();
		listSet.removeAll(list);
		listComponent.setListData(listSet.toArray());
		if(listSet.isEmpty())
			if(listPanel.getComponent(0) instanceof JList){
				listPanel.removeAll();
				listPanel.add(new JLabel("None"), new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
												  GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
				listPanel.revalidate();
			}
		parentDialog.pack();
	}
	
	protected void removeAllItems(JDialog parentDialog, JList<Object> listComponent,
								  JPanel listPanel, LinkedHashSet<String> listSet){
		
		listSet.clear();
		listComponent.setListData(listSet.toArray());
		if(listPanel.getComponent(0) instanceof JList){
				listPanel.removeAll();
				listPanel.add(new JLabel("None"), new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
												  GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
				listPanel.revalidate();
		}
		parentDialog.pack();
	}
	
	protected void addItemsToJList(ArrayList<File> selectedFileList, final JDialog parentDialog,
								   final JList<Object> listComponent, final JPanel listPanel,
								   final LinkedHashSet<String> listSet) throws IOException{
			
		if(listComponent.getName().equals(ListType.SOURCE.toString()) ||
			listComponent.getName().equals(ListType.RECORD.toString())){
			for(File f : selectedFileList){
				String path = f.getCanonicalPath();
				listSet.add(path);
			}
			listComponent.setListData(listSet.toArray());
				
			if(listPanel.getComponent(0) instanceof JLabel){
				listPanel.removeAll();
				listPanel.add(listComponent);
				listPanel.revalidate();
			}
		}
		else if(listComponent.getName().equals(ListType.DESTINATION.toString()) ||
				listComponent.getName().equals(ListType.SYMLINK_DESTINATION.toString())){
			listSet.clear();
			
			if(!selectedFileList.isEmpty()){
				String path = selectedFileList.get(0).getCanonicalPath();
				listSet.add(path);
				listComponent.setListData(listSet.toArray());
				
				if(listPanel.getComponent(0) instanceof JLabel){
					listPanel.removeAll();
					listPanel.add(listComponent);
					listPanel.revalidate();
				}
			}
		}
		parentDialog.pack();		
	}
	
	protected int selectFilesThenVerifyPaths(
					 JFileChooser fc, JDialog parentDialog, ArrayList<File> selectedFileList,
					 LinkedHashSet<String> sourceSet, LinkedHashSet<String> destSet,
					 LinkedHashSet<String> recSet, LinkedHashSet<String> destLinkSet,
					 boolean selectType, String listComponentName) throws IOException{
						 
		FileChooserProperties.setToDefaults(fc);
		fc.setMultiSelectionEnabled(selectType);
		
		if(listComponentName.equals(ListType.RECORD.toString())){
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileFilter(new FileNameExtensionFilter("Text Files","txt"));
		}
		
		int val = fc.showOpenDialog(parentDialog);
		
		File[] files = null;
		if(val == JFileChooser.APPROVE_OPTION){
			
			if(listComponentName.equals(ListType.SOURCE.toString())){
			   files = FileChooserProperties.fChooserSelectBugFix(fc);
			   
			   for(int i = 0; i < files.length; i++){
				   String shortPath = files[i].getParentFile().getName()+
											 File.separator+files[i].getName();
				   
				   String message = "<html>A selected source directory <u>"+shortPath+
				   "</u><br>is already set as destination directory!</html>";
				   int checkResult = checkCrossDuplicatePath(parentDialog, destSet, files[i], message);
				   if(checkResult == JFileChooser.CANCEL_OPTION)
					  return checkResult;
				   
				   message = "<html>A selected source directory <u>"+shortPath+
				   "</u><br>is already set as record reference directory!</html>";
				   checkResult = checkCrossDuplicatePath(parentDialog, recSet, files[i], message);
				   if(checkResult == JFileChooser.CANCEL_OPTION)
					  return checkResult;
				  
				   message = "<html>A selected source directory <u>"+shortPath+
				   "</u><br>is already set as symlink destination directory!</html>";
				   checkResult = checkCrossDuplicatePath(parentDialog, destLinkSet, files[i], message);
				   if(checkResult == JFileChooser.CANCEL_OPTION)
					  return checkResult;
				   
			   }
			}
			else if(listComponentName.equals(ListType.DESTINATION.toString())){
				//This program doesn't support multiple destination
				//directories
				files = new File[]{fc.getSelectedFile()};
				
				for(int i = 0; i < files.length; i++){
					String shortPath = files[i].getParentFile().getName()+
											  File.separator+files[i].getName();
					
					String message = "<html>A selected destination directory <u>"+shortPath+
					"</u><br>is already set as source directory!</html>";
				    int checkResult = checkCrossDuplicatePath(parentDialog, sourceSet, files[i], message);
				    if(checkResult == JFileChooser.CANCEL_OPTION)
					   return checkResult;
				   
				    message = "<html>A selected destination directory <u>"+shortPath+
					"</u><br>is already set as record reference directory!</html>";
				    checkResult = checkCrossDuplicatePath(parentDialog, recSet, files[i], message);
				    if(checkResult == JFileChooser.CANCEL_OPTION)
					   return checkResult;
					
					message = "<html>A selected destination directory <u>"+shortPath+
					"</u><br>is already set as symlink destination directory!</html>";
				    checkResult = checkCrossDuplicatePath(parentDialog, destLinkSet, files[i], message);
				    if(checkResult == JFileChooser.CANCEL_OPTION)
					   return checkResult;
				   
				}
			}
			else if(listComponentName.equals(ListType.RECORD.toString())){
				files = FileChooserProperties.fChooserSelectBugFix(fc);
			   
			    for(int i = 0; i < files.length; i++){
				   String shortPath = files[i].getParentFile().getName()+
											 File.separator+files[i].getName();
				   
				   String message = "<html>A selected record reference directory <u>"+shortPath+
				   "</u><br>is already set as source directory!</html>";
				   int checkResult = checkCrossDuplicatePath(parentDialog, sourceSet, files[i], message);
				   if(checkResult == JFileChooser.CANCEL_OPTION)
					  return checkResult;
				   
				   message = "<html>A selected record reference directory <u>"+shortPath+
				   "</u><br>is already set as destination directory!</html>";
				   checkResult = checkCrossDuplicatePath(parentDialog, destSet, files[i], message);
				   if(checkResult == JFileChooser.CANCEL_OPTION)
					  return checkResult;
				   
				   message = "<html>A selected record reference directory <u>"+shortPath+
				   "</u><br>is already set as symlink destination directory!</html>";
				   checkResult = checkCrossDuplicatePath(parentDialog, destLinkSet, files[i], message);
				   if(checkResult == JFileChooser.CANCEL_OPTION)
					  return checkResult;
				   
			   }
			}
			else if(listComponentName.equals(ListType.SYMLINK_DESTINATION.toString())){
				//This program doesn't support multiple symlink destination
				//directories
				files = new File[]{fc.getSelectedFile()};
				
				for(int i = 0; i < files.length; i++){
					String shortPath = files[i].getParentFile().getName()+
											  File.separator+files[i].getName();
					
					String message = "<html>A selected symlink destination directory <u>"+shortPath+
					"</u><br>is already set as source directory!</html>";
					int checkResult = checkCrossDuplicatePath(parentDialog, sourceSet, files[i], message);
					if(checkResult == JFileChooser.CANCEL_OPTION)
						return checkResult;
					
					message = "<html>A selected symlink destination directory <u>"+shortPath+
					"</u><br>is already set as record reference directory!</html>";
					checkResult = checkCrossDuplicatePath(parentDialog, recSet, files[i], message);
					if(checkResult == JFileChooser.CANCEL_OPTION)
						return checkResult;
					
					message = "<html>A selected symlink destination directory <u>"+shortPath+
					"</u><br>is already set as record reference directory!</html>";
					checkResult = checkCrossDuplicatePath(parentDialog, destSet,files[i], message);
					if(checkResult == JFileChooser.CANCEL_OPTION)
						return checkResult;
				}
			}
			
			if(files != null){
				selectedFileList.addAll(Arrays.asList(files));
			}
		}
		return val;
	}
	
	private int checkCrossDuplicatePath(JDialog parentDialog, LinkedHashSet<String> set, 
										File selectedFile, String message) throws IOException{
		Iterator<String> it = set.iterator();
		while(it.hasNext()){
			String targetPath = it.next();
			if(selectedFile.getCanonicalPath().equals(targetPath)){
				JOptionPane.showMessageDialog(
				parentDialog, message, "Cross Duplicate Path",JOptionPane.ERROR_MESSAGE);
				return JFileChooser.CANCEL_OPTION; 
			}
		}
		return JFileChooser.APPROVE_OPTION;
	}
	
	protected void randomize(JDialog parentDialog, int fileCount, LinkedHashSet<String> sourceSet,
							 LinkedHashSet<String> destSet, LinkedHashSet<String> recSet,
							 LinkedHashSet<String> destLinkSet) throws IOException{
		//container for new symlinks that are going to be created
		ArrayList<Path> linkDir = new ArrayList<>();
		getListPathsNonDepth(destLinkSet, linkDir);
		if(linkDir.isEmpty()){
			int result = JOptionPane.showConfirmDialog(parentDialog,
						 "Symlinks destination is empty. No new Symlinks are gonna be created.\n"
						 +"Do you still wanna proceed?", "Empty Symlink Destination",
						 JOptionPane.OK_CANCEL_OPTION);
			
			if(result == JOptionPane.CANCEL_OPTION)
				return;
		}
		
		//get destination directory
		ArrayList<Path> destDir = new ArrayList<>();
		getListPathsNonDepth(destSet, destDir);
		if(destDir.isEmpty()){
			JOptionPane.showMessageDialog(
			parentDialog,
			"<html>No destination directory!</html>",
			"Empty Source",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//get directories in the source 
		ArrayList<Path> dPaths = new ArrayList<>();
		for(String sourceDir : sourceSet){
			Path path = Paths.get(sourceDir);
			if(Files.isDirectory(path) && Files.exists(path)){
				FileVisitorForRandomize fvr = new FileVisitorForRandomize(path);
				Files.walkFileTree(path, java.util.EnumSet.noneOf(java.nio.file.FileVisitOption.class),
								   1, fvr);
				dPaths.addAll(fvr.getDirectories());
			}
		}
		if(dPaths.isEmpty()){
			JOptionPane.showMessageDialog(
			parentDialog,
			"<html>No source directories!</html>",
			"Empty Source",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//get directories for records
		ArrayList<Path> symlinks = new ArrayList<>();
		for(String recDir : recSet){
			Path path = Paths.get(recDir);
			if(Files.isDirectory(path) && Files.exists(path)){
				FileVisitorForRandomize fvr = new FileVisitorForRandomize(path);
				Files.walkFileTree(path, fvr);
				symlinks.addAll(fvr.getSymbolicLinks());
			}
		}
		
		//setup random directories
		ArrayList<Path> randomFiles = new ArrayList<>();
		fileCount = randomizeDirectories(fileCount, dPaths, randomFiles, symlinks);
		if(randomFiles.isEmpty()){
			JOptionPane.showMessageDialog(
			parentDialog,
			"<html>No directories can be randomed! Source directory might be empty or\n"+
			"all directories in the source have existing symlinks."+
			"</html>",
			"Empty Source",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		copyRandomizedDirectories(fileCount, parentDialog, randomFiles, 
								  destDir, linkDir);
	}
	
	protected void checkCrossDupsInLoadedPaths(ArrayList<String> sourceTemp, ArrayList<String> destTemp,
											   ArrayList<String> recTemp, ArrayList<String> destLinkTemp){
		int sourceIndex = 0;
		int destIndex = 0;
		int recIndex = 0;
		while(true){
			
			String subject = null;
			String option = "";
			if(!sourceTemp.isEmpty() && sourceIndex < sourceTemp.size()){
				subject = sourceTemp.get(sourceIndex);
				option = "source";
			}
			else if(!destTemp.isEmpty() && destIndex < destTemp.size()){
				subject = destTemp.get(destIndex);
				option = "dest";
			}
			else if(!recTemp.isEmpty() && recIndex < recTemp.size()){
				subject = recTemp.get(recIndex);
				option = "rec";
			}
			else break;
			
			boolean incSourceIndex = true;
			for(int i = 0; i < destTemp.size(); i++)
				if(option.equals("source"))
					if(destTemp.get(i).equals(subject)){
						destTemp.remove(i);
						incSourceIndex = false;
					}
					
			boolean incDestIndex = true;
			for(int i = 0; i < recTemp.size(); i++){
				if(option.equals("source")){
					if(recTemp.get(i).equals(subject)){
						recTemp.remove(i);
						incSourceIndex = false;
					}
				}
				else if(option.equals("dest")){
					if(recTemp.get(i).equals(subject)){
						recTemp.remove(i);
						incDestIndex = false;
					}
				}
			}
					
			boolean incRecIndex = true;
			for(int i = 0; i < destLinkTemp.size(); i++){
				if(option.equals("source")){
					if(destLinkTemp.get(i).equals(subject)){
						destLinkTemp.remove(i);
						incSourceIndex = false;
					}
				}
				else if(option.equals("dest")){
					if(destLinkTemp.get(i).equals(subject)){
						destLinkTemp.remove(i);
						incDestIndex = false;
					}
				}
				else if(option.equals("rec")){
					if(destLinkTemp.get(i).equals(subject)){
						destLinkTemp.remove(i);
						incRecIndex = false;
					}
				}
			}
					
			if(!incSourceIndex && option.equals("source"))
				sourceTemp.remove(sourceIndex);
			else if(incSourceIndex && option.equals("source"))
				sourceIndex++;
			
			if(!incDestIndex && option.equals("dest"))
				destTemp.remove(destIndex);
			else if(incDestIndex && option.equals("dest"))
				destIndex++;
			
			if(!incRecIndex && option.equals("rec"))
				recTemp.remove(recIndex);
			else if(incRecIndex && option.equals("rec"))
				recIndex++;
		}
	}
	
	protected enum ListType{
		SOURCE, DESTINATION, RECORD, SYMLINK_DESTINATION
	}
}
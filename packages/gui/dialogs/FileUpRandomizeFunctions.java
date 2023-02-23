package gui.dialogs;

import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.FileInputStream;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.FileSystemException;
import java.nio.file.LinkOption;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.LinkedHashSet;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

class FileUpRandomizeFunctions{
	
	protected FileUpRandomizeFunctions(){}
	
	protected void getListPathsNonDepth(LinkedHashSet<String> set,
					  ArrayList<Path> list){
		for(String sourceDir : set){
			Path path = Paths.get(sourceDir);
			if(Files.isDirectory(path) && Files.exists(path))
				list.add(path);
		}
	}
	
	protected int randomizeDirectories(int fileCount, ArrayList<Path> dPaths,
									ArrayList<Path> randomFiles, ArrayList<Path> symlinks)throws IOException{
										
		Random rand = new Random();
		//if the # of requested random directories
		//is greater than the # of directories in the source/s
		//directories, take all the directories in the source/s
		if(fileCount >= dPaths.size()){
			fileCount = dPaths.size();
			randomFiles.addAll(dPaths);
		}
		//Otherwise, randomly take directories one by one
		//until the # of requested random directories is met
		else{
			randomFiles.ensureCapacity(fileCount);
			//get random files in every directory
			//in the source
			while(randomFiles.size() != fileCount &&
				  !dPaths.isEmpty()){
				int randDirNum = rand.nextInt(dPaths.size());
				boolean isInRandomFiles = false;
				boolean linkExists = false;
				
				Path randDir = dPaths.get(randDirNum);
				
				//check if the file is already randomed
				for(Path p : randomFiles){
					if(p.toRealPath().toString()
						.equals(randDir.toRealPath().toString())){
						isInRandomFiles = true;
						break;
					}
				}
				
				//verify if the random directory has a soft link already
				for(Path links : symlinks){
					Path resolvedPath = links.resolveSibling(randDir.toFile().getName());
					if(Files.exists(resolvedPath, LinkOption.NOFOLLOW_LINKS)){
						linkExists = true;
						break;
					}
				}	
					
				if(!isInRandomFiles && !linkExists){
					randomFiles.add(randDir);
					dPaths.remove(randDirNum);
				}
				else if(linkExists)
					dPaths.remove(randDirNum);
				
			}
		}
		return fileCount;
	}
	
	protected void copyRandomizedDirectories(int fileCount, JDialog parentDialog,
					  ArrayList<Path> randomFiles, ArrayList<Path> destDir,
					  ArrayList<Path> linkDir){
		int copiedDir = 0;
		int destSymLink = 0;
		int dirExists = 0;
		
		String fileOp = null;
		String symlinkOp = null;
		
		File file = new File("prefs"+File.separator+"randomizersettings.cfg");
		
		if(!file.exists()){
			fileOp = "copy";
			symlinkOp = "fromsource";
		}
		else{
			try(FileInputStream fis = new FileInputStream(file);
				DataInputStream das = new DataInputStream(fis)){
			
				boolean copy = das.readBoolean();
				boolean move = das.readBoolean();
				boolean fromCopy = das.readBoolean();
				boolean fromSource = das.readBoolean();
				
				if(copy)
					fileOp = "copy";
				else if(move)
					fileOp = "move";
				
				if(fromCopy)
					symlinkOp = "fromcopy";
				else if(fromSource)
					symlinkOp = "fromsource";
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(
					parentDialog,
					"<html>Serious Error Occured! Loading settings failed!<br>"+
					"Operation aborted!<br><br>"+
					"Error Description: "+e.getMessage()+"</html>",
					"Operation Aborted",JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		for(Path p : randomFiles){
							
			Path destination = destDir.get(0).resolve(p.toFile().getName());
			Path newSymLinkPath = null;
				
			
			try{
				//create symbolic link from the source to the destination(symlinks) path
				if(!linkDir.isEmpty() && symlinkOp.equals("fromsource")){
					//resolve randomized directory to destination(symlinks) path
					newSymLinkPath = linkDir.get(0).resolve(p.toFile().getName());
					
					if(!Files.exists(newSymLinkPath, LinkOption.NOFOLLOW_LINKS))
						Files.createSymbolicLink(newSymLinkPath, p);
				}
					
				if(Files.exists(destination, LinkOption.NOFOLLOW_LINKS)){
					if(Files.isSymbolicLink(destination))
						destSymLink++;
					else
						dirExists++;
				}
				else{
					FileVisitorCopyFileTree fvc = new FileVisitorCopyFileTree(p, destination, fileOp);
					Files.walkFileTree(p, fvc);
					copiedDir++;
					
					//create symbolic link from the copy to the destination(symlinks) path
					if(!linkDir.isEmpty() && symlinkOp.equals("fromcopy")){
						newSymLinkPath = linkDir.get(0).resolve(p.toFile().getName());
					
						if(!Files.exists(newSymLinkPath, LinkOption.NOFOLLOW_LINKS))
							Files.createSymbolicLink(newSymLinkPath, destination);
					}
				}
					
			}
			catch(FileSystemException e){
				//e.printStackTrace();
				JOptionPane.showMessageDialog(
				parentDialog,
				"<html>An error occured!<br>Reason: <u>"+
				e.getReason()+"</u><br>"+
				"Operation aborted. Some files may not be copied.</html>",
				"Empty Source",JOptionPane.ERROR_MESSAGE);
				return;
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(
				parentDialog,
				"<html>An IO exception occured!<br><u>"+
				e.getCause()+"</u><br><u>"+e.getMessage()+"</u><br>"+
				"Operation aborted. Some files may not be copied.</html>",
				"Empty Source",JOptionPane.ERROR_MESSAGE);
				return;
			}
				
		}
		//
		JOptionPane.showMessageDialog(
		parentDialog,
		"<html>Operation Complete!"+
		"<br><b>Random directories count: </b>"+fileCount+
		"<br><b>Copied directories(all contents)</b>: "+copiedDir+
		"<br><b>Skipped directories due to "+
		"duplicate directories in the destination path: </b>"+dirExists+
		"<br><b>Skipped directories due to "+
		"duplicate directories(symlinks) in the destination path: </b>"+destSymLink
		+"</html>",
		"Randomizer",JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected class FileVisitorForRandomize extends SimpleFileVisitor<Path>{
		private Path startDir;
		private LinkedList<Path> paths = new LinkedList<>();
		
		protected FileVisitorForRandomize(Path startDir){
			this.startDir = startDir;
		}
		
		List<Path> getDirectories(){
			return paths.stream().filter(e -> Files.isDirectory(e))
								 .toList();
		}
		
		List<Path> getRegularFiles(){
			return paths.stream().filter(e -> Files.isRegularFile(e))
								 .toList();
		}
		
		List<Path> getSymbolicLinks(){
			return paths.stream().filter(e -> Files.isSymbolicLink(e))
								 .toList();
		}
		
		List<Path> getPaths(){
			return paths;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, 
							   BasicFileAttributes attrs) throws IOException{
			paths.add(file);
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException{
			
			if(!startDir.toRealPath().toString().equals(dir.toRealPath().toString()))
				paths.add(dir);
			return FileVisitResult.CONTINUE;
		}
	}
	
	protected class FileVisitorCopyFileTree extends SimpleFileVisitor<Path>{
		private final Path source;
		private final Path destination; 
		private final String operation;
		
		protected FileVisitorCopyFileTree(Path source, Path destination, String operation){
			this.source = source;
			this.destination = destination;
			this.operation = operation;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, 
							   BasicFileAttributes attrs) throws IOException{
			if(operation.equals("copy"))
				Files.copy(file, destination.resolve(source.relativize(file)));
			else if(operation.equals("move"))
				Files.move(file, destination.resolve(source.relativize(file)));
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException{
			if(operation.equals("move")){
				if(exc == null)
					Files.deleteIfExists(dir);
				else throw exc;
			}
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException{
			Path dirPath = destination.resolve(source.relativize(dir));
			
			try{
				Files.copy(dir, dirPath);
			}
			catch(FileAlreadyExistsException e){
				if(!Files.isDirectory(dirPath)){
					String reason = "The directory <br>"+dirPath+
					"<br> has a non-directory duplicate in the destination path!";
					throw new FileAlreadyExistsException(e.getFile(), e.getOtherFile(), reason);
				}
			}
			return FileVisitResult.CONTINUE;
		}
	}
}
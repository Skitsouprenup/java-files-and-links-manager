package gui.subpanels.filetree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;

import gui.subpanels.ReportsPanel;
import gui.SortFiles;

public class FileTree extends JTree{
	
	private final JFrame mainFrame;
	private final FileTreePopup ftp;
	private File[] rootSources;
	
	public FileTree(JFrame mainFrame, ReportsPanel rp){
		if(rp == null)
		  throw new NullPointerException("Can't instantiate FileTree!\n"+
		  "ReportsPanel \"rp\" must be instantiated first.");
		
		this.mainFrame = mainFrame;
		ftp = FileTreePopup.instantiate();
		ftp.addMenuActionListener(TreePopupActions.instantiate(ftp, rp));
		setTransferHandler(new FileTransferHandler());
		setDragEnabled(true);
		//According to javadoc:
		/*
		Popup menus are triggered differently on different systems. Therefore, isPopupTrigger
		should be checked in both mousePressed and mouseReleased for proper cross-platform
		functionality.
		*/
		addMouseListener(new MouseAdapter(){
		
			@Override
			public void mousePressed(MouseEvent e){
				if(SwingUtilities.isRightMouseButton(e))
					if(e.isPopupTrigger())
						ftp.getPopup().show(e.getComponent(),e.getX(),e.getY());
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				if(SwingUtilities.isRightMouseButton(e))
					if(e.isPopupTrigger())
						ftp.getPopup().show(e.getComponent(),e.getX(),e.getY());
			}
		
		});
	}
	//--//
	
	//TransferHandler(Drag Support)
	private class FileTransferHandler extends TransferHandler{
		@Override
		public int getSourceActions(JComponent c){
			return TransferHandler.COPY;
		}
	
		@Override
		protected Transferable createTransferable(JComponent source) {
			LinkedList<File> files = new LinkedList<>();
			
			for(TreePath p : getSelectionPaths()){
				DefaultMutableTreeNode node = 
					(DefaultMutableTreeNode)p.getLastPathComponent();
				
				FileTree.FileNode fNode = (FileTree.FileNode)node.getUserObject();
				try{
					int pathLength = fNode.getFile().getCanonicalPath().length();
					if(pathLength > 250){
						//System.out.println("Path length: " + pathLength + " " + 
						//fNode.getFile().getName());
						
					File sourceFile = fNode.getFile();
					Path filePath = Paths.get( sourceFile.getPath() );
					Path destPath = Paths.get( fNode.getFile().getParentFile().getParent()+
					File.separator + fNode.getFile().getName() );
						if(sourceFile.exists()){
							Files.move(filePath, destPath,
							StandardCopyOption.REPLACE_EXISTING);
							files.add(destPath.toFile());
						}
						else
							if(destPath.toFile().exists())
								files.add(destPath.toFile());
							else System.out.println(destPath + " doesn't exist!");
					}
					else files.add(fNode.getFile());
					
				}catch(IOException e){e.printStackTrace();}
				
			}
		    
			Comparator<File> sortType = new SortFiles().createInstance("IgnoreCase");
			Collections.sort(files, sortType);
			return new FileTypeTransfer(files);
		}

	}
	//--//
	
	//Subclass Transferable to allow file types to be dragged
	//from a java program to Windows explorer or desktop or other programs
	private class FileTypeTransfer implements Transferable {

        private List<File> files;

        public FileTypeTransfer(List<File> files) {
            this.files = files;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.javaFileListFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.javaFileListFlavor);
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException, IOException{
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return files;
        }
    }
	
	//
	void addTreeComponents() throws IOException{
		if(rootSources != null)
			addTreeComponents(rootSources);
		else
			System.err.println
		    ("No Root Directories to be displayed in the tree!");
	}
	//
	void addTreeComponents(Object[] source) throws IOException{
		int count = 0;
		File[] fSources = new File[source.length];
			
		for(int i = 0; i < fSources.length; i++){
			if(source[i] instanceof File){
				fSources[i] = (File)source[i];
				count++;
			}
		}
		addTreeComponents(
		java.util.Arrays.copyOfRange(fSources, 0, count));
			
	}
	
	//
	public void addTreeComponents(File[] source) throws IOException{
		
		DefaultMutableTreeNode root = null;
		rootSources = source;
		
		if(source.length == 1){
			root = new DefaultMutableTreeNode( new FileNode(source[0]) );
			setModel( new DefaultTreeModel(createNodes(root),true) );
			ftp.getFileTree(this, mainFrame);
		}
		else if(source.length > 1) {
			File parent = source[0].getParentFile();
			if(parent == null){
				JOptionPane.showMessageDialog(mainFrame,"Parent of " + source[0].getName() + " Doesn't exist.",
										  "No Parent Directory",JOptionPane.ERROR_MESSAGE);
				return;
			}
			else root = new DefaultMutableTreeNode( new FileNode(parent) );
			
			setModel( new DefaultTreeModel(createNodes(source,root),true) );
			ftp.getFileTree(this, mainFrame);
		}
		else
			JOptionPane.showMessageDialog(mainFrame,"No files to be processed!",
										  "No File Selected",JOptionPane.ERROR_MESSAGE);
	}
	
	private DefaultMutableTreeNode createNodes(DefaultMutableTreeNode root){
		FileNode fn = (FileNode)root.getUserObject();
		File file = fn.getFile();
		
		if(file.isDirectory()){
			File[] fileList = file.listFiles();
			iterateFileList(fileList,root);
		}
		
		return root;
	}
	
	private DefaultMutableTreeNode createNodes(File[] source,DefaultMutableTreeNode root){
		FileNode fn = (FileNode)root.getUserObject();
		File file = fn.getFile();
		
		if(file.isDirectory())
			for(File subSource : source)
				if(subSource.isDirectory()){
					File[] fileList = subSource.listFiles();
					
					if(checkNullDir(fileList,subSource.getName()))
						continue;
					
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
													   new FileNode(subSource),true);
					root.add(childNode);
					iterateFileList(fileList,childNode);
				}
		return root;
	}
	
	private void iterateFileList(File[] list,DefaultMutableTreeNode root){
		
		for(File source : list){
			
			if(source.isDirectory()){
				File[] fileList = source.listFiles();
				
				if(checkNullDir(fileList,source.getName()))
					continue;
				
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
												   new FileNode(source),true);
				root.add(childNode);
					
				iterateFileList(fileList,childNode);
				continue;
			}

			root.add( new DefaultMutableTreeNode(new FileNode(source),false) );
			
		}
		
	}
	
	//This method checks when listFiles() returns null
	//even a file is a directory
	private boolean checkNullDir(File[] listFiles, String sourceName){
		if(listFiles == null){
			JOptionPane.showMessageDialog(mainFrame,"Error Occured: "+sourceName+" couldn't be "+
										  "accessed\nor its contents couldn't be read.",
										  "Unreadable/Unaccessible Directory",JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}
	
	static class FileNode{
		private File file;
		
		FileNode(File file){
			this.file = file;
		}
		
		@Override
		public String toString(){
			return file.getName();
		}
		
		public File getFile(){ return file; }
	}
	
}
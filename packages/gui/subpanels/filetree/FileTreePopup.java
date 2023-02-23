package gui.subpanels.filetree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import gui.events.TreePopupEvents;
import fileoperations.FileOperations;
import fileoperations.LinkOperations;
import gui.subpanels.ReportsPanel;

public class FileTreePopup extends TreePopupEvents{
	private final JMenuItem[] popupOptions;
	private final JPopupMenu jpm;
	private final JMenu deleteMenu,selectMenu;
	
	private static FileTreePopup instance;
	private static boolean isInstantiated;
	
	private JTree tree;
	private DefaultTreeModel defTreeModel;
	private JFrame mainFrame;
	
	public static FileTreePopup instantiate(){
		if(!isInstantiated){
			instance = new FileTreePopup();
			isInstantiated = true;
		}
		
		return instance;
	}
	
	//add events to popup buttons of FileViewPanel
	void addMenuActionListener(TreePopupActions tpa){
		for(JMenuItem item : popupOptions)
			item.addActionListener(tpa);
	}
	
	private FileTreePopup(){
		jpm = new JPopupMenu();
		deleteMenu = new JMenu("Delete Selected...");
		selectMenu = new JMenu("Select...");
		popupOptions = new JMenuItem[8];
		//delete options
		popupOptions[0] = new JMenuItem("File/s");
		popupOptions[1] = new JMenuItem("Zip File/s");
		popupOptions[2] = new JMenuItem("\"dummy_file\" File/s");
		popupOptions[3] = new JMenuItem("Expand Selected Directories");
		popupOptions[4] = new JMenuItem("Collapse Selected Directories");
		//select options
		popupOptions[5] = new JMenuItem("Zip Files");
		//make dir
		popupOptions[6] = new JMenuItem("Create Directories for Files");
		//
		popupOptions[7] = new JMenuItem("Replace Text...");
		
		deleteMenu.add(popupOptions[0]);
		deleteMenu.add(popupOptions[1]);
		deleteMenu.add(popupOptions[2]);
		
		selectMenu.add(popupOptions[5]);
		
		jpm.add(deleteMenu);
		jpm.add(selectMenu);
		jpm.add(popupOptions[3]);
		jpm.add(popupOptions[4]);
		jpm.add(popupOptions[6]);
		jpm.add(popupOptions[7]);
	}
	
	void getFileTree(JTree tree, JFrame mainFrame){
		this.tree = tree;
		if(this.mainFrame != null)
			this.mainFrame = mainFrame;
		
		if(tree.getModel() instanceof DefaultTreeModel)
			defTreeModel = (DefaultTreeModel)tree.getModel();
		else throw new NullPointerException("This program only supports DefaultTreeModel."+
											 "Current File Tree model is not DefaultTreeModel.");
	}
	
	void selectSpecificFiles(SelectOption ssf, StringBuilder msg)
	                                           throws IOException{
		TreePath[] nodePaths = tree.getSelectionPaths();
		
		if(nodePaths != null){
			msg.append("Selected File/s<br />");
			for(TreePath p : nodePaths){
				DefaultMutableTreeNode node = 
						(DefaultMutableTreeNode)p.getLastPathComponent();
				
				FileTree.FileNode fNode = (FileTree.FileNode)node.getUserObject();
				
				File file = fNode.getFile();
				
				if(ssf == SelectOption.ZIP){
					String pattern = "\\.zip|\\.zip\\.{1}\\d+";
					boolean isZip = LinkOperations.
					                findSubSequence(pattern,file.getName(),true);
					if(isZip){
						tree.addSelectionPath(p);
						msg.append(file.getCanonicalPath() + "<br />");
					}
					else tree.removeSelectionPath(p);
				}
			}
		}
		else
			JOptionPane.showMessageDialog(
			mainFrame,"No Selected Files!",
			"No File Selected",JOptionPane.ERROR_MESSAGE);
	}
	
	void expandCollapseDirs(boolean doExpand){
		TreePath[] nodePaths = tree.getSelectionPaths();
		
		if(nodePaths != null){
			
			DefaultMutableTreeNode node = null;
			
			if(doExpand)
				for(TreePath p : nodePaths){
					node = (DefaultMutableTreeNode)p.getLastPathComponent();
					if(!tree.isExpanded(p) && node.getAllowsChildren())
						tree.expandPath(p);
				}
						
			else
				for(TreePath p : nodePaths){
					node = (DefaultMutableTreeNode)p.getLastPathComponent();
					if(!tree.isCollapsed(p) && node.getAllowsChildren())
					tree.collapsePath(p);
				}
			
		}
		else 
			JOptionPane.showMessageDialog(
			mainFrame,"No Selected Files!",
			"No File Selected",JOptionPane.ERROR_MESSAGE);
	}
	
	void createDirsForFiles(StringBuilder msg) throws IOException{
		TreePath[] nodePaths = tree.getSelectionPaths();
		
		if(nodePaths != null){
			
			int operationCompletedCount = 0;
			msg.append("Created Directories<br />");
			for(TreePath p : nodePaths){
				DefaultMutableTreeNode node = 
				(DefaultMutableTreeNode)p.getLastPathComponent();
				
				FileTree.FileNode fNode = (FileTree.FileNode)node.getUserObject();
				File file = fNode.getFile();
				
				if(file.isFile()){
					String pattern = "[.][a-zA-Z0-9]+[^.]";
					//[0] = start-index
					//[1] = end-index
					int[] fRange = LinkOperations.getFileExtensionRange
					               (file.getName(),pattern,true);
					if(fRange != null){
						String noExtensionName = 
						file.getName()
						    .substring(0,fRange[0]);
						
						String newDir = 
						file.getParentFile()
						    .getCanonicalPath()
							+File.separator
							+noExtensionName;
							
						//trim unnecessary
						//leading and trailing characters(like white space)
						//to avoid InvalidPathException
						newDir = newDir.trim();
						if(new File(newDir).mkdir()){
							msg.append("<u>"+newDir
							+"</u> <span style=\"color:green;\"> directory has been created!</span><br />");
							
							
							Path source = file.toPath();
							//trim unnecessary
							//leading and trailing characters(like white space)
							//Target path in the parameter of move() shouldn't
							//have any trailing whitespaces. Otherwise,
							//InvalidPathException will be thrown
							//examples of invalid target paths in Path interface
							//C://test /file.txt
							//C://test/file .txt
							Path target = Paths.get(newDir+File.separator
							              +noExtensionName.trim()
										  +file.getName().substring(fRange[0],fRange[1]));
							try{
								Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
								msg.append("<u>"+file.getCanonicalPath()
							    +"</u> <span style=\"color:green;\">has been moved!</span><br />");
								operationCompletedCount++;
							}
							catch(IOException e){
								System.err.println(e.getMessage());
								msg.append("<u>"+file.getCanonicalPath() +
								"</u> <span style=\"color:red;\">couldn't"+
								" be moved to new directory!</span><br />");
								continue;
							}
							
						}
						else
							msg.append("<u>"+newDir
						    +"</u> <span style=\"color:red;\">couldn't be created!</span><br />");
					}
					else
						msg.append("<u>"+
					    file.getCanonicalPath() + "</u>"+
						" <span style=\"color:red;\">has invalid file extension!</span><br />");
					
				}
				else 
				  msg.append("<u>"+
				  file.getCanonicalPath() + "</u>"+
				  " <span style=\"color:red;\">is already a directory!</span><br />");
				msg.append("<br />");
			}
			
			if(operationCompletedCount > 0){
				if(tree instanceof FileTree){
				   FileTree ft = (FileTree)tree;
				   ft.addTreeComponents();
				}
				else throw new IllegalStateException("tree is not FileTree!");
			}
		}
		else
			JOptionPane.showMessageDialog(
			mainFrame,"No Selected Files!",
			"No File Selected",JOptionPane.ERROR_MESSAGE);
	}
	
	void deleteSelectedFiles(DeleteOption option, StringBuilder msg)
                   	                              throws IOException{
		TreePath[] nodePaths = tree.getSelectionPaths();
		
		if(nodePaths != null){
			int delFileCount = 0;
			String delFileStr = "";
			String zipDeleteExtraMsg = "";
			StringBuilder delInfo = new StringBuilder();
			
			//separate directories from files
			for(TreePath p : nodePaths){
				DefaultMutableTreeNode node = 
						(DefaultMutableTreeNode)p.getLastPathComponent();
				
				FileTree.FileNode fNode = (FileTree.FileNode)node.getUserObject();
				File file = fNode.getFile();
				
				boolean opt = false;
				boolean isZip = false;
				
				switch(option){
					
					case DELETE_ANY:
					opt = file.exists();
					delInfo.append("<br /><b>Deleted File/s and Directory/ies</b><br />");
					delFileStr = "<b>Number of Deleted File/s and Directory/ies:</b> ";
					break;
					
					case DELETE_ZIP:
					String pattern = "\\.zip|\\.zip\\.{1}\\d+";
					isZip = LinkOperations.
					        findSubSequence(pattern,file.getName(),true);
					opt = file.exists() && isZip && file.isFile();
					zipDeleteExtraMsg = "<br /><b>Deleted Zip File/s</b><br />";
					delFileStr = "<b>Number of Deleted Zip File/s:</b> ";
					break;
					
					case DELETE_DUMMY:
					opt = file.exists() && file.getName().equals("dummy_file") && file.isFile();
					delFileStr = "<b>Number of Deleted Dummy File/s:</b> ";
					break;
					
				}
				
				if(opt){
					if(FileOperations.getMoveToTrashSupport() &&
					   option != DeleteOption.DELETE_DUMMY){
					   if(!FileOperations.getDesktop().moveToTrash(file)){
						   System.err.println(file.getName()+
						   "(FileTreePopup.java)(1) couldn't be deleted or not existing.");
						   continue;
					   }else{ 
					     delInfo.append(file.getCanonicalPath() + "<br />");
					     node.removeFromParent();
						 delFileCount++;
					   }
					}
					else{
					   //if statement for deleting dummy files
					   if(!file.delete()){
						   System.err.println(file.getName()+
						   "(FileTreePopup.java)(2) couldn't be deleted or not existing.");
						   continue;
						}else{
						  node.removeFromParent();
						  delFileCount++;
						}
					}
				}
				else{
					//This if statement checks if the zip file in the file viewer still
					//exists in its directory. If not, the program might have moved the 
					//file from the parent of its directory.
					if(!file.exists() && isZip){
					File f = new File(file.getParentFile().getParent()+File.separator+file.getName());
					    //delete the file if it's moved by the program
						if(f.exists()){
							if(!f.delete()){
								System.err.println(f.getName()+
								"(FileTreePopup.java)(3) couldn't be deleted or not existing.");
								continue;
							}else{
							  node.removeFromParent();
							  delFileCount++;
							  delInfo.append(file.getCanonicalPath() + "<br />");
							}
						}
						//Otherwise, the moved file may have been deleted.
						else{
							System.err.println(f.getPath()+
							" doesn't exist in the parent path!");
						}
					}
				}
			}
			defTreeModel.reload();
			String totalDelFile = delFileStr.concat(Integer.valueOf(delFileCount).toString());
			msg.append(totalDelFile + "<br />" + zipDeleteExtraMsg + delInfo.toString());
			
		}
		else 
			JOptionPane.showMessageDialog(
			mainFrame,"No Selected Files!",
			"No File Selected",JOptionPane.ERROR_MESSAGE);
	}
	
	void replaceTextDialog(StringBuilder msg, ReportsPanel rp){
		TreePath[] nodePaths = tree.getSelectionPaths();
		
		if(nodePaths != null){
			if(!ReplaceTextDialog.isInstantiated())
				new ReplaceTextDialog(mainFrame, msg, tree, rp);
			else ReplaceTextDialog.retainFocus();
		}
		else
			JOptionPane.showMessageDialog(
			mainFrame,"No Selected Files!",
			"No File Selected",JOptionPane.ERROR_MESSAGE);
	}
	
	public int getTreePopupOptionsLn(){ return popupOptions.length; }
	public JMenuItem getTreePopupOption(int index){ return popupOptions[index]; }
	public JPopupMenu getPopup(){ return jpm; }
	
	enum DeleteOption{
		DELETE_ANY,DELETE_ZIP,DELETE_DUMMY
	}
	
	enum SelectOption{
		ZIP
	}
}
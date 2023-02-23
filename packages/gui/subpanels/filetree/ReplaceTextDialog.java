package gui.subpanels.filetree;

import java.awt.Dialog.ModalityType;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import java.util.regex.Pattern;
import java.util.ArrayList;

import gui.subpanels.ReportsPanel;

class ReplaceTextDialog{
	private static JDialog staticMainFrame;
	
	private JDialog mainFrame;
	private JPanel mainPanel;
	private Window parent;
	
	private JButton okBtn, cancelBtn;
	private JCheckBox startsWithCb, endsWithCb, replaceAllCb;
	
	private JTextField inputTxt, replaceWithTxt;
	
	private JLabel textToFindLbl, replaceLbl;
	
	private StringBuilder msg;
	private JTree tree;
	private ReportsPanel rp;
	ReplaceTextDialog(Window parent, StringBuilder msg,
	                  JTree tree, ReportsPanel rp){
		this.parent = parent;
		this.msg = msg;
		this.tree = tree;
		this.rp = rp;
		mainFrame = new JDialog(parent,"Replace Text", ModalityType.MODELESS);
		mainPanel = new JPanel(new GridBagLayout());
		staticMainFrame = mainFrame;
		
		okBtn = new JButton("Replace");
		cancelBtn = new JButton("Cancel");
		
		startsWithCb = new JCheckBox("Leading");
		endsWithCb = new JCheckBox("Trailing");
		replaceAllCb = new JCheckBox("Replace All");
		replaceAllCb.setSelected(true);
		
		textToFindLbl = new JLabel("Text: ");
		replaceLbl = new JLabel("Replace with: ");
		inputTxt = new JTextField(15);
		replaceWithTxt = new JTextField(15);
		
		addComponents();
		
		mainFrame.addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e){
				staticMainFrame = null;
			}
		});
	}
	
	public static boolean isInstantiated(){
		if(staticMainFrame != null)
			return true;
		else return false;
	}
	
	public static void retainFocus(){
		if(staticMainFrame != null)
			staticMainFrame.requestFocus();
	}
	
	private void addComponents(){
		JPanel inputPnl = new JPanel(new GridBagLayout());
		JPanel replaceWithPnl = new JPanel(new GridBagLayout());
		JPanel cbPnl = new JPanel(new GridBagLayout());
		JPanel btnPnl = new JPanel(new GridBagLayout());
		
		inputPnl.add(textToFindLbl, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.EAST,
									    GridBagConstraints.NONE,new Insets(5,5,5,1),0,0));
		inputPnl.add(inputTxt, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.EAST,
								   GridBagConstraints.NONE,new Insets(5,5,1,5),0,0));
		mainPanel.add(inputPnl, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.EAST,
								    GridBagConstraints.NONE,new Insets(5,5,1,5),0,0));
		
		replaceWithPnl.add(replaceLbl, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
									       GridBagConstraints.NONE,new Insets(5,5,5,0),0,0));
		replaceWithPnl.add(replaceWithTxt, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,
								               GridBagConstraints.NONE,new Insets(5,5,1,5),0,0));
		mainPanel.add(replaceWithPnl, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.CENTER,
								          GridBagConstraints.NONE,new Insets(5,5,1,5),0,0));
		
		cbPnl.add(startsWithCb, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
								GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		cbPnl.add(endsWithCb, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,
							  GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		cbPnl.add(replaceAllCb, new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.CENTER,
						   GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		mainPanel.add(cbPnl, new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.CENTER,
								 GridBagConstraints.NONE,new Insets(1,5,1,5),0,0));
								
		btnPnl.add(okBtn, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							  GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		btnPnl.add(cancelBtn, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,
							      GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		mainPanel.add(btnPnl, new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.CENTER,
								  GridBagConstraints.NONE,new Insets(1,5,5,5),0,0));
		
		addActions();
		mainFrame.add(mainPanel);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(parent);
		mainFrame.setVisible(true);
	}
	
	private void addActions(){
		ReplaceTextActions rta = new ReplaceTextActions();
		ReplaceTextCheckedActions rtca = new ReplaceTextCheckedActions();
		
		okBtn.addActionListener(rta);
		cancelBtn.addActionListener(rta);
		
		startsWithCb.addActionListener(rtca);
		endsWithCb.addActionListener(rtca);
		replaceAllCb.addActionListener(rtca);
	}
	
	private void replaceText() throws IOException{
		int operationCompletedCount = 0;
		int operationFailedCount = 0;
		ArrayList<File> rootComponents = new ArrayList<>();
		
		TreePath[] nodePaths = tree.getSelectionPaths();
		
		if(nodePaths == null){
			JOptionPane.showMessageDialog(
			parent,"No Selected Files!",
			"No File Selected",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		msg.append("<div style='white-space:nowrap;'>");
		for(TreePath p : nodePaths){
			DefaultMutableTreeNode node = 
				(DefaultMutableTreeNode)p.getLastPathComponent();
				
			FileTree.FileNode fNode = (FileTree.FileNode)node.getUserObject();
			File file = fNode.getFile();
			
			String filename = file.getName();
			Path source = file.toPath();
			String encodedString = Pattern.quote(inputTxt.getText());
			
			if(startsWithCb.isSelected() && endsWithCb.isSelected()){
				filename = filename.replaceAll
				("^"+encodedString+"|"+encodedString+"$",replaceWithTxt.getText());
			}
			else if(startsWithCb.isSelected()){
				filename = filename.replaceFirst
				(encodedString, replaceWithTxt.getText());
			}
			else if(endsWithCb.isSelected()){
				filename = filename.replaceAll
				(encodedString+"$",replaceWithTxt.getText());
			}
			else if(replaceAllCb.isSelected()){
				filename = filename.replaceAll
				(encodedString,replaceWithTxt.getText());
			}
			else{
				JOptionPane.showMessageDialog(
				parent,"No replace type is checked!",
				"No Checked Replace Type",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Path target = source.resolveSibling(filename.trim());
			if(source.toString().equals(target.toString())){
				msg.append("<span style=\"color:red;\"><u>"+source.toString()
				+"</u> is equal to the target path! Renaming failed!</span><br />");
				operationFailedCount++;
				continue;
			}
			
			try{
				Files.move(source, target);
			}
			catch(FileAlreadyExistsException e){
				msg.append("<span style=\"color:red;\"><u>"+target.toString()
				+"</u> already exists! Renaming failed!</span><br />");
				operationFailedCount++;
				continue;
			}
			catch(NoSuchFileException e){
				msg.append("<span style=\"color:red;\"><u>"+target.toString()
				+"</u> doesn't exist! Renaming failed!</span><br />");
				operationFailedCount++;
				continue;
			}
			
			operationCompletedCount++;
			if(p.getPathCount() == 1)
				rootComponents.add(target.toFile());
			
			msg.append("<span style=\"color:green;\"><u>"+source.toFile().getName()
			+"</u> is successfully renamed to <u>"+target.toFile().getName()+"</u></span><br />");
		}
		
		if(operationCompletedCount > 0){
			if(tree instanceof FileTree){
				FileTree ft = (FileTree)tree;
				
				if(rootComponents.size() != 0)
					ft.addTreeComponents(rootComponents.toArray());
				else
					ft.addTreeComponents();
			}
			else throw new IllegalStateException("tree is not FileTree!");
		}
		msg.append("</div>");
		rp.getSubPnlep().setText(msg.toString());
		
		JOptionPane.showMessageDialog(
		parent,"<html>Operation Complete!"+
		"<br> Number of completed operation/s: " + operationCompletedCount+
		"<br> Number of failed operation/s: " + operationFailedCount+
		"<br><br> Check the Reports panel for more information.</html>",
		"Operation Complete",JOptionPane.INFORMATION_MESSAGE);
		msg.setLength(0);
	}
	
	private class ReplaceTextCheckedActions implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try{ checkActionSource(e); }
			catch(IOException f){
				f.printStackTrace();
			}
		}
	}
	
	private class ReplaceTextActions implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try{ actionSource(e); }
			catch(IOException f){
				f.printStackTrace();
			}
		}
	}
	
	private void checkActionSource(ActionEvent e) throws IOException{
		if(e.getSource() == startsWithCb){
			if(replaceAllCb.isSelected())
				replaceAllCb.setSelected(false);
		}
		else if(e.getSource() == endsWithCb){
			if(replaceAllCb.isSelected())
				replaceAllCb.setSelected(false);
		}
		else if(e.getSource() == replaceAllCb){
			if(startsWithCb.isSelected())
				startsWithCb.setSelected(false);
			if(endsWithCb.isSelected())
				endsWithCb.setSelected(false);
			
		}
	}
	
	private void actionSource(ActionEvent e) throws IOException{
		if(e.getSource() == okBtn){
			replaceText();
		}
		else if(e.getSource() == cancelBtn){
			staticMainFrame = null;
			mainFrame.dispose();
		}
	}
	
}
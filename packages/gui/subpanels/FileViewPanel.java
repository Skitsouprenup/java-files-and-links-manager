package gui.subpanels;

import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.events.FileViewPEvents;
import gui.subpanels.filetree.FileTree;

public class FileViewPanel extends FileViewPEvents implements ISubPanel{
	private final JPanel subPanel;
	
	private final JButton[] btns;
	private final JPanel pnl1;
	private final FileTree tree;
	private final JScrollPane treePane;
	
	private static FileViewPanel instance;
	
	private FileViewPanel(JPanel subPanel,ActionListener btnListener, 
					      JFrame mainFrame){
		this.subPanel = subPanel;
		btns = new JButton[1];
		pnl1 = new JPanel(new GridBagLayout());
		tree = new FileTree(mainFrame, ReportsPanel.getInstance());
		treePane = new JScrollPane(tree);
		treePane.setVisible(false);
		addComponents();
		addBtnActListener(btnListener);
	}
	
	public static FileViewPanel instantiate(JPanel subPanel,ActionListener btnListener, 
										    JFrame mainFrame){
		if(instance == null)
			instance = new FileViewPanel(subPanel,btnListener,mainFrame);
		
		return instance;
	}
	
	@Override
	public void addBtnActListener(ActionListener btnListener){
		if(instance != null) return;
		
		for(JButton btn : btns)
			btn.addActionListener(btnListener);
	}
	
	@Override
	public void addGuiRelatedActListener(ActionListener guiListener){}
	
	@Override
	public void addComponents(){
		if(instance != null) return;
		
		JLabel lbl1 = new JLabel("File Hierarchy Viewer",JLabel.CENTER);
		JLabel lbl2 = new JLabel("<html><center>This panel lets you view files in hirerarchical"+
								 " order</center></html>",JLabel.CENTER);
		
		pnl1.add(lbl1,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
						  GridBagConstraints.NONE,new Insets(5,5,5,10),0,0));
												 
		pnl1.add(lbl2,new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.CENTER,
						  GridBagConstraints.NONE,new Insets(5,5,5,10),0,0));
											
		subPanel.add(pnl1,new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.CENTER,
							  GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
												 
		subPanel.add(treePane,new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,
								  GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
		
		btns[0] = new JButton("Select Directories");
		subPanel.add(btns[0],new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.CENTER,
													GridBagConstraints.NONE,new Insets(5,5,5,11),0,0));
	}
	
	@Override
	public void setEnabledMainComps(boolean state){
		btns[0].setEnabled(state);
		tree.setEnabled(state);
	}
	
	public int getSubPnlBtnsLn(){ return btns.length; }
	public JButton getSubPnlBtn(int index){ return btns[index]; }
	public boolean getTreePaneVisibility(){ return treePane.isVisible(); }
	
	public void setTreePaneVisibility(boolean state){ treePane.setVisible(state); }
	
	public void setFileTreeNodes(File[] source) throws IOException {
		tree.addTreeComponents(source);
	}
	
	@Override
	public JPanel getPanel(){ return subPanel; }
}
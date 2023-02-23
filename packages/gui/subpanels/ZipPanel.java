package gui.subpanels;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import gui.DefaultFont;
import gui.events.ZipPEvents;

public class ZipPanel extends ZipPEvents implements ISubPanel{
	private final JPanel subPanel;
	//sub panel4's buttons
	private final JButton[] btns;
	//sub panel4's radiobuttons
	private final JRadioButton[] rbs;
	//sub panel4's checkbox
	private final JCheckBox[] cbs;
	//sub panel4's radiobutton
	private final JComboBox<String> jcb;
	
	private static ZipPanel instance;
	private static boolean isInstantiated;
	
	private ZipPanel(JPanel subPanel){
		this.subPanel = subPanel;
		btns = new JButton[2];
		rbs = new JRadioButton[4];
		cbs = new JCheckBox[4];
		String[] compressionTypes = {"Best","Normal","Fastest","Store"};
		jcb = new JComboBox<String>(compressionTypes);
		jcb.setSelectedIndex(3);
	}
	
	public static ZipPanel instantiate(JPanel subPanel,ActionListener btnListener,
									   ActionListener guiListener){
		if(!isInstantiated){
			instance = new ZipPanel(subPanel);
			instance.addComponents();
			instance.addBtnActListener(btnListener);
			instance.addGuiRelatedActListener(guiListener);
			isInstantiated = true;
		}
		return instance;
	}
	
	@Override
	public void addBtnActListener(ActionListener btnListener){
		if(isInstantiated) return;
		
		for(JButton button : btns)
			button.addActionListener(btnListener);
	}
	
	@Override
	public void addGuiRelatedActListener(ActionListener guiListener){
		if(isInstantiated) return;
		
		rbs[2].addActionListener(guiListener);
		rbs[3].addActionListener(guiListener);
	}
	
	@Override
	public void addComponents(){
		if(isInstantiated) return;
		
		JLabel lbl1 = new JLabel("<html><center><p>Select directories and the program will compress "+ 
								 "those selected directories.</p></center></html>",JLabel.CENTER);
		subPanel.add(lbl1,new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.CENTER,
													  GridBagConstraints.HORIZONTAL,new Insets(10,40,3,50),0,0));
		//
		JLabel lbl1b = new JLabel("Compression Type");
		lbl1b.setFont(new Font(DefaultFont.getFontName(),DefaultFont.getStyle(),14));
		subPanel.add(lbl1b,new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.CENTER,
												  GridBagConstraints.NONE,new Insets(5,5,0,21),0,0));
		//jcb add & constraints
		subPanel.add(jcb,new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.CENTER,
											    GridBagConstraints.NONE,new Insets(5,5,10,25),0,0));
		
		cbs[2] = new JCheckBox("Exclude Collection Directory",true);
		subPanel.add(cbs[2],new GridBagConstraints(0,3,1,1,1,0,GridBagConstraints.CENTER,
												   GridBagConstraints.NONE,new Insets(0,0,0,20),0,0));
												  
	    cbs[3] = new JCheckBox("Exclude post.txt",true);
		subPanel.add(cbs[3],new GridBagConstraints(0,4,1,1,1,0,GridBagConstraints.CENTER,
												   GridBagConstraints.NONE,new Insets(0,0,5,20),0,0));
		
		JPanel pnl1 = new JPanel(new GridBagLayout());
		JLabel lbl1a = new JLabel("Packaging Type");
		lbl1a.setFont(new Font(DefaultFont.getFontName(),DefaultFont.getStyle(),14));
		ButtonGroup btnGrp1 = new ButtonGroup();
		rbs[0] = new JRadioButton("Per Selected Directory");
		rbs[1] = new JRadioButton("Single Package");
		btnGrp1.add(rbs[0]);
		btnGrp1.add(rbs[1]);
		rbs[0].setSelected(true);
		cbs[0] = new JCheckBox("Include Sub-Directories",false);
		//
		pnl1.add(lbl1a,new GridBagConstraints(0,0,2,1,0,0,GridBagConstraints.CENTER,
											  GridBagConstraints.NONE,new Insets(0,0,5,0),0,0));
		//
		pnl1.add(rbs[0],new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.CENTER,
											   GridBagConstraints.NONE,new Insets(0,5,0,5),0,0));
		//
		pnl1.add(rbs[1],new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.CENTER,
											   GridBagConstraints.NONE,new Insets(0,5,0,5),0,0));
		//
		pnl1.add(cbs[0],new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.CENTER,
											   GridBagConstraints.NONE,new Insets(0,5,0,7),0,0));
		//
		subPanel.add(pnl1,new GridBagConstraints(0,5,1,1,1,0,GridBagConstraints.CENTER,
												 GridBagConstraints.NONE,new Insets(0,5,5,20),0,0));
		
		JPanel compBtnPnl = new JPanel(new GridBagLayout());
		btns[0] = new JButton("Compress Files");
		compBtnPnl.add(btns[0], new GridBagConstraints(0,0,1,1,0,0, GridBagConstraints.CENTER,
							        GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
		
		subPanel.add(compBtnPnl,new GridBagConstraints(0,6,1,1,0,0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL,new Insets(0,0,10,26),0,0));
		
		JLabel lbl2 = new JLabel("<html><center><p>Select zip files or directories and the program will"+
									  " decompress those selected zip files or zip files in the selected"+
									  " directories</p></center></html>",JLabel.CENTER);
		subPanel.add(lbl2,new GridBagConstraints(0,7,1,1,1,0,GridBagConstraints.CENTER,
											     GridBagConstraints.HORIZONTAL,new Insets(10,40,3,50),0,0));
		
		JPanel pnl2 = new JPanel(new GridBagLayout());
		JLabel lbl3 = new JLabel("Selection Type");
		lbl3.setFont(new Font(DefaultFont.getFontName(),DefaultFont.getStyle(),14));
		ButtonGroup btnGrp2 = new ButtonGroup();
		rbs[2] = new JRadioButton("Directory");
		rbs[3] = new JRadioButton("Zip File");
		btnGrp2.add(rbs[2]);
		btnGrp2.add(rbs[3]);
		rbs[2].setSelected(true);
		cbs[1] = new JCheckBox("Include Sub-Directories",false);
		
		pnl2.add(lbl3,new GridBagConstraints(0,0,2,1,0,0,GridBagConstraints.CENTER,
												  GridBagConstraints.NONE,new Insets(0,0,5,6),0,0));
		//
		pnl2.add(rbs[2],new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.CENTER,
						   					   GridBagConstraints.NONE,new Insets(0,5,0,5),0,0));
		//
		pnl2.add(rbs[3],new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.CENTER,
											   GridBagConstraints.NONE,new Insets(0,5,0,5),0,0));
		//
		pnl2.add(cbs[1],new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.CENTER,
											   GridBagConstraints.NONE,new Insets(0,5,0,5),0,0));
		//
		subPanel.add(pnl2,new GridBagConstraints(0,8,1,1,1,0,GridBagConstraints.CENTER,
												 GridBagConstraints.NONE,new Insets(5,5,5,20),0,0));
		
		JPanel deCompBtnPnl = new JPanel(new GridBagLayout());
		btns[1] = new JButton("Decompress Files");
		deCompBtnPnl.add(btns[1], new GridBagConstraints(0,0,1,1,0,0, GridBagConstraints.CENTER,
							          GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
		
		subPanel.add(deCompBtnPnl,new GridBagConstraints(0,9,1,1,0,0,GridBagConstraints.CENTER,
									  GridBagConstraints.HORIZONTAL,new Insets(0,0,10,26),0,0));
													
		//Resize Buttons
		Dimension dm = new Dimension();
		dm.setSize(170, 25);
		for(JButton btn : btns)
			btn.setPreferredSize(dm);
	}
	
	@Override
	public void setEnabledMainComps(boolean state){
		jcb.setEnabled(state);
		
		for(JButton btn : btns)
			btn.setEnabled(state);
		for(JRadioButton rb : rbs)
			rb.setEnabled(state);
		for(JCheckBox cb : cbs)
			cb.setEnabled(state);
	}
	
	public int getSubPnlBtnsLn(){ return btns.length; }
	public JButton getSubPnlBtn(int index){ return btns[index]; }
	public JRadioButton getSubPnlRb(int index){ return rbs[index]; }
	public JCheckBox getSpnlCb(int index){ return cbs[index]; }
	public JComboBox<String> getSpnlJcb(){ return jcb; }
	
	@Override
	public JPanel getPanel(){ return subPanel; }
}
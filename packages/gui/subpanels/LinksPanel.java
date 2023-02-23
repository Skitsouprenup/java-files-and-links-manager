package gui.subpanels;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Color;

import gui.DefaultFont;
import gui.events.LinksPEvents;

public class LinksPanel extends LinksPEvents implements ISubPanel{
	private final JPanel subPanel;
	//subPnl3's button
	private final JButton[] btns;
	//subPnl3's text area
	private final JTextArea jta;
	//subPnl3's radio buttons
	private final JRadioButton[] rbs;
	//subPnl3's checkbox
	private final JCheckBox cb;
	
	private static LinksPanel instance;
	private static boolean isInstantiated;
	
	private LinksPanel(JPanel subPanel){
		this.subPanel = subPanel;
		btns = new JButton[2];
		rbs = new JRadioButton[2];
		jta = new JTextArea();
		
		//best way to set components' background color
		//This method remove artifacts during hover event.
		cb = new JCheckBox("Include Sub-Directories", false){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				
				g.setColor(new Color(0,0,0,1));
				g.fillRect(0,0,getWidth(),getHeight());
			}
		};
		cb.setOpaque(false);
	}
	
	public static LinksPanel instantiate(JPanel subPanel,ActionListener btnListener,
										 ActionListener guiListener){
		if(!isInstantiated){
			instance = new LinksPanel(subPanel);
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
		
		for(JRadioButton rBtn : rbs)
			rBtn.addActionListener(guiListener);
	}
	
	@Override
	public void addComponents(){
		if(isInstantiated) return;
		
		JLabel lbl1 = new JLabel("<html><center><p>Select directories and the program will check "+
		                         "those and then put links those directories with existing post.txt"+
								 "</p></center></html>",JLabel.CENTER);
		//lbl1 add & constraints
		subPanel.add(lbl1,new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.CENTER,
												 GridBagConstraints.HORIZONTAL,new Insets(10,43,3,53),0,0));
		
		//It's very important to set the weightx and weighty of a textarea in a gridbaglayout container.
		//Unlike textfields, text area's size is hard to adjust especially if there's a jlabel on top of
		//the text area.
		JScrollPane jsp1 = new JScrollPane(jta);
		jsp1.setPreferredSize(new Dimension(300,200));
		subPanel.add(jsp1,new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,
													   GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
		//
		JPanel pnl1 = new JPanel(new GridBagLayout());
		
		JPanel pLinksBtnPnl = new JPanel(new GridBagLayout());
		btns[0] = new JButton("Put Links");
		pLinksBtnPnl.add(btns[0], new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							          GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
		
		pnl1.add(pLinksBtnPnl,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
								  GridBagConstraints.NONE,new Insets(5,0,10,10),0,0));
								  
		JPanel clearBtnPnl = new JPanel(new GridBagLayout());
		btns[1] = new JButton("Clear");
		clearBtnPnl.add(btns[1], new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							         GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
		
		pnl1.add(clearBtnPnl,new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,
								 GridBagConstraints.NONE,new Insets(5,0,10,15),0,0));
														 
		subPanel.add(pnl1,new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.CENTER,
												 GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0));
		//
		JLabel lbl2 = new JLabel("<html><center><p>Selection Type</p></center></html>",JLabel.CENTER);
		lbl2.setFont(new Font(DefaultFont.getFontName(),DefaultFont.getStyle(),14));
		//
		JPanel pnl2 = new JPanel(new GridBagLayout());
		JPanel pnl3 = new JPanel(new GridLayout(2,1));
		ButtonGroup btnGrp1 = new ButtonGroup();
		rbs[0] = new JRadioButton("Single");
		rbs[1] = new JRadioButton("Multiple");
		btnGrp1.add(rbs[0]);
		btnGrp1.add(rbs[1]);
		rbs[1].setSelected(true);
		pnl2.add(rbs[0]);
		pnl2.add(rbs[1]);
		//
		pnl2.add(lbl2,new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.CENTER,
											 GridBagConstraints.NONE,new Insets(0,5,0,6),0,0));
		//
		pnl3.add(rbs[0]);
		//
		pnl3.add(rbs[1]);
		//
		pnl2.add(pnl3,new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.CENTER,
														   GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
		//
		subPanel.add(pnl2,new GridBagConstraints(0,4,1,1,1,0,GridBagConstraints.CENTER,
														GridBagConstraints.HORIZONTAL,new Insets(5,5,5,21),0,0));
		//
		subPanel.add(cb,new GridBagConstraints(0,5,1,1,1,0,GridBagConstraints.CENTER,
													GridBagConstraints.NONE,new Insets(0,5,5,8),0,0));
													
		//Resize
		Dimension dm = new Dimension();
		dm.setSize(100, 25);
		for(JButton btn : btns)
			btn.setPreferredSize(dm);
	}
	
	@Override
	public void setEnabledMainComps(boolean state){
		cb.setEnabled(state);
		
		for(JButton btn : btns)
			btn.setEnabled(state);
		for(JRadioButton rb : rbs)
			rb.setEnabled(state);
	}
	
	public int getSubPnlBtnsLn(){ return btns.length; }
	public JButton getSubPnlBtn(int index){ return btns[index]; }
	public JCheckBox getSubPnlCb(){ return cb; }
	public JTextArea getSubPnlJta(){ return jta; }
	public JRadioButton getSubPnlRb(int index){ return rbs[index]; }
	
	@Override
	public JPanel getPanel(){ return subPanel; }
}
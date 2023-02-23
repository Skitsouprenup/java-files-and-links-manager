package gui.subpanels;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import gui.CharacterLimiter;
import gui.DefaultFont;
import gui.events.FileGenPEvents;
import fileoperations.FileOperations;

public class FileGenPanel extends FileGenPEvents implements ISubPanel{
	private final JPanel subPanel;
	//subPnl1's buttons
	private final JButton[] btns;
	//subPnl1's textfield
	private final JTextField txtf;
	//subPnl1's combobox
	private final JComboBox<String> jcb;
	//subPnl1's checkbox
	private final JCheckBox[] cbs; 
	//
	JLabel infoLbl;
	
	private static FileGenPanel instance;
	private static boolean isInstantiated;
	
	private FileGenPanel(JPanel subPanel, JFrame mainFrame){
		this.subPanel = subPanel;
		btns = new JButton[3];
		cbs = new JCheckBox[2];
		txtf = new JTextField();
		String[] postTypes = {"Books","Books(simplified)","Tutorials"};
		jcb = new JComboBox<String>(postTypes);
		
		infoLbl = new JLabel("<html><a style='text-decoration:underline;white-space:nowrap;'>"+
							 "What is this?</a></html>");
		infoLbl.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseClicked(MouseEvent e){
				JOptionPane.showMessageDialog(mainFrame,FileOperations.instantiate().getColKeywords(),
											  "Collection Keywords", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
	}
	
	public static FileGenPanel instantiate(JPanel subPanel,ActionListener btnListener, JFrame mainFrame){
		if(!isInstantiated){
			instance = new FileGenPanel(subPanel, mainFrame);
			instance.addComponents();
			instance.addBtnActListener(btnListener);
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
	public void addGuiRelatedActListener(ActionListener guiListener){}
	
	/*Add components for subPnls[0] and set their gridBagConstraints*/
	public void addComponents(){
		if(isInstantiated) return;
		
		JLabel lbl1 = new JLabel("<html><center><p>Select directories and the program will check " +
		                        "those and then create post.txt in each directory.</p></center></html>",JLabel.CENTER);
		//spnl1_lbl1 add & constraints
		subPanel.add(lbl1,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
													   GridBagConstraints.HORIZONTAL,new Insets(10,42,3,52),0,0));
		//
		JLabel lbl2 = new JLabel("<html><center><p>Post Type</p></center></html>",JLabel.CENTER);
		lbl2.setFont(new Font(DefaultFont.getFontName(),DefaultFont.getStyle(),14));
		//lbl2 add & constraints
		subPanel.add(lbl2,new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.CENTER,
												 GridBagConstraints.HORIZONTAL,new Insets(3,0,5,20),0,0));
		//jcb add & constraints
		subPanel.add(jcb,new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.CENTER,
												GridBagConstraints.NONE,new Insets(0,0,0,15),0,0));
		//
		JPanel pnl1a = new JPanel(new GridBagLayout());
		cbs[0] = new JCheckBox("Use Collection Template",true);
		pnl1a.add(cbs[0],new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
															GridBagConstraints.NONE,new Insets(0,5,0,0),0,0));
		//
		infoLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		pnl1a.add(infoLbl,new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,
															 GridBagConstraints.NONE,new Insets(0,0,0,5),0,0));
		//
		subPanel.add(pnl1a,new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.CENTER,
														 GridBagConstraints.NONE,new Insets(0,-20,0,0),0,0));
		//
		JPanel cPostPnl = new JPanel(new GridBagLayout());
		btns[0] = new JButton("Create post.txt");
		cPostPnl.add(btns[0], new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							         GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
		//sPnl1Btns[0] add & constraints
		subPanel.add(cPostPnl,new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.CENTER,
														 GridBagConstraints.HORIZONTAL,new Insets(5,0,5,20),0,0));
		//JLabel.CENTER center's jlabel in its parent container
		//<center> tag centers jlabel's content within its boundary
		//<p> tag formats text in paragraph form.
		JLabel lbl3 = new JLabel("<html><center><p>Select directories and the program will check " +
								 "those and then create dummy_file in each selected directories that"+
								 " has files with a total of an amount that is less than the threshold."+
								 "</p></center></html>",JLabel.CENTER);
		
		//lbl3 add & constraints
		subPanel.add(lbl3,new GridBagConstraints(0,5,1,1,0,0,GridBagConstraints.CENTER,
												 GridBagConstraints.HORIZONTAL,new Insets(0,40,10,50),0,0));
			
		//
		JLabel lbl4 = new JLabel("<html><center><p>Threshold</p></center></html>",JLabel.CENTER);
		lbl4.setFont(new Font(DefaultFont.getFontName(),DefaultFont.getStyle(),14));
		//lbl4 add & constraints
		subPanel.add(lbl4,new GridBagConstraints(0,6,1,1,0,0,GridBagConstraints.CENTER,
												 GridBagConstraints.NONE,new Insets(3,0,5,20),0,0));
		//
		JPanel pnl1 = new JPanel(new GridLayout(1,1));
		pnl1.setSize(new Dimension(100,50));
		txtf.setDocument(new CharacterLimiter(2));
		txtf.setText("5");
		JLabel lbl5 = new JLabel("mb");
			
		pnl1.add(txtf);
		pnl1.add(lbl5);
		
		subPanel.add(pnl1,new GridBagConstraints(0,7,1,1,1,0,GridBagConstraints.CENTER,
												 GridBagConstraints.NONE,new Insets(0,0,0,5),10,0));
												 
		JPanel cDummPnl = new JPanel(new GridBagLayout());
		btns[1] = new JButton("Create dummy_file");
		cDummPnl.add(btns[1], new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							         GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
		//sPnl1Btns[1] constraints
		subPanel.add(cDummPnl,new GridBagConstraints(0,8,1,1,0,0,GridBagConstraints.CENTER,
														 GridBagConstraints.HORIZONTAL, new Insets(5,0,5,20),0,0));
		//
		JLabel lbl6 = new JLabel("<html><center><p>Delete dummy_file/s in selected directories.</p>"+
								 "</center></html>",JLabel.CENTER);
								 
		JPanel dDummPnl = new JPanel(new GridBagLayout());
		btns[2] = new JButton("Delete dummy_file");
		dDummPnl.add(btns[2], new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							         GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
		//
		subPanel.add(lbl6,new GridBagConstraints(0,9,1,1,0.5,0,GridBagConstraints.CENTER,
												 GridBagConstraints.HORIZONTAL,new Insets(5,36,5,55),0,0));
		subPanel.add(dDummPnl,new GridBagConstraints(0,10,1,1,0.5,0,GridBagConstraints.CENTER,
														 GridBagConstraints.HORIZONTAL,new Insets(5,0,5,20),0,0));
		//
		JPanel pnl2 = new JPanel(new GridBagLayout());
		cbs[1] = new JCheckBox("Include Sub-Directories",false);
		pnl2.add(cbs[1],new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
														   GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
		//
		subPanel.add(pnl2,new GridBagConstraints(0,11,1,1,0.5,0,GridBagConstraints.CENTER,
														GridBagConstraints.HORIZONTAL,new Insets(0,0,0,15),0,0));
		
		//Resize
		Dimension dm = new Dimension();
		dm.setSize(170, 25);
		for(JButton btn : btns)
			btn.setPreferredSize(dm);
	}
	
	@Override
	public void setEnabledMainComps(boolean state){
		jcb.setEnabled(state);
		txtf.setEnabled(state);
		
		for(JCheckBox cb : cbs)
			cb.setEnabled(state);
		for(int i = 0; i < btns.length; i++){
			if(i != 3)
				btns[i].setEnabled(state);
		}
	}
	
	public int getSubPnlBtnsLn(){ return btns.length; }
	public JButton getSubPnlBtn(int index){ return btns[index]; }
	public JCheckBox getSubPnlCb(int index){ return cbs[index]; }
	public JComboBox<String> getSubPnlJcb(){ return jcb; }
	public JTextField getSubPnlTf(){ return txtf; }
	
	@Override
	public JPanel getPanel(){ return subPanel; }
}
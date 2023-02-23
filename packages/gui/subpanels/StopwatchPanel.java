package gui.subpanels;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import gui.DefaultFont;
import gui.CharacterLimiter;
import gui.events.StopWatchPEvents;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Color;

public class StopwatchPanel extends StopWatchPEvents implements ISubPanel{
	private final JPanel subPanel;
	
	private final JButton[] btns;
	private final JCheckBox cb;
	private final JLabel lbl[];
	private final JTextField[] txt;
	private JPanel pnl2;
	
	private static StopwatchPanel instance;
	private static boolean isInstantiated;
	
	private StopwatchPanel(JPanel subPanel){
		this.subPanel = subPanel;
		btns = new JButton[3];
		lbl = new JLabel[3];
		txt = new JTextField[2];
		cb = new JCheckBox("No Interruptions");
		cb.setSelected(false);
		pnl2 = new JPanel(new GridBagLayout());
	}
	
	public static StopwatchPanel instantiate(JPanel subPanel,ActionListener btnListener,
											 ActionListener guiListener){
		if(!isInstantiated){
			instance = new StopwatchPanel(subPanel);
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
		
		cb.addActionListener(guiListener);
	}
	
	@Override
	public void addComponents(){
		if(isInstantiated) return;
		//
		JLabel lbl1 = new JLabel("<html><center><p>Stopwatch</p></center></html>",JLabel.CENTER);
		lbl1.setFont(new Font(DefaultFont.getFontName(),DefaultFont.getStyle(),14));
		subPanel.add(lbl1,new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.CENTER,
												 GridBagConstraints.HORIZONTAL,new Insets(5,0,5,4),0,0));
		//
		subPanel.add(cb,new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.CENTER,
											   GridBagConstraints.NONE,new Insets(5,5,5,6),0,0));
		//
		lbl[0] = new JLabel("<html><center><p>Note: Stopwatch will stop "+
									  "if you select any operation.</p></center></html>",JLabel.CENTER);
		subPanel.add(lbl[0],new GridBagConstraints(0,3,1,1,1,0,GridBagConstraints.CENTER,
												   GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
		
		txt[0] = new JTextField(2);
		txt[0].setPreferredSize(new Dimension(30,25));
		txt[0].setDocument(new CharacterLimiter(2));
		txt[0].setText("3");
		txt[1] = new JTextField(2);
		txt[1].setPreferredSize(new Dimension(30,25));
		txt[1].setDocument(new CharacterLimiter(2));
		txt[1].setText("0");

		//Add transparent border to fix shrinking textboxes width
		//in stopwatch panel for ubuntu.
		//I assume the reason for this problem is that the subpanel has 
		//an overlap somewhere. This is not a problem on windows.
		Border transparentBorder = 
			BorderFactory.createLineBorder(new Color(255, 255, 255, 1));
		subPanel.setBorder(transparentBorder);

		JLabel lbl3 = new JLabel("minutes");
		JLabel lbl4 = new JLabel("seconds");
		JPanel pnl1 = new JPanel(new GridBagLayout());
		//
		pnl1.add(txt[0],new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
											 GridBagConstraints.NONE,new Insets(5,1,5,1),0,0));
		//
		pnl1.add(lbl3,new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,
											   GridBagConstraints.NONE,new Insets(5,1,5,7),0,0));
		//
		pnl1.add(txt[1],new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.CENTER,
											 GridBagConstraints.NONE,new Insets(5,7,5,1),0,0));
		//
		pnl1.add(lbl4,new GridBagConstraints(3,0,1,1,0,0,GridBagConstraints.CENTER,
											   GridBagConstraints.NONE,new Insets(5,1,5,1),0,0));
		//
		subPanel.add(pnl1,new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.CENTER,
												 GridBagConstraints.NONE,new Insets(1,1,5,0),0,0));
		//
		lbl[1] = new JLabel("Laps: 0");
		lbl[2] = new JLabel("Elapsed Time: 0");
		//
		subPanel.add(lbl[1],new GridBagConstraints(0,5,1,1,0,0,GridBagConstraints.CENTER,
												   GridBagConstraints.NONE,new Insets(2,0,5,0),0,0));
			
		subPanel.add(lbl[2],new GridBagConstraints(0,6,1,1,0,0,GridBagConstraints.CENTER,
												   GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));											
		//
		btns[0] = new JButton("Start");
		//
		subPanel.add(btns[0],new GridBagConstraints(0,7,1,1,1,0,GridBagConstraints.CENTER,
													GridBagConstraints.NONE,new Insets(5,0,5,5),0,0));
		
		btns[1] = new JButton("Pause");
		btns[2] = new JButton("Stop");
		
		pnl2.add(btns[1],new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
												GridBagConstraints.NONE,new Insets(1,1,1,4),0,0));
			
		pnl2.add(btns[2],new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,
												GridBagConstraints.NONE,new Insets(1,4,1,1),0,0));
														
		subPanel.add(pnl2,new GridBagConstraints(0,8,1,1,0,0,GridBagConstraints.CENTER,
												 GridBagConstraints.NONE,new Insets(1,1,1,10),0,0));
		pnl2.setVisible(false);
		
	}
	
	//Stopwatch UI has unique behavior so I didn't put 
	//any code here. Enabling/Disabling stopwatch
	//components depends on the state of "no interruptions"
	//checkbox of this panel
	@Override
	public void setEnabledMainComps(boolean state){}
	
	//enable/disable stopwatch components. Stopwatch
	//components can be disabled if "no interruptions" checkbox
	//is not checked and clients select an operation, which 
	//also stops the stopwatch and resets its values.
	public void setEnabledMainCompsForStpWtch(boolean state){
		cb.setEnabled(state);
		for(JButton btn : btns)
			btn.setEnabled(state);
		
		for(JTextField txtf : txt)
			txtf.setEnabled(state);
	}
	
	public void stopWatchStartStop(boolean state){
		//state == true: stopwatch will start
		//state == false: stopwatch will stop
		boolean state2 = false;
		pnl2.setVisible(state);
		
		state2 = (state == true) ? false : true;
		
		btns[0].setVisible(state2);
		txt[0].setEditable(state2);
		txt[1].setEditable(state2);
	}
	
	public int getSubPnlBtnsLn(){ return btns.length; }
	public JButton getSubPnlBtn(int index){ return btns[index]; }
	public JCheckBox getSubPnlCb(){ return cb; }
	public JLabel getSubPnlLbl(int index){ return lbl[index]; }
	public JTextField getSubPnlTxt(int index){ return txt[index]; }
	public JPanel getSubPnlPnl2(){ return pnl2; }
	
	@Override
	public JPanel getPanel(){ return subPanel; }
}
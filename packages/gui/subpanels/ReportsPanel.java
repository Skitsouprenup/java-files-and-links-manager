package gui.subpanels;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import gui.events.ReportsPEvents;

public class ReportsPanel extends ReportsPEvents implements ISubPanel{
	private JPanel subPanel;
	
	//children panel of subPnl2 that holds 
	//two buttons and one label
	private final JPanel pnl1;
	//text area in subPnl2 that displays program's
	//events
	private final JEditorPane jedit;
	//subPnl2's buttons
	private final JButton[] btns;
	
	private static ReportsPanel instance;
	private static boolean isInstantiated;
	
	private ReportsPanel(JPanel subPanel){
		this.subPanel = subPanel;
		btns = new JButton[3];
		jedit = new JEditorPane("text/html","No Selected Operation...");
		jedit.setEditable(false);
		pnl1 = new JPanel(new GridBagLayout());
	}
	
	public static ReportsPanel instantiate(JPanel subPanel,ActionListener btnListener){
		if(!isInstantiated){
			instance = new ReportsPanel(subPanel);
			instance.addComponents();
			instance.addBtnActListener(btnListener);
			isInstantiated = true;
		}
		return instance;
	}
	
	//another way to get ReportsPanel singleton instance
	//returns null if ReportsPanel is not instantiated
	public static ReportsPanel getInstance(){
		if(!isInstantiated)
			return null;
		return instance;
	}
	
	@Override
	public void addBtnActListener(ActionListener btnListener){
		if(isInstantiated) return;
		
		for(JButton button : btns)
			button.addActionListener(btnListener);
	}
	
	//This panel doesn't support gui-related-events. However, the
	//approve and cancel buttons of this panel are hiding the panel
	//where they reside. Hiding GUI component is a gui-related-event.
	//
	//I didn't separate the gui-related-event of this panel from the
	//program's events because separating them in this panel would
	//complicate the implementation of this panel.
	@Override
	public void addGuiRelatedActListener(ActionListener guiListener){}
	
	/*Add components for subPnls[1] and set their gridBagConstraints*/
	@Override
	public void addComponents(){
		if(isInstantiated) return;
		
		//reference of a technique to remove the default word wrap of JEditorPane
		//https://stackoverflow.com/questions/8960732/how-to-turn-off-word-wrap-in-jeditorpane
		JScrollPane jsp1 = new JScrollPane(jedit);
		jsp1.setPreferredSize(new Dimension(300,200));
		//
		subPanel.add(jsp1,new GridBagConstraints(0,0,3,1,1,1,GridBagConstraints.CENTER,
												 GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
		//
		btns[0] = new JButton("Clear");
		//
		subPanel.add(btns[0],new GridBagConstraints(1,1,3,1,0,0,GridBagConstraints.CENTER,
												    GridBagConstraints.NONE,new Insets(5,5,10,15),0,0));
		//
		JLabel lbl1 = new JLabel("Commence Operation?",JLabel.CENTER);
		btns[1] = new JButton("Yes");
		btns[2] = new JButton("No");
		//
		pnl1.add(lbl1,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
											 GridBagConstraints.HORIZONTAL,new Insets(0,0,0,10),0,0));
		//
		pnl1.add(btns[1],new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,
													 GridBagConstraints.NONE,new Insets(0,10,0,10),0,0));
		//
		pnl1.add(btns[2],new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.CENTER,
													 GridBagConstraints.NONE,new Insets(0,10,0,10),0,0));
		//
		subPanel.add(pnl1,new GridBagConstraints(0,2,3,1,0,0,GridBagConstraints.CENTER,
												 GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0));
		pnl1.setVisible(false);
		
	}
	
	@Override
	public void setEnabledMainComps(boolean state){
		btns[0].setEnabled(state);
	}
	
	public int getSubPnlBtnsLn(){ return btns.length; }
	public JButton getSubPnlBtn(int index){ return btns[index]; }
	public JPanel getSubPnlPnl(){ return pnl1; }
	public JEditorPane getSubPnlep(){return jedit; }
	
	@Override
	public JPanel getPanel(){ return subPanel; }
}
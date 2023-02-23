package gui.dialogs.linkchecker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import gui.dialogs.subpanels.prefs_subpanel.AlternativeHost;

public class LinkCheckerDialog extends LinkCheckerDialogFunctions{
	
	public LinkCheckerDialog(JEditorPane ep, Window parent){
		super(ep, parent);
		
		addComponents();
		addActions(bal, pal);
		if(!loadAltHostFile())
			suppHostCb.addItem(new AlternativeHost("None"));
	}
	
	private void addComponents(){
		
		altHostPnl = new JPanel(new GridBagLayout());
		//altHostPnl.setPreferredSize(new Dimension(250,20));
		altHostPane = new JScrollPane(altHostPnl);
		
		JPanel dirPnl = new JPanel(new GridBagLayout());
		dirPnl.setPreferredSize(new Dimension(301,450));
		dirPnl.setBorder(BorderFactory
						  .createTitledBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.BLACK),
						  "Directory List", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		
		JPanel hostPnlCb = new JPanel(new GridBagLayout());
		hostPnlCb.setPreferredSize(new Dimension(300,225));
		
		JPanel hostPnlTop = new JPanel(new GridBagLayout());
		hostPnlTop.setPreferredSize(new Dimension(300,225));
		hostPnlTop.setBorder(BorderFactory.createEmptyBorder());
		
		JPanel hostPnlBot = new JPanel(new GridBagLayout());
		hostPnlBot.setPreferredSize(new Dimension(300,225));
		hostPnlBot.setBorder(BorderFactory.createEmptyBorder());
		
		JPanel hostPnl = new JPanel(new GridBagLayout());
		hostPnl.setPreferredSize(new Dimension(300,450));
		hostPnl.setBorder(BorderFactory
						  .createTitledBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.BLACK),
						  "Supported Host/s", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		
		JPanel linksPnl = new JPanel(new GridBagLayout());
		linksPnl.setPreferredSize(new Dimension(301,450));
		linksPnl.setBorder(BorderFactory
						      .createTitledBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.BLACK),
							  "Link/s", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		
		JScrollPane linksPane = new JScrollPane(linksTable);
		linksPane.setPreferredSize(new Dimension(285, 385));
		linksPane.setBorder(BorderFactory.createEmptyBorder());
		
		JScrollPane dirListPane = new JScrollPane(selectedDirList);
		dirListPane.setPreferredSize(new Dimension(285, 385));
		dirListPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		JScrollPane searchListPane = new JScrollPane(searchForList);
		searchListPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		Dimension dm = new Dimension();
		dm.setSize(160, 25);
		
		selectDirBtn.setPreferredSize(dm);
		checkLinksBtn.setPreferredSize(dm);
		clearLinksBtn.setPreferredSize(dm);
		searchForBtn.setPreferredSize(dm);
		suppHostCb.setPreferredSize(dm);
		
		rClickPopup.add(remove);
		rClickPopup.add(removeAll);
		
		enableListPopup(selectedDirList);
		enableListPopup(searchForList);
		
		dirPnl.add(dirListPane, new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.NORTH,
							        GridBagConstraints.NONE,new Insets(3,3,3,3),0,0));
									
		JPanel selectDirBtnPnl = new JPanel(new GridBagLayout());
		selectDirBtnPnl.add(selectDirBtn, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
							            GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
		dirPnl.add(selectDirBtnPnl, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.SOUTH,
							            GridBagConstraints.HORIZONTAL,new Insets(3,3,3,3),0,0));
		
		hostPnlCb.add(suppHostCb, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							        GridBagConstraints.NONE,new Insets(5,5,10,3),0,0));
		hostPnlTop.add(altHostLbl, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.CENTER,
							           GridBagConstraints.NONE,new Insets(3,3,3,3),0,0));
		hostPnlTop.add(altHostPane, new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER,
							            GridBagConstraints.BOTH,new Insets(3,3,3,3),0,0));
		hostPnlBot.add(searchForBtn, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							             GridBagConstraints.NONE,new Insets(3,3,3,3),0,0));
		hostPnlBot.add(searchForLbl, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.CENTER,
							             GridBagConstraints.NONE,new Insets(10,3,3,3),0,0));
		hostPnlBot.add(searchListPane, new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER,
							               GridBagConstraints.BOTH,new Insets(3,3,3,3),0,0));
		
		hostPnl.add(hostPnlCb, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							        GridBagConstraints.HORIZONTAL,new Insets(5,5,10,3),0,0));
		hostPnl.add(hostPnlTop, new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,
							        GridBagConstraints.BOTH,new Insets(1,1,1,1),0,0));
		hostPnl.add(hostPnlBot, new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER,
							        GridBagConstraints.BOTH,new Insets(1,1,1,1),0,0));
		
		linksPnl.add(linksPane, new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.NORTH,
							        GridBagConstraints.NONE,new Insets(3,3,3,3),0,0));
									
		JPanel clearLinksBtnPnl = new JPanel(new GridBagLayout());
		clearLinksBtnPnl.add(clearLinksBtn, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
							                    GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
		linksPnl.add(clearLinksBtnPnl, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.SOUTH,
							               GridBagConstraints.HORIZONTAL,new Insets(3,3,3,3),0,0));
		
		mainPanel.add(dirPnl, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTH,
							      GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
		mainPanel.add(linksPnl, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.CENTER,
							        GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
		mainPanel.add(hostPnl, new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.CENTER,
							       GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
								   
		JPanel checkLinksBtnPnl = new JPanel(new GridBagLayout());
		checkLinksBtnPnl.setBorder(BorderFactory.createMatteBorder(1,0,0,0, Color.BLACK));
		checkLinksBtnPnl.add(checkLinksBtn, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
							                GridBagConstraints.NONE,new Insets(10,5,5,12),0,0));
		
		mainPanel.add(checkLinksBtnPnl, new GridBagConstraints(0,2,3,1,1,0,GridBagConstraints.CENTER,
							                GridBagConstraints.HORIZONTAL,new Insets(2,1,1,1),0,0));
		mainFrame.add(mainPanel);
		
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(parent);
		mainFrame.setVisible(true);
	}
	
	private void addActions(ButtonsActionListener bal, PopupActionListener pal){
		
		selectDirBtn.addActionListener(bal);
		checkLinksBtn.addActionListener(bal);
		clearLinksBtn.addActionListener(bal);
		searchForBtn.addActionListener(bal);
		suppHostCb.addActionListener(bal);
		
		remove.addActionListener(pal);
		removeAll.addActionListener(pal);
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
	
}
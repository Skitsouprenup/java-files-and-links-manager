package gui.dialogs.subpanels.prefs_subpanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StopWatchSubPnl{
	
	private final JPanel panel;
	private final JLabel notSupportedLbl;
	
	public StopWatchSubPnl(){
		
		panel = new JPanel(new GridBagLayout());
		notSupportedLbl = new JLabel("Not Supported for the Time Being.",SwingConstants.LEFT);
		
		panel.add(notSupportedLbl, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
	}
	
	public JPanel getPanel(){
		return panel;
	}
}
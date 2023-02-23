package gui.ui_design;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainPanelDesign {
	
	public MainPanelDesign(JPanel mainPanel, JPanel... subPanels) {
		mainPanel.setBackground(new Color(166, 166, 166));
		
		designPanels(subPanels);
	}
	
	private void designPanels(JPanel... subPanels){
		
		for(JPanel panel : subPanels){
			panel.setBackground(new Color(0,0,0,1));
				
			for(Component comp : panel.getComponents())
				if(comp instanceof JPanel)
					designPanels((JPanel)comp);
				else 
					if(!(comp instanceof JTextField))
						comp.setBackground(Color.WHITE);
					
		}
	}
}
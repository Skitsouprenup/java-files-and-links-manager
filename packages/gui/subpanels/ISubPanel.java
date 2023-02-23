package gui.subpanels;

import javax.swing.JPanel;
import java.awt.event.ActionListener;

interface ISubPanel{
	
	//-----//
	//It is preferrable for these methods to be invoked once
	//when these methods are implemented by concrete classes.
	//Panel classes in subpanels subpackage are all singleton
	//classes and these methods set the core components and 
	//events of one of the subpanels which are preferrable or 
	//required to be set once.
	//To block users from invoking core components multiple times
	//check the instance value or the isIntantiated value.
	//All subpanels have those variables 'cause those variables
	//are part of singleton class' components.
	//e.g 
	//if(isInstantiated) return;
	//if(instance != null) return;
	void addBtnActListener(ActionListener btnListener);
	void addGuiRelatedActListener(ActionListener guiListener);
	void addComponents();
	//------//
	void setEnabledMainComps(boolean state);
	JPanel getPanel();
}
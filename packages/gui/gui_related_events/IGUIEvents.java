package gui.gui_related_events;

interface IGUIEvents{
	
	//The overriding method of this abstract method is
	//used to verify events in GUIEventSource.java
	boolean checkSource(Object source, Object component);
	//The overriding method of this abstract method is
	//used to fire events in GUIEventSource.java
	void fireEvent(Object source, Object component);
}
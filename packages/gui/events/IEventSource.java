package gui.events;

interface IEventSource{
	
	//The overriding methods of these abstract methods are
	//used to verify events in GUIEventSource.java
	boolean checkSource(Object source, Object component);
	boolean checkGUIEvtSource(Object source, Object component);
}
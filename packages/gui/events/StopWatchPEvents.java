package gui.events;

import static main.UtilityClasses.IntObj;
import static main.UtilityClasses.LongObj;

import gui.gui_related_events.StopWatchPGUIEvents;
import gui.subpanels.StopwatchPanel;
import main.Stopwatch;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class StopWatchPEvents extends StopWatchPGUIEvents implements IEventSource{
	private Stopwatch stopwatch;
	
	private final IntObj laps = new IntObj(0);
	//Elapsed Time in seconds
	private final LongObj eTime = new LongObj(0);
	
	//This checkSource() method overrides checkSource() in
	//IEventSource
	@Override
	public boolean checkSource(Object source, Object component){
		StopwatchPanel swp = checkPanelType(component);
		
		for(int i = 0 ; i < swp.getSubPnlBtnsLn(); i++)
			if(source == swp.getSubPnlBtn(i)) return true;
		return false;
	}
	
	@Override
	public boolean checkGUIEvtSource(Object source, Object component){
		//This checkSource() method invokes checkSource() in 
		//StopWatchPGUIEvents.checkSource() in 
		//StopWatchPGUIEvents is concrete. Thus, in my opinion,
		//hides checkSource() in IEventSource.
		//This invocation makes sense because you can't
		//invoke an abstract method.
		return super.checkSource(source,component);
	}
	
	public void openStopWatch(Object source,JFrame mainFrame,StopwatchPanel swp){
		int minutes = 0,seconds = 0;
		try {
			boolean isError = false;
			minutes = Integer.valueOf(swp.getSubPnlTxt(0).getText());
			seconds = Integer.valueOf(swp.getSubPnlTxt(1).getText());
			int minToSec = minutes * 60;
			int secSum = minToSec + seconds;
			minutes = secSum / 60;
			seconds = secSum % 60;
				
			if(minutes > 99){
				JOptionPane.showMessageDialog(mainFrame,"Maximum minutes can't be greater than 99",
											  "Maximum Minutes Exceeded",JOptionPane.ERROR_MESSAGE);
			    isError = true;
			}
			
			if(minutes < 0 || seconds < 0){
				JOptionPane.showMessageDialog(mainFrame,"Negative minutes or seconds are not allowed!",
											  "No Negative Numbers",JOptionPane.ERROR_MESSAGE);
				isError = true;
			}
				
			if(minutes == 0 && seconds == 0){
				JOptionPane.showMessageDialog(mainFrame,"Please, add some minutes or seconds",
											  "Add minutes or seconds",JOptionPane.INFORMATION_MESSAGE);
				isError = true;
			}
				
			if(isError) return;
		}
		catch(NumberFormatException f){
			//f.printStackTrace();
			JOptionPane.showMessageDialog(mainFrame,"Minutes or seconds are not numbers",
										  "Not A Number",JOptionPane.ERROR_MESSAGE);
			return;
		}
			
		//start
		if(source == swp.getSubPnlBtn(0)){
			
			if(minutes > 0 || seconds > 0){
				swp.stopWatchStartStop(true);
				
				stopwatch = new Stopwatch(laps,eTime,minutes,seconds);
				stopwatch.start();
			}
		}
		//pause
		else if(source == swp.getSubPnlBtn(1)){
				
			if(swp.getSubPnlBtn(1).getText().equals("Pause")){
				swp.getSubPnlBtn(1).setText("Resume");
				if(stopwatch != null)
					stopwatch.stopThread();
			}
			else if(swp.getSubPnlBtn(1).getText().equals("Resume")){
				swp.getSubPnlBtn(1).setText("Pause");
				
				if(minutes > 0 || seconds > 0){
					stopwatch = new Stopwatch(laps,eTime,minutes,seconds);
					stopwatch.start();
				}
			}
		}
		//stop
		else if(source == swp.getSubPnlBtn(2)){
			swp.getSubPnlTxt(0).setText("3");
			swp.getSubPnlTxt(1).setText("0");
			if(swp.getSubPnlBtn(1).getText().equals("Resume"))
					swp.getSubPnlBtn(1).setText("Pause");
			
			swp.stopWatchStartStop(false);
			
			if(stopwatch != null)
				stopwatch.stopThread();
		}
	}
	
	public Stopwatch getStopWatch(){ return stopwatch; }
	
}
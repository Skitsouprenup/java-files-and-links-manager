package gui;

import java.awt.EventQueue;

public interface StopwatchInterface{
	
	static void displayLaps(int laps){

		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run(){
				GUIComponents.instantiate().stopWatchP().getSubPnlLbl(1).setText("Laps: "+laps);
			}
		});
	}
	
	static void diplayTime(String elapsedTime,long remainingMin,long remainingSec){
		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run(){
				GUIComponents.instantiate().stopWatchP().getSubPnlLbl(2).setText("Elapsed Time: "+elapsedTime);
				GUIComponents.instantiate().stopWatchP().getSubPnlTxt(0).setText(String.valueOf(remainingMin));
				GUIComponents.instantiate().stopWatchP().getSubPnlTxt(1).setText(String.valueOf(remainingSec));
			}
		});
	}
	
	static void resetMinSec(){
		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run(){
				GUIComponents.instantiate().stopWatchP().getSubPnlTxt(0).setText("3");
				GUIComponents.instantiate().stopWatchP().getSubPnlTxt(1).setText("0");
			}
		});
	}
	
	static void stopWatchStartStop(boolean state){
		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run(){
				GUIComponents.instantiate().stopWatchP().stopWatchStartStop(state);
			}
		});
	}
}
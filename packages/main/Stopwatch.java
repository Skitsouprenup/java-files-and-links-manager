package main;

import static main.UtilityClasses.*;
import gui.StopwatchInterface;

//Simple stopwatch in seconds resolution created by Kayle V. Lazatin
//This stopwatch is a bit off by milliseconds due to discarded decimal value
//due to conversion from millis to seconds and seconds to millis.
//Also, pausing this stopwatch discards some elapsed milliseconds in-between seconds.
public class Stopwatch extends Thread{
	private IntObj laps;
	private LongObj eTime;
	private int minutes = 0;
	private int seconds = 0;
	private long startTimeMillis = 0L;
	private long startTimeSeconds = 0L;
	private volatile StopWatchState swState = null;
	
	public Stopwatch(IntObj laps,LongObj eTime,
					 int minutes,int seconds){
		this.minutes = minutes;
		this.seconds = seconds;
		initialize(laps,eTime);
	}
	
	public StopWatchState getStopWatchState(){ return swState; }
	
	private void initialize(IntObj laps,LongObj eTime){
		this.laps = laps;
		this.eTime = eTime;
		int minToSec = minutes * 60;
		//add all seconds
		startTimeSeconds = seconds + minToSec;
		//convert seconds to millis
		startTimeMillis = startTimeSeconds * 1000L;
	}
	
	@Override
	public void run(){
		swState = StopWatchState.ONGOING;
		
		long start = System.nanoTime();
		long eTimeMillis = 0L;
		long prev_e_time = 0L;
		long prev_etimesec = 0L;
		
		String elapsedTime = null;
		while(swState == StopWatchState.ONGOING){
			try{
				long timeDurNanos = System.nanoTime() - start;
				eTimeMillis = timeDurNanos/1_000_000L;
				long eTimeSec = eTimeMillis / 1000L;
				prev_e_time = eTimeSec - prev_etimesec;
				prev_etimesec = (eTimeSec > prev_etimesec) ? eTimeSec : prev_etimesec;
				eTime.setValue(eTime.longValue()+prev_e_time);
				
				long elapsedHour = eTime.longValue() / 60 / 60;
				long elapsedMin = eTime.longValue() / 60;
				long elapsedSec = eTime.longValue() % 60;
				
				long rTimeSec = startTimeSeconds - eTimeSec;
				long remainingMin = rTimeSec / 60;
				long remainingSec = rTimeSec % 60;
				
				if(eTimeMillis >= startTimeMillis){
					laps.setValue(laps.intValue()+1);
					StopwatchInterface.displayLaps(laps.intValue());
					swState = StopWatchState.FINISHED;
				}
				
				if(elapsedHour != 0)
					elapsedTime = elapsedHour + "h " + (elapsedMin % 60) + "m " + elapsedSec + "s";
				else if(elapsedMin != 0)
					elapsedTime = elapsedMin + "m " + elapsedSec + "s";
				else
					elapsedTime = elapsedSec + "s";
				
				StopwatchInterface.diplayTime(elapsedTime,remainingMin,remainingSec);
				
				if(swState == StopWatchState.FINISHED){
					StopwatchInterface.stopWatchStartStop(false);
					StopwatchInterface.resetMinSec();
					break;
				}
				Thread.sleep(50);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public void stopThread(){
		swState = StopWatchState.STOPPED;
	}
}
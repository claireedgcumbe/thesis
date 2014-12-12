//import java.lang.*;
package minicon;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
public class RapTimer{
	//this class is a wrapper to whatever timing devices I choose to use, currently
	//it will be based on the Calendar getTimeInMillis, but now I can 
	//change it whenever I like.... mmm. abstraction....
	
	private long _last_start_time;
	private long _first_start_time;
	private long _accumulated_time;
	private long _last_stop_time;

	public RapTimer(){
		//_calendar = new GregorianCalendar();
		_last_start_time = System.currentTimeMillis();
		_last_stop_time = _last_start_time;
		_first_start_time = _last_start_time;
		_accumulated_time = 0;
	}
	
	public void start(){
		_last_start_time = System.currentTimeMillis();
	}

	public long stop(){
		_last_stop_time = System.currentTimeMillis();
		_accumulated_time += _last_stop_time - _last_start_time;
		return _last_stop_time - _last_start_time;
	}
	
	public long getAccumulatedTime(){
		return _accumulated_time;
	}
	
	public void clear(){
		_accumulated_time = 0;
	}
	
	public long getTimeDifference(){
		return System.currentTimeMillis() - _last_start_time;
	}
}

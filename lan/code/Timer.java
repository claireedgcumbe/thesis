package pdms;

public class Timer {
	
	public static long timerStart = 0;
	public static long timerEnd = 0;
	
	public Timer(){
	
	}
	

	public static long getInterval(){
		return timerEnd - timerStart;
	}
	
}

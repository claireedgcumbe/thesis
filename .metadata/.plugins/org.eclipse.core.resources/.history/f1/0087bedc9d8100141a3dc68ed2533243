//import java.lang.*;
package minicon;
import java.util.Vector;
public class TimingResults{
    //the purpose of this class is to figure out all of the statistics that
    //are needed for my results
    //one issue that needs to be carefully thought of is how to update the 
    //statistics so that they will be accurate.
    //at the moment I'm just going to do it on demand... and keep a statistic
    //on if it's up to date
    Vector _raw_results;
    int _num_runs;
    double _average;
    double _variation;
	long _max;
	long _min;
	private static final long MAX_INT = 100000000;
    boolean _up_to_date;
    public TimingResults(){
        _raw_results = new Vector(20,20);
        _num_runs = 0;
        _up_to_date = true;
        _average = 0;
        _variation = 0;
		_max = -1;
		_min = MAX_INT;
    }
	
	public TimingResults(TimingResults to_copy){
        _raw_results = (Vector)to_copy._raw_results.clone();
        _num_runs = to_copy._num_runs;
        _up_to_date = to_copy._up_to_date;
        _average = to_copy._average;
        _variation = to_copy._variation;
		_max = to_copy._max;
		_min = to_copy._min;
			
	}
	public String getRawResultsString(){
		StringBuffer retval = new StringBuffer(_num_runs * 10);
		for (int i = 0; i < _num_runs; i++){
			retval.append(((Long) _raw_results.elementAt(i)).toString());
			retval.append("\n");
		}
		return retval.toString();
	}
	
	public void addAllStatistics(TimingResults other){
		for (int i = 0; i < other._num_runs;i++){
			addStatistic(((Long)other._raw_results.elementAt(i)).longValue());
		}
	}
	
    public void addStatistic(long time){
		if (time < _min){
			_min = time;
		}
		if (time > _max){
			_max = time;
		}
        _up_to_date = false;
        _num_runs++;
        _raw_results.addElement(new Long(time));
    }
    
    private void recalc(){    
        if (!_up_to_date && _num_runs > 0)
        //need to make sure that we don't do this if there are zero correct
        //answers or if the current value is correct.
        {
            double a_var;
            int i;
            _average = 0;
            for (i = 0; i < _num_runs; i++){
                _average +=((Long) _raw_results.elementAt(i)).doubleValue();
            }
            //at this point, we have the total, now figure out the average
            _average = _average / (double) _num_runs;
            //now might as well calculate the variation
            _variation = 0;
            for (i = 0; i < _num_runs; i++){
                a_var = ((Long)_raw_results.elementAt(i)).doubleValue()- _average;
                if (a_var < 0){
                    _variation -= a_var;
                }
                else {
                    //it was positive, so just add that value
                    _variation += a_var;
                }
                    
                
            }
            _variation = _variation / (double) _num_runs;
            _up_to_date = true;
        }
    }
    
    public double getAverage(){
        recalc();
        return _average;
    }
    public double getVariation(){
        recalc();
        return _variation;
    }
	
	public long getMin(){
		return _min;
	}
	
	public long getMax(){
		return _max;
	}
	
    public int getNumRuns(){
        return _num_runs;
    }

}

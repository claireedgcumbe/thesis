package minicon;
public class ReformulationTimingResults
{
	protected TimingResults _mapping_creation;
	protected TimingResults _mapping_combination;
	
	public ReformulationTimingResults(){
		_mapping_creation = new TimingResults();
		_mapping_combination = new TimingResults();
	}
	
	public void addStatistic(long time_to_create_mappings, long time_to_combine_mappings){
		_mapping_creation.addStatistic(time_to_create_mappings);
		_mapping_combination.addStatistic(time_to_combine_mappings);
	}//end addStatistic
	
	
	public double getAverageCombinationTime(){
		return _mapping_combination.getAverage();
	}
	
	public String getAllTimingStrings(){
		StringBuffer retval = new StringBuffer(10000);
		retval.append("CreationTimes\n");
		retval.append(_mapping_creation.getRawResultsString());
		retval.append("CombinationTimes\n");
		retval.append(_mapping_combination.getRawResultsString());
		int i;
		Long result;
		retval.append("TotalTime\n");
		int size = _mapping_creation.getNumRuns();
		for (i= 0; i < size; i++){
			result = new Long(((Long)_mapping_creation._raw_results.elementAt(i)).longValue() + ((Long)_mapping_combination._raw_results.elementAt(i)).longValue());
			//result = _mapping_creation._raw_results[i] + _mapping_combination._raw_results[i];
			retval.append(result.toString() + "\n");
		}
		retval.append("\n");
				
		return retval.toString();
	}
	public double getAverageCreationTime(){
		return _mapping_creation.getAverage();
	}
	
	public double getAverageTotalTime(){
		return _mapping_combination.getAverage() + _mapping_creation.getAverage();
	}
	
	public int getNumRuns(){
		return _mapping_combination.getNumRuns();
	}
	
}//end class ReformulationTimingResults

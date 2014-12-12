package minicon;
import java.io.*;

public class ExperimentStatistics
{
	//the purpose of this class is to keep track of all of the statistics that 
	//one might wish to keep track of in running the same experiment setup over
	//and over
	
	protected ReformulationTimingResults [] _results;
	protected int _size;
	
	public ExperimentStatistics(){
		_size = 1000;
		_results = new ReformulationTimingResults[_size];
		for (int i = 0; i < _size; i++){
			_results[i] = new ReformulationTimingResults();
		}//end for initializing _results
	}
	
	public void addStatistic(int num_rewritings, long time_to_create_mappings, long time_to_combine_mappings){
		if (num_rewritings > _size -1){
			//if we need to just stick it in the biggest bucket
			_results[_size-1].addStatistic(time_to_create_mappings,time_to_combine_mappings);
		}//end of if we need to just stick it in the biggest bucket
		else{
			//it can just go in the existing bucket
			_results[num_rewritings].addStatistic(time_to_create_mappings,time_to_combine_mappings);
		}
	}//end addStatistic
	
	public int size(){
		return _size;
	}
	
	public ReformulationTimingResults getResultsWithIRewritings(int i){
		return _results[i];
	}
	
	public double getAverageTotalTime(){
		long total = 0;
		int num_answers = 0;
		int i;
		for (i = 0; i < _size; i++){
			total+= (_results[i].getAverageTotalTime() * _results[i].getNumRuns());
			num_answers += _results[i].getNumRuns();
		}
		return (double) total / (double) num_answers;
		
	}//end getAverageTotalTime
	
	public double getAverageTotalCreationTime(){
		long total = 0;
		int num_answers = 0;
		int i;
		for (i = 0; i < _size; i++){
			total+= (_results[i].getAverageCreationTime() * _results[i].getNumRuns());
			num_answers += _results[i].getNumRuns();
		}
		return (double) total / (double) num_answers;
	}//end getAverageTotalCreationTime		
		
	public double getAverageTotalCombinationTime(){
		long total = 0;
		int num_answers = 0;
		int i;
		for (i = 0; i < _size; i++){
			total+= (_results[i].getAverageTotalTime() * _results[i].getNumRuns());
			num_answers += _results[i].getNumRuns();
		}
		return (double) total / (double) num_answers;
	}//end getAverageTotalCombinationTime		
	
	public double getAverageNoRewritingsTime(){
		return _results[0].getAverageCombinationTime() + _results[0].getAverageCreationTime();
	}
	
	public double getAverageNoRewritingsCreationTime(){
		return _results[0].getAverageCreationTime();
	}
	
	public int getNumWithRewritings(){
		int retval = 0;
		for (int i = 1; i < _size; i++){
			retval += _results[i].getNumRuns();
		}
		return retval;
	}
		
	
	public int getNumRuns(){
		return getNumWithRewritings() + _results[0].getNumRuns();
	}
	
	public int getNumWithoutRewritings(){
		return _results[0].getNumRuns();
	}
	
	public double getAverageHadRewritingsTime(){
		long total = 0;
		int num_answers = 0;
		int i;
		for (i = 1; i < _size; i++){
			total+= (_results[i].getAverageTotalTime() * _results[i].getNumRuns());
			num_answers += _results[i].getNumRuns();
		}
		if (num_answers == 0){
			return 0;
		}
		return (double) total / (double) num_answers;
		
	}//end getAverageHadRewritingsTime
	
	public double getAverageHadRewritingsCreationTime(){
		long total = 0;
		int num_answers = 0;
		int i;
		for (i = 1; i < _size; i++){
			total+= (_results[i].getAverageCreationTime() * _results[i].getNumRuns());
			num_answers += _results[i].getNumRuns();
		}
		return (double) total / (double) num_answers;
	}//end getAverageHadRewritingsCreationTime		
		
	public double getAverageHadRewritingsCombinationTime(){
		long total = 0;
		int num_answers = 0;
		int i;
		for (i = 1; i < _size; i++){
			total+= (_results[i].getAverageTotalTime() * _results[i].getNumRuns());
			num_answers += _results[i].getNumRuns();
		}
		return (double) total / (double) num_answers;
	}//end getAverageHadRewritingsCombinationTime		

	
	public void printAllAverageStatistics(String filename){
		FileWriter output;
		int i;
		try {
			output = new FileWriter(filename);
			for (i = 0; i < _size; i++){
				if ((_results[i].getNumRuns()>0)){
					output.write((new Integer(i)).toString());
					output.write(" ");
					output.write((new Integer(_results[i].getNumRuns())).toString());
					output.write(" ");
					output.write((new Double(_results[i].getAverageTotalTime())).toString());
					output.write(" ");
					output.write((new Double(_results[i].getAverageCreationTime())).toString());
					output.write(" ");
					output.write((new Double(_results[i].getAverageCombinationTime())).toString());
											 
					output.write("\n");
				}
			}//end for looping over each of the buckets
			output.close();
		} catch (IOException e){
			System.out.println("couldn't open output file in ExperimentStatistics.printAverageStatistics; exiting");
			System.exit(1);
		}
	}
	
	public void printAllStatistics2(String filename){
		FileWriter output;
		int i;
		try {
			output = new FileWriter(filename);
			for (i = 0; i < _size; i++){
				if ((_results[i].getNumRuns()>0)){
					output.write("Bucket = " + ((new Integer(i)).toString()) + "\n");
					output.write(("Number of runs = " + new Integer(_results[i].getNumRuns())).toString() + "\n");
					//output.write(" ");
					//output.write((new Double(_results[i].getAverageTotalTime())).toString());
					//output.write(" ");
					//output.write((new Double(_results[i].getAverageCreationTime())).toString());
					//output.write(" ");
					//output.write((new Double(_results[i].getAverageCombinationTime())).toString());
					output.write(_results[i].getAllTimingStrings());
					output.write("\n");
				}
			}//end for looping over each of the buckets
			output.close();
		} catch (IOException e){
			System.out.println("couldn't open output file in ExperimentStatistics.printAverageStatistics; exiting");
			System.exit(1);
		}
		
	
	}
	
	public void printAllStatistics(String filename){
		FileWriter output;
		int i;
		try {
			output = new FileWriter(filename);
			for (i = 0; i < _size; i++){
				if ((_results[i].getNumRuns()>0)){
					output.write((new Integer(i)).toString());
					output.write(" ");
					output.write((new Integer(_results[i].getNumRuns())).toString());
					//output.write(" ");
					//output.write((new Double(_results[i].getAverageTotalTime())).toString());
					//output.write(" ");
					//output.write((new Double(_results[i].getAverageCreationTime())).toString());
					//output.write(" ");
					//output.write((new Double(_results[i].getAverageCombinationTime())).toString());
					output.write(_results[i].getAllTimingStrings());
					output.write("that was bucket " + ((new Integer(i)).toString()));
					output.write("\n");
				}
			}//end for looping over each of the buckets
			output.close();
		} catch (IOException e){
			System.out.println("couldn't open output file in ExperimentStatistics.printAverageStatistics; exiting");
			System.exit(1);
		}
	}

	public void printTotalAverageStatistics(String filename){
		FileWriter output;
		int i;
		try {
			output = new FileWriter(filename);
			for (i = 0; i < _size; i++){
				if ((_results[i].getNumRuns()>0)){
					output.write((new Integer(i)).toString());
					output.write(" ");
					output.write((new Integer(_results[i].getNumRuns())).toString());
					output.write(" ");
					output.write((new Double(_results[i].getAverageTotalTime())).toString());
					output.write("\n");
				}
			}//end for looping over each of the buckets
			output.close();
		} catch (IOException e){
			System.out.println("couldn't open output file in ExperimentStatistics.printAverageStatistics; exiting");
			System.exit(1);
		}
	}
	
		public void printCreationAverageStatistics(String filename){
		FileWriter output;
		int i;
		try {
			output = new FileWriter(filename);
			for (i = 0; i < _size; i++){
				if (_results[i].getNumRuns()> 0){
					output.write((new Integer(i)).toString());
					output.write(" ");
					output.write(_results[i].getNumRuns());
					output.write(" ");
					output.write((new Double(_results[i].getAverageCreationTime()).toString()));
				}
				output.close();
			}//end for looping over each of the buckets
		} catch (IOException e){
			System.out.println("couldn't open output file in ExperimentStatistics.printAverageCreationStatistics; exiting");
			System.exit(1);
		}
	}

		public void printCombinationAverageStatistics(String filename){
		FileWriter output;
		int i;
		try {
			output = new FileWriter(filename);
			for (i = 0; i < _size; i++){
				if (_results[i].getNumRuns() > 0){
					output.write((new Integer(i)).toString());
					output.write(" ");
					output.write(_results[i].getNumRuns());
					output.write(" ");
					output.write((new Double(_results[i].getAverageCombinationTime()).toString()));
					output.flush();
					output.close();
				}
			}//end for looping over each of the buckets
		} catch (IOException e){
			System.out.println("couldn't open output file in ExperimentStatistics.printCombinationAverageStatistics; exiting");
			System.exit(1);
		}
	}

}

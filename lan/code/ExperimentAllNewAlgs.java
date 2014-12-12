//import java.lang.*;
package minicon;
import java.io.*;
import java.util.Vector;


public class ExperimentAllNewAlgs{
	private ExperimentStatistics [] _statistics;
	private String _directory;
	private int _num_algorithms;
	public Algorithm [] _workers;
	private Vector _views;
	private Query _query;
	private boolean _ready;
	private String _query_type;
	private String _view_type;
	private String _algorithm_type;
	private int _num_different_vars_in_query;
	private int _num_different_vars_in_view;
	private int _num_view_predicates;
	private int _num_query_predicates;
	private int _num_vars_in_view_predicates;
	private int _num_vars_in_query_predicates;
	private int _num_distinguished_in_view;
	private int _num_distinguished_in_query;
	private int _view_function_start;
	private int _view_function_stop;
	private int _query_function_start;
	private int _query_function_stop;
	private int _num_views;
	private int _num_runs;
	private int _num_query_function_dup;
	private int _num_view_function_dup;
	//private RapTimer _timer;
	private RandomStatementGenerator _query_generator;
	private RandomStatementGenerator _view_generator;
	private String _output_file_name;
	private FileWriter _output_file;
	private FileWriter _query_file;
	private boolean _print_debug_info;
	private boolean _check_soundness;
	private boolean _do_algorithm[];
	private boolean _change_num_views;
	/*private TimingResults [] _timing_results_had_rewriting;
	private TimingResults [] _timing_results_no_rewriting;
	private TimingResults [] _timing_results_all_results;
	*/
	public ExperimentAllNewAlgs(){
		_num_algorithms = 5;
		_output_file_name = "noisy.txt"; 
		_directory = "";
		//_directory = "g:\\research\\minicon\\out\\";
		//_use_new_algorithm = true;
		_algorithm_type = new String("OriginalNew");
		_check_soundness = false;
		_print_debug_info = false;
		_num_vars_in_view_predicates = 5;
		_num_vars_in_query_predicates = 5;
		_num_query_predicates = 5; 
		_num_view_predicates = 5;
		_num_distinguished_in_view = 2;
		_num_distinguished_in_query = 2;
		_view_function_start = 5;
		_view_function_stop = 25;
		_query_function_start = 5;
		_query_function_stop = 25;
		_num_different_vars_in_query = 5;
		_num_different_vars_in_view = 5;
		_num_runs = 100;
		_num_query_function_dup = 1;
		_num_view_function_dup = 1 ;
		_views = null;
		_query = null;
		_query_type = new String("Chain");
		_view_type = new String("Chain");
		_query_generator = null;
		_view_generator = null;        
		_ready = false; 
		_num_views = 10;
		_change_num_views = false;
		/*_timing_results_had_rewriting = new TimingResults[_num_algorithms];
		_timing_results_no_rewriting = new TimingResults[_num_algorithms];
		_timing_results_all_results = new TimingResults[_num_algorithms];
		*/
		_statistics = new ExperimentStatistics[_num_algorithms ];
		_workers = new Algorithm[_num_algorithms];
		_do_algorithm = new boolean[_num_algorithms];
		for (int i = 0; i < _num_algorithms; i++){
			//_timing_results_had_rewriting[i] = new TimingResults();
			//_timing_results_no_rewriting[i] = new TimingResults();
			//_timing_results_all_results[i] = new TimingResults();
			_statistics[i] = new ExperimentStatistics();
			_workers[i] = null;
			_do_algorithm[i] = false;
		}
		//assume we always want to do the first algorithm 
		_do_algorithm[0] = true;
			
	}
	
	public boolean init(){
		//takes the variables and initializes them
		//this cannot be run before processArgs, or none of
		//the values will carry
		boolean retval = true;
		initQueryGenerator();
		initViewGenerator();
		initWorker();
		
		return retval;
	}//end public boolean init()
	
	public Vector getViews(){
		return _workers[0].getViews();
	}
	
	public Query getQuery(){
		return _workers[0].getQuery();
	}
	
	private boolean initWorker(){
		//note, this must be run after the algorithm type has been selected, or it won't take 
		//effect
		_workers[0] =  new BucketEndingAlgorithm();
		_workers[1] = new InverseRules();
		///_workers[2] = new BucketAlgorithm();
		_workers[3] = new QuasiTreeAlgorithm();
		//_workers[4] = new OptimumAlgorithm();
		return true;
		//note, at the moment this really doesn't require any sort of return value.
		//but since the others do, this is being done for consistency
	}
	
	private boolean initQueryGenerator(){
		
		if (_query_type.equals("Normal")){
			//then we just want a normal random statement, with all the items I've
			//specified
			_query_generator = new NormalRandomStatementGenerator(_query_function_start,_query_function_stop,_num_query_predicates,_num_different_vars_in_query);  


		}
		else if (_query_type.equals("Chain")){
			_query_generator = new ChainRandomStatementGenerator(_query_function_start,_query_function_stop,_num_query_predicates,_num_vars_in_query_predicates,_num_distinguished_in_query,_num_query_function_dup);
		}
		else if (_query_type.equals("Star")){
			//need to change it so that it modifies both number of predicates *and* size of predicates
			//I choose to take the one that's different
			if (_num_vars_in_query_predicates != 5){
				_num_query_predicates = _num_vars_in_query_predicates +1;
			}
			else if(_num_query_predicates != 5){
				_num_vars_in_query_predicates = _num_query_predicates -1;
			}

			_query_generator = new StarRandomStatementGenerator(_query_function_start, _query_function_stop,_num_query_predicates,_num_vars_in_query_predicates,_num_distinguished_in_query,_num_query_function_dup);
		}													  
		else if (_query_type.equals("Full")){
			_query_generator = new FullRandomStatementGenerator(_query_function_start,_query_function_stop,_num_vars_in_query_predicates,_num_distinguished_in_query,_num_query_function_dup);
		}
		else if (_query_type.equals("Duplicate")){
			_query_generator = new DuplicateCountRandomStatementGenerator(_query_function_start,_query_function_stop,_num_query_function_dup,_num_vars_in_query_predicates,_num_query_predicates,_num_distinguished_in_query,_num_different_vars_in_query);
//			_query_generator = new DuplicateCountRandomStatementGenerator(_query_function_start,_query_function_stop,_num_different_vars_in_query,_num_query_dup,_num_vars_in_query_predicates,_num_query_predicates);
		}
		else if (_query_type.equals("Regular")){
			_query_generator = new RegularRandomStatementGenerator(_query_function_start,_query_function_stop,_num_query_predicates,_num_different_vars_in_query);  
		}
		else{
			System.out.println("No query type chosen, using Regular");
			_query_generator = new RegularRandomStatementGenerator(_query_function_start,_query_function_stop,_num_query_predicates,_num_different_vars_in_query);  
		}
		
		return true;
	}//end initQueryGenerator
	
	private boolean initViewGenerator(){
		if (_view_type.equals("Normal")){
			//then we just want a normal random statement, with all the items I've
			//specified
			_view_generator = new NormalRandomStatementGenerator(_view_function_start,_view_function_stop,_num_view_predicates,_num_different_vars_in_view);  


		}
		else if (_view_type.equals("Chain")){
			_view_generator = new ChainRandomStatementGenerator(_view_function_start,_view_function_stop,_num_view_predicates,_num_vars_in_view_predicates,_num_distinguished_in_view,_num_view_function_dup);
		}
		else if (_view_type.equals("Star")){
			if (_num_vars_in_view_predicates != 5){
				_num_view_predicates = _num_vars_in_view_predicates +1;
			}
			else if(_num_query_predicates != 5){
				_num_vars_in_view_predicates = _num_view_predicates -1;
			}


			_view_generator = new StarRandomStatementGenerator(_view_function_start, _view_function_stop,_num_view_predicates,_num_vars_in_view_predicates,_num_distinguished_in_view,_num_view_function_dup);
		}
		else if (_view_type.equals("Full")){
			_view_generator = new FullRandomStatementGenerator(_view_function_start,_view_function_stop,_num_vars_in_view_predicates,_num_distinguished_in_view,_num_view_function_dup);
		}

		else if (_view_type.equals("Duplicate")){
			_view_generator = new DuplicateCountRandomStatementGenerator(_view_function_start,_view_function_stop,_num_view_function_dup,_num_vars_in_view_predicates,_num_view_predicates,_num_distinguished_in_view,_num_different_vars_in_view);
		}
		else{
			System.out.println("No view type chosen, using Regular");
			_view_generator = new RegularRandomStatementGenerator(_view_function_start,_view_function_stop,_num_view_predicates,_num_different_vars_in_view);  
			
		} 
		return true;
	}//end initViewGenerator
	
	
	private int getIntValue(String input){
		try {
			return (new Integer(input)).intValue();
		}
		catch (NumberFormatException e){
			System.out.print("Expecting an integer, got ");
			System.out.println(input);
			System.exit(1);
			return 0;
		}

	}
	
	public String getSetupString(){
		//this function string returns the whole amount.
		System.out.println("Experiment.getSetupString() not implemented");
		return null;
	}
	
	
	public boolean processArgs(String args[]){
		//System.out.println(args.length);
		boolean retval = true;
		int i;
		for (i = 0; i < args.length; i++){
			//note at this point we assume properly formed arguments

			if (args[i].equals("-viewshape")){
				i++;
				if (args[i].equals("Normal")){
					_view_type = "Normal";   
				}
				else if (args[i].equals("Chain")){
					_view_type = "Chain";
				}
				else if (args[i].equals("Duplicate")){
					_view_type = "Duplicate";
				}
				else if (args[i].equals("Regular")){
					_view_type = "Regular";
				}
				else if (args[i].equals("Star")){
					_view_type = "Star";
				}
				else if (args[i].equals("Full")){
					_view_type = "Full";
				}
				else {
					//there was some unrecognized answer
					System.out.print("unrecognized query shape ");
					System.out.println(args[i]);
					retval = false;
				}
				
			}//end of if we're dealing with the shape of the views
			else if (args[i].equals("-queryshape")){
				i++;
				if (args[i].equals("Normal")){
					_query_type = "Normal";
				}
				else if (args[i].equals("Chain")){
					_query_type = "Chain";
				}
				else if (args[i].equals("Duplicate")){
					_query_type = "Duplicate";
				}
				else if (args[i].equals("Regular")){
					_query_type = "Regular";
				}
				else if (args[i].equals("Star")){
					_query_type = "Star";
				}
				else if (args[i].equals("Full")){
					_query_type = "Full";
				}
				else {
					//there was some unrecognized answer
					System.out.print("unrecognized query shape ");
					System.out.println(args[i]);
					retval = false;
				}
			}//end of if we're looking at the shape of the queries
			else if (args[i].equals("-usealg")){
				i++;
				if (args[i].equals("OriginalNew")){
					_algorithm_type = "OriginalNew";
				}
				else if (args[i].equals("NoExistentialCheck")){
					_algorithm_type = "NoExistentialCheck";
				}
				else if (args[i].equals("Bucket")){
					_algorithm_type = "Bucket";
				}
				else if (args[i].equals("BucketEnding")){
					_algorithm_type = "BucketEnding";
				}
				else {
					System.out.println("unrecognized algorithm type ");
					System.out.println(args[i]);
					retval = false;
				}

			}//end of parsing arguments to use the algorithm
			else if (args[i].equals("-querysize")){
				i++;
				_num_query_predicates = getIntValue(args[i]);            
			}//end of if it was querysize
			else if (args[i].equals("-viewsize")){
				i++;
				_num_view_predicates = getIntValue(args[i]);	            
			}//end of if it was view size
			else if (args[i].equals("-viewpredicatesize")){
				i++;	            
				_num_vars_in_view_predicates = getIntValue(args[i]);
			}//end of if it was the number of predicates in a view
			else if (args[i].equals("-querypredicatesize")){
				i++;
				_num_vars_in_query_predicates = getIntValue(args[i]);	        
			}//end of if it was the number of predicates in a view
			else if (args[i].equals("-viewfunstart")){
				i++;
				_view_function_start= getIntValue(args[i]);
			}
			else if (args[i].equals("-viewfunstop")){
				i++;
				_view_function_stop = getIntValue(args[i]);
			}
			else if (args[i].equals("-queryfunstart")){
				i++;
				_query_function_start = getIntValue(args[i]);
			}
			else if (args[i].equals("-queryfunstop")){
				i++;
				_query_function_stop = getIntValue(args[i]);
			}
			else if (args[i].equals("-querynumdist")){
				i++;
				_num_distinguished_in_query = getIntValue(args[i]);
			}
			else if (args[i].equals("-viewnumdist")){
				i++;
				_num_distinguished_in_view = getIntValue(args[i]);
			}
			else if (args[i].equals("-numviews")){
				i++;
				_num_views = getIntValue(args[i]);
			}
			else if (args[i].equals("-numqueryvars")){
				i++;
				_num_different_vars_in_query = getIntValue(args[i]);
			}
			else if (args[i].equals("-numviewvars")){
				i++;                
				_num_different_vars_in_view = getIntValue(args[i]);
			}
			
			else if (args[i].equals("-numruns")){
				i++;
				_num_runs = getIntValue(args[i]);
				
			}
			else if (args[i].equals("-numqueryfunctiondups")){
				i++;
				_num_query_function_dup = getIntValue(args[i]);
			}
			else if (args[i].equals("-numviewfunctiondups")){
				i++;
				_num_view_function_dup = getIntValue(args[i]);
			}
			else if (args[i].equals("-noisy")){
				_print_debug_info = true;
			}
			/*else if(args[i].equals("-usenewalg")){
			_use_new_algorithm = true;
			}
			else if(args[i].equals("-usebucketalg")){
			_use_new_algorithm = false;
			}
			*/
			else if (args[i].equals("-checksoundness")){
				_check_soundness = true;
			}
			else if (args[i].equals("-DoBucketAlgorithm")){
				_do_algorithm[2] = true;
			}
			else if (args[i].equals("-DoOnlyBucketAlgorithm")){
				for (int foo = 0; foo < _num_algorithms; foo++){
					_do_algorithm[foo] = false;
				}
				_do_algorithm[2] = true;
			}
			else if (args[i].equals("-DontDoBucketAlgorithm")){
				_do_algorithm[2] = false;
			}
			else if (args[i].equals("-DoInverseRules")){
				_do_algorithm[1] = true;
			}
			else if(args[i].equals("-DoOnlyInverseRules")){
				for (int foo = 0; foo < _num_algorithms; foo++){
					_do_algorithm[foo] = false;
				}
				_do_algorithm[1] = true;
			}
			else if (args[i].equals("-DoQuasiTree")){
				_do_algorithm[3] = true;
			}
			else if (args[i].equals("-DoOptimum")){
				_do_algorithm[4] = true;
			}
			else {//default; we don't recognize this option
				System.out.print("unrecognized command line argument ");
				System.out.print(args[i]);
				retval = false;
			}
		}//end of looping over the input arguments
		return retval;
	}
	
	public void generateNewQuery(){
		Query a_query = new Query(_query_generator.getRandomStatement(false));
		for (int i = 0; i < _num_algorithms; i++){
			if (_do_algorithm[i]){
				_workers[i].setQuery(a_query);
			}
		}
	}
	
	public void clearViews(){
		_views = new Vector(5);
	}
	
	public void addView(){
		View a_view = new View(_view_generator.getRandomStatement(false));
		for(int i = 0; i < _num_algorithms; i++){
			if (_do_algorithm[i]){
				_workers[i].addView(a_view);
			}
		}
	}
	
	public void addView(RandomStatementGenerator a_generator){
		View a_view = new View(a_generator.getRandomStatement(false));
		for(int i = 0; i < _num_algorithms; i++){
			if (_do_algorithm[i]){
				_workers[i].addView(a_view);
			}
		}
	}
	public double runN(int n, boolean run_bucket_algorithm){
		//runs the experiments n times, where in is a parameter.
		//does not reuse any of the views or queries
		double total =0;
		try{
			_query_file = new FileWriter("querynumviews" + _num_views + "numsubgoals" + _num_query_predicates + ".txt");
			if (_print_debug_info){
				_output_file = new FileWriter(_directory + _output_file_name,true);
			}
			for (int i = 0; i < n; i++){
				runOnce(run_bucket_algorithm);
				//System.out.println("finished one run");
			}
			if (_print_debug_info){
				_output_file.close();
			}
			_query_file.close();
			return total / (double) n;
		}catch (IOException e){
			System.out.println("we can't open the file");
			System.exit(1);
			return 0;

		}
	}
	
	public double runN234(int n, boolean run_bucket_algorithm){
		//runs the experiments n times, where in is a parameter.
		//does not reuse any of the views or queries
		double total =0;
		if (_print_debug_info){
			try{
				_output_file = new FileWriter(_directory + _output_file_name,true);
			}catch (IOException e){
				System.out.println("we can't open the file");
				System.exit(1);
			}
		}
		for (int i = 0; i < n; i++){
			runOnce234(run_bucket_algorithm);
			//System.out.println("finished one run");
		}
		if (_print_debug_info){
			try {
				_output_file.close();
			}
			catch (IOException e){
				System.out.println("Booger, couldn't close file");
			}
		}
		return total / (double) n;
	}
	
	public ExperimentStatistics[] runAll(String args[], boolean run_bucket_algorithm){
		//this is the function to call if you just want to 
		//pretend that you are running it from main; initializes everything,
		//runs it, returns the answer.
		//_timing_results_had_rewriting = new TimingResults();
		//_timing_results_no_rewriting = new TimingResults();
		processArgs(args);
		init();
		runN(_num_runs, run_bucket_algorithm);
		//a_result =new TimingResults(_timing_results_had_rewriting);
		//a_result.addAllResults(_timing_results_no_rewriting);
		return _statistics;
		//return _timing_results;
	}
	public ExperimentStatistics[] runAll234(String args[], boolean run_bucket_algorithm){
		//this is the function to call if you just want to 
		//pretend that you are running it from main; initializes everything,
		//runs it, returns the answer.
		//_timing_results_had_rewriting = new TimingResults();
		//_timing_results_no_rewriting = new TimingResults();
		processArgs(args);
		init();
		runN234(_num_runs, run_bucket_algorithm);
		//a_result =new TimingResults(_timing_results_had_rewriting);
		//a_result.addAllResults(_timing_results_no_rewriting);
		return _statistics;
		//return _timing_results;
	}
	
	/*
	public TimingResults [] getRewritingResults(){
		return _timing_results_had_rewriting;
	}
	public TimingResults [] getNoRewritingResults(){
		return _timing_results_no_rewriting;
	}
	*/
	public void runOnce(boolean run_bucket_algorithm){
		//returns the amount of time that it took for the experiment to run
		try{
			int i;
			for (i = 0; i < _num_algorithms; i++){
				_workers[i] = null;
			}
			String answer;
			init();		_do_algorithm[2] = run_bucket_algorithm;
			generateNewQuery();
			for (i = 0; i < _num_views; i++){
				addView();
			}
			//now save the queries and views to that file
			_query_file.write(_workers[0].getQuery().printString()+ "\n");
			Vector views = _workers[0].getViews();
			for (i = 0; i < _num_views; i++){
				_query_file.write(((View)views.elementAt(i)).printString() + "\n" );
			}
			for (i = 0; i < _num_algorithms; i++){
				if (_do_algorithm[i]){
					System.gc();
					answer   = _workers[i].run();
					_statistics[i].addStatistic(_workers[i].getNumRewritings(),_workers[i].getMappingCreationTime(),
												_workers[i].getMappingCombinationTime());										 
					String filename = _directory + "r" + _workers[i].type() + _num_views + ".txt";
					FileWriter writeAll  = new FileWriter(filename,true);
					writeAll.write(_workers[i].getTotalTime() + "\n");
					writeAll.close();				
					
					if (_check_soundness){
						//we don't need to check the soundness of the bucket algorithm; it was
						//already checked since it uses the containment checker
						_workers[i].allAnswersContained(answer);
					}
					if (_print_debug_info){
						_output_file.write(_workers[i].printString().toString());
						_output_file.write(answer);
						_output_file.write("\n");
						_output_file.flush();
					}//end if we need to print debug info
				}
			}//end for
		}//end try
		catch(IOException e){					
			System.out.println("Couldn't open file in experimentallnewalgs.runonce");	
		}

	}//end runOnce
		
	public void runOnce234(boolean run_bucket_algorithm){
		//returns the amount of time that it took for the experiment to run
		int i;
		for (i = 0; i < _num_algorithms; i++){
			_workers[i] = null;
		}
		String answer;
		init();		
		_do_algorithm[2] = run_bucket_algorithm;
		generateNewQuery();
		RandomStatementGenerator views234[] = new RandomStatementGenerator[3];
		views234[0] = new ChainRandomStatementGenerator(_view_function_start,_view_function_stop,2,_num_vars_in_view_predicates,_num_distinguished_in_view,_num_view_function_dup);
		views234[1] = new ChainRandomStatementGenerator(_view_function_start,_view_function_stop,3,_num_vars_in_view_predicates,_num_distinguished_in_view,_num_view_function_dup);
		views234[2] = new ChainRandomStatementGenerator(_view_function_start,_view_function_stop,4,_num_vars_in_view_predicates,_num_distinguished_in_view,_num_view_function_dup);

		for (i = 0; i < _num_views; i++){
			addView(views234[i%3]);
		}
		for (i = 0; i < _num_algorithms; i++){
			if (_do_algorithm[i]){
				System.gc();
				answer   = _workers[i].run();
				_statistics[i].addStatistic(_workers[i].getNumRewritings(),_workers[i].getMappingCreationTime(),
											_workers[i].getMappingCombinationTime());										 
				try{
					FileWriter writeAll  = new FileWriter(_directory + "r" + _workers[i].type() + _num_views + ".txt",true);
					writeAll.write(_workers[i].getTotalTime() + "\n");
					writeAll.close();				
				}				
				catch(IOException e){					
					System.out.println("Couldn't open file writeall in experiment.java");	
				}
				if (_check_soundness){
					//we don't need to check the soundness of the bucket algorithm; it was
					//already checked since it uses the containment checker
					_workers[i].allAnswersContained(answer);
				}
				if (_print_debug_info){
					try{
						_output_file.write(_workers[i].printString().toString());
						_output_file.write(answer);
						_output_file.write("\n");
						_output_file.flush();
					} catch (IOException e){
						System.out.println("Couldn't write to the output file in Experiment.java");
					}
				}//end if we need to print debug info
			}
		}//end for
	}//end runOnce

	public static void main(String args[]) throws IOException{
		Experiment e = new Experiment();
		System.out.println(e.runAll(args));
		System.out.println("done!!!");
		
	}//end main
}

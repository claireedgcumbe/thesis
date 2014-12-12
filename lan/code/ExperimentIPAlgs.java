//import java.lang.*;
package minicon;
import java.io.*;
import java.util.Vector;


public class ExperimentIPAlgs{
	private String _directory;
	private ExperimentStatistics [] _statistics;
	private int _num_algorithms;
	public IPAlgorithm [] _workers;
	public NewAlgorithm _regular_worker;
	private Vector _views;
	private IPQuery _query;
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
	private int _num_query_dup;
	private int _num_view_dup;
	private RandomStatementGenerator _query_generator;
	private RandomStatementGenerator _view_generator;
	private IPGenerator _query_ip_generator;
	private IPGenerator _view_ip_generator;
	private String _output_file_name;
	private FileWriter _output_file;
	private boolean _print_debug_info;
	private boolean _check_soundness;
	private int _ip_start;
	private int _ip_stop;
	private Vector _ip_types;
	private int _ip_num_vars;
	
	public ExperimentIPAlgs(){
		//_directory = "g:\\research\\minicon\\out\\";
		_directory = "";
		_num_algorithms = 1;
		_output_file_name = "noisy.txt"; 
		//_use_new_algorithm = true;
		_algorithm_type = new String("IPImplied");
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
		_num_query_dup = 1;
		_num_view_dup = 1 ;
		_views = null;
		_query = null;
		_query_type = new String("Chain");
		_view_type = new String("Chain");
		_query_generator = null;
		_view_generator = null;        
		_query_ip_generator = null;
		_view_ip_generator = null;
		_ready = false; 
		_num_views = 10;
		_ip_start = 1;
		_ip_stop = 10;
		_ip_num_vars = 3;
		_ip_types = new Vector(10);
		_ip_types.addElement(">");
		_ip_types.addElement("<");
		/*_timing_results_had_rewriting = new TimingResults[_num_algorithms];
		_timing_results_no_rewriting = new TimingResults[_num_algorithms];
		_timing_results_all_results = new TimingResults[_num_algorithms];
		*/
		_statistics = new ExperimentStatistics[_num_algorithms + 1];
		_workers = new IPAlgorithm[_num_algorithms];
		for (int i = 0; i < _num_algorithms; i++){
			//_timing_results_had_rewriting[i] = new TimingResults();
			//_timing_results_no_rewriting[i] = new TimingResults();
			//_timing_results_all_results[i] = new TimingResults();
			_statistics[i] = new ExperimentStatistics();
			_workers[i] = null;
		}
		_regular_worker = null;
		_statistics[1] = new ExperimentStatistics();
	
			
	}
	
	public boolean init(){
		//takes the variables and initializes them
		//this cannot be run before processArgs, or none of
		//the values will carry
		boolean retval = true;
		initQueryGenerator();
		initViewGenerator();
		initWorker();
		initIPGenerator();
		
		return retval;
	}//end public boolean init()
	
	public Vector getViews(){
		return _workers[0].getViews();
	}
	
	public IPQuery getQuery(){
		return _workers[0].getQuery();
	}
	
	private boolean initWorker(){
		//note, this must be run after the algorithm type has been selected, or it won't take 
		//effect
		_workers[0] =  new IPImpliedAlgorithm();
		_regular_worker = new BucketEndingAlgorithm();
		return true;
		//note, at the moment this really doesn't require any sort of return value.
		//but since the others do, this is being done for consistency
	}
	
	private boolean initIPGenerator(){
		//at the moment, let's just assume they have the same values,
		//shall we?
		//_query_ip_generator = new IPGenerator(_ip_start,_ip_stop,_ip_types,_num_query_predicates);
		_query_ip_generator = new IPGenerator(_ip_start,_ip_stop,_ip_types,15);
		_view_ip_generator = _query_ip_generator;
		return true;
	}
	private boolean initQueryGenerator(){
		
		if (_query_type.equals("Normal")){
			//then we just want a normal random statement, with all the items I've
			//specified
			_query_generator = new NormalRandomStatementGenerator(_query_function_start,_query_function_stop,_num_query_predicates,_num_different_vars_in_query);  


		}
		else if (_query_type.equals("Chain")){
			_query_generator = new ChainRandomStatementGenerator(_query_function_start,_query_function_stop,_num_query_predicates,_num_vars_in_query_predicates,_num_distinguished_in_query,_num_query_dup);
		}
		else if (_query_type.equals("Star")){
			if (_num_vars_in_query_predicates != 5){
				_num_query_predicates = _num_vars_in_query_predicates +1;
			}
			else if(_num_query_predicates != 5){
				_num_vars_in_query_predicates = _num_query_predicates -1;
			}


			_query_generator = new StarRandomStatementGenerator(_query_function_start, _query_function_stop,_num_query_predicates,_num_vars_in_query_predicates,_num_distinguished_in_query,_num_query_dup);
		}													  

		else if (_query_type.equals("Duplicate")){
			System.out.println("EVIL");
			System.exit(1);
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
			_view_generator = new ChainRandomStatementGenerator(_view_function_start,_view_function_stop,_num_view_predicates,_num_vars_in_view_predicates,_num_distinguished_in_view,_num_view_dup);
		}
		else if (_view_type.equals("Star")){
			if (_num_vars_in_view_predicates != 5){
				_num_view_predicates = _num_vars_in_view_predicates +1;
			}
			else if(_num_query_predicates != 5){
				_num_vars_in_view_predicates = _num_view_predicates -1;
			}


			_view_generator = new StarRandomStatementGenerator(_view_function_start, _view_function_stop,_num_view_predicates,_num_vars_in_view_predicates,_num_distinguished_in_view,_num_view_dup);
		}													  
		else if (_view_type.equals("Duplicate")){
			System.out.println("EVIL");
			System.exit(1);
//			_view_generator = new DuplicateCountRandomStatementGenerator(_view_function_start,_view_function_stop,_num_different_vars_in_view,_num_view_dup,_num_vars_in_view_predicates,_num_view_predicates);
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
			else if (args[i].equals("-numquerydups")){
				i++;
				_num_query_dup = getIntValue(args[i]);
			}
			else if (args[i].equals("-numviewdups")){
				i++;
				_num_view_dup = getIntValue(args[i]);
			}
			else if (args[i].equals("-ipstart")){
				i++;
				_ip_start = getIntValue(args[i]);
			}
			else if (args[i].equals("-ipstop")){
				i++;
				_ip_stop = getIntValue(args[i]);
			}
			else if (args[i].equals("-ipnumvars")){
				i++;
				_ip_num_vars = getIntValue(args[i]);
			}
			else if (args[i].equals("-setiptypes")){
				int how_many;
				int poo;
				i++;
				how_many = getIntValue(args[i]);
				_ip_types = new Vector(how_many);
				for (poo = 0; poo < how_many; poo++){
					i++;
					_ip_types.addElement(args[i]);
				}
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
			else {//default; we don't recognize this option
				System.out.print("unrecognized command line argument ");
				System.out.print(args[i]);
				retval = false;
			}
		}//end of looping over the input arguments
		return retval;
	}
	
	public void generateNewQuery(){
		IPQuery a_query = new IPQuery(_query_ip_generator.getRandomIPs(_query_generator.getRandomStatement(false)));
		//for (int i = 0; i < _num_algorithms; i++){
			_workers[0].setQuery(a_query);
			_regular_worker.setQuery(new Query(a_query.getStatement()));
		//}
	}
	
	public void clearViews(){
		_views = new Vector(5);
	}
	
	public void addView(){
		IPView a_view = new IPView(_view_ip_generator.getRandomIPs(_view_generator.getRandomStatement(false)));
		//for(int i = 0; i < _num_algorithms; i++){
		_workers[0].addView(a_view);
		_regular_worker.addView(new View(a_view.getStatement()));
		//}
	}
	
	public double runN(int n){
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
			runOnce();
			//System.out.println("finished one run");
		}
		if (_print_debug_info){
			try {
				_output_file.close();
			}
			catch (IOException e){
				System.out.println("Booger, couldn't close file in ExerimentIPAlgs");
			}
		}
		return total / (double) n;
	}
	
	public ExperimentStatistics[] runAll(String args[]){
		//this is the function to call if you just want to 
		//pretend that you are running it from main; initializes everything,
		//runs it, returns the answer.
		//_timing_results_had_rewriting = new TimingResults();
		//_timing_results_no_rewriting = new TimingResults();
		processArgs(args);
		init();
		runN(_num_runs);
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
	public void runOnce(){
		//returns the amount of time that it took for the experiment to run
		int i;
		int start= 0;
		int stop = _num_algorithms;
		for (i = start; i < stop; i++){
			_workers[i] = null;
		}
		String answer;
		init();            
//		try{
			generateNewQuery();
			for (i = 0; i < _num_views; i++){
				addView();
			}
/*            FileWriter outputqueries = new FileWriter("queries.txt", true);
            outputqueries.write(_workers[0].getQuery().printString() + "\n\n");
            for (i = 0; i < _num_views; i++){
				outputqueries.write(((IPView)_workers[0].getViews().elementAt(i)).printString().toString()+ "\n\n");
            }
			outputqueries.close();
		}//end try
        catch (IOException e){
			System.out.println("couldn't print queries in ExperimetIpAlgs.runOnce()\n\n");
		}
*/		//_workers[0].getQuery().print();
		for (i = start; i < stop; i++){
			System.gc();
			answer   = _workers[i].run();
			_statistics[i].addStatistic(_workers[i].getNumRewritings(),_workers[i].getMappingCreationTime(),
										_workers[i].getMappingCombinationTime());
			System.gc();
			_regular_worker.run();
			_statistics[1].addStatistic(_regular_worker.getNumRewritings(),_regular_worker.getMappingCreationTime(),
										_regular_worker.getMappingCombinationTime());
											
										
			try{
				FileWriter writeAll  = new FileWriter(_directory + "r" + _workers[i].type() + _num_views + ".txt",true);
				writeAll.write(_workers[i].getTotalTime() + "\n");
				writeAll.close();				
				FileWriter writeAll2  = new FileWriter(_directory + "r" + _regular_worker.type() + _num_views + ".txt",true);
				writeAll2.write(_regular_worker.getTotalTime() + "\n");
				writeAll2.close();				
				FileWriter writeAll3  = new FileWriter(_directory + "num" + _workers[i].type() + _num_views + ".txt",true);
				writeAll3.write("num rewrites = "  + Integer.toString(_workers[i].getNumRewritings())   +"\t" + Integer.toString(_workers[i].getNumMCDs()) + "\n");
				writeAll3.close();				
				FileWriter writeAll4  = new FileWriter(_directory + "num" + _regular_worker.type() + _num_views + ".txt",true);
				writeAll4.write("num rewrites = " + Integer.toString( _regular_worker.getNumRewritings()) + "\t" + Integer.toString(_regular_worker.getNumMCDs()) + "\n");
				writeAll4.close();				
			}				
			catch(IOException e){					
				System.out.println("Couldn't open file writeall = " + _directory + "r" + _workers[i].type() + _num_views + ".txt in experimentipalgs.java");	
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
			
		}//end for
	}//end runOnce
	
	public static void main(String args[]) throws IOException{
		Experiment e = new Experiment();
		System.out.println(e.runAll(args));
		System.out.println("done!!!");
		
	}//end main
}

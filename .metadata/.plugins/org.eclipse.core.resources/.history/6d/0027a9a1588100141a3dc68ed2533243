//import java.lang.*;
package minicon;
import java.util.Vector;
import java.io.*;

public class ExperimentSeries{
	//private Experiment _experiment;
	private int _num_reps;
	private String _output_file;
	private String _output_file_2;
	public ExperimentSeries(){
		//at this point, do nothing
		_num_reps = 20;
		//_output_file = new String("g:\\research\\minicon\\out\\out.txt");
		//_output_file_2 = new String("g:\\research\\minicon\\out\\out2.txt");
		_output_file = new String("out.txt");
		_output_file_2 = new String("out2.txt");
	}
	
	public void growNumberViewsAndSizeIP(int num_views_start, int num_views_stop,int skip, String alg_type, int num_distinct, int num_runs, int num_subgoals_start, int num_subgoals_stop, double percent_dist, boolean change_dist){
		//this function takes the current parameters in the experiment series, and
		//starts a new set of experiments that grows the number of predicates
		//from start to stop.  It returns the vector of average time taken
		int i,j,k;
		Query query = null;
		Vector views = null;
		int start_printing = 0;
		int stop_printing = 2;
		//int skip = 5;
		boolean stop_all = false;
		ExperimentIPAlgs new_experiment;
		ExperimentStatistics [] a_result;
		int num_args = 20;
		boolean should_stop = false;
		int num_distinguished;
		String [] input_new = new String[num_args];
		input_new[0] = "-numviews";
		input_new[1] = (new Integer(1)).toString();
		input_new[2] = "-viewshape";
		input_new[3] = alg_type;
		input_new[4] = "-numruns";
		input_new[5] = (new Integer(num_runs)).toString();
		input_new[6] = "-querysize";
		input_new[7] = "6";
		input_new[8] = "-viewsize";
		input_new[9] = "6";
		input_new[10] = "-viewnumdist";
		input_new[11] = (new Integer(num_distinct)).toString();
		//for NumberDistinguished, I'm going to assume that -2 means use
		//all of the non joined ones, and -3 means use all of the joined ones
		//input_new[12] = "-DoOnlyBucketAlgorithm";
		input_new[12] = "-queryshape";
		input_new[13] = alg_type;
		input_new[14] = "-querynumdist";
		input_new[15] = (new Integer(num_distinct)).toString();
		input_new[16] = "-viewfunstop";
		input_new[17] = "30";
		input_new[18] = "-queryfunstop";
		input_new[19] = "30";
		//input_new[20] = "-DoOnlyBucketAlgorithm";
		String a_type;
		FileWriter output;
		FileWriter output2;
		try{
			output = new FileWriter(_output_file,true);
			output2 = new FileWriter(_output_file_2,true);
			//now write the commands to the output file
			for	(k = num_subgoals_start;	k <	num_subgoals_stop; k++){
				input_new[7] = Integer.toString(k);
				input_new[9] = Integer.toString(k);
				input_new[17] = Integer.toString(k + 10);//set the numbers of predicate names we want
				input_new[19] = Integer.toString(k + 10);
				num_distinguished = new Double(k * percent_dist).intValue();
				if (change_dist){
					if (num_distinguished <1){
						num_distinguished = 1;
					}
					input_new[11] = Integer.toString(num_distinguished);
					input_new[15] = Integer.toString(num_distinguished);
				}
				for	(i = num_views_start,	stop_all = false; !stop_all && i < num_views_stop; i+= skip){
					input_new[1] = (new	Integer(i)).toString();
					System.out.println(i);
					new_experiment = new ExperimentIPAlgs();
					//then we want to do the new experiment
					System.out.println("running experiment with " + k + " subgoals and " + i+ " views");
					for (j = 0; j < num_args; j++){
						output2.write(input_new[j]);
						output2.write(" ");
					}
					output2.write("\n");
					output2.flush();

					a_result = new_experiment.runAll(input_new);
					// a_result.getAverage();
					//output.write("new	");
					for	(j = 0;	j <	2; j++){
						
						if (j==0){
							a_type = new_experiment._workers[j].type();
						}
						else{
							a_type ="BucketEnding";
						}
						output.write(a_type);
						output.write("\t");
						output.write(Integer.toString(k));
						output.write("\t");
						output.write((new Integer(i)).toString());
						output.write("\t");
						output.write((new Double(a_result[j].getAverageTotalTime())).toString());
						output.write("\t");
						output.write((new Double(a_result[j].getAverageNoRewritingsTime())).toString());
						output.write("\t");
						output.write((new Double(a_result[j].getAverageHadRewritingsTime())).toString());	
						output.write("\t");
						output.write((new Integer(a_result[j].getNumWithRewritings())).toString());	
						output.write("\t");						//output.write(a_result.getNumRuns());
						//output.write(a_result.getRawResultsString());
						output.flush();
						a_result[j].printAllAverageStatistics(a_type	+ "numviews=" + i	+ "numsubgoals=" +k + ".txt");
						a_result[j].printAllStatistics2(a_type + "numviews" + i + "numsubgoals" +  k + ".txt");
						
						System.out.print(a_type);
						System.out.print("\t");
						System.out.print(i);
						System.out.print("\t");
						System.out.print((new Double(a_result[j].getAverageTotalTime())).toString());
						System.out.print("\t");
						System.out.println(a_result[j].getNumRuns());
						if (a_result[0].getAverageTotalTime() >	100000){
							stop_all = true;
						}
						else{
							should_stop = false;
						}
					}//end looping over j
				}//end looping over i
				//at this point, we need to check to see if we perhaps didn't go anywhere
				/*if (i <= start + skip){
				//then we need to relax the restrictions so we can get some answers
				k--;//decrement k so that we'll try it again
				if (start == 1){
				stop_all = true;
				}
				start -= 100; //move start down so that maybe well get some answers
				//now we need to add a check to make sure that we don't cause an infinite loop
				//at a certain point it may take more than 10 seconds to do anything...
				if (start < 1){
				start = 1;
				}
				}//end if we didn't get any values
				*/
			}//end looping over k
			output.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open output file in ExperimentSeries.growNumViewsAllAlgs()");
			System.exit(1);
		}
		
	}
	public void growNumberViewsAndSizeIPAltered(int num_views_start, int num_views_stop,int skip, String alg_type, int num_distinct, int num_runs, int num_subgoals_start, int num_subgoals_stop, double percent_dist, boolean change_dist){
		//this function takes the current parameters in the experiment series, and
		//starts a new set of experiments that grows the number of predicates
		//from start to stop.  It returns the vector of average time taken
		int i,j,k;
		Query query = null;
		Vector views = null;
		int start_printing = 0;
		int stop_printing = 2;
		//int skip = 5;
		boolean stop_all = false;
		ExperimentIPAlgs new_experiment;
		ExperimentStatistics [] a_result;
		int num_args = 24;
		boolean should_stop = false;
		int num_distinguished;
		String [] input_new = new String[num_args];
		input_new[0] = "-numviews";
		input_new[1] = (new Integer(1)).toString();
		input_new[2] = "-viewshape";
		input_new[3] = alg_type;
		input_new[4] = "-numruns";
		input_new[5] = (new Integer(num_runs)).toString();
		input_new[6] = "-querysize";
		input_new[7] = "6";
		input_new[8] = "-viewsize";
		input_new[9] = "6";
		input_new[10] = "-viewnumdist";
		input_new[11] = (new Integer(num_distinct)).toString();
		//for NumberDistinguished, I'm going to assume that -2 means use
		//all of the non joined ones, and -3 means use all of the joined ones
		//input_new[12] = "-DoOnlyBucketAlgorithm";
		input_new[12] = "-queryshape";
		input_new[13] = alg_type;
		input_new[14] = "-querynumdist";
		input_new[15] = (new Integer(num_distinct)).toString();
		input_new[16] = "-viewfunstop";
		input_new[17] = "30";
		input_new[18] = "-queryfunstop";
		input_new[19] = "30";
		input_new[20] = "-querypredicatesize";
		input_new[21] = "20";
		input_new[22] = "-viewpredicatesize";
		input_new[23] = "20";
	
		//input_new[20] = "-DoOnlyBucketAlgorithm";
		String a_type;
		FileWriter output;
		FileWriter output2;
		try{
			output = new FileWriter(_output_file,true);
			output2 = new FileWriter(_output_file_2,true);
			//now write the commands to the output file
			for	(k = num_subgoals_start;	k <	num_subgoals_stop; k++){
				input_new[7] = Integer.toString(k);
				input_new[9] = Integer.toString(k);
				input_new[17] = Integer.toString(k + 10);//set the numbers of predicate names we want
				input_new[19] = Integer.toString(k + 10);
				num_distinguished = new Double(k * percent_dist).intValue();
				if (change_dist){
					if (num_distinguished <1){
						num_distinguished = 1;
					}
					input_new[11] = Integer.toString(num_distinguished);
					input_new[15] = Integer.toString(num_distinguished);
				}
				for	(i = num_views_start,	stop_all = false; !stop_all && i < num_views_stop; i+= skip){
					input_new[1] = (new	Integer(i)).toString();
					System.out.println(i);
					new_experiment = new ExperimentIPAlgs();
					//then we want to do the new experiment
					System.out.println("running experiment with " + k + " subgoals and " + i+ " views");
					for (j = 0; j < num_args; j++){
						output2.write(input_new[j]);
						output2.write(" ");
					}
					output2.write("\n");
					output2.flush();

					a_result = new_experiment.runAll(input_new);
					// a_result.getAverage();
					//output.write("new	");
					for	(j = 0;	j <	2; j++){
						
						if (j==0){
							a_type = new_experiment._workers[j].type();
						}
						else{
							a_type ="BucketEnding";
						}
						output.write(a_type);
						output.write("\t");
						output.write(Integer.toString(k));
						output.write("\t");
						output.write((new Integer(i)).toString());
						output.write("\t");
						output.write((new Double(a_result[j].getAverageTotalTime())).toString());
						output.write("\t");
						output.write((new Double(a_result[j].getAverageNoRewritingsTime())).toString());
						output.write("\t");
						output.write((new Double(a_result[j].getAverageHadRewritingsTime())).toString());	
						output.write("\t");
						output.write((new Integer(a_result[j].getNumWithRewritings())).toString());	
						//output.write("\t");

						output.write("\n");
						//output.write(a_result.getNumRuns());
						//output.write(a_result.getRawResultsString());
						output.flush();
						a_result[j].printAllAverageStatistics(a_type	+ "numviews=" + i	+ "numsubgoals=" +k + ".txt");
						a_result[j].printAllStatistics2(a_type + "numviews" + i + "numsubgoals" +  k + ".txt");
						
						System.out.print(a_type);
						System.out.print("\t");
						System.out.print(i);
						System.out.print("\t");
						System.out.print((new Double(a_result[j].getAverageTotalTime())).toString());
						System.out.print("\t");
						System.out.println(a_result[j].getNumRuns());
						if (a_result[0].getAverageTotalTime() >	10000){
							stop_all = true;
						}
						else{
							should_stop = false;
						}
					}//end looping over j
				}//end looping over i
				//at this point, we need to check to see if we perhaps didn't go anywhere
				/*if (i <= start + skip){
				//then we need to relax the restrictions so we can get some answers
				k--;//decrement k so that we'll try it again
				if (start == 1){
				stop_all = true;
				}
				start -= 100; //move start down so that maybe well get some answers
				//now we need to add a check to make sure that we don't cause an infinite loop
				//at a certain point it may take more than 10 seconds to do anything...
				if (start < 1){
				start = 1;
				}
				}//end if we didn't get any values
				*/
			}//end looping over k
			output.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open output file in ExperimentSeries.growNumViewsAllAlgs()");
			System.exit(1);
		}
		
	}
public void growNumberViewsAndSizeAll(int num_views_start, int num_views_stop, int skip,String alg_type, int num_distinct, int num_runs, int num_subgoals_start, int num_subgoals_stop, double percent_dist, boolean change_dist){
		//this function takes the current parameters in the experiment series, and
		//starts a new set of experiments that grows the number of predicates
		//from start to stop.  It returns the vector of average time taken
		int i,j,k;
		Query query = null;
		Vector views = null;
		int start_printing = 0;
		int stop_printing = 2;
		//int skip = 5;
		boolean bucket_over = false;
		boolean stop_all = false;
		ExperimentAllNewAlgs new_experiment;
		ExperimentStatistics [] a_result;
		int num_args = 25;
		boolean should_stop = false;
		int num_distinguished;
		String [] input_new = new String[num_args];
		input_new[0] = "-numviews";
		input_new[1] = (new Integer(1)).toString();
		input_new[2] = "-viewshape";
		input_new[3] = alg_type;
		input_new[4] = "-numruns";
		input_new[5] = (new Integer(num_runs)).toString();
		input_new[6] = "-viewsize";
		input_new[7] = "6";
		input_new[8] = "-querysize";
		input_new[9] = "10";
		input_new[10] = "-viewnumdist";
		input_new[11] = (new Integer(num_distinct)).toString();
		//for NumberDistinguished, I'm going to assume that -2 means use
		//all of the non joined ones, and -3 means use all of the joined ones
		//input_new[12] = "-DoOnlyBucketAlgorithm";
		input_new[12] = "-queryshape";
		input_new[13] = alg_type;
		input_new[14] = "-querynumdist";
		input_new[15] = (new Integer(num_distinct)).toString();
		input_new[16] = "-viewfunstop";
		input_new[17] = "2";
		input_new[18] = "-queryfunstop";
		input_new[19] = "2";
		input_new[20] = "-DoInverseRules";
		input_new[21] = "-numqueryvars";
		input_new[22] = "5";
		input_new[23] = "-numviewvars";
		input_new[24] = "5";
		
		//input_new[21] = "-DoBucketAlgorithm";
		//input_new[22] = "-DoOptimum";
		//input_new[21] = "-DoQuasiTree";
		FileWriter output;
		FileWriter output2;
		try{
			output = new FileWriter(_output_file,true);
			output2 = new FileWriter(_output_file_2,true);
			//now write the commands to the output file
			//start off by writing the actual experiment run to the top of output2

			output2.write("Running growNumberViewsAndSizeAll(" + num_views_start + ", " + num_views_stop + ", " + skip + ", " + alg_type + ", " + num_distinct + ", " + num_runs + ", " + num_subgoals_start + ", " + num_subgoals_stop + ", " + percent_dist + ", " + change_dist + ")\n");
			output2.flush();
			for	(k = num_subgoals_start;	k <	num_subgoals_stop; k++){
				input_new[7] = Integer.toString(k);
				input_new[9] = Integer.toString(k);
				input_new[17] = Integer.toString(k + 10);//set the numbers of predicate names we want
				input_new[19] = Integer.toString(k + 10);
				input_new[22] = Integer.toString(k * 5);
				input_new[24] = Integer.toString(k * 5);
				num_distinguished = new Double(k * percent_dist).intValue();
				if (change_dist){
					if (num_distinguished < 1){
						num_distinguished = 1;
					}
					
					input_new[11] = Integer.toString(num_distinguished);
					input_new[15] = Integer.toString(num_distinguished);
				}
				for	(i = num_views_start, stop_all = false; !stop_all && i < num_views_stop; i+= skip){
					input_new[1] = (new	Integer(i)).toString();
					System.out.println(i);
					new_experiment = new ExperimentAllNewAlgs();
					//then we want to do the new experiment
					/*						if (bucket_over == true){
					input_new[21] = "DontDoBucketAlgorithm";
					}
					*/						System.out.println("running experiment with " + k + " subgoals and " + i+ " views");
											for (j = 0; j < num_args; j++){
												output2.write(input_new[j]);
												output2.write(" ");
											}
											output2.write("\n");
											output2.flush();
											a_result = new_experiment.runAll(input_new, !bucket_over);
											// a_result.getAverage();
											//output.write("new	");
											for	(j = 0;	j <	3; j++){
												if (!(j	== 2 &&	bucket_over	== true)){
													output.write(new_experiment._workers[j].type());
													output.write("\t");
													output.write(Integer.toString(k));
													output.write("\t");
													output.write((new Integer(i)).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageTotalTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageNoRewritingsTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageHadRewritingsTime())).toString());	
													output.write("\t");
													output.write((new Integer(a_result[j].getNumWithRewritings())).toString());	
													//output.write("\t");
													output.write("\n");
													//output.write(a_result.getNumRuns());
													//output.write(a_result.getRawResultsString());
													output.flush();
													a_result[j].printAllAverageStatistics(new_experiment._workers[j].type()	+ "numviews=" + i	+ "numsubgoals=" +k + ".txt");
													a_result[j].printAllStatistics2(new_experiment._workers[j].type() + "numviews" + i + "numsubgoals" +  k + ".txt");
													System.out.print(new_experiment._workers[j].type());
													System.out.print("\t");
													System.out.print(i);
													System.out.print("\t");
													System.out.print((new Double(a_result[j].getAverageTotalTime())).toString());
													System.out.print("\t");
													System.out.println(a_result[j].getNumRuns());
													if (j == 0){
														if (a_result[0].getAverageTotalTime() >	10000){
															stop_all = true;
														}
														else{
															should_stop = false;
														}
													}
													if (j == 2 && !bucket_over){
														if (a_result[2].getAverageTotalTime()> 60000){
															//then it's	over my	cut	off	limit
															bucket_over	= true;
															
														}//end if we need to stop running the bucket algorithm
													}//end if we need to check if we're running the bucket algorithm

												}//end if we ran the bucket algorithm
											}//end looping over j
				}//end looping over i
				//at this point, we need to check to see if we perhaps didn't go anywhere
				/*if (i <= num_views_start + skip){
				//then we need to relax the restrictions so we can get some answers
				k--;//decrement k so that we'll try it again
				if (num_views_start == 1){
				stop_all = true;
				}
				start -= 100; //move start down so that maybe well get some answers
				//now we need to add a check to make sure that we don't cause an infinite loop
				//at a certain point it may take more than 10 seconds to do anything...
				if (start < 1){
				start = 1;
				}
				
				}//end if we didn't get any values
				*/
			}//end looping over k
			output.close();
			output2.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open output file in ExperimentSeries.growNumViewsAllAlgs()");
			System.exit(1);
		}
		
	}
public void growNumberViewsAndSizeReplay(int num_views_start, int num_views_stop, int skip,String alg_type, int num_distinct, int num_runs, int num_subgoals_start, int num_subgoals_stop, double percent_dist, boolean change_dist){
		//this function takes the current parameters in the experiment series, and
		//starts a new set of experiments that grows the number of predicates
		//from start to stop.  It returns the vector of average time taken
		int i,j,k;
		Query query = null;
		Vector views = null;
		int start_printing = 0;
		int stop_printing = 2;
		//int skip = 5;
		boolean bucket_over = false;
		boolean stop_all = false;
		ExperimentReplay new_experiment;
		ExperimentStatistics [] a_result;
		int num_args = 7;
		boolean should_stop = false;
		int num_distinguished;
		String [] input_new = new String[num_args];
		input_new[0] = "-numviews";
		input_new[1] = (new Integer(1)).toString();
		input_new[2] = "-viewsize";
		input_new[3] = "6";
		input_new[4] = "-querysize";
		input_new[5] = "10";
		input_new[6] = "-DoInverseRules";
		
		//input_new[21] = "-DoBucketAlgorithm";
		//input_new[22] = "-DoOptimum";
		//input_new[21] = "-DoQuasiTree";
		FileWriter output;
		FileWriter output2;
		try{
			output = new FileWriter(_output_file,true);
			output2 = new FileWriter(_output_file_2,true);
			//now write the commands to the output file
			//start off by writing the actual experiment run to the top of output2

			output2.write("Running growNumberViewsAndSizeAll(" + num_views_start + ", " + num_views_stop + ", " + skip + ", " + alg_type + ", " + num_distinct + ", " + num_runs + ", " + num_subgoals_start + ", " + num_subgoals_stop + ", " + percent_dist + ", " + change_dist + ")\n");
			output2.flush();
			for	(k = num_subgoals_start;	k <	num_subgoals_stop; k++){
				input_new[3] = Integer.toString(k);
				input_new[5] = Integer.toString(k);
				for	(i = num_views_start, stop_all = false; !stop_all && i < num_views_stop; i+= skip){
					input_new[1] = (new	Integer(i)).toString();
					System.out.println(i);
					new_experiment = new ExperimentReplay();
					//then we want to do the new experiment
					/*						if (bucket_over == true){
					input_new[21] = "DontDoBucketAlgorithm";
					}
					*/						System.out.println("running experiment with " + k + " subgoals and " + i+ " views");
											for (j = 0; j < num_args; j++){
												output2.write(input_new[j]);
												output2.write(" ");
											}
											output2.write("\n");
											output2.flush();
											a_result = new_experiment.runAll(input_new, !bucket_over);
											// a_result.getAverage();
											//output.write("new	");
											for	(j = 0;	j <	3; j++){
												if (!(j	== 2 &&	bucket_over	== true)){
													output.write(new_experiment._workers[j].type());
													output.write("\t");
													output.write(Integer.toString(k));
													output.write("\t");
													output.write((new Integer(i)).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageTotalTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageNoRewritingsTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageHadRewritingsTime())).toString());	
													output.write("\t");
													output.write((new Integer(a_result[j].getNumWithRewritings())).toString());	
													//output.write("\t");
													output.write("\n");
													//output.write(a_result.getNumRuns());
													//output.write(a_result.getRawResultsString());
													output.flush();
													a_result[j].printAllAverageStatistics(new_experiment._workers[j].type()	+ "numviews=" + i	+ "numsubgoals=" +k + ".txt");
													a_result[j].printAllStatistics2(new_experiment._workers[j].type() + "numviews" + i + "numsubgoals" +  k + ".txt");
													System.out.print(new_experiment._workers[j].type());
													System.out.print("\t");
													System.out.print(i);
													System.out.print("\t");
													System.out.print((new Double(a_result[j].getAverageTotalTime())).toString());
													System.out.print("\t");
													System.out.println(a_result[j].getNumRuns());
													if (j == 0){
														if (a_result[0].getAverageTotalTime() >	10000){
															stop_all = true;
														}
														else{
															should_stop = false;
														}
													}
													if (j == 2 && !bucket_over){
														if (a_result[2].getAverageTotalTime()> 60000){
															//then it's	over my	cut	off	limit
															bucket_over	= true;
															
														}//end if we need to stop running the bucket algorithm
													}//end if we need to check if we're running the bucket algorithm

												}//end if we ran the bucket algorithm
											}//end looping over j
				}//end looping over i
				//at this point, we need to check to see if we perhaps didn't go anywhere
				/*if (i <= num_views_start + skip){
				//then we need to relax the restrictions so we can get some answers
				k--;//decrement k so that we'll try it again
				if (num_views_start == 1){
				stop_all = true;
				}
				start -= 100; //move start down so that maybe well get some answers
				//now we need to add a check to make sure that we don't cause an infinite loop
				//at a certain point it may take more than 10 seconds to do anything...
				if (start < 1){
				start = 1;
				}
				
				}//end if we didn't get any values
				*/
			}//end looping over k
			output.close();
			output2.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open output file in ExperimentSeries.growNumViewsAllAlgs()");
			System.exit(1);
		}
		
	}
	public void growNumberViewsAndSizeInverse(int num_views_start, int num_views_stop, int skip,String alg_type, int num_distinct, int num_runs, int num_subgoals_start, int num_subgoals_stop, double percent_dist, boolean change_dist){
		//this function takes the current parameters in the experiment series, and
		//starts a new set of experiments that grows the number of predicates
		//from start to stop.  It returns the vector of average time taken
		int i,j,k;
		Query query = null;
		Vector views = null;
		int start_printing = 0;
		int stop_printing = 2;
		//int skip = 5;
		boolean bucket_over = true;
		boolean stop_all = false;
		ExperimentAllNewAlgs new_experiment;
		ExperimentStatistics [] a_result;
		int num_args = 25;
		boolean should_stop = false;
		int num_distinguished;
		String [] input_new = new String[num_args];
		input_new[0] = "-numviews";
		input_new[1] = (new Integer(1)).toString();
		input_new[2] = "-viewshape";
		input_new[3] = alg_type;
		input_new[4] = "-numruns";
		input_new[5] = (new Integer(num_runs)).toString();
		input_new[6] = "-viewsize";
		input_new[7] = "6";
		input_new[8] = "-querysize";
		input_new[9] = "10";
		input_new[10] = "-viewnumdist";
		input_new[11] = (new Integer(num_distinct)).toString();
		//for NumberDistinguished, I'm going to assume that -2 means use
		//all of the non joined ones, and -3 means use all of the joined ones
		//input_new[12] = "-DoOnlyBucketAlgorithm";
		input_new[12] = "-queryshape";
		input_new[13] = alg_type;
		input_new[14] = "-querynumdist";
		input_new[15] = (new Integer(num_distinct)).toString();
		input_new[16] = "-viewfunstop";
		input_new[17] = "2";
		input_new[18] = "-queryfunstop";
		input_new[19] = "2";
		input_new[20] = "-DoInverseRules";
		input_new[21] = "-numqueryvars";
		input_new[22] = "5";
		input_new[23] = "-numviewvars";
		input_new[24] = "5";
		
		//input_new[21] = "-DoBucketAlgorithm";
		//input_new[22] = "-DoOptimum";
		//input_new[21] = "-DoQuasiTree";
		FileWriter output;
		FileWriter output2;
		try{
			output = new FileWriter(_output_file,true);
			output2 = new FileWriter(_output_file_2,true);
			//now write the commands to the output file
			//start off by writing the actual experiment run to the top of output2

			output2.write("Running growNumberViewsAndSizeInverse(" + num_views_start + ", " + num_views_stop + ", " + skip + ", " + alg_type + ", " + num_distinct + ", " + num_runs + ", " + num_subgoals_start + ", " + num_subgoals_stop + ", " + percent_dist + ", " + change_dist + ")\n");
			output2.flush();
			for	(k = num_subgoals_start;	k <	num_subgoals_stop; k++){
				input_new[7] = Integer.toString(k);
				input_new[9] = Integer.toString(k);
				input_new[17] = Integer.toString(k + 10);//set the numbers of predicate names we want
				input_new[19] = Integer.toString(k + 10);
				input_new[22] = Integer.toString(k * 5);
				input_new[24] = Integer.toString(k * 5);
				num_distinguished = new Double(k * percent_dist).intValue();
				if (change_dist){
					if (num_distinguished < 1){
						num_distinguished = 1;
					}
					
					input_new[11] = Integer.toString(num_distinguished);
					input_new[15] = Integer.toString(num_distinguished);
				}
				for	(i = num_views_start, stop_all = false, bucket_over = true; !stop_all && i < num_views_stop; i+= skip){
					input_new[1] = (new	Integer(i)).toString();
					System.out.println(i);
					new_experiment = new ExperimentAllNewAlgs();
					//then we want to do the new experiment
					/*						if (bucket_over == true){
					input_new[21] = "DontDoBucketAlgorithm";
					}
					*/						System.out.println("running experiment with " + k + " subgoals and " + i+ " views");
											for (j = 0; j < num_args; j++){
												output2.write(input_new[j]);
												output2.write(" ");
											}
											output2.write("\n");
											output2.flush();
											a_result = new_experiment.runAll(input_new, !bucket_over);
											// a_result.getAverage();
											//output.write("new	");
											for	(j = 0;	j <	3; j++){
												if (!(j	== 2 &&	bucket_over	== true)){
													output.write(new_experiment._workers[j].type());
													output.write("\t");
													output.write(Integer.toString(k));
													output.write("\t");
													output.write((new Integer(i)).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageTotalTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageNoRewritingsTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageHadRewritingsTime())).toString());	
													output.write("\t");
													output.write((new Integer(a_result[j].getNumWithRewritings())).toString());	
													//output.write("\t");
													output.write("\n");
													//output.write(a_result.getNumRuns());
													//output.write(a_result.getRawResultsString());
													output.flush();
													a_result[j].printAllAverageStatistics(new_experiment._workers[j].type()	+ "numviews=" + i	+ "numsubgoals=" +k + ".txt");
													a_result[j].printAllStatistics2(new_experiment._workers[j].type() + "numviews" + i + "numsubgoals" +  k + ".txt");
													System.out.print(new_experiment._workers[j].type());
													System.out.print("\t");
													System.out.print(i);
													System.out.print("\t");
													System.out.print((new Double(a_result[j].getAverageTotalTime())).toString());
													System.out.print("\t");
													System.out.println(a_result[j].getNumRuns());
													if (j == 0){
														if (a_result[0].getAverageTotalTime() >	10000){
															stop_all = true;
														}
														else{
															should_stop = false;
														}
													}
													if (j == 1 && !bucket_over){
														if (a_result[1].getAverageTotalTime()> 60000){
															//then it's	over my	cut	off	limit
															bucket_over	= true;
															
														}//end if we need to stop running the bucket algorithm
													}//end if we need to check if we're running the bucket algorithm

												}//end if we ran the bucket algorithm
											}//end looping over j
				}//end looping over i
				//at this point, we need to check to see if we perhaps didn't go anywhere
				/*if (i <= num_views_start + skip){
				//then we need to relax the restrictions so we can get some answers
				k--;//decrement k so that we'll try it again
				if (num_views_start == 1){
				stop_all = true;
				}
				start -= 100; //move start down so that maybe well get some answers
				//now we need to add a check to make sure that we don't cause an infinite loop
				//at a certain point it may take more than 10 seconds to do anything...
				if (start < 1){
				start = 1;
				}
				
				}//end if we didn't get any values
				*/
			}//end looping over k
			output.close();
			output2.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open output file in ExperimentSeries.growNumViewsAllAlgs()");
			System.exit(1);
		}
		
	}
	public void growNumberViewsAndSizeInverseChanged(int num_views_start, int num_views_stop, int skip,String alg_type, int num_distinct, int num_runs, int num_subgoals_start, int num_subgoals_stop, double percent_dist, boolean change_dist){
		//this function takes the current parameters in the experiment series, and
		//starts a new set of experiments that grows the number of predicates
		//from start to stop.  It returns the vector of average time taken
		int i,j,k;
		Query query = null;
		Vector views = null;
		int start_printing = 0;
		int stop_printing = 2;
		//int skip = 5;
		boolean bucket_over = true;
		boolean stop_all = false;
		ExperimentAllNewAlgs new_experiment;
		ExperimentStatistics [] a_result;
		int num_args = 25;
		boolean should_stop = false;
		int num_distinguished;
		String [] input_new = new String[num_args];
		input_new[0] = "-numviews";
		input_new[1] = (new Integer(1)).toString();
		input_new[2] = "-viewshape";
		input_new[3] = alg_type;
		input_new[4] = "-numruns";
		input_new[5] = (new Integer(num_runs)).toString();
		input_new[6] = "-viewsize";
		input_new[7] = "5";
		input_new[8] = "-querysize";
		input_new[9] = "5";
		input_new[10] = "-viewnumdist";
		input_new[11] = (new Integer(num_distinct)).toString();
		//for NumberDistinguished, I'm going to assume that -2 means use
		//all of the non joined ones, and -3 means use all of the joined ones
		//input_new[12] = "-DoOnlyBucketAlgorithm";
		input_new[12] = "-queryshape";
		input_new[13] = alg_type;
		input_new[14] = "-querynumdist";
		input_new[15] = (new Integer(num_distinct)).toString();
		input_new[16] = "-viewfunstop";
		input_new[17] = "2";
		input_new[18] = "-queryfunstop";
		input_new[19] = "2";
		input_new[20] = "-DoInverseRules";
		input_new[21] = "-numqueryvars";
		input_new[22] = "20";
		input_new[23] = "-numviewvars";
		input_new[24] = "20";
		
		//input_new[21] = "-DoBucketAlgorithm";
		//input_new[22] = "-DoOptimum";
		//input_new[21] = "-DoQuasiTree";
		FileWriter output;
		FileWriter output2;
		try{
			output = new FileWriter(_output_file,true);
			output2 = new FileWriter(_output_file_2,true);
			//now write the commands to the output file
			//start off by writing the actual experiment run to the top of output2

			output2.write("Running growNumberViewsAndSizeInverse(" + num_views_start + ", " + num_views_stop + ", " + skip + ", " + alg_type + ", " + num_distinct + ", " + num_runs + ", " + num_subgoals_start + ", " + num_subgoals_stop + ", " + percent_dist + ", " + change_dist + ")\n");
			output2.flush();
			for	(k = num_subgoals_start;	k <	num_subgoals_stop; k++){
				input_new[7] = Integer.toString(k);
				input_new[9] = Integer.toString(k);
				input_new[17] = Integer.toString(k + 10);//set the numbers of predicate names we want
				input_new[19] = Integer.toString(k + 10);
				input_new[22] = Integer.toString(k * 5);
				input_new[24] = Integer.toString(k * 5);
				num_distinguished = new Double(k * percent_dist).intValue();
				if (change_dist){
					if (num_distinguished < 1){
						num_distinguished = 1;
					}
					
					input_new[11] = Integer.toString(num_distinguished);
					input_new[15] = Integer.toString(num_distinguished);
				}
				for	(i = num_views_start, stop_all = false, bucket_over = true; !stop_all && i < num_views_stop; i+= skip){
					input_new[1] = (new	Integer(i)).toString();
					System.out.println(i);
					new_experiment = new ExperimentAllNewAlgs();
					//then we want to do the new experiment
					/*						if (bucket_over == true){
					input_new[21] = "DontDoBucketAlgorithm";
					}
					*/						System.out.println("running experiment with " + k + " subgoals and " + i+ " views");
											for (j = 0; j < num_args; j++){
												output2.write(input_new[j]);
												output2.write(" ");
											}
											output2.write("\n");
											output2.flush();
											a_result = new_experiment.runAll(input_new, !bucket_over);
											// a_result.getAverage();
											//output.write("new	");
											for	(j = 0;	j <	3; j++){
												if (!(j	== 2 &&	bucket_over	== true)){
													output.write(new_experiment._workers[j].type());
													output.write("\t");
													output.write(Integer.toString(k));
													output.write("\t");
													output.write((new Integer(i)).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageTotalTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageNoRewritingsTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageHadRewritingsTime())).toString());	
													output.write("\t");
													output.write((new Integer(a_result[j].getNumWithRewritings())).toString());	
													//output.write("\t");
													output.write("\n");
													//output.write(a_result.getNumRuns());
													//output.write(a_result.getRawResultsString());
													output.flush();
													a_result[j].printAllAverageStatistics(new_experiment._workers[j].type()	+ "numviews=" + i	+ "numsubgoals=" +k + ".txt");
													a_result[j].printAllStatistics2(new_experiment._workers[j].type() + "numviews" + i + "numsubgoals" +  k + ".txt");
													System.out.print(new_experiment._workers[j].type());
													System.out.print("\t");
													System.out.print(i);
													System.out.print("\t");
													System.out.print((new Double(a_result[j].getAverageTotalTime())).toString());
													System.out.print("\t");
													System.out.println(a_result[j].getNumRuns());
													if (j == 0){
														if (a_result[0].getAverageTotalTime() >	60000){
															stop_all = true;
														}
														else{
															should_stop = false;
														}
													}
													if (j == 1 && !bucket_over){
														if (a_result[1].getAverageTotalTime()> 60000){
															//then it's	over my	cut	off	limit
															bucket_over	= true;
															
														}//end if we need to stop running the bucket algorithm
													}//end if we need to check if we're running the bucket algorithm

												}//end if we ran the bucket algorithm
											}//end looping over j
				}//end looping over i
				//at this point, we need to check to see if we perhaps didn't go anywhere
				/*if (i <= num_views_start + skip){
				//then we need to relax the restrictions so we can get some answers
				k--;//decrement k so that we'll try it again
				if (num_views_start == 1){
				stop_all = true;
				}
				start -= 100; //move start down so that maybe well get some answers
				//now we need to add a check to make sure that we don't cause an infinite loop
				//at a certain point it may take more than 10 seconds to do anything...
				if (start < 1){
				start = 1;
				}
				
				}//end if we didn't get any values
				*/
			}//end looping over k
			output.close();
			output2.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open output file in ExperimentSeries.growNumViewsAllAlgs()");
			System.exit(1);
		}
		
	}
	public void growNumberViewsAndSizeInverseChangeViewLength(int num_views_start, int num_views_stop, int skip,String alg_type, int num_distinct, int num_runs, int num_subgoals_start, int num_subgoals_stop, double percent_dist, boolean change_dist, int start_num_view_subgoals, int stop_num_view_subgoals){
		//this function takes the current parameters in the experiment series, and
		//starts a new set of experiments that grows the number of predicates
		//from start to stop.  It returns the vector of average time taken
		int i,j,k;
		Query query = null;
		Vector views = null;
		int start_printing = 0;
		int stop_printing = 2;
		//int skip = 5;
		boolean bucket_over = true;
		boolean stop_all = false;
		ExperimentAllNewAlgs new_experiment;
		ExperimentStatistics [] a_result;
		int num_args = 25;
		boolean should_stop = false;
		int num_distinguished;
		String [] input_new = new String[num_args];
		input_new[0] = "-numviews";
		input_new[1] = (new Integer(1)).toString();
		input_new[2] = "-viewshape";
		input_new[3] = alg_type;
		input_new[4] = "-numruns";
		input_new[5] = (new Integer(num_runs)).toString();
		input_new[6] = "-viewsize";
		input_new[7] = "6";
		input_new[8] = "-querysize";
		input_new[9] = "10";
		input_new[10] = "-viewnumdist";
		input_new[11] = (new Integer(num_distinct)).toString();
		//for NumberDistinguished, I'm going to assume that -2 means use
		//all of the non joined ones, and -3 means use all of the joined ones
		//input_new[12] = "-DoOnlyBucketAlgorithm";
		input_new[12] = "-queryshape";
		input_new[13] = alg_type;
		input_new[14] = "-querynumdist";
		input_new[15] = (new Integer(num_distinct)).toString();
		input_new[16] = "-viewfunstop";
		input_new[17] = "2";
		input_new[18] = "-queryfunstop";
		input_new[19] = "2";
		input_new[20] = "-DoInverseRules";
		input_new[21] = "-numqueryvars";
		input_new[22] = "5";
		input_new[23] = "-numviewvars";
		input_new[24] = "5";
		
		//input_new[21] = "-DoBucketAlgorithm";
		//input_new[22] = "-DoOptimum";
		//input_new[21] = "-DoQuasiTree";
		FileWriter output;
		FileWriter output2;
		try{
			output = new FileWriter(_output_file,true);
			output2 = new FileWriter(_output_file_2,true);
			//now write the commands to the output file
			//start off by writing the actual experiment run to the top of output2

			output2.write("Running growNumberViewsAndSizeInverseChangeViewLength(" + num_views_start + ", " + num_views_stop + ", " + skip + ", " + alg_type + ", " + num_distinct + ", " + num_runs + ", " + num_subgoals_start + ", " + num_subgoals_stop + ", " + percent_dist + ", " + change_dist + ")\n");
			output2.flush();
			for	(k = num_subgoals_start;	k <	num_subgoals_stop; k++){
				input_new[7] = Integer.toString(k);
				input_new[9] = Integer.toString(k);
				input_new[17] = Integer.toString(k + 10);//set the numbers of predicate names we want
				input_new[19] = Integer.toString(k + 10);
				input_new[22] = Integer.toString(k * 5);
				input_new[24] = Integer.toString(k * 5);
				num_distinguished = new Double(k * percent_dist).intValue();
				if (change_dist){
					if (num_distinguished < 1){
						num_distinguished = 1;
					}
					
					input_new[11] = Integer.toString(num_distinguished);
					input_new[15] = Integer.toString(num_distinguished);
				}
				for	(i = num_views_start, stop_all = false, bucket_over = true; !stop_all && i < num_views_stop; i+= skip){
					input_new[1] = (new	Integer(i)).toString();
					System.out.println(i);
					new_experiment = new ExperimentAllNewAlgs();
					//then we want to do the new experiment
					/*						if (bucket_over == true){
					input_new[21] = "DontDoBucketAlgorithm";
					}
					*/						System.out.println("running experiment with " + k + " subgoals and " + i+ " views");
											for (j = 0; j < num_args; j++){
												output2.write(input_new[j]);
												output2.write(" ");
											}
											output2.write("\n");
											output2.flush();
											a_result = new_experiment.runAll234(input_new, !bucket_over);
											// a_result.getAverage();
											//output.write("new	");
											for	(j = 0;	j <	3; j++){
												if (!(j	== 2 &&	bucket_over	== true)){
													output.write(new_experiment._workers[j].type());
													output.write("\t");
													output.write(Integer.toString(k));
													output.write("\t");
													output.write((new Integer(i)).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageTotalTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageNoRewritingsTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageHadRewritingsTime())).toString());	
													output.write("\t");
													output.write((new Integer(a_result[j].getNumWithRewritings())).toString());	
													//output.write("\t");
													output.write("\n");
													//output.write(a_result.getNumRuns());
													//output.write(a_result.getRawResultsString());
													output.flush();
													a_result[j].printAllAverageStatistics(new_experiment._workers[j].type()	+ "numviews=" + i	+ "numsubgoals=" +k + ".txt");
													a_result[j].printAllStatistics2(new_experiment._workers[j].type() + "numviews" + i + "numsubgoals" +  k + ".txt");
													System.out.print(new_experiment._workers[j].type());
													System.out.print("\t");
													System.out.print(i);
													System.out.print("\t");
													System.out.print((new Double(a_result[j].getAverageTotalTime())).toString());
													System.out.print("\t");
													System.out.println(a_result[j].getNumRuns());
													if (j == 0){
														if (a_result[0].getAverageTotalTime() >	10000){
															stop_all = true;
														}
														else{
															should_stop = false;
														}
													}
													if (j == 1 && !bucket_over){
														if (a_result[1].getAverageTotalTime()> 60000){
															//then it's	over my	cut	off	limit
															bucket_over	= true;
															
														}//end if we need to stop running the bucket algorithm
													}//end if we need to check if we're running the bucket algorithm

												}//end if we ran the bucket algorithm
											}//end looping over j
				}//end looping over i
				//at this point, we need to check to see if we perhaps didn't go anywhere
				/*if (i <= num_views_start + skip){
				//then we need to relax the restrictions so we can get some answers
				k--;//decrement k so that we'll try it again
				if (num_views_start == 1){
				stop_all = true;
				}
				start -= 100; //move start down so that maybe well get some answers
				//now we need to add a check to make sure that we don't cause an infinite loop
				//at a certain point it may take more than 10 seconds to do anything...
				if (start < 1){
				start = 1;
				}
				
				}//end if we didn't get any values
				*/
			}//end looping over k
			output.close();
			output2.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open output file in ExperimentSeries.growNumViewsAllAlgs()");
			System.exit(1);
		}
		
	}
	public void growNumberViewsAndSizeBucket(int num_views_start, int num_views_stop, int skip,String alg_type, int num_distinct, int num_runs, int num_subgoals_start, int num_subgoals_stop, double percent_dist, boolean change_dist){
		//this function takes the current parameters in the experiment series, and
		//starts a new set of experiments that grows the number of predicates
		//from start to stop.  It returns the vector of average time taken
		int i,j,k;
		Query query = null;
		Vector views = null;
		int start_printing = 0;
		int stop_printing = 2;
		//int skip = 5;
		boolean bucket_over = true;
		boolean stop_all = false;
		ExperimentAllNewAlgs new_experiment;
		ExperimentStatistics [] a_result;
		int num_args = 25;
		boolean should_stop = false;
		int num_distinguished;
		String [] input_new = new String[num_args];
		input_new[0] = "-numviews";
		input_new[1] = (new Integer(1)).toString();
		input_new[2] = "-viewshape";
		input_new[3] = alg_type;
		input_new[4] = "-numruns";
		input_new[5] = (new Integer(num_runs)).toString();
		input_new[6] = "-viewsize";
		input_new[7] = "6";
		input_new[8] = "-querysize";
		input_new[9] = "6";
		input_new[10] = "-viewnumdist";
		input_new[11] = (new Integer(num_distinct)).toString();
		//for NumberDistinguished, I'm going to assume that -2 means use
		//all of the non joined ones, and -3 means use all of the joined ones
		//input_new[12] = "-DoOnlyBucketAlgorithm";
		input_new[12] = "-queryshape";
		input_new[13] = alg_type;
		input_new[14] = "-querynumdist";
		input_new[15] = (new Integer(num_distinct)).toString();
		input_new[16] = "-viewfunstop";
		input_new[17] = "2";
		input_new[18] = "-queryfunstop";
		input_new[19] = "2";
		input_new[20] = "-DoOnlyBucketAlgorithm";
		input_new[21] = "-numqueryvars";
		input_new[22] = "5";
		input_new[23] = "-numviewvars";
		input_new[24] = "5";
		//input_new[25] = "-DoInverseRules";
		
		//input_new[21] = "-DoBucketAlgorithm";
		//input_new[22] = "-DoOptimum";
		//input_new[21] = "-DoQuasiTree";
		FileWriter output;
		FileWriter output2;
		try{
			output = new FileWriter(_output_file,true);
			output2 = new FileWriter(_output_file_2,true);
			//now write the commands to the output file
			output2.write("Running growNumberViewsAndSizeBucket(" + num_views_start + ", " + num_views_stop + ", " + skip + ", " + alg_type + ", " + num_distinct + ", " + num_runs + ", " + num_subgoals_start + ", " + num_subgoals_stop + ", " + percent_dist + ", " + change_dist + ")\n");
			output2.flush();

			for	(k = num_subgoals_start;	k <	num_subgoals_stop; k++){
				input_new[7] = Integer.toString(k);
				input_new[9] = Integer.toString(k);
				input_new[17] = Integer.toString(k + 10);//set the numbers of predicate names we want
				input_new[19] = Integer.toString(k + 10);
				input_new[22] = Integer.toString(k * 5);
				input_new[24] = Integer.toString(k * 5);
				num_distinguished = new Double(k * percent_dist).intValue();
				if (change_dist){
					if (num_distinguished < 1){
						num_distinguished = 1;
					}
					
					input_new[11] = Integer.toString(num_distinguished);
					input_new[15] = Integer.toString(num_distinguished);
				}
				for	(i = num_views_start, stop_all = false, bucket_over = false; !stop_all && i < num_views_stop; i+= skip){
					input_new[1] = (new	Integer(i)).toString();
					System.out.println(i);
					new_experiment = new ExperimentAllNewAlgs();
					//then we want to do the new experiment
					if (bucket_over == true){
						input_new[20] = "DontDoBucketAlgorithm";
					}
											System.out.println("running experiment with " + k + " subgoals and " + i+ " views");
											for (j = 0; j < num_args; j++){
												output2.write(input_new[j]);
												output2.write(" ");
											}
											output2.write("\n");
											output2.flush();
											a_result = new_experiment.runAll(input_new, !bucket_over);
											// a_result.getAverage();
											//output.write("new	");
											for	(j = 2;	j <	3; j++){
												if (!(j == 2 && bucket_over == true)){
													output.write(new_experiment._workers[j].type());
													output.write("\t");
													output.write(Integer.toString(k));
													output.write("\t");
													output.write((new Integer(i)).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageTotalTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageNoRewritingsTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageHadRewritingsTime())).toString());	
													output.write("\t");
													output.write((new Integer(a_result[j].getNumWithRewritings())).toString());	
													//output.write("\t");
													output.write("\n");
													//output.write(a_result.getNumRuns());
													//output.write(a_result.getRawResultsString());
													output.flush();
													a_result[j].printAllAverageStatistics(new_experiment._workers[j].type()	+ "numviews=" + i	+ "numsubgoals=" +k + ".txt");
													a_result[j].printAllStatistics2(new_experiment._workers[j].type() + "numviews" + i + "numsubgoals" +  k + ".txt");
													System.out.print(new_experiment._workers[j].type());
													System.out.print("\t");
													System.out.print(i);
													System.out.print("\t");
													System.out.print((new Double(a_result[j].getAverageTotalTime())).toString());
													System.out.print("\t");
													System.out.println(a_result[j].getNumRuns());
													if (j == 0){
														if (a_result[0].getAverageTotalTime() >	10000){
															stop_all = true;
														}
														else{
															should_stop = false;
														}
													}
													if (j == 1 && !bucket_over){
														if (a_result[1].getAverageTotalTime()> 60000){
															//then it's	over my	cut	off	limit
															bucket_over	= true;
															
														}//end if we need to stop running the bucket algorithm
													}//end if we need to check if we're running the bucket algorithm

												}//end if we ran the bucket algorithm
											}//end looping over j
				}//end looping over i
				//at this point, we need to check to see if we perhaps didn't go anywhere
				/*if (i <= num_views_start + skip){
				//then we need to relax the restrictions so we can get some answers
				k--;//decrement k so that we'll try it again
				if (num_views_start == 1){
				stop_all = true;
				}
				start -= 100; //move start down so that maybe well get some answers
				//now we need to add a check to make sure that we don't cause an infinite loop
				//at a certain point it may take more than 10 seconds to do anything...
				if (start < 1){
				start = 1;
				}
				
				}//end if we didn't get any values
				*/
			}//end looping over k
			output.close();
			output2.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open output file in ExperimentSeries.growNumViewsAllAlgs()");
			System.exit(1);
		}
		
	}
	public void growNumberViewsAndSizeDuplicate(int num_views_start, int num_views_stop, int skip,String alg_type, int num_distinct, int num_runs, int num_subgoals_start, int num_subgoals_stop, int num_query_dups,int num_view_dups){
		//this function takes the current parameters in the experiment series, and
		//starts a new set of experiments that grows the number of predicates
		//from start to stop.  It returns the vector of average time taken
		int i,j,k;
		Query query = null;
		Vector views = null;
		int start_printing = 0;
		int stop_printing = 2;
		//int skip = 5;
		boolean bucket_over = true;
		boolean stop_all = false;
		ExperimentAllNewAlgs new_experiment;
		ExperimentStatistics [] a_result;
		int num_args = 30;
		boolean should_stop = false;
		int num_distinguished;
		String [] input_new = new String[num_args];
		input_new[0] = "-numviews";
		input_new[1] = (new Integer(1)).toString();
		input_new[2] = "-viewshape";
		input_new[3] = alg_type;
		input_new[4] = "-numruns";
		input_new[5] = (new Integer(num_runs)).toString();
		input_new[6] = "-viewsize";
		input_new[7] = "6";
		input_new[8] = "-querysize";
		input_new[9] = "6";
		input_new[10] = "-viewnumdist";
		input_new[11] = (new Integer(num_distinct)).toString();
		//for NumberDistinguished, I'm going to assume that -2 means use
		//all of the non joined ones, and -3 means use all of the joined ones
		//input_new[12] = "-DoOnlyBucketAlgorithm";
		input_new[12] = "-queryshape";
		input_new[13] = alg_type;
		input_new[14] = "-querynumdist";
		input_new[15] = (new Integer(num_distinct)).toString();
		input_new[16] = "-viewfunstop";
		input_new[17] = "2";
		input_new[18] = "-queryfunstop";
		input_new[19] = "2";
		input_new[20] = "-DoInverseRules";
		input_new[21] = "-numqueryvars";
		input_new[22] = "5";
		input_new[23] = "-numviewvars";
		input_new[24] = "5";
		input_new[25] = "-numqueryfunctiondups";
		input_new[26] = (new Integer(num_query_dups)).toString();
		input_new[27] = "-numviewfunctiondups";
		input_new[28] = (new Integer(num_view_dups)).toString();
		input_new[29] = "-DoBucketAlgorithm";
		//input_new[22] = "-DoOptimum";
		//input_new[21] = "-DoQuasiTree";
		FileWriter output;
		FileWriter output2;
		try{
			output = new FileWriter(_output_file,true);
			output2 = new FileWriter(_output_file_2,true);
			//now write the commands to the output file
			for	(k = num_subgoals_start;	k <	num_subgoals_stop; k++){
				input_new[7] = Integer.toString(k);
				input_new[9] = Integer.toString(k);
				input_new[17] = Integer.toString(k + 10);//set the numbers of predicate names we want
				input_new[19] = Integer.toString(k + 10);
				input_new[22] = Integer.toString(k * 5);
				input_new[24] = Integer.toString(k * 5);
				//num_distinguished = new Double(k * .30).intValue();
				//input_new[11] = Integer.toString(num_distinguished);
				//input_new[15] = Integer.toString(num_distinguished);
				for	(i = num_views_start, stop_all = false, bucket_over = true; !stop_all && i < num_views_stop; i+= skip){
					input_new[1] = (new	Integer(i)).toString();
					System.out.println(i);
					new_experiment = new ExperimentAllNewAlgs();
					//then we want to do the new experiment
					/*						if (bucket_over == true){
					input_new[21] = "DontDoBucketAlgorithm";
					}
					*/						System.out.println("running experiment with " + k + " subgoals and " + i+ " views");
											for (j = 0; j < num_args; j++){
												output2.write(input_new[j]);
												output2.write(" ");
											}
											output2.write("\n");
											output2.flush();
											a_result = new_experiment.runAll(input_new, !bucket_over);
											// a_result.getAverage();
											//output.write("new	");
											for	(j = 0;	j <	3; j++){
												if (!(j	== 2 &&	bucket_over	== true)){
													output.write(new_experiment._workers[j].type());
													output.write("\t");
													output.write(Integer.toString(k));
													output.write("\t");
													output.write((new Integer(i)).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageTotalTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageNoRewritingsTime())).toString());
													output.write("\t");
													output.write((new Double(a_result[j].getAverageHadRewritingsTime())).toString());	
													output.write("\t");
													output.write((new Integer(a_result[j].getNumWithRewritings())).toString());	
													//output.write("\t");
													output.write("\n");
													//output.write(a_result.getNumRuns());
													//output.write(a_result.getRawResultsString());
													output.flush();
													a_result[j].printAllAverageStatistics(new_experiment._workers[j].type()	+ "numviews=" + i	+ "numsubgoals=" +k + ".txt");
													//a_result[j].printAllStatistics(new_experiment._workers[j].type() + "numviews" + i + "numsubgoals" +  k + ".txt");
													System.out.print(new_experiment._workers[j].type());
													System.out.print("\t");
													System.out.print(i);
													System.out.print("\t");
													System.out.print((new Double(a_result[j].getAverageTotalTime())).toString());
													System.out.print("\t");
													System.out.println(a_result[j].getNumRuns());
													if (j == 0){
														if (a_result[0].getAverageTotalTime() >	10000){
															stop_all = true;
														}
														else{
															should_stop = false;
														}
													}
													if (j == 1 && !bucket_over){
														if (a_result[1].getAverageTotalTime()> 60000){
															//then it's	over my	cut	off	limit
															bucket_over	= true;
															
														}//end if we need to stop running the bucket algorithm
													}//end if we need to check if we're running the bucket algorithm

												}//end if we ran the bucket algorithm
											}//end looping over j
				}//end looping over i
				//at this point, we need to check to see if we perhaps didn't go anywhere
				/*if (i <= num_views_start + skip){
				//then we need to relax the restrictions so we can get some answers
				k--;//decrement k so that we'll try it again
				if (num_views_start == 1){
				stop_all = true;
				}
				start -= 100; //move start down so that maybe well get some answers
				//now we need to add a check to make sure that we don't cause an infinite loop
				//at a certain point it may take more than 10 seconds to do anything...
				if (start < 1){
				start = 1;
				}
				
				}//end if we didn't get any values
				*/
			}//end looping over k
			output.close();
			output2.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open output file in ExperimentSeries.growNumViewsAllAlgs()");
			System.exit(1);
		}
		
	}
	
	public static void main(String args[]) throws IOException{
		//int	start =	2000;
		int skip = 20;
		ExperimentSeries e = new ExperimentSeries();
		//e.growNumberViewsAndSizeDuplicate(1,40,5,"Random",3,20,4,10,1,1);
		//e.growNumberViewsAndSizeDuplicate(1,10,1,"Star",-3,20,4,10,3,3);
		//e.growNumberViewsAndSizeIP(1,100,1,"Star",-2,25,5,10,.5,false);
		//e.growNumberViewsAndSizeIP100,1,"Star",3,20,5,10);
		
		//note, we're now only changing *view* size
		//e.growNumberViewsAndSizeInverse(20,21,1,"Star",-2,2000,5,20,.35,false);
		//e.growNumberViewsAndSizeBucket(1,20,1,"Star",-2,20,10,11,.35,false);//used for star?
		//e.growNumberViewsAndSizeBucket(1,20,1,"Chain",2,30,10,11,.35,false);//used for chain2
		//e.growNumberViewsAndSizeInverse(1,20,1,"Chain",0,20,5,6,.35,false);//used for chainall?
		//e.growNumberViewsAndSizeInverse(1,40,1,"Full",4,500,10,11,.35, true);//used for full?
		
		//e.growNumberViewsAndSizeInverse(1,40,2,"Full",4,10,10,11,.35, true);//used for full?
		//System.out.println("got here!!!!");
		//e.growNumberViewsAndSizeInverse(1,50,1,"Full",3,1000,10,11,.35, false);//just used for new full
		//e.growNumberViewsAndSizeInverse(1,15,1,"Chain",0,100,5,6,.35,false);//just used for new chain2//run on kimtah
		//e.growNumberViewsAndSizeInverse(1,400,1,"Chain",2,100,10,11,.35,false);//just used for new chain2
		//e.growNumberViewsAndSizeBucket(8,20,1,"Star",-2,50,10,11,.35,false);//just used for first half of star2
		//e.growNumberViewsAndSizeInverse(10,20,1,"Star",-2,2000,10,11,.35,false);
		//e.growNumberViewsAndSizeAll(1,20,1,"Chain",2,3,3,4,.35,false);//doesn't quite work
		//e.growNumberViewsAndSizeAll(1,10,1,"Chain",0,50,5,6,.35,false);
		//e.growNumberViewsAndSizeAll(1,10,1,"Chain",0,75,5,6,.35,false);		//e.growNumberViewsAndSizeIP(1,400,5,"Chain",2,50,10,11,.35,false);
		//e.growNumberViewsAndSizeIPAltered(1,14,1,"Chain",0,50,3,4,.35,false);
		e.growNumberViewsAndSizeInverseChanged(1,15,1,"Chain",0,100,8,9,.35,false);//run on bach
		//e.growNumberViewsAndSizeReplay(1,15,1,"Chain",0,100,8,9,.35,false);//run on bach
		//e.growNumberViewsAndSizeIP(1,400,10,"Chain",2,100,10,11,.35,false);
						
		//e.growNumberViewsAndSizeInverseChangeViewLength(200,400,5,"Chain",2,100,12,13,.35,false,2,4);
		//e.growNumberViewsAndSizeIP(5,1,"Chain",2);
		//e.growNumberViewsAndSizeIP(5,1,"Chain",0);
		//e.growNumberViewsAndSizeIP(5,1,"Star",-2);
		//e.growNumberViewsAndSizeIP(5,1,"Star",-3);		e.growNumberViewsAndSizeIPAltered(1,400,5,"Chain",2,50,10,11,.35,false);
	}
}

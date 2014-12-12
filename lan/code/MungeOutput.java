package minicon;
import java.io.*;
import java.util.StringTokenizer;
public class MungeOutput
{

    int _num_runs;
    int _num_algs;
    String [] _file_prefixes;
    public MungeOutput(int a_num){
	   _num_runs = a_num;
	   _num_algs = 4;
	   _file_prefixes = new String[_num_algs];
	   _file_prefixes[0] = "BucketEnding";
	   _file_prefixes[1] = "ExtendedMapping";
	   _file_prefixes[2] = "NoExistential";
	   _file_prefixes[3] = "OriginalNew";
    }

	public void printNumRewritings(){
		int i;
		int j;
		int k;
		FileWriter output;
		FileWriter gnuplot_file;
		BufferedReader input;
		String input_name; 
		String output_name;
		FileReader in;
		String a_line;
		String bucket;
		String num_rewritings;
		String creation_time;
		String combination_time;
		String total_time;
		StringTokenizer tokenizer;
		try {
			gnuplot_file = new FileWriter("numRewritings.gnu");
			gnuplot_file.write("set xlabel \"Number of Rewritings\"");
			gnuplot_file.write("\n");
			gnuplot_file.write("set ylabel \"Number of Occurrances\"");
			gnuplot_file.write("\n");
			gnuplot_file.write("set terminal gif");
			gnuplot_file.write("\n");
			gnuplot_file.write("set output \"numrewritings.gif\"");
			gnuplot_file.write("\n");
			gnuplot_file.write("plot");
			for(i = 1; i <= _num_runs; i++){
				for (j = 0; j < _num_algs; j++){
					input_name = _file_prefixes[j] + i + ".txt";
					System.out.println(input_name);
					in = new FileReader(input_name);
					input = new BufferedReader(in);
					output_name = _file_prefixes[j] + "NumRewritings" + i + ".txt";
					System.out.println(output_name);
					output = new FileWriter(output_name);
					//output.write("bob");
					a_line = input.readLine();
					while (a_line != null){
						tokenizer = new StringTokenizer(a_line);
						bucket = tokenizer.nextToken();
						num_rewritings =tokenizer.nextToken();
						total_time =tokenizer.nextToken();
						creation_time = tokenizer.nextToken();
						combination_time = tokenizer.nextToken();
						output.write(bucket + " " + num_rewritings +"\n");
						a_line = input.readLine();
					}//end of reading one file
					gnuplot_file.write(" \"" + output_name + "\" with linespoints");
					if (!(i == _num_runs && j == _num_algs -1)){
						gnuplot_file.write(",");
					}
					output.close();
				}//end for looping over each algorithm file
			}//end of looping over all of the files
			gnuplot_file.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open input or output in MungeOutput.printNumRewritings");
			System.exit(1);
		}//end catch

	}//end printNumRewritings

	public void printStatistic(String what,int interval){
		int i;
		int j;
		int k;
		FileWriter output;
		FileWriter gnuplot_file;
		BufferedReader input;
		String input_name; 
		String output_name;
		FileReader in;
		String a_line;
		String bucket;
		String num_rewritings;
		String creation_time;
		String combination_time;
		String total_time;
		StringTokenizer tokenizer;
		try {
			gnuplot_file = new FileWriter(what + ".gnu");
			gnuplot_file.write("set xlabel \"Number of rewritings\"");
			gnuplot_file.write("\n");
			gnuplot_file.write("set ylabel \"Time in milliseconds\"");
			gnuplot_file.write("\n");
			gnuplot_file.write("set terminal gif");
			gnuplot_file.write("\n");
			gnuplot_file.write("set output \"" + what + ".gif\"");
			gnuplot_file.write("\n");
			gnuplot_file.write("plot");
			for(i = 1; i <= _num_runs; i+=interval){
				for (j = 0; j < _num_algs; j++){
					input_name = _file_prefixes[j] + i + ".txt";
					System.out.println(input_name);
					in = new FileReader(input_name);
					input = new BufferedReader(in);
					output_name = _file_prefixes[j] + what + i + ".txt";
					System.out.println(output_name);
					output = new FileWriter(output_name);
					//output.write("bob");
					a_line = input.readLine();
					while (a_line != null){
						tokenizer = new StringTokenizer(a_line);
						bucket = tokenizer.nextToken();
						num_rewritings =tokenizer.nextToken();
						total_time =tokenizer.nextToken();
						creation_time = tokenizer.nextToken();
						combination_time = tokenizer.nextToken();
						output.write(bucket + " ");
						if (what.equals("NumRewritings")){
							output.write(num_rewritings);
						}
						else if (what.equals("TotalTime")){
							output.write(total_time);
						}
						else if (what.equals("CreationTime")){
							output.write(creation_time);
						}
						else if (what.equals("CombinationTime")){
							output.write(combination_time);
						}
						else{
							System.out.println("unrecognized option " + what + " in MungeOutput.printStatistic");
						}
						output.write("\n");
						a_line = input.readLine();
					}//end of reading one file
					gnuplot_file.write(" \"" + output_name + "\" with linespoints");
					if (!(i+interval > _num_runs && j == _num_algs -1)){
						gnuplot_file.write(",");
					}
					output.close();
				}//end for looping over each algorithm file
			}//end of looping over all of the files
			gnuplot_file.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open input or output in MungeOutput.printStatistics");
			System.exit(1);
		}//end catch

	}//end printNumRewritings
	public void printTimePerBucket(){
		int i;
		int j;
		int k;
		FileWriter output;
		FileWriter gnuplot_file;
		BufferedReader input;
		String input_name; 
		String output_name;
		FileReader in;
		String a_line;
		String bucket;
		String num_rewritings;
		String creation_time;
		String combination_time;
		String total_time;
		StringTokenizer tokenizer;
		try {
			gnuplot_file = new FileWriter("timePerBucket.gnu");
			gnuplot_file.write("set xlabel \"Time Per Bucket\"");
			gnuplot_file.write("\n");
			gnuplot_file.write("set ylabel \"Time Per Bucket\"");
			gnuplot_file.write("\n");
			gnuplot_file.write("set terminal gif");
			gnuplot_file.write("\n");
			gnuplot_file.write("set output \"bucketime.gif\"");
			gnuplot_file.write("\n");
			gnuplot_file.write("plot");
			for(i = 1; i <= _num_runs; i++){
				for (j = 0; j < _num_algs; j++){
					input_name = _file_prefixes[j] + i + ".txt";
					System.out.println(input_name);
					in = new FileReader(input_name);
					input = new BufferedReader(in);
					output_name = _file_prefixes[j] + "timePerBucket" + i + ".txt";
					System.out.println(output_name);
					output = new FileWriter(output_name);
					//output.write("bob");
					a_line = input.readLine();
					while (a_line != null){
						tokenizer = new StringTokenizer(a_line);
						bucket = tokenizer.nextToken();
						num_rewritings =tokenizer.nextToken();
						total_time =tokenizer.nextToken();
						creation_time = tokenizer.nextToken();
						combination_time = tokenizer.nextToken();
						output.write(bucket + " " + total_time + "\n");
						a_line = input.readLine();
					}//end of reading one file
					gnuplot_file.write(" \"" + output_name + "\" with linespoints");
					if (!(i == _num_runs && j == _num_algs -1)){
						gnuplot_file.write(",");
					}
					output.close();
				}//end for looping over each algorithm file
			}//end of looping over all of the files
			gnuplot_file.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open input or output in MungeOutput.printNumRewritings");
			System.exit(1);
		}//end catch

	}//end printNumRewritings

    public static void main (String args[]){
	   MungeOutput m = new MungeOutput((new Integer(args[0])).intValue());
	   int interval = (new Integer(args[1])).intValue();
	   //m.printNumRewritings();
	   //m.printTimePerBucket();
	   int i;
	   for (i = 2; i < args.length; i++){
		   m.printStatistic(args[i],interval);
	   }
	}//end main
	
}

package minicon;
import java.io.*;

public class GnuplotScripts
{
	String [] _algorithm_types;
	int _num_algs;
	public GnuplotScripts(){
		_num_algs = 4;
		_algorithm_types = new String[_num_algs];
		_algorithm_types[0] = "BucketEnding";
		_algorithm_types[1] = "ExtendedMapping";
		_algorithm_types[2] = "NoExistential";
		_algorithm_types[3] = "OriginalNew";
		
	}
	
	public void plotAlgorithmTypeTogether(String type, int how_many,int interval){
		int i;
		int j;
		FileWriter htmlfile;
		String output_name;
		try {
			htmlfile = new FileWriter(type +"ByType.html");
			htmlfile.write("<html><body>");
			htmlfile.write("These graphs show the " + type);
			htmlfile.write(" with all runs of the same algorithm plotted together<p>");
			//htmlfile.write(" the plots are arranged from those with one view to those with " + how_many + " views");
			for (i = 0; i < _num_algs; i++){
				
				FileWriter output = new FileWriter( _algorithm_types[i]+ type  + ".gnu");		
				output.write("set xlabel \"Number of Rewritings\"");
				output.write("\n");
				output.write("set ylabel \"Time in milliseconds\"");
				output.write("\n");
				output.write("set terminal gif");
				output.write("\n");
				output.write("set output \"" +_algorithm_types[i] + type + ".gif" + "\"");
				output_name = _algorithm_types[i] + type  + ".gif";
				output.write("\n");
				output.write("plot");
				htmlfile.write("<img src = \"" + output_name + "\">"); 
				htmlfile.write("<p>");
				for (j = 1; j <= how_many; j+=interval){
					output.write(" \"" + _algorithm_types[i] + type + j + ".txt\" with linespoints");
					if (j +interval <= how_many){
						output.write(",");
					}
				}//end looping over each file name
				output.close();
			}//end looping over each algorithm
			htmlfile.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open file in GnuplotScripts.plotAlgorithmTypeTogether");
		}
			
		
	}//end plotAlgorithmTypeTogether

		public void plotSizeTogether(String type, int how_many, int interval){
		int i;
		int j;
		String output_name;
		FileWriter htmlfile;
		try {		
			htmlfile = new FileWriter(type +"BySize.html");
			htmlfile.write("<html><body>");
			htmlfile.write("These graphs show the " + type);
			htmlfile.write(" with all algorithms with the same number of views plotted together \n");
			htmlfile.write("The plots are arranged from those with one view to those with " + how_many + " views<p>");
			for (j = 1; j <= how_many; j+=interval){

				FileWriter output = new FileWriter( type + j + ".gnu");		
				output.write("set xlabel \"Number of rewritings\"");
				output.write("\n");
				output.write("set ylabel \"Time in milliseconds\"");
				output.write("\n");
				output.write("set terminal gif");
				output.write("\n");
				output_name = type + j + ".gif";
				output.write("set output \"" + output_name + "\"");
				output.write("\n");
				output.write("plot");
				htmlfile.write("<img src = \"" + output_name + "\">");
				htmlfile.write("<p>");
				for (i = 0; i < _num_algs; i++){
					output.write(" \"" + _algorithm_types[i] + type + j + ".txt\" with linespoints");
					if (i  != _num_algs -1){
						output.write(",");
					}
				}//end looping over each file name
				output.close();
			}//end looping over each algorithm
			htmlfile.write("</body> </html>");
			htmlfile.close();
		}//end try
		catch (IOException e){
			System.out.println("couldn't open file in GnuplotScripts.plotAlgorithmTypeTogether");
		}
			
		
	}//end plotSizeTogether

	public static void main(String args[]){
		int num_runs = (new Integer(args[0])).intValue();
		int interval = (new Integer(args[1])).intValue();
		int i;
		GnuplotScripts g = new GnuplotScripts();
		for (i = 2; i < args.length;i++){
			g.plotAlgorithmTypeTogether(args[i], num_runs,interval);
			g.plotSizeTogether(args[i], num_runs,interval);
		}
	}

}

/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package semantics;
import java.io.*;

import minicon.*;
/**
 * @author rap
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SemanticMergeExperiment {
	protected SemanticMerge m_merge;

	public static Schema mergeFile(String p_filename)
	{
		SemanticMerge a_merge = new SemanticMerge();
		Schema retval = new Schema();
		BufferedReader input;
		String line;
		StringBuffer statement = new StringBuffer();
		Predicate pred;
		Schema input_schema = new Schema();
		Mapping mapping = new Mapping();
		View view;
		
		try {
			FileReader file = new FileReader(p_filename);
			input = new BufferedReader(file);
			line = input.readLine();			
			while (line != null && !line.startsWith("###")){
				if (!line.startsWith("//") && !line.trim().equals("")){
					pred = new Predicate();
					pred.read(line);
					input_schema.addRelation(pred);
					//System.out.println("relation added: " + pred.printString().toString());
				}
				line = input.readLine();
			}//end of getting the input schemas

			if (line != null){
				line = input.readLine().trim();//get rid of the ### line
			}	
			//now get the mapping
			while (line != null)
			{
				if (!line.startsWith("//")){
					statement.append(line);
					if (line.endsWith("."))
					{
						//then we're done parsing it
						view = new View();
						view.read(statement.substring(0,statement.length() - 1));
						//System.out.println("statement added = " + statement.substring(0,statement.length() - 1));
						//System.out.println("view added" + view.printString().toString());
						mapping.addView(view);
						statement = new StringBuffer();
	
					}
					
				}//end of if it wasn't a comment.
				line = input.readLine();
			}//end getting the mapping
			//at this point we have the mapping and the schema.  let's try merging.  
			//note that I've actually left the second schema blank, because it doesn't really matter
			//if they're in one schema or two, but I need a second one for it to run
			a_merge.setMapping(mapping);
			a_merge.setSchema1(input_schema);
			a_merge.setSchema2(new Schema());
			a_merge.merge();
			Schema output = a_merge.getMergedSchema();
			System.out.println("schema output = \n" + output.printString());
			System.out.println("mapping output = \n" + a_merge.getGLAVMapping().printString());
		}//end try block
		catch (Exception e)
		{
			System.out.println("file " + p_filename + " not available for reading");
		}
		return retval;
	}
	
	public static void main(String[] args) {
		System.out.println("experiments are us");
		SemanticMergeExperiment exp = new SemanticMergeExperiment();
		//mergeFile("C:\\Documents and Settings\\rap\\Desktop\\courses.txt");
		//mergeFile("c:\\schemas\\anhai\\inventory.txt");
		//mergeFile("c:\\schemas\\anhai\\courses.txt");
		//mergeFile("c:\\schemas\\anhai\\real_estate.txt");
		mergeFile("c:\\schemas\\anhai\\real_estate2.txt");
	}
}

/*
 * Created on Feb 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package semantics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Vector;

import minicon.Predicate;
import minicon.*;

/**
 * @author rap
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestLAVCourses {

	
	
	protected static Vector readViewsFromFile(String p_filename)
	{
		Vector retval = new Vector();
		StringBuffer a_buffer = new StringBuffer();
		String line;
		try
		{
			FileReader file = new FileReader(p_filename);
			BufferedReader input = new BufferedReader(file);
			line = input.readLine();
			while (line != null)
			{
				a_buffer.append(line + "\n");
				line = input.readLine();
			}
			
			file.close();
			input.close();
			retval = readViewsFromString(a_buffer.toString());
		}
		catch(Exception e)
		{
			System.out.println("can't open filename " + p_filename);
		}
		
		return retval;
	}
	
	public static Vector readViewsFromString(String p_input)
	{
		BufferedReader input;
		String line = "";
		StringBuffer statement = new StringBuffer();
		Predicate pred;
		Vector retval = new Vector();
		//Schema input_schema = new Schema();
		//Mapping mapping = new Mapping();
		View view;
		
		try {
			StringReader string_reader = new StringReader(p_input);
			input = new BufferedReader(string_reader);
			boolean success;
			line = input.readLine().trim();//get rid of the ### line
			//now get the mapping
			while (line != null)
			{
				if (!line.startsWith("//") && !line.equals("")){
					statement.append(line);
					if (line.endsWith(".") || (!line.endsWith(",") && !line.endsWith(":-")))
					{
						//then we're done parsing it
						view = new View();
						if (line.endsWith(".")){
							statement.setLength(statement.length() - 1);	
						}
						success = view.read(statement.toString());
						//System.out.println("statement added = " + statement.substring(0,statement.length() - 1));
						//System.out.println("view added" + view.printString().toString());
						if (success){
							retval.addElement(view);
						}
						else
						{
							System.out.println("failed to parse in mapping.readFromString" + statement.toString() + "ignoring it");
						}
						statement = new StringBuffer();
	
					}
					
				}//end of if it wasn't a comment.
				line = input.readLine();
				if (line != null){
					line.trim();
				}
			}//end getting the mapping
			input.close();
		}//end try block
		catch (Exception e)
		{
			System.out.println("Problem parsing " + line);
		}
		return retval;
	}

	public static long rewriteQuery(Query p_query, Vector p_views)
	{
		String retval = null;
		BucketEndingAlgorithm minicon = new BucketEndingAlgorithm();
		int i, numviews;
		View a_view;
		numviews = p_views.size();
		for (i = 0; i < numviews; i++)
		{
			a_view = (View)p_views.elementAt(i);
			minicon.addView(a_view);
		}
		minicon.setQuery(p_query);
		retval = minicon.run();
		//System.out.println("am here");
		return minicon.getTotalTime();
		//return retval;

	}
	
	public static void main(String[] args) {
		int i, numviews;
		Vector views = readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\fake-lav.txt");
		numviews = views.size();
		View a_view;
		/*for (i =0; i < numviews; i++)
		{
			a_view = (View)views.elementAt(i);
			System.out.println(a_view.printString().toString());
		}
		*/
		/*
		Query query1 = new Query("q(course_code):-course-listing(dk1,course_code,dk2,dk3,dk4,dk5,dk83,dk6,dk7,dk8,dk9,dk10,dk11,dk12,dk13,dk14,dk15,dk16)");
		System.out.println("query1:");
		System.out.println(rewriteQuery(query1,views));
		Query query2 = new Query("q(course_code):-course_code(dk17,course_code,dk18,dk19,dk20,dk21,dk22,dk23,dk24,dk25,dk26,dk27,dk28,dk29,dk30,dk31,dk32)");
		System.out.println("query2:");
		System.out.println(rewriteQuery(query2,views));
		Query query3 = new Query("q(course_code):-section(dk33,course_code,dk34,dk35,dk36,dk37,dk38,dk39,dk40,dk41,dk42,dk43,dk44,dk45,dk46,dk47,dk48,dk49,dk50,dk51,dk52)");
		System.out.println("query3:");
		System.out.println(rewriteQuery(query3,views));
		Query query4 = new Query("q(section_id):-lecture(course_code,dk53,dk54,dk55,dk56,section_id,dk58,dk59,dk60,dk61,dk62,dk63,dk64,dk65,dk66,dk67,dk68,dk69,dk70,dk71)");
		System.out.println("query4:");
		System.out.println(rewriteQuery(query4,views));
		Query query5 = new Query("q(time_id):-times(time_id,start_time,end_time)");
		System.out.println("query5:");
		System.out.println(rewriteQuery(query5,views));
		Query query6 = new Query("q(place_id):-places(place_id,building,room)");
		System.out.println("query6:");
		System.out.println(rewriteQuery(query6,views));
		*/
		Query big_query = new Query("(course_code):-"+
		"course-listing(dk1,course_code,dk2,dk3,dk4,dk5,dk83,dk6,dk7,dk8,dk9,dk10,dk11,dk12,dk13,dk14,dk15,dk16),"+
		"course_code(dk17,course_code,dk18,dk19,dk20,dk21,dk22,dk23,dk24,dk25,dk26,dk27,dk28,dk29,dk30,dk31,dk32),"+
		"section(dk33,course_code,dk34,dk35,dk36,dk37,dk38,dk39,dk40,dk41,dk42,dk43,dk44,dk45,dk46,dk47,dk48,dk49,dk50,dk51,dk52),"+
		"lecture(course_code,dk53,dk54,dk55,dk56,section_id,dk58,dk59,dk60,dk61,dk62,dk63,dk64,dk65,dk66,dk67,dk68,dk69,dk70,dk71),"+
		"times(time_id,start_time,end_time),"+
		"places(place_id,building,room)");
		rewriteQuery(big_query,views);

	}
}

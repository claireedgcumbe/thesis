/*
 * Created on Feb 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package semantics;
import minicon.*;

import java.util.Vector;

/**
 * @author rap
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestCourses {

	private Schema m_ww_schema;
	private Schema m_wwr_schema;
	private Schema m_wwrr_schema;
	private Schema m_all_schema;
	protected Vector m_ww_views;
	protected Vector m_wwr_views;
	protected Vector m_wwrr_views;
	protected Vector m_all_views;
	protected Predicate [] m_ww_rels;
	protected Predicate [] m_wwr_rels;
	protected Predicate [] m_wwrr_rels;
	protected Predicate [] m_all_rels;
	protected TimingResults [] m_ww_results;
	protected TimingResults [] m_wwr_results;
	protected TimingResults [] m_wwrr_results;
	protected TimingResults [] m_all_results;
	protected TimingResults [] m_ww_lav_results;
	protected TimingResults [] m_wwr_lav_results;
	protected TimingResults [] m_wwrr_lav_results;
	protected TimingResults [] m_all_lav_results;
	
	
	public TestCourses()
	{
		m_ww_schema = null;
		m_wwr_schema = null;
		m_wwrr_schema = null;
		m_all_schema = null;
		m_ww_views = null;
		m_wwr_views = null;
		m_wwrr_views = null;
		m_all_views = null;
		m_ww_rels = new Predicate[10];
		m_wwr_rels = new Predicate[10];
		m_wwrr_rels = new Predicate[10];
		m_all_rels = new Predicate[10];
		m_ww_results = new TimingResults[10];
		m_wwr_results = new TimingResults[10];
		m_wwrr_results = new TimingResults[10];
		m_all_results = new TimingResults[10];
		m_ww_lav_results = new TimingResults[10];
		m_wwr_lav_results = new TimingResults[10];
		m_wwrr_lav_results = new TimingResults[10];
		m_all_lav_results = new TimingResults[10];
		int i;
		for (i = 0; i < 10; i++)
		{
			m_ww_results[i] = new TimingResults();
			m_wwr_results[i] = new TimingResults();
			m_wwrr_results[i] = new TimingResults();
			m_all_results[i] = new TimingResults();
			m_ww_lav_results[i] = new TimingResults();
			m_wwr_lav_results[i] = new TimingResults();
			m_wwrr_lav_results[i] = new TimingResults();
			m_all_lav_results[i] = new TimingResults();
		}
		
	}
	
	public void setupLAV()
	{
		m_ww_views = TestLAVCourses.readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\fake-lav-ww.txt");
		m_wwr_views = TestLAVCourses.readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\fake-lav-wwrice.txt");
		m_wwrr_views = TestLAVCourses.readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\fake-lav-wwrr.txt");
		m_all_views = TestLAVCourses.readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\fake-lav.txt");
	}
	
	public void setupRels()
	///This function sets up the relations that we need for easy generation of experiments
	{
		m_ww_rels[0] = new Predicate("course_listing-ww(course_code,title0,section_id0,schedule_line0,section_code0,course_credits0,restrictions0,note0,level0)");
		m_ww_rels[1] = new Predicate("course_code-ww(course_code,title1,section_id1,note1,course_credits1,level1,restrictions1)");
		m_ww_rels[2] = new Predicate("section-ww(course_code,section_id2,schedule_line2,section_code2,section_credits2,restrictions2,section_note2,section2,days2,hours_id2,place_id2,instructor2,comments2)");
		m_ww_rels[3] = new Predicate("lecture-ww(course_id,lecture_id3,section_note3,section3,date3,time_id,place_id,lecturer3,comments3,section_id3)");
		m_ww_rels[4] = new Predicate("times-ww(time_id,start_time4,end_time4)");
		m_ww_rels[5] = new Predicate("places-ww(place_id,building5,room5)");
		m_wwr_rels[0] = new Predicate("course-listing-rice-ww(course_code,title0,credits0,note0,section_id0,schedule_line0,section_code0,restrictions0,level0)");
		m_wwr_rels[1] = new Predicate("code-rice-ww(course_code,title1,section_id1,note1,course_credits1,level1,restrictions1,comments1)");
		m_wwr_rels[2] = new Predicate("section-rice-ww(course_code,section_id2,schedule_line2,section_code2,section_credits2,restrictions2,section_note2,section2,days2,hours_id2,place_id2,instructor2,comments2)");
		m_wwr_rels[3] = new Predicate("lecture-rice-ww(course_code,section_id3,section_num3,date3,time_id,place_id,lecturer3,lecture_id3,section_note3,comments3)");
		m_wwr_rels[4] = new Predicate("times-rice-ww(time_id,start_time4,end_time4)");
		m_wwr_rels[5] = new Predicate("places-ww-rice(place_id,building5,room5)");
		m_wwrr_rels[0] = new Predicate("course-listing-rrww(course_code,subj0,crse0,section_id0,title0,credits0,instructor0,days0,time_id0,place_id0,note0,schedule_line0,section_code0,restrictions0,level0)");
		m_wwrr_rels[1] = new Predicate("course-code-rrww(course_code,title1,section_id1,note1,credits1,level1,restrictions1,comments1,subj1,crse1,instructor1,days1,time_id1,place_id1)");
		m_wwrr_rels[2] = new Predicate("section-rrww(course_code,section_id2,schedule_line2,section_code2,section_credits2,restrictions2,section_note2,section2,days2,hours_id2,place_id2,instructor2,comments2,subj2,crse2,title2,credits2)");
		m_wwrr_rels[3] = new Predicate("lecture-rrww(course_code,section_id3,section_num3,date3,time_id,place_id,lecturer3,lecture_id3,section_note3,comments3,subj3,crse3,title3,credits3)");
		m_wwrr_rels[4] = new Predicate("times-rrww(time_id,start_time4,end_time4)");
		m_wwrr_rels[5] = new Predicate("places-rrww(place_id,building5,room5)");
		m_all_rels[0] = new Predicate("course-listing(note0,course_code,subj0,crse0,lab0,section_id0,title0,credits0,days0,time_id0,place_id0,instructor0,limit0,enrolled0,schedule_line0,section_code0,restrictions0,level0)");
		m_all_rels[1] = new Predicate("course_code(note1,course_code,subject1,course1,lab1,section_id1,title1,credits1,days1,time_id1,place_id1,instructor1,limit1,enrolled1,level1,restrictions1,comments1)");
		m_all_rels[2] = new Predicate("section(note2,course_code,subject2,course2,lab2,section_id2,title2,credits2,days2,hours_id2,place_id2,instructor2,limit2,enrolled2,schedule_line2,section_code2,section_credits2,restrictions2,section_note2,section2,comments2)");
		m_all_rels[3] = new Predicate("lecture(course_code,section_id3,section_num3,days3,time_id,place_id,lecturer3,lecture_id3,section_note3,comments3,subj3,crse3,title3,credits3,note3,subject3,course3,lab3,limit3,enrolled3)");
		m_all_rels[4] = new Predicate("times(time_id,start_time4,end_time4)");
		m_all_rels[5] = new Predicate("places(place_id,building5,room5)");
	
	}
	
	public Schema CreateMediatedSchema()
	{
		Schema reed = new Schema();
		Schema rice = new Schema();
		Schema washington = new Schema();
		Schema wisconsin = new Schema();
		Schema wsu = new Schema();
		Mapping ww_mapping = new Mapping();
		SemanticMerge ww;
		int i;
		reed.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\reed.txt");
		rice.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\rice.txt");
		washington.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\washington.txt");
		wisconsin.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\wisconsin.txt");
		wsu.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\wsu.txt");
		ww_mapping.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\input-ww.txt");
		ww = new SemanticMerge();
		ww.setSchema1(washington);
		ww.setSchema2(wisconsin);
		ww.setMapping(ww_mapping);
		ww.merge();
		m_ww_schema = ww.getMergedSchema();
		//System.out.println("Mediated schema for washington and wisconsin = \n" + ww.getMergedSchema().printString());
		//System.out.println("mapping for washington and wisconsin = \n" + ww.getGLAVMapping().printString());
		Mapping wwr_mapping = new Mapping();
		wwr_mapping.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\input-ww-rice.txt");
		SemanticMerge wwr = new SemanticMerge();
		wwr.setSchema1(ww.getMergedSchema());
		wwr.setSchema2(rice);
		wwr.setMapping(wwr_mapping);
		wwr.merge();
		m_wwr_schema = wwr.getMergedSchema();
		//System.out.println("Mediated schema for rice, washington and wisconsin = \n" + wwr.getMergedSchema().printString());
		//System.out.println("mapping for rice, washington and wisconsin = \n" + wwr.getGLAVMapping().printString());
		Mapping ww_rr_mapping = new Mapping();
		ww_rr_mapping.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\input-ww-rice-reed.txt");
		SemanticMerge ww_rr = new SemanticMerge();
		ww_rr.setSchema1(wwr.getMergedSchema());
		ww_rr.setSchema2(reed);
		ww_rr.setMapping(ww_rr_mapping);
		ww_rr.merge();
		m_wwrr_schema = ww_rr.getMergedSchema();
		//System.out.println("Mediated schema for rice, reed, washington and wisconsin = \n" + ww_rr.getMergedSchema().printString());
		//System.out.println("mapping for rice, reed, washington and wisconsin = \n" + ww_rr.getGLAVMapping().printString());
		Mapping all_mapping = new Mapping();
		all_mapping.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\courses\\input-all.txt");
		SemanticMerge all_merge = new SemanticMerge();
		all_merge.setSchema1(ww_rr.getMergedSchema());
		all_merge.setSchema2(wsu);
		all_merge.setMapping(all_mapping);
		all_merge.merge();
		m_all_schema = all_merge.getMergedSchema();
		//System.out.println("Mediated schema for all = \n" + all_merge.getMergedSchema().printString());
		//System.out.println("mapping for all = \n" + all_merge.getGLAVMapping().printString());
		
		
		return all_merge.getMergedSchema();
	}
	
	public static long askQuery(Query p_query, Schema p_schema)
	{
		RapTimer timer = new RapTimer();
		Vector retval;
		GLAVMapping a_map = p_schema.getMapping();
		timer.start();
		retval = RewriteGLAVQuery.findRewriting(p_query,p_schema);
		timer.stop();
		long time = timer.getAccumulatedTime();
/*		if (retval != null){
			int i, num_rewritings = retval.size();
			for (i = 0; i < num_rewritings; i++)
			{
				System.out.println(((Statement)retval.elementAt(i)).printString().toString());
			}
		}//end if the answer wasn't null
		else
		{
			System.out.println("no rewritings for query " + p_query.printString().toString());
		}
*/		return time;
	}
	
	public static void main(String[] args) {
		TestCourses testy = new TestCourses();
		testy.setupRels();
		testy.setupLAV();
		testy.CreateMediatedSchema();
		//System.out.println(testy.m_ww_schema.printString());
		//System.out.println(testy.m_wwrice_schema.printString());
		//System.out.println(testy.m_wwrr_schema.printString());
		//System.out.println(testy.m_all_schema.printString());
		Vector views;
		Query ww_query = new Query();
		Query wwr_query = new Query();
		Query wwrr_query = new Query();
		Query all_query = new Query();

		ww_query.setHead(new Predicate("q(course_code)"));
		wwr_query.setHead(new Predicate("q(course_code)"));
		wwrr_query.setHead(new Predicate("q(course_code)"));
		all_query.setHead(new Predicate("q(course_code)"));
		int i,j;
		long result_time;
		double avg_result_time;
		double deviation;
		for (i = 0; i < 6; i++)
		{
			ww_query.addSubgoal(testy.m_ww_rels[i]);
			wwr_query.addSubgoal(testy.m_wwr_rels[i]);
			wwrr_query.addSubgoal(testy.m_wwrr_rels[i]);
			all_query.addSubgoal(testy.m_all_rels[i]);
			for (j = 0; j < 5; j++)
			{
				result_time = askQuery(ww_query,testy.m_ww_schema);
				testy.m_ww_results[i].addStatistic(result_time);
				System.out.println("ww  \t\t: subgoals = " + i + " " +result_time);
				result_time = askQuery(wwr_query,testy.m_wwr_schema);
				testy.m_wwr_results[i].addStatistic(result_time);
				System.out.println("wwr  \t\t: subgoals = " + i + " " +result_time);
				result_time = askQuery(wwrr_query,testy.m_wwrr_schema);
				testy.m_wwrr_results[i].addStatistic(result_time);
				System.out.println("wwrr \t\t: subgoals = " + i + " " + result_time);
				result_time = askQuery(all_query,testy.m_all_schema);
				testy.m_all_results[i].addStatistic(result_time);
				System.out.println("all \t\t: subgoals = " + i + " " + result_time);

				result_time = TestLAVCourses.rewriteQuery(ww_query,testy.m_ww_views);
				System.out.println("ww lav  \t: subgoals = " + i + " " +result_time);
				testy.m_ww_lav_results[i].addStatistic(result_time);
				result_time = TestLAVCourses.rewriteQuery(wwr_query,testy.m_wwr_views);
				System.out.println("wwr lav \t: subgoals = " + i + " " + result_time);
				testy.m_wwr_lav_results[i].addStatistic(result_time);
				result_time = TestLAVCourses.rewriteQuery(wwrr_query,testy.m_wwrr_views);
				System.out.println("wwrr lav \t: subgoals = " + i + " " +result_time);
				testy.m_wwrr_lav_results[i].addStatistic(result_time);
				result_time = TestLAVCourses.rewriteQuery(all_query,testy.m_all_views);
				System.out.println("all lav \t: subgoals = " + i + " " +result_time);
				testy.m_all_lav_results[i].addStatistic(result_time);
				
				
			}
			
			
		}//end getting all the statistics
		//now output them so that we know what it is:
		System.out.println("ww\twwr\twwrr\tall\tww lav\twwr lav\twwrr lav \tall lav");
		for (i = 0; i < 6; i++)
		{

			avg_result_time = testy.m_ww_results[i].getAverage();
			System.out.print(avg_result_time);
			avg_result_time = testy.m_wwr_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_wwrr_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_all_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_ww_lav_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_wwr_lav_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_wwrr_lav_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_all_lav_results[i].getAverage();

			System.out.print("\t\t" + avg_result_time + "\n");
			
			
		}
		
		
/*		query.addSubgoal(testy.m_wwrr_rels[0]);
		query.addSubgoal(testy.m_wwrr_rels[1]);
		query.addSubgoal(testy.m_wwrr_rels[2]);
		query.addSubgoal(testy.m_wwrr_rels[3]);
		query.addSubgoal(testy.m_wwrr_rels[4]);
		query.addSubgoal(testy.m_wwrr_rels[5]);
		*/
/*		query.addSubgoal(testy.m_all_rels[0]);
		query.addSubgoal(testy.m_all_rels[1]);
		query.addSubgoal(testy.m_all_rels[2]);
		query.addSubgoal(testy.m_all_rels[3]);
		query.addSubgoal(testy.m_all_rels[4]);
		query.addSubgoal(testy.m_all_rels[5]);
*/
		/*System.out.println("Query = " + query.printString().toString());
		System.out.println(askQuery(query, testy.m_wwrr_schema));
		System.out.println(TestLAVCourses.rewriteQuery(query,testy.m_wwrr_views));
		*/
		//Query query = new Query("q(course_code):-course-listing-rice-ww(course_code,title,credits,note,section_id,schedule_line,section_code,restrictions,level),Washington.time(time_id,start_time,end_time)");
		//Query query = new Query("q(course_code):-course_listing-ww(course_code,title,section_id,schedule_line,section_code,course_credits,restrictions,note,level)");
		//Query query = new Query("q(course_code):-course-listing-rrww(course_code,subj,crse,section_id,title,credits,instructor,days,time_id,place_id,note,schedule_line,section_code,restrictions,level)");
		//Query query = new Query("q(course_code):-course-listing(note,course_code,subj,crse,lab,section_id,title,credits,days,time_id,place_id,instructor,limit,enrolled,schedule_line,section_code,restrictions,level)");
		//System.out.println(schema.printString());
		/*Query query = new Query("q(course_code):-course-listing(dk1,course_code,dk2,dk3,dk4,dk5,dk83,dk6,dk7,dk8,dk9,dk10,dk11,dk12,dk13,dk14,dk15,dk16), " +
				"course_code(dk17,course_code,dk18,dk19,dk20,dk21,dk22,dk23,dk24,dk25,dk26,dk27,dk28,dk29,dk30,dk31,dk32), " +
				"section(dk33,course_code,dk34,dk35,dk36,dk37,dk38,dk39,dk40,dk41,dk42,dk43,dk44,dk45,dk46,dk47,dk48,dk49,dk50,dk51,dk52)," +
				"lecture(course_code,dk53,dk54,dk55,dk56,dk57,dk58,dk59,dk60,dk61,dk62,dk63,dk64,dk65,dk66,dk67,dk68,dk69,dk70,dk71)," +
				"times(time_id,start_time,end_time), " +
				"places(place_id,building,room)");
				*/
	}
}

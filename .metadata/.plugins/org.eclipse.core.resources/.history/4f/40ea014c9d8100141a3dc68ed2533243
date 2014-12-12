/*
 * Created on Feb 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package semantics;
import java.util.Random;
import java.util.Vector;

import minicon.*;
/**
 * @author rap
 * This class provides a way to test the rewriting speed
 * for a general rewriting strategy.  The catch is that
 * we can't test the same LAV & CMS equivalents since
 * they have differing expressive capabilities
 * therefore, we test similar ones, just as in 
 * TestRealEstate and TestCourses vs TestLAVCourses
 * and TestLAVRealEstate
 */
public class TestRewritingSpeed {
	protected Schema m_msc;
	protected BucketEndingAlgorithm m_lav;
	protected GenerateEquivalentLAVAndMSC m_generator;
	protected Vector m_msc_results;
	protected Vector m_lav_results;
//	protected Vector m_num_sources;
	protected boolean m_debug_mode;
	
	public  TestRewritingSpeed()
	{
		m_msc = null;
		m_lav = null;
		m_generator = new GenerateEquivalentLAVAndMSC();
		m_msc_results = new Vector();
		m_lav_results = new Vector();
		m_debug_mode = false;
//		m_num_sources = new Vector();
	}
	
	public void setDebugMode(boolean p_debug_mode){
		m_debug_mode = p_debug_mode;
	}
	
	public void setNumSchemas(int p_num_schemas)
	{
		m_generator.setNumSources(p_num_schemas);
	}
	
	public void getSchemas()
	{
		m_generator.createMSCFromSources();
		m_generator.CreateLAVFromSources();
		m_msc = m_generator.getMSCs();
		m_lav = m_generator.getLAV();
	}
	
	protected long timeMSCRewriting(Query p_query)
	{
		RapTimer timer = new RapTimer();
		Vector retval;
		GLAVMapping a_map = m_msc.getMapping();
		timer.start();
		retval = RewriteGLAVQuery.findRewriting(p_query,m_msc);
		timer.stop();
		long time = timer.getAccumulatedTime();
		if (m_debug_mode == true){
			System.out.println(retval.size() + " msc rewritings");
		}
		if (m_debug_mode == true)
		{
			if (retval != null){
				System.out.println("Rewritings for MSC = ");
				int i, num_rewritings = retval.size();
				for (i = 0; i < num_rewritings; i++)
				{
					System.out.println(((Statement)retval.elementAt(i)).printString().toString());
				}
			}//end if the answer wasn't null
			else
			{
				System.out.println("no rewritings for MSC query " + p_query.printString().toString());
			}
		}
		return time;

	}
	
	protected long timeLAVRewriting(Query p_query)
	{
		String retval = null;
		m_lav.setQuery(p_query);
		retval = m_lav.run();
		if (m_debug_mode == true)
		{
			System.out.println("lav rewritings = \n");
			System.out.println(retval);
			System.out.println(m_lav.getNumRewritings() + " lav rewritings");
		}
		return m_lav.getTotalTime();

	}
	
	public void timeRewritings(int p_num_sources, int p_num_rels, int p_query_length)
	{
		long lav;
		long msc;
		if (m_debug_mode == true){
			System.out.println("TimeRewritings; num sources = " + p_num_sources + " num rels = " + p_num_rels + " query length = " + p_query_length);
		}
		m_generator = new GenerateEquivalentLAVAndMSC(m_debug_mode);
		setNumSchemas(p_num_sources);
		m_generator.setNumRelations(p_num_rels);
		getSchemas();
		Query q = generateQuery(p_query_length);
		lav = timeLAVRewriting(q);
		msc = timeMSCRewriting(q);
		System.out.println("# sources = " + p_num_sources + " msc = " + msc + " lav = " + lav);
	}
	
	public Query generateQuery(int p_num_rels)
	/*
	 * Note, I assume that we're only dealing with very simple chain queries
	 * And for the moment I'm going to assume that it's over the first and 
	 * last attributes, which for the moment I'm going to assume are the common attributes
	 */
	{
		Random num_generator = new Random(System.currentTimeMillis());//create a new 
		//random number generator with the current time as the seed
		int num_rels = m_msc.numRelations();
		int [] take_rels = new int[num_rels];
		int i,j;
		int temp;
		Predicate p;
		int next_free  = 0;
		int next_in_chain = 0;
		Query retval = new Query();
		int num_vars = 0;
		Predicate old_rel;
		//randomize the relations used to make sure we're not screwing anything up.
		for (i = 0; i < num_rels; i++)
		{
			take_rels[i] = i;
		}
		int swap1, swap2;
		for (i = 0; i < num_rels; i++)
		{
			swap1 = num_generator.nextInt(num_rels);
			swap2 = num_generator.nextInt(num_rels);
			temp = take_rels[swap1];
			take_rels[swap1] = take_rels[swap2];
			take_rels[swap2] = temp;
		}
		//at this point, it should be decently randomized
		//set up the head of the query
		p = new Predicate();
		p.setFunctionHead("Q");
		p.addVariable("chain_" + next_in_chain);
		retval.setHead(p);
		for (i = 0; i < p_num_rels; i++)//we're ready to add in the query predicates
		{
			
			old_rel = m_msc.relationI(take_rels[i]);
			p = new Predicate();
			p.setFunctionHead(old_rel.getFunctionHead());
			num_vars = old_rel.size();
			//first, deal with first var, which should be the same as the last in the 
			//chain
			p.addVariable("chain_" + next_in_chain);
			next_in_chain++;
			//then deal with not-common-vars, which is everything but the 
			//first and second
			//now add the second
			p.addVariable("chain_" + next_in_chain);
			for (j = 2; j <num_vars; j++)
			{
				p.addVariable("free_" + next_free);
				next_free++;
			}

			//note, we don't increment because we want the next relation
			//in the chain to have the same first value as this last value
			retval.addSubgoal(p);
		}
		if (m_debug_mode == true)
		{
			System.out.println(retval.printString().toString());
		}
		return retval;
	}//end public Query generateQuery(int p_num_rels)
	
	
	
	public static void main(String[] args) {
		TestRewritingSpeed tester = new TestRewritingSpeed();
		//tester.setDebugMode(true);
		tester.getSchemas();
		int i;
		for (i = 2; i <12;i++)
		{
			//System.out.print("i = " + i + " ");
			//parameters for timeRewritings are int p_num_sources, int num_rels, int query_length
			//very bad idea to have query length > num_rels
			tester.timeRewritings(i,5,5);
		}
		//parameters for timeRewritings are int p_num_sources, int num_rels, int query_length
		//very bad idea to have query length > num_rels
		//to force it to run w/ a bigger heap space, try something like -Xmn100M -Xms500M -Xmx500M
		//but not with these values, or the whole stupid computer will freeze up
/*		tester.timeRewritings(2,2,2);

		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		tester.timeRewritings(3,2,2);
		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		tester.timeRewritings(4,2,2);
		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		tester.timeRewritings(5,2,2);
		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		tester.timeRewritings(50,2,2);
		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		//System.out.println("**********************************************************************************");
		tester.timeRewritings(500,2,2);
*/		
	//	Query q = tester.generateQuery(5);
//		System.out.println(q.printString().toString());	
	}
}

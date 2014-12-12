/*
 * Created on Feb 16, 2005
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



public class RewriteGLAVQuery {

	public static Schema requiresFurtherRewritingWithSchema(Predicate a_pred, Schema p_mediated_schema)
	{
		String rel_name = a_pred.getFunctionHead();
		int num_schemas = p_mediated_schema.getMapping().numInputSchemas();
		Schema a_schema;
		for(int i = 0; i < num_schemas; i++){
			a_schema = p_mediated_schema.getMapping().getInputSchemaI(i);
			if (a_schema.isCompositeSchema() && a_schema.containsRelation(rel_name)){
				return a_schema;
			}
		}
		return null;
	}
	
	
	public static Vector findRewriting(Query p_query,Schema p_mediated_schema)
	{
		String query_string = p_query.printString().toString();
		String schema_string = p_mediated_schema.printString();
		System.out.println("query_string: " + query_string);
		System.out.println("schema_string: " + schema_string);
		GLAVMapping glav_mapping = p_mediated_schema.getMapping();
		Vector qprimes = useLV(p_query, p_mediated_schema);
		if (qprimes == null)
		{
			return null;
		}
		Vector retval = new Vector();
		int i;
		int num_rewritings;
		Statement conjunctive_rewriting;
		num_rewritings = qprimes.size();
		Statement conjunctive_totally_rewritten;
		StringBuffer conjunctive_string_rewriting = new StringBuffer("");
		Statement returned_from_gvs;
		Predicate a_lv_rewriting;
		Vector recursive_rewritings;
		int j, num_new_subgoals;
		Predicate check_recursively;
		Predicate new_head;

		Query recursive_query;
		Schema recursive_schema_to_check;
		//Now we have the query in the intermediate state; we now need to replace
		//each subgoal with its view definition
		//now add in global views into the mix
		
		System.out.println("-------------------num_rewritings: " + num_rewritings);
		for (i = 0; i < num_rewritings; i++)
		{
			conjunctive_string_rewriting = new StringBuffer("");
			
			conjunctive_totally_rewritten= new Statement();
			conjunctive_totally_rewritten.setHead(p_query.getHead());
			conjunctive_rewriting = new Statement((String)qprimes.elementAt(i));
			for (conjunctive_rewriting.first(); !conjunctive_rewriting.isDone(); conjunctive_rewriting.next())
			{
				a_lv_rewriting = conjunctive_rewriting.current();
				System.out.println("a_lv_rewriting: " + a_lv_rewriting.printString());
				returned_from_gvs = glav_mapping.useGVToRewriteQueryPredicate(a_lv_rewriting);
				//now to question if we need to answer these recursively
				num_new_subgoals = returned_from_gvs.size();
				System.out.println("returned_from_gvs: " + returned_from_gvs.printString());    // this is the result already
				System.out.println("num_new_subgoals: " + num_new_subgoals);
				for (j = 0; j < num_new_subgoals; j++)
				{
					check_recursively = (Predicate)returned_from_gvs.subgoalI(j);
					recursive_schema_to_check = requiresFurtherRewritingWithSchema(check_recursively,p_mediated_schema);
					System.out.println("--------------------check_recursively: " + check_recursively.printString());
					if (recursive_schema_to_check != null)
					{
						//then we need to check that recursively
						recursive_query = new Query();
						//recursive_query.setHead(check_recursively);
						new_head = new Predicate(p_query.getHead());
						new_head.setFunctionHead(check_recursively.getFunctionHead());
						recursive_query.setHead(new_head);

						recursive_query.addSubgoal(check_recursively);
						System.out.println("--------------------recursive_query: " + recursive_query.printString());
						recursive_rewritings = findRewriting(recursive_query,recursive_schema_to_check);
						if (recursive_rewritings != null){
							retval.addAll(recursive_rewritings);
						}
						
						
					}//end checking recursively
				}
				System.out.println("--------------------returned_from_gvs: " + returned_from_gvs.printString());
				conjunctive_totally_rewritten.addAllSubgoals(returned_from_gvs);
			}
			System.out.println("--------------------conjunctive_totally_rewritten: " + conjunctive_totally_rewritten.printString());
			retval.addElement(conjunctive_totally_rewritten);
		}
		
		return retval;
		
	}
	
	public static Vector useLV(Query p_query,Schema p_mediated_schema)
	{
		Vector retval = new Vector();
		//first thing we need to do is set it up so MiniCon can run. 
		//that shouldn't be hard.
		BucketEndingAlgorithm minicon = new BucketEndingAlgorithm();
		int i;
		GLAVMapping map = p_mediated_schema.getMapping();
		int num_views = map.numLVs();
		View a_view;
		for (i = 0; i < num_views; i++)
		{
			a_view = map.lvI(i);
			minicon.addView(a_view);
			System.out.println("a_view: " + a_view.printString());
		}
		minicon.setQuery(p_query);
		retval = minicon.runConjunctiveRewritings();
		return retval;
	}//end public Vector useLV(Query p_query,Schema p_mediated_schema)
	
	public static void testRewriting1()
	{
		View ms1 = new View("q(x):-e1(x,y)");
		View ms2 = new View("q(x):-f1(x,z)");
		Schema s1 = new Schema();
		s1.addRelation("e1(a,b)");
		s1.addRelation("e3(c,d)");
		s1.addRelation("e4(e,f)");
		Schema s2 = new Schema();
		s2.addRelation("f1(c,d");
		s2.addRelation("f2(e,f)");
		Mapping map = new Mapping();
		map.addView(ms1);
		map.addView(ms2);
		ms1 = new View("q2(x,y):-e3(x,y),e4(y,z)");
		ms2 = new View("q2(x,y):-f2(x,y)");
		map.addView(ms1);
		map.addView(ms2);
		SemanticMerge a_merge = new SemanticMerge();
		a_merge.setMapping(map);
		a_merge.setSchema1(s1);
		a_merge.setSchema2(s2);
		a_merge.merge();
		//now we've merged the schemas, let's rewrite a query
		//Query query = new Query("q(x):-q2(x,y,z)");//works
		//Query query = new Query("q(x,y,z):-q2(x,y,z)");
		Query query = new Query("q(x,y,z):-q2(x,y,z),e3(x,r)");
		Vector rewritings = findRewriting(query,a_merge.getMergedSchema());
		int i, num_rewritings;
		num_rewritings = rewritings.size();
		Statement rewriting;
		for (i = 0; i < num_rewritings; i++)
		{
			rewriting = (Statement)rewritings.elementAt(i);
			System.out.println(rewriting.printString().toString());
		}
	}//end public static void testRewriting1()

	public static void testRewriting2()
	{
		View ms1 = new View("q(x):-e1(x,y)");
		View ms2 = new View("q(x):-f1(x,z)");
		Schema s1 = new Schema();
		s1.addRelation("e1(a,b)");
		s1.addRelation("e3(c,d)");
		s1.addRelation("e4(e,f)");
		Schema s2 = new Schema();
		s2.addRelation("f1(c,d)");
		s2.addRelation("f2(e,f)");
		Mapping map = new Mapping();
		map.addView(ms1);
		map.addView(ms2);
		ms1 = new View("q2(x,y):-e3(x,y),e4(y,z)");
		ms2 = new View("q2(x,y):-f2(x,y)");
		map.addView(ms1);
		map.addView(ms2);
		SemanticMerge a_merge = new SemanticMerge();
		a_merge.setMapping(map);
		a_merge.setSchema1(s1);
		a_merge.setSchema2(s2);
		a_merge.merge();
		//now we've merged the first set of schemas.  let's merge on a third
		s1 = new Schema();
		s1.addRelation("g(a,b)");
		map = new Mapping();
		ms1 = new View("r1(x):-g(x,t)");
		ms2 = new View("r1(x):-q2(x,y,z),e4(y,z)");
		map.addView(ms1);
		map.addView(ms2);
		SemanticMerge second_merge = new SemanticMerge();
		second_merge.setMapping(map);
		second_merge.setSchema1(a_merge.getMergedSchema());
		second_merge.setSchema2(s1);
		second_merge.merge();
		System.out.println(second_merge.getMergedSchema().printString());
		
		//now we've merged the schemas, let's rewrite a query
		//Query query = new Query("q(x):-q2(x,y,z)");//works
		//Query query = new Query("q(x,y,z):-q2(x,y,z)");//works for first mediated schema
		Query query = new Query("s(x):-r1(x,y,z,t)");
		Vector rewritings = findRewriting(query,second_merge.getMergedSchema());
		int i, num_rewritings;
		num_rewritings = rewritings.size();
		Statement rewriting;
		for (i = 0; i < num_rewritings; i++)
		{
			rewriting = (Statement)rewritings.elementAt(i);
			System.out.println(rewriting.printString().toString());
		}
	}//end public static void testRewriting2()
	
	
	public static void main(String[] args) {
		testRewriting1();
	}
}

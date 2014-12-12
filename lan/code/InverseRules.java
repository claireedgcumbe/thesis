//import java.lang.*;
package minicon;
import java.util.Vector;

public class InverseRules extends Algorithm{

    /*the purpose of this class is to implement the inverse rules as
	*created by Duschka.  The inverse rule takes a view and creates
	*an inverse of it, basically by putting the body as the head and
	*the head as the body.  This should be fairly easy to implement;
	*the only catch comes in figuring out how to combine them; as is,
	*the only method seems to be to take the output from the inverse
	*rules and stick them into buckets and then do the entire bucket
	*algorithm but this is really, really overly stupid.  Thus, at
	*the moment I'll just implement the part that does the
	*inversion*/
	protected Vector _inverted_rules;
	protected int _num_inverted_rules;
	protected int _query_length;
	protected int _fresh_view;
	protected Vector [] _rule_buckets;
	InverseRules(){
		super();	
		_fresh_view = 0;
		_inverted_rules = new Vector(10);
		_rule_buckets = null;
		_query_length = 0;
	}//end default constructor
	
	boolean invertRules(){
		//this function inverts all of the views.
		//it should only need to be called once.
		int current_view_num;
		int counter;
		View current_view;
		Predicate current_predicate;
		Predicate new_predicate;
		View rule_to_add;
		Predicate a_pred;
		String function_body;//the body of the function, which will just be the 
		//variables of the view, actually.
		Vector distinguished_variables;
		for (current_view_num = 0; current_view_num < numViews(); current_view_num++){
			current_view = viewI(current_view_num);
			distinguished_variables = current_view.getDistinguishedVariables();
			//now, set up the function in case we have any existential variables
			function_body = current_view.getHead().printString().toString();
			//now get rid of the function head
			counter = function_body.indexOf("(");
			function_body = function_body.substring(counter);
			//note this may be obob.  need to check.
			for (current_view.first(); !current_view.isDone();  current_view.next()){
				//now we loop over the predicates and add each inverse rule to the answer
				current_predicate = current_view.current();
				rule_to_add = new View(1);// because this is an inverse rule, we know
				//that it will only have one item in the body
				rule_to_add.addSubgoal(current_view.getHead());//add the head as the only
				//body.
				new_predicate = new Predicate(current_predicate);
				rule_to_add.setHead(new_predicate);
				//we need to add in the same predicate as the function body
				//but we need to do a copy because we may be changing some of
				//the variable values if they are existentials
				for (new_predicate.first(); !new_predicate.isDone(); new_predicate.next()){
				    if (!new_predicate.current().containedInVector(distinguished_variables)){
					   
						//then we need to change this variable over to be the 
						//skolem function value
						new_predicate.replaceCurrent(new IPValue("f_" + current_view_num +
																	new_predicate.current() +
																	function_body));
						
					}//end of if the current variable was existential
				}//end looping over all of the variables in the predicate
				_inverted_rules.addElement(rule_to_add);
			}//end looping over a predicate
		}//end looping over all the views
		return true;
	}
	public String run(){
		String result_string;
		_timer.start();
		invertRules();
		Vector results;
		_mapping_creation_time = _timer.stop();
		_timer.start();
		results = makeResults();
		result_string =  makeResultString(results);
		_mapping_combination_time = _timer.stop();
		return result_string;
	}
	
	public String makeResultString(Vector results){
		/**This function takes the results and makes them into a result string
		 */
		StringBuffer retval = new StringBuffer();
		int i,j;
		int results_size = results.size();
		Vector a_rewriting;
		//int a_result_size;
		_num_rewritings = 0;
		for (i = 0; i < results_size; i++){
			a_rewriting = (Vector)results.elementAt(i);
			//a_result_size = a_rewriting.size();
			_num_rewritings++;
			retval.append(makeRewriting(a_rewriting));
		
		}
		return retval.toString();
	}
	
	public Vector makeResults(){
		/**at this point we assume that the query has been issued, and that the inverse
		 * rules have been calculated.  First, we need to figure out which inverse
		 * rules are relevant to each query subgoal.
		 */
		if (myQuery == null){
			System.out.println("You can't run InverseRules.makeResults without having a query to run");
			System.out.println("Program will now exit");
			System.exit(1);
		}
		int num_inverted_rules = _inverted_rules.size();
		_query_length = myQuery.size();					
		makeQueryBuckets(num_inverted_rules);
		//okay, now we have everything in nice bucket entries and we have to unify them.
		Vector query_vars = myQuery.findUniqueVariables();
		int i;
		int num_query_vars = query_vars.size();
		IRMapping [] mappings = makeIRMapping(myQuery);
		
			
		Vector results;
		results = unify(0, 0, mappings, new Vector(_query_length), num_query_vars);
		//now we need to change the answers into the final value...
		return results;
	}//end makeResults
	
	
	public IRMapping[] makeIRMapping(Query a_query){
		int i; 
		int j;
		int total;
		IRMapping [] ret;
		Predicate a_pred;
		Vector retval = new Vector(5 * a_query.size());
		for (i = 0, total = 0; i < a_query.size(); i++){
			a_pred = a_query.subgoalI(i);
			for (j = 0; j < a_pred.size(); j++, total++){
				retval.addElement(new IRMapping(a_pred.variableI(j),i));
			}
		}
		ret = new IRMapping[retval.size()];
		retval.copyInto(ret);
		return ret;
	}
	
	
	public Vector unify(int current_subgoal, int current_variable, IRMapping [] mappings, Vector view_mappings, int num_query_vars){
		Vector a_bucket = _rule_buckets[current_subgoal];
		int bucket_size = a_bucket.size();
		int i, j,k;
		IPValue a_query_var;
		View a_state;
		Predicate a_head;
		Predicate a_subgoal;
		int predicate_length;
		int old_view;
		IPValue a_mapping;
		Vector retval;
		Vector some_values;
		int a_size;
		Predicate query_subgoal = myQuery.subgoalI(current_subgoal);					  
		IRMapping [] new_mappings;
		boolean possible;
		int this_subgoal;
		int var_location;
		IRViewMapping new_head;
		IRViewMapping a_view_mapping;
		retval = new Vector(100);
		Vector new_heads;
		for (i = 0; i < bucket_size; i++){
			a_state = (View)a_bucket.elementAt(i);
			a_subgoal = a_state.getHead();
			new_mappings = copyMappings(mappings);
			new_heads = (Vector)view_mappings.clone();
			predicate_length = query_subgoal.size();
			_fresh_view++;
			a_view_mapping = new IRViewMapping(a_state, current_subgoal,_fresh_view);
			for (j = 0, this_subgoal = current_subgoal, possible = true, old_view = -1; j < predicate_length && possible; j++){
				a_query_var = query_subgoal.variableI(j);
				a_mapping = new_mappings[current_variable + j].mapping;
				if (!a_mapping.equals("")){
					//then we need to make sure that it's not mapped to an existential
					if (a_mapping.printString().startsWith("f_")){
						if(!a_mapping.equals(a_subgoal.variableI(j))){
							//then we can't map it
							possible = false;
						}
						else{
							old_view = new_mappings[current_variable + j].first_view;
							this_subgoal = old_view;
						}
					}
					else if (a_subgoal.variableI(j).printString().startsWith("f_")){
						possible = false;
					}
				}
				if (possible != false){
					//then we can map it, so add the mapping.
					new_mappings[j + current_variable].mapping = a_subgoal.variableI(j);
					a_view_mapping.addMapping(a_query_var,a_subgoal.variableI(j));
				}
			}//end of looping over the subgoal variables
			//if possible != false, then we could add it.
			//we only want to do something if the answer was possible
			if (possible == true){
				//then we need to add the head mapping, add query equalities, the
				//whole 9 yards.
				//first, we need to discover if the view is an old one
				if (old_view != -1){
					//then we need to change the values for these variables first
					//of all
					for (j = 0; j < predicate_length; j++){
						new_mappings[j + current_variable].first_view = old_view;
					}
					//now we need to combine the mappings in the view info
					
					
				}
				else{
					new_head = new IRViewMapping(a_state,current_subgoal, _fresh_view);
			
					for (j = current_variable; j < current_variable + predicate_length; j++){
						if (!new_mappings[j].mapping.printString().startsWith("f_")){
							new_head.addMapping(new_mappings[j].mapping, new_mappings[j].var);	
						}
					}
			
					new_heads.addElement(new_head);
				}
				//and now we need to update the info in new_mappings
				
				for(j = current_variable; j < current_variable + predicate_length; j++){
					//loop over all of the current values and change their mappings
					for(k = current_variable + predicate_length; k < new_mappings.length; k++){
						if (new_mappings[j].var.equals(new_mappings[k].var)){
							//then we need to set the mappings and first subgoal info
							new_mappings[k].mapping = new_mappings[j].mapping;
							new_mappings[k].first_view = this_subgoal;
						}
					}
				}
				
				if (current_subgoal == _query_length -1){
					//then we're done
					retval.addElement(new_heads);
				}
				else{
					//we need to recurse
					some_values = unify(current_subgoal+1, current_variable + predicate_length,new_mappings, new_heads, num_query_vars);
					a_size = some_values.size();
					for(j = 0; j < a_size; j++){
						retval.addElement(some_values.elementAt(j));
					}
				}
			}
			
		}//end of looping over the elements in the bucket.
		return retval;
	}//end unify
	
	
	private void moveMappings(Vector list, IRViewMapping later_view_mapping, int copy_to){
		/**This function assumes that copy_to has the view id of the view we wish
		 * to copy the values in old_view_mapping to
		 */
		int i, j,k;
		int list_size;
		IRViewMapping a_view_mapping;
		Vector old_values;
		Vector new_values;
		IPValue a_value;
		list_size = list.size();
		for(i = 0; i < list_size; i++){
			a_view_mapping = (IRViewMapping)list.elementAt(i);
			if (a_view_mapping.id == copy_to){
				//then we've found the right one
				for (j = 0; j < a_view_mapping.variables.length; j++){
					//now we have each variable
					old_values = a_view_mapping.mappings[j];
					new_values = later_view_mapping.mappings[j];
					for (k = 0; k < new_values.size(); k++){
						a_value = (IPValue)new_values.elementAt(k);
						if (!a_value.containedInVector(old_values)){
							old_values.addElement(a_value);
						}
					}
				}
				return;
			}//end if we've found the right one
		}
	
	}
	public IPValue findVariable(IRMapping [] mappings, int mapping_size, IPValue view_variable){
		int i;
		for (i = 0; i < mapping_size; i++){
			if (mappings[i].mapping.equals(view_variable)){
				return mappings[i].var;
			}
		}
		//at this point, we haven't found it, so mark it as a fresh var and return
		//the first one
		return new IPValue(view_variable + "fresh" + _fresh_view);
	
	}
	
	public String makeRewriting(Vector a_set){
		Predicate query_head = myQuery.getHead();
		StringBuffer retval;
		retval = new StringBuffer(query_head.getFunctionHead()+"(");
		IRViewMapping a_view_mapping;
		Vector [] equals;
		Vector an_equal;
		Vector mappings;
		int i,j,k;
		int equal_size;
		int num_vars;
		int set_size = a_set.size();
		QueryEquality an_equality = new QueryEquality();
		for (i = 0; i < set_size; i++){
			a_view_mapping = (IRViewMapping) a_set.elementAt(i);
			equals = a_view_mapping.mappings;
			equal_size = equals.length;
			for (j = 0; j < equal_size; j++){
				an_equal = equals[j];
				if (an_equal.size() > 1){
					for(k = 0; k < an_equal.size(); k++){
						an_equality.addEquality((IPValue)an_equal.elementAt(0),(IPValue)an_equal.elementAt(k));	
					}
				}
			}
		}
		//at this point we've set the equalities, now we need to actually create the rewriting
		//first, we need to get all the variables of the head.
		for (i = 0; i < query_head.size(); i++){
			retval.append(an_equality.getRepresentative(query_head.variableI(i)) + ",");
		}
		//now get rid of that last comma and prepare for the subgoals
		retval.setCharAt(retval.length()-1,')');
		retval.append(":-");
		for (i =0; i < set_size; i++){
			a_view_mapping = (IRViewMapping)a_set.elementAt(i);
			retval.append(a_view_mapping.view.subgoalI(0).getFunctionHead());
			retval.append("(");
			num_vars = a_view_mapping.variables.length;
			for (j = 0; j < num_vars; j++){
				mappings = a_view_mapping.mappings[j];
				if (mappings.size() > 0){
					retval.append(an_equality.getRepresentative((IPValue)a_view_mapping.mappings[j].firstElement()).printString());
					retval.append(",");	
				}
				else{
					retval.append("ex" + a_view_mapping.variables[j].printString() + a_view_mapping.id);
				}
			}
			//at this point we have all the variable, but we have an extra comma; we 
			//need to replace that
			retval.setLength(retval.length() -1);
			retval.append("),");
		}
		//at this point we have all the info there, but we have an extra , at the end, 
		//so change it to a \n
		retval.setCharAt(retval.length() -1, '\n');
		return retval.toString();
		
	}
	
	
	public int findMappingLocation(Mapping [] mappings, int mapping_size, String query_var){
		int i;
		for (i =0; i < mapping_size; i++){
			if (mappings[i].variable.equals(query_var)){
				return i;
			}
		}
		//at this point, we haven't found it; return -1
		return -1;
	}
	
	public IRMapping[] copyMappings(IRMapping [] mappings){
		int i;
		int mapping_size = mappings.length;
		IRMapping [] retval = new IRMapping[mapping_size];
		for (i = 0; i < mapping_size; i++){
			retval[i] = (IRMapping) mappings[i].clone();
		}
		return retval;
	}
	
	private void makeQueryBuckets(int num_inverted_rules){
		int avg_bucket_entries = num_inverted_rules / _query_length;
		_rule_buckets = new Vector[_query_length];	
		Statement a_rule;
		Predicate a_head;
		String a_value;
		int i,j;
		for (i = 0; i < _query_length; i++){
			_rule_buckets[i] = new Vector(avg_bucket_entries, avg_bucket_entries);
		}
		//at this point we have set up all the buckets; now see if each of the inverted rules
		//can be used in it
		for (i = 0; i < num_inverted_rules; i++){
			a_rule = (Statement) _inverted_rules.elementAt(i);
			a_head = a_rule.getHead();
			a_value = a_head.getFunctionHead();
			for (j = 0; j < _query_length; j++){
				if (a_value.equals(myQuery.subgoalI(j).getFunctionHead())){
					//then we need to add it to bucket j
					//if the distinguished variables check out.
					if (checkDistinguished(myQuery.getHead(), myQuery.subgoalI(j),a_rule.getHead())){
						_rule_buckets[j].addElement(a_rule);
					}
				}
			}//end looping over each bucket
		}//end putting things into the buckets
	}//end makeQueryBuckets
	
	boolean checkDistinguished(Predicate query_head, Predicate query_body, Predicate view_body){
		int i;
		IPValue a_query_var;
		IPValue a_view_var;
		int num_vars = query_body.size();
		for (i = 0; i < num_vars; i++){
			a_query_var = query_body.variableI(i);
			a_view_var = view_body.variableI(i);
			if (query_head.containsVariable(a_query_var)){
				if (a_view_var.printString().startsWith("f_")){
					return false;
				}
			}
				
		}
		
		return true;
	}//end checkDistinguished
	
	public void clear(){
		_inverted_rules = new Vector(10);
		_num_rewritings = 0;
        _num_mappings = 0;
        _view_rewrite_number = 0;
        myQuery = null;
        Views = new Vector();
        NumViews = 0;
		_rule_buckets = null;
		_query_length = 0;
	}
									
							
    
	public String type(){
		return "InverseRules";
			
	}
	
	public StringBuffer printString(){
		int i;
		StringBuffer retval = new StringBuffer();
		for (i = 0; i < _inverted_rules.size(); i++){
			retval.append(((Statement)_inverted_rules.elementAt(i)).printString());
			retval.append("\n");
			
		}
		return retval;
	}//end printString
	
	
	public static void main(String args[]){
		//the purpose of this main is just to test and make sure that 
		//I've implemented things in some vague manner that resembles correctly
		InverseRules a = new InverseRules();
		String results;
		View a_view = new View();
		a_view.read("v1(a):-e1(a,a)");
		a.addView(a_view);
		a_view = new View("v2(b):-e2(b)");
		a.addView(a_view);
		a_view = new View("v3(f):-e1(f,g),e2(g,f)");
//		a.addView(a_view);
		a.setQuery(new Query("q(x):-e1(x,y),e2(y)"));
		results = a.run();
		System.out.println(results);
		
	}//end main

}

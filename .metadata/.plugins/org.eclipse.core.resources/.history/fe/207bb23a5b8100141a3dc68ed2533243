package minicon;
import java.util.Vector;

public class IRViewMapping
{
	public View view;
	public int subgoal;
	public int id;
	public IPValue [] variables;
	public Vector [] mappings;
	/*public IRViewMapping(){
		a_view = null;
		subgoal = -1;
		variables = null;
		mappings = null;
		}
	*/
	public IRViewMapping(View a_view, int a_subgoal, int a_id){
		view = a_view;
		subgoal = a_subgoal;
		Predicate head = view.subgoalI(0);
		int i;
		id = a_id;
		int head_size = head.size();
		variables = new IPValue[head_size];
		mappings = new Vector[head_size];
		for (i = 0; i < head_size;i++){
			variables[i] = head.variableI(i);
			mappings[i] = new Vector();
			
		}
	}//end constructor view, int
	public boolean addMapping(IPValue a_var, IPValue a_mapping){
		int i, a_var_location;
		Predicate pred;
		pred = view.subgoalI(0);
		for (i = 0; i < pred.size();i++){
			if (variables[i].equals(a_var)){
				if (!mappings[i].contains(a_mapping)){
					mappings[i].addElement(a_mapping);
				}
				return true;
			}
		}
		return false;
	}
	
	public Object clone(){
		//makes a deep copy of those things that need a deep copy; makes
		//a shallow copy of variables 'cause we only need a shallow copy
		IRViewMapping retval = new IRViewMapping(view,subgoal,id);
		retval.variables = variables;
		int i;
		retval.mappings = new Vector[mappings.length];
		for (i = 0; i < mappings.length; i++){
			retval.mappings[i] = (Vector)mappings[i].clone();
		}
		return retval;
	}
}

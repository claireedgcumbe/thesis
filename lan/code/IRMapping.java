package minicon;
public class IRMapping
{
	public IPValue var;
	public IPValue mapping;
	public int first_view;
	public int subgoal;
	public IRMapping(){
		var = null;
		mapping = null;
		first_view = -1;
		subgoal = -1;
	}
	
	public IRMapping(IPValue a_var,int a_subgoal){
		var = a_var;
		mapping = new IPValue();
		first_view = a_subgoal;
		subgoal = a_subgoal;
	}
	
	public Object clone(){
	/**makes a deep copy
	 */	
		IRMapping retval = new IRMapping();
		retval.var = new IPValue(var);
		retval.mapping = new IPValue(mapping);
		retval.first_view = first_view;
		retval.subgoal = subgoal;
		return retval;
	}
}

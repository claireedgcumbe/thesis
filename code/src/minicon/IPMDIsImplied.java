package minicon;
import java.util.Vector;
public class IPMDIsImplied extends IPMD
{
	   //the stuff that was in the extension of MD
	protected boolean _predicate_conflicts;
	
	public IPMDIsImplied(IPQuery a_query, IPView a_view){
		super(a_query,a_view);
		_predicate_conflicts = false;
	}//end public IPMDIsImplied(IPQuery a_query, IPView a_view)
	
	public void calculateCoveredIPs(){
	/** The purpose of this function is to tell calculate which IPs are covered, 
	 *  and which are not. 
	 *  Actually, it strikes me that we need something else, which is something 
	 *  that tells us which
	 *  Interpreted predicates the view actually conflicts with....
	 */	
		IPValue query_lhs;
		double query_rhs;
		IPValue view_lhs;
		double view_rhs;
		IPValue query_lhs_mapped_to;
		double query_constant;
		double view_constant;
		IPValue query_var;
		IPValue view_var;
		String query_operator;
		String view_operator;
		InterpretedPredicate a_query_ip;
		InterpretedPredicate a_view_ip;
		int i,j;
		Vector query_ips = _query.getInterpretedPredicates();
		Vector view_ips = _view.getInterpretedPredicates();
		int num_query_ips = query_ips.size();
		int num_view_ips = view_ips.size();
		for (i = 0; i < num_query_ips && !_predicate_conflicts; i++){
			a_query_ip = (InterpretedPredicate) query_ips.elementAt(i);
			query_lhs = a_query_ip.getLHS();
			query_operator = a_query_ip.getOperator();
			query_rhs = a_query_ip.getRHSConstant();
			query_lhs_mapped_to = variableMappedTo(query_lhs);
			//now let's loop over the interpreted predicates in the view to 
			//see if we cover this.
			for (j = 0; j < num_view_ips && !_predicate_conflicts;j++){
				a_view_ip = (InterpretedPredicate) view_ips.elementAt(j);
				view_lhs = a_view_ip.getLHS();
				if(view_lhs.equals(query_lhs_mapped_to)){
					view_rhs = a_view_ip.getRHSConstant();
					view_operator = a_view_ip.getOperator();
					//then we need to check it
					//first, check to see if they can prove that it's covered
					if((!view_operator.equals(">") && !view_operator.equals("<")) || (!query_operator.equals(">") && !query_operator.equals("<"))){
						System.out.println("unrecognized operator in IPMDIsImplied.calculateCoveredIPs");
						System.out.println("only currently recognized operators are < or >");
						System.out.println("program will now exit");
						System.exit(1);
					}
					if (view_operator.equals(query_operator)){
						//then we can prove that they're the same, and we can't prove that
						//they're not the same.
						if (view_operator.equals(">")){
							if (view_rhs >= query_rhs){
								_predicates_covered[i] = true;
							}
						}
						else {//don't need to check if it's < because we checked that above.
							if (view_rhs <= query_rhs){
								_predicates_covered[i] = true;
							}
						}
					}//end if they were the same.
					else{
						//they weren't the same and can be disproved.
						if (view_operator.equals(">")){
							//then query_operator equals < 
							if(query_rhs <= view_rhs){
								_predicate_conflicts = true;
							}
						}
						else{ //then the view operator is < and the query operator is >
							if(view_rhs <= query_rhs){
								_predicate_conflicts = true;
							}
						}
					}//else if there could be a conflict.
				}//end if the interpreted predicates dealt with the same variables
			}//end looping over the views interpreted predicates
		}//end looping over the query's interpreted predicates
	}//end public void calculateCoveredIPs()
	
	public boolean isValid(){
		/**returns true if the mapping is okay with the interpreted predicates of 
		* the query are implied by the view mapping; false else.
		*/
		InterpretedPredicate an_ip;
		int num_query_ips;
		num_query_ips = _query.numInterpretedPredicates();
		int i;
		IPValue old_lhs;
		IPValue old_rhs;
		IPValue lhs_mapping;
		IPValue rhs_mapping;
		boolean lhs_is_constant;
		boolean rhs_is_constant;
		calculateCoveredIPs();
		if (_predicate_conflicts){
			return false;
		}
		//if there's a predicate that conflicts, there's no way we can 
		//use it and we should return false
		
		for (i = 0; i < num_query_ips; i++){
			an_ip = _query.interpretedPredicateI(i);
			old_lhs = an_ip.getLHS();
			old_rhs = an_ip.getRHS();
			lhs_is_constant = old_lhs.isNumericalConstant();
			rhs_is_constant = old_rhs.isNumericalConstant();
			if (lhs_is_constant){
				//then we assume that rhs is *not* a constant
				rhs_mapping = variableMappedTo(old_rhs);
				if (!(rhs_mapping == null || _view.variableIsDistinguished(rhs_mapping) || _predicates_covered[i])){
					return false;
				}
			}
			//at this point we know that lhs is a variable
			else if (rhs_is_constant){
				lhs_mapping = variableMappedTo(old_lhs);
				if (!(lhs_mapping == null || _view.variableIsDistinguished(lhs_mapping) || _predicates_covered[i])){
					return false;
				}
			}
			else{
				//at this point we know that they are both variables
				lhs_mapping = variableMappedTo(old_lhs);
				rhs_mapping = variableMappedTo(old_rhs);
				if (lhs_mapping != null && rhs_mapping !=null){
					if (!(_view.variableIsDistinguished(lhs_mapping) && _view.variableIsDistinguished(rhs_mapping))){
						//then we need to make sure that the predicate is covered
						if (!_predicates_covered[i] == true){
							return false;
						}
					}//if we needed to check this one
				}//end of if both of the variables are mapped
				else if (lhs_mapping == null && rhs_mapping == null){
				    //I'm not sure if I've screwed something up, but this is happening now, which 
				    //seems wrong.  On the other hand, if both sides are null, it seems like it's okay.
				    return true;
				}
				
				else{
					//one of them was null, so we need to make sure that the other one was 
					//distinguished...
					if (lhs_mapping == null && !_view.variableIsDistinguished(rhs_mapping)){
						return false;
					}
					if (rhs_mapping == null && !_view.variableIsDistinguished(lhs_mapping)){
						return false;
					}
				}//end of if one of the variables wasn't mapped 			
			}//end of if both were variables
		}//end of looping over all of the predicates
		//if we get to this part, we know that it was true, so return true
		return true;
	}//end isValid
	
    public static void main(String args[])
    {
	   IPMDIsImplied a_md = new IPMDIsImplied(
									  new IPQuery("Q('5'):-e1('5'),e2('5')"), 
									  new IPView("V(x):-e1(x),e2('5')"));
	   Mapping a_map = new Mapping("'5'","x");
	   System.out.println(a_md.addMapping(a_map));
	   a_map = new Mapping("'5'","'5'");
	   System.out.println(a_md.addMapping(a_map));
	   Vector ans = a_md.mapsToConstants();
	   for (int i = 0; i < ans.size();i++){
		  
		  System.out.println(((Mapping)ans.elementAt(i)).printString());
	   }
	   
	   
    }//end public static void main(String args[])
    
    
}



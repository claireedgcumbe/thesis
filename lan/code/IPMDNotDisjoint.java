package minicon;
public class IPMDNotDisjoint extends IPMD
{
	
    public IPMDNotDisjoint(IPQuery a_query, IPView a_view){
		super(a_query,a_view);
	}

	public boolean isValid(){
		/**for one of these mds to be valid, we must have that the md is *not* the 
		 * opposite of what the one in the query is
		 */
		if (_graph == null){
			//then we need to calculate it.
			calculateCoveredIPs();//we'll need to know this anyway
		}
		int i;
		InterpretedPredicate an_ip;
		for(i = 0; i < _num_ips; i++){
			an_ip = _query.interpretedPredicateI(i).invertIP();
			if (_graph.isImplied(an_ip)){
				return false;
			}
		}
		//if we get here, it's okay
		return true;
	}
}

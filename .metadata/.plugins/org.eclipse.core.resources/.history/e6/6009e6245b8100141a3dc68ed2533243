package minicon;
import java.util.Vector;

public class IPConstantsMetaMD extends IPMetaMD
{
    //the point of this class is to allow us to have a meta md that
    //does the right thing for when there are constants in the query
    //rather than just being in the comparison predicates
    //the most recent set of predicates covered checked

    public IPConstantsMetaMD(int a_size, int num_predicates)
    {
	   super(a_size, num_predicates);

    }

    public IPConstantsMetaMD(IPMetaMD a_meta_md)
    {   
	   //sort of copy constructor to avoid stupidity of java not
	   //letting me do what I clearly think I should be able to do, but
	   //perhaps I'm just nuts
	   int i;
	   size = a_meta_md.size;
	   _num_predicates = a_meta_md._num_predicates;
        MDList = (Vector) a_meta_md.MDList.clone();
        VariablesMapped = (Vector) a_meta_md.VariablesMapped.clone();
        numUncovered = a_meta_md.numUncovered;
	   _predicates_covered = (boolean []) a_meta_md._predicates_covered.clone();
	   
	   subgoalsCovered = (boolean []) a_meta_md.subgoalsCovered.clone();

    }

    

        public boolean addMD(IPMD aMD){
        int i;
	   boolean [] new_covered = newSubgoalsCovered(aMD);
	   boolean [] new_predicates_covered;
        if (new_covered != null){
            //if we can add this one
            MDList.addElement(aMD);
		  subgoalsCovered = new_covered;
		  numUncovered = size;
		  for(i = 0; i < size; i++){
			 if (subgoalsCovered[i]){
				numUncovered--;
			 }
		  }
            Vector aMD_vars_mapped = aMD.variablesMapped();
            for (i = 0; i < aMD_vars_mapped.size();i ++){
                if (!VariablesMapped.contains(aMD_vars_mapped.elementAt(i))){
                    VariablesMapped.addElement(aMD_vars_mapped.elementAt(i));
                }
            }
		  //now we need to add on the stuff that will allow us to see what
		  //new predicates have been covered
		  for (i = 0; i < _num_predicates; i++){
			 new_predicates_covered = aMD.getCoveredIPs();
			 if(new_predicates_covered[i] &&_predicates_covered[i] == false){
				_predicates_covered[i] = true;
				_num_predicates_uncovered--;
			 }
		  }
            return true;
            
        }//end if
        else{
            //otherwise, there was overlap, so check to see if all of
            //the things in there are constants which map to one
            //another, if so, it's okay, otherwise not
		  return false;
		  
        }
    }//end addMD

	private boolean[] newSubgoalsCovered(IPMD aMD){
		//returns null if they aren't distinct, the new set otherwise
		boolean [] retval = (boolean []) subgoalsCovered.clone();
		System.out.println("got to newSubgoalsCovered");
		
		int i;
		for (i = 0; i < size; i++){
			if (aMD.checkCovered(i)){ // then we need to make sure that it's set to 
				//true.  However, if both of them cover the same one, then we don't
				//want to use it, so it returns false.
				retval[i] = true;
				if (subgoalsCovered[i] == true){
				    //now we need to check if we can get away with it because the
				    //only thing mapped are both to the same constants.
				    System.out.println("right before not all constants");
				    
				    if (notAllConstants(aMD,i)){
					   return null;
				    }
				    
				}
				
				//note, this was previously subgoalsCovered[i] = true, which may
				//have been causing our little bug.
			}//end if it was covered in the MD we are adding
		}//end for
		return retval;
	}

    private boolean notAllConstants(IPMD aMD, int a_subgoal)
    {
	   //returns true if all of the things mapped in the subgoal
	   //a_subgoal were mapped to the same constant in both aMD and
	   //this
	   Predicate subgoal = _query.getStatement().subgoalI(a_subgoal);
	   IPValue query_slot;
	   IPValue meta_md_val;
	   IPValue amd_val;
	   System.out.println("Got to notAllConstants");
	   
	   for (subgoal.first(); !subgoal.isDone(); subgoal.next()){
		  query_slot = subgoal.current();
		  amd_val = aMD.variableMappedTo(query_slot);
		  meta_md_val = _query_equality.getRepresentative(query_slot);
		  if (!amd_val.isAnyConstant() || !meta_md_val.isAnyConstant() || !amd_val.equals(new IPValue(meta_md_val))){
			 return false;
		  }
		  
	   }
	   return false;
	   

    }//end private boolean notAllConstants(int a_subgoal)
    
    public static void main(String args[])
    {
	   
    }//end public static void main(String args[])

}//end class IPConstantsMetaMD

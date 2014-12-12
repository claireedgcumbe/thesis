//import java.lang.*;
package minicon;
import java.util.Vector;
public class MetaMDWithoutExistentialCheck extends MetaMD{
    
    public MetaMDWithoutExistentialCheck(int a_size){
		super(a_size);
	}//end constructor

	public boolean addMD(MD aMD){
		   return addMD((MDWithoutExistentialCheck) aMD);
	}
    
    public boolean addMD(MDWithoutExistentialCheck aMD){
        int i;
		boolean [] new_covered = subgoalsDistinct(aMD);
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
        //    Vector aMD_vars_mapped = aMD.variablesMapped();
 /*           for (i = 0; i < aMD_vars_mapped.size();i ++){
                if (!VariablesMapped.contains(aMD_vars_mapped.elementAt(i))){
                    VariablesMapped.addElement(aMD_vars_mapped.elementAt(i));
                }
            }
			*/
            //Vector aMD_n = aMD.getN();
            return true;
            
        }//end if
        else{
            //otherwise, there was overlap, so return false
            return false;
        }
    }//end addMD
    
	private boolean[] subgoalsDistinct(MDWithoutExistentialCheck aMD){
		//returns null if they aren't distinct, the new set otherwise
		boolean [] retval = (boolean []) subgoalsCovered.clone();
		int i;
		for (i = 0; i < size; i++){
			if (aMD.checkCovered(i)){ // then we need to make sure that it's set to 
				//true.  However, if both of them cover the same one, then we don't
				//want to use it, so it returns false.
				if (subgoalsCovered[i]){
					return null;
				}
				retval[i] = true;
				//note, this was previously subgoalsCovered[i] = true, which may
				//have been causing our little bug.
			}//end if it was covered in the MD we are adding
		}//end for
		return retval;
	}
	
    public MetaMDWithoutExistentialCheck copy(){
        int i;
        MetaMDWithoutExistentialCheck retval = new MetaMDWithoutExistentialCheck(size);
        retval.MDList = (Vector) MDList.clone();
//        retval.VariablesMapped = (Vector) VariablesMapped.clone();
        retval.numUncovered = numUncovered;
        for (i = 0; i < size; i++){
            retval.subgoalsCovered[i] = subgoalsCovered[i];
        }       
        return retval;
    }//end of copy
    
}    

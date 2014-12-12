//import java.lang.*;
package minicon;
import java.util.Vector;
public class QuasiTreeMetaMD {
	Vector MDList;
	boolean [] subgoalsCovered;
	int numUncovered;
	int size;
	int _num_rewritings;
	Predicate _query_head;
	int _num_buckets;
    public QuasiTreeMetaMD(Predicate query_head, int a_size){
		MDList = new Vector();
		size = a_size;
		_query_head = query_head;
		subgoalsCovered = new boolean[size];
        for (int i = 0; i < size; i++){
            subgoalsCovered[i] = false;
        }
		_num_buckets = 0;
		numUncovered = a_size;
		_num_rewritings = 0;
	}//end constructor
/*
	public boolean addMD(MD aMD){
		//okay, this is not the way to do this, but it'll do for the moment
		System.out.println("can't actually call QuasiTreeMetaMD.addMD");
		System.out.println("System will exit");
		System.exit(1);
		return false;
	}
  */  
		public int getNumUncovered(){
		return numUncovered;
	}
	
	public int getNextUncovered(int current){
		//returns the number of the next uncovered subgoal after current
		//if there are none, it returns -1
		if (numUncovered == 0){
			return -1;
		}
		for (int i = current+1; i < size; i++){
			if (subgoalsCovered[i] == false){
				return i;
			}
		}//end for
		return -1;
	}//end getNextUncovered
	
    public boolean addMD(QuasiTreeBucketEntry aMD){
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
			_num_buckets++;
/*            Vector aMD_vars_mapped = aMD.variablesMapped();
            for (i = 0; i < aMD_vars_mapped.size();i ++){
                if (!VariablesMapped.contains(aMD_vars_mapped.elementAt(i))){
                    VariablesMapped.addElement(aMD_vars_mapped.elementAt(i));
                }
            }
*/            return true;
            
        }//end if
        else{
            //otherwise, there was overlap, so return false
            return false;
        }
    }//end addMD
    
	private boolean[] subgoalsDistinct(QuasiTreeBucketEntry aMD){
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
	
    public QuasiTreeMetaMD copy(){
        int i;
        QuasiTreeMetaMD retval = new QuasiTreeMetaMD(_query_head,size);
        //retval.MDList = (Vector) MDList.clone();
        //retval.VariablesMapped = (Vector) VariablesMapped.clone();
        retval.numUncovered = numUncovered;
        for (i = 0; i < size; i++){
            retval.subgoalsCovered[i] = subgoalsCovered[i];
        } 
		//sadly we can't just rely on MDList.clone anymore...
		int md_list_size = MDList.size();
		retval.MDList = new Vector(md_list_size);
		for (i = 0; i < md_list_size; i++){
			retval.MDList.addElement(((QuasiTreeBucketEntry)MDList.elementAt(i)).copy());
		}
		retval._query_head = _query_head;
		retval._num_rewritings = _num_rewritings;
		retval._num_buckets = _num_buckets;
        return retval;
    }//end of copy
	
	public void setHead(Predicate query_head){
		_query_head = query_head;
	}
	public String getRewritings(){
		//at this point what we have is a list of QuasiTreeBucket entries, where we
		//*know* that each element from the cross product is a contained rewriting.
		//so all we need to do is take each element from the cross product, add
		//in the query equality, and we're golden
		//so call it recursively
		return getRecursiveRewritings(0,0,new MetaMDLite(_query_head,size)).toString();
		
	}//end getRewritings
	
	public int getNumRewritings(){
		return _num_rewritings;
	}
	
	public StringBuffer getRecursiveRewritings(int which_bucket, int place_in_bucket,MetaMDLite so_far){
		/**we assume that we have never called this when which_bucket == num_buckets;
		 */
		int i;
		StringBuffer retval = new StringBuffer();
		QuasiTreeBucketEntry a_bucket = (QuasiTreeBucketEntry)MDList.elementAt(which_bucket);
		int bucket_size_minus = a_bucket.size()-1;
		MetaMDLite new_meta_md = new MetaMDLite(so_far);
		new_meta_md.addMD(a_bucket.MDI(place_in_bucket));
		if (place_in_bucket != bucket_size_minus){
			retval.append(getRecursiveRewritings(which_bucket,place_in_bucket +1,so_far));
		}
		if (which_bucket == _num_buckets-1){
			//then we need to make this rewriting
			retval.append(new_meta_md.printString());
			_num_rewritings++;
		}
		else{
			//we need to recurse
			retval.append(getRecursiveRewritings(which_bucket +1,0,new_meta_md));
		}
		
		//now we know how big the buckets are
		return retval;
	}//end getRecursiveRewritings
	
}    

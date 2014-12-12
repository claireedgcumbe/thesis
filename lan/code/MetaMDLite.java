//import java.lang.*;
package minicon;
import java.util.Vector;
public class MetaMDLite{
    protected Vector MDList;
    protected  Predicate _query_head;
    protected QueryEquality _query_equality;
    protected boolean [] _subgoals_covered;
	protected int _size;
    public MetaMDLite(Predicate query_head, int a_size){
        MDList = new Vector(a_size);
        _query_equality = null;
        _query_head = query_head;
		_size = a_size;
		_subgoals_covered = new boolean[_size];
		for(int i = 0; i < _size; i++){
			_subgoals_covered[i] = false;
		}
	}//end constructor

	public MetaMDLite(MetaMDLite original){
		_query_equality = null;
		_query_head = original._query_head;
		MDList = (Vector)original.MDList.clone();
		_size = original._size;
		_subgoals_covered = new boolean[_size];
		for (int i = 0; i < _size; i++){
			_subgoals_covered[i] = original._subgoals_covered[i];
		}
	}
   
	public void addMD(MD an_md){
		MDList.addElement(an_md);
		//temporary check for debugging purposes
		for(int i = 0; i < _size; i++){
			if (an_md.checkCovered(i)){
				if (_subgoals_covered[i] == true){
					//then we've done something bad
					System.out.println("Uh Oh; covered non disjoint subsets in MetaMDLite");
					System.exit(1);
				}
				else{
					_subgoals_covered[i] = true;
				}
			}
		}
	}
	
    protected void mergeQueryEqualities(){
        int i;
        MD a_md;
        if (_query_head == null){
            System.out.println("can't run mergeQueryEqualities in MetaMD without setting _query_head");
            System.exit(1);
        }
        _query_equality = new QueryEquality(_query_head.variables);
        for (i = 0; i < MDList.size();i++){
            a_md = (MD) MDList.elementAt(i);
            _query_equality.mergeEqualities(a_md._view_equalities);
        }
        _query_equality.sort();
    }
    
    public String printString(){
        int i;
        //we can't do this without a query equality, so if it's not there, bomb
        if (_query_equality == null){
			mergeQueryEqualities();
        }
        if (_query_head == null){
        }
		//okay, we've assumed that we *know* at this point that we have all of the subgoals covered,
		//but for the sake of being thorough, let's check
		for (i = 0; i < _size; i++){
			if (_subgoals_covered[i] == false){
				//then we've screwed up
				System.out.println("not all subgoals covered in MetaMDLite; exiting");
				System.exit(1);
			}
		}
        StringBuffer retval= new StringBuffer("q(");  
        for (_query_head.first(); !_query_head.isDone(); _query_head.next()){
            retval.append(_query_equality.getRepresentative(_query_head.current()));
            retval.append(",");
        }
		if (_query_head.size() != 0){//there were distinguished variables
			retval.setLength(retval.length() -1);//get rid of the last comma
		}
        retval.append("):-");
        for (i = 0; i < MDList.size()-1;i++){
    
            retval.append(((MD)MDList.elementAt(i)).printRewriting(_query_equality));
            retval.append(",");
        }
        retval.append(((MD)MDList.elementAt(i)).printRewriting(_query_equality));
        return retval.toString();
    } //end printString

}    

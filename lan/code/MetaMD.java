//import java.lang.*;
package minicon;
import java.util.Vector;
public abstract class MetaMD{
    protected Vector MDList;
//    protected Vector VariablesMapped;
    protected Vector EList;
    protected  Predicate _query_head;
    protected QueryEquality _query_equality;
    protected int size;
    protected boolean [] subgoalsCovered;
    protected int numUncovered;
    
    public MetaMD(int a_size){
        size = a_size;
        subgoalsCovered = new boolean[a_size];
        MDList = new Vector(a_size);
        for (int i = 0; i < a_size; i++){
            subgoalsCovered[i] = false;
        }
        EList = new Vector(a_size);
        numUncovered = size;
//        VariablesMapped = new Vector(a_size);
        _query_equality = null;
        _query_head = null;
	}//end constructor

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
								
    public void setQueryHead(Predicate a_head){
        _query_head = a_head;
    }
    
    public Predicate getQueryHead(){
        return _query_head;
    }
    
    public void setQueryEquality(QueryEquality an_equality){
        _query_equality = an_equality;
    }
    
    public QueryEquality getQueryEquality(){
        return _query_equality;
    }
    
    public void mergeQueryEqualities(){
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
    
    public boolean checkCovered(int num){
        return subgoalsCovered[num];
    }
   
    public abstract boolean addMD(MD aMD);
//    public abstract MetaMD copy();
    
    public void setCovered(int num){
        if (!subgoalsCovered[num]){
            numUncovered--;
            subgoalsCovered[num] = true;
        }
    }//end of setCovered

    public String printString(){
        int i;
        //we can't do this without a query equality, so if it's not there, bomb
        if (_query_equality == null){
            System.out.println("Can't print out without the list of equalities in MetaMD");
            System.exit(0);
        }
        if (_query_head == null){
            System.out.println("Can't print without the head in MetaMD");
        }
        StringBuffer retval= new StringBuffer("q(");  
        for (_query_head.first(); !_query_head.isDone(); _query_head.next()){
		  retval.append((_query_equality.getRepresentative(_query_head.current())).printString());
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

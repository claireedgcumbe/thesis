//import java.lang.*;
package minicon;
import java.util.Vector;
public class IPMetaMD{
    protected IPQuery _query;
    protected Vector MDList;
    protected Vector VariablesMapped;
    protected Vector EList;
    protected Predicate _query_head;
    protected QueryEquality _query_equality;
    protected int size;
    protected boolean [] subgoalsCovered;
    protected int numUncovered;
    protected boolean[] _predicates_covered;
    protected int _num_predicates;
    protected int _num_predicates_uncovered;
	
    protected IPMetaMD()
    {
	   //Note, this is getting called by some of the sub class's
	   //methods.  No clue why, 'cause it really shouldn't be.  But
	   //I'll just make it private so that no one else can do it by
	   //accident.
	   
    }
    

    public IPMetaMD(IPQuery a_query)
    {
	   this(a_query.size(),a_query.numInterpretedPredicates());
    }
    
    public IPMetaMD(int a_size, int num_predicates){
        size = a_size;
        subgoalsCovered = new boolean[a_size];
        MDList = new Vector(a_size);
		int i;
        for (i = 0; i < a_size; i++){
            subgoalsCovered[i] = false;
        }
        EList = new Vector(a_size);
        numUncovered = size;
        VariablesMapped = new Vector(a_size);
        _query_equality = null;
        _query_head = null;
		_num_predicates = num_predicates;
		_predicates_covered = new boolean[_num_predicates];
		_num_predicates_uncovered = _num_predicates;
		for (i = 0; i < _num_predicates; i++){
			_predicates_covered[i] = false;
		}
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
	
    public void setQueryHead(IPQuery a_query){
        _query_head = a_query.getHead();
	   _query = a_query;
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
        IPMD a_md;
        if (_query_head == null){
            System.out.println("can't run mergeQueryEqualities in MetaMD without setting _query_head");
            System.exit(1);
        }
        _query_equality = new QueryEquality(_query_head.variables);
        for (i = 0; i < MDList.size();i++){
            a_md = (IPMD) MDList.elementAt(i);
            _query_equality.mergeEqualities(a_md._view_equalities);
        }
        _query_equality.sort();
    }
    
    public boolean checkCovered(int num){
        return subgoalsCovered[num];
    }
   
    
    public void setCovered(int num){
        if (!subgoalsCovered[num]){
            numUncovered--;
            subgoalsCovered[num] = true;
        }
    }//end of setCovered

    public boolean mapsToExistentialNonConstant(IPValue val)
    {
	   int size = MDList.size();
	   int i;
	   IPMD a_md;
	   for (i = 0; i < size; i++){
		  a_md = (IPMD)MDList.elementAt(i);
		  if (a_md.mapsToExistentialNonConstant(val)){
			 return true;
		  }
		  
	   }
	   //then it wasn't mapped to an existential non constant, so return false
	   return false;
    }
    

    public Vector getVariablesMappedTo(IPValue a_val)
    {
	   Vector retval = new Vector();
	   int i;
	   IPValue a_mapping;
	   
	   int size = MDList.size();
	   IPMD a_md;
	   
	   for (i = 0; i < size; i++){
		  a_md = (IPMD)MDList.elementAt(i);
		  a_mapping = a_md.variableMappedTo(a_val);
		  if (a_mapping != null){
			 retval.addElement(a_mapping);
		  }
		  
	   }
	   return retval;
	   
	   
    }
    

    public String printString(){
	   boolean printed_an_ip = false;
        int i;
        //we can't do this without a query equality, so if it's not there, bomb
        if (_query_equality == null){
            System.out.println("Can't print out without the list of equalities in MetaMD");
            System.exit(0);
        }
        if (_query_head == null){
            System.out.println("Can't print without the head in MetaMD");
        }
        StringBuffer retval= new StringBuffer(_query_head.getFunctionHead() + "(");  
        for (_query_head.first(); !_query_head.isDone(); _query_head.next()){
            retval.append(_query_equality.getRepresentative(_query_head.current()).printString());
            retval.append(",");
        }
		if (_query_head.size() != 0){//there were distinguished variables
			retval.setLength(retval.length() -1);//get rid of the last comma
		}
        retval.append("):-");
        for (i = 0; i < MDList.size()-1;i++){
    
            retval.append(((IPMD)MDList.elementAt(i)).printRewriting(_query_equality));
            retval.append(",");
        }
        retval.append(((IPMD)MDList.elementAt(i)).printRewriting(_query_equality));
		//at this point we need to add on any interpreted predicates that we've left uncovered
	   retval.append(";");
	   InterpretedPredicate an_ip;
	   for (i = 0; i < _num_predicates-1; i++){
		  if (!_predicates_covered[i]){
			 retval.append(_query.interpretedPredicateI(i).printString(_query_equality));
			 retval.append(",");
			 printed_an_ip = true;
			 
		  }
		  
	   }
	   if (_num_predicates != 0 && !_predicates_covered[i]){
		  retval.append(_query.interpretedPredicateI(i).printString(_query_equality));
		  printed_an_ip = true;
		  
	   }
	   //note, it's not keeping track of num_uncovered like it should be; I should check on this.
	   if (!printed_an_ip){
		  
		  retval.setLength(retval.length() -1);//get rid of the
		  //semi-colon if there were no interpreted predicates
	   }
	   
        return retval.toString();
    } //end printString

    public boolean addMD(IPMD aMD){
        int i;
		boolean [] new_covered = subgoalsDistinct(aMD);
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
            //otherwise, there was overlap, so return false
            return false;
        }
    }//end addMD
    
	private boolean [] getPredicatesCovered(){
		return _predicates_covered;	
	}
	
	private boolean[] subgoalsDistinct(IPMD aMD){
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


    public Vector mapsToConstants()
    {
	   //Returns all of the items that map to constants
	   Vector retval = new Vector();
	   int j;
	   int mapping_size;
	   IPMD a_md;
	   Vector temp;
	   
	   mapping_size = MDList.size();
	   for (j = 0; j < MDList.size(); j++){
		  a_md = (IPMD)MDList.elementAt(j);
		  //		  System.out.println("checked md");
		  temp = a_md.mapsToConstants();
		  //		  System.out.println(temp);
		  
		  retval.addAll(temp);
		  
					 
	   }
	   return retval;
	   
    }
    
    public IPMetaMD copy(){
        int i;
        IPMetaMD retval = new IPMetaMD(size,_num_predicates);
        retval.MDList = (Vector) MDList.clone();
        retval.VariablesMapped = (Vector) VariablesMapped.clone();
        retval.numUncovered = numUncovered;
		retval._num_predicates = _num_predicates;
		retval._predicates_covered = new boolean[_num_predicates];
		for (i = 0; i < _num_predicates; i++){
			retval._predicates_covered[i] = _predicates_covered[i];
		}
        for (i = 0; i < size; i++){
            retval.subgoalsCovered[i] = subgoalsCovered[i];
        }       
        return retval;
    }//end of copy

    public static void main(String args[])
    {
	   IPMDIsImplied a_md;
	   int i;
	   Mapping a_map;
	   IPQuery aquery =  new IPQuery("Q(a):-e(a),e2(x),e3('5')");
	   IPMetaMD ametamd = new IPMetaMD(aquery);
	   
	   for (i = 0; i < 10; i++){
		  
		  a_md = new IPMDIsImplied(aquery, new IPView("V(x):-e1(x),e2('1'), e2('2'), e2('3'), e2('4'), e2('5'), e2('6'), e2('7'), e2('8'), e2('9'), e2('10')"));
		  a_md.addMapping(new Mapping("x", "'" + i + "'"));
		  
		  ametamd.addMD(a_md);
		  
	   }

	   Vector ans = ametamd.mapsToConstants();
	   System.out.println("got to right before the end");
	   
	   for (i = 0; i < ans.size();i++){
		  
		  System.out.println(((Mapping)ans.elementAt(i)).printString());
	   }
		  
		  
    }//end public static void main(String args[])
    
    
}    

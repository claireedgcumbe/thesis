//import java.lang.*;
package minicon;
import java.util.Vector;
//note, need to find out exactly how we can assign to static variables
public abstract class IPMD{
    //the purpose of this class is to allow us to keep track of
    //what's being done in our algorithm

	
    protected boolean _predicates_covered[];   
    protected boolean _is_satisfiable;
    protected static int uidsUsed=0;
    protected IPView _view;
    protected int uid;
    protected int _size;
    protected SimpleComparisons _graph;
    protected Vector mapping; //this is the list of mappings, oh for a
    //templated Vector class....
    protected Vector _h_equalities; //this *looks* like a mapping, but
    //it's really an equality... stupid, but true
    protected QueryEquality _view_equalities;
    protected IPQuery _query;
    protected boolean subgoalsCovered[];
    protected int _num_ips;
	
    public IPMD(IPQuery a_query, IPView a_view){
        _view = a_view;
        _size = a_query.size();
        mapping = new Vector(5);
	   _query = a_query;
        _h_equalities = new Vector(5);
        _view_equalities = new QueryEquality();
        subgoalsCovered = new boolean[_size];
	   _is_satisfiable = true;
        uid = uidsUsed;
        uidsUsed++;
	   int i;
        for (i = 0; i < _size; i++){
            subgoalsCovered[i] = false;
        }
	   _graph = null;
	   _num_ips = a_query.numInterpretedPredicates();
	   _predicates_covered = new boolean[_num_ips];
	   for (i = 0; i < _num_ips; i++){
		  _predicates_covered[i] = false;
	   }

    }
    
    public boolean sameSubgoalsCovered(IPMD a_md){
        //returns true if it has the same number of subgoals 
        //covered, false else
        int i; 
        for (i = 0; i < _size; i++){
            if (a_md.subgoalsCovered[i] != subgoalsCovered[i]){
                //then there was a difference, return false
                return false;
            }
        }
        //if we make it to the end with no difference, then it was
        //the same, so return true
        return true;
    }
    
    
    public void setSubgoalCovered(int sub){
        subgoalsCovered[sub] = true;
    }
 
    public void setView(IPView aview){
        _view = new IPView(aview);
    }

    
    public IPView getView(){
        return _view;
    }

    public void setQuery(IPQuery a_query){
	   _query = a_query;
    }
	
    public IPQuery getQuery(){
	   return _query;
    }
	
    public int firstSubgoalCovered(){
	   //returns the subgoal number of the first subgoal that was covered,
	   //if there were no subgoals covered, it returns -1
	   for (int i = 0; i < _size; i++){
		  if (subgoalsCovered[i] == true){
			 return i;
		  }
	   }//end for
	   //if we've gotten this far, none were covered, so return the default.
	   return -1;
    }//end firstSubgoalCovered
		
    public Vector variablesMapped(){
        Vector retval= new Vector(mapping.size()); 
        for (int i = 0; i < mapping.size();i++){
            retval.addElement(((Mapping)mapping.elementAt(i)).variable);
        }
        return retval;
    }
       
    public int size(){
        return _size;
    }
    
    public Vector getEqualities(){
        return _h_equalities;
    }

    public boolean mapsToExistentialNonConstant(IPValue val)
    {
	   IPValue mapped_to = variableMappedTo(val);
	   if (!mapped_to.isAnyConstant() && !_view.variableIsDistinguished(mapped_to)){
		  return true;
	   }
	   
	   return false;
    }
    
    
    public boolean addEquality(IPValue one, IPValue two)
    {
        //this function adds a string to the vector for that set of
        //strings.  It is saved in the first one that matches.
        //I think that I may need to check for inconsistencies, but
        //I'm not entirely sure how I should go about doing that.
        //thus, at the moment it always returns true
        Vector temp;
        IPValue current_val;
        int i, j;
        for (i = 0; i < _h_equalities.size();i++){
            temp = (Vector) _h_equalities.elementAt(i);
            for (j = 0; j < temp.size(); j++){
                current_val = (IPValue)temp.elementAt(j);
                if (current_val.equals(one)){
                    temp.addElement(two);
                    return true;
                }
                if (current_val.equals(two)){
                    temp.addElement(one);
                    return true;
                }
            }//end for j loop
        }//end for i loop
        //we only get here if the value has not been added elsewhere
        temp = new Vector(5);
        temp.addElement(one);
        temp.addElement(two);
        _h_equalities.addElement(temp);
        return true;
    }//end addEquality
                    
    public void print(){
        System.out.println(printString());
    }
    public boolean checkCovered(int subgoal_index){
        //this function returns true if the subgoal is covered, false else
        return subgoalsCovered[subgoal_index];
    }
    
    public IPValue mappingToVariable(IPValue varInView){
        int i; 
        for (i = 0; i < mapping.size(); i++){
            if (varInView.equals(((Mapping)mapping.elementAt(i)).mapping)){
                return ((Mapping)mapping.elementAt(i)).variable;
            }
        }
        return null;
    }
    
    public String printRewriting(QueryEquality an_equality){
        StringBuffer retval = new StringBuffer("");
        Predicate md_head;
        md_head = _view.getHead();
        retval.append(md_head.function);
        retval.append("(");  
        IPValue query_variable;
        IPValue a_variable;
        for (md_head.first();!md_head.isDone();md_head.next()){
            //now we are going to loop over the variables
            //and do the actual assigning.  We will choose the 
            //variable to be that variable if it was mapped in the
            //mapping, and v[viewnumber]_varname else
            a_variable = md_head.current();
            query_variable = mappingToVariable(a_variable);
            if (query_variable == null){
                //if it was not mapped to anything, print it out as is
                retval.append("ccd");
                retval.append(uid);// append the unique id for the mapping
                retval.append("_");
                retval.append(a_variable.printString());
            }
            else {
                //it was mapped to samething, so print that out
                retval.append(an_equality.getRepresentative(query_variable).printString());
            }
		  retval.append(",");
        }//end of looping over the variables in the head except the last one
        //now print out the last one
        //actually, at the moment, let's just try deleting that last comma
        retval.setLength(retval.length() -1);
        retval.append(")");
        return retval.toString();
    }//end printListString
   
    public boolean mapsVariable(IPValue variable){
        return variableMappedTo(variable) != null;
    }
    
    public Vector mapsToConstants()
    {
	   //Returns all of the items that map to constants
	   Vector retval = new Vector();
	   int i;
	   //System.out.println("entered mapsToConstants");
	   int mapping_size = mapping.size();
	   //	   System.out.println("mapping_size = " + mapping_size);
	   
	   for (i = 0; i < mapping_size; i++){
		  //  System.out.println(((Mapping)mapping.elementAt(i)).mapping.printString());
		  
		  if (((Mapping)mapping.elementAt(i)).mapping.isAnyConstant()){
			 retval.addElement(mapping.elementAt(i));
			 // System.out.println(((Mapping)mapping.elementAt(i)).printString());
			 
		  }
		  
	   }
	   return retval;
	   

    }//end Vector mapToConstants()
    
    public IPValue variableMappedTo(IPValue variable){
        //this function returns what a variable is mapped to; if it
        //is not mapped to anything, it returns null
        int i;
        for (i = 0; i < mapping.size();i++){
            if (variable.equals(((Mapping)mapping.elementAt(i)).variable)){
                return ((Mapping)mapping.elementAt(i)).mapping;
            }
        }
        //at this point, we know that it has not been mapped, so
        return null;
    }//end variableMappedTo
    
    public Vector getViewVariableEqualities(String a_variable){
	   for (int i = 0; i < _h_equalities.size();i++){
		  if (((Vector) _h_equalities.elementAt(i)).contains(a_variable)){
			 return (Vector)_h_equalities.elementAt(i);
		  }//end if this is the equality that we want
	   }//end for
	   //at this point, we haven't seen anything that matches it, so return null				
	   return null;
    }//end getViewVariableEqualities
	
    public StringBuffer printString(){
        StringBuffer output = new StringBuffer("(");
        output.append(_view.printString().toString());
        int i,j;
        Vector temp;
        output.append(",");
        output.append(uid);
        output.append(", {");
        if (mapping.size() > 0){
            for (i = 0; i < mapping.size()-1;i++){
                output.append(((Mapping)mapping.elementAt(i)).printString().toString());
                output.append(",");
            }//end for
            output.append(((Mapping)mapping.elementAt(i)).printString().toString());
            output.append("}");
        }//if mapping.size > 0
        output.append(" ");
        if (_h_equalities.size()> 0) {
            for (i = 0; i < _h_equalities.size();i++){
                temp = (Vector) _h_equalities.elementAt(i);
                for(j = 0; j < temp.size(); j++){
                    output.append((String)temp.elementAt(j));
                    output.append(" = ");
                }
                output.append(";");
            }
        }
        output.append("{");
        for (i = 0; i < _size; i++){   
            if (subgoalsCovered[i] == true){
                output.append(i);
                output.append(",");
            }
        }
        output.append("})");
	   if (_num_ips > 0){
		  output.append("; predicates covered = {");
			
		  for (i = 0; i < _num_ips; i++){
			 if (_predicates_covered[i] = true){
				output.append(i);
				output.append(",");
			 }
		  }
		  output.append("}");
	   }
		
        return output;
    }//end printString
    
    public boolean[] getCoveredIPs(){
	   if (_graph == null){
		  calculateCoveredIPs();
	   }
	   return _predicates_covered;
    }
	
    public int numInterpretedPredicates(){
	   return _num_ips;
    }
	
    public abstract boolean isValid();

    public void calculateCoveredIPs(){
	   int i;
	   InterpretedPredicate an_ip;
	   //_graph = new  Graph();
	   _graph = new SimpleComparisons(_query.getInterpretedPredicates());
	   IPValue old_lhs;
	   IPValue old_rhs;
	   IPValue new_lhs = null;
	   IPValue new_rhs = null;
	   boolean lhs_is_constant;
	   boolean rhs_is_constant;
	   int num_query_ips;
	   //_graph.setInequalities(new Inequality(_view.getVariables(), _view.getConstants()));
	   InterpretedPredicate translated_ip;
	   for (i = 0; i < _num_ips; i++){
		  an_ip = _query.interpretedPredicateI(i);
		  old_lhs = an_ip.getLHS();
		  old_rhs = an_ip.getRHS();
		  lhs_is_constant = old_lhs.isNumericalConstant();
		  rhs_is_constant = old_rhs.isNumericalConstant();
		  translated_ip = new InterpretedPredicate();
		  translated_ip.setOperator(an_ip.getOperator());
		  if (!lhs_is_constant){
			 new_lhs = new IPValue(mappingToVariable(old_lhs));
			 if (new_lhs == null){
				//then we just take the old value with a distinct name
				new_lhs = new IPValue(old_lhs.getVariable() + "in_ccd");
			 }
		  }
		  if (!rhs_is_constant){
			 new_rhs = new IPValue(mappingToVariable(old_rhs));
			 if (new_rhs == null){
				//then we take the old value with a distinct name;
				new_rhs = new IPValue(old_rhs.getVariable() + "in_ccd");
			 }
		  }
		  if (lhs_is_constant){
			 translated_ip.setLHS(old_lhs);
			 if (!rhs_is_constant){
				//note, I assume that we don't have two constants, because
				//if we did, that would be a pretty stupid predicate
				if (new_rhs !=null){
				    //then we need to add it.
				    translated_ip.setRHS(new_rhs);
				    _graph.addInterpretedPredicate(translated_ip);
				}
			 }
		  }
		  else{
			 //lhs was a variable
			 if (new_lhs != null){
				translated_ip.setLHS(new_lhs);
				if (rhs_is_constant){
				    translated_ip.setRHS(old_rhs);
				    _graph.addInterpretedPredicate(translated_ip);
				}
				else{
				    if (new_rhs != null){
					   translated_ip.setRHS(new_rhs);
					   _graph.addInterpretedPredicate(translated_ip);
							
				    }
				}
			 }//end if lhs was mapped
		  }//end of if lhs was a variable
	   }//end of looping over adding the interpreted predicates from the query
	   if (_graph.isSatisfiable()){
		  //then the values in there can possibly some answers, so let's check
		  //the predicates in the query
		  num_query_ips = _query.numInterpretedPredicates();
		  for (i = 0; i < num_query_ips; i++){
			 if (_graph.isImplied(_query.interpretedPredicateI(i))){
				_predicates_covered[i] = true;
			 }
		  }//end looping over the query ips
			

	   }//end if the graph was satisfiable
	   else{
		  _is_satisfiable = false;
	   }
    }
		
    public boolean addMapping(Mapping amap){
        //this function returns true if adding the mapping suceeded
        //false if it fails.  A mapping fails if what it was mapped to
        //was previously mapped from something else
        //returns true if it succeeds, or if the variable was already
        //mapped to the same thing
        int mapping_size = mapping.size();
        //boolean used = false;
	   //	   System.out.println("addMapping + " + amap.printString());
	   
        if (!_view.variableIsDistinguished(amap.mapping) 
		  && _query.variableIsDistinguished(amap.variable)){
            //we must make sure that we don't map head variables 
		  //to non head- variables
		  //now check to see if they're constants
		  if (amap.mapping.equals(amap.variable) && amap.mapping.isAnyConstant()){
			 //then it's okay
			 mapping.addElement(new Mapping(amap));
			 return true;
		  }
		  
            return false;
        }

        for (int i = 0; i < mapping_size ;i++){
            if ((((Mapping)mapping.elementAt(i)).variable.equals(amap.variable)) &&
                ((Mapping)mapping.elementAt(i)).mapping.equals(amap.mapping)){
			 //Prego!  It's in there
			 return true;
            }
            
            if (((Mapping)mapping.elementAt(i)).variable.equals(amap.variable)){ 
			 //&&((Mapping)mapping.elementAt(i)).variable.equals(amap.variable)){ 
			 //okay, it's really disturbing that the preceeding line was there...
			 //at this point, if it is not the case that both of them are 
			 //head variables, we need to return false, otherwise we need
			 //to add an equality and return true.  We don't need to check
			 //the rest of the variables, because they will have already been 
			 //checked and added into the equality.
			 if (!(_view.variableIsDistinguished(((Mapping)mapping.elementAt(i)).mapping) &&
				  _view.variableIsDistinguished(amap.mapping))){
				//in this case, we can't map the variables because they
				//both need to be distinguished
				//now we need to check on the constants
				if (amap.variable.isAnyConstant()){
				    //because we know that they're they same, then
				    //they both must be the *same* constant, it's
				    //good to go
				    mapping.addElement(new Mapping(amap));
				    return true;
				}
				    
				return false;
				    
			 }
			 else {
				//we need to add the equality and then return true; it's fine
				addEquality(((Mapping)mapping.elementAt(i)).mapping,amap.mapping);
				mapping.addElement(new Mapping(amap));
				return true;
			 }
		  }
                    
 
            //end if we need to check on the mapping...
            //note, i need to figure out what to do if the mapping has a problem
            //because it is mapping the same thing to different vars; clearly this
            //is something that needs to be checked in the equalities; i'll deal
            //with that later.  Possibly the right thing to do is to check and see if
            //that's the case, and then add it to the equalities if it is, and then
            //check the head thingy later.... I think that's the right thing to do.
	   }//end for

	   //now we know that other than constants we're set.  So we need to check
	   //and see if there's a constant problem
	   if (amap.variable.isAnyConstant()){
		  //then we need to see if there's a conflict.  If only the view entry
		  //is a constant, then we don't need to worry about it until combination
		  //time (other than the special case on if they're both the same constant)
		  if (!amap.mapping.isAnyConstant()){ 
			 if (!_view.variableIsDistinguished(amap.mapping)){
				//if the query variable is a constant, and the view
				//variable is a variable that's not distinguished
				//then it won't work, return false
				return false;
				
			 }
		  }
		  
		  else{//it's a constant, in which case it only works if they're equal
			 if (!amap.mapping.equals(amap.variable)){
				return false;
			 }
			 
		  }
		  
	   }//end of if the query element is a constant; otherwise should work as planned

				
		  
	   //at this point, we know that we need to add the mapping to the end, so add it.
	   //at this point we still need to check to see
	   //if we need to add an equality

	   IPValue temp = mappingToVariable(amap.mapping);
	   if (temp != null){
		  //if this is the case, then it was already mapped to something.
		  //so we must add an equality
            _view_equalities.addEquality(amap.variable, temp);    
	   }
	   mapping.addElement(new Mapping(amap));
	   //copy it over to avoid problems
	   return true;
    }

}
   
    


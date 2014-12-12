//import java.lang.*;
package minicon;
import java.util.Vector;

//note, need to find out exactly how we can assign to static variables
public abstract class MD{
    //the purpose of this class is to allow us to keep track of
    //what's being done in our algorithm
    protected static int uidsUsed=0;
    protected View _view;
    protected int uid;
    protected int _size;
    protected Vector mapping; //this is the list of mappings, oh for a
    //templated Vector class....
    protected Vector _h_equalities; //this *looks* like a mapping, but
    //it's really an equality... stupid, but true
    protected QueryEquality _view_equalities;
	protected Query _query;
    protected boolean subgoalsCovered[];
    public MD(Query a_query, View a_view){
        _view = a_view;
        _size = a_query.size();
        mapping = new Vector(5);
		_query = a_query;
        _h_equalities = new Vector(5);
        _view_equalities = new QueryEquality();
        subgoalsCovered = new boolean[_size];
        uid = uidsUsed;
        uidsUsed++;
        for (int i = 0; i < _size; i++){
            subgoalsCovered[i] = false;
        }
    }

	public boolean sameSubgoalsCovered(MD a_md){
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
 
	public void setView(View aview){
        _view = new View(aview);
    }

    
    public Statement getView(){
        return _view;
    }

	public void setQuery(Query a_query){
		_query = a_query;
	}
	
	public Query getQuery(){
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
        //System.out.println("mapping.size(): " + mapping.size());
        for (i = 0; i < mapping.size(); i++){
        	System.out.println("((Mapping)mapping.elementAt(i)): " + ((Mapping)mapping.elementAt(i)).printString());
        	//System.out.println("((Mapping)mapping.elementAt(i)).mapping: " + ((Mapping)mapping.elementAt(i)).mapping.printString());
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
        return output;
    }//end printString
    
    public abstract boolean addMapping(Mapping amap);//, View a_view);
}

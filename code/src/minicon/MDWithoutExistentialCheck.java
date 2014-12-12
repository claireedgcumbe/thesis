//import java.lang.*;
package minicon;
import java.util.Vector;

//note, need to find out exactly how we can assign to static variables
public class MDWithoutExistentialCheck extends MD{
    //the purpose of this class is to allow us to keep track of
    //what's being done in our algorithm
   public MDWithoutExistentialCheck(Query a_query, View a_view){
		super(a_query, a_view);
	
    }
   
	
   public boolean addMapping(Mapping amap){//, View a_view){
        //this function returns true if adding the mapping suceeded
        //false if it fails.  A mapping fails if what it was mapped to
        //was previously mapped from something else
        //returns true if it succeeds, or if the variable was already
        //mapped to the same thing
        int mapping_size = mapping.size();
        //boolean used = false;
        if (!_view.variableIsDistinguished(amap.mapping) && _query.variableIsDistinguished(amap.variable)){
            //we must make sure that we don't map head variables to non head- variables
            return false;
        }

        for (int i = 0; i < mapping_size ;i++){
            if ((((Mapping)mapping.elementAt(i)).variable.equals(amap.variable)) &&
                ((Mapping)mapping.elementAt(i)).mapping.equals(amap.mapping)){
                    //Prego!  It's in there
                    return true;
            }
            
            if (((Mapping)mapping.elementAt(i)).variable.equals(amap.variable) &&
                ((Mapping)mapping.elementAt(i)).variable.equals(amap.variable)){
                    //at this point, if it is not the case that both of them are 
                    //head variables, we need to return false, otherwise we need
                    //to add an equality and return true.  We don't need to check
                    //the rest of the variables, because they will have already been 
                    //checked and added into the equality.
                    if (!(_view.variableIsDistinguished(((Mapping)mapping.elementAt(i)).mapping) &&
                    _view.variableIsDistinguished(amap.mapping))){
                        //in this case, we can't map the variables because they
                        //both need to be distinguished
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

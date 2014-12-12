//import java.lang.*;
package minicon;
import java.util.Vector;

public class IPEquality{
    //this class is to solve the problem of what happens when two variables in the query 
    //are mapped to the same variable in the view.
    //this class is meant to be used both with the MD and the meta MD.
    //when used in the metaMD, it should only be added in at the very end (otherwise they will
    //need to be deleted, etc).
    //note, I put any constant values in the *first* slot
    private Vector Equalities;// this will be a vector in a vector...
    //each vector containing a vector of variables that are equal to each other
    //it is assumed that variables are strings
    private Vector _value_is_fixed;
    private boolean _sorted;
    
    public IPEquality(){
	   Equalities = new Vector(10);
	   _value_is_fixed = new Vector(10);
	   _sorted = false;
	  
    }
	

    /*
	 public Double getRepresentative(IPValue a_val){
	 if (a_val.isNumericalConstant()){
	 return new Double(a_val.getNumericalConstant());
	 }
	 else{
	 return getRepresentative(a_val.getVariable());
	 }
	    
	 }
    */
    public IPValue getRepresentative(IPValue a_var){
	   //returns a_var if a_var is not in the list of equalities, returns the
	   //representative otherwise.
	   int i; 
	   Vector equal;
	   for (i = 0; i < Equalities.size(); i++){
		  equal = (Vector) Equalities.elementAt(i);
		  //this is the list of current equalities.
		  if (equal.contains(a_var)){
			 //then we've found the equality it's in; return the first variable
			 //note, this could, conceivably, return null, but that's something
			 //that should never happen given my assumptions
			 return (IPValue)equal.elementAt(0);
		  }
	   }
	   //at this point, we've cycled through all of the possibilities, so return only the
	   //current var
	   return null;
    }
	
    private void sortReps()
    {
	   //Sorts the variables so that if there's a non numerical constant
	   //that's in slot 1
	   int size;
	   int which_row;
	   Vector an_equal;
	   int num_equals = Equalities.size();
	   boolean flag = false;
	   
	   int i;
	   String temp;
	   for (which_row = 0; which_row < num_equals; which_row++){
		  an_equal = (Vector) Equalities.elementAt(which_row);
		  size = an_equal.size();
		  
		  if (size >=3){
			 if (!((String)Equalities.elementAt(1))
				.startsWith(IPValue._string_constant_delimiter)){
				//then we want to sort
				for (i = 2, flag = false; i < size && !flag; i++){
				    temp = (String)an_equal.elementAt(i);
				    if(temp.startsWith(IPValue._string_constant_delimiter)){
					   an_equal.setElementAt(an_equal.elementAt(1),i);
					   an_equal.setElementAt(temp,1);
					   flag = true;
				    }
				}
			 }
		  }
	   }
	   _sorted = true;
	   
	   
    }//end sort reps
	   

    public String getVariableRepresentative(String a_var){
	   /**this function returns the string representatives, where it
	    * is defined to be the first string that occurs in slot 1.  */
	   int i; 
	   Vector equal;
	   if (!_sorted){
		  sortReps();
	   }
	   
	   for (i = 0; i < Equalities.size(); i++){
		  equal = (Vector) Equalities.elementAt(i);
		  //this is the list of current equalities.
		  if (equal.contains(a_var)){
			 //then we've found the equality it's in; return the first variable
			 //note, this could, conceivably, return null, but that's something
			 //that should never happen given my assumptions
			 return (String)equal.elementAt(1);
		  }
	   }
	   //at this point, we've cycled through all of the possibilities, so return only the
	   //current var
	   return null;
		
	
    }
	
    public String getVariableRepresentative(double a_val){
	   /**this function returns the string representatives, where it is defined
	    * to be the first string that occurs in slot 1.
	    */
	   int i; 
	   Vector equal;
	   Double first_val;
	   for (i = 0; i < Equalities.size(); i++){
		  equal = (Vector) Equalities.elementAt(i);
		  //this is the list of current equalities.
		  first_val = (Double)equal.elementAt(0);
		  if (first_val != null && first_val.doubleValue() == a_val && ((Boolean)_value_is_fixed.elementAt(i)).booleanValue() == true){
			 //then we've found the equality it's in; return the first variable
			 //note, this could, conceivably, return null, but that's something
			 //that should never happen given my assumptions
			 return (String)equal.elementAt(1);
		  }
	   }
	   //at this point, we've cycled through all of the possibilities, so return only the
	   //current var
	   return null;
    }
	
    private int getEqualityLocation(String a_var){
	   int i;
	   Vector equal;
	   for (i = 0; i < Equalities.size();i++){
		  equal= (Vector) Equalities.elementAt(i);
		  if(equal.contains(a_var)){
			 return i;
		  }
	   }
	   return -1;
    }
		
    private int getEqualityLocation(double a_val){
	   /**Returns the location of the value of a_val.  
	    * It will not return a value *unless* the value is fixed.
	    */
	   int i;
	   Vector equal;
	   Double first_elt;
	   for (i = 0; i < Equalities.size(); i++){
		  equal = (Vector)Equalities.elementAt(i);
		  first_elt = (Double)equal.elementAt(0);
		  if (first_elt != null && first_elt.doubleValue()== a_val &&
			 ((Boolean)_value_is_fixed.elementAt(i)).booleanValue() == true){
			 return i;
		  }
	   }
	   return -1;
    }
	
    private Vector getEqualities(String a_var){
	   //this returns the equalities containing a current variable; returns null otherwise
	   int i;
	   Vector equal;
	   for (i = 0; i < Equalities.size();i++){
		  equal = (Vector) Equalities.elementAt(i);
		  if (equal.contains(a_var)){
			 return equal;
		  }
	   }
	   //at this point, we know that there are no equalities containing this variable
	   //so return null
	   return null;
    }
	
    public Vector getEqualities(IPValue a_val){
	   if (a_val.isNumericalConstant()){
		  return getEqualities(a_val.getNumericalConstant());
	   }
	   else
		  {
			 return getEqualities(a_val.getVariable());
		  }
    }
	
    private Vector getEqualities(double a_val){
	   //this returns the equalities containing a current variable; returns null otherwise
	   int i;
	   Vector equal;
	   Double a_val_2;
	   for (i = 0; i < Equalities.size();i++){
		  equal = (Vector) Equalities.elementAt(i);
		  a_val_2 = (Double)equal.elementAt(0);
		  if (a_val_2 != null && a_val_2.doubleValue()==a_val){
			 return equal;
		  }
	   }
	   //at this point, we know that there are no equalities containing this variable
	   //so return null
	   return null;
    }
		
	
    private Vector getAllEqualities (Vector equality){
	   //NOTE: I'm pretty sure that this function is completely bogus
	   //this function gets all of the variables that are equivalent
	   //returns just the vector if there are no others
	   Vector retval = (Vector)equality.clone();
	   int i,j;
	   Vector temp;
	   String a_var;
	   for (i = 0; i < equality.size();i++){
		  a_var = (String)equality.elementAt(i);
		  temp = getEqualities(a_var);
		  if (temp != null){
			 //if there were more, then we need to add them in
			 for (j = 0; j < temp.size();j++){
				if (!retval.contains(temp.elementAt(j))){
				    //then we need to add it
				    retval.addElement(temp.elementAt(j));
				}
			 }
		  }
	   }
	   return retval;
    }
    public boolean mergeEqualities(IPEquality an_equal){
	   //this function merges the current equality with the new one. 
	   //note, this may need some optimizing, but that can be done if necessary
	   int i,j;
	   Vector temp;
	   boolean retval;
	   for (i = 0; i < an_equal.Equalities.size();i++){
		  temp = (Vector)an_equal.Equalities.elementAt(i);
		  for (j = 0; j < temp.size()-1; j++){
			 //note, we still have to check on the size one case
			 retval = addEquality((String)temp.elementAt(j),(String)temp.elementAt(j+1));
			 if (retval == false){
				return false;
			 }
				
		  }
	   }//end for
	   return true;
    }//end mergeEqualities
	
    public boolean addEquality(String var, double constant){
	   return addEquality(constant,var);
    }

    public boolean addEqualityPossibility(String var, double constant){
	   return addEqualityPossibility(constant,var);
    }
	
    public boolean addEqualityPossibility(double constant, String var){
	   /**Adds an equality constrant between the double listed and the variable.
	    * doesn't check to make sure that the constant value match
	    **/
	   //this returns the equalities containing a current variable; returns null otherwise
	   int var_loc,i;
	   Vector an_equal;
	   if (var == null){
		  System.out.println("var was null in IPEquality.addEqualityPossibility.\nSystem will now exit");
		  System.exit(1);
		  
	   }
	   
	   if (var.startsWith(IPValue._string_constant_delimiter)){
		  //then they were different kinds of constants, which can't happen;
		  //return false
		  return false;
	   }
	   
	   for(i = 0; i < Equalities.size();i++){
		  an_equal = (Vector) Equalities.elementAt(i);
		  if (an_equal.contains(var)){
			 //then we need to set the constant value at this location to be 
			 //constant.
			 if (an_equal.elementAt(0) != null){
				if (((Double)an_equal.elementAt(0)).doubleValue()!= constant){
				    if (((Boolean)_value_is_fixed.elementAt(i)).booleanValue() == true){
					   return false;
				    }
				    else{
					   an_equal.setElementAt(new Double(constant),0);
					   return true;
				    }
				}
				else {
				    return true;
				}
			 }
			 an_equal.setElementAt(new Double(constant),0);
			 _value_is_fixed.setElementAt(new Boolean(false),i);
			 return true;
		  }
	   }//end looping over equalities so far
	   //otherwise, we need to add it to both.
	   an_equal = new Vector();
	   an_equal.addElement(new Double(constant));
	   an_equal.addElement(var);
	   Equalities.addElement(an_equal);
	   _value_is_fixed.addElement(new Boolean(false));
	   return true;
    }
	
	
    private void addEqualitySimple(String var, int location){
	   Vector equal = (Vector)Equalities.elementAt(location);
	   equal.addElement(var);
    }
	
	
    public boolean addEquality(IPValue val_1, IPValue val_2){
	   if (val_1.isVariable() && val_2.isVariable()){
		  return addEquality(val_1.getVariable(), val_2.getVariable());
	   }
	   else if (val_1.isVariable()){
		  //then we know that val2 must be a constant
		  return addEquality(val_1.getVariable(),val_2.getNumericalConstant());
	   }
	   else if (val_2.isVariable()){
		  return addEquality(val_2.getVariable(),val_1.getNumericalConstant());
	   }
	   else{
		  //they were both constants
		  return (val_2.getNumericalConstant() == val_1.getNumericalConstant());
	   }
    }
	
	
    public boolean addEquality(String var_1, String var_2){
	   //this function takes the two values and makes sure that they are represented
	   //as equal by the class.  Since we don't know aprori which, if either, of them
	   //already are there, we must check them both.
	   //need to change the vector so that it always stores the head variables first.
	   int i,j;
	   int var_1_match = getEqualityLocation(var_1);
	   int var_2_match = getEqualityLocation(var_2);
	   Vector equal;
	   if(var_1_match == -1 && var_2_match == -1){
		  //then neither of them were in there, and we need to add them both.
		  equal = new Vector(10);
		  equal.addElement(null);
		  equal.addElement(var_1);
		  equal.addElement(var_2);
		  Equalities.addElement(equal);
		  _value_is_fixed.addElement(new Boolean(false));
		  //note, need to test and make sure this does the right thing...
		  return true;
	   }
	   else if (var_1_match == -1){
		  //then we know that var_2_match alone must not equal -1
		  addEqualitySimple(var_1,var_2_match);
		  return true;
	   }
	   else if (var_2_match == -1){
		  addEqualitySimple(var_2,var_1_match);
		  return true;
	   }
	   else if (var_1_match == var_2_match){
		  //we already have it, so just return true
		  return true;
	   }
	   else {
		  //they both had entries... so do it the hard way
		  return addEqualityComplex(var_1_match, var_2_match);
	   }	
    }//end addEquality
	

    public boolean addEquality(double constant, String var){
	   int constant_location = getEqualityLocation(constant);
	   int variable_location = getEqualityLocation(var);
	   Vector an_equal;
	   Double a_val;
	   if (var.startsWith(IPValue._string_constant_delimiter))
	   {
		  //then they were different types of constants which can't be allowed
		  //to happen, so return false
		  return false;
		  
	   }
		  
		  
	   if (constant_location == -1){
		  //if there was no constant with this value
		  if (variable_location != -1){
			 //then we need to set the double value
			 an_equal = (Vector)Equalities.elementAt(variable_location);
			 a_val = (Double)an_equal.elementAt(0);
			 if (a_val != null && a_val.doubleValue() != constant && ((Boolean)_value_is_fixed.elementAt(variable_location)).booleanValue()){
				return false;
			 }
			 else
				{
				    an_equal.setElementAt(new Double(constant),0);
				    _value_is_fixed.setElementAt(new Boolean(true),variable_location);
				    return true;
				}
		  }
		  else{
			 //otherwise, we didn't have anything matching the string, so create a 
			 //new location
			 an_equal = new Vector();
			 an_equal.addElement(new Double(constant));
			 an_equal.addElement(var);
			 _value_is_fixed.addElement(new Boolean(true));
			 Equalities.addElement(an_equal);
			 return true;
		  }
	   }//end of if we had a value for the location of the constant
	   else if (variable_location == -1){
		  //then we can just add it to the double location
		  an_equal = (Vector)Equalities.elementAt(constant_location);
		  an_equal.addElement(var);
		  return true;
	   }
	   else
		  {
			 //they both are there, and we need to check and make sure there are 
			 //no conflicts and then merge.
			 an_equal = (Vector)Equalities.elementAt(variable_location);
			 a_val = (Double)an_equal.elementAt(0);
			 if (variable_location == constant_location){
				//then they are already equated
				return true;
			 }
			 else if (a_val == null || !(a_val.doubleValue() != constant && 
								    ((Boolean)_value_is_fixed.elementAt(variable_location)).booleanValue() == true)){
				//then we can combine them.
				Vector sec_vec = (Vector)Equalities.elementAt(constant_location);
				addAll(an_equal,sec_vec);
				//now that we're allowing them to be combined even if the constant at variable_location
				//isn't the same, but isn't fixed, we need to make sure that they're the value of
				//constant at the end
				an_equal.setElementAt(new Double(constant),0);
				Equalities.setElementAt(Equalities.lastElement(),constant_location);
				//move the last item into the second place's slot
				Equalities.setSize(Equalities.size()-1);
				//now, mimic for value_is_fixed
				_value_is_fixed.setElementAt(new Boolean(true),variable_location);
				_value_is_fixed.setElementAt(_value_is_fixed.lastElement(),constant_location);
				_value_is_fixed.setSize(_value_is_fixed.size() -1);
				return true;
				
			 }
			 else{
				//then they can't be joined.
				return false;
			 }
		  }	
			  
    }

    private boolean valueIsFixedI(int i){
	   return ((Boolean)_value_is_fixed.elementAt(i)).booleanValue();
    }
	
    private boolean addEqualityComplex(int loc_1, int loc_2){
	   Vector entry_1 = (Vector)Equalities.elementAt(loc_1);
	   Vector entry_2 = (Vector)Equalities.elementAt(loc_2);
	   Double constant_1 = (Double)entry_1.elementAt(0);
	   Double constant_2 = (Double)entry_2.elementAt(0);
	   double value_1;
	   double value_2;
	   if (constant_1 !=null && constant_2 != null){
		  //then both are non null, and we need to make sure that they don't
		  //conflict
		  value_1 = constant_1.doubleValue();
		  value_2 = constant_2.doubleValue();
		  if (value_1 != value_2){
			 if (valueIsFixedI(loc_1) && valueIsFixedI(loc_2)){
				//then we can't add both of these together and need to return 
				//false
				return false;
			 }
			 else if (valueIsFixedI(loc_2)){
				//we need to figure out which one to use and set _value_is_fixed accordingly
				//necessary.  Since we are going to be just copying everything over to
				//entry1, we need only change things if the one in loc_2 is the one we
				//need to use
				entry_1.setElementAt(entry_2.elementAt(0),0);
				_value_is_fixed.setElementAt(new Boolean(true),loc_1);
			 }
		  }
		  //else, we can add the rest of the values
		  addAll(entry_1, entry_2);
	   }
	   //otherwise, we know that at most one is the same
	   else if (constant_2 != null){
		  addAll(entry_2, entry_1);
		  entry_1 = entry_2;
	   }
	   else {
		  addAll(entry_1, entry_2);
	   }
	   //at this point we have all of the answers in entry_1 and we just need
	   //to delete entry_2
	   Equalities.setElementAt(Equalities.lastElement(),loc_2);
	   //move the last item into the second place's slot
	   Equalities.setSize(Equalities.size()-1);
	   //decrement the count
	   //now do the same for the fixed vars.
	   _value_is_fixed.setElementAt(_value_is_fixed.lastElement(),loc_2);
	   _value_is_fixed.setSize(_value_is_fixed.size()-1);
	   return true;
    }//end addEqualityComplex
		
	
	
    private void addAll(Vector one, Vector two){
	   /**Appends all of the values from two to the values in one
	    * except the first one, which is the constant
	    */
	   int i;
	   for (i = 1; i < two.size();i++){
		  one.addElement(two.elementAt(i));
	   }//end for
    }//end addAll
	
    public static void main(String args[]){
	   IPEquality e = new IPEquality();
	   IPValue one;
	   IPValue two;
	   IPValue three;
	   IPValue four;
	   IPValue five;
	   one = new IPValue(2.0);
	   two = new IPValue("f");
	   three = new IPValue("h");
	   four = new IPValue("g");
	   five = new IPValue(2.0);
	   e.addEquality(one, two);
	   e.addEquality(three, four);
	   e.addEquality(four,five);
    }//end main
}//end class IPEquality
	

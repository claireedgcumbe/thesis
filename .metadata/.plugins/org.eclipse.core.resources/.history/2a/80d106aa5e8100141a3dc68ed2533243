//import java.lang.*;
package minicon;
import java.util.Vector;

public class QueryEquality{
    /*this class is to solve the problem of what happens when two
	*variables in the query are mapped to the same variable in the
	*view.  this class is meant to be used both with the MD and the
	*meta MD.  when used in the metaMD, it should only be added in at
	*the very end (otherwise they will need to be deleted, etc).  */
    protected Vector _head_variables;
    private Vector Equalities;// this will be a vector in a vector...
    //each vector containing a vector of variables that are equal to each other
    //it is assumed that variables are strings
 
    public QueryEquality(){
	   Equalities = new Vector(10);
	   _head_variables = null;
    }
 
    public QueryEquality(Vector head_variables){
	   Equalities = new Vector(10);
	   _head_variables = head_variables;
    }
 
    public boolean sort(){
	   /*this function makes sure that if there is a constant, it's
	    *at the front.  Barring that, it makes sure that if there's
	    *head variable, it's in the front. since it can only be done
	    *if the head variables are known, it returns false else and
	    *at the moment, I'm going to output an error message*/
	   if (_head_variables == null){
		  System.out.println("You tried to sort a query equality that had no head variables");
		  return false;
	   }
	   int i,j;
	   boolean flag_head = false;
	   boolean flag_constant = false;
	   Vector a_row;
	   IPValue temp;
	   for (i = 0; i < Equalities.size() ; i++){
		  a_row = (Vector) Equalities.elementAt(i);
		  if (!((IPValue)a_row.elementAt(0)).isAnyConstant()){
			 //then we need to sort
			 flag_head = false;
			 
			 if (((IPValue)a_row.elementAt(0)).containedInVector(_head_variables)){
				//then we set the head flag to true
				flag_head = true;
			 }
		  
			 for (j = 1, flag_constant = false; j < a_row.size() && !flag_constant; j++){
				if (((IPValue)a_row.elementAt(j)).isAnyConstant()){
				    //then we want to swap with the first element and exit the loop
				    temp = (IPValue) a_row.elementAt(j);
				    a_row.setElementAt(a_row.elementAt(0), j);
				    a_row.setElementAt(temp,0);
				    flag_constant = true;
				}
				
				else if (((IPValue)a_row.elementAt(j)).containedInVector(_head_variables) && flag_head == false){
				    //then we need to swap with the first element
				    temp = (IPValue) a_row.elementAt(j);
				    a_row.setElementAt(a_row.elementAt(0), j);
				    a_row.setElementAt(temp,0);
				    flag_head = true;
				}//end if we need to swap
			 }//end inner for
		  }//end if we needed to check for head variables
        
	   }//end outer for
	   return true;
    }

    public IPValue getRepresentative(IPValue a_var){
	   //returns a_var if a_var is not in the list of equalities, returns the
	   //representative otherwise.
	   int i,j;
	   Vector equal;
	   boolean flag;
	   
	   
	   for (i = 0; i < Equalities.size(); i++){
		  equal = (Vector) Equalities.elementAt(i);
		  //this is the list of current equalities.
		  if (a_var.containedInVector(equal)){
			 //then we've found the equality it's in; return the first variable
			 return (IPValue) equal.elementAt(0);
		  }
	   }
	   //at this point, we've cycled through all of the possibilities, so return only the
	   //current var
	   return a_var;
    }
 
    private Vector getEqualities(IPValue a_var){
	   //this returns the equalities containing a current variable; returns null otherwise
	   int i;
	   Vector equal;
	   for (i = 0; i < Equalities.size();i++){
		  equal = (Vector) Equalities.elementAt(i);
		  if (a_var.containedInVector(equal)){
			 
			 return equal;
		  }
	   }
	   //at this point, we know that there are no equalities containing this variable
	   //so return null
	   return null;
    }
 
    private Vector getAllEqualities (Vector equality){
	   /*NOTE: I'm pretty sure that this function is completely bogus
	    *this function gets all of the variables that are equivalent
	    *returns just the vector if there are no others*/
	   Vector retval = (Vector)equality.clone();
	   int i,j;
	   Vector temp;
	   IPValue a_var;
	   for (i = 0; i < equality.size();i++){
		  a_var = (IPValue)equality.elementAt(i);
		  temp = getEqualities(a_var);
		  if (temp != null){
			 //if there were more, then we need to add them in
			 for (j = 0; j < temp.size();j++){
				if (!((IPValue)temp.elementAt(j)).containedInVector(retval)){
				    //then we need to add it
				    retval.addElement(temp.elementAt(j));
				}
			 }
		  }
	   }
	   return retval;
    }
    public void mergeEqualities(QueryEquality an_equal){
	   //this function merges the current equality with the new one. 
	   //note, this may need some optimizing, but that can be done if necessary
	   int i,j;
	   Vector temp;

	   for (i = 0; i < an_equal.Equalities.size();i++){
		  temp = (Vector)an_equal.Equalities.elementAt(i);
		  for (j = 0; j < temp.size()-1; j++){
			 //note, we still have to check on the size one case
			 addEquality((IPValue)temp.elementAt(j),(IPValue)temp.elementAt(j+1));
		  }
		  //now check the size one case
		  /*if (temp.size() == 1){
		    //then we need to do this the hard way.
		    a_var = getRepresentative((String)temp.elementAt(0));

		    if (a_var.equals((String)temp.elementAt(0))){
		    //then we found nothing new, add this to the end of the equalities
		    temp2 = new Vector(5);
		    temp2.addElement(temp.elementAt(0));
		    Equalities.addElement(temp2);
		    }
		    else{
		    //it was equal to something already in there
		    addEquality((String)temp.elementAt(0),a_var);
		    }   
		    }//end if
		    //actually, we don't need to check the size one cases; we just 
		    //don't save those
		  */
	   }//end for
    }//end mergeEqualities
 
    public void addEquality(IPValue var_1, IPValue var_2){
	   /*this function takes the two values and makes sure that they
	    *are represented as equal by the class.  Since we don't know
	    *aprori which, if either, of them already are there, we must
	    *check them both.  need to change the vector so that it
	    *always stores the head variables first.*/
	   int i,j;
	   int var_1_match = -1;
	   int var_2_match = -1;
	   Vector equal;
	   for (i = 0; i < Equalities.size();i++){
		  equal = (Vector) Equalities.elementAt(i);
		  if (var_1.containedInVector(equal) && !var_2.containedInVector(equal)){
			 //this part is if we need to add the equality for var_2
			 if (var_1_match != -1){
				//then we need to combine the two rows.
				for (j = 0; j < equal.size();j++){
				    ((Vector)Equalities.elementAt(var_1_match)).addElement(equal.elementAt(j));
				    //note, we don't have to check to make sure that they aren't
				    //equal to each other, or we would have combined them already
				}
				//end of adding everything from the first equality thing to the end
				//now shove the thing at the end into the current last slot
				if (i != Equalities.size() - 1){
                    
				    Equalities.setElementAt(Equalities.lastElement(),i);
				    Equalities.removeElementAt(Equalities.size() - 1);
				}
				return;//we've already covered all possible new equalities, so 
				//leave
			 }//end if we already had matched the other var
			 equal.addElement(var_2);
			 var_2_match = i; 
		  }//end of if we have found the match for the second variable   
		  else if (!var_1.containedInVector(equal) && var_2.containedInVector(equal)){
			 //this part is if we need to add the equality for var_2
			 if (var_2_match != -1){
				//then we need to combine the two rows.
				for (j = 0; j < equal.size();j++){
				    ((Vector)Equalities.elementAt(var_2_match)).addElement(equal.elementAt(j));
				    //note, we don't have to check to make sure that they aren't
				    //equal to each other, or we would have combined them already
				}
				//end of adding everything from the first equality thing to the end
				//now shove the thing at the end into the current last slot
				if (i != Equalities.size() - 1){
                    
				    Equalities.setElementAt(Equalities.lastElement(),i);
				    Equalities.removeElementAt(Equalities.size() - 1);
				}
				return;//we've already covered all possible new equalities, so 
				//leave
			 }//end if we already had matched the other var
			 equal.addElement(var_1);
			 var_1_match = i; 
		  }//end of if we have found the match for the second variable   
		  else if (var_2.containedInVector(equal) && var_1.containedInVector(equal)){
			 //then it's already taken care of, just return out of there
			 return;
		  }
	   }
	   //}//end for
	   //if we get here, we know that we will have seen none of them, so we must add them both
	   //to a new one
	   equal = new Vector(10);
	   equal.addElement(var_1);
	   equal.addElement(var_2);
	   Equalities.addElement(equal);
	   
    }//end addEquality

    public static void main(String args[])
	   {


		  Vector vec = new Vector(10);
		  vec.addElement(new IPValue("b"));

		  QueryEquality an_equal = new QueryEquality(vec);
		  an_equal.addEquality(new IPValue("b"), new IPValue("'5'"));
		  an_equal.sort();
		  
		  an_equal.getRepresentative(new IPValue("b")).print();
		  System.out.println("");
		  
		  
	   }
    

}

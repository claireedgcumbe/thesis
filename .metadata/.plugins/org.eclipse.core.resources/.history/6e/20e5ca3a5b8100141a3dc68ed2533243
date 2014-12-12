package minicon;
import java.io.Serializable;
import java.util.Vector;

public class IPValue implements Serializable
{
    //this class is meant to hold the values of interpreted predicates.
    //at the moment, they can either be doubles or strings
    private double _numerical_constant;
    private String _variable;
    private boolean _is_numerical_constant;
    public static char _numerical_delimiter = '\'';
    public static String _string_constant_delimiter = "\"";
    
    
    public IPValue(){
	   _numerical_constant = 0;
	   _is_numerical_constant = true;
	   _variable = null;
    }

    public IPValue(IPValue an_ipvalue){
	   _numerical_constant = an_ipvalue._numerical_constant;
	   _is_numerical_constant = an_ipvalue._is_numerical_constant;
	   if (an_ipvalue._variable == null){
		  _variable = null;
	   }
	   else{
		  
		  _variable = new String(an_ipvalue._variable);
	   }
	   
    }
	
	
    public IPValue(String variable){
	   /* note that this checks to make sure that it's actually a 
	    * string rather than being a numerical constant in surprise */
	   int index,index2;
	   index = variable.indexOf(_numerical_delimiter);
	   if (index >= 0){
		  //then it was a numerical constant
		  index2 = variable.lastIndexOf(_numerical_delimiter);
		  if (index == index2){
			 //then someone screwed up; print an error message
			 System.out.println("Malformed variable or constant " + variable + " in IPValue(String variable); system will now exit");
			 System.exit(1);
		  }
		  try{
			 
			 setNumericalConstant((new Double(variable.substring(index + 1, index2).trim())).doubleValue());
		  }
		  catch(NumberFormatException e){
			 System.out.println("Attempt to read variable " + variable + " as a double when it is not; system will now exit");
			 System.exit(1);
			 
		  }
		  
	   }
	   else{
		  
		  _is_numerical_constant = false;
		  _variable = new String(variable);
		  
	   }
	   
    }

    public IPValue(double constant){
	   _is_numerical_constant = true;
	   _numerical_constant = constant;
    }
	
    public void setNumericalConstant(double constant){
	   _numerical_constant = constant;
	   _is_numerical_constant = true;
    }
	
    public void setVariable(String variable){
	   _variable = variable;
	   _is_numerical_constant = false;
    }
	
	
    public void setValue(double constant){
	   _numerical_constant = constant;
	   _is_numerical_constant = true;
    }
	
    public void setValue(String variable){
	   _variable = variable;
	   _is_numerical_constant = false;

    }
	
    public boolean isNumericalConstant(){
	   return _is_numerical_constant;
    }
	
    public boolean isVariable(){
	   return !_is_numerical_constant;
    }

    public boolean isNonNumericalConstant()
    {
	   return !_is_numerical_constant && 
		  _variable.startsWith(_string_constant_delimiter);
    }
    

    public boolean isAnyConstant()
    {
	   /**Returns whether or not value is either a constant - 
	    * either numerical or not 
	    */
	   return _is_numerical_constant ||  
		  _variable.startsWith(_string_constant_delimiter);
	   
    }// end public boolean isAnyConstant()
    

    public double getNumericalConstant(){
	   return _numerical_constant;
    }
	
    public String getVariable(){
	   return _variable;
    }
	
    public IPValue copy(){
	   IPValue retval = new IPValue();
	   retval._numerical_constant = _numerical_constant;
	   if (_variable == null){
		  retval._variable = null;
	   }
	   else{
		  
		  retval._variable = new String(_variable);
	   }
	   
	   retval._is_numerical_constant = _is_numerical_constant;
	   return retval;
    }

    public void print(){
	   System.out.print(printString());
    }
	

    /*    public boolean equals(Object an_object)
    {
	   if 
    }//main function to make sure that it gets called when necessary.
    */

    public boolean equals(String a_string)
    {
	   
	   if (_is_numerical_constant){
		  return false;
		  
	   }
	   
	   return (_variable.equals(a_string));
    }
    

    public boolean equals(IPValue an_ipvalue)
    {
	   /**This function is much like the equals function for strings; it 
	    * looks to see that the value represents the same rather than that
	    * it has the same memory references 
	    */
	   if (an_ipvalue == null){
		  if (this == null){
			 return true;
		  }
		  
		  else{
			 
			 return false;
		  }
		  
	   }
	   
	   if (an_ipvalue._is_numerical_constant && _is_numerical_constant){
		  //then we need to see if the constants are equal
		  if (_numerical_constant == an_ipvalue._numerical_constant){
			 return true;
		  }
		  
	   }
	   if(!an_ipvalue._is_numerical_constant && !_is_numerical_constant){
		  return _variable.equals(an_ipvalue._variable);
	   }
	   
	   //otherwise one was a numerical constant and the other wasn't so 
	   //return false
	   return false;
	   
    }//end public boolean equals()
    

    public String printString(){
	   if (_is_numerical_constant){
		  return _numerical_delimiter + (new Double(_numerical_constant)).toString() + _numerical_delimiter;
	   }
	   else{
		  return   _variable;
	   }
	   
    }

    public boolean containedInVector(Vector a_vector)
    {
	   /*checks to see if the vector contains this IPValue since the
          stupid Vector.contains() function doesn't work right with
          IPValues although it does with Strings.  Returns true if
          a_vector contains the value, false else */
	   int i;
	   int size = a_vector.size();
	   for (i = 0; i < size; i++){
		  if (this.equals((IPValue)a_vector.elementAt(i))){
			 return true;
		  }
		  
	   }
	   return false;
	   
    }//end public boolean vectorContains(Vector a_vector)
    

    public static void main(String args[])
    {
	   IPValue bob = new IPValue("\"bob\"");
	   bob.print();
	   bob.equals("foo");
	   
	   System.out.println("\nIs numerical constant " + bob.isNumericalConstant());
	   System.out.println("Is non numerical constant " + bob.isNonNumericalConstant());
	   System.out.println("Is any constant " + bob.isAnyConstant());
	   
	   
	   
    }
    

}



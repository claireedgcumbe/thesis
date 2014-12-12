//import java.lang.*;
package minicon;
import java.io.Serializable;
import java.util.Vector;
public class Predicate implements Cloneable, Serializable{
	
	//This class implements a Predicate.  A predicate consists of a 
	//Function name (or relation name), and a listing of variables.
	//the variables and function name are all strings
	//The following functions are available:
	//Constructors: Predicate()
	//				Predicate(int size) which does not constrain a predicate 
	//                              to be that size, but initializes it to that size
	//				Predicate (Predicate p) a copy constructor.  Makes a deep copy
	//int size()-- returns the number of variables in the predicate
	//String variableI(int i) -- returns the ith variable
	//String getFunctionHead() -- returns the function head
	//boolean read(String read) -- takes the input string and turns it into a predicate
	//                             it returns false if the conversion fails
	//void print() -- prints a representation to system.out
	//StringBuffer printString() -- returns the string representation of the predicate
	//boolean equals(Predicate a) -- returns true if the two predicates are equal in function
	//								 and all of the variables.
	//boolean addVariable(String avar) -- adds a variable to the predicate.  at the moment
	//									  this always returns true
	//boolean replaceVariableI(String new_var, int i) -- replaces the variable at i
	//													 with new_var
	//int variableIsAt(String a_var) -- returns the first location that a_var occurs
	//									and returns -1 if it does not occur
	//boolean containsVariable(String avar) -- returns true if the variable occurs in the predicate	
	//
	// There is also an iterator over the variables in the predicate:
	//
	//void first() -- sets the iterator to the first value
	//boolean isDone() -- returns if the iterator is done
	//void next() -- advances the current state of the iterator
	//String current() -- returns the current variable
	//int getCurrentLocation() -- returns the current location of the iterator
	//void replaceCurrent(String new_var) -- replaces the current string with another

    public Vector variables; //holds the variables. 
    int currentvar;			 //the number of the current variable for the iterator
    protected int numvariables;//the number of variables.  should be the same as variables.size()
    String function;         //the relation name
	
    public Predicate (){
        variables = new Vector(5);
        currentvar = 0;
        numvariables = 0;
	}//end constructor
    
    public Predicate(int a_size){
        //note, because we are going to be making an awful lot of new predicates
        //where we already know how big they'll get, we can optimize by using giving
        //size
        variables = new Vector(a_size);
        currentvar = 0;
        numvariables = 0;
    }
         
    public Predicate(String p_string)
    {
    	variables = new Vector(5);
    	currentvar = 0;
    	numvariables = 0;
    	read(p_string);
    }
    
    public Predicate(Predicate p){
        function = new String(p.function);
        variables  = new Vector(p.numvariables);
        currentvar = 0;
        numvariables = p.numvariables;
        for (int i = 0; i < numvariables; i++){
            variables.addElement(new IPValue((IPValue)p.variables.elementAt(i)));
        }
    }//end copy constructor
        

    public int size(){
        return numvariables;
    }

    
    public IPValue variableI(int i){
        return ((IPValue)variables.elementAt(i));
    }
    
//    public String variableI(int i){
//        return ((String)variables.elementAt(i));
//    }
    
    public String getFunctionHead(){
        return function;
    }

	public void setFunctionHead(String aval){
        function = aval;
    }

    public boolean read(String input){
        //read in a predicate; assume that it is of the
        //form F(a,b,e)... meaning 1. no () or , in variable names
        //returns true if success, false if failure
    	//System.out.println("-------input--------"+input);
    	// if it has the parentheses (), then leave the whole word together, do not separate the , in ()
        String head;
        String substr,substr2="";
        int index, index1;
        substr = input.trim();
        index = substr.indexOf("(");
        head = new String(input.substring(0,index));
        function = head.trim();
        substr = input.substring(index+1);
        index = substr.indexOf(",");
        index1 = substr.indexOf("(");
        //System.out.println("-------index--------"+index+"   "+index1);
        //System.out.println("-------substr--------"+substr); //wrap_mode(repeatS, repeatT)
        while (index > -1){
        	if(index<index1||index1==-1) {
        		substr2  = substr.substring(0,index);
                addVariable(substr2.trim());
                substr = substr.substring(index+1);
                index = substr.indexOf(",");
                index1 = substr.indexOf("(");
        	} else {
        		index = substr.indexOf("),");
        		if(index>-1) {
        			substr2  = substr.substring(0,index+1);
                    addVariable(substr2.trim());
                    substr = substr.substring(index+2);
                    index = substr.indexOf(",");
                    index1 = substr.indexOf("(");
        		} else {
        			index = substr.indexOf(")");
        			substr2  = substr.substring(0,index+1);
                    addVariable(substr2.trim());
                    substr = substr.substring(index+1);
                    index = -1;
                    index1 = substr.indexOf("(");
        		}
        	}
        	//System.out.println("---!!!----substr2--------"+substr2+"  ---"+substr);
        }//end while
        //at this point, we have gotten all but the last one...
        //now get it...
        
        index = substr.indexOf(")");
        if (index < 0){
            return false;
        }//end if
        if(index==0)
        	return true;
        if(substr.indexOf("(")>-1)
        	substr = substr.substring(0,index+1);
        else 
        	substr = substr.substring(0,index);
        //System.out.println("----------last substr---------------"+substr);
        addVariable(new String(substr.trim()));
        //System.out.println("----numvariables------"+numvariables);
        return true;
    }//end read

    public void print(){
        System.out.println(printString());
    }
    
    public StringBuffer printString(){ //should be named as toPrintString();
        int counter;
	   if (function == null){
		  return new StringBuffer("");
	   }
	   
        StringBuffer retval = new StringBuffer(function);
        retval.append("(");
        if(numvariables >0){
            for (counter = 0; counter < numvariables-1; counter++){
                //note, while I'd really like to use the iterator here, I
                //don't want to have print screw up whatever the user is doing
                retval.append(variableI(counter).printString());
                retval.append(",");
            }//end for
            retval.append(variableI(numvariables-1).printString());
        }//end if
        retval.append(")");
        return retval;
    }//end print

    public boolean equals(Predicate a){
        int counter;
        if (numvariables== a.numvariables && function.equals(a.function)){
            for (counter = 0; counter < numvariables; counter++){
                if (!variableI(counter).equals(a.variableI(counter))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
	
    public boolean addVariable(IPValue avar){
        variables.addElement(avar);
        numvariables++;
        return true;
    }//end addVariable


    public boolean addVariable(String avar){
        variables.addElement(new IPValue(avar));
        numvariables++;
        return true;
    }//end addVariable

    public void first(){
           currentvar = 0;
    }

    public boolean isDone(){
        if (currentvar >= numvariables){
            return true;
        } else {
            return false;
        }
    }

    public boolean onNextToLast(){
        //returns if we are on the next to last variable; needed for the 
        //output
        if (currentvar == numvariables -1){
            return true;
        }
        else {
            return false;
        }
    }
	
    public int getCurrentLocation(){
        return currentvar;
    }
       
    public void next(){
        currentvar++;
    }

    public void replaceCurrent(IPValue new_var){
        variables.setElementAt(new_var, currentvar);
    }

    public boolean replaceVariableI(IPValue new_var, int i){
        variables.setElementAt(new_var, i);
        return true;
    }
    
    public boolean replaceVariableI(String new_var, int i){
        variables.setElementAt(new IPValue(new_var), i);
        return true;
    }

    public IPValue current(){
        return (IPValue) variables.elementAt(currentvar);
    }

    public int variableIsAt(IPValue a_var){
        //this function returns what location the variable is at... 
        //returns  -1 if it's not there
        int counter;
        for (counter = 0; counter < numvariables; counter++){
            if (a_var.equals(variableI(counter))){
                return counter;
            }
        }
        return -1;
    }
    
    public int variableIsAt(String a_var){
        //this function returns what location the variable is at... 
        //returns  -1 if it's not there
        int counter;
        for (counter = 0; counter < numvariables; counter++){
            if (a_var.equals(variableI(counter).printString())){
                return counter;
            }
        }
        return -1;
    }
    
    public boolean containsVariable(IPValue avar){
        int counter;
        for (counter = 0; counter < numvariables; counter++){
            if (avar.equals(variableI(counter))){
                return true;
            }
        }
        //if we get here, then we have checked all of the variables, so
        return false;
    }//end containsVariable

    public boolean containsVariable(String astringvar){
	   IPValue avar = new IPValue(astringvar);
	   
        int counter;
        for (counter = 0; counter < numvariables; counter++){
            if (avar.equals(variableI(counter))){
                return true;
            }
        }
        //if we get here, then we have checked all of the variables, so
        return false;
    }//end containsVariable
    
    
    public static void main (String args[])
    {
       //System.out.println("foobar");
        //String bob="(ae,ou, aody )";
        //List alist = new List();
        Predicate bob2;
        bob2 = new Predicate();
        bob2.read("Texture(name, tex_texture_type, tex_image_url, texture_coordinates, world_to_texture, wrap_mode(repeatS, repeatT))");
        //int i;
 /*       List list2 = new List(alist);
        list2.appendAVar(new ListNode(new Integer(i+1)));
        alist.print();
        System.out.println("foo");
        alist.appendList(list2);
        list2.print();
        alist.print();
        List bigList;
        bigList = new List();
        bigList.appendAVar(new ListNode(alist));
        bigList.appendAVar(new ListNode(list2));
 */       
//        bob2.read(bob);
//
    }

}

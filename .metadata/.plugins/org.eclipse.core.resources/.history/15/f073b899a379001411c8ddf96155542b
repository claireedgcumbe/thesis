//import java.lang.*;
package minicon;
import java.util.Vector;
//import java.util.Random;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.Date;
import java.io.*;


public abstract class Algorithm{
	protected long _mapping_creation_time;
	protected long _mapping_combination_time;
	protected RapTimer _timer;
	protected Vector Views;
	protected int NumViews;
	protected Query myQuery;
	public long timeStarted;
	protected int _view_rewrite_number;
	protected int _num_rewritings;
	protected int _num_mappings;
	protected Vector _rewritings;
	protected int _size;
	public Algorithm(){
		_timer = new RapTimer();
		_mapping_creation_time = 0;
		_mapping_combination_time = 0;
		myQuery = null;
		Views = new Vector(10);
		NumViews = 0;
		_view_rewrite_number = 0;
		_num_mappings = 0;
		_num_rewritings = 0;
		_size = 0;
	}

    public int getNumMappings(){
    	_num_mappings = Views.size();
        return _num_mappings;
    }
    
	public long getMappingCreationTime(){
		return _mapping_creation_time;
	}
	
	public long getTotalTime(){
		return _mapping_creation_time + _mapping_combination_time;
	}
	
	public long getMappingCombinationTime(){
		return _mapping_combination_time;		
	}
	
    public int getNumRewritings(){
        return _num_rewritings;
    }

    public Vector expandAllAnswers(String all_rewritings){
        //this is UGLY!!!! *** and really shouldn't be in here; need to figure out a better way
        return null;
    }

	public Vector getViews(){
		return Views;
	}
	
    public boolean allAnswersContained(String answers){
		
        Vector expanded_answers = expandAllAnswers(answers);
        int i;
        for (i = 0; i < expanded_answers.size();i++){
            if (!answerIsContained((Statement)expanded_answers.elementAt(i))){
				FileWriter output;
				try {
					output = new FileWriter("notcontained.txt",true);
					output.write((String)_rewritings.elementAt(i));
					output.write("\n");
					output.write(((Statement)expanded_answers.elementAt(i)).printString().toString());
					output.write("\nnot contained in \n");
					output.write(printString().toString());
					output.write("\n");
					output.flush();
					output.close();
				} catch (IOException e){
					System.out.println("couldn't open notcontained.txt in allAnswersContained in Algorithm.java");
				}
                //if one wasn't they all weren't :(
                //so print out the answer (for debugging purposes)
                //and return false
                ((Statement)expanded_answers.elementAt(i)).print();
                System.out.println(" was not contained in ");
                myQuery.print();
                return false;
            }
        }
        //if we made it this far, they all checked out.
        return true;
    }

    public boolean answerIsContained(Statement answer){
        return myQuery.contains(answer);
        
    }
    
    public View findView(String predicate_name){
        int i;
        for (i = 0; i < NumViews; i++){
            if (((View)Views.elementAt(i)).getHead().getFunctionHead().equals(predicate_name)){
                return (View)Views.elementAt(i);
            }//end if
        }//end for
        return null;
    }//end findView
     
    //public String findHeadVariableMatch(String 
    
   
    public View expandView(Predicate a_pred,Statement actual_view){
        //this function expands the current view with the variables in a_pred.
        //it takes all of the unassigned variables and sets them to something
        //new... maybe exp_[uniquenum][viewvar]
        View retval; 
        //first, find out which statement it is....
        //Statement actual_view = findView(a_pred.function);
        Predicate a_view_predicate;
        retval = new View(actual_view);
        //at this point we have the view, now make sure that there was something
        if (actual_view == null){
            System.out.println("invalid view check in Algorithm.expandView");
            System.exit(0);
        }
        //now translate
        for (retval.first(); !retval.isDone(); retval.next()){
            a_view_predicate = retval.current();
            for (a_view_predicate.first(); ! a_view_predicate.isDone(); a_view_predicate.next()){
                a_view_predicate.replaceCurrent(translateVariables(a_view_predicate.current(), a_pred, retval.getHead()));
            }
        }
        //retval.setHead(Predicate)
        return retval;
        
    }
    
    protected IPValue translateVariables(IPValue old, Predicate new_head, Predicate old_head){
        
        int var_loc;
        var_loc = old_head.variableIsAt(old);
        if (var_loc == -1){
            //then it wasn't in there, so return what we're prepending pluss the variable
            if (old.isNonNumericalConstant()){
			 return new IPValue(old);
		  }
		  else{
			 
			 return new IPValue("_re_" + _view_rewrite_number + old.getVariable());
		  }
	   }
	   
        return new_head.variableI(var_loc);
    }
    

    public String printRewritings(Vector rewritings){
        StringBuffer retval = new StringBuffer("");
        int i;
        MetaMD complete_mapping;
        if (rewritings == null || rewritings.size()==0){
            return ("no results\n");
        }
        for (i = 0; i < rewritings.size(); i++){
            complete_mapping = (MetaMD) rewritings.elementAt(i); 
            complete_mapping.setQueryHead(myQuery.getHead());
            complete_mapping.mergeQueryEqualities();
            retval.append(complete_mapping.printString());
            retval.append("\n");
        }
        
        return retval.toString();
    }//end printRewritings

	public void setQuery(Query aquery){
		myQuery = aquery;
		_size = myQuery.size();
	}

	public Query getQuery(){
		return myQuery;
	}
    
    public abstract String run();

    public abstract void clear();
    
    public abstract String type();

	public void addView(View aview){
	    aview.getHead().setFunctionHead(new String("V"+NumViews));
		Views.addElement(aview);
		NumViews++;
	}

    public void setViews(Vector someviews){
        Views = someviews;
    }

	public int numViews(){
		return NumViews;
	}

	public View viewI (int i){
		return (View) Views.elementAt(i);
	}

	public boolean generateViews(int numViews,int numSubgoals){
		if (numViews < 1){
			return false;
		}
		View a_view;
		NormalRandomStatementGenerator genny;
		genny = new NormalRandomStatementGenerator(numSubgoals,10,4,5);
		genny.setVariableGenerator("Normal",10,5);
		genny.setFunctionGenerator("Normal",4,3);
		
		for (int i = 0; i < numViews; i++){
			a_view = new View(genny.getRandomStatement(true));
			a_view.getHead().setFunctionHead(new String("V"+i));
			addView(a_view);
		}
		return true;
	}//end generateViews


	public boolean vectorContains(Vector vec, IPValue str){
		//this is a helper function for only checking for strings.	I
		//don't trust the built in vector one as far as I can throw it...
		//my guess is that it compares pointers
		int i;
		for(i= 0; i < vec.size(); i++){
			if (str.equals((IPValue)vec.elementAt(i))){
				return true;
			} //end if
		}//end for
		return false;
	}
	public StringBuffer printString(){
		StringBuffer retval;
		retval = new StringBuffer("");
		retval.append("Views = \n");
		for (int i = 0; i < NumViews; i++){
			retval.append(((View)Views.elementAt(i)).printString());
			retval.append("\n");
		}
		retval.append("Query = ");
		retval.append(myQuery.printString());
		return retval;
	}
	
	public void print(){
		System.out.println(printString());
	}
	
	public static void main(String args[]) throws IOException{
		
		/*
		Algorithm a = new Algorithm();
		Statement bob= new Statement();
		Statement babs = new Statement();
		List returned;
		*/
		//Vector atest = new Vector();
	/*	String container;
		String contained;
		boolean wasContainedIn;
		Vector returned;
		bob.read("V(x):- e1(x,y),e(y,z)");
		babs.read("V(a,b):-e1(a,b),e(b,a)");
	  */
		/*a.makeViewMapping(bob,babs);
		System.out.print("query equals");
		babs.print();
		for (i= 0; i < a.MDList.size();i++){
			((MD)a.MDList.elementAt(i)).print();
			//System.out.println("end of MD");
		}
		String aquery;
*/
 /*   Vector answers;
    File a_file = new File(".","allview.txt");
    
	FileWriter output = new FileWriter(a_file);
    System.out.println("at beginning");
	NormalRandomStatementGenerator genny;
	long current_time;
	GregorianCalendar a_calendar = new GregorianCalendar();
	int numViews = 2;
	int iter;
	for (iter = 0; iter < 3; iter++, numViews ++){    
	    current_time = System.currentTimeMillis();
        for (i = 0; i < 1; i++){
                System.out.println("getting views");
	            a.clear();
	    	    a.generateViews(5,numViews);
    	        genny = new NormalRandomStatementGenerator(3,10,4,4);
		        genny.setVariableGenerator("Normal",4,25);
		        genny.setFunctionGenerator("Normal",4,3);
    		
		        aquery=new Query ( genny.getRandomStatement(false));
		        a.setQuery(aquery);
    			
                //output.write(a.printString().toString());
                //output.flush();
                answers = a.run();
                //output.flush();
                //output.write(a.printString().toString());
                //output.flush();
                //output.write(a.printRewritings(answers));
                //output.write("\n\n");
                //output.flush();
                //System.out.println(a.printString().toString());
                //System.out.println(a.printRewritings(answers));
            }
    	
	    long milli_passed = System.currentTimeMillis() - current_time;
	    System.gc();
	    //output.write(numViews);
	    //output.write("");
	    //output.write((int)milli_passed);
	    //output.write("\n");
	    
	    StringBuffer out_string = new StringBuffer("");
	    out_string.append(numViews);
	    out_string.append(" ");
	    out_string.append((int)milli_passed);
	    out_string.append("\n");
	    System.out.print(out_string);
	    output.write(out_string.toString());
	    output.flush();
	    
	    
	    //System.out.println("done with an iteration!");
	}
	output.close();
	System.out.println("done!");
	    
*/
    
    }
}

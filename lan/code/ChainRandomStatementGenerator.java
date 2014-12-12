//import java.lang.*;
package minicon;
import java.util.Random;
import java.util.Vector;


public class ChainRandomStatementGenerator extends RandomStatementGenerator{ 
 //this class is going to have chained queries
 //a chained query is one where all of the atoms are linked
 //by a chain of variables, and they share no other variables.
 //The differerent parameters that can be set are:
 //Function starting:
 //the number at which functions will start
 //Function stopping:
 //the number at which functions will stop
 //the number of each function that you're gounig to get is roughly 
 //1/(function stopping - function starting)
 //Function length: length of each function; note, a function must have 
 //at least 2 variables, because otherwise, it can't chain.
 //currently, all lengths will be the same
 //Number of distinguished variables
 //The number of variables that are distinguished.  Since
 //this is a chain query, as long as one variable is distinguished, 
 //it makes sense.
 //at the moment, the first variable in the function is always the 
 //chained one
    
    
    int NumberFunctions;
    int FunctionStart;
    int FunctionStop;
    int FunctionLength;
	int _max_number_duplicates;
    int _number_distinguished;
    RandomVariableGenerator FunctionGenerator;
    String FunctionOffset;
  
    
    public ChainRandomStatementGenerator( int start,int stop, int num_fun,int length,int dist, int num_dup){
            _max_number_duplicates = 1;
			//need to figure out how to do assertions
            //assert start <  stop 
            //assert num_fun > 0
            //assert len
            //note, I assume that it means that if NumberDistinguished = -1, then
            //it wants them all.
            //actually, i assume in this case that we have three choices,
            //1,2, all, but that's beside the point
            NumberFunctions = num_fun;
            FunctionStart = start;
            FunctionStop = stop;
			if (FunctionStart > FunctionStop){
				//then we've screwed up; return an error message and exit
				System.out.println("function stop greater than the function start in ChainRandomStatementGenerator; exiting");
				System.exit(1);
			}
			if (NumberFunctions > _max_number_duplicates * (FunctionStop - FunctionStart)){
				System.out.println("You have requested more predicates than available in ChainRandomStatementGenerator; exiting");
				System.exit(1);
			}
            FunctionLength = length;
            _number_distinguished = dist;
			_max_number_duplicates = num_dup;
            FunctionGenerator = new RegularRandomVariableGenerator(start,stop);
            //Random rand = new Random();
            if (FunctionLength < 10){
                FunctionOffset = "00" + FunctionLength;
            }
            else if (FunctionLength < 100){
                FunctionOffset = "0" + FunctionLength;
            }
}//end ChainRandomStatementGenerator(int,int)
    
 private String getFunctionHead(){
    
    String retval = FunctionGenerator.getRandomVariable() + FunctionOffset;
    return retval;
 }
    
    
    
    
 public Statement getRandomStatement(boolean use_all){
     //this function returns a random statement based on the
     //values for the mean and deviation of the 
     //size of the query, and the mean and deviation of the 
     //num of variables and the number of function heads.
     //note; need to think about this more carefully for 
     //variables and function heads.
     Statement retval = new Statement();
     int i,j;
     int extra_variable = FunctionLength+1;
     //int length;
     //int fcnlength;
     //String temp;
     Predicate apred;
	 String a_function_head;
     for (i = 0; i < NumberFunctions; i++){
        apred = new Predicate();
		a_function_head = getFunctionHead();
		while (retval.numPredicateOccurances(a_function_head) == _max_number_duplicates){
			//if this is the case, then we need to pick a new one
			a_function_head = getFunctionHead();
			//note, we should be safe entering this loop, because we have carefully 
			//checked the number of predicates available earlier
		}
        apred.setFunctionHead(a_function_head);
        apred.addVariable((new Integer(i)).toString());
        apred.addVariable((new Integer(i+1)).toString());
        for (j = 2; j < FunctionLength; j++){
            apred.addVariable((new Integer(extra_variable)).toString());
            extra_variable++;
        }
        retval.addSubgoal(apred);  
        
     }
     generateHead(retval); 
     return retval;

  }
   
    public void generateHead(Statement a_state){
        //this function generates the head of the statement.  Note that probably only the
        //variables used will really matter, because we'll change the head name anyway.
        //thus i will call them all "q", which, come to think of it, is probably 
        //why i didn't get any errors in this before.  oy, i feel dumb.
        Predicate head = new Predicate();
		head.setFunctionHead("q");
        if (_number_distinguished == 1){
            head.addVariable(a_state.subgoalI(0).variableI(0));
        }
        else if  (_number_distinguished == 2){
            head.addVariable(a_state.subgoalI(0).variableI(0));
            head.addVariable(a_state.subgoalI(a_state.size()-1).variableI(1));
        }
		else if (_number_distinguished < 0){
			System.out.println("can't have a statement with no distinguished variables in ChainRandomStatementGenerator.generateHead");
			System.out.println("system will exit");
			System.exit(1);
		}
		else if (_number_distinguished == 0){
			Vector unique_vars = a_state.findUniqueVariables();
			int i;
			_number_distinguished = unique_vars.size();
			for (i = 0; i < _number_distinguished; i++){
				head.addVariable((String)unique_vars.elementAt(i));
			}
		}
			
		else{
			Vector unique_vars = a_state.findUniqueVariables();
			
				
			int i;
			int rand;
			float percent_distinguished = (float) _number_distinguished /
										  (float) unique_vars.size();
			int num_dist_needed = _number_distinguished;

			boolean all_done = false;
			boolean add_to_end = false;
			Random random = new Random();

			for (i = 0; all_done == false && 
						add_to_end == false &&
						i < unique_vars.size();i++){
				if (random.nextFloat() < percent_distinguished){
					//then we need to add it
					head.addVariable((String)unique_vars.elementAt(i));
					num_dist_needed--;
					if (num_dist_needed == 0){
						//then we can stop adding- break out of loop
						all_done = true;
					}//end if
				}//end if
				else if((unique_vars.size() - i   - 1) == num_dist_needed){
					//we need to check and make sure that we don't
					//need to just add the rest....
					add_to_end = true;
				}//end else if
				
			}//end for
			if (add_to_end){
				for (; i < unique_vars.size(); i++){
					head.addVariable((String)unique_vars.elementAt(i));
				}
			}
		}
        a_state.setHead(head);
        
        }//end of generateHead
        
   
}

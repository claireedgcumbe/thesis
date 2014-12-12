//import java.lang.*;
package minicon;
import java.util.Vector;
//import java.util.Random;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.Date;
import java.io.*;


public abstract class IPAlgorithm{
	protected Vector MDList;
	protected long _mapping_creation_time;
	protected long _mapping_combination_time;
	protected RapTimer _timer;
	protected Vector Views;
	protected int NumViews;
	protected IPQuery myQuery;
	public long timeStarted;
	protected int _view_rewrite_number;
	protected int _num_rewritings;
	protected int _num_mappings;
	protected Vector _rewritings;
	protected int _size;

	public int getNumMCDs(){
		if (MDList == null){
			return 0;
		}
		return MDList.size();
	}
	
	
	public String printRewritings(Vector rewritings){
		StringBuffer retval = new StringBuffer("");
		int i;
		IPMetaMD complete_mapping;
		if (rewritings == null || rewritings.size()==0){
			return ("no results\n");
		}
		int num_rewritings = rewritings.size();
		for (i = 0; i < num_rewritings; i++){
			complete_mapping = (IPMetaMD) rewritings.elementAt(i); 
			complete_mapping.setQueryHead(myQuery);
			complete_mapping.mergeQueryEqualities();
			_num_rewritings++;
			retval.append(complete_mapping.printString());
			retval.append("\n");
		}
		
		return retval.toString();
	}//end printRewritings
	
	public StatementWIP expandAnswer(String rewriting){
		StatementWIP retval = new StatementWIP();
		_view_rewrite_number = 0;
		IPView rewrite_statement = new IPView();
		rewrite_statement.read(rewriting);
		//now we need to add in the different parts; first get the head
		
		
		for (rewrite_statement.first(); !rewrite_statement.isDone(); rewrite_statement.next()){
			//now we have the statement, so add the rewriting per predicate
			retval.addAllSubgoalsAndIPs(expandView(rewrite_statement.current(),findView(rewrite_statement.current().getFunctionHead())));
			_view_rewrite_number ++;
		}
		//now we need to add in all interpreted predicates still in the query:
		for (int i = 0; i < rewrite_statement.numInterpretedPredicates(); i++){
		    retval.addInterpretedPredicate((InterpretedPredicate)rewrite_statement.interpretedPredicateI(i));
		}
		
		retval.setHead(rewrite_statement.getHead());
		return retval;
	}

	

	public Vector expandAllAnswers(String all_rewritings){
		Vector retval = new Vector(5);
		String current_substr = all_rewritings;
		String substr_1;
		int i;
		_rewritings = new Vector();
		int index; 
		index = all_rewritings.indexOf("\n");
		while (index != -1){
			substr_1 = current_substr.substring(0,index);
			//System.out.print(substr_1);
			if (!substr_1.equals("no results")){
				//then there were results
				retval.addElement(expandAnswer(substr_1));
			}
			_rewritings.addElement(substr_1);
			current_substr = current_substr.substring(index +1);
			index = current_substr.indexOf("\n");
		}
		
		return retval;
		
		
	}
	
	public void clear(){
		Views = new Vector(10);
		myQuery = null;
		MDList = new Vector(10);
		NumViews = 0;
		_num_rewritings = 0;
		_num_mappings = 0;
		myQuery = null;
		Views = new Vector();
		NumViews = 0;
	}

	public String run(){
		//this code actually runs the algorithm based on the input so far
		int i;
		//print();
		Vector list_of_combos;
		String retval;
		_timer.start();
		for (i = 0; i < NumViews; i++){
			makeViewMapping((IPView)Views.elementAt(i),myQuery);
		}
		_mapping_creation_time = _timer.stop();
		_timer.start();

		list_of_combos =  combineMDList();
		retval = printRewritings(list_of_combos);
		_mapping_combination_time = _timer.stop();
		return retval;
	}

	protected abstract boolean makeViewMapping(IPView view, IPQuery query);
	
	public IPAlgorithm(){
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

		MDList = new Vector(10);
	}


	public String type(){
		return "IP";
	}
	
	

	public Vector combineMDList(){
		//this function is going to take two at combining the
		//md's again.  It is going to be non recursive, but
		//actually work. ;)
		Vector next;
		int i,j,k;
		Vector current = new Vector();
		IPMetaMD current_md;
		IPMD a_md;
		int num_MDs = MDList.size();
		int query_size = myQuery.size();
		current.addElement(new 
		    IPMetaMD(myQuery.size(),
				   ((IPQuery)myQuery).numInterpretedPredicates()));
		for (i = 0; i < query_size; i++){
			//loop over all of the subgoals in the query
			next = new  Vector (2 * current.size());
			for (j = 0; j < current.size();j ++){
				//loop over the rewritings we have built up so far
				current_md = (IPMetaMD)current.elementAt(j);
				if (!current_md.checkCovered(i)){
					for (k = 0; k < num_MDs;k++){
						//loop over the different choices in our MD's
						a_md = (IPMD)MDList.elementAt(k);
						if (a_md.checkCovered(i)){
							IPMetaMD new_md = current_md.copy();
							if (new_md.addMD(a_md)){
								next.addElement(new_md);
							}
						} //end of if we need to add the mapping to the next list
						
					}//end looping over MDList
				}//end of if we need to try to add to this one
				else {
					//the current  one that we have already covers it
					next.addElement(current_md);
				}//end else
			}//end of looping over things in the current list
			if (next.size() == 0){
				return null;
			}
			current = next;
		}//end for of the query subgoals
		//System.gc();
		return current;
	}

/*
	public boolean makeViewMapping(IPView view, IPQuery query){
		int i,j,k;
		Vector small_md_list = new Vector(5);
		//we are going to first add to the smaller one, and then we are going to 
		//check and see if we have any that have the exact same footprint and 
		//remove it.

		boolean still_possible;
		Predicate current_query_subgoal, current_view_subgoal;
		
		
		Vector varsToCheck;
		for (i = 0; i < query.size(); i++){
			current_query_subgoal = query.subgoalI(i);
			varsToCheck = new Vector(5);
			for (j = 0; j < view.size();j++){
				IPMD aMD;
				current_view_subgoal = view.subgoalI(j);
				if (current_query_subgoal.getFunctionHead().equals(current_view_subgoal.getFunctionHead())) {
					_num_mappings++;
					//System.out.println("got here; new");
					aMD = new IPMD(query,view);
					for (still_possible = true, k=0;
						 still_possible == true &&
						 k < current_view_subgoal.size();
						 k ++){
						still_possible = aMD.addMapping(new Mapping(current_query_subgoal.variableI(k),current_view_subgoal.variableI(k)));
						if (!view.variableIsDistinguished(current_view_subgoal.variableI(k))){
							varsToCheck.addElement(current_query_subgoal.variableI(k));
						}
					}
					//note that it's not strictly necessary to check if the
					//still possible is true, but in the case of equivalent
					//rewritings of views, you'd need it there, so I'll leave
					//it in.
					if (still_possible){
						if (varsToCheck.size()> 0){
							//then we need to recursively check
							//and when it's returned, we have to send it 
							//to see if we've duplicated it.
							//aMD.setQuery(query);
							//aMD.setView(view);
							aMD.setSubgoalCovered(i);
							if (recursiveCheck(varsToCheck,new Vector(),query,view,aMD,i)){
								//at this point we must check to see if there is
								//any recursive things that must be added... so go ahead
								//now we need to make sure that it doesn't
								//already exist in the list
								//if (isNotDuplicate(aMD,small_md_list)){
								small_md_list.addElement(aMD);
								//}
							}
						}
						else {
							//we didn't need to do any recursive checks,
							//so we can just return the list as is
							//aMD.setView(view);
							aMD.setSubgoalCovered(i);
							MDList.addElement(aMD);
							

						}
					}



				}//end if the head was the same.
			}
		}
		
		//at this point, we need to check and see if there were any 
		//of the solutions that were exactly the same
		//at this point, we need to add everything from the small list
		for (i = 0; i  <small_md_list.size();i++){
			if (isNonDuplicate((IPMD)small_md_list.elementAt(i),small_md_list,i)){
				MDList.addElement(small_md_list.elementAt(i));
			}
		}
		//addNonDuplicates(small_md_list);
		return true;
	} //end makeViewMapping

	*/
	boolean isNonDuplicate(IPMD md_to_check, Vector already_there,int elt_on){
		//returns true if we should add this one, 
		//false else
		int i;
		IPMD current_md;
		int already_there_size = already_there.size();
		for (i = elt_on+1; i < already_there_size; i++){
			current_md = (IPMD) already_there.elementAt(i);
			if (md_to_check.sameSubgoalsCovered(current_md)){
				//if they were the same, then we need to keep looking
				//otherwise, they were different, so keep looking
				return false;
				
			}
		}
		return true;
	}
	


	public boolean recursiveCheck(Vector varsToCheck, Vector varsChecked,
								  Query query, View view, IPMD aMD,int original_predicate_location){
		//this function checks the variables in varsToCheck, and makes sure
		//that if they are mentioned elsewhere in the query that
		//all subgoals using them also are covered.
		//if they are, it returns true.  If not, it checks to see if
		//it can cover the rest of the subgoals, given the current
		//mapping.	If it can, it recurses on any new variables mapped, if
		//it can't, it returns false
		//predicate_number is the number of the predicate that we are currently
		//on; if we find one before this that needs it, we don't check it,
		//because we've already found that solution
		IPValue avar;
		boolean retval = true;
		int i,j,k,l;
		Predicate a_query_pred;
		Predicate a_view_pred;
		int vars_to_check_size = varsToCheck.size();
		Vector next_list_to_check;
		boolean mapping_found = true;
		boolean possible;
		int query_size = query.size();
		int view_size = view.size();
		IPValue map_value;
		//set up the next things to check; note that at the moment
		//this will be done regardless of whether we recurse or not,
		//but I can't come up with a better method of doing it off of
		//the top of my head.
		next_list_to_check = new Vector(5); //initially, we don't need
		//to check anything.
		//now set up the next checked list.  Note that by that point we
		//will have already checked everything in this list, so it should
		//just be the current list plus everything in the list to check
		//note that we set this up now, rather than later because we
		//don't wish to tell it to search for the transitive closure of
		//things that we will be checking for.	Since we don't care
		//what the old value of it is on the way back up, we can
		//change the original one without having to worry
		for (i = 0; i < vars_to_check_size; i++){
			varsChecked.addElement((String)varsToCheck.elementAt(i));
		}

		for (i = 0; i < vars_to_check_size && retval; i++){
			//this loop loops over the variables to check
			avar = (IPValue) varsToCheck.elementAt(i);
			map_value= aMD.variableMappedTo(avar);

			for (j = 0; j < query_size && retval;j++){
				//this loop loops over the subgoals
				a_query_pred = query.subgoalI(j);
				if (a_query_pred.containsVariable(avar)&& ! aMD.checkCovered(j)){
					//we need to map it
					//necessary?  need to think this over...
					//at this point, we can check it, and if it needs to be mapped
					//and it comes before the initial predicate we are checking, we 
					//can just through it out because this solution has already been
					//found
					//if (j < original_predicate_location){
					//    //we need to say that we can't use this one.  So return false
					//    return false;
					//}
					//actually, that won't work, because we want to use 
					//the one that covers the *most* subgoals; the first
					//one might only cover one... 
					//however we have the problem of what to do when we want
					//the one that has the fewest number of constrained variables
					//so perhaps the thing to do is check 
					//and see if there are exact duplicates....
					
					for(k = 0,mapping_found = false; mapping_found == false
													 && k < view_size; k++){
						a_view_pred = view.subgoalI(k);
						if (a_view_pred.getFunctionHead().equals(a_query_pred.getFunctionHead())){

							for (l = 0,possible = true; l < a_view_pred.size() && possible;l++){
								possible = aMD.addMapping(new Mapping(a_query_pred.variableI(l),
																	  a_view_pred.variableI(l)));
								//at this point, if the var in the query is mapped,
								//we must add it to the next checked list...
							}//end of the for mapping all of the variables
							if (possible){
								mapping_found = true;
								aMD.setSubgoalCovered(j);
								for (l= 0; l < a_view_pred.size();l++){
									if(!vectorContains(varsChecked,a_query_pred.variableI(l))){
										//then we need to check it on the next round
										next_list_to_check.addElement(a_query_pred.variableI(l));
									}
								}
							}

						}//end of if we can map it
					}//if the function head matches and we should thus
					//try to map it
					if (mapping_found == false){
						retval = false;
					}
				}//end if we need to map it
			}//end of looping over subgoals
		}//end of looping over the variables to check
		if (retval == false || next_list_to_check.size() == 0){
			//if either of these cases is true, then we don't need to
			//continue with the recursion, otherwise we do.
			return retval;
		}
		else {
			return recursiveCheck(next_list_to_check, varsChecked,
								  query, view, aMD,original_predicate_location);
		}
	}//end of recursive check

	    public int getNumMappings(){
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

	public Vector getViews(){
		return Views;
	}
	
    public boolean allAnswersContained(String answers){
		
        Vector expanded_answers = expandAllAnswers(answers);
        int i;
        for (i = 0; i < expanded_answers.size();i++){
            if (!answerIsContained((StatementWIP)expanded_answers.elementAt(i))){
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

    public boolean answerIsContained(StatementWIP answer){
        return myQuery.contains(answer);
        
    }
    
    public IPView findView(String predicate_name){
        int i;
        for (i = 0; i < NumViews; i++){
            if (((IPView)Views.elementAt(i)).getHead().getFunctionHead().equals(predicate_name)){
                return (IPView)Views.elementAt(i);
            }//end if
        }//end for
        return null;
    }//end findView
     
    //public String findHeadVariableMatch(String 
    
   
    public IPView expandView(Predicate a_pred,IPView actual_view){
        //this function expands the current view with the variables in a_pred.
        //it takes all of the unassigned variables and sets them to something
        //new... maybe exp_[uniquenum][viewvar]
        IPView retval; 
        //first, find out which statement it is....
        //Statement actual_view = findView(a_pred.function);
        Predicate a_view_predicate;
	   int i;
	   InterpretedPredicate an_int_pred;
	   //System.out.println("actual_view = " + actual_view.printString());
	   
        retval = new IPView(actual_view);
	   int num_int_preds = retval.numInterpretedPredicates();
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
	   //now we need to deal with the interpreted predicates
	   for (i = 0; i < num_int_preds; i ++){
		  an_int_pred = retval.interpretedPredicateI(i);
		  an_int_pred.setLHS(translateVariables(an_int_pred.getLHS(),a_pred, retval.getHead()));
		  an_int_pred.setRHS(translateVariables(an_int_pred.getRHS(),a_pred, retval.getHead()));
	   }//end dealing with the intepreted predicates
	   
        //retval.setHead(Predicate)
        return retval;
        
    }
    
    protected IPValue translateVariables(IPValue old, Predicate new_head, Predicate old_head){
        
        int var_loc;
	   if (old.isAnyConstant()){
		  return new IPValue(old);
	   }
	   
		  
        var_loc = old_head.variableIsAt(old);
        if (var_loc == -1){
            //then it wasn't in there, so return what we're prepending plus the variable
            return new IPValue("_re_" + _view_rewrite_number + old.printString());
        }
        return new IPValue(new_head.variableI(var_loc));
    }
    


	public void setQuery(IPQuery aquery){
		myQuery = aquery;
		_size = myQuery.size();
	}

	public IPQuery getQuery(){
		return myQuery;
	}
    
	public void addView(IPView aview){
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
		NoExistentialCheckAlgorithm a = new NoExistentialCheckAlgorithm();
		View V0 = new View();
		View V1 = new View();
		View V2 = new View();
		View V3 = new View();
		View V4 = new View();
		View V5 = new View();
		View V6 = new View();
		View V7 = new View();
		View V8 = new View();
		View V9 = new View();
		View V10 = new View();
		View V11 = new View();
		View V12 = new View();
		View V13 = new View();
		
		V0.read("V0(0,1,2,4,5,6,8,9,10,11,12,14,15,16,17,18,20,21,22,23):-16005(0,1,2,3,4),23005(5,6,7,8,9),6005(10,11,12,13,14),24005(15,16,17,18,19),22005(20,21,22,23,24),20005(3,7,13,19,24)");
		V1.read("V1(0,1,2,4,5,6,8,9,10,11,12,14,15,16,17,18,20,21,22,23):-23005(0,1,2,3,4),24005(5,6,7,8,9),5005(10,11,12,13,14),7005(15,16,17,18,19),8005(20,21,22,23,24),11005(3,7,13,19,24)");
		V2.read("V2(0,1,2,4,5,6,8,9,10,11,12,14,15,16,17,18,20,21,22,23):-14005(0,1,2,3,4),17005(5,6,7,8,9),19005(10,11,12,13,14),6005(15,16,17,18,19),7005(20,21,22,23,24),22005(3,7,13,19,24)");
		V3.read("V3(0,1,2,4,5,6,8,9,10,11,12,14,15,16,17,18,20,21,22,23):-5005(0,1,2,3,4),12005(5,6,7,8,9),20005(10,11,12,13,14),21005(15,16,17,18,19),11005(20,21,22,23,24),23005(3,7,13,19,24)");
		V4.read("V4(0,1,2,4,5,6,7,9,10,11,13,14,15,16,18,19,20,21,23,24):-8005(0,1,2,3,4),24005(5,6,7,8,9),9005(10,11,12,13,14),7005(15,16,17,18,19),19005(20,21,22,23,24),23005(3,8,12,17,22)");
		V5.read("V5(0,1,2,4,5,6,7,8,11,12,13,14,16,17,18,19,20,22,23,24):-21005(0,1,2,3,4),18005(5,6,7,8,9),16005(10,11,12,13,14),19005(15,16,17,18,19),20005(20,21,22,23,24),6005(3,9,10,15,21)");
		V6.read("V6(0,1,2,4,5,6,7,8,11,12,13,14,16,17,18,19,20,22,23,24):-21005(0,1,2,3,4),5005(5,6,7,8,9),18005(10,11,12,13,14),16005(15,16,17,18,19),14005(20,21,22,23,24),10005(3,9,10,15,21)");
		V7.read("V7(0,1,2,4,5,6,7,8,11,12,13,14,16,17,18,19,20,22,23,24):-6005(0,1,2,3,4),12005(5,6,7,8,9),14005(10,11,12,13,14),7005(15,16,17,18,19),23005(20,21,22,23,24),8005(3,9,10,15,21)");
		V8.read("V8(0,1,2,4,5,6,7,8,11,12,13,14,16,17,18,19,20,22,23,24):-22005(0,1,2,3,4),19005(5,6,7,8,9),20005(10,11,12,13,14),24005(15,16,17,18,19),18005(20,21,22,23,24),12005(3,9,10,15,21)");
		V9.read("V9(0,1,2,4,5,6,7,8,11,12,13,14,16,17,18,19,20,22,23,24):-19005(0,1,2,3,4),14005(5,6,7,8,9),17005(10,11,12,13,14),9005(15,16,17,18,19),6005(20,21,22,23,24),23005(3,9,10,15,21)");
		V10.read("V10(0,1,2,4,5,6,7,8,11,12,13,14,16,17,18,19,20,22,23,24):-19005(0,1,2,3,4),20005(5,6,7,8,9),14005(10,11,12,13,14),22005(15,16,17,18,19),9005(20,21,22,23,24),16005(3,9,10,15,21)");
		V11.read("V11(0,1,2,4,5,6,7,9,10,12,13,14,15,17,18,19,20,22,23,24):-5005(0,1,2,3,4),16005(5,6,7,8,9),19005(10,11,12,13,14),11005(15,16,17,18,19),9005(20,21,22,23,24),23005(3,8,11,16,21)");
		V12.read("V12(0,1,2,4,5,6,7,9,10,12,13,14,15,17,18,19,20,22,23,24):-23005(0,1,2,3,4),7005(5,6,7,8,9),22005(10,11,12,13,14),24005(15,16,17,18,19),8005(20,21,22,23,24),18005(3,8,11,16,21)");
		V13.read("V13(0,1,2,4,5,6,7,9,10,12,13,14,15,17,18,19,20,22,23,24):-22005(0,1,2,3,4),24005(5,6,7,8,9),8005(10,11,12,13,14),9005(15,16,17,18,19),6005(20,21,22,23,24),14005(3,8,11,16,21)");
		Query query = new Query();
		query.read("q(10,15):-16005(0,1,2,3,4),18005(5,6,7,8,9),24005(10,11,12,13,14),5005(15,16,17,18,19),8005(20,21,22,23,24),22005(3,7,13,19,24)");
		a.addView(V0);
		a.addView(V1);
		a.addView(V2);
		a.addView(V3);
		a.addView(V4);
		a.addView(V5);
		a.addView(V6);
		a.addView(V7);
		a.addView(V8);
		a.addView(V9);
		a.addView(V10);
		a.addView(V11);
		a.addView(V12);
		a.addView(V13);
		a.setQuery(query);
		System.out.println("about to run");
		String answers = a.run();
		System.out.println("finished running");
		//System.out.println(answers);
		a.allAnswersContained(answers);
		System.out.println("done containment check");
	}		
}

package minicon;
import java.util.*;
import java.util.Vector;
import java.util.Random;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.io.*;


public class IPConstantsAlgorithm extends IPImpliedAlgorithm{

	public Vector combineMDList(){
		//this function is going to take two at combining the
		//md's again.  It is going to be non recursive, but
		//actually work. ;)
		Vector next;
		int i,j,k,l;
		Vector current = new Vector();
		IPMetaMD current_md;
		Vector a_md_constants;
		Vector meta_md_constants;

		IPMD a_md;
		int num_MDs = MDList.size();
		int query_size = myQuery.size();
		boolean invalid_constants = false;
		IPValue an_ip_val;
		IPValue mapped_to;
		Vector mapped_to_vector;
		IPValue second_mapped_to;


		//System.out.println("there are " + num_MDs + " Mapping descriptions");

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
						    //now we need to check on the constants
						    a_md_constants = a_md.mapsToConstants();
						    meta_md_constants = current_md.mapsToConstants();
						    invalid_constants = false;
						    //note, still not quite right 'cause need to check if maps to same
						    //constant for it to be okay.
						    for (l = 0; l < a_md_constants.size() && invalid_constants == false; l++){
							   an_ip_val = ((Mapping)a_md_constants.elementAt(l)).variable;
							   mapped_to = ((Mapping)a_md_constants.elementAt(l)).mapping;

							   if (current_md.mapsToExistentialNonConstant(an_ip_val))
							   {
								  //System.out.println("invalid entry was " + an_ip_val.printString());

								  invalid_constants = true;
							   }
							   mapped_to_vector = current_md.getVariablesMappedTo(an_ip_val);
							   //System.out.println(mapped_to_vector);

							   if (mapped_to_vector.size() > 0 && !mapped_to.containedInVector(mapped_to_vector)){

								  invalid_constants = true;
								  //System.out.println("invalid in first loop");

							   }

						    }

						    for (l = 0; l < meta_md_constants.size() && invalid_constants == false; l++){
							   an_ip_val = ((Mapping)meta_md_constants.elementAt(l)).variable;
							   mapped_to = ((Mapping)meta_md_constants.elementAt(l)).mapping;

							   if (a_md.mapsToExistentialNonConstant(an_ip_val))
							   {
								  //System.out.println("invalid entry was " +an_ip_val+ " in second loop");

								  invalid_constants = true;
							   }
							   second_mapped_to = a_md.variableMappedTo(an_ip_val);

							   if (second_mapped_to != null && !mapped_to.equals(second_mapped_to)){

								  invalid_constants = true;
								  //System.out.println("invalid in secondloop again");

							   }


						    }


						    if (!invalid_constants){

							   IPMetaMD new_md =
								  current_md.copy();

							   if (new_md.addMD(a_md)){
								  next.addElement(new_md);
							   }
						    }//end of if there were no invalid constants

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


        public Vector combineRewritings2(Vector rewritings){
		Vector retval = null;
		int i;
		StatementWIP result;
		String PrintedResult;

		IPMetaMD complete_mapping;
		if (rewritings == null || rewritings.size()==0){
                        return retval;
		}
		int num_rewritings = rewritings.size();
		for (i = 0; i < num_rewritings; i++){
		    complete_mapping = (IPMetaMD) rewritings.elementAt(i);
		    complete_mapping.setQueryHead(myQuery);
		    complete_mapping.mergeQueryEqualities();
		    _num_rewritings++;
		    PrintedResult = complete_mapping.printString();
		    System.out.println("PrintedResult = " + PrintedResult);

		    System.out.println("expanding = " + expandAnswer(PrintedResult).printString());

		    result =  expandAnswer(PrintedResult);
		    result.printString();
		    Statement result_statement = result.getStatement();
		    Statement query_statement = myQuery.getStatement();
		    System.out.println("result = " + result_statement.printString());
		    System.out.println("query = " + query_statement.printString());
		    if (result.getStatement().contains(myQuery.getStatement())){
			   System.out.println("it was contained");
                           retval.addElement(complete_mapping);
         	    }

		}

		return retval;
	}//end combineRewritings2


        public boolean ipMetaMDsContained (Vector smallMetaVector, Vector largeMetaVector) {
          /*Vector validComboMDs = smallMetaVector.MDList;
          IPMetaMD meta;
          for (int i=0; i<smallMetaVector.size(); i++) {
            meta = (IPMetaMD)smallMetaVector(i);
          }
          Vector potentialComboMDs = largeMetaVector.MDList;
          */
          if (largeMetaVector.containsAll(smallMetaVector)) {
            return true;
          }
          else { return false; }
        }



    /*        public boolean constraintsCovered(IPMetaMD mapping) {
          Vector mds = mapping.MDList;
          int num_mds = mds.size();
          int i = 0;
          IPMD md;
          boolean safe;

          while (i < num_mds) {
            md = (IPMD)mds.elementAt(i);
            Vector viewIPs = md.getView().getInterpretedPredicates();
            if (! viewIPs.isEmpty()){ // a view has IPs, we have to start checking to see that the query and views' IPs match well
              safe = checkConstraints (md, viewIPs);
              if (safe == false) {
                return false;
              }
            }
            i++;
          }

          return true; // if we made it to this point that means that either non of the mapping's views had IPs and
          // thus the mapping is good. or the views' IPs matched the query's IPs well.

        }
    */







    /*        public Constraint getRelatedQueryConstraints(IPValue queryVar) {

          int i = 0, value = 0;
          double val;
          Double v;
          InterpretedPredicate ipQ;
          IPValue lhs, rhs;
          String variable, op;
          Vector queryIPs = myQuery.getInterpretedPredicates();
          Constraint queryCons, newCons;
          String qV = queryVar.getVariable();
          queryCons = new Constraint(qV);
          boolean first = true;

          while (i < queryIPs.size()) {
            ipQ = (InterpretedPredicate)queryIPs.elementAt(i);
            lhs = ipQ.getLHS();
            if (lhs.equals(queryVar)) {
              variable = lhs.getVariable();
              op = ipQ.getOperator();
              rhs = ipQ.getRHS();
              if (rhs.isNumericalConstant()) {
                val = rhs.getNumericalConstant();
                v = new Double(val);
                value = v.intValue();

                if (first) {
                  queryCons = new Constraint(variable, op, value);
                  first = false;
                }
                else { // there is more than one constraint on a particular variable
                  newCons = new Constraint(variable, op, value);
                  queryCons.intersectCons(newCons);
                }
              }
              // if value is not numerical we won't be able to compare, so ignore for now
            }
            i++;
          }
          return queryCons;
        }

    */

        public String combineRewritings(Vector rewritings){
		StringBuffer retval = new StringBuffer("");
		int i;
		StatementWIP result;
		String PrintedResult;

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
		    PrintedResult = complete_mapping.printString();
		    System.out.println("PrintedResult = " + PrintedResult);

		    System.out.println("expanding = " + expandAnswer(PrintedResult).printString());

		    result =  expandAnswer(PrintedResult);
		    result.printString();
		    Statement result_statement = result.getStatement();
		    Statement query_statement = myQuery.getStatement();
		    System.out.println("result = " + result_statement.printString());
		    System.out.println("query = " + query_statement.printString());
		    if (result.getStatement().contains(myQuery.getStatement())){
			   System.out.println("it was contained");

			   retval.append(PrintedResult);


					    }


		    retval.append("\n");
		}

		return retval.toString();
	}//end combineRewritings



	public boolean makeViewMapping(IPView view, IPQuery query){
	    //it's changed from the original version because it doesn't
	    //require a position to be checked if it's mapped to a
	    //constant as well as if it's distinguished in the view
		int query_size = query.size();
		int view_size = view.size();
		int i,j,k;
		int current_view_subgoal_size;
		Vector small_md_list = new Vector(5);
		//we are going to first add to the smaller one, and then we
		//are going to check and see if we have any that have the
		//exact same footprint and remove it.
		boolean still_possible;
		Predicate current_query_subgoal, current_view_subgoal;
		Vector varsToCheck;

		for (i = 0; i < query_size; i++){
			current_query_subgoal = query.subgoalI(i);
			varsToCheck = new Vector(5);
			for (j = 0; j < view_size;j++){
				IPMDIsImplied aMD;
				current_view_subgoal = view.subgoalI(j);
				//				System.out.println(view.subgoalI(j).printString());
				//				System.out.println(view_size);
				//				System.out.println(query_size);

				if (current_query_subgoal.getFunctionHead().equals(current_view_subgoal.getFunctionHead())) {
					_num_mappings++;
					//System.out.println("got here; new");
					aMD = new IPMDIsImplied(query,view);
					current_view_subgoal_size = current_view_subgoal.size();
					for (still_possible = true, k=0;
						 still_possible == true &&
						 k < current_view_subgoal_size;
						 k ++){
						still_possible = aMD.addMapping(new Mapping(current_query_subgoal.variableI(k),current_view_subgoal.variableI(k)));
						if (!view.variableIsDistinguished(current_view_subgoal.variableI(k))
						    && !current_view_subgoal.variableI(k).isAnyConstant()){
							varsToCheck.addElement(current_query_subgoal.variableI(k));
							//System.out.println(((IPValue)varsToCheck.elementAt(0)).printString());

						}
					}
					//note that it's not strictly necessary to check if the
					//still possible is true, but in the case of equivalent
					//rewritings of views, you'd need it there, so I'll leave
					//it in.
					if (still_possible){
						if (aMD.isValid()){
						if (varsToCheck.size()> 0){
							//then we need to recursively check
							//and when it's returned, we have to send it
							//to see if we've duplicated it.
							//aMD.setQuery(query);
							//aMD.setView(view);
							aMD.setSubgoalCovered(i);
							if (recursiveCheck(varsToCheck,new Vector(5),query,view,aMD,i)){
								//at this point we must check to see if there is
								//any recursive things that must be added... so go ahead
								//now we need to make sure that it doesn't
								//already exist in the list
								//if (isNotDuplicate(aMD,small_md_list)){
								if (aMD.isValid()){
									small_md_list.addElement(aMD);
								}
								//}
							}
						}
						else {
							//we didn't need to do any recursive checks,
							//so we can just return the list as is
							//aMD.setView(view);
							aMD.setSubgoalCovered(i);
							if (aMD.isValid()){
								MDList.addElement(aMD);
							}
						}
						}
					}

				}//end if the head was the same.
			}
		}

		//at this point, we need to check and see if there were any
		//of the solutions that were exactly the same
		//at this point, we need to add everything from the small list
		int small_md_list_size = small_md_list.size();
		for (i = 0; i  <small_md_list_size;i++){
			if (isNonDuplicate((IPMDIsImplied)small_md_list.elementAt(i),small_md_list,i) && ((IPMDIsImplied)small_md_list.elementAt(i)).isValid()){
				MDList.addElement(small_md_list.elementAt(i));
			}
		}
		//addNonDuplicates(small_md_list);
		return true;
	} //end makeViewMapping
	public boolean recursiveCheck(Vector varsToCheck, Vector varsChecked,
							IPQuery query, IPView view,
							IPMDIsImplied aMD,
							int original_predicate_location){
	    //this function checks the variables in varsToCheck, and
	    //makes sure that if they are mentioned elsewhere in the
	    //query that all subgoals using them also are covered.  if
	    //they are, it returns true.  If not, it checks to see if it
	    //can cover the rest of the subgoals, given the current
	    //mapping. If it can, it recurses on any new variables
	    //mapped, if it can't, it returns false predicate_number is
	    //the number of the predicate that we are currently on; if
	    //we find one before this that needs it, we don't check it,
	    //because we've already found that solution

	    //it's changed from the original version because it doesn't
	    //require a position to be checked if it's mapped to a
	    //constant as well as if it's distinguished in the view
	    IPValue avar;
	    boolean retval = true;
	    int i,j,k,l;
	    Predicate a_query_pred;
	    Predicate a_view_pred;

	    Vector next_list_to_check;
	    boolean mapping_found = true;
	    boolean possible;
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
	    for (i = 0; i < varsToCheck.size(); i++){
		   varsChecked.addElement((IPValue)varsToCheck.elementAt(i));
	    }

	    for (i = 0; i < varsToCheck.size() && retval; i++){
		   //this loop loops over the variables to check
		   avar = (IPValue) varsToCheck.elementAt(i);
		   map_value= aMD.variableMappedTo(avar);

		   for (j = 0; j < query.size() && retval;j++){
			  //this loop loops over the subgoals
			  a_query_pred = query.subgoalI(j);
			  if (a_query_pred.containsVariable(avar)&&
				 !aMD.checkCovered(j)){
				 //we need to map it necessary?  need to think
				 //this over...  at this point, we can check
				 //it, and if it needs to be mapped and it
				 //comes before the initial predicate we are
				 //checking, we can just through it out
				 //because this solution has already been
				 //found if (j < original_predicate_location){
				 ////we need to say that we can't use this
				 //one.  So return false return false; }
				 //actually, that won't work, because we want
				 //to use the one that covers the *most*
				 //subgoals; the first one might only cover
				 //one...  however we have the problem of what
				 //to do when we want the one that has the
				 //fewest number of constrained variables so
				 //perhaps the thing to do is check and see if
				 //there are exact duplicates....

				 for(k = 0,mapping_found = false; mapping_found == false
					    && k < view.size(); k++){
					a_view_pred = view.subgoalI(k);
					if (a_view_pred.getFunctionHead()
					    .equals(a_query_pred.getFunctionHead())){

					    for (l = 0,possible = true;
						    l < a_view_pred.size() && possible;l++){
						   possible = aMD.addMapping(new
							  Mapping(a_query_pred.variableI(l),
									a_view_pred.variableI(l)));
						   //at this point, if the var in the
						   //query is mapped, we must add it to
						   //the next checked list...
					    }//end of the for mapping all of the variables
					    if (possible){
						   mapping_found = true;
						   aMD.setSubgoalCovered(j);
						   for (l= 0; l < a_view_pred.size();l++){
							  if(!vectorContains(varsChecked,
											 a_query_pred.variableI(l)) &&
								!a_view_pred.variableI(l).isAnyConstant()){
								 //then we need to check it on
								 //the next round
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
							query, view, aMD,
							original_predicate_location);
	    }
	}//end of recursive check

	public static void main(String args[]) throws IOException{
	    String results;
	    IPQuery bob = new IPQuery();
	    IPConstantsAlgorithm imd = new IPConstantsAlgorithm();
	    bob.read("C1858142-A0('49338',$y) :- fm_anatomy.xml('49338',$y,$foo0,$foo1,$foo2)");
	    
	    IPView v1 = new IPView();
	    IPView v2 = new IPView();
	    IPView v3 = new IPView();
	    v1.read("base1($id,$sub,$sup,$name,$documentation) :- fm_anatomy.xml($id,$sub,$sup,$name,$documentation)");
	    
	    v2.read("base2($id,$sub,$sup,$name,$documentation) :- fm_anatomy.xml($id,$sub,$sup,$name,$documentation)");
	    
	    v3.read("base3($id,$sub,$sup,$name,$documentation) :- fm_anatomy.xml($id,$sub,$sup,$name,$documentation)");
	    
	    imd.setQuery(bob);
	    imd.addView(v1);
	    imd.addView(v2);
	    imd.addView(v3);
	    
	    //IPQuery query = new IPQuery();
	    //query.read("a(x):-e1(x),e2(y); x > '5'");
	    //IPView view = new IPView();
	    //view.read("v1(a):-e1(a),e2(b); a > '4'");
	    //imd.setQuery(query);
	    //imd.addView(view);
	    
	    results = imd.run();
	    System.out.println(results);
	}
}

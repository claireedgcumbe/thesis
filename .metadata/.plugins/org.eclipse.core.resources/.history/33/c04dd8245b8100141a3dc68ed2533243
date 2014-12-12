//import java.lang.*;
// this is the class that implements the algorithm that is complete for 
// the case where we have only expressions of the form x < c or x > c.
// this is the only implementation of interpreted predicates that 
// minicon is currently implemented for.
package minicon;
import java.util.Vector;
import java.util.Random;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.io.*;


public class IPImpliedAlgorithm extends IPAlgorithm{
	//private Vector MDList;
	public IPImpliedAlgorithm(){
		super();
		MDList = new Vector(10);
	}


	public String type(){
		return "IPImplied";
	}
	
	
	public boolean makeViewMapping(View view, Query query){
		System.out.println("can't call makeView mapping with non-ip predicates in IPImpliedAlgorithm");
		System.out.println("program will now exit");
		System.exit(1);
		return false;
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


	public boolean makeViewMapping(IPView view, IPQuery query){
		int query_size = query.size();
		int view_size = view.size();
		int i,j,k;
		int current_view_subgoal_size;
		Vector small_md_list = new Vector(5);
		//we are going to first add to the smaller one, and then we are going to 
		//check and see if we have any that have the exact same footprint and 
		//remove it.

		boolean still_possible;
		Predicate current_query_subgoal, current_view_subgoal;
		
		
		Vector varsToCheck;
		for (i = 0; i < query_size; i++){
			current_query_subgoal = query.subgoalI(i);
			varsToCheck = new Vector(5);
			for (j = 0; j < view_size;j++){
				IPMDIsImplied aMD;
				current_view_subgoal = view.subgoalI(j);
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
						if (!view.variableIsDistinguished(current_view_subgoal.variableI(k))){
							varsToCheck.addElement(current_query_subgoal.variableI(k));
							System.out.println(((IPValue)varsToCheck.elementAt(0)).printString());
							
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

	boolean isNonDuplicate(IPMDIsImplied md_to_check, Vector already_there,int elt_on){
		//returns true if we should add this one, 
		//false else
		int i;
		IPMDIsImplied current_md;
		int already_there_size = already_there.size();
		for (i = elt_on+1; i < already_there_size; i++){
			current_md = (IPMDIsImplied) already_there.elementAt(i);
			if (md_to_check.sameSubgoalsCovered(current_md)){
				//if they were the same, then we need to keep looking
				//otherwise, they were different, so keep looking
				return false;
				
			}
		}
		return true;
	}
	


	public boolean recursiveCheck(Vector varsToCheck, Vector varsChecked,
								  IPQuery query, IPView view, IPMDIsImplied aMD,int original_predicate_location){
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
													 && k < view.size(); k++){
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

	
	
	public static void main(String args[]) throws IOException{	
		String results;
		IPQuery bob = new IPQuery();
		bob.read("q(x):-e1(x,y);y < '6'");
		IPView babs = new IPView();
		babs.read("q(a,b):-e1(a,b)");
		IPImpliedAlgorithm imd = new IPImpliedAlgorithm();
		imd.setQuery(bob);
		imd.addView(babs);
		results = imd.run();
		System.out.println(results);
		
		/*NoExistentialCheckAlgorithm a = new NoExistentialCheckAlgorithm();
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
		*/
	}		
}

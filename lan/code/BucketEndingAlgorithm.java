//import java.lang.*;
package minicon;
import java.util.Vector;
//import java.util.Random;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.Date;
import java.io.*;


public class BucketEndingAlgorithm extends NewAlgorithm{
	protected Vector [] _buckets;
	public BucketEndingAlgorithm(){
		super();
		_buckets = null;
	}


	public String type(){
		return "BucketEnding";
	}

	public Vector getConjunctiveRewritings(Vector p_rewritings)
	{
		if (p_rewritings == null)
		{
		 return null;
		}
		Vector retval = new Vector(p_rewritings.size());
		int i;
		Statement a_state;
		int num_rewritings = p_rewritings.size();
		MetaMDWithoutExistentialCheck complete_mapping;
		for (i = 0; i < p_rewritings.size(); i++){
			complete_mapping = (MetaMDWithoutExistentialCheck) p_rewritings.elementAt(i); 
			complete_mapping.setQueryHead(myQuery.getHead());
			complete_mapping.mergeQueryEqualities();
			_num_rewritings++;
			retval.addElement(complete_mapping.printString() + "\n");
		}
		return retval;
	}
	
	
	public String printRewritings(Vector rewritings){
		StringBuffer retval = new StringBuffer("");
		int i;
		MetaMDWithoutExistentialCheck complete_mapping;
		if (rewritings == null || rewritings.size()==0){
			return ("no results\n");
		}
		for (i = 0; i < rewritings.size(); i++){
			complete_mapping = (MetaMDWithoutExistentialCheck) rewritings.elementAt(i); 
			complete_mapping.setQueryHead(myQuery.getHead());
			complete_mapping.mergeQueryEqualities();
			_num_rewritings++;
			retval.append(complete_mapping.printString());
			retval.append("\n");
		}
		
		return retval.toString();
	}//end printRewritings
	
	
	public Vector runConjunctiveRewritings()
	{
		//this code actually runs the algorithm based on the input so far
		int i;
		//print();
		Vector retval;
		Vector list_of_combos;
		_buckets = new Vector[myQuery.size()];
		for (i = 0; i < myQuery.size(); i++){
			_buckets[i] = new Vector();
		}
		for (i = 0; i < NumViews; i++){
			makeViewMapping((View)Views.elementAt(i),myQuery);
		}
		list_of_combos =  combineMDList();
		retval = getConjunctiveRewritings(list_of_combos);
		return retval;
	}
	
	public String run(){
		//this code actually runs the algorithm based on the input so far
		int i;
		//print();
		String retval;
		Vector list_of_combos;
		_timer.start();
		_buckets = new Vector[myQuery.size()];
		for (i = 0; i < myQuery.size(); i++){
			_buckets[i] = new Vector();
		}
		for (i = 0; i < NumViews; i++){
			makeViewMapping((View)Views.elementAt(i),myQuery);
		}
		_mapping_creation_time = _timer.stop();
		_timer.start();
		list_of_combos =  combineMDList();
		retval = printRewritings(list_of_combos);
		_mapping_combination_time = _timer.stop();
		return retval;
	}


	
	

	public Vector combineMDList(){
		//this function combines the md list.  this is completely different from the 
		//old version, in fact, I think this one will be recursive for clarity.
		return combineMDBuckets(0,new MetaMDWithoutExistentialCheck(_size),0);
	}//end combineMDList

		
	public Vector combineMDBuckets(int bucket_to_check, MetaMDWithoutExistentialCheck meta_MD, int current_bucket_elt){
		MetaMDWithoutExistentialCheck new_meta_md = meta_MD.copy();
		Vector retval = null;
		Vector temp_vector = null;
		int next_to_check;
		//if there's nothing in the bucket, there's no point in going on
		if (_buckets[bucket_to_check].size() == 0){
			return null;
		}
		if (new_meta_md.addMD((MDWithoutExistentialCheck)_buckets[bucket_to_check].elementAt(current_bucket_elt))){
			//then we need to check this one and add it with the rest
			next_to_check = new_meta_md.getNextUncovered(bucket_to_check);
			if (next_to_check != -1){
				//then we need to check the next bucket
				//and we need to check all items in that bucket, so we need to start at zero
				//note, this should fix a previous bug
				temp_vector = combineMDBuckets(next_to_check,new_meta_md,0);
			}//end of if we need to check the next bucket	
			if (new_meta_md.getNumUncovered() != 0){
				//then this wasn't a final answer
				new_meta_md = null;
			}
		}//end if we needed to add this one
		else{
			//we did nothing, so I'll mark that by setting new_meta_md to null
			new_meta_md = null;
		}
		//now check the rest of the current bucket
		//next_to_check = new_meta_md.getNextUncovered(bucket_to_check);
		if (current_bucket_elt +1 < _buckets[bucket_to_check].size()){
			//then we need to check the rest.
			retval = combineMDBuckets(bucket_to_check,meta_MD,current_bucket_elt +1);
		}
		//now we have to combine the answers
		if (retval == null && temp_vector == null){
			if(new_meta_md == null){
				//then there were no answers, return null
				return null;
			}
			else{
				//then we need to return a vector with just new_meta
				retval = new Vector();
				retval.addElement(new_meta_md);
				return retval;
			}//end of if we need to just return the new meta md
		}//end of if there were no answers returned by either recursive call
		if (retval !=null && temp_vector != null){
			//then we need to combine the two
			int i;
			for (i = 0; i < temp_vector.size(); i++){
				retval.addElement(temp_vector.elementAt(i));
			}
		}//end of if we need to combine the two
		else{
			if (retval == null){
				//move over to retval so that it will all be symmetrical
				//we know that temp_vector is not null, or we would have exited earlier
				retval = temp_vector;
			}
		}
		//at this point we know that we have all of the stuff in retval unless there 
		//is something in new_meta_md
		if (new_meta_md !=null){
			retval.addElement(new_meta_md);
		}
		return retval;
	}//end recursiveCombineMDList
	
	
	public void clear(){
		super.clear();
		_buckets = null;
	}


	public boolean makeViewMapping(View view, Query query){
		int i,j,k;
		Vector small_md_list = new Vector(5);
		//System.out.println("In makeViewMapping view =  " + view.printString().toString());
		//System.out.println("In makeViewMapping query = " + query.printString().toString());
		//we are going to first add to the smaller one, and then we are going to 
		//check and see if we have any that have the exact same footprint and 
		//remove it.
		boolean still_possible;
		Predicate current_query_subgoal, current_view_subgoal;
		Vector varsToCheck;
		//this is different for the regular algorithm and the new version, we now add in that
		//we need to have the different buckets created:
		//if (_buckets == null){
		//	_buckets = new Vector[query.size()];
		//}
		//now initialize them to new buckets
		//for (i = 0; i < query.size(); i++){
		//	_buckets[i] = new Vector();												  
		//}
		
		//back to our regularly scheduled program
		for (i = 0; i < query.size(); i++){
			current_query_subgoal = query.subgoalI(i);
			varsToCheck = new Vector(5);
			for (j = 0; j < view.size();j++){
				MDWithoutExistentialCheck aMD;
				current_view_subgoal = view.subgoalI(j);
				if (current_query_subgoal.getFunctionHead().equals(current_view_subgoal.getFunctionHead())) {
					_num_mappings++;
					aMD = new MDWithoutExistentialCheck(query,view);
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
							//aMD.setView(view);
							//aMD.setQuery(query);
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
							//MDList.addElement(aMD);
							//at this point we need to add it to the correct bucket.
							//since there was no recursive check, we know that it just goes in
							//bucket i
							//_buckets[i].addElement(aMD);
							putInBucket(aMD);

						}
					}
				}//end if the head was the same.
			}
		}
		
		//at this point, we need to check and see if there were any 
		//of the solutions that were exactly the same
		//at this point, we need to add everything from the small list
		for (i = 0; i  <small_md_list.size();i++){
			if (isNonDuplicate((MDWithoutExistentialCheck)small_md_list.elementAt(i),small_md_list,i)){
				//MDList.addElement(small_md_list.elementAt(i));
				//now we need to figure out which bucket to add things to....
				//_buckets[((MDWithoutExistentialCheck)small_md_list.elementAt(i)).firstSubgoalCovered()].addElement(small_md_list.elementAt(i));
				putInBucket((MDWithoutExistentialCheck)small_md_list.elementAt(i));																						
			}
		}
		//addNonDuplicates(small_md_list);
		return true;
	} //end makeViewMapping

	boolean isNonDuplicate(MDWithoutExistentialCheck md_to_check, Vector already_there,int elt_on){
		//returns true if we should add this one, 
		//false else
		int i;
		MDWithoutExistentialCheck current_md;
		for (i = elt_on+1; i < already_there.size(); i++){
			current_md = (MDWithoutExistentialCheck) already_there.elementAt(i);
			if (md_to_check.sameSubgoalsCovered(current_md)){
				//if they were the same, then we need to keep looking
				//otherwise, they were different, so keep looking
				return false;
				
			}
		}
		return true;
	}
	
	protected boolean putInBucket(MDWithoutExistentialCheck a_md){
		_buckets[a_md.firstSubgoalCovered()].addElement(a_md);
		return true;
	}

	public boolean recursiveCheck(Vector varsToCheck, Vector varsChecked,
								  Query query, View view, MDWithoutExistentialCheck aMD,int original_predicate_location){
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
		System.out.println("Entering main for BucketEndingAlgorithm");
		BucketEndingAlgorithm a = new BucketEndingAlgorithm();
		View v0 = new View();
		View v1 = new View();
		View v2 = new View();
		View v3 = new View();
		View v4 = new View();
		View v5 = new View();
		View v6 = new View();
		Query q = new Query();
		v0.read("V0(a,b):-e1(a,b)");
		q.read("q(a):-e1(a,b),e2(b,c)");
		a.addView(v0);
		v1.read("V1(b,c):-e2(b,c)");
		a.addView(v1);
		
		/*		v0.read("V0(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-8005(0,1,2,3,4),21005(5,6,7,8,9),5005(10,11,12,13,14),12005(15,16,17,18,19),11005(20,21,22,23,24),10005(0,9,10,17,21)");
		v1.read("V1(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-17005(0,1,2,3,4),8005(5,6,7,8,9),14005(10,11,12,13,14),20005(15,16,17,18,19),9005(20,21,22,23,24),19005(0,9,10,17,21)");
		v2.read("V2(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-20005(0,1,2,3,4),22005(5,6,7,8,9),13005(10,11,12,13,14),14005(15,16,17,18,19),16005(20,21,22,23,24),9005(0,9,10,17,21)");
		v3.read("V3(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-22005(0,1,2,3,4),19005(5,6,7,8,9),7005(10,11,12,13,14),9005(15,16,17,18,19),16005(20,21,22,23,24),18005(0,9,10,17,21)");
		v4.read("V4(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-16005(0,1,2,3,4),19005(5,6,7,8,9),17005(10,11,12,13,14),24005(15,16,17,18,19),22005(20,21,22,23,24),10005(0,9,10,17,21)");
		v5.read("V5(1,2,3,4,6,7,8,9,10,11,12,13,15,17,18,19,20,21,23,24):-14005(0,1,2,3,4),23005(5,6,7,8,9),22005(10,11,12,13,14),21005(15,16,17,18,19),19005(20,21,22,23,24),9005(0,5,14,16,22)");
		v6.read("V6(1,2,3,4,6,7,8,9,10,11,12,13,15,17,18,19,20,21,23,24):-22005(0,1,2,3,4),10005(5,6,7,8,9),14005(10,11,12,13,14),17005(15,16,17,18,19),6005(20,21,22,23,24),21005(0,5,14,16,22)");
		a.addView(v0);
		a.addView(v1);
		a.addView(v2);
		a.addView(v3);
		a.addView(v4);
		a.addView(v5);
		a.addView(v6);		
		v0.read("V0(0,1,6,7,8,2,9,10,11,3,12,13,14,4,15,16,17,5,18,19,20,21,22,23):-12005(0,1,6,7,8),16005(1,2,9,10,11),23005(2,3,12,13,14),21005(3,4,15,16,17),11005(4,5,18,19,20),15005(5,6,21,22,23)");
		v1.read("V1(0,1,6,7,8,2,9,10,11,3,12,13,14,4,15,16,17,5,18,19,20,21,22,23):-12005(0,1,6,7,8),20005(1,2,9,10,11),7005(2,3,12,13,14),17005(3,4,15,16,17),8005(4,5,18,19,20),13005(5,6,21,22,23)");
		q.read("q(0,6):-12005(0,1,6,7,8),7005(1,2,9,10,11),17005(2,3,12,13,14),8005(3,4,15,16,17),13005(4,5,18,19,20),15005(5,6,21,22,23)");

		
		q.read("q(0,15):-8005(0,1,2,3,4),18005(5,6,7,8,9),14005(10,11,12,13,14),17005(15,16,17,18,19),23005(20,21,22,23,24),16005(0,9,10,17,21)");*/
		a.setQuery(q);
		String answers = a.run();
		System.out.println(answers);
		System.out.println("done");
	}
}

//import java.lang.*;
package minicon;
import java.util.Vector;
//import java.util.Random;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.Date;
//import java.io.*;


public abstract class NewAlgorithm extends Algorithm{
	protected Vector MDList;
	public NewAlgorithm(){
		super();
		MDList = new Vector(10);
	}


	public String type(){
		return "New";
	}
		public int getNumMCDs(){
		if (MDList == null){
			return 0;
		}
		else{
			return MDList.size();		}
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
			_num_rewritings++;
			retval.append(complete_mapping.printString());
			retval.append("\n");
		}
		
		return retval.toString();
	}//end printRewritings
	
	public Statement expandAnswer(String rewriting){
		Statement retval = new Statement();
		_view_rewrite_number = 0;
		Statement rewrite_statement = new Statement();
		rewrite_statement.read(rewriting);
		//now we need to add in the different parts; first get the head
		
		
		for (rewrite_statement.first(); !rewrite_statement.isDone(); rewrite_statement.next()){
			//now we have the statement, so add the rewriting per predicate
			retval.addAllSubgoals(expandView(rewrite_statement.current(),findView(rewrite_statement.current().getFunctionHead())));
			_view_rewrite_number ++;
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
			makeViewMapping((View)Views.elementAt(i),myQuery);
		}
		_mapping_creation_time = _timer.stop();
		_timer.start();
		list_of_combos =  combineMDList();
		retval = printRewritings(list_of_combos);
		_mapping_combination_time = _timer.stop();
		return retval;
	}

	protected abstract boolean makeViewMapping(View view, Query query);
	
	protected abstract Vector combineMDList();

	//protected abstract boolean isNonDuplicate(MD md_to_check, Vector already_there,int elt_on);
	
//	protected boolean recursiveCheck(Vector varsToCheck, Vector varsChecked,
//								  Query query, View view, MD aMD,int original_predicate_location);
	
	
}

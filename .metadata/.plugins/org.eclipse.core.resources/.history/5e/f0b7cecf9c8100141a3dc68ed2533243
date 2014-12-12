package minicon;
import java.util.Vector;

//import com.ms.util.VectorSort;
//import com.ms.util.Comparison;

public class StatementWIP
{
	//this class is short for StatementWithInterpretedPredicates
	//the purpose of this class is to handle statements with interpreted
	//predicates.  
	//at the moment i'll implement constants *only* as equalities at
	//the end, but this may need to be changed later....
	protected Vector _interpreted_predicates;
	protected Vector _constants;
	protected Statement _statement;
	protected Vector _lteqs;
	protected Vector _neqs;
	
	public StatementWIP(){
		_statement = new Statement();
		_interpreted_predicates = new Vector();
		_constants = new Vector();
		_lteqs = new Vector();
		_neqs = new Vector();
	}

	public StatementWIP(Statement a_state){
		_statement = a_state;
		_interpreted_predicates = new Vector();
		_constants = new Vector();
		_lteqs = new Vector();
		_neqs = new Vector();
	}
	

    public StatementWIP(String a_string)
    {
	   this();
	   
	   read(a_string);
    }
    

	public StatementWIP(StatementWIP a_state){
		/**Copy constructor; makes a deep copy
		*/	

		_statement = new Statement(a_state._statement);
		int a_size;
		a_size =a_state._interpreted_predicates.size();
		int i;
		_interpreted_predicates = new Vector(a_size);
		for (i = 0; i < a_size; i++){
			_interpreted_predicates.addElement(new InterpretedPredicate(a_state.interpretedPredicateI(i)));
		}
		a_size = a_state._constants.size();
		_constants = new Vector(a_size);
		for  (i = 0; i < a_size; i++){
			_constants.addElement(a_state._constants.elementAt(i));
		}
		a_size = a_state._lteqs.size();
		_lteqs = new Vector(a_size);
		for (i = 0; i < a_size; i++){
			_lteqs.addElement(new InterpretedPredicate((InterpretedPredicate)a_state._lteqs.elementAt(i)));
		}
		a_size = a_state._neqs.size();
		_neqs = new Vector(a_size);
		for (i = 0; i < a_size; i++){
			_neqs.addElement(new InterpretedPredicate((InterpretedPredicate)a_state._neqs.elementAt(i)));
		}	
	}//end copy constructor
	
	
	public int size(){
		return _statement.size();
	}
	
	public Predicate getHead(){
		return _statement.getHead();
	}
	
	public Vector getVariables(){
		return _statement.findUniqueVariables();
	}
	
	public Vector getNumericalConstants(){
		return _constants;
	}
	

	public void setStatement(Statement a_state){
		_statement = a_state;
	}
	
	public Statement getStatement(){
		return _statement;
	}
	
	public int numInterpretedPredicates(){
		return _interpreted_predicates.size();
	}
	
	
	public void addInterpretedPredicate(InterpretedPredicate an_ip){
		addInterpretedPredicate(an_ip,_lteqs,_neqs);
	}
	
	public void addInterpretedPredicate(InterpretedPredicate an_ip,Vector lteqs,Vector neqs){
		_interpreted_predicates.addElement(an_ip);
		InterpretedPredicate new_ips[];
		if (an_ip.isLHSAConstant()){
			//then we need to add the lhs to the list of constants;
			addConstant(an_ip.getLHSConstant());
		}
		if (an_ip.isRHSAConstant()){
			//then we need to add the rhs to the list of constants;
			addConstant(an_ip.getRHSConstant());
		}
		//now we've added the interpreted predicate to the final answer for
		//the output; now add it to the lteq's for checking the satisfaction
		new_ips = an_ip.translateIP();
		if (new_ips[0] != null){
			lteqs.addElement(new_ips[0]);
		}
		if (new_ips[1] != null){
			neqs.addElement(new_ips[1]);
		}
		/*
		if (an_ip.getOperator().equals("<=")){
			lteqs.addElement(an_ip.copy());
		}
		else if(an_ip.getOperator().equals("!=")){
			neqs.addElement(an_ip.copy());
		}
		else if(an_ip.getOperator().equals("<")){
			new_ip = an_ip.copy();
			new_ip.setOperator("<=");
			lteqs.addElement(new_ip);
			new_ip = an_ip.copy();
			new_ip.setOperator("!=");
			neqs.addElement(new_ip);
		}
		else if(an_ip.getOperator().equals(">")){
			new_ip = an_ip.switchSides();
			new_ip.setOperator("<=");
			lteqs.addElement(new_ip);
		}
		else if (an_ip.getOperator().equals(">=")){
			new_ip = an_ip.switchSides();
			new_ip.setOperator("<=");
			lteqs.addElement(new_ip);
			new_ip = an_ip.switchSides();
			new_ip.setOperator("!=");
			neqs.addElement(new_ip);
		}
		else if (an_ip.getOperator().equals("==")){
			new_ip = an_ip.copy();
			new_ip.setOperator("<=");
			lteqs.addElement(new_ip);
			new_ip = an_ip.switchSides();
			new_ip.setOperator("<=");
		}
		else {
			System.out.println("couldn't find operator " + an_ip.getOperator() + 
							   " in StatementWIP.addInterpretedPredicate");
		}//end else			
		*/
	}//end addIntepretedPredicate
	
	protected void addConstant(double a_const){
		/**Adds a constant to the sorted list of constants.
		 */
		Double double_val = new Double(a_const);
		if (! _constants.contains(double_val)){
			_constants.addElement(double_val);
		}
		
	}//end addConstant
	
	
	protected void sortConstants(){
	    //VectorSort.sort(_constants,new DoubleComparison());
	    VectorSort.sort(_constants);
	    
	}//end sortConstants
	
	
	public InterpretedPredicate interpretedPredicateI(int i){
		return (InterpretedPredicate) _interpreted_predicates.elementAt(i);
	}
	
	protected boolean inEqualityChecksAreValid(Vector [] vars){
		/**This function checks to make sure that all equalities and inequalities hold.
		 */
		InterpretedPredicate a_pred;
		int i,j;
		boolean keep_trying;
		for (i = 0; i < _interpreted_predicates.size(); i++){
			a_pred = (InterpretedPredicate)_interpreted_predicates.elementAt(i);
			//now we need to make sure that if both sides are variables, 
			//if they're supposed to be in the same one they are
			if (!a_pred.isLHSAConstant() &&  !a_pred.isRHSAConstant()){
				//if not, then we don't need to check this one
				if (a_pred.getOperator().equals("!=")){
					for (j = 0, keep_trying = true; j < _interpreted_predicates.size() && keep_trying && vars[j] != null; j++){
						if (vars[j].contains(a_pred.getLHSVariable())){
							//then the rhs better not be there
							if (vars[j].contains(new Double(a_pred.getRHSConstant()))){
								return false;
							}
							//since we only have each variable in one bucket,
							//then we know we're safe, and we don't have to keep 
							//checking.
							else {
								keep_trying = false;
							}
						}
					}
				}
			}//end of making sure that both sides are variables
		}//end looping over all of the interpreted predicate constraints.
		return true;
	}//end inEqualityChecksAreValid
	
	protected void addConstantConstraints(){
		/**this adds all of the constraints that say that the constants are all
		/* less than each other
		 */
		InterpretedPredicate an_ip;
		sortConstants();//we'd better sort them first, or we'll wind up with incorrect answers
		for(int i = 0; i < _constants.size() -1; i++){
			an_ip = new InterpretedPredicate();
			an_ip.setLHS(((Double)(_constants.elementAt(i))).doubleValue());
			an_ip.setRHS(((Double)(_constants.elementAt(i+1))).doubleValue());
			an_ip.setOperator("!=");
			_neqs.addElement(an_ip);
			an_ip = an_ip.copy();
			an_ip.setOperator("<=");
			_lteqs.addElement(an_ip);
		}
	}//end addConstantConstraints
	
	public String printString(){
	    StringBuffer retval;
	    if (_statement != null){
		   retval = _statement.printString();	
	    }
	    else{
		   retval = new StringBuffer();
	    }
	    
	    
		int i;
		if (_interpreted_predicates.size() > 0){
			retval.append("; ");
		}
		for(i = 0; i < _interpreted_predicates.size() -1; i++){
			retval.append(((InterpretedPredicate)_interpreted_predicates.elementAt(i)).printString());
			retval.append(", ");
		}
		if (_interpreted_predicates.size() > 0){
			retval.append(((InterpretedPredicate)_interpreted_predicates.elementAt(i)).printString());
		}
		
		return retval.toString();
	}
	
	public void print(){
		System.out.println(printString());
	}
	
	public boolean contains(StatementWIP q1){
		//this function checks to see if one statement is contained in the other.
		Vector q1_unique_vars = q1.getStatement().findUniqueVariables();
		Set a_set = new Set(q1_unique_vars.size());
		Vector partitions = a_set.createPartitions();
		Vector a_partition_lteqs;
		Vector a_partition_neqs;
		Vector an_ordering_lteqs;
		Vector constants;
		//Vector an_ordering_neqs;
		Vector [] a_partitioning;
		Vector a_partition;
		int [] order;
		Graph graph;
		int keep_going;
		int count,i,j,k;
		Permutation a_perm;
		q1.addConstantConstraints();
		InterpretedPredicate a_pred;
		//now we have the set of all partitions; we must order them and see if we can
		//then use containment checking to see if 
		//For all Di’s passing step 2 the head of Q1 can be derived by applying Q2
		boolean valid_ordering;
		Vector an_order;
		IPValue first_partition_element;
		IPValue partition_element_k;
		for (i = 0; i < partitions.size();i++){
			a_partition_lteqs = (Vector)q1._lteqs.clone();
			a_partition_neqs = (Vector)q1._neqs.clone();
			//now we check to make sure that everything that's equal to each other
			//is equal to each other, and everything that's not equal to each other
			//is not equal to each other
			a_partitioning = (Vector [])partitions.elementAt(i);
			if (inEqualityChecksAreValid(a_partitioning)){
				//find the size of the partition;
				//and add in the equalities we currently have...
				for (count = 0, keep_going = 1; count < a_partitioning.length && keep_going==1; count++){
					//we need it find the size of the current partition.  
					if (a_partitioning[count] == null){
						keep_going = 0;
						count --;
					}
				}
				//now we need to add in the equalities that we currently have
				for(j = 0; j < count;j++){
					a_partition = a_partitioning[j];
					//each element in a_partition is an Integer that encodes the
					//location of the variable in q1_unique_vars;

					first_partition_element =(IPValue) q1_unique_vars.elementAt(((Integer)a_partition.elementAt(0)).intValue());
					for (k = 1; k < a_partition.size();k++){
						partition_element_k = (IPValue)q1_unique_vars.elementAt(((Integer)a_partition.elementAt(k)).intValue());
						a_pred = new InterpretedPredicate();
						a_pred.setLHS(first_partition_element);
						a_pred.setRHS(partition_element_k);
						a_pred.setOperator("<=");
						a_partition_lteqs.addElement(a_pred);
						a_pred = a_pred.switchSides();
						a_partition_lteqs.addElement(a_pred);
					}//end of looping over the partition
					for (k = 0; k < j; k++){
						partition_element_k = (IPValue)q1_unique_vars.elementAt(((Integer)a_partitioning[k].elementAt(0)).intValue());
						a_pred = new InterpretedPredicate();
						a_pred.setLHS(first_partition_element);
						a_pred.setRHS(partition_element_k);
						a_pred.setOperator("!=");
						a_partition_neqs.addElement(a_pred);
					}//end of looping over each partition we've already covered
					
				}//end of looping over the partitioning
				a_perm = new Permutation(count);
				keep_going = 1;
				while(keep_going != -1){
					order = a_perm.getCurrent();
					an_ordering_lteqs = (Vector)a_partition_lteqs.clone();
					//now we have the ordering, now we need to see if we can
					//find a satisfying assignment
					//at this point, we need to add in the constraints added in
					//from our ordering...
					for (j = 0; j < order.length-1;j++){
						first_partition_element = (IPValue)q1_unique_vars.elementAt(((Integer)a_partitioning[j].elementAt(0)).intValue());
						partition_element_k = (IPValue)q1_unique_vars.elementAt(((Integer)a_partitioning[j+1].elementAt(0)).intValue());
						a_pred = new InterpretedPredicate();
						a_pred.setLHS(first_partition_element);
						a_pred.setRHS(partition_element_k);
						a_pred.setOperator("<=");
						an_ordering_lteqs.addElement(a_pred);
					}
					Inequality inequalities = new Inequality(q1_unique_vars,q1._constants);
					for (j = 0; j < a_partition_neqs.size(); j++){
						inequalities.addInequality(((InterpretedPredicate)a_partition_neqs.elementAt(j)).getLHS(),
												   ((InterpretedPredicate)a_partition_neqs.elementAt(j)).getRHS());
					}//end adding all of the inequalities
					//now make a new graph and add in all of the constaints there
					graph = new Graph();
					for(j = 0; j < q1._constants.size();j++){
						graph.addNode(((Double)q1._constants.elementAt(j)).doubleValue());
					}
					/*for(j = 0; j < q1_unique_vars.size();j++){
						graph.addNode((String)q1_unique_vars.elementAt(j));	
					}*/
					for (j = 0; j < an_ordering_lteqs.size(); j++){
						graph.addEdge((InterpretedPredicate)an_ordering_lteqs.elementAt(j));
					}//end looping over all of the lteqs
					//now we need to do the depth first search.
					graph.setInequalities(inequalities);
					if (graph.isSatisifiable()){
						//then we need to do the containment check
						graph.assignValues();
						if (!anOrderingContains(q1,graph)){
							return false;
						}
					}
					keep_going = a_perm.getNext();
				}//end of checking all permutations
				//at this point count has the number of partitions
			}//end of if we need to check the partition ordering
		}//end looping over each of the partitions == i
		return true;
	}

	public boolean anOrderingContains(StatementWIP R,Graph graph){
		Statement R_statement = R.getStatement();
		// Vector uniqueQVariables;
		// int varcounter;
		int i;
		// int match = 0;
		Predicate a_pred;
		// Statement ordered_original = this;
		// boolean flag = true;
		Statement q2 = new Statement();
		q2.setHead(new Predicate(R_statement.getHead()));
		//now we need to take into account all of the equalities that we've created
		//thanks to the partitioning.
		for (R_statement.first();!R_statement.isDone();R_statement.next()){
			a_pred = new Predicate(R_statement.currentSubgoal());
			//now replace all of the variables in apred.
			for (i = 0; i < a_pred.size();i++){
			    a_pred.replaceVariableI(graph.getEqualities().getRepresentative(a_pred.variableI(i)),i);
			    
			}
			q2.addSubgoal(a_pred);
		}//end of remapping all of the variables in the body of R.  Now do the same for the head
		a_pred = q2.getHead();
		for (i = 0; i < a_pred.size();i++){
			a_pred.replaceVariableI(graph.getEqualities().getRepresentative(a_pred.variableI(i)),i);
		}
		// Statement x;
		// uniqueQVariables = R_statement.getfindUniqueVariables();
		/*        for(R_statement.first();!R_statement.isDone();R_statement.next()){//looping over the statement
		apred = new Predicate(R_statement.currentSubgoal());
		flag = true;
		for (apred.first(),varcounter = 0;!apred.isDone();
		apred.next(),varcounter++){//now we are looping over the variables
		for(i = 0 , flag = false;
		i < uniqueQVariables.size() && flag==false ;
		i++){
		if (uniqueQVariables.elementAt(i).equals(apred.current())){
		flag = true;
		match = i;
		}
		}
		if (flag == false){ 
		//then we screwed up somewhere, this should never happen
		System.out.println("We failed to set the unique values properly in containment");
		return false;
		}
		
		apred.variables.setElementAt(Integer.toString(match),varcounter);
		}//end looping over the variables
		q2.addSubgoal(apred);
		}//end looping over the whole thing...
		//note, we still need to do the head; block copy from above
		//q2.print();           
		apred = q2.getHead();
		for (apred.first(),varcounter = 0;!apred.isDone();apred.next(),varcounter++){
		for(i = 0 , flag = false;
		i < uniqueQVariables.size() && flag==false; i++){
		if (uniqueQVariables.elementAt(i).equals(apred.current())){
		flag = true;
		match = i;
		}
		}
		if (flag == false){ 
		//then we screwed up somewhere, this should never happen
		System.out.println("We failed to set the unique values properly on the head");
		return false;
		}
		apred.variables.setElementAt(Integer.toString(match),varcounter);
		}
		x = new Statement(q2);
		//*to do* before this point we need to figure out what the constraints we
		//have map to, then pass them to answers to query.
		//damn, this may involve treating the stupid things like actual numbers
		//after all, no, I don't think that it has to...
		//need to figure out what the hell I'm doing better, though.
		*/       Statement answers = answersToQuery(q2,graph);
				 if(!answers.containsSubgoal(q2.getHead())){
					 return false;
				 }
				 
				 return true;
	}//end anOrderingContains

	public Statement answersToQuery(Statement tuples,Graph g){
		Statement this_statement = _statement;
        Vector an_assignment = new Vector(this_statement.size());
        Vector assignments = new Vector(this_statement.size());
        Vector temp  = new Vector(this_statement.size());
        Vector temp2;
        int i,j;
        Statement retval = new Statement();
        int variableposition;
        boolean flag,invalid;
        int location = 0;
        Mapping current_mapping;
        Vector unique_vars = this_statement.findUniqueVariables();
        int num_unique_vars = unique_vars.size();
        Predicate apred;
        Predicate atuple;
        IPValue this_var;
        for (i = 0; i < num_unique_vars; i++){
            an_assignment.addElement(new Mapping((IPValue)unique_vars.elementAt(i),new IPValue(" ")));
        }
        assignments.addElement(an_assignment);
        for (this_statement.first();!this_statement.isDone();this_statement.next()){
            //loop over each of the Predicates
            apred = this_statement.currentSubgoal();
            temp = new Vector(5);
            for (i = 0; i < assignments.size();i++){
                an_assignment = (Vector) assignments.elementAt(i);
                for (tuples.first();
                !tuples.isDone();tuples.next()){
                    atuple = tuples.currentSubgoal();
                    //loop over each assignment;
                    if (atuple.function.equals(apred.function)){
                        //at this point, we know that the functions
                        //are the same; now see if it's consistent
                        Vector newAssign = this_statement.copyAssignments(an_assignment);
                        for (invalid = false,apred.first(); invalid == false && !apred.isDone();apred.next()){
                      
                            this_var = apred.current();
                            variableposition = apred.getCurrentLocation();
                            //now find the mapping value...
                            for(j = 0, flag = false; 
                            flag == false && j < an_assignment.size();j++){
                                current_mapping = (Mapping)an_assignment.elementAt(j);
                                if (current_mapping.variable.equals(this_var)){
                                    if (current_mapping.mapping.equals(" ")){
                                        //then we've hit a blank, and should add it
                                        flag = true;
                                        ((Mapping)newAssign.elementAt(j)).mapping = atuple.variableI(variableposition);
                                    }
                                    else if (!current_mapping.mapping.equals(atuple.variableI(variableposition))){
                                        flag = true;
                                        invalid = true;
                                    }
                                }//end if current_mapping == this var
                            }//end of finding the mapping value
                        }//end for looking over this Predicate
                        //at this point, if all of the variables match, then we want to copy it
                        //over
                        if (invalid == false){
                            temp.addElement(newAssign);
                        }
                    }//end of if the function head is the same
                }//end of looping over tuples
            }//end of looping over assignments
            
            assignments = temp;
            
        }//end of looping over Predicates
        //now we have all the assignments; we want to 
        //1. make sure that we are only returning those for which there was a
        //complete mapping (meaning those where we really did cover everything)
        //2. only return the answers to queries
		//3. only return those mappings that correctly satisfy the interpreted predicates
        temp = new Vector(5);
        for (i = 0; i < assignments.size();i++){
            an_assignment = (Vector) assignments.elementAt(i);
            for (invalid = false, j = 0;
            j < an_assignment.size() && invalid == false;
            j++){
                //make sure there exists a mapping for everything
                if (((Mapping)an_assignment.elementAt(j)).mapping.equals(" ")){
                    invalid = true;
                }
            }
			//at this point we've finished looping over all of the variables to 
			//ensure that they've all been mapped; now we need to make sure that
			//all assignments meet the requirements set up by the interpreted 
			//predicates
			for(j = 0; !invalid  && j < _interpreted_predicates.size();j++){
				invalid = !satisfies(an_assignment,(InterpretedPredicate) _interpreted_predicates.elementAt(j));
			}
            if (!invalid){
                temp.addElement(an_assignment);
            }
        }
        assignments = new Vector();
        for (i = 0; i < temp.size();i++){
            //this part appears to be adding in mappings for all things in temp2
            //it should be able to benefit from the new version of predicate()
            temp2 = (Vector) temp.elementAt(i);
            apred = new Predicate(temp2.size());
            apred.setFunctionHead(new String (this_statement.head.getFunctionHead()));
            for (this_statement.head.first();!this_statement.head.isDone();this_statement.head.next()){
                //find the variable
                this_var = this_statement.head.current();
                for (j = 0, flag = false;
                j < temp2.size() && flag == false;
                j++){
                    if (((Mapping)temp2.elementAt(j)).variable.equals(this_var)){
                        location = j;
                        flag = true;
                    }
                }//end for going over temp
                if (flag == false){
                    System.out.println("Error: head variables must be included in the body");
                }
                else {
                    apred.addVariable(new IPValue (((Mapping)temp2.elementAt(location)).mapping));
                }
            }// end for each Predicate
            retval.addSubgoal(apred);
        }//end of each assignment
        //note, still need to check on the interpreted predicate constraints...     
        return retval;
    }//end answersToQuery
   
	public Vector getInterpretedPredicates(){
		return _interpreted_predicates;
	}

	protected boolean satisfies(Vector assignment, InterpretedPredicate an_ip){
		int i;
		double lhs_constant;
		double rhs_constant;
		String lhs_variable;
		String rhs_variable;
		boolean found;
		Mapping a_mapping;
		IPValue lhs = an_ip.getLHS();
		IPValue rhs = an_ip.getRHS();
		InterpretedPredicate copy = an_ip.copy();
		if (lhs.isVariable()){
			lhs_variable = lhs.getVariable();
			for (i = 0, found = false; i < assignment.size() && !found; i++){
				if (((Mapping)assignment.elementAt(i)).variable.equals(lhs_variable)){
					lhs.setNumericalConstant(((Mapping)assignment.elementAt(i)).mapping.getNumericalConstant());
					found = true;
				}
			}
		}//end finding the value for lhs
		if (rhs.isVariable()){
			rhs_variable = rhs.getVariable();
			for (i = 0, found = false; i < assignment.size() && !found; i++){
				if (((Mapping)assignment.elementAt(i)).variable.equals(rhs_variable)){
					rhs.setNumericalConstant(((Mapping)assignment.elementAt(i)).mapping.getNumericalConstant());
					found = true;
				}
			}
		}//end finding the value for rhs
		return an_ip.Evaluate();

	}//end satisfies

	public boolean variableIsDistinguished(IPValue variable){
		return _statement.variableIsDistinguished(variable);
	}
	
	public void read(String in){
		int an_index;
		String first_half;
		String last_half;
		String an_ip_string;
		String remaining;
		an_index = in.indexOf(";");
		if (an_index < 0){
			//then there were no interpreted predicates, just read in the string
		    _statement = new Statement(in);
		}
		else{
			//there were interpretedpredicates
			//I assume that there will always be something in front of the 
			//interpreted predicates, or it doesn't make sense
			first_half = in.substring(0,an_index);
			last_half = in.substring(an_index + 1);
			first_half = first_half.trim();
			remaining = last_half.trim();
			_statement = new Statement(first_half);
			//now we've taken care of the statement, let's take care of the interpreted
			//predicates

			an_index = remaining.indexOf(",");
			while (an_index > -1){
				an_ip_string = remaining.substring(0,an_index);
				remaining = remaining.substring(an_index +1);
				an_ip_string = an_ip_string.trim();
				_interpreted_predicates.addElement(new InterpretedPredicate(an_ip_string));
				an_index = remaining.indexOf(",");
			}//end while we are looping over all of the rest of the predicates
			//we need to get the last one now
			remaining = remaining.trim();
			_interpreted_predicates.addElement(new InterpretedPredicate(remaining));
		}
		//at this point, we've read them all in, now calculate the constants
		calculateConstants();
	}
	
	public boolean calculateConstants(){
		/**This function calculates the constants that are in the interpretedpredicates
		*/	
		_constants = new Vector(10);
		int i;
		int _num_ips = _interpreted_predicates.size();
		InterpretedPredicate an_ip;
		for (i = 0; i < _num_ips; i++){
			an_ip = (InterpretedPredicate) _interpreted_predicates.elementAt(i);
			if (an_ip.getLHS().isNumericalConstant()){
				_constants.addElement(new Double(an_ip.getLHS().getNumericalConstant()));
			}
			if (an_ip.getRHS().isNumericalConstant()){
				_constants.addElement(new Double(an_ip.getRHS().getNumericalConstant()));
			}
		}//end looping over the values
		return true;
	}
	public boolean isDone(){
		return _statement.isDone();
	}
	
	public void next(){
		_statement.next();
	}
	
	public void first(){
		_statement.first();
	}
	
	public boolean addAllSubgoals(Statement a_state){
		return _statement.addAllSubgoals(a_state);
	}

	public boolean addAllSubgoalsAndIPs(StatementWIP a_state){
	    _statement.addAllSubgoals(a_state._statement);
	    int i;
	    int num_ips_to_add = a_state._interpreted_predicates.size();
	    
	    for (i = 0; i < num_ips_to_add;i++){
		   _interpreted_predicates.addElement(a_state._interpreted_predicates.elementAt(i));
	    }
	    return true;
	    
	}
	
	public void setHead(Predicate a_pred){
		_statement.setHead(a_pred);
	}
	
	public Predicate current(){
		return _statement.current();
	}
	
	
	public Predicate subgoalI(int i){
		return _statement.subgoalI(i);
	}
	
	public static void main(String args[]){
		StatementWIP one;
		one = new StatementWIP();
		one.read("q(a):-e1(\"a\",b); \"a\" < b, '1' < '2' ");
		one.addConstant(5);
		one.addConstant(4);
		one.addConstant(3);
		one.addConstant(6);
		one.addConstant(5);
		one.sortConstants();
		one.print();
		
		

		/*StatementWIP container = new StatementWIP();
		StatementWIP contained = new StatementWIP();
		Statement a_statement = new Statement();
		a_statement.read("p(X) :- q(X,Y,Z),q(X,Z,Y),q(Y,Z,X)");
		contained.setStatement(a_statement);
		a_statement = new Statement();
		a_statement.read("p(X):-q(X,Y,Z)");
		container.setStatement(a_statement);
		InterpretedPredicate a_pred = new InterpretedPredicate(new IPValue("X"),
															   "<=",new IPValue("Z"));
		container.addInterpretedPredicate(a_pred);
		a_pred = new InterpretedPredicate(new IPValue("Y"), "<=",new IPValue("Z"));
		container.addInterpretedPredicate(a_pred);
		boolean outcome;
		outcome = contained.contains(container);
		if (outcome == true){
			System.out.println("container contains containee");
		}
		else {
			System.out.println("container doesn't contain containee");
		}
		*/
	}//end main
		
}

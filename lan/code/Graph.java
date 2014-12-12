package minicon;
import java.util.Vector;

public class Graph
{
	
	/*in order to fix this class, we'd need to change it so that in
	 *order to check to see if something is less than something else,
	 *we have to first find the path that says that it's less than or
	 *equal to it, and then change check all elements on that path
	 *(and their equivalents) to see if there's a not equals
	 *somewhere the purpose of this class is to keep track of the
	 *inequalities that come in when there are interpreted
	 *predicates.  the idea is something like this: I keep a matrix
	 *of inequalities.  each axis has both the variables and
	 *constants in my problem. I put an x in each of the slots if
	 *they are not equal to each other for the inequalities, i put a
	 *new link from x to y in if there is a lteq from x to y.  then,
	 *when i'm done constructing this graph, i collapse all cycles,
	 *and if there are no neqs in any cycle, then there is a solution
	 *to the problem*/

	private Vector _nodes;
	private Vector _root_nodes;
	private Inequality _inequalities;
	private Vector _variables;
	private Vector _constants;
	private IPEquality _var_equalities;
	public static double _min_value = -1000000000;
	public Graph(){
		_nodes = new Vector();
		_root_nodes = new Vector();
		_inequalities = null;
		_constants = null;
		_var_equalities = new IPEquality();
	}
	
	public IPEquality getEqualities(){
		return _var_equalities;
	}
	
	public void addNode(String value){
		Node a_node = new  Node(value);
		_nodes.addElement(a_node);
		_root_nodes.addElement(a_node);
	}
	
	public void addNode(double value){
		Node a_node = new  Node(value);
		_nodes.addElement(a_node);
		_root_nodes.addElement(a_node);
	}
			
	public void addNode(Node node){
		_nodes.addElement(node);
		_root_nodes.addElement(node);
	}
	
	public Node findNode(String value){
		/**Returns the node that matches the value
		 * returns null if no such node exists
		 */
		int i;
		Node a_node;
		for(i = 0; i < _nodes.size();i++){
			a_node = (Node)_nodes.elementAt(i);
			if (a_node.getValues().isVariable() && a_node.getVariable().equals(value)){
				return (Node) _nodes.elementAt(i);
			}
		}
		return null;
	}//end findNode
	
	public Node findNode(double value){
		/**Returns the node that matches the value
		 * returns null if no such node exists
		 */
		int i;
		Node a_node;
		for(i = 0; i < _nodes.size();i++){
			a_node = (Node)_nodes.elementAt(i);
			if (a_node.getValues().isNumericalConstant() && a_node.getNumericalConstant()==value){
				return (Node) _nodes.elementAt(i);
			}
		}
		return null;
	}//end findNode
	
	public Node findNode(IPValue value){
		if (value.isNumericalConstant()){
			return findNode(value.getNumericalConstant());
		}
		else{
			return findNode(value.getVariable());
		}
	}
	
	public boolean addEdge(String from, String to){
		/**Adds an edge from the node for from, to the node for to.
		 * If a node for one or the other does not exist, it is added
		 */
		Node from_node= null;
		Node to_node = null;
		from_node = findNode(from);
		to_node = findNode(to);
		if (from_node == null){
			from_node = new Node(from);
			addNode(from_node);
		}
		if (to_node == null){
			to_node = new Node(to);
			addNode(to_node);
		}
		from_node.addEdge(to_node);
		//also, if it's there, remove the to node from the list of roots
		removeRootNode(to_node);
		return true;	
	}//end addEdge(String, String)
	
	public boolean addEdge(double from, String to){
		/**Adds an edge from the node for from, to the node for to.
		 * Returns creates a new node if one exists
		 */
		Node from_node= null;
		Node to_node = null;
		from_node = findNode(from);
		to_node = findNode(to);
		if (from_node == null){
			from_node = new Node(from);
			addNode(from_node);
		}
		if (to_node == null){
			to_node = new Node(to);
			addNode(to_node);
		}
		from_node.addEdge(to_node);
		//also, if it's there, remove the to node from the list of roots
		removeRootNode(to_node);
		return true;	
	}//end addEdge(double, String)
	
	private void deleteNode(Node to_delete, Node move_edges_to){
		int i;
		int num_edges = to_delete.numEdges();
		int num_nodes = _nodes.size();
		Node a_node;
		for (i = 0; i < num_edges; i++){
			//note, we don't want to do this if the edge is the same as the current node,
			//or things will get all f*cked up.
			a_node = to_delete.edgeI(i);
			//if (a_node.getValues() != move_edges_to.getValues()){
				
				move_edges_to.addEdge(a_node);
			//}
		}
		//we've moved over all of the links, now all we need to do is change all links
		//from the old node to the new one
		//for (i = 0; i < num_nodes; i++){
		changeEdgesTo(to_delete,move_edges_to);	
		to_delete.setToBeDeleted(true);
	}
	
	private void deleteNodesToBeDeleted(){
		int i,j;
		Node a_node;
		Vector new_nodes = new Vector(_nodes.size());
		for (i = 0; i < _nodes.size(); i++){
			a_node = (Node)_nodes.elementAt(i);
			if (!a_node.isToBeDeleted()){
				new_nodes.addElement(a_node);
				//now make sure that we have no links to ourselves
				//for (j = 0; j < a_node.num
				//a_node.setToBeDeleted(false);
			}
		}
		_nodes = new_nodes;
	}//end deleteNodesToBeDeleted
	
	public boolean addEdge(String from, double to){
		/**Adds an edge from the node for from, to the node for to.
		 * Returns false if a node for from or to doesn't exist.
		 */
		Node from_node= null;
		Node to_node = null;
		from_node = findNode(from);
		to_node = findNode(to);
		if (from_node == null){
			from_node = new Node(from);
			addNode(from_node);
		}
		if (to_node == null){
			to_node = new Node(to);
			addNode(to_node);
		}
		from_node.addEdge(to_node);
		//also, if it's there, remove the to node from the list of roots
		removeRootNode(to_node);
		return true;	
	}//end addEdge(String, double)
		
	public boolean addEdge(InterpretedPredicate an_ip){
		/**
		 */
		if (an_ip.getOperator().equals("<=")){
			return addEdge(an_ip.getRHS(), an_ip.getLHS());
		}
		else if (an_ip.getOperator().equals(">=")){
			return addEdge(an_ip.getLHS(),an_ip.getRHS());
		}
		else{
			System.out.println("can't add an edge with operator " + an_ip.getOperator() + " in Graph.addEdge()");
			return false;
		}
	}
	
	public boolean addEdge(IPValue from, IPValue to){
		boolean from_is_constant = from.isNumericalConstant();
		boolean to_is_constant = to.isNumericalConstant();
		if (from_is_constant && to_is_constant){
			return addEdge(from.getNumericalConstant(), to.getNumericalConstant());
		}
		else if (from_is_constant){
			return addEdge(from.getNumericalConstant(),to.getVariable());
		}
		else if (to_is_constant){
			return addEdge(from.getVariable(),to.getNumericalConstant());
		}
		else{
			return addEdge(from.getVariable(),to.getVariable());
		}
	}
	
	public boolean addEdge(double from, double to){
		/**Adds an edge from the node for from, to the node for to.
		 * Returns false if a node for from or two doesn't exist.
		 */
		Node from_node= null;
		Node to_node = null;
		from_node = findNode(from);
		to_node = findNode(to);
		if (from_node == null){
			from_node = new Node(from);
			addNode(from_node);
		}
		if (to_node == null){
			to_node = new Node(to);
			addNode(to_node);
		}
		from_node.addEdge(to_node);
		//also, if it's there, remove the to node from the list of roots
		removeRootNode(to_node);
		return true;	
	}//end addEdge(double, double)

	public boolean removeRootNode(Node to_remove){
		/**Removes node from list of root nodes.
		 * returns false if it wasn't there, true else
		 */
		int i;
		for (i = 0; i < _root_nodes.size(); i++){
			if (_root_nodes.elementAt(i).equals(to_remove)){
				_root_nodes.removeElementAt(i);
				return true;
			}
		}//end for
		return false;
	}//end removeNode
	
	public boolean collapseCycle(Node cycle,Vector path,Node root){
		/**Collapses the cycle started at by cycle.
		 * path is measured from root
		 * returns false if it cannot be collapsed.
		 */
		//*doh! we need to get rid of all of the pointers to the variable we're
		//trying to get rid of...
		//silly me.
		Node traveller;
		Node top = root;
		Vector vars = new Vector();
		Node to_delete;
		boolean retval;
		int path_location;
		int i,j;
		//first, we need to move top down the list until we get to cycle...
		//then we need have path_location set correctly
		for (path_location = 0; top != cycle; path_location++){
			top = top.edgeI(((Integer)path.elementAt(path_location)).intValue());
		}
		vars.addElement(top);
		for (i = path_location; i < path.size()-1;i++){
			traveller = top;
			for(j = i; j < path.size()-1; j++){
				traveller = traveller.edgeI(((Integer)path.elementAt(j)).intValue());
				if (_inequalities.inequalityExists(top.getValues(),traveller.getValues())){
					return false;
				}
			}
			top = top.edgeI(((Integer)path.elementAt(i)).intValue());
			changeEdgesTo(top,cycle);
			vars.addElement(top);
		}//end outer for
		//at this point, we know that they're equivalent, so add them to the equality
		for (i = 0; i < vars.size() -1; i++){
			to_delete = (Node)vars.elementAt(i);
			retval =_var_equalities.addEquality(to_delete.getValues(),((Node)vars.elementAt(i+1)).getValues());
			if (retval == false){
				return false;
			}
		}
		for (i = 1; i < vars.size(); i++){
			to_delete = (Node)vars.elementAt(i);
			deleteNode(to_delete,top);
		}
		//now we need to make them all into the same node...
		//now let's actually delete the node
		deleteNodesToBeDeleted();
		return true;
	}
	
	private void changeEdgesTo(Node from, Node to){
		int i;
		Node a_node;
		for(i = 0; i < _nodes.size();i++){
			a_node = (Node)_nodes.elementAt(i);
			if (a_node != to){
				a_node.switchEdge(from,to);
			}
			else{
				a_node.deleteEdge(from);
			}
				
		}//end looping over all of the nodes
	}
	
	public Inequality getInequalities(){
		return _inequalities;
	}
	
	public void setInequalities(Inequality inequals){
		_inequalities = inequals;
	}
	
	public boolean isSatisifiable(){
		int i;
		Node a_node;
		int returned;
		for (i = 0; i < _nodes.size(); i++){
			//next = new Vector();
			//next.addElement(new Integer(i));
			a_node = (Node)_nodes.elementAt(i);
			if (!a_node.isToBeDeleted()){
				returned = depthFirstSearch(a_node,new Vector(),a_node);
				if (returned == 0){
					return false;
				}
				else if (returned > 1){
					deleteNodesToBeDeleted();
					recalculateRootNodes();
					clearMarks();
					i = -1;
				}
			}
			//clearMarks();
		}
		//at this point, we've done all that; now we need to check and recalculate root nodes
		//deleteNodesToBeDeleted();
		//recalculateRootNodes();
		return true;
	}
	
	private void recalculateRootNodes(){
		int i,j;
		boolean flag;
		int num_nodes = _nodes.size();
		Node a_node;
		_root_nodes = new Vector();
		for(i = 0; i < num_nodes; i++){
			a_node = (Node)_nodes.elementAt(i);
			for (j = 0, flag = false; !flag && j < num_nodes; j++){
				if (j != i){
					//it's okay for something to point to itself.
					if (((Node)_nodes.elementAt(j)).hasEdgeTo(a_node)){
						flag = true;
					}
				}
			}
			if (!flag){
				_root_nodes.addElement(a_node);
			}
		}
	}//end recalculateRootNodes
	
	private void clearMarks(){
		/**This function sets all of the marks in the graph to false
		 */
		for (int i = 0; i < _nodes.size(); i++){
			((Node)_nodes.elementAt(i)).setMarked(false);
		}
	}								 
		
	public boolean isImplied(InterpretedPredicate an_ip){
	/**returns true if we can include that the interpreted predicate listed in
	 * an_ip is implied, false else.
	 */	
		InterpretedPredicate [] new_ip = an_ip.translateIP();
		//now we have the lteq and the neq
		if (new_ip[1] != null && !_inequalities.inequalityExists(new_ip[1].getLHS(), new_ip[1].getRHS())){
			return false;
		}
		if (new_ip[0] != null && !isLessThan(new_ip[0].getLHS(),new_ip[0].getRHS())){
			return false;
		}
		return true;
	}
	
	public boolean isLessThan(IPValue lhs, IPValue rhs)
	{
		//damn, need to check the representatives as well.
		Vector lhs_equalities = _var_equalities.getEqualities(lhs);//note, if there was no equality, this will be null!!
		Vector rhs_equalities = _var_equalities.getEqualities(rhs);//"
		int lhs_size = 0;
		if (lhs_equalities != null){
			lhs_size = lhs_equalities.size();
		}
		int rhs_size = 0;
		if (rhs_equalities != null){
			rhs_size = rhs_equalities.size(); 
		}
		Node lhs_node = findNode(lhs);//thisvalue
		Node rhs_node = findNode(rhs);
		Double a_double;
		int i;
		for (i = 1; i < lhs_size && lhs_node == null; i++){
			//loop over all of the equivalent values
			//note that we don't want to check for the 
			//first value yet because it's a special case, but we need to do it eventaully
			lhs_node = findNode((String)lhs_equalities.elementAt(i));
		}
		//now check the special case of the double value, if necessary, being sure
		//to check it only if the value is a certain value, not just a guess.
		if (lhs_equalities == null){
			lhs_equalities = new Vector();
			lhs_equalities.addElement(null);
			lhs_equalities.addElement(lhs_node);
		}

		if (lhs_node == null){
			//then we need to check the double value
			a_double = (Double)lhs_equalities.elementAt(0);
			if (a_double == null){
				return false;
				//then there was no value, give up.
			}
			lhs_node = findNode(a_double.doubleValue());
			if (lhs_node == null){
				return false;
			}
		}
		//end of getting the values for the left hand side, now get the value for the right
		//hand side.
								
		
		for (i = 1; i < rhs_size && rhs_node == null; i++){
			//loop over all of the equivalent values
			//note that we don't want to check for the 
			//first value yet because it's a special case, but we need to do it eventaully
			rhs_node = findNode((String)rhs_equalities.elementAt(i));
		}
		if (rhs_equalities == null){
			rhs_equalities = new Vector();
			rhs_equalities.addElement(null);
			rhs_equalities.addElement(rhs_node);
		}

		if (rhs_node == null){
			//then we need to check the double value
			a_double = (Double)rhs_equalities.elementAt(0);
			if (a_double == null){
				return false;
				//then there was no value, give up.
			}
			rhs_node = findNode(a_double.doubleValue());
			if (rhs_node == null){
				return false;
			}
		}
		
		if (lhs_node == rhs_node){
			return true;
		}
		return searchLessThan(rhs_node,lhs_node, lhs_equalities);
	}
	
	//private boolean equalityVectorContains(
	
	private boolean searchLessThan(Node root, Node goal,Vector goal_equalities){
	/**returns true if we can reach the goal from the root.  Done recursively
	 */	
		int i;
		Node an_edge;
		for (i = 0; i < root.numEdges(); i++){
			an_edge = root.edgeI(i);
			if (an_edge != root){
				//no sense in looping over yourself.
				if (goal_equalities.contains(an_edge)){
					//check to make sure that the goal is not an immediate child.
					return true;
				}
				//here!!!
				if (searchLessThan(an_edge,goal,goal_equalities)){
					return true;
				}
			}
		}
		//at this point, we know that we can't derive it, so return false;
		return false;
	}
	
	public boolean assignValues(){
		int i;
		Node a_node;
		Double top_val;
		double pass_top;
		for (i = 0; i < _root_nodes.size();i++){
			a_node = (Node)_root_nodes.elementAt(i);
			/*if (a_node.getValues().isVariable()){
				top_val = _var_equalities.getRepresentative(a_node.getVariable());
				if (top_val== null){
					//then there was no value, assign the maximum number we can...
					pass_top = Double.MAX_VALUE;
				}
				else {
					pass_top = top_val.doubleValue();
				}
				_var_equalities.addEquality(a_node.getVariable(),pass_top);
			}
			else {
				pass_top = a_node.getNumericalConstant();
			}
			*/
			assignRecursiveValues(a_node,Double.MAX_VALUE);
		}
		return true;
	}//end assignValues
	
	public double assignRecursiveValues(Node root, double top_val){
		int i;
		Node traveller;
		int num_to_assign;
		double next_top;
		int num_to_bottom;
		int num_edges = root.numEdges();
		double max_child_val = _min_value;
		IPValue aval = _var_equalities.getRepresentative(root.getValues());
		double curr_val = 0;
		
		//		 _var_equalities.getRepresentative(root.getValues()).getNumericalConstant();
		if(aval!= null){
		    
		     curr_val = aval.getNumericalConstant();
		}
		
		//		if (curr_val != null && curr_val < top_val){
		if (aval != null && curr_val < top_val){
		    next_top = curr_val;
		    
			if (num_edges == 0){
				return next_top;
			}
			for (i = 0; i < num_edges; i++){
				if (root.edgeI(i)!=root){
					assignRecursiveValues(root.edgeI(i),next_top);
				}	
			}//end looping over children
		}//that's if the value was null.... otherwise we need to figure it out
		//from what's below
		else{
			//we need to get the value from below
			for (i = 0; i < num_edges; i++){
				if(root.edgeI(i) !=root){
					next_top = assignRecursiveValues(root.edgeI(i),top_val);
					if (next_top > max_child_val){
						max_child_val = next_top;
					}
				}
			}//end looping over the values.  Now we know that this value
			//needs to be less than the one we assign here, so assign the
			//middle point between here and the top
			next_top = (max_child_val + top_val) / 2.0;
			_var_equalities.addEqualityPossibility(root.getVariable(),next_top);
		}
		return next_top;	
	}//end assignRecursiveValues
	
	public int depthFirstSearch(Node root,Vector path, Node original_root){
		/**Conducts a depth first search from this node.
		/* Detects a loop by marking all nodes and then unmarking when
		 * done
		 * Note, this is a destructive function, because it will change the tree
		 * */
		//now this is messy, but it will work.
		//it returns 1 if everything was okay.  
		//it returns 2 or more if it was okay but found a cycle.
		//it returns 0 if not possible
		int i;
		Vector next_path;
		Node next_node;
		int retval = 1;
		boolean collapse_val;
		int num_edges = root.numEdges();
		if (root.isMarked()){
			//then we've detected a cycle; *do something*
			if (collapseCycle(root,path,original_root) == false){
				return 0;
			}
			return 2;
		}
		root.setMarked(true);
		for (i = 0; i < num_edges;i++){
			next_path = (Vector)path.clone();
			next_path.addElement(new Integer(i));
			next_node = root.edgeI(i);
			if (next_node != root){
				//we don't need to check ourselves again.
				retval = depthFirstSearch(root.edgeI(i),next_path,original_root) * retval;
				if (retval != 1){
					return retval;													 
				}
			}
		}
		root.setMarked(false);
		return 1;
	}//end depth firstsearch
	
	
	public boolean addInterpretedPredicate(InterpretedPredicate an_ip){
		InterpretedPredicate new_ips[];
		
		//now we've added the interpreted predicate to the final answer for
		//the output; now add it to the lteq's for checking the satisfaction
		new_ips = an_ip.translateIP();
		if (new_ips[0] != null){
			//then there's something with a <=
			addEdge(new_ips[0]);
		}
		
		if (new_ips[1] != null){
			//then there's something with a !=
			_inequalities.addInequality(new_ips[1]);
		}
		//now we need to add all of the inequalities that we can derive; if 
		//we have x < 4, and there is constant of 5, we need to add the constrant
		//that x != 5 as well.
		
		return true;
	}//end addInterpretedPredicate
		
	
	private boolean testLessThan(){
		Graph graph = new Graph();
		Vector constants = new Vector();
		//constants.addElement(new Double(2));
		//constants.addElement(new Double(1));
		Vector variables = new Vector();
		variables.addElement("a");
		variables.addElement("b");
		Inequality in = new Inequality(variables,constants);
		//in.addInequality(new IPValue("a"), new IPValue("b"));
		InterpretedPredicate a = new InterpretedPredicate("a","<=","b");
		InterpretedPredicate b = new InterpretedPredicate("b","<=","a");
		InterpretedPredicate c = new InterpretedPredicate("c", "<=", "b");
		InterpretedPredicate d = new InterpretedPredicate("a","<=","d");
		InterpretedPredicate e = new InterpretedPredicate(new IPValue("a"),"<=",new IPValue("c"));
		InterpretedPredicate f = new InterpretedPredicate("c", "<=","d");
		graph.addEdge(a);
		graph.addEdge(b);
		graph.addEdge(c);
		graph.addEdge(d);
		//graph.addEdge(e);
		//graph.addEdge(f);
		graph.setInequalities(in);
		if (!graph.isSatisifiable()){
			return false;
		}

		if (graph.isImplied(f)){
			System.out.println("was implied");
		}
		else {
			System.out.println("was not implied");
		}
		//System.out.println(graph.isSatisifiable());
		//System.out.println(graph.assignValues());
		//System.out.println(new Double(Double.NEGATIVE_INFINITY).toString());
		//System.out.println(graph.getEqualities().getRepresentative("a").toString());
		System.out.println("done");
		return true;
	}

	private boolean testIsSatisfiable(){
		Graph graph = new Graph();
		Vector constants = new Vector();
		//constants.addElement(new Double(2));
		//constants.addElement(new Double(1));
		Vector variables = new Vector();
		variables.addElement("a");
		variables.addElement("b");
		Inequality in = new Inequality(variables,constants);
		InterpretedPredicate a = new InterpretedPredicate("a","<=",2);
		InterpretedPredicate b = new InterpretedPredicate(2,"<=","b");
		InterpretedPredicate c = new InterpretedPredicate("b","<=",1);
		InterpretedPredicate d = new InterpretedPredicate(1,"<=","a");
		InterpretedPredicate e = new InterpretedPredicate(new IPValue("a"),"<=",new IPValue("c"));
		InterpretedPredicate f = new InterpretedPredicate("c", "<=","b");
		graph.addEdge(a);
		graph.addEdge(b);
		graph.addEdge(c);
		graph.addEdge(d);
		//graph.addEdge(e);
		//graph.addEdge(f);
		graph.setInequalities(in);
		System.out.println(graph.isSatisifiable());
		//System.out.println(graph.assignValues());
		//System.out.println(new Double(Double.NEGATIVE_INFINITY).toString());
		//System.out.println(graph.getEqualities().getRepresentative("a").toString());
		System.out.println("done");
		return true;
	}//end testIsSatisfiable
	
	public static void main(String args[]){
		Graph graph = new Graph();
		graph.testLessThan();
	}//end main
}//end Graph


package minicon;
import java.util.Vector;

public class Node
{
	private IPValue _value;
	private Vector _edge_to;
	private boolean _marked;
	private boolean _to_be_deleted;
	public Node(){
		_edge_to = new Vector();
		_marked = false;
		_value = null;
		_to_be_deleted = false;
	}
	
	public Node(String value){
		_value = new IPValue(value);
		_marked = false;
		_edge_to = new Vector();
		_to_be_deleted = false;
	}
	
	public Node(double value){
		_value = new IPValue(value);
		_marked = false;
		_edge_to = new Vector();
		_to_be_deleted = false;
	}
	
	public void addEdge(Node to){
		_edge_to.addElement(to);
	}
	
	public IPValue getValues(){
		return _value;
	}
	
	public boolean isMarked(){
		return _marked;
	}
	
	public void setMarked(boolean value){
		_marked = value;
	}
	
	public void setToBeDeleted(boolean value){
		_to_be_deleted = value;
	}
	
	public boolean isToBeDeleted(){
		return _to_be_deleted;
	}
	
	public void setValue(String value){
		_value.setValue(value);
	}
	
	public void setValue(double constant){
		_value.setValue(constant);
	}
	
	public String getVariable(){
		return _value.getVariable();
	}
	
	public double getNumericalConstant(){
		return _value.getNumericalConstant();
	}
	
	
	public boolean hasEdgeTo(Node to){
		return _edge_to.contains(to);
	}
	
	
	public int numEdges(){
		return _edge_to.size();
	}
	
	public boolean hasEdgeToValue(String to){
		int i;
		for (i = 0; i < _edge_to.size();i++){
			if (((Node)_edge_to.elementAt(i)).getVariable().equals(to)){
				return true;
			}
		}//if we haven't seen it at this point, it wasn't there, so return false
		return false;
	}//end hasEdgeToValue

	public boolean hasEdgeToValue(double to){
		int i;
		for (i = 0; i < _edge_to.size();i++){
			if (((Node)_edge_to.elementAt(i)).getNumericalConstant()==to){
				return true;
			}
		}//if we haven't seen it at this point, it wasn't there, so return false
		return false;
	}//end hasEdgeToValue
	
	
	public boolean switchEdge(Node old_to, Node new_to){
		int i;
		boolean retval = false;
		for (i = 0; i < _edge_to.size(); i++){
			if (_edge_to.elementAt(i) == old_to){
				_edge_to.setElementAt(new_to,i);
				retval = true;
			}
		}
		return retval;
	}//end switchEdge
	
	public boolean deleteEdge(Node to){
		/**Deletes all edges to the node "to"
		 */
		int i;
		for (i = 0; i < _edge_to.size();i++){
			if (_edge_to.elementAt(i) == to){
				//then we want to delete it
				_edge_to.removeElementAt(i);
				i--;
			}
		}
		return true;
	}//end deleteEdge			
		
	
	public Node edgeI(int i){
		return (Node)_edge_to.elementAt(i);	
	}
	
}

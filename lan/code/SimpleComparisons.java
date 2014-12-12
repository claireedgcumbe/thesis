package minicon;
import java.util.Vector;
public class SimpleComparisons
{
	protected VariableExtents[] _vars;
	protected int _num_vars;
	protected boolean _is_valid;
	public SimpleComparisons(Vector vars){
		int i;
		_num_vars = vars.size();
		_vars = new VariableExtents[_num_vars];
		for (i = 0; i < _num_vars; i++){
			_vars[i] = new VariableExtents(((InterpretedPredicate)vars.elementAt(i)).getLHSVariable());
		}
		_is_valid= true;
	}//end constructor
	
	
	public boolean isSatisfiable(){
		return _is_valid;
	}
	
	
	public boolean addInterpretedPredicate(InterpretedPredicate a_val){
		/**note we assume that the lhs is a variable, the right a constant
		*/
		if (_is_valid == false){
			return false;
		}
		String lhs = a_val.getLHS().getVariable();
		int i;
		for (i = 0; i < _num_vars; i++){
			if (_vars[i]._var.equals(a_val.getLHSVariable())){
				if (_vars[i].addRestriction(a_val.getOperator(),a_val.getRHSConstant())){
					//then it was okay
					return true;
				}
				//otherwise, we know it wasn't valid so change is_valid, and
				//return false
				_is_valid = false;
				return false;
			}
		}//end looping over the variables
		//if we ever get here, then we know that we have an invalid system 
		//namely, it's not a safe query, so set _is_valid and return false
		_is_valid = false;
		return false;
	}
	
	public boolean isImplied(InterpretedPredicate a_val){
		/**note, we assume that lhs is a variable and the right is a constant
		*/
		String lhs = a_val.getLHSVariable();
		int i;
		if (_is_valid == false){
			return false;
		}
		for (i = 0; i < _num_vars; i++){
			if (_vars[i].addRestriction(a_val.getOperator(),a_val.getLHSConstant())){
				//then it was okay
				return true;
			}
			//otherwise, we know it wasn't valid so change is_valid, and
			//return false
			_is_valid = false;
			return false;
		}//end looping over the variables
		//if we ever get here, then we know that we have an invalid system 
		//namely, it's not a safe query, so set _is_valid and return false
		_is_valid = false;
		return false;
	}//end isImplied
			
}

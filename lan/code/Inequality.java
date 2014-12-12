package minicon;
import java.util.Vector;
public class Inequality
{
	/**This class keeps track of all of the inequalities.
	 * Currently it is implemented as a matrix, but that could change
	 * 
	 */
	private boolean[][] _matrix;
	private int _size;
	private int _number_of_variables;
	private int _number_of_constants;
	private IPValue[] _variables;
	private double[] _constants;
	public Inequality(Vector variables, Vector constants){
		int i, j;
		_number_of_variables = variables.size();
		_number_of_constants = constants.size();
		_size = _number_of_variables + _number_of_constants;
		_variables = new IPValue[_number_of_variables];
		_constants = new double[_number_of_constants];
		_matrix = new boolean[_size][_size];
		for (i = 0; i < _size; i++){
			for (j = 0; j < _size; j++){
			_matrix[i][j] = false;
			}
		}
		for (i = 0; i < _number_of_variables; i++){
			_variables[i] = (IPValue)variables.elementAt(i);
			
		}
		for (i = 0; i < _number_of_constants; i++){
			_constants[i] = ((Double) constants.elementAt(i)).doubleValue();
		}
		//now mark that we know that the constants are distinct
		for (i = _number_of_variables; i < _size; i++){
			for (j = _number_of_variables; j < _size; j++){
				if (i !=j){
					_matrix[i][j] = true;
				}
			}
		}
	}//end constructor
	
	
	private int findLocation(IPValue value){
		if (value.isNumericalConstant()){
			return findConstantLocation(value.getNumericalConstant());
		}
		else{
			//it's a variable
			return findVariableLocation(value);
		}
	}
	
	private int findVariableLocation(IPValue variable){
		int i;
		for (i = 0; i < _number_of_variables; i++){
			if (_variables[i].equals(variable)){
				return i;
			}
		}
		return -1;
	}
	
	
	private int findConstantLocation(double constant){
		int i;
		for (i = 0; i < _number_of_constants; i++){
			if(_constants[i]== constant){
				return i + _number_of_variables;
			}
		}
		return -1;
	}
	
	public boolean addInequality(InterpretedPredicate a_pred){
		if (!(a_pred.getOperator().equals("!="))){
			return false;
		}
		return addInequality(a_pred.getLHS(),a_pred.getRHS());
	}
	
	
	public boolean addInequality(IPValue lhs, IPValue rhs){
		//first, check to see if they're both constants; if so
		//make sure that they're not equal and return true; else return false
		int lhs_location;
		int rhs_location;
		if (lhs.isNumericalConstant() && rhs.isNumericalConstant()){
			return (lhs.getNumericalConstant() != rhs.getNumericalConstant());
		}
		else{
			lhs_location = findLocation(lhs);
			rhs_location = findLocation(rhs);
			if (lhs_location == -1 || rhs_location == -1 || lhs_location == rhs_location){
				return false;
			}
			else{
				_matrix[lhs_location][rhs_location] = true;
				_matrix[rhs_location][lhs_location] = true;
			}
			return true;
		}//end if they aren't both constants
	}//end addInequality
	
	
	
	public boolean inequalityExists(IPValue lhs, IPValue rhs){
		int i;
		int lhs_slot = -1;
		int rhs_slot = -1;
		if (lhs.isNumericalConstant() && rhs.isNumericalConstant()){
			return lhs.getNumericalConstant() != rhs.getNumericalConstant();
		}
		else{
			lhs_slot = findLocation(lhs);
			rhs_slot = findLocation(rhs);
			if (lhs_slot == -1 || rhs_slot == -1){
				//then there can't possibly be an inequality, because they
				//aren't both in our matrix.
				//we may wish to change the implementation of this in the future,
				//but it's better than nothing.
				return false;
			}
			return _matrix[lhs_slot][rhs_slot];
		}
	}//end inequalityExists
}

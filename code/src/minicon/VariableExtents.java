package minicon;
import java.util.Vector;

public class VariableExtents
{
	public String _var;
	public double _lteq;
	public double _gteq;
	public boolean _strictly_less_than;
	public boolean _strictly_greater_than;
	protected Vector _not_equals;
	public static double _min_value = -1000000000;
	public boolean _is_valid;
	public VariableExtents(String var){
		_var = var;
		_lteq = _min_value * -1;
		_gteq = _min_value;
		_is_valid = true;
		_strictly_less_than = true;
		_strictly_greater_than = true;
		_not_equals = new Vector(10);
	}
	
	public boolean addRestriction(String operator,double value){
		if (_is_valid){
			/**Takes the operator and the value and adds it
			* into the constraints; return true if it succeeds, false else
			* should be read, this operator value
			*/
			if (operator.equals("!=")){
				//then we just need to make sure that we haven't decided
				//that the value is exactly equal to this one and add it 
				//to the list of things it can't equal
				if (_lteq == value && ! _strictly_less_than && _gteq == value && !_strictly_greater_than){
					_is_valid = false;
					return false;
				}
				else{
					_not_equals.addElement(new Double(value));
					return true;
				}
			}//end if the operator was !=
			else if (operator.equals("<=")){
				//then we need to make sure that this isn't any bigger
				if (value <= _gteq){
					//first, check to see if they can be equal
					if (!_strictly_greater_than && value == _gteq){
						//then they can be equal
						_lteq = value;
						_strictly_less_than = false;
						if (_not_equals.contains(new Double(value))){
							_is_valid = false;
							return false;
						}

					}
					else{
						//we can't add it
						_is_valid = false;
						return false;
					}
				}//end if we need to interact with gteq
				else{
					if (value < _lteq){
						_lteq = value;
						_strictly_less_than = false;
					}
				}//end if we don't need to interact with gteq
				//otherwise _lteq is at least that restrictive, 
				//so we need to do nothing
				return true;
			}//end if the operator was <=
			else if (operator.equals(">=")){
				//then we need to make sure that this isn't any smaller
				if (value >= _lteq){
					//first, check to see if they can be equal
					if (!_strictly_less_than && value == _lteq){
						//then they can be equal
						_gteq = value;
						_strictly_greater_than = false;
						//but we need to check and make sure that
						//it's not in the list of inequalities
						if (_not_equals.contains(new Double(value))){
							_is_valid = false;
							return false;
						}
					}
					else{
						//we can't add it
						_is_valid = false;
						return false;
					}
					
				}//end if we need to interact with gteq
				else{
					if (value > _gteq){
						_gteq = value;
						_strictly_greater_than = false;
					}
				}//end if we don't need to interact with gteq
				//otherwise _lteq is at least that restrictive, 
				//so we need to do nothing
				return true;
			}//end if the operator was >=
			else if (operator.equals("<")){
				//first we need to check and make sure that this doesn't conflict
				//with gteq
				if (_gteq > value){
					_is_valid = false;
					return false;
				}
				//else we need to see if we have to change the value
				if (_lteq >= value){
					//we need to change it
					//if it was equal, then the first step is redundant,
					//and the second step may be, but it's not that much
					//work, so we might as well do it.
					_lteq = value;
					_strictly_less_than = true;
					
				}
				return true;
			}//end if it was < 
			else if (operator.equals(">")){
				//first we need to check and make sure that this doesn't conflict
				//with gteq
				if (_lteq < value){
					_is_valid = false;
					return false;
				}
				//else we need to see if we have to change the value
				if (_gteq <= value){
					//we need to change it
					//if it was equal, then the first step is redundant,
					//and the second step may be, but it's not that much
					//work, so we might as well do it.
					_gteq = value;
					_strictly_greater_than = true;
					
				}
				return true;
			}//end if operator was > 
			else if (operator.equals("==")){
				//then we need to change the max value, min value,
				//and make sure it's not in the list of inequalities
				if(_gteq > value ||(_gteq == value && _strictly_greater_than)
				   || _lteq < value || (_lteq == value && _strictly_less_than)){
					_is_valid = false;
					return false;
				}
				//now we just need to check the inequalities
				if (_not_equals.contains(new Double(value))){
					_is_valid = false;
					return false;
				}
				//otherwise, we can add it
				_gteq = value;
				_lteq = value;
				_strictly_less_than = false;
				_strictly_greater_than = false;
				return true;
			}
			else{
				//unknown operator
				return false;
				
			}
		}
		else{
			return false;
		}
	}//end addRestriction
	
	public boolean checkRestriction(String operator, double value){
		if (_is_valid == false){
			return false;
		}
		else{
			if (operator.equals("==")){
				if(_gteq == value && _lteq == value){
					return true;
				}
				else{
					return false;
				}
			}//end if it was ==
			else if (operator.equals("!=")){
				if (_not_equals.contains(new Double(value))){
					return true;
				}
				else if (value < _gteq || (value == _gteq && _strictly_less_than == true)
						 || value > _lteq || (value == _lteq && _strictly_greater_than == true)){
					return true;
				}
				//otherwise, we don't know
				return false;
			}
			else if (operator.equals("<")){
				if (_lteq < value || (_lteq == value && _strictly_less_than == true)){
					return true;
				}
				else{
					return false;
				}
			}//end if it was <
			else if (operator.equals(">")){
				if (_gteq < value || (_gteq == value && _strictly_greater_than == true)){
					return true;
				}
				else{
					return false;
				}
			}//end if it was >
			else if (operator.equals("<=")){
				if (_lteq <= value){
					return true;
				}
				else{
					return false;
				}
			}
			else if (operator.equals(">=")){
				if (_gteq >= value){
					return true;
				}
				else{
					return false;
				}
			}
			else{
				System.out.println("invalid operator "+operator+ " in VariableExtents.checkRestrictions");
				System.out.println("Program will now exit");
				System.exit(0);
				return false;
			}
			
		}//end if it was valid
	}//end check restriction
	
	public static void main(String args []){
		VariableExtents a = new VariableExtents("a");	
		VariableExtents b = new VariableExtents("b");	
		VariableExtents c = new VariableExtents("c");	
		VariableExtents d = new VariableExtents("d");	
		VariableExtents e = new VariableExtents("e");	
		VariableExtents f = new VariableExtents("f");	
		//VariableExtent g = new VariableExtents("a");	
		a.addRestriction(">=",5);
		//b.addRestriction(">=",5);
		//c.addRestriction(">",5);
		//d.addRestriction("<",5);
		//e.addRestriction("!=",5);
		//f.addRestriction("==",5);
		a.addRestriction("!=",5);
		a.addRestriction("<=",5);
		//c.addRestriction("<",6);
		//d.addRestriction(">",4);
		//e.addRestriction("==",5);
		//f.addRestriction("!=",5);
		//a.checkRestriction("==",5);
		//f.checkRestriction("==",5);
				
	}
}

//import java.lang.*;
package minicon;
import java.util.Random;
import java.util.Vector;

public class IPGenerator  {
    Random _random;
    RandomStatementGenerator a_state_generator;
    int _ip_start;
	Vector _ip_types;
	int _ip_stop;
	int _ip_num_vars;
	int _num_ip_types;
	public static long _seed = System.currentTimeMillis();
		
	
	public IPGenerator(int ip_start, int ip_stop, Vector ip_types, int ip_num_vars){
		_ip_start = ip_start;
		_ip_stop = ip_stop;
		_ip_types = ip_types;
		_ip_num_vars = ip_num_vars;
		_num_ip_types = _ip_types.size();
		_random = new Random(_seed);
		_seed++;
		
	}
	
	public StatementWIP getRandomIPs(Statement a_state){
		StatementWIP retval = new StatementWIP(a_state);
		int i;
		Vector unique_vars = a_state.findUniqueVariables();
		int unique_var_size = unique_vars.size();
		int which_var;
		int operator_number;
		double value;
		for (i = 0; i < _ip_num_vars; i++){
			//first, we decide which variable to have it on
			which_var = (int)(_random.nextDouble() * unique_var_size);
			//now, which operator	
			if (_random.nextDouble() > 0.5){
				operator_number = 0;
			}
			else{
				operator_number = 1;
			}

			value = _ip_start + _random.nextDouble() * (_ip_start - _ip_stop);
			retval.addInterpretedPredicate(
				new InterpretedPredicate((String) unique_vars.elementAt(which_var),
										 ((String) _ip_types.elementAt(operator_number)),
										 value));
		}//end adding all of the predicates
		return retval;
	}
    
}

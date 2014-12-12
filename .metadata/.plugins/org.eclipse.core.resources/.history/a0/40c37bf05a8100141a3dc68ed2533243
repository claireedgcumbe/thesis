package minicon;
public class InterpretedPredicate implements Cloneable
{
	private String _operator;
	private IPValue _lhs;
	private IPValue _rhs;
	
	public InterpretedPredicate(){
		_operator = null;
		_lhs = new IPValue();
		_rhs = new IPValue();
	}
	
	public InterpretedPredicate(String in){
		//time to parse....
		int i;
		int in_size = in.length();
		int an_index;
		int middle_index;
		String left; 
		String right;
		an_index = in.indexOf("<=");
		if (an_index > 0){
			left = in.substring(0,an_index);
			right = in.substring(an_index+2);
			_operator = new String("<=");
			_lhs = parseSide(left);
			_rhs = parseSide(right);
			return;
		}
		an_index = in.indexOf(">=");
		if (an_index > 0){
			left = in.substring(0,an_index);
			right = in.substring(an_index+2);
			_operator = new String(">=");
			_lhs = parseSide(left);
			_rhs = parseSide(right);
			return;
		}

		an_index = in.indexOf("==");
		if (an_index > 0){
			left = in.substring(0,an_index);
			right = in.substring(an_index+2);
			_operator = new String("==");
			_lhs = parseSide(left);
			_rhs = parseSide(right);
			return;
		}
		an_index = in.indexOf("!=");
		if (an_index > 0){
			left = in.substring(0,an_index);
			right = in.substring(an_index+2);
			_operator = new String("!=");
			_lhs = parseSide(left);
			_rhs = parseSide(right);
			return;
		}

		an_index = in.indexOf("<");
		if (an_index > 0){
			left = in.substring(0,an_index);
			right = in.substring(an_index+1);
			_operator = new String("<");
			_lhs = parseSide(left);
			_rhs = parseSide(right);
			return;
		}

		an_index = in.indexOf(">");
		if (an_index > 0){
			left = in.substring(0,an_index);
			right = in.substring(an_index+1);
			_operator = new String(">");
			_lhs = parseSide(left);
			_rhs = parseSide(right);
			return;
		}
		System.out.println("badly formed InterpretedPredicate " + in + "in InterpretedPredicate constructor");
		System.out.println("System will now exit");
		System.exit(1);
									   
		
	}
	
	public IPValue parseSide(String a_side){
		a_side = a_side.trim();
		//this function parses a side; I assume properly formed
		//strings; either two "s or an int inside
		return new IPValue(a_side);
	}

			
		
	
	public InterpretedPredicate(InterpretedPredicate an_ip){
		_lhs = an_ip._lhs.copy();
		_operator = an_ip._operator;
		_rhs = an_ip._rhs.copy();
	}


	
	public InterpretedPredicate(IPValue lhs, String operator, IPValue rhs){
		_operator = operator;
		_lhs = lhs;
		_rhs = rhs;
	}
	
	public InterpretedPredicate(String lhs, String operator, String rhs){
		_operator = operator;
		_lhs = new IPValue(lhs);
		_rhs = new IPValue(rhs);
	}

	public InterpretedPredicate(double lhs, String operator, String rhs){
		_operator = operator;
		_lhs = new IPValue(lhs);
		_rhs = new IPValue(rhs);
	}
	
	public InterpretedPredicate(String lhs, String operator, double rhs){
		_operator = operator;
		_lhs = new IPValue(lhs);
		_rhs = new IPValue(rhs);
	}
	
	public InterpretedPredicate(double lhs, String operator, double rhs){
		_operator = operator;
		_lhs = new IPValue(lhs);
		_rhs = new IPValue(rhs);
	}
	
	public InterpretedPredicate copy(){
		InterpretedPredicate retval = new InterpretedPredicate();
		retval._lhs = _lhs.copy();
		retval._operator = _operator;
		retval._rhs = _rhs.copy();
		return retval;	
	}
	
	public boolean isLHSAConstant(){
		return _lhs.isNumericalConstant();
	}
	
	public boolean isRHSAConstant(){
		return _rhs.isNumericalConstant();
	}
	
	public void setLHS(double a_val){
		_lhs.setValue(a_val);
	}
	
	public void setRHS(double a_val){
		_rhs.setValue(a_val);
	}
	
	public void setLHS(String a_val){
		_lhs.setValue(a_val);
	}
	
	public void setRHS(String a_val){
		_rhs.setValue(a_val);
	}
	
	public IPValue getLHS(){
		return _lhs;
	}
	
	public void setLHS(IPValue a_val){
		_lhs = a_val;
	}
	
	public void setRHS(IPValue a_val){
		_rhs = a_val;
	}
	
	public IPValue getRHS(){
		return _rhs;
	}
	
	public double getLHSConstant(){
		return _lhs.getNumericalConstant();
	}
	
	public double getRHSConstant(){
		return _rhs.getNumericalConstant();
	}
	
	public String getLHSVariable(){
		return _lhs.getVariable();
	}
	
	public String getRHSVariable(){
		return _rhs.getVariable();
	}
			
	public void setOperator(String an_op){
		_operator = an_op;
	}
	
	public String getOperator(){
		return _operator;
	}
	
	public boolean Evaluate(){
		//returns whether or not a given statement is true.
		//if either of the sides are variables, it returns false...
		if (!_rhs.isNumericalConstant() || !_lhs.isNumericalConstant()){
			return false;
		}
		if (_operator.equals("<")){
			return _lhs.getNumericalConstant() < _rhs.getNumericalConstant();
		}
		if (_operator.equals("!=")){
			return _lhs.getNumericalConstant() != _rhs.getNumericalConstant();
		}
		if (_operator.equals("==")){
			return _lhs.getNumericalConstant() == _rhs.getNumericalConstant();
		}
		if (_operator.equals(">")){
			return _lhs.getNumericalConstant() > _rhs.getNumericalConstant();
		}
		if (_operator.equals(">=")){
			return _lhs.getNumericalConstant() >= _rhs.getNumericalConstant();
		}
		if (_operator.equals("<=")){
			return _lhs.getNumericalConstant() <= _rhs.getNumericalConstant();
		}
		System.out.println("unrecognized operator " + _operator + " in InterpretedPredicate.evaluate()");
		return false;
		
	}
	
	public InterpretedPredicate switchSides(){
		return new InterpretedPredicate(_rhs,_operator,_lhs);
	}
	
	public String printString(){
		return (_lhs.printString() + " " + _operator + " " +  _rhs.printString());
	}
	
	public String printString(QueryEquality equals){
		StringBuffer retval = new StringBuffer("");
		if (_lhs.isNumericalConstant()){
		    retval.append(_lhs.printString());
		}
		else{
			//it's a variable, so print out the variable equivalent
			retval.append(equals.getRepresentative(_lhs).printString());
		}
		retval.append(" ");
		retval.append(_operator);
		retval.append(" ");
		
		if (_rhs.isNumericalConstant()){
		    retval.append(_rhs.printString());
		    
		}
		else{
			//it's a variable, so print out the variable equivalent
			retval.append(equals.getRepresentative(_rhs).printString());
		}
		return retval.toString();											 
	}
	
	public void print(){
		System.out.print(printString());
	}
	
	public InterpretedPredicate[] translateIP(){
		/**This function takes the interpreted predicate and translates it to an 
		 * equivalent version that uses only <= and !=
		 * It returns an array of size two; the first slot is the slot for <=
		 * and the second slot is for !=.  If one of these is not needed, then it 
		 * is left at null;
		 */
		InterpretedPredicate [] retval = new InterpretedPredicate[2];
		int i;
		InterpretedPredicate new_ip;
		retval[0] = null;
		retval[1] = null;
		if (getOperator().equals("<=")){
			retval[0] = this;
		}
		else if(getOperator().equals("!=")){
			retval[1] = this;
		}
		else if(getOperator().equals("<")){
			new_ip = copy();
			new_ip.setOperator("<=");
			retval[0] = new_ip;
			new_ip = copy();
			new_ip.setOperator("!=");
			retval[1] = new_ip;
		}
		else if(getOperator().equals(">")){
			new_ip = switchSides();
			new_ip.setOperator("<=");
			retval[0] = new_ip;
		}
		else if (getOperator().equals(">=")){
			new_ip = switchSides();
			new_ip.setOperator("<=");
			retval[0] = new_ip;
			new_ip = switchSides();
			new_ip.setOperator("!=");
			retval[1] = new_ip;
		}
		else if (getOperator().equals("==")){
			new_ip = copy();
			new_ip.setOperator("<=");
			retval[0] = new_ip;
			new_ip = switchSides();
			new_ip.setOperator("<=");
		}
		else {
			System.out.println("couldn't find operator " + getOperator() + 
							   " in InterpretedPredicate.translateIP");
		}//end else			
	
		return retval;
	}//end translateIP
	
	public InterpretedPredicate invertIP(){
		/**This function takes the interpreted predicate and translates it to an 
		 * equivalent version that uses only <= and !=
		 * It returns an array of size two; the first slot is the slot for <=
		 * and the second slot is for !=.  If one of these is not needed, then it 
		 * is left at null;
		 */
		InterpretedPredicate an_ip = this;
		InterpretedPredicate retval = new InterpretedPredicate(an_ip);
		String operator = an_ip.getOperator();
		if (operator.equals("!=")){
			retval.setOperator("==");
			return retval;
		}
		if (operator.equals("==")){
			retval.setOperator("!=");
			return retval;
		}
		if (operator.equals(">=")){
			retval.setOperator("<");
			return retval;
		}
		if (operator.equals("<")){
			retval.setOperator(">=");
			return retval;
		}
		if (operator.equals(">")){
			retval.setOperator("<=");
			return retval;
		}
		if (operator.equals("<=")){
			retval.setOperator(">");
			return retval;
		}
		//at this point, if we have not returned, then we have an unrecognized 
		//operator, so say so and exit
		System.out.println("Unrecognized operator " + operator + " in InterpretedPredicate.invertIP");
		System.out.println("Program will now exit");
		System.exit(1);
		return null;
	}//end invertIP

	public static void main(String args[]){
		InterpretedPredicate a = new InterpretedPredicate("a","<=","b");
		InterpretedPredicate b = new InterpretedPredicate("a",">=",2.0);
		InterpretedPredicate c = new InterpretedPredicate(2.0,">",3.0);
		InterpretedPredicate d = new InterpretedPredicate(2.0,"!=","a");
		InterpretedPredicate e = new InterpretedPredicate(new IPValue("a"),"==",new IPValue("b"));
		InterpretedPredicate f = new InterpretedPredicate(3.0, ">",4.0);
		System.out.println("done!");
		a.setLHS(2.0);
		a.setRHS(3.0);
		b.setLHS(2.0);
		d.setRHS(4.0);
		d.setLHS("a");
		d.setLHS(3.0);
		System.out.println(a.Evaluate());
		System.out.println(b.Evaluate());
		System.out.println(c.Evaluate());
		System.out.println(d.Evaluate());
		System.out.println(e.Evaluate());
		System.out.println(f.Evaluate());
	}//end main
	
}//end class InterpretedPredicate

/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package minicon;

/**
 * @author rap
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GAVMD extends MD {

	protected static String m_free_function_name = "_unv";
	protected String m_my_function_name;
	protected int m_next_free_num;
   public GAVMD(Query a_query, View a_view){
		super(a_query, a_view);
		m_my_function_name = m_free_function_name + "1";
		m_free_function_name = m_my_function_name;
		m_next_free_num = 0;
    }
 
	/* (non-Javadoc)
	 * @see minicon.MD#addMapping(minicon.Mapping)
	 */
	public boolean addMapping(Mapping amap) {
		//System.out.println("------------------------------add to mapping ");
		mapping.addElement(new Mapping(amap));
		return false;
	}

	public IPValue getVar(IPValue p_var)
	{
		System.out.println("p_var: " + p_var.printString());
		IPValue mapping_val =mappingToVariable(p_var);
		if (mapping_val == null)
		{
			m_next_free_num++;
			return new IPValue(m_my_function_name + "_" + m_next_free_num + p_var.printString());
	
		}
		else
		{
			return mapping_val;
		}
	}//end 
	
	public static void main(String[] args) {
		Query q = new Query("q(x):-v1(x)");
		View v = new View("v1(a):-e1(a,b,c)");
		GAVMD md = new GAVMD(q,v);
		Mapping a_map = new Mapping(q.getHead().variableI(0),v.getHead().variableI(0));
		md.addMapping(a_map);
		Predicate a_pred = v.subgoalI(0);
		IPValue val;
		for (a_pred.first(); !a_pred.isDone();a_pred.next())
		{
			val = a_pred.current();
			IPValue mapping_val =md.mappingToVariable(val); 
			if (mapping_val != null)
			{
				System.out.print(mapping_val.printString()+ " ");
			}
			else
			{
				System.out.print(val.printString()+ " ");
			}
		}
		md.addMapping(a_map);
		QueryEquality dummy = new QueryEquality();
		System.out.println(md.printRewriting(dummy).toString());
		
	}
}

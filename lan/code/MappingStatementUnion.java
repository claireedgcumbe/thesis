/*
 * Created on Jan 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package semantics;
import minicon.*;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.Vector;



/**
 * @author rap
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MappingStatementUnion implements Serializable{
	protected String m_head;
	protected Vector m_mapping_statements;
	public MappingStatementUnion()
	{
		m_head = new String("");
		m_mapping_statements = new Vector();
	}
	
	public MappingStatementUnion(View p_state)
	{
		m_mapping_statements = new Vector();
		m_mapping_statements.addElement(p_state);
		m_head = p_state.getHead().getFunctionHead();
	}
	
	public boolean isHead(String p_function){
		if (m_head.equals(p_function))
		{
			return true;
		}
		return false;
	}

	public String getHead()
	{
		return m_head;
	}
	
	public Vector getVariables()
	{
		Vector retval;
		int i,j;
		String a_var;
		Vector vars;
		int num_vars;
		Statement a_state;
		int num_statements= m_mapping_statements.size();
		if (num_statements == 0)
		{
			return new Vector();
		}
		a_state = (Statement) m_mapping_statements.elementAt(0);
		
		retval = new Vector();
		for (i = 0; i < num_statements; i++){
			a_state = (Statement)m_mapping_statements.elementAt(i);
			vars = a_state.findUniqueVariables();
			//need to see if this is right
			num_vars = vars.size();
			for(j = 0; j < num_vars; j++)
			{
				a_var = ((IPValue) vars.elementAt(j)).printString();
				if (!retval.contains(a_var))
				{
					retval.addElement(a_var);
				}
			}
		}
		return retval;
	}
	
	public void addStatement(Statement p_statement){
		m_mapping_statements.addElement(p_statement);
	}
	
	public int numStatements(){
		return m_mapping_statements.size();
	}
	
	public Statement statementI(int p_i)
	{
		try {
			return (Statement)m_mapping_statements.elementAt(p_i);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public String toString()
	{
		StringBuffer retval = new StringBuffer();
		int i; 
		int num_statements = numStatements();
		Statement a_state;
		for (i = 0; i < num_statements; i++)
		{
			a_state = (Statement)m_mapping_statements.elementAt(i);
			retval.append(a_state.printString()+ "\n");
		}
		return retval.toString();
	}
	
	public LinkedList<Attribute> getIndirectAttribute()
	{
		LinkedList<Attribute> mappings = new LinkedList<Attribute>();
		int num_statements = numStatements();
		Statement ifc_state, city_state;
		if(num_statements==2) {
			ifc_state = (Statement)m_mapping_statements.elementAt(0);
			Predicate ifc_head = ifc_state.getHead();
			Vector ifc_headList = ifc_head.variables;
			city_state = (Statement)m_mapping_statements.elementAt(1);
			System.out.println("-------city_state   " +city_state.printString());
			Predicate city_head = city_state.getHead();
			Vector city_headList = city_head.variables;
			if(ifc_head.size()>0){
				System.out.println("-------ifc_head.size   " +ifc_head.size() + "   "+ifc_head.printString());
				System.out.println("-------city_head.size   " +city_head.size()+ "   "+city_head.printString());
	            for (int i = 0; i < ifc_head.size(); i++){
	            	String ifc = ((IPValue) ifc_headList.elementAt(i)).printString();
	            	String citygml = ((IPValue) city_headList.elementAt(i)).printString();
	            	if(!ifc.equals(citygml)) {
	            		System.out.println("-------indirect------schema-----------------------" +ifc+"   " +citygml);
	            		Predicate ifc_body = ifc_state.subgoalI(0);
	            		//System.out.println("-------ifc_body   " +ifc_body.printString());
	            		Predicate city_body = city_state.subgoalI(0);
	            		//System.out.println("-------city_body   " +city_body.printString());
	            		Attribute attri = new Attribute();
	            		if(ifc_body.containsVariable(ifc)) {
	            			attri.setSchema("IFC");
	            			attri.setName(ifc);
	            			attri.setMapping(citygml);
	            		} else if(city_body.containsVariable(citygml)) {
	            			attri.setSchema("CityGML");
	            			attri.setName(citygml);
	            			attri.setMapping(ifc);
	            		} else 
	            			continue;
	            			//System.out.println("-------ifc citygml   " +ifc+"   "+citygml);
	            		mappings.add(attri);
	            	}
	            	
	            }
	        }
		}
		if(mappings.size()>0) {
			System.out.println("----------------------" +mappings.size());
			System.out.println("----------attri------------" +mappings.get(0).schema+" "+mappings.get(0).name+" "+mappings.get(0).mapping);
		}
		return mappings;
	}
	
	/*
	 * Mar 28, 2006
	 * @author: jzhao
	 */
	public boolean isProjectionOnly(Predicate p_pred){
		int num_ms = this.numStatements();
		for (int j = 0; j < num_ms; j++)
		{
			Statement state = (Statement)this.statementI(j);
			if (state.containsPredicateName(p_pred.getFunctionHead()) &&
					state.isProjectionOnly()){
				return true;
			}
		}
		return false;
	}
	
	
	public static void main(String[] args) {
	}
}

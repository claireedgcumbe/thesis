package mediation;

import java.io.*;
import java.util.Vector;
import minicon.*;

public class Schema implements Serializable{
	protected Vector m_relations;
	protected String m_name;
//	protected Vector m_mapping; //the mapping that the data integration system is the mediated schema for.
	
	public Schema()
	{
		m_relations = new Vector();
		m_name = "";
//		m_mapping = null;
	}

	public boolean containsRelation(String p_name)
	{
		int i;
		int num_rels = m_relations.size();
		Predicate a_pred;
		for (i = 0; i < num_rels; i++)
		{
			a_pred = (Predicate)m_relations.elementAt(i);
			if (a_pred.getFunctionHead().equals(p_name))
			{
				return true;
			}
		}
		return false;
	}
	
/*
	//whether it is a mediated schema or not
	public boolean isCompositeSchema() 
	{
		if (m_mapping==null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
*/	
	public void addRelation(Predicate p_predicate)
	{
		m_relations.add(p_predicate);
	}

	public void addRelation(String p_string) {
		Predicate p = new Predicate();
		p.read(p_string);
		this.addRelation(p);
	}
	
	public void setName(String p_name)
	{
		m_name = p_name;
	}
	
	/*added by jzhao*/
	public String getName(){
		return m_name;
	}
	
	public String printString(){
		StringBuffer retval = new StringBuffer("Schema name: " + m_name + "\nRelations:\n");
		int num_rels = m_relations.size();
		Predicate a_pred;
		for (int i = 0; i < num_rels; i++)
		{
			a_pred = (Predicate) m_relations.elementAt(i);
			retval.append("\t" + a_pred.printString() + "\n");
		}
		return retval.toString();
	}
	
	public Schema(Schema p_to_copy)
	{
		int i;
		Predicate a_pred;
		int num_rels;
		num_rels = p_to_copy.m_relations.size();
		m_relations = new Vector(num_rels);
		for (i = 0; i < num_rels; i++){
			a_pred = p_to_copy.relationI(i);
			m_relations.addElement(new Predicate(a_pred));
		}
		m_name = new String(p_to_copy.m_name);
	}
	
	public int numRelations(){
		return m_relations.size();
	}
	
	public Predicate relationI(int p_i)
	{
		try{
			return (Predicate) m_relations.elementAt(p_i);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public Predicate findRelation(String p_name)
	{
		int i;
		Predicate a_pred;
		int num_rels = m_relations.size();
		for (i = 0; i < num_rels; i++){
			a_pred = (Predicate)m_relations.elementAt(i);
			if (a_pred.getFunctionHead().equals(p_name))
			{
				return a_pred;
			}
		}
		return null;
	}
	
	public int findRelationI(String p_name)
	{
		int i;
		Predicate a_pred;
		int num_rels = m_relations.size();
		for (i = 0; i < num_rels; i++){
			a_pred = (Predicate)m_relations.elementAt(i);
			if (a_pred.getFunctionHead().equals(p_name))
			{
				return i;
			}
		}
		return -1;
	}
	
	/* 
	 * April 9, 2006
	 * added by jzhao
	 */
	public void delRelation(String p_name){
		int i;
		Predicate a_pred;
		int num_rels = m_relations.size();
		for (i = 0; i < num_rels; i++){
			a_pred = (Predicate)m_relations.elementAt(i);
//			System.out.println("current relation:" + a_pred.printString());
			if (a_pred.getFunctionHead().equals(p_name)){
//				System.out.println("i: " + i);
				m_relations.removeElementAt(i);
				return;
			}
		}
	}
	
	public void readFromFile(String p_filename)
	{
		StringBuffer a_buffer = new StringBuffer();
		String line;
		try
		{
			FileReader file = new FileReader(p_filename);
			BufferedReader input = new BufferedReader(file);
			line = input.readLine();
			while (line != null)
			{
				a_buffer.append(line + "\n");
				line = input.readLine();
			}
			
			file.close();
			input.close();
			readFromString(a_buffer.toString());
		}
		catch(Exception e)
		{
			System.out.println("can't open filename " + p_filename);
		}
	}
	
	public void readFromString(String p_schema_string)
	{
		//I assume that the schema has one relation
		//per line, and that the only thing that interrupts
		//this is comments, which start with "//"
		String line;
		Predicate pred;
		try {
			StringReader string_reader = new StringReader(p_schema_string);
			BufferedReader input = new BufferedReader(string_reader);
			line = input.readLine();			
			while (line != null){
				if (!line.startsWith("//") && !line.trim().equals("")){
					pred = new Predicate();
					pred.read(line);
					addRelation(pred);
					//System.out.println("relation added: " + pred.printString().toString());
				}
				line = input.readLine();
			}//end of getting the input schemas
			string_reader.close();
			input.close();

		}//end try block
		catch (Exception e){
			System.out.println("error processing" + p_schema_string);
		}

	}
	
	public static void testInput()
	{
		Schema bob = new Schema();
		bob.setName("bob");
		Predicate a_pred = new Predicate();
		a_pred.read("q(x,y,z)");
		bob.addRelation(a_pred);
		Schema fred = new Schema(bob);
		a_pred = bob.relationI(0);
		a_pred.setFunctionHead("notq");
		fred.setName("fred");
		System.out.println(bob.printString());
		System.out.println(fred.printString());
		
	}
	
	public static void testReadFromFile()
	{
		Schema a_schema = new Schema();
		a_schema.readFromFile("D:\\UBC study\\projects\\test\\schema.txt");
		System.out.println(a_schema.printString());
	}
	
	public static void main(String[] args) {
		//testReadFromFile();
		testInput();
	}

}

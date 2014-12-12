package semantics;
import minicon.*;
import java.util.Vector;
public class GenerateSources {

	/**
	 * this class generates sources based on 
	 * parameters given
	 */
	protected int m_num_sources;
	protected int m_num_attributes;
	protected int m_num_relations;
	protected Vector m_sources;
	protected Vector m_common_attributes;
	protected int m_next_free;
	protected String m_common_prefix;
	
	public GenerateSources()
	{
		m_num_sources = 5;
		m_num_attributes = 5;
		m_num_relations = 10;
		m_sources = new Vector();
		m_common_attributes = new Vector();
		m_next_free = 0;
		m_common_prefix = "common";
		setCommonAttributes();
	}
 
	/*
	 * Assumes that the vector contains "true"
	 * if the value is supposed to be common, 
	 * false else
	 */
	protected void setCommonAttributes()
	{
		m_common_attributes.addElement(m_common_prefix + "_1");
		m_common_attributes.addElement(m_common_prefix + "_2");
		m_common_attributes.addElement(null);
		m_common_attributes.addElement(null);
		m_common_attributes.addElement(null);
		

	}
	
	public void setNumRelations(int p_num_rels)
	{
		m_num_relations = p_num_rels;
	}
	
	public void setNumSources(int p_num_sources)
	{
		m_num_sources = p_num_sources;
	}
	
	public void setSpecificAttributesToAttributes(Vector p_attributes)
	/*
	 * The goal of this function is to allow us to set the specific
	 * attributes to be values that we want.  
	 * We assume that each value is "null" if we just want a fresh value
	 * otherwise, it's a string
	 */
	{
		m_common_attributes = p_attributes;
	}
	
	public void setCommonAttributes(Vector p_attributes)
	/*
	 * Assumes that p_attributes contains a list of boolean values.  If the value is "true", the value should be 
	 * common, else it shouldn't be.  I'll assume that for now there's nothing more complicated than that
	 */
	{
		int i; 
		int num_attrs = p_attributes.size();
		for (i = 0; i < num_attrs; i++)
		{
			if (((Boolean)p_attributes.elementAt(i)).booleanValue() == true)
			{
				m_common_attributes.addElement(m_common_prefix + "_" + i);
			}
			else
			{
				m_common_attributes.addElement(null);
			}
		}
	}
	
	public void generateNSources(int p_n)
	{
		int i;
		for (i = 0; i< p_n; i++)
		{
			generateASource(i,m_common_attributes,m_num_relations,m_num_attributes);
		}
	}
	
	protected void generateASource(int p_source,Vector p_common_attrs, int p_num_rels, int p_num_attrs)
	{
		int i,j; 
		Predicate p;
		String common;
		Schema new_source = new Schema();
		for (i = 0; i < p_num_rels;i++)
		{
			//generating a relation
			p = new Predicate();
			p.setFunctionHead(p_source + "_" + i);
			for (j = 0; j < p_num_attrs; j++)
			{
				common = (String)p_common_attrs.elementAt(j);
				if (common == null)
				{
					p.addVariable(new String("new_" + m_next_free));
					m_next_free++;
				}
				else
				{
					//it was common, so just add the value from the vector
					p.addVariable((String)p_common_attrs.elementAt(j)+ "_" + i);
				}
			}//end adding attributes
			new_source.addRelation(p);
		}//end adding relations
		m_sources.addElement(new_source);
	}//end protected void generateASource

	public Vector getSources()
	{
		return m_sources;
	}
	
	public String toString()
	{
		StringBuffer retval = new StringBuffer("Schemas:");
		Schema a_schema;
		int i;
		int num_schemas = m_sources.size();
		for (i = 0; i < num_schemas; i++)
		{
			a_schema = (Schema)m_sources.elementAt(i);
			retval.append("\t schema " + i + "\n");
			retval.append(a_schema.printString()+"\n");
		}
		return retval.toString();
	}
	
	public static void main(String[] args) {
		GenerateSources g = new GenerateSources();
		g.generateNSources(3);
		System.out.print(g.toString());

	}

}

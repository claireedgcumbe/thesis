/*
 * Created on Jan 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package semantics;
import minicon.*;
import java.util.Vector;
/**
 * @author rap
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SemanticMerge {
	
	protected Mapping m_mapping;
	protected Schema m_merged_schema;
	protected Schema m_schema1;
	protected Schema m_schema2;
	protected GLAVMapping m_glav;

	public SemanticMerge()
	{
		m_merged_schema = new Schema();
		m_schema1 = new Schema();
		m_schema2 = new Schema();
		m_mapping = new Mapping();
		m_glav = new GLAVMapping();
		m_merged_schema.setMapping(m_glav);
	}
	
	public void setSchema1(Schema p_schema)
	{
		m_schema1 = p_schema;
	}
	public void setSchema2(Schema p_schema)
	{
		m_schema2 = p_schema;
	}
	
	public void setMapping(Mapping p_mapping)
	{
		m_mapping = p_mapping;
	}
	

	public GLAVMapping getGLAVMapping()
	{
		return m_glav;
	}
	
	
	public void merge()
	{
		m_glav.addInputSchema(this.m_schema1);
		m_glav.addInputSchema(this.m_schema2);
		int num_mapping_unions = m_mapping.numMappingUnions();
		int i;
		Predicate a_rel;
		int j;
		int k;
		View a_view;
		Vector attributes;
		int num_attrs;
		MappingStatementUnion msu;
		Schema schema_to_check;
		Statement a_state;
		Predicate query_head;
		for (i = 0; i < num_mapping_unions; i++)
		{
			a_rel = new Predicate();
			msu = m_mapping.MappingUnionI(i);
			a_rel.setFunctionHead(msu.getHead());
			attributes = msu.getVariables();
			num_attrs = attributes.size();
			for (j = 0; j < num_attrs; j++)
			{
				a_rel.addVariable((String)attributes.elementAt(j));
			}
			System.out.println("a_rel:" + a_rel.printString());
			m_merged_schema.addRelation(a_rel);
			//now, create the mapping views for this mapping
			int num_mss = msu.numStatements();
			for (j = 0; j < num_mss; j++)
			{
				a_state = msu.statementI(j);
				attributes = a_state.findUniqueVariables();
				query_head = new Predicate();
				for (k = 0; k < attributes.size();k++)
				{
					query_head.addVariable(((IPValue)attributes.elementAt(k)).printString());
				}
				query_head.setFunctionHead(m_glav.getCurrentFreeName());
				a_view = new View();
				a_view.addSubgoal(a_rel);
				a_view.setHead(query_head);
				m_glav.addLV(a_view);
				a_view = new View(a_state);
				a_view.setHead(query_head);
				m_glav.addGV(a_view);
				m_glav.incrementNextFreeNum();
			}//done dealing with a mapping statment
		}//done dealing with mapping
		
		for (i = 0; i < 2; i++){
			if (i == 0){
				schema_to_check = m_schema1;
			}
			else
			{
				schema_to_check = m_schema2;
			}
			int num_rels = schema_to_check.numRelations();
			for (j = 0; j < num_rels; j++)
			{
				a_rel = schema_to_check.relationI(j);
				if (!m_mapping.inProjectionOnlyStatement(a_rel))
				{
					m_merged_schema.addRelation(a_rel);
					//also need to add mapping for it.
					a_view = new View();
					query_head = new Predicate(a_rel);
					query_head.setFunctionHead(m_glav.getCurrentFreeName());
					a_view.setHead(query_head);
					a_view.addSubgoal(a_rel);
					m_glav.addGV(a_view);
					m_glav.addLV(a_view);
					m_glav.incrementNextFreeNum();
				}
			}
		}
	}
	
	public Schema getMergedSchema()
	{
		return m_merged_schema;
	}
	
	public static void testMerge()
	{
		View ms1 = new View("q(x):-e1(x,y)");
		View ms2 = new View("q(x):-f1(x,z)");
		Schema s1 = new Schema();
		s1.addRelation("e1(a,b)");
		s1.addRelation("e3(c,d)");
		s1.addRelation("e4(e,f)");
		Schema s2 = new Schema();
		s2.addRelation("f1(c,d");
		s2.addRelation("f2(e,f)");
		Mapping map = new Mapping();
		map.addView(ms1);
		map.addView(ms2);
		ms1 = new View("q2(x,y):-e3(x,y),e4(y,z)");
		ms2 = new View("q2(x,y):-f2(x,y)");
		map.addView(ms1);
		map.addView(ms2);
		SemanticMerge a_merge = new SemanticMerge();
		a_merge.setMapping(map);
		a_merge.setSchema1(s1);
		a_merge.setSchema2(s2);
		a_merge.merge();
		System.out.println(a_merge.m_merged_schema.printString());
		System.out.println(a_merge.m_glav.printString());
	}
	
	public static void main(String[] args) {
		System.out.println("Making semantic merge, yeah");
		testMerge();
	}
}

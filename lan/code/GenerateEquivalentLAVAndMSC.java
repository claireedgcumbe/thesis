package semantics;
import minicon.*;
import java.util.Vector;

public class GenerateEquivalentLAVAndMSC {

	protected Vector m_sources;
	protected BucketEndingAlgorithm m_lav;
	protected Schema m_mscs;
	protected GLAVMapping m_glav_mapping;
	protected int m_free_predicate_name;
	protected GenerateSources m_source_generator;
	protected int m_num_sources;
	protected boolean m_debug_mode;
	protected int m_num_relations;
	
	public GenerateEquivalentLAVAndMSC()
	{
		m_sources = new Vector();
		m_lav = null;
		m_mscs = null;
		m_free_predicate_name = 0;
		m_glav_mapping = null;
		m_source_generator = new GenerateSources();
		m_num_sources = 5;
		m_debug_mode = false;
		m_num_relations = 5;
	}
	public GenerateEquivalentLAVAndMSC(boolean p_debug_mode)
	{
		m_sources = new Vector();
		m_lav = null;
		m_mscs = null;
		m_free_predicate_name = 0;
		m_glav_mapping = null;
		m_source_generator = new GenerateSources();
		m_num_sources = 5;
		m_debug_mode = p_debug_mode;
		m_num_relations = 5;
	}
	
	public void setNumRelations(int p_num_relations)
	{
		m_num_relations = p_num_relations;
		m_source_generator.setNumRelations(m_num_relations);
	}
	public void setNumSources(int p_num_sources){
		//m_source_generator.setNumSources(p_num_sources);
		m_num_sources = p_num_sources;
	}
	
	public BucketEndingAlgorithm getLAV()
	{
		return m_lav;
	}
	
	public Schema getMSCs()
	{
		return m_mscs;
	}
	
	public void generateSources()
	{
		m_source_generator.generateNSources(m_num_sources);
		m_sources = m_source_generator.getSources();
		
	}
	
	
	public void createMSCFromSources()
	{

		int i,j,k,l;
		SemanticMerge a_merge = null;
		Mapping mapping = null;
		Schema current_schema = new Schema();
		Schema next_schema;
		Schema a_schema;
		Predicate a_rel;
		Predicate a_common_rel;
		IPValue var1;
		Predicate mapping_head;
		View a_view;
		int num_preds,num_schemas;
		int current_schema_length;
		int pred_length;
		generateSources();
		num_schemas = m_sources.size();
		for (i = 0; i < num_schemas; i++)
		{
			a_schema = (Schema)m_sources.elementAt(i);
			//now we need to deal with the mapping
			if (i == 0)
				//this is the first time through the loop, so no merging, just 
				//create the schema and get it set up for next time
			{
				current_schema = a_schema;
			}
			else//this wasn't the first time through the loop, there's a schema in "current_schema"
				//so we need to actually merge
			{
				//so we're going to loop through the predicates and see where they match. 
				//if they match, add a new mapping
				current_schema_length = current_schema.numRelations();
				mapping = new Mapping();
				num_preds = a_schema.numRelations();
				for (j = 0; j < num_preds; j++)
				{
					a_rel = a_schema.relationI(j);
					for (k = 0; k < current_schema_length; k++)
					{
						a_common_rel = current_schema.relationI(k);
						//now we have the two that we're checking for equality
						mapping_head = new Predicate();
						mapping_head.setFunctionHead("map"+i + "_" + m_free_predicate_name);
						m_free_predicate_name++;
						pred_length = a_common_rel.size();
						for (l = 0; l < pred_length;l++)
						{
							var1 = a_common_rel.variableI(l);
							if (a_rel.containsVariable(var1)){
								if (!mapping_head.containsVariable(var1))
								{
									mapping_head.addVariable(var1);
								}
							}//then it was common, add it to our mapping
						}//end looping over the predicate
						//at this point we know the common values.  if there are any,
						//add the mapping
						if (mapping_head.size()> 0)
						{

							//in this case, there was something to the mapping, so add it to our result
							//first, add the view for the new sources
							a_view = new View();
							a_view.setHead(mapping_head);
							a_view.addSubgoal(a_rel);
							mapping.addView(a_view);
							//now add the source for the new schema
							a_view = new View();
							a_view.setHead(mapping_head);
							a_view.addSubgoal(a_common_rel);
							mapping.addView(a_view);
						}//end if there were things in common so we need to add a source
					
					}//end looping over the current schema looking for matches
					//loops over k
					//at this point, if there are things to be merged, there's something in the mapping
					//loops over j

				}//end if it wasn't the first time through the loop, so we needed to merge
				if (mapping.numMappingUnions()> 0)
				{
					a_merge = new SemanticMerge();
					a_merge.setMapping(mapping);
					a_merge.setSchema1(a_schema);
					a_merge.setSchema2(current_schema);
					a_merge.merge();
					next_schema = a_merge.getMergedSchema();
					current_schema = next_schema;
				}
			}
			if (a_merge != null)
			{
				m_mscs = a_merge.getMergedSchema();
				m_glav_mapping = a_merge.getGLAVMapping();
				if (m_debug_mode == true)
				{
					System.out.println("Mediated schema and glav mappings = ");
					System.out.println(a_merge.m_merged_schema.printString());
					System.out.println(a_merge.m_glav.printString());
				}
			}

		}//end looking over this schema looking for matches
		
	}
	
	public void CreateLAVFromSources()
	//whoops.  Need to have mediated schema in order for this to work
	//need to do that first
	//now we can tell 
	{
		if (m_mscs == null)
		{
			createMSCFromSources();
		}
		if (m_mscs == null)
		{
			System.out.println("Error: for some reason, Create MSC from sources failed");
		}
		Predicate p;
		IPValue a_val;
		Predicate a_mediated_rel;
		Schema mediated_schema = m_mscs;
		int num_mediated_rels = m_mscs.numRelations();
		Schema local_source;
		int i,j,k,l;
		boolean contains_common =false;
		int num_attrs;
		int num_preds;
		View a_view;
		m_lav = new BucketEndingAlgorithm();
		int num_sources = m_sources.size();
		for (i = 0; i < num_sources; i++){
			local_source = (Schema) m_sources.elementAt(i);
			num_preds = local_source.numRelations();
			for (j = 0; j < num_preds; j++)
			{
				p = local_source.relationI(j);
				//at this point we have the relation, now
				//figure out what the common attributes are
				//so that we know what to combine
				a_mediated_rel = mediated_schema.relationI(j);
				for (k = 0, contains_common = false; k < num_mediated_rels && contains_common == false; k++)
				{
					num_attrs = p.size();
					contains_common = false;
			
					for (l = 0; l < num_attrs && contains_common == false; l++)
					{
						a_val = p.variableI(l);
						if(a_mediated_rel.containsVariable(a_val))
						{
							contains_common = true;
						}
					}//end looping over attributes
					if (contains_common){//then we know that there is a common attribute, 
						//so we should create a view.
						a_view = new View();
						a_view.setHead(p);
						a_view.addSubgoal(a_mediated_rel);
						m_lav.addView(a_view);
					}

				}//end looping over mediated schema rels
				//at this point we know if contains_common = true, we need to make 
				//sure to add a mappinge to it   
			}//end looping over relations
		}//end looping over schemas
		//at this point we should have the LAV views.  Check and make sure that it works.

		if (m_debug_mode == true){
			int num_views;
			num_views = m_lav.getNumMappings();
			System.out.println("views for LAV =");
			for (i = 0; i < num_views; i++)
			{
				a_view = m_lav.viewI(i);
				System.out.println(a_view.printString().toString());
			}//end printing out the views
		}
	}//end function CreateLAVFromSources()
	
	public static void main(String[] args) {
		GenerateEquivalentLAVAndMSC test = new GenerateEquivalentLAVAndMSC();
		test.createMSCFromSources();
		test.CreateLAVFromSources();

	}

}

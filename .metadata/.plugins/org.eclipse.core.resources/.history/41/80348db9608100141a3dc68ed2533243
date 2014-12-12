package mediation;

/*
 * Created on Mar 26, 2006

 * @author: jzhao
 * later can be extended to peer class
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import minicon.IPValue;
import minicon.Predicate;
import minicon.View;

import semantics.Attribute;
import semantics.MappingStatementUnion;

public class SchemaMediationTest {
	protected Vector schemaSet;
	protected Hashtable schemaSetHash;
	protected Vector mappingSet; //includes mappingInfo Map->E,F
	protected Vector mtSet; //all MappingTables, grouped by concept, hash this later
	protected Schema m_schema;
	protected Vector m_glav;
	
	public SchemaMediationTest(){
		schemaSet = new Vector();
		schemaSetHash = new Hashtable();
		mappingSet = new Vector();
		m_schema = new Schema();
		m_schema.setName("M");
		mtSet = new Vector();
		m_glav = new Vector();
	}
	
	public void addSchema(Schema p_schema){
		schemaSet.add(p_schema);
		schemaSetHash.put(p_schema.getName(), p_schema);
	}
	
	public void addMapping(Mapping p_mapping){
		mappingSet.add(p_mapping);
	}
	
	public void printAllSchemas(){
		System.out.println("Schemas now in the network: ");
		for (int i = 0; i < schemaSet.size(); i++){
			System.out.print(" " + schemaSet.get(i));
		}
		System.out.println();
	}


	public Schema getMergedSchema()	{
		return m_schema;
	}

	public Vector getGLAVMapping()	{
		return m_glav;
	}

	/*
	 * now only consider three schemas
	 * later need to consider the inputs //four is okay now
	 * 
	 * TWO ASSUMPTIONS:
	 * 
	 * 1)all peers, if they indicate they are 
	 * using the mediated schema,  they are using 
	 * the current mediated schema
	 * 
	 * 2)each mapping is between two peer schemas; 
	 * this is the general case when people are 
	 * dealing with mapping in PDMS, like the mappings 
	 * in Piazza and Heptox.
	 * 
	 * STEPS:
	 * 1. get one mapping from the mapping set
	 * 2. get the schema info (number of schemas in this mapping,
	 *    and schema name) 
	 * 3. decompose it to several mapping statements (conjunctive mapping)
	 * 4. for each conjunctive mapping, check whether the head (concept)
	 *    has already been taken into the mediated schema already
	 * 5. if not, use the SemanticMerge (old)
	 * 6. if so, use need to process with MappingTable
	 * 7. process all other relations not in the mapping
	 */
	public void schemaMerge(){ //for more than 3                          // if exists then update, otherwise create
		for (int i = 0; i < mappingSet.size(); i++){
			Mapping cur_mapping = (Mapping) mappingSet.get(i);
			System.out.println("=====================");
			System.out.println("The " + i + " mapping: \n");
			System.out.println("MappingID:" + cur_mapping.MappingID);
			System.out.println("numberOfSchemas:" + cur_mapping.numberOfSchemas);
			int mappingID = cur_mapping.MappingID;
//			for (int j = 0; j < cur_mapping.numberOfSchemas; j++){
				//assume only two schemas for one mapping now
//				Schema s1 = (String)cur_mapping.schemaInfo.get(0); //get schema name
//				Schema s2 = (String)cur_mapping.schemaInfo.get(1);				
//			}
			
//			Schema schema1 = (Schema)schemaSetHash.get(s1); //get schema
            Schema schema1 = (Schema)cur_mapping.schemaInfo.get(0);
//			System.out.println("schemaSetHash.size = " + schemaSetHash.size());
//			System.out.println("schemaInfo.size = " + cur_mapping.schemaInfo.size());
			cur_mapping.printSchemas();
			System.out.println("schema1.getName = " + schema1.getName());
//			Schema schema2 = (Schema)schemaSetHash.get(s2);
            Schema schema2 = (Schema)cur_mapping.schemaInfo.get(1);
			System.out.println("schema2.getName = " + schema2.getName());
			int num_mapping_unions = cur_mapping.numMappingUnions();
			System.out.println("---------------------------numOfMappingUnion:" + cur_mapping.numMappingUnions());			
			
			
//			Schema tempm_schema = new Schema(); //mediated schema for current mapping
			for (int k = 0; k < num_mapping_unions; k++){
				System.out.println("k = " + k);
				Predicate m_rel = new Predicate(); //new mediated relation
				MappingStatementUnion msu = cur_mapping.MappingUnionI(k);
//				m_rel.setFunctionHead(msu.getHead());
			    
				int mtIndex = this.findMT(msu.getHead());
				System.out.println("mtIndex = " + mtIndex + "; msu.getHead = " + msu.getHead());

				MappingTable current;
				if (mtIndex < 0) { //new concept
					LinkedList<Attribute> attriList = msu.getIndirectAttribute();
					//System.out.println("--------------msu------------------  :" +msu.getIndirectAttribute());
					current = MergeSchema(m_rel, schema1, schema2, mappingID, msu);		 // schema just the ifc and citygml schema 
					//System.out.println("outside function if:" +m_rel.printString());
					//current.printMappingTable();
					mtSet.add(current);

					m_schema.addRelation(m_rel);
					//m_schema.addRelation("q1(x,y,z)");
					//System.out.println("mtSet number: " + mtSet.size());
					//System.out.println("m_schema's size: " + m_schema.numRelations());
                    System.out.println("name: " + m_schema.getName());
					//System.out.println("m_schema: " + m_schema.relationI(0).printString());
					computeGLAVMapping(m_rel, current, attriList);
					
				}
				else { //concept already exists 
					/* add codes here: mapping compatible check*/
					LinkedList<Attribute> attriList = msu.getIndirectAttribute();
					MappingTable oldMT =  (MappingTable)mtSet.get(mtIndex);
					current = MergeSchema(m_rel, schema1, schema2, mappingID, msu);
//					System.out.println("outside function else:" +m_rel.printString());
					current.printMappingTable();
					MappingTable newMT = current.combineMappingTable(oldMT);
					newMT.printMappingTable();
					mtSet.remove(mtIndex);
					mtSet.add(newMT);
					/* add codes here: get new mediated relation based on 
					 * the new MappingTable*/
					
//					System.out.println("m_schema is : " + m_schema.printString());
//					System.out.println("m_rel.getFunctionHead: " + m_rel.getFunctionHead());
					//System.out.println("position: " + m_schema.findRelationI(m_rel.getFunctionHead()));
					Predicate m_old_rel = m_schema.findRelation(m_rel.getFunctionHead());
					//Predicate m_old_rel = new Predicate("q1(x,y,z)");
//					System.out.println("old mediated relation1:");
//					System.out.println(m_old_rel.printString());
//					System.out.println("old mediated relation2:");
//					System.out.println(m_rel.printString());
					
					Predicate m_new_rel = computeNewMediatedRel(m_old_rel, m_rel, newMT);
//					System.out.println("m_new_rel: " + m_new_rel.printString());
					m_schema.delRelation(m_rel.getFunctionHead());
					m_schema.addRelation(m_new_rel);
					
					//remove old GLAV for that concept first
					removeGLAVMapping(m_new_rel);                  // remove the old one, keep the newest one
					computeGLAVMapping(m_new_rel, newMT, attriList); 

				}	
				

				
			}
		}
	}
	
	public void removeGLAVMapping(Predicate m_new_rel){
		for (int i = 0; i < m_glav.size(); i++){
			GLAVMapping a = (GLAVMapping) m_glav.elementAt(i);
			Predicate new_sg = new Predicate(m_schema.getName() + "." + m_new_rel.printString());			
			int index = a.findExistingGVByConcept(new_sg);
			if (index >= 0){
				((GLAVMapping) m_glav.elementAt(i)).removeGLAV(index);				
			}
		}
	}
	
	public void computeGLAVMapping(Predicate m_new_rel, MappingTable newMT, LinkedList<Attribute> attriList){
		
		System.out.println("----------111--------------m_new_rel: " + m_new_rel.printString()); // the mediated schema
		//start building m_glav
		String next_rel;
		String cur_rel;
		String next_schema;
		String cur_schema;
		Vector attributes = new Vector();
		View a_view = new View();
		int clear = 0;
		String pre = "";
		System.out.println("--------------------------newMT.name: " + newMT.name);
		for (int i = 1; i < newMT.rowNum; i++){	
			System.out.println("-------------------i-------clear: " +i +"  "+clear);
			if (clear == 1){
				attributes = new Vector();
				a_view = new View();
				clear = 0;
			} else {
				View temp = a_view;
				a_view = new View();
				a_view = temp.copy();
				//System.out.println("--------------------------temp: " + temp.printString());
			}
			cur_rel = (String)newMT.relStrings.elementAt(i); // the rel is the XXX.YYY
			System.out.println("cur_rel: " + cur_rel);
			cur_schema = getSchemaFromRel(cur_rel);          // this returns the schema's owner
			System.out.println("cur_schema: " + cur_schema); 
			if (cur_schema.equals("")){
				System.out.println("here cur_schema is nothing, continue");
				attributes = new Vector();
				a_view = new View();
				clear = 0;
				continue;
			}
			if (i == newMT.rowNum - 1){
				next_rel = "";
			}
			else {
				next_rel = (String)newMT.relStrings.elementAt(i + 1);
			}
			next_schema = getSchemaFromRel(next_rel);
			System.out.println("next_schema: " + next_schema); 
			//construct subgoal
			Predicate sg = new Predicate();
			sg.setFunctionHead(cur_rel);
			for (int j = 0; j < newMT.colNum; j++){
				if (newMT.attrMapping[i][j] != 0){
					IPValue cur_var = m_new_rel.variableI(j);
					sg.addVariable(cur_var);
					//System.out.print("    "+cur_var.printString());
					if (!attributes.contains(cur_var)){
						attributes.addElement(cur_var);
					}
				}
			}
			//System.out.println("    ");
			a_view.addSubgoal(sg);             // a_view will keep the schema from last iterate and add the current one
			//System.out.println("add " + sg.printString() + "to subgoal");
			if (!cur_schema.equals(next_schema)){
            //need to create a new glav
				int schemaI = getSchemaIndex(cur_schema);
				//System.out.println("---------------------------------schemaI: "+schemaI);
				if (schemaI < 0){
					continue;
				}
				Predicate query_head = new Predicate();
				for (int k = 0; k < attributes.size();k++){
					//System.out.println(" --------mapping head----------------------------------   "+((IPValue)attributes.elementAt(k)).printString());
					query_head.addVariable(((IPValue)attributes.elementAt(k)).printString());    // it will add the attributes from last iterate 
				}
				
				// add the indirect attributes
				if(attriList!=null&&attriList.size()>=0) {
					for(int k=0; k<attriList.size(); k++) {
						Attribute attri = attriList.get(k);
						System.out.println("----!!!!!!!!!--------------schema---------------: "+cur_schema+"   "+attri.getSchema()+"   "+attri.getName());
						if(!cur_schema.equals(attri.getSchema())) {
							query_head.addVariable(attri.getName());
							//sg.addVariable(attri.getMapping());
							a_view.addExpress(attri.getName()+"="+attri.getMapping());
							System.out.println("----!!!!!!!!!-----------------------------attri: "+attri.getName()+"   "+attri.getMapping());
						}
					}
				}
				
				
				
				
				//System.out.println("    ");
				/*
				 * Note: need to check whether a_view already 
				 * in the LV and exactly the same
				 * but here, just use the simplest method to check
				 * Here, assume, one concept only has one GLAV 
				 * to one local schema
				 * Add more later.
				 */
				GLAVMapping a = (GLAVMapping) m_glav.elementAt(schemaI);
				
				//System.out.println("m_new_rel: " + m_new_rel.printString());
//				System.out.println(""+a.printString());
				
				Predicate new_sg = new Predicate(m_schema.getName() + "." + m_new_rel.printString());
				System.out.println("add new_sg " + new_sg.printString() + "to subgoal");
				if (a.findExistingGVByHead(new_sg) != null){ //already exist
//					System.out.println("duplicate");
					clear = 1;
					continue;
				}
				System.out.println("-----SchemaName   "+getSchemaNameFromRel(cur_rel));
				query_head.setFunctionHead(((GLAVMapping) 
						m_glav.elementAt(schemaI)).getCurrentFreeName()+"_"+newMT.name);	 // this is just the name of the schema change the function head
				//System.out.println("-----getCurrentFreeName   "+((GLAVMapping) m_glav.elementAt(schemaI)).getCurrentFreeName());
				System.out.println("-----query_head   "+query_head.printString());
				a_view.setHead(query_head);
				//System.out.println("-----------m_glav   numLVs: "+schemaI+"   " + ((GLAVMapping) m_glav.elementAt(schemaI)).numLVs());
				((GLAVMapping) m_glav.elementAt(schemaI)).addLV(a_view);
				//System.out.println("-----------m_glav   numLVs: "+schemaI+"   " + ((GLAVMapping) m_glav.elementAt(schemaI)).numLVs());
				System.out.println("subgoal constructed: " + a_view.printString());
				a_view = new View();
				a_view.setHead(query_head);
				//Predicate new_sg = new Predicate(m_schema.getName() + "." + m_new_rel.printString());
				//a_view.addSubgoal(m_new_rel);
				a_view.addSubgoal(new_sg);
				//System.out.println("---subgoal constructed: " + a_view.printString());
				//System.out.println("-----------m_glav   numGVs: "+schemaI+"   " + ((GLAVMapping) m_glav.elementAt(schemaI)).numGVs());
				((GLAVMapping) m_glav.elementAt(schemaI)).addGV(a_view);
        		((GLAVMapping) m_glav.elementAt(schemaI)).incrementNewFreeNameNum();
        		//System.out.println("-----------m_glav   numGVs: "+schemaI+"   " + ((GLAVMapping) m_glav.elementAt(schemaI)).numGVs());
        		
        		clear = 1;              // which will create a new mapping for the next schema
			}
		}
	}
	
	public String getSchemaFromRel(String rel){
		int startI = 0;
		int endI = rel.indexOf(".");
		if (endI >= 0){
			return rel.substring(startI, endI);
		}
		return "";
	}
	
	public String getSchemaNameFromRel(String rel){
		int startI = 0;
		int endI = rel.indexOf(".");
		if (endI >= 0){
			return rel.substring(endI+1);
		}
		return "";
	}
	
	public int getSchemaIndex(String schemaName){
		for (int i = 0; i < m_glav.size(); i++){
			GLAVMapping glavm = (GLAVMapping)m_glav.elementAt(i);
			if (glavm.getSchema().equals(schemaName)){
				return i;
			}
		}
		return -1;
	}
	
	public Predicate computeNewMediatedRel(Predicate m_old_rel, 
			Predicate m_rel, MappingTable MT){
		Predicate m_update = new Predicate();
		m_update.setFunctionHead(m_rel.getFunctionHead());
		int index1 = 0, index2 = 0;  //find the two intermediated relation head in MT
		int tag = 0;
		for (int i = 1; i < MT.rowNum; i++){
			String head = (String) MT.relStrings.get(i);
			if (tag == 0 && head.equals(MT.name)){
				index1 = i;				
				tag = 1;
				continue;
			}
			else if (tag == 1 && head.equals(MT.name)){
				index2 = i;
				break;
			}			
		}
		for (int i = 0; i < MT.colNum; i++){
			IPValue var;
			if (MT.attrMapping[index1][i] != 0){
				var = m_rel.variableI(MT.attrMapping[index1][i] - 1);
				m_update.addVariable(var);
			}
			else if(MT.attrMapping[index2][i] != 0) {
				var = m_old_rel.variableI(MT.attrMapping[index2][i] - 1);
				m_update.addVariable(var);
			}
		}
		return m_update;
	}
	
	public MappingTable MergeSchema(Predicate m_rel, Schema schema1, Schema schema2, 
			int mappingID,  MappingStatementUnion msu){
//		Predicate a_rel = new Predicate();
		m_rel.setFunctionHead(msu.getHead());
		Vector attributes = msu.getVariables();
		int num_attrs = attributes.size();
		for (int j = 0; j < num_attrs; j++){
			m_rel.addVariable((String)attributes.elementAt(j));
		}//done dealing with a mapping statment
		
		MappingTable mt = new MappingTable(m_rel, mappingID,  msu);
	
		return mt;
	}
	
	public int findMT(String concept){
		for (int i = 0; i < mtSet.size(); i++){
			MappingTable a = (MappingTable)mtSet.get(i);
			if (a.name.equals(concept)){
				return i;
			}
		}
		return -1;
	}
	
	public static void testSchemaMerge(){
		/*initialize with three schemas and two mappings*/
		SchemaMediationTest s_merge = new SchemaMediationTest();
		Schema s1 = new Schema();
		s1.setName("Go");
		s1.addRelation("Go.flight(f-num, time, meal)");
		s1.addRelation("Go.price(f-num, date, price)");
//		s1.addRelation("UBC.proj_area(project, area)");
		Schema s2 = new Schema();
		s2.setName("Ok");
		s2.addRelation("Ok.flight(f-num, date, time, price, nonstop)");
//		s2.addRelation("UW.project(univ, project)");
//		s2.addRelation("UW.proj_lab(project, lab)");
		Mapping map1 = new Mapping();
		
		View ms1 = new View("Flight(f-num, date, time, price):-Go.flight(f-num, time, meal), Go.price(f-num, date, price)");
		View ms2 = new View("Flight(f-num, date, time, price):-Ok.flight(f-num, date, time, price, nonstop)");
		map1.addView(ms1);
		map1.addView(ms2);
		
//		ms1 = new View("project(univ,project):-" +
//				"UBC.univ_proj(univ, project)," +
//				"UBC.proj_area(project, area)");
//		ms2 = new View("project(univ,project):-" +
//				"UW.project(univ, project), " +
//				"UW.proj_lab(project, lab)");
//		map1.addView(ms1);
//		map1.addView(ms2);
		
		map1.addSchema(s1);
		map1.addSchema(s2);
		s_merge.addSchema(s1);
		s_merge.addSchema(s2);
		s_merge.addMapping(map1);
	

//		Schema s3 = new Schema();
//		s3.setName("UT");
//		s3.addRelation("UT.conference-paper(venue, title, first-author)");
//		s3.addRelation("UT.conference-paper2(venue, title, link)");
//		Mapping map2 = new Mapping();
//		ms1 = new View("conf-paper(title, venue):-" +
//				"UBC.conf-paper(title, venue, year, pages)");
//		ms2 = new View("conf-paper(title, venue):-" +
//				"UT.conference-paper(venue, title, first-author), " +
//				"UT.conference-paper2(venue, title, link)");
//		map2.addView(ms1);
//		map2.addView(ms2);
//		map2.addSchema(s1);
//		map2.addSchema(s3);
//		s_merge.addSchema(s3);
//		s_merge.addMapping(map2);
//		
//		Schema s4 = new Schema();
//		s4.setName("UCB");
//		s4.addRelation("UCB.conf-paper(title, location");
//		s4.addRelation("UCB.conf-paper1(title, abstract");
//		Mapping map3 = new Mapping();
//		ms1 = new View("conf-paper(title, venue):-" +
//				"UCB.conf-paper(title, venue), " +
//				"UCB.conf-paper1(title, abstract)");
//		ms2 = new View("conf-paper(title, venue):-" +
//				"UT.conference-paper(venue, title, first-author), " +
//				"UT.conference-paper2(venue, title, link)");
//		map3.addView(ms1);
//		map3.addView(ms2);
//		map3.addSchema(s3);
//		map3.addSchema(s4);
//		s_merge.addSchema(s4);
//		s_merge.addMapping(map3);
//		
//		Mapping map4 = new Mapping();
//		ms1 = new View("conf-paper(title, venue):-" +
//				"UT.conference-paper(venue, title, first-author), " +
//		"UT.conference-paper2(venue, title, link)");
//		ms2 = new View("conf-paper(title, venue, link):-" +
//				"UW.conf-paper(title, venue, year, link)");
//		map4.addView(ms1);
//		map4.addView(ms2);
//		map4.addSchema(s3);
//		map4.addSchema(s2);
//		s_merge.addMapping(map4);

		int num_local_schema = s_merge.schemaSet.size();
		for (int i = 0; i < num_local_schema; i++){ 
			Schema local_schema = (Schema)s_merge.schemaSet.elementAt(i);
			GLAVMapping glav = new GLAVMapping(local_schema.getName(), "");
			
	//		glav.setSchema(local_schema.getName());
			s_merge.m_glav.add(glav);
		}
		s_merge.schemaMerge();
		System.out.println("---------------------------after schema merge-------------------------------------------");
		System.out.println(s_merge.getMergedSchema().printString());
		for (int i = 0; i < s_merge.getGLAVMapping().size(); i++ ){
			System.out.println("GLAV for " + ((GLAVMapping)s_merge.getGLAVMapping().elementAt(i)).schemaName);
			System.out.println(((GLAVMapping)s_merge.getGLAVMapping().elementAt(i)).printString());
		}
	}
	
	
	public static void testSchemaMerge1(){
		/*initialize with three schemas and two mappings*/
		SchemaMediationTest s_merge = new SchemaMediationTest();
		Schema s1 = new Schema();
		s1.setName("UBC");
		s1.addRelation("UBC.conf-paper(title, venue, year, pages)");
		s1.addRelation("UBC.univ_proj(univ, project)");
		s1.addRelation("UBC.proj_area(project, area, title)");
		Schema s2 = new Schema();
		s2.setName("UW");
		s2.addRelation("UW.conf-paper(title, venue, year, url)");
		s2.addRelation("UW.project(univ, project)");
		s2.addRelation("UW.proj_lab(project, lab, title)");
		Mapping map1 = new Mapping();
		
		View ms1 = new View("conf-paper(title, venue, year):-" +
				"UBC.conf-paper(title, venue, year, pages)");
		View ms2 = new View("conf-paper(title, venue, year):-" +
				"UW.conf-paper(title, venue, year, url)");
		map1.addView(ms1);
		map1.addView(ms2);
		
		ms1 = new View("project(univ,project,title):-" +
				"UBC.univ_proj(univ, project)," +
				"UBC.proj_area(project, area, title)");
		ms2 = new View("project(univ,project,title):-" +
				"UW.project(univ, project), " +
				"UW.proj_lab(project, lab, title)");
		map1.addView(ms1);
		map1.addView(ms2);
		
		map1.addSchema(s1);
		map1.addSchema(s2);
		s_merge.addSchema(s1);
		s_merge.addSchema(s2);
		s_merge.addMapping(map1);
	

//		Schema s3 = new Schema();
//		s3.setName("UT");
//		s3.addRelation("UT.conference-paper(venue, title, first-author)");
//		s3.addRelation("UT.conference-paper2(venue, title, link)");
//		Mapping map2 = new Mapping();
//		ms1 = new View("conf-paper(title, venue):-" +
//				"UBC.conf-paper(title, venue, year, pages)");
//		ms2 = new View("conf-paper(title, venue):-" +
//				"UT.conference-paper(venue, title, first-author), " +
//				"UT.conference-paper2(venue, title, link)");
//		map2.addView(ms1);
//		map2.addView(ms2);
//		map2.addSchema(s1);
//		map2.addSchema(s3);
//		s_merge.addSchema(s3);
//		s_merge.addMapping(map2);
//		
//		Schema s4 = new Schema();
//		s4.setName("UCB");
//		s4.addRelation("UCB.conf-paper(title, location");
//		s4.addRelation("UCB.conf-paper1(title, abstract");
//		Mapping map3 = new Mapping();
//		ms1 = new View("conf-paper(title, venue):-" +
//				"UCB.conf-paper(title, venue), " +
//				"UCB.conf-paper1(title, abstract)");
//		ms2 = new View("conf-paper(title, venue):-" +
//				"UT.conference-paper(venue, title, first-author), " +
//				"UT.conference-paper2(venue, title, link)");
//		map3.addView(ms1);
//		map3.addView(ms2);
//		map3.addSchema(s3);
//		map3.addSchema(s4);
//		s_merge.addSchema(s4);
//		s_merge.addMapping(map3);
//		
//		Mapping map4 = new Mapping();
//		ms1 = new View("conf-paper(title, venue):-" +
//				"UT.conference-paper(venue, title, first-author), " +
//		"UT.conference-paper2(venue, title, link)");
//		ms2 = new View("conf-paper(title, venue, link):-" +
//				"UW.conf-paper(title, venue, year, link)");
//		map4.addView(ms1);
//		map4.addView(ms2);
//		map4.addSchema(s3);
//		map4.addSchema(s2);
//		s_merge.addMapping(map4);

		int num_local_schema = s_merge.schemaSet.size();
		for (int i = 0; i < num_local_schema; i++){ 
			Schema local_schema = (Schema)s_merge.schemaSet.elementAt(i);
			GLAVMapping glav = new GLAVMapping(local_schema.getName(), "");
			
	//		glav.setSchema(local_schema.getName());
			s_merge.m_glav.add(glav);
		}
		s_merge.schemaMerge();
		System.out.println("---------------------------after schema merge-------------------------------------------");
		System.out.println(s_merge.getMergedSchema().printString());
		for (int i = 0; i < s_merge.getGLAVMapping().size(); i++ ){
			System.out.println("GLAV for " + ((GLAVMapping)s_merge.getGLAVMapping().elementAt(i)).schemaName);
			System.out.println(((GLAVMapping)s_merge.getGLAVMapping().elementAt(i)).printString());
		}
	}
	
	public static void testIFCCityGMLSchemaMerge(){
		/*initialize with three schemas and two mappings*/
		
		SchemaMediationTest s_merge = new SchemaMediationTest();
		Schema ifc_s = new Schema();
		Schema citygml_s = new Schema();
		ifc_s.setName("IFC");
		citygml_s.setName("CityGML");
		Mapping map1 = new Mapping();
		
		StringBuffer a_buffer = new StringBuffer();
		String line;
		try
		{
			FileReader file = new FileReader("C:\\ifc_data\\IFC_source_data.txt");
			System.out.println("Start reading file:   ");
			BufferedReader input = new BufferedReader(file);
			line = input.readLine();
			while (line != null)
			{
				if(!"".equals(line.trim()))
					ifc_s.addRelation(line);
				line = input.readLine();
			}
			
			FileReader file1 = new FileReader("C:\\ifc_data\\CityGML_source_data.txt");
			BufferedReader input1 = new BufferedReader(file1);
			line = input1.readLine();
			while (line != null)
			{
				if(!"".equals(line.trim()))
					citygml_s.addRelation(line);
				line = input1.readLine();
			}
			
			FileReader file2 = new FileReader("C:\\ifc_data\\mapping.txt");
			BufferedReader input2 = new BufferedReader(file2);
			String line1, line2;
			line1 = input2.readLine();
			line2 = input2.readLine();
			while (line1 != null)
			{
//				System.out.println("---1---"+line1);
//				System.out.println("---2---"+line2);
				View ms1 = new View(line1);
				View ms2 = new View(line2);
				map1.addView(ms1);
				map1.addView(ms2);
				line1 = input2.readLine();
				line2 = input2.readLine();
			}
			
			file.close();
			input.close();
			file1.close();
			input1.close();
			file2.close();
			input2.close();
		} catch(Exception e)
		{
			System.out.println("can't open file");
		}
				
		map1.addSchema(ifc_s);
		map1.addSchema(citygml_s);
		s_merge.addSchema(ifc_s);
		s_merge.addSchema(citygml_s);
		s_merge.addMapping(map1);
	
		int num_local_schema = s_merge.schemaSet.size();
		for (int i = 0; i < num_local_schema; i++){ 
			Schema local_schema = (Schema)s_merge.schemaSet.elementAt(i);
			GLAVMapping glav = new GLAVMapping(local_schema.getName(), "");
			
	//		glav.setSchema(local_schema.getName());
			s_merge.m_glav.add(glav);
		}
		s_merge.schemaMerge();
		System.out.println(s_merge.getMergedSchema().printString());
		for (int i = 0; i < s_merge.getGLAVMapping().size(); i++ ){
			System.out.println("GLAV for " + ((GLAVMapping)s_merge.getGLAVMapping().elementAt(i)).schemaName);
			System.out.println(((GLAVMapping)s_merge.getGLAVMapping().elementAt(i)).printString());
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Start merging:");
		//testSchemaMerge1();
		testIFCCityGMLSchemaMerge();
	}

}

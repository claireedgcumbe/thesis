package mediation;

/*
 * created on April 9, 2006
 * author by jzhao
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import minicon.*;

public class GLAVMapping implements Serializable{
	//this GLAVMapping is associated with each schema
	protected String schemaName ;
	protected Vector m_lv;
	protected Vector m_gv;
	protected static int m_new_free_name_num = 0;
	protected String m_my_free_name;
//	protected String m_new_free_name;
//	protected static String m_new_free_var = "_gvvar";
//	protected String m_my_free_var;
//	protected int m_next_free_num;
	
	public GLAVMapping(String schemaName1, String schemaName2){
		m_lv = new Vector();
		m_gv = new Vector();
		m_my_free_name = "Q_" + schemaName1; // +"_" + schemaName2 + "_" + m_new_free_name_num;
		this.schemaName = schemaName1;
		m_new_free_name_num++;
//		m_my_free_var = m_new_free_var + "I";
//		m_new_free_name = m_my_free_name;
//		m_new_free_var = m_my_free_var;
//		m_next_free_num = 0;
	}
	
	public GLAVMapping(){
		m_lv = new Vector();
		m_gv = new Vector();
		m_my_free_name = "Q_" + m_new_free_name_num;
//		this.schemaName = schemaName1;
		m_new_free_name_num++;
//		m_my_free_var = m_new_free_var + "I";
//		m_new_free_name = m_my_free_name;
//		m_new_free_var = m_my_free_var;
//		m_next_free_num = 0;
	}
	
	public void setSchema(String p_schema)
	{
		schemaName = p_schema;
	}
	
	public String getSchema()
	{
		return schemaName;
	}
/*	
	protected int getNextFreeNum()
	{
		return m_next_free_num;
	}
*/	
	public String getCurrentFreeName()
	{
		String myname = m_my_free_name;
		m_new_free_name_num++;
		return myname;
		
	}
	
	public void incrementNewFreeNameNum(){
		m_new_free_name_num++;
	}
	
/*
	public void incrementNextFreeNum()
	{
		m_next_free_num++;
	}
*/	
	public void addLV(View p_view)
	{
		m_lv.addElement(p_view);
	}
	
	public void addGV(View p_view)
	{
		m_gv.addElement(p_view);
	}
	
	public void removeLV(int LVI){
		m_lv.remove(LVI);
	}
	
	public void removeGV(int GVI){
		m_gv.remove(GVI);
	}
	
	public void removeGLAV(int index){
		m_lv.remove(index);
		m_gv.remove(index);
	}
	
	public int numLVs(){
		return m_lv.size();
	}
	public int numGVs(){
		return m_gv.size();
	}
	
	public View lvI(int p_i)
	{
		return (View)m_lv.elementAt(p_i);
	}
	
	public View gvI(int p_i)
	{
		return (View)m_gv.elementAt(p_i);
	}
	
	public boolean containHead(String headfunc){
		int i;
		int num_gvs = numGVs();
		View a_view;
		for (i = 0; i < num_gvs; i++){
			a_view = gvI(i);
			if (headfunc.equals(a_view.getHead().getFunctionHead())){
				return true;
			}
			
		}
		return false;		
	}
	
	public Statement useGVToRewriteQueryPredicate(Predicate p_query_pred)
	{
		//expand a particular view definition

		System.out.println("p_query_pred: " + p_query_pred.printString());
		View a_view = findRelevantGV(p_query_pred);         // find a mapping which has the same head as p_query_pred
		System.out.println("a_view: " + a_view.printString());
		View retval = new View(a_view);
		Query filler = new Query();
		GAVMD mapping = new GAVMD(filler,a_view);//okay, this is bad, but may work.
		System.out.println("mapping: " + mapping.printString());
		//first, set up the mappings
		Predicate a_view_pred = a_view.getHead();
		System.out.println("a_view_pred: " + a_view.getHead().printString());
		for (p_query_pred.first(), a_view_pred.first(); ! a_view_pred.isDone();a_view_pred.next(), p_query_pred.next())
		{
			minicon.Mapping a_var_mapping =new minicon.Mapping(p_query_pred.current(),a_view_pred.current());  // the variable in the predicate
			mapping.addMapping(a_var_mapping);                                                                 // create the mapping of the variables in the predicate
			System.out.println("p_query_pred.current(): " + p_query_pred.current().printString());             
			System.out.println("a_view_pred.current(): " + a_view_pred.current().printString());
			
		}
		Predicate a_pred;
		IPValue a_var;
		int pred_iter;
		int query_iter;
		int i,j;
		for (a_view.first(),query_iter = 0, i = 0; ! a_view.isDone(); a_view.next(), query_iter++, i++)
		{
			a_pred = a_view.current();               // the subgoal of the mapping
			System.out.println("a_pred: " + a_pred.printString());
			for (a_pred.first(), pred_iter = 0, j = 0;!a_pred.isDone(); a_pred.next(), pred_iter++, j++)
			{
				a_var = a_pred.current();         // the variable in each subgoal
				System.out.println("a_var: " + a_var.printString());
				System.out.println("retval.subgoalI(i): " + retval.subgoalI(i).printString());
				//System.out.println("mapping.getVar(a_var): " + mapping.getVar(a_var).printString());
				//System.out.println("a_var: " + a_var.printString());
				retval.subgoalI(i).replaceVariableI(mapping.getVar(a_var),j);
				
			}//end looping over the predicate
			
		}//end looping over the view
		return (Statement) retval;
	}
	
	public View findRelevantGV(Predicate p_query_head)
	//Note: this function assumes that there is only one 
	//view with a given IDB name.  Should be true for
	//the particular case I'm doing, but not in general
	{
		int i;
		int num_gvs = numGVs();
		View a_view;
		for (i = 0; i < num_gvs; i++)
		{
			a_view = gvI(i);
			System.out.println("a_view: " + a_view.getHead().getFunctionHead());
			System.out.println("p_query_head: " + p_query_head.getFunctionHead());
			if (p_query_head.getFunctionHead().equals(a_view.getHead().getFunctionHead())){
				return a_view;
			}
		}
		return null;
	}//end public View findRelevantView(Query p_query) 

	/*
	 * April 10
	 * author by jzhao
	 */
	public View findExistingGVByHead(Predicate p_query_head)
	{
		int i;
		int num_gvs = numGVs();
		View a_view;
		for (i = 0; i < num_gvs; i++)
		{
			a_view = gvI(i);
			if (a_view.containsSubgoal(p_query_head)){	
				return a_view;
			}
		}
		return null;
	}
	
	public int findExistingGVByConcept(Predicate p_query_head)
	{
		int i;
		int num_gvs = numGVs();
		View a_view;
		for (i = 0; i < num_gvs; i++)
		{
			System.out.println("num_gvs = " + num_gvs + " i = " + i);
			a_view = gvI(i);
			
			String a = a_view.subgoalI(0).getFunctionHead();
			System.out.println("a: " + a);
			int index = a.indexOf(".");		
			String result = a.substring(index + 1);

			
//			System.out.println("inside findExistingGVByConcept");
//			System.out.println("p_query_head:" + p_query_head.getFunctionHead());
//			System.out.println("a_view:" + a_view.subgoalI(0).getFunctionHead());
			if (p_query_head.getFunctionHead().equals(a) ||
					p_query_head.getFunctionHead().equals(result)){
				return i;
			}
		}
		return -1;
	}
	
	public ArrayList<View> findRelevantGVs(Predicate p_query_body)
	//Note: this function assumes that there is only one 
	//view with a given IDB name.  Should be true for
	//the particular case I'm doing, but not in general
	{
		int i;
		int num_lvs = numLVs();
		ArrayList<View> GVs = new ArrayList<View>();
		View lv_view;
		for (i = 0; i < num_lvs; i++)
		{
			lv_view = lvI(i);
//			System.out.println("p_query_head:" + p_query_body.getFunctionHead());
//			System.out.println("lv_view:" + lv_view.current().getFunctionHead()); 
//			System.out.println("num_gvs:" + numGVs());
			if (p_query_body.getFunctionHead().equals(lv_view.current().getFunctionHead())){
				System.out.println("gvI:" + gvI(i).printString());
				GVs.add(gvI(i));
			}
		}
		return GVs;
	}//end public View findRelevantView(Query p_query) 

	
	//jzhao added: March 22, 2006
	public ArrayList<View> findRelevantLVs(Predicate p_query_body){
		int i;
		int num_gvs = numGVs();
		ArrayList<View> LVs = new ArrayList<View>();;
		View gv_view;
		for (i = 0; i < num_gvs; i++){
			gv_view = gvI(i);
//			System.out.println("p_query_head:" + p_query_body.getFunctionHead());
//			System.out.println("gv_view:" + gv_view.current().getFunctionHead()); 
//			System.out.println("numLVs:" + numLVs());
			if (p_query_body.getFunctionHead().equals(gv_view.current().getFunctionHead())){
				LVs.add(lvI(i));
			}
		}
		return LVs;
	}
	
	public View findRelevantLVsByHead(Predicate head){
		int i;
		int num_lvs = numLVs();
		View lv_view = new View();
		for (i = 0; i < num_lvs; i++){
			lv_view = lvI(i);
			System.out.println("head:" + head.getFunctionHead());
			if (head.getFunctionHead().equals(lv_view.getHead().getFunctionHead())){
				return lv_view;
			}
		}
		return null;
	}
	
	public String printString()
	{
		StringBuffer retval = new StringBuffer();
		int i, num_views;
		View a_view;
		
		retval.append("\nGlobal Views:\n");
		num_views = m_gv.size();
		for (i = 0; i < num_views; i++){
			a_view = (View)m_gv.elementAt(i);
			retval.append("\t" + a_view.printString() + "\n");
		}
		
		retval.append("Local Views:\n");
		num_views = m_lv.size();

		for (i = 0; i < num_views; i++){
			a_view = (View)m_lv.elementAt(i);
			retval.append("\t" + a_view.printString() + "\n");
		}

		return retval.toString();
	}
	
	// find the position of the attributes in the head of the query in the body 
	public void mappingAttribute(Predicate goal, Predicate relation, HashMap<String, String> attris) {
		
		System.out.println("goal:" + goal.printString());
		System.out.println("relation:" + relation.printString());
		int num_body = goal.size();
		for(int i=0; i<num_body; i++) {
			if(!relation.variableI(i).printString().equals(goal.variableI(i).printString())) {
				String name = relation.variableI(i).printString();
				System.out.println(" map : " + goal.variableI(i).printString()+"  "+name);
				attris.put(name, goal.variableI(i).printString()); // put the new variable in the query and the old variable name
			}
			
		}
		
	}
	
	public ArrayList<Query> queryReformulation(Query query, ArrayList<Predicate> meditateList) {
		
		ArrayList<Query> result = new ArrayList<Query>();
		int subgoalNum = query.size();
		System.out.println("query size:" + subgoalNum);
		query.first();
		Predicate queryBody = query.current();
		Predicate query_head = query.getHead();
		HashMap<String, String> attris = new HashMap<String, String>();
		System.out.println("queryBody:" + queryBody.printString());
		System.out.println("mediatedSchema:" + meditateList.get(0).getFunctionHead());
		for (int i = 0; i < subgoalNum; i++, query.next()){
			Predicate subgoal = query.current();
			if(subgoal.getFunctionHead().equals(meditateList.get(i).getFunctionHead())) {   // if the query is asked on the mediated schema, then find the corresponding source schema
			    System.out.println("current subgoal if:" + subgoal.printString());  // check the LAVs
				ArrayList<View> lav = findRelevantLVs(subgoal);
				System.out.println("lav size:" + lav.size());
				mappingAttribute(query.subgoalI(i), meditateList.get(i), attris);
				ArrayList<Query> temp = new ArrayList<Query>();
				for(int k=0; k<lav.size(); k++) {
					System.out.println("lav :" + lav.get(k).printString());
					// check if they have the attributes
					Predicate mapping_head = lav.get(k).getHead();
					int num_attri = query_head.size();
					boolean flag = true;
					// change the variable name to the ones in the query
					//String body = lav.get(k).getBody();
					for(String str:attris.keySet()) {
						int pos = mapping_head.variableIsAt(str);
						System.out.println("    pos  " + pos+"   "+str);
						if(pos>-1)
							mapping_head.replaceVariableI(attris.get(str), pos);
						
					}
					Query qry = new Query();
					qry.setHead(query.getHead());
					qry.addSubgoal(mapping_head);
					temp.add(qry);
					System.out.println("query :" + qry.printString());
					//System.out.println("------------------query:" + qry.printString());
				}
				result = temp;
			} else {
			    System.out.println("current subgoal else:" + subgoal.printString());  // check the GAVs
				if(result!=null&&result.size()>0) {
					for(int id=0; id<result.size(); id++) {
						result.get(id).addSubgoal(subgoal);
					}	
				} else {
					Query qry = new Query();
					qry.setHead(query.getHead());
					qry.addSubgoal(subgoal);
					result.add(qry);
				}
			}
		}
		
		for(int id=0; id<result.size(); id++) {
			
			Query qry = result.get(id);
			Predicate pre = qry.getHead();
			int num_p = pre.size();
			int num_g = qry.size();
			for(int np=0; np<num_p; np++) {
				boolean flag = false;
				for(int ng=0; ng<num_g; ng++) {
					Predicate p = qry.subgoalI(ng);
					if(p.containsVariable(pre.variableI(np).printString()))
						flag = true;
				}
				if(!flag)
					result.remove(id);
			}
			
			//System.out.println(result.get(id).printString());
		}
		ArrayList<Query> real_result = new ArrayList<Query>();
		for(int id=0; id<result.size(); id++) {
			int num_g = result.get(id).size();
			Query qry = result.get(id);
			Query q = new Query();
			q.setHead(qry.getHead());
			System.out.println("---num_g---"+num_g);
			for(int i=0; i<num_g; i++) {
				Predicate body = qry.subgoalI(i);
				System.out.println("---body---"+body.printString());
				View lav = findRelevantLVsByHead(body);
				System.out.println("---lav---"+lav.printString());
				int num_subg = lav.size();
				for(int j=0; j<num_subg; j++) {
					for(String str:attris.keySet()) {
						Predicate pre = lav.subgoalI(j);
						System.out.println("pre:" + pre.printString());
						int pos = pre.variableIsAt(str);
						System.out.println("    pos  " + pos+"   "+str);
						if(pos>-1)
							pre.replaceVariableI(attris.get(str), pos);
					}
					q.addSubgoal(lav.subgoalI(j));
				}
				ArrayList<String> express = lav.getExpress();
				if(express.size()>0) {
					q.setExpress(lav.getExpress());
					for(int k=0; k<express.size(); k++) {
						System.out.println("express:" + express.get(k));
						for(String str:attris.keySet()) {
							String new_str = attris.get(str);
							String old_str = str;
							System.out.println("old_str:" + old_str+"   "+"new_str:" + new_str);
							String str1 = express.get(k).replace(old_str, new_str);
							q.setExpressByIndex(str1, k);
						}
					}
					
				}
			}
			real_result.add(q);
		}
			
		

		
		for(int id=0; id<real_result.size(); id++) {
			System.out.println(real_result.get(id).printString());
		}
				
		return real_result;
		
	}
	
	
	public static void testGVRewriting()
	{
		View ms1 = new View("v(a):-e1(a,b)");
		Query query = new Query("q(x):-v(x)");
		GLAVMapping a_map = new GLAVMapping("", "");
		a_map.addGV(ms1);
		System.out.println(a_map.useGVToRewriteQueryPredicate(query.subgoalI(0)).printString());
	}
	
//	Global Views:
//		Q_UBC__0(title,venue,year,pages):-M.conf-paper(title,venue,year,pages,url)
//	Local Views:
//		Q_UBC__0(title,venue,year,pages):-UBC.conf-paper(title,venue,year,pages)
//
//	GLAV for UW
//	Global Views:
//		Q_UW__1(title,venue,year,url):-M.conf-paper(title,venue,year,pages,url)
//	Local Views:
//		Q_UW__1(title,venue,year,url):-UW.conf-paper(title,venue,year,url)
	public static void testIFCCityGMLGVRewriting()
	{
		View ms1 = new View("M1(b,c):-M.S(b,c,e,f)");
		View ms2 = new View("M2(b,c,e,f):-M.S(b,c,e,f)");
		View ms3 = new View("M1(b,c):-S1(b,c)");
		View ms4 = new View("M2(b,c,e,f):-S2(e,f,c), b=e+f");
		Query query = new Query("q(x):-M.S(x,y,z,w)");
		GLAVMapping a_map = new GLAVMapping("S1", "S2");
		ArrayList<Predicate> meditateList = new ArrayList<Predicate>();
		Predicate mediatedSchema1 = new Predicate("M.S(b,c,e,f)");
		
//		View ms1 = new View("Q_IFC_Room(space_name,number,description,building_name,room_type,floor,area,height,width,length,perimeter,volume,hasWindow,interiorOrExteriorSpace,room_name):-M.Room(space_name,number,description,building_name,room_type,floor,area,height,width,length,perimeter,volume,hasWindow,interiorOrExteriorSpace,room_name,name_codespace,class,function,usage,building_id,lodX_geometry_id,building_parent_id,building_root_id,year_of_construction,year_of_demolition,roof_type,measured_height,storeys_above_ground,storeys_below_ground,storey_heights_above_ground,storey_heights_below_ground,lodX_terrain_intersection,lodX_multi_curve,lodX_multi_surface_id,gmlid,gmlid_codespace,parent_id,root_id,is_xlink,is_reverse,is_solid,is_composite,is_triangulated,geometry)");
//		View ms2 = new View("Q_CityGML_Room(description,room_name,name_codespace,class,function,usage,building_id,lodX_geometry_id,building_name,building_parent_id,building_root_id,year_of_construction,year_of_demolition,roof_type,measured_height,storeys_above_ground,storeys_below_ground,storey_heights_above_ground,storey_heights_below_ground,lodX_terrain_intersection,lodX_multi_curve,lodX_multi_surface_id,gmlid,gmlid_codespace,parent_id,root_id,is_xlink,is_reverse,is_solid,is_composite,is_triangulated,geometry,area,perimeter,length,width):-M.Room(space_name,number,description,building_name,room_type,floor,area,height,width,length,perimeter,volume,hasWindow,interiorOrExteriorSpace,room_name,name_codespace,class,function,usage,building_id,lodX_geometry_id,building_parent_id,building_root_id,year_of_construction,year_of_demolition,roof_type,measured_height,storeys_above_ground,storeys_below_ground,storey_heights_above_ground,storey_heights_below_ground,lodX_terrain_intersection,lodX_multi_curve,lodX_multi_surface_id,gmlid,gmlid_codespace,parent_id,root_id,is_xlink,is_reverse,is_solid,is_composite,is_triangulated,geometry)");
//		View ms3 = new View("Q_IFC_Room(space_name,number,description,building_name,room_type,floor,area,height,width,length,perimeter,volume,hasWindow,interiorOrExteriorSpace,room_name):-IFC.Space(space_name,number,description,building_name,room_type,floor,area,height,width,length,perimeter,volume,hasWindow,interiorOrExteriorSpace),room_name=space_name+number");
//		View ms4 = new View("Q_CityGML_Room(description,room_name,name_codespace,class,function,usage,building_id,lodX_geometry_id,building_name,building_parent_id,building_root_id,year_of_construction,year_of_demolition,roof_type,measured_height,storeys_above_ground,storeys_below_ground,storey_heights_above_ground,storey_heights_below_ground,lodX_terrain_intersection,lodX_multi_curve,lodX_multi_surface_id,gmlid,gmlid_codespace,parent_id,root_id,is_xlink,is_reverse,is_solid,is_composite,is_triangulated,geometry,area,perimeter,length,width):-CityGML.Room(description,room_name,name_codespace,class,function,usage,building_id,lodX_geometry_id),CityGML.Building(description,building_name,class,function,usage,building_id,lodX_geometry_id,building_parent_id,building_root_id,year_of_construction,year_of_demolition,roof_type,measured_height,storeys_above_ground,storeys_below_ground,storey_heights_above_ground,storey_heights_below_ground,lodX_terrain_intersection,lodX_multi_curve),CityGML.Surface_Geometry(lodX_multi_surface_id,gmlid,gmlid_codespace,parent_id,root_id,is_xlink,is_reverse,is_solid,is_composite,is_triangulated,geometry),area=SDO_GEOM.SDO_AREA(geometry, 0.005),perimeter=SDO_GEOM.SDO_LENGTH(geometry),length=SDO_GEOM.SDO_MAX_MBR_ORDINATE(geometry,DIMINFO,1),width=SDO_GEOM.SDO_MAX_MBR_ORDINATE(geometry, diminfo, 2)");
//		Query query = new Query("q(bn,rn,area,perimeter,length,width,lodX_geometry_id):-M.Room(space_name,number,description,bn,room_type,floor,area,height,width,length,perimeter,volume,hasWindow,interiorOrExteriorSpace,rn,name_codespace,class,function,usage,building_id,lodX_geometry_id,building_parent_id,building_root_id,year_of_construction,year_of_demolition,roof_type,measured_height,storeys_above_ground,storeys_below_ground,storey_heights_above_ground,storey_heights_below_ground,lodX_terrain_intersection,lodX_multi_curve,lodX_multi_surface_id,gmlid,gmlid_codespace,parent_id,root_id,is_xlink,is_reverse,is_solid,is_composite,is_triangulated,geometry)");
//		GLAVMapping a_map = new GLAVMapping("IFC", "CityGML");
//		ArrayList<Predicate> meditateList = new ArrayList<Predicate>();
//		Predicate mediatedSchema1 = new Predicate("M.Room(space_name,number,description,building_name,room_type,floor,area,height,width,length,perimeter,volume,hasWindow,interiorOrExteriorSpace,room_name,name_codespace,class,function,usage,building_id,lodX_geometry_id,building_parent_id,building_root_id,year_of_construction,year_of_demolition,roof_type,measured_height,storeys_above_ground,storeys_below_ground,storey_heights_above_ground,storey_heights_below_ground,lodX_terrain_intersection,lodX_multi_curve,lodX_multi_surface_id,gmlid,gmlid_codespace,parent_id,root_id,is_xlink,is_reverse,is_solid,is_composite,is_triangulated,geometry)");
//		
		//Predicate mediatedSchema2 = new Predicate("M.conf-paper(title,venue,year,pages,url)");
		meditateList.add(mediatedSchema1);
		//meditateList.add(mediatedSchema2);
		a_map.addGV(ms1);
		a_map.addGV(ms2);
		a_map.addLV(ms3);
		a_map.addLV(ms4);
		a_map.queryReformulation(query, meditateList);
	}
	
		
	public static void main(String[] args) {
		testIFCCityGMLGVRewriting();
	}
}

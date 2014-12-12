package pdms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;

import deepcopy.DeepCopy;
import mediation.GLAVMapping;
import mediation.MappingTable;
import mediation.Schema;
import minicon.Predicate;
import minicon.Query;
import minicon.View;

public class QueryReformulation {

	private Query query;
	private GLAVMapping glav;  
	private Query resultQuery;
	
	public QueryReformulation(Query query, GLAVMapping glav){
		resultQuery = new Query();
		this.query = query;
		this.glav = glav;
		reformulateQuery();
	}
	
	public void setGLAVMapping(GLAVMapping glavMapping){
		glav = glavMapping;
	}
	
	public void setQuery(Query q){
		this.query = q;
	}
	
	public Query getResultQuery(){
		return resultQuery;
	}
	
//	View ms1 = new View("UBC(title,venue,year,pages):-M.conf-paper(title,venue,year,pages,url)");
//	View ms2 = new View("UW(title,venue,year,url):-M.conf-paper(title,venue,year,pages,url)");
//	View ms3 = new View("UBC(title,venue,year,pages):-UBC.conf-paper(title,venue,year,pages)");
//	View ms4 = new View("UW(title,venue,year,url):-UW.conf-paper(title,venue,year,url)");
//	Query query = new Query("q(title):-UBC.conf-paper(title,venue,year,pages)");
	
	public void reformulateQuery(){
		resultQuery.setHead(query.getHead()); // set the head of the resulted rewritten query
		int subgoalNum = query.size();
		System.out.println("query size:" + subgoalNum);
		query.first();
		for (int i = 0; i < subgoalNum; i++, query.next()){
			Predicate subgoal = query.current();
			System.out.println("current subgoal:" + subgoal.printString());
			int index = glav.findExistingGVByConcept(subgoal);
			if (index < 0){
				System.out.println("No corresponding relation exists for this local schema");
				return;
			}
			View gv = (View) DeepCopy.copy(glav.gvI(index));
			View lv = (View) DeepCopy.copy(glav.lvI(index));
			for (int j = 0; j < subgoal.size(); j++){
				//for each var in the query body
				//change the var in gv to be the same as that in the query
				String curVar = subgoal.variableI(j).getVariable();
				String varInGV = gv.subgoalI(0).variableI(j).getVariable();
				if (!curVar.equals(varInGV)){
					for (int k = 0; k < lv.size(); k++){
						//for each subgoal in lv, change the var to the corresponding one in
						//the query if the var is the one we are handling in gv (VarInGV)
						Predicate curLvSg = lv.subgoalI(k); 
						for (int l = 0; l < curLvSg.size(); l++){//for all variables
							String varInLV = curLvSg.variableI(l).getVariable();
							if (varInLV.equals(varInGV)){
								curLvSg.variableI(l).setVariable(curVar);
							}
						}
						
					}					
					
//					change the var in gv to the one in the query
					gv.subgoalI(0).variableI(i).setVariable(curVar); 
				}

			}
			
			for (int j = 0; j < lv.size(); j++){
				resultQuery.addSubgoal(lv.subgoalI(j));
			}			
		}
		
	}
	
	
	public static GLAVMapping readFile() throws IOException, ClassNotFoundException{
		FileInputStream fis = new FileInputStream("C:\\ifc_data\\finalResult.log");
	    ObjectInputStream ois = new ObjectInputStream(fis);
		
		int size = ois.readInt();
		Vector glavMappings = new Vector();

		
		for (int i = 0; i < size; i++){ //first print glavMappings
			GLAVMapping glavmap = (GLAVMapping)ois.readObject();
			glavMappings.addElement(glavmap);
			System.out.println(glavmap.printString());
		}

		
		Schema s = (Schema) ois.readObject();
		System.out.println(s.printString());
/*		
		size = ois.readInt();
		for (int i = 0; i < size; i++){
			MappingTable mtset = (MappingTable)ois.readObject();
			mtset.printMappingTable();
		}

		
		size = ois.readInt();
		for(int i = 0; i < size; i++){
			String mappingInfo = (String)ois.readObject();
			System.out.println(mappingInfo);
		}
*/		
		ois.close();
		
		return (GLAVMapping)glavMappings.elementAt(2);  //assume that the first glav is what we want
		
	}
	
	public static void testGVRewriting()
	{
		View ms1 = new View("UBC(title,venue,year,pages):-M.conf-paper(title,venue,year,pages,url)");
		View ms2 = new View("UW(title,venue,year,url):-M.conf-paper(title,venue,year,pages,url)");
		View ms3 = new View("UBC(title,venue,year,pages):-UBC.conf-paper(title,venue,year,pages)");
		View ms4 = new View("UW(title,venue,year,url):-UW.conf-paper(title,venue,year,url)");
		Query query = new Query("q(title):-UBC.conf-paper(title,venue,year,pages)");
		GLAVMapping a_map = new GLAVMapping("UBC", "UW");
		a_map.addGV(ms1);
		a_map.addGV(ms2);
		a_map.addLV(ms3);
		a_map.addLV(ms4);
		QueryReformulation qf = new QueryReformulation(query, a_map);
		Query result = qf.getResultQuery();
		System.out.println(result.printString());
	}
	
	
	public static void main(String args[]) throws IOException, ClassNotFoundException{
		testGVRewriting();
//		GLAVMapping glav = readFile();
////		System.out.println(glav.printString());
//		Query query = new Query("q(x,y,z) :- M.concept0(bp5,x,cr2,br5,cr4,cr5,bs3,bp2," +
//				"tp1,tp2,rp0,tp4,tq1,tq2,nq0,lp2,z,bp3,y,br0,br1,br4,bs4,np0,np1,lq1," +
//				"gq1,hr2,hr3,xxT3,iq1,iq0,xxN6,xxN5,ip5,xxN4,xxN3,ip2,ip1,xxN2,vr3,vr2," +
//				"up0,vp5,vp4,vp3,vp2,xxN1,uq1,hr7,xxB1,rp1,rp2,xxC3,xxC4,rp5,xxB2,rp7," +
//				"rr1,xxB3,kp0,jp2,kp2,kp3,xxB13,ks0,ks1,ks2,ks4,ks6,jp0,xxR0,jr1,jr2,jr3," +
//				"jr5,jr6,xxK0,xxK1,xq3,xq4,xq5,xs0,xs1,xs2,xs3,xs5,lp0,lp1,xxJ1,xxJ2,lp5," +
//				"xxB14,xxH1,uq5,up1)");
//		System.out.println(query.printString());
//		
//		QueryReformulation qf = new QueryReformulation(query, glav);
//		Query result = qf.getResultQuery();
//		System.out.println(result.printString());
//			
	}
	
	

}

package dataGenerators;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Vector;

import deepcopy.DeepCopy;

import mediation.Mapping;
import mediation.Schema;
import minicon.IPValue;
import minicon.Predicate;
import minicon.View;

/**
 * 
 * @author jzhao
 *
 */

/*
 * input: schemas, and concept choices
 * output: mappings generated for all possible acquaintances
 * 
 * Note that not all mappings generated here will be used;
 * only those mappings that are chosen as acquaintances in
 * TopologyGenerator will be used later
 * 
 */
public class RandomMappingGenerator {
	
	Vector schemas; 
	Vector schema_concept;
	//schema_concept: each entry is an array, indicating each schema;
	//each entry in the array indicates which concept that relation belongs to
	Random a;


	int concept[][] = {
			{1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1},
			{1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0}};
	
	Vector schemas_copy; //changed some variable names for generating mappings
	Vector acquaintanceLists;
	Vector mappings;	

	
	public RandomMappingGenerator() throws IOException{
//		data.schemas.clear();
//		data.mappings.clear();
//		data.topology.clear();
		
		a = new Random();
		schemas = new Vector();
		
		FileOutputStream fos_schemas = new FileOutputStream("C:\\Temp\\jzhao\\Project\\PeerMediation\\pdms\\schemas.log");
		ObjectOutputStream oos_schemas = new ObjectOutputStream(fos_schemas);
		
		oos_schemas.writeInt(concept[0].length);
		for (int i = 0; i < concept[0].length; i++){ //generate m schemas
			RandomSchemaGenerator rsg = new RandomSchemaGenerator(i);
			schemas.addElement(rsg.lschema);
			oos_schemas.writeObject(rsg.lschema);
//			data.schemas.addElement(rsg.lschema);
			System.out.println("Schema " + i + ":");

			for (int j = 0; j < rsg.lschema.numRelations(); j++){
				System.out.println(((Predicate)rsg.lschema.relationI(j)).printString());
			}
			System.out.println();

		}
		
		oos_schemas.close(); 
		
		TopologyGenerator tg = new TopologyGenerator(2);
		acquaintanceLists = tg.acquaintanceLists;
		
		FileOutputStream fos_topology = new FileOutputStream("C:\\Temp\\jzhao\\Project\\PeerMediation\\pdms\\topology.log");
		ObjectOutputStream oos_topology = new ObjectOutputStream(fos_topology);
		oos_topology.writeInt(acquaintanceLists.size());
		for (int i = 0; i < acquaintanceLists.size(); i++){
			Vector list = new Vector();
			list = (Vector) acquaintanceLists.elementAt(i);
			oos_topology.writeObject(list);
		}
		oos_topology.close();
		
		schema_concept = new Vector();
		schemas_copy = (Vector)DeepCopy.copy(schemas);

		pickRelationsForConcept();
		changeVarInRelationsForJoin();
		changeVarInRelationsForConcept();
		
		mappings = new Vector();
		getAllMappings();
		
		FileOutputStream fos_mappings = new FileOutputStream("C:\\Temp\\jzhao\\Project\\PeerMediation\\pdms\\mappings.log");
		ObjectOutputStream oos_mappings = new ObjectOutputStream(fos_mappings);
		oos_mappings.writeInt(mappings.size());
		for (int i = 0; i < mappings.size(); i++){
			Mapping map = new Mapping();
			map = (Mapping) mappings.elementAt(i);
			oos_mappings.writeObject(map);
		}
		oos_mappings.close();
		
	}
	
	
	
	public int countConcept(int schemaNum){ 
		//count how many concept this schema has
		//schemaNum is colNum
		int count = 0;
		for (int i = 0; i < concept.length; i++){
			count += concept[i][schemaNum];
		}
		return count;
	}
	
	public int getConceptNum(int schemaNum, int ithConcept){
		int i, count;
		for (i = 0, count = 0; i < concept.length; i++){
			if (concept[i][schemaNum] == 1 && ithConcept == count){
				return i;
			}
			else if (concept[i][schemaNum] == 1){
				count++;
			}
		}
		return -1;
	}
	
	public void pickRelationsForConcept(){
		//result will be put in schema_concept
		for (int i = 0; i < schemas.size(); i++){
			Schema schema = (Schema)schemas.elementAt(i);
			int schemaSize = schema.numRelations(); //relation number
			int[] tag = new int[schemaSize];
			int[] sc = new int[schemaSize]; //store each relation contains which concept
			for (int j = 0; j < sc.length; j++){
				sc[j] = -1;
			}//initialize: -1 means this relation doesn't belong to any concept
			int NumOfConcept = countConcept(i);
			
			//each concept, at least contains one relation
			for (int j = 0; j < NumOfConcept; j++){
				int random = a.nextInt(schemaSize);
				while (tag[random] == 1){
					random = a.nextInt(schemaSize);
				}
				sc[random] = getConceptNum(i, j);
				tag[random] = 1;
			}
			//for the remaining relations, randomly decide whether they
			//belong to some concept or not
			for (int j = 0; j < schemaSize; j++){
				if (tag[j] == 1){
					continue;
				}

				int random = a.nextInt(NumOfConcept + NumOfConcept);
				if (random >= NumOfConcept){
					continue;
				}
				else{
					sc[j] = getConceptNum(i, random);
				}
			}			
			schema_concept.add(sc);
		}
		
		for (int i = 0; i < schema_concept.size(); i++){
			System.out.println("schema " + i);
			int[] temp = (int[])schema_concept.elementAt(i);
			for (int j = 0; j < temp.length; j++){
				System.out.print(temp[j] + " ");
			}
			System.out.println();
			
		}
	}
	
	public void changeVarInRelationsForJoin(){ 
		//change variable names in relations in the same schema 
		//so that relations grouped for the same concept have
		//overlapped variables
		
		for (int i = 0; i < schema_concept.size(); i++){
			int[] scon = (int[])schema_concept.elementAt(i);
			Schema sc = (Schema)schemas_copy.elementAt(i); //sc is one schema
			Schema s = (Schema)schemas.elementAt(i);
			Predicate rel_current;
			Predicate rel_prev;
			Predicate rel_original;
			for (int j = 0; j < concept.length; j++){
				rel_current = new Predicate();
				rel_prev = new Predicate();
				if (concept[j][i] == 1){//i-th schema, j-th concept
					
//					System.out.println("handling concept " + j + " schema " + i);
					
					for (int k = 0; k < scon.length; k++){ //sc.length = relation size
//						System.out.println("relation " + k);
						
						if (scon[k] != j){
//							System.out.println(k + "'s relation is concept " + scon[k] );
							
							continue;
						}
						else if (scon[k] == j && rel_prev.variables.isEmpty()){
							rel_prev = sc.relationI(k);
						}
						else if (scon[k] == j){
							rel_current = sc.relationI(k);
							rel_original = s.relationI(k);
							
							
//							System.out.println("rel_current: " + rel_current.printString());
//							System.out.println("rel_original: " + rel_original.printString());
							
							int[] tag = new int[rel_prev.size()];
							
//							System.out.println("rel_prev: " + rel_prev.printString());
							
							for (int l = 0; l < rel_current.size(); l++){//handle every attr
								
//								System.out.println("handling attr " + l);

								int rand = a.nextInt(2); //decide whether to change this attr name or not
										
								if (rand == 0){
									continue;
								}
						
								
								int m;
								for (m = 0; m < tag.length && tag[m] == 1; m++);
								if (m == tag.length) 
									break; 
								int pos = a.nextInt(rel_prev.size());
								
								while (tag[pos] == 1){
									pos = a.nextInt(rel_prev.size());
								}
								
								
//								System.out.println("changing current attr to previous relation attr of pos = " + pos);
								
								
								
								tag[pos] = 1;
								rel_current.variables.setElementAt(rel_prev.variables.elementAt(pos), l);
								
								
//								System.out.println("rel_current: " + rel_current.printString());
//								System.out.println("rel_original: " + rel_original.printString());
							}
							if (rel_current.equals(rel_original)){ 
								//make sure that there's at least one variable be 
								//the same as previous one with the same concept
								int pos_prev = a.nextInt(rel_prev.size());
								int pos_curr = a.nextInt(rel_current.size());
								rel_current.variables.setElementAt(rel_prev.variables.elementAt(pos_prev), pos_curr);
							}
				
							
							rel_prev = rel_current;
						}
					}					
				}
			}			
		}		
		
		
		/*
		 * print schema_concept
		 */
		
		for (int i = 0; i < schemas_copy.size(); i++){
			System.out.println("Schema " + i + ":");
			Schema sc = (Schema)schemas_copy.elementAt(i); //current schema
			for (int j = 0; j < sc.numRelations(); j++){
				System.out.println((sc.relationI(j)).printString());
			}
			System.out.println();
		}
		
	}

	
	public void changeVarInRelationsForConcept(){
		for (int i = 0; i < concept.length; i++){ //i-th concept
			Vector schemaRels = new Vector();
			for (int j = 0; j < concept[0].length; j++){
				if (concept[i][j] == 1){ //schema j has concept i
					Vector schemaRel = new Vector();
					Schema schema_copy = (Schema)schemas_copy.elementAt(j);
					int[] sc = (int[]) schema_concept.elementAt(j);
					for (int k = 0; k < schema_copy.numRelations(); k++){ //handle each relation
						if (sc[k] == i){
							schemaRel.addElement(schema_copy.relationI(k));							
						}
					}
					schemaRels.addElement(schemaRel);
				}
			}
			
//			System.out.println("handling concept " + i);
			for (int j = 0; j < schemaRels.size(); j++) { 
//				System.out.println(" -- schema " + j);
				Vector schemaRel = (Vector)schemaRels.elementAt(j);
				for (int k = 0; k < schemaRel.size(); k++){
//					System.out.println(((Predicate)schemaRel.elementAt(k)).printString());
				}
			}
			
			IPValue backup = new IPValue(); //just in case there's no common attribute for all schemas relations of that concept
			Vector schr_1 = (Vector)schemaRels.elementAt(0);
			Vector differentVars_1 = getDifferentVars(schr_1);
			int r = a.nextInt(differentVars_1.size());
			backup = (IPValue)differentVars_1.elementAt(r);
			
			for (int j = 0; j < schemaRels.size() - 1; j++){
				Vector schr1 = (Vector)schemaRels.elementAt(j);
				Vector schr2 = (Vector)schemaRels.elementAt(j + 1);
				Vector differentVars1 = getDifferentVars(schr1);
				Vector differentVars2 = getDifferentVars(schr2);
				int tag[] = new int[differentVars1.size()];
				for (int l = 0; l < differentVars2.size(); l++){
					
					int rand = a.nextInt(2);
					if (rand == 0){
						continue;
					}
					rand = a.nextInt(differentVars1.size());
					
					int m;
					for(m = 0; m < differentVars1.size() && tag[m] == 1 ; m++);
					if (m == differentVars1.size()){
						break;
					}
/*					
					for (int x = 0; x < tag.length; x++){
						System.out.print(tag[x] + " ");
					}
					System.out.println();
*/					
					
					while (tag[rand] == 1){
						rand = a.nextInt(differentVars1.size());
					}
					tag[rand] = 1;
					IPValue var1 = (IPValue)differentVars1.elementAt(rand);
//					System.out.println("var1: " + var1.printString());
					IPValue var2 = (IPValue)differentVars2.elementAt(l);
//					System.out.println("var2: " + var2.printString());
					for (int k = 0; k < schr2.size(); k++){
						Predicate p2 = (Predicate)schr2.elementAt(k);
						if (p2.variables.contains(var2)){
							int index = p2.variables.indexOf(var2);
							p2.variables.setElementAt(var1, index);
						}
					}					
				}
				
			}
			
			Vector temp = (Vector)schemaRels.elementAt(0);
			Vector diffTempVars = getDifferentVars(temp);
			int needToUseBackup = 0;
			for (int j = 1; j < schemaRels.size(); j++){
				Vector temp1 = (Vector)schemaRels.elementAt(j);
				Vector diffTempVars1 = getDifferentVars(temp1);
				diffTempVars = getCommonVars(diffTempVars, diffTempVars1);
				if (diffTempVars.isEmpty()){
					needToUseBackup = 1;
					break;
				}
			}
			if (needToUseBackup == 1){
			
				
				for (int j = 1; j < schemaRels.size(); j++){
					Vector schr1 = (Vector)schemaRels.elementAt(j);
					
					Vector differentVars1 = getDifferentVars(schr1);
					if (differentVars1.contains(backup)){
						continue;
					}
					else{
						int rand = a.nextInt(differentVars1.size());
						IPValue var1 = (IPValue)differentVars1.elementAt(rand);
						for (int k = 0; k < schr1.size(); k++){
							Predicate p2 = (Predicate)schr1.elementAt(k);
							if (p2.variables.contains(var1)){
								int index = p2.variables.indexOf(var1);
								p2.variables.setElementAt(backup, index);
							}
						}
					}//else
				}//for
								
			}//if		
		}
		
		
		for (int i = 0; i < schemas_copy.size(); i++){
			System.out.println("Schema " + i + ":");
			Schema sc = (Schema)schemas_copy.elementAt(i); //current schema
			for (int j = 0; j < sc.numRelations(); j++){
				System.out.println((sc.relationI(j)).printString());
			}
			System.out.println();
		}	
	}
	
	
	public Mapping getMapping(int schemaIndex1, int schemaIndex2){ //assume schema1 and schema2 are acquaintances
		Mapping map = new Mapping();
		map.addSchema((Schema)schemas.elementAt(schemaIndex1));
		map.addSchema((Schema)schemas.elementAt(schemaIndex2));
		for (int i = 0; i < concept.length; i++){
			if (concept[i][schemaIndex1] == 1 && 
					concept[i][schemaIndex2] == 1){
				
				Vector schemaRel1 = new Vector();
				Vector schemaRel2 = new Vector();
				Schema schema_copy1 = (Schema)schemas_copy.elementAt(schemaIndex1);
				Schema schema_copy2 = (Schema)schemas_copy.elementAt(schemaIndex2);
				Vector differentVars1 = new Vector();
				Vector differentVars2 = new Vector();
				
				int[] sc1 = (int[]) schema_concept.elementAt(schemaIndex1);
				for (int j = 0; j < schema_copy1.numRelations(); j++){ //handle each relation
					if (sc1[j] == i){
						schemaRel1.addElement(schema_copy1.relationI(j));							
					}
				}
				differentVars1 = this.getDifferentVars(schemaRel1);
				
				int[] sc2 = (int[]) schema_concept.elementAt(schemaIndex2);
				for (int j = 0; j < schema_copy2.numRelations(); j++){ //handle each relation
					if (sc2[j] == i){
						schemaRel2.addElement(schema_copy2.relationI(j));							
					}
				}
				differentVars2 = this.getDifferentVars(schemaRel2);
				
				Vector commonVars = this.getCommonVars(differentVars1, differentVars2); //head vars
	
				View v1 = new View();
				View v2 = new View();

				Predicate headfunc = new Predicate();
				
				headfunc.setFunctionHead("concept" + i);
				for (int j = 0; j < commonVars.size(); j++){
					headfunc.addVariable((IPValue)commonVars.elementAt(j));
				}
				
				v1.setHead(headfunc);
				v2.setHead(headfunc);
				
				for (int j = 0; j < schemaRel1.size(); j++){
					v1.addSubgoal((Predicate)schemaRel1.elementAt(j));
				}
				
				for (int j = 0; j < schemaRel2.size(); j++){
					v2.addSubgoal((Predicate)schemaRel2.elementAt(j));
				}
				
				map.addView(v1);
				map.addView(v2);

			}
		}
				
		return map;
	}
	
	public void getAllMappings(){
//		data.topology = this.acquaintanceLists;
		for (int i = 0; i < acquaintanceLists.size(); i++){
			Vector list = (Vector) acquaintanceLists.elementAt(i);
			for (int j = 0; j < list.size(); j++){
				int node1 = i;
				int node2 = ((Integer)list.elementAt(j)).intValue();
				if (node1 < node2){
					
					System.out.println(node1 + " -- " + node2 );
					Mapping map = getMapping(node1, node2);
					mappings.addElement(map);					
					printMapping(node1, node2);
//					data.mappings.addElement(map);
				}
			}
		}

	}
	
	public void printMapping(int schemaIndex1, int schemaIndex2){
		Mapping map = getMapping(schemaIndex1, schemaIndex2);
		System.out.println(map.printString());
	}
	
	
	public Vector getDifferentVars(Vector schemaRels){
		Vector differentVars = new Vector();
		for (int i = 0; i < schemaRels.size(); i++){
			Predicate rel = new Predicate();
			rel = (Predicate)schemaRels.elementAt(i);
			for (int j = 0; j < rel.size(); j++){
				IPValue var = (IPValue) rel.variables.elementAt(j);
				if (differentVars.contains(var)){
					continue;
				}
				else
					differentVars.addElement(var);
			}
		}

		/*
		System.out.println("different vars: ");
		for (int i = 0; i < differentVars.size(); i++){
			System.out.print(((IPValue)differentVars.elementAt(i)).printString() + " ");
		}
		System.out.println();
		*/
		
		return differentVars;
	}
	
	public Vector getCommonVars(Vector diffVars1, Vector diffVars2){
		Vector commonVars = new Vector();
		for (int i = 0; i < diffVars1.size(); i++){
			if (diffVars2.contains(diffVars1.elementAt(i))){
				commonVars.addElement(diffVars1.elementAt(i));
			}
		}
		
		return commonVars;
	}
	

	
	
	public static void main(String args[]) throws IOException{
		RandomMappingGenerator rmg = new RandomMappingGenerator();
		for (int i = 0; i < rmg.acquaintanceLists.size(); i++){
			Vector list = (Vector) rmg.acquaintanceLists.elementAt(i);
			for (int j = 0; j < list.size(); j++){
				int node1 = i;
				int node2 = ((Integer)list.elementAt(j)).intValue();
				if (node1 < node2){
					
					System.out.println(node1 + " -- " + node2 );
					rmg.printMapping(node1, node2);
				}
			}
		}
		
	}
}

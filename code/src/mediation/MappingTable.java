package mediation;

/*
 * Created on March 21, 2006
 * @author jzhao
 * 
 * Input: only one conjunctive query
 */

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import minicon.IPValue;
import minicon.Predicate;
import minicon.Statement;
import minicon.View;
import semantics.MappingStatementUnion;

public class MappingTable implements Serializable{
	protected String name; 
	//name of MappingTable is set as the same as the concept name
	protected int MappingID; //the same as MappingID in Mapping
//	protected int MappingStatementID;  //ID
	protected Hashtable relNames;
	protected Vector relStrings;
	protected int attrMapping[][];
	protected int rowNum = 0;
	protected int colNum = 0;
	protected int index1 = 0;
	protected int index2 = 0;
	
	
	public MappingTable(String mt_name, int rownum, int colnum){
		name = mt_name;
		relNames = new Hashtable();
		relStrings = new Vector();
		attrMapping = new int[rownum][colnum];
		for (int i = 0; i < rownum; i++){
			for (int j = 0; j < colnum; j++){
				attrMapping[i][j] = 0;
			}
		}
		colNum = colnum;
		rowNum = rownum;
		MappingID = 9999; // 9999 is the MappingID only for merged MappingTable
	}
	
	public MappingTable(Predicate m, int mappingID, 
			MappingStatementUnion m_conj){
		MappingID = mappingID;
//		MappingStatementID = mappingstatementID;
		name = m.getFunctionHead();
		colNum = m.size();
		relNames = new Hashtable();
		relStrings = new Vector();
		System.out.println("print0:" + m.getFunctionHead()+"\n");
		relNames.put(m.getFunctionHead(),new Integer(0));
		relStrings.addElement(m.getFunctionHead());
		rowNum = 1;
		for (int i = 0; i < m_conj.numStatements(); i++){
			for (int j = 0; j < m_conj.statementI(i).size(); j++){
				//System.out.println("print1:" + 	m_conj.statementI(i).subgoalI(j).getFunctionHead().toString());
				relNames.put(m_conj.statementI(i).subgoalI(j).getFunctionHead(),
						new Integer(rowNum++));
				relStrings.addElement(m_conj.statementI(i).subgoalI(j).getFunctionHead());
			}
		}
		
		//initiate attrMapping, fill all entries with 0
		attrMapping = new int[rowNum][colNum];
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				attrMapping[i][j] = 0;
		
		//put the attribute index value into the MappingTable 
		//for the mediated relation m
		for (int k = 0; k < m.size(); k++){
			Integer n = (Integer)relNames.get(m.getFunctionHead());
			attrMapping[n.intValue()][k]=k+1;
//			System.out.println("n: " + n.intValue()+", k: "+ k + ", " 
//					+ attrMapping[n.intValue()][k]);
		}

		int num_mss = m_conj.numStatements();
		Vector attributes = m_conj.getVariables();
//		System.out.println("attributes:" + attributes.toString());
		int num_attrs = attributes.size();
		Predicate a_rel = new Predicate();
		a_rel.setFunctionHead(m_conj.getHead());
		for (int j = 0; j < num_attrs; j++){
//			System.out.println("elem:" + (String)attributes.elementAt(j));
			a_rel.addVariable((String)attributes.elementAt(j));
//			System.out.println("a_rel size:" + a_rel.size());
		}
//		System.out.println(a_rel.getFunctionHead());
//		System.out.println("a_rel: "+ a_rel.printString());
//		System.out.println("element2:" +((IPValue)a_rel.variables.get(2)).getVariable());
		Predicate subgoal = new Predicate();
		for (int j = 0; j < num_mss; j++){
			Statement a_state = m_conj.statementI(j);
//			System.out.println("a_state:" + j + " " + a_state.printString());
			for (int k = 0; k < a_state.size(); k++){
//				System.out.println("a_state.size:" + a_state.size());
				subgoal = a_state.subgoalI(k);
				for (int l = 0; l < subgoal.size(); l++){
//					System.out.println("attrNum in the subgoal:" + subgoal.size());
					IPValue attr = new IPValue();
					attr = (IPValue)subgoal.variables.get(l);
//					System.out.println("attr:" + attr.printString());
					int colIndex = a_rel.variableIsAt(attr); //needs checking
//					System.out.println("colIndex:" + colIndex);
					Integer rowIndex = (Integer)relNames.get(subgoal.getFunctionHead());
//					System.out.println("rolIndex:" + rowIndex.intValue());
					attrMapping[rowIndex.intValue()][colIndex] = l + 1;
				}
			}
		}
	}


	public MappingTable(MappingTable old){
		relNames = new Hashtable(old.relNames);
		relStrings = new Vector(old.relStrings);
		int rowNum = old.attrMapping.length;
		int colNum = old.attrMapping[0].length;
		attrMapping = new int[rowNum][colNum];
	}
	
	public String getName(){
		return name;
	}
	
	public int getRowNum(){
		return rowNum;
	}
	
	public int getColNum(){
		return colNum;
	}
	
	
	public int getMappingID(){
		return MappingID;
	}
	
	public Hashtable getRelNames(){
		return relNames;
	}
	
	public String getRelStrings(int i){
		return (String)relStrings.elementAt(i);
	}
	
	public int getAttrMapping(int i, int j){
		return attrMapping[i][j];
	}
	
	public int getIndex1(){
		return index1;
	}
	
	public int getIndex2(){
		return index2;
	}

	public void printMappingTable(){
		System.out.println("MappingTable: " + this.name);
		for (int i = 0; i < this.rowNum; i++){
			System.out.print(relStrings.get(i) + " ");
			for (int j = 0; j < this.colNum; j++){
				System.out.print(" " + this.attrMapping[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void printEmptyMappingTable(){ //if current MappingTable is still empty, 
		                                  //use this print function
		for (int i = 0; i < this.rowNum; i++) {
			for (int j = 0; j < this.colNum; j++){
				System.out.print(" " + this.attrMapping[i][j]);
			}
			System.out.println();
		}
		
	}
/*
	public int getMappingStatementID(){
		return MappingStatementID;
	}
*/	
	public static void testConstructor(){
		Mapping map = new Mapping();
		View ms1 = new View("q2(x,y):-e3(x,y),e4(y,z)");
		View ms2 = new View("q2(x,y):-f2(x,y)");
		map.addView(ms1);
		map.addView(ms2);
		Predicate m = new Predicate("q2(x,y,z)");
		MappingTable mt = new MappingTable(m, 0, map.MappingUnionI(0));
		System.out.println("relName:" + mt.relNames);
		System.out.println(mt.rowNum + " " + mt.colNum);
		for (int i = 0; i < mt.rowNum; i++){
			for (int j = 0; j< mt.colNum; j++){
				System.out.println("i = "+i+", j = "+j+":" + mt.attrMapping[i][j]);
			}
		}
		Set names = mt.relNames.keySet();
		int size = names.size();
		String[] nameStringArray = new String[size];
		names.toArray(nameStringArray);
		for (int i = 0; i < size; i++){
			System.out.println(nameStringArray[i]);
		}

	}
	
	public String[] getAllRelationName(MappingTable MT) {
		Set names = MT.relNames.keySet();
		int size = names.size();
		String[] nameStringArray = new String[size];
		names.toArray(nameStringArray);
        return nameStringArray;
	}
	
	/*
	 * public boolean havingSameConcept(MappingTable MT1)
	 * 
	 * check whether this MappingTable and MT1 represent the 
	 * same concept
	 */	
	public boolean havingSameConcept(MappingTable MT1){
		if (this.name.equals(MT1.name))
			return true;
		else return false;
	}
	
	/*
	 * public void updateWithAdditionalInfo(MappingTable oldMT)
	 * 
	 * update current MappingTable and oldMT by 
	 * combining corresponding columns with the additional 
	 * information that can be obtained from each other
	 * 
	 * oldMT is the MappingTable originally residing in the network
	 * with the same IDB name as this new created MappingTable from 
	 * given mapping
	 */
	public void updateWithAdditionalInfo(MappingTable oldMT){
		if (!this.havingSameConcept(oldMT)){
			return;
		}
		
//		System.out.println("inside updateWithAdditionalInfo");
//		System.out.println("old mappingtable: ");
//		oldMT.printMappingTable();
//		System.out.println("this mappingtable: ");
//		this.printMappingTable();
		Vector v = new Vector();
		v = findOverlappedRelations(oldMT);

		if (v.size() < 2){
			return;
		}
/*		
		System.out.println("overlapped relations: ");
		for (int i = 0; i < v.size(); i++){
			System.out.println((String)v.elementAt(i));
		}
*/		
		int[][] temp2 = new int[v.size()][this.attrMapping[0].length]; //only check the overlapped relations
		int[][] temp4 = new int[v.size()][oldMT.attrMapping[0].length];
		for (int i = 0; i < v.size(); i++){ //put the attr value of the overlapped relations into temp2 for easy operation
			int row_orig = ((Integer)this.relNames.get(v.get(i))).intValue(); 
			for (int j = 0; j < attrMapping[0].length; j++){
				temp2[i][j] = attrMapping[row_orig][j];
			}		    
		}
/*		
		System.out.println("temp2: ");
		for (int i = 0; i < temp2.length; i++){
			for (int j = 0; j < temp2[0].length; j++){
				System.out.print(temp2[i][j] + " ");
			}
				System.out.println();
		}
*/		
		for (int i = 0; i < v.size(); i++){
			int row_orig = ((Integer)oldMT.relNames.get(v.get(i))).intValue(); 
			for (int j = 0; j < oldMT.attrMapping[0].length; j++){
				temp4[i][j] = oldMT.attrMapping[row_orig][j];
			}
		}
/*		
		System.out.println("temp4: ");
		for (int i = 0; i < temp4.length; i++){
			for (int j = 0; j < temp4[0].length; j++){
				System.out.print(temp4[i][j] + " ");
			}
				System.out.println();
		}
*/		
//		merge oldMT
		Vector mergeOld = new Vector();
		
		for (int i = 0; i < this.attrMapping[0].length; i++){ //col
			for (int j = 0; j < v.size() - 1; j++){ //row
				int attrNum1 = temp2[j][i];
				if (attrNum1 == 0){
					continue;
				}
				for (int k = j + 1; k < v.size(); k++){ //need to check every two rows in the overlapped vector					
					int attrNum2 = temp2[k][i];
					if (attrNum2 == 0){
						continue;
					}
					
					int col1inOld = 0, col2inOld = 0;
					for (int l = 0; l < oldMT.attrMapping[0].length; l++){
						if (temp4[j][l] == attrNum1){
							col1inOld = l;
							break;
						}
					}
					for (int l = 0; l < oldMT.attrMapping[0].length; l++){
						if (temp4[k][l] == attrNum2){
							col2inOld = l;
							break;
						}
					}
					if (col1inOld != col2inOld){
						if (col1inOld > col2inOld){
							int tmp = col2inOld;
							col2inOld = col1inOld;
							col1inOld = tmp;
						}
						int[] merge = new int[2];
						merge[0] = col1inOld;
						merge[1] = col2inOld;
						int tagAdd = 1;
						for (int m = 0; m < mergeOld.size(); m++){
							int[] mergeTmp = (int[])mergeOld.elementAt(m);
							if (mergeTmp[0] == merge[0] && mergeTmp[1] == merge[1]){
								tagAdd = 0;
								break;
							}
								
						}
						
						if (tagAdd == 1){ 
							mergeOld.addElement(merge);
						}					
					}
				}
			}			
		}
/*		
		System.out.println("mergeOld:");
		for (int i = 0; i < mergeOld.size(); i++){
			int[] print = (int[])mergeOld.elementAt(i);
			System.out.println(print[0] + " " + print[1]);
		}
*/		


//		merge this MT
		Vector mergeThis = new Vector();
		
		for (int i = 0; i < oldMT.attrMapping[0].length; i++){ //col
			for (int j = 0; j < v.size() - 1; j++){ //row
				int attrNum1 = temp4[j][i];
				if (attrNum1 == 0){
					continue;
				}
				for (int k = j + 1; k < v.size(); k++){ //need to check every two rows in the overlapped vector
					int attrNum2 = temp4[k][i];
					if (attrNum2 == 0){
						continue;
					}
					int col1inThis = 0, col2inThis = 0;
					for (int l = 0; l < this.attrMapping[0].length; l++){
						if (temp2[j][l] == attrNum1){
							col1inThis = l;
							break;
						}
					}
					for (int l = 0; l < this.attrMapping[0].length; l++){
						if (temp2[k][l] == attrNum2){
							col2inThis = l;
							break;
						}
					}
					if (col1inThis != col2inThis){
						if (col1inThis > col2inThis){
							int tmp = col2inThis;
							col2inThis = col1inThis;
							col1inThis = tmp;
						}
						int[] merge = new int[2];
						merge[0] = col1inThis;
						merge[1] = col2inThis;
					
						int tagAdd = 1;
						for (int m = 0; m < mergeThis.size(); m++){
							int[] mergeTmp = (int[])mergeThis.elementAt(m);
							if (mergeTmp[0] == merge[0] && mergeTmp[1] == merge[1]){
								tagAdd = 0;
								break;
							}
								
						}
		
						if (tagAdd == 1){ 
							mergeThis.addElement(merge);
						}			
					
					}
				}
			}
		}
/*		
		System.out.println("mergeThis:");
		for (int i = 0; i < mergeThis.size(); i++){
			int[] print = (int[])mergeThis.elementAt(i);
			System.out.println(print[0] + " " + print[1]);
		}
*/	
		
		if (mergeOld.size() > 0){
			mergeColumns(oldMT, mergeOld);
		}
		
		freshMappingTable(oldMT);
		
		if (mergeThis.size() > 0){
			mergeColumns(this, mergeThis);
		}

		freshMappingTable(this);
	}
	
	/*
	 * public void mergeColumns(MappingTable MT, int[][] temp)
	 * 
	 * merge columns in MT based on a given array temp;
	 * For each columns in the array, get all values
	 * from that columns; merge all columns in MT according 
	 * to these values
	 */
	/*
	public void mergeColumns(MappingTable MT, int[][] temp){
		for (int i = 0; i < temp.length - 1; i++){
			for (int j = 0; j < temp[0].length; j++){
				if (!(temp[i][j] == temp[i+1][j])){
					merge2Columns(MT, temp[i][j], temp[i+1][j]);
					temp[i+1][j] = temp[i][j];
				}
			}
		}
	}
*/
	
	public void mergeColumns(MappingTable MT, Vector mergeMT){
		for (int i = 0; i < mergeMT.size(); i++){
			int tmp[] = (int[])mergeMT.elementAt(i);
			int colTo = tmp[0];
			int colFrom = tmp[1];
			for (int j = 0; j < MT.attrMapping.length; j++){//row
				if (MT.attrMapping[j][colFrom] == 0){
					continue;
				}
				else if (MT.attrMapping[j][colFrom] > 0 && MT.attrMapping[j][colTo] == 0){
					MT.attrMapping[j][colTo] = MT.attrMapping[j][colFrom];
					MT.attrMapping[j][colFrom] = 0;
				}
				else if (MT.attrMapping[j][colFrom] > 0 && MT.attrMapping[j][colTo] > 0){
					MT.attrMapping[j][colFrom] = 0;
				}
				
			}

		}
	}
	
	public void delOverlappedRow(){
		for (int i = this.rowNum; i > 1; i--){
			String concept = this.getName();
			//for the second row, if there are two rows using
			//the concept name as the relation name, delete 
			//the first one
			if (this.relStrings.elementAt(i).toString().equals(concept) &&
					this.relStrings.elementAt(i - 1).toString().equals(concept)){
				this.relStrings.remove(i - 1);
			}
		}
	}
	
	
	
	/*
	 * public void merge2Columns(MappingTable MT, int colNum1, int ColNum2)
	 * 
	 * merge 2 columns in MT given the column numbers
	 */
	public void merge2Columns(MappingTable MT, int colNum1, int colNum2){
		if (colNum1 > colNum2) { //make sure that colNum1 > colNum2
			int temp = colNum1;
			colNum1 = colNum2;
			colNum2 = temp;
		}
		for (int i = 0; i < MT.rowNum; i++){
			if (MT.attrMapping[i][colNum1] == 0 
					&& MT.attrMapping[i][colNum2] > 0){
				MT.attrMapping[i][colNum1] = MT.attrMapping[i][colNum2]; //copy
			}
		}
		MT.attrMapping[0][colNum2] = 0;  
		//set the value in the first row colNum2 col as 0, 
		//indicating this col is never used
	}
	
	public void freshMappingTable(MappingTable MT){
		for (int i = 0; i < MT.attrMapping[0].length - 1; i++){ //i: col
			if (MT.attrMapping[0][i] == 0){ //only check the first row
				for (int j = i + 1; j < MT.attrMapping[0].length; j++){ 
					//j: col, from i to last col
					if (MT.attrMapping[0][j] != 0){//find the first non-zero col
						for (int k = 0; k < MT.attrMapping.length; k++){//k: row
							int tmp = MT.attrMapping[k][i];
							MT.attrMapping[k][i] = MT.attrMapping[k][j];
							MT.attrMapping[k][j] = tmp;
						}
					}
				}
			}
		}
	}
	
	/*
	 * public Vector findOverlappedRelations(MappingTable MT)
	 * 
	 * Find overlapped relation names 
	 * in the two MappingTables: this and MT
	 */
	public Vector findOverlappedRelations(MappingTable MT){
		Vector v = new Vector();
		
		for (int i = 1; i < this.relStrings.size(); i++){
			for (int j = 1; j < MT.relStrings.size(); j++){
				String a = (String)MT.relStrings.elementAt(j);
			    int index = a.indexOf(".");
			    if (index < 0)
			    	continue;
				if (this.relStrings.elementAt(i).equals(MT.relStrings.elementAt(j))){
					if (!v.contains(this.relStrings.elementAt(i))){
						v.add(this.relStrings.elementAt(i));
					}
				}
			}
		}
		return v;
/*
		Set names = this.relNames.keySet();
		int size1 = names.size();
		int size1 = this.relStrings.size();
		String[] nameStringArray1 = new String[size1];
		names.toArray(nameStringArray1);
		names = MT.relNames.keySet();
		int size2 = names.size();
		String[] nameStringArray2 = new String[size2];
		names.toArray(nameStringArray2);
*/
//		int numOfOverlapRel = 0;
		//get the overlapped relation names for both MT 
		//and put them into v
/*		
		for (int i = 1; i < size1; i++){ //skip the first row
			for (int j = 1; j < size2; j++){
				if (nameStringArray1[i].equals(nameStringArray2[j])){
					System.out.println("i = " + i + " j = " + j);
					System.out.println(nameStringArray1[i]);
					System.out.println(nameStringArray2[j]);
					v.add(numOfOverlapRel++, nameStringArray1[i]);
				}				
			}
		}
*/
		
	}
	
	public MappingTable combineMappingTable(MappingTable oldMT){
		
		if (this.equals(oldMT)){
			return this;
		}
		updateWithAdditionalInfo(oldMT);
		
//		System.out.println("after updateWithAdditionalInfo, here is the MappingTable");
		
		this.printMappingTable();
		oldMT.printMappingTable();
		MappingTable newMT;
		int newMTcol, newMTrow;

		int actualColNum1, actualColNum2;
		//get the actual colNum for this after updating with additional info
        actualColNum1 = getActualColSize(this);
//        System.out.println("actualColNum1 this: " + actualColNum1);
		//get the actual colNum for oldMT after updating with additional info
        actualColNum2 = getActualColSize(oldMT);
//        System.out.println("actualColNum2 oldMT: " + actualColNum2);
        //compute the overlapped column numbers for this and oldMT
		int overlappedColNum = getOverlappedColSize(oldMT);
//		System.out.println("overlappedColNum = " + overlappedColNum);
        newMTcol = actualColNum1 + actualColNum2 - overlappedColNum;
        
        if (newMTcol < actualColNum1 || newMTcol < actualColNum2){
        	newMTcol = actualColNum1 > actualColNum2 ? actualColNum1 : actualColNum2;
        } 
        
        
        
        
        int unusedrow = 0;
//      System.out.println(relNames.toString());
        for (int i = 1; i < this.rowNum; i++){
        	if (this.relStrings.elementAt(i).equals(relStrings.elementAt(0))){
        		unusedrow++;
        	}
        }
        int deleteRow1 = unusedrow; 
        System.out.println("deleteRow1: " + deleteRow1);
        
        for (int i = 1; i < oldMT.rowNum; i++){
//      	System.out.println((String)oldMT.relStrings.elementAt(i));
        	if (this.relNames.containsKey(oldMT.relStrings.elementAt(i))){
        		unusedrow++;
        	}
        }
        
        System.out.println("unusedrow: " + unusedrow);
      
        newMTrow = this.rowNum + oldMT.rowNum + 1 - unusedrow; 
       
        
        
//        System.out.println("newMTcol = " + newMTcol);
//        newMTrow = this.rowNum + oldMT.rowNum + 1; 

//        System.out.println("newMTrow = " + newMTrow);
        //the first row is for the new mediated relation
		newMT = new MappingTable(oldMT.name, newMTrow, newMTcol);

		//copy this to newMT 
		int i = 0;
		//create the first row for the mediated relation
        
		//!!! need checking codes here for the first row !!!! //
		newMT.relNames.put(this.relStrings.get(i), new Integer(0));
		newMT.relStrings.addElement(this.relStrings.get(i));
		for (int j = 0; j < newMTcol; j++){
			newMT.attrMapping[0][j] = j + 1;
		}
		

		i = 1;
		for (int k = 0; k < this.rowNum; k++){
			if (k > 0 && this.relStrings.elementAt(k).equals(relStrings.elementAt(0))){
				continue;
			}
			newMT.relNames.put(this.relStrings.get(k), new Integer(i));
			newMT.relStrings.addElement(this.relStrings.get(k));
			for (int j = 0; j < actualColNum1; j++){
				newMT.attrMapping[i][j] = this.attrMapping[k][j];
			}
			i++;
		}
		
		Hashtable mapForOldMT = new Hashtable();
		mapForOldMT = 
			getColMapping(oldMT, newMTcol, actualColNum1, actualColNum2);

		//copy oldMT to newMT
//		i++;
        for (int k = 0; k < oldMT.rowNum; k++){
        	if (k > 0 && this.relNames.containsKey(oldMT.relStrings.elementAt(k))){
        		continue;
        	}
    		newMT.relNames.put(oldMT.relStrings.get(k),new Integer(i));
    		newMT.relStrings.addElement(oldMT.relStrings.get(k));
        	for (int j = 0; j < actualColNum2; j++){
        		Integer col = (Integer)mapForOldMT.get(new Integer(j));
//        		System.out.println("old colnum  j = " + j + " new colnum  col = " + col);
        		newMT.attrMapping[i][col.intValue()] = oldMT.attrMapping[k][j];
        	}
        	i++;
        }

        newMT.index1 = 1;
        newMT.index2 = this.rowNum - deleteRow1 + 1;
		return newMT;
	}
	
	public Hashtable getColMapping(MappingTable MT, 
			int newColNum, int thisActualColNum, int MTActualColNum){
		Hashtable overlappedColMap = new Hashtable();
		overlappedColMap = getOverlappedColMapping(MT);
		Hashtable colMapping = new Hashtable(overlappedColMap);
		int countThis = thisActualColNum;
		for (int i = 0; i < MTActualColNum; i++){
//			Integer n = (Integer)relNames.get(m.getFunctionHead());
			if ((Integer)colMapping.get(new Integer(i)) == null){
				colMapping.put(new Integer(i), new Integer(countThis++));
			}
		}
//		System.out.println("colMapping");
//		System.out.println(colMapping.entrySet());
		return colMapping;
	}
	
	
	public Hashtable getOverlappedColMapping(MappingTable MT){
		Hashtable colMap = new Hashtable();
		Vector v = findOverlappedRelations(MT);
		Integer n, m;
		int tag = 0;
		for (int j = 0; j < this.attrMapping[0].length; j++){
			for (int i = 0; i < v.size(); i++){ 
				n = (Integer)this.relNames.get(v.get(i));
				if (this.attrMapping[n.intValue()][j] > 0){
					//find this value for the same rel in MT
					m = (Integer)MT.relNames.get(v.get(i)); //same relation
					for (int k = 0; k < MT.colNum; k++){
						if (MT.attrMapping[m.intValue()][k] 
						       == this.attrMapping[n.intValue()][j]){
							colMap.put(new Integer(k), new Integer(j));
							tag = 1;
							break;
						}//end if
					}//end for
				}//end if
				if (tag == 1){
					tag = 0;
					break;
				}
			}//end for
		}//end for

		return colMap;
	}
	
	/*
	 * public int getOverlappedColSize(MappingTable MT)
	 * 
	 * compute overlapped col numbers for this and MT
	 */
	public int getOverlappedColSize(MappingTable MT){
		Vector v = findOverlappedRelations(MT);
		//print all relation in v
//		System.out.println("overlapped relations are as follows:");
//		for (int i = 0; i < v.size(); i++) {
//			System.out.println(v.get(i));
//		}
		//end
		
		int[][] temp2 = new int[v.size()][this.attrMapping[0].length]; //only check the overlapped relations
		for (int i = 0; i < v.size(); i++){ //put the attr value of the overlapped relations into temp2 for easy operation
			int row_orig = ((Integer)this.relNames.get(v.get(i))).intValue(); 
			for (int j = 0; j < attrMapping[0].length; j++){
				temp2[i][j] = attrMapping[row_orig][j];
			}		    
		}
		
		/*
		System.out.println("temp2: ");
		for (int i = 0; i < temp2.length; i++){
			for (int j = 0; j < temp2[0].length; j++){
				System.out.print(temp2[i][j] + " ");
			}
				System.out.println();
		}
		*/
		int overlappedColNum = 0;
		for (int j = 0; j < this.attrMapping[0].length; j++){
			for (int i = 0; i < v.size(); i++){ 
				if (temp2[i][j] > 0){
					overlappedColNum++;
					break;
				}
			}
		}
		return overlappedColNum;
/*
		Integer n;
		int overlappedColNum = 0;
		for (int j = 0; j < this.attrMapping[0].length; j++){
			for (int i = 0; i < v.size(); i++){ 
				n = (Integer)this.relNames.get(v.get(i));
//				System.out.println("n = " + n);
				if (this.attrMapping[n.intValue()][j] > 0){
					overlappedColNum++;
					break;
				}
			}
		}
		return overlappedColNum;
*/
	}
	
	public int getActualColSize(MappingTable MT){
//		System.out.println("in getActualColSize...");
//		MT.printMappingTable();
		int colNum = MT.attrMapping[0].length;
//		System.out.println("colNum: " + colNum);///
		//get the actual colNum for this after updating with additional info
		for (int i = 0; i < colNum; i++){
			if (MT.attrMapping[0][i] == 0){
				return i;
			}
		}
		return colNum;
	}
	
	
	public static void main(String[] args){
		testConstructor();

	}
}

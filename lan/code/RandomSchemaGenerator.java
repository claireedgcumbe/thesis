package dataGenerators;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.Vector;

import mediation.Schema;
import minicon.Predicate;


/**
 * 
 * @author jzhao
 *
 */
public class RandomSchemaGenerator {
	
	int numOfRelation; //schema size
	int numOfAvgAttr;
	Random random;
	String[] schemaNames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", 
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", 
			"AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", 
			"AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",
			"BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", 
			"BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ",
			"CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", 
			"CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ",
			"DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", 
			"DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ"};
	
	String[] names = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", 
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", 
			"aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", 
			"ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az",
			"ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", 
			"bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz",
			"ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", 
			"co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz",
			"da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj", "dk", "dl", "dm", "dn", 
			"do", "dp", "dq", "dr", "ds", "dt", "du", "dv", "dw", "dx", "dy", "dz"};
	String[] attrs = {"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	int[] attr;
	Schema lschema;
//	Vector relations;

	
	public RandomSchemaGenerator(int schemaIndex){ //schemaIndex is used for naming this schema
		random = new Random();
		numOfRelation = this.getRandomValue(5, 10); 

//		numOfRelation = this.getRandomValue(3,5);
		//our schema size varies between 5-11
		numOfAvgAttr = 7; //average number of attributes in the current schema
//		numOfAvgAttr = 4;
	    attr = (int[])Array.newInstance(int.class, numOfRelation);
	    lschema = new Schema();
	    lschema.setName(schemaNames[schemaIndex]);
	    
//	    relations = new Vector();


	    genAttrNumForEachRelation();
	    for (int i = 0; i < numOfRelation; i++){
	    	
	    	Predicate relation = new Predicate();
	    	relation.setFunctionHead(schemaNames[schemaIndex] + "." + names[schemaIndex] + i);
	    	for (int j = 0; j < attr[i]; j++){
	    		relation.addVariable(names[schemaIndex] + attrs[i] + j);	    		
	    	}
//	    	relations.addElement(relation);
	    	lschema.addRelation(relation);
	
	    	
/*	    	
	    	StringBuffer relation =  new StringBuffer();
	    	relation.append(names[schemaIndex] + i + "(");
	    	for (int j = 0; j < attr[i] - 1; j++){
	    		relation.append(names[schemaIndex] + attrs[i] + j + ", ");	    		
	    	}
	    	relation.append(names[schemaIndex] + attrs[i] + (attr[i] - 1) + ")");
	    	relations.addElement(relation.toString());
*/
	    }
	}
	
	public void genAttrNumForEachRelation(){
		for (int i = 0; i < attr.length; i++){
			attr[i] = this.getRandomValue(2, 10);
		}//set the attribute number for each relation
		int total = sum(attr);
		while ((int)total/numOfRelation != numOfAvgAttr){
			int toChange = getRandomValue(0, attr.length);
			if ((int)total/numOfRelation > numOfAvgAttr){
				if (attr[toChange] > 2){
					attr[toChange]--;
				}
			}
			else{  //total/numOfRelation < numOfAvgAttr
				if (attr[toChange] < 10){
					attr[toChange]++;
				}
			}
			total = sum(attr);
		}
	}
	
	public int sum(int array[]){
		int total = 0;
		for (int i = 0; i < array.length; i++){
			total += array[i];
		}
		return total;
	}
	
	public int getRandomValue(int start, int end){
		int result;
		Random a = new Random();
		result = (int)(a.nextFloat() * (end - start) + start);		
		return result;
	}
	
	public Schema getSchema(){
		return lschema;
	}
	
	public static void main (String args[]){
		Vector schemas = new Vector();
		for (int i = 0; i < 100; i++){ //generate 100 schemas
			RandomSchemaGenerator rsg = new RandomSchemaGenerator(i);
			schemas.addElement(rsg.lschema);
			System.out.println("Schema " + i + ":");
			for (int j = 0; j < rsg.lschema.numRelations(); j++){
				System.out.println(((Predicate)rsg.lschema.relationI(j)).printString());
			}
			System.out.println();
		}
	}

}

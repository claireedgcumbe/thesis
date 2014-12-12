package dataGenerators;

import java.util.Vector;

/**
 * 
 * @author jzhao
 * 
 * potential acquaintances generator,
 * input: concept array
 * output: list of acquaintances for each node
 * 
 * used for generate topology for the whole network
 *
 */

public class AcquaintanceGenerator {


	int concept[][] = {
			{1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1},
			{1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0}}; 
	
	
	
	Vector potentialList;
	
//	AcquaintanceGenerator(int[][] concept){
	AcquaintanceGenerator(){
//		this.concept = concept;
		potentialList = new Vector();
	    createPotentialList(); 
	}
	
	public void createPotentialList(){
		for (int i = 0; i < concept[0].length; i++){//col
			Vector list = new Vector();
			for (int j = 0; j < concept.length; j++){//row
				if (concept[j][i] == 1){
//					System.out.println("col: " + i + " row: " + j);
					for (int k = 0; k < concept[0].length; k++){
						if (concept[j][k] == 1){
							if (k == i || list.contains(new Integer(k)))
								continue;
//							System.out.println("add " + k + " into list " + i);
							list.addElement(new Integer(k));
						}						
					}
				}
			}
			potentialList.addElement(list);
		}
	}
	

	
	
	public static void main(String args[]){
		AcquaintanceGenerator ag = new AcquaintanceGenerator();
		System.out.println("potential list: ");
		for (int i = 0; i < ag.potentialList.size(); i++){
			System.out.print(i + ": ");
			Vector list = (Vector)ag.potentialList.elementAt(i);
			for (int j = 0; j < list.size(); j++){
				Integer val = (Integer)list.elementAt(j);
				System.out.print(val + " ");
			}
			System.out.println();
		}
		
		
	}
	
	

}

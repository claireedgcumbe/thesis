package dataGenerators;

import java.util.Random;

/**
 * 
 * @author jzhao
 *
 */

public class RandomConceptGenerator {
	
	int numOfSchemas;
	int numOfConcept;
	int[][] choices;
	public RandomConceptGenerator(int numOfSchemas, int numOfConcept, int ratio){
		this.numOfConcept = numOfConcept;
		this.numOfSchemas = numOfSchemas;
		Random a = new Random();
		choices = new int[numOfConcept][numOfSchemas];
		
		for (int i = 0; i < numOfConcept; i++){
			for (int j = 0; j < numOfSchemas; j++){
				
				int possibility = (a.nextInt(ratio) + 1)/ratio;
				choices[i][j] = possibility;
			}			
		}
		
		for (int i = 0 ; i < numOfSchemas; i++){
			if (sum(choices, i) == 0){ //every schema should contain at least one concept
//				System.out.println("column " + i + " is zero");
				
				int change = a.nextInt(numOfConcept);
				choices[change][i] = 1;
			}
			if (sum(choices, i) > 5){  
				//if schema i's concept is more than 6, then del one concept
				//this is because, the average relation number in a schema is 6-7
				
				int change = a.nextInt(numOfConcept);
				choices[change][i] = 0;
			}
				
		}

	}


	public int sum(int[][] array, int col){
		int total = 0;
		for (int i = 0; i < col; i++){
			for (int j = 0; j < array.length; j++){
				total += array[j][col];
			}
		}
		return total;
	}
	
	public static void main (String args[]){
//		RandomConceptGenerator rcg = new RandomConceptGenerator(10, 5, 3);
		RandomConceptGenerator rcg = new RandomConceptGenerator(24, 3, 1);
		System.out.println("choices are: ");
		int colNum = rcg.choices[0].length;
		for (int i = 0; i < rcg.choices.length; i++){
			System.out.print("{");
			for (int j = 0; j < rcg.choices[0].length - 1; j++){
				System.out.print(rcg.choices[i][j] + ", ");
			}
			System.out.print(rcg.choices[i][colNum - 1]);
			System.out.println("},");
		}
	}

}

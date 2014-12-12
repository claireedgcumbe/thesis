package pdms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;

public class CountHop {
	
	/* 
	 * input: topology file
	 * output: for all nodes, caculate the shortest path from the starting node, 
	 *         return the max value
	 * */
	
	
	
	public CountHop(){
		
	}
	
	public static int[] countMaxDuration(Vector tpIn){
		int size = tpIn.size();
		int[][] adjacentM = new int[size][size];
	
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				adjacentM[i][j] = 9999;
			}						
		}
	
		
		/* construct the matrix from topology vector*/
	
		for (int i = 0; i < size; i++){
			Vector acq = (Vector)tpIn.elementAt(i);
			for (int j = 0; j < acq.size(); j++){
				int nodeI = ((Integer)acq.elementAt(j)).intValue();
				adjacentM[i][nodeI] = 1;
				adjacentM[nodeI][i] = 1;
			}
		}
/*		
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				System.out.print(adjacentM[i][j] + " ");
			}
			System.out.println();
		}
*/		
		
		for (int k = 0; k < size; k++){
			for (int i = 0; i < size; i++){
				for (int j = 0; j < size; j++){
					adjacentM[i][j] = Math.min(adjacentM[i][j], adjacentM[i][k] + adjacentM[k][j]);
				}
			}
		}
		
		
		int[] maxDuration = new int[size];
		
		int[] hopFromStartpoint = new int[size];
		
		//count the hop num for each node from the starting node
		hopFromStartpoint[0] = 0;
		for (int i = 1; i < size; i++){
			hopFromStartpoint[i] = adjacentM[0][i];
		}
		
		
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				if (i == j){
					continue;
				}
				int temp = adjacentM[0][j] + adjacentM[j][i];
				if (maxDuration[i] < temp){
					maxDuration[i] = temp;
				}
			}
		}
		
		
/*		
		int max = 0;
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				if (adjacentM[i][j] > max){
					max = adjacentM[i][j];
				}
			}
		}
*/		
		return maxDuration;
	
	
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException{
		Vector tpIn = new Vector();
		FileInputStream fis_topology = new FileInputStream("pdms/topology.log");
		ObjectInputStream ois_topology = new ObjectInputStream(fis_topology);

		
		int size = ois_topology.readInt();
		for (int i = 0; i < size; i++){
			Vector acq = (Vector)ois_topology.readObject();
			tpIn.addElement(acq);
		}
		
		ois_topology.close();
		
		for (int i = 0; i < size; i++){
			System.out.println("acquaintance " + i + ":");
			Vector List = (Vector) tpIn.elementAt(i);
			for (int j = 0; j < List.size(); j++){
				System.out.print((Integer)List.elementAt(j) + " ");
			}
			
			System.out.println();				
		}
		
		int[] max = countMaxDuration(tpIn);
		for (int i = 0; i < size; i++){
			System.out.println("node " + i + " need " + max[i] + " hops");
		}		
	}

}

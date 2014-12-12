package dataGenerators;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

/**
 * 
 * @author jzhao
 *
 */

public class TopologyGenerator {

	Vector potentialLists;
	Vector acquaintanceLists;
	int numOfPeers;
	int acqNum;

	
	public TopologyGenerator(int acqNum) throws IOException{
		this.acqNum = acqNum;

		potentialLists = new Vector();
		AcquaintanceGenerator ag = new AcquaintanceGenerator();
		this.potentialLists = ag.potentialList;
		numOfPeers = potentialLists.size();
		acquaintanceLists = new Vector();	
		for (int i = 0; i < potentialLists.size(); i++){
			Vector List = new Vector();
			acquaintanceLists.addElement(List);
		}
		createTopologyFirstRound();
		
		
		System.out.println("Topology Generated: ");
		System.out.println("size is " + acquaintanceLists.size());
		for (int i = 0; i < acquaintanceLists.size(); i++){
			System.out.print("node " + i + " : ");
			Vector acqList = (Vector)acquaintanceLists.elementAt(i);
			for (int j = 0; j < acqList.size(); j++){
				System.out.print((Integer)(acqList.elementAt(j)) + " ");
			}
			System.out.println();
		}
		
/*		
		System.out.println("total degree generated is " + calculateEdge());
		System.out.println("accept this TopologyGenerator? (y for yes)");
		
		
		char in = 0;
		in = (char)System.in.read();
	
		while (in != 'y'){
			potentialLists = new Vector();
			ag = new AcquaintanceGenerator();
			this.potentialLists = ag.potentialList;
			numOfPeers = potentialLists.size();
			acquaintanceLists = new Vector();	
			for (int i = 0; i < potentialLists.size(); i++){
				Vector List = new Vector();
				acquaintanceLists.addElement(List);
			}
			createTopologyFirstRound();			
			
			System.out.println("Topology Generated: ");
			System.out.println("size is " + acquaintanceLists.size());
			for (int i = 0; i < acquaintanceLists.size(); i++){
				System.out.print("node " + i + " : ");
				Vector acqList = (Vector)acquaintanceLists.elementAt(i);
				for (int j = 0; j < acqList.size(); j++){
					System.out.print((Integer)(acqList.elementAt(j)) + " ");
				}
				System.out.println();
			}
			System.out.println("total degree generated is " + calculateEdge());
			System.out.println("accept this TopologyGenerator? (y for yes)");
			in = (char)System.in.read();
   	    }
   	    
 */  	    
   	    
		checkAndConnect();
		
		
		System.out.println("Topology Generated finally: ");
		System.out.println("size is " + acquaintanceLists.size());
		for (int i = 0; i < acquaintanceLists.size(); i++){
			System.out.print("node " + i + " : ");
			Vector acqList = (Vector)acquaintanceLists.elementAt(i);
			for (int j = 0; j < acqList.size(); j++){
				System.out.print((Integer)(acqList.elementAt(j)) + " ");
			}
			System.out.println();
		}
		System.out.println("total degree generated is " + calculateEdge());
			
	}

/*
	public void createTopologyFirstRound(){
		int firstRoundAcq = acqNum - 1;
		Random a = new Random();
		for (int i = 0; i < potentialLists.size(); i++){
			Vector List = (Vector)acquaintanceLists.elementAt(i);
			Vector potList = (Vector)potentialLists.elementAt(i);
			for (int j = 0; j < firstRoundAcq; j++){
//				System.out.println("node " + i + " , round " + j);
				int k = potList.size(); //k is the range that we can randomly pick up one spot
				if (k == 0){
					continue;
				}
				if (List.size() >= firstRoundAcq){
//					System.out.println(i + " size " + List.size());
					continue;
				}
				
				
				int random = a.nextInt(k); 
//				System.out.println("random: " + random);
				int pick = ((Integer)potList.elementAt(random)).intValue();//pick is the real num (peer num) that spot k stores
//				System.out.println("pick: " + pick);
				
				Vector List2;
				Vector potList2;
				List2 = (Vector)acquaintanceLists.elementAt(pick);	
				
		
				List.addElement(new Integer(pick));
				potList.remove(random);
				//now handling this acquaintance

				List2.add(new Integer(i));
				potList2 = (Vector)potentialLists.elementAt(pick);
				int indexToRm = potList2.indexOf(new Integer(i));
				potList2.remove(indexToRm);
								
			}			
		}
	}
*/	
	
	public void createTopologyFirstRound(){
		int firstRoundAcq = 1;
		Random a = new Random();
		for (int i = 0; i < potentialLists.size(); i++){
			Vector List = (Vector)acquaintanceLists.elementAt(i);
			Vector potList = (Vector)potentialLists.elementAt(i);
			for (int j = 0; j < firstRoundAcq; j++){
//				System.out.println("node " + i + " , round " + j);
				int k = potList.size(); //k is the range that we can randomly pick up one spot
				if (k == 0){
					continue;
				}
				if (List.size() >= firstRoundAcq){
//					System.out.println(i + " size " + List.size());
					continue;
				}
				
				
				int random = a.nextInt(k); 
//				System.out.println("random: " + random);
				int pick = ((Integer)potList.elementAt(random)).intValue();//pick is the real num (peer num) that spot k stores
//				System.out.println("pick: " + pick);
				
				Vector List2;
				Vector potList2;
				List2 = (Vector)acquaintanceLists.elementAt(pick);	
				
		
				List.addElement(new Integer(pick));
				potList.remove(random);
				//now handling this acquaintance

				List2.add(new Integer(i));
				potList2 = (Vector)potentialLists.elementAt(pick);
				int indexToRm = potList2.indexOf(new Integer(i));
				potList2.remove(indexToRm);
								
			}			
		}
	}
	
	/*
	 * check whether after the first round, the topology is already 
	 * fully connected; otherwise, make it connected
	 */
	public void checkAndConnect(){
		int[] connectTag = (int[])Array.newInstance(int.class, numOfPeers);
		connectTag[0] = 1;
		Vector List;
		LinkedList ll = new LinkedList();
		ll.addLast(new Integer(0)); //start caculating connectivity from node 0;
		int ptr_for_ll = 0;
		while (!ll.isEmpty() && ll.size() > ptr_for_ll){
			int nextIndex = ((Integer)ll.get(ptr_for_ll)).intValue();
			connectTag[nextIndex] = 1;
			List = (Vector)this.acquaintanceLists.elementAt(nextIndex);
			for (int i = 0; i < List.size(); i++){
				Integer aa = (Integer)List.elementAt(i);
				if (ll.contains(aa)){
					continue;
				}
				ll.addLast(List.elementAt(i));
			}
//			ll.removeFirst();
			ptr_for_ll++;
		}
		System.out.println("after check: ");
		for (int i = 0; i < numOfPeers; i++){
			System.out.print(connectTag[i] + " ");
		}
		System.out.println();
		
		/*
		 * handle isolated points
		 */
		
		Random a = new Random();
//		LinkedList isolatedPoints = new LinkedList();
		for (int i = 0; i < connectTag.length; i++){
			if (connectTag[i] == 0){
				Vector plist = (Vector)this.potentialLists.elementAt(i);
				int pick;
				int random;
				int kk = 0;
				do{
					random = a.nextInt(plist.size());
					pick = ((Integer)plist.elementAt(random)).intValue();
					kk++;
				}
				while(connectTag[pick] == 0 && kk < acqNum);
				if (kk == acqNum){
					System.out.println("no solution for current topology, please run again!");
					break;
				}
				Vector List1 = (Vector)acquaintanceLists.elementAt(i);
				Vector potList1 = (Vector)potentialLists.elementAt(i);
				List1.addElement(new Integer(pick));
				potList1.removeElementAt(random);
				
				Vector List2 = (Vector)acquaintanceLists.elementAt(pick);
				Vector potList2 = (Vector)potentialLists.elementAt(pick);
				List2.addElement(new Integer(i));
				int indexToRm = potList2.indexOf(new Integer(i));
				potList2.remove(indexToRm);
				
			}
		}
		
		System.out.println("after handling isolated points: ");
		for (int i = 0; i < numOfPeers; i++){
			System.out.print(connectTag[i] + " ");
		}
		System.out.println();
		
		
		/*
		 * handle the remaining edges
		 */
		
		System.out.println("remaining edges to be added: " + this.remainingEdge());
		
		int remain = this.remainingEdge();
		for (int i = 0; i < this.acquaintanceLists.size() && remain > 0; i++){
			Vector List1 = (Vector)acquaintanceLists.elementAt(i);
			Vector potList1 = (Vector)potentialLists.elementAt(i);
			if (List1.size() < acqNum && potList1.size() > 0){
				int random = a.nextInt(potList1.size());				
				int pick = ((Integer)potList1.elementAt(random)).intValue();
				List1.addElement(new Integer(pick));
				potList1.remove(random);
				
				Vector List2 = (Vector)acquaintanceLists.elementAt(pick);
				Vector potList2 = (Vector)potentialLists.elementAt(pick);
				List2.addElement(new Integer(i));
				int indexToRm = potList2.indexOf(new Integer(i));
				potList2.remove(indexToRm);
				remain -= 2;
			}
			if (i == acquaintanceLists.size() && remain > 0){
				i = 0;
			}
		}		
			
	}
	
	public int calculateEdge(){
		int total = 0;
		for (int i = 0; i < this.acquaintanceLists.size(); i++){
			Vector List = (Vector)acquaintanceLists.elementAt(i);
			total += List.size();
		}
		
		return total;
	}
	
	public int remainingEdge(){
		int remaining = 0;
		remaining = acqNum * numOfPeers - calculateEdge() + 2;
		return remaining;
	}
	

	
	public static void main(String args[]) throws IOException{
//		int acqnum = (int)System.in.read();		
		TopologyGenerator tg = new TopologyGenerator(3);

/*
		System.out.println("remaining potential list: ");
		System.out.println("size is " + tg.potentialLists.size());
		for (int i = 0; i < tg.potentialLists.size(); i++){
			System.out.print("node " + i + " : ");
			Vector acqList = (Vector)tg.potentialLists.elementAt(i);
			for (int j = 0; j < acqList.size(); j++){
				System.out.print((Integer)(acqList.elementAt(j)) + " ");
			}
			System.out.println();
		}
*/		

		
		
	}
	

}

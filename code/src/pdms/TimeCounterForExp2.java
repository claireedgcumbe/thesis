package pdms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class TimeCounterForExp2 {

	static String[] schemaNames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", 
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", 
			"AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", 
			"AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",
			"BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", 
			"BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ",
			"CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", 
			"CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ",
			"DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", 
			"DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ"};
	public TimeCounterForExp2(){
		
	}
	
	public static void main(String args[]) throws IOException{
		
		int totalPeerNum = Integer.parseInt(args[0]); 
		long startTime;
		int lastone = 0;
		
		FileReader file1 = new FileReader("log/timer/systemstart.log");
		BufferedReader fileInput1 = new BufferedReader(file1);
        String line1 = fileInput1.readLine();
         
        startTime = Long.parseLong(line1);
        System.out.println("start:" + startTime);
        fileInput1.close();
        
        long[] timeForQT = new long[totalPeerNum];
        long[] endTime = new long[totalPeerNum];
        
        for (int i = 0; i < totalPeerNum; i++){
			
			FileReader file = new FileReader("log/timer/queryc" + schemaNames[i] + ".log");
			BufferedReader fileInput = new BufferedReader(file);
			String line = fileInput.readLine();

	        String splitStrings[];

	        splitStrings = line.split(" ");
	        long eTime, sTime = 0;
//	        System.out.println("splitstring length: " + splitStrings.length);

	        sTime = Long.parseLong(splitStrings[2]);
	        eTime = Long.parseLong(splitStrings[4]);
	        endTime[i] = eTime;
	        timeForQT[i] = eTime - sTime;
	    
	        fileInput.close();
		}
        
        
        

        long latestEndTime = 0;
		for (int i = 0; i < totalPeerNum; i++){
	        if (endTime[i] > latestEndTime){
	        	latestEndTime = endTime[i];
	        	lastone = i;
	        }
		}
		
		long avg = 0;
		long max = 0;
		long min = 999999999;
		
		for (int i = 0; i < totalPeerNum; i++){
			System.out.println("node " + i + " spent time in QT : " + timeForQT[i]);
			avg += timeForQT[i];
			if (timeForQT[i] > max){
				max = timeForQT[i];
			}
			if (timeForQT[i] < min){
				min = timeForQT[i];
			}
		}
		avg /= totalPeerNum;
		System.out.println("average spent time in QT : " + avg);
		
		System.out.println("max spent time in QT : " + max);
		
		System.out.println("min spent time in QT : " + min);
		
		System.out.println("endTime:" + latestEndTime);
		
		long duration = latestEndTime - startTime;
		System.out.println("Total time used(ns)		" + duration);
		
		

		System.out.println("lastone = " + lastone);
		
		
		
/*		
		
//		FileInputStream in;
//		String line;
	
		int[] numOfLC = new int[totalPeerNum];
		long[] timeForLocal = new long[totalPeerNum];
		long[] avgForLC = new long[totalPeerNum];
		
		for (int i = 0; i < totalPeerNum; i++){
			
			FileReader file = new FileReader("log/timer/queryc" + schemaNames[i] + ".log");
			BufferedReader fileInput = new BufferedReader(file);
			String line = fileInput.readLine();

	        String splitStrings[];

	        splitStrings = line.split(" ");
	        long eTime, sTime = 0;
//	        System.out.println("splitstring length: " + splitStrings.length);
	        for (int j = 1; j < splitStrings.length; j+=4){
	        	sTime = Long.parseLong(splitStrings[j + 1]);
	        	eTime = Long.parseLong(splitStrings[j + 3]);
	        	timeForLocal[i] += eTime - sTime;
	        	numOfLC[i]++;
	        }
	        avgForLC[i] = timeForLocal[i]/numOfLC[i];
	        System.out.println("Peer " + i + " spent time in local computing (querycNum|querycTime|avg) 	" 
	        		+ numOfLC[i] + "	" + timeForLocal[i] + "		" + avgForLC[i]);
	        fileInput.close();
		}
		
		int[] numOfMsg = new int[totalPeerNum];
		long[] timeForMsg = new long[totalPeerNum];
		long[] avgForMsg = new long[totalPeerNum];
		for (int i = 0; i < totalPeerNum; i++){
			
			FileReader file = new FileReader("log/timer/Msg" + schemaNames[i] + ".log");
			BufferedReader fileInput = new BufferedReader(file);
			String line = fileInput.readLine();

	        String splitStrings[];

	        splitStrings = line.split(" ");
	        long eTime, sTime = 0;
	        for (int j = 1; j < splitStrings.length; j+=4){
	        	sTime = Long.parseLong(splitStrings[j + 1]);
	        	eTime = Long.parseLong(splitStrings[j + 3]);
	        	timeForMsg[i] += eTime - sTime;
	        	numOfMsg[i]++;
	        }
	        avgForMsg[i] = timeForMsg[i]/numOfMsg[i];
	        System.out.println("Peer " + i + " spent time in delivering msg	(MsgNum|MsgTime|avg)	" 
	        		+ numOfMsg[i] + "	 " + timeForMsg[i] + "		" + avgForMsg[i]);
	        fileInput.close();
		}
		
		long[] totalTForPeer = new long[totalPeerNum];
		long[] waitTForPeer = new long[totalPeerNum];
		long totalWT = 0;
		for (int i = 0; i < totalPeerNum; i++){
			totalTForPeer[i] = timeForLocal[i] + timeForMsg[i];
			waitTForPeer[i] = duration - totalTForPeer[i];
			System.out.println("Peer " + i + " spent time in waiting 	" + waitTForPeer[i]);
			totalWT += waitTForPeer[i];
		}
		System.out.println("average waiting time for each peer 		" + totalWT/totalPeerNum);

*/
	}

}

package pdms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TimeCounter {
	
    int totalPeerNum;
    File[] files;
    long[] modified;
    
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
    
	public TimeCounter(int totalpeernum) throws IOException{
		files = new File[totalpeernum];
		this.totalPeerNum = totalpeernum;
		modified = new long[totalpeernum];
		for (int i = 0; i < totalpeernum; i++){
			String fn = "log/timer/" + schemaNames[i] + ".log";
			files[i] = new File(fn);
			boolean succ = files[i].createNewFile();
			if (succ == true){
				System.out.println("file " + fn + " created");
			}
			modified[i] = files[i].lastModified();
//			System.out.println(schemaNames[i] + ": " + modified[i]);
			
		}
	}
	
	public void check() throws IOException{

//		String fileName2 = "log/timer/timeCounter.log";		  
//		FileWriter fw = new FileWriter(fileName2);

		/*
		String fileName1 = "log/timer/0.log";
		while (!(new File(fileName1)).exists());
		*/
		String fileName1 = "log/timer/0.log";
		File f = new File(fileName1);
		f.createNewFile();
		long modify0 = f.lastModified();
		while(f.lastModified() == modify0);
				
		
		Timer.timerStart = System.currentTimeMillis(); 
		System.out.println("start...");
  
		
		
		

		int tag = 0;
		while(true){
//			System.out.println(Timer.timerEnd - Timer.timerStart);
			if (tag == 0 && System.currentTimeMillis() - Timer.timerStart > 10*1000){ //30 seconds no files changed, stop
				break;
			}
			tag = 0;
			for (int i = 0; i < totalPeerNum; i++){
				if (files[i].lastModified() > this.modified[i]){
//					System.out.println("i: " + files[i].lastModified());
					Timer.timerEnd = System.currentTimeMillis();
//					System.out.println("End: " + Timer.timerEnd);
					this.modified[i] = files[i].lastModified();
					tag = 1;
				}
			}
		}
		
		System.out.println(Timer.timerEnd - Timer.timerStart);
		
/*		
		long startTime;
		FileReader file1 = new FileReader("log/timer/0.log");
		BufferedReader fileInput1 = new BufferedReader(file1);
        String line1 = fileInput1.readLine();
         
        startTime = Long.parseLong(line1);
        System.out.println("start:" + startTime);
        fileInput1.close();
        
        long[] endTime = new long[totalPeerNum];
        long latestEndTime = 0;
		for (int i = 0; i < totalPeerNum; i++){
			FileReader file = new FileReader("log/timer/" + schemaNames[i] + ".log");
			BufferedReader fileInput = new BufferedReader(file);
	        String line = fileInput.readLine();

	        String splitStrings[];

//	        System.out.println(schemaNames[i] + ":" + line);
	        splitStrings = line.split(" ");
	        int num = splitStrings.length;
	        endTime[i] = Long.parseLong(splitStrings[num - 1]);	    
	        if (endTime[i] > latestEndTime){
	        	latestEndTime = endTime[i];
	        }
	        fileInput.close();
		}
		
		
		for (int i = 0; i < totalPeerNum; i++){
			System.out.println("node " + i + ":" + endTime[i]);
		}
		
		System.out.println("endTime:" + latestEndTime);
		
		long duration = latestEndTime - startTime;
		System.out.println("Total time used(ns)		" + duration);

		
		
		
		
//		FileInputStream in;
//		String line;
	
		int[] numOfLC = new int[totalPeerNum];
		long[] timeForLocal = new long[totalPeerNum];
		long[] avgForLC = new long[totalPeerNum];
		
		for (int i = 0; i < totalPeerNum; i++){
			
			FileReader file = new FileReader("log/timer/lc" + schemaNames[i] + ".log");
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
	        System.out.println("Peer " + i + " spent time in local computing (lcNum|lcTime|avg) 	" 
	        		+ numOfLC[i] + " 	" + timeForLocal[i] + "		" + avgForLC[i]);
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
	
	public static void main(String args[]) throws IOException{
		int peernum = Integer.parseInt(args[0]);
		TimeCounter tc = new TimeCounter(peernum);
		tc.check();
	
	}

}

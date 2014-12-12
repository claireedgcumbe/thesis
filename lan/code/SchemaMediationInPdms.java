package pdms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Vector;


import rice.environment.Environment;
import rice.pastry.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

import mediation.GLAVMapping;
import mediation.Mapping;
import mediation.MappingTable;
import mediation.Schema;

/*
 * @author:jzhao
 * May 15, 2006
 */

public class SchemaMediationInPdms {
	  // this will keep track of our applications

	 protected int nodeSeq;
	 public Hashtable nodeHash;
	 
	 Vector schemasIn;
	 Vector mappingsIn;
	 Vector tpIn;
	 PdmsApp app;
	 
	 public SchemaMediationInPdms(int bindport, InetSocketAddress bootaddress,
			 int nodeSeq, Environment env, int totalPeerNum) throws Exception {
		 // Generate the NodeIds Randoml
		 nodeHash = new Hashtable();
		 buildNodeHash();
		 this.nodeSeq = nodeSeq; 
		 
		 schemasIn = new Vector();
		 mappingsIn = new Vector();
		 tpIn = new Vector();
		 
         //	Generate the NodeIds Randomly
		 NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
		 // construct the PastryNodeFactory, this is how we use rice.pastry.socket
		 
		 String selfIPandPort = (String)nodeHash.get(new Integer(nodeSeq));
		 int index = selfIPandPort.indexOf(":");
		 String selfIP = selfIPandPort.substring(0, index);
		 
		 
		 InetAddress selfAddr = InetAddress.getByName(selfIP);

		 PastryNodeFactory factory = new SocketPastryNodeFactory(nidFactory, selfAddr,bindport, env);
		 
		 // This will return null if we there is no node at that location		 
		 NodeHandle bootHandle = ((SocketPastryNodeFactory) factory).getNodeHandle(bootaddress);
		 
		 // construct a node, passing the null boothandle on the first loop will cause the node to start its own ring		 
		 PastryNode node = factory.newNode(bootHandle);
		 
		 // the node may require sending several messages to fully boot into the ring
		 while (!node.isReady()) {
		 // delay so we don't busy-wait
			 Thread.sleep(100);
		 }

		 System.out.println("Finished creating new node " + node);

		 // construct a new MyApp
		 app = new PdmsApp(node, factory);
		 

		 readInFiles();
		 Schema lschema = (Schema)schemasIn.elementAt(nodeSeq);
		 app.setPeerSchema((Schema)schemasIn.elementAt(nodeSeq));
		 Vector acqList = (Vector)tpIn.elementAt(nodeSeq);
		 for (int i = 0; i < acqList.size(); i++){
			 int acq = ((Integer)acqList.elementAt(i)).intValue();
			 app.setAcquaintance(acq);
		 }
		 for (int i = 0; i < mappingsIn.size(); i++){
			 Mapping map = (Mapping) mappingsIn.elementAt(i);
			 Schema schema0 = (Schema)map.getSchemaInfo(0);
			 Schema schema1 = (Schema)map.getSchemaInfo(1);
			 
			 
			 if (schema0.getName().equals(lschema.getName()) || 
					 schema1.getName().equals(lschema.getName())){
				 app.setMapping(map);
			 }
		 }
		 
		 System.out.println(app.getPeerSchema().printString());
		 System.out.println(app.mappings.size());
		 for (int i = 0; i < app.mappings.size(); i++){
			 System.out.println(((Mapping)app.mappings.elementAt(i)).printString());
		 }
		 
		 
		 
		 /*
    	  * for Asynchronism: create file "nodeSeq.log" in directory "log"
    	  */
    	 
		 File file = new File("log/"+ nodeSeq + ".log"); 
		 // seperator: "/"	for unix and windows, "\\" for windows
		 // Create file if it does not exist
		 boolean success = file.createNewFile();
		 if (success) {
			 System.out.println("File "+  nodeSeq + ".log was created.");
		 }
	
	
		 // wait for other peer nodes to be created
		 boolean flag;
		 do {
			 flag = true;
        	   
			 for (int i = 0; i < totalPeerNum; i++){
				 String fileName = "log/" + i + ".log";
				 boolean exists = (new File(fileName)).exists();
				 flag = flag && exists;
				 if (flag == false)
					 break;
			 }
		 }while(flag == false); // when flag is true, the loop terminates.
                                 // all peer nodes have been created.
		 
		 
	     if (nodeSeq != 0){ //not the starting node
	    	 app.setAcq_handle();
	    	 File fileHandle = new File("log/handle"+ nodeSeq + ".log"); 
			 // seperator: "/"	for unix and windows, "\\" for windows
			 // Create file if it does not exist
			 boolean succ = fileHandle.createNewFile();
			 if (succ) {
				 System.out.println("File handle"+  nodeSeq + ".log was created.");
			 }
/*
	    	 System.out.println("this is the starting nodes. " +
	    	 		"all other nodes ready? (y for yes)");
	    	 char in = (char)System.in.read();
	    	 while (in != 'y'){
	    		 System.out.println("all other nodes ready? (y for yes)");
		    	 in = (char)System.in.read(); 
	    	 }
	    	    	 
	    	 System.out.println("nodeHanle set? (y for yes)");
	    	 while (in != 'y'){
	    		 System.out.println("all other nodes ready? (y for yes)");
		    	 in = (char)System.in.read(); 
	    	 }	    	 
*/
	      }
	     else if (nodeSeq == 0){
	    	 app.setAcq_handle();
	    	 File fileHandle = new File("log/handle"+ nodeSeq + ".log"); 
			 // seperator: "/"	for unix and windows, "\\" for windows
			 // Create file if it does not exist
			 boolean succ = fileHandle.createNewFile();
			 if (succ) {
				 System.out.println("File handle"+  nodeSeq + ".log was created.");
			 }
	    	 
	    	 boolean flag1;
			 do {
				 flag1 = true;
	        	   
				 for (int i = 1; i < totalPeerNum; i++){
					 String fileName = "log/handle" + i + ".log";
					 boolean exists = (new File(fileName)).exists();
					 flag1 = flag1 && exists;
					 if (flag1 == false){
//						 System.out.println("log/handle" + i + ".log not exist");
						 break;
					 }
						 
				 }
			 }while(flag1 == false); 
	    	     	 
	    	 app.sendBroadCastingMsg(app.getNode().getLocalNodeHandle());
/*	    	 System.out.println("all other nodes ready? (y for yes)");
	    	 char in = (char)System.in.read();
	    	 while(in != 'y'){
	    		 System.out.println("all other nodes ready? (y for yes)");
		    	 in = (char)System.in.read();
	    	 }
*/	    	 
	    	    	 
	     }		   
	  }
	 
	 
	  public void readInFiles() throws IOException, ClassNotFoundException{
		  
//			FileInputStream fis_schemas = new FileInputStream("C:\\Temp\\jzhao\\Project\\PDMSwithSchemaMediation\\pdms\\schemas.log");
		    FileInputStream fis_schemas = new FileInputStream("pdms/schemas.log");
		    ObjectInputStream ois_schemas = new ObjectInputStream(fis_schemas);
			
			int size = ois_schemas.readInt();
			for (int i = 0; i < size; i++){
				Schema s = (Schema)ois_schemas.readObject();
				schemasIn.addElement(s);
			}
			
			ois_schemas.close();
			
/*
			for (int i = 0; i < schemasIn.size(); i++){
				System.out.println("Schema " + i + ":");
				Schema sc = (Schema)schemasIn.elementAt(i); //current schema
				for (int j = 0; j < sc.numRelations(); j++){
					System.out.println((sc.relationI(j)).printString());
				}
				System.out.println();
					
			}
*/			
			
//			FileInputStream fis_mappings = new FileInputStream("C:\\Temp\\jzhao\\Project\\PDMSwithSchemaMediation\\pdms\\mappings.log");
			FileInputStream fis_mappings = new FileInputStream("pdms/mappings.log");
			ObjectInputStream ois_mappings = new ObjectInputStream(fis_mappings);

			size = ois_mappings.readInt();
					
			
			for (int i = 0; i < size; i++){
				Mapping m = (Mapping)ois_mappings.readObject();
				mappingsIn.addElement(m);
			}
			
			ois_mappings.close();
			
/*
			for (int i = 0; i < mappingsIn.size(); i++){
				System.out.println("mapping " + i + ":");
				Mapping m = (Mapping)mappingsIn.elementAt(i); //current schema
				System.out.println(m.printString());				
//				System.out.println("schemas in this mapping :" + ((Schema)m.getSchemaInfo(0)).printString());
//				System.out.println("schemas in this mapping :" + ((Schema)m.getSchemaInfo(1)).printString());
			}
			
*/			
			
//			FileInputStream fis_topology = new FileInputStream("C:\\Temp\\jzhao\\Project\\PDMSwithSchemaMediation\\pdms\\topology.log");
			FileInputStream fis_topology = new FileInputStream("pdms/topology.log");
			ObjectInputStream ois_topology = new ObjectInputStream(fis_topology);

			size = ois_topology.readInt();
			for (int i = 0; i < size; i++){
				Vector acq = (Vector)ois_topology.readObject();
				tpIn.addElement(acq);
			}
			
			ois_topology.close();
/*			
			for (int i = 0; i < size; i++){
				System.out.println("acquaintance " + i + ":");
				Vector List = (Vector) tpIn.elementAt(i);
				for (int j = 0; j < List.size(); j++){
					System.out.print((Integer)List.elementAt(j) + " ");
				}				
				System.out.println();				
			}
*/
	  }

	  /*	  	  	  
	  public void buildNodeHash(){
	  
		  nodeHash.put(new Integer(0), "hal.cs.ubc.ca: 9001");
		  nodeHash.put(new Integer(1), "hal.cs.ubc.ca: 9002");
		  nodeHash.put(new Integer(2), "hal.cs.ubc.ca: 9003");
		  nodeHash.put(new Integer(3), "hal.cs.ubc.ca: 9004");
		  nodeHash.put(new Integer(4), "hal.cs.ubc.ca: 9005");		
		  nodeHash.put(new Integer(5), "hal.cs.ubc.ca: 9006");
		  nodeHash.put(new Integer(6), "hal.cs.ubc.ca: 9007");
		  nodeHash.put(new Integer(7), "hal.cs.ubc.ca: 9008");
		  nodeHash.put(new Integer(8), "hal.cs.ubc.ca: 9009");
		  nodeHash.put(new Integer(9), "hal.cs.ubc.ca: 9010");	
	  }
*/
	    
	  public void buildNodeHash(){
		  
		  nodeHash.put(new Integer(0), "node-1-big-lan: 9001");
		  nodeHash.put(new Integer(1), "node-2-big-lan: 9001");
		  nodeHash.put(new Integer(2), "node-3-big-lan: 9001");
		  nodeHash.put(new Integer(3), "node-4-big-lan: 9001");
		  nodeHash.put(new Integer(4), "node-5-big-lan: 9001");		
		  nodeHash.put(new Integer(5), "node-6-big-lan: 9001");
		  nodeHash.put(new Integer(6), "node-7-big-lan: 9001");
		  nodeHash.put(new Integer(7), "node-8-big-lan: 9001");
		  nodeHash.put(new Integer(8), "node-9-big-lan: 9001");
		  nodeHash.put(new Integer(9), "node-10-big-lan: 9001");	
		  nodeHash.put(new Integer(10), "node-11-big-lan: 9001");
		  nodeHash.put(new Integer(11), "node-12-big-lan: 9001");
		  nodeHash.put(new Integer(12), "node-13-big-lan: 9001");
		  nodeHash.put(new Integer(13), "node-14-big-lan: 9001");
		  nodeHash.put(new Integer(14), "node-15-big-lan: 9001");		
		  nodeHash.put(new Integer(15), "node-16-big-lan: 9001");
		  nodeHash.put(new Integer(16), "node-17-big-lan: 9001");
		  nodeHash.put(new Integer(17), "node-18-big-lan: 9001");
		  nodeHash.put(new Integer(18), "node-19-big-lan: 9001");
		  nodeHash.put(new Integer(19), "node-20-big-lan: 9001");	
		  nodeHash.put(new Integer(20), "node-21-big-lan: 9001");
		  nodeHash.put(new Integer(21), "node-22-big-lan: 9001");
		  nodeHash.put(new Integer(22), "node-23-big-lan: 9001");
		  nodeHash.put(new Integer(23), "node-24-big-lan: 9001");
		  nodeHash.put(new Integer(24), "node-25-big-lan: 9001");		
		  nodeHash.put(new Integer(25), "node-26-big-lan: 9001");
		  nodeHash.put(new Integer(26), "node-27-big-lan: 9001");
		  nodeHash.put(new Integer(27), "node-28-big-lan: 9001");
		  nodeHash.put(new Integer(28), "node-29-big-lan: 9001");
		  nodeHash.put(new Integer(29), "node-30-big-lan: 9001");	
		  
	  }

	  /**
	   * Usage: HelloWorld [-msgs m] [-nodes n] [-verbose|-silent|-verbosity v]
	   * [-simultaneous_joins] [-simultaneous_msgs] [-help]
	   *
	   * @param args DESCRIBE THE PARAMETER
	   */
	  public static void main(String args[]) throws Exception {
	    
	    Environment env = new Environment();
	    int seq; 
	    // the port to use locally
	    int bindport = Integer.parseInt(args[0]);

//	      int bindport = Integer.parseInt("9001");
	      
	    // build the bootaddress
	      
	    InetAddress bootaddr = InetAddress.getByName("node-1-big-lan");
	
//	      InetAddress bootaddr = InetAddress.getByName("hal.cs.ubc.ca");
	    int bootport = Integer.parseInt("9001");


	    InetSocketAddress bootaddress = new InetSocketAddress(bootaddr, bootport);

	    seq = Integer.parseInt(args[1]);
//	      int seq = Integer.parseInt(args[0]);
	      
	    int totalpeernum = Integer.parseInt(args[2]);
//	      int totalpeernum = Integer.parseInt(args[1]);
	      
	    // launch our node!
	    SchemaMediationInPdms driver = new SchemaMediationInPdms(bindport, bootaddress, 
	    		seq, env, totalpeernum);
   
	       
	    if (seq == 0){
	    	String fileName1 = "log/canWriteResults.log";
		    File f = new File(fileName1);
		    f.createNewFile();
		    long modify0 = f.lastModified();
		    while(f.lastModified() == modify0);
			    
		    
		    
			FileOutputStream output = new FileOutputStream("log/Query/finalResult.log");
			ObjectOutputStream oos = new ObjectOutputStream(output);
		
			Vector glavMaps = driver.app.getGLAVMapping();
			oos.writeInt(glavMaps.size());
			for (int i = 0; i < glavMaps.size(); i++){
				GLAVMapping glavmap = (GLAVMapping) glavMaps.elementAt(i);
				oos.writeObject(glavmap);
			}
			
			Schema ms = driver.app.getMediatedSchema();
			oos.writeObject(ms);
				
			Vector mtsets = driver.app.mtSet;
			
			oos.writeInt(mtsets.size());
			for (int i = 0; i < mtsets.size(); i++){
				MappingTable mtset = (MappingTable) mtsets.elementAt(i);
				oos.writeObject(mtset);
			}
			
			Vector mappingInfo = driver.app.mappingInfo;
			
			oos.writeInt(mappingInfo.size());
			for (int i = 0; i < mappingInfo.size(); i++){
				String minfo = (String)mappingInfo.elementAt(i);
				oos.writeObject(minfo);
			}
					
			oos.close();
	
	    }
	    
	   
    
/*
	    try {
	      Thread.sleep(2200);
	    } catch (InterruptedException ie) {
	    }
	    
	    env.destroy();
*/	  
	  } 
	  
}


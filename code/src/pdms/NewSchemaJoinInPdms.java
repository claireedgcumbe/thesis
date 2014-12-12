package pdms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Vector;

import mediation.GLAVMapping;
import mediation.Mapping;
import mediation.MappingTable;
import mediation.Schema;
import minicon.View;
import rice.environment.Environment;
import rice.pastry.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

public class NewSchemaJoinInPdms {
	protected int nodeSeq;
	 public Hashtable nodeHash;
	 
	 Vector schemasIn;
//	 Vector mappingsIn;
	 Vector tpIn;
	 Vector glavsIn;
	 Schema m_schema;
	 Vector mtsIn;
	 Vector mappingInfosIn;
	 
	 NewSchemaJoinApp app;
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
	 
	 public NewSchemaJoinInPdms(int bindport, InetSocketAddress bootaddress,
			 int nodeSeq, Environment env, int totalPeerNum) throws Exception {
		 // Generate the NodeIds Randoml
		 nodeHash = new Hashtable();
		 this.nodeSeq = nodeSeq; 
		 buildNodeHash();
		 
		 schemasIn = new Vector();
//		 mappingsIn = new Vector();
		 tpIn = new Vector();
		 glavsIn = new Vector();
		 m_schema = new Schema();
		 mtsIn = new Vector();
		 mappingInfosIn = new Vector();
		 
		 
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
		 app = new NewSchemaJoinApp(node, factory);
		 
		 System.out.println("new schema join app created!!!!");
		 

		 /*
		  * read content from files and install the peerschema, acquaintances,
		  * mediated schema and glav mapping onto that peer
		  */
		 readInFiles();
		
		 app.setPeerSchema((Schema)schemasIn.elementAt(nodeSeq));
		 Vector acqList = (Vector)tpIn.elementAt(nodeSeq);
		 for (int i = 0; i < acqList.size(); i++){
			 int acq = ((Integer)acqList.elementAt(i)).intValue();
			 app.setAcquaintance(acq);
		 }
		 app.setMediatedSchema(m_schema);
		
		 app.setGLAVMappings(this.glavsIn);
		 
		 app.setMappingInfos(this.mappingInfosIn);
		 
		 app.setMtSet(this.mtsIn);
		 
		 
//		 String peerName = schemaNames[nodeSeq];		 
		 
		 
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
						 break;
					 }
						 
				 }
			 }while(flag1 == false); 
	    	     
			 FileReader queryfile = new FileReader("log/NewMapping/newmapping.log");
			 BufferedReader fileInput = new BufferedReader(queryfile);
			 Mapping map = new Mapping();
			 int size = Integer.parseInt(fileInput.readLine());
			 for(int i = 0; i < size; i++){
				 String newmapping = fileInput.readLine();
				 View a = new View(newmapping);
				 map.addView(a);
			 }
			 
			 app.sendMappingMsg(app.getNode().getLocalNodeHandle(), map);

	     }		   
	  }
	 
	 
	  public void readInFiles() throws IOException, ClassNotFoundException{
		  FileInputStream fis = new FileInputStream("log/Query/finalResult.log");
		  ObjectInputStream ois = new ObjectInputStream(fis);
				
		  int size = ois.readInt();
		  for (int i = 0; i < size; i++){ //first print glavMappings
			  GLAVMapping glavmap = (GLAVMapping)ois.readObject();
			  glavsIn.addElement(glavmap);
//			  System.out.println(glavmap.printString());
		  }
			
		  m_schema = (Schema) ois.readObject();
//		  System.out.println(s.printString());
			
		  size = ois.readInt();
		  for (int i = 0; i < size; i++){
			  MappingTable mtset = (MappingTable)ois.readObject();
			  mtsIn.addElement(mtset);
//			  mtset.printMappingTable();
		  }
			
		  size = ois.readInt();
		  for(int i = 0; i < size; i++){
			  String mappingInfo = (String)ois.readObject();
			  mappingInfosIn.addElement(mappingInfo);
//			  System.out.println(mappingInfo);
		  }
			
		  ois.close();
		  
		  
		  
		  FileInputStream fis_schemas = new FileInputStream("pdms/schemas.log");
	      ObjectInputStream ois_schemas = new ObjectInputStream(fis_schemas);
	      
	      size = ois_schemas.readInt();
	      for (int i = 0; i < size; i++){
	    	  Schema lschema = (Schema)ois_schemas.readObject();
	    	  schemasIn.addElement(lschema);
	      }
			
	      ois_schemas.close();
			
			
	      FileInputStream fis_topology = new FileInputStream("pdms/topology.log");
	      ObjectInputStream ois_topology = new ObjectInputStream(fis_topology);

	      size = ois_topology.readInt();
	      for (int i = 0; i < size; i++){
	    	  Vector acq = (Vector)ois_topology.readObject();
	    	  tpIn.addElement(acq);
	      }
			
	      ois_topology.close();
		  		  
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
	    NewSchemaJoinInPdms driver = new NewSchemaJoinInPdms(bindport, bootaddress, 
	    		seq, env, totalpeernum);


	  } 


}

package pdms;


import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;



import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNodeFactory;
import rice.pastry.socket.SocketPastryNodeFactory;


public class CheckTopologyTimeApp implements Application {

	  /**
	   * The Endpoint represents the underlieing node. By making calls on the
	   * Endpoint, it assures that the message will be delivered to a MyApp on
	   * whichever node the message is intended for.
	   */
	  protected Endpoint endpoint;
	  /**
	   * The node we were constructed on.
	   */
	  protected Node node;

	  private String peer_name;
	  
	  private Vector msgQueue;
	  
	  private int numInMsgQueue = 0;
	  
	  private Vector acquaintances;
	  
	  private Vector Info; 
	  
	  private Vector Info_inComp;
	  
	  private int m_schema_created = 0;

	  private int inComputing = 0;
	  
	  private int needToSendMsg = 0; //1 for yes; 0 for no
	  
	  public Hashtable nodeHash;
	  
	  public PastryNodeFactory factory;
	  
	  public Vector acq_handle;
	  
	  protected HashSet infohash;
	  
	  protected long localStartTime = 0;
	  
	  protected long localEndTime = 0;
	  
	  protected long procStartT = 0;
	  
	  protected long procEndT = 0;
	  
	  protected long localProcT = 0;
	  
	  protected long msgTime = 0;
	  
	  protected int receiveMsgNum = 0;
	  
	  protected int sendMsgNum = 0;
	  
	  
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
	  
	  public void setPeerName(String peername){
		  this.peer_name = peername;
	  }
	  /**
	   * Constructor
	   *
	   * @param pn DESCRIBE THE PARAMETER
	   */
	  public CheckTopologyTimeApp(Node node, PastryNodeFactory factory) {		  
		    this.endpoint = node.registerApplication(this, "MyPdmsApp");
		    this.node = node;
		    this.factory = factory;
		   	peer_name = "";
			acquaintances = new Vector();
			Info = new Vector();
			Info_inComp = new Vector();
			msgQueue = new Vector();
			nodeHash = new Hashtable();
			acq_handle = new Vector();
			infohash = new HashSet();
			buildNodeHash();
	  }
	  
	  public void setAcq_handle(){
		  for (int i = 0; i < acquaintances.size(); i++){
			  Integer seq = (Integer)acquaintances.elementAt(i);
			  NodeHandle nh = getNodeHandle(seq, factory);
			  System.out.println("nh: " + nh);
			  if (nh == null)
				  System.out.println("Cannot get nh");
			  acq_handle.addElement(nh);
			  System.out.println("nodehandle added " + seq);
		  }
		    
	  }
		
	  
	  
	  
	  /**
	   * Getter for the node.
	   *
	   * @return The Node value
	   */
	  public Node getNode() {
		  return node;
	  }
	  
	  /**
	   * Set Schema Name
	   */

	  
	  public String getPeerName(){
		  return peer_name;
	  }
	  
	  
	  public void setAcquaintance(int tgtSeq){
		  acquaintances.addElement(new Integer(tgtSeq));
	  }
	  
	  public Vector getAcquaintances(){
		  return acquaintances;
	  }
	  

	  /**
	   * Called to directly send a message to the nh
	   *
	   * @param nh DESCRIBE THE PARAMETER
	   */
	  public void routeMyMsgDirect(NodeHandle nh) {
	    
	    System.out.println("Sending broadcasting message from " + 
				  this + " " + this.peer_name +
				  " to the dest " + nh);
		  PdmsMsg msg = new PdmsMsg(endpoint.getId(), nh.getId(), 
				  this.peer_name, "");
		  msg.setMappingInfo(Info);
		  endpoint.route(null, msg, nh);
	    
	  }
	  
	  
  
	  
	  public Vector combineInfo(Vector Info1, 
			  Vector Info2){
		  //if mappingInfo1 contains mappingInfo2, return 1
		  //if mappingInfo2 contains mappingInfo1, return -1
		  //else return 0, which means needs combination
		  
		  Vector InfoComb = new Vector();
		  
		 
		  for (int i = 0; i < Info1.size(); i++){
			  InfoComb.addElement(Info1.elementAt(i));
		  }
		  
		  infohash = new HashSet(InfoComb);
		  for (int i = 0; i < Info2.size(); i++){
			  if (!this.containsInfo(InfoComb, (String)Info2.elementAt(i))){
				  InfoComb.addElement(Info2.elementAt(i));
			  }
		  }

		  return InfoComb;

	  }
	  
	  public boolean containsInfo(Vector mappinginfo, String edge){
			int index = edge.indexOf(" ");
			String schema1 = edge.substring(0, index);
			String schema2 = edge.substring(index + 1);
			if (infohash.contains(schema1 + " " + schema2) ||
					infohash.contains(schema2 + " " + schema1)){		  
				return true;
		    }
			else
				return false;
		}
	  

	  
	  /**
	   * Called when we receive a message.
	   *
	   * @param id DESCRIBE THE PARAMETER
	   * @param message DESCRIBE THE PARAMETER
	   */
	  public void deliver(Id id, Message msg) {
		  System.out.println(System.nanoTime());
		  this.receiveMsgNum++;
//		  System.out.println("message received");
		  String msgType = msg.getClass().getName();
		  if (msgType.equals("Message")){
		  }
		  else if (msgType.equals("pdms.PdmsMsg")){
			  
			  if (localStartTime == 0){
				  localStartTime = System.nanoTime();
			  }
			  
			 			  
			  PdmsMsg msgtmp = (PdmsMsg)msg;
			  
			  Vector Info_tmp = msgtmp.getMappingInfo();
			  
			  System.out.println("Received PdmsMsg " + msgtmp + " at " + 
					  "NodeID: " + this + this.peer_name);
			  
			  /*
			  String fnStart = "log2/timer/lc" + this.peer_name + ".log";
			  			  
			  FileWriter fw1 = null;
              try {
                     fw1 = new FileWriter(fnStart, true); 
    //                 fw1.write(" start " + System.currentTimeMillis()); 
                     fw1.write(" start " + System.nanoTime()); 
                     fw1.close(); 
              }
              catch (IOException ex) {  }
*/		
			  
			  
//			  System.out.println("inComputing = " + inComputing);
			  
			  if (m_schema_created == 0){
				  
				  procStartT = System.nanoTime();
				  
				  if (msgtmp.peerName.equals(this.peer_name)){
					 //note down when the first peer starts computation
					  
					  String fileName = "log2/timer/systemstart.log";
					  
					  FileWriter fw = null;
		              try {
		                     fw = new FileWriter(fileName, true); 
		  //                   fw.write(System.currentTimeMillis() + ""); 
		                     
		                     fw.write(System.nanoTime() + ""); 
		                     fw.close(); 
		              }
		              catch (IOException ex) {  }

				  }
				  
				  //first message to trigger
				  //local schema mediation using 
				  //its mappings with acquaintances
		          				  
				  inComputing = 1;
				  
				  System.out.println("In computing local mediated schema " + 
						  this.peer_name);
				  
				  for (int i = 0; i < this.acquaintances.size(); i++){
					  int acqInt = ((Integer)this.acquaintances.elementAt(i)).intValue();
					  String acqStr = this.schemaNames[acqInt]; 
					  String edge = acqStr + " " + this.peer_name;
					  Info_inComp.addElement(edge);
				  }
				  
			  
				  m_schema_created = 1;
				  inComputing = 0;
				  
				  procEndT = System.nanoTime();
				  
				  this.localProcT += procEndT - procStartT;
				  
				  if (msgtmp.peerName.equals(this.peer_name)){
					 					 
              
		              if (inComputing == 0){
						  //update the mediated schema and according info 
						  //with the new computed info set
						  Info = Info_inComp;
						  
						  /*						  
						  fnStart = "log2/timer/lc" + this.peer_name + ".log";
						  
						  fw1 = null;
			              try {
			                     fw1 = new FileWriter(fnStart, true); 
			   //                  fw1.write(" end " + System.currentTimeMillis()); 
			                     fw1.write(" end " + System.nanoTime()); 
			                     fw1.close(); 
			              }
			              catch (IOException ex) {  }
*/			              
			              
						  
						  System.out.println("sending msg to " + acquaintances.size() + " nodes");
						  for (int i = 0; i < acquaintances.size(); i++){
							  long msgStart = System.nanoTime();
							  System.out.println(msgStart);

/*							  
							  String fnMsg = "log2/timer/Msg" + this.peer_name + ".log";
							  FileWriter fw = null;
							  try{
				            	  fw = new FileWriter(fnMsg, true); 
				  //          	  fw.write(" Start " + System.currentTimeMillis());
				            	  fw.write(" Start " + System.nanoTime());
				            	  fw.close(); 
				              }
				              catch (IOException ex) {  }
*/
							  
							  Integer nodeseq = ((Integer)acquaintances.elementAt(i));
							  
				//			  NodeHandle nodeHandle = getNodeHandle(nodeseq, factory);
					
							  NodeHandle nodeHandle = (NodeHandle)acq_handle.elementAt(i);
							  if (nodeseq.intValue() == 0)
								  continue;
							  
							  while (nodeHandle == null){
								  System.out.print("node not ready!");								  
								  try {
										Thread.sleep(100);
								  } catch (Exception e) {
									 
								  }
								  nodeHandle = getNodeHandle(nodeseq, factory);
								  
							  }
							  
							
							  System.out.println("Sending mediated schema to " + 
									  nodeHandle);
								
							  
							  this.routeMyMsgDirect(nodeHandle);
							  this.sendMsgNum++;
							  
							  long msgEnd = System.nanoTime();
							  this.msgTime += msgEnd - msgStart;
/*							  
							  fnMsg = "log2/timer/Msg" + this.peer_name + ".log";
							  fw = null;
							  try{
				            	  fw = new FileWriter(fnMsg, true); 
				 //           	   fw.write(" Start " + System.currentTimeMillis());
				            	  fw.write(" Start " + System.nanoTime());
				            	  fw.close(); 
				              }
				              catch (IOException ex) {  }
*/
						  }
						  
						  
					  }
		              
		              
		              System.out.println("Info: ");
					  for (int i = 0; i < Info.size(); i++){
						  System.out.println((String)Info.elementAt(i));
					  }
					  
					  localEndTime = System.nanoTime();

				  }		  
			  }//end for if		  
			  
			  if (!msgtmp.peerName.equals(this.peer_name)){
				  msgQueue.add(numInMsgQueue++, msg); //enqueue
//				  System.out.println("num in " + this.getPeerName() + 
//						  " msg queue:" + numInMsgQueue);
					  
				  		 
				  while (inComputing == 1);
				  
				  procStartT = System.nanoTime();
				  			  
				  if (inComputing == 0) {
					  inComputing = 1;

					  PdmsMsg pdmsmsg = (PdmsMsg)msgQueue.elementAt(--numInMsgQueue);
					
					  Info_tmp = pdmsmsg.getMappingInfo();
					  
					  
										  
					  if (compareInfo(Info_tmp, Info_inComp) == -1 &&
							  compareInfo(Info_inComp, Info_tmp) == -1){
						  //the mediated schema contained in the new message 
						  //is the same as the mediated schema residing on this peer
						  //no need to send out msg
						  Info = Info_inComp;
//						  System.out.println("needToSendMsg: "+ needToSendMsg);
						  					  
					  }
					  				  
					  else if (compareInfo(Info_tmp, Info_inComp) == 1){
						  
//						  System.out.println("compare mappinginfo = 1");
						  
//						  this.printMappingInfo(mappingInfo_tmp);
//						  this.printMappingInfo(mappingInfo_inComp);
						  
						  
						  //mappingInfo_tmp contain mappingInfo_inComp
						  Info_inComp = Info_tmp;			
						  needToSendMsg = 1;
					  }
					  /*
					  else if (compareInfo(Info_tmp, Info_inComp) == -1){
//						  System.out.println("compare mappinginfo = -1");
						  
//						  this.printMappingInfo(mappingInfo_tmp);
//						  this.printMappingInfo(mappingInfo_inComp);
						  
						  needToSendMsg = 1;
						  
					  }
					  */
					  else if (compareInfo(Info_tmp, Info_inComp) == 0){
//						  System.out.println("compare mappinginfo = 0");
						  
//						  System.out.println("merge two mediated schema...");
						  
//						  System.out.println("I'm out!!!");
						  
//						  this.printMappingInfo(mappingInfo_tmp);
//						  this.printMappingInfo(mappingInfo_inComp);
						  
						  Info_inComp = combineInfo(Info_inComp, Info_tmp);
						  
//						  this.printMappingInfo(mappingInfo_inComp);
						  //combine mappinginfo
				          //merge M_inComp and M_tmp from pdmsmsg
						  
						  needToSendMsg = 1;
					  }  
					  //caculate mediated schema (new function)
					  //dequeue from msgQueue
					  inComputing = 0;
					  /*					  
					  String fileName = "log2/timer/" + this.peer_name + ".log";
				//	  String fn2 = "log/timer/lc" + this.peer_name + ".log";

					  
					  FileWriter fw = null;
		              try {
		            	  fw = new FileWriter(fileName, true); 
	//	            	  fw.write(" " + System.currentTimeMillis());
		            	  fw.write(" " + System.nanoTime());
		            	  fw.close(); 
		              }
		              catch (IOException ex) {  }
*/		              	              
		              
		              /*
		              String fn2 = "log2/timer/lc" + this.peer_name + ".log";
		              try{
		            	  fw = new FileWriter(fn2, true); 
	//	            	  fw.write(" End " + System.currentTimeMillis());
		            	  fw.write(" End " + System.nanoTime());
		            	  fw.close(); 
		              }
		              catch (IOException ex) {  }
*/		
				  }
				  
				  procEndT = System.nanoTime();
				  
				  this.localProcT += procEndT - procStartT;
				  
				  localEndTime = System.nanoTime();
				  
				  
				  if (inComputing == 0 && numInMsgQueue == 0 &&
						  needToSendMsg == 1){
					  //update the mediated schema and according info 
					  //with the new computed info set
					  					  
					  
					  System.out.println("Ready to send message!!!!");
					
				  	  Info = Info_inComp;
					  
					  
					  System.out.println("sending out message to " + acquaintances.size() + " nodes ");
					  
					  
					  for (int i = 0; i < acquaintances.size(); i++){
//					      System.out.println("in for loop!!!");  
//						  Integer nodeseq = ((Integer)acquaintances.elementAt(i));
//						  System.out.println("nodeseq = " + nodeseq);
						  						  
				//		  NodeHandle nodeHandle = getNodeHandle(nodeseq, factory);
/*
						  String fnMsg = "log2/timer/Msg" + this.peer_name + ".log";
						  FileWriter fw = null;
						  try{
			            	  fw = new FileWriter(fnMsg, true); 
//			            	  fw.write(" Start " + System.currentTimeMillis());
			            	  fw.write(" Start " + System.nanoTime());
			            	  fw.close(); 
			              }
			              catch (IOException ex) {  }
*/						  long msgStart = System.nanoTime();

                          System.out.println(msgStart);

												  
						  NodeHandle nodeHandle = (NodeHandle)acq_handle.elementAt(i);
						  System.out.println(this.peer_name + "'s aquaintance is node" +
								  acquaintances.elementAt(i).toString());
						  
						  System.out.println("Sending out mediated schema msg from " + 
								  this.getPeerName() + " to node" + 
								  acquaintances.elementAt(i).toString());
						  
						  while (nodeHandle == null){
							  System.out.print("node" + 
									  acquaintances.elementAt(i).toString() + "leaving!");								  
							  try {
									Thread.sleep(100);
							  } catch (Exception e) {
								 
							  }
		//					  nodeHandle = getNodeHandle(nodeseq, factory);
							  nodeHandle = (NodeHandle)acq_handle.elementAt(i);
						  }
						  
						  this.routeMyMsgDirect(nodeHandle);
						  this.sendMsgNum++;
						  
						  
						  long msgEnd = System.nanoTime();
						  this.msgTime += msgEnd - msgStart;
/*						  
						  fnMsg = "log2/timer/Msg" + this.peer_name + ".log";
						  fw = null;
						  try{
			            	  fw = new FileWriter(fnMsg, true); 
//			            	  fw.write(" End " + System.currentTimeMillis());
			            	  fw.write(" End " + System.nanoTime());
			            	  fw.close(); 
			              }
			              catch (IOException ex) {  }
*/
					  }
					  
					  
					  
					  
					  needToSendMsg = 0;
					  
			    	  
					  System.out.println("sending out info:");
					  for (int i = 0; i < this.Info.size(); i++ ){
						  
						  System.out.println((String)Info.elementAt(i));
						 
					  }
					  
					  localEndTime = System.nanoTime();
					  
					  System.out.println("just sent out msg");
					  
					  
					  
					  if (Info.size() == 18){
						  printAllTime();
						  
						  String endsysfile = "log2/timer/endSys" + this.peer_name + ".log";
						  FileWriter fw = null;
						  try{
			            	  fw = new FileWriter(endsysfile, true); 
			            	  fw.write("" + System.nanoTime());
			            	  fw.flush();
			            	  fw.close(); 
			              }
			              catch (IOException ex) {  }
						  
				  	  }
					  
				  }
				  				  			  
			  } //end for if
		  }
	  }
	  
	  
	  public void printAllTime(){
		  System.out.println("local start time " + this.localStartTime);
		  System.out.println("local computing up to now: " + this.localProcT);
		  System.out.println("total time for current node up to now: " + (this.localEndTime - this.localStartTime));
		  System.out.println("sending out msg using: " + this.msgTime);
		  System.out.println("received " + this.receiveMsgNum + " msg ");
		  System.out.println("sent out " + this.sendMsgNum + " msg ");		
	  }
	  
	  
	  public int compareInfo(Vector Info1, Vector Info2){
		  //if mappingInfo1 contains mappingInfo2, return 1
		  //if mappingInfo2 contains mappingInfo1, return -1
		  //else return 0, which means needs combination
		  HashSet infoset1 = new HashSet(Info1);
		  HashSet infoset2 = new HashSet(Info2);
		  int i;
		  for (i = 0; i < Info1.size(); i++){
			  String a = (String)Info1.elementAt(i);
			  String[] result = a.split("\\s");
			  String a1 = result[1] + " " + result[0];
			  if (!infoset2.contains(a) && !infoset2.contains(a1))
				  break;
		  }
		  if (i == Info1.size()){
			  return -1;
		  }
		  for (i = 0; i < Info2.size(); i++){
			  String a = (String)Info2.elementAt(i);
			  String[] result = a.split("\\s");
			  String a1 = result[1] + " " + result[0];
			  if (!infoset1.contains(a) && !infoset1.contains(a1))
				  break;
		  }
		  if (i == Info2.size()){
			  return 1;
		  }
		  
		  return 0;  
		  
	  }
		  
	  
	  
	  public NodeHandle getNodeHandle(Integer nodeSeq, PastryNodeFactory factory){
		  String nodeAddr = (String)nodeHash.get(nodeSeq);
		  int index = nodeAddr.indexOf(":");
		  
		  InetAddress bootaddr = null;
		  try {
			  bootaddr = InetAddress.getByName(nodeAddr.substring(0,index));
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  String port = nodeAddr.substring(index + 2);
	      int bootport = Integer.parseInt(port);
	      InetSocketAddress bootaddress = new InetSocketAddress(bootaddr, bootport);
	      NodeHandle nodeHandle = ((SocketPastryNodeFactory) factory).getNodeHandle(bootaddress);
		  return nodeHandle;
	  }
	  
		
		/**
		   * Called when you hear about a new neighbor. Don't worry about this method
		   * for now.
		   *
		   * @param handle DESCRIBE THE PARAMETER
		   * @param joined DESCRIBE THE PARAMETER
		   */		
		public void update(NodeHandle handle, boolean joined) {
		  }
		
		
		  		
		/**
		   * Called a message travels along your path. Don't worry about this method for
		   * now.
		   *
		   * @param message DESCRIBE THE PARAMETER
		   * @return DESCRIBE THE RETURN VALUE
		   */
		public boolean forward(RouteMessage message) {
		    return true;
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

	  
	}

  
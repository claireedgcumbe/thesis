package pdms;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import mediation.GLAVMapping;
import mediation.Mapping;
import mediation.MappingTable;
import mediation.Schema;
import minicon.IPValue;
import minicon.Predicate;
import minicon.View;
import semantics.MappingStatementUnion;


import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNodeFactory;
import rice.pastry.socket.SocketPastryNodeFactory;



public class PdmsApp implements Application {

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
	
	  private String msgid;
	  
	  private int nextid = 0;

	  private String peer_name;
	  
	  private Schema p_schema;
	   
	  protected Schema m_schema; //mediated schema in use
	  
	  private Schema m_schema_inComp; //in the computing module
	  
	  protected Vector mappingInfo; 
	  
	  private Vector mappingInfo_inComp;
	  
	  private Vector msgQueue;
	  
	  private int numInMsgQueue = 0;
	  
	  protected Vector acquaintances;
	  
	  protected Vector mappings; //mappings with its aquaintances
	  
	  protected Vector mtSet;
	  
	  private Vector mtSet_inComp;
	  
	  protected Vector m_glav;
	  
	  private Vector m_glav_inComp;
	  
	  private int inComputing = 0;
	  
	  private int m_schema_created = 0; 
	  //flag to indiate whether
	  //there's mediated schema created for 
	  //the local peer (using its acquaintances'
	  //mapping information
	  
	  private int needToSendMsg = 0; //1 for yes; 0 for no
	  
	  public Hashtable nodeHash;
	  
	  public PastryNodeFactory factory;
	  
	  public Vector acq_handle;
	  
	  public static int ipvNextNum = 0;
	  	  
	  protected HashSet infohash;
	  
	  protected long localStartTime = 0;
	  
	  protected long localEndTime = 0;
	  
	  protected long procStartT = 0;
	  
	  protected long procEndT = 0;
	  
	  protected long localProcT = 0;
	  
	  protected long msgTime = 0;
	  
	  protected int receiveMsgNum = 0;
	  
	  protected int sendMsgNum = 0;
	  
	  
	  /**
	   * Constructor
	   *
	   * @param pn DESCRIBE THE PARAMETER
	   */
	  public PdmsApp(Node node, PastryNodeFactory factory) {		  
		    this.endpoint = node.registerApplication(this, "MyPdmsApp");
		    this.node = node;
		    this.factory = factory;
		    msgid = "";
			peer_name = "";
			p_schema = new Schema();
			acquaintances = new Vector();
			mappings = new Vector();
			m_schema = new Schema();
			m_schema_inComp = new Schema();
			m_schema.setName("M");
			m_schema_inComp.setName("M");
			mtSet = new Vector();
			mtSet_inComp = new Vector();
			m_glav = new Vector();
			m_glav_inComp = new Vector();
			mappingInfo = new Vector();
			mappingInfo_inComp = new Vector();
			infohash = new HashSet();
			msgQueue = new Vector();
			nodeHash = new Hashtable();
			acq_handle = new Vector();
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

	  public String getMsgId(){
		  return p_schema.getName() + (nextid++);  
	  }
	  
	  public String getPeerName(){
		  return peer_name;
	  }
	  
	  public void setPeerSchema(Schema pschema){
		  p_schema = pschema;
		  peer_name = pschema.getName();  
		  //set the peer name same as the local schema name
	  }
	 
	  public Schema getPeerSchema(){
		  return p_schema;
	  }

	  
	  /**
	   * Get Schema Name
	   */
	  public String getSchemaName(){
		  return p_schema.getName();
	  }
	  
	  public Schema getMediatedSchema(){
		  return m_schema;
	  }
	  
	  public Vector getGLAVMapping(){
		  return m_glav;
	  }
	  
	  public void setAcquaintance(int tgtSeq){
		  acquaintances.addElement(new Integer(tgtSeq));
	  }
	  
	  public Vector getAcquaintances(){
		  return acquaintances;
	  }
	  
	  /*
	  public PdmsApp getAcquaintance(String p_name){ //p_name: acquaintance's schema name
		  for (int i = 0; i < acquaintances.size(); i++){
			  if (((PdmsApp)acquaintances.get(i)).getSchemaName().equals(p_name)){
				  return (PdmsApp)acquaintances.get(i);
			  }
		  }
	      return null;
	   }
	  */
	  
	  public void setMapping(Mapping mapping){
		  mappings.addElement(mapping);
	  }
	  
	  public Vector getMappings(){
		  return mappings;
	  }

	  /*
	  public Mapping getMapping(String p_name){
		  for (int i = 0; i < acquaintances.size(); i++){
			  Mapping acqMap = (Mapping)acquaintances.get(i);
			  for (int j = 0; j < acqMap.getNumberOfSchemas(); j++){
				  if (acqMap.getSchemaInfo(j).getName().equals(p_name)){
					  return acqMap;
				  }
			  }
		  }
		  return null;
	  }
	  */
	  
	  
	  /**
	   * Called to route a message to the id
	   *
	   * @param id DESCRIBE THE PARAMETER
	   */
	  public void routeMyMsg(Id tgtId) {		  
		  System.out.println("Sending message from " + 
				  this + this.p_schema.getName() + 
				  " to the dest " + tgtId);
		  msgid = getMsgId();


		  Message msg = new PdmsMsg(endpoint.getId(), tgtId, 
				  this.peer_name, msgid);
		  endpoint.route(tgtId, msg, null);
	  }

	  /**
	   * Called to directly send a message to the nh
	   *
	   * @param nh DESCRIBE THE PARAMETER
	   */
	  public void routeMyMsgDirect(NodeHandle nh) {
	    System.out.println(this + this.p_schema.getName() + 
	    		" sending direct to " + nh);
	    msgid = getMsgId();
	    Message msg = new PdmsMsg(endpoint.getId(), nh.getId(), 
	    		this.peer_name, msgid);
	    endpoint.route(null, msg, nh);
	  }
	  

	  /**
	   * send the message to other peers;
	   * the message includes all mediated schema information:
	   * 1) mediated schema
	   * 2) mappingTables
	   * 3) glavMappings
	   * 4) mappingInfo
	   * 
	   * @param tgdApp
	   */
	  public void sendBroadCastingMsg(NodeHandle nh) {  
		  System.out.println("Sending broadcasting message from " + 
				  this + " " + this.p_schema.getName() +
				  " to the dest " + nh);
		  msgid = getMsgId();
		  PdmsMsg msg = new PdmsMsg(endpoint.getId(), nh.getId(), 
				  this.peer_name, msgid);
		  msg.setMediatedSchema(m_schema);
		  msg.setMTSet(mtSet);
		  msg.setGlavMappings(m_glav);
		  msg.setMappingInfo(mappingInfo);
		  endpoint.route(null, msg, nh);
	  }
	  
	  
	  /**
	   * when this is the new peer schema, it needs to 
	   * send a PdmsJoinMsg: local peer schema and mapping to the target
	   * to the target node 
	   * 
	   * won't be used for now
	   * 
	   * @param tgdApp
	   */
	  /*
	  public void sendJoinMsg(NodeHandle nh, Mapping m) {  
		  System.out.println("Sending join message from " + 
				  this + this.p_schema.getName() +
				  " to the dest " + nh);
		  msgid = getMsgId();
		  PdmsJoinMsg msg = new PdmsJoinMsg(endpoint.getId(), nh.getId(), 
				  msgid);
		  msg.setPeerSchema(p_schema);
		  msg.setMapping(m);
		  endpoint.route(null, msg, nh);
	  }
	  */
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
//		  System.out.println("msgType: " + msgType);
		  if (msgType.equals("Message")){
		      System.out.println("Received " + msg + " at " + 
		    		  "NodeID: " + this +
		    	      " nodeName: " + this.getPeerName());  
		  }
		  else if (msgType.equals("pdms.PdmsMsg")){
			  
			  if (localStartTime == 0){
				  localStartTime = System.nanoTime();
			  }
			  
			  PdmsMsg msgtmp = (PdmsMsg)msg;
			  
			  System.out.println("Received PdmsMsg " + msgtmp + " at " + 
					  "NodeID: " + this + this.p_schema.getName());
			  
			  /*
			  String fnStart = "log/timer/lc" + this.peer_name + ".log";
			  
			  FileWriter fw1 = null;
              try {
                     fw1 = new FileWriter(fnStart, true); 
                     fw1.write(" start " + System.nanoTime()); 
                     fw1.close(); 
              }
              catch (IOException ex) {  }
		
			  */
			  
//			  System.out.println("inComputing = " + inComputing);
			  
			  if (m_schema_created == 0){
				  
				  procStartT = System.nanoTime();
				  
				  if (msgtmp.peerName.equals(this.getSchemaName())){
					  //note down when the first peer starts computation
					  String fileName = "log/timer/systemstart.log";
					  
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
						  this.p_schema.getName() + "...");
				  System.out.println();
				  for (int i = 0; i < mappings.size(); i++){
					  Mapping mapping = (Mapping)mappings.elementAt(i);
					  				  
					  mappingInfo_inComp.addElement(
							  mapping.getSchemaInfo(0).getName()
							  + " " + 
							  mapping.getSchemaInfo(1).getName());

				      initializeGLAV(mapping);
					  schemaMerge(mapping);
					  
				  }
	  
				  m_schema_created = 1;
				  inComputing = 0;  

				  procEndT = System.nanoTime();
				  
				  this.localProcT += procEndT - procStartT;
				  
				  if (msgtmp.peerName.equals(this.getSchemaName())){
					  //this is the first peer to start the mediation
		//			  System.out.println("This is the startup peer...");
					 
              
		              if (inComputing == 0){
						  //update the mediated schema and according info 
						  //with the new computed info set
						  m_schema = m_schema_inComp;
						  mtSet = mtSet_inComp;
						  m_glav = m_glav_inComp;
						  mappingInfo = mappingInfo_inComp;
						  
						  /*
						  fnStart = "log/timer/lc" + this.peer_name + ".log";
						  
						  fw1 = null;
			              try {
			                     fw1 = new FileWriter(fnStart, true); 
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
							  ///////start note down the msg sending time
							  String fnMsg = "log/timer/Msg" + this.peer_name + ".log";
							  FileWriter fw = null;
							  try{
				            	  fw = new FileWriter(fnMsg, true); 
				            	  fw.write(" Start " + System.nanoTime());
				            	  fw.close(); 
				              }
				              catch (IOException ex) {  }
				              */
				              //////////
							  Integer nodeseq = ((Integer)acquaintances.elementAt(i));
							  System.out.println(this.getSchemaName() + "'s aquaintance is node" +
									  acquaintances.elementAt(i).toString());
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
								
							  
							  sendBroadCastingMsg(nodeHandle);
							  
							  this.sendMsgNum++;
							  
							  long msgEnd = System.nanoTime();
							  this.msgTime += msgEnd - msgStart;
/*							  
							  /////writing down the msg end time
							  fnMsg = "log/timer/Msg" + this.peer_name + ".log";
							  fw = null;
							  try{
				            	  fw = new FileWriter(fnMsg, true); 
				            	  fw.write(" End " + System.nanoTime());
				            	  fw.close(); 
				              }
				              catch (IOException ex) {  }
*/				              ///////// 
				
						  }
						  
						  
					  }
		              
		              System.out.println("=================================");
					  System.out.println("The mediated schema just sent is:");
			    	  System.out.println(m_schema.printString());
			    	  System.out.println("Mappings covered by this mediated schema:");
			    	  printMappingInfo(mappingInfo);
			    	  System.out.println("=================================");
			    	  for (int i = 0; i < mtSet.size(); i++){
			    		  ((MappingTable)mtSet.elementAt(i)).printMappingTable();
			    	  }
			    	  
			    	  System.out.println("=================================");
			    	  System.out.println("Local GLAV mapping is: ");
					  for (int i = 0; i < this.getGLAVMapping().size(); i++ ){
						  
						  System.out.println("GLAV for " + ((GLAVMapping)this.getGLAVMapping().elementAt(i)).getSchema());
						  System.out.println(((GLAVMapping)this.getGLAVMapping().elementAt(i)).printString());
					  }
					  System.out.println("=================================");
					  localEndTime = System.nanoTime();

				  }		  
			  }//end for if		  
			  
			  if (!msgtmp.peerName.equals(this.getSchemaName())){
				  msgQueue.add(numInMsgQueue++, msg); //enqueue
//				  System.out.println("num in " + this.getPeerName() + 
//						  " msg queue:" + numInMsgQueue);
					  
				  		 
				  while (inComputing == 1);
				  procStartT = System.nanoTime();
				  			  
				  if (inComputing == 0) {
					  inComputing = 1;

					  PdmsMsg pdmsmsg = (PdmsMsg)msgQueue.elementAt(--numInMsgQueue);
					  Vector mappingInfo_tmp = pdmsmsg.getMappingInfo();
					  Vector mtSet_tmp = pdmsmsg.getMTSet();
					  Schema m_schema_tmp = pdmsmsg.getMediatedSchema();
					  Vector m_glav_tmp = pdmsmsg.getGlavMappings();
					  
					  
//					  System.out.println("msg handling and removed from " + 
//							  this.getPeerName() + " queue");
					  
					  
//					  this.printMappingInfo(mappingInfo_tmp);
					  
//					  this.printMappingInfo(mappingInfo_inComp);
					  
					  if (compareMappingInfo(mappingInfo_tmp, mappingInfo_inComp) == -1 &&
							  compareMappingInfo(mappingInfo_inComp, mappingInfo_tmp) == -1){
						  //the mediated schema contained in the new message 
						  //is the same as the mediated schema residing on this peer
						  //no need to send out msg
						  m_schema = m_schema_inComp;
						  mtSet = mtSet_inComp;
						  m_glav = m_glav_inComp;
						  mappingInfo = mappingInfo_inComp;
//						  System.out.println("needToSendMsg: "+ needToSendMsg);
						  					  
					  }
					  				  
					  else if (compareMappingInfo(mappingInfo_tmp, mappingInfo_inComp) == 1){
						  
//						  System.out.println("compare mappinginfo = 1");
						  
//						  this.printMappingInfo(mappingInfo_tmp);
//						  this.printMappingInfo(mappingInfo_inComp);
						  
						  
						  //mappingInfo_tmp contain mappingInfo_inComp
						  m_schema_inComp = m_schema_tmp;
						  mtSet_inComp = mtSet_tmp;
						  m_glav_inComp = m_glav_tmp;
						  mappingInfo_inComp = mappingInfo_tmp;			
						  needToSendMsg = 1;
					  }
		/*			  
					  else if (compareMappingInfo(mappingInfo_tmp, mappingInfo_inComp) == -1){
//						  System.out.println("compare mappinginfo = -1");
						  
//						  this.printMappingInfo(mappingInfo_tmp);
//						  this.printMappingInfo(mappingInfo_inComp);
						  
						  needToSendMsg = 1;
						  
					  }
			*/		  
					  else if (compareMappingInfo(mappingInfo_tmp, mappingInfo_inComp) == 0){
//						  System.out.println("compare mappinginfo = 0");
						  
						  System.out.println("merging two mediated schema...");
						  System.out.println();
						  criticalAreaForMediation(pdmsmsg);
						  
//						  System.out.println("I'm out!!!");
						  
//						  this.printMappingInfo(mappingInfo_tmp);
//						  this.printMappingInfo(mappingInfo_inComp);
						  
						  combineMappingInfo(mappingInfo_inComp, mappingInfo_tmp);
						  
//						  this.printMappingInfo(mappingInfo_inComp);
						  //combine mappinginfo
				          //merge M_inComp and M_tmp from pdmsmsg
						  
						  needToSendMsg = 1;
					  }  
					  //caculate mediated schema (new function)
					  //dequeue from msgQueue
					  inComputing = 0;
					  /*
					  String fileName = "log/timer/" + this.peer_name + ".log";
				//	  String fn2 = "log/timer/lc" + this.peer_name + ".log";
	  
					  FileWriter fw = null;
		              try {
		            	  fw = new FileWriter(fileName, true); 
		            	  fw.write(" " + System.nanoTime());
		            	  fw.close(); 
		              }
		              catch (IOException ex) {  }
		              
		              String fn2 = "log/timer/lc" + this.peer_name + ".log";
		              try{
		            	  fw = new FileWriter(fn2, true); 
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
					  m_schema = m_schema_inComp;
					  mtSet = mtSet_inComp;
					  m_glav = m_glav_inComp;
					  mappingInfo = mappingInfo_inComp;
					  
					 			  
					  
					  System.out.println("sending out message to " + acquaintances.size() + " nodes ");
							  
					  for (int i = 0; i < acquaintances.size(); i++){
//					      System.out.println("in for loop!!!");  
//						  Integer nodeseq = ((Integer)acquaintances.elementAt(i));
//						  System.out.println("nodeseq = " + nodeseq);
						  						  
				//		  NodeHandle nodeHandle = getNodeHandle(nodeseq, factory);
				/*		  
						  //////////write down the msg start sending time
						  String fnMsg = "log/timer/Msg" + this.peer_name + ".log";
						  FileWriter fw = null;
						  try{
			            	  fw = new FileWriter(fnMsg, true); 
			            	  fw.write(" Start " + System.nanoTime());
			            	  fw.close(); 
			              }
			              catch (IOException ex) {  }
			              ///////////////
					*/
						  
						  long msgStart = System.nanoTime();
						  System.out.println(msgStart);
						  NodeHandle nodeHandle = (NodeHandle)acq_handle.elementAt(i);
						  System.out.println(this.getSchemaName() + "'s aquaintance is node" +
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
						  
						  sendBroadCastingMsg(nodeHandle);
						  
						  this.sendMsgNum++;
						  		  
						  long msgEnd = System.nanoTime();
						  this.msgTime += msgEnd - msgStart;
/*						  
						  ////////write down the msg end writing time
						  fnMsg = "log/timer/Msg" + this.peer_name + ".log";
						  fw = null;
						  try{
			            	  fw = new FileWriter(fnMsg, true); 
			            	  fw.write(" End " + System.nanoTime());
			            	  fw.close(); 
			              }
			              catch (IOException ex) {  }
			              /////////
*/			              
					  }
					  
					  
					  needToSendMsg = 0;
					  
					  System.out.println("=================================");
					  System.out.println("The mediated schema just sent is:");
			    	  System.out.println(m_schema.printString());
			    	  System.out.println("Mappings covered by this mediated schema:");
			    	  printMappingInfo(mappingInfo);
			    	  System.out.println("=================================");
				    
			    	  for (int i = 0; i < mtSet.size(); i++){
			    		  ((MappingTable)mtSet.elementAt(i)).printMappingTable();
			    	  }
			    	  System.out.println("=================================");
			    	  System.out.println("Local GLAV mapping is: ");
			    	  for (int i = 0; i < this.getGLAVMapping().size(); i++ ){		  
						  System.out.println("GLAV for " + ((GLAVMapping)this.getGLAVMapping().elementAt(i)).getSchema());
						  System.out.println(((GLAVMapping)this.getGLAVMapping().elementAt(i)).printString());
					  }
					  System.out.println("=================================");   	  
					  
					  localEndTime = System.nanoTime();
					  					  
//					  System.out.println("just sent out msg");
					  
				  

					  if (mappingInfo.size() == 19){
						  printAllTime();
						  
						  String endsysfile = "log/timer/endSys" + this.peer_name + ".log";
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
		  }//end for pdms
	  }
	  
	  
	  public void printAllTime(){
		  System.out.println("local start time " + this.localStartTime);
		  System.out.println("local computing up to now: " + this.localProcT);
		  System.out.println("total time for current node up to now: " + (this.localEndTime - this.localStartTime));
		  System.out.println("sending out msg using: " + this.msgTime);
		  System.out.println("received " + this.receiveMsgNum + " msg ");
		  System.out.println("sent out " + this.sendMsgNum + " msg ");		
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
	  
	  
	  public void criticalAreaForMediation(PdmsMsg pdmsmsg){
		  System.out.println("In critical area...");
		  	  
		  Vector mtSet_tmp = pdmsmsg.getMTSet();
		  Schema m_schema_tmp = pdmsmsg.getMediatedSchema();
		  Vector m_glav_tmp = pdmsmsg.getGlavMappings();
		  
		  for (int i = 0; i < m_schema_tmp.numRelations(); i++){ 
			  //is each relation in the temp schema 
			  //also in the mtSet_inComp?
			  Predicate m_rel_tmp = m_schema_tmp.relationI(i);
//			  System.out.println("relation handling: " + m_rel_tmp.printString());
			  int mtIndex = findMT(mtSet_inComp, 
					  m_rel_tmp.getFunctionHead());
			  int mtIndex1 = findMT(mtSet_tmp, 
					  m_rel_tmp.getFunctionHead());
			  
//			  System.out.println("mtIndex: "+mtIndex);
//			  System.out.println("mtIndex1: "+mtIndex1);
			  
			  
			  MappingTable current = (MappingTable)mtSet_tmp.get(mtIndex1);
			  if (mtIndex == -1){
				  //the mediated relation passed from the msg
				  //doesn't appear in the local mediated schema
				  //need to append it to the local mediated schema
				  m_schema_inComp.addRelation(m_rel_tmp);
				  mtSet_inComp.add(current);
				  
				  for (int j = 0; j < m_glav_tmp.size(); j++){
					  //add the glav mapping that exist in msg
					  //but doesn't contain in m_glav_inComp to
					  //m_glav_inComp
				  
					  GLAVMapping glavTmp = (GLAVMapping)m_glav_tmp.get(j);
					  
//					  System.out.println("printing glavTmp " + j);
//					  System.out.println("glavTmp" + j + ":" + glavTmp.printString());
					  
					  
					  int gvIndex = glavTmp.findExistingGVByConcept(m_rel_tmp);
//					  System.out.println("gvIndex: " + gvIndex);
					  
					  if (gvIndex < 0)
						  continue;
//					  System.out.println("hihihihihi");
					  int k;
					  for (k = 0; k < m_glav_inComp.size(); k++){
						  GLAVMapping glavinComp = (GLAVMapping)m_glav_inComp.get(k);
						  
//						  System.out.println("glavinComp.getSchema():" + glavinComp.getSchema());
//						  System.out.println("glavTmp.getSchema():" + glavTmp.getSchema());
						  
//						  System.out.println("glavinComp:" + glavinComp.printString());
						  
						  if (glavinComp.getSchema().equals(glavTmp.getSchema())){
							  glavinComp.addGV(glavTmp.gvI(gvIndex));
							  glavinComp.addLV(glavTmp.lvI(gvIndex));
//							  System.out.println("glavinComp:" + glavinComp.printString());
							  break;
						  }
					  }
					  if (k == m_glav_inComp.size()){//new schema
						 m_glav_inComp.addElement(glavTmp);						  
					  }
				  }
			  }
			  else{//overlapped rel
				  
//				  System.out.println("handling overlapped rel...");			  
//				  System.out.println("current mappingtable: ");
//				  current.printMappingTable();
				  			  
				  
				  MappingTable oldMT = (MappingTable)mtSet_inComp.get(mtIndex);
				  
//				  System.out.println("oldMT mappingtable: ");
//				  oldMT.printMappingTable();
				  
				  MappingTable newMT = current.combineMappingTable(oldMT);
	//			  newMT.delOverlappedRow();
								  
//				  System.out.println("newMT mappingtable: ");
//				  newMT.printMappingTable();
				  
				  mtSet_inComp.remove(mtIndex);
				  mtSet_inComp.add(newMT);
				  
				  Predicate m_old_rel = m_schema_inComp.findRelation(m_rel_tmp.getFunctionHead());
				  Predicate m_new_rel = computeNewMediatedRel(m_old_rel, m_rel_tmp, newMT);
				  m_schema_inComp.delRelation(m_rel_tmp.getFunctionHead());
				  m_schema_inComp.addRelation(m_new_rel);
				  
//				  System.out.println("m_new_rel: " + m_new_rel.printString());
				  
				  //remove old GLAV for that concept first
				  removeGLAVMapping(m_glav_inComp, m_new_rel);
				  removeGLAVMapping(m_glav_tmp, m_new_rel);
				  computeGLAVMapping2(m_new_rel, newMT, m_glav_tmp);
//				  System.out.println("m_glav_inComp.size(): " + m_glav_inComp.size());
					  
					  				  
			  }
			  //update the mappinginfo
		  }	  
		  
		  
		  
		  
	  }
	  
	  public int compareMappingInfo(Vector mappingInfo1, 
			  Vector mappingInfo2){
		  //if mappingInfo1 contains mappingInfo2, return 1
		  //if mappingInfo2 contains mappingInfo1, return -1
		  //else return 0, which means needs combination
		  
		  int i;
		  for (i = 0; i < mappingInfo1.size(); i++){
			  String a = (String)mappingInfo1.elementAt(i);
			  String[] result = a.split("\\s");
	          String a1 = result[1] + " " + result[0];

			  if (!mappingInfo2.contains(a) && !mappingInfo2.contains(a1))
				  break;
		  }
		  if (i == mappingInfo1.size()){
			  return -1;
		  }
		  for (i = 0; i < mappingInfo2.size(); i++){
			  String a = (String)mappingInfo2.elementAt(i);
			  String[] result = a.split("\\s");
	          String a1 = result[1] + " " + result[0];
	          
			  if (!mappingInfo1.contains(a) && !mappingInfo1.contains(a1))
				  break;
		  }
		  if (i == mappingInfo2.size()){
			  return 1;
		  }
		  
		  return 0;

	  }
	  
	  public void combineMappingInfo(Vector mappinginfo1, 
			  Vector mappinginfo2){
		  //add mappingInfo2 to mappingInfo1
		  for (int i = 0; i < mappinginfo2.size(); i++){
			  String mapping = (String)mappinginfo2.elementAt(i);
			  if (!this.containsMapping(mappinginfo1, mapping)){
				  mappinginfo1.add(mapping);
			  }
		  }
		  
	  }
	  
	  
	  public void printMappingInfo(Vector mappinginfo){
		  System.out.println("MappingInfo: ");
		  for (int i = 0; i < mappinginfo.size(); i++){
			  System.out.println("\tMapping " + i + ": " +
					  mappinginfo.elementAt(i).toString());
		  }
		  System.out.println();
	  }
	  
	  public void schemaMerge(Mapping mapping){ //for more than 3
		  int mappingID = mapping.getMappingID();
		  
		  Schema schema1 = mapping.getSchemaInfo(0); //get schema
		  Schema schema2 = mapping.getSchemaInfo(1);
		   
		  int num_mapping_unions = mapping.numMappingUnions();

				
		  for (int k = 0; k < num_mapping_unions; k++){
			  Predicate m_rel = new Predicate(); //new mediated relation
			  MappingStatementUnion msu = mapping.MappingUnionI(k);
		    
			  int mtIndex = findMT(mtSet_inComp, msu.getHead());

			  MappingTable current;
			  if (mtIndex < 0) { //new concept
				  current = MergeSchema(m_rel, schema1, schema2, mappingID, msu);		
				  mtSet_inComp.add(current);

				  m_schema_inComp.addRelation(m_rel);
//				  System.out.println(m_rel.printString());
//				  current.printMappingTable();
				  computeGLAVMapping(m_rel, current);
			  }
					
			  else { //concept already exists 
					 /* add codes here: mapping compatible check*/
				  MappingTable oldMT =  (MappingTable)mtSet_inComp.get(mtIndex);
//				  System.out.println("in SchemaMerge: print oldMT:");
//				  oldMT.printMappingTable();
				  current = MergeSchema(m_rel, schema1, schema2, mappingID, msu);
//				  System.out.println("in SchemaMerge: print current:");
//				  current.printMappingTable();
				  MappingTable newMT = current.combineMappingTable(oldMT);
//				  System.out.println("in SchemaMerge: print newMT:");
//				  newMT.printMappingTable();

				  
				  mtSet_inComp.remove(mtIndex);
				  mtSet_inComp.add(newMT);
				  /* add codes here: get new mediated relation based on 
				   * the new MappingTable*/
				  Predicate m_old_rel = m_schema_inComp.findRelation(m_rel.getFunctionHead());
						
				  Predicate m_new_rel = computeNewMediatedRel(m_old_rel, m_rel, newMT);
				  m_schema_inComp.delRelation(m_rel.getFunctionHead());
				  m_schema_inComp.addRelation(m_new_rel);
						
				  //remove old GLAV for that concept first
				  removeGLAVMapping(m_glav_inComp, m_new_rel);
				  computeGLAVMapping(m_new_rel, newMT);
			  }			
		  }
	   }
	  
		public String getSchemaFromRel(String rel){
			int startI = 0;
			int endI = rel.indexOf(".");
			if (endI >= 0){
				return rel.substring(startI, endI);
			}
			return "";
		}
	  
	   public int findMT(Vector mtSet, String concept){
		   for (int i = 0; i < mtSet.size(); i++){
			   MappingTable a = (MappingTable)mtSet.get(i);
			   if (a.getName().equals(concept)){
				   return i;
			   }
		   }
		   return -1;
	   }
	   

		public MappingTable MergeSchema(Predicate m_rel, Schema schema1, Schema schema2, 
				int mappingID,  MappingStatementUnion msu){
			m_rel.setFunctionHead(msu.getHead());
			Vector attributes = msu.getVariables();
			int num_attrs = attributes.size();
			for (int j = 0; j < num_attrs; j++){
				m_rel.addVariable((String)attributes.elementAt(j));
			}//done dealing with a mapping statment
			
			MappingTable mt = new MappingTable(m_rel, mappingID,  msu);
		
			return mt;
		}
	   
		public void computeGLAVMapping(Predicate m_new_rel, MappingTable newMT){
			
			//start building m_glav
			String next_rel;
			String cur_rel;
			String next_schema;
			String cur_schema;
			Vector attributes = new Vector();
			View a_view = new View();
			int clear = 0;
			for (int i = 1; i < newMT.getRowNum(); i++){
				if (clear == 1){
					attributes = new Vector();
					a_view = new View();
					clear = 0;
				}
				cur_rel = newMT.getRelStrings(i);
//				System.out.println("cur_rel: " + cur_rel);
				cur_schema = getSchemaFromRel(cur_rel);

				if (cur_schema.equals("")){
					clear = 1;
					continue;
				}
				if (i == newMT.getRowNum() - 1){
					next_rel = "";
				}
				else {
					next_rel = (String)newMT.getRelStrings(i + 1);
				}
				next_schema = getSchemaFromRel(next_rel);
//				System.out.println("next_schema: " + next_schema);
				//construct subgoal
				Predicate sg = new Predicate();
				sg.setFunctionHead(cur_rel);
				Hashtable ht = new Hashtable();
				for (int j = 0; j < newMT.getColNum(); j++){
					if (newMT.getAttrMapping(i, j) > 0){
						ht.put(new Integer(newMT.getAttrMapping(i, j)), new Integer(j));
					}
				}
				for (int j = 1; j <= ht.size(); j++){
					Integer col = (Integer)ht.get(new Integer(j));
					IPValue cur_var = m_new_rel.variableI(col.intValue());
					sg.addVariable(cur_var);
					if (!attributes.contains(cur_var)){
						attributes.addElement(cur_var);
					}
					
				}
//				System.out.println("SG:" + sg.printString());
				a_view.addSubgoal(sg);
//				System.out.println("a_view:" + a_view.printString());
				
			
				
				if (!cur_schema.equals(next_schema)){
	            //need to create a new glav
//					System.out.println("IN: cur_schema: " + cur_schema);
					int schemaI = getSchemaIndex(cur_schema, m_glav_inComp);
					
//					System.out.println("schemaI:" + schemaI);
					if (schemaI < 0){
						continue;
					}
					
					
					
					Predicate query_head = new Predicate();
					for (int k = 0; k < attributes.size();k++){
						query_head.addVariable(((IPValue)attributes.elementAt(k)).printString());
					}
//					System.out.println("query_head:" + query_head.printString());
					
					/*
					 * Note: need to check whether a_view already 
					 * in the LV and exactly the same
					 * but here, just use the simplest method to check
					 * Here, assume, one concept only has one GLAV 
					 * to one local schema
					 * Add more later.
					 */
					GLAVMapping a = (GLAVMapping) m_glav_inComp.elementAt(schemaI);
					
//					System.out.println("m_new_rel: " + m_new_rel.printString());
//					System.out.println(a.printString());
					
					Predicate new_sg = new Predicate(m_schema_inComp.getName() + "." + m_new_rel.printString());
					if (a.findExistingGVByHead(new_sg) != null){ //already exist
//						System.out.println("duplicate");
						clear = 1;
						continue;
					}
					
					
					String funchead = ((GLAVMapping) 
							m_glav_inComp.elementAt(schemaI)).getCurrentFreeName();

/*					
					while (((GLAVMapping)m_glav_inComp.elementAt(schemaI)).containHead(funchead)){
						((GLAVMapping)m_glav_inComp.elementAt(schemaI)).incrementNewFreeNameNum();
						funchead = ((GLAVMapping)m_glav_inComp.elementAt(schemaI)).getCurrentFreeName();
					}
*/					
					
					query_head.setFunctionHead(funchead);				
					a_view.setHead(query_head);
					
					((GLAVMapping) m_glav_inComp.elementAt(schemaI)).addLV(a_view);
					a_view = new View();
					a_view.setHead(query_head);
					//Predicate new_sg = new Predicate(m_schema.getName() + "." + m_new_rel.printString());
					//a_view.addSubgoal(m_new_rel);
					a_view.addSubgoal(new_sg);

					((GLAVMapping) m_glav_inComp.elementAt(schemaI)).addGV(a_view);
		//			((GLAVMapping) m_glav_inComp.elementAt(schemaI)).incrementNewFreeNameNum();
					clear = 1;
				}
			}
		}
		
		
		public void computeGLAVMapping2(Predicate m_new_rel, MappingTable newMT, 
				Vector m_glav_tmp){
			//combining two mediated schema (containing overlapped relations, but not 
			//exactly the same)
			
			//start building m_glav
			String next_rel;
			String cur_rel;
			String next_schema;
			String cur_schema;
			Vector attributes = new Vector();
			View a_view = new View();
			int clear = 0;
			for (int i = 1; i < newMT.getRowNum(); i++){
				if (clear == 1){
					attributes = new Vector();
					a_view = new View();
					clear = 0;
				}
				cur_rel = newMT.getRelStrings(i);
//				System.out.println("cur_rel: " + cur_rel);
				cur_schema = getSchemaFromRel(cur_rel);

				if (cur_schema.equals("")){
					clear = 1;
					continue;
				}
				if (i == newMT.getRowNum() - 1){
					next_rel = "";
				}
				else {
					next_rel = (String)newMT.getRelStrings(i + 1);
				}
				next_schema = getSchemaFromRel(next_rel);
//				System.out.println("next_schema: " + next_schema);
				//construct subgoal
				Predicate sg = new Predicate();
				sg.setFunctionHead(cur_rel);
				Hashtable ht = new Hashtable();
				for (int j = 0; j < newMT.getColNum(); j++){
					if (newMT.getAttrMapping(i, j) > 0){
						ht.put(new Integer(newMT.getAttrMapping(i, j)), new Integer(j));
					}
				}
				for (int j = 1; j <= ht.size(); j++){
					Integer col = (Integer)ht.get(new Integer(j));
					IPValue cur_var = m_new_rel.variableI(col.intValue());
					sg.addVariable(cur_var);
					if (!attributes.contains(cur_var)){
						attributes.addElement(cur_var);
					}
					
				}
//				System.out.println("SG:" + sg.printString());
				a_view.addSubgoal(sg);
//				System.out.println("a_view:" + a_view.printString());
				
			
				
				if (!cur_schema.equals(next_schema)){
	            //need to create a new glav
//					System.out.println("IN: cur_schema: " + cur_schema);
					int schemaI = getSchemaIndex(cur_schema, m_glav_inComp);
					int schemaI_tmp = getSchemaIndex(cur_schema, m_glav_tmp);
//					System.out.println("schemaI:" + schemaI);
//					System.out.println("schemaI_tmp:" + schemaI);
					if (schemaI < 0 && schemaI_tmp <0){
						continue;
					}
					
					if (schemaI >= 0){
						Predicate query_head = new Predicate();
						for (int k = 0; k < attributes.size();k++){
							query_head.addVariable(((IPValue)attributes.elementAt(k)).printString());
						}
//						System.out.println("query_head:" + query_head.printString());
						
						/*
						 * Note: need to check whether a_view already 
						 * in the LV and exactly the same
						 * but here, just use the simplest method to check
						 * Here, assume, one concept only has one GLAV 
						 * to one local schema
						 * Add more later.
						 */
						GLAVMapping a = (GLAVMapping) m_glav_inComp.elementAt(schemaI);
						
//						System.out.println("m_new_rel: " + m_new_rel.printString());
//						System.out.println(a.printString());
						
						Predicate new_sg = new Predicate(m_schema_inComp.getName() + "." + m_new_rel.printString());
						if (a.findExistingGVByHead(new_sg) != null){ //already exist
//							System.out.println("duplicate");
							clear = 1;
							continue;
						}
						
						String funchead = ((GLAVMapping) 
								m_glav_inComp.elementAt(schemaI)).getCurrentFreeName();
/*						
						while (((GLAVMapping)m_glav_inComp.elementAt(schemaI)).containHead(funchead)){
							((GLAVMapping)m_glav_inComp.elementAt(schemaI)).incrementNewFreeNameNum();
							funchead = ((GLAVMapping)m_glav_inComp.elementAt(schemaI)).getCurrentFreeName();
						}
*/						
						query_head.setFunctionHead(funchead);				
						a_view.setHead(query_head);
						
						((GLAVMapping) m_glav_inComp.elementAt(schemaI)).addLV(a_view);
						a_view = new View();
						a_view.setHead(query_head);
						//Predicate new_sg = new Predicate(m_schema.getName() + "." + m_new_rel.printString());
						//a_view.addSubgoal(m_new_rel);
						a_view.addSubgoal(new_sg);

						((GLAVMapping) m_glav_inComp.elementAt(schemaI)).addGV(a_view);
			//			((GLAVMapping) m_glav_inComp.elementAt(schemaI)).incrementNewFreeNameNum();
						clear = 1;
					}
					else{ //schemaI_tmp > 0
						Predicate query_head = new Predicate();
						for (int k = 0; k < attributes.size();k++){
							query_head.addVariable(((IPValue)attributes.elementAt(k)).printString());
						}
//						System.out.println("query_head:" + query_head.printString());
						
						/*
						 * Note: need to check whether a_view already 
						 * in the LV and exactly the same
						 * but here, just use the simplest method to check
						 * Here, assume, one concept only has one GLAV 
						 * to one local schema
						 * Add more later.
						 */
						GLAVMapping a = (GLAVMapping) m_glav_tmp.elementAt(schemaI_tmp);
						
//						System.out.println("m_new_rel: " + m_new_rel.printString());
//						System.out.println(a.printString());
						
						Predicate new_sg = new Predicate(m_schema_inComp.getName() + "." + m_new_rel.printString());
						if (a.findExistingGVByHead(new_sg) != null){ //already exist
//							System.out.println("duplicate");
							continue;
						}
						
						String funchead = ((GLAVMapping) 
								m_glav_tmp.elementAt(schemaI_tmp)).getCurrentFreeName();
						
						
/*					
						while (((GLAVMapping)m_glav_tmp.elementAt(schemaI_tmp)).containHead(funchead)){
							((GLAVMapping)m_glav_tmp.elementAt(schemaI_tmp)).incrementNewFreeNameNum();
							funchead = ((GLAVMapping)m_glav_tmp.elementAt(schemaI_tmp)).getCurrentFreeName();
						}
*/					
						query_head.setFunctionHead(funchead);				
						a_view.setHead(query_head);
						((GLAVMapping) m_glav_tmp.elementAt(schemaI_tmp)).addLV(a_view);
						
						a_view = new View();
						a_view.setHead(query_head);
						//Predicate new_sg = new Predicate(m_schema.getName() + "." + m_new_rel.printString());
						//a_view.addSubgoal(m_new_rel);
						a_view.addSubgoal(new_sg);

						((GLAVMapping) m_glav_tmp.elementAt(schemaI_tmp)).addGV(a_view);
						
						m_glav_inComp.add(m_glav_tmp.elementAt(schemaI_tmp));
						
						clear = 1;
					}				
				}
			}
		}
		
		
		
		public Predicate computeNewMediatedRel(Predicate m_old_rel, 
				Predicate m_rel, MappingTable MT){
			Predicate m_update = new Predicate();
			m_update.setFunctionHead(m_rel.getFunctionHead());
		
	/*		
			int index1 = 0, index2 = 0;  //find the two intermediated relation head in MT
			int tag = 0;
			for (int i = 1; i < MT.getRowNum(); i++){
				String head = (String) MT.getRelStrings(i);
				if (tag == 0 && head.equals(MT.getName())){
					index1 = i;				
					tag = 1;
					continue;
				}
				else if (tag == 1 && head.equals(MT.getName())){
					index2 = i;
					break;
				}			
			}
	*/		
			int index1 = MT.getIndex1();
			int index2 = MT.getIndex2();

//			System.out.println("index11: " + index11);
//			System.out.println("index22: " + index22);
			
			
			for (int i = 0; i < MT.getColNum(); i++){
				IPValue var;
				if (MT.getAttrMapping(index1, i)!= 0){
					var = m_rel.variableI(MT.getAttrMapping(index1, i) - 1);
					if (m_update.containsVariable(var)){
						var = this.getNextIPValue();
					}
					m_update.addVariable(var);
				}
				else if(MT.getAttrMapping(index2, i) != 0) {
					var = m_old_rel.variableI(MT.getAttrMapping(index2, i) - 1);
					if (m_update.containsVariable(var)){
						var = this.getNextIPValue();
					}
					m_update.addVariable(var);
				}
			}
			return m_update;
		}
		
		public int getSchemaIndex(String schemaName, Vector glavVector ){
//			System.out.println("IN schemaName: " + schemaName);
			for (int i = 0; i < glavVector.size(); i++){
				GLAVMapping glavm = (GLAVMapping)glavVector.get(i);
//				System.out.println("glav" + i + " :" + glavm.getSchema());
				if (glavm.getSchema().equals(schemaName)){
					return i;
				}
			}
			return -1;
		}
		
		public void removeGLAVMapping(Vector m_glavtmp, Predicate m_new_rel){
			for (int i = 0; i < m_glavtmp.size(); i++){
				GLAVMapping a = (GLAVMapping) m_glavtmp.elementAt(i);
				Predicate new_sg = new Predicate(m_schema_inComp.getName() + "." + m_new_rel.printString());			
//	            System.out.println("new_sg: " + new_sg.printString());
				int index = a.findExistingGVByConcept(new_sg);
				if (index >= 0){
					((GLAVMapping) m_glavtmp.elementAt(i)).removeGLAV(index);				
				}
			}
		}
		
		public void initializeGLAV(Mapping mapping){
//			System.out.println("initialize glav...");
			Schema schema1 = mapping.getSchemaInfo(0);
			Schema schema2 = mapping.getSchemaInfo(1);
//			System.out.println("IN initializing...");
//			System.out.println("schema1:" + schema1.getName());
//			System.out.println("schema2:" + schema2.getName());
			
			int not1 = 0;
			int not2 = 0;
			for (int i = 0; i < m_glav_inComp.size(); i++){
//				System.out.println(((GLAVMapping)m_glav_inComp.elementAt(i)).getSchema());
				if (((GLAVMapping)m_glav_inComp.elementAt(i)).getSchema().equals(schema1.getName())){
					not1 = 1;
				}
				if (((GLAVMapping)m_glav_inComp.elementAt(i)).getSchema().equals(schema2.getName())){
					not2 = 1;
				}
			}
			
			if (not1 == 0){
				GLAVMapping glav = new GLAVMapping(schema1.getName(), schema2.getName());
//				System.out.println("first glav: " + schema1.getName());
			//	glav.setSchema(schema1.getName());
				m_glav_inComp.addElement(glav);
			}
			if (not2 == 0){
				GLAVMapping glav = new GLAVMapping(schema2.getName(), schema1.getName());
//				System.out.println("second glav:" + schema2.getName());
			//	glav.setSchema(schema2.getName());
				m_glav_inComp.addElement(glav);
			}
//			System.out.println("m_glav.size: " + m_glav_inComp.size());
		}
		

		
		public boolean containsMapping(Vector mappinginfo, String mapping){
			int index = mapping.indexOf(" ");
			String schema1 = mapping.substring(0, index);
			String schema2 = mapping.substring(index + 1);
			if (mappinginfo.contains(schema1 + " " + schema2) ||
					mappinginfo.contains(schema2 + " " + schema1)){		  
				return true;
		    }
			else
				return false;
		}
		
		
		public IPValue getNextIPValue(){
			String ipv = "xx"+ this.getPeerName();
			
			
			IPValue var = new IPValue(ipv+ipvNextNum);
			ipvNextNum++;
			
			return var;
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
		  
		public void setTestPdms1(){
			  Schema s1 = new Schema();
			  s1.setName("UBC");
			  s1.addRelation("UBC.conf-paper(title, venue, year, pages)");
			  s1.addRelation("UBC.univ_proj(univ, project)");
			  s1.addRelation("UBC.proj_area(project, area)");
			  setPeerSchema(s1); 
		  }
		  
		  public void setTestPdms2(){
			  Schema s2 = new Schema();
			  s2.setName("UW");
			  s2.addRelation("UW.conf-paper(title, venue, year, url)");
			  s2.addRelation("UW.project(univ, project)");
			  setPeerSchema(s2);
		  }
		  
		  public void setTestPdms3(){
			  Schema s3 = new Schema();
			  s3.setName("UT");
			  s3.addRelation("UT.conference-paper(venue, title, first-author)");
		      setPeerSchema(s3);
		  }
		  
		  public void setTestPdms4(){
			  Schema s4 = new Schema();
			  s4.setName("UCB");
			  s4.addRelation("UCB.conf-paper(title, location, abstract)");
		      setPeerSchema(s4);
		  }
		  
		  public void setTestPdms5(){
			  Schema s5 = new Schema();
			  s5.setName("UV");
			  s5.addRelation("UV.conf-paper(title, venue, sub-area)");
		      setPeerSchema(s5);
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


	
  
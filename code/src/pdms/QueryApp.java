package pdms;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Vector;

import mediation.GLAVMapping;
import mediation.Schema;
import minicon.Query;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNodeFactory;
import rice.pastry.socket.SocketPastryNodeFactory;

public class QueryApp implements Application {

	protected Endpoint endpoint;
	  /**
	   * The node we were constructed on.
	   */
	protected Node node;
	
	protected Query query;
	
	protected Query resultQ;
	  
	private String peer_name;
	  
	private Schema p_schema;
	   
	protected Schema m_schema;
	
	protected Vector acquaintances;
	
	public Hashtable nodeHash;
	  
	public PastryNodeFactory factory;
	  
	public Vector acq_handle;
	
	protected GLAVMapping m_glav;
	
	private int queryReceived = 0;
	
	protected long localStartTime = 0;
	  
	protected long localEndTime = 0;
	  
	protected long procStartT = 0;
	  
	protected long procEndT = 0;
	  
	protected long localProcT = 0;
	  
	protected long msgTime = 0;
	  
	protected int receiveMsgNum = 0;
	  
	protected int sendMsgNum = 0;
	
	
	public QueryApp(Node node, PastryNodeFactory factory) {	
		this.endpoint = node.registerApplication(this, "MyQueryApp");
	    this.node = node;
	    this.factory = factory;
	    query = new Query();
	    resultQ = new Query();
	   	peer_name = "";
		p_schema = new Schema();
		acquaintances = new Vector();
		m_schema = new Schema();
		m_schema.setName("M");
		nodeHash = new Hashtable();
		acq_handle = new Vector();
		m_glav = new GLAVMapping();
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
	 
	 
	 public Node getNode() {
		  return node;
	 }
	 
	 public Query getQuery(){
		 return query;
	 }
	 
	 public void setQuery(Query q){
		 this.query = q;
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
	 
	 public void setMediatedSchema(Schema mschema){
		 this.m_schema = mschema;
	 }
	  
	 public GLAVMapping getGLAVMapping(){
		 return m_glav;
	 }
	 
	 public void setGLAVMapping(GLAVMapping glav){
		 m_glav = glav;
	 }
	  
	 public void setAcquaintance(int tgtSeq){
		 acquaintances.addElement(new Integer(tgtSeq));
	 }
	  
	 public Vector getAcquaintances(){
		 return acquaintances;
	 } 
	 
	 

	
	 public void sendQueryMsg(NodeHandle nh) {  
		 System.out.println("Sending broadcasting message from " + 
				 this + " " + this.p_schema.getName() +
				 " to the dest " + nh);
		 PdmsQueryMsg qmsg = new PdmsQueryMsg(endpoint.getId(), nh.getId(), 
				 this.query, this.peer_name, this.node.getLocalNodeHandle());
		 endpoint.route(null, qmsg, nh);
	 }
	 
	 
	 
	 public void deliver(Id id, Message msg) {
		 System.out.println("message received");
		 String msgType = msg.getClass().getName();
		 System.out.println("msgType: " + msgType);
		 System.out.println(System.nanoTime());
		 if (queryReceived == 0){//never received msg; if received, won't process for this experiment
			 queryReceived = 1;
			 if (msgType.equals("Message")){
			      System.out.println("Received " + msg + " at " + 
			    		  "NodeID: " + this +
			    	      " nodeName: " + this.getPeerName());  
			 }
			 else if (msgType.equals("pdms.PdmsQueryMsg")){
				 System.out.println("Received " + msg + " at " + 
						 "NodeID: " + this +
						 " nodeName: " + this.getPeerName());  
				 PdmsQueryMsg queryMsg = (PdmsQueryMsg)msg;
				 this.query = queryMsg.query;
				 
	             if (queryMsg.schemaName.equals(this.getSchemaName())){
					 //note down when the first peer starts computation
	            	 String fileName = "log/timer/systemstart.log";
					  
	            	 FileWriter fw = null;
	            	 try {
	            		 fw = new FileWriter(fileName, true); 
	            		 fw.write(System.nanoTime() + ""); 
	            		 fw.flush();
	            		 fw.close(); 
	            	 }
	            	 catch (IOException ex) {  }
	             }
	             
	             /*
	              * first send to the same query to others
	              */
             
	             
	             System.out.println("sending QueryMsg to " + acquaintances.size() + " nodes");
	             for (int i = 0; i < acquaintances.size(); i++){
	            	 long msgStart = System.nanoTime();
     	  
	            	 Integer nodeseq = ((Integer)acquaintances.elementAt(i));
			
	            	 NodeHandle nodeHandle = (NodeHandle)acq_handle.elementAt(i);
					  
	            	 while (nodeHandle == null){
	            		 System.out.print("node not ready!");								  
	            		 try {
	            			 Thread.sleep(100);
	            		 } catch (Exception e) {
	            			 
	            		 }
	            		 nodeHandle = getNodeHandle(nodeseq, factory);
						  
	            	 }
	            	 
	            	 if (queryMsg.handle.equals(nodeHandle)){//won't send back
	            		 continue;	            		 
	            	 }
			/*		 
	            	 ///////start sending msg
	            	 String fnMsg = "log/timer/Msg" + this.peer_name + ".log";
		             FileWriter fw = null;
		             try{
		            	 fw = new FileWriter(fnMsg, true); 
		            	 fw.write(" Start " + System.nanoTime());
		            	 fw.flush();
		            	 fw.close(); 
		             }
		             catch (IOException ex) {  }
			*/		
	            	 System.out.println("Sending query to " + nodeHandle);
	            	 System.out.println(System.nanoTime());
					
	            	 sendQueryMsg(nodeHandle);
	       /*     	 
	            	 /////// write down the msg end sending time
					  fnMsg = "log/timer/Msg" + this.peer_name + ".log";
					  fw = null;
					  try{
		            	  fw = new FileWriter(fnMsg, true); 
		            	  fw.write(" End " + System.nanoTime());
		            	  fw.flush();
		            	  fw.close(); 
		              }
		              catch (IOException ex) {  }
		              /////////
			*/	
	            	  this.sendMsgNum++;
					  
					  long msgEnd = System.nanoTime();
					  this.msgTime += msgEnd - msgStart;
	             }
          
	             
	             /*
	              * second do local computation
	              */
	           /* 
	             //////start counting time
	             String fnStart = "log/timer/queryc" + this.peer_name + ".log";
				 FileWriter fw1 = null;
	             try {
	                    fw1 = new FileWriter(fnStart, true); 
	                    fw1.write(" start " + System.nanoTime()); 
	                    fw1.flush();
	                    fw1.close(); 
	             }
	             catch (IOException ex) {  }
	             /////////
	           */
	             this.localStartTime = System.nanoTime();
	             
	             System.out.println("query: " + query.printString());
	             System.out.println("m_glav: " + m_glav.printString());
	             QueryReformulation qr = new QueryReformulation(query, m_glav);
	             
	             for (int i = 0; i < 10; i++){
	            	 resultQ = qr.getResultQuery(); 
	             }
	             
	             this.localEndTime = System.nanoTime();	             
	             
	             /////end counting time
	             /*
	             String fnEnd = "log/timer/queryc" + this.peer_name + ".log";
	             fw1 = null;
	             try {
	            	 fw1 = new FileWriter(fnEnd, true); 
	            	 fw1.write(" end " + System.nanoTime()); 
	            	 fw1.flush();
	            	 fw1.close(); 
	             }
	             catch (IOException ex) {  }
	             */
	             ////////
	             
	             
	             String fnEnd = "log/timer/queryc" + this.peer_name + ".log";
	             FileWriter fw1 = null;
	             try {
	            	 fw1 = new FileWriter(fnEnd, true); 
	            	 fw1.write(" start " + this.localStartTime);
	            	 fw1.write(" end " + this.localEndTime); 
	            	 fw1.flush();
	            	 fw1.close(); 
	             }
	             catch (IOException ex) {  }
	             
	             
	             ///write to file
	             String result = "log/Query/result" + this.peer_name + ".log";
				 FileWriter fw = null;
	             try {
	                    fw = new FileWriter(result, true); 
	                    fw.write(resultQ.printString()+""); 
	                    fw.close(); 
	             }
	             catch (IOException ex) {  }
	                         
			 }
			 printAllTime();	 
		 }
			 
	 }

	 
	  public void printAllTime(){
		  System.out.println("local start time " + this.localStartTime);
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

	
}

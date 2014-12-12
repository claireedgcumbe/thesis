package pdms;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Vector;

import mediation.GLAVMapping;
import mediation.Mapping;
import mediation.MappingTable;
import mediation.Schema;
import minicon.IPValue;
import minicon.Predicate;
import minicon.Query;
import minicon.View;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNodeFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import semantics.MappingStatementUnion;

public class NewSchemaJoinApp implements Application {

	protected Endpoint endpoint;
	/**
	 * The node we were constructed on.
	 */
	protected Node node;
	
	protected Query resultQ;
	  
	private String peer_name;
	  
	private Schema p_schema;
	   
	protected Schema m_schema;
	
	protected Vector mtSets;
	
	protected Vector mappingInfo;
	
	protected Vector acquaintances;
	
	public Hashtable nodeHash;
	  
	public PastryNodeFactory factory;
	  
	public Vector acq_handle;
	
	protected Vector m_glav;
	
	private int schemaReceived = 0;	
	
	public static int ipvNextNum = 0;
	
	protected long localStartTime = 0;
	  
	protected long localEndTime = 0;
	  
	protected long updateStartT = 0;
	  
	protected long updateEndT = 0;
	  
	protected long localProcT = 0;
	  
	protected long msgTime = 0;
	  
	protected int receiveMsgNum = 0;
	  
	protected int sendMsgNum = 0;
	
	protected long lastMsgEndT = 0;
		
	
	public NewSchemaJoinApp(Node node, PastryNodeFactory factory) {	
		this.endpoint = node.registerApplication(this, "MyJoinApp");
	    this.node = node;
	    this.factory = factory;
	    resultQ = new Query();
	   	peer_name = "";
		p_schema = new Schema();
		mtSets = new Vector();
		mappingInfo = new Vector();
		acquaintances = new Vector();
		m_schema = new Schema();
		m_schema.setName("M");
		nodeHash = new Hashtable();
		acq_handle = new Vector();
		m_glav = new Vector();
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
	  
	 public Vector getGLAVMapping(){
		 return m_glav;
	 }
	 
	 public void setGLAVMappings(Vector m_glav){
		 this.m_glav = m_glav;
	 }
	  
	 public void setAcquaintance(int tgtSeq){
		 acquaintances.addElement(new Integer(tgtSeq));
	 }
	  
	 public Vector getAcquaintances(){
		 return acquaintances;
	 } 
	 
	 public void setMtSet(Vector mtSet){
		 this.mtSets = mtSet;
	 }
	 
	 public void setMappingInfos(Vector mappingInfo){
		 this.mappingInfo = mappingInfo;
	 }

	
	 public void sendMSchemaMsg(NodeHandle nh) {  
		 System.out.println("Sending broadcasting message from " + 
				 this + " " + this.p_schema.getName() +
				 " to the dest " + nh);
		 PdmsNewSchemaMsg qmsg = new PdmsNewSchemaMsg(endpoint.getId(), nh.getId(), 
				 this.peer_name, this.node.getLocalNodeHandle());
		 qmsg.setGlav(m_glav);
		 qmsg.setMappingInfos(mappingInfo);
		 qmsg.setMschema(m_schema);
		 qmsg.setMTSet(mtSets);
		 
		 endpoint.route(null, qmsg, nh);
	 }
	 
	 public void sendMappingMsg(NodeHandle nh, Mapping map){
		 System.out.println("Sending new peer mapping message from " + 
				 this + " " + this.p_schema.getName() +
				 " to the dest " + nh);
		 PdmsJoinMsg jmsg = new PdmsJoinMsg(endpoint.getId(), nh.getId(), peer_name, 
				 this.node.getLocalNodeHandle());
		 jmsg.setMapping(map);
		 endpoint.route(null, jmsg, nh);
	 }
	 
	 
	 public void deliver(Id id, Message msg) {
		 System.out.println("message received");
		 String msgType = msg.getClass().getName();
		 System.out.println("msgType: " + msgType);
		 System.out.println(System.nanoTime());

		 if (msgType.equals("Message")){
			 System.out.println("Received " + msg + " at " + 
					 "NodeID: " + this +
					 " nodeName: " + this.getPeerName());  
		 }
		 else if (msgType.equals("pdms.PdmsJoinMsg")){
			 PdmsJoinMsg jmsg = (PdmsJoinMsg) msg;
			 if (jmsg.schemaName.equals(this.getSchemaName())){
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
              * do the new mediated schema computation
              */
			 
            
 /*            //////start counting time
             String fnStart = "log/timer/lc" + this.peer_name + ".log";
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
             Mapping map = jmsg.getMapping();
             
             schemaMerge(map);
             
             this.localEndTime = System.nanoTime();	 
             
             
             
 /*            
             /////end counting time
             String fnEnd = "log/timer/lc" + this.peer_name + ".log";
             fw1 = null;
             try {
            	 fw1 = new FileWriter(fnEnd, true); 
            	 fw1.write(" end " + System.nanoTime()); 
            	 fw1.flush();
            	 fw1.close(); 
             }
             catch (IOException ex) {  }
             ////////
             
             ///write to file
             String result = "log/NewSchema/result" + this.peer_name + ".log";
			 fw1 = null;
             try {
                    fw1 = new FileWriter(result, true); 
                    fw1.write("done"); 
                    fw1.close(); 
             }
             catch (IOException ex) {  }
    */         
             
             /////send out PmdsNewSchemaMsg
             
             /*
              * send this msg to all the acquaintances except the source
              */
         
             
             System.out.println("sending NewSchemaMsg to " + acquaintances.size() + " nodes");
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
	             //////
				
            	 System.out.println("Sending New Mediated schema to " + nodeHandle);
				
            	 sendMSchemaMsg(nodeHandle);
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
				  lastMsgEndT = msgEnd;
				  this.msgTime += msgEnd - msgStart;
             }			 
             
             String fnEnd = "log/timer/lc" + this.peer_name + ".log";
             FileWriter fw1 = null;
             try {
            	 fw1 = new FileWriter(fnEnd, true); 
            	 fw1.write(" start " + this.localStartTime);
            	 fw1.write(" end " + this.localEndTime); 
            	 fw1.flush();
            	 fw1.close(); 
             }
             catch (IOException ex) {  }
             
             fnEnd = "log/timer/msg" + this.peer_name + ".log";
             fw1 = null;
             try {
            	 fw1 = new FileWriter(fnEnd, true); 
            	 fw1.write("" + this.lastMsgEndT); 
            	 fw1.flush();
            	 fw1.close(); 
             }
             catch (IOException ex) {  }
             
		 }
		 
		 else if (msgType.equals("pdms.PdmsNewSchemaMsg")){
			 System.out.println("Received " + msg + " at " + 
					 "NodeID: " + this +
					 " nodeName: " + this.getPeerName());  
			 if (schemaReceived == 0){//never received msg; if received, won't process for this experiment
				 schemaReceived = 1;
				 
			/*	 
				 String fnUpd = "log/timer/update" + this.peer_name + ".log";
	             FileWriter fw1 = null;
	             try{
	            	 fw1 = new FileWriter(fnUpd, true); 
	            	 fw1.write(" Start " + System.nanoTime());
	            	 fw1.flush();
	            	 fw1.close(); 
	             }
	             catch (IOException ex) {  }
				*/
				 
				 updateStartT = System.nanoTime();
				 
				 PdmsNewSchemaMsg newSchemaMsg = (PdmsNewSchemaMsg)msg;
				 this.m_glav = newSchemaMsg.m_glav;
				 this.m_schema = newSchemaMsg.m_schema;
				 this.mappingInfo = newSchemaMsg.mappingInfos;
				 this.mtSets = newSchemaMsg.mtSet;
				
				 /*
				 fnUpd = "log/timer/update" + this.peer_name + ".log";
	             fw1 = null;
	             try{
	            	 fw1 = new FileWriter(fnUpd, true); 
	            	 fw1.write(" End " + System.nanoTime());
	            	 fw1.flush();
	            	 fw1.close(); 
	             }
	             catch (IOException ex) {  }
				 */
				 updateEndT = System.nanoTime();
           
	             
	             /*
	              * send this msg to all the acquaintances except the source
	              */
             
	             
	             System.out.println("sending NewSchemaMsg to " + acquaintances.size() + " nodes");
	             for (int i = 0; i < acquaintances.size(); i++){
//	            	 System.out.println("sending...");
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
	            	 
	            	 if (newSchemaMsg.handle.equals(nodeHandle)){//won't send back
	            		 this.lastMsgEndT = System.nanoTime();
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
		             //////
	            	 long msgStart = System.nanoTime();
	            	 System.out.println("Sending New Mediated schema to " + nodeHandle);
					
	            	 sendMSchemaMsg(nodeHandle);
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
					  this.lastMsgEndT = msgEnd;
					  
					  this.msgTime += msgEnd - msgStart;
					  
	            	 
	             }
	             
	             String fnUpd = "log/timer/update" + this.peer_name + ".log";
	             FileWriter fw1 = null;
	             try{
	            	 fw1 = new FileWriter(fnUpd, true); 
	            	 fw1.write(" Start " + this.updateStartT);
	            	 fw1.write(" End " + this.updateEndT);
	            	 fw1.flush();
	            	 fw1.close(); 
	             }
	             catch (IOException ex) {  }
          
                         
	             String fnEnd = "log/timer/msg" + this.peer_name + ".log";
	             fw1 = null;
	             try {
	            	 fw1 = new FileWriter(fnEnd, true); 
	            	 fw1.write("" + this.lastMsgEndT); 
	            	 fw1.flush();
	            	 fw1.close(); 
	             }
	             catch (IOException ex) {  }
			 }
			 
		 }
			 
	 }
	 
	 public void printAllTime(){
		  System.out.println("local start time " + this.localStartTime);
		  System.out.println("total time for current node up to now: " + (this.localEndTime - this.localStartTime));
		  System.out.println("sending out msg using: " + this.msgTime);
		  System.out.println("received " + this.receiveMsgNum + " msg ");
		  System.out.println("sent out " + this.sendMsgNum + " msg ");		
	  }
	 
	 public void schemaMerge(Mapping mapping){ //for more than 3
		  int mappingID = 9999;
		  
		  int num_mapping_unions = mapping.numMappingUnions();

				
		  for (int k = 0; k < num_mapping_unions; k++){
			  Predicate m_rel = new Predicate(); //new mediated relation
			  MappingStatementUnion msu = mapping.MappingUnionI(k);
		    
			  int mtIndex = findMT(this.mtSets, msu.getHead());

			  MappingTable current;
			  if (mtIndex < 0) { //new concept
				  current = MergeSchema(m_rel, mappingID, msu);		
				  mtSets.add(current);

				  m_schema.addRelation(m_rel);
//				  System.out.println(m_rel.printString());
//				  current.printMappingTable();
				  computeGLAVMapping(m_rel, current);
			  }
					
			  else { //concept already exists 
					 /* add codes here: mapping compatible check*/
				  MappingTable oldMT =  (MappingTable)mtSets.get(mtIndex);
				  current = MergeSchema(m_rel, mappingID, msu);
				  MappingTable newMT = current.combineMappingTable(oldMT);

				  
				  mtSets.remove(mtIndex);
				  mtSets.add(newMT);
				  /* add codes here: get new mediated relation based on 
				   * the new MappingTable*/
				  Predicate m_old_rel = m_schema.findRelation(m_rel.getFunctionHead());
						
				  Predicate m_new_rel = computeNewMediatedRel(m_old_rel, m_rel, newMT);
				  m_schema.delRelation(m_rel.getFunctionHead());
				  m_schema.addRelation(m_new_rel);
						
				  //remove old GLAV for that concept first
				  removeGLAVMapping(m_glav, m_new_rel);
				  computeGLAVMapping(m_new_rel, newMT);
			  }			
		  }
	   }

	 
	 public MappingTable MergeSchema(Predicate m_rel,
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
	 
	 
	 
	 public Predicate computeNewMediatedRel(Predicate m_old_rel, 
				Predicate m_rel, MappingTable MT){
			Predicate m_update = new Predicate();
			m_update.setFunctionHead(m_rel.getFunctionHead());
		
			int index1 = MT.getIndex1();
			int index2 = MT.getIndex2();
		
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
	 
	 
	 public void removeGLAVMapping(Vector m_glavtmp, Predicate m_new_rel){
			for (int i = 0; i < m_glavtmp.size(); i++){
				GLAVMapping a = (GLAVMapping) m_glavtmp.elementAt(i);
				Predicate new_sg = new Predicate(m_schema.getName() + "." + m_new_rel.printString());			
//	            System.out.println("new_sg: " + new_sg.printString());
				int index = a.findExistingGVByConcept(new_sg);
				if (index >= 0){
					((GLAVMapping) m_glavtmp.elementAt(i)).removeGLAV(index);				
				}
			}
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
					int schemaI = getSchemaIndex(cur_schema, m_glav);
					
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
					GLAVMapping a = (GLAVMapping) m_glav.elementAt(schemaI);
					
					Predicate new_sg = new Predicate(m_schema.getName() + "." + m_new_rel.printString());
					if (a.findExistingGVByHead(new_sg) != null){ //already exist
						clear = 1;
						continue;
					}
					
					
					String funchead = ((GLAVMapping) 
							m_glav.elementAt(schemaI)).getCurrentFreeName();

					
					query_head.setFunctionHead(funchead);				
					a_view.setHead(query_head);
					
					((GLAVMapping) m_glav.elementAt(schemaI)).addLV(a_view);
					a_view = new View();
					a_view.setHead(query_head);
					a_view.addSubgoal(new_sg);

					((GLAVMapping) m_glav.elementAt(schemaI)).addGV(a_view);
					clear = 1;
				}
			}
		}
	 
	 
		public int getSchemaIndex(String schemaName, Vector glavVector ){
			for (int i = 0; i < glavVector.size(); i++){
				GLAVMapping glavm = (GLAVMapping)glavVector.get(i);
				if (glavm.getSchema().equals(schemaName)){
					return i;
				}
			}
			return -1;
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
	   
	 public IPValue getNextIPValue(){
			String ipv = "xx"+ this.getPeerName();
			
			
			IPValue var = new IPValue(ipv+ipvNextNum);
			ipvNextNum++;
			
			return var;
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

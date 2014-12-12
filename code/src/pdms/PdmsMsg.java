package pdms;

import java.util.Vector;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;

import mediation.Schema;

/**
 * @author jzhao
 * PdmsMsg is a message that passes intermediate mediated schema
 * from one peer to its aquaintance.
 */

public class PdmsMsg implements Message {
	/**
	   * Where the Message came from.
	   */
	  protected Id from;
	  /**
	   * Where the Message is going.
	   */
      protected Id to;
	  
      protected String peerName;
	  protected String msgid;
	  protected Schema m_schema;
	  protected Vector glav_mappings;
	  protected Vector mtSet;
	  protected Vector mappingInfo; 
	  protected Vector backPath;
	  //indicates what mapping info
	  //this mediated schema is using
	  //e.g., if M uses mapping A_B, then 
	  //one entry in M should be "A--B"
	  
  
	
	  public PdmsMsg(Id from, Id to, String peerName, String mid){
		  this.from = from;
		  this.to = to;	
		  this.peerName = peerName;
		  msgid = mid;
		  m_schema = new Schema();
		  glav_mappings = new Vector();
		  mtSet = new Vector();
		  mappingInfo = new Vector();
		  backPath = new Vector();
	  }
	  
	  /*
	  public PdmsMsg(PdmsApp src, PdmsApp tgt, String mid) {
    super(tgt.getAddress());
//    super.setSender(src.getNodeHandle());
    srcApp = src;
    tgtApp = tgt;
    msgid = mid;
    m_schema = new Schema();
    glav_mappings = new Vector();
    mtSet = new Vector();
    mappingInfo = new Vector();
    msgType = 1;
  }*/
	  
	  
	  /**
	   * Use low priority to prevent interference with overlay maintenance traffic.
	   *
	   * @return The Priority value
	   */
	  public int getPriority() {
	    return Message.LOW_PRIORITY;
	  }  
	  
	  
	  public void setMediatedSchema(Schema mediatedschema){
		  m_schema = mediatedschema;
	  }
	  
	  public Schema getMediatedSchema(){
		  return m_schema;
	  }
	  
	  public void setGlavMappings(Vector glavMappings){
		  glav_mappings = glavMappings;
	  }
	  
	  public Vector getGlavMappings(){
		  return glav_mappings;
	  }
	  
	  public void setMTSet(Vector mappingTables){
		  mtSet = mappingTables;
	  }
	  
	  public Vector getMTSet(){
		  return mtSet;
	  }
	  
	  public Vector getMappingInfo(){
		  return mappingInfo;
	  }
	  
	  public void setMappingInfo(Vector mapInfo){
		  mappingInfo = mapInfo; 
	  }

	/*
	  public int containsMapping(String schema1, String schema2){
		  if (mappingInfo.contains(schema1 + " " + schema2) ||
			  mappingInfo.contains(schema2 + " " + schema1)){		  
			  return 1;
		  }
		  else
			  return -1;
	  }
	*/
	  
	  public String getMsgId() {
		    return msgid;
		  }
  
	  
	  /**
	   * DESCRIBE THE METHOD
	   *
	   * @return DESCRIBE THE RETURN VALUE
	   */
	  public String toString() {
	    return "PMDS Mediated schema Broadcasting MSG #" + 
	    msgid + " from " + from + " to " + to;
	  }


}

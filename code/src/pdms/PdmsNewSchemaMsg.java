package pdms;

import java.util.Vector;

import mediation.Schema;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

public class PdmsNewSchemaMsg implements Message {
	
	/**
	   * Where the Message came from.
	   */
	  protected Id from;
	  /**
	   * Where the Message is going.
	   */
	  protected Id to;
	  
	  protected Schema m_schema;
	  
	  protected Vector mtSet;
	  
	  protected Vector mappingInfos;
	  
	  protected Vector m_glav;

	  protected String schemaName;
	  
	  protected NodeHandle handle;
	  

	
	  public PdmsNewSchemaMsg(Id from, Id to, String sname, NodeHandle handle){
		  this.from = from;
		  this.to = to;	
		  this.schemaName = sname;
		  this.handle = handle;
		  m_schema = new Schema();
		  mtSet = new Vector();
		  m_glav = new Vector();
		  mappingInfos = new Vector();
	  }
	  
	  public void setMschema(Schema m_schema){
		  this.m_schema = m_schema;
	  }
	  
	  public void setMTSet(Vector mtSet){
		  this.mtSet = mtSet;
	  }
	  
	  public void setGlav(Vector m_glav){
		  this.m_glav = m_glav;
	  }
	  	
	  public void setMappingInfos(Vector mappingInfos){
		  this.mappingInfos = mappingInfos;
	  }
	  
	  /**
	   * Use low priority to prevent interference with overlay maintenance traffic.
	   *
	   * @return The Priority value
	   */
	  public int getPriority() {
	    return Message.LOW_PRIORITY;
	  }  
	  
	    
	  
	  /**
	   * DESCRIBE THE METHOD
	   *
	   * @return DESCRIBE THE RETURN VALUE
	   */
	  public String toString() {
	    return "PMDS Query Broadcasting MSG #" + 
	    " from " + from + " to " + to;
	  }

}

	
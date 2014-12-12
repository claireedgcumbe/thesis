package pdms;


import minicon.Query;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

public class PdmsQueryMsg implements Message {
	
	/**
	   * Where the Message came from.
	   */
	  protected Id from;
	  /**
	   * Where the Message is going.
	   */
	  protected Id to;
	  
	  protected Query query;
	  
	  protected String schemaName;
	  
	  protected NodeHandle handle;
	  

	
	  public PdmsQueryMsg(Id from, Id to, Query query, String sname, NodeHandle handle ){
		  this.from = from;
		  this.to = to;	
		  this.query = query;
		  this.schemaName = sname;
		  this.handle = handle;
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

	
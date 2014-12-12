package pdms;

import mediation.Mapping;
import mediation.Schema;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

public class PdmsJoinMsg implements Message {

	  protected Id from;
	  /**
	   * Where the Message is going.
	   */
      protected Id to;
      private Schema p_schema;
      private Mapping mapping;
	  protected String schemaName;
	  protected NodeHandle handle;
     
      public PdmsJoinMsg(Id from, Id to, String sname, NodeHandle handle) {
    	  this.from = from;
		  this.to = to;	
		  this.schemaName = sname;
		  this.handle = handle;
      	  p_schema = new Schema();
      	  mapping = new Mapping();
      }
      
      public int getPriority() {
  	    return Message.LOW_PRIORITY;
  	  }
      
      public void setPeerSchema(Schema pschema){
      	p_schema = pschema;
      }
      
      public Schema getPeerSchema(){
      	return p_schema;
      }
      
      public void setMapping(Mapping m){
      	mapping = m;
      }
      
      public Mapping getMapping(){
      	return mapping;
      }
      
 
      
      public String toString() {
          return "PMDSJOIN MSG #" + " from " + 
          from + " to " + to;
      }


    
    
    
    
}

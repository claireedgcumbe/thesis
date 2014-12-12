package pdms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
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

import mediation.Mapping;
import mediation.Schema;
import minicon.View;

/*
 * @author:jzhao
 * May 15, 2006
 */

public class SchemaMediationInPdmsExp1 {
	  // this will keep track of our applications

	 protected int nodeSeq;
	 public Hashtable nodeHash;
	 
	 Vector schemasIn;
	 Vector mappingsIn;
	 Vector tpIn;
	 
	 
	 public SchemaMediationInPdmsExp1(int bindport, InetSocketAddress bootaddress,
			 int nodeSeq, Environment env) throws Exception {
		 // Generate the NodeIds Randoml
		 nodeHash = new Hashtable();
		 this.nodeSeq = nodeSeq; 
		 
		 schemasIn = new Vector();
		 mappingsIn = new Vector();
		 tpIn = new Vector();
		 
		 
         //	Generate the NodeIds Randomly
		 NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
		 // construct the PastryNodeFactory, this is how we use rice.pastry.socket
		 PastryNodeFactory factory = new SocketPastryNodeFactory(nidFactory, bindport, env);
		 
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
		 PdmsApp app = new PdmsApp(node, factory);
		 
		  /**
		   * 
		   *        Test case topoloty:
		   *   
		   *                UBC
		   *              1/   \ 2
		   *             UW     UT
		   *            4/       \ 3   
		   *           UV         UCB 
		   */ 
		  

		 switch (nodeSeq) {
	     case 0:  	    	 
	    	 app.setTestPdms1();
	         app.setAcquaintance(1);
	         app.setMapping(this.genMapping1());
	  	         
	         app.setAcquaintance(2);
	         app.setMapping(this.genMapping2());
	         break;
	         
	     case 1:  
         	 app.setTestPdms2();
         	 app.setAcquaintance(0);
         	 app.setMapping(this.genMapping1());
         	 
     	     app.setAcquaintance(4);
         	 app.setMapping(this.genMapping4()); 
         	 break;
               
         case 2:  
        	 app.setTestPdms3();
        	 app.setAcquaintance(0);
        	 app.setMapping(this.genMapping2());
        	 
        	 app.setAcquaintance(3);
        	 app.setMapping(this.genMapping3());
        	 break;
         	
         	
         case 3:  
        	 app.setTestPdms4();
        	 app.setAcquaintance(2);
        	 app.setMapping(this.genMapping3());
        	 break;
        	 
         case 4: 
        	 app.setTestPdms5();
        	 app.setAcquaintance(1);
        	 app.setMapping(this.genMapping4());
          	 break;
         	 
         default:  
         	 break;     
	     }

		 
		 /*		 
		 
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
		 
*/		 
		 
		 
	     if (nodeSeq == 0){
	    	 System.out.println("this is the starting nodes. " +
	    	 		"all other nodes ready? (y for yes)");
	    	 char in = (char)System.in.read();
	    	 while (in != 'y'){
	    		 System.out.println("all other nodes ready? (y for yes)");
		    	 in = (char)System.in.read(); 
	    	 }
	    	 app.setAcq_handle();
	    	 
	    	 app.sendBroadCastingMsg(app.getNode().getLocalNodeHandle());
	      }
	     else{
	    	 System.out.println("all other nodes ready? (y for yes)");
	    	 char in = (char)System.in.read();
	    	 while(in != 'y'){
	    		 System.out.println("all other nodes ready? (y for yes)");
		    	 in = (char)System.in.read();
	    	 }
	    	 app.setAcq_handle();
	    	 
	     }
	     
		  
		   
	  }
	 
	 
	  public void readInFiles() throws IOException, ClassNotFoundException{
		  
			FileInputStream fis_schemas = new FileInputStream("C:\\Temp\\jzhao\\Project\\PDMSwithSchemaMediation\\pdms\\schemas.log");
			ObjectInputStream ois_schemas = new ObjectInputStream(fis_schemas);
			
//			int size = ois_schemas.readInt();
			for (int i = 0; i < 10; i++){
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
			
			FileInputStream fis_mappings = new FileInputStream("C:\\Temp\\jzhao\\Project\\PDMSwithSchemaMediation\\pdms\\mappings.log");
			ObjectInputStream ois_mappings = new ObjectInputStream(fis_mappings);

			int size = ois_mappings.readInt();
					
			
			
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
			
			FileInputStream fis_topology = new FileInputStream("C:\\Temp\\jzhao\\Project\\PDMSwithSchemaMediation\\pdms\\topology.log");
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

	  

	  public void buildNodeHash(){
/*		  
		  for (int i = 1; i < 10; i++){
			  nodeHash.put(new Integer(i), "hal.cs.ubc.ca: 900" + i);
		  }
		  nodeHash.put(new Integer(10), "hal.cs.ubc.ca: 9010");
*/
		  
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
/*
	 public void buildNodeHash(){
		  nodeHash.put(new Integer(1), "128.189.142.240: 9001");
		  nodeHash.put(new Integer(2), "128.189.142.240: 9002");
		  nodeHash.put(new Integer(3), "128.189.142.240: 9003");
		  nodeHash.put(new Integer(4), "128.189.142.240: 9004");
		  nodeHash.put(new Integer(5), "128.189.142.240: 9005");		 
	  }
*/	  

		  
	 
	  
	  public Schema generateSchema1(){
		  Schema s1 = new Schema();
		  s1.setName("UBC");
		  s1.addRelation("UBC.conf-paper(title, venue, year, pages)");
		  s1.addRelation("UBC.univ_proj(univ, project)");
		  s1.addRelation("UBC.proj_area(project, area)");
		  return s1;
	  }
	  
	  public Schema generateSchema2(){
		  Schema s2 = new Schema();
		  s2.setName("UW");
		  s2.addRelation("UW.conf-paper(title, venue, year, url)");
		  s2.addRelation("UW.project(univ, project)");
		  return s2;
	  }
	  
	  public Schema generateSchema3(){
		  Schema s3 = new Schema();
		  s3.setName("UT");
		  s3.addRelation("UT.conference-paper(venue, title, first-author)");
		  s3.addRelation("UT.professionals(area, prof)");
	      return s3;
	  }
	  
	  public Schema generateSchema4(){
		  Schema s4 = new Schema();
		  s4.setName("UCB");
		  s4.addRelation("UCB.conf-paper(title, location, abstract)");
		  s4.addRelation("UCB.scientist(name, area)");
	      return s4;
	  }
	  
	  public Schema generateSchema5(){
		  Schema s5 = new Schema();
		  s5.setName("UV");
		  s5.addRelation("UV.conf-paper(title, venue, sub-area)");
	      return s5;
	  }
	  
	  public Mapping genMapping1(){
		  Mapping map1 = new Mapping();
		  View ms1 = new View("conf-paper(title, venue, year):-" + 
				  "UBC.conf-paper(title, venue, year, pages)");
	      View ms2 = new View("conf-paper(title, venue, year):-" +
	    		  "UW.conf-paper(title, venue, year, url)");
	      map1.addView(ms1);
	      map1.addView(ms2);

	      ms1 = new View("project(univ,project):-" +
	    		  "UBC.univ_proj(univ, project)," + 
	    		  "UBC.proj_area(project, area)");
	      ms2 = new View("project(univ,project):-" +
	    		  "UW.project(univ, project)");
	      map1.addView(ms1);
	      map1.addView(ms2);
	      
	      map1.addSchema(generateSchema1());
	      map1.addSchema(generateSchema2());
	      return map1;
	  }

	  public Mapping genMapping2(){
		  Mapping map2 = new Mapping();
		  View ms1 = new View("conf-paper(title, venue):-" + 
				  "UBC.conf-paper(title, venue, year, pages)");
		  View ms2 = new View("conf-paper(title, venue):-" +
				  "UT.conference-paper(venue, title, first-author)");
		  map2.addView(ms1);
		  map2.addView(ms2);
		  map2.addSchema(generateSchema1());
		  map2.addSchema(generateSchema3());
		  return map2;
	  }
	  
	  
	  public Mapping genMapping3(){
		  Mapping map3 = new Mapping();
/*
		  View ms1 = new View("conf-paper(title, venue):-" +
				  "UCB.conf-paper(title, venue, abstract)");
		  View ms2 = new View("conf-paper(title, venue):-" +
			      "UT.conference-paper(venue, title, first-author)");
*/
		  View ms1 = new View ("scientist(area, name):- " +
		  		"UT.professionals(area, name)");
		  View ms2 = new View ("scientist(area, name):-" +
		  		"UCB.scientist(name, area)");
		  
		  map3.addView(ms1);
		  map3.addView(ms2);
		  map3.addSchema(generateSchema3());
		  map3.addSchema(generateSchema4());
		  return map3;
	  }
	  
	  public Mapping genMapping4(){
		  Mapping map4 = new Mapping();
		  View ms1 = new View("conf-paper(title, venue):-" +
				  "UW.conf-paper(title, venue, year, url)");
		  View ms2 = new View("conf-paper(title, venue):-" +
			      "UV.conf-paper(title, venue, sub-area)");
		  map4.addView(ms1);
		  map4.addView(ms2);
		  map4.addSchema(this.generateSchema2());
		  map4.addSchema(this.generateSchema5());
		  return map4;
	  }

	  


	  /**
	   * Usage: HelloWorld [-msgs m] [-nodes n] [-verbose|-silent|-verbosity v]
	   * [-simultaneous_joins] [-simultaneous_msgs] [-help]
	   *
	   * @param args DESCRIBE THE PARAMETER
	   */
	  public static void main(String args[]) throws Exception {
	    
	    Environment env = new Environment();

	    try {
	      // the port to use locally
	      int bindport = Integer.parseInt(args[0]);

	      // build the bootaddress from the command line args
	      InetAddress bootaddr = InetAddress.getByName(args[1]);
	      int bootport = Integer.parseInt(args[2]);
	      InetSocketAddress bootaddress = new InetSocketAddress(bootaddr, bootport);

	      int seq = Integer.parseInt(args[3]);
	      
	      // launch our node!
	      SchemaMediationInPdmsExp1 driver = new SchemaMediationInPdmsExp1(bindport, bootaddress, 
	    		  seq, env);
     
      
	    } catch (Exception e) {
	        throw e;
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



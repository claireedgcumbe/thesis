Package dataGenerators:

- RandomConceptGenerator.
  Input: number of schemas, number of concepts to be generated, a parameter to tune the possibility of each peer to have that concept. 

- RandomSchemaGenerator.
  Generate schemas; two parametors are specified in the constructor: numOfRelation and numOfAvgAttr.

- AcquaintanceGenerator.
  Generate potential acquaintances for each peer based on the concept matrix.  The concept matrix can be obtained by running RandomConceptGenerator.

- TopologyGenerator.
  Generate a topology based on the results from AcquaitanceGenerator.  Input: number of acquaintance for each peer.

- RandomMappingGenerator.
  This is the major class for the whole package.  This class randomly generate the mappings based on the concept matrix obtained from RandomConceptGenerator.  Then this class generate all schemas, topology and mappings for the system.  results will be written into three different files: schemas.log, mappings.log and topology.log.  For ease of processing, these files are written as an object.

Things to tune in this package to generate mappings:
1) RandomConceptGenerator: after the concept is generated, we might need to change this matrix
2) TopologyGenerator public int remainingEdge(), remaining = acqNum * numOfPeers - calculateEdge(). 
   We can add some more values to "remaining" when we want to add more edges.  e.g. the above equation can be changed to remaining = acqNum * numOfPeers - calculateEdge() + 2; 
   This is because if we don't do this, the generated topology are not satisfiable and cannot get our desired hops. 
3) may need to run several times of RandomMappingGenerator to get a good topology sample


Package pdms:

- CountHopFrom0.
  After running RandomConceptGenerator, run CounHopFrom0, which will return the hop for each node from the starting node

- LongestpathCounter
  After running RandomConceptGenerator, run LongestpathCounter, which will return the Topology Depth for the whole topology

- SchemaMediationInPdms
  This will keep track of the peer application.  Each node run this process in order to start communication between acquaintances and do the setup stuff.

- PdmsApp
  This is the application run on each peer.  The major function is public void deliver(Id id, Message msg), which receives a message from others and process schema mediation accordingly.

- QueryReformation
  Reformulate queries based on the given mediated schema, mappings to local sources (which are specified in result file: finalResult.log) and the given query.  

- ReadMediationResultFromFile
  print all information in finalResult.log.  finalResult.log contains the mediated schema, mappings, written as objects and not readable.

- QueryInPdms
  Same usage as SchemaMediationInPdms.  But this class is used to manage the experiment of query translation among different peers.

- QueryApp
  Same usage as PdmsApp.  This application is used to translate queries for local peer. 

- NewSchemaJoinInPdms
  Same usage as SchemaMediationInPdms.  This is used for the experiment of new peer entering the topology.

- NewSchemaJoinApp
  Same usage as PdmsApp.  This application is used to do the schema mediation or updating stuff at each local peer.

- TimeCounterForExp1
  This class reads all the files resulted from the experiment 1 (schema mediation setting up) and caculate the final time result for each experiment.

- TimeCounterForExp2
  This class reads all the files resulted from the experiment 2 (query reformulation) and caculate the final time result for each experiment.

- TimeCounterForExp3
  This class reads all the files resulted from the experiment 3 (new peer joining) and caculate the final time result for each experiment.


Package minicon

Package deepcopy: open source from the web. which implemented deep copy function for an object

Package mediation:
- GLAVMapping
  The mapping from mediated schema to a local source.  Each peer has a GLAVMapping.

- MappingTable
  implemented all the functions related to a MappingTable such as create MappingTable, merge MappingTable, etc

- SchemaMediation
  A schema mediation algorithm for the data integration system using PSM algorithm (which is based on the use of MappingTable)

- Package rice:
  Obtained from FreePastry, which provides the P2P network layer.


/*************************************************************************

"FreePastry" Peer-to-Peer Application Development Substrate 

Copyright 2002, Rice University. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

- Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.

- Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

- Neither  the name  of Rice  University (RICE) nor  the names  of its
contributors may be  used to endorse or promote  products derived from
this software without specific prior written permission.

This software is provided by RICE and the contributors on an "as is"
basis, without any representations or warranties of any kind, express
or implied including, but not limited to, representations or
warranties of non-infringement, merchantability or fitness for a
particular purpose. In no event shall RICE or contributors be liable
for any direct, indirect, incidental, special, exemplary, or
consequential damages (including, but not limited to, procurement of
substitute goods or services; loss of use, data, or profits; or
business interruption) however caused and on any theory of liability,
whether in contract, strict liability, or tort (including negligence
or otherwise) arising in any way out of the use of this software, even
if advised of the possibility of such damage.

********************************************************************************/
package rice.p2p.replication.manager.testing;

import java.io.IOException;

import rice.Continuation;
import rice.environment.Environment;
import rice.environment.params.Parameters;
import rice.environment.params.simple.SimpleParameters;
import rice.environment.time.simulated.DirectTimeSource;
import rice.p2p.commonapi.*;
import rice.p2p.commonapi.testing.CommonAPITest;
import rice.p2p.replication.manager.*;

/**
 * @(#) ReplicationRegrTest.java Provides regression testing for the replication
 * manager service using distributed nodes.
 *
 * @version $Id: ReplicationManagerRegrTest.java 3040 2006-02-07 12:57:36Z jeffh
 *      $
 * @author Alan Mislove
 */
public class ReplicationManagerRegrTest extends CommonAPITest {

  /**
   * the replication impls in the ring
   */
  protected ReplicationManagerImpl[] replications;

  /**
   * The clients
   */
  protected TestReplicationManagerClient[] clients;

  /**
   * The replication factor to use
   */
  public static int REPLICATION_FACTOR = 3;

  /**
   * the instance name to use
   */
  public static String INSTANCE = "ReplicationRegrTest";

  /**
   * Constructor which sets up all local variables
   *
   * @param env DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public ReplicationManagerRegrTest(Environment env) throws IOException {
    super(env);
    replications = new ReplicationManagerImpl[NUM_NODES];
    clients = new TestReplicationManagerClient[NUM_NODES];
  }

  /**
   * Method which should process the given newly-created node
   *
   * @param node The newly created node
   * @param num The number of this node
   */
  protected void processNode(int num, Node node) {
    clients[num] = new TestReplicationManagerClient(node);
    replications[num] = new ReplicationManagerImpl(node, clients[num], REPLICATION_FACTOR, INSTANCE);
  }

  /**
   * Method which should run the test - this is called once all of the nodes
   * have been created and are ready.
   */
  protected void runTest() {
    for (int i = 0; i < NUM_NODES; i++) {
      simulate();
    }

    testBasic();
    testOverload();
    testStress();
    testMaintenance();
  }

  /*
   *  ---------- Test methods and classes ----------
   */
  /**
   * Tests basic functionality
   */
  public void testBasic() {
    int num = environment.getRandomSource().nextInt(NUM_NODES);
    Id id = nodes[num].getId();

    IdRange all = FACTORY.buildIdRange(FACTORY.buildId(new byte[20]), FACTORY.buildId(new byte[20]));

    sectionStart("Testing Basic Functionality");

    stepStart("Inserting Object");

    clients[num].insert(id);

    stepDone(SUCCESS);

    stepStart("Initiating Maintenance");

    runMaintenance();

    for (int i = 0; i < NUM_NODES; i++) {
      simulate();
    }

    int count = 0;

    for (int i = 0; i < NUM_NODES; i++) {
      if (clients[i].scan(all).isMemberId(id)) {
        count++;
      }
    }

    assertTrue("Correct number of replicas should be " + (REPLICATION_FACTOR + 1) + " was " + count,
      count == REPLICATION_FACTOR + 1);

    stepDone(SUCCESS);

    sectionDone();
  }

  /**
   * Tests maintenance functionality
   */
  public void testMaintenance() {
    int num = environment.getRandomSource().nextInt(NUM_NODES);
    Id id = nodes[num].getId();

    IdRange all = FACTORY.buildIdRange(FACTORY.buildId(new byte[20]), FACTORY.buildId(new byte[20]));

    sectionStart("Testing Maintenance Functionality");

    stepStart("Inserting Object");

    clients[num].insert(id);

    stepDone(SUCCESS);

    stepStart("Initiating Maintenance");

    runMaintenance();
    simulate();

    int count = 0;

    for (int i = 0; i < NUM_NODES; i++) {
      if (clients[i].scan(all).isMemberId(id)) {
        count++;
      }
    }

    assertTrue("Correct number of replicas should be " + (REPLICATION_FACTOR + 1) + " was " + count,
      count == REPLICATION_FACTOR + 1);

    stepDone(SUCCESS);

    stepStart("Killing Primary Replica");

    kill(num);

    stepDone(SUCCESS);

    stepStart("Initiating Maintenance");

    runMaintenance();
    simulate();

    count = 0;

    for (int i = 0; i < NUM_NODES; i++) {
      if (clients[i].scan(all).isMemberId(id)) {
        count++;
      }
    }

    assertTrue("Correct number of replicas should be " + (REPLICATION_FACTOR + 2) + " was " + count,
      count == REPLICATION_FACTOR + 2);

    stepDone(SUCCESS);

    sectionDone();
  }

  /**
   * Tests basic functionality
   */
  public void testOverload() {
    int NUM_TO_INSERT = 16;
    int num = environment.getRandomSource().nextInt(NUM_NODES);
    Id id = nodes[num].getId();

    IdRange all = FACTORY.buildIdRange(FACTORY.buildId(new byte[20]), FACTORY.buildId(new byte[20]));

    sectionStart("Testing Overload Functionality");

    stepStart("Inserting " + NUM_TO_INSERT + " Objects");

    for (int i = 0; i < NUM_TO_INSERT; i++) {
      clients[num].insert(addToId(id, i));
      simulate();
    }

    stepDone(SUCCESS);

    stepStart("Initiating Maintenance");

    runMaintenance();
    simulate();

    for (int i = 0; i < NUM_TO_INSERT + 1; i++) {
      try {
        Thread.sleep(replications[0].FETCH_DELAY);
      } catch (InterruptedException e) {
        System.out.println(e.toString());
      }

      simulate();
    }

    for (int j = 0; j < NUM_TO_INSERT; j++) {
      int count = 0;

      Id thisId = addToId(id, j);

      for (int i = 0; i < NUM_NODES; i++) {
        if (clients[i].scan(all).isMemberId(thisId)) {
          count++;
        }
      }

      assertTrue("Correct number of replicas for " + thisId + " should be " + (REPLICATION_FACTOR + 1) + " was " + count,
        count == REPLICATION_FACTOR + 1);
    }

    stepDone(SUCCESS);

    sectionDone();
  }

  /**
   * Tests basic functionality
   */
  public void testStress() {
    int NUM_TO_INSERT = 45;
    Id[] ids = new Id[NUM_TO_INSERT];
    int num = environment.getRandomSource().nextInt(NUM_NODES);
    Id id = nodes[num].getId();

    IdRange all = FACTORY.buildIdRange(FACTORY.buildId(new byte[20]), FACTORY.buildId(new byte[20]));

    sectionStart("Testing Stressed Functionality");

    stepStart("Inserting " + NUM_TO_INSERT + " Objects");

    for (int i = 0; i < NUM_TO_INSERT; i++) {
      ids[i] = addToId(id, i);
      clients[num].insert(ids[i]);
    }

    stepDone(SUCCESS);

    stepStart("Initiating Maintenance");

    runMaintenance();
    simulate();

    try {
      Thread.sleep(25000);
    } catch (InterruptedException e) {
      System.out.println(e.toString());
    }

    simulate();

    for (int j = 0; j < NUM_TO_INSERT; j++) {
      int count = 0;

      Id thisId = ids[j];

      for (int i = 0; i < NUM_NODES; i++) {
        if (clients[i].scan(all).isMemberId(thisId)) {
          count++;
        }
      }

      assertTrue("Correct number of replicas for " + j + " " + thisId + " should be " + (REPLICATION_FACTOR + 1) + " was " + count,
        count == REPLICATION_FACTOR + 1);
    }

    stepDone(SUCCESS);

    sectionDone();
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void runMaintenance() {
    for (int i = 0; i < NUM_NODES; i++) {
      replications[i].getReplication().replicate();
    }

    simulate();
  }

  /**
   * Private method which generates a random Id
   *
   * @return A new random Id
   */
  private Id generateId() {
    byte[] data = new byte[20];
    environment.getRandomSource().nextBytes(data);
    return FACTORY.buildId(data);
  }

  /**
   * Adds a feature to the ToId attribute of the ReplicationManagerRegrTest
   * object
   *
   * @param id The feature to be added to the ToId attribute
   * @param num The feature to be added to the ToId attribute
   * @return DESCRIBE THE RETURN VALUE
   */
  private Id addToId(Id id, int num) {
    byte[] bytes = id.toByteArray();
    bytes[0] += num;

    return FACTORY.buildId(bytes);
  }


  /**
   * Usage: ReplicationRegrTest [-port p] [-bootstrap host[:port]] [-nodes n]
   * [-protocol (rmi|wire)] [-help]
   *
   * @param args DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public static void main(String args[]) throws IOException {
    Environment env = parseArgs(args);
    Parameters param = env.getParameters();
//    param.setString("loglevel","ALL");
//    param.setBoolean("environment_logToFile",true);
    param.setString("fileLogManager_filePrefix", "retest_");
    param.setString("fileLogManager_fileSuffix", ".log");
    ReplicationManagerRegrTest test = new ReplicationManagerRegrTest(env);
    test.start();
    env.destroy();
  }

  /**
   * DESCRIBE THE CLASS
   *
   * @version $Id: ReplicationManagerRegrTest.java 3040 2006-02-07 12:57:36Z
   *      jeffh $
   * @author amislove
   */
  protected class TestReplicationManagerClient implements ReplicationManagerClient {

    /**
     * DESCRIBE THE FIELD
     */
    public Node node;

    /**
     * DESCRIBE THE FIELD
     */
    public IdSet set;

    /**
     * Constructor for TestReplicationManagerClient.
     *
     * @param node DESCRIBE THE PARAMETER
     */
    public TestReplicationManagerClient(Node node) {
      this.set = node.getIdFactory().buildIdSet();
      this.node = node;
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param id DESCRIBE THE PARAMETER
     * @param hint DESCRIBE THE PARAMETER
     * @param command DESCRIBE THE PARAMETER
     */
    public void fetch(Id id, NodeHandle hint, Continuation command) {
      set.addId(id);
      command.receiveResult(new Boolean(true));
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param id DESCRIBE THE PARAMETER
     * @param command DESCRIBE THE PARAMETER
     */
    public void remove(Id id, Continuation command) {
      set.removeId(id);
      command.receiveResult(new Boolean(true));
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param range DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public IdSet scan(IdRange range) {
      return set.subSet(range);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param id DESCRIBE THE PARAMETER
     */
    public void insert(Id id) {
      set.addId(id);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param id DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public boolean exists(Id id) {
      return set.isMemberId(id);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param id DESCRIBE THE PARAMETER
     * @param command DESCRIBE THE PARAMETER
     */
    public void existsInOverlay(Id id, Continuation command) {
      // XXX we don't test this new functionality yet
      command.receiveResult(Boolean.TRUE);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param id DESCRIBE THE PARAMETER
     * @param command DESCRIBE THE PARAMETER
     */
    public void reInsert(Id id, Continuation command) {
      // XXX we don't test this new functionality yet
      command.receiveResult(Boolean.TRUE);
    }
  }
}








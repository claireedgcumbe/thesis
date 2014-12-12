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
package rice.pastry.testing;

import rice.environment.Environment;
import rice.environment.params.simple.SimpleParameters;
import rice.environment.time.simulated.DirectTimeSource;
import rice.pastry.*;
import rice.pastry.direct.*;
import rice.pastry.standard.*;
import rice.pastry.join.*;
import rice.pastry.client.*;
import rice.pastry.messaging.*;
import rice.pastry.security.*;
import rice.pastry.routing.*;
import rice.pastry.leafset.*;

import java.io.IOException;
import java.util.*;

/**
 * PastryRegrTest a regression test suite for pastry.
 *
 * @version $Id: DirectPastryRegrTest.java 3048 2006-02-10 16:25:33Z jeffh $
 * @author andrew ladd
 * @author peter druschel
 * @author sitaram iyer
 */

public class DirectPastryRegrTest extends PastryRegrTest {
  private NetworkSimulator simulator;

  /**
   * constructor
   *
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  private DirectPastryRegrTest() throws IOException {
    super(Environment.directEnvironment());
    simulator = new SphereNetwork(environment);
    factory = new DirectPastryNodeFactory(new RandomNodeIdFactory(environment),
      simulator,
      environment);
  }

  /**
   * Get pastryNodes.last() to bootstrap with, or return null.
   *
   * @param firstNode DESCRIBE THE PARAMETER
   * @return The Bootstrap value
   */
  protected NodeHandle getBootstrap(boolean firstNode) {
    NodeHandle bootstrap = null;
    try {
      PastryNode lastnode = (PastryNode) pastryNodes.lastElement();
      bootstrap = lastnode.getLocalHandle();
    } catch (NoSuchElementException e) {
    }
    return bootstrap;
  }

  /**
   * get authoritative information about liveness of node.
   *
   * @param nh DESCRIBE THE PARAMETER
   * @return The ReallyAlive value
   */
  protected boolean isReallyAlive(NodeHandle nh) {
    return simulator.isAlive((DirectNodeHandle) nh);
  }

  /**
   * wire protocol specific handling of the application object e.g., RMI may
   * launch a new thread
   *
   * @param pn pastry node
   * @param app newly created application
   */
  protected void registerapp(PastryNode pn, RegrTestApp app) {
  }

  /**
   * send one simulated message
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  protected boolean simulate() {
    try {
      Thread.sleep(300);
    } catch (InterruptedException ie) {
    }
    return false;
//    boolean res = simulator.simulate();
//    if (res)
//      msgCount++;
//    return res;
  }

  // do nothing in the simulated world
  /**
   * DESCRIBE THE METHOD
   *
   * @param ms DESCRIBE THE PARAMETER
   */
  public void pause(int ms) {
  }

  /**
   * murder the node. comprehensively.
   *
   * @param pn DESCRIBE THE PARAMETER
   */
  protected void killNode(PastryNode pn) {
    pn.destroy();
//    NetworkSimulator enet = (NetworkSimulator) simulator;
//    enet.setAlive(pn.getNodeId(), false);
  }

  /**
   * main. just create the object and call PastryNode's main.
   *
   * @param args DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public static void main(String args[]) throws IOException {
    DirectPastryRegrTest pt = new DirectPastryRegrTest();
    mainfunc(pt, args, 500
    /*
     *  n
     */
      , 100
    /*
     *  d
     */
      , 10
    /*
     *  k
     */
      , 100
    /*
     *  m
     */
      , 1
    /*
     *  conc
     */
      );
  }
}


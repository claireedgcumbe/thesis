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

package rice.pastry.direct;

import java.util.*;

import rice.environment.logging.Logger;
import rice.pastry.*;
import rice.pastry.messaging.*;

/**
 * the node handle used with the direct network
 *
 * @version $Id: DirectNodeHandle.java 3038 2006-02-07 10:01:01Z jeffh $
 * @author Andrew Ladd
 * @author Rongmei Zhang/Y. Charlie Hu
 */

public class DirectNodeHandle extends NodeHandle implements Observer {
  private DirectPastryNode remoteNode;
  /**
   * DESCRIBE THE FIELD
   */
  public NetworkSimulator simulator;
  /**
   * DESCRIBE THE FIELD
   */
  protected Logger logger;

  /**
   * Constructor for DirectNodeHandle.
   *
   * @param ln The local pastry node
   * @param rn The remote pastry node
   * @param sim The current network simulator
   */
  DirectNodeHandle(DirectPastryNode ln, DirectPastryNode rn, NetworkSimulator sim) {
    localnode = ln;
    logger = ln.getEnvironment().getLogManager().getLogger(getClass(), null);
    if (rn == null) {
      throw new IllegalArgumentException("rn must be non-null");
    }
    remoteNode = rn;
    simulator = sim;

    rn.addObserver(this);
  }

  /**
   * Gets the Remote attribute of the DirectNodeHandle object
   *
   * @return The Remote value
   */
  public DirectPastryNode getRemote() {
    return remoteNode;
  }

  /**
   * Gets the NodeId attribute of the DirectNodeHandle object
   *
   * @return The NodeId value
   */
  public NodeId getNodeId() {
    return remoteNode.getNodeId();
  }

  /**
   * Gets the Alive attribute of the DirectNodeHandle object
   *
   * @return The Alive value
   */
  public int getLiveness() {
    if (remoteNode.isAlive()) {
      return LIVENESS_ALIVE;
    }
    return LIVENESS_DEAD;
  }

  /**
   * Gets the Simulator attribute of the DirectNodeHandle object
   *
   * @return The Simulator value
   */
  public NetworkSimulator getSimulator() {
    return simulator;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param arg DESCRIBE THE PARAMETER
   */
  public void notifyObservers(Object arg) {
    setChanged();
    super.notifyObservers(arg);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean ping() {
    return isAlive();
  }

  /**
   * DESCRIBE THE METHOD
   */
  public final void assertLocalNode() {
    if (DirectPastryNode.currentNode == null) {
//      ctor.printStackTrace();
      throw new RuntimeException("PANIC: localnode is null in " + this + "@" + System.identityHashCode(this));
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public int proximity() {
    assertLocalNode();
    if (!simulator.getEnvironment().getSelectorManager().isSelectorThread()) {
      throw new RuntimeException("Must be called on selector thread.");
    }
    int result = simulator.proximity((DirectNodeHandle) DirectPastryNode.currentNode.getLocalHandle(), this);

    return result;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   */
  public void receiveMessage(Message msg) {
    // shortcut if called on the local node
    if (simulator.getEnvironment().getSelectorManager().isSelectorThread() &&
      (remoteNode == DirectPastryNode.currentNode)) {
      remoteNode.receiveMessage(msg);
      return;
    }

    if (!remoteNode.isAlive()) {
      if (logger.level <= Logger.WARNING) {
        logger.log(
          "DirectNodeHandle: attempt to send message " + msg + " to a dead node " + getNodeId() + "!");
      }
    } else {
      simulator.deliverMessage(msg, remoteNode);
    }
  }

  /**
   * Equivalence relation for nodehandles. They are equal if and only if their
   * corresponding NodeIds are equal.
   *
   * @param obj the other nodehandle .
   * @return true if they are equal, false otherwise.
   */
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    NodeHandle nh = (NodeHandle) obj;

    if (this.getNodeId().equals(nh.getNodeId())) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Hash codes for node handles.It is the hashcode of their corresponding
   * NodeId's.
   *
   * @return a hash code.
   */
  public int hashCode() {
    return this.getNodeId().hashCode();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "[DNH " + getNodeId() + "]";
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param arg0 DESCRIBE THE PARAMETER
   * @param arg1 DESCRIBE THE PARAMETER
   */
  public void update(Observable arg0, Object arg1) {
    if (remoteNode.alive) {
      notifyObservers(NodeHandle.DECLARED_LIVE);
    } else {
      notifyObservers(NodeHandle.DECLARED_DEAD);
    }
  }
}

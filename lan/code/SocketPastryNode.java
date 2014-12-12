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
package rice.pastry.socket;

import java.io.IOException;

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.pastry.*;
import rice.pastry.dist.DistPastryNode;
import rice.pastry.messaging.Message;

/**
 * An Socket-based Pastry node, which has two threads - one thread for
 * performing route set and leaf set maintainance, and another thread for
 * listening on the sockets and performing all non-blocking I/O.
 *
 * @version $Id: SocketPastryNode.java 2969 2006-01-30 15:23:43Z jeffh $
 * @author Alan Mislove
 */
public class SocketPastryNode extends DistPastryNode {

  // The address (ip + port) of this pastry node
  private EpochInetSocketAddress address;

  // The SocketManager, controlling the sockets
  SocketSourceRouteManager srManager;

  /**
   * Constructor
   *
   * @param id The NodeId of this Pastry node.
   * @param e DESCRIBE THE PARAMETER
   */
  public SocketPastryNode(NodeId id, Environment e) {
    super(id, e);
  }

  /**
   * Returns the SocketSourceRouteManager for this pastry node.
   *
   * @return The SocketSourceRouteManager for this pastry node.
   */
  public SocketSourceRouteManager getSocketSourceRouteManager() {
    return srManager;
  }

  /**
   * Helper method which allows the WirePastryNodeFactory to initialize a number
   * of the pastry node's elements.
   *
   * @param address The address of this pastry node.
   * @param lsmf Leaf set maintenance frequency. 0 means never.
   * @param rsmf Route set maintenance frequency. 0 means never.
   */
  public void setSocketElements(EpochInetSocketAddress address,
                                int lsmf, int rsmf) {
    this.address = address;
    this.leafSetMaintFreq = lsmf;
    this.routeSetMaintFreq = rsmf;
  }

  /**
   * Sets the SocketSourceRouteManager attribute of the SocketPastryNode object
   *
   * @param srManager The new SocketSourceRouteManager value
   */
  public void setSocketSourceRouteManager(SocketSourceRouteManager srManager) {
    this.srManager = srManager;
  }

  /**
   * Called after the node is initialized.
   *
   * @param bootstrap The node which this node should boot off of.
   */
  public void doneNode(NodeHandle bootstrap) {
    super.doneNode(bootstrap);
    initiateJoin(bootstrap);
  }

  /**
   * Prints out a String representation of this node
   *
   * @return a String
   */
  public String toString() {
    return "SocketNodeHandle (" + getNodeId() + "/" + address + ")";
  }

  /**
   * Makes this node resign from the network. Is designed to be used for
   * debugging and testing. If run on the SelectorThread, then destroys now.
   * Other threads cause a task to be placed on the selector, and destroyed
   * asap. Make sure to call super.destroy() !!!
   */
  public void destroy() {
    super.destroy();
    if (getEnvironment().getSelectorManager().isSelectorThread()) {
      // destroy now
      try {
        super.destroy();
        srManager.destroy();
      } catch (IOException e) {
        getEnvironment().getLogManager().getLogger(SocketPastryNode.class,
          "ERROR: Got exception " + e + " while resigning node!");
      }
    } else {
      // schedule to be destroyed on the selector
      getEnvironment().getSelectorManager().invoke(
        new Runnable() {
          public void run() {
            destroy();
          }
        });
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param newHandle DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public NodeHandle coalesce(NodeHandle newHandle) {
    return srManager.coalesce(newHandle);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param handle DESCRIBE THE PARAMETER
   * @param message DESCRIBE THE PARAMETER
   */
  public void send(NodeHandle handle, Message message) {
    SocketNodeHandle snh = (SocketNodeHandle) handle;
    if (getNodeId().equals(snh.getId())) {
      //debug("Sending message " + msg + " locally");
      receiveMessage(message);
    } else {
      if (logger.level <= Logger.FINER) {
        logger.log(
          "Passing message " + message + " to the socket controller for writing");
      }
      getSocketSourceRouteManager().send(snh.getEpochAddress(), message);
    }

  }

}

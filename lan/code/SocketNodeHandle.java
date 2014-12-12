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

import java.net.InetSocketAddress;
import java.util.*;

import rice.environment.logging.Logger;
import rice.pastry.NodeId;
import rice.pastry.dist.DistNodeHandle;
import rice.pastry.messaging.Message;
import rice.pastry.socket.SocketSourceRouteManager.AddressManager;

/**
 * Class which represents the address and nodeId of a remote node. In the socket
 * protocol, it simply represents this information - all other details are
 * managed by the local nodes. SocketNodeHandle can now internally exist without
 * a NodeId. This is to get the memory management correct
 *
 * @version $Id: SocketNodeHandle.java 3197 2006-04-11 12:30:58Z jeffh $
 * @author Alan Mislove
 */
public class SocketNodeHandle extends DistNodeHandle {

  /**
   * DESCRIBE THE FIELD
   */
  protected EpochInetSocketAddress eaddress;

  transient AddressManager addressManager;

  final static long serialVersionUID = -5452528188786429274L;

  // a special liveness value that indicates that this node will never come alive again
  /**
   * DESCRIBE THE FIELD
   */
  public final static int LIVENESS_DEAD_FOREVER = 4;

  // the default distance, which is used before a ping
  /**
   * DESCRIBE THE FIELD
   */
  public static int DEFAULT_PROXIMITY = Integer.MAX_VALUE;

  /**
   * Constructor
   *
   * @param nodeId This node handle's node Id.
   * @param address DESCRIBE THE PARAMETER
   */
  public SocketNodeHandle(EpochInetSocketAddress address, NodeId nodeId) {
    super(nodeId, address.getAddress());

    this.eaddress = address;
  }

  /**
   * Gets the EpochAddress attribute of the SocketNodeHandle object
   *
   * @return The EpochAddress value
   */
  public EpochInetSocketAddress getEpochAddress() {
    return eaddress;
  }

  /**
   * Returns the last known liveness information about the Pastry node
   * associated with this handle. Invoking this method does not cause network
   * activity.
   *
   * @return true if the node is alive, false otherwise.
   */
  public int getLiveness() {
    SocketPastryNode spn = (SocketPastryNode) getLocalNode();

    if (spn == null || getLocalNode().getLocalHandle() == null) {
      return LIVENESS_ALIVE;
    } else {
      if (isLocal()) {
        return LIVENESS_ALIVE;
      } else {
        // make sure this isn't an old existance of ourself:
        EpochInetSocketAddress localEaddr = ((SocketNodeHandle) getLocalNode().getLocalHandle()).getEpochAddress();
        InetSocketAddress localAddress = localEaddr.getAddress();
        InetSocketAddress myAddress = getEpochAddress().getAddress();
        if (localAddress.equals(myAddress) && (!getEpochAddress().equals(localEaddr))) {
          return LIVENESS_DEAD_FOREVER;
        }

        return spn.getSocketSourceRouteManager().getLiveness(getEpochAddress());
      }
    }
  }

  /**
   * Method which returns whether or not this node handle is on its home node.
   *
   * @return Whether or not this handle is local
   */
  public boolean isLocal() {
    assertLocalNode();
    return getLocalNode().getLocalHandle().equals(this);
  }

  /**
   * Sets the NodeId attribute of the SocketNodeHandle object
   *
   * @param nodeId The new NodeId value
   */
  public void setNodeId(NodeId nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Sets the LocalNode attribute of the SocketNodeHandle object
   *
   * @param spn The new LocalNode value
   */
  public void setLocalNode(SocketPastryNode spn) {
    localnode = spn;
    this.logger = spn.getEnvironment().getLogManager().getLogger(getClass(), null);
  }

  /**
   * Method which FORCES a check of liveness of the remote node. Note that this
   * method should ONLY be called by internal Pastry maintenance algorithms -
   * this is NOT to be used by applications. Doing so will likely cause a blowup
   * of liveness traffic.
   *
   * @return true if node is currently alive.
   */
  public boolean checkLiveness() {
    SocketPastryNode spn = (SocketPastryNode) getLocalNode();

    if (spn != null) {
      spn.getSocketSourceRouteManager().checkLiveness(getEpochAddress());
    }

    return isAlive();
  }

  /**
   * Called to send a message to the node corresponding to this handle.
   *
   * @param msg Message to be delivered, may or may not be routeMessage.
   */
  public void receiveMessage(final Message msg) {
    assertLocalNode();

    final SocketPastryNode spn = (SocketPastryNode) getLocalNode();

//    Runnable runnable = new Runnable() {
//      public void run() {
    if (spn.getNodeId().equals(nodeId)) {
      //debug("Sending message " + msg + " locally");
      spn.receiveMessage(msg);
    } else {
      if (logger.level <= Logger.FINER) {
        logger.log(
          "Passing message " + msg + " to the socket controller for writing");
      }
      spn.getSocketSourceRouteManager().send(getEpochAddress(), msg);
    }
//      }
//    };
//
//    SelectorManager sm = spn.getEnvironment().getSelectorManager();
//    if (sm.isSelectorThread()) {
//      runnable.run();
//    } else {
//      sm.invoke(runnable);
//    }
  }

  /**
   * Method which is used by Pastry to start the bootstrapping process on the
   * local node using this handle as the bootstrap handle. Default behavior is
   * simply to call receiveMessage(msg), but transport layer implementations may
   * care to perform other tasks by overriding this method, since the node is
   * not technically part of the ring yet.
   *
   * @param msg the bootstrap message.
   */
  public void bootstrap(Message msg) {
    ((SocketPastryNode) getLocalNode()).getSocketSourceRouteManager().bootstrap(getEpochAddress(), msg);
  }

  /**
   * Returns a String representation of this DistNodeHandle. This method is
   * designed to be called by clients using the node handle, and is provided in
   * order to ensure that the right node handle is being talked to.
   *
   * @return A String representation of the node handle.
   */
  public String toString() {
    if (getLocalNode() == null) {
      return "[SNH: " + nodeId + "/" + getEpochAddress() + "]";
    } else {
      return "[SNH: " + getLocalNode().getNodeId() + " -> " + nodeId + "/" + getEpochAddress() + "]";
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
    if (!(obj instanceof SocketNodeHandle)) {
      return false;
    }

    SocketNodeHandle other = (SocketNodeHandle) obj;

    return (other.getNodeId().equals(getNodeId()) && other.eaddress.equals(eaddress));
  }

  /**
   * Hash codes for node handles. It is the hashcode of their corresponding
   * NodeId's.
   *
   * @return a hash code.
   */
  public int hashCode() {
    return getNodeId().hashCode() ^ eaddress.hashCode();
  }

  /**
   * Returns the last known proximity information about the Pastry node
   * associated with this handle. Invoking this method does not cause network
   * activity. Smaller values imply greater proximity. The exact nature and
   * interpretation of the proximity metric implementation-specific.
   *
   * @return the proximity metric value
   */
  public int proximity() {
    SocketPastryNode spn = (SocketPastryNode) getLocalNode();

    if (spn == null) {
      return DEFAULT_PROXIMITY;
    } else if (spn.getNodeId().equals(nodeId)) {
      return 0;
    } else {
      return spn.getSocketSourceRouteManager().proximity(getEpochAddress());
    }
  }

  /**
   * Ping the node. Refreshes the cached liveness status and proximity value of
   * the Pastry node associated with this. Invoking this method causes network
   * activity.
   *
   * @return true if node is currently alive.
   */
  public boolean ping() {
    final SocketPastryNode spn = (SocketPastryNode) getLocalNode();

//    Runnable runnable = new Runnable() {
//      public void run() {
    if ((spn != null) && spn.srManager != null) {
      spn.srManager.ping(getEpochAddress());
    }
//      }
//    };
//
//    SelectorManager sm = spn.getEnvironment().getSelectorManager();
//    if (sm.isSelectorThread()) {
//      runnable.run();
//    } else {
//      sm.invoke(runnable);
//    }

    return isAlive();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param o DESCRIBE THE PARAMETER
   * @param obj DESCRIBE THE PARAMETER
   */
  public void update(Observable o, Object obj) {
  }

//  transient HashSet obs = new HashSet();
//  public void addObserver(Observer o) {
//    if (obs == null) obs = new HashSet();
//    if (logger.level <= Logger.FINER) logger.log(this+".addObserver("+o+")");
//    synchronized (obs) {
//      obs.add(o);
//    }
//    super.addObserver(o);
//  }
//
//  public void deleteObserver(Observer o) {
//    if (logger.level <= Logger.FINER) logger.log(this+".deleteObserver("+o+")");
//    super.deleteObserver(o);
//    synchronized (obs) {
//      obs.remove(o);
//    }
//  }

  /**
   * Method which allows the observers of this socket node handle to be updated.
   * This method sets this object as changed, and then sends out the update.
   *
   * @param update The update
   */
  protected void update(Object update) {
    if (logger != null) {
      if (logger.level <= Logger.FINE) {
//        String s = "";
//        if (obs != null)
//          synchronized(obs) {
//            Iterator i = obs.iterator();
//            while(i.hasNext())
//              s+=","+i.next();
//          }
        logger.log(this + ".update(" + update + ")" + countObservers());
      }
    }
    setChanged();
    notifyObservers(update);
    if (logger != null) {
      if (logger.level <= Logger.FINEST) {
//        String s = "";
//        if (obs != null)
//          synchronized(obs) {
//            Iterator i = obs.iterator();
//            while(i.hasNext())
//              s+=","+i.next();
//          }
        logger.log(this + ".update(" + update + ")" + countObservers() + " done");
      }
    }
  }
}


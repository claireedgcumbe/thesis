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

package rice.pastry.commonapi;

import java.security.InvalidParameterException;

import rice.*;
import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.p2p.commonapi.*;
import rice.pastry.NodeId;
import rice.pastry.PastryNode;
import rice.pastry.client.PastryAppl;
import rice.pastry.dist.*;
import rice.pastry.leafset.LeafSet;
import rice.pastry.routing.SendOptions;
import rice.pastry.security.Credentials;
import rice.pastry.security.PermissiveCredentials;
import rice.selector.TimerTask;

/**
 * This class serves as gluecode, which allows applications written for the
 * common API to work with pastry.
 *
 * @version $Id: PastryEndpoint.java 3248 2006-04-27 13:04:07Z jeffh $
 * @author Alan Mislove
 * @author Peter Druschel
 */
public class PastryEndpoint extends PastryAppl implements Endpoint {

  /**
   * DESCRIBE THE FIELD
   */
  protected Credentials credentials = new PermissiveCredentials();

  /**
   * DESCRIBE THE FIELD
   */
  protected Application application;

  /**
   * DESCRIBE THE FIELD
   */
  protected String instance;

  /**
   * Constructor.
   *
   * @param pn the pastry node that the application attaches to.
   * @param application DESCRIBE THE PARAMETER
   * @param instance DESCRIBE THE PARAMETER
   */
  public PastryEndpoint(PastryNode pn, Application application, String instance) {
    super(pn, application.getClass().getName() + instance, null);

    this.instance = application.getClass().getName() + instance;
    this.application = application;
    register();
  }

  /**
   * Constructor.
   *
   * @param pn the pastry node that the application attaches to.
   * @param application DESCRIBE THE PARAMETER
   * @param port DESCRIBE THE PARAMETER
   */
  public PastryEndpoint(PastryNode pn, Application application, int port) {
    super(pn, port);

    this.instance = "[PORT " + port + "]";
    this.application = application;
    register();
  }

  // API methods to be invoked by applications

  /**
   * Returns this node's id, which is its identifier in the namespace.
   *
   * @return The local node's id
   */
  public Id getId() {
    return thePastryNode.getNodeId();
  }

  /**
   * Returns a handle to the local node below this endpoint.
   *
   * @return A NodeHandle referring to the local node.
   */
  public NodeHandle getLocalNodeHandle() {
    return thePastryNode.getLocalHandle();
  }

  // PastryAppl support

  /**
   * Returns the credentials of this application.
   *
   * @return the credentials.
   */
  public Credentials getCredentials() {
    return credentials;
  }

  /**
   * Returns a unique instance name of this endpoint, sort of a mailbox name for
   * this application.
   *
   * @return The unique instance name of this application
   */
  public String getInstance() {
    return instance;
  }

  /*
   *  (non-Javadoc)
   *  @see rice.p2p.commonapi.Endpoint#getEnvironment()
   */
  /**
   * Gets the Environment attribute of the PastryEndpoint object
   *
   * @return The Environment value
   */
  public Environment getEnvironment() {
    return thePastryNode.getEnvironment();
  }

  /**
   * This operation forwards a message towards the root of key. The optional
   * hint argument specifies a node that should be used as a first hop in
   * routing the message. A good hint, e.g. one that refers to the key's current
   * root, can result in the message being delivered in one hop; a bad hint adds
   * at most one extra hop to the route. Either K or hint may be NULL, but not
   * both. The operation provides a best-effort service: the message may be
   * lost, duplicated, corrupted, or delayed indefinitely.
   *
   * @param key the key
   * @param msg the message to deliver.
   * @param hint the hint
   */
  public void route(Id key, Message msg, NodeHandle hint) {
    if (logger.level <= Logger.FINER) {
      logger.log(
        "[" + thePastryNode + "] route " + msg + " to " + key);
    }

    PastryEndpointMessage pm = new PastryEndpointMessage(this.getAddress(), msg, thePastryNode.getLocalHandle());
    if ((key == null) && (hint == null)) {
      throw new InvalidParameterException("key and hint are null!");
    }
    boolean noKey = false;
    if (key == null) {
      noKey = true;
      key = hint.getId();
    }
    rice.pastry.routing.RouteMessage rm = new rice.pastry.routing.RouteMessage((rice.pastry.Id) key,
      pm,
      (rice.pastry.NodeHandle) hint,
      getAddress());

    if (noKey) {
      rm.getOptions().setMultipleHopsAllowed(false);
    }
    thePastryNode.receiveMessage(rm);
  }

  /**
   * Schedules a message to be delivered to this application after the provided
   * number of milliseconds.
   *
   * @param message The message to be delivered
   * @param delay The number of milliseconds to wait before delivering the
   *      message
   * @return DESCRIBE THE RETURN VALUE
   */
  public CancellableTask scheduleMessage(Message message, long delay) {
    PastryEndpointMessage pm = new PastryEndpointMessage(this.getAddress(), message, thePastryNode.getLocalHandle());
    return thePastryNode.scheduleMsg(pm, delay);
  }

  /**
   * Schedules a message to be delivered to this application every period number
   * of milliseconds, after delay number of miliseconds have passed.
   *
   * @param message The message to be delivered
   * @param delay The number of milliseconds to wait before delivering the fist
   *      message
   * @param period DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public CancellableTask scheduleMessage(Message message, long delay, long period) {
    PastryEndpointMessage pm = new PastryEndpointMessage(this.getAddress(), message, thePastryNode.getLocalHandle());
    return thePastryNode.scheduleMsg(pm, delay, period);
  }

  /**
   * Schedule the specified message for repeated fixed-rate delivery to the
   * local node, beginning after the specified delay. Subsequent executions take
   * place at approximately regular intervals, separated by the specified
   * period.
   *
   * @param msg a message that will be delivered to the local node after the
   *      specified delay
   * @param delay time in milliseconds before message is to be delivered
   * @param period time in milliseconds between successive message deliveries
   * @return the scheduled event object; can be used to cancel the message
   */
  public CancellableTask scheduleMessageAtFixedRate(Message msg,
                                                    long delay, long period) {
    PastryEndpointMessage pm = new PastryEndpointMessage(this.getAddress(), msg, thePastryNode.getLocalHandle());
    return thePastryNode.scheduleMsgAtFixedRate(pm, delay, period);
  }

  /**
   * This method produces a list of nodes that can be used as next hops on a
   * route towards key, such that the resulting route satisfies the overlay
   * protocol's bounds on the number of hops taken. If safe is true, the
   * expected fraction of faulty nodes in the list is guaranteed to be no higher
   * than the fraction of faulty nodes in the overlay; if false, the set may be
   * chosen to optimize performance at the expense of a potentially higher
   * fraction of faulty nodes. This option allows applications to implement
   * routing in overlays with byzantine node failures. Implementations that
   * assume fail-stop behavior may ignore the safe argument. The fraction of
   * faulty nodes in the returned list may be higher if the safe parameter is
   * not true because, for instance, malicious nodes have caused the local node
   * to build a routing table that is biased towards malicious
   * nodes~\cite{Castro02osdi}.
   *
   * @param key the message's key
   * @param num the maximal number of next hops nodes requested
   * @param safe
   * @return the nodehandle set
   */
  public NodeHandleSet localLookup(Id key, int num, boolean safe) {
    // safe ignored until we have the secure routing support

    // get the nodes from the routing table
    return getRoutingTable().alternateRoutes((rice.pastry.Id) key, num);
  }

  /**
   * This method produces an unordered list of nodehandles that are neighbors of
   * the local node in the ID space. Up to num node handles are returned.
   *
   * @param num the maximal number of nodehandles requested
   * @return the nodehandle set
   */
  public NodeHandleSet neighborSet(int num) {
    return getLeafSet().neighborSet(num);
  }

  /**
   * This method returns an ordered set of nodehandles on which replicas of the
   * object with key can be stored. The call returns nodes with a rank up to and
   * including max_rank. If max_rank exceeds the implementation's maximum
   * replica set size, then its maximum replica set is returned. The returned
   * nodes may be used for replicating data since they are precisely the nodes
   * which become roots for the key when the local node fails.
   *
   * @param id DESCRIBE THE PARAMETER
   * @param maxRank DESCRIBE THE PARAMETER
   * @return the replica set
   */
  public NodeHandleSet replicaSet(Id id, int maxRank) {
    LeafSet leafset = getLeafSet();
    if (maxRank > leafset.maxSize() / 2 + 1) {
      throw new IllegalArgumentException("maximum replicaSet size for this configuration exceeded; asked for " + maxRank + " but max is " + leafset.maxSize() / 2 + 1);
    }
    if (maxRank > leafset.size()) {
      if (logger.level <= Logger.FINER) {
        logger.log(
          "trying to get a replica set of size " + maxRank + " but only " + leafset.size() + " nodes in leafset");
      }
    }

    return leafset.replicaSet((rice.pastry.Id) id, maxRank);
  }

  /**
   * This methods returns an ordered set of nodehandles on which replicas of an
   * object with a given id can be stored. The call returns nodes up to and
   * including a node with maxRank. This call also allows the application to
   * provide a remote "center" node, as well as other nodes in the vicinity.
   *
   * @param id The object's id.
   * @param maxRank The number of desired replicas.
   * @param set The set of other nodes around the root handle
   * @param root DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public NodeHandleSet replicaSet(Id id, int maxRank, NodeHandle root, NodeHandleSet set) {
    LeafSet leaf = new LeafSet((rice.pastry.NodeHandle) root, getLeafSet().maxSize(), false);
    for (int i = 0; i < set.size(); i++) {
      leaf.put((rice.pastry.NodeHandle) set.getHandle(i));
    }

    return leaf.replicaSet((rice.pastry.Id) id, maxRank);
  }

  /**
   * This method provides information about ranges of keys for which the node n
   * is currently a r-root. The operations returns null if the range could not
   * be determined. It is an error to query the range of a node not present in
   * the neighbor set as returned by the update upcall or the neighborSet call.
   * Some implementations may have multiple, disjoint ranges of keys for which a
   * given node is responsible (Pastry has two). The parameter key allows the
   * caller to specify which range should be returned. If the node referenced by
   * n is the r-root for key, then the resulting range includes key. Otherwise,
   * the result is the nearest range clockwise from key for which n is
   * responsible.
   *
   * @param n nodeHandle of the node whose range is being queried
   * @param r the rank
   * @param key the key
   * @param cumulative if true, returns ranges for which n is an i-root for 0<i
   *      <=r
   * @return the range of keys, or null if range could not be determined for the
   *      given node and rank
   */
  public IdRange range(NodeHandle n, int r, Id key, boolean cumulative) {
    rice.pastry.Id pKey = (rice.pastry.Id) key;

    if (cumulative) {
      return getLeafSet().range((rice.pastry.NodeHandle) n, r);
    }

    rice.pastry.IdRange ccw = getLeafSet().range((rice.pastry.NodeHandle) n, r, false);
    rice.pastry.IdRange cw = getLeafSet().range((rice.pastry.NodeHandle) n, r, true);

    if (cw == null || ccw.contains(pKey) || pKey.isBetween(cw.getCW(), ccw.getCCW())) {
      return ccw;
    } else {
      return cw;
    }
  }

  /**
   * This method provides information about ranges of keys for which the node n
   * is currently a r-root. The operations returns null if the range could not
   * be determined. It is an error to query the range of a node not present in
   * the neighbor set as returned by the update upcall or the neighborSet call.
   * Some implementations may have multiple, disjoint ranges of keys for which a
   * given node is responsible (Pastry has two). The parameter key allows the
   * caller to specify which range should be returned. If the node referenced by
   * n is the r-root for key, then the resulting range includes key. Otherwise,
   * the result is the nearest range clockwise from key for which n is
   * responsible.
   *
   * @param n nodeHandle of the node whose range is being queried
   * @param r the rank
   * @param key the key
   * @return the range of keys, or null if range could not be determined for the
   *      given node and rank
   */
  public IdRange range(NodeHandle n, int r, Id key) {
    return range(n, r, key, false);
  }

  // Upcall to Application support

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   */
  public final void messageForAppl(rice.pastry.messaging.Message msg) {
    if (logger.level <= Logger.FINER) {
      logger.log(
        "[" + thePastryNode + "] deliver " + msg + " from " + msg.getSenderId());
    }

    if (msg instanceof PastryEndpointMessage) {
      // null for now, when RouteMessage stuff is completed, then it will be different!
      application.deliver(null, ((PastryEndpointMessage) msg).getMessage());
    } else {
      if (logger.level <= Logger.WARNING) {
        logger.log(
          "Received unknown message " + msg + " - dropping on floor");
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   * @param key DESCRIBE THE PARAMETER
   * @param nextHop DESCRIBE THE PARAMETER
   * @param opt DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public final boolean enrouteMessage(Message msg, Id key, NodeHandle nextHop, SendOptions opt) {
    if (msg instanceof RouteMessage) {
      if (logger.level <= Logger.FINER) {
        logger.log(
          "[" + thePastryNode + "] forward " + msg);
      }
      return application.forward((RouteMessage) msg);
    } else {
      return true;
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nh DESCRIBE THE PARAMETER
   * @param wasAdded DESCRIBE THE PARAMETER
   */
  public void leafSetChange(rice.pastry.NodeHandle nh, boolean wasAdded) {
    application.update(nh, wasAdded);
  }

  /**
   * Called by pastry to deliver a message to this client. Not to be overridden.
   *
   * @param msg the message that is arriving.
   */
  public void receiveMessage(rice.pastry.messaging.Message msg) {
    if (logger.level <= Logger.FINER) {
      logger.log(
        "[" + thePastryNode + "] recv " + msg);
    }

    if (msg instanceof rice.pastry.routing.RouteMessage) {
      rice.pastry.routing.RouteMessage rm = (rice.pastry.routing.RouteMessage) msg;

      // call application
      if (logger.level <= Logger.FINER) {
        logger.log(
          "[" + thePastryNode + "] forward " + msg);
      }
      if (application.forward(rm)) {
        if (rm.nextHop != null) {
          rice.pastry.NodeHandle nextHop = rm.nextHop;

          // if the message is for the local node, deliver it here
          if (getNodeId().equals(nextHop.getNodeId())) {
            PastryEndpointMessage pMsg = (PastryEndpointMessage) rm.unwrap();
            if (logger.level <= Logger.FINER) {
              logger.log(
                "[" + thePastryNode + "] deliver " + pMsg + " from " + pMsg.getSenderId());
            }
            application.deliver(rm.getTarget(), pMsg.getMessage());
          } else {
            // route the message
            rm.routeMessage((rice.pastry.NodeHandle) getLocalNodeHandle());
          }
        }
      }
    } else {
      // if the message is not a RouteMessage, then it is for the local node and
      // was sent with a PastryAppl.routeMsgDirect(); we deliver it for backward compatibility
      messageForAppl(msg);
    }
  }

  /**
   * Schedules a job for processing on the dedicated processing thread. CPU
   * intensive jobs, such as encryption, erasure encoding, or bloom filter
   * creation should never be done in the context of the underlying node's
   * thread, and should only be done via this method.
   *
   * @param task The task to run on the processing thread
   * @param command The command to return the result to once it's done
   */
  public void process(Executable task, Continuation command) {
    thePastryNode.process(task, command);
  }

}



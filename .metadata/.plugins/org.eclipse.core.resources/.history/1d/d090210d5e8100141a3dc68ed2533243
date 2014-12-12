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
package rice.pastry.standard;

import java.util.*;

import rice.environment.logging.Logger;
import rice.environment.params.Parameters;
import rice.environment.random.RandomSource;
import rice.environment.random.simple.SimpleRandomSource;
import rice.pastry.*;
import rice.pastry.client.PastryAppl;
import rice.pastry.leafset.BroadcastLeafSet;
import rice.pastry.leafset.InitiateLeafSetMaintenance;
import rice.pastry.leafset.LeafSet;
import rice.pastry.leafset.LeafSetProtocolAddress;
import rice.pastry.leafset.RequestLeafSet;
import rice.pastry.messaging.Address;
import rice.pastry.messaging.Message;
import rice.pastry.messaging.MessageReceiver;
import rice.pastry.routing.RoutingTable;
import rice.pastry.security.*;

/**
 * An implementation of a periodic-style leafset protocol
 *
 * @version $Id: PeriodicLeafSetProtocol.java 2862 2005-12-20 10:09:37Z jeffh $
 * @author Alan Mislove
 */
public class PeriodicLeafSetProtocol extends PastryAppl {

  /**
   * DESCRIBE THE FIELD
   */
  protected NodeHandle localHandle;

  /**
   * DESCRIBE THE FIELD
   */
  protected PastryNode localNode;

  /**
   * DESCRIBE THE FIELD
   */
  protected PastrySecurityManager security;

  /**
   * DESCRIBE THE FIELD
   */
  protected LeafSet leafSet;

  /**
   * DESCRIBE THE FIELD
   */
  protected RoutingTable routeTable;

  /**
   * NodeHandle -> Long remembers the TIME when we received a BLS from that
   * NodeHandle
   */
  protected WeakHashMap lastTimeReceivedBLS;

  /**
   * Related to rapidly determining direct neighbor liveness.
   */
  public final int PING_NEIGHBOR_PERIOD;

  /**
   * DESCRIBE THE FIELD
   */
  public final int CHECK_LIVENESS_PERIOD;

  ScheduledMessage pingNeighborMessage;

  RandomSource random;

  /**
   * DESCRIBE THE FIELD
   */
  protected Credentials cred = new PermissiveCredentials();

  /**
   * Builds a periodic leafset protocol
   *
   * @param ln DESCRIBE THE PARAMETER
   * @param local DESCRIBE THE PARAMETER
   * @param sm DESCRIBE THE PARAMETER
   * @param ls DESCRIBE THE PARAMETER
   * @param rt DESCRIBE THE PARAMETER
   */
  public PeriodicLeafSetProtocol(PastryNode ln, NodeHandle local,
                                 PastrySecurityManager sm, LeafSet ls, RoutingTable rt) {
    super(ln);
    this.localNode = ln;

    Parameters params = ln.getEnvironment().getParameters();
    if (params.contains("pastry_periodic_leafset_protocol_use_own_random")
      && params.getBoolean("pastry_periodic_leafset_protocol_use_own_random")) {
      if (params.contains("pastry_periodic_leafset_protocol_random_seed")
        && !params.getString("pastry_periodic_leafset_protocol_random_seed").equalsIgnoreCase(
        "clock")) {
        this.random = new SimpleRandomSource(params.getLong("pastry_periodic_leafset_protocol_random_seed"), ln.getEnvironment().getLogManager(),
          "socket");
      } else {
        this.random = new SimpleRandomSource(ln.getEnvironment().getLogManager(), "periodic_leaf_set");
      }
    } else {
      this.random = ln.getEnvironment().getRandomSource();
    }

    this.localHandle = local;
    this.security = sm;
    this.leafSet = ls;
    this.routeTable = rt;
    this.lastTimeReceivedBLS = new WeakHashMap();
    Parameters p = ln.getEnvironment().getParameters();
    PING_NEIGHBOR_PERIOD = p.getInt("pastry_protocol_periodicLeafSet_ping_neighbor_period");
    CHECK_LIVENESS_PERIOD = PING_NEIGHBOR_PERIOD
      + p.getInt("pastry_protocol_periodicLeafSet_checkLiveness_neighbor_gracePeriod");

    // Removed after meeting on 5/5/2005 Don't know if this is always the
    // appropriate policy.
    // leafSet.addObserver(this);
    pingNeighborMessage = localNode.scheduleMsgAtFixedRate(
      new InitiatePingNeighbor(), PING_NEIGHBOR_PERIOD, PING_NEIGHBOR_PERIOD);
  }

  /**
   * Gets the Credentials attribute of the PeriodicLeafSetProtocol object
   *
   * @return The Credentials value
   */
  public Credentials getCredentials() {
    return cred;
  }

  /**
   * Get address.
   *
   * @return gets the address.
   */
  public Address getAddress() {
    return new LeafSetProtocolAddress();
  }

  /**
   * Receives messages.
   *
   * @param msg the message.
   */
  public void receiveMessage(Message msg) {
    if (msg instanceof BroadcastLeafSet) {
      // receive a leafset from another node
      BroadcastLeafSet bls = (BroadcastLeafSet) msg;

      lastTimeReceivedBLS.put(bls.from(), new Long(localNode.getEnvironment()
        .getTimeSource().currentTimeMillis()));

      // if we have now successfully joined the ring, set the local node ready
      if (bls.type() == BroadcastLeafSet.JoinInitial) {
        // merge the received leaf set into our own
        leafSet.merge(bls.leafSet(), bls.from(), routeTable, security, false,
          null);

        // localNode.setReady();
        broadcastAll();
      } else {
        // first check for missing entries in their leafset
        NodeSet set = leafSet.neighborSet(Integer.MAX_VALUE);

        // if we find any missing entries, check their liveness
        for (int i = 0; i < set.size(); i++) {
          if (bls.leafSet().test(set.get(i))) {
            set.get(i).checkLiveness();
          }
        }

        // now check for assumed-dead entries in our leafset
        set = bls.leafSet().neighborSet(Integer.MAX_VALUE);

        // if we find any missing entries, check their liveness
        for (int i = 0; i < set.size(); i++) {
          if (!set.get(i).isAlive()) {
            set.get(i).checkLiveness();
          }
        }

        // merge the received leaf set into our own
        leafSet.merge(bls.leafSet(), bls.from(), routeTable, security, false,
          null);
      }
    } else if (msg instanceof RequestLeafSet) {
      // request for leaf set from a remote node
      RequestLeafSet rls = (RequestLeafSet) msg;

      rls.returnHandle().receiveMessage(
        new BroadcastLeafSet(localHandle, leafSet, BroadcastLeafSet.Update));
    } else if (msg instanceof InitiateLeafSetMaintenance) {
      // perform leafset maintenance
      NodeSet set = leafSet.neighborSet(Integer.MAX_VALUE);

      if (set.size() > 1) {
        NodeHandle handle = set.get(random.nextInt(set.size() - 1) + 1);
        handle.receiveMessage(new RequestLeafSet(localHandle));
        handle.receiveMessage(new BroadcastLeafSet(localHandle, leafSet,
          BroadcastLeafSet.Update));

        NodeHandle check = set.get(random.nextInt(set.size() - 1) + 1);
        check.checkLiveness();
      }
    } else if (msg instanceof InitiatePingNeighbor) {
      // IPN every 20 seconds
      NodeHandle left = leafSet.get(-1);
      NodeHandle right = leafSet.get(1);

      // send BLS to left neighbor
      if (left != null) {
        left.receiveMessage(new BroadcastLeafSet(localHandle, leafSet,
          BroadcastLeafSet.Update));
        left.receiveMessage(new RequestLeafSet(localHandle));
      }
      // see if received BLS within past 30 seconds from right neighbor
      if (right != null) {
        Long time = (Long) lastTimeReceivedBLS.get(right);
        if (time == null
          || (time.longValue() < (localNode.getEnvironment().getTimeSource()
          .currentTimeMillis() - CHECK_LIVENESS_PERIOD))) {
          // else checkLiveness() on right neighbor
          if (logger.level <= Logger.FINE) {
            logger.log("PeriodicLeafSetProtocol: Checking liveness on right neighbor:"
              + right);
          }
          right.checkLiveness();
        }
      }
      if (left != null) {
        Long time = (Long) lastTimeReceivedBLS.get(left);
        if (time == null
          || (time.longValue() < (localNode.getEnvironment().getTimeSource()
          .currentTimeMillis() - CHECK_LIVENESS_PERIOD))) {
          // else checkLiveness() on left neighbor
          if (logger.level <= Logger.FINE) {
            logger.log("PeriodicLeafSetProtocol: Checking liveness on left neighbor:"
              + left);
          }
          left.checkLiveness();
        }
      }
    }
  }

  /**
   * Broadcast the leaf set to all members of the local leaf set.
   *
   */
  protected void broadcastAll() {
    BroadcastLeafSet bls = new BroadcastLeafSet(localHandle, leafSet,
      BroadcastLeafSet.JoinAdvertise);
    NodeSet set = leafSet.neighborSet(Integer.MAX_VALUE);

    for (int i = 1; i < set.size(); i++) {
      set.get(i).receiveMessage(bls);
    }
  }

  /**
   * Used to kill self if leafset shrunk by too much. NOTE: PLSP is not
   * registered as an observer.
   *
   * @param msg DESCRIBE THE PARAMETER
   */
  // public void update(Observable arg0, Object arg1) {
  // NodeSetUpdate nsu = (NodeSetUpdate)arg1;
  // if (!nsu.wasAdded()) {
  // if (localNode.isReady() && !leafSet.isComplete() && leafSet.size() <
  // (leafSet.maxSize()/2)) {
  // // kill self
  // localNode.getEnvironment().getLogManager().getLogger(PeriodicLeafSetProtocol.class,
  // null).log(Logger.SEVERE,
  // "PeriodicLeafSetProtocol:
  // "+localNode.getEnvironment().getTimeSource().currentTimeMillis()+" Killing
  // self due to leafset collapse. "+leafSet);
  // localNode.resign();
  // }
  // }
  // }
  /**
   * Should not be called becasue we are overriding the receiveMessage()
   * interface anyway.
   *
   * @param msg DESCRIBE THE PARAMETER
   */
  public void messageForAppl(Message msg) {
    throw new RuntimeException("Should not be called.");
  }

  /**
   * We always want to receive messages.
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean deliverWhenNotReady() {
    return true;
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void destroy() {
    if (logger.level <= Logger.INFO) {
      logger.log("PLSP: destroy() called");
    }
    pingNeighborMessage.cancel();
  }

}

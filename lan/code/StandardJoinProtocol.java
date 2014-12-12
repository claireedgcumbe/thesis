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

import rice.environment.logging.Logger;
import rice.pastry.*;
import rice.pastry.messaging.*;
import rice.pastry.leafset.*;
import rice.pastry.routing.*;
import rice.pastry.security.*;
import rice.pastry.client.PastryAppl;
import rice.pastry.join.*;

import java.util.*;

/**
 * An implementation of a simple join protocol.
 *
 * @version $Id: StandardJoinProtocol.java 3095 2006-02-23 20:14:55Z jstewart $
 * @author Peter Druschel
 * @author Andrew Ladd
 * @author Rongmei Zhang
 * @author Y. Charlie Hu
 */

public class StandardJoinProtocol extends PastryAppl {
  /**
   * DESCRIBE THE FIELD
   */
  protected NodeHandle localHandle;

  /**
   * DESCRIBE THE FIELD
   */
  protected PastrySecurityManager security;

  /**
   * DESCRIBE THE FIELD
   */
  protected RoutingTable routeTable;

  /**
   * DESCRIBE THE FIELD
   */
  protected LeafSet leafSet;

  /**
   * DESCRIBE THE FIELD
   */
  protected Credentials cred = new PermissiveCredentials();

  /**
   * Constructor.
   *
   * @param lh the local node handle.
   * @param sm the Pastry security manager.
   * @param ln DESCRIBE THE PARAMETER
   * @param rt DESCRIBE THE PARAMETER
   * @param ls DESCRIBE THE PARAMETER
   */

  public StandardJoinProtocol(PastryNode ln, NodeHandle lh,
                              PastrySecurityManager sm, RoutingTable rt, LeafSet ls) {
    super(ln);
    localHandle = lh;
    security = sm;

    routeTable = rt;
    leafSet = ls;
  }

  /**
   * Get address.
   *
   * @return gets the address.
   */
  public Address getAddress() {
    return new JoinAddress();
  }

  /**
   * Gets the Credentials attribute of the StandardJoinProtocol object
   *
   * @return The Credentials value
   */
  public Credentials getCredentials() {
    return cred;
  }

  /**
   * Can be overloaded to do additional things before going ready. For example,
   * verifying that other nodes are aware of us, so that consistent routing is
   * guaranteed.
   */
  protected void setReady() {
    thePastryNode.setReady();
  }

  /**
   * Receives a message from the outside world.
   *
   * @param msg the message that was received.
   */

  public void receiveMessage(Message msg) {
    if (msg instanceof JoinRequest) {
      JoinRequest jr = (JoinRequest) msg;

      NodeHandle nh = jr.getHandle();

      nh = security.verifyNodeHandle(nh);

      // if (nh.isAlive() == true) // the handle is alive
      if (jr.accepted() == false) {
        // this is the terminal node on the request path
        // leafSet.put(nh);
        if (thePastryNode.isReady()) {
          jr.acceptJoin(localHandle, leafSet);
          nh.receiveMessage(jr);
        } else {
          if (logger.level <= Logger.INFO) {
            logger.log(
              "NOTE: Dropping incoming JoinRequest " + jr
              + " because local node is not ready!");
          }
        }
      } else {
        // this is the node that initiated the join request in the first
        // place
        NodeHandle jh = jr.getJoinHandle();
        // the node we joined to.

        jh = security.verifyNodeHandle(jh);

        if (jh.getId().equals(localHandle.getId()) && !jh.equals(localHandle)) {
          if (logger.level <= Logger.WARNING) {
            logger.log(
              "NodeId collision, unable to join: " + localHandle + ":" + jh);
          }
        } else if (jh.isAlive() == true) {
          // the join handle is alive
          routeTable.put(jh);
          // add the num. closest node to the routing table

          // update local RT, then broadcast rows to our peers
          broadcastRows(jr);

          // now update the local leaf set
          BroadcastLeafSet bls = new BroadcastLeafSet(jh, jr.getLeafSet(),
            BroadcastLeafSet.JoinInitial);
          localHandle.receiveMessage(bls);

          // we have now successfully joined the ring, set the local node ready
          setReady();
        }
      }
    } else if (msg instanceof RouteMessage) {
      // a join request message at an intermediate node
      RouteMessage rm = (RouteMessage) msg;

      JoinRequest jr = (JoinRequest) rm.unwrap();

      NodeId localId = localHandle.getNodeId();
      NodeHandle jh = jr.getHandle();
      NodeId nid = jh.getNodeId();

      jh = security.verifyNodeHandle(jh);

      if (!jh.equals(localHandle)) {
        int base = thePastryNode.getRoutingTable().baseBitLength();

        int msdd = localId.indexOfMSDD(nid, base);
        int last = jr.lastRow();

        for (int i = last - 1; msdd > 0 && i >= msdd; i--) {
          RouteSet[] row = routeTable.getRow(i);

          jr.pushRow(row);
        }

        rm.routeMessage(localHandle);
      }
    } else if (msg instanceof InitiateJoin) {
      // request from the local node to
      // join
      InitiateJoin ij = (InitiateJoin) msg;

      NodeHandle nh = ij.getHandle();

      if (nh == null) {
        if (logger.level <= Logger.SEVERE) {
          logger.log(
            "ERROR: Cannot join ring.  All bootstraps are faulty.");
        }
      } else {
        nh = security.verifyNodeHandle(nh);
        if (nh.isAlive() == true) {
          JoinRequest jr = new JoinRequest(localHandle, thePastryNode.getRoutingTable().baseBitLength());

          RouteMessage rm = new RouteMessage(localHandle.getNodeId(), jr,
            new PermissiveCredentials(), getAddress());
          rm.getOptions().setRerouteIfSuspected(false);
          nh.bootstrap(rm);
        }
      }
    }
  }

  /**
   * Broadcasts the route table rows.
   *
   * @param jr the join row.
   */

  public void broadcastRows(JoinRequest jr) {
    // NodeId localId = localHandle.getNodeId();
    int n = jr.numRows();

    // send the rows to the RouteSetProtocol on the local node
    for (int i = jr.lastRow(); i < n; i++) {
      RouteSet row[] = jr.getRow(i);

      if (row != null) {
        BroadcastRouteRow brr = new BroadcastRouteRow(localHandle, row);

        localHandle.receiveMessage(brr);
      }
    }

    // now broadcast the rows to our peers in each row

    for (int i = jr.lastRow(); i < n; i++) {
      RouteSet row[] = jr.getRow(i);

      BroadcastRouteRow brr = new BroadcastRouteRow(localHandle, row);

      for (int j = 0; j < row.length; j++) {
        RouteSet rs = row[j];
        if (rs == null) {
          continue;
        }

        // send to closest nodes only

        NodeHandle nh = rs.closestNode();
        if (nh != null) {
          nh = security.verifyNodeHandle(nh);
        }
        if (nh != null) {
          nh.receiveMessage(brr);
        }
        /*
         *  int m = rs.size(); for (int k=0; k<m; k++) { NodeHandle nh =
         *  rs.get(k);
         *
         *  nh.receiveMessage(brr); }
         */
      }
    }
  }

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
}

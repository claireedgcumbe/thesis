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

import rice.pastry.*;
import rice.pastry.direct.*;
import rice.pastry.standard.*;
import rice.pastry.join.*;
import rice.pastry.client.*;
import rice.pastry.messaging.*;
import rice.pastry.security.*;
import rice.pastry.routing.*;

import java.util.*;

/**
 * RegrTestApp A regression test suite for pastry. This is the per-node app
 * object.
 *
 * @version $Id: RegrTestApp.java 3048 2006-02-10 16:25:33Z jeffh $
 * @author andrew ladd
 * @author peter druschel
 */

public class RegrTestApp extends CommonAPIAppl {

  private PastryRegrTest prg;

  private static Credentials cred = new PermissiveCredentials();

  private static Address addr = new RTAddress();

  /**
   * Constructor for RegrTestApp.
   *
   * @param pn DESCRIBE THE PARAMETER
   * @param prg DESCRIBE THE PARAMETER
   */
  public RegrTestApp(PastryNode pn, PastryRegrTest prg) {
    super(pn);
    this.prg = prg;
  }

  /**
   * Gets the Address attribute of the RegrTestApp object
   *
   * @return The Address value
   */
  public Address getAddress() {
    return addr;
  }

  /**
   * Gets the Credentials attribute of the RegrTestApp object
   *
   * @return The Credentials value
   */
  public Credentials getCredentials() {
    return cred;
  }

  /**
   * Gets the PastryNode attribute of the RegrTestApp object
   *
   * @return The PastryNode value
   */
  public PastryNode getPastryNode() {
    return thePastryNode;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nid DESCRIBE THE PARAMETER
   */
  public void sendMsg(NodeId nid) {
    routeMsg(nid, new RTMessage(addr, getNodeHandle(), nid), cred,
      new SendOptions());
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nid DESCRIBE THE PARAMETER
   */
  public void sendTrace(NodeId nid) {
    //System.out.println("sending a trace from " + getNodeId() + " to " +
    // nid);
    routeMsg(nid, new RTMessage(addr, getNodeHandle(), nid), cred,
      new SendOptions());
  }

  //public void messageForAppl(Message msg) {
  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   * @param msg DESCRIBE THE PARAMETER
   */
  public void deliver(Id key, Message msg) {

    /*
     *  System.out.print(msg); System.out.println(" received at " +
     *  getNodeId());
     */
    // check if numerically closest
    RTMessage rmsg = (RTMessage) msg;
    //NodeId key = rmsg.target;
    NodeId localId = getNodeId();

    if (localId != key) {
      int inBetween;
      if (localId.compareTo(key) < 0) {
        int i1 = prg.pastryNodesSortedReady.subMap(localId, key).size();
        int i2 = prg.pastryNodesSortedReady.tailMap(key).size()
          + prg.pastryNodesSortedReady.headMap(localId).size();

        inBetween = (i1 < i2) ? i1 : i2;
      } else {
        int i1 = prg.pastryNodesSortedReady.subMap(key, localId).size();
        int i2 = prg.pastryNodesSortedReady.tailMap(localId).size()
          + prg.pastryNodesSortedReady.headMap(key).size();

        inBetween = (i1 < i2) ? i1 : i2;
      }

      if (inBetween > 1) {
        System.out.println("messageForAppl failure, inBetween=" + inBetween);
        System.out.print(msg);
        System.out.println(" received at " + getNodeId());
        System.out.println(getLeafSet());
      }
    }
  }

  //public boolean enrouteMessage(Message msg, Id key, NodeId nextHop,
  // SendOptions opt) {
  /**
   * DESCRIBE THE METHOD
   *
   * @param rm DESCRIBE THE PARAMETER
   */
  public void forward(RouteMessage rm) {
    /*
     *  System.out.print(msg); System.out.println(" at " + getNodeId());
     */
    Message msg = rm.unwrap();
    Id key = rm.getTarget();
    NodeId nextHop = rm.getNextHop().getNodeId();

    NodeId localId = getNodeId();
    NodeId.Distance dist = localId.distance(key);
    int base = getRoutingTable().baseBitLength();

    if (prg.lastMsg == msg) {
      int localIndex = localId.indexOfMSDD(key, base);
      int lastIndex = prg.lastNode.indexOfMSDD(key, base);

      if ((localIndex > lastIndex && nextHop != localId)
        || (localIndex == lastIndex && dist.compareTo(prg.lastDist) > 0)) {
        System.out.println("at... " + getNodeId()
          + " enrouteMessage failure with " + msg + " lastNode="
          + prg.lastNode + " lastDist=" + prg.lastDist + " dist=" + dist
          + " nextHop=" + nextHop + " loci=" + localIndex + " lasti="
          + lastIndex);
      }

      prg.lastDist = dist;
    }
    prg.lastMsg = msg;
    prg.lastDist = dist;
    prg.lastNode = localId;

    //return true;
  }

  //public void leafSetChange(NodeHandle nh, boolean wasAdded) {
  /**
   * DESCRIBE THE METHOD
   *
   * @param nh DESCRIBE THE PARAMETER
   * @param wasAdded DESCRIBE THE PARAMETER
   */
  public void update(NodeHandle nh, boolean wasAdded) {
    final NodeId nid = nh.getNodeId();

    /*
     *  System.out.println("at... " + getNodeId() + "'s leaf set");
     *  System.out.print("node " + nid + " was "); if (wasAdded)
     *  System.out.println("added"); else System.out.println("removed");
     */
    if (!prg.pastryNodesSorted.containsKey(nid) && nh.isAlive()) {
      System.out.println("at... " + getNodeId()
        + "leafSetChange failure 1 with " + nid);
    }

    NodeId localId = thePastryNode.getNodeId();

    if (localId == nid) {
      System.out.println("at... " + getNodeId()
        + "leafSetChange failure 2 with " + nid);
    }

    int inBetween;

    if (localId.compareTo(nid) < 0) {
      // localId < nid?
      int i1 = prg.pastryNodesSorted.subMap(localId, nid).size();
      int i2 = prg.pastryNodesSorted.tailMap(nid).size()
        + prg.pastryNodesSorted.headMap(localId).size();

      inBetween = (i1 < i2) ? i1 : i2;
    } else {
      int i1 = prg.pastryNodesSorted.subMap(nid, localId).size();
      int i2 = prg.pastryNodesSorted.tailMap(localId).size()
        + prg.pastryNodesSorted.headMap(nid).size();

      inBetween = (i1 < i2) ? i1 : i2;
    }

    int lsSize = getLeafSet().maxSize() / 2;

    if ((inBetween > lsSize && wasAdded
      && !prg.pastryNodesLastAdded.contains(thePastryNode) && !prg.inConcJoin)
      || (inBetween <= lsSize && !wasAdded && !getLeafSet().member(nh))
      && prg.pastryNodesSorted.containsKey(nh.getNodeId())) {

      System.out.println("at... " + getNodeId()
        + "leafSetChange failure 3 with " + nid + " wasAdded=" + wasAdded
        + " inBetween=" + inBetween);
      System.out.println(getLeafSet());
      /*
       *  Iterator it = prg.pastryNodesSorted.keySet().iterator(); while
       *  (it.hasNext()) System.out.println(it.next());
       */
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nh DESCRIBE THE PARAMETER
   * @param wasAdded DESCRIBE THE PARAMETER
   */
  public void routeSetChange(NodeHandle nh, boolean wasAdded) {
    NodeId nid = nh.getNodeId();

    /*
     *  System.out.println("at... " + getNodeId() + "'s route set");
     *  System.out.print("node " + nid + " was "); if (wasAdded)
     *  System.out.println("added"); else System.out.println("removed");
     *  System.out.println(getRoutingTable());
     */
    if (!prg.pastryNodesSorted.containsKey(nid)) {
      if (nh.isAlive() || wasAdded) {
        System.out.println("at... " + getNodeId()
          + "routeSetChange failure 1 with " + nid + " wasAdded=" + wasAdded);
      }
    }
  }

  /**
   * Invoked when the Pastry node has joined the overlay network and is ready to
   * send and receive messages
   */

  public void notifyReady() {
    //if (getLeafSet().size() == 0) System.out.println("notifyReady at " +
    // getNodeId() + " : leafset is empty!!");
  }

  /**
   * DESCRIBE THE CLASS
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */
  private static class RTAddress implements Address {
    private int myCode = 0x9219d6ff;

    /**
     * DESCRIBE THE METHOD
     *
     * @return DESCRIBE THE RETURN VALUE
     */
    public int hashCode() {
      return myCode;
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param obj DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public boolean equals(Object obj) {
      return (obj instanceof RTAddress);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @return DESCRIBE THE RETURN VALUE
     */
    public String toString() {
      return "[RTAddress]";
    }
  }

}

/**
 * DO NOT declare this inside PingClient; see HelloWorldApp for details.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */

class RTMessage extends Message {

  /**
   * DESCRIBE THE FIELD
   */
  public NodeHandle sourceNode;

  //public NodeId source;
  /**
   * DESCRIBE THE FIELD
   */
  public NodeId target;

  /**
   * Constructor for RTMessage.
   *
   * @param addr DESCRIBE THE PARAMETER
   * @param src DESCRIBE THE PARAMETER
   * @param tgt DESCRIBE THE PARAMETER
   */
  public RTMessage(Address addr, NodeHandle src, NodeId tgt) {
    super(addr);
    sourceNode = src;
    //source = src.getNodeId();
    target = tgt;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    String s = "";
    s += "RTMsg from " + sourceNode + " to " + target;
    return s;
  }
}

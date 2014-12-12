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

import rice.environment.logging.Logger;
import rice.environment.random.RandomSource;
import rice.pastry.*;
import rice.pastry.direct.*;
import rice.pastry.standard.*;
import rice.pastry.join.*;
import rice.pastry.client.*;
import rice.pastry.security.*;
import rice.pastry.messaging.*;
import rice.pastry.routing.*;

import java.util.*;
import java.io.*;

/**
 * A hello world example for pastry. This is the per-node app object.
 *
 * @version $Id: HelloWorldApp.java 3038 2006-02-07 10:01:01Z jeffh $
 * @author Sitaram Iyer
 */

public class HelloWorldApp extends PastryAppl {

  private int msgid = 0;

  private static Address addr = new HelloAddress();

  private static Credentials cred = new PermissiveCredentials();

  /**
   * Constructor for HelloWorldApp.
   *
   * @param pn DESCRIBE THE PARAMETER
   */
  public HelloWorldApp(PastryNode pn) {
    super(pn, null, addr);
    register();
  }

  // The remaining methods override abstract methods in the PastryAppl API.

  /**
   * Get address.
   *
   * @return the address of this application.
   */
  public Address getAddress() {
    return addr;
  }

  /**
   * Get credentials.
   *
   * @return credentials.
   */
  public Credentials getCredentials() {
    return cred;
  }

  /**
   * Sends a message to a randomly chosen node. Yeah, for fun.
   *
   * @param rng Randon number generator.
   */
  public void sendRndMsg(RandomSource rng) {
    Id rndid = Id.makeRandomId(rng);
    System.out.println("Sending message from " + getNodeId() + " to random dest " + rndid);
    Message msg = new HelloMsg(addr, thePastryNode.getLocalHandle(), rndid,
      ++msgid);
    routeMsg(rndid, msg, cred, new SendOptions());
  }

  /**
   * Invoked on destination node when a message arrives.
   *
   * @param msg Message being routed around
   */
  public void messageForAppl(Message msg) {
    System.out.println("Received " + msg + " at " + getNodeId());
  }

  /**
   * Invoked on intermediate nodes in routing path.
   *
   * @param msg Message that's passing through this node.
   * @param key destination
   * @param nextHop next hop
   * @param opt send options
   * @return true if message needs to be forwarded according to plan.
   */
  public boolean enrouteMessage(Message msg, Id key, NodeHandle nextHop,
                                SendOptions opt) {
    System.out.println("Enroute " + msg + " at " + getNodeId());
    return true;
  }

  /**
   * Invoked upon change to leafset.
   *
   * @param nh node handle that got added/removed
   * @param wasAdded added (true) or removed (false)
   */
  public void leafSetChange(NodeHandle nh, boolean wasAdded) {
    String s = "In " + getNodeId() + "'s leaf set, " + "node " + nh.getNodeId()
      + " was ";
    if (wasAdded) {
      s += "added";
    } else {
      s += "removed";
    }

    System.out.println(s);
  }

  /**
   * Invoked upon change to routing table.
   *
   * @param nh node handle that got added/removed
   * @param wasAdded added (true) or removed (false)
   */
  public void routeSetChange(NodeHandle nh, boolean wasAdded) {
    String s = "In " + getNodeId() + "'s route set, " + "node "
      + nh.getNodeId() + " was ";
    if (wasAdded) {
      s += "added";
    } else {
      s += "removed";
    }

    System.out.println(s);
  }

  /**
   * Invoked by {RMI,Direct}PastryNode when the node has something in its leaf
   * set, and has become ready to receive application messages.
   */
  public void notifyReady() {
    if (true
    /*
     *  Log.ifp(6)
     */
      ) {
      System.out.println("Node " + getNodeId()
        + " ready, waking up any clients");
    }
    //sendRndMsg(new Randon());

  }

  /**
   * DESCRIBE THE CLASS
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */
  private static class HelloAddress implements Address {
    private int myCode = 0x1984abcd;

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
      return (obj instanceof HelloAddress);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @return DESCRIBE THE RETURN VALUE
     */
    public String toString() {
      return "[HelloAddress]";
    }
  }
}


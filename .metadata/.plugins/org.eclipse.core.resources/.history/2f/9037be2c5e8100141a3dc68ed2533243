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
import rice.pastry.client.*;
import rice.pastry.routing.*;
import rice.pastry.messaging.*;
import rice.pastry.security.*;

import java.util.*;

/**
 * A very simple ping object.
 *
 * @version $Id: PingClient.java 3038 2006-02-07 10:01:01Z jeffh $
 * @author Andrew Ladd
 */

public class PingClient extends PastryAppl {

  private Credentials pingCred = new PermissiveCredentials();

  private static Address pingAddress = new PingAddress();

  /**
   * Constructor for PingClient.
   *
   * @param pn DESCRIBE THE PARAMETER
   */
  public PingClient(PastryNode pn) {
    super(pn);
  }

  /**
   * Gets the Address attribute of the PingClient object
   *
   * @return The Address value
   */
  public Address getAddress() {
    return pingAddress;
  }

  /**
   * Gets the Credentials attribute of the PingClient object
   *
   * @return The Credentials value
   */
  public Credentials getCredentials() {
    return pingCred;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nid DESCRIBE THE PARAMETER
   */
  public void sendPing(NodeId nid) {
    // routeMessage, sans the getAddress() in the RouteMessage constructor
    routeMsg(nid, new PingMessage(pingAddress, getNodeId(), nid), pingCred,
      new SendOptions());
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nid DESCRIBE THE PARAMETER
   */
  public void sendTrace(NodeId nid) {
    System.out.println("sending a trace from " + getNodeId() + " to " + nid);
    // sendEnrouteMessage
    routeMsg(nid, new PingMessage(pingAddress, getNodeId(), nid), pingCred,
      new SendOptions());
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   */
  public void messageForAppl(Message msg) {
    System.out.print(msg);
    System.out.println(" received");
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   * @param from DESCRIBE THE PARAMETER
   * @param nextHop DESCRIBE THE PARAMETER
   * @param opt DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean enrouteMessage(Message msg, Id from, NodeHandle nextHop,
                                SendOptions opt) {
    System.out.print(msg);
    System.out.println(" at " + getNodeId());

    return true;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nh DESCRIBE THE PARAMETER
   * @param wasAdded DESCRIBE THE PARAMETER
   */
  public void leafSetChange(NodeHandle nh, boolean wasAdded) {
    if (true) {
      return;
    }
    System.out.println("at... " + getNodeId() + "'s leaf set");
    System.out.print("node " + nh.getNodeId() + " was ");
    if (wasAdded) {
      System.out.println("added");
    } else {
      System.out.println("removed");
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nh DESCRIBE THE PARAMETER
   * @param wasAdded DESCRIBE THE PARAMETER
   */
  public void routeSetChange(NodeHandle nh, boolean wasAdded) {
    if (true) {
      return;
    }
    System.out.println("at... " + getNodeId() + "'s route set");
    System.out.print("node " + nh.getNodeId() + " was ");
    if (wasAdded) {
      System.out.println("added");
    } else {
      System.out.println("removed");
    }
  }

  /**
   * DESCRIBE THE CLASS
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */
  private static class PingAddress implements Address {
    private int myCode = 0x9219d8ff;

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
      return (obj instanceof PingAddress);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @return DESCRIBE THE RETURN VALUE
     */
    public String toString() {
      return "[PingAddress]";
    }
  }
}

/**
 * DO NOT declare this inside PingClient; see HelloWorldApp for details.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */

class PingMessage extends Message {

  private NodeId source;

  private NodeId target;

  /**
   * Constructor for PingMessage.
   *
   * @param pingAddress DESCRIBE THE PARAMETER
   * @param src DESCRIBE THE PARAMETER
   * @param tgt DESCRIBE THE PARAMETER
   */
  public PingMessage(Address pingAddress, NodeId src, NodeId tgt) {
    super(pingAddress);
    source = src;
    target = tgt;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    String s = "";
    s += "ping from " + source + " to " + target;
    return s;
  }
}


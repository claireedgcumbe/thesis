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
import rice.pastry.direct.*;

import java.util.*;

/**
 * Ping A performance test suite for pastry. This is the per-node app object.
 *
 * @version $Id: Ping.java 2805 2005-11-17 16:22:24Z jeffh $
 * @author Rongmei Zhang
 */

public class Ping extends PastryAppl {

  private Credentials pingCred = new PermissiveCredentials();
  private static Address pingAddress = new PingAddress();

  /**
   * Constructor for Ping.
   *
   * @param pn DESCRIBE THE PARAMETER
   */
  public Ping(PastryNode pn) {
    super(pn);
  }

  /**
   * Gets the Address attribute of the Ping object
   *
   * @return The Address value
   */
  public Address getAddress() {
    return pingAddress;
  }

  /**
   * Gets the Credentials attribute of the Ping object
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
    routeMsg(nid, new PingMessageNew(pingAddress, getNodeHandle(), nid), pingCred,
      new SendOptions());
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   */
  public void messageForAppl(Message msg) {

    PingMessageNew pMsg = (PingMessageNew) msg;
    int nHops = pMsg.getHops() - 1;
    double fDistance = pMsg.getDistance();
    double rDistance;

    NetworkSimulator sim = ((DirectNodeHandle) ((DirectPastryNode) thePastryNode)
      .getLocalHandle()).getSimulator();
    PingTestRecord tr = (PingTestRecord) (sim.getTestRecord());

    double dDistance = sim.proximity((DirectNodeHandle) thePastryNode.getLocalHandle(), (DirectNodeHandle) pMsg.getSender());
    if (dDistance == 0) {
      rDistance = 0;
    } else {
      rDistance = fDistance / dDistance;
    }
    tr.addHops(nHops);
    tr.addDistance(rDistance);

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

    PingMessageNew pMsg = (PingMessageNew) msg;
    pMsg.incrHops();
    pMsg.incrDistance(((DirectNodeHandle) ((DirectPastryNode) thePastryNode)
      .getLocalHandle()).getSimulator().proximity(
      (DirectNodeHandle) thePastryNode.getLocalHandle(),
      (DirectNodeHandle) nextHop));

    return true;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nh DESCRIBE THE PARAMETER
   * @param wasAdded DESCRIBE THE PARAMETER
   */
  public void leafSetChange(NodeHandle nh, boolean wasAdded) {
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param nh DESCRIBE THE PARAMETER
   * @param wasAdded DESCRIBE THE PARAMETER
   */
  public void routeSetChange(NodeHandle nh, boolean wasAdded) {
  }
}


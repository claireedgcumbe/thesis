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
import rice.pastry.messaging.*;

import java.util.*;

/**
 * PingMessageNew A performance test suite for pastry.
 *
 * @version $Id: PingMessageNew.java 2805 2005-11-17 16:22:24Z jeffh $
 * @author Rongmei Zhang
 */

public class PingMessageNew extends Message {
  private NodeId target;

  private int nHops = 0;
  private double fDistance = 0;

  /**
   * Constructor for PingMessageNew.
   *
   * @param pingAddress DESCRIBE THE PARAMETER
   * @param src DESCRIBE THE PARAMETER
   * @param tgt DESCRIBE THE PARAMETER
   */
  public PingMessageNew(Address pingAddress, NodeHandle src, NodeId tgt) {
    super(pingAddress);
    setSender(src);
    target = tgt;
  }

  /**
   * Gets the Hops attribute of the PingMessageNew object
   *
   * @return The Hops value
   */
  public int getHops() {
    return nHops;
  }

  /**
   * Gets the Distance attribute of the PingMessageNew object
   *
   * @return The Distance value
   */
  public double getDistance() {
    return fDistance;
  }

  /**
   * Gets the Source attribute of the PingMessageNew object
   *
   * @return The Source value
   */
  public NodeId getSource() {
    return getSender().getNodeId();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    String s = "";
    s += "ping from " + getSender().getNodeId() + " to " + target;
    return s;
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void incrHops() {
    nHops++;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param dist DESCRIBE THE PARAMETER
   */
  public void incrDistance(double dist) {
    fDistance += dist;
  }
}


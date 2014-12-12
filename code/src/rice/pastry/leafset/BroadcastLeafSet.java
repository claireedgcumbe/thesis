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
package rice.pastry.leafset;

import rice.pastry.*;
import rice.pastry.messaging.*;
import rice.pastry.security.*;

import java.io.*;
import java.util.*;

/**
 * Broadcast a leaf set to another node.
 *
 * @version $Id: BroadcastLeafSet.java 2556 2005-06-07 22:02:14Z jeffh $
 * @author Andrew Ladd
 */

public class BroadcastLeafSet extends Message implements Serializable {

  private NodeHandle fromNode;

  private LeafSet theLeafSet;

  private int theType;
  /**
   * DESCRIBE THE FIELD
   */
  public final static int Update = 0;

  /**
   * DESCRIBE THE FIELD
   */
  public final static int JoinInitial = 1;

  /**
   * DESCRIBE THE FIELD
   */
  public final static int JoinAdvertise = 2;

  /**
   * DESCRIBE THE FIELD
   */
  public final static int Correction = 3;

  /**
   * Constructor.
   *
   * @param from DESCRIBE THE PARAMETER
   * @param leafSet DESCRIBE THE PARAMETER
   * @param type DESCRIBE THE PARAMETER
   */

  public BroadcastLeafSet(NodeHandle from, LeafSet leafSet, int type) {
    super(new LeafSetProtocolAddress());
    fromNode = from;
    theLeafSet = leafSet;
    theType = type;
    setPriority(0);
  }

  /**
   * Constructor.
   *
   * @param cred the credentials.
   * @param from DESCRIBE THE PARAMETER
   * @param leafSet DESCRIBE THE PARAMETER
   * @param type DESCRIBE THE PARAMETER
   */

  public BroadcastLeafSet(Credentials cred, NodeHandle from, LeafSet leafSet,
                          int type) {
    super(new LeafSetProtocolAddress(), cred);

    fromNode = from;
    theLeafSet = leafSet;
    theType = type;
    setPriority(0);
  }

  /**
   * Constructor.
   *
   * @param stamp the timestamp
   * @param from DESCRIBE THE PARAMETER
   * @param leafSet DESCRIBE THE PARAMETER
   * @param type DESCRIBE THE PARAMETER
   */

  public BroadcastLeafSet(Date stamp, NodeHandle from, LeafSet leafSet, int type) {
    super(new LeafSetProtocolAddress(), stamp);

    fromNode = from;
    theLeafSet = leafSet;
    theType = type;
    setPriority(0);
  }

  /**
   * Constructor.
   *
   * @param cred the credentials.
   * @param stamp the timestamp
   * @param from DESCRIBE THE PARAMETER
   * @param leafSet DESCRIBE THE PARAMETER
   * @param type DESCRIBE THE PARAMETER
   */

  public BroadcastLeafSet(Credentials cred, Date stamp, NodeHandle from,
                          LeafSet leafSet, int type) {
    super(new LeafSetProtocolAddress(), cred, stamp);

    fromNode = from;
    theLeafSet = leafSet;
    theType = type;
    setPriority(0);
  }

  /**
   * Returns the node id of the node that broadcast its leaf set.
   *
   * @return the node id.
   */

  public NodeHandle from() {
    return fromNode;
  }

  /**
   * Returns the leaf set that was broadcast.
   *
   * @return the leaf set.
   */

  public LeafSet leafSet() {
    return theLeafSet;
  }

  /**
   * Returns the type of leaf set.
   *
   * @return the type.
   */

  public int type() {
    return theType;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    String s = "BroadcastLeafSet(of " + fromNode.getNodeId() + ":" + theLeafSet + ")";
    return s;
  }
}

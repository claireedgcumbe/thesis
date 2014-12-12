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
package rice.pastry.join;

import rice.pastry.*;
import rice.pastry.messaging.*;
import rice.pastry.security.*;

import java.io.*;
import java.util.*;

/**
 * Request for the join protocols on the local node to join the overlay.
 *
 * @version $Id: InitiateJoin.java 2902 2006-01-10 15:23:05Z jeffh $
 * @author Andrew Ladd
 */

public class InitiateJoin extends Message implements Serializable {
  private NodeHandle[] handle;

  /**
   * Constructor.
   *
   * @param nh the node handle that the join will begin from.
   */

  public InitiateJoin(NodeHandle nh) {
    super(new JoinAddress());
    handle = new NodeHandle[1];
    handle[0] = nh;
  }


  /**
   * Constructor for InitiateJoin.
   *
   * @param nh DESCRIBE THE PARAMETER
   */
  public InitiateJoin(NodeHandle[] nh) {
    super(new JoinAddress());

    handle = nh;
  }

  /**
   * Constructor.
   *
   * @param stamp the timestamp
   * @param nh the node handle that the join will begin from.
   */

  public InitiateJoin(Date stamp, NodeHandle[] nh) {
    super(new JoinAddress(), stamp);

    handle = nh;
  }

  /**
   * Constructor.
   *
   * @param cred the credentials
   * @param nh the node handle that the join will begin from.
   */

  public InitiateJoin(Credentials cred, NodeHandle[] nh) {
    super(new JoinAddress(), cred);

    handle = nh;
  }

  /**
   * Constructor.
   *
   * @param cred the credentials
   * @param stamp the timestamp
   * @param nh the node handle that the join will begin from.
   */

  public InitiateJoin(Credentials cred, Date stamp, NodeHandle[] nh) {
    super(new JoinAddress(), cred, stamp);

    handle = nh;
  }

  /**
   * Gets the handle for the join. Gets the first non-dead handle for the join.
   *
   * @return the handle.
   */

  public NodeHandle getHandle() {
    for (int i = 0; i < handle.length; i++) {
      if (handle[i].isAlive()) {
        return handle[i];
      }
    }
    return null;
  }
}


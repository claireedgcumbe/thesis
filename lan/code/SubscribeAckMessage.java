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

package rice.p2p.scribe.messaging;

import rice.*;
import rice.p2p.commonapi.*;
import rice.p2p.scribe.*;

/**
 * @(#) SubscribeAckMessage.java The ack for a subscribe message.
 *
 * @version $Id: SubscribeAckMessage.java 2302 2005-03-11 00:58:26Z jeffh $
 * @author Alan Mislove
 */
public class SubscribeAckMessage extends AbstractSubscribeMessage {

  /**
   * The contained path to the root
   */
  protected Id[] pathToRoot;

  /**
   * Constructor which takes a unique integer Id
   *
   * @param id The unique id
   * @param source The source address
   * @param topic DESCRIBE THE PARAMETER
   * @param pathToRoot DESCRIBE THE PARAMETER
   */
  public SubscribeAckMessage(NodeHandle source, Topic topic, Id[] pathToRoot, int id) {
    super(source, topic, id);

    this.pathToRoot = pathToRoot;
  }

  /**
   * Returns the path to the root for the node receiving this message
   *
   * @return The new path to the root for the node receiving this message
   */
  public Id[] getPathToRoot() {
    return pathToRoot;
  }

  /**
   * Returns a String representation of this ack
   *
   * @return A String
   */
  public String toString() {
    return "SubscribeAckMessage " + topic + " ID: " + id;
  }

}


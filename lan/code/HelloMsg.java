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
/*
 *  Created on Dec 22, 2003
 *
 */
package rice.pastry.testing;

import rice.p2p.commonapi.Id;
import rice.pastry.NodeHandle;
import rice.pastry.messaging.Address;
import rice.pastry.messaging.Message;

/**
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public class HelloMsg extends Message {
  /**
   * DESCRIBE THE FIELD
   */
  public Id target;
  private int msgid;
  /**
   * DESCRIBE THE FIELD
   */
  public boolean messageDirect = false;
  private NodeHandle src;

  /**
   * Constructor for HelloMsg.
   *
   * @param addr DESCRIBE THE PARAMETER
   * @param src DESCRIBE THE PARAMETER
   * @param tgt DESCRIBE THE PARAMETER
   * @param mid DESCRIBE THE PARAMETER
   */
  public HelloMsg(Address addr, NodeHandle src, Id tgt, int mid) {
    super(addr);
    target = tgt;
    msgid = mid;
    this.src = src;
  }

  /**
   * Gets the Info attribute of the HelloMsg object
   *
   * @return The Info value
   */
  public String getInfo() {

    String s = toString();
    if (messageDirect) {
      s += " direct";
    } else {
      s += " routed";
    }
//    s += " lastAt:"+intermediateSource+" nextHop:"+nextHop+" from:" + source + " to:" + target;// +"<"+state+">"; // + " received by "+actualReceiver+"}";
    return s;
  }

  /**
   * Gets the Id attribute of the HelloMsg object
   *
   * @return The Id value
   */
  public int getId() {
    return msgid;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "Hello #" + msgid + " from " + src.getId();
  }

}

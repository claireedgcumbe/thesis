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
package rice.p2p.glacier.v2.messaging;

import rice.*;
import rice.p2p.commonapi.*;
import rice.p2p.glacier.*;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public abstract class GlacierMessage implements Message {

  // the unique id for this message
  /**
   * DESCRIBE THE FIELD
   */
  protected int id;

  // the tag of this message
  /**
   * DESCRIBE THE FIELD
   */
  protected char tag;

  // the source Id of this message
  /**
   * DESCRIBE THE FIELD
   */
  protected NodeHandle source;

  // the destination of this message
  /**
   * DESCRIBE THE FIELD
   */
  protected Id dest;

  // true if this is a response to a previous message
  /**
   * DESCRIBE THE FIELD
   */
  protected boolean isResponse;

  // serialver for backward compatibility
  private final static long serialVersionUID = -5849182107707420256L;

  /**
   * Constructor which takes a unique integer Id
   *
   * @param id The unique id
   * @param source The source address
   * @param dest The destination address
   * @param isResponse DESCRIBE THE PARAMETER
   * @param tag DESCRIBE THE PARAMETER
   */
  protected GlacierMessage(int id, NodeHandle source, Id dest, boolean isResponse, char tag) {
    this.id = id;
    this.source = source;
    this.dest = dest;
    this.isResponse = isResponse;
    this.tag = tag;
  }

  /**
   * Method which should return the priority level of this message. The messages
   * can range in priority from 0 (highest priority) to Integer.MAX_VALUE
   * (lowest) - when sending messages across the wire, the queue is sorted by
   * message priority. If the queue reaches its limit, the lowest priority
   * messages are discarded. Thus, applications which are very verbose should
   * have LOW_PRIORITY or lower, and applications which are somewhat quiet are
   * allowed to have MEDIUM_PRIORITY or possibly even HIGH_PRIORITY.
   *
   * @return This message's priority
   */
  public int getPriority() {
    return LOW_PRIORITY;
  }

  /**
   * Method which returns this messages' unique id
   *
   * @return The id of this message
   */
  public int getUID() {
    return id;
  }

  /**
   * Method which returns this messages' source address
   *
   * @return The source of this message
   */
  public NodeHandle getSource() {
    return source;
  }

  /**
   * Method which returns this messages' destination address
   *
   * @return The dest of this message
   */
  public Id getDestination() {
    return dest;
  }

  /**
   * Gets the Response attribute of the GlacierMessage object
   *
   * @return The Response value
   */
  public boolean isResponse() {
    return isResponse;
  }

  /**
   * Gets the Tag attribute of the GlacierMessage object
   *
   * @return The Tag value
   */
  public char getTag() {
    return tag;
  }
}


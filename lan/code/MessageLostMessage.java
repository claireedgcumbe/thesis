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

package rice.p2p.past.messaging;

import rice.*;
import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.p2p.commonapi.*;
import rice.p2p.past.*;

/**
 * @(#) MessageLostMessage.java This class represents a reminder to Past that an
 * outstanding message exists, and that the waiting continuation should be
 * informed if the message is lost.
 *
 * @version $Id: MessageLostMessage.java 3004 2006-02-01 16:10:12Z jstewart $
 * @author Alan Mislove
 * @author Ansley Post
 * @author Peter Druschel
 */
public class MessageLostMessage extends PastMessage {

  // the id the message was sent to
  /**
   * DESCRIBE THE FIELD
   */
  protected Id id;

  // the hint the message was sent to
  /**
   * DESCRIBE THE FIELD
   */
  protected NodeHandle hint;

  // the message
  /**
   * DESCRIBE THE FIELD
   */
  protected Message message;

  private final static long serialVersionUID = -8664827144233122095L;

  /**
   * Constructor which takes a unique integer Id and the local id
   *
   * @param uid The unique id
   * @param local The local nodehandle
   * @param id DESCRIBE THE PARAMETER
   * @param message DESCRIBE THE PARAMETER
   * @param hint DESCRIBE THE PARAMETER
   */
  public MessageLostMessage(int uid, NodeHandle local, Id id, Message message, NodeHandle hint) {
    super(uid, local, local.getId());

    setResponse();
    this.hint = hint;
    this.message = message;
    this.id = id;
  }

  /**
   * Method by which this message is supposed to return it's response - in this
   * case, it lets the continuation know that a the message was lost via the
   * receiveException method.
   *
   * @param c The continuation to return the reponse to.
   * @param env DESCRIBE THE PARAMETER
   * @param instance DESCRIBE THE PARAMETER
   */
  public void returnResponse(Continuation c, Environment env, String instance) {
    Logger logger = env.getLogManager().getLogger(getClass(), instance);
    Exception e = new PastException("Outgoing message '" + message + "' to " + id + "/" + hint + " was lost - please try again.");
    if (logger.level <= Logger.WARNING) {
      logger.logException("ERROR: Outgoing PAST message " + message + " with UID " + getUID() + " was lost", e);
    }
    c.receiveException(e);
  }

  /**
   * Returns a string representation of this message
   *
   * @return A string representing this message
   */
  public String toString() {
    return "[MessageLostMessage]";
  }
}


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

package rice.pastry.commonapi;

import rice.p2p.commonapi.Message;
import rice.pastry.NodeHandle;
import rice.pastry.messaging.Address;

/**
 * This class is an internal message to the commonapi gluecode.
 *
 * @version $Id: PastryEndpointMessage.java 2848 2005-12-15 14:54:02Z jstewart $
 * @author Alan Mislove
 * @author Peter Druschel
 */
public class PastryEndpointMessage extends rice.pastry.messaging.Message {

  /**
   * DESCRIBE THE FIELD
   */
  protected Message message;

  private final static long serialVersionUID = 4499456388556140871L;

  /**
   * Constructor.
   *
   * @param address DESCRIBE THE PARAMETER
   * @param message DESCRIBE THE PARAMETER
   * @param sender DESCRIBE THE PARAMETER
   */
  public PastryEndpointMessage(Address address, Message message, NodeHandle sender) {
    super(address);
    setSender(sender);
    this.message = message;
    setPriority(message.getPriority());
  }

  /**
   * Returns the internal message
   *
   * @return the credentials.
   */
  public Message getMessage() {
    return message;
  }


  /**
   * Returns the internal message
   *
   * @param message The new Message value
   */
  public void setMessage(Message message) {
    this.message = message;
  }

  /**
   * Returns the String representation of this message
   *
   * @return The string
   */
  public String toString() {
    return "[PEM " + message + "]";
  }
}



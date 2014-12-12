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

package rice.pastry.socket.messaging;

import java.net.*;
import java.io.*;

import rice.environment.Environment;
import rice.pastry.socket.*;
import rice.pastry.*;

/**
 * Class which represents a "ping" message sent through the socket pastry
 * system.
 *
 * @version $Id: WrongEpochMessage.java 2628 2005-07-05 16:08:25Z jeffh $
 * @author Alan Mislove
 */
public class WrongEpochMessage extends DatagramMessage {

  /**
   * DESCRIBE THE FIELD
   */
  protected EpochInetSocketAddress incorrect;
  /**
   * DESCRIBE THE FIELD
   */
  protected EpochInetSocketAddress correct;

  final static long serialVersionUID = 2838948342952784682L;

  /**
   * Constructor
   *
   * @param outbound DESCRIBE THE PARAMETER
   * @param inbound DESCRIBE THE PARAMETER
   * @param incorrect DESCRIBE THE PARAMETER
   * @param correct DESCRIBE THE PARAMETER
   * @param start DESCRIBE THE PARAMETER
   */
  public WrongEpochMessage(SourceRoute outbound, SourceRoute inbound, EpochInetSocketAddress incorrect, EpochInetSocketAddress correct, long start) {
    super(outbound, inbound, start);

    this.incorrect = incorrect;
    this.correct = correct;
  }

  /**
   * Gets the Incorrect attribute of the WrongEpochMessage object
   *
   * @return The Incorrect value
   */
  public EpochInetSocketAddress getIncorrect() {
    return incorrect;
  }

  /**
   * Gets the Correct attribute of the WrongEpochMessage object
   *
   * @return The Correct value
   */
  public EpochInetSocketAddress getCorrect() {
    return correct;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "PingResponseMessage";
  }
}

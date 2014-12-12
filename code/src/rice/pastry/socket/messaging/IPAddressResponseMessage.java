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

import java.io.*;
import java.net.*;

import rice.environment.Environment;
import rice.pastry.socket.*;
import rice.pastry.*;

/**
 * Class which represents a request for the external visible IP address
 *
 * @version $Id: IPAddressResponseMessage.java 2562 2005-06-09 16:26:21Z
 *      jstewart $
 * @author Alan Mislove
 */
public class IPAddressResponseMessage extends DatagramMessage {

  /**
   * DESCRIBE THE FIELD
   */
  protected InetSocketAddress address;

  /**
   * Constructor
   *
   * @param address DESCRIBE THE PARAMETER
   * @param start DESCRIBE THE PARAMETER
   */
  public IPAddressResponseMessage(InetSocketAddress address, long start) {
    super(null, null, start);

    this.address = address;
  }

  /**
   * Gets the Address attribute of the IPAddressResponseMessage object
   *
   * @return The Address value
   */
  public InetSocketAddress getAddress() {
    return address;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "IPAddressResponseMessage";
  }
}

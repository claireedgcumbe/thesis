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
package rice.pastry.standard;

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.pastry.*;

import java.net.*;
import java.security.*;

/**
 * Constructs NodeIds for virtual nodes derived from the IP address and port
 * number of this Java VM.
 *
 * @version $Id: IPNodeIdFactory.java 2808 2005-11-22 14:38:49Z jeffh $
 * @author Peter Druschel
 */

public class IPNodeIdFactory implements NodeIdFactory {

  private InetAddress localIP;

  private int port;

  Environment environment;

  /**
   * DESCRIBE THE FIELD
   */
  protected Logger logger;
  private static int nextInstance = 0;

  /**
   * Constructor.
   *
   * @param port the port number on which this Java VM listens
   * @param localIP DESCRIBE THE PARAMETER
   * @param env DESCRIBE THE PARAMETER
   */

  public IPNodeIdFactory(InetAddress localIP, int port, Environment env) {
    this.port = port;
    this.environment = env;
    this.logger = env.getLogManager().getLogger(getClass(), null);
    try {
      this.localIP = localIP;
      if (localIP.isLoopbackAddress()) {
        throw new Exception(
          "got loopback address: nodeIds will not be unique across computers!");
      }
    } catch (Exception e) {
      if (logger.level <= Logger.SEVERE) {
        logger.log(
          "ALERT: IPNodeIdFactory cannot determine local IP address: " + e);
      }
    }
  }

  /**
   * generate a nodeId multiple invocations result in a deterministic series of
   * randomized NodeIds, seeded by the IP address of the local host.
   *
   * @return the new nodeId
   */

  public NodeId generateNodeId() {
    byte rawIP[] = localIP.getAddress();

    byte rawPort[] = new byte[2];
    int tmp = port;
    for (int i = 0; i < 2; i++) {
      rawPort[i] = (byte) (tmp & 0xff);
      tmp >>= 8;
    }

    byte raw[] = new byte[4];
    tmp = ++nextInstance;
    for (int i = 0; i < 4; i++) {
      raw[i] = (byte) (tmp & 0xff);
      tmp >>= 8;
    }

    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException e) {
      if (logger.level <= Logger.SEVERE) {
        logger.log("No SHA support!");
      }
      throw new RuntimeException("No SHA support!", e);
    }

    md.update(rawIP);
    md.update(rawPort);
    md.update(raw);
    byte[] digest = md.digest();

    // now, we randomize the least significant 32 bits to ensure
    // that stale node handles are detected reliably.
    //	byte rand[] = new byte[4];
    //	rng.nextBytes(rand);
    //	for (int i=0; i<4; i++)
    //	    digest[i] = rand[i];

    NodeId nodeId = NodeId.buildNodeId(digest);

    return nodeId;
  }

}


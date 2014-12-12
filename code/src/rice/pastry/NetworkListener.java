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

package rice.pastry;

import java.net.*;

/**
 * Represents a listener to pastry network activity
 *
 * @version $Id: NetworkListener.java 2834 2005-12-13 14:14:38Z jeffh $
 * @author Peter Druschel
 */
public interface NetworkListener {

  /**
   * DESCRIBE THE FIELD
   */
  public static int TYPE_UDP = 0x001;
  /**
   * DESCRIBE THE FIELD
   */
  public static int TYPE_TCP = 0x010;
  /**
   * DESCRIBE THE FIELD
   */
  public static int TYPE_SR_UDP = 0x101;
  /**
   * DESCRIBE THE FIELD
   */
  public static int TYPE_SR_TCP = 0x110;

  /**
   * DESCRIBE THE FIELD
   */
  public static int REASON_NORMAL = 0;
  /**
   * DESCRIBE THE FIELD
   */
  public static int REASON_SR = 1;
  /**
   * DESCRIBE THE FIELD
   */
  public static int REASON_BOOTSTRAP = 2;

  /**
   * DESCRIBE THE FIELD
   */
  public static int REASON_ACC_NORMAL = 3;
  /**
   * DESCRIBE THE FIELD
   */
  public static int REASON_ACC_SR = 4;
  /**
   * DESCRIBE THE FIELD
   */
  public static int REASON_ACC_BOOTSTRAP = 5;

  /**
   * DESCRIBE THE METHOD
   *
   * @param addr DESCRIBE THE PARAMETER
   * @param reason DESCRIBE THE PARAMETER
   */
  public void channelOpened(InetSocketAddress addr, int reason);

  /**
   * DESCRIBE THE METHOD
   *
   * @param addr DESCRIBE THE PARAMETER
   */
  public void channelClosed(InetSocketAddress addr);

  /**
   * DESCRIBE THE METHOD
   *
   * @param message DESCRIBE THE PARAMETER
   * @param address DESCRIBE THE PARAMETER
   * @param size DESCRIBE THE PARAMETER
   * @param type DESCRIBE THE PARAMETER
   */
  public void dataSent(Object message, InetSocketAddress address, int size, int type);

  /**
   * DESCRIBE THE METHOD
   *
   * @param message DESCRIBE THE PARAMETER
   * @param address DESCRIBE THE PARAMETER
   * @param size DESCRIBE THE PARAMETER
   * @param type DESCRIBE THE PARAMETER
   */
  public void dataReceived(Object message, InetSocketAddress address, int size, int type);

}


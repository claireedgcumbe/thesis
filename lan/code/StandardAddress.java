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
import rice.pastry.messaging.*;

import java.security.*;

/**
 * Constructs an address for a specific class and instance name.
 *
 * @version $Id: StandardAddress.java 2808 2005-11-22 14:38:49Z jeffh $
 * @author Alan Mislove
 */
public class StandardAddress implements Address {

  /**
   * DESCRIBE THE FIELD
   */
  protected int myCode;

  /**
   * DESCRIBE THE FIELD
   */
  protected String name;

  //serial ver for backward compatibility
  private final static long serialVersionUID = 1564239935633411277L;

  /**
   * Constructor for StandardAddress.
   *
   * @param port DESCRIBE THE PARAMETER
   */
  public StandardAddress(int port) {
    this.myCode = port;
  }

  /**
   * Constructor for StandardAddress.
   *
   * @param c DESCRIBE THE PARAMETER
   * @param instance DESCRIBE THE PARAMETER
   * @param env DESCRIBE THE PARAMETER
   */
  public StandardAddress(Class c, String instance, Environment env) {
    MessageDigest md = null;

    try {
      md = MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException e) {
      Logger logger = env.getLogManager().getLogger(getClass(), null);
      if (logger.level <= Logger.SEVERE) {
        logger.log(
          "No SHA support!");
      }
    }

    name = c.toString() + "-" + instance;

    md.update(name.getBytes());
    byte[] digest = md.digest();

    myCode = (digest[0] << 24) + (digest[1] << 16) +
      (digest[2] << 8) + digest[3];

  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public int hashCode() {
    return myCode;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param obj DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean equals(Object obj) {
    if (obj instanceof StandardAddress) {
      return ((StandardAddress) obj).myCode == myCode;
    } else {
      return false;
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "[StandardAddress: " + name + "]";
  }
}


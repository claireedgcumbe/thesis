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
package rice.p2p.glacier.v2;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public abstract class GlacierContinuation {

  /**
   * DESCRIBE THE FIELD
   */
  protected int myUID;

  /**
   * DESCRIBE THE FIELD
   */
  protected boolean terminated;

  /**
   * Gets the Timeout attribute of the GlacierContinuation object
   *
   * @return The Timeout value
   */
  public abstract long getTimeout();

  /**
   * Gets the MyUID attribute of the GlacierContinuation object
   *
   * @return The MyUID value
   */
  public int getMyUID() {
    return myUID;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param uid DESCRIBE THE PARAMETER
   */
  public void setup(int uid) {
    myUID = uid;
    terminated = false;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param result DESCRIBE THE PARAMETER
   */
  public abstract void receiveResult(Object result);

  /**
   * DESCRIBE THE METHOD
   *
   * @param exception DESCRIBE THE PARAMETER
   */
  public abstract void receiveException(Exception exception);

  /**
   * DESCRIBE THE METHOD
   */
  public abstract void timeoutExpired();

  /**
   * DESCRIBE THE METHOD
   */
  public void init() {
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param result DESCRIBE THE PARAMETER
   */
  public void syncReceiveResult(Object result) {
    if (!terminated) {
      receiveResult(result);
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param exception DESCRIBE THE PARAMETER
   */
  public void syncReceiveException(Exception exception) {
    if (!terminated) {
      receiveException(exception);
    }
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void syncTimeoutExpired() {
    if (!terminated) {
      timeoutExpired();
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean hasTerminated() {
    return terminated;
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void terminate() {
    terminated = true;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "Unknown continuation #" + getMyUID();
  }
}

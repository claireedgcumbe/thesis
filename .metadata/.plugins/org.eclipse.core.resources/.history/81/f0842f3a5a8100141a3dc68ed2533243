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
public class GlacierResponseMessage extends GlacierMessage {
  /**
   * DESCRIBE THE FIELD
   */
  protected FragmentKey[] keys;
  /**
   * DESCRIBE THE FIELD
   */
  protected long[] lifetimes;
  /**
   * DESCRIBE THE FIELD
   */
  protected boolean[] haveIt;
  /**
   * DESCRIBE THE FIELD
   */
  protected boolean[] authoritative;

  /**
   * Constructor for GlacierResponseMessage.
   *
   * @param uid DESCRIBE THE PARAMETER
   * @param key DESCRIBE THE PARAMETER
   * @param haveIt DESCRIBE THE PARAMETER
   * @param lifetime DESCRIBE THE PARAMETER
   * @param authoritative DESCRIBE THE PARAMETER
   * @param source DESCRIBE THE PARAMETER
   * @param dest DESCRIBE THE PARAMETER
   * @param isResponse DESCRIBE THE PARAMETER
   * @param tag DESCRIBE THE PARAMETER
   */
  public GlacierResponseMessage(int uid, FragmentKey key, boolean haveIt, long lifetime, boolean authoritative, NodeHandle source, Id dest, boolean isResponse, char tag) {
    this(uid, new FragmentKey[]{key}, new boolean[]{haveIt}, new long[]{lifetime}, new boolean[]{authoritative}, source, dest, isResponse, tag);
  }

  /**
   * Constructor for GlacierResponseMessage.
   *
   * @param uid DESCRIBE THE PARAMETER
   * @param keys DESCRIBE THE PARAMETER
   * @param haveIt DESCRIBE THE PARAMETER
   * @param lifetimes DESCRIBE THE PARAMETER
   * @param authoritative DESCRIBE THE PARAMETER
   * @param source DESCRIBE THE PARAMETER
   * @param dest DESCRIBE THE PARAMETER
   * @param isResponse DESCRIBE THE PARAMETER
   * @param tag DESCRIBE THE PARAMETER
   */
  public GlacierResponseMessage(int uid, FragmentKey[] keys, boolean[] haveIt, long[] lifetimes, boolean[] authoritative, NodeHandle source, Id dest, boolean isResponse, char tag) {
    super(uid, source, dest, isResponse, tag);

    this.keys = keys;
    this.haveIt = haveIt;
    this.authoritative = authoritative;
    this.lifetimes = lifetimes;
  }

  /**
   * Gets the Key attribute of the GlacierResponseMessage object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The Key value
   */
  public FragmentKey getKey(int index) {
    return keys[index];
  }

  /**
   * Gets the HaveIt attribute of the GlacierResponseMessage object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The HaveIt value
   */
  public boolean getHaveIt(int index) {
    return haveIt[index];
  }

  /**
   * Gets the Authoritative attribute of the GlacierResponseMessage object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The Authoritative value
   */
  public boolean getAuthoritative(int index) {
    return authoritative[index];
  }

  /**
   * Gets the Expiration attribute of the GlacierResponseMessage object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The Expiration value
   */
  public long getExpiration(int index) {
    return lifetimes[index];
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public int numKeys() {
    return keys.length;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "[GlacierResponse for " + keys[0] + " (" + (numKeys() - 1) + " more keys)]";
  }
}


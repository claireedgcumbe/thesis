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
public class GlacierFetchMessage extends GlacierMessage {
  /**
   * DESCRIBE THE FIELD
   */
  protected FragmentKey[] keys;
  /**
   * DESCRIBE THE FIELD
   */
  protected int request;

  /**
   * DESCRIBE THE FIELD
   */
  public final static int FETCH_FRAGMENT = 1;
  /**
   * DESCRIBE THE FIELD
   */
  public final static int FETCH_MANIFEST = 2;
  /**
   * DESCRIBE THE FIELD
   */
  public final static int FETCH_FRAGMENT_AND_MANIFEST = FETCH_FRAGMENT | FETCH_MANIFEST;

  /**
   * Constructor for GlacierFetchMessage.
   *
   * @param uid DESCRIBE THE PARAMETER
   * @param key DESCRIBE THE PARAMETER
   * @param request DESCRIBE THE PARAMETER
   * @param source DESCRIBE THE PARAMETER
   * @param dest DESCRIBE THE PARAMETER
   * @param tag DESCRIBE THE PARAMETER
   */
  public GlacierFetchMessage(int uid, FragmentKey key, int request, NodeHandle source, Id dest, char tag) {
    this(uid, new FragmentKey[]{key}, request, source, dest, tag);
  }

  /**
   * Constructor for GlacierFetchMessage.
   *
   * @param uid DESCRIBE THE PARAMETER
   * @param keys DESCRIBE THE PARAMETER
   * @param request DESCRIBE THE PARAMETER
   * @param source DESCRIBE THE PARAMETER
   * @param dest DESCRIBE THE PARAMETER
   * @param tag DESCRIBE THE PARAMETER
   */
  public GlacierFetchMessage(int uid, FragmentKey[] keys, int request, NodeHandle source, Id dest, char tag) {
    super(uid, source, dest, false, tag);

    this.keys = keys;
    this.request = request;
  }

  /**
   * Gets the AllKeys attribute of the GlacierFetchMessage object
   *
   * @return The AllKeys value
   */
  public FragmentKey[] getAllKeys() {
    return keys;
  }

  /**
   * Gets the Request attribute of the GlacierFetchMessage object
   *
   * @return The Request value
   */
  public int getRequest() {
    return request;
  }

  /**
   * Gets the NumKeys attribute of the GlacierFetchMessage object
   *
   * @return The NumKeys value
   */
  public int getNumKeys() {
    return keys.length;
  }

  /**
   * Gets the Key attribute of the GlacierFetchMessage object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The Key value
   */
  public FragmentKey getKey(int index) {
    return keys[index];
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "[GlacierFetch for " + keys[0] + " and " + (keys.length - 1) + " other keys, req=" + request + "]";
  }
}


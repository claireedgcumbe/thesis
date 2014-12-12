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
import rice.p2p.glacier.VersionKey;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class GlacierRefreshCompleteMessage extends GlacierMessage {

  /**
   * DESCRIBE THE FIELD
   */
  protected VersionKey[] keys;
  /**
   * DESCRIBE THE FIELD
   */
  protected int[] updates;

  /**
   * Constructor for GlacierRefreshCompleteMessage.
   *
   * @param uid DESCRIBE THE PARAMETER
   * @param keys DESCRIBE THE PARAMETER
   * @param updates DESCRIBE THE PARAMETER
   * @param source DESCRIBE THE PARAMETER
   * @param dest DESCRIBE THE PARAMETER
   * @param tag DESCRIBE THE PARAMETER
   */
  public GlacierRefreshCompleteMessage(int uid, VersionKey[] keys, int[] updates, NodeHandle source, Id dest, char tag) {
    super(uid, source, dest, true, tag);

    this.keys = keys;
    this.updates = updates;
  }

  /**
   * Gets the Key attribute of the GlacierRefreshCompleteMessage object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The Key value
   */
  public VersionKey getKey(int index) {
    return keys[index];
  }

  /**
   * Gets the Updates attribute of the GlacierRefreshCompleteMessage object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The Updates value
   */
  public long getUpdates(int index) {
    return updates[index];
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
    return "[GlacierRefreshComplete for " + keys[0] + " (" + (numKeys() - 1) + " more keys)]";
  }
}

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

import rice.p2p.commonapi.Id;
import rice.p2p.glacier.v2.*;
import rice.p2p.glacier.*;
import rice.Continuation;
import java.io.Serializable;

/**
 * DESCRIBE THE INTERFACE
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public interface GlacierPolicy {

  /**
   * DESCRIBE THE METHOD
   *
   * @param manifest DESCRIBE THE PARAMETER
   * @param key DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean checkSignature(Manifest manifest, VersionKey key);

  /**
   * DESCRIBE THE METHOD
   *
   * @param obj DESCRIBE THE PARAMETER
   * @param generateFragment DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public Fragment[] encodeObject(Serializable obj, boolean[] generateFragment);

  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   * @param obj DESCRIBE THE PARAMETER
   * @param fragments DESCRIBE THE PARAMETER
   * @param expiration DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public Manifest[] createManifests(VersionKey key, Serializable obj, Fragment[] fragments, long expiration);

  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   * @param manifest DESCRIBE THE PARAMETER
   * @param newExpiration DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public Manifest updateManifest(VersionKey key, Manifest manifest, long newExpiration);

  /**
   * DESCRIBE THE METHOD
   *
   * @param fragments DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public Serializable decodeObject(Fragment[] fragments);

  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   * @param command DESCRIBE THE PARAMETER
   */
  public void prefetchLocalObject(VersionKey key, Continuation command);

}
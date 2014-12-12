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

import java.io.Serializable;

import rice.*;
import rice.p2p.past.PastContentHandle;
import rice.p2p.past.gc.GCPastContentHandle;
import rice.p2p.commonapi.*;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class GlacierContentHandle implements PastContentHandle, GCPastContentHandle {

  /**
   * DESCRIBE THE FIELD
   */
  protected Id id;
  /**
   * DESCRIBE THE FIELD
   */
  protected NodeHandle nodeHandle;
  /**
   * DESCRIBE THE FIELD
   */
  protected Manifest manifest;
  /**
   * DESCRIBE THE FIELD
   */
  protected long version;

  /**
   * Constructor for GlacierContentHandle.
   *
   * @param id DESCRIBE THE PARAMETER
   * @param version DESCRIBE THE PARAMETER
   * @param nodeHandle DESCRIBE THE PARAMETER
   * @param manifest DESCRIBE THE PARAMETER
   */
  public GlacierContentHandle(Id id, long version, NodeHandle nodeHandle, Manifest manifest) {
    this.id = id;
    this.nodeHandle = nodeHandle;
    this.manifest = manifest;
    this.version = version;
  }

  /**
   * Gets the Id attribute of the GlacierContentHandle object
   *
   * @return The Id value
   */
  public Id getId() {
    return id;
  }

  /**
   * Gets the NodeHandle attribute of the GlacierContentHandle object
   *
   * @return The NodeHandle value
   */
  public NodeHandle getNodeHandle() {
    return nodeHandle;
  }

  /**
   * Gets the Version attribute of the GlacierContentHandle object
   *
   * @return The Version value
   */
  public long getVersion() {
    return version;
  }

  /**
   * Gets the Expiration attribute of the GlacierContentHandle object
   *
   * @return The Expiration value
   */
  public long getExpiration() {
    return manifest.expirationDate;
  }

  /**
   * Gets the Manifest attribute of the GlacierContentHandle object
   *
   * @return The Manifest value
   */
  public Manifest getManifest() {
    return manifest;
  }
}




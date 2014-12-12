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

import rice.p2p.past.*;
import rice.p2p.past.gc.*;
import rice.p2p.commonapi.*;
import rice.p2p.glacier.*;
import java.io.*;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class DebugContent implements PastContent, GCPastContent {

  /**
   * DESCRIBE THE FIELD
   */
  protected Id myId;
  /**
   * DESCRIBE THE FIELD
   */
  protected boolean isMutable;
  /**
   * DESCRIBE THE FIELD
   */
  protected long version;
  /**
   * DESCRIBE THE FIELD
   */
  protected transient byte[] payload;

  /**
   * Constructor for DebugContent.
   *
   * @param id DESCRIBE THE PARAMETER
   * @param isMutable DESCRIBE THE PARAMETER
   * @param version DESCRIBE THE PARAMETER
   * @param payload DESCRIBE THE PARAMETER
   */
  public DebugContent(Id id, boolean isMutable, long version, byte[] payload) {
    myId = id;
    this.isMutable = isMutable;
    this.version = version;
    this.payload = payload;
  }

  /**
   * Gets the Version attribute of the DebugContent object
   *
   * @return The Version value
   */
  public long getVersion() {
    return version;
  }

  /**
   * Gets the Handle attribute of the DebugContent object
   *
   * @param local DESCRIBE THE PARAMETER
   * @return The Handle value
   */
  public PastContentHandle getHandle(Past local) {
    return new DebugContentHandle(myId, version, GCPast.INFINITY_EXPIRATION, local.getLocalNodeHandle());
  }

  /**
   * Gets the Handle attribute of the DebugContent object
   *
   * @param local DESCRIBE THE PARAMETER
   * @param expiration DESCRIBE THE PARAMETER
   * @return The Handle value
   */
  public GCPastContentHandle getHandle(GCPast local, long expiration) {
    return new DebugContentHandle(myId, version, expiration, local.getLocalNodeHandle());
  }

  /**
   * Gets the Id attribute of the DebugContent object
   *
   * @return The Id value
   */
  public Id getId() {
    return myId;
  }

  /**
   * Gets the Mutable attribute of the DebugContent object
   *
   * @return The Mutable value
   */
  public boolean isMutable() {
    return this.isMutable;
  }

  /**
   * Gets the Payload attribute of the DebugContent object
   *
   * @return The Payload value
   */
  public byte[] getPayload() {
    return payload;
  }

  /**
   * Gets the Metadata attribute of the DebugContent object
   *
   * @param expiration DESCRIBE THE PARAMETER
   * @return The Metadata value
   */
  public GCPastMetadata getMetadata(long expiration) {
    return new GCPastMetadata(expiration);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param id DESCRIBE THE PARAMETER
   * @param existingContent DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   * @exception PastException DESCRIBE THE EXCEPTION
   */
  public PastContent checkInsert(Id id, PastContent existingContent) throws PastException {
    if (!isMutable || !(existingContent instanceof DebugContent)) {
      return this;
    }

    DebugContent dc = (DebugContent) existingContent;
    return (this.version > dc.version) ? this : dc;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param oos DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    oos.writeInt(payload.length);
    oos.write(payload);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param ois DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   * @exception ClassNotFoundException DESCRIBE THE EXCEPTION
   */
  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    payload = new byte[ois.readInt()];
    ois.readFully(payload, 0, payload.length);
  }
}


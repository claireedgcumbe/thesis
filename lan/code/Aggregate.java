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
package rice.p2p.aggregation;

import rice.p2p.past.gc.GCPastContent;
import rice.p2p.past.*;
import rice.p2p.past.gc.*;
import rice.p2p.commonapi.Id;
import rice.p2p.glacier.VersionKey;
import java.security.*;
import java.io.*;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class Aggregate implements GCPastContent {
  /**
   * DESCRIBE THE FIELD
   */
  protected GCPastContent[] components;
  /**
   * DESCRIBE THE FIELD
   */
  protected Id[] pointers;
  /**
   * DESCRIBE THE FIELD
   */
  protected Id myId;

  private final static long serialVersionUID = -4891386773008082L;

  /**
   * Constructor for Aggregate.
   *
   * @param components DESCRIBE THE PARAMETER
   * @param pointers DESCRIBE THE PARAMETER
   */
  public Aggregate(GCPastContent[] components, Id[] pointers) {
    this.components = components;
    this.myId = null;
    this.pointers = pointers;
  }

  /**
   * Gets the Id attribute of the Aggregate object
   *
   * @return The Id value
   */
  public Id getId() {
    return myId;
  }

  /**
   * Gets the Pointers attribute of the Aggregate object
   *
   * @return The Pointers value
   */
  public Id[] getPointers() {
    return pointers;
  }

  /**
   * Gets the Component attribute of the Aggregate object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The Component value
   */
  public GCPastContent getComponent(int index) {
    return components[index];
  }

  /**
   * Gets the Version attribute of the Aggregate object
   *
   * @return The Version value
   */
  public long getVersion() {
    return 0;
  }

  /**
   * Gets the Mutable attribute of the Aggregate object
   *
   * @return The Mutable value
   */
  public boolean isMutable() {
    return false;
  }

  /**
   * Gets the ContentHash attribute of the Aggregate object
   *
   * @return The ContentHash value
   */
  public byte[] getContentHash() {
    byte[] bytes = null;

    try {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

      objectStream.writeObject(components);
      objectStream.writeObject(pointers);
      objectStream.flush();

      bytes = byteStream.toByteArray();
    } catch (IOException ioe) {
      return null;
    }

    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException e) {
      return null;
    }

    md.reset();
    md.update(bytes);

    return md.digest();
  }

  /**
   * Gets the Handle attribute of the Aggregate object
   *
   * @param local DESCRIBE THE PARAMETER
   * @return The Handle value
   */
  public PastContentHandle getHandle(Past local) {
    return new AggregateHandle(local.getLocalNodeHandle(), myId, getVersion(), GCPast.INFINITY_EXPIRATION);
  }

  /**
   * Gets the Handle attribute of the Aggregate object
   *
   * @param local DESCRIBE THE PARAMETER
   * @param expiration DESCRIBE THE PARAMETER
   * @return The Handle value
   */
  public GCPastContentHandle getHandle(GCPast local, long expiration) {
    return new AggregateHandle(local.getLocalNodeHandle(), myId, getVersion(), expiration);
  }

  /**
   * Gets the Metadata attribute of the Aggregate object
   *
   * @param expiration DESCRIBE THE PARAMETER
   * @return The Metadata value
   */
  public GCPastMetadata getMetadata(long expiration) {
    return new GCPastMetadata(expiration);
  }

  /**
   * Sets the Id attribute of the Aggregate object
   *
   * @param myId The new Id value
   */
  public void setId(Id myId) {
    this.myId = myId;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public int numComponents() {
    return components.length;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param id DESCRIBE THE PARAMETER
   * @param existingContent DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   * @exception PastException DESCRIBE THE EXCEPTION
   */
  public PastContent checkInsert(rice.p2p.commonapi.Id id, PastContent existingContent) throws PastException {
    if (existingContent == null) {
      return this;
    } else {
      return existingContent;
    }
  }
}

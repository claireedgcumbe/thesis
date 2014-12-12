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

package rice.p2p.past;

import java.io.Serializable;

import rice.*;
import rice.p2p.commonapi.*;

/**
 * @(#) ContentHashPastContent.java An abstract class for content-hash objects
 * stored in Past. Provided as a convenience.
 *
 * @version $Id: ContentHashPastContent.java 2688 2005-08-03 01:45:34Z jstewart
 *      $
 * @author Peter Druschel
 * @author Alan Mislove
 */
public abstract class ContentHashPastContent implements PastContent {

  /**
   * DESCRIBE THE FIELD
   */
  protected Id myId;

  private final static long serialVersionUID = 6375789163758367025L;

  // ----- PastCONTENT METHODS -----

  /**
   * Constructor for ContentHashPastContent.
   *
   * @param myId DESCRIBE THE PARAMETER
   */
  public ContentHashPastContent(Id myId) {
    this.myId = myId;
  }

  /**
   * Produces a handle for this content object. The handle is retrieved and
   * returned to the client as a result of the Past.lookupHandles() method.
   *
   * @param local The local past service
   * @return the handle
   */
  public PastContentHandle getHandle(Past local) {
    return new ContentHashPastContentHandle(local.getLocalNodeHandle(), getId());
  }

  /**
   * Returns the Id under which this object is stored in Past.
   *
   * @return the id
   */
  public Id getId() {
    return myId;
  }

  /**
   * States if this content object is mutable. Mutable objects are not subject
   * to dynamic caching in Past.
   *
   * @return true if this object is mutable, else false
   */
  public boolean isMutable() {
    return false;
  }

  /**
   * Checks if a insert operation should be allowed. Invoked when a Past node
   * receives an insert request and it is a replica root for the id; invoked on
   * the object to be inserted. This method determines the effect of an insert
   * operation on an object that already exists: it computes the new value of
   * the stored object, as a function of the new and the existing object.
   *
   * @param id the key identifying the object
   * @param existingContent DESCRIBE THE PARAMETER
   * @return null, if the operation is not allowed; else, the new object to be
   *      stored on the local node.
   * @exception PastException DESCRIBE THE EXCEPTION
   */
  public PastContent checkInsert(Id id, PastContent existingContent) throws PastException {
    // can't overwrite content hash objects
    if (existingContent != null) {
      throw new PastException("ContentHashPastContent: can't insert, object already exists");
    }

    // only allow correct content hash key
    if (!id.equals(getId())) {
      throw new PastException("ContentHashPastContent: can't insert, content hash incorrect");
    }
    return this;
  }
}






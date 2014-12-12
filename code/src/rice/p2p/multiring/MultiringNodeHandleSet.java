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
package rice.p2p.multiring;

import java.util.*;

import rice.p2p.commonapi.*;

/**
 * @(#) MultiringNodeHandleSet.java An implementation of a NodeHandleSet for use
 * with multiple rings
 *
 * @version $Id: MultiringNodeHandleSet.java 2302 2005-03-11 00:58:26Z jeffh $
 * @author Alan Mislove
 */
public class MultiringNodeHandleSet implements NodeHandleSet {

  /**
   * The actual node handle set
   */
  protected NodeHandleSet set;

  /**
   * The handle's ringId
   */
  protected Id ringId;

  /**
   * Constructor
   *
   * @param ringId DESCRIBE THE PARAMETER
   * @param set DESCRIBE THE PARAMETER
   */
  protected MultiringNodeHandleSet(Id ringId, NodeHandleSet set) {
    this.ringId = ringId;
    this.set = set;

    if ((ringId instanceof RingId) || (set instanceof MultiringNodeHandleSet)) {
      throw new IllegalArgumentException("Illegal creation of MRNodeHandleSet: " + ringId.getClass() + ", " + set.getClass());
    }
  }

  /**
   * Returns the internal set
   *
   * @return The internal set
   */
  protected NodeHandleSet getSet() {
    return set;
  }

  /**
   * Finds the NodeHandle associated with the NodeId.
   *
   * @param id a node id.
   * @return the handle associated with that id or null if no such handle is
   *      found.
   */
  public NodeHandle getHandle(Id id) {
    NodeHandle handle = set.getHandle(((RingId) id).getId());

    if (handle != null) {
      return new MultiringNodeHandle(ringId, handle);
    } else {
      return null;
    }
  }

  /**
   * Gets the ith element in the set.
   *
   * @param i an index.
   * @return the handle associated with that id or null if no such handle is
   *      found.
   */
  public NodeHandle getHandle(int i) {
    NodeHandle handle = set.getHandle(i);

    if (handle != null) {
      return new MultiringNodeHandle(ringId, handle);
    } else {
      return null;
    }
  }

  /**
   * Gets the index of the element with the given node id.
   *
   * @param id the id.
   * @return the index or throws a NoSuchElementException.
   * @exception NoSuchElementException DESCRIBE THE EXCEPTION
   */
  public int getIndexHandle(Id id) throws NoSuchElementException {
    return set.getIndexHandle(((RingId) id).getId());
  }

  /**
   * Puts a NodeHandle into the set.
   *
   * @param handle the handle to put.
   * @return true if the put succeeded, false otherwise.
   */
  public boolean putHandle(NodeHandle handle) {
    return set.putHandle(((MultiringNodeHandle) handle).getHandle());
  }

  /**
   * Verifies if the set contains this particular id.
   *
   * @param id a node id.
   * @return true if that node id is in the set, false otherwise.
   */
  public boolean memberHandle(Id id) {
    return set.memberHandle(((RingId) id).getId());
  }

  /**
   * Removes a node id and its handle from the set.
   *
   * @param id DESCRIBE THE PARAMETER
   * @return the node handle removed or null if nothing.
   */
  public NodeHandle removeHandle(Id id) {
    NodeHandle handle = set.removeHandle(((RingId) id).getId());

    if (handle != null) {
      return new MultiringNodeHandle(ringId, handle);
    } else {
      return null;
    }
  }

  /**
   * Gets the size of the set.
   *
   * @return the size.
   */
  public int size() {
    return set.size();
  }

  /**
   * Determines equality
   *
   * @param o DESCRIBE THE PARAMETER
   * @return Equals
   */
  public boolean equals(Object o) {
    MultiringNodeHandleSet other = (MultiringNodeHandleSet) o;
    return (other.getSet().equals(set) && other.ringId.equals(ringId));
  }

  /**
   * Returns the hashCode
   *
   * @return hashCode
   */
  public int hashCode() {
    return (set.hashCode() + ringId.hashCode());
  }

  /**
   * Prints out the string
   *
   * @return A string
   */
  public String toString() {
    return "{RingId " + ringId + " " + set.toString() + "}";
  }

}

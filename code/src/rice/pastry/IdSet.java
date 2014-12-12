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

package rice.pastry;

import java.io.Serializable;
import java.util.*;
import java.security.*;
import rice.p2p.util.*;

/**
 * Represents a set of Pastry ids. *
 *
 * @version $Id: IdSet.java 2575 2005-06-14 19:48:34Z jeffh $
 * @author Peter Druschel
 */

public class IdSet implements rice.p2p.commonapi.IdSet {

  private SortedMap idSet;

  // a cache of the fingerprint hash
  private byte[] cachedHash;
  private boolean validHash;

  final static long serialVersionUID = -1565571743719309172L;

  /**
   * Constructor.
   */
  public IdSet() {
    idSet = new RedBlackMap();
    validHash = false;
  }

  /**
   * Constructor. constructs
   *
   * @param s the TreeSet based on which we construct a new IdSet
   */
  public IdSet(SortedMap s) {
    idSet = s;
    validHash = false;
  }

  /**
   * test membership
   *
   * @param id the id to test
   * @return true of id is a member, false otherwise
   */
  public boolean isMember(Id id) {
    return idSet.containsKey(id);
  }

  /**
   * return an iterator over the elements of this set
   *
   * @return the interator
   */
  public Iterator getIterator() {
    return idSet.keySet().iterator();
  }

  /**
   * compute a fingerprint of the members in this IdSet
   *
   * @return an Id containing the secure hash of this set
   */
  public byte[] getHash() {
    if (validHash) {
      return cachedHash;
    }

    // recompute the hash
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("No SHA support!", e);
      //return null;
    }

    Iterator it = getIterator();
    byte[] raw = new byte[Id.IdBitLength / 8];
    Id id;

    while (it.hasNext()) {
      id = (Id) it.next();
      id.blit(raw);
      md.update(raw);
    }

    cachedHash = md.digest();
    validHash = true;

    return cachedHash;
  }

  /**
   * test membership
   *
   * @param id the id to test
   * @return true of id is a member, false otherwise
   */
  public boolean isMemberId(rice.p2p.commonapi.Id id) {
    return isMember((Id) id);
  }

  /**
   * Copy constructor. constructs a shallow copy of the given IdSet o.
   *
   * @return DESCRIBE THE RETURN VALUE
   */
//  public IdSet(IdSet o) {
//    idSet = o.idSet;
//    cachedHash = o.cachedHash;
//    validHash = o.validHash;
//  }

  /**
   * return the number of elements
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public int numElements() {
    return idSet.size();
  }

  /**
   * add a member
   *
   * @param id the id to add
   */
  public void addMember(Id id) {
    idSet.put(id, null);
    validHash = false;
  }

  /**
   * remove a member
   *
   * @param id the id to remove
   */
  public void removeMember(Id id) {
    idSet.remove(id);
    validHash = false;
  }

  /**
   * return the smallest member id
   *
   * @return the smallest id in the set
   */
  public Id minMember() {
    return (Id) idSet.firstKey();
  }

  /**
   * return the largest member id
   *
   * @return the largest id in the set
   */
  public Id maxMember() {
    return (Id) idSet.lastKey();
  }

  /**
   * return a subset of this set, consisting of the member ids in a given range
   *
   * @param from the counterclockwise end of the range (inclusive)
   * @param to the clockwise end of the range (exclusive)
   * @return the subset
   */
  public IdSet subSet(Id from, Id to) {
    return new IdSet(idSet.subMap(from, to));
    /*
     *  IdSet res;
     *  if (from.compareTo(to) <= 0) {
     *  res = new IdSet(idSet.subMap(from, to));
     *  } else {
     *  res = new IdSet(idSet.tailMap(from));
     *  /SortedSet ss = idSet.tailSet(from);
     *  /ss.addAll(idSet.headSet(to));
     *  /res = new IdSet( (TreeSet) ss);
     *  res.idSet.putAll(idSet.headMap(to));
     *  }
     *  return res;
     */
  }

  /**
   * return a subset of this set, consisting of the member ids in a given range
   *
   * @param range the range
   * @return the subset
   */
  public IdSet subSet(IdRange range) {
    if (range.isEmpty()) {
      return new IdSet();
    } else if (range.getCCW().equals(range.getCW())) {
      return this;
    } else {
      return subSet(range.getCCW(), range.getCW());
    }
  }

  /**
   * Returns a string representation of the IdSet.
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    Iterator it = getIterator();
    Id key;
    String s = "[ IdSet: ";
    while (it.hasNext()) {
      key = (Id) it.next();
      s = s + key + ",";

    }
    s = s + " ]";
    return s;
  }

  // Common API Support

  /**
   * add a member
   *
   * @param id the id to add
   */
  public void addId(rice.p2p.commonapi.Id id) {
    addMember((Id) id);
  }

  /**
   * remove a member
   *
   * @param id the id to remove
   */
  public void removeId(rice.p2p.commonapi.Id id) {
    removeMember((Id) id);
  }

  /**
   * Returns a new, empty IdSet of this type
   *
   * @return A new IdSet
   */
  public rice.p2p.commonapi.IdSet build() {
    return new IdSet();
  }

  /**
   * return a subset of this set, consisting of the member ids in a given range
   *
   * @param range DESCRIBE THE PARAMETER
   * @return the subset
   */
  public rice.p2p.commonapi.IdSet subSet(rice.p2p.commonapi.IdRange range) {
    //return subSet((Id) range.getCWId(), (Id) range.getCCWId());
    return subSet((IdRange) range);
  }

  /**
   * return a hash of this set
   *
   * @return the hash of this set
   */
  public byte[] hash() {
    return getHash();
  }

  /**
   * return this set as an array
   *
   * @return the array
   */
  public rice.p2p.commonapi.Id[] asArray() {
    return (rice.p2p.commonapi.Id[]) idSet.keySet().toArray(new rice.p2p.commonapi.Id[0]);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public Object clone() {
    return new IdSet(idSet);
  }

}

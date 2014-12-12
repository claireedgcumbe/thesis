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

import java.io.Serializable;
import rice.p2p.commonapi.Id;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class ObjectDescriptor implements Serializable, Comparable {

  /**
   * DESCRIBE THE FIELD
   */
  public Id key;
  /**
   * DESCRIBE THE FIELD
   */
  public long version;
  /**
   * DESCRIBE THE FIELD
   */
  public long currentLifetime;
  /**
   * DESCRIBE THE FIELD
   */
  public long refreshedLifetime;
  /**
   * DESCRIBE THE FIELD
   */
  public int size;

  private final static long serialVersionUID = -3035115249019556223L;

  /**
   * Constructor for ObjectDescriptor.
   *
   * @param key DESCRIBE THE PARAMETER
   * @param version DESCRIBE THE PARAMETER
   * @param currentLifetime DESCRIBE THE PARAMETER
   * @param refreshedLifetime DESCRIBE THE PARAMETER
   * @param size DESCRIBE THE PARAMETER
   */
  public ObjectDescriptor(Id key, long version, long currentLifetime, long refreshedLifetime, int size) {
    this.key = key;
    this.currentLifetime = currentLifetime;
    this.refreshedLifetime = refreshedLifetime;
    this.size = size;
    this.version = version;
  }

  /**
   * Gets the AliveAt attribute of the ObjectDescriptor object
   *
   * @param pointInTime DESCRIBE THE PARAMETER
   * @return The AliveAt value
   */
  public boolean isAliveAt(long pointInTime) {
    return (currentLifetime > pointInTime) || (refreshedLifetime > pointInTime);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "objDesc[" + key.toStringFull() + "v" + version + ", lt=" + currentLifetime + ", rt=" + refreshedLifetime + ", size=" + size + "]";
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param other DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public int compareTo(Object other) {
    ObjectDescriptor metadata = (ObjectDescriptor) other;

    int result = this.key.compareTo(metadata.key);
    if (result != 0) {
      return result;
    }

    if (metadata.version > this.version) {
      return -1;
    }
    if (metadata.version < this.version) {
      return 1;
    }

    if (metadata.currentLifetime > this.currentLifetime) {
      return -1;
    }
    if (metadata.currentLifetime < this.currentLifetime) {
      return 1;
    }

    if (metadata.refreshedLifetime > this.refreshedLifetime) {
      return -1;
    }
    if (metadata.refreshedLifetime < this.refreshedLifetime) {
      return 1;
    }

    if (metadata.size > this.size) {
      return -1;
    } else if (metadata.size < this.size) {
      return 1;
    }

    return 0;
  }
}


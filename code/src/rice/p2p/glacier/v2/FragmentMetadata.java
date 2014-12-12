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

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class FragmentMetadata implements Serializable, Comparable {

  /**
   * DESCRIBE THE FIELD
   */
  protected long currentExpirationDate;
  /**
   * DESCRIBE THE FIELD
   */
  protected long previousExpirationDate;
  /**
   * DESCRIBE THE FIELD
   */
  protected long storedSince;

  private final static long serialVersionUID = 3380538644355999384L;

  /**
   * Constructor for FragmentMetadata.
   *
   * @param currentExpirationDate DESCRIBE THE PARAMETER
   * @param previousExpirationDate DESCRIBE THE PARAMETER
   * @param storedSince DESCRIBE THE PARAMETER
   */
  public FragmentMetadata(long currentExpirationDate, long previousExpirationDate, long storedSince) {
    this.currentExpirationDate = currentExpirationDate;
    this.previousExpirationDate = previousExpirationDate;
    this.storedSince = storedSince;
  }

  /**
   * Gets the CurrentExpiration attribute of the FragmentMetadata object
   *
   * @return The CurrentExpiration value
   */
  long getCurrentExpiration() {
    return currentExpirationDate;
  }

  /**
   * Gets the PreviousExpiration attribute of the FragmentMetadata object
   *
   * @return The PreviousExpiration value
   */
  long getPreviousExpiration() {
    return previousExpirationDate;
  }

  /**
   * Gets the StoredSince attribute of the FragmentMetadata object
   *
   * @return The StoredSince value
   */
  long getStoredSince() {
    return storedSince;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param object DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public int compareTo(Object object) {
    FragmentMetadata metadata = (FragmentMetadata) object;

    if (metadata.currentExpirationDate > currentExpirationDate) {
      return -1;
    } else if (metadata.currentExpirationDate < currentExpirationDate) {
      return 1;
    } else if (metadata.previousExpirationDate < previousExpirationDate) {
      return -1;
    } else if (metadata.previousExpirationDate > previousExpirationDate) {
      return 1;
    } else if (metadata.storedSince < storedSince) {
      return -1;
    } else if (metadata.storedSince > storedSince) {
      return 1;
    } else {
      return 0;
    }
  }
}

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

package rice.pastry.socket;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * Class which represets a source route to a remote IP address.
 *
 * @version $Id: EpochInetSocketAddress.java 2971 2006-01-30 15:29:33Z jeffh $
 * @author Alan Mislove
 */
public class EpochInetSocketAddress implements Serializable {

  // the address
  /**
   * DESCRIBE THE FIELD
   */
  protected InetSocketAddress address;

  // the epoch number of the remote node
  /**
   * DESCRIBE THE FIELD
   */
  protected long epoch;

  /**
   */
  private final static long serialVersionUID = 2081191512212313338L;

  // a static epoch which indicates an unknown (and unmattering) epoch number
  /**
   * DESCRIBE THE FIELD
   */
  public final static long EPOCH_UNKNOWN = -1;

  /**
   * Constructor - don't use this unless you know what you are doing
   *
   * @param address The remote address
   */
  public EpochInetSocketAddress(InetSocketAddress address) {
    this(address, EPOCH_UNKNOWN);
  }

  /**
   * Constructor
   *
   * @param address The remote address
   * @param epoch The remote epoch
   */
  public EpochInetSocketAddress(InetSocketAddress address, long epoch) {
    this.address = address;
    this.epoch = epoch;
  }

  /**
   * Method which returns the address of this address
   *
   * @return The address
   */
  public InetSocketAddress getAddress() {
    return address;
  }

  /**
   * Method which returns the epoch of this address
   *
   * @return The epoch
   */
  public long getEpoch() {
    return epoch;
  }

  /**
   * Returns the hashCode of this source route
   *
   * @return The hashCode
   */
  public int hashCode() {
    return (int) (address.hashCode() ^ epoch);
  }

  /**
   * Checks equaltiy on source routes
   *
   * @param o The source route to compare to
   * @return The equality
   */
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    EpochInetSocketAddress that = (EpochInetSocketAddress) o;
    if (this.epoch != that.epoch) {
      return false;
    }
    return (this.address.equals(that.address));
  }

  /**
   * Internal method for computing the toString of an array of
   * InetSocketAddresses
   *
   * @return THe string
   */
  public String toString() {
    return address.toString() + " [" + epoch + "]";
  }
}


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

package rice.p2p.replication.messaging;

import rice.p2p.commonapi.*;
import rice.p2p.replication.*;
import rice.p2p.util.*;

/**
 * @(#) RequestMessage.java This class represents a request for a set of keys in
 * the replication system.
 *
 * @version $Id: RequestMessage.java 2302 2005-03-11 00:58:26Z jeffh $
 * @author Alan Mislove
 */
public class RequestMessage extends ReplicationMessage {

  // the list of ranges for this message
  /**
   * DESCRIBE THE FIELD
   */
  protected IdRange[] ranges;

  // the list of hashes for this message
  /**
   * DESCRIBE THE FIELD
   */
  protected IdBloomFilter[] filters;

  /**
   * Constructor which takes a unique integer Id
   *
   * @param source The source address
   * @param ranges DESCRIBE THE PARAMETER
   * @param filters DESCRIBE THE PARAMETER
   */
  public RequestMessage(NodeHandle source, IdRange[] ranges, IdBloomFilter[] filters) {
    super(source);

    this.ranges = ranges;
    this.filters = filters;
  }

  /**
   * Method which returns this messages' ranges
   *
   * @return The ranges of this message
   */
  public IdRange[] getRanges() {
    return ranges;
  }

  /**
   * Method which returns this messages' bloom filters
   *
   * @return The bloom filters of this message
   */
  public IdBloomFilter[] getFilters() {
    return filters;
  }
}


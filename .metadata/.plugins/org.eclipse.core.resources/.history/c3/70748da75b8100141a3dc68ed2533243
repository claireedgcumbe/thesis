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
/*
 *  Created on Aug 16, 2005
 */
package rice.pastry.leafset;

import rice.p2p.commonapi.*;

/**
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public class LSRangeCannotBeDeterminedException extends RangeCannotBeDeterminedException {

  /**
   * DESCRIBE THE FIELD
   */
  public int r;
  /**
   * DESCRIBE THE FIELD
   */
  public int pos;
  /**
   * DESCRIBE THE FIELD
   */
  public int uniqueCount;
  /**
   * DESCRIBE THE FIELD
   */
  public NodeHandle nh;

  /**
   * @param string
   * @param r DESCRIBE THE PARAMETER
   * @param pos DESCRIBE THE PARAMETER
   * @param uniqueNodes DESCRIBE THE PARAMETER
   * @param nh DESCRIBE THE PARAMETER
   * @param ls DESCRIBE THE PARAMETER
   */
  public LSRangeCannotBeDeterminedException(String string, int r, int pos, int uniqueNodes, NodeHandle nh, LeafSet ls) {
    super(string + " replication factor:" + r + " nh position:" + pos + " handle:" + nh + " ls.uniqueNodes():" + uniqueNodes + " " + ls.toString());
    this.r = r;
    this.pos = pos;
    this.nh = nh;
    this.uniqueCount = uniqueNodes;
  }

}

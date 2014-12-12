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
package rice.p2p.splitstream;

import java.io.*;

import rice.*;
import rice.p2p.commonapi.*;
import rice.p2p.scribe.*;

/**
 * This represents data sent through scribe for splitstream during a subscribe
 *
 * @version $Id: SplitStreamSubscribeContent.java 2302 2005-03-11 00:58:26Z
 *      jeffh $
 * @author Alan Mislove
 */
public class SplitStreamSubscribeContent implements ScribeContent {

  /**
   * The stage that the client attempting to join is in
   */
  protected int stage;

  /**
   * The first stage of the join process
   */
  public static int STAGE_NON_FINAL = -10;

  /**
   * The final stage of the join process
   */
  public static int STAGE_FINAL = -9;

  /**
   * Constructor taking in a byte[]
   *
   * @param stage DESCRIBE THE PARAMETER
   */
  public SplitStreamSubscribeContent(int stage) {
    this.stage = stage;
  }

  /**
   * Returns the data for this content
   *
   * @return The data for this content
   */
  public int getStage() {
    return stage;
  }
}


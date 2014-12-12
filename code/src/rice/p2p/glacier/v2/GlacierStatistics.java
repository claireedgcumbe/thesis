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

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.p2p.commonapi.IdRange;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class GlacierStatistics {
  /**
   * DESCRIBE THE FIELD
   */
  public int messagesSentByTag[];
  /**
   * DESCRIBE THE FIELD
   */
  public int pendingRequests;
  /**
   * DESCRIBE THE FIELD
   */
  public int numNeighbors;
  /**
   * DESCRIBE THE FIELD
   */
  public int numFragments;
  /**
   * DESCRIBE THE FIELD
   */
  public int numContinuations;
  /**
   * DESCRIBE THE FIELD
   */
  public int numObjectsInTrash;
  /**
   * DESCRIBE THE FIELD
   */
  public int activeFetches;
  /**
   * DESCRIBE THE FIELD
   */
  public IdRange responsibleRange;
  /**
   * DESCRIBE THE FIELD
   */
  public long fragmentStorageSize;
  /**
   * DESCRIBE THE FIELD
   */
  public long trashStorageSize;
  /**
   * DESCRIBE THE FIELD
   */
  public long tbegin;
  /**
   * DESCRIBE THE FIELD
   */
  public long bucketMin;
  /**
   * DESCRIBE THE FIELD
   */
  public long bucketMax;
  /**
   * DESCRIBE THE FIELD
   */
  public long bucketConsumed;
  /**
   * DESCRIBE THE FIELD
   */
  public long bucketTokensPerSecond;
  /**
   * DESCRIBE THE FIELD
   */
  public long bucketMaxBurstSize;
  /**
   * DESCRIBE THE FIELD
   */
  public Environment environment;

  /**
   * Constructor for GlacierStatistics.
   *
   * @param numTags DESCRIBE THE PARAMETER
   * @param env DESCRIBE THE PARAMETER
   */
  public GlacierStatistics(int numTags, Environment env) {
    this.environment = env;
    this.messagesSentByTag = new int[numTags];
    this.pendingRequests = 0;
    this.numNeighbors = 0;
    this.numFragments = 0;
    this.numContinuations = 0;
    this.numObjectsInTrash = 0;
    this.fragmentStorageSize = 0;
    this.trashStorageSize = 0;
    this.activeFetches = 0;
    this.tbegin = env.getTimeSource().currentTimeMillis();
    this.bucketMin = 0;
    this.bucketMax = 0;
    this.bucketConsumed = 0;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param logger DESCRIBE THE PARAMETER
   */
  public void dump(Logger logger) {
    String s = "";
    s += "@L.ME free=" + Runtime.getRuntime().freeMemory() + " max=" + Runtime.getRuntime().maxMemory() + " total=" + Runtime.getRuntime().totalMemory() + "\n";
    s += "@L.GL interval=" + tbegin + "-" + environment.getTimeSource().currentTimeMillis() + " range=" + responsibleRange + "\n";
    s += "@L.GL   neighbors=" + numNeighbors + " fragments=" + numFragments + " trash=" + numObjectsInTrash + "\n";
    s += "@L.GL   continuations=" + numContinuations + " pending=" + pendingRequests + "\n";
    s += "@L.GL   fragSizeBytes=" + fragmentStorageSize + " trashSizeBytes=" + trashStorageSize + "\n";
    s += "@L.GL   activeFetches=" + activeFetches + " bucketMin=" + bucketMin + " bucketMax=" + bucketMax + "\n";
    s += "@L.GL   bucketConsumed=" + bucketConsumed + "\n";
    s += "@L.GL   byTag=";
    for (int i = 0; i < messagesSentByTag.length; i++) {
      s += messagesSentByTag[i] + " ";
    }
    s += "\n";
    if (logger.level <= Logger.INFO) {
      logger.log(s);
    }
  }
}

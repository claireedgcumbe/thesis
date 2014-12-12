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
package rice.pastry.testing;

import rice.pastry.direct.TestRecord;

/**
 * PingAddress A performance test suite for pastry.
 *
 * @version $Id: PingTestRecord.java 2805 2005-11-17 16:22:24Z jeffh $
 * @author Rongmei Zhang
 */

public class PingTestRecord extends TestRecord {
  private int nIndex;

  private int nHops[];

  private double fProb[];

  private double fHops;

  private double fDistance = 0;

  /**
   * Constructor for PingTestRecord.
   *
   * @param n DESCRIBE THE PARAMETER
   * @param k DESCRIBE THE PARAMETER
   * @param baseBitLength DESCRIBE THE PARAMETER
   */
  public PingTestRecord(int n, int k, int baseBitLength) {
    super(n, k);

    nIndex = (int) Math.ceil(Math.log(n) / Math.log(Math.pow(2, baseBitLength)));
    nIndex *= 3;
    nHops = new int[nIndex * 2];
    fProb = new double[nIndex * 2];
  }

  /**
   * Gets the AveHops attribute of the PingTestRecord object
   *
   * @return The AveHops value
   */
  public double getAveHops() {
    return fHops;
  }

  /**
   * Gets the AveDistance attribute of the PingTestRecord object
   *
   * @return The AveDistance value
   */
  public double getAveDistance() {
    return fDistance;
  }

  /**
   * Gets the Probability attribute of the PingTestRecord object
   *
   * @return The Probability value
   */
  public double[] getProbability() {
    return fProb;
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void doneTest() {
    int i;
    //calculate averages ...
    long sum = 0;
    for (i = 0; i < nIndex; i++) {
      sum += nHops[i] * i;
    }
    fHops = ((double) sum) / nTests;
    fDistance = fDistance / nTests;

    for (i = 0; i < nIndex; i++) {
      fProb[i] = i * nHops[i] / ((double) sum);
    }
  }

  /**
   * Adds a feature to the Hops attribute of the PingTestRecord object
   *
   * @param index The feature to be added to the Hops attribute
   */
  public void addHops(int index) {
    nHops[index]++;
  }

  /**
   * Adds a feature to the Distance attribute of the PingTestRecord object
   *
   * @param rDistance The feature to be added to the Distance attribute
   */
  public void addDistance(double rDistance) {
    fDistance += rDistance;
  }
}


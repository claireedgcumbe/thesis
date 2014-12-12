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

import java.util.Arrays;

import rice.environment.Environment;
import rice.environment.logging.Logger;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class AggregationStatistics {
  /**
   * DESCRIBE THE FIELD
   */
  public final long granularity;
  /**
   * DESCRIBE THE FIELD
   */
  public int numObjectsTotal;
  /**
   * DESCRIBE THE FIELD
   */
  public int numObjectsAlive;
  /**
   * DESCRIBE THE FIELD
   */
  public int numAggregatesTotal;
  /**
   * DESCRIBE THE FIELD
   */
  public int numPointerArrays;
  /**
   * DESCRIBE THE FIELD
   */
  public int criticalAggregates;
  /**
   * DESCRIBE THE FIELD
   */
  public int orphanedAggregates;
  /**
   * DESCRIBE THE FIELD
   */
  public int[] objectLifetimeHisto;
  /**
   * DESCRIBE THE FIELD
   */
  public int[] aggregateLifetimeHisto;
  /**
   * DESCRIBE THE FIELD
   */
  public long totalObjectsSize;
  /**
   * DESCRIBE THE FIELD
   */
  public long liveObjectsSize;
  /**
   * DESCRIBE THE FIELD
   */
  public long time;
  private Environment environment;

  /**
   * Constructor for AggregationStatistics.
   *
   * @param histoLength DESCRIBE THE PARAMETER
   * @param granularityArg DESCRIBE THE PARAMETER
   * @param env DESCRIBE THE PARAMETER
   */
  public AggregationStatistics(int histoLength, long granularityArg, Environment env) {
    this.environment = env;
    numObjectsTotal = 0;
    numObjectsAlive = 0;
    numAggregatesTotal = 0;
    numPointerArrays = 0;
    totalObjectsSize = 0;
    liveObjectsSize = 0;
    granularity = granularityArg;
    objectLifetimeHisto = new int[histoLength];
    Arrays.fill(objectLifetimeHisto, 0);
    aggregateLifetimeHisto = new int[histoLength];
    Arrays.fill(aggregateLifetimeHisto, 0);
    time = environment.getTimeSource().currentTimeMillis();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param logger DESCRIBE THE PARAMETER
   */
  public void dump(Logger logger) {
    String s = "@L.AG interval=" + time + "-" + environment.getTimeSource().currentTimeMillis() + " granularity=" + granularity + "\n";
    s += "@L.AG   objsTotal=" + numObjectsTotal + " objsAlive=" + numObjectsAlive + "\n";
    s += "@L.AG   objBytesTotal=" + totalObjectsSize + " objBytesAlive=" + liveObjectsSize + "\n";
    s += "@L.AG   aggrTotal=" + numAggregatesTotal + " ptrArrays=" + numPointerArrays + " critical=" + criticalAggregates + " orphaned=" + orphanedAggregates;
    if (logger.level <= Logger.INFO) {
      logger.log(s);
    }
  }
}

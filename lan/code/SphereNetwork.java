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
package rice.pastry.direct;
import java.lang.*;

import java.util.*;

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.pastry.*;
import rice.pastry.messaging.*;

/**
 * Sphere network topology and idealized node life. Emulates a network of nodes
 * that are randomly placed on a sphere. Proximity is based on euclidean
 * distance on the sphere.
 *
 * @version $Id: SphereNetwork.java 2919 2006-01-16 12:54:15Z jeffh $
 * @author Y. Charlie Hu
 * @author Rongmei Zhang
 */
public class SphereNetwork extends BasicNetworkSimulator {

  /**
   * Constructor.
   *
   * @param env DESCRIBE THE PARAMETER
   */
  public SphereNetwork(Environment env) {
    super(env);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public NodeRecord generateNodeRecord() {
    return new SphereNodeRecord();
  }

  /**
   * Initialize a random Sphere NodeRecord
   *
   * @version $Id: SphereNetwork.java 2919 2006-01-16 12:54:15Z jeffh $
   * @author amislove
   */
  private class SphereNodeRecord implements NodeRecord {
    /**
     * DESCRIBE THE FIELD
     */
    public double theta, phi;

    /**
     * Constructor for NodeRecord.
     *
     */
    public SphereNodeRecord() {
      theta = Math.asin(2.0 * random.nextDouble() - 1.0);
      phi = 2.0 * Math.PI * random.nextDouble();
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param that DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public int proximity(NodeRecord that) {
      SphereNodeRecord nr = (SphereNodeRecord) that;
      return (int) (10000 * Math.acos(Math.cos(phi - nr.phi) * Math.cos(theta) * Math.cos(nr.theta) +
        Math.sin(theta) * Math.sin(nr.theta)));
    }
  }
}


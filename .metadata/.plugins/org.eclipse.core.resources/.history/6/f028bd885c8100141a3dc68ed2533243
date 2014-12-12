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

import rice.environment.Environment;
import rice.pastry.*;
import rice.pastry.messaging.*;

/**
 * Interface to an object which is simulating the network.
 *
 * @version $Id: NetworkSimulator.java 3038 2006-02-07 10:01:01Z jeffh $
 * @author Andrew Ladd
 */

public interface NetworkSimulator {

  /**
   * Gets the Environment attribute of the NetworkSimulator object
   *
   * @return The Environment value
   */
  public Environment getEnvironment();

  /**
   * Registers a node handle with the simulator.
   *
   * @param dpn DESCRIBE THE PARAMETER
   */
  public void registerNode(DirectPastryNode dpn);

  /**
   * Checks to see if a node id is alive.
   *
   * @param nh DESCRIBE THE PARAMETER
   * @return true if alive, false otherwise.
   */

  public boolean isAlive(DirectNodeHandle nh);

  /**
   * Determines proximity between two nodes.
   *
   * @param a a node id.
   * @param b another node id.
   * @return proximity of b to a.
   */

  public int proximity(DirectNodeHandle a, DirectNodeHandle b);

  /**
   * Deliver message.
   *
   * @param msg message to deliver.
   * @param node the Pastry node to deliver it to.
   * @param delay DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public ScheduledMessage deliverMessage(Message msg, DirectPastryNode node, int delay);

  /**
   * Deliver message.
   *
   * @param msg message to deliver.
   * @param node the Pastry node to deliver it to.
   * @param period to deliver the message after the delay
   * @param delay DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public ScheduledMessage deliverMessage(Message msg, DirectPastryNode node, int delay, int period);

  /**
   * Deliver message.
   *
   * @param msg message to deliver.
   * @param node the Pastry node to deliver it to.
   * @param period to deliver the message after the delay
   * @param delay DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public ScheduledMessage deliverMessageFixedRate(Message msg, DirectPastryNode node, int delay, int period);

  /**
   * Deliver message ASAP.
   *
   * @param msg message to deliver.
   * @param node the Pastry node to deliver it to.
   * @return DESCRIBE THE RETURN VALUE
   */
  public ScheduledMessage deliverMessage(Message msg, DirectPastryNode node);

  /**
   * Sets the TestRecord attribute of the NetworkSimulator object
   *
   * @param tr The new TestRecord value
   */
  public void setTestRecord(TestRecord tr);

  /**
   * Gets the TestRecord attribute of the NetworkSimulator object
   *
   * @return The TestRecord value
   */
  public TestRecord getTestRecord();

  /**
   * Returns the closest Node in proximity.
   *
   * @param nh DESCRIBE THE PARAMETER
   * @return
   */
  public DirectNodeHandle getClosest(DirectNodeHandle nh);

  /**
   * DESCRIBE THE METHOD
   *
   * @param dpn DESCRIBE THE PARAMETER
   */
  public void destroy(DirectPastryNode dpn);

  /**
   * Generates a random node record
   *
   * @return
   */
  public NodeRecord generateNodeRecord();

  /**
   * DESCRIBE THE METHOD
   *
   * @param node DESCRIBE THE PARAMETER
   */
  public void removeNode(DirectPastryNode node);

  /**
   * DESCRIBE THE METHOD
   */
  public void start();
}

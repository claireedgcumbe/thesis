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

import java.util.Hashtable;

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.pastry.*;
import rice.pastry.join.InitiateJoin;
import rice.pastry.messaging.Message;
import rice.selector.Timer;

/**
 * Direct pastry node. Subclasses PastryNode, and does about nothing else.
 *
 * @version $Id: DirectPastryNode.java 3062 2006-02-14 16:04:25Z jeffh $
 * @author Sitaram Iyer
 */

public class DirectPastryNode extends PastryNode {

  private NetworkSimulator simulator;
  /**
   * DESCRIBE THE FIELD
   */
  protected boolean alive = true;
  NodeRecord record;

  /**
   * DESCRIBE THE FIELD
   */
  protected Timer timer;

  Hashtable nodeHandles = new Hashtable();
  /**
   * Used for proximity calculation of DirectNodeHandle. This will probably go
   * away when we switch to a byte-level protocol.
   */
  public static DirectPastryNode currentNode = null;

  /**
   * Constructor for DirectPastryNode.
   *
   * @param id DESCRIBE THE PARAMETER
   * @param sim DESCRIBE THE PARAMETER
   * @param e DESCRIBE THE PARAMETER
   * @param nr DESCRIBE THE PARAMETER
   */
  public DirectPastryNode(NodeId id, NetworkSimulator sim, Environment e, NodeRecord nr) {
    super(id, e);
    timer = e.getSelectorManager().getTimer();
    simulator = sim;
    record = nr;
  }

  /**
   * Gets the Alive attribute of the DirectPastryNode object
   *
   * @return The Alive value
   */
  public boolean isAlive() {
    return alive;
  }

  /**
   * Gets the Logger attribute of the DirectPastryNode object
   *
   * @return The Logger value
   */
  public Logger getLogger() {
    return logger;
  }

  /**
   * Sets the DirectElements attribute of the DirectPastryNode object
   */
  public void setDirectElements(
  /*
   *  simulator
   */
    ) {
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param bootstrap DESCRIBE THE PARAMETER
   */
  public void doneNode(NodeHandle bootstrap) {
    initiateJoin(bootstrap);
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void destroy() {
    super.destroy();
    alive = false;
    setReady(false);
    simulator.removeNode(this);
  }


  /**
   * DESCRIBE THE METHOD
   *
   * @param bootstrap DESCRIBE THE PARAMETER
   */
  public final void initiateJoin(NodeHandle bootstrap) {
    NodeHandle[] boots = new NodeHandle[1];
    boots[0] = bootstrap;
    initiateJoin(boots);
  }

  /**
   * Sends an InitiateJoin message to itself.
   *
   * @param bootstrap Node handle to bootstrap with.
   */
  public final void initiateJoin(NodeHandle[] bootstrap) {
    if (bootstrap != null && bootstrap[0] != null) {
      simulator.deliverMessage(new InitiateJoin(bootstrap), this);
//      this.receiveMessage(new InitiateJoin(bootstrap));
    } else {
      setReady();
      // no bootstrap node, so ready immediately
    }
  }

  /**
   * Called from PastryNode after the join succeeds.
   */
  public final void nodeIsReady() {
  }

  /**
   * Schedule the specified message to be sent to the local node after a
   * specified delay. Useful to provide timeouts.
   *
   * @param msg a message that will be delivered to the local node after the
   *      specified delay
   * @param delay time in milliseconds before message is to be delivered
   * @return the scheduled event object; can be used to cancel the message
   */
  public ScheduledMessage scheduleMsg(Message msg, long delay) {
    return simulator.deliverMessage(msg, this, (int) delay);
  }

  /**
   * Schedule the specified message for repeated fixed-delay delivery to the
   * local node, beginning after the specified delay. Subsequent executions take
   * place at approximately regular intervals separated by the specified period.
   * Useful to initiate periodic tasks.
   *
   * @param msg a message that will be delivered to the local node after the
   *      specified delay
   * @param delay time in milliseconds before message is to be delivered
   * @param period time in milliseconds between successive message deliveries
   * @return the scheduled event object; can be used to cancel the message
   */
  public ScheduledMessage scheduleMsg(Message msg, long delay, long period) {
    return simulator.deliverMessage(msg, this, (int) delay, (int) period);
  }

  /**
   * Schedule the specified message for repeated fixed-rate delivery to the
   * local node, beginning after the specified delay. Subsequent executions take
   * place at approximately regular intervals, separated by the specified
   * period.
   *
   * @param msg a message that will be delivered to the local node after the
   *      specified delay
   * @param delay time in milliseconds before message is to be delivered
   * @param period time in milliseconds between successive message deliveries
   * @return the scheduled event object; can be used to cancel the message
   */
  public ScheduledMessage scheduleMsgAtFixedRate(Message msg, long delay,
                                                 long period) {
    return simulator.deliverMessageFixedRate(msg, this, (int) delay, (int) period);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param newHandle DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public NodeHandle coalesce(NodeHandle newHandle) {
    NodeHandle ret = (NodeHandle) nodeHandles.get(newHandle);
    if (ret == null) {
      nodeHandles.put(newHandle, newHandle);
      ret = newHandle;
    }
    return ret;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   */
  public synchronized void receiveMessage(Message msg) {
//    System.out.println("setting currentNode from "+currentNode+" to "+this+" on "+Thread.currentThread());
    if (!getEnvironment().getSelectorManager().isSelectorThread()) {
      simulator.deliverMessage(msg, this);
      return;
    }

    DirectPastryNode temp = currentNode;
//    if ((currentNode != null) && (currentNode != this))
//      throw new RuntimeException("receiveMessage called recursively!");
//    System.out.println("currentNode != null");
    currentNode = this;
    super.receiveMessage(msg);
    currentNode = temp;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param handle DESCRIBE THE PARAMETER
   * @param message DESCRIBE THE PARAMETER
   */
  public void send(NodeHandle handle, Message message) {
    handle.receiveMessage(message);
  }
}


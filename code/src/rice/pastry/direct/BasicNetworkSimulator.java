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
 *  Created on Nov 8, 2005
 */
package rice.pastry.direct;

import java.nio.channels.Selector;
import java.util.*;

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.environment.params.Parameters;
import rice.environment.random.RandomSource;
import rice.environment.random.simple.SimpleRandomSource;
import rice.environment.time.simulated.DirectTimeSource;
import rice.pastry.*;
import rice.pastry.messaging.Message;
import rice.pastry.routing.BroadcastRouteRow;
import rice.selector.SelectorManager;
import rice.selector.TimerTask;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public abstract class BasicNetworkSimulator implements NetworkSimulator {

  Vector nodes = new Vector();

  // these messages should be delivered when the timer expires
  /**
   * DESCRIBE THE FIELD
   */
  protected TreeSet taskQueue = new TreeSet();

  Environment environment;

  DirectTimeSource timeSource;

  private TestRecord testRecord;

  /**
   * DESCRIBE THE FIELD
   */
  protected Logger logger;

  /**
   * DESCRIBE THE FIELD
   */
  protected RandomSource random;

  /**
   * DESCRIBE THE FIELD
   */
  protected int MIN_DELAY = 1;

  /**
   * DESCRIBE THE FIELD
   */
  protected SelectorManager manager;

  boolean running = false;

  /**
   * Constructor for BasicNetworkSimulator.
   *
   * @param env DESCRIBE THE PARAMETER
   */
  public BasicNetworkSimulator(Environment env) {
    this.environment = env;
    manager = environment.getSelectorManager();
    Parameters params = env.getParameters();
    if (params.contains("pastry_direct_use_own_random")
      && params.getBoolean("pastry_direct_use_own_random")) {

      if (params.contains("pastry_direct_random_seed")
        && !params.getString("pastry_direct_random_seed").equalsIgnoreCase(
        "clock")) {
        this.random = new SimpleRandomSource(params.getLong("pastry_direct_random_seed"), env.getLogManager(),
          "direct");
      } else {
        this.random = new SimpleRandomSource(env.getLogManager(), "direct");
      }
    } else {
      this.random = env.getRandomSource();
    }
    this.logger = env.getLogManager().getLogger(getClass(), null);
    try {
      timeSource = (DirectTimeSource) env.getTimeSource();
    } catch (ClassCastException cce) {
      throw new IllegalArgumentException(
        "env.getTimeSource() must return a DirectTimeSource instead of a "
        + env.getTimeSource().getClass().getName());
    }
    testRecord = null;
    start();
  }

  /**
   * get TestRecord
   *
   * @return the returned TestRecord
   */
  public TestRecord getTestRecord() {
    return testRecord;
  }

  /**
   * testing if a NodeId is alive
   *
   * @param nh DESCRIBE THE PARAMETER
   * @return true if nid is alive false otherwise
   */
  public boolean isAlive(DirectNodeHandle nh) {
    return nh.getRemote().isAlive();
  }

  /**
   * find the closest NodeId to an input NodeId out of all NodeIds in the
   * network
   *
   * @param nh DESCRIBE THE PARAMETER
   * @return the NodeId closest to the input NodeId in the network
   */
  public DirectNodeHandle getClosest(DirectNodeHandle nh) {
    Iterator it = nodes.iterator();
    DirectNodeHandle bestHandle = null;
    int bestProx = Integer.MAX_VALUE;
    NodeId theId;

    while (it.hasNext()) {
      DirectPastryNode theNode = (DirectPastryNode) it.next();
      int theProx = theNode.record.proximity(nh.getRemote().record);
      theId = theNode.getNodeId();
      if (!theNode.isAlive() || !theNode.isReady()
        || theId.equals(nh.getNodeId())) {
        continue;
      }

      if (theProx < bestProx) {
        bestProx = theProx;
        bestHandle = (DirectNodeHandle) theNode.getLocalHandle();
      }
    }
    return bestHandle;
  }

  /**
   * Gets the Environment attribute of the BasicNetworkSimulator object
   *
   * @return The Environment value
   */
  public Environment getEnvironment() {
    return environment;
  }

  /**
   * set TestRecord
   *
   * @param tr input TestRecord
   */
  public void setTestRecord(TestRecord tr) {
    testRecord = tr;
  }
  // Invariant: only modified on the selector

  /**
   * DESCRIBE THE METHOD
   */
  public void start() {
    // this makes things single threaded
    manager.invoke(
      new Runnable() {
        public void run() {
          if (running) {
            return;
          }
          running = true;
          manager.invoke(
            new Runnable() {
              public void run() {
                if (!running) {
                  return;
                }
                if (!simulate()) {
                  Selector sel = manager.getSelector();
                  synchronized (sel) {
                    try {
                      sel.wait(100);
                      // must wait on the real clock, because the simulated clock can only be advanced by simulate()
                    } catch (InterruptedException ie) {
                      logger.logException("BasicNetworkSimulator interrupted.", ie);
                    }
                  }
                }
                manager.invoke(this);
              }
            });
        }
      });
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void stop() {
    manager.invoke(
      new Runnable() {
        public void run() {
          running = false;
        }
      });
  }

  /**
   * Adds a feature to the Task attribute of the BasicNetworkSimulator object
   *
   * @param dtt The feature to be added to the Task attribute
   */
  private void addTask(DirectTimerTask dtt) {
    if (logger.level <= Logger.FINE) {
      logger.log("addTask(" + dtt + ")");
    }
//    System.out.println("addTask("+dtt+")");
    synchronized (taskQueue) {
      taskQueue.add(dtt);
    }
//    start();
//    if (!manager.isSelectorThread()) Thread.yield();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   * @param node DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public ScheduledMessage deliverMessage(Message msg, DirectPastryNode node) {
    if (logger.level <= Logger.FINE) {
      logger.log("GNS: deliver " + msg + " to " + node);
    }
    DirectTimerTask dtt = null;
    if (msg.getSender() == null || msg.getSender().isAlive()) {
      MessageDelivery md = new MessageDelivery(msg, node);
      dtt = new DirectTimerTask(md, timeSource.currentTimeMillis() + MIN_DELAY);
      addTask(dtt);
    }
    return dtt;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   * @param node DESCRIBE THE PARAMETER
   * @param delay DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public ScheduledMessage deliverMessage(Message msg, DirectPastryNode node,
                                         int delay) {
    DirectTimerTask dtt = null;
    if (msg.getSender().isAlive()) {
      MessageDelivery md = new MessageDelivery(msg, node);
      dtt = new DirectTimerTask(md, timeSource.currentTimeMillis() + delay);
      addTask(dtt);
    }
    return dtt;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   * @param node DESCRIBE THE PARAMETER
   * @param delay DESCRIBE THE PARAMETER
   * @param period DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public ScheduledMessage deliverMessage(Message msg, DirectPastryNode node,
                                         int delay, int period) {
    DirectTimerTask dtt = null;
    if (msg.getSender().isAlive()) {
      MessageDelivery md = new MessageDelivery(msg, node);
      dtt = new DirectTimerTask(md, timeSource.currentTimeMillis() + delay,
        period);
      addTask(dtt);
    }
    return dtt;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   * @param node DESCRIBE THE PARAMETER
   * @param delay DESCRIBE THE PARAMETER
   * @param period DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public ScheduledMessage deliverMessageFixedRate(Message msg,
                                                  DirectPastryNode node, int delay, int period) {
    DirectTimerTask dtt = null;
    if (msg.getSender().isAlive()) {
      MessageDelivery md = new MessageDelivery(msg, node);
      dtt = new DirectTimerTask(md, timeSource.currentTimeMillis() + delay,
        period, true);
      addTask(dtt);
    }
    return dtt;
  }

  /**
   * Delivers 1 message. Will advance the clock if necessary. If there is a
   * message in the queue, deliver that and return true. If there is a message
   * in the taskQueue, update the clock if necessary, deliver that, then return
   * true. If both are empty, return false;
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  private boolean simulate() {
    if (!environment.getSelectorManager().isSelectorThread()) {
      throw new RuntimeException("Must be on selector thread");
    }

    DirectTimerTask task;
    synchronized (taskQueue) {
      // take a task from the taskQueue
      if (taskQueue.isEmpty()) {
        if (logger.level <= Logger.INFO) {
          logger.log("taskQueue is empty");
        }
        return false;
      }
      task = (DirectTimerTask) taskQueue.first();
      if (logger.level <= Logger.INFO) {
        logger.log("simulate():" + task);
      }
      taskQueue.remove(task);
    }
    // increment the clock if needed
    if (task.scheduledExecutionTime() > timeSource.currentTimeMillis()) {
      if (logger.level <= Logger.FINER) {
        logger.log("the time is now " + task.scheduledExecutionTime());
      }
      timeSource.setTime(task.scheduledExecutionTime());
    }

    if (task.execute(timeSource)) {
      addTask(task);
    }
    return true;
  }

  /**
   * set the liveliness of a NodeId
   *
   * @param node DESCRIBE THE PARAMETER
   */
  public void destroy(DirectPastryNode node) {
    node.destroy();
    // NodeRecord nr = (NodeRecord) nodeMap.get(nid);
    //
    // if (nr == null) {
    // throw new Error("setting node alive for unknown node");
    // }
    //
    // if (nr.alive != alive) {
    // nr.alive = alive;
    //
    // DirectNodeHandle[] handles = (DirectNodeHandle[]) nr.handles.toArray(new
    // DirectNodeHandle[0]);
    //
    // for (int i = 0; i < handles.length; i++) {
    // if (alive) {
    // handles[i].notifyObservers(NodeHandle.DECLARED_LIVE);
    // } else {
    // handles[i].notifyObservers(NodeHandle.DECLARED_DEAD);
    // }
    // }
    // }
  }

  /**
   * computes the proximity between two NodeIds
   *
   * @param a the first NodeId
   * @param b the second NodeId
   * @return the proximity between the two input NodeIds
   */
  public int proximity(DirectNodeHandle a, DirectNodeHandle b) {
    NodeRecord nra = a.getRemote().record;
    NodeRecord nrb = b.getRemote().record;

    if (nra == null || nrb == null) {
      throw new Error("asking about node proximity for unknown node(s)");
    }

    return nra.proximity(nrb);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param dpn DESCRIBE THE PARAMETER
   */
  public void registerNode(DirectPastryNode dpn) {
    nodes.add(dpn);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param node DESCRIBE THE PARAMETER
   */
  public void removeNode(DirectPastryNode node) {
    nodes.remove(node);
  }

}

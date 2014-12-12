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
package rice.pastry;

import java.util.*;

import rice.*;
import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.pastry.client.PastryAppl;
import rice.pastry.leafset.LeafSet;
import rice.pastry.messaging.*;
import rice.pastry.routing.RouteMessage;
import rice.pastry.routing.RoutingTable;
import rice.pastry.security.*;

/**
 * A Pastry node is single entity in the pastry network.
 *
 * @version $Id: PastryNode.java 3010 2006-02-02 14:10:25Z jeffh $
 * @author Andrew Ladd
 */

public abstract class PastryNode extends Observable implements MessageReceiver, rice.p2p.commonapi.Node, Destructable {

  /**
   * DESCRIBE THE FIELD
   */
  protected NodeId myNodeId;

  private Environment myEnvironment;

  private PastrySecurityManager mySecurityManager;

  private MessageDispatch myMessageDispatch;

  private LeafSet leafSet;

  private RoutingTable routeSet;

  /**
   * DESCRIBE THE FIELD
   */
  protected NodeHandle localhandle;

  private boolean ready;

  /**
   * DESCRIBE THE FIELD
   */
  protected Vector apps;

  /**
   * DESCRIBE THE FIELD
   */
  protected Logger logger;

  /**
   * This variable makes it so notifyReady() is only called on the apps once.
   * Deprecating
   */
  private boolean neverBeenReady = true;


  /**
   * Constructor, with NodeId. Need to set the node's ID before this node is
   * inserted as localHandle.localNode.
   *
   * @param id DESCRIBE THE PARAMETER
   * @param e DESCRIBE THE PARAMETER
   */
  protected PastryNode(NodeId id, Environment e) {
    myEnvironment = e;
    myNodeId = id;
    ready = false;
    apps = new Vector();
    logger = e.getLogManager().getLogger(getClass(), null);
  }

  /**
   * Gets the LocalNodeHandle attribute of the PastryNode object
   *
   * @return The LocalNodeHandle value
   */
  public rice.p2p.commonapi.NodeHandle getLocalNodeHandle() {
    return localhandle;
  }

  /**
   * Gets the Environment attribute of the PastryNode object
   *
   * @return The Environment value
   */
  public Environment getEnvironment() {
    return myEnvironment;
  }

  /**
   * Gets the LocalHandle attribute of the PastryNode object
   *
   * @return The LocalHandle value
   */
  public NodeHandle getLocalHandle() {
    return localhandle;
  }

  /**
   * Gets the NodeId attribute of the PastryNode object
   *
   * @return The NodeId value
   */
  public NodeId getNodeId() {
    return myNodeId;
  }

  /**
   * Gets the Ready attribute of the PastryNode object
   *
   * @return The Ready value
   */
  public boolean isReady() {
    return ready;
  }

  /**
   * FOR TESTING ONLY - DO NOT USE!
   *
   * @return The MessageDispatch value
   */
  public MessageDispatch getMessageDispatch() {
    return myMessageDispatch;
  }

  /**
   * Called by the layered Pastry application to check if the local pastry node
   * is the one that is currently closest to the object key id.
   *
   * @param key the object key id
   * @return true if the local node is currently the closest to the key.
   */
  public boolean isClosest(NodeId key) {

    if (leafSet.mostSimilar(key) == 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets the LeafSet attribute of the PastryNode object
   *
   * @return The LeafSet value
   */
  public LeafSet getLeafSet() {
    return leafSet;
  }

  /**
   * Gets the RoutingTable attribute of the PastryNode object
   *
   * @return The RoutingTable value
   */
  public RoutingTable getRoutingTable() {
    return routeSet;
  }

  /**
   * Returns the Id of this node
   *
   * @return This node's Id
   */
  public rice.p2p.commonapi.Id getId() {
    return getNodeId();
  }

  /**
   * Returns a factory for Ids specific to this node's protocol.
   *
   * @return A factory for creating Ids.
   */
  public rice.p2p.commonapi.IdFactory getIdFactory() {
    return new rice.pastry.commonapi.PastryIdFactory(getEnvironment());
  }

  /**
   * Combined accessor method for various members of PastryNode. These are
   * generated by node factories, and assigned here. Other elements specific to
   * the wire protocol are assigned via methods set{RMI,Direct}Elements in the
   * respective derived classes.
   *
   * @param lh Node handle corresponding to this node.
   * @param sm Security manager.
   * @param md Message dispatcher.
   * @param ls Leaf set.
   * @param rt Routing table.
   */
  public void setElements(NodeHandle lh, PastrySecurityManager sm,
                          MessageDispatch md, LeafSet ls, RoutingTable rt) {
    localhandle = lh;
    mySecurityManager = sm;
    myMessageDispatch = md;
    leafSet = ls;
    routeSet = rt;
  }

  /**
   * Sets the MessageDispatch attribute of the PastryNode object
   *
   * @param md The new MessageDispatch value
   */
  public void setMessageDispatch(MessageDispatch md) {
    myMessageDispatch = md;
  }

  /**
   * Sets the Ready attribute of the PastryNode object
   */
  public void setReady() {
    setReady(true);
  }

  /**
   * Sets the Ready attribute of the PastryNode object
   *
   * @param r The new Ready value
   */
  public void setReady(boolean r) {

    // It is possible to have the setReady() invoked more than once if the
    // message
    // denoting the termination of join protocol is duplicated.
    if (ready == r) {
      return;
    }
    //      if (r == false)
    if (logger.level <= Logger.CONFIG) {
      logger.log("PastryNode.setReady(" + r + ")");
    }

    ready = r;

    if (ready) {
      nodeIsReady();
      // deprecate this
      nodeIsReady(true);

      setChanged();
      notifyObservers(new Boolean(true));

      if (neverBeenReady) {
        // notify applications
        // we iterate over private copy to allow addition of new apps in the
        // context of notifyReady()
        Vector tmpApps = new Vector(apps);
        Iterator it = tmpApps.iterator();
        while (it.hasNext()) {
          ((PastryAppl) (it.next())).notifyReady();
        }
        neverBeenReady = false;
      }

      // deliver all buffered messages to all registered apps, because the node
      // is now ready
      myMessageDispatch.deliverAllBufferedMessages();

      // signal any apps that might be waiting for the node to get ready
      synchronized (this) {
        notifyAll();
      }
    } else {
      nodeIsReady(false);
      setChanged();
      notifyObservers(new Boolean(false));

      //        Vector tmpApps = new Vector(apps);
      //        Iterator it = tmpApps.iterator();
      //        while (it.hasNext())
      //           ((PastryAppl) (it.next())).notifyFaulty();
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param newHandle DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public abstract NodeHandle coalesce(NodeHandle newHandle);

  /**
   * Overridden by derived classes, and invoked when the node has joined
   * successfully. This one is for backwards compatability. It will soon be
   * deprecated.
   */
  public abstract void nodeIsReady();

  /**
   * Overridden by derived classes, and invoked when the node has joined
   * successfully. This should probably be abstract, but maybe in a later
   * version.
   *
   * @param state true when the node is ready, false when not
   */
  public void nodeIsReady(boolean state) {

  }

  /**
   * Overridden by derived classes to initiate the join process
   *
   * @param bootstrap Node handle to bootstrap with.
   */
  public abstract void initiateJoin(NodeHandle bootstrap);

  /**
   * Add a leaf set observer to the Pastry node.
   *
   * @param o the observer.
   * @deprecated use addLeafSetListener
   */
  public void addLeafSetObserver(Observer o) {
    leafSet.addObserver(o);
  }

  /**
   * Delete a leaf set observer from the Pastry node.
   *
   * @param o the observer.
   * @deprecated use deleteLeafSetListener
   */
  public void deleteLeafSetObserver(Observer o) {
    leafSet.deleteObserver(o);
  }

  /**
   * Adds a feature to the LeafSetListener attribute of the PastryNode object
   *
   * @param listener The feature to be added to the LeafSetListener attribute
   */
  public void addLeafSetListener(NodeSetListener listener) {
    leafSet.addNodeSetListener(listener);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param listener DESCRIBE THE PARAMETER
   */
  public void deleteLeafSetListener(NodeSetListener listener) {
    leafSet.deleteNodeSetListener(listener);
  }

  /**
   * Add a route set observer to the Pastry node.
   *
   * @param o the observer.
   * @deprecated use addRouteSetListener
   */
  public void addRouteSetObserver(Observer o) {
    routeSet.addObserver(o);
  }

  /**
   * Delete a route set observer from the Pastry node.
   *
   * @param o the observer.
   * @deprecated use deleteRouteSetListener
   */
  public void deleteRouteSetObserver(Observer o) {
    routeSet.deleteObserver(o);
  }

  /**
   * Adds a feature to the RouteSetListener attribute of the PastryNode object
   *
   * @param listener The feature to be added to the RouteSetListener attribute
   */
  public void addRouteSetListener(NodeSetListener listener) {
    routeSet.addNodeSetListener(listener);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param listener DESCRIBE THE PARAMETER
   */
  public void removeRouteSetListener(NodeSetListener listener) {
    routeSet.removeNodeSetListener(listener);
  }

  /**
   * message receiver interface. synchronized so that the external message
   * processing thread and the leafset/route maintenance thread won't interfere
   * with application messages.
   *
   * @param msg DESCRIBE THE PARAMETER
   */
  public synchronized void receiveMessage(Message msg) {
    if (mySecurityManager.verifyMessage(msg) == true) {
      myMessageDispatch.dispatchMessage(msg);
    }
  }

  /**
   * Registers a message receiver with this Pastry node.
   *
   * @param cred the credentials.
   * @param address the address that the receiver will be at.
   * @param receiver the message receiver.
   */

  public void registerReceiver(Credentials cred, Address address,
                               MessageReceiver receiver) {
    if (mySecurityManager.verifyAddressBinding(cred, address) == true) {
      myMessageDispatch.registerReceiver(address, receiver);
    } else {
      throw new Error("security failure");
    }
  }

  /**
   * Registers an application with this pastry node.
   *
   * @param app the application
   */

  public void registerApp(PastryAppl app) {
    if (isReady()) {
      app.notifyReady();
    }
    apps.add(app);
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
  public abstract ScheduledMessage scheduleMsg(Message msg, long delay);

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
  public abstract ScheduledMessage scheduleMsg(Message msg, long delay,
                                               long period);

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
  public abstract ScheduledMessage scheduleMsgAtFixedRate(Message msg,
                                                          long delay, long period);

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "Pastry node " + myNodeId.toString();
  }

  // Common API Support

  /**
   * This returns a VirtualizedNode specific to the given application and
   * instance name to the application, which the application can then use in
   * order to send an receive messages.
   *
   * @param application The Application
   * @param instance An identifier for a given instance
   * @return The endpoint specific to this applicationk, which can be used for
   *      message sending/receiving.
   */
  public rice.p2p.commonapi.Endpoint registerApplication(
                                                         rice.p2p.commonapi.Application application, String instance) {
    return new rice.pastry.commonapi.PastryEndpoint(this, application, instance);
  }

  /**
   * This returns a Endpoint specific to the given application and instance name
   * to the application, which the application can then use in order to send an
   * receive messages. This method allows advanced developers to specify which
   * "port" on the node they wish their application to register as. This "port"
   * determines which of the applications on top of the node should receive an
   * incoming message.
   *
   * @param application The Application
   * @param port The port to use
   * @return The endpoint specific to this applicationk, which can be used for
   *      message sending/receiving.
   */
  public rice.p2p.commonapi.Endpoint registerApplication(
                                                         rice.p2p.commonapi.Application application, int port) {
    return new rice.pastry.commonapi.PastryEndpoint(this, application, port);
  }

  /**
   * Schedules a job for processing on the dedicated processing thread, should
   * one exist. CPU intensive jobs, such as encryption, erasure encoding, or
   * bloom filter creation should never be done in the context of the underlying
   * node's thread, and should only be done via this method.
   *
   * @param task The task to run on the processing thread
   * @param command The command to return the result to once it's done
   */
  public void process(Executable task, Continuation command) {
    try {
      myEnvironment.getProcessor().process(task,
        command,
        myEnvironment.getSelectorManager(),
        myEnvironment.getTimeSource(),
        myEnvironment.getLogManager());

//      command.receiveResult(task.execute());
    } catch (final Exception e) {
      command.receiveException(e);
    }
  }

  /**
   * Method which kills a PastryNode. Note, this doesn't implicitly kill the
   * environment. Make sure to call super.destroy() !!!
   */
  public void destroy() {
    myMessageDispatch.destroy();
  }


  /**
   * DESCRIBE THE METHOD
   *
   * @param handle DESCRIBE THE PARAMETER
   * @param message DESCRIBE THE PARAMETER
   */
  public abstract void send(NodeHandle handle, Message message);
}


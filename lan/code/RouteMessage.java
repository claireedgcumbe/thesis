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
package rice.pastry.routing;

import rice.pastry.commonapi.*;

import rice.pastry.*;
import rice.pastry.messaging.*;
import rice.pastry.security.*;

import java.io.*;

/**
 * A route message contains a pastry message that has been wrapped to be sent to
 * another pastry node.
 *
 * @version $Id: RouteMessage.java 3175 2006-03-24 12:45:46Z jeffh $
 * @author Andrew Ladd
 */

public class RouteMessage extends Message implements Serializable,
  rice.p2p.commonapi.RouteMessage {

  private Id target;

  private Message internalMsg;

  private NodeHandle prevNode;

  private transient SendOptions opts;

  private Address auxAddress;

  /**
   * DESCRIBE THE FIELD
   */
  public transient NodeHandle nextHop;
  private final static long serialVersionUID = 3492981895989180093L;

  /**
   * Constructor.
   *
   * @param target this is id of the node the message will be routed to.
   * @param msg the wrapped message.
   * @param cred the credentials for the message.
   */

  public RouteMessage(Id target, Message msg, Credentials cred) {
    super(new RouterAddress());
    this.target = target;
    internalMsg = msg;
    this.opts = new SendOptions();

    nextHop = null;
  }

  /**
   * Constructor.
   *
   * @param target this is id of the node the message will be routed to.
   * @param msg the wrapped message.
   * @param cred the credentials for the message.
   * @param opts the send options for the message.
   */

  public RouteMessage(Id target, Message msg, Credentials cred, SendOptions opts) {
    super(new RouterAddress());
    this.target = target;
    internalMsg = msg;
    this.opts = opts;

    nextHop = null;
  }

  /**
   * Constructor.
   *
   * @param dest the node this message will be routed to
   * @param msg the wrapped message.
   * @param cred the credentials for the message.
   * @param opts the send options for the message.
   * @param aux an auxilary address which the message after each hop.
   */

  public RouteMessage(NodeHandle dest, Message msg, Credentials cred,
                      SendOptions opts, Address aux) {
    super(new RouterAddress());
    this.target = dest.getNodeId();
    internalMsg = msg;
    this.opts = opts;
    nextHop = dest;
    auxAddress = aux;
  }

  /**
   * Constructor.
   *
   * @param target this is id of the node the message will be routed to.
   * @param msg the wrapped message.
   * @param cred the credentials for the message.
   * @param aux an auxilary address which the message after each hop.
   */

  public RouteMessage(Id target, Message msg, Credentials cred, Address aux) {
    super(new RouterAddress());
    this.target = target;
    internalMsg = msg;
    this.opts = new SendOptions();

    auxAddress = aux;

    nextHop = null;
  }

  /**
   * Constructor.
   *
   * @param target this is id of the node the message will be routed to.
   * @param msg the wrapped message.
   * @param cred the credentials for the message.
   * @param opts the send options for the message.
   * @param aux an auxilary address which the message after each hop.
   */

  public RouteMessage(Id target, Message msg, Credentials cred,
                      SendOptions opts, Address aux) {
    super(new RouterAddress());
    this.target = target;
    internalMsg = msg;
    this.opts = opts;

    auxAddress = aux;

    nextHop = null;
  }

  /**
   * Constructor.
   *
   * @param target this is id of the node the message will be routed to.
   * @param msg the wrapped message.
   * @param firstHop the nodeHandle of the first hop destination
   * @param aux an auxilary address which the message after each hop.
   */

  public RouteMessage(Id target, Message msg, NodeHandle firstHop, Address aux) {
    super(new RouterAddress());
    this.target = (Id) target;
    internalMsg = msg;
    this.opts = new SendOptions();
    auxAddress = aux;
    nextHop = firstHop;
  }

  /**
   * Gets the target node id of this message.
   *
   * @return the target node id.
   */

  public Id getTarget() {
    return target;
  }

  /**
   * Gets the PrevNode attribute of the RouteMessage object
   *
   * @return The PrevNode value
   */
  public NodeHandle getPrevNode() {
    return prevNode;
  }

  /**
   * Gets the NextHop attribute of the RouteMessage object
   *
   * @return The NextHop value
   */
  public NodeHandle getNextHop() {
    return nextHop;
  }

  /**
   * Get priority
   *
   * @return the priority of this message.
   */

  public int getPriority() {
    return internalMsg.getPriority();
  }

  /**
   * Get receiver address.
   *
   * @return the address.
   */

  public Address getDestination() {
    if (nextHop == null || auxAddress == null) {
      return super.getDestination();
    }

    return auxAddress;
  }

  /**
   * Get transmission options.
   *
   * @return the options.
   */

  public SendOptions getOptions() {
    if (opts == null) {
      opts = new SendOptions();
    }
    return opts;
  }

  // Common API Support

  /**
   * Gets the DestinationId attribute of the RouteMessage object
   *
   * @return The DestinationId value
   */
  public rice.p2p.commonapi.Id getDestinationId() {
    return getTarget();
  }

  /**
   * Gets the NextHopHandle attribute of the RouteMessage object
   *
   * @return The NextHopHandle value
   */
  public rice.p2p.commonapi.NodeHandle getNextHopHandle() {
    return nextHop;
  }

  /**
   * Gets the Message attribute of the RouteMessage object
   *
   * @return The Message value
   */
  public rice.p2p.commonapi.Message getMessage() {
    return ((PastryEndpointMessage) unwrap()).getMessage();
  }

  /**
   * Sets the PrevNode attribute of the RouteMessage object
   *
   * @param n The new PrevNode value
   */
  public void setPrevNode(NodeHandle n) {
    prevNode = n;
  }

  /**
   * Sets the NextHop attribute of the RouteMessage object
   *
   * @param nh The new NextHop value
   */
  public void setNextHop(NodeHandle nh) {
    nextHop = nh;
  }

  /**
   * Sets the DestinationId attribute of the RouteMessage object
   *
   * @param id The new DestinationId value
   */
  public void setDestinationId(rice.p2p.commonapi.Id id) {
    target = (Id) id;
  }

  /**
   * Sets the NextHopHandle attribute of the RouteMessage object
   *
   * @param nextHop The new NextHopHandle value
   */
  public void setNextHopHandle(rice.p2p.commonapi.NodeHandle nextHop) {
    this.nextHop = (NodeHandle) nextHop;
  }

  /**
   * Sets the Message attribute of the RouteMessage object
   *
   * @param message The new Message value
   */
  public void setMessage(rice.p2p.commonapi.Message message) {
    ((PastryEndpointMessage) unwrap()).setMessage(message);
  }

  /**
   * Routes the messages if the next hop has been set up.
   *
   * @param localHandle DESCRIBE THE PARAMETER
   * @return true if the message got routed, false otherwise.
   */

  public boolean routeMessage(NodeHandle localHandle) {
    if (nextHop == null) {
      return false;
    }
    setSender(localHandle);

    NodeHandle handle = nextHop;
    nextHop = null;

    if (localHandle.equals(handle)) {
      localHandle.getLocalNode().send(handle, internalMsg);
    } else {
      localHandle.getLocalNode().send(handle, this);
    }

    return true;
  }

  /**
   * The wrapped message.
   *
   * @return the wrapped message.
   */

  public Message unwrap() {
    return internalMsg;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    String str = "";
    str += "[ " + internalMsg + " ]";

    return str;
  }
}

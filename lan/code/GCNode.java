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

package rice.p2p.past.gc;

import rice.environment.Environment;
import rice.p2p.commonapi.*;

/**
 * @(#) GCNode.java This class wraps a Node
 *
 * @version $Id: GCNode.java 2622 2005-06-30 22:22:26Z jeffh $
 * @author Alan Mislove
 */
public class GCNode implements Node {

  /**
   * The node which this mulitring node is wrapping
   */
  protected Node node;

  /**
   * Constructor
   *
   * @param node The node which this multiring node is wrapping
   */
  public GCNode(Node node) {
    this.node = node;
  }

  /**
   * Method which returns the node handle to the local node
   *
   * @return A handle to the local node
   */
  public NodeHandle getLocalNodeHandle() {
    return node.getLocalNodeHandle();
  }

  /**
   * Returns the Id of this node
   *
   * @return This node's Id
   */
  public Id getId() {
    return node.getId();
  }

  /**
   * Returns a factory for Ids specific to this node's protocol.
   *
   * @return A factory for creating Ids.
   */
  public IdFactory getIdFactory() {
    return new GCIdFactory(node.getIdFactory());
  }

  /*
   *  (non-Javadoc)
   *  @see rice.p2p.commonapi.Node#getEnvironment()
   */
  /**
   * Gets the Environment attribute of the GCNode object
   *
   * @return The Environment value
   */
  public Environment getEnvironment() {
    return node.getEnvironment();
  }

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
  public Endpoint registerApplication(Application application, String instance) {
    return new GCEndpoint(node.registerApplication(application, instance));
  }

  /**
   * This returns a Endpoint specific to the given application and instance name
   * to the application, which the application can then use in order to send an
   * receive messages. This method allows advanced developers to specify which
   * "port" on the node they wish their application to register as. This "port"
   * determines which of the applications on top of the node should receive an
   * incoming message. NOTE: Use of this method of registering applications is
   * recommended only for advanced users - 99% of all applications should just
   * use the other registerApplication
   *
   * @param application The Application
   * @param port The port to use
   * @return The endpoint specific to this applicationk, which can be used for
   *      message sending/receiving.
   */
  public Endpoint registerApplication(Application application, int port) {
    return new GCEndpoint(node.registerApplication(application, port));
  }

  /**
   * Prints out the string
   *
   * @return A string
   */
  public String toString() {
    return "{GCNode " + node + "}";
  }
}



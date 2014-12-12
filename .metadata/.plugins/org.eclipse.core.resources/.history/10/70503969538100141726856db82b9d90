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

import rice.Continuation;
import rice.environment.Environment;
import rice.environment.logging.*;
import rice.p2p.commonapi.CancellableTask;
import rice.pastry.*;
import rice.pastry.messaging.*;
import rice.pastry.security.*;
import rice.pastry.standard.*;
import rice.pastry.routing.*;
import rice.pastry.leafset.*;

import java.io.*;
import java.util.*;

/**
 * Pastry node factory for direct connections between nodes (local instances).
 *
 * @version $Id: DirectPastryNodeFactory.java 2904 2006-01-10 21:50:37Z jeffh $
 * @author Andrew Ladd
 * @author Sitaram Iyer
 * @author Rongmei Zhang/Y. Charlie Hu
 */
public class DirectPastryNodeFactory extends PastryNodeFactory {

  private NodeIdFactory nidFactory;
  private NetworkSimulator simulator;

  Hashtable recordTable = new Hashtable();

  /**
   * Main constructor.
   *
   * @param nf the NodeIdFactory
   * @param sim the NetworkSimulator
   * @param env DESCRIBE THE PARAMETER
   */
  public DirectPastryNodeFactory(NodeIdFactory nf, NetworkSimulator sim, Environment env) {
    super(env);
    nidFactory = nf;
    simulator = sim;
  }

  /**
   * Getter for the NetworkSimulator.
   *
   * @return the NetworkSimulator we are using.
   */
  public NetworkSimulator getNetworkSimulator() {
    return simulator;
  }

  /**
   * This method returns the remote leafset of the provided handle to the
   * caller, in a protocol-dependent fashion. Note that this method may block
   * while sending the message across the wire.
   *
   * @param handle The node to connect to
   * @return The leafset of the remote node
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public LeafSet getLeafSet(NodeHandle handle) throws IOException {
    DirectNodeHandle dHandle = (DirectNodeHandle) handle;

    return dHandle.getRemote().getLeafSet();
  }


  /**
   * Gets the LeafSet attribute of the DirectPastryNodeFactory object
   *
   * @param handle DESCRIBE THE PARAMETER
   * @param c DESCRIBE THE PARAMETER
   * @return The LeafSet value
   */
  public CancellableTask getLeafSet(NodeHandle handle, Continuation c) {
    DirectNodeHandle dHandle = (DirectNodeHandle) handle;
    c.receiveResult(dHandle.getRemote().getLeafSet());
    return new NullCancellableTask();
  }

  /**
   * This method returns the remote route row of the provided handle to the
   * caller, in a protocol-dependent fashion. Note that this method may block
   * while sending the message across the wire.
   *
   * @param handle The node to connect to
   * @param row The row number to retrieve
   * @return The route row of the remote node
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public RouteSet[] getRouteRow(NodeHandle handle, int row) throws IOException {
    DirectNodeHandle dHandle = (DirectNodeHandle) handle;

    return dHandle.getRemote().getRoutingTable().getRow(row);
  }

  /**
   * Gets the RouteRow attribute of the DirectPastryNodeFactory object
   *
   * @param handle DESCRIBE THE PARAMETER
   * @param row DESCRIBE THE PARAMETER
   * @param c DESCRIBE THE PARAMETER
   * @return The RouteRow value
   */
  public CancellableTask getRouteRow(NodeHandle handle, int row, Continuation c) {
    DirectNodeHandle dHandle = (DirectNodeHandle) handle;
    c.receiveResult(dHandle.getRemote().getRoutingTable().getRow(row));
    return new NullCancellableTask();
  }

  /**
   * This method determines and returns the proximity of the current local node
   * the provided NodeHandle. This will need to be done in a protocol- dependent
   * fashion and may need to be done in a special way.
   *
   * @param local DESCRIBE THE PARAMETER
   * @param remote DESCRIBE THE PARAMETER
   * @return The proximity of the provided handle
   */
  public int getProximity(NodeHandle local, NodeHandle remote) {
    return simulator.proximity((DirectNodeHandle) local, (DirectNodeHandle) remote);
  }

  /**
   * Manufacture a new Pastry node.
   *
   * @param bootstrap DESCRIBE THE PARAMETER
   * @return a new PastryNode
   */
  public PastryNode newNode(NodeHandle bootstrap) {
    return newNode(bootstrap, nidFactory.generateNodeId());
  }

  /**
   * Manufacture a new Pastry node.
   *
   * @param bootstrap DESCRIBE THE PARAMETER
   * @param nodeId DESCRIBE THE PARAMETER
   * @return a new PastryNode
   */
  public PastryNode newNode(NodeHandle bootstrap, NodeId nodeId) {
    if (bootstrap == null) {
      if (logger.level <= Logger.WARNING) {
        logger.log(
          "No bootstrap node provided, starting a new ring...");
      }
    }

    // this code builds a different environment for each PastryNode
    Environment environment = this.environment;
    if (this.environment.getParameters().getBoolean("pastry_factory_multipleNodes")) {
      if (this.environment.getLogManager() instanceof CloneableLogManager) {
        environment = new Environment(
          this.environment.getSelectorManager(),
          this.environment.getProcessor(),
          this.environment.getRandomSource(),
          this.environment.getTimeSource(),
          ((CloneableLogManager) this.environment.getLogManager()).clone("0x" + nodeId.toStringBare()),
          this.environment.getParameters());
      }
    }

    NodeRecord nr = (NodeRecord) recordTable.get(nodeId);
    if (nr == null) {
      nr = simulator.generateNodeRecord();
      recordTable.put(nodeId, nr);
    }
    DirectPastryNode pn = new DirectPastryNode(nodeId, simulator, environment, nr);

    DirectNodeHandle localhandle = new DirectNodeHandle(pn, pn, simulator);
    simulator.registerNode(pn);

    DirectSecurityManager secureMan = new DirectSecurityManager(simulator);
    MessageDispatch msgDisp = new MessageDispatch(pn);

    RoutingTable routeTable = new RoutingTable(localhandle, rtMax, rtBase, environment);
    LeafSet leafSet = new LeafSet(localhandle, lSetSize);

    StandardRouter router =
      new StandardRouter(pn, secureMan);
    StandardLeafSetProtocol lsProtocol =
      new StandardLeafSetProtocol(pn, localhandle, secureMan, leafSet, routeTable);
    StandardRouteSetProtocol rsProtocol =
      new StandardRouteSetProtocol(localhandle, secureMan, routeTable, environment);

    msgDisp.registerReceiver(router.getAddress(), router);
    msgDisp.registerReceiver(lsProtocol.getAddress(), lsProtocol);
    msgDisp.registerReceiver(rsProtocol.getAddress(), rsProtocol);

    pn.setElements(localhandle, secureMan, msgDisp, leafSet, routeTable);
    pn.setDirectElements(
    /*
     *  simulator
     */
      );
    secureMan.setLocalPastryNode(pn);

    StandardJoinProtocol jProtocol =
      new StandardJoinProtocol(pn, localhandle, secureMan, routeTable, leafSet);

    // pn.doneNode(bootstrap);
    //pn.doneNode( simulator.getClosest(localhandle) );
    pn.doneNode(getNearest(localhandle, bootstrap));

    return pn;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param local DESCRIBE THE PARAMETER
   * @param handle DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  protected int proximity(NodeHandle local, NodeHandle handle) {
    return getProximity(local, handle);
  }

  /**
   * The non-blocking versions here all execute immeadiately. This
   * CancellableTask is just a placeholder.
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author Jeff Hoye
   */
  class NullCancellableTask implements CancellableTask {
    /**
     * Main processing method for the NullCancellableTask object
     */
    public void run() {
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @return DESCRIBE THE RETURN VALUE
     */
    public boolean cancel() {
      return false;
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @return DESCRIBE THE RETURN VALUE
     */
    public long scheduledExecutionTime() {
      return 0;
    }
  }

}

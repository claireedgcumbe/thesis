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
package rice.pastry.socket;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;

import rice.Continuation;
import rice.environment.Environment;
import rice.environment.logging.*;
import rice.environment.params.Parameters;
import rice.environment.params.simple.SimpleParameters;
import rice.environment.processing.Processor;
import rice.environment.processing.simple.SimpleProcessor;
import rice.environment.random.RandomSource;
import rice.environment.random.simple.SimpleRandomSource;
import rice.p2p.commonapi.CancellableTask;
import rice.pastry.*;
import rice.pastry.dist.DistPastryNodeFactory;
import rice.pastry.leafset.LeafSet;
import rice.pastry.messaging.*;
import rice.pastry.routing.*;
import rice.pastry.socket.messaging.*;
import rice.pastry.standard.*;
import rice.selector.*;

/**
 * Pastry node factory for Socket-linked nodes.
 *
 * @version $Id: SocketPastryNodeFactory.java,v 1.6 2004/03/08 19:53:57 amislove
 *      Exp $
 * @author Alan Mislove
 */
public class SocketPastryNodeFactory extends DistPastryNodeFactory {

  private Environment environment;

  private NodeIdFactory nidFactory;

  private int port;

  /**
   * Large period (in seconds) means infrequent, 0 means never.
   */
  private int leafSetMaintFreq;

  private int routeSetMaintFreq;

  private RandomSource random;

  private InetAddress localAddress;

  /**
   * Constructor. Here is order for bind address 1) bindAddress parameter 2) if
   * bindAddress is null, then parameter: socket_bindAddress (if it exists) 3)
   * if socket_bindAddress doesn't exist, then InetAddress.getLocalHost()
   *
   * @param nf The factory for building node ids
   * @param bindAddress which address to bind to
   * @param startPort The port to start creating nodes on
   * @param env The environment.
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public SocketPastryNodeFactory(NodeIdFactory nf, InetAddress bindAddress,
                                 int startPort, Environment env) throws IOException {
    super(env);
    localAddress = bindAddress;
    if (localAddress == null) {
      if (env.getParameters().contains("socket_bindAddress")) {
        localAddress = env.getParameters().getInetAddress("socket_bindAddress");
      }
    }
    if (localAddress == null) {
      localAddress = InetAddress.getLocalHost();
      try {
        ServerSocket test = new ServerSocket();
        test.bind(new InetSocketAddress(localAddress, port));
      } catch (SocketException e) {
        Socket temp = new Socket("yahoo.com", 80);
        localAddress = temp.getLocalAddress();
        temp.close();

        if (logger.level <= Logger.WARNING) {
          logger.log("Error binding to default IP, using " + localAddress);
        }
      }
    }

    environment = env;
    nidFactory = nf;
    port = startPort;
    Parameters params = env.getParameters();
    leafSetMaintFreq = params.getInt("pastry_leafSetMaintFreq");
    routeSetMaintFreq = params.getInt("pastry_routeSetMaintFreq");

    if (params.contains("pastry_socket_use_own_random")
      && params.getBoolean("pastry_socket_use_own_random")) {
      if (params.contains("pastry_socket_random_seed")
        && !params.getString("pastry_socket_random_seed").equalsIgnoreCase(
        "clock")) {
        this.random = new SimpleRandomSource(params.getLong("pastry_socket_random_seed"), env.getLogManager(),
          "socket");
      } else {
        this.random = new SimpleRandomSource(env.getLogManager(), "socket");
      }
    } else {
      this.random = env.getRandomSource();
    }
  }

  /**
   * Constructor for SocketPastryNodeFactory.
   *
   * @param nf DESCRIBE THE PARAMETER
   * @param startPort DESCRIBE THE PARAMETER
   * @param env DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public SocketPastryNodeFactory(NodeIdFactory nf, int startPort,
                                 Environment env) throws IOException {
    this(nf, null, startPort, env);
  }

  /**
   * This method returns the routes a remote node is using
   *
   * @param handle The node to connect to
   * @param local DESCRIBE THE PARAMETER
   * @return The leafset of the remote node
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public SourceRoute[] getRoutes(NodeHandle handle, NodeHandle local)
     throws IOException {
    SocketNodeHandle wHandle = (SocketNodeHandle) handle;

    RoutesResponseMessage lm = (RoutesResponseMessage) getResponse(wHandle.getAddress(), new RoutesRequestMessage());

    return lm.getRoutes();
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
    SocketNodeHandle wHandle = (SocketNodeHandle) handle;

    LeafSetResponseMessage lm = (LeafSetResponseMessage) getResponse(wHandle.getAddress(), new LeafSetRequestMessage());

    return lm.getLeafSet();
  }

  /**
   * Gets the LeafSet attribute of the SocketPastryNodeFactory object
   *
   * @param handle DESCRIBE THE PARAMETER
   * @param c DESCRIBE THE PARAMETER
   * @return The LeafSet value
   */
  public CancellableTask getLeafSet(NodeHandle handle, final Continuation c) {
    SocketNodeHandle wHandle = (SocketNodeHandle) handle;

    return getResponse(wHandle.getAddress(), new LeafSetRequestMessage(),
      new Continuation() {
        public void receiveResult(Object result) {
          LeafSetResponseMessage lm = (LeafSetResponseMessage) result;
          c.receiveResult(lm.getLeafSet());
        }

        public void receiveException(Exception result) {
          c.receiveException(result);
        }
      });
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
    SocketNodeHandle wHandle = (SocketNodeHandle) handle;

    RouteRowResponseMessage rm = (RouteRowResponseMessage) getResponse(wHandle.getAddress(), new RouteRowRequestMessage(row));

    return rm.getRouteRow();
  }

  /**
   * Gets the RouteRow attribute of the SocketPastryNodeFactory object
   *
   * @param handle DESCRIBE THE PARAMETER
   * @param row DESCRIBE THE PARAMETER
   * @param c DESCRIBE THE PARAMETER
   * @return The RouteRow value
   */
  public CancellableTask getRouteRow(NodeHandle handle, int row,
                                     final Continuation c) {
    SocketNodeHandle wHandle = (SocketNodeHandle) handle;

    return getResponse(wHandle.getAddress(), new RouteRowRequestMessage(row),
      new Continuation() {
        public void receiveResult(Object result) {
          RouteRowResponseMessage rm = (RouteRowResponseMessage) result;
          c.receiveResult(rm.getRouteRow());
        }

        public void receiveException(Exception result) {
          c.receiveException(result);
        }
      });
  }

  /**
   * This method determines and returns the proximity of the current local node
   * to the provided NodeHandle. This will need to be done in a protocol-
   * dependent fashion and may need to be done in a special way.
   *
   * @param handle The handle to determine the proximity of
   * @param local DESCRIBE THE PARAMETER
   * @return The proximity of the provided handle
   */
  public int getProximity(NodeHandle local, NodeHandle handle) {
    EpochInetSocketAddress lAddress = ((SocketNodeHandle) local)
      .getEpochAddress();
    EpochInetSocketAddress rAddress = ((SocketNodeHandle) handle)
      .getEpochAddress();

    lAddress = new EpochInetSocketAddress(new InetSocketAddress(lAddress.getAddress().getAddress(), lAddress.getAddress().getPort() + 1));

    // if this is a request for an old version of us, then we return
    // infinity as an answer
    if (lAddress.getAddress().equals(rAddress.getAddress())) {
      return Integer.MAX_VALUE;
    }

    DatagramSocket socket = null;
    SourceRoute route = SourceRoute.build(new EpochInetSocketAddress[]{rAddress});

    try {
      socket = new DatagramSocket(lAddress.getAddress().getPort());
      socket.setSoTimeout(5000);

      byte[] data = PingManager.addHeader(route, new PingMessage(route, route.reverse(lAddress), environment.getTimeSource().currentTimeMillis()),
        lAddress, environment, logger);

      socket.send(new DatagramPacket(data, data.length, rAddress.getAddress()));

      long start = environment.getTimeSource().currentTimeMillis();
      socket.receive(new DatagramPacket(new byte[10000], 10000));
      return (int) (environment.getTimeSource().currentTimeMillis() - start);
    } catch (IOException e) {
      return Integer.MAX_VALUE - 1;
    } finally {
      if (socket != null) {
        socket.close();
      }
    }
  }

  /**
   * This method anonymously sends the given message to the remote address,
   * blocks until a response is received, and then closes the socket and returns
   * the response.
   *
   * @param address The address to send to
   * @param message The message to send
   * @return The response
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected Message getResponse(InetSocketAddress address, Message message)
     throws IOException {
    // create reader and writer
    SocketChannelWriter writer;
    SocketChannelReader reader;
    writer = new SocketChannelWriter(environment, SourceRoute.build(new EpochInetSocketAddress(address,
      EpochInetSocketAddress.EPOCH_UNKNOWN)));
    reader = new SocketChannelReader(environment, SourceRoute.build(new EpochInetSocketAddress(address,
      EpochInetSocketAddress.EPOCH_UNKNOWN)));

    // bind to the appropriate port
    SocketChannel channel = SocketChannel.open();
    channel.configureBlocking(true);
    channel.socket().connect(address, 20000);
    channel.socket().setSoTimeout(20000);

    writer.enqueue(SocketCollectionManager.HEADER_DIRECT);
    writer.enqueue(message);
    writer.write(channel);
    Object o = null;

    while (o == null) {
      o = reader.read(channel);
    }

    if (logger.level <= Logger.FINER) {
      logger.log("SPNF.getResponse(): Closing " + channel);
    }
    channel.socket().shutdownOutput();
    channel.socket().close();
    channel.close();
    if (logger.level <= Logger.FINER) {
      logger.log("SPNF.getResponse(): Closed " + channel);
    }

    return (Message) o;
  }

  /**
   * Gets the Response attribute of the SocketPastryNodeFactory object
   *
   * @param address DESCRIBE THE PARAMETER
   * @param message DESCRIBE THE PARAMETER
   * @param c DESCRIBE THE PARAMETER
   * @return The Response value
   */
  protected CancellableTask getResponse(final InetSocketAddress address,
                                              final Message message, final Continuation c) {
    // create reader and writer
    final SocketChannelWriter writer;
    final SocketChannelReader reader;
    writer = new SocketChannelWriter(environment, SourceRoute.build(new EpochInetSocketAddress(address,
      EpochInetSocketAddress.EPOCH_UNKNOWN)));
    reader = new SocketChannelReader(environment, SourceRoute.build(new EpochInetSocketAddress(address,
      EpochInetSocketAddress.EPOCH_UNKNOWN)));
    writer.enqueue(SocketCollectionManager.HEADER_DIRECT);
    writer.enqueue(message);

    // bind to the appropriate port
    try {
      final SocketChannel channel = SocketChannel.open();
      channel.configureBlocking(false);
      final SelectionKey key = environment.getSelectorManager().register(
        channel,
        new SelectionKeyHandler() {
          public void connect(SelectionKey key) {
            if (logger.level <= Logger.FINE) {
              logger.log("SPNF.getResponse(" + address + "," + message
                + ").connect()");
            }
            try {
              if (channel.finishConnect()) {
                key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
              }

              if (logger.level <= Logger.FINE) {
                logger.log("(SPNF) Found connectable channel - completed connection");
              }
              // channel.socket().connect(address, 20000);
              // channel.socket().setSoTimeout(20000);
            } catch (IOException ioe) {
              handleException(ioe);
            }
          }

          public void read(SelectionKey key) {
            if (logger.level <= Logger.FINE) {
              logger.log("SPNF.getResponse(" + address + "," + message
                + ").read()");
            }
            try {
              Object o = null;

              while (o == null) {
                o = reader.read(channel);
              }
              channel.socket().close();
              channel.close();
              key.cancel();
              c.receiveResult(o);
            } catch (IOException ioe) {
              handleException(ioe);
            }
          }

          public void write(SelectionKey key) {
            if (logger.level <= Logger.FINE) {
              logger.log("SPNF.getResponse(" + address + "," + message
                + ").write()");
            }
            try {
              if (writer.write(channel)) {
                key.interestOps(SelectionKey.OP_READ);
              }
            } catch (IOException ioe) {
              handleException(ioe);
            }
          }

          public void handleException(Exception e) {
            try {
              channel.socket().close();
              channel.close();
              channel.keyFor(environment.getSelectorManager().getSelector())
                .cancel();
            } catch (IOException ioe) {

              if (logger.level <= Logger.WARNING) {
                logger.logException("Error while trying requesting "
                  + message + " from " + address, e);
              }
            } finally {
              c.receiveException(e);
            }
          }
        }, 0);

      if (logger.level <= Logger.FINE) {
        logger.log("(SPNF) Initiating socket connection to address " + address);
      }

      if (channel.connect(address)) {
        key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
      } else {
        key.interestOps(SelectionKey.OP_CONNECT | SelectionKey.OP_WRITE
          | SelectionKey.OP_READ);
      }

      return
        new CancellableTask() {
          public void run() {
          }

          public boolean cancel() {
            environment.getSelectorManager().invoke(
              new Runnable() {
                public void run() {

                  try {
                    synchronized (key) {
                      channel.socket().close();
                      channel.close();
//                  if (logger.level <= Logger.WARNING) {
//                    if (!environment.getSelectorManager().isSelectorThread()) {
//                      logger.logException("WARNING: cancelling key:"+key+" on the wrong thread.", new Exception("Stack Trace"));
//                    }
//                  }
                      key.cancel();
                    }
//                return true;
                  } catch (Exception ioe) {
                    if (logger.level <= Logger.WARNING) {
                      logger.logException("Error cancelling task.", ioe);
                    }
//                return false;
                  }
                }
              });
            return true;
          }

          public long scheduledExecutionTime() {
            return 0;
          }
        };
    } catch (IOException ioe) {
      c.receiveException(ioe);
      return null;
    }
  }

  /**
   * Method which constructs an InetSocketAddres for the local host with the
   * specifed port number.
   *
   * @param portNumber The port number to create the address at.
   * @param epoch DESCRIBE THE PARAMETER
   * @return An InetSocketAddress at the localhost with port portNumber.
   */
  private EpochInetSocketAddress getEpochAddress(int portNumber, long epoch) {
    EpochInetSocketAddress result = null;

    result = new EpochInetSocketAddress(new InetSocketAddress(localAddress,
      portNumber), epoch);
    return result;
  }

  /**
   * Way to generate a NodeHandle with a maximum timeout to receive the result.
   * Helper funciton for using the non-blocking version. However this method
   * behaves as a blocking call.
   *
   * @param address
   * @param timeout maximum time in millis to return the result. <= 0 will use
   *      the blocking version.
   * @return
   */
  public NodeHandle generateNodeHandle(InetSocketAddress address, int timeout) {
    if (timeout <= 0) {
      return generateNodeHandle(address);
    }

    TimerContinuation c = new TimerContinuation();

    CancellableTask task = generateNodeHandle(address, c);

    synchronized (c) {
      try {
        c.wait(timeout);
      } catch (InterruptedException ie) {
        return null;
      }
    }
    task.cancel();

    if (logger.level <= Logger.FINER) {
      logger.log("SPNF.generateNodeHandle() returning " + c.ret
        + " after trying to contact " + address);
    }

    return (NodeHandle) c.ret;
  }

  /**
   * Method which contructs a node handle (using the socket protocol) for the
   * node at address NodeHandle.
   *
   * @param address The address of the remote node.
   * @return A NodeHandle cooresponding to that address
   */
  public NodeHandle generateNodeHandle(InetSocketAddress address) {
    // send nodeId request to remote node, wait for response
    // allocate enought bytes to read a node handle
    if (logger.level <= Logger.FINE) {
      logger.log("Socket: Contacting bootstrap node " + address);
    }

    try {
      NodeIdResponseMessage rm = (NodeIdResponseMessage) getResponse(address,
        new NodeIdRequestMessage());

      return new SocketNodeHandle(new EpochInetSocketAddress(address, rm.getEpoch()), rm.getNodeId());
    } catch (IOException e) {
      if (logger.level <= Logger.WARNING) {
        logger.log("Error connecting to address " + address + ": " + e);
      }
      return null;
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param address DESCRIBE THE PARAMETER
   * @param c DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public CancellableTask generateNodeHandle(final InetSocketAddress address,
                                                  final Continuation c) {
    if (logger.level <= Logger.FINE) {
      logger.log("Socket: Contacting bootstrap node " + address);
    }

    return getResponse(address, new NodeIdRequestMessage(),
      new Continuation() {
        public void receiveResult(Object result) {
          NodeIdResponseMessage rm = (NodeIdResponseMessage) result;
          c.receiveResult(new SocketNodeHandle(new EpochInetSocketAddress(
            address, rm.getEpoch()), rm.getNodeId()));
        }

        public void receiveException(Exception result) {
          if (logger.level <= Logger.WARNING) {
            logger.log("Error connecting to address " + address + ": " + result);
          }
          c.receiveException(result);
        }
      });
  }

  /**
   * Method which creates a Pastry node from the next port with a randomly
   * generated NodeId.
   *
   * @param bootstrap Node handle to bootstrap from.
   * @return A node with a random ID and next port number.
   */
  public PastryNode newNode(NodeHandle bootstrap) {
    // if (bootstrap == null) {
    // return newNode(bootstrap, NodeId.buildNodeId());
    // }
    return newNode(bootstrap, nidFactory.generateNodeId());
  }

  /**
   * Method which creates a Pastry node from the next port with a randomly
   * generated NodeId.
   *
   * @param bootstrap Node handle to bootstrap from.
   * @param nodeId DESCRIBE THE PARAMETER
   * @return A node with a random ID and next port number.
   */
  public PastryNode newNode(final NodeHandle bootstrap, NodeId nodeId) {
    return newNode(bootstrap, nodeId, null);
  }

  /**
   * Method which creates a Pastry node from the next port with a randomly
   * generated NodeId.
   *
   * @param bootstrap Node handle to bootstrap from.
   * @param proxy DESCRIBE THE PARAMETER
   * @return A node with a random ID and next port number.
   */
  public PastryNode newNode(NodeHandle bootstrap, InetSocketAddress proxy) {
    return newNode(bootstrap, nidFactory.generateNodeId(), proxy);
  }

  /**
   * Method which creates a Pastry node from the next port with a randomly
   * generated NodeId.
   *
   * @param bootstrap Node handle to bootstrap from.
   * @param nodeId DESCRIBE THE PARAMETER
   * @param pAddress DESCRIBE THE PARAMETER
   * @return A node with a random ID and next port number.
   */
  public PastryNode newNode(NodeHandle bootstrap, NodeId nodeId,
                            InetSocketAddress pAddress) {
    if (bootstrap == null) {
      if (logger.level <= Logger.WARNING) {
        logger.log("No bootstrap node provided, starting a new ring binding to address "
          + localAddress + "...");
      }
    }

    // this code builds a different environment for each PastryNode
    Environment environment = this.environment;
    if (this.environment.getParameters().getBoolean(
      "pastry_factory_multipleNodes")) {
      if (this.environment.getLogManager() instanceof CloneableLogManager) {
        LogManager lman = ((CloneableLogManager) this.environment
          .getLogManager()).clone("0x" + nodeId.toStringBare());
        SelectorManager sman = this.environment.getSelectorManager();
        Processor proc = this.environment.getProcessor();
        if (this.environment.getParameters().getBoolean(
          "pastry_factory_selectorPerNode")) {
          sman = new SelectorManager(nodeId.toString() + " Selector",
            this.environment.getTimeSource(), lman);
        }
        if (this.environment.getParameters().getBoolean(
          "pastry_factory_processorPerNode")) {
          proc = new SimpleProcessor(nodeId.toString() + " Processor");
        }

        environment = new Environment(sman, proc, this.environment
          .getRandomSource(), this.environment.getTimeSource(), lman,
          this.environment.getParameters());

        this.environment.addDestructable(environment);
      }
    }

    final SocketPastryNode pn = new SocketPastryNode(nodeId, environment);

    SocketSourceRouteManager srManager = null;
    EpochInetSocketAddress localAddress = null;
    EpochInetSocketAddress proxyAddress = null;
    // NOTE: We _don't_ want to use the environment RandomSource because this
    // will cause
    // problems if we run the same node twice quickly with the same seed. Epochs
    // should really
    // be different every time.
    long epoch = random.nextLong();

    synchronized (this) {
      localAddress = getEpochAddress(port, epoch);

      if (pAddress == null) {
        proxyAddress = localAddress;
      } else {
        proxyAddress = new EpochInetSocketAddress(pAddress, epoch);
      }

      srManager = new SocketSourceRouteManager(pn, localAddress, proxyAddress,
        random);
      if (environment.getParameters().getBoolean("pastry_socket_increment_port_after_construction")) {
        port++;
      }
    }

    pn.setSocketSourceRouteManager(srManager);
    SocketNodeHandle localhandle = new SocketNodeHandle(proxyAddress, nodeId);
    localhandle = (SocketNodeHandle) pn.coalesce(localhandle);
    SocketPastrySecurityManager secureMan = new SocketPastrySecurityManager(
      localhandle);
    MessageDispatch msgDisp = new MessageDispatch(pn);
    RoutingTable routeTable = new RoutingTable(localhandle, rtMax, rtBase,
      environment);
    LeafSet leafSet = new LeafSet(localhandle, lSetSize);

    StandardRouter router = new StandardRouter(pn, secureMan);
    msgDisp.registerReceiver(router.getAddress(), router);

    StandardRouteSetProtocol rsProtocol = new StandardRouteSetProtocol(
      localhandle, secureMan, routeTable, environment);
    msgDisp.registerReceiver(rsProtocol.getAddress(), rsProtocol);

    pn.setElements(localhandle, secureMan, msgDisp, leafSet, routeTable);
    pn.setSocketElements(proxyAddress, leafSetMaintFreq, routeSetMaintFreq);
    secureMan.setLocalPastryNode(pn);

    PeriodicLeafSetProtocol lsProtocol = new PeriodicLeafSetProtocol(pn,
      localhandle, secureMan, leafSet, routeTable);
    // msgDisp.registerReceiver(lsProtocol.getAddress(), lsProtocol);
    ConsistentJoinProtocol jProtocol = new ConsistentJoinProtocol(pn,
      localhandle, secureMan, routeTable, leafSet);

    if (bootstrap != null) {
      bootstrap = (SocketNodeHandle) pn.coalesce(bootstrap);
    }

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }

    pn.doneNode(getNearest(localhandle, bootstrap));
    // pn.doneNode(bootstrap);

    return pn;
  }

  /**
   * Method which can be used to test the connectivity contstrains of the local
   * node. This (optional) method is designed to be called by applications to
   * ensure that the local node is able to connect through the network - checks
   * can be done to check TCP/UDP connectivity, firewall setup, etc... If the
   * method works, then nothing should be done and the method should return. If
   * an error condition is detected, an exception should be thrown.
   *
   * @param timeout DESCRIBE THE PARAMETER
   * @param local DESCRIBE THE PARAMETER
   * @param existing DESCRIBE THE PARAMETER
   * @param env DESCRIBE THE PARAMETER
   * @param logger DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public static InetSocketAddress verifyConnection(int timeout,
                                                   InetSocketAddress local, InetSocketAddress[] existing, Environment env,
                                                   Logger logger) throws IOException {
    if (logger.level <= Logger.INFO) {
      logger.log("Verifying connection of local node " + local + " using "
        + existing[0] + " and " + existing.length + " more");
    }
    DatagramSocket socket = null;

    try {
      socket = new DatagramSocket(local);
      socket.setSoTimeout(timeout);

      for (int i = 0; i < existing.length; i++) {
        byte[] buf = PingManager.addHeader(SourceRoute.build(new EpochInetSocketAddress(existing[i])),
          new IPAddressRequestMessage(env.getTimeSource()
          .currentTimeMillis()), new EpochInetSocketAddress(local),
          env, logger);
        DatagramPacket send = new DatagramPacket(buf, buf.length, existing[i]);
        socket.send(send);
      }

      DatagramPacket receive = new DatagramPacket(new byte[10000], 10000);
      socket.receive(receive);

      byte[] data = new byte[receive.getLength() - 38];
      System.arraycopy(receive.getData(), 38, data, 0, data.length);

      return ((IPAddressResponseMessage) PingManager.deserialize(data, env,
        null, logger)).getAddress();
    } finally {
      if (socket != null) {
        socket.close();
      }
    }
  }

  /**
   * DESCRIBE THE CLASS
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */
  class TimerContinuation implements Continuation {
    /**
     * DESCRIBE THE FIELD
     */
    public Object ret = null;

    /**
     * DESCRIBE THE METHOD
     *
     * @param result DESCRIBE THE PARAMETER
     */
    public void receiveResult(Object result) {
      ret = result;
      synchronized (this) {
        this.notify();
      }
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param result DESCRIBE THE PARAMETER
     */
    public void receiveException(Exception result) {
      synchronized (this) {
        this.notify();
      }
    }
  }
}

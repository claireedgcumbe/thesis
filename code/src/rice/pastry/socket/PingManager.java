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

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.environment.params.Parameters;
import rice.environment.time.TimeSource;
import rice.pastry.*;
import rice.pastry.socket.messaging.*;
import rice.selector.*;

/**
 * @version $Id: PingManager.java 3203 2006-04-17 18:07:26Z jstewart $
 * @author jeffh To change the template for this generated type comment go to
 *      Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PingManager extends SelectionKeyHandler {

  // whether or not we should use short pings
  /**
   * DESCRIBE THE FIELD
   */
  public final boolean USE_SHORT_PINGS;

  // the size of the buffer used to read incoming datagrams must be big enough
  // to encompass multiple datagram packets
  /**
   * DESCRIBE THE FIELD
   */
  public final int DATAGRAM_RECEIVE_BUFFER_SIZE;

  // the size of the buffer used to send outgoing datagrams this is also the
  // largest message size than can be sent via UDP
  /**
   * DESCRIBE THE FIELD
   */
  public final int DATAGRAM_SEND_BUFFER_SIZE;

  // SourceRoute -> ArrayList of PingResponseListener
  /**
   * DESCRIBE THE FIELD
   */
  protected WeakHashMap pingListeners = new WeakHashMap();

  // SourceRoute -> Long
  /**
   * DESCRIBE THE FIELD
   */
  protected WeakHashMap lastPingTime = new WeakHashMap();

  // The list of pending meesages
  /**
   * DESCRIBE THE FIELD
   */
  protected ArrayList pendingMsgs;

  // the buffer used for writing datagrams
  private ByteBuffer buffer;

  // the channel used from talking to the network
  private DatagramChannel channel;

  // the key used to determine what has taken place
  private SelectionKey key;

  // the source route manager
  private SocketSourceRouteManager manager;

  // the local address of this node
  private EpochInetSocketAddress localAddress;

  // the local node
  private SocketPastryNode spn;

  private Logger logger;

  private TimeSource timeSource;

  private Environment environment;

  long lastTimePrinted = 0;

  /**
   * DESCRIBE THE FIELD
   */
  public final static int PING_THROTTLE = 1500;
  // = false;

  // the header which signifies a normal socket
    /**
   * DESCRIBE THE FIELD
   */
  protected static byte[] HEADER_PING = new byte[]{0x49, 0x3A, 0x09, 0x5C};

  // the header which signifies a new, shorter ping
  /**
   * DESCRIBE THE FIELD
   */
  protected static byte[] HEADER_SHORT_PING = new byte[]{0x31, 0x1C, 0x0E, 0x11};

  // the header which signifies a new, shorter ping
  /**
   * DESCRIBE THE FIELD
   */
  protected static byte[] HEADER_SHORT_PING_RESPONSE = new byte[]{0x31, 0x1C, 0x0E, 0x12};

  // the length of the ping header
  /**
   * DESCRIBE THE FIELD
   */
  public static int HEADER_SIZE = HEADER_PING.length;

  /**
   * @param manager DESCRIBE THE PARAMETER
   * @param spn DESCRIBE THE PARAMETER
   * @param bindAddress DESCRIBE THE PARAMETER
   * @param proxyAddress DESCRIBE THE PARAMETER
   */
  public PingManager(SocketPastryNode spn, SocketSourceRouteManager manager, EpochInetSocketAddress bindAddress, EpochInetSocketAddress proxyAddress) {
    this.spn = spn;
    this.environment = spn.getEnvironment();
    this.logger = environment.getLogManager().getLogger(PingManager.class, null);
    this.timeSource = environment.getTimeSource();

    Parameters p = environment.getParameters();
    this.manager = manager;
    this.pendingMsgs = new ArrayList();
    this.localAddress = proxyAddress;
    USE_SHORT_PINGS = p.getBoolean("pastry_socket_pingmanager_smallPings");
    DATAGRAM_RECEIVE_BUFFER_SIZE = p.getInt("pastry_socket_pingmanager_datagram_receive_buffer_size");
    DATAGRAM_SEND_BUFFER_SIZE = p.getInt("pastry_socket_pingmanager_datagram_send_buffer_size");

    // allocate enought bytes to read data
    this.buffer = ByteBuffer.allocateDirect(DATAGRAM_SEND_BUFFER_SIZE);

    try {
      // bind to the appropriate port
      channel = DatagramChannel.open();
      channel.configureBlocking(false);
      channel.socket().setReuseAddress(true);
      channel.socket().bind(bindAddress.getAddress());
      channel.socket().setSendBufferSize(DATAGRAM_SEND_BUFFER_SIZE);
      channel.socket().setReceiveBufferSize(DATAGRAM_RECEIVE_BUFFER_SIZE);

      key = environment.getSelectorManager().register(channel, this, 0);
      key.interestOps(SelectionKey.OP_READ);
    } catch (IOException e) {
      if (logger.level <= Logger.SEVERE) {
        logger.log(
          "PANIC: Error binding datagram server to address " + localAddress + ": " + e);
      }
    }
  }

  /**
   * ----- EXTERNAL METHODS -----
   *
   * @param path DESCRIBE THE PARAMETER
   * @param prl DESCRIBE THE PARAMETER
   */

  /**
   * Method which actually sends a ping to over the specified path, and returns
   * the result to the specified listener. Note that if no ping response is ever
   * received, the listener is never called.
   *
   * @param path The path to send the ping over
   * @param prl The listener which should hear about the response
   */
  protected void ping(SourceRoute path, PingResponseListener prl) {
    // this code is to throttle pings
    // I don't know what to do if there is a prl, because it is difficult to know
    // if there is still an outstanding ping, so we can only throttle if there is no
    // prl
    long curTime = timeSource.currentTimeMillis();
    if (prl == null) {
      Long time = (Long) lastPingTime.get(path);
      if (time != null) {
        if ((time.longValue() + PING_THROTTLE) > curTime) {
          if (logger.level <= Logger.FINE) {
            logger.log(
              "(PM) Suppressing ping via path " + path + " local " + localAddress);
          }
          return;
        }
      }
    }

    if (logger.level <= Logger.FINE) {
      logger.log(
        "(PM) Actually sending ping via path " + path + " local " + localAddress);
    }

    lastPingTime.put(path, new Long(curTime));

    addPingResponseListener(path, prl);

    if (USE_SHORT_PINGS) {
      sendShortPing(path);
    } else {
      enqueue(path, new PingMessage(path, path.reverse(localAddress), environment.getTimeSource().currentTimeMillis()));
    }
  }

  /**
   * Makes this node resign from the network. Is designed to be used for
   * debugging and testing.
   *
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected void resign() throws IOException {
    if (key != null) {
      if (key.channel() != null) {
        key.channel().close();
      }
      key.cancel();
    }
  }

  /**
   * Internal testing method which simulates a stall. DO NOT USE!!!!!
   */
  public void stall() {
    key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
  }

  /**
   * ----- INTERNAL METHODS -----
   *
   * @param route DESCRIBE THE PARAMETER
   */

  /**
   * Builds the data for a short ping
   *
   * @param route DESCRIBE THE PARAMETER
   */
  protected void sendShortPing(SourceRoute route) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);

      dos.write(HEADER_SHORT_PING);
      dos.writeLong(environment.getTimeSource().currentTimeMillis());

      dos.flush();

      enqueue(route, baos.toByteArray());
    } catch (Exception canthappen) {
      if (logger.level <= Logger.SEVERE) {
        logger.log(
          "CANT HAPPEN: " + canthappen);
      }
    }
  }

  /**
   * Builds the data for a short ping response
   *
   * @param route DESCRIBE THE PARAMETER
   * @param payload DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected void shortPingReceived(SourceRoute route, byte[] payload) throws IOException {
    System.arraycopy(HEADER_SHORT_PING_RESPONSE, 0, payload, 0, HEADER_SHORT_PING_RESPONSE.length);
    enqueue(route.reverse(), payload);
  }

  /**
   * Processes a short ping response
   *
   * @param route DESCRIBE THE PARAMETER
   * @param payload DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected void shortPingResponseReceived(SourceRoute route, byte[] payload) throws IOException {
    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(payload));
    dis.readFully(new byte[HEADER_SHORT_PING_RESPONSE.length]);
    long start = dis.readLong();
    int ping = (int) (environment.getTimeSource().currentTimeMillis() - start);

    SourceRoute from = route.reverse();

    manager.markAlive(from);
    manager.markProximity(from, ping);
    notifyPingResponseListeners(from, ping, start);
  }

  /**
   * Adds a feature to the PingResponseListener attribute of the PingManager
   * object
   *
   * @param prl The feature to be added to the PingResponseListener attribute
   * @param path DESCRIBE THE PARAMETER
   */
  protected void removePingResponseListener(SourceRoute path, PingResponseListener prl) {
    if (prl == null) {
      return;
    }

    ArrayList list = (ArrayList) pingListeners.get(path);

    if (list != null) {
      // remove all
      while (list.remove(prl)) {
        ;
      }
    }
  }

  /**
   * Adds a feature to the PingResponseListener attribute of the PingManager
   * object
   *
   * @param prl The feature to be added to the PingResponseListener attribute
   * @param path The feature to be added to the PingResponseListener attribute
   */
  protected void addPingResponseListener(SourceRoute path, PingResponseListener prl) {
    if (prl == null) {
      return;
    }

    ArrayList list = (ArrayList) pingListeners.get(path);

    if (list == null) {
      list = new ArrayList();
      pingListeners.put(path, list);
    }

    list.add(prl);
  }

  /**
   * caller must synchronized(pingResponseTimes)
   *
   * @param proximity
   * @param lastTimePinged
   * @param path DESCRIBE THE PARAMETER
   */
  protected void notifyPingResponseListeners(SourceRoute path, int proximity, long lastTimePinged) {
    ArrayList list = (ArrayList) pingListeners.remove(path);

    if (list != null) {
      Iterator i = list.iterator();

      while (i.hasNext()) {
        ((PingResponseListener) i.next()).pingResponse(path, proximity, lastTimePinged);
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param msg DESCRIBE THE PARAMETER
   * @param path DESCRIBE THE PARAMETER
   */
  public void enqueue(SourceRoute path, Object msg) {
    try {
      byte[] data = addHeader(path, msg, localAddress, environment, logger);

      synchronized (pendingMsgs) {
        pendingMsgs.add(new Envelope(path.getFirstHop(), data));
      }

      if (spn != null) {
        ((SocketPastryNode) spn).broadcastSentListeners(msg, path.getLastHop().address, data.length, NetworkListener.TYPE_UDP);
      }

      if (!(msg instanceof byte[])) {
        if (logger.level <= Logger.FINER) {
          logger.log(
            "COUNT: Sent message " + msg.getClass() + " of size " + data.length + " to " + path);
        }
      } else if (((byte[]) msg)[3] == 0x11) {
        if (logger.level <= Logger.FINER) {
          logger.log(
            "COUNT: Sent message rice.pastry.socket.messaging.ShortPingMessage of size " + data.length + " to " + path);
        }
      } else if (((byte[]) msg)[3] == 0x12) {
        if (logger.level <= Logger.FINER) {
          logger.log(
            "COUNT: Sent message rice.pastry.socket.messaging.ShortPingResponseMessage of size " + data.length + " to " + path);
        }
      }

      environment.getSelectorManager().modifyKey(key);
    } catch (IOException e) {
      if (logger.level <= Logger.SEVERE) {
        logger.log(
          "ERROR: Received exceptoin " + e + " while enqueuing ping " + msg);
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param message DESCRIBE THE PARAMETER
   * @param size DESCRIBE THE PARAMETER
   * @param from DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public void receiveMessage(Object message, int size, InetSocketAddress from) throws IOException {
    if (message instanceof DatagramMessage) {
      DatagramMessage dm = (DatagramMessage) message;
      long start = dm.getStartTime();
      SourceRoute path = dm.getInboundPath();

      if (path == null) {
        path = SourceRoute.build(new EpochInetSocketAddress(from));
      }

      if (spn != null) {
        ((SocketPastryNode) spn).broadcastReceivedListeners(dm, path.reverse().getLastHop().address, size, NetworkListener.TYPE_UDP);
      }

      if (dm instanceof PingMessage) {
        if (logger.level <= Logger.FINER) {
          logger.log(
            "COUNT: Read message " + message.getClass() + " of size " + size + " from " + dm.getInboundPath().reverse());
        }

        enqueue(dm.getInboundPath(), new PingResponseMessage(dm.getOutboundPath(), dm.getInboundPath(), start));
      } else if (dm instanceof PingResponseMessage) {
        if (logger.level <= Logger.FINER) {
          logger.log(
            "COUNT: Read message " + message.getClass() + " of size " + size + " from " + dm.getOutboundPath().reverse());
        }
        int ping = (int) (environment.getTimeSource().currentTimeMillis() - start);

        manager.markAlive(dm.getOutboundPath());
        manager.markProximity(dm.getOutboundPath(), ping);
        notifyPingResponseListeners(dm.getOutboundPath(), ping, start);
      } else if (dm instanceof WrongEpochMessage) {
        WrongEpochMessage wem = (WrongEpochMessage) dm;

        if (logger.level <= Logger.FINER) {
          logger.log(
            "COUNT: Read message " + message.getClass() + " of size " + size + " from " + dm.getOutboundPath().reverse());
        }

        manager.markAlive(dm.getOutboundPath());
        manager.markDead(wem.getIncorrect());
      } else if (dm instanceof IPAddressRequestMessage) {
        if (logger.level <= Logger.FINER) {
          logger.log(
            "COUNT: Read message " + message.getClass() + " of size " + size + " from " + SourceRoute.build(new EpochInetSocketAddress(from)));
        }

        enqueue(SourceRoute.build(new EpochInetSocketAddress(from)), new IPAddressResponseMessage(from, environment.getTimeSource().currentTimeMillis()));
      } else {
        if (logger.level <= Logger.WARNING) {
          logger.log(
            "ERROR: Received unknown DatagramMessage " + dm);
        }
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   */
  public void read(SelectionKey key) {
    try {
      InetSocketAddress address = null;

      while ((address = (InetSocketAddress) channel.receive(buffer)) != null) {
        buffer.flip();

        /*
         *  if (address.getPort() % 2 == localAddress.getAddress().getPort() % 2) {
         *  buffer.clear();
         *  log(Logger.INFO,"Dropping packet");
         *  return;
         *  }
         */
        if (buffer.remaining() > 0) {
          readHeader(address);
        } else {
          if (logger.level <= Logger.INFO) {
            logger.log(
              "(PM) Read from datagram channel, but no bytes were there - no bad, but wierd.");
          }
          break;
        }
      }
    } catch (IOException e) {
      if (logger.level <= Logger.WARNING) {
        logger.logException(
          "ERROR (datagrammanager:read): ", e);
      }
    } finally {
      buffer.clear();
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   */
  public void write(SelectionKey key) {
    Envelope write = null;

    try {
      synchronized (pendingMsgs) {
        Iterator i = pendingMsgs.iterator();

        while (i.hasNext()) {
          write = (Envelope) i.next();

          try {
            if (channel.send(ByteBuffer.wrap(write.data), write.destination.getAddress()) == write.data.length) {
              i.remove();
            } else {
              break;
            }
          } catch (IOException e) {
            i.remove();
            throw e;
          }
        }
      }
    } catch (IOException e) {
      if (logger.level <= Logger.WARNING) {
        // This code prevents this line from filling up logs during some kinds of network outages
        // it makes this error only be printed 1ce/second
        long now = timeSource.currentTimeMillis();
        if (lastTimePrinted + 1000 > now) {
          return;
        }
        lastTimePrinted = now;

        logger.logException(
          "ERROR (datagrammanager:write) to " + (write == null ? null : write.destination.getAddress()), e);
      }
    } finally {
      if (pendingMsgs.isEmpty()) {
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   */
  public void modifyKey(SelectionKey key) {
    synchronized (pendingMsgs) {
      if (!pendingMsgs.isEmpty()) {
        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
      }
    }
  }

  /**
   * Method which adds a header for the provided path to the given data.
   *
   * @param header DESCRIBE THE PARAMETER
   * @return The messag with a header attached
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public SourceRoute decodeHeader(byte[] header) throws IOException {
    EpochInetSocketAddress[] route = new EpochInetSocketAddress[header.length / SocketChannelRepeater.HEADER_BUFFER_SIZE];

    for (int i = 0; i < route.length; i++) {
      route[i] = SocketChannelRepeater.decodeHeader(header, i);
    }

    return SourceRoute.build(route);
  }

  /**
   * Method which processes an incoming message and hands it off to the
   * appropriate handler.
   *
   * @param address DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected void readHeader(InetSocketAddress address) throws IOException {
    byte[] header = new byte[HEADER_SIZE];
    buffer.get(header);

    if (Arrays.equals(header, HEADER_PING)) {
      byte[] metadata = new byte[2];
      buffer.get(metadata);

      // first, read all of the source route
      byte[] route = new byte[SocketChannelRepeater.HEADER_BUFFER_SIZE * metadata[1]];
      buffer.get(route);

      // now, check to make sure our hop is correct
      EpochInetSocketAddress eisa = SocketChannelRepeater.decodeHeader(route, metadata[0]);

      // if so, process the packet
      if ((eisa.equals(localAddress)) || (eisa.getAddress().equals(localAddress.getAddress()) &&
        (eisa.getEpoch() == EpochInetSocketAddress.EPOCH_UNKNOWN))) {
        // if the packet is at the end of the route, accept it
        // otherwise, forward it to the next hop (and increment the stamp)
        if (metadata[0] + 1 == metadata[1]) {
          // The message was meant for me
          byte[] array = new byte[buffer.remaining()];
          buffer.get(array);
          buffer.clear();

          byte[] test = new byte[HEADER_SHORT_PING.length];
          System.arraycopy(array, 0, test, 0, test.length);

          SourceRoute sr = decodeHeader(route).removeLastHop();

          if (Arrays.equals(test, HEADER_SHORT_PING)) {
            // the PING was meant for me
            int len = (header.length + metadata.length + array.length + route.length);
            if (logger.level <= Logger.FINER) {
              logger.log(
                "COUNT: Read message rice.pastry.socket.messaging.ShortPingMessage of size " + len + " from " + sr);
            }
            if (spn != null) {
              ((SocketPastryNode) spn).broadcastReceivedListeners(array, address, len, NetworkListener.TYPE_UDP);
            }

            shortPingReceived(sr, array);
          } else if (Arrays.equals(test, HEADER_SHORT_PING_RESPONSE)) {
            // the PING_RESPONSE was meant for me
            int len = (header.length + metadata.length + array.length + route.length);
            if (logger.level <= Logger.FINER) {
              logger.log(
                "COUNT: Read message rice.pastry.socket.messaging.ShortPingResponseMessage of size " + len + " from " + sr);
            }

            if (spn != null) {
              ((SocketPastryNode) spn).broadcastReceivedListeners(array, address, len, NetworkListener.TYPE_UDP);
            }
            shortPingResponseReceived(sr, array);
          } else {
            receiveMessage(deserialize(array, environment, spn, logger), array.length, address);
          }
        } else {
          // sourceroute hop
          EpochInetSocketAddress next = SocketChannelRepeater.decodeHeader(route, metadata[0] + 1);
          buffer.position(0);
          byte[] packet = new byte[buffer.remaining()];
          buffer.get(packet);

          // increment the hop count
          packet[HEADER_SIZE]++;

          if (spn != null) {
            ((SocketPastryNode) spn).broadcastReceivedListeners(packet, address, packet.length, NetworkListener.TYPE_SR_UDP);
            ((SocketPastryNode) spn).broadcastSentListeners(packet, next.address, packet.length, NetworkListener.TYPE_SR_UDP);
          }

          synchronized (pendingMsgs) {
            pendingMsgs.add(new Envelope(next, packet));
          }

          environment.getSelectorManager().modifyKey(key);
        }
      } else {
        // if this is an old epoch of ours, reply with an update
        if (eisa.getAddress().equals(localAddress.getAddress())) {
          SourceRoute back = SourceRoute.build(new EpochInetSocketAddress[0]);
          SourceRoute outbound = SourceRoute.build(new EpochInetSocketAddress[0]);

          for (int i = 0; i < metadata[0]; i++) {
            back = back.append(SocketChannelRepeater.decodeHeader(route, i));
            if (i > 0) {
              outbound = outbound.append(SocketChannelRepeater.decodeHeader(route, i));
            }
          }

          outbound = outbound.append(localAddress);
//          if (spn != null) {
//            ((SocketPastryNode) spn).broadcastReceivedListeners(packet, address, packet.length, NetworkListener.TYPE_SR_UDP);
//          }

          enqueue(back.reverse(), new WrongEpochMessage(outbound, back.reverse(), eisa, localAddress, environment.getTimeSource().currentTimeMillis()));
        } else {
          if (logger.level <= Logger.WARNING) {
            logger.log(
              "WARNING: Received packet destined for EISA (" + metadata[0] + " " + metadata[1] + ") " + eisa + " but the local address is " + localAddress + " - dropping silently.");
          }
          throw new IOException("Received packet destined for EISA (" + metadata[0] + " " + metadata[1] + ") " + eisa + " but the local address is " + localAddress + " - dropping silently.");
        }
      }
    } else {
      if (logger.level <= Logger.WARNING) {
        logger.log(
          "WARNING: Received unrecognized message header - ignoring from " + address + ".");
      }
      throw new IOException("Improper message header received - ignoring from " + address + ". Read " + ((byte) header[0]) + " " + ((byte) header[1]) + " " + ((byte) header[2]) + " " + ((byte) header[3]));
    }
  }

  /**
   * Method which serializes a given object into a ByteBuffer, in order to
   * prepare it for writing.
   *
   * @param message DESCRIBE THE PARAMETER
   * @param environment DESCRIBE THE PARAMETER
   * @param logger DESCRIBE THE PARAMETER
   * @return A ByteBuffer containing the object
   * @exception IOException if the object can't be serialized
   */
  public static byte[] serialize(Object message, Environment environment, Logger logger) throws IOException {
    try {

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(message);
      oos.close();

      byte[] ret = baos.toByteArray();
      return ret;
    } catch (InvalidClassException e) {
      if (logger.level <= Logger.SEVERE) {
        logger.logException(
          "PANIC: Object to be serialized was an invalid class!", e);
      }
      throw new IOException("Invalid class during attempt to serialize.");
    } catch (NotSerializableException e) {
      if (logger.level <= Logger.SEVERE) {
        logger.logException(
          "PANIC: Object to be serialized was not serializable! [" + message + "]", e);
      }
      throw new IOException("Unserializable class " + message + " during attempt to serialize.");
    }
  }

  /**
   * Method which takes in a ByteBuffer read from a datagram, and deserializes
   * the contained object.
   *
   * @param array DESCRIBE THE PARAMETER
   * @param env DESCRIBE THE PARAMETER
   * @param spn DESCRIBE THE PARAMETER
   * @param logger DESCRIBE THE PARAMETER
   * @return The deserialized object.
   * @exception IOException if the buffer can't be deserialized
   */
  public static Object deserialize(byte[] array, Environment env, SocketPastryNode spn, Logger logger) throws IOException {
    PastryObjectInputStream ois = new PastryObjectInputStream(new ByteArrayInputStream(array), spn);

    try {
      Object ret = ois.readObject();
      return ret;
    } catch (ClassNotFoundException e) {
      if (logger.level <= Logger.SEVERE) {
        logger.logException(
          "PANIC: Unknown class type in serialized message!", e);
      }
      throw new IOException("Unknown class type in message - closing channel.");
    } catch (InvalidClassException e) {
      if (logger.level <= Logger.SEVERE) {
        logger.logException(
          "PANIC: Serialized message was an invalid class!", e);
      }
      throw new IOException("Invalid class in message - closing channel.");
    }
  }

  /**
   * Method which adds a header for the provided path to the given data.
   *
   * @param path The feature to be added to the Header attribute
   * @param data The feature to be added to the Header attribute
   * @param localAddress The feature to be added to the Header attribute
   * @param env The feature to be added to the Header attribute
   * @param logger The feature to be added to the Header attribute
   * @return The messag with a header attached
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public static byte[] addHeader(SourceRoute path, Object data, EpochInetSocketAddress localAddress, Environment env, Logger logger) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    dos.write(HEADER_PING);
    dos.write((byte) 1);
    dos.write((byte) path.getNumHops() + 1);
    dos.write(SocketChannelRepeater.encodeHeader(localAddress));

    for (int i = 0; i < path.getNumHops(); i++) {
      dos.write(SocketChannelRepeater.encodeHeader(path.getHop(i)));
    }

    if (data instanceof byte[]) {
      dos.write((byte[]) data);
    } else {
      dos.write(serialize(data, env, logger));
    }

    dos.flush();

    return baos.toByteArray();
  }

  /**
   * Internal class which holds a pending datagram
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author amislove
   */
  public class Envelope {
    /**
     * DESCRIBE THE FIELD
     */
    protected EpochInetSocketAddress destination;
    /**
     * DESCRIBE THE FIELD
     */
    protected byte[] data;

    /**
     * Constructor for Envelope.
     *
     * @param destination DESCRIBE THE PARAMETER
     * @param data DESCRIBE THE PARAMETER
     */
    public Envelope(EpochInetSocketAddress destination, byte[] data) {
      this.destination = destination;
      this.data = data;
    }
  }
}

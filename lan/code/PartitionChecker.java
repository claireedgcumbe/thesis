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
 *  Created on Jul 5, 2005
 */
package rice.pastry.testing;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

import rice.environment.Environment;
import rice.pastry.*;
import rice.pastry.dist.DistPastryNodeFactory;
import rice.pastry.leafset.LeafSet;
import rice.pastry.socket.*;

/**
 * Pass in a certificate which contains bootstrap nodes. Output is an ordered
 * list of independent rings, and which bootstrap nodes belong to each.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public class PartitionChecker {

  /**
   * DESCRIBE THE FIELD
   */
  protected final int MAX_THREADS = 100;

  int numThreads = 0;

  /**
   * Set of InetSocketAddress The set of bootstrap nodes that need to be
   * checked. Gained from the certificate. This list is reduced each time a node
   * from this ring is identified
   */
  HashSet unmatchedBootstraps;

  /**
   * of InetSocketAddress. This list is kept around.
   */
  HashSet bootstraps;

  final PrintStream ps = new PrintStream(new FileOutputStream("response.txt"));

  /**
   * A list of Ring. Increased whenever a new one is found.
   */
  ArrayList rings;

  Environment environment;

  HashSet dead = new HashSet();

  /**
   * Constructor for PartitionChecker.
   *
   * @param ringIdString DESCRIBE THE PARAMETER
   * @exception Exception DESCRIBE THE EXCEPTION
   */
  public PartitionChecker(String ringIdString) throws Exception {
    environment = new Environment();
    buildBootstrapSetFromCert(ringIdString);
    rings = new ArrayList();

    SocketPastryNodeFactory factory = new SocketPastryNodeFactory(null, 1, environment);

    while (unmatchedBootstraps.size() > 0) {
      rings.add(buildRing(factory, (InetSocketAddress) (unmatchedBootstraps.iterator().next())));
    }
    Collections.sort(rings);
    Iterator i = rings.iterator();
    while (i.hasNext()) {
      System.out.println(i.next());
    }
  }


  /**
   * DESCRIBE THE METHOD
   *
   * @param ringIdString DESCRIBE THE PARAMETER
   * @exception Exception DESCRIBE THE EXCEPTION
   */
  protected void buildBootstrapSetFromCert(String ringIdString) throws Exception {
    unmatchedBootstraps = new HashSet();
    bootstraps = new HashSet();

//    byte[] ringIdbytes = ringIdString.getBytes();


//    InetSocketAddress[] addr = RingCertificate.getCertificate(null).getBootstraps();

    BufferedReader in = new BufferedReader(new FileReader(ringIdString));

    String line;

    while (in.ready()) {
      line = in.readLine();
      String[] parts = line.split(":");

      int port = 10003;
      if (parts.length > 1) {
        port = Integer.parseInt(parts[1]);
      }
      InetSocketAddress addr = new InetSocketAddress(parts[0], port);

      unmatchedBootstraps.add(addr);
      bootstraps.add(addr);
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param factory DESCRIBE THE PARAMETER
   * @param bootstrap DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   * @exception Exception DESCRIBE THE EXCEPTION
   */
  protected Ring buildRing(final DistPastryNodeFactory factory, InetSocketAddress bootstrap) throws Exception {
//    System.out.println("buildRing("+bootstrap+")");
    unmatchedBootstraps.remove(bootstrap);
    numThreads = 0;
    final Ring ring = new Ring(bootstrap);

//    final HashMap leafsets = new HashMap();
    final HashSet unseen = new HashSet();

    unseen.add(factory.getNodeHandle(bootstrap, 20000));

    synchronized (unseen) {
      while (true) {
        if (numThreads >= MAX_THREADS) {
          unseen.wait();
        }

        if (unseen.size() > 0) {
          numThreads++;

          final SocketNodeHandle handle = (SocketNodeHandle) unseen.iterator().next();
          if (handle == null) {
            break;
          }
          unseen.remove(handle);
          ring.addNode(handle);
//            System.out.println("Fetching leafset of " + handle + " (thread " + numThreads + " of "+MAX_THREADS+")");

          Thread t =
            new Thread() {
              public void run() {
                try {
                  LeafSet ls = factory.getLeafSet(handle);
//                  System.out.println("Response:"+handle+" "+ring.getName()+" "+ls);

                  ps.println(handle.getEpochAddress().getAddress().getAddress().getHostAddress() + ":" + handle.getEpochAddress().getAddress().getPort());
                  //        SourceRoute[] routes = factory.getRoutes(handle);

                  //        for (int i=0; i<routes.length; i++)
                  //          System.out.println("ROUTE:\t" + routes[i].prepend(handle.getEpochAddress()));


                  NodeSet ns = ls.neighborSet(Integer.MAX_VALUE);

                  if (!ns.get(0).equals(handle)) {
                    ring.addFailure(handle, new Exception("Node is now " + ns.get(0)));
                  }

                  synchronized (unseen) {
                    for (int i = 1; i < ns.size(); i++) {
                      if ((!ring.contains(ns.get(i))) && (!dead.contains(ns.get(i)))) {
                        unseen.add(ns.get(i));
                      }
                    }
                  }

                } catch (java.net.ConnectException e) {
                  ring.addFailure(handle, e);
                  dead.add(handle);
                } catch (java.net.SocketTimeoutException e) {
                  ring.addFailure(handle, e);
                  dead.add(handle);
                } catch (IOException e) {
//                  environment.getLogManager().getLogger(PastryNetworkTest.class, null).log(Logger.WARNING,"GOT OTHER ERROR CONNECTING TO " + handle + " - " + e);
                } finally {
                  synchronized (unseen) {
                    numThreads--;
                    unseen.notifyAll();
                  }
                }
              }
            };

          t.start();
        } else if (numThreads > 0) {
          unseen.wait();
        } else {
          break;
        }
      }
    }

//    System.out.println("Fetched all leafsets - return...  Found " + nodes.size() + " nodes.");

    System.out.println("buildRing() complete:" + ring);
    return ring;
  }

  /**
   * The main program for the PartitionChecker class
   *
   * @param args The command line arguments
   * @exception Exception DESCRIBE THE EXCEPTION
   */
  public static void main(String[] args) throws Exception {
    new PartitionChecker(args[0]);
    System.exit(0);
  }


  /**
   * DESCRIBE THE CLASS
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */
  class Ring implements Comparable {
    /**
     * of InetSocketAddress
     */
    HashSet myBootstraps;
    /**
     * of NodeHandle
     */
    HashSet nodes;

    String name;

    /**
     * Constructor for Ring.
     *
     * @param bootAddr DESCRIBE THE PARAMETER
     */
    public Ring(InetSocketAddress bootAddr) {
      name = bootAddr.toString();
      myBootstraps = new HashSet();
      myBootstraps.add(bootAddr);
      nodes = new HashSet();
    }

    /**
     * @return
     */
    public String getName() {
      return name;
    }

    /**
     * @param handle
     * @return
     */
    public boolean contains(NodeHandle handle) {
      return nodes.contains(handle);
    }

    /**
     * @param handle
     * @param e
     */
    public void addFailure(SocketNodeHandle handle, Exception e) {
//      System.err.println("Failure:"+handle);
//      e.printStackTrace(System.err);
    }

    /**
     * Adds a feature to the Node attribute of the Ring object
     *
     * @param snh The feature to be added to the Node attribute
     */
    public synchronized void addNode(SocketNodeHandle snh) {

      InetSocketAddress newAddr = snh.getAddress();
      synchronized (unmatchedBootstraps) {
        if (unmatchedBootstraps.contains(newAddr)) {
          unmatchedBootstraps.remove(newAddr);
        }
      }

      if (bootstraps.contains(newAddr)) {
        myBootstraps.add(newAddr);
      }
      nodes.add(snh);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @return DESCRIBE THE RETURN VALUE
     */
    public String toString() {
      String s = nodes.size() + ":" + myBootstraps.size() + ": boots:";
      synchronized (myBootstraps) {
        Iterator i = myBootstraps.iterator();
        s += i.next();
        while (i.hasNext()) {
          s += "," + i.next();
        }
      }
      s += " non-boots:";
      synchronized (nodes) {
        Iterator i = nodes.iterator();
        while (i.hasNext()) {
          Object nxt = i.next();
          if (!bootstraps.contains(nxt)) {
            s += "," + nxt;
          }
        }
      }
      return s;
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param arg0 DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public int compareTo(Object arg0) {
      Ring that = (Ring) arg0;
      return this.size() - that.size();
//      return that.size() - this.size();
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @return DESCRIBE THE RETURN VALUE
     */
    public int size() {
      return nodes.size();
    }
  }
}

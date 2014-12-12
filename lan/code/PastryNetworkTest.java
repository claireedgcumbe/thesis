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
package rice.pastry.testing;

import java.io.*;
import java.net.*;
import java.util.*;

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.pastry.*;
import rice.pastry.routing.*;
import rice.pastry.leafset.*;
import rice.pastry.dist.*;
import rice.pastry.socket.*;

/**
 * Utility class for checking the consistency of an existing pastry network.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class PastryNetworkTest {

  /**
   * DESCRIBE THE FIELD
   */
  protected SocketPastryNodeFactory factory;

  /**
   * DESCRIBE THE FIELD
   */
  protected InetSocketAddress bootstrap;

  /**
   * DESCRIBE THE FIELD
   */
  protected HashSet nodes;

  /**
   * DESCRIBE THE FIELD
   */
  protected HashSet dead;

  /**
   * DESCRIBE THE FIELD
   */
  protected HashSet unknown;

  /**
   * DESCRIBE THE FIELD
   */
  protected final int MAX_THREADS = 100;

  /**
   * DESCRIBE THE FIELD
   */
  protected Environment environment;

  int numThreads = 0;

  /**
   * Constructor for PastryNetworkTest.
   *
   * @param env DESCRIBE THE PARAMETER
   * @param factory DESCRIBE THE PARAMETER
   * @param bootstrap DESCRIBE THE PARAMETER
   */
  public PastryNetworkTest(Environment env, SocketPastryNodeFactory factory, InetSocketAddress bootstrap) {
    this.environment = env;
    this.factory = factory;
    this.bootstrap = bootstrap;
    this.nodes = new HashSet();
    this.dead = new HashSet();
    this.unknown = new HashSet();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception Exception DESCRIBE THE EXCEPTION
   */
  protected HashMap fetchLeafSets() throws Exception {
    final HashMap leafsets = new HashMap();
    final HashSet unseen = new HashSet();

    final PrintStream ps = new PrintStream(new FileOutputStream("response.txt"));

    unseen.add(((DistPastryNodeFactory) factory).getNodeHandle(bootstrap));

    synchronized (unseen) {
      while (true) {
        if (numThreads >= MAX_THREADS) {
          unseen.wait();
        }

        if (unseen.size() > 0) {
          numThreads++;

          final SocketNodeHandle handle = (SocketNodeHandle) unseen.iterator().next();
          unseen.remove(handle);
          nodes.add(handle);
          System.out.println("Fetching leafset of " + handle + " (thread " + numThreads + " of " + MAX_THREADS + ")");

          Thread t =
            new Thread() {
              public void run() {
                boolean gotResponse = false;
                try {
                  LeafSet ls = factory.getLeafSet(handle);
                  System.out.println("Response:" + handle + " " + ls);
                  gotResponse = true;
                  ps.println(handle.getEpochAddress().getAddress().getAddress().getHostAddress() + ":" + handle.getEpochAddress().getAddress().getPort());
                  //        SourceRoute[] routes = factory.getRoutes(handle);

                  //        for (int i=0; i<routes.length; i++)
                  //          System.out.println("ROUTE:\t" + routes[i].prepend(handle.getEpochAddress()));

                  leafsets.put(handle, ls);

                  NodeSet ns = ls.neighborSet(Integer.MAX_VALUE);

                  if (!ns.get(0).equals(handle)) {
                    dead.add(handle);
                    nodes.remove(handle);
                    leafsets.remove(handle);
                    leafsets.put(ns.get(0), ls);
                  }

                  for (int i = 1; i < ns.size(); i++) {
                    if ((!nodes.contains(ns.get(i))) && (!dead.contains(ns.get(i)))) {
                      unseen.add(ns.get(i));
                    }
                  }

                } catch (java.net.ConnectException e) {
                  dead.add(handle);
                } catch (java.net.SocketTimeoutException e) {
                  unknown.add(handle);
                } catch (IOException e) {
                  System.out.println("GOT OTHER ERROR CONNECTING TO " + handle + " - " + e);
                } finally {
                  if (!gotResponse) {
                    System.out.println("Did not hear from " + handle);
                  }
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

    System.out.println("Fetched all leafsets - return...  Found " + nodes.size() + " nodes.");

    return leafsets;
  }

  /**
   * A unit test for JUnit
   *
   * @exception Exception DESCRIBE THE EXCEPTION
   */
  protected void testLeafSets() throws Exception {
    HashMap leafsets = fetchLeafSets();

    Iterator sets = leafsets.values().iterator();

    while (sets.hasNext()) {
      Iterator nodes = leafsets.keySet().iterator();
      LeafSet set = (LeafSet) sets.next();

      if (set != null) {
        while (nodes.hasNext()) {
          NodeHandle node = (NodeHandle) nodes.next();

          if (dead.contains(node) && set.member(node)) {
            System.out.println("LEAFSET ERROR: Leafset for " + set.get(0) + " contains dead node " + node);
          } else if ((!dead.contains(node)) && set.isComplete() && set.test(node)) {
            System.out.println("LEAFSET ERROR: Leafset for " + set.get(0) + " is missing " + node);
          }
        }
      }
    }

    // check leafset sfor unknowns...

    System.out.println("Done testing...");
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param row DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected HashMap fetchRouteRow(int row) throws IOException {
    HashMap routerows = new HashMap();
    Iterator i = nodes.iterator();

    while (i.hasNext()) {
      NodeHandle handle = (NodeHandle) i.next();

      System.out.println("Fetching route row " + row + " of " + handle);

      RouteSet[] set = factory.getRouteRow(handle, row);

      if (set != null) {
        routerows.put(handle, set);
      }
    }

    System.out.println("Fetched all route rows - return...");

    return routerows;
  }

  /**
   * A unit test for JUnit
   *
   * @param row DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected void testRouteRow(int row) throws IOException {
    HashMap routerows = fetchRouteRow(row);

    Iterator i = nodes.iterator();

    while (i.hasNext()) {
      NodeHandle node = (NodeHandle) i.next();
      RoutingTable rt = new RoutingTable(node, 1, environment.getParameters().getInt("pastry_rtBaseBitLength"), environment);

      Iterator j = nodes.iterator();

      while (j.hasNext()) {
        rt.put((NodeHandle) j.next());
      }

      RouteSet[] ideal = (RouteSet[]) rt.getRow(row);
      RouteSet[] actual = (RouteSet[]) routerows.get(node);

      for (int k = 0; k < ideal.length; k++) {
        if (((actual[k] == null) || (actual[k].size() == 0)) && ((ideal[k] != null) && (ideal[k].size() > 0))) {
          System.out.println("ROUTING TABLE ERROR: " + node + " has no entry in row " + row + " column " + k + " but " + ideal[k].get(0) + " exists");
        }

        if (((actual[k] != null) && (actual[k].size() > 0)) && ((ideal[k] == null) || (ideal[k].size() == 0))) {
          System.out.println("ROUTING TABLE ERROR: " + node + " has no non-existent entry in row " + row + " column " + k + " entry " + actual[k].get(0) + " exists");
        }
      }
    }

    System.out.println("Done testing...");
  }

  /**
   * A unit test for JUnit
   *
   * @exception Exception DESCRIBE THE EXCEPTION
   */
  protected void testRoutingTables() throws Exception {
    testRouteRow(39);
    testRouteRow(38);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @exception Exception DESCRIBE THE EXCEPTION
   */
  public void start() throws Exception {
    testLeafSets();
    //testRoutingTables();
    System.exit(0);
  }

  /**
   * The main program for the PastryNetworkTest class
   *
   * @param args The command line arguments
   * @exception Exception DESCRIBE THE EXCEPTION
   */
  public static void main(String[] args) throws Exception {
//    PrintStream ps = new PrintStream(new FileOutputStream("lses.txt"));
//    System.setOut(ps);
//    System.setErr(ps);
    Environment env = new Environment();
    PastryNetworkTest test = new PastryNetworkTest(env, new SocketPastryNodeFactory(null, 1, env), new InetSocketAddress(args[0], Integer.parseInt(args[1])));
    test.start();
  }
}

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
 *  Created on Jul 27, 2004
 *
 *  To change the template for this generated file go to
 *  Window>Preferences>Java>Code Generation>Code and Comments
 */
package rice.selector.testing;

import java.io.IOException;

import rice.environment.Environment;
import rice.selector.SelectorManager;
import rice.selector.Timer;
import rice.selector.TimerTask;

/**
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh To change the template for this generated type comment go to
 *      Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SelectorTest {
  /**
   * DESCRIBE THE FIELD
   */
  public static boolean logAll = true;
  /**
   * DESCRIBE THE FIELD
   */
  public static boolean logIssues = true;
  /**
   * DESCRIBE THE FIELD
   */
  public static Environment environment;

  /**
   * The main program for the SelectorTest class
   *
   * @param args The command line arguments
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public static void main(String[] args) throws IOException {
    environment = new Environment();

    System.out.println("hello world <selector test>");
    SelectorManager sman = environment.getSelectorManager();
    Timer timer = sman.getTimer();
    scheduleRepeated(timer, sman);
    for (int i = 0; i < 10; i++) {
      scheduleStuff(timer, sman);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ie) {
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param timer DESCRIBE THE PARAMETER
   * @param sman DESCRIBE THE PARAMETER
   */
  public static void scheduleRepeated(Timer timer, SelectorManager sman) {
    final long t1Start = environment.getTimeSource().currentTimeMillis();
    final int t1Delay = 3000;
    timer.schedule(
      new TimerTask() {
        long lastTime = t1Start;

        public void run() {
          long curTime = environment.getTimeSource().currentTimeMillis();
          long delay = curTime - lastTime;
          lastTime = curTime;
          if ((logAll == true) || (delay - t1Delay) > 100) {
            System.out.println("Scheduled many times for delay " + t1Delay + " actual delay " + delay);
          }
        }
      }, t1Delay, t1Delay);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param timer DESCRIBE THE PARAMETER
   * @param sman DESCRIBE THE PARAMETER
   */
  public static void scheduleStuff(Timer timer, SelectorManager sman) {
    final long t1Start = environment.getTimeSource().currentTimeMillis();
    final int t1Delay = 5000;
    timer.schedule(
      new TimerTask() {
        public void run() {
          long curTime = environment.getTimeSource().currentTimeMillis();
          curTime -= t1Start;
          if ((logAll) || (logIssues && (curTime - t1Delay) > 100)) {
            System.out.println("Scheduled once for delay " + t1Delay + " actual delay " + curTime);
          }
        }
      }, t1Delay);
    //if (true) return;
    final long i1Start = environment.getTimeSource().currentTimeMillis();
    sman.invoke(
      new Runnable() {
        public void run() {
          long curTime = environment.getTimeSource().currentTimeMillis();
          curTime -= i1Start;
          if ((logAll) || (logIssues && (curTime > 100))) {
            System.out.println("invoked after " + curTime + " millis.");
          }
        }
      });
  }
}

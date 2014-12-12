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
 *  Created on May 6, 2004
 *
 *  To change the template for this generated file go to
 *  Window>Preferences>Java>Code Generation>Code and Comments
 */
package rice.pastry;

import rice.pastry.messaging.Message;
import rice.selector.Timer;
import rice.selector.TimerTask;

/**
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh To change the template for this generated type comment go to
 *      Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ExponentialBackoffScheduledMessage extends ScheduledMessage {
  boolean cancelled = false;
  EBTimerTask myTask;
  Timer timer;
  long initialPeriod;
  double expBase;
  int numTimes = 0;
  long lastTime = 0;

  /**
   * @param node
   * @param msg
   * @param initialPeriod
   * @param expBase
   * @param timer DESCRIBE THE PARAMETER
   * @param delay DESCRIBE THE PARAMETER
   */
  public ExponentialBackoffScheduledMessage(PastryNode node, Message msg, Timer timer, long delay, long initialPeriod, double expBase) {
    super(node, msg);
    this.timer = timer;
    this.initialPeriod = initialPeriod;
    this.expBase = expBase;
    schedule(delay);
  }

  /**
   * Constructor for ExponentialBackoffScheduledMessage.
   *
   * @param node DESCRIBE THE PARAMETER
   * @param msg DESCRIBE THE PARAMETER
   * @param timer DESCRIBE THE PARAMETER
   * @param initialDelay DESCRIBE THE PARAMETER
   * @param expBase DESCRIBE THE PARAMETER
   */
  public ExponentialBackoffScheduledMessage(PastryNode node, Message msg, Timer timer, long initialDelay, double expBase) {
    super(node, msg);
    this.timer = timer;
    this.initialPeriod = initialDelay;
    this.expBase = expBase;
    schedule(initialDelay);
    numTimes = 1;
  }


  /**
   * DESCRIBE THE METHOD
   *
   * @param time DESCRIBE THE PARAMETER
   */
  private void schedule(long time) {
    myTask = new EBTimerTask();
    timer.schedule(myTask, time);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean cancel() {
    super.cancel();
    if (myTask != null) {
      myTask.cancel();
      myTask = null;
    }
    boolean temp = cancelled;
    cancelled = true;
    return temp;
  }

  /**
   * Main processing method for the ExponentialBackoffScheduledMessage object
   */
  public void run() {
    if (!cancelled) {
      if (myTask != null) {
        lastTime = myTask.scheduledExecutionTime();
      }
      super.run();
      long time = (long) (initialPeriod * Math.pow(expBase, numTimes));
      schedule(time);
      numTimes++;
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public long scheduledExecutionTime() {
    return lastTime;
  }

  /**
   * DESCRIBE THE CLASS
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */
  class EBTimerTask extends TimerTask {
    /**
     * Main processing method for the EBTimerTask object
     */
    public void run() {
      ExponentialBackoffScheduledMessage.this.run();
    }
  }
}

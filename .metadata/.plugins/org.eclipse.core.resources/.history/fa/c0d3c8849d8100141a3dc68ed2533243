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
 *  Created on Nov 17, 2004
 */
package rice.selector;

import rice.environment.time.TimeSource;
import rice.p2p.commonapi.CancellableTask;

/**
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public abstract class TimerTask implements Comparable, CancellableTask {
  /**
   * DESCRIBE THE FIELD
   */
  protected long nextExecutionTime;
  /**
   * DESCRIBE THE FIELD
   */
  protected boolean cancelled = false;

  /**
   * If period is positive, task will be rescheduled.
   */
  protected int period = -1;

  /**
   * DESCRIBE THE FIELD
   */
  protected boolean fixedRate = false;

  /**
   * Gets the Cancelled attribute of the TimerTask object
   *
   * @return The Cancelled value
   */
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Main processing method for the TimerTask object
   */
  public abstract void run();

  /**
   * Returns true if should re-insert.
   *
   * @param ts DESCRIBE THE PARAMETER
   * @return
   */
  public boolean execute(TimeSource ts) {
    if (cancelled) {
      return false;
    }
    run();
    // often cancelled in the execution
    if (cancelled) {
      return false;
    }
    if (period > 0) {
      if (fixedRate) {
        nextExecutionTime += period;
        return true;
      } else {
        nextExecutionTime = ts.currentTimeMillis() + period;
        return true;
      }
    } else {
      return false;
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean cancel() {
    if (cancelled) {
      return false;
    }
    cancelled = true;
    return true;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public long scheduledExecutionTime() {
    return nextExecutionTime;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param arg0 DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public int compareTo(Object arg0) {
    TimerTask tt = (TimerTask) arg0;
    if (tt == this) {
      return 0;
    }
//    return (int)(tt.nextExecutionTime-nextExecutionTime);
    int diff = (int) (nextExecutionTime - tt.nextExecutionTime);
    if (diff == 0) {
      return System.identityHashCode(this) < System.identityHashCode(tt) ? 1 : -1;
    }
    return diff;
  }

}

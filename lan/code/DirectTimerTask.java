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
 *  Created on Nov 8, 2005
 */
package rice.pastry.direct;

import rice.pastry.ScheduledMessage;
import rice.selector.TimerTask;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class DirectTimerTask extends ScheduledMessage {

  MessageDelivery md;

  /**
   * Constructor for DirectTimerTask.
   *
   * @param md DESCRIBE THE PARAMETER
   * @param nextExecutionTime DESCRIBE THE PARAMETER
   * @param period DESCRIBE THE PARAMETER
   * @param fixed DESCRIBE THE PARAMETER
   */
  DirectTimerTask(MessageDelivery md, long nextExecutionTime, int period, boolean fixed) {
    super(md.node, md.msg);
    this.md = md;
    this.nextExecutionTime = nextExecutionTime;
    this.period = period;
    this.fixedRate = fixed;
  }

  /**
   * Constructor for DirectTimerTask.
   *
   * @param md DESCRIBE THE PARAMETER
   * @param nextExecutionTime DESCRIBE THE PARAMETER
   * @param period DESCRIBE THE PARAMETER
   */
  DirectTimerTask(MessageDelivery md, long nextExecutionTime, int period) {
    this(md, nextExecutionTime, -1, false);
  }

  /**
   * Constructor for DirectTimerTask.
   *
   * @param md DESCRIBE THE PARAMETER
   * @param nextExecutionTime DESCRIBE THE PARAMETER
   */
  DirectTimerTask(MessageDelivery md, long nextExecutionTime) {
    this(md, nextExecutionTime, -1, false);
  }

  /**
   * Main processing method for the DirectTimerTask object
   */
  public void run() {
    md.deliver();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "DirectTT for " + msg + " to " + md.node;
  }

}

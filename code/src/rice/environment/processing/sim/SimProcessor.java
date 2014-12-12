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
 *  Created on Feb 6, 2006
 */
package rice.environment.processing.sim;

import rice.*;
import rice.environment.logging.LogManager;
import rice.environment.processing.*;
import rice.environment.processing.simple.ProcessingRequest;
import rice.environment.time.TimeSource;
import rice.selector.SelectorManager;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class SimProcessor implements Processor {
  SelectorManager selector;

  /**
   * Constructor for SimProcessor.
   *
   * @param selector DESCRIBE THE PARAMETER
   */
  public SimProcessor(SelectorManager selector) {
    this.selector = selector;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param task DESCRIBE THE PARAMETER
   * @param command DESCRIBE THE PARAMETER
   * @param selector DESCRIBE THE PARAMETER
   * @param ts DESCRIBE THE PARAMETER
   * @param log DESCRIBE THE PARAMETER
   */
  public void process(Executable task, Continuation command,
                      SelectorManager selector, TimeSource ts, LogManager log) {
    selector.invoke(new ProcessingRequest(task, command, log, ts, selector));
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param request DESCRIBE THE PARAMETER
   */
  public void processBlockingIO(WorkRequest request) {
    selector.invoke(request);
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void destroy() {
    // TODO Auto-generated method stub

  }

}

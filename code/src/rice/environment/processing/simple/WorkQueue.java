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
 *  Created on Aug 16, 2005
 */
package rice.environment.processing.simple;

import java.util.*;

import rice.environment.processing.WorkRequest;

/**
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public class WorkQueue {
  List q = new LinkedList();
  /*
   *  A negative capacity, is equivalent to infinted capacity
   */
  int capacity = -1;

  boolean running = true;

  /**
   * Constructor for WorkQueue.
   */
  public WorkQueue() {
    /*
     *  do nothing
     */
  }

  /**
   * Constructor for WorkQueue.
   *
   * @param capacity DESCRIBE THE PARAMETER
   */
  public WorkQueue(int capacity) {
    this.capacity = capacity;
  }

  /**
   * Gets the Length attribute of the WorkQueue object
   *
   * @return The Length value
   */
  public synchronized int getLength() {
    return q.size();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param request DESCRIBE THE PARAMETER
   */
  public synchronized void enqueue(WorkRequest request) {
    if (capacity < 0 || q.size() < capacity) {
      q.add(request);
      notifyAll();
    } else {
      request.returnError(new WorkQueueOverflowException());
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public synchronized WorkRequest dequeue() {
    while (q.isEmpty() && running) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    if (!running) {
      return null;
    }
    return (WorkRequest) q.remove(0);
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void destroy() {
    running = false;
    synchronized (this) {
      notifyAll();
    }
  }

}
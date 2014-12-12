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
 *  Created on Apr 6, 2005
 */
package rice.environment.logging.simple;

import java.io.PrintStream;
import java.text.*;
import java.util.Date;

import javax.swing.text.DateFormatter;

import rice.environment.logging.*;
import rice.environment.time.TimeSource;

/**
 * This logger writes its name:time:message to the printstream provided, unless
 * the priority is lower than the minimumPriority.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public class SimpleLogger extends HeirarchyLogger {

  /**
   * The name of this logger.
   */
  String loggerName;

  /**
   * The stream to print to.
   */
  AbstractLogManager alm;

  /**
   * Constructor.
   *
   * @param loggerName the name of this logger.
   * @param alm DESCRIBE THE PARAMETER
   * @param level DESCRIBE THE PARAMETER
   * @param useDefault DESCRIBE THE PARAMETER
   */
  public SimpleLogger(String loggerName, AbstractLogManager alm, int level, boolean useDefault) {
    this.loggerName = loggerName;
    this.alm = alm;
    this.level = level;
    this.useDefault = useDefault;
  }

  /**
   * Prints out loggerName:currentTime:message
   *
   * @param message DESCRIBE THE PARAMETER
   */
  public void log(String message) {
    synchronized (alm) {
      String dateString = "" + alm.getTimeSource().currentTimeMillis();
      if (alm.dateFormatter != null) {
        try {
          Date date = new Date(alm.getTimeSource().currentTimeMillis());
          dateString = alm.dateFormatter.valueToString(date);
        } catch (ParseException pe) {
          pe.printStackTrace();
        }
      }

      alm.getPrintStream().println(alm.getPrefix() + ":" + loggerName + ":" + dateString + ":" + message);
    }
  }

  /**
   * Prints out logger:currentTime:exception.stackTrace();
   *
   * @param message DESCRIBE THE PARAMETER
   * @param exception DESCRIBE THE PARAMETER
   */
  public void logException(String message, Throwable exception) {
    synchronized (alm) {
      String dateString = "" + alm.getTimeSource().currentTimeMillis();
      if (alm.dateFormatter != null) {
        try {
          Date date = new Date(alm.getTimeSource().currentTimeMillis());
          dateString = alm.dateFormatter.valueToString(date);
        } catch (ParseException pe) {
          pe.printStackTrace();
        }
      }

      alm.getPrintStream().print(alm.getPrefix() + ":" + loggerName + ":" + dateString + ":" + message + " ");
      exception.printStackTrace(alm.getPrintStream());
    }
  }
}

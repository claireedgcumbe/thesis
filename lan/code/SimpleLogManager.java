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
import java.util.Hashtable;

import rice.environment.logging.*;
import rice.environment.logging.LogManager;
import rice.environment.params.ParameterChangeListener;
import rice.environment.params.Parameters;
import rice.environment.time.TimeSource;
import rice.environment.time.simple.SimpleTimeSource;

/**
 * This class creates loggers that log to a specified PrintStream System.out by
 * default.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public class SimpleLogManager extends AbstractLogManager implements CloneableLogManager {

  /**
   * Constructor.
   *
   * @param stream the stream to write to
   * @param timeSource the timesource to get times from
   * @param params DESCRIBE THE PARAMETER
   */
  public SimpleLogManager(PrintStream stream, TimeSource timeSource, Parameters params) {
    this(stream, timeSource, params, "", null);
  }

  /**
   * Constructor for SimpleLogManager.
   *
   * @param stream DESCRIBE THE PARAMETER
   * @param timeSource DESCRIBE THE PARAMETER
   * @param params DESCRIBE THE PARAMETER
   * @param prefix DESCRIBE THE PARAMETER
   * @param dateFormat DESCRIBE THE PARAMETER
   */
  public SimpleLogManager(PrintStream stream, TimeSource timeSource, Parameters params, String prefix, String dateFormat) {
    super(stream, timeSource, params, prefix, dateFormat);
  }


  /**
   * Convienience constructor. Defauts to System.out as the stream, and
   * SimpleTimeSource as the timesource.
   *
   * @param params DESCRIBE THE PARAMETER
   */
  public SimpleLogManager(Parameters params) {
    this(null, new SimpleTimeSource(), params);
  }

  /**
   * Convienience constructor. Defauts to SimpleTimeSource as the timesource.
   *
   * @param stream the stream to write to
   * @param params DESCRIBE THE PARAMETER
   */
  public SimpleLogManager(PrintStream stream, Parameters params) {
    this(stream, new SimpleTimeSource(), params);
  }

  /**
   * Convienience constructor. Defauts to System.out as the stream.
   *
   * @param timeSource the timesource to get times from
   * @param params DESCRIBE THE PARAMETER
   */
  public SimpleLogManager(TimeSource timeSource, Parameters params) {
    this(null, timeSource, params);
  }

  /**
   * Gets the PrintStream attribute of the SimpleLogManager object
   *
   * @return The PrintStream value
   */
  public PrintStream getPrintStream() {
    return ps;
  }

  /**
   * Gets the Parameters attribute of the SimpleLogManager object
   *
   * @return The Parameters value
   */
  public Parameters getParameters() {
    return params;
  }

  /**
   * Gets the TimeSource attribute of the SimpleLogManager object
   *
   * @return The TimeSource value
   */
  public TimeSource getTimeSource() {
    return time;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param clazz DESCRIBE THE PARAMETER
   * @param level DESCRIBE THE PARAMETER
   * @param useDefault DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  protected Logger constructLogger(String clazz, int level, boolean useDefault) {
    return new SimpleLogger(clazz, this, level, useDefault);
  }

  /*
   *  (non-Javadoc)
   *  @see rice.environment.logging.CloneableLogManager#clone(java.lang.String)
   */
  /**
   * DESCRIBE THE METHOD
   *
   * @param detail DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public LogManager clone(String detail) {
    return new SimpleLogManager(ps, time, params, detail, dateFormat);
  }

}

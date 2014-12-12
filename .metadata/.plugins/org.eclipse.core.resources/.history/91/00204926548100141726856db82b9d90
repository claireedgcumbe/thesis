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
package rice.environment;

import java.io.IOException;
import java.util.*;

import rice.Destructable;
import rice.environment.logging.*;
import rice.environment.logging.file.FileLogManager;
import rice.environment.logging.simple.SimpleLogManager;
import rice.environment.params.Parameters;
import rice.environment.params.simple.SimpleParameters;
import rice.environment.processing.Processor;
import rice.environment.processing.sim.SimProcessor;
import rice.environment.processing.simple.SimpleProcessor;
import rice.environment.random.RandomSource;
import rice.environment.random.simple.SimpleRandomSource;
import rice.environment.time.TimeSource;
import rice.environment.time.simple.SimpleTimeSource;
import rice.environment.time.simulated.DirectTimeSource;
import rice.selector.SelectorManager;

/**
 * Used to provide properties, timesource, loggers etc to the FreePastry apps
 * and components. XXX: Plan is to place the environment inside a PastryNode.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public class Environment implements Destructable {

  private SelectorManager selectorManager;
  private Processor processor;
  private RandomSource randomSource;
  private TimeSource time;
  private LogManager logManager;
  private Parameters params;
  private Logger logger;

  private HashSet destructables = new HashSet();
  /**
   * DESCRIBE THE FIELD
   */
  public final static String[] defaultParamFileArray = {"freepastry"};

  /**
   * Constructor. You can provide null values for all/any paramenters, which
   * will result in a default choice. If you want different defaults, consider
   * extending Environment and providing your own chooseDefaults() method.
   *
   * @param sm the SelectorManager. Default: rice.selector.SelectorManager
   * @param rs the RandomSource. Default:
   *      rice.environment.random.simple.SimpleRandomSource
   * @param time the TimeSource. Default:
   *      rice.environment.time.simple.SimpleTimeSource
   * @param lm the LogManager. Default: rice.environment.logging.simple.SimpleLogManager
   * @param proc DESCRIBE THE PARAMETER
   * @param params DESCRIBE THE PARAMETER
   */
  public Environment(SelectorManager sm, Processor proc, RandomSource rs, TimeSource time, LogManager lm, Parameters params) {
    this.selectorManager = sm;
    this.randomSource = rs;
    this.time = time;
    this.logManager = lm;
    this.params = params;
    this.processor = proc;

    if (params == null) {
      throw new IllegalArgumentException("params cannot be null");
    }

    // choose defaults for all non-specified parameters
    chooseDefaults();

    addDestructable(this.selectorManager);
    addDestructable(this.processor);

    logger = this.logManager.getLogger(getClass(), null);
  }

  /**
   * Convienience for defaults.
   *
   * @param paramFileName the file where parameters are saved
   * @param orderedDefaultFiles DESCRIBE THE PARAMETER
   * @throws IOException
   */
  public Environment(String[] orderedDefaultFiles, String paramFileName) {
    this(null, null, null, null, null, new SimpleParameters(orderedDefaultFiles, paramFileName));
  }

  /**
   * Constructor for Environment.
   *
   * @param paramFileName DESCRIBE THE PARAMETER
   */
  public Environment(String paramFileName) {
    this(defaultParamFileArray, paramFileName);
  }

  /**
   * Convienience for defaults. Has no parameter file to load/store.
   */
  public Environment() {
    this(null);
  }

  // Accessors
  /**
   * Gets the SelectorManager attribute of the Environment object
   *
   * @return The SelectorManager value
   */
  public SelectorManager getSelectorManager() {
    return selectorManager;
  }

  /**
   * Gets the Processor attribute of the Environment object
   *
   * @return The Processor value
   */
  public Processor getProcessor() {
    return processor;
  }

  /**
   * Gets the RandomSource attribute of the Environment object
   *
   * @return The RandomSource value
   */
  public RandomSource getRandomSource() {
    return randomSource;
  }

  /**
   * Gets the TimeSource attribute of the Environment object
   *
   * @return The TimeSource value
   */
  public TimeSource getTimeSource() {
    return time;
  }

  /**
   * Gets the LogManager attribute of the Environment object
   *
   * @return The LogManager value
   */
  public LogManager getLogManager() {
    return logManager;
  }

  /**
   * Gets the Parameters attribute of the Environment object
   *
   * @return The Parameters value
   */
  public Parameters getParameters() {
    return params;
  }

  /**
   * Can be easily overridden by a subclass.
   */
  protected void chooseDefaults() {
    // choose defaults for all non-specified parameters
//    if (params == null) {
//      params = new SimpleParameters("temp");
//    }
    if (time == null) {
      time = generateDefaultTimeSource();
    }
    if (logManager == null) {
      logManager = generateDefaultLogManager(time, params);
    }
    if (randomSource == null) {
      randomSource = generateDefaultRandomSource(params, logManager);
    }
    if (selectorManager == null) {
      selectorManager = generateDefaultSelectorManager(time, logManager);
    }
    if (processor == null) {
      processor = generateDefaultProcessor();
    }
  }

  /**
   * Tears down the environment. Calls params.store(),
   * selectorManager.destroy().
   */
  public void destroy() {
    try {
      params.store();
    } catch (IOException ioe) {
      if (logger.level <= Logger.WARNING) {
        logger.logException("Error during shutdown", ioe);
      }
    }
    Iterator i = destructables.iterator();
    while (i.hasNext()) {
      Destructable d = (Destructable) i.next();
      d.destroy();
    }
    selectorManager.destroy();
    processor.destroy();
  }

  /**
   * Adds a feature to the Destructable attribute of the Environment object
   *
   * @param destructable The feature to be added to the Destructable attribute
   */
  public void addDestructable(Destructable destructable) {
    destructables.add(destructable);

  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param destructable DESCRIBE THE PARAMETER
   */
  public void removeDestructable(Destructable destructable) {
    destructables.remove(destructable);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public static Environment directEnvironment() {
    Parameters params = new SimpleParameters(Environment.defaultParamFileArray, null);
    DirectTimeSource dts = new DirectTimeSource(params);
    LogManager lm = generateDefaultLogManager(dts, params);
    dts.setLogManager(lm);
    SelectorManager selector = generateDefaultSelectorManager(dts, lm);
    Processor proc = new SimProcessor(selector);
    Environment ret = new Environment(selector, proc, null, dts, lm,
      params);
    return ret;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param params DESCRIBE THE PARAMETER
   * @param logging DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public static RandomSource generateDefaultRandomSource(Parameters params, LogManager logging) {
    RandomSource randomSource;
    if (params.getString("random_seed").equalsIgnoreCase("clock")) {
      randomSource = new SimpleRandomSource(logging);
    } else {
      randomSource = new SimpleRandomSource(params.getLong("random_seed"), logging);
    }

    return randomSource;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public static TimeSource generateDefaultTimeSource() {
    return new SimpleTimeSource();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param time DESCRIBE THE PARAMETER
   * @param params DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public static LogManager generateDefaultLogManager(TimeSource time, Parameters params) {
    if (params.getBoolean("environment_logToFile")) {
      return new FileLogManager(time, params);
    }
    return new SimpleLogManager(time, params);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param time DESCRIBE THE PARAMETER
   * @param logging DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public static SelectorManager generateDefaultSelectorManager(TimeSource time, LogManager logging) {
    return new SelectorManager("Default", time, logging);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public static Processor generateDefaultProcessor() {
    return new SimpleProcessor("Default");
  }
}


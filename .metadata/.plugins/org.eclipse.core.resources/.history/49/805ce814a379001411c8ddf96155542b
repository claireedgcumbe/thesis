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
package rice.p2p.aggregation;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Date;
import java.util.Vector;
import java.io.*;

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;
import rice.p2p.glacier.VersionKey;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class AggregateList {

  /**
   * DESCRIBE THE FIELD
   */
  protected final Hashtable aggregateList;
  /**
   * DESCRIBE THE FIELD
   */
  protected final String configFileName;
  /**
   * DESCRIBE THE FIELD
   */
  protected final String logFileName;
  /**
   * DESCRIBE THE FIELD
   */
  protected final String label;
  /**
   * DESCRIBE THE FIELD
   */
  protected final IdFactory factory;
  /**
   * DESCRIBE THE FIELD
   */
  protected final PrintStream logFile;
  /**
   * DESCRIBE THE FIELD
   */
  protected final boolean loggingEnabled;
  /**
   * DESCRIBE THE FIELD
   */
  protected Id rootKey;
  /**
   * DESCRIBE THE FIELD
   */
  protected boolean wasReadOK;
  /**
   * DESCRIBE THE FIELD
   */
  protected long nextSerial;
  /**
   * DESCRIBE THE FIELD
   */
  protected String instance;
  /**
   * DESCRIBE THE FIELD
   */
  protected Environment environment;
  /**
   * DESCRIBE THE FIELD
   */
  protected Logger logger;

  /**
   * Constructor for AggregateList.
   *
   * @param configFileName DESCRIBE THE PARAMETER
   * @param label DESCRIBE THE PARAMETER
   * @param factory DESCRIBE THE PARAMETER
   * @param loggingEnabled DESCRIBE THE PARAMETER
   * @param instance DESCRIBE THE PARAMETER
   * @param env DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public AggregateList(String configFileName, String label, IdFactory factory, boolean loggingEnabled, String instance, Environment env) throws IOException {
    this.instance = instance;
    this.configFileName = configFileName;
    this.aggregateList = new Hashtable();
    this.factory = factory;
    this.label = label;
    this.rootKey = null;
    this.nextSerial = 0;
    this.environment = env;
    this.logger = environment.getLogManager().getLogger(AggregateList.class, instance);
    this.wasReadOK = readFromDisk();
    this.loggingEnabled = loggingEnabled;
    this.logFileName = configFileName + ".log";

    if (loggingEnabled) {
      recoverLog();
      this.logFile = new PrintStream(new FileOutputStream(logFileName, true));
    } else {
      this.logFile = null;
    }
  }

  /**
   * Gets the Root attribute of the AggregateList object
   *
   * @return The Root value
   */
  public Id getRoot() {
    return rootKey;
  }

  /**
   * Gets the Empty attribute of the AggregateList object
   *
   * @return The Empty value
   */
  public boolean isEmpty() {
    return aggregateList.isEmpty();
  }

  /**
   * Gets the ADC attribute of the AggregateList object
   *
   * @param id DESCRIBE THE PARAMETER
   * @return The ADC value
   */
  public AggregateDescriptor getADC(Id id) {
    return (AggregateDescriptor) aggregateList.get(id);
  }

  /**
   * Gets the SomePointers attribute of the AggregateList object
   *
   * @param referenceThreshold DESCRIBE THE PARAMETER
   * @param max DESCRIBE THE PARAMETER
   * @param excludes DESCRIBE THE PARAMETER
   * @return The SomePointers value
   */
  public Id[] getSomePointers(int referenceThreshold, int max, Id[] excludes) {
    if (rootKey == null) {
      return new Id[]{};
    }

    if (excludes != null) {
      recalculateReferenceCounts(excludes);
    }

    Vector pointers = new Vector();
    Enumeration enumeration = elements();

    resetMarkers();
    while (enumeration.hasMoreElements()) {
      AggregateDescriptor aggr = (AggregateDescriptor) enumeration.nextElement();
      if (!aggr.marker) {
        aggr.marker = true;

        boolean isExcluded = false;
        if (excludes != null) {
          for (int i = 0; i < excludes.length; i++) {
            if (excludes[i].equals(aggr.key)) {
              isExcluded = true;
            }
          }
        }

        if ((aggr.referenceCount < referenceThreshold) && (pointers.size() < max) && !isExcluded) {
          pointers.add(aggr.key);
        }
      }
    }

    return (Id[]) pointers.toArray(new Id[]{});
  }

  /**
   * Gets the Statistics attribute of the AggregateList object
   *
   * @param granularity DESCRIBE THE PARAMETER
   * @param range DESCRIBE THE PARAMETER
   * @param nominalReferenceCount DESCRIBE THE PARAMETER
   * @return The Statistics value
   */
  public AggregationStatistics getStatistics(long granularity, long range, int nominalReferenceCount) {
    final int maxHistoIndex = (int) (range / granularity);
    final long now = environment.getTimeSource().currentTimeMillis();
    AggregationStatistics stats = new AggregationStatistics(1 + maxHistoIndex, granularity, environment);
    Enumeration enumeration = elements();

    recalculateReferenceCounts(null);
    resetMarkers();

    while (enumeration.hasMoreElements()) {
      AggregateDescriptor aggr = (AggregateDescriptor) enumeration.nextElement();
      if (!aggr.marker) {
        aggr.marker = true;

        stats.numAggregatesTotal++;
        stats.numObjectsTotal += aggr.objects.length;
        if (aggr.objects.length == 0) {
          stats.numPointerArrays++;
        }
        if (aggr.referenceCount < nominalReferenceCount) {
          stats.criticalAggregates++;
        }
        if (aggr.referenceCount < 1) {
          stats.orphanedAggregates++;
        }

        int aggrPos = (int) ((aggr.currentLifetime - now) / granularity);
        if (aggrPos < 0) {
          aggrPos = 0;
        }
        if (aggrPos > maxHistoIndex) {
          aggrPos = maxHistoIndex;
        }
        stats.aggregateLifetimeHisto[aggrPos]++;

        for (int i = 0; i < aggr.objects.length; i++) {
          stats.totalObjectsSize += aggr.objects[i].size;
          if (aggr.objects[i].isAliveAt(now)) {
            stats.numObjectsAlive++;
            stats.liveObjectsSize += aggr.objects[i].size;
          }
          int objPos = (int) ((aggr.objects[i].refreshedLifetime - now) / granularity);
          if (objPos < 0) {
            objPos = 0;
          }
          if (objPos > maxHistoIndex) {
            objPos = maxHistoIndex;
          }
          stats.objectLifetimeHisto[objPos]++;
        }
      }
    }

    return stats;
  }

  /**
   * Gets the LogPrefix attribute of the AggregateList object
   *
   * @return The LogPrefix value
   */
  private String getLogPrefix() {
    return "COUNT: " + environment.getTimeSource().currentTimeMillis() + " AL";
  }

  /**
   * Sets the Root attribute of the AggregateList object
   *
   * @param root The new Root value
   */
  public void setRoot(Id root) {
    rootKey = root;
    logEntry("setRoot|" + ((root == null) ? "null" : root.toStringFull()));
  }

  /**
   * Sets the AggregateLifetime attribute of the AggregateList object
   *
   * @param adc The new AggregateLifetime value
   * @param lifetime The new AggregateLifetime value
   */
  public void setAggregateLifetime(AggregateDescriptor adc, long lifetime) {
    logEntry("setAL|" + adc.key.toStringFull() + "|" + lifetime);
    adc.currentLifetime = lifetime;
  }

  /**
   * Sets the ObjectCurrentLifetime attribute of the AggregateList object
   *
   * @param adc The new ObjectCurrentLifetime value
   * @param index The new ObjectCurrentLifetime value
   * @param lifetime The new ObjectCurrentLifetime value
   */
  public void setObjectCurrentLifetime(AggregateDescriptor adc, int index, long lifetime) {
    logEntry("setOCL|" + adc.key.toStringFull() + "|" + index + "|" + lifetime);
    adc.objects[index].currentLifetime = lifetime;
  }

  /**
   * Sets the ObjectRefreshedLifetime attribute of the AggregateList object
   *
   * @param adc The new ObjectRefreshedLifetime value
   * @param index The new ObjectRefreshedLifetime value
   * @param lifetime The new ObjectRefreshedLifetime value
   */
  public void setObjectRefreshedLifetime(AggregateDescriptor adc, int index, long lifetime) {
    logEntry("setORL|" + adc.key.toStringFull() + "|" + index + "|" + lifetime);
    adc.objects[index].refreshedLifetime = lifetime;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean readOK() {
    return wasReadOK;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public Enumeration elements() {
    return aggregateList.elements();
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void recoverLog() {
    BufferedReader logFile = null;
    long expectedSerial = -1;
    int entriesReplayed = 0;

    try {
      logFile = new BufferedReader(new FileReader(logFileName));

      while (true) {
        String line = readLineSkipComments(logFile);
        if (line == null) {
          logFile.close();
          break;
        }

        if (!line.startsWith("$") || !line.endsWith("@")) {
          throw new AggregationException("Malformed log entry (expected $...@): " + line);
        }

        if ((line.lastIndexOf('$') != 0) || (line.indexOf('@') != line.length() - 1)) {
          throw new AggregationException("Overlapping log entries: " + line);
        }

        String[] parts = line.split("\\|");

        long thisSerial = Long.parseLong(parts[1]);
        if ((expectedSerial >= 0) && (thisSerial != expectedSerial)) {
          throw new AggregationException("Malformed log entry (expected serial #" + expectedSerial + "): " + line);
        }

        expectedSerial = thisSerial + 1;

        if (thisSerial > nextSerial) {
          throw new AggregationException("Entries " + nextSerial + ".." + (thisSerial - 1) + " missing from log... cannot recover!");
        }

        if (thisSerial == nextSerial) {
          if (logger.level <= Logger.FINE) {
            logger.log(
              "Replaying log entry #" + thisSerial);
          }
          entriesReplayed++;

          if (parts[3].equals("setRoot")) {
            if (parts[4].equals("null")) {
              rootKey = null;
              if (logger.level <= Logger.FINER) {
                logger.log(
                  "  - rootKey = null");
              }
            } else {
              rootKey = factory.buildIdFromToString(parts[4]);
              if (logger.level <= Logger.FINE) {
                logger.log(
                  "  - rootKey = " + rootKey.toStringFull());
              }
            }

          } else if (parts[3].equals("setAL")) {
            Id adcKey = factory.buildIdFromToString(parts[4]);
            long lifetime = Long.parseLong(parts[5]);
            AggregateDescriptor adc = (AggregateDescriptor) aggregateList.get(adcKey);
            if (adc == null) {
              throw new AggregationException("Cannot find aggregate (" + adcKey.toStringFull() + ": " + line);
            }
            adc.currentLifetime = lifetime;
            if (logger.level <= Logger.FINER) {
              logger.log(
                "  - lifetime=" + lifetime + " in ADC " + adcKey.toStringFull());
            }
          } else if (parts[3].equals("setOCL")) {
            Id adcKey = factory.buildIdFromToString(parts[4]);
            int index = Integer.parseInt(parts[5]);
            long lifetime = Long.parseLong(parts[6]);
            AggregateDescriptor adc = (AggregateDescriptor) aggregateList.get(adcKey);
            if (adc == null) {
              throw new AggregationException("Cannot find aggregate (" + adcKey.toStringFull() + ": " + line);
            }
            if (adc.objects.length <= index) {
              throw new AggregationException("Object index mismatch (" + index + "/" + adc.objects.length + "): " + line);
            }
            adc.objects[index].currentLifetime = lifetime;
            if (logger.level <= Logger.FINER) {
              logger.log(
                "  - currentLifetime=" + lifetime + " in ADC " + adcKey.toStringFull() + " index " + index);
            }
          } else if (parts[3].equals("setORL")) {
            Id adcKey = factory.buildIdFromToString(parts[4]);
            int index = Integer.parseInt(parts[5]);
            long lifetime = Long.parseLong(parts[6]);
            AggregateDescriptor adc = (AggregateDescriptor) aggregateList.get(adcKey);
            if (adc == null) {
              throw new AggregationException("Cannot find aggregate (" + adcKey.toStringFull() + ": " + line);
            }
            if (adc.objects.length <= index) {
              throw new AggregationException("Object index mismatch (" + index + "/" + adc.objects.length + "): " + line);
            }
            adc.objects[index].refreshedLifetime = lifetime;
            if (logger.level <= Logger.FINER) {
              logger.log(
                "  - refreshedLifetime=" + lifetime + " in ADC " + adcKey.toStringFull() + " index " + index);
            }
          } else if (parts[3].equals("refresh")) {
            Id adcKey = factory.buildIdFromToString(parts[4]);
            long lifetime = Long.parseLong(parts[5]);
            AggregateDescriptor adc = (AggregateDescriptor) aggregateList.get(adcKey);
            if (adc == null) {
              throw new AggregationException("Cannot find aggregate (" + adcKey.toStringFull() + ": " + line);
            }

            adc.currentLifetime = lifetime;
            for (int i = 0; i < adc.objects.length; i++) {
              adc.objects[i].currentLifetime = adc.objects[i].refreshedLifetime;
            }
            if (logger.level <= Logger.FINER) {
              logger.log(
                " - refresh=" + lifetime + " in ADC " + adcKey.toStringFull());
            }
          } else if (parts[3].equals("removeAggregate")) {
            Id adcKey = factory.buildIdFromToString(parts[4]);
            AggregateDescriptor adc = (AggregateDescriptor) aggregateList.get(adcKey);
            if (adc == null) {
              throw new AggregationException("Cannot find aggregate (" + adcKey.toStringFull() + ": " + line);
            }

            removeAggregateDescriptor(adc, false);
            if (logger.level <= Logger.FINER) {
              logger.log(
                " - remove ADC " + adcKey.toStringFull());
            }
          } else if (parts[3].equals("addAggregate")) {
            Id adcKey = factory.buildIdFromToString(parts[4]);
            AggregateDescriptor adc = (AggregateDescriptor) aggregateList.get(adcKey);
            if (adc != null) {
              throw new AggregationException("Aggregate already present (" + adcKey.toStringFull() + ": " + line);
            }

            adc = readAggregate(logFile, adcKey);
            addAggregateDescriptor(adc, false);
            if (logger.level <= Logger.FINER) {
              logger.log(
                " - add ADC " + adcKey.toStringFull());
            }
          } else {
            throw new AggregationException("Unknown command (" + parts[3] + "): " + line);
          }

          nextSerial++;
        } else {
          /*
           *  skip addAggregate if necessary
           */
          if (parts[3].equals("addAggregate")) {
            readAggregate(logFile, factory.buildIdFromToString(parts[4]));
          }
        }
      }

    } catch (FileNotFoundException fnfe) {
      if (logger.level <= Logger.WARNING) {
        logger.log(
          "No aggregate log found; using configuration file only");
      }
    } catch (Exception e) {
      if (logger.level <= Logger.WARNING) {
        logger.logException(
          "Exception while recovering aggregate log: ", e);
      }
      System.exit(1);
    }

    if (entriesReplayed > 0) {
      writeToDisk();
      if (logger.level <= Logger.WARNING) {
        logger.log(
          entriesReplayed + " entries replayed from aggregate log");
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param entry DESCRIBE THE PARAMETER
   */
  public void logEntry(String entry) {
    if (loggingEnabled) {
      if (logFile != null) {
        logFile.println("$|" + (nextSerial++) + "|" + environment.getTimeSource().currentTimeMillis() + "|" + entry + "|@");
        logFile.flush();
      } else {
        if (logger.level <= Logger.WARNING) {
          logger.log(
            "Aggregation cannot write to log: " + entry);
        }
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void clear() {
    aggregateList.clear();
    rootKey = null;
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void resetMarkers() {
    Enumeration enumerationeration = aggregateList.elements();
    while (enumerationeration.hasMoreElements()) {
      AggregateDescriptor aggr = (AggregateDescriptor) enumerationeration.nextElement();
      aggr.marker = false;
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param adc DESCRIBE THE PARAMETER
   * @param lifetime DESCRIBE THE PARAMETER
   */
  public void refreshAggregate(AggregateDescriptor adc, long lifetime) {
    logEntry("refresh|" + adc.key.toStringFull() + "|" + lifetime);
    adc.currentLifetime = lifetime;
    for (int i = 0; i < adc.objects.length; i++) {
      adc.objects[i].currentLifetime = adc.objects[i].refreshedLifetime;
    }
  }

  /**
   * Adds a feature to the AggregateDescriptor attribute of the AggregateList
   * object
   *
   * @param aggr The feature to be added to the AggregateDescriptor attribute
   */
  public void addAggregateDescriptor(AggregateDescriptor aggr) {
    addAggregateDescriptor(aggr, true);
  }

  /**
   * Adds a feature to the AggregateDescriptor attribute of the AggregateList
   * object
   *
   * @param aggr The feature to be added to the AggregateDescriptor attribute
   * @param logThis The feature to be added to the AggregateDescriptor attribute
   */
  private void addAggregateDescriptor(AggregateDescriptor aggr, boolean logThis) {
    if ((logFile != null) && logThis && loggingEnabled) {
      logEntry("addAggregate|" + aggr.key.toStringFull());
      writeAggregate(logFile, aggr);
      logFile.flush();
    }

    aggregateList.put(aggr.key, aggr);

    for (int i = 0; i < aggr.objects.length; i++) {
      aggregateList.put(new VersionKey(aggr.objects[i].key, aggr.objects[i].version), aggr);
      AggregateDescriptor prevDesc = (AggregateDescriptor) aggregateList.get(aggr.objects[i].key);
      int objDescIndex = (prevDesc == null) ? -1 : prevDesc.lookupNewest(aggr.objects[i].key);
      if ((objDescIndex < 0) || (prevDesc.objects[objDescIndex].version <= aggr.objects[i].version)) {
        aggregateList.put(aggr.objects[i].key, aggr);
      }
    }

    for (int i = 0; i < aggr.pointers.length; i++) {
      AggregateDescriptor ref = (AggregateDescriptor) aggregateList.get(aggr.pointers[i]);
      if (ref != null) {
        ref.addReference();
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param aggr DESCRIBE THE PARAMETER
   */
  public void removeAggregateDescriptor(AggregateDescriptor aggr) {
    removeAggregateDescriptor(aggr, true);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param aggr DESCRIBE THE PARAMETER
   * @param logThis DESCRIBE THE PARAMETER
   */
  private void removeAggregateDescriptor(AggregateDescriptor aggr, boolean logThis) {
    if (logThis) {
      logEntry("removeAggregate|" + aggr.key.toStringFull());
    }

    aggregateList.remove(aggr.key);

    for (int i = 0; i < aggr.objects.length; i++) {
      VersionKey vkey = new VersionKey(aggr.objects[i].key, aggr.objects[i].version);
      AggregateDescriptor prevDesc1 = (AggregateDescriptor) aggregateList.get(vkey);
      if ((prevDesc1 != null) && prevDesc1.key.equals(aggr.key)) {
        aggregateList.remove(vkey);
      }
      AggregateDescriptor prevDesc2 = (AggregateDescriptor) aggregateList.get(aggr.objects[i].key);
      if ((prevDesc2 != null) && prevDesc2.key.equals(aggr.key)) {
        aggregateList.remove(aggr.objects[i].key);
      }
    }

    if (aggregateList.containsValue(aggr)) {
      if (logger.level <= Logger.WARNING) {
        logger.log(
          "Removal from aggregate list incomplete: " + aggr.key.toStringFull());
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param excludes DESCRIBE THE PARAMETER
   */
  public void recalculateReferenceCounts(Id[] excludes) {
    Enumeration enumeration = aggregateList.elements();
    while (enumeration.hasMoreElements()) {
      AggregateDescriptor aggr = (AggregateDescriptor) enumeration.nextElement();
      aggr.referenceCount = 0;
      aggr.marker = false;

      /*
       *  References from excluded aggregates don't count
       */
      if (excludes != null) {
        for (int i = 0; i < excludes.length; i++) {
          if (excludes[i].equals(aggr.key)) {
            aggr.marker = true;
          }
        }
      }
    }

    enumeration = aggregateList.elements();
    while (enumeration.hasMoreElements()) {
      AggregateDescriptor aggr = (AggregateDescriptor) enumeration.nextElement();
      if (!aggr.marker) {
        aggr.marker = true;
        for (int i = 0; i < aggr.pointers.length; i++) {
          AggregateDescriptor ref = (AggregateDescriptor) aggregateList.get(aggr.pointers[i]);
          if (ref != null) {
            ref.addReference();
          }
        }
      }
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param br DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  private String readLineSkipComments(BufferedReader br) throws IOException {
    while (true) {
      String line = br.readLine();
      if ((line != null) && ((line.length() == 0) || (line.charAt(0) == '#'))) {
        continue;
      }
      return line;
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param reader DESCRIBE THE PARAMETER
   * @param aggrKey DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   * @exception AggregationException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  private AggregateDescriptor readAggregate(BufferedReader reader, Id aggrKey) throws AggregationException, IOException {
    String[] expiresS = readLineSkipComments(reader).split("=");
    if (!expiresS[0].equals("expires")) {
      throw new AggregationException("Cannot find expiration date: " + expiresS[0]);
    }
    long expires = Long.parseLong(expiresS[1]);

    String[] objectNumS = readLineSkipComments(reader).split("=");
    if (!objectNumS[0].equals("objects")) {
      throw new AggregationException("Cannot find number of objects: " + objectNumS[0]);
    }
    int numObjects = Integer.parseInt(objectNumS[1]);

    ObjectDescriptor[] objects = new ObjectDescriptor[numObjects];
    for (int i = 0; i < numObjects; i++) {
      String[] objS = readLineSkipComments(reader).split("=");
      String[] objArgS = objS[1].split(";");
      String[] objIdS = objArgS[0].split("v");
      objects[i] = new ObjectDescriptor(
        factory.buildIdFromToString(objIdS[0]),
        Long.parseLong(objIdS[1]),
        Long.parseLong(objArgS[1]),
        Long.parseLong(objArgS[2]),
        Integer.parseInt(objArgS[3])
        );
    }

    String[] pointerNumS = readLineSkipComments(reader).split("=");
    if (!pointerNumS[0].equals("pointers")) {
      throw new AggregationException("Cannot find number of pointers: " + pointerNumS[0]);
    }
    int numPointers = Integer.parseInt(pointerNumS[1]);

    Id[] pointers = new Id[numPointers];
    for (int i = 0; i < numPointers; i++) {
      String[] ptrS = readLineSkipComments(reader).split("=");
      pointers[i] = factory.buildIdFromToString(ptrS[1]);
    }

    return new AggregateDescriptor(aggrKey, expires, objects, pointers);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean readFromDisk() {
    rootKey = null;
    aggregateList.clear();

    String fileName;
    if ((new File(configFileName)).exists()) {
      fileName = configFileName;
    } else if ((new File(configFileName + ".new")).exists()) {
      fileName = configFileName + ".new";
    } else {
      return false;
    }

    BufferedReader configFile = null;
    boolean readSuccessful = false;

    try {
      configFile = new BufferedReader(new FileReader(fileName));

      String[] root = readLineSkipComments(configFile).split("=");
      if (root[0].equals("nextid")) {
        nextSerial = Long.parseLong(root[1]);
        root = readLineSkipComments(configFile).split("=");
      }
      if (!root[0].equals("root")) {
        throw new AggregationException("Cannot read root key: " + root[0]);
      }
      rootKey = factory.buildIdFromToString(root[1]);

      while (true) {
        String aggrKeyLine = readLineSkipComments(configFile);
        if (aggrKeyLine == null) {
          readSuccessful = true;
          break;
        }

        String[] aggrKeyS = aggrKeyLine.split("\\[|\\]");
        Id aggrKey = factory.buildIdFromToString(aggrKeyS[1]);
        AggregateDescriptor adc = readAggregate(configFile, aggrKey);

        addAggregateDescriptor(adc, false);
      }
    } catch (Exception e) {
      if (logger.level <= Logger.WARNING) {
        logger.logException(
          "Cannot read configuration file: " + configFileName + " (e=" + e + ")", e);
      }
    }

    if (configFile != null) {
      try {
        configFile.close();
      } catch (Exception e) {
      }
    }

    if (!readSuccessful) {
      rootKey = null;
      aggregateList.clear();
    }

    return readSuccessful;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param stream DESCRIBE THE PARAMETER
   * @param adc DESCRIBE THE PARAMETER
   */
  private void writeAggregate(PrintStream stream, AggregateDescriptor adc) {
    stream.println("expires=" + adc.currentLifetime);
    stream.println("objects=" + adc.objects.length);
    for (int i = 0; i < adc.objects.length; i++) {
      stream.println("obj" + i + "=" +
        adc.objects[i].key.toStringFull() + "v" +
        adc.objects[i].version + ";" +
        adc.objects[i].currentLifetime + ";" +
        adc.objects[i].refreshedLifetime + ";" +
        adc.objects[i].size
        );
    }
    stream.println("pointers=" + adc.pointers.length);
    for (int i = 0; i < adc.pointers.length; i++) {
      stream.println("ptr" + i + "=" + adc.pointers[i].toStringFull());
    }
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void writeToDisk() {
    if (rootKey == null) {
      return;
    }

    try {
      PrintStream configFile = new PrintStream(new FileOutputStream(configFileName + ".new"));
      Enumeration enumeration = aggregateList.elements();

      resetMarkers();
      configFile.println("# Aggregate list at " + label + " (" + (new Date()) + ")");
      configFile.println();
      configFile.println("nextid=" + nextSerial);
      configFile.println("root=" + rootKey.toStringFull());
      configFile.println();

      while (enumeration.hasMoreElements()) {
        AggregateDescriptor aggr = (AggregateDescriptor) enumeration.nextElement();
        if (!aggr.marker) {
          configFile.println("[" + aggr.key.toStringFull() + "]");
          writeAggregate(configFile, aggr);
          configFile.println("");

          aggr.marker = true;
        }
      }

      configFile.close();
      (new File(configFileName)).delete();
      (new File(configFileName + ".new")).renameTo(new File(configFileName));
    } catch (IOException ioe) {
      if (logger.level <= Logger.WARNING) {
        logger.logException(
          "AggregationImpl cannot write to its aggregate list: " + configFileName + " (" + ioe + ")", ioe);
      }
    }
  }
}

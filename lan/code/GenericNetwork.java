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
package rice.pastry.direct;

import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.environment.params.Parameters;
import rice.environment.random.RandomSource;
import rice.environment.random.simple.SimpleRandomSource;
import rice.pastry.*;
import rice.pastry.messaging.*;
import rice.pastry.routing.*;
// import rice.pastry.mytesting.*;
// import rice.p2p.lala.testing.Tracker;

import java.util.*;
import java.lang.*;
import java.io.*;

// This topology will read in a topology-distance matrix and the corresponding
// coordinates from input files
/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class GenericNetwork extends BasicNetworkSimulator {
  // This contains the total number of nodes assigned so far
  private int countIndex = 0;

  // This stores the matrix
  private int distance[][];

  // This stores the coordinates
  /**
   * DESCRIBE THE FIELD
   */
  public Hashtable nodePos = new Hashtable();

  // This keeps track of the indices that have already been assigned
  /**
   * DESCRIBE THE FIELD
   */
  public Vector assignedIndices = new Vector();

  /**
   * DESCRIBE THE FIELD
   */
  public String inFile_Matrix = "GNPINPUT";

  /**
   * DESCRIBE THE FIELD
   */
  public String inFile_Coord = "COORD";

  /**
   * DESCRIBE THE FIELD
   */
  public String outFile_RawGNPError = "RawGNPError";

//  private Vector transit = new Vector();

  /**
   * DESCRIBE THE FIELD
   */
  public static int MAXOVERLAYSIZE = 1000;

  // The static variable MAXOVERLAYSIZE should be set to the n, where its input
  // is a N*N matrix
  /**
   * Constructor for GenericNetwork.
   *
   * @param env DESCRIBE THE PARAMETER
   * @param inFile DESCRIBE THE PARAMETER
   */
  public GenericNetwork(Environment env, String inFile) {
    super(env);

    MAXOVERLAYSIZE = env.getParameters().getInt("pastry_direct_gtitm_max_overlay_size");

    inFile_Matrix = inFile;
    if (inFile_Matrix == null) {
      inFile_Matrix = env.getParameters().getString("pastry_direct_gtitm_matrix_file");
    }

//    System.out.println("TOPOLOGY : Generic toplogy");
    // rng = new Random(PastrySeed.getSeed());
    readOverlayMatrix();
    // readOverlayPos();
    // computeRawGNPError();
    // System.exit(1);

  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public NodeRecord generateNodeRecord() {
    return new GNNodeRecord();
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void readOverlayMatrix() {
    FileReader fr = null;
    try {
      fr = new FileReader(inFile_Matrix);
    } catch (Exception e) {
      System.out.println("ERROR: The required inter-host distance matrix for Generic Network not found");
      System.exit(1);
    }
    BufferedReader in = new BufferedReader(fr);

    int lineCount = 0;
    String line = null;
    try {
      while ((line = in.readLine()) != null) {
        String[] words;
        words = line.split("[ \t]+");
        if (distance == null) {
          MAXOVERLAYSIZE = words.length;
          distance = new int[MAXOVERLAYSIZE][MAXOVERLAYSIZE];
        }
        int nodeCount = 0;
//        for (int i = 0; i < words.length; i++) {
        for (int i = 0; i < MAXOVERLAYSIZE; i++) {
          if (words[i].length() > 0) {
//            if ((nodeCount >= MAXOVERLAYSIZE) || (lineCount >= MAXOVERLAYSIZE)) {
//              System.out
//                  .println("ERROR: the matrix has more entries than MAXOVERLAYSIZE which is a static variable set in main()");
//              System.exit(1);
//            }
            distance[lineCount][nodeCount] = (int) Float.parseFloat(words[i]);
            nodeCount++;

          }
        }
        lineCount++;
      }
      System.out.println("Size of Generic Network matrix= " + lineCount);
    } catch (IOException e) {
      System.out.println("Exception" + e);
    }
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void readOverlayPos() {
    BufferedReader fin = null;
    try {
      fin = new BufferedReader(new FileReader(inFile_Coord));
      String line;
      line = fin.readLine();
      while (line != null) {
        String[] words;
        words = line.split("[ \t]+");
        // System.out.println("words.length= " + words.length);
        // for(int i=0; i< words.length; i++) {
        // System.out.println("word[" + i + "]= " + words[i]);
        // }
        if (words[0].equals("Done")) {
          // This means that we are done
          break;
        }
        if (words[0].equals("##index=")) {
          int index;
          double pos[] = new double[Coordinate.GNPDIMENSIONS];
          index = Integer.parseInt(words[1]);
          for (int i = 0; i < Coordinate.GNPDIMENSIONS; i++) {
            pos[i] = Double.parseDouble(words[2 + i]);
          }
          Coordinate state = new Coordinate(index, pos);
          nodePos.put(new Integer(index), state);
          // System.out.println("inputfile coord[" + index + "]= " + state);
        }
        line = fin.readLine();
      }
      fin.close();

    } catch (IOException e) {
      System.out.println("ERROR: In opening input/output files");
    }

  }

  // This evaluates the GNP error (predicted - actual)/min(predicted,actual)
  // function
  /**
   * DESCRIBE THE METHOD
   */
  public void computeRawGNPError() {
    BufferedWriter fout = null;
    try {
      String s = "";
      fout = new BufferedWriter(new FileWriter(outFile_RawGNPError));

      for (int i = 0; i < MAXOVERLAYSIZE; i++) {
        for (int j = 0; j < MAXOVERLAYSIZE; j++) {
          double actual = (double) distance[i][j];
          Coordinate state_i = (Coordinate) nodePos.get(new Integer(i));
          Coordinate state_j = (Coordinate) nodePos.get(new Integer(j));
          double predicted = state_i.distance(state_j);
          double min;
          if (actual != -1) {
            if (actual < predicted) {
              min = actual;
            } else {
              min = predicted;
            }
            if (min > 0) {
              double gnpError = 0;
              gnpError = (Math.abs(predicted - actual)) / min;
              s = "" + gnpError;
              fout.write(s, 0, s.length());
              fout.newLine();
              fout.flush();
            }
          }
        }
      }

    } catch (IOException e) {
      System.out.println("ERROR: In opening input/output files");
    }
  }

  /**
   * DESCRIBE THE CLASS
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */
  private class GNNodeRecord implements NodeRecord {
    /**
     * DESCRIBE THE FIELD
     */
    public boolean alive;

    /**
     * DESCRIBE THE FIELD
     */
    public int index;
    // index in the symmetric inter-host latency matrix

    /**
     * Constructor for GNNodeRecord.
     */
    public GNNodeRecord() {
      if (countIndex >= MAXOVERLAYSIZE) {
        throw new RuntimeException("No more nodes int he network.");
      }
      alive = true;
      // index = countIndex++;
      index = random.nextInt(MAXOVERLAYSIZE);
      while (assignedIndices.contains(new Integer(index))) {
        index = random.nextInt(MAXOVERLAYSIZE);
      }
      // System.out.println("index= " + index);
      assignedIndices.add(new Integer(index));
      countIndex++;
    }

    // this return the index in the matrix/CoordinateArray that this NodeRecord
    // cooresponds to
    /**
     * Gets the Index attribute of the GNNodeRecord object
     *
     * @return The Index value
     */
    public int getIndex() {
      return index;
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param that DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public int proximity(NodeRecord that) {
      GNNodeRecord nr = (GNNodeRecord) that;
      int res = distance[index][nr.index];
      if (res < 0) {
        return Integer.MAX_VALUE;
      } else {
        return res;
      }
    }
  }

  /**
   * DESCRIBE THE CLASS
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */
  public static class Coordinate implements Serializable {
    /**
     * DESCRIBE THE FIELD
     */
    public int index;
    /**
     * DESCRIBE THE FIELD
     */
    public double[] pos = new double[GNPDIMENSIONS];

    /**
     * DESCRIBE THE FIELD
     */
    public static int GNPDIMENSIONS = 8;

    /**
     * Constructor for Coordinate.
     *
     * @param _index DESCRIBE THE PARAMETER
     * @param arr DESCRIBE THE PARAMETER
     */
    public Coordinate(int _index, double[] arr) {
      for (int i = 0; i < GNPDIMENSIONS; i++) {
        pos[i] = arr[i];
      }
    }


    /**
     * DESCRIBE THE METHOD
     *
     * @param o DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public double distance(Coordinate o) {
      double dpos[] = new double[GNPDIMENSIONS];
      double sumDist = 0;
      for (int i = 0; i < GNPDIMENSIONS; i++) {
        dpos[i] = Math.abs(o.pos[i] - pos[i]);
        sumDist = sumDist + (dpos[i] * dpos[i]);
      }
      double dist = Math.sqrt(sumDist);
      return dist;
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @return DESCRIBE THE RETURN VALUE
     */
    public String toString() {
      String s = "(";
      for (int i = 0; i < GNPDIMENSIONS; i++) {
        s = s + pos[i] + ",";
      }
      s = s + ")";
      return s;
    }

  }
}

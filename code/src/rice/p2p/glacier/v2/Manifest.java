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
package rice.p2p.glacier.v2;

import java.security.*;
import java.io.*;

import rice.environment.logging.Logger;
import rice.p2p.glacier.Fragment;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class Manifest implements Serializable {

  /**
   * DESCRIBE THE FIELD
   */
  protected transient byte[] objectHash;
  /**
   * DESCRIBE THE FIELD
   */
  protected transient byte[][] fragmentHash;
  /**
   * DESCRIBE THE FIELD
   */
  protected transient byte[] signature;
  /**
   * DESCRIBE THE FIELD
   */
  protected long expirationDate;

  final static long serialVersionUID = -436805143199825662L;

  /**
   * Constructor for Manifest.
   *
   * @param objectHash DESCRIBE THE PARAMETER
   * @param fragmentHash DESCRIBE THE PARAMETER
   * @param expirationDate DESCRIBE THE PARAMETER
   */
  public Manifest(byte[] objectHash, byte[][] fragmentHash, long expirationDate) {
    this.objectHash = objectHash;
    this.fragmentHash = fragmentHash;
    this.expirationDate = expirationDate;
    this.signature = null;
  }

  /**
   * Gets the ObjectHash attribute of the Manifest object
   *
   * @return The ObjectHash value
   */
  public byte[] getObjectHash() {
    return objectHash;
  }

  /**
   * Gets the FragmentHash attribute of the Manifest object
   *
   * @param fragmentID DESCRIBE THE PARAMETER
   * @return The FragmentHash value
   */
  public byte[] getFragmentHash(int fragmentID) {
    return fragmentHash[fragmentID];
  }

  /**
   * Gets the FragmentHashes attribute of the Manifest object
   *
   * @return The FragmentHashes value
   */
  public byte[][] getFragmentHashes() {
    return fragmentHash;
  }

  /**
   * Gets the Signature attribute of the Manifest object
   *
   * @return The Signature value
   */
  public byte[] getSignature() {
    return signature;
  }

  /**
   * Gets the Expiration attribute of the Manifest object
   *
   * @return The Expiration value
   */
  public long getExpiration() {
    return expirationDate;
  }

  /**
   * Sets the Signature attribute of the Manifest object
   *
   * @param signature The new Signature value
   */
  public void setSignature(byte[] signature) {
    this.signature = signature;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param newExpirationDate DESCRIBE THE PARAMETER
   * @param newSignature DESCRIBE THE PARAMETER
   */
  public void update(long newExpirationDate, byte[] newSignature) {
    expirationDate = newExpirationDate;
    signature = newSignature;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param fragment DESCRIBE THE PARAMETER
   * @param fragmentID DESCRIBE THE PARAMETER
   * @param logger DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean validatesFragment(Fragment fragment, int fragmentID, Logger logger) {
    if ((fragmentID < 0) || (fragmentID >= fragmentHash.length)) {
      return false;
    }

    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException e) {
      if (logger.level <= Logger.SEVERE) {
        logger.log("*** SHA-1 not supported ***" + toStringFull());
      }
      return false;
    }

    md.reset();
    md.update(fragment.getPayload());

    byte[] thisHash = md.digest();

    if (thisHash.length != fragmentHash[fragmentID].length) {
      if (logger.level <= Logger.WARNING) {
        logger.log("*** LENGTH MISMATCH: " + thisHash.length + " != " + fragmentHash[fragmentID].length + " ***" + toStringFull());
      }
      return false;
    }

    for (int i = 0; i < thisHash.length; i++) {
      if (thisHash[i] != fragmentHash[fragmentID][i]) {
        String s = "*** HASH MISMATCH: POS#" + i + ", " + thisHash[i] + " != " + fragmentHash[fragmentID][i] + " ***\n";
        s += "Hash: ";
        for (int j = 0; j < thisHash.length; j++) {
          s += thisHash[j];
        }
        s += "\n" + toStringFull();
        if (logger.level <= Logger.WARNING) {
          logger.log(s);
        }
        return false;
      }
    }

    return true;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toString() {
    return "[Manifest obj=[" + dump(objectHash, false) + "] expires=" + expirationDate + "]";
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  public String toStringFull() {
    String result = "";

    result = result + "Manifest (expires " + expirationDate + ")\n";
    result = result + "  - objectHash = [" + dump(objectHash, false) + "]\n";
    result = result + "  - signature  = [" + dump(signature, false) + "]\n";
    for (int i = 0; i < fragmentHash.length; i++) {
      result = result + "  - fragmHash" + i + " = [" + dump(fragmentHash[i], false) + "]\n";
    }

    return result;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param oos DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    oos.writeInt(objectHash.length);
    oos.writeInt(fragmentHash.length);
    oos.writeInt(fragmentHash[0].length);
    oos.writeInt(signature.length);
    oos.write(objectHash);
    int dim1 = fragmentHash.length;
    int dim2 = fragmentHash[0].length;
    byte[] fragmentHashField = new byte[dim1 * dim2];
    for (int i = 0; i < dim1; i++) {
      for (int j = 0; j < dim2; j++) {
        fragmentHashField[i * dim2 + j] = fragmentHash[i][j];
      }
    }
    oos.write(fragmentHashField);
    oos.write(signature);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param ois DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   * @exception ClassNotFoundException DESCRIBE THE EXCEPTION
   */
  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    int objectHashLength = ois.readInt();
    int fragmentHashLength = ois.readInt();
    int fragmentHashSubLength = ois.readInt();
    int signatureLength = ois.readInt();
    objectHash = new byte[objectHashLength];
    ois.readFully(objectHash, 0, objectHashLength);
    byte[] fragmentHashField = new byte[fragmentHashLength * fragmentHashSubLength];
    ois.readFully(fragmentHashField, 0, fragmentHashLength * fragmentHashSubLength);
    fragmentHash = new byte[fragmentHashLength][fragmentHashSubLength];
    for (int i = 0; i < fragmentHashLength; i++) {
      for (int j = 0; j < fragmentHashSubLength; j++) {
        fragmentHash[i][j] = fragmentHashField[i * fragmentHashSubLength + j];
      }
    }
    signature = new byte[signatureLength];
    ois.readFully(signature, 0, signatureLength);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param data DESCRIBE THE PARAMETER
   * @param linebreak DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  private static String dump(byte[] data, boolean linebreak) {
    final String hex = "0123456789ABCDEF";
    String result = "";

    for (int i = 0; i < data.length; i++) {
      int d = data[i];
      if (d < 0) {
        d += 256;
      }
      int hi = (d >> 4);
      int lo = (d & 15);

      result = result + hex.charAt(hi) + hex.charAt(lo);
      if (linebreak && (((i % 16) == 15) || (i == (data.length - 1)))) {
        result = result + "\n";
      } else if (i != (data.length - 1)) {
        result = result + " ";
      }
    }

    return result;
  }
}

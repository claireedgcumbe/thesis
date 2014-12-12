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

import rice.environment.Environment;
import rice.environment.logging.*;
import rice.environment.logging.LogManager;
import rice.p2p.glacier.*;
import rice.Continuation;
import java.io.*;
import java.util.Arrays;
import java.security.*;

/**
 * DESCRIBE THE CLASS
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class GlacierDefaultPolicy implements GlacierPolicy {

  /**
   * DESCRIBE THE FIELD
   */
  protected ErasureCodec codec;
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
   * Constructor for GlacierDefaultPolicy.
   *
   * @param codec DESCRIBE THE PARAMETER
   * @param instance DESCRIBE THE PARAMETER
   * @param env DESCRIBE THE PARAMETER
   */
  public GlacierDefaultPolicy(ErasureCodec codec, String instance, Environment env) {
    this.codec = codec;
    this.instance = instance;
    this.environment = env;
    logger = environment.getLogManager().getLogger(GlacierDefaultPolicy.class, instance);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param manifest DESCRIBE THE PARAMETER
   * @param key DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean checkSignature(Manifest manifest, VersionKey key) {
    if (manifest.getSignature() == null) {
      return false;
    }

    return Arrays.equals(manifest.getSignature(), key.toByteArray());
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param manifest DESCRIBE THE PARAMETER
   * @param key DESCRIBE THE PARAMETER
   */
  protected void signManifest(Manifest manifest, VersionKey key) {
    manifest.setSignature(key.toByteArray());
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   * @param command DESCRIBE THE PARAMETER
   */
  public void prefetchLocalObject(VersionKey key, Continuation command) {
    command.receiveResult(null);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param fragments DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public Serializable decodeObject(Fragment[] fragments) {
    return (Serializable) codec.decode(fragments);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   * @param obj DESCRIBE THE PARAMETER
   * @param fragments DESCRIBE THE PARAMETER
   * @param expiration DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public Manifest[] createManifests(VersionKey key, Serializable obj, Fragment[] fragments, long expiration) {
    byte bytes[] = null;
    try {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

      objectStream.writeObject(obj);
      objectStream.flush();

      bytes = byteStream.toByteArray();
    } catch (IOException ioe) {
      if (logger.level <= Logger.WARNING) {
        logger.log(
          "Cannot serialize object: " + ioe);
      }
      return null;
    }

    /*
     *  Get the SHA-1 hash object.
     */
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException e) {
      if (logger.level <= Logger.WARNING) {
        logger.log(
          "No SHA support!");
      }
      return null;
    }

    /*
     *  Compute the hash values.
     */
    byte[][] fragmentHash = new byte[fragments.length][];
    for (int i = 0; i < fragments.length; i++) {
      md.reset();
      md.update(fragments[i].getPayload());
      fragmentHash[i] = md.digest();
    }

    byte[] objectHash = null;
    md.reset();
    md.update(bytes);
    objectHash = md.digest();

    /*
     *  Create the manifest
     */
    Manifest[] manifests = new Manifest[fragments.length];
    for (int i = 0; i < fragments.length; i++) {
      manifests[i] = new Manifest(objectHash, fragmentHash, expiration);
      signManifest(manifests[i], key);
    }

    return manifests;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param obj DESCRIBE THE PARAMETER
   * @param generateFragment DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public Fragment[] encodeObject(Serializable obj, boolean[] generateFragment) {
    if (logger.level <= Logger.FINER) {
      logger.log(
        "Serialize object: " + obj);
    }

    byte bytes[] = null;
    try {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

      objectStream.writeObject(obj);
      objectStream.flush();

      bytes = byteStream.toByteArray();
    } catch (IOException ioe) {
      if (logger.level <= Logger.WARNING) {
        logger.log(
          "Cannot serialize object: " + ioe);
      }
      return null;
    }

    if (logger.level <= Logger.FINER) {
      logger.log(
        "Create fragments: " + obj);
    }
    Fragment[] fragments = codec.encode(bytes, generateFragment);
    if (logger.level <= Logger.FINER) {
      logger.log(
        "Completed: " + obj);
    }

    return fragments;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param key DESCRIBE THE PARAMETER
   * @param manifest DESCRIBE THE PARAMETER
   * @param newExpiration DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public Manifest updateManifest(VersionKey key, Manifest manifest, long newExpiration) {
    if (!checkSignature(manifest, key)) {
      return null;
    }

    Manifest newManifest = new Manifest(manifest.getObjectHash(), manifest.getFragmentHashes(), newExpiration);
    signManifest(newManifest, key);

    return newManifest;
  }
}

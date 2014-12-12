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

package rice.persistence.testing;

/*
 *  @(#) StorageTest.java
 *
 *  @author Ansley Post
 *  @author Alan Mislove
 *
 *  @version $Id: Test.java 2551 2005-06-06 19:01:02Z jeffh $
 */
import java.util.*;

import rice.*;
import rice.environment.Environment;
import rice.persistence.*;

/**
 * This class is a class which tests the Storage class in the rice.persistence
 * package.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public abstract class Test {

  /**
   * DESCRIBE THE FIELD
   */
  protected Environment environment;

  /**
   * DESCRIBE THE FIELD
   */
  protected final static String SUCCESS = "SUCCESS";
  /**
   * DESCRIBE THE FIELD
   */
  protected final static String FAILURE = "FAILURE";

  /**
   * DESCRIBE THE FIELD
   */
  protected final static int PAD_SIZE = 60;

  /**
   * Constructor for Test.
   *
   * @param env DESCRIBE THE PARAMETER
   */
  public Test(Environment env) {
    environment = env;
  }

  /**
   * DESCRIBE THE METHOD
   */
  public abstract void start();

  /**
   * DESCRIBE THE METHOD
   *
   * @param name DESCRIBE THE PARAMETER
   */
  protected void sectionStart(String name) {
    System.out.println(name);
  }

  /**
   * DESCRIBE THE METHOD
   */
  protected void sectionEnd() {
    System.out.println();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param name DESCRIBE THE PARAMETER
   */
  protected void stepStart(String name) {
    System.out.print(pad("  " + name));
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param status DESCRIBE THE PARAMETER
   */
  protected void stepDone(String status) {
    System.out.println("[" + status + "]");
    if (status.equals(FAILURE)) {
      System.exit(0);
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param status DESCRIBE THE PARAMETER
   * @param message DESCRIBE THE PARAMETER
   */
  protected void stepDone(String status, String message) {
    System.out.println("[" + status + "]");
    System.out.println("    " + message);
    if (status.equals(FAILURE)) {
      System.exit(0);
    }
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param e DESCRIBE THE PARAMETER
   */
  protected void stepException(Exception e) {
    System.out.println();

    System.out.println("Exception " + e + " occurred during testing.");

    e.printStackTrace();
    System.exit(0);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param start DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  private String pad(String start) {
    if (start.length() >= PAD_SIZE) {
      return start.substring(0, PAD_SIZE);
    } else {
      int spaceLength = PAD_SIZE - start.length();
      char[] spaces = new char[spaceLength];
      Arrays.fill(spaces, '.');

      return start.concat(new String(spaces));
    }
  }
}

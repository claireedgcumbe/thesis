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
 *  Created on Jun 1, 2005
 */
package rice.environment.params.simple;

import java.io.*;
import java.io.PrintWriter;

/**
 * Unable to load the default parameters file. This is required for running
 * pastry.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public class ParamsNotPresentException extends RuntimeException {
  Exception subexception;

  /**
   * @param reason DESCRIBE THE PARAMETER
   * @param e DESCRIBE THE PARAMETER
   */
  public ParamsNotPresentException(String reason, Exception e) {
    super(reason);
    this.subexception = e;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param arg0 DESCRIBE THE PARAMETER
   */
  public void printStackTrace(PrintStream arg0) {
    super.printStackTrace(arg0);
    subexception.printStackTrace(arg0);
  }

  /**
   * DESCRIBE THE METHOD
   */
  public void printStackTrace() {
    super.printStackTrace();
    subexception.printStackTrace();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param pw DESCRIBE THE PARAMETER
   */
  public void printStackTrace(PrintWriter pw) {
    super.printStackTrace(pw);
    subexception.printStackTrace(pw);
  }
}

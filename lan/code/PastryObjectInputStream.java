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
 *  Created on May 10, 2004
 *
 *  To change the template for this generated file go to
 *  Window>Preferences>Java>Code Generation>Code and Comments
 */
package rice.pastry;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh To change the template for this generated type comment go to
 *      Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PastryObjectInputStream extends ObjectInputStream {

  /**
   * DESCRIBE THE FIELD
   */
  protected PastryNode node;

  /**
   * @param stream DESCRIBE THE PARAMETER
   * @param node DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   * @throws java.io.IOException
   */
  public PastryObjectInputStream(InputStream stream, PastryNode node)
     throws IOException {
    super(stream);
    this.node = node;
    enableResolveObject(true);
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param input DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected Object resolveObject(Object input) throws IOException {
    if (input instanceof NodeHandle) {
//      System.out.println("POIS.resolveObject("+input+"@"+System.identityHashCode(input)+"):"+node);
      if (node != null) {
        // coalesce
        input = node.coalesce((NodeHandle) input);
      }
    }

    return input;
  }
}

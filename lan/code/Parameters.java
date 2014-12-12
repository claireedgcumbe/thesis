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
 *  Created on May 26, 2005
 */
package rice.environment.params;

import java.io.IOException;
import java.net.*;

/**
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author Jeff Hoye
 */
public interface Parameters {
  //
  /**
   * DESCRIBE THE METHOD
   *
   * @param name DESCRIBE THE PARAMETER
   */
  public void remove(String name);

  /**
   * DESCRIBE THE METHOD
   *
   * @param name DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  public boolean contains(String name);

  /**
   * DESCRIBE THE METHOD
   *
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public void store() throws IOException;

  // getters
  /**
   * Gets the String attribute of the Parameters object
   *
   * @param paramName DESCRIBE THE PARAMETER
   * @return The String value
   */
  public String getString(String paramName);

  /**
   * Gets the StringArray attribute of the Parameters object
   *
   * @param paramName DESCRIBE THE PARAMETER
   * @return The StringArray value
   */
  public String[] getStringArray(String paramName);

  /**
   * Gets the Int attribute of the Parameters object
   *
   * @param paramName DESCRIBE THE PARAMETER
   * @return The Int value
   */
  public int getInt(String paramName);

  /**
   * Gets the Double attribute of the Parameters object
   *
   * @param paramName DESCRIBE THE PARAMETER
   * @return The Double value
   */
  public double getDouble(String paramName);

  /**
   * Gets the Float attribute of the Parameters object
   *
   * @param paramName DESCRIBE THE PARAMETER
   * @return The Float value
   */
  public float getFloat(String paramName);

  /**
   * Gets the Long attribute of the Parameters object
   *
   * @param paramName DESCRIBE THE PARAMETER
   * @return The Long value
   */
  public long getLong(String paramName);

  /**
   * Gets the Boolean attribute of the Parameters object
   *
   * @param paramName DESCRIBE THE PARAMETER
   * @return The Boolean value
   */
  public boolean getBoolean(String paramName);

  /**
   * String format is dnsname ex: "computer.school.edu"
   *
   * @param paramName
   * @return
   * @throws UnknownHostException
   */
  public InetAddress getInetAddress(String paramName) throws UnknownHostException;


  /**
   * String format is name:port ex: "computer.school.edu:1984"
   *
   * @param paramName
   * @return
   * @exception UnknownHostException DESCRIBE THE EXCEPTION
   */
  public InetSocketAddress getInetSocketAddress(String paramName) throws UnknownHostException;

  /**
   * String format is comma seperated. ex:
   * "computer.school.edu:1984,computer2.school.edu:1984,computer.school.edu:1985"
   *
   * @param paramName
   * @return
   * @exception UnknownHostException DESCRIBE THE EXCEPTION
   */
  public InetSocketAddress[] getInetSocketAddressArray(String paramName) throws UnknownHostException;

  // setters
  /**
   * Sets the String attribute of the Parameters object
   *
   * @param paramName The new String value
   * @param val The new String value
   */
  public void setString(String paramName, String val);

  /**
   * Sets the StringArray attribute of the Parameters object
   *
   * @param paramName The new StringArray value
   * @param val The new StringArray value
   */
  public void setStringArray(String paramName, String[] val);

  /**
   * Sets the Int attribute of the Parameters object
   *
   * @param paramName The new Int value
   * @param val The new Int value
   */
  public void setInt(String paramName, int val);

  /**
   * Sets the Double attribute of the Parameters object
   *
   * @param paramName The new Double value
   * @param val The new Double value
   */
  public void setDouble(String paramName, double val);

  /**
   * Sets the Float attribute of the Parameters object
   *
   * @param paramName The new Float value
   * @param val The new Float value
   */
  public void setFloat(String paramName, float val);

  /**
   * Sets the Long attribute of the Parameters object
   *
   * @param paramName The new Long value
   * @param val The new Long value
   */
  public void setLong(String paramName, long val);

  /**
   * Sets the Boolean attribute of the Parameters object
   *
   * @param paramName The new Boolean value
   * @param val The new Boolean value
   */
  public void setBoolean(String paramName, boolean val);

  /**
   * Sets the InetAddress attribute of the Parameters object
   *
   * @param paramName The new InetAddress value
   * @param val The new InetAddress value
   */
  public void setInetAddress(String paramName, InetAddress val);

  /**
   * Sets the InetSocketAddress attribute of the Parameters object
   *
   * @param paramName The new InetSocketAddress value
   * @param val The new InetSocketAddress value
   */
  public void setInetSocketAddress(String paramName, InetSocketAddress val);

  /**
   * Sets the InetSocketAddressArray attribute of the Parameters object
   *
   * @param paramName The new InetSocketAddressArray value
   * @param val The new InetSocketAddressArray value
   */
  public void setInetSocketAddressArray(String paramName, InetSocketAddress[] val);

  /**
   * Adds a feature to the ChangeListener attribute of the Parameters object
   *
   * @param p The feature to be added to the ChangeListener attribute
   */
  public void addChangeListener(ParameterChangeListener p);

  /**
   * DESCRIBE THE METHOD
   *
   * @param p DESCRIBE THE PARAMETER
   */
  public void removeChangeListener(ParameterChangeListener p);
}

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
package rice.p2p.util;

import org.xmlpull.v1.*;
import java.io.*;
import java.util.*;

/**
 * This class is a memory-efficient implementation of most of the XML pull
 * parsing API.
 *
 * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
 * @author jeffh
 */
public class XMLParser implements XmlPullParser {

  /**
   * The internal reader used to read data
   */
  protected Reader reader;

  /**
   * The internal buffer used to process data
   */
  protected char[] buffer;

  /**
   * Internal pointers into the buffer
   */
  protected int bufferPosition;
  /**
   * DESCRIBE THE FIELD
   */
  protected int bufferLimit;

  /**
   * The StringCache used to reduce the memory requirements
   */
  protected StringCache cache;

  /**
   * The internal stack of tags which have been read
   */
  protected Stack tags;

  /**
   * If the tag parsed was a start/end, the name of the tag
   */
  protected String name;

  /**
   * If the tag parsed was text, the text
   */
  protected String text;

  /**
   * If the tag parsed was a start tag, the list of attribute-> value pairs
   */
  protected String[] attributeKeys = new String[MAX_ATTRIBUTES];
  /**
   * DESCRIBE THE FIELD
   */
  protected String[] attributeValues = new String[MAX_ATTRIBUTES];
  /**
   * DESCRIBE THE FIELD
   */
  protected int numAttributes;

  /**
   * Whether or not we are currently in a tag...
   */
  protected boolean inTag;

  /**
   * Internal variable which keeps track of the current mark
   */
  protected int mark;
  /**
   * DESCRIBE THE FIELD
   */
  protected CharArrayBuffer marked;

  /**
   * The size of the internal buffer to allocate
   */
  public final static int BUFFER_SIZE = 32000;
  /**
   * DESCRIBE THE FIELD
   */
  public final static int MAX_ATTRIBUTES = 100;

  /**
   * DESCRIBE THE FIELD
   */
  public final static char[] QUOTE = new char[]{'\'', '"'};
  /**
   * DESCRIBE THE FIELD
   */
  public final static char[] TAG_END = new char[]{'>', '/', '?'};
  /**
   * DESCRIBE THE FIELD
   */
  public final static char[] WHITESPACE = new char[]{' ', '\t', '\n', '\r'};
  /**
   * DESCRIBE THE FIELD
   */
  public final static char[] WHITESPACE_OR_TAG_END = new char[]{' ', '\t', '\n', '\r', '>', '/', '?'};
  /**
   * DESCRIBE THE FIELD
   */
  public final static char[] WHITESPACE_OR_EQUALS = new char[]{' ', '\t', '\n', '\r', '='};
  /**
   * DESCRIBE THE FIELD
   */
  public final static char[] SINGLE = new char[1];

  /**
   * DESCRIBE THE FIELD
   */
  public final static String[][] ENTITIES = new String[][]{{"&apos;", "'"},
    {"&quot;", "\""},
    {"&lt;", "<"},
    {"&gt;", ">"},
    {"&#13;", String.valueOf((char) 13)},
    {"&#10;", String.valueOf((char) 10)},
    {"&#9;", String.valueOf((char) 9)},
    {"&amp;", "&"}};

  /**
   * Constructor
   */
  public XMLParser() {
    this.buffer = new char[BUFFER_SIZE];
    this.bufferPosition = 0;
    this.bufferLimit = 0;
    this.mark = -1;
    this.tags = new Stack();
    this.cache = new StringCache();
    this.inTag = false;
    this.numAttributes = 0;
  }

  /**
   * Returns the text content of the current event as String. The value returned
   * depends on current event type, for example for TEXT event it is element
   * content (this is typical case when next() is used). See description of
   * nextToken() for detailed description of possible returned values for
   * different types of events. <p>
   *
   * <strong>NOTE:</strong> in case of ENTITY_REF, this method returns the
   * entity replacement text (or null if not available). This is the only case
   * where getText() and getTextCharacters() return different values.
   *
   * @return The Text value
   * @see #getEventType
   * @see #next
   * @see #nextToken
   */
  public String getText() {
    return this.text;
  }

  /**
   * For START_TAG or END_TAG events, the (local) name of the current element is
   * returned when namespaces are enabled. When namespace processing is
   * disabled, the raw name is returned. For ENTITY_REF events, the entity name
   * is returned. If the current event is not START_TAG, END_TAG, or ENTITY_REF,
   * null is returned. <p>
   *
   * <b>Please note:</b> To reconstruct the raw element name when namespaces are
   * enabled and the prefix is not null, you will need to add the prefix and a
   * colon to localName..
   *
   * @return The Name value
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the attributes value identified by namespace URI and namespace
   * localName. If namespaces are disabled namespace must be null. If current
   * event type is not START_TAG then IndexOutOfBoundsException will be thrown.
   * <p>
   *
   * <strong>NOTE:</strong> attribute value must be normalized (including entity
   * replacement text if PROCESS_DOCDECL is false) as described in <a
   * href="http://www.w3.org/TR/REC-xml#AVNormalize">XML 1.0 section 3.3.3
   * Attribute-Value Normalization</a>
   *
   * @param namespace Namespace of the attribute if namespaces are enabled
   *      otherwise must be null
   * @param name If namespaces enabled local name of attribute otherwise just
   *      attribute name
   * @return value of attribute or null if attribute with given name does not
   *      exist
   * @see #defineEntityReplacementText
   */
  public String getAttributeValue(String namespace, String name) {
    for (int i = 0; i < numAttributes; i++) {
      if (attributeKeys[i].equals(name)) {
        return attributeValues[i];
      }
    }

    return null;
  }

  /**
   * Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.)
   *
   * @return The EventType value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @see #next()
   * @see #nextToken()
   */
  public int getEventType() throws XmlPullParserException {
    return 0;
  }

  /**
   * Checks whether the current TEXT event contains only whitespace characters.
   * For IGNORABLE_WHITESPACE, this is always true. For TEXT and CDSECT, false
   * is returned when the current event text contains at least one non-white
   * space character. For any other event type an exception is thrown. <p>
   *
   * <b>Please note:</b> non-validating parsers are not able to distinguish
   * whitespace and ignorable whitespace, except from whitespace outside the
   * root element. Ignorable whitespace is reported as separate event, which is
   * exposed via nextToken only.
   *
   * @return The Whitespace value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */
  public boolean isWhitespace() throws XmlPullParserException {
    return isWhitespace(text);
  }

  /**
   * Internal method which checks for existence
   *
   * @param text DESCRIBE THE PARAMETER
   * @return The Whitespace value
   */
  public boolean isWhitespace(String text) {
    for (int i = 0; i < text.length(); i++) {
      if (!contains(WHITESPACE, text.charAt(i))) {
        return false;
      }
    }

    return true;
  }

  /**
   * Gets the Feature attribute of the XMLParser object
   *
   * @param name DESCRIBE THE PARAMETER
   * @return The Feature value
   */
  public boolean getFeature(String name) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the Property attribute of the XMLParser object
   *
   * @param name DESCRIBE THE PARAMETER
   * @return The Property value
   */
  public Object getProperty(String name) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the InputEncoding attribute of the XMLParser object
   *
   * @return The InputEncoding value
   */
  public String getInputEncoding() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the NamespaceCount attribute of the XMLParser object
   *
   * @param depth DESCRIBE THE PARAMETER
   * @return The NamespaceCount value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */
  public int getNamespaceCount(int depth) throws XmlPullParserException {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the NamespacePrefix attribute of the XMLParser object
   *
   * @param pos DESCRIBE THE PARAMETER
   * @return The NamespacePrefix value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */
  public String getNamespacePrefix(int pos) throws XmlPullParserException {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the NamespaceUri attribute of the XMLParser object
   *
   * @param pos DESCRIBE THE PARAMETER
   * @return The NamespaceUri value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */
  public String getNamespaceUri(int pos) throws XmlPullParserException {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the Namespace attribute of the XMLParser object
   *
   * @param prefix DESCRIBE THE PARAMETER
   * @return The Namespace value
   */
  public String getNamespace(String prefix) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the Depth attribute of the XMLParser object
   *
   * @return The Depth value
   */
  public int getDepth() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the PositionDescription attribute of the XMLParser object
   *
   * @return The PositionDescription value
   */
  public String getPositionDescription() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the LineNumber attribute of the XMLParser object
   *
   * @return The LineNumber value
   */
  public int getLineNumber() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the ColumnNumber attribute of the XMLParser object
   *
   * @return The ColumnNumber value
   */
  public int getColumnNumber() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the TextCharacters attribute of the XMLParser object
   *
   * @param holderForStartAndLength DESCRIBE THE PARAMETER
   * @return The TextCharacters value
   */
  public char[] getTextCharacters(int[] holderForStartAndLength) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the Namespace attribute of the XMLParser object
   *
   * @return The Namespace value
   */
  public String getNamespace() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the Prefix attribute of the XMLParser object
   *
   * @return The Prefix value
   */
  public String getPrefix() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the EmptyElementTag attribute of the XMLParser object
   *
   * @return The EmptyElementTag value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */
  public boolean isEmptyElementTag() throws XmlPullParserException {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the AttributeNamespace attribute of the XMLParser object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The AttributeNamespace value
   */
  public String getAttributeNamespace(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the AttributePrefix attribute of the XMLParser object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The AttributePrefix value
   */
  public String getAttributePrefix(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the AttributeType attribute of the XMLParser object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The AttributeType value
   */
  public String getAttributeType(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the AttributeDefault attribute of the XMLParser object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The AttributeDefault value
   */
  public boolean isAttributeDefault(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the AttributeCount attribute of the XMLParser object
   *
   * @return The AttributeCount value
   */
  public int getAttributeCount() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the AttributeName attribute of the XMLParser object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The AttributeName value
   */
  public String getAttributeName(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the AttributeValue attribute of the XMLParser object
   *
   * @param index DESCRIBE THE PARAMETER
   * @return The AttributeValue value
   */
  public String getAttributeValue(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * Set the input source for parser to the given reader and resets the parser.
   * The event type is set to the initial value START_DOCUMENT. Setting the
   * reader to null will just stop parsing and reset parser state, allowing the
   * parser to free internal resources such as parsing buffers.
   *
   * @param in The new Input value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */
  public void setInput(Reader in) throws XmlPullParserException {
    this.reader = in;
  }

  /**
   * ----- UNSUPPORTED METHODS -----
   *
   * @param name The new Feature value
   * @param state The new Feature value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */

  public void setFeature(String name, boolean state) throws XmlPullParserException {
    throw new UnsupportedOperationException();
  }

  /**
   * Sets the Property attribute of the XMLParser object
   *
   * @param name The new Property value
   * @param value The new Property value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */
  public void setProperty(String name, Object value) throws XmlPullParserException {
    throw new UnsupportedOperationException();
  }

  /**
   * Sets the Input attribute of the XMLParser object
   *
   * @param inputStream The new Input value
   * @param inputEncoding The new Input value
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */
  public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
    throw new UnsupportedOperationException();
  }

  /**
   * Get next parsing event - element content wil be coalesced and only one TEXT
   * event must be returned for whole element content (comments and processing
   * instructions will be ignored and emtity references must be expanded or
   * exception mus be thrown if entity reerence can not be exapnded). If element
   * content is empty (content is "") then no TEXT event will be reported. <p>
   *
   * <b>NOTE:</b> empty element (such as &lt;tag/>) will be reported with two
   * separate events: START_TAG, END_TAG - it must be so to preserve parsing
   * equivalency of empty element to &lt;tag>&lt;/tag>. (see isEmptyElementTag
   * ())
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   * @see #isEmptyElementTag
   * @see #START_TAG
   * @see #TEXT
   * @see #END_TAG
   * @see #END_DOCUMENT
   */
  public int next() throws XmlPullParserException, IOException {
    char next = 0;

    try {
      next = current();
    } catch (EOFException e) {
      return END_DOCUMENT;
    }

    switch (next) {
      case '<':
        int result = parseTag();

        if (result == START_DOCUMENT) {
          return next();
        } else {
          return result;
        }
      case '/':
        if (this.inTag) {
          return parseEndTag((String) tags.pop());
        } else {
          return parseText();
        }
      default:
        return parseText();
    }
  }

  /**
   * ----- INTERNAL PARSING METHODS -----
   *
   * @exception IOException DESCRIBE THE EXCEPTION
   */

  /**
   * Internal method which actually fills the buffer
   *
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected void fillBuffer() throws IOException {
    // process the mark
    if (marked != null) {
      this.marked.append(buffer, 0, bufferPosition);
    } else if (mark != -1) {
      this.marked = new CharArrayBuffer(buffer, mark, bufferPosition - mark);
    }

    int read = reader.read(buffer);

    if (read > 0) {
      bufferLimit = read;
      bufferPosition = 0;
    }
  }

  /**
   * Method which returns the current char in the buffer
   *
   * @return The current char
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected char current() throws IOException {
    if (bufferPosition == bufferLimit) {
      fillBuffer();
    }
    if (bufferPosition == bufferLimit) {
      throw new EOFException();
    }

    return buffer[bufferPosition];
  }

  /**
   * Method which steps forward in the buffer
   */
  protected void step() {
    bufferPosition++;
  }

  /**
   * Sets the mark
   */
  protected void mark() {
    this.mark = bufferPosition;
  }

  /**
   * Unsets the mark
   *
   * @return DESCRIBE THE RETURN VALUE
   */
  protected String unmark() {
    try {
      if (this.marked != null) {
        this.marked.append(buffer, 0, bufferPosition);
        return cache.get(marked.getBuffer(), 0, marked.getLength());
      } else {
        return cache.get(buffer, mark, bufferPosition - mark);
      }
    } finally {
      this.mark = -1;
      this.marked = null;
    }
  }

  /**
   * Internal method which clears the list of attributes
   */
  protected void clearAttributes() {
    this.numAttributes = 0;
  }

  /**
   * Internal method which adds an attributes
   *
   * @param key The feature to be added to the Attribute attribute
   * @param value The feature to be added to the Attribute attribute
   */
  protected void addAttribute(String key, String value) {
    if (numAttributes == attributeKeys.length) {
      throw new RuntimeException("More than " + attributeKeys.length + " attributes encountered - unsupported!");
    }

    attributeKeys[numAttributes] = key;
    attributeValues[numAttributes] = value;
    numAttributes++;
  }

  /**
   * An assertion method
   *
   * @param c DESCRIBE THE PARAMETER
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected void expect(char c) throws XmlPullParserException, IOException {
    if (current() != c) {
      throw new XmlPullParserException("Expected character '" + c + "' got '" + current() + "'");
    } else {
      step();
    }
  }

  /**
   * Internal method which checks for existence
   *
   * @param chars The chars to check for
   * @param c DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   */
  protected boolean contains(char[] chars, char c) {
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == c) {
        return true;
      }
    }

    return false;
  }

  /**
   * Method which parses and returns up to the next token
   *
   * @param chars DESCRIBE THE PARAMETER
   * @return The token
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected String parseUntil(char[] chars) throws IOException {
    mark();

    while (true) {
      if (contains(chars, current())) {
        break;
      } else {
        step();
      }
    }

    return unmark();
  }

  /**
   * Method which parses and returns up to the next token
   *
   * @param c DESCRIBE THE PARAMETER
   * @return The token
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected String parseUntil(char c) throws IOException {
    mark();

    while (true) {
      if (current() == c) {
        break;
      } else {
        step();
      }
    }

    return unmark();
  }

  /**
   * Method which parses up to the next token
   *
   * @param chars DESCRIBE THE PARAMETER
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected void parseUntilNot(char[] chars) throws IOException {
    while (true) {
      if (!contains(chars, current())) {
        break;
      } else {
        step();
      }
    }
  }

  /**
   * Method which parses an end tag of the form <tag />
   *
   * @param tag DESCRIBE THE PARAMETER
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected int parseEndTag(String tag) throws XmlPullParserException, IOException {
    expect('/');
    expect('>');

    this.name = tag;
    this.inTag = false;

//    System.out.println("Parsed default END tag '" + this.name + "'");

    return END_TAG;
  }

  /**
   * Internal method which parses a tag
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected int parseTag() throws XmlPullParserException, IOException {
    expect('<');

    switch (current()) {
      case '/':
        return parseEndTag();
      case '?':
        return parseDocumentTag();
      default:
        return parseStartTag();
    }
  }

  /**
   * Method which parses an end tag of the form <tag />
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected int parseEndTag() throws XmlPullParserException, IOException {
    expect('/');
    parseUntilNot(WHITESPACE);

    clearAttributes();
    this.name = parseUntil(WHITESPACE_OR_TAG_END);
    tags.pop();
    this.inTag = false;

    parseUntilNot(WHITESPACE);
    expect('>');

//    System.out.println("Parsed END tag '" + this.name + "'");

    return END_TAG;
  }

  /**
   * Method which parses a start tag
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected int parseStartTag() throws XmlPullParserException, IOException {
    parseUntilNot(WHITESPACE);

    this.name = parseUntil(WHITESPACE_OR_TAG_END);
    tags.push(this.name);

    parseUntilNot(WHITESPACE);

//    System.out.println("Parsed START tag '" + this.name + "'");

    parseAttributes();

    parseUntilNot(WHITESPACE);

    if (current() != '/') {
      expect('>');
      this.inTag = false;
    } else {
      this.inTag = true;
    }

    return START_TAG;
  }

  /**
   * Method which parses a document tag
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected int parseDocumentTag() throws XmlPullParserException, IOException {
    expect('?');

    parseUntilNot(WHITESPACE);

    String type = parseUntil(WHITESPACE_OR_TAG_END);

    if (!(type.toLowerCase().equals("xml"))) {
      throw new XmlPullParserException("This does not appear to be an XML document - found '" + type + "'!");
    }

    parseUntilNot(WHITESPACE);

    parseAttributes();
    clearAttributes();

    parseUntilNot(WHITESPACE);
    this.inTag = false;

    expect('?');
    expect('>');

//    System.out.println("Parsed START DOCUMENT tag '" + type + "'");

    return START_DOCUMENT;
  }

  /**
   * Method which parses all of the attributes of a start tag
   *
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected void parseAttributes() throws XmlPullParserException, IOException {
    clearAttributes();

    while (true) {
      parseUntilNot(WHITESPACE);

      if (contains(TAG_END, current())) {
        return;
      } else {
        String key = parseUntil(WHITESPACE_OR_EQUALS);
        parseUntilNot(WHITESPACE);
        expect('=');
        parseUntilNot(WHITESPACE);
        String value = null;
        char quote = current();

        if (contains(QUOTE, quote)) {
          expect(quote);
          value = convert(parseUntil(quote));
          expect(quote);
        } else {
          value = convert(parseUntil(WHITESPACE_OR_TAG_END));
        }

//        System.out.println("Parsed ATTRIBUTE '" + key + "' -> '"  + value + "'");

        addAttribute(key, value);
      }
    }
  }

  /**
   * Method which parses an end tag of the form <tag />
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  protected int parseText() throws XmlPullParserException, IOException {
    clearAttributes();
    this.text = convert(parseUntil('<'));
    this.inTag = false;

    return TEXT;
  }

  /**
   * Internal method which deconverts all of the HTML/XML entities like &amp,
   * &gt, &lt, etc...
   *
   * @param string The string to convert
   * @return The result
   */
  protected String convert(String string) {
    if (string.indexOf('&') < 0) {
      return string;
    }

    for (int i = 0; i < ENTITIES.length; i++) {
      string = string.replaceAll(ENTITIES[i][0], ENTITIES[i][1]);
    }

    return string;
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param entityName DESCRIBE THE PARAMETER
   * @param replacementText DESCRIBE THE PARAMETER
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   */
  public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {
    throw new UnsupportedOperationException();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public int nextToken() throws XmlPullParserException, IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @param type DESCRIBE THE PARAMETER
   * @param namespace DESCRIBE THE PARAMETER
   * @param name DESCRIBE THE PARAMETER
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public String nextText() throws XmlPullParserException, IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * DESCRIBE THE METHOD
   *
   * @return DESCRIBE THE RETURN VALUE
   * @exception XmlPullParserException DESCRIBE THE EXCEPTION
   * @exception IOException DESCRIBE THE EXCEPTION
   */
  public int nextTag() throws XmlPullParserException, IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * ----- INTERNAL CLASS -----
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */

  /**
   * This class implements a char array buffer
   *
   * @version $Id: pretty.settings 2305 2005-03-11 20:22:33Z jeffh $
   * @author jeffh
   */
  public class CharArrayBuffer {

    /**
     * The internal buffer
     */
    protected char[] buffer;

    /**
     * The markers
     */
    protected int length;

    /**
     * The default initial capacity
     */
    public final static int DEFAULT_CAPACITY = 32;

    /**
     * Constructor
     *
     * @param chars DESCRIBE THE PARAMETER
     * @param length DESCRIBE THE PARAMETER
     * @param off DESCRIBE THE PARAMETER
     */
    public CharArrayBuffer(char[] chars, int length, int off) {
      this.buffer = new char[DEFAULT_CAPACITY];
      this.length = 0;

      append(chars, length, off);
    }

    /**
     * Returns the internal array
     *
     * @return The array
     */
    public char[] getBuffer() {
      return buffer;
    }

    /**
     * Returns the length
     *
     * @return The length
     */
    public int getLength() {
      return length;
    }

    /**
     * Appends some more chars!
     *
     * @param chars DESCRIBE THE PARAMETER
     * @param off DESCRIBE THE PARAMETER
     * @param len DESCRIBE THE PARAMETER
     */
    public void append(char[] chars, int off, int len) {
      while (length + len > buffer.length) {
        expandBuffer();
      }

      System.arraycopy(chars, off, buffer, length, len);
      length += len;
    }

    /**
     * Expands the buffer
     */
    protected void expandBuffer() {
      char[] newbuffer = new char[buffer.length * 2];

      System.arraycopy(buffer, 0, newbuffer, 0, length);
      this.buffer = newbuffer;
    }
  }
}


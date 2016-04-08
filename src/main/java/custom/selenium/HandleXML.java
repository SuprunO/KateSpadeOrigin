/*
 * Copyright (c) 2015, Speroteck Inc. (www.speroteck.com)
 * and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Speroteck or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package custom.selenium;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class is used to create XML and handle all needed operations for
 * read, write, parse and format XML.
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class HandleXML {
    static final Logger logger = Logger.getLogger(TestFactory.class);

    private DocumentBuilder builder;
    private Document temporaryXML;
    private Transformer transformer;
    private static final String FILE_TYPE_XML = ".xml";

    /**
     * Main constructor with default XML properties set.
     */
    HandleXML() {
        paramLangXML();  //@CAPT.OBVIOUS: XML initialization
        temporaryXML = builder.newDocument(); //@CAPT.OBVIOUS: empty xml to work with.
        createTransformer(); //@CAPT.OBVIOUS: transformer with default xml properties set

    }

    /**
     * Creates {@link javax.xml.transform.Transformer} with xml output setup.
     * Probably will require split options
     * if several simultaneous property sets might be needed
     */
    private void createTransformer() {
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e){
            logger.error(e);
            throw new TestFrameworkRuntimeException("Failed to create Transformer(javax.xml.transform)", e);
        }
        setXMLDefaults();
    }

    /**
     * Default Properties to format output XML file to be pretty and indented.
     */
    private void setXMLDefaults() {
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //moves root container to the next line
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    }

    /**
     * Creates the API instance to work with DOM {@link Document} from XML.
     */
    public void paramLangXML() {
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        try {
            builder=factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.error(e);
            throw new TestFrameworkRuntimeException(e);
        }
    }

    /**
     * Creates New <code>Element</code> and adds it as
     * the root <code>Element</code> to {@link #temporaryXML}.
     *
     * @param  newChildName
     *         The name of new child node to add.
     */
    public void addParentNode(String newChildName) {
        Element node = temporaryXML.createElement(newChildName);
        temporaryXML.appendChild(node);
    }

    /**
     * Creates New <code>Element</code> and adds it to the end of the list
     * of children of  Target <code>Element</code> obtained by
     * the <code>tagName</code> from {@link #temporaryXML}.
     * If the <code>newChild</code> is already in the tree, it
     * is first removed.
     *
     * @param  parentNodeName
     *         Target Target <code>Element</code> name where new child
     *         is going to be created in.
     *
     * @param  newChildName
     *         The name of new child node to add.
     */
    public void appendChildNode(String parentNodeName, String newChildName) {
        Element newChildNode = temporaryXML.createElement(newChildName);
        Node parentNode = temporaryXML.getElementsByTagName(parentNodeName).item(0);
        parentNode.appendChild(newChildNode);
    }

    /**
     * Creates New <code>Element</code> and adds it as the first child to
     * the list of children of Target <code>Element</code> obtained by
     * the <code>tagName</code> from {@link #temporaryXML}.
     * If the <code>newChild</code> is already in the tree, it
     * is first removed.
     *
     * @param  parentNodeName
     *         Target Target <code>Element</code> name where new child
     *         is going to be inserted as the first child.
     *
     * @param  newChildName
     *         The name of new child node to add.
     */
    public void appendFirstChild(String parentNodeName, String newChildName) {
        Element newChildNode = temporaryXML.createElement(newChildName);
        Node parentNode = getElementByTagName(parentNodeName);
        parentNode.insertBefore(newChildNode, parentNode.getFirstChild());
    }

    /**
     * Returns the first <code>Node</code> from the <code>NodeList</code> of
     * all the <code>Elements</code> in document order with a given tag name
     * and are contained in the {@link #temporaryXML}.
     *
     * @param   tagName
     *          The name of the tag to match on. The special value "*"
     *          matches the first tag from the all tags. For XML, the
     *          <code>tagName</code> parameter is case-sensitive, otherwise
     *          it depends on case-sensitivity of the markup language in use.
     *
     * @return  A new <code>Node</code> object containing the first matched
     *          <code>Element</code>.
     * @throws  TestFrameworkRuntimeException
     *          If element was not found or found has different tag name
     */
    public Node getElementByTagName(String tagName) {
        Node targetNode = temporaryXML.getElementsByTagName(tagName).item(0);
        if(targetNode == null || !targetNode.getNodeName().equals(tagName)) {
            throw new TestFrameworkRuntimeException("<"+ tagName + "> was not found!");
        }
        return targetNode;
    }

    /**
     * Creates New <code>Element</code> with given text value and adds it to
     * the end of the list of children of  Target <code>Element</code>
     * obtained by the <code>tagName</code> from {@link #temporaryXML}.
     * If the <code>newChild</code> is already in the tree, it
     * is first removed.
     *
     * @param  parentNodeName
     *         Target Target <code>Element</code> name where new child
     *         is going to be created in.
     *
     * @param  newChildName
     *         The name of new child node to add.
     *
     * @param   text
     *          The data for the text value
     *
     * @throws  TestFrameworkRuntimeException
     *          If text argument is null, prevents
     *          NullPointerException during the {@link Transformer#transform}
     */
    public void appendChildNodeWithText(String parentNodeName, String newChildName, String text) {
        if(text == null) {
            String caller = Thread.currentThread().getStackTrace()[2].toString();
            throw new TestFrameworkRuntimeException("'text' cannot be <code>null</code>! at " + caller);
        }
        Node parentNode = getElementByTagName(parentNodeName);
        Element newChildNode = temporaryXML.createElement(newChildName);
        newChildNode.appendChild(temporaryXML.createTextNode(text));
        parentNode.appendChild(newChildNode);
    }

    /**
     * Creates new <code>File</code> by passed "file path + file name",
     * creating folders tree if it doesn't exist.
     *
     * @param   filePath
     *          String absolute|relative File Path + File Name
     *
     * @return  <code>File</code> Object created using given file path
     *
     * @throws  IOException
     *          If an I/O error occurred
     *
     * @throws  SecurityException
     *          If a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkWrite(java.lang.String)}</code>
     *          method denies write access to the file
     */
    private static File getFileByPath(String filePath) throws IOException {
        File targetFile = new File(filePath);
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();
        return targetFile;
    }

    /**
     * <p>Checks for the extension in the passed string which expected be:
     * absolute|relative File Path + File Name, Ex.:</p>
     *  <ul>
     *     <li>C:\temp\MyFile</li>
     *     <li>C:\temp\MyFile.xml</li>
     *     <li>./logs/MyFile</li>
     *     <li>logs/MyFile.html</li>
     *  </ul>
     *
     * @param   fileName
     *          String absolute|relative File Path + File Name
     *
     * @return  True is passed string is file and file name contains "."
     *
     * @see     <a href="https://en.wikipedia.org/wiki/Path_(computing)">
     *          Path_(computing) on wikipedia</a>
     */
    private static boolean isFileTypePresent(String fileName){
        return new File(fileName).isFile() && fileName.contains(".");
    }

    /**
     * <p>Adds ".xml" extension to passed file name <code>String</code>
     * if the extension is absent in it.</p>
     *
     * @param   fileName
     *          String absolute|relative File Path + File Name
     *
     * @return  File path with added ".xml" if "." was not present
     */
    private static String addXMLFileType(String fileName){
        if(!isFileTypePresent(fileName)){
            return fileName + FILE_TYPE_XML;
        } else {
            return fileName;
        }
    }

    /**
     * Creates and writes the result of xml DOM that stored in
     * {@link #temporaryXML} in file by passed filePath with adding ".xml"
     * if file extension is absent.
     *
     * @param   filePath
     *          String absolute|relative File Path + File Name
     *
     * @throws  TestFrameworkRuntimeException
     *          Encapsulated {@link IOException}. If an I/O error occurred
     *
     * @throws  TestFrameworkRuntimeException
     *          Encapsulated {@link TransformerException}. If an exceptional
     *          condition occured during the transformation process.
     */
    public void writeXMLtoFile(String filePath) {
        long start = System.currentTimeMillis();
        String normalizedPath = addXMLFileType(filePath);
        try {
            DOMSource source = new DOMSource(temporaryXML);
            logger.info("Logging to file: " + normalizedPath);
            File targetFile = getFileByPath(normalizedPath);
            FileOutputStream outputFile = new FileOutputStream(targetFile);
            StreamResult file = new StreamResult(outputFile);
            transformer.transform(source, file); //@CAPT.OBVIOUS: action "write" xml contains to file
        } catch (TransformerException e) {
            throw new TestFrameworkRuntimeException("An error occurred during the transformation", e);
        } catch (IOException e) {
            throw new TestFrameworkRuntimeException("Failed to create file: " + normalizedPath, e);
        }
        long duration = System.currentTimeMillis() - start;
        logger.info("Logging to file: " + normalizedPath + " (done) | time=" + duration + "ms");
    }

}

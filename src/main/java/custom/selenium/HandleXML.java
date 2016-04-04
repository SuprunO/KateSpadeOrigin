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
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Class is used to create XML and handle all needed operations for read, write, parse and format XML.
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class HandleXML {
//TODO: perform clean code refactoring
//TODO: write javadoc for all methods
    private static final Logger logger = Logger.getLogger(TestFactory.class);

    private DocumentBuilder builder;
    private Document temporaryXML;
    private Transformer transformer;
    private static final String FILE_TYPE_XML = ".xml";

    HandleXML() {
        paramLangXML();  // XML initialization
        temporaryXML = builder.newDocument(); //empty xml to work with.
        createTransformer();

    }

    /**
     * Creates {@link javax.xml.transform.Transformer} with xml output setup.
     * Probably will require split option if several simultaneous property sets might be needed
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
     * Setup default Properties to format output XML file.
     */
    private void setXMLDefaults() {
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    }

    public void paramLangXML() {
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        try {
            builder=factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.error(e);
            throw new TestFrameworkRuntimeException(e);
        }
    }

    public void addParentNode(String nodeName) {
        Element node = temporaryXML.createElement(nodeName);
        temporaryXML.appendChild(node);
    }

    public void appendChildNode(String parentNodeName, String childNodeName) {
        Element childNode = temporaryXML.createElement(childNodeName);
        Node parentNode = temporaryXML.getElementsByTagName(parentNodeName).item(0);
        parentNode.appendChild(childNode);
    }

    public void addNodeasFirstChild(String parentNodeName, String childNodeName) {
        Element childNode = temporaryXML.createElement(childNodeName);
        Node parentNode = temporaryXML.getElementsByTagName(parentNodeName).item(0);
        parentNode.insertBefore(childNode, parentNode.getFirstChild());
    }

    public void insertTextToNode(String targetNodeName, String text) {
        Node targetNode = temporaryXML.getElementsByTagName(targetNodeName).item(0);
        targetNode.appendChild(temporaryXML.createTextNode(text));
    }

    public Node getElementByTagName(String tagName) {
        Node targetNode = temporaryXML.getElementsByTagName(tagName).item(0);
        Assert.assertNotNull("<"+ tagName + "> was not found!", targetNode);
        return targetNode;
    }

    public void appendChildNodeWithText(String parentNodeName, String childNodeName, String text) {
        String caller = Thread.currentThread().getStackTrace()[2].toString();
        Assert.assertNotNull("'text' cannot be <code>null</code>! at " + caller, text);
        Node parentNode = getElementByTagName(parentNodeName);
        Element childNode = temporaryXML.createElement(childNodeName);
        childNode.appendChild(temporaryXML.createTextNode(text));
        parentNode.appendChild(childNode);
    }

    /**
     * Creates File by passed filepath and file name, creating passed folder tree if it doesn't exist.
     *
     * @param filePath  String absolute|relative File Path + File Name
     * @return  <code>File</code> Object
     * @throws  IOException
     *          If an I/O error occurred
     * @throws  SecurityException
     *          If a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkWrite(java.lang.String)}</code>
     *          method denies write access to the file
     *
     */
    private static File getFileByPath(String filePath) throws IOException {
        File targetFile = new File(filePath);
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();
        return targetFile;
    }

    /**
     * <p>Checks for the extension in the passed string which expected be: absolute|relative File Path + File Name, Ex.:</p>
     *  <ul>
     *     <li>C:\temp\MyFile</li>
     *     <li>C:\temp\MyFile.xml</li>
     *     <li>./logs/MyFile</li>
     *     <li>logs/MyFile.doc</li>
     *  </ul>
     * <p>See https://en.wikipedia.org/wiki/Path_(computing) to have exhaustive information about file Paths</p>
     *
     * @param fileName    String absolute|relative File Path + File Name
     * @return            True is passed string is file and file name contains "."
     */
    private static boolean isFileTypePresent(String fileName){
        return new File(fileName).isFile() && fileName.contains(".");
    }

    /**
     * <p>adds ".xml" extension to passed string if it extension is absent.</p>
     *
     * @param   fileName  String absolute|relative File Path + File Name
     * @return  File path String
     */
    private static String addXMLFileType(String fileName){
        if(!isFileTypePresent(fileName)){
            return fileName + FILE_TYPE_XML;
        } else {
            return fileName;
        }
    }

    /**
     * Creates and writes the result of xml DOM that stored in {link temporaryXML} in file by passed filePath
     * with adding ".xml" if file extension is absent.
     *
     * @param   filePath String absolute|relative File Path + File Name
     * @throws  TestFrameworkRuntimeException (IOException)
     *          If an I/O error occurred
     * @throws  TestFrameworkRuntimeException (TransformerException)
     *          If an exceptional condition occured during the transformation process.
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

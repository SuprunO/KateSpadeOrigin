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
    private static Logger logger = Logger.getLogger(HandleXML.class);

    private DocumentBuilder builder;
    private Document temporaryXML;
    private Transformer transformer;
    private OutputStream formattedTempXML = new ByteArrayOutputStream();
    private static final String FILE_TYPE_XML = ".xml";

    HandleXML() {
        paramLangXML();  // XML initialization
        temporaryXML = builder.newDocument(); //create empty xml
        initTransformer();

    }

    private void initTransformer() {
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e){
            logger.error(e.getMessage());
        }
        setXMLDefaults();
    }

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
            e.printStackTrace();
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

    private File getFileByPath(String filePath){
        File targetFile = new File(filePath);
        targetFile.getParentFile().mkdirs();
//        try {
            //targetFile.createNewFile();
//        } catch (IOException e){
//            logger.error("No access to the file:" + filePath, e);
//            logger.error(e.getMessage());
//        }
        return targetFile;
    }

    private boolean isFileTypePresent(String fileName){
        return new File(fileName).isFile() && fileName.contains(".");
    }

    private String setXMLFileType(String fileName){
        if(!isFileTypePresent(fileName)){
            return fileName + FILE_TYPE_XML;
        } else {
            return fileName;
        }
    }

    public void writeXMLtoFile(String filePath) {
        long start = System.currentTimeMillis();
        String normalizedPath = setXMLFileType(filePath);
        try {
            DOMSource source = new DOMSource(temporaryXML);

            logger.info("Logging to file: " + normalizedPath);
            File targetFile = getFileByPath(normalizedPath);
            FileOutputStream outputFile = new FileOutputStream(targetFile);
            StreamResult file = new StreamResult(outputFile);
            transformer.transform(source, file);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            logger.error("An error occurred during the transformation", e);
        }
        long duration = System.currentTimeMillis() - start;
        logger.info("Logging to file: " + normalizedPath + " (done) | time=" + duration + "ms");
    }

}

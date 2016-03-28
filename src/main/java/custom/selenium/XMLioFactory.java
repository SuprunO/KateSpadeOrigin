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
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class is used to create Field-sets to fill in inputs and select dropdowns using single function.
 * Field set is supposed to be ArrayList, each element consists of String type("input" or "dropdown"),
 * By locator(locator to find current filed or dropdown) and String value(text or value to select or fill in)
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class XMLioFactory {

    private static Logger logger = Logger.getLogger(XMLioFactory.class);

    DocumentBuilder builder;
    Document temporaryXML;
    Transformer transformXML;

    XMLioFactory() {
        paramLangXML();  // XML initialization
        temporaryXML = builder.newDocument(); //create empty xml

    }

    private void initTransformer() {
        try {
            transformXML = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e){
            logger.error(e.getMessage());
        }

        setTranfromerDefault();
    }

    private void setTranfromerDefault() {
        transformXML.setOutputProperty(OutputKeys.METHOD, "xml");
        transformXML.setOutputProperty(OutputKeys.INDENT, "yes");
        transformXML.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    }

    public void paramLangXML() {
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        try {
            builder=factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Запись настроек в XML файл
     * <?xml version = "1.0"?>
     * <proxy>
     *     <use>true</use>
     *     <host>127.0.0.1</host>
     *     <port>8080</port>
     * </proxy>
     *
     */
    public void WriteParamXML()throws TransformerException, IOException {

        Element rootElement = temporaryXML.createElement("proxy");

        Element nameElementTitle = temporaryXML.createElement("use");
        nameElementTitle.appendChild(temporaryXML.createTextNode("true"));
        rootElement.appendChild(nameElementTitle);

        Element nameElementCompile = temporaryXML.createElement("host");
        nameElementCompile.appendChild(temporaryXML.createTextNode("127.0.0.1"));
        rootElement.appendChild(nameElementCompile);

        Element nameElementRuns = temporaryXML.createElement("port");
        nameElementRuns.appendChild(temporaryXML.createTextNode("8080"));
        rootElement.appendChild(nameElementRuns);

        temporaryXML.appendChild(rootElement);

        writeXMLtoFile("proxy.xml");
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

    public void appendChildNodeWithText(String childNodeName, String text) {
        Element childNode = temporaryXML.createElement(childNodeName);
        Node parentNode = temporaryXML.getDocumentElement();
        parentNode.appendChild(childNode.appendChild(temporaryXML.createTextNode(text)));

    }

    public void appendChildNodeWithText(String parentNodeName, String childNodeName, String text) {
        Element childNode = temporaryXML.createElement(childNodeName);
        Node parentNode = temporaryXML.getElementsByTagName(parentNodeName).item(0);
        parentNode.appendChild(childNode.appendChild(temporaryXML.createTextNode(text)));
    }


    public void writeXMLtoFile(String targetFile) {
        initTransformer();
        try {
            logger.info("Logging to file: " + targetFile);
            transformXML.transform(new DOMSource(temporaryXML), new StreamResult(new FileOutputStream(targetFile)));
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (TransformerException e) {
            logger.error(e.getMessage());
        }
    }

}

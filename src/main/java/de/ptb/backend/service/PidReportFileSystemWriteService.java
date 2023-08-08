/*
Copyright (c) 2023 Physikalisch-Technische Bundesanstalt (PTB), all rights reserved.
This source code and software is free software: you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License as published
by the Free Software Foundation, version 3 of the License.
The software is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.
You should have received a copy of the GNU Lesser General Public License
along with this XSD.  If not, see http://www.gnu.org/licenses.
CONTACT: 		info@ptb.de
DEVELOPMENT:	https://d-si.ptb.de
AUTHORS:		Wafa El Jaoua, Tobias Hoffmann, Clifford Brown, Daniel Hutzschenreuter
LAST MODIFIED:	2023-08-08
*/
package de.ptb.backend.service;

import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.MeasurementResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.List;

public class PidReportFileSystemWriteService {
    String pid;
    List<Participant> participants;
    List<MeasurementResult> mResults;
    String dccTemplatePath = null;
    public PidReportFileSystemWriteService(String pid, List<Participant> participants, List<MeasurementResult> mResults) {
        this.pid = pid;
        this.participants = participants;
        this.mResults = mResults;
        if(System.getProperty("os.name").contains("Windows")) {
            this.dccTemplatePath = "src\\main\\resources\\TestFiles\\DCCTemplate.xml";
        }else{
            this.dccTemplatePath = "DCCTemplate.xml";
        }
    }
    public File writeDataIntoDCC() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, TransformerException {
        File dccFile = new File(this.dccTemplatePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(dccFile);
        doc.getDocumentElement().normalize();
        String content = convertDocumentToString(doc);
        String results = "";
        for (MeasurementResult mResult : mResults){
            results+=mResult;
        }
        content = content.substring(0, content.indexOf("<dcc:measurementResults"))+"<dcc:measurementResults>\n"+results+"</dcc:measurementResults>\n"+content.substring(content.indexOf("</dcc:digitalCalibrationCertificate>"));
        Document newDoc = convertStringToDocument(content);
        //write Participants and unique identifier
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "/digitalCalibrationCertificate/administrativeData/coreData/uniqueIdentifier";
        NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(newDoc, XPathConstants.NODESET);
        for (int idx = 0; idx < nodeList.getLength(); idx++) {
            Node value = nodeList.item(idx);
            value.setTextContent(pid);
        }
        expression = "/digitalCalibrationCertificate/administrativeData/calibrationLaboratory/contact/name";
        nodeList = (NodeList) xpath.compile(expression).evaluate(newDoc, XPathConstants.NODESET);
        for (int idx = 0; idx < nodeList.getLength(); idx++) {
            Node value = nodeList.item(idx);
            value.setTextContent(participants.get(0).getName().substring(1,participants.get(0).getName().length()-1));
        }
        DOMSource source = new DOMSource(newDoc);
        String tmpPath = null;
        if(System.getProperty("os.name").contains("Windows")) {
            tmpPath = "src\\main\\resources\\tmp\\output.xml";
        }else{
            tmpPath = "src/main/resources/tmp/output.xml";
        }
        FileWriter writer = new FileWriter(tmpPath);
        StreamResult result = new StreamResult(writer);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);
        return new File(tmpPath);
    }
    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}

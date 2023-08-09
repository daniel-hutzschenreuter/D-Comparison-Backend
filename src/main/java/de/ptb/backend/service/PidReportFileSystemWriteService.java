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
LAST MODIFIED:	2023-08-09
*/
package de.ptb.backend.service;

import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.MeasurementResult;
import lombok.Data;
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
@Data
public class PidReportFileSystemWriteService {
    String pid;
    List<Participant> participants;
    List<MeasurementResult> mResults;
    String dccTemplatePath;

    /**
     * This class is used to write the previously generated measurement results into a new DCC.
     * @param pid String
     * @param participants List<Participant>
     * @param mResults List<MeasurementResult>
     */
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

    /**
     * This function creates a new DCC file out of the measurement results and a template dcc file.
     * @return File which contains the newly generated Dcc file
     * @throws IOException Throws exception when path to file is nonexistent.
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     * @throws TransformerException
     */
    public File writeDataIntoDCC() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, TransformerException {
        File dccFile = new File(this.dccTemplatePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(dccFile);
        doc.getDocumentElement().normalize();
        String content = convertDocumentToString(doc);
        StringBuilder results = new StringBuilder();
        for (MeasurementResult mResult : mResults){
            results.append(mResult);
        }
        assert content != null;
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
        String tmpPath;
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
    /** This is an auxiliary function to create a xml document from the rewritten string.
     * @param xmlStr String
     * @return Document containing all the information from the string
     */
    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            return builder.parse( new InputSource( new StringReader( xmlStr ) ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This is an auxiliary function to create a string from the template dcc file.
     * @param doc
     * @return String containing the information of the xml file
     */
    private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}

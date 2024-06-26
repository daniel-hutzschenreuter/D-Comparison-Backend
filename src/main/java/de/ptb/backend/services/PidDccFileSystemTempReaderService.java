/*
 * Copyright (c) 2022 - 2023 Physikalisch-Technische Bundesanstalt (PTB), all rights reserved.
 * This source code and software is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, version 3 of the License.
 * The software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this XSD.  If not, see http://www.gnu.org/licenses.
 * CONTACT: 		info@ptb.de
 * DEVELOPMENT:	https://d-si.ptb.de
 * AUTHORS:		Wafa El Jaoua, Tobias Hoffmann, Clifford Brown, Daniel Hutzschenreuter
 * LAST MODIFIED:	29.08.23, 13:26
 */

package de.ptb.backend.services;

import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.*;
import lombok.Data;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;


@Service
@Data
public class PidDccFileSystemTempReaderService implements I_PidDccFileSystemReader {

    DKCRRequestMessage message;
    List<Document> documets = new ArrayList<>();
    /**
     * This function sets the message which is used to read out all the requested DCC files.
     * @param message DKCRRequestMessage which contains all the information from request of the frontend
     */
    @Override
    public void setMessage(DKCRRequestMessage message){
        this.message = message;
    }

    /**
     * This function iterates through the participantList of the Request-message and the dcc files on the DCC_Backend
     * and creates SiReals for every matching name containing the values of the respective dcc file.
     * @return List<SiReal> which contains the mass values of the participant dcc files
     * @throws ParserConfigurationException Throws exception if the DocumentBuilderFactory is not set up properly.
     */

    @Override
    public List<SiReal> readFiles() {return null;}

    @Override
    public void loadFiles()  throws ParserConfigurationException {
        this.documets = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        List<Participant> participants = this.message.getParticipantList();
        for (Participant participant : participants) {
            try {
                String response = restTemplate.getForObject(participant.getDccPid(), String.class, 200);
                byte[] byteBase64 = Base64.getDecoder().decode(response);
                String decodedXml = new String(byteBase64);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();

                Document document = builder.parse(new InputSource(new StringReader(decodedXml)));
                this.documets.add(document);
            } catch (IOException | SAXException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<SiReal> readRadianceTemperature() throws JSONException{
        List<SiReal> siReals = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"basic_measuredValue basic_arithmenticMean temperature_ITS-90\"]/real";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        Double value = Double.valueOf(eElement.getElementsByTagName("si:value").item(0).getTextContent());
                        String unit = eElement.getElementsByTagName("si:unit").item(0).getTextContent();
                        String dateTime = "nicht existent";//eElement.getElementsByTagName("si:dateTime").item(0).getTextContent();
                        Double uncertainty = Double.valueOf(eElement.getElementsByTagName("si:uncertainty").item(0).getTextContent());
                        int coverageFactor = Integer.parseInt(eElement.getElementsByTagName("si:coverageFactor").item(0).getTextContent());
                        Double coverageProbability = Double.valueOf(eElement.getElementsByTagName("si:coverageProbability").item(0).getTextContent());
                        SiExpandedUnc expUnc = new SiExpandedUnc(uncertainty, coverageFactor, coverageProbability);
                        siReals.add(new SiReal(name, value, unit, dateTime, expUnc));
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }

        return siReals;
    }

    @Override
    public List<SiReal> readNominalTemperature() throws JSONException{
        List<SiReal> siReals = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"basic_nominalValue\"]/real";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        Double value = Double.valueOf(eElement.getElementsByTagName("si:value").item(0).getTextContent());
                        String unit = eElement.getElementsByTagName("si:unit").item(0).getTextContent();
                        siReals.add(new SiReal(name, value, unit));
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }
        return siReals;
    }

    @Override
    public List<SiReal> readValueSensor1() throws JSONException{
        List<SiReal> siReals = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"temperature_measuredValueSensor1\"]/real";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        Double value = Double.valueOf(eElement.getElementsByTagName("si:value").item(0).getTextContent());
                        String unit = eElement.getElementsByTagName("si:unit").item(0).getTextContent();
                        String dateTime = "nicht existent";//eElement.getElementsByTagName("si:dateTime").item(0).getTextContent();
                        Double uncertainty = Double.valueOf(eElement.getElementsByTagName("si:uncertainty").item(0).getTextContent());
                        int coverageFactor = Integer.parseInt(eElement.getElementsByTagName("si:coverageFactor").item(0).getTextContent());
                        Double coverageProbability = Double.valueOf(eElement.getElementsByTagName("si:coverageProbability").item(0).getTextContent());
                        SiExpandedUnc expUnc = new SiExpandedUnc(uncertainty, coverageFactor, coverageProbability);
                        siReals.add(new SiReal(name, value, unit, dateTime, expUnc));
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }
        return siReals;
    }

    @Override
    public List<SiReal> readValueSensor2() throws JSONException{

        List<SiReal> siReals = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"temperature_measuredValueSensor2\"]/real";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        Double value = Double.valueOf(eElement.getElementsByTagName("si:value").item(0).getTextContent());
                        String unit = eElement.getElementsByTagName("si:unit").item(0).getTextContent();
                        String dateTime = "nicht existent";//eElement.getElementsByTagName("si:dateTime").item(0).getTextContent();
                        Double uncertainty = Double.valueOf(eElement.getElementsByTagName("si:uncertainty").item(0).getTextContent());
                        int coverageFactor = Integer.parseInt(eElement.getElementsByTagName("si:coverageFactor").item(0).getTextContent());
                        Double coverageProbability = Double.valueOf(eElement.getElementsByTagName("si:coverageProbability").item(0).getTextContent());
                        SiExpandedUnc expUnc = new SiExpandedUnc(uncertainty, coverageFactor, coverageProbability);
                        siReals.add(new SiReal(name, value, unit, dateTime, expUnc));
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }
        return siReals;
    }

    @Override
    public List<SiReal> readIndicatedTemperature() throws JSONException{
        List<SiReal> siReals = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"temperature_indicatedValue\"]/real";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        Double value = Double.valueOf(eElement.getElementsByTagName("si:value").item(0).getTextContent());
                        String unit = eElement.getElementsByTagName("si:unit").item(0).getTextContent();
                        siReals.add(new SiReal(name, value, unit));
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }
        return siReals;
    }

    private static String getNameFromDocument(Document document) throws XPathExpressionException {
        String name = "";
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "/digitalCalibrationCertificate/administrativeData/calibrationLaboratory/contact/name";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                name = eElement.getElementsByTagName("dcc:content").item(0).getTextContent();
            }
        }
        return name;
    }

    // Methods to read in XMLLists
    @Override
    public List<SiRealListXMLList> readNominalTemperatureList() throws JSONException {
        List<SiRealListXMLList> siRealLists = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"basic_nominalValue\"]/realListXMLList";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        SiRealListXMLList SiRealList = buildSiRealListWithoutUncertainty(eElement, name);
                        siRealLists.add(SiRealList);
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }
        return siRealLists;
    }


    @Override
    public List<SiRealListXMLList> readRadianceTemperatureList() throws JSONException{
        List<SiRealListXMLList> siRealLists = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"basic_measuredValue basic_arithmenticMean temperature_ITS-90\"]/realListXMLList";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        SiRealListXMLList siRealList = buildSiRealListWithUncertainty(eElement, name);
                        siRealLists.add(siRealList);
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }

        return siRealLists;
    }

    @Override
    public List<SiRealListXMLList> readValueSensor1List() throws JSONException{
        List<SiRealListXMLList> siRealLists = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"temperature_measuredValueSensor1\"]/realListXMLList";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        SiRealListXMLList siRealList = buildSiRealListWithUncertainty(eElement, name);
                        siRealLists.add(siRealList);
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }

        return siRealLists;
    }

    @Override
    public List<SiRealListXMLList> readValueSensor2List() throws JSONException{
        List<SiRealListXMLList> siRealLists = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"temperature_measuredValueSensor2\"]/realListXMLList";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        SiRealListXMLList siRealList = buildSiRealListWithUncertainty(eElement, name);
                        siRealLists.add(siRealList);
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }

        return siRealLists;
    }

    @Override
    public List<SiRealListXMLList> readIndicatedTemperatureList() throws JSONException {
        List<SiRealListXMLList> siRealLists = new ArrayList<>();
        for (Document document : this.documets) {
            try {
                String name = getNameFromDocument(document);

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"temperature_radianceTemperature\"]/data/quantity[@refType=\"temperature_indicatedValue\"]/realListXMLList";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        SiRealListXMLList SiRealList = buildSiRealListWithoutUncertainty(eElement, name);
                        siRealLists.add(SiRealList);
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }
        return siRealLists;
    }


    private static SiRealListXMLList buildSiRealListWithUncertainty(Element eElement, String name) {
        String valueString = eElement.getElementsByTagName("si:valueXMLList").item(0).getTextContent();
        List<Double> valueDoubleList = convertValueStringToDoubleList(valueString);

        String unit = eElement.getElementsByTagName("si:unitXMLList").item(0).getTextContent();

        String uncString = eElement.getElementsByTagName("si:uncertaintyXMLList").item(0).getTextContent();
        List<Double> uncDoubleList = convertValueStringToDoubleList(uncString);

        int coverageFactor = Integer.parseInt(eElement.getElementsByTagName("si:coverageFactorXMLList").item(0).getTextContent());

        Double coverageProbability = Double.valueOf(eElement.getElementsByTagName("si:coverageProbabilityXMLList").item(0).getTextContent());

        String distribution = eElement.getElementsByTagName("si:distributionXMLList").item(0).getTextContent();

        SiExpandedUncXMLList expUncList = new SiExpandedUncXMLList(uncDoubleList, coverageFactor, coverageProbability, distribution);

        return new SiRealListXMLList(name, valueDoubleList, unit, expUncList);
    }


    private static SiRealListXMLList buildSiRealListWithoutUncertainty(Element eElement, String name) {
        String valueString = eElement.getElementsByTagName("si:valueXMLList").item(0).getTextContent();
        List<Double> valueDoubleList = convertValueStringToDoubleList(valueString);

        String unit = eElement.getElementsByTagName("si:unitXMLList").item(0).getTextContent();

        return new SiRealListXMLList(name, valueDoubleList, unit);

    }

    private static List<Double> convertValueStringToDoubleList(String valueString) {
        List<String> StringList = Arrays.asList(valueString.split("\\s+"));
        List<Double> valueDoubleList = new ArrayList<>(StringList.size());
        for (String s : StringList) {
            valueDoubleList.add(Double.valueOf(s));
        }
        return valueDoubleList;
    }
}


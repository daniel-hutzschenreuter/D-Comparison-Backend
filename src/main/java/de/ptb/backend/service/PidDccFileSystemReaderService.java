package de.ptb.backend.service;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PidDccFileSystemReaderService {
    String path = null;
    DKCRRequestMessage message;

    public PidDccFileSystemReaderService(DKCRRequestMessage message) {
        this.message = message;
        if(System.getProperty("os.name").contains("Windows")){
            this.path = "src\\main\\resources\\DCCFiles";
        }else{
            this.path = "DCCFiles";
        }
    }

    public List<SiReal> readFiles() throws ParserConfigurationException {
        List<SiReal> siReals = new ArrayList<>();
        File directory = new File(this.path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        for(File file: Objects.requireNonNull(directory.listFiles())) {
            for(Participant participant: this.message.getParticipantList()){
                if(Objects.equals(file.getName().substring(0,file.getName().length()-4), participant.getDccPid().substring(1,participant.getDccPid().length()-1))){
                    try {
                        Document doc = db.parse(file);
                        doc.getDocumentElement().normalize();
                        XPath xPath =  XPathFactory.newInstance().newXPath();
                        String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"participant_mass\"]/data/quantity[@refType=\"Kilogram\"]/real";
                        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
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
                                siReals.add(new SiReal(value, unit, dateTime, expUnc));
                            }
                        }
                    } catch (SAXException | XPathExpressionException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return siReals;
    }
}

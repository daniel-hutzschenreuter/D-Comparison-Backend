package de.ptb.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.ptb.backend.BERT.DIR;
import de.ptb.backend.BERT.RunResult;
import de.ptb.backend.BERT.RunfDKCR;
import de.ptb.backend.BERT.fDKCR;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.DKCRResponseMessage;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.SiConstant;
import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.formula.EEqualsMC2;
import de.ptb.backend.service.PidDccFileSystemReaderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
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
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api")
public class BackendController {
    final String dConstantUrl = "http://localhost:8082/api/";
    final String dccPath = "src/main/resources/TestFiles/DCCOutputXML.xml";
    @GetMapping("/sayHello")
    public String sayHelloWorld() {
        return "Hello World!";
    }


    @PostMapping("/keyComparison")
    public String evaluateDKCR(@RequestBody JsonNode payload) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        JsonNode data = payload.get("keyComparisonData");
        String pidReport = data.get("pidReport").toString();
        List<Participant> participantList = new ArrayList<>();
        for (JsonNode participant : data.get("participantList")) {
            participant = participant.get("participant");
            participantList.add(new Participant(participant.get("name").toString(), participant.get("pidDCC").toString()));
        }
        DKCRRequestMessage request = new DKCRRequestMessage(pidReport, participantList);
        PidDccFileSystemReaderService reader = new PidDccFileSystemReaderService(request);
        List<SiReal> SiReals = reader.readFiles();
        System.out.println(SiReals);
        SiConstant speedOfLight = getSpeedOfLight();
        System.out.println(speedOfLight);

        System.out.println("Test E=mcc");
        EEqualsMC2 equalsMC2 = new EEqualsMC2(speedOfLight, SiReals);
        System.out.println("Calculate 1.");
        System.out.println(equalsMC2.calculate(SiReals.get(0).getValue()).toString());
        System.out.println("Calculate All");
        List<SiReal> ergebnisse = equalsMC2.calculate();
        for (SiReal ergebnis : ergebnisse) {
            System.out.println(ergebnis.toString());
        }
        fDKCR fdkcr = new fDKCR();
        RunfDKCR objRunfDKCR = new RunfDKCR();
        Vector<DIR> inputs = new Vector<>();
        for (SiReal siReal : SiReals) {
            DIR sirealAsDIR = new DIR(siReal.getValue(), siReal.getExpUnc().getUncertainty());
            inputs.add(sirealAsDIR);
        }
        int result1 = objRunfDKCR.ReadData();
        int result2 = objRunfDKCR.ReadDKRCContributions();
        fdkcr.setData(objRunfDKCR.getDKCRTitle(), objRunfDKCR.getDKCRID(), objRunfDKCR.getNTotalContributions(), objRunfDKCR.getPilotOrganisationID(),objRunfDKCR.getDKCRDimension(), objRunfDKCR.getDKCRUnit(), SiReals.size(), inputs, objRunfDKCR.getRunResults());
        objRunfDKCR.setNr(fdkcr.processDKCR());
        Vector<RunResult> Results = objRunfDKCR.getRunResults();
        System.out.println(Results);
        DKCRResponseMessage response = new DKCRResponseMessage(pidReport, writeDataIntoDCC(SiReals, Results));
        return response.toString();
    }
    public SiConstant getSpeedOfLight() {
        final String url = dConstantUrl+"si:speed_of_light_vacuum:2019";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);
        assert result != null;
        result = result.substring(2, result.length() - 2);
        result = result.replaceAll("\"", "");
        String[] resultValues = result.split(",");
        String[] constantValues = new String[11];
        int count = 0;
        for (String resultValue : resultValues) {
            String rValue = resultValue.substring(resultValue.indexOf(":")+1);
            constantValues[count]=rValue;
            count++;
        }
        return new SiConstant(constantValues[0], constantValues[1], Objects.equals(constantValues[2], "true"),constantValues[4],constantValues[5],Double.parseDouble(constantValues[6]),constantValues[7],constantValues[8],Integer.parseInt(constantValues[9]),constantValues[10]);
    }
    public File writeDataIntoDCC(List<SiReal> enSiReals, Vector<RunResult> kcResults) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        File dccFile = new File(dccPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(dccFile);
        doc.getDocumentElement().normalize();

        XPath xPather =  XPathFactory.newInstance().newXPath();
        String expression = "/digitalCalibrationCertificate/measurementResults";
        NodeList measurementResults = (NodeList) xPather.compile(expression).evaluate(doc, XPathConstants.NODESET);
        for(int k = 0; k < measurementResults.getLength(); k++){
            Node measurementResult = measurementResults.item(k);

        }
        return dccFile;
    }
}
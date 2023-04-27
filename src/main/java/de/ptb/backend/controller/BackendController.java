package de.ptb.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.ptb.backend.BERT.DIR;
import de.ptb.backend.BERT.RunResult;
import de.ptb.backend.BERT.RunfDKCR;
import de.ptb.backend.BERT.fDKCR;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.DKCRResponseMessage;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.MeasurementResult;
import de.ptb.backend.model.dsi.SiConstant;
import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.formula.EEqualsMC2;
import de.ptb.backend.service.PidDccFileSystemReaderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api")
public class BackendController {
    final String dConstantUrl = "http://localhost:8082/api/";
    final String dccPath = "src/main/resources/TestFiles/DCCTemplate.xml";
    @GetMapping("/sayHello")
    public String sayHelloWorld() {
        return "Hello World!";
    }


    @PostMapping("/keyComparison")
    public String evaluateDKCR(@RequestBody JsonNode payload) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
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
        System.out.println(SiReals.get(0).getValue());
        manipulateMassValues(SiReals, -1.0);
        System.out.println(SiReals.get(0).getValue());
        SiConstant speedOfLight = getSpeedOfLight();
        EEqualsMC2 equalsMC2 = new EEqualsMC2(speedOfLight, SiReals);
        List<SiReal> ergebnisse = equalsMC2.calculate();
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
        List<MeasurementResult> mResults = generateMResults(SiReals, Results, ergebnisse);
        DKCRResponseMessage response = new DKCRResponseMessage(pidReport, writeDataIntoDCC(mResults));
        return response.toString();
    }
    public SiConstant getSpeedOfLight() {
        final String url = dConstantUrl+"si:speed_of_light_vacuum:2019";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
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
    public File writeDataIntoDCC(List<MeasurementResult> mResults) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, TransformerException {
        File dccFile = new File(dccPath);
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
        content = content.substring(0, content.indexOf("<dcc:measurementResults"))+"<dcc:measurementResults>"+results+"</dcc:measurementResults>"+content.substring(content.indexOf("</dcc:digitalCalibrationCertificate>"));
        Document newDoc = convertStringToDocument(content);
        DOMSource source = new DOMSource(newDoc);
        FileWriter writer = new FileWriter(new File("src/main/resources//tmp/output.xml"));
        StreamResult result = new StreamResult(writer);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);
        return new File("src/main/resources//tmp/output.xml");
    }

    public List<MeasurementResult> generateMResults(List<SiReal> mass, Vector<RunResult> enKcValues, List<SiReal> energy){
        List<MeasurementResult> results = new ArrayList<>();
        RunResult runResult = enKcValues.get(0);
        for(int i = 0; i<mass.size(); i++){
            results.add(new MeasurementResult(mass.get(i), runResult.getxRef(), runResult.getEOResults().get(i).getEquivalenceValue(), energy.get(i)));
        }
        results.add(new MeasurementResult(mass.get(0),runResult.getxRef(),energy.get(0)));
        return results;
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
    private static void manipulateMassValues(List<SiReal> siReals, Double manipulator){
        for (SiReal real: siReals){
            real.manipulateValue(manipulator);
        }
    }
}
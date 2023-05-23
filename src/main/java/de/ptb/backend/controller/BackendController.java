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
import de.ptb.backend.service.PidReportFileSystemWriteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
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
    @GetMapping("/sayHello")
    public String sayHelloWorld() {
        return "Hello World!";
    }

    @PostMapping("/keyComparison")
    public String evaluateDKCR(@RequestBody JsonNode payload) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
        JsonNode data = payload.get("keyComparisonData");
        String pidReport = data.get("pidReport").toString().substring(1,data.get("pidReport").toString().length()-1);
        List<Participant> participantList = new ArrayList<>();
        for (JsonNode participant : data.get("participantList")) {
            participant = participant.get("participant");
            participantList.add(new Participant(participant.get("name").toString(), participant.get("pidDCC").toString()));
        }
        DKCRRequestMessage request = new DKCRRequestMessage(pidReport, participantList);
        PidDccFileSystemReaderService reader = new PidDccFileSystemReaderService(request);
        List<SiReal> SiReals = reader.readFiles();
        manipulateMassValues(SiReals, -1.0);
        SiConstant speedOfLight = getSpeedOfLight();
        EEqualsMC2 equalsMC = new EEqualsMC2(speedOfLight, SiReals);
        List<SiReal> ergebnisse = equalsMC.calculate();
        fDKCR fdkcr = new fDKCR();
        RunfDKCR objRunfDKCR = new RunfDKCR();
        Vector<DIR> inputs = new Vector<>();
        for (SiReal ergebnis : ergebnisse) {
            DIR sirealAsDIR = new DIR(ergebnis.getValue(), ergebnis.getExpUnc().getUncertainty());
            inputs.add(sirealAsDIR);
        }
        objRunfDKCR.ReadData();
        objRunfDKCR.ReadDKRCContributions();
        fdkcr.setData(objRunfDKCR.getDKCRTitle(), objRunfDKCR.getDKCRID(), objRunfDKCR.getNTotalContributions(), objRunfDKCR.getPilotOrganisationID(),objRunfDKCR.getDKCRDimension(), objRunfDKCR.getDKCRUnit(), SiReals.size(), inputs, objRunfDKCR.getRunResults());
        objRunfDKCR.setNr(fdkcr.processDKCR());
        Vector<RunResult> Results = objRunfDKCR.getRunResults();
        SiReal kcVal = equalsMC.calculate(Results.get(0).getxRef());
        List<MeasurementResult> mResults = generateMResults(SiReals, Results, kcVal, ergebnisse);
        PidReportFileSystemWriteService dccWriter = new PidReportFileSystemWriteService(pidReport, participantList, mResults);
        DKCRResponseMessage response = new DKCRResponseMessage(pidReport, dccWriter.writeDataIntoDCC());
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

    public List<MeasurementResult> generateMResults(List<SiReal> mass, Vector<RunResult> enMassValues, SiReal kcValue, List<SiReal> energy){
        List<MeasurementResult> results = new ArrayList<>();
        RunResult runResult = enMassValues.get(0);
        for(int i = 0; i<mass.size(); i++){
            results.add(new MeasurementResult(mass.get(i), runResult.getxRef(), kcValue, runResult.getEOResults().get(i).getEquivalenceValue(), energy.get(i)));
        }
        return results;
    }
    private static void manipulateMassValues(List<SiReal> siReals, Double manipulator){
        for (SiReal real: siReals){
            real.manipulateValue(manipulator);
        }
    }
}
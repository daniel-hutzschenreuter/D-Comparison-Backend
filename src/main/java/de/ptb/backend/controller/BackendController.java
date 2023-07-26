package de.ptb.backend.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.ptb.backend.BERT.*;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.DKCRResponseMessage;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.MeasurementResult;
import de.ptb.backend.model.dsi.SiConstant;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.formula.EEqualsMC2;
import de.ptb.backend.service.PidConstantWebReaderService;
import de.ptb.backend.service.PidDccFileSystemReaderService;
import de.ptb.backend.service.PidReportFileSystemWriteService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/d-comparison")
public class BackendController {
    @GetMapping("/sayHello")
    public String sayHelloWorld() {
        return "Hello World!";
    }

    @PostMapping("/evaluateComparison")
    public ResponseEntity<DKCRResponseMessage> evaluateDKCR(@RequestBody JsonNode payload) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
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
        PidConstantWebReaderService speedOfLightWebReader = new PidConstantWebReaderService("speedOfLightInVacuum2018");
        SiConstant speedOfLight = speedOfLightWebReader.getConstant();
        EEqualsMC2 equalsMC = new EEqualsMC2(speedOfLight, SiReals);
        List<SiReal> ergebnisse = equalsMC.calculate();
        System.out.println(SiReals);
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
        SiReal kcVal = equalsMC.calculate(new SiReal(Results.get(0).getxRef(), "//joule", "", new SiExpandedUnc(0.0, 0, 0.0)));
        DKCR grubsTestDKCR = new DKCR(inputs);
        double mean = grubsTestDKCR.CalcMean();
        double stddev = grubsTestDKCR.CalcStdDev(mean);
        Vector<GRunResult> gRunResults = grubsTestDKCR.ProcessGrubsDKCR(mean, stddev);
        List<MeasurementResult> mResults = generateMResults(SiReals, Results, kcVal, ergebnisse, gRunResults);
        mResults.add(new MeasurementResult(SiReals.get(0).getMassDifference(),Results.get(0).getxRef(), kcVal, gRunResults.get(0).getxRef(), gRunResults.get(0).getURef()));
        PidReportFileSystemWriteService dccWriter = new PidReportFileSystemWriteService(pidReport, participantList, mResults);
        DKCRResponseMessage response = new DKCRResponseMessage(pidReport, dccWriter.writeDataIntoDCC());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/converterKCDB")
    public ResponseEntity<String> converterKCDB(@RequestBody JsonNode payload){
        return new ResponseEntity<>("KCDB-Beispielausgabe", HttpStatus.OK);
    }

    public List<MeasurementResult> generateMResults(List<SiReal> mass, Vector<RunResult> enMassValues, SiReal kcValue, List<SiReal> energy, Vector<GRunResult> grubsValues){
        List<MeasurementResult> results = new ArrayList<>();
        RunResult runResult = enMassValues.get(0);
        for(int i = 0; i<mass.size(); i++){
            results.add(new MeasurementResult(mass.get(i), runResult.getxRef(), kcValue, runResult.getEOResults().get(i).getEquivalenceValue(), energy.get(i), grubsValues.get(0).getGEOResults().get(i)));
        }
        return results;
    }
    private static void manipulateMassValues(List<SiReal> siReals, Double manipulator){
        for (SiReal real: siReals){
            real.manipulateValue(manipulator);
        }
    }
}
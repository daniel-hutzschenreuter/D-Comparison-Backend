package de.ptb.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.SiConstant;
import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.formula.EEqualsMC2;
import de.ptb.backend.service.PidDccFileSystemReaderService;
import lombok.AllArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api")
public class BackendController {
    @GetMapping("/sayHello")
    public String sayHelloWorld() {
        return "Hello World!";
    }


    @PostMapping("/keyComparison")
    public String evaluateDKCR(@RequestBody JsonNode payload) throws ParserConfigurationException, IOException, SAXException, JSONException {
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
        return null;
    }
    @PostMapping("/getSpeedOfLight")
    public SiConstant getSpeedOfLight() {
        final String url = "http://localhost:8082/api/si:definingconstant:planck_constant:2019";
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

}
package de.ptb.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.FundamentalConstant;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.formula.EEqualsMC2;
import de.ptb.backend.repository.FundamentalConstantRepository;
import de.ptb.backend.service.PidDccFileSystemReaderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api")
public class BackendController {
    private FundamentalConstantRepository fundamentalConstantRepository;
    @GetMapping("/sayHello")
    public String sayHelloWorld(){
        return "Hello World!";
    }


    @PostMapping("/keyComparison")
    public String evaluateDKCR(@RequestBody JsonNode payload) throws ParserConfigurationException, IOException, SAXException {
        JsonNode data = payload.get("keyComparisonData");
        String pidReport = data.get("pidReport").toString();
        List<Participant> participantList = new ArrayList<>();
        for(JsonNode participant: data.get("participantList")){
            participant = participant.get("participant");
            participantList.add(new Participant(participant.get("name").toString(),participant.get("pidDCC").toString()));
        }
        DKCRRequestMessage request = new DKCRRequestMessage(pidReport,participantList);
        PidDccFileSystemReaderService reader = new PidDccFileSystemReaderService(request);
        List<SiReal> SiReals = reader.readFiles();
        System.out.println(SiReals);
        FundamentalConstant speedOfLight = getSpeedOfLight();
        System.out.println(speedOfLight);

        System.out.println("Test E=mcc");
        EEqualsMC2 equalsMC2 = new EEqualsMC2(speedOfLight, SiReals);
        System.out.println("Calculate 1.");
        System.out.println(equalsMC2.calculate(SiReals.get(0).getValue()).toString());
        System.out.println("Calculate All");
        List<SiReal> ergebnisse = equalsMC2.calculate();
        for(SiReal ergebnis: ergebnisse){
            System.out.println(ergebnis.toString());
        }
        return null;
    }

    public FundamentalConstant getSpeedOfLight(){
        List<FundamentalConstant> fundamentalConstantList = getAllFundamentalConstants();
        FundamentalConstant speedOfLight=null;
        for(FundamentalConstant fConstant: fundamentalConstantList){
            if(fConstant.getPid().equals("si:speed_of_light_vacuum:2019")){
                speedOfLight=fConstant;
            }
        }
        return speedOfLight;
    }
    @GetMapping("/fundametalconstants")
    public List<FundamentalConstant> getAllFundamentalConstants(){
        return fundamentalConstantRepository.findAll();
    }
}
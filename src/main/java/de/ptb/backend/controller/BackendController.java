package de.ptb.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.SiReal;
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
    @PostMapping("/sayHello")
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
        //DKCRResponseMessage response = new DKCRResponseMessage(pidReport, dccFile);
        return null;
    }
}
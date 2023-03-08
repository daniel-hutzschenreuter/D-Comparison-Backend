package de.ptb.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.Participant;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api")
public class BackendController {
    @GetMapping("/sayHello")
    public String sayHelloWorld(){
        return "Hello World!";
    }

    @PostMapping("/keyComparison")
    public String evaluateDKCR(@RequestBody JsonNode payload){
        JsonNode data = payload.get("keyComparisonData");
        String pidReport = data.get("pidReport").toString();
        List<Participant> participantList = new ArrayList<>();
        for(JsonNode participant: data.get("participantList")){
            participant = participant.get("participant");
            participantList.add(new Participant(participant.get("name").toString(),participant.get("pidDCC").toString()));
        }
        DKCRRequestMessage request = new DKCRRequestMessage(pidReport,participantList);



        return null;
    }
}
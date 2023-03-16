package de.ptb.backend.service;

import de.ptb.backend.model.Participant;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.Report;
import de.ptb.backend.repository.ParticipantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
@Slf4j
public class ParticipantServiceImpl implements ParticipantService{
    ParticipantRepository participantRepository;

    @Override
    public List<Participant> getParticipantList() {

        return participantRepository.getAllParticipants() ;
    }

    @Override
    public Report getReport() {
        return participantRepository.getReport();
    }


    @Override
    public String delete(int id) {
        participantRepository.delete(id);
        return "participant removed !! " + id;
    }

    @Override
    public Participant addParticipant(Participant participant) {
        return  participantRepository.addParticipant(participant);
    }

    @Override
    public Participant update(Participant participant) {
        return participantRepository.update(participant);
    }

    @Override
    public Report addReport(Report report) {
        return participantRepository.addReport(report);
    }

//    @Override
//    public String encode(String value) {
//        return  Base64.encodeBytes(value!=null?value.getBytes():null);
//    }
//
//    @Override
//    public String decode(String base64) {
//        try {
//            byte[] value = Base64.decode(base64);
//            return  new String(value);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}

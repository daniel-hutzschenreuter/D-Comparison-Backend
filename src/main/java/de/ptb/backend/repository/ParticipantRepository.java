package de.ptb.backend.repository;

import de.ptb.backend.model.Participant;
import de.ptb.backend.model.Report;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ParticipantRepository{
    public List<Participant> participantList =new ArrayList<Participant>();
    public Report report = new Report();

    public Report getReport() {
        return report;
    }


    public List<Participant> getAllParticipants(){
        return participantList;
    }

//    public Participant findById(int id){
//        for (int i = 0; i < participantList.size(); i++) {
//            if (participantList.get(i).getId() == (id)) {
//                return participantList.get(i);
//            }
//        }
//        return null;
//    }
    public String delete(Integer id) {
        participantList.removeIf(x -> x.getId() == (id));
        return null;
    }

    public Participant update(Participant p) {
        int idx = 0;
        int id = 0;
        for (int i = 0; i < participantList.size(); i++) {
            if (participantList.get(i).getId() == (p.getId())) {
                id = p.getId();
                idx = i;
                break;
            }
        }
        Participant participant = new Participant();
        participant.setId(p.getId());
        participant.setName(p.getName());
        participant.setDccPid(p.getDccPid());
        participantList.add(participant);
        return participant;
    }

    public Participant addParticipant(Participant p) {
        Participant participant = new Participant();
        participant.setId(p.getId());
        participant.setName(p.getName());
        participant.setDccPid(p.getDccPid());
        participantList.add(participant);
        return participant;
    }
    public Report addReport(Report r) {
        Report report= new Report();
        report.setPidReport(r.getPidReport());
        report.setParticipantList(r.getParticipantList());
        return report;
    }
}

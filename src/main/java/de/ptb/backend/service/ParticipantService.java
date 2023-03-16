package de.ptb.backend.service;



import de.ptb.backend.model.Participant;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.Report;

import java.util.List;

public interface ParticipantService {
    List<Participant> getParticipantList();
    Report getReport();

    public String delete(int id);
    public Participant addParticipant(Participant participant);
    public Participant update(Participant p);
    public Report addReport(Report report);
//    public String encode(String value);
//    public String decode(String base64);
}

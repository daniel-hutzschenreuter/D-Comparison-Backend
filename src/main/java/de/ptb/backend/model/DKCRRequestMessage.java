package de.ptb.backend.model;

import java.util.List;

public class DKCRRequestMessage {
    String pidReport;
    List<Participant> participantList;

    public DKCRRequestMessage(String pidReport, List<Participant> participantList) {
        this.pidReport = pidReport;
        this.participantList = participantList;
    }

    public String getPidReport() {
        return pidReport;
    }

    public void setPidReport(String pidReport) {
        this.pidReport = pidReport;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
    }
}

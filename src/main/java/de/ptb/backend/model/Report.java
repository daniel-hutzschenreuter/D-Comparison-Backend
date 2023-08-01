package de.ptb.backend.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class Report  implements Serializable {
//    static int instanceCounter=0;
//    private  int id=0;
    private String pidReport;
    private List<Participant> participantList;
    public Report() {
//        instanceCounter++;
//        id=instanceCounter;
    }

}

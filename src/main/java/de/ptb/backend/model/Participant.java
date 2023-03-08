package de.ptb.backend.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Participant implements Serializable {
    static int instanceCounter=0;
    private  int id=0;
    private  String name;
    private String  dccPid;
    public Participant() {
        instanceCounter++;
        id=instanceCounter;
    }

    public Participant(String name, String dccPid) {
        this.name = name;
        this.dccPid = dccPid;
    }
}

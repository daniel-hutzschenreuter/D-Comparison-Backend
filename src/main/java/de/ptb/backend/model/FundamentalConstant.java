package de.ptb.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "codataapi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FundamentalConstant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String pid;
    private String bipmPID;
    private Boolean dSIApproved;
    private String label;
    private String quantityType;
    private Double value;
    private String unit;
    @Temporal(TIMESTAMP)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateTime;
    private int uncertainty;
    private String distribution;
}

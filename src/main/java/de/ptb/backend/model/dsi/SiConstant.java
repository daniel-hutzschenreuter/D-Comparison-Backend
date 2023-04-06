package de.ptb.backend.model.dsi;

import org.springframework.format.annotation.DateTimeFormat;

public class SiConstant {
    private final String pid;
    private final String bipmPID;
    private final Boolean dSIApproved;
    private final String label;
    private final String quantityType;
    private final Double value;
    private final String unit;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private String dateTime;
    private final int uncertainty;
    private final String distribution;

    public SiConstant(String pid, String bipmPID, Boolean dSIApproved, String label, String quantityType, Double value, String unit, String dateTime, int uncertainty, String distribution) {
        this.pid = pid;
        this.bipmPID = bipmPID;
        this.dSIApproved = dSIApproved;
        this.label = label;
        this.quantityType = quantityType;
        this.value = value;
        this.unit = unit;
        this.dateTime = dateTime;
        this.uncertainty = uncertainty;
        this.distribution = distribution;
    }

    @Override
    public String toString() {
        return "SiConstant{" +
                "pid='" + pid + '\'' +
                ", bipmPID='" + bipmPID + '\'' +
                ", dSIApproved=" + dSIApproved +
                ", label='" + label + '\'' +
                ", quantityType='" + quantityType + '\'' +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", uncertainty=" + uncertainty +
                ", distribution='" + distribution + '\'' +
                '}';
    }

    public String getPid() {
        return pid;
    }

    public String getBipmPID() {
        return bipmPID;
    }

    public Boolean getdSIApproved() {
        return dSIApproved;
    }

    public String getLabel() {
        return label;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public Double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getUncertainty() {
        return uncertainty;
    }

    public String getDistribution() {
        return distribution;
    }
}

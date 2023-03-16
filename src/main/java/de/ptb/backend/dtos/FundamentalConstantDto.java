
package de.ptb.backend.dtos;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@XmlRootElement(name = "fundamentalConstant", namespace = NAMESPACES.CDT)
@XmlType( propOrder = { "pid", "bipmPID", "dSIApproved", "constant","label", "quantityType","value", "unit", "dateTime", "uncertainty", "distribution"})
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FundamentalConstantDto {
    private String pid;
    private String bipmPID;
    private Boolean dSIApproved;
    private ConstantDto constant;

    private String label;
    private String quantityType;
    private Double value;
    private String unit;
    private Date dateTime;
    private int uncertainty;
    private String distribution;

    public String getLabel() {
        return label;
    }

    @XmlElement
    public void setLabel(String label) {
        this.label = label;
    }

    public String getQuantityType() {
        return quantityType;
    }

    @XmlElement
    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public Double getValue() {
        return value;
    }

    @XmlElement
    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    @XmlElement
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getDateTime() {
        return dateTime;
    }

    @XmlElement
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getUncertainty() {
        return uncertainty;
    }

    @XmlElement
    public void setUncertainty(int uncertainty) {
        this.uncertainty = uncertainty;
    }

    public String getDistribution() {
        return distribution;
    }

    @XmlElement
    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getPid() {
        return pid;
    }

    @XmlElement
    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBipmPID() {
        return bipmPID;
    }

    @XmlElement
    public void setBipmPID(String bipmPID) {
        this.bipmPID = bipmPID;
    }

    public Boolean getdSIApproved() {
        return dSIApproved;
    }

    @XmlElement
    public void setdSIApproved(Boolean dSIApproved) {
        this.dSIApproved = dSIApproved;
    }

    public ConstantDto getConstant() {
        return constant;
    }

    @XmlElement
    public void setConstant(ConstantDto constant) {
        this.constant = constant;
    }
}

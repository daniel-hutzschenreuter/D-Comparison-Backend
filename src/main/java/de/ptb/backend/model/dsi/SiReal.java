package de.ptb.backend.model.dsi;

public class SiReal {
    Double value;
    String unit;
    String dateTime;
    SiExpandedUnc expUnc;
    Double massDifference = 0.0;
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public SiExpandedUnc getExpUnc() {
        return expUnc;
    }

    public void setExpUnc(SiExpandedUnc expUnc) {
        this.expUnc = expUnc;
    }

    public Double getMassDifference() {
        return massDifference;
    }

    public SiReal(Double value, String unit, String dateTime, SiExpandedUnc expUnc) {
        this.value = value;
        this.unit = unit;
        this.dateTime = dateTime;
        this.expUnc = expUnc;
    }

    public void manipulateValue(Double manipulator){
        this.massDifference=manipulator;
        this.value+=manipulator;
    }

    @Override
    public String toString() {
        return "SiReal{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", expUnc=" + expUnc.toString() +
                '}';
    }
}

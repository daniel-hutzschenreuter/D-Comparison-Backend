package de.ptb.backend.model.dsi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class SiRealListXMLList {
    String name;
    List<Double> value;
    String unit;
    List<String> dateTime;
    SiExpandedUncXMLList expUnc;

    public SiRealListXMLList(String name, List<Double> valueList, String unit, List<String> dateTimeList,
                             SiExpandedUncXMLList expUncXMLList) {
        this.name = name;
        this.value = valueList;
        this.unit = unit;
        this.dateTime = dateTimeList;
        this.expUnc = expUncXMLList;
    }

    public SiRealListXMLList(String name, List<Double> valueList, String unit, SiExpandedUncXMLList expUncXMLList) {
        this.name = name;
        this.value = valueList;
        this.unit = unit;
        this.expUnc = expUncXMLList;
    }

    public SiRealListXMLList(String name, List<Double> valueList, String unit) {
        this.name = name;
        this.value = valueList;
        this.unit = unit;
    }

    public SiRealListXMLList(List<Double> value, String unit, SiExpandedUncXMLList expUncXMLList) {
        this.value = value;
        this.unit = unit;
        this.expUnc = expUncXMLList;
    }

}

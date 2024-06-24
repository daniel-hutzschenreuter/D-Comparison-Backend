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
    List<Double> values;
    String unit;
    List<String> dateTimes;
    SiExpandedUncXMLList expUncList;

    public SiRealListXMLList(String name, List<Double> valueList, String unit, List<String> dateTimeList,
                             SiExpandedUncXMLList expUncXMLList) {
        this.name = name;
        this.values = valueList;
        this.unit = unit;
        this.dateTimes = dateTimeList;
        this.expUncList = expUncXMLList;
    }

    public SiRealListXMLList(String name, List<Double> valueList, String unit, SiExpandedUncXMLList expUncXMLList) {
        this.name = name;
        this.values = valueList;
        this.unit = unit;
        this.expUncList = expUncXMLList;
    }

    public SiRealListXMLList(String name, List<Double> valueList, String unit) {
        this.name = name;
        this.values = valueList;
        this.unit = unit;
    }

    public SiRealListXMLList(List<Double> value, String unit, SiExpandedUncXMLList expUncXMLList) {
        this.values = value;
        this.unit = unit;
        this.expUncList = expUncXMLList;
    }

}

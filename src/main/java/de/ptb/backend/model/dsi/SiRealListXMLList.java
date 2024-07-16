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
    String labelList;

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

    public SiRealListXMLList(String name, String labelList, List<Double> valueList, String unit) {
        this.name = name;
        this.labelList = labelList;
        this.values = valueList;
        this.unit = unit;
    }

    public SiRealListXMLList(List<Double> valueList, String labelList,  String unit) {
        this.labelList = labelList;
        this.values = valueList;
        this.unit = unit;
    }


    public SiRealListXMLList(List<Double> value, String unit, SiExpandedUncXMLList expUncXMLList) {
        this.values = value;
        this.unit = unit;
        this.expUncList = expUncXMLList;
    }

    public String toXMLString(){
        // Check if Label List ist available
        String labelListString = labelList != null ? "<si:labelXMLList>" + labelList + "</si:labelXMLList>\n" : "";

        // Format the Value String
        String valueString = values.toString().replaceAll("[,]","");
        valueString = valueString.substring(1, valueString.length()-1);

        // Check if Uncertainty is available
        String uncString = expUncList != null ? expUncList.toXMLString() : "";

        // Printout XML String
        String XMLstring =
                "<si:realListXMLList>\n" +
                labelListString +
                "<si:valueXMLList>" + valueString + "</si:valueXMLList>\n" +
                "<si:unitXMLList>"+ this.unit + "</si:unitXMLList>\n" +
                uncString +
                "</si:realListXMLList>\n";
        return XMLstring;
    }
}

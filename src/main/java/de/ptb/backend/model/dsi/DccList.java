package de.ptb.backend.model.dsi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DccList {
    List<DccQuantity> quantities = new ArrayList<>();
    DccMeasurementMetaData measurementMetaData;

    public void addQuantity(DccQuantity quantity){
        this.quantities.add(quantity);
    }

    public String toXMLString(){
        StringBuilder XMLString = new StringBuilder();

        for (DccQuantity quantity : quantities){
            XMLString.append(quantity.toXMLString());
        }

        String measurementMetaDataString = this.measurementMetaData != null ? measurementMetaData.toXMLString() : "";

        return "<dcc:list>\n" +
                XMLString +
                measurementMetaDataString +
                "</dcc:list>\n";
    }
}

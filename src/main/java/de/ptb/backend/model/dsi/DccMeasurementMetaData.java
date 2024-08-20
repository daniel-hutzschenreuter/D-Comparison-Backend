package de.ptb.backend.model.dsi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DccMeasurementMetaData {
    DccMetaData metaData;

    public String toXMLString(){
        return "<dcc:measurementMetaData>" + this.metaData.toXMLString() + "</dcc:measurementMetaData>\n";
    }
}

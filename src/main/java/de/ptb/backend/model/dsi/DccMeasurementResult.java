package de.ptb.backend.model.dsi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DccMeasurementResult {
    String refType;
    String refID;
    DccName name;
    String id;
    DccResults results;

    public DccMeasurementResult(String id, String refID, DccName name, DccResults results){
        this.id = id;
        this.refID = refID;
        this.name = name;
        this.results = results;
    }
    public DccMeasurementResult(String id, DccName name, DccResults results){
        this.id = id;
        this.name = name;
        this.results = results;
    }

    public DccMeasurementResult(String id, DccName name){
        this.id = id;
        this.name = name;
    }

    public String toXMLString(){
        String refTypeString = this.refType != null ? " refType=\"" + this.refType + "\"" : "";
        String refIDString = this.refID != null ? " refId=\"" + this.refID + "\"" : "";
        String idString = this.id != null ? " id=\"" + this.id + "\"" : "";
        String nameString = this.name != null ? this.name.toXMLString() : "";
        String resultsString = this.results != null ? this.results.toXMLString() : "";

        String XMLString = "<dcc:measurementResult" + idString + refIDString + refTypeString + ">\n" +
                nameString +
                resultsString +
                "</dcc:measurementResult>\n";

        return XMLString;
    }
}

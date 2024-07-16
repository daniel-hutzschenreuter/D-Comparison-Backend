package de.ptb.backend.model.dsi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DccMeasurementResults {
    String refType;
    DccName name;
    List<DccMeasurementResult> measurementResults = new ArrayList<>();

    public void addMeasurementResult(DccMeasurementResult measurementResult){
        this.measurementResults.add(measurementResult);
    }

    public DccMeasurementResults(List<DccMeasurementResult> measurementResults){
        this.measurementResults = measurementResults;
    }

    public String toXMLString(){
        String refTypeString = refType != null ? " refType=\"" + this.refType + "\"" : "";
        String nameString = this.name != null ? this.name.toXMLString() : "";

        StringBuilder resultsString = new StringBuilder();
        if(this.measurementResults != null){
            for (DccMeasurementResult measurementResult : this.measurementResults){
                resultsString.append(measurementResult.toXMLString());
            }
        }

        String XMLString = "<dcc:measurementResults" + refTypeString + ">\n" +
                nameString +
                resultsString +
                "</dcc:measurementResults>\n";

        return XMLString;
    }
}

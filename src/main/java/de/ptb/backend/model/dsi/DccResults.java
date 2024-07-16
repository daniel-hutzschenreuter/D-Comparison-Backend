package de.ptb.backend.model.dsi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DccResults {
    String refType;
    DccName name;
    List<DccResult> results = new ArrayList<>();

    public void addresult(DccResult result){
        this.results.add(result);
    }

    public String toXMLString(){
        String refTypeString = refType != null ? " refType=\"" + this.refType + "\"" : "";
        String nameString = this.name != null ? this.name.toXMLString() : "";

        StringBuilder resultsString = new StringBuilder();
        if(this.results != null){
            for (DccResult result : this.results){
                resultsString.append(result.toXMLString());
            }
        }

        String XMLString = "<dcc:results" + refTypeString + ">\n" +
                nameString +
                resultsString +
                "</dcc:results>\n";

        return XMLString;
    }
}

package de.ptb.backend.model.dsi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DccResult {
    String refType;
    String refID;
    DccName name;
    DccData data;

    public DccResult(String refType, DccName name, DccData data){
        this.refType= refType;
        this.name = name;
        this.data = data;
    }

    public String toXMLString(){
        String refTypeString = this.refType != null ? " refType=\"" + this.refType + "\"" : "";
        String refIDString = this.refID != null ? " refId=\"" + this.refID + "\"" : "";
        String nameString = this.name != null ? this.name.toXMLString() : "";
        String dataString = this.data != null ? this.data.toXMLString() : "";

        String XMLString = "<dcc:result" + refIDString + refTypeString + ">\n" +
                nameString +
                dataString +
                "</dcc:result>\n";

        return XMLString;
    }
}

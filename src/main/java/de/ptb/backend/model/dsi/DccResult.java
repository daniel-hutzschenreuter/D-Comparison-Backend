package de.ptb.backend.model.dsi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DccResult {
    String refType;
    DccName name;
    DccData data;

    public String toXMLString(){
        String refTypeString = refType != null ? " refType=\"" + this.refType + "\"" : "";
        String nameString = this.name != null ? this.name.toXMLString() : "";
        String dataString = this.data != null ? this.data.toXMLString() : "";

        String XMLString = "<dcc:result" + refTypeString + ">\n" +
                nameString +
                dataString +
                "</dcc:result>\n";

        return XMLString;
    }
}

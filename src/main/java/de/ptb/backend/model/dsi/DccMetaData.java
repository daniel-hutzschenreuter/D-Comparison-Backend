package de.ptb.backend.model.dsi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DccMetaData {
    DccData data;

    public String toXMLString(){
        return "<dcc:metaData>" + this.data.toXMLString() + "</dcc:metaData>\n";
    }
}

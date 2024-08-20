package de.ptb.backend.model.dsi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DccQuantity {
    String refType;
    String refID;
    DccName name;
    SiRealListXMLList siRealList;
    SiReal siReal;

    public DccQuantity(DccName name, SiReal siReal){
        this.name = name;
        this.siReal = siReal;
    }

    public DccQuantity(String refType, DccName name, SiRealListXMLList siRealList){
        this.refType = refType;
        this.name = name;
        this.siRealList = siRealList;
    }

    public DccQuantity(String refType, DccName name, SiReal siReal){
        this.refType = refType;
        this.name = name;
        this.siReal = siReal;
    }

    public DccQuantity(String refType, String refID, DccName name, SiRealListXMLList siRealList){
        this.refType = refType;
        this.refID = refID;
        this.name = name;
        this.siRealList = siRealList;
    }

    public DccQuantity(String refType, String refID, DccName name, SiReal siReal){
        this.refType = refType;
        this.refID = refID;
        this.name = name;
        this.siReal = siReal;
    }


    public String toXMLString(){
        String refTypeString = this.refType != null ? " refType=\"" + this.refType + "\"" : "";
        String refIDString = this.refID != null ? " refId=\"" + this.refID + "\"" : "";
        String nameString = this.name != null ? this.name.toXMLString() : "";
        String siRealListString = this.siRealList != null ? this.siRealList.toXMLString() : "";
        String siRealString = this.siReal != null ? this.siReal.toXMLString() : "";

        String XMLString = "<dcc:quantity" + refIDString + refTypeString + ">\n" +
                nameString +
                siRealString +
                siRealListString +
                "</dcc:quantity>\n";

        return XMLString;
    }
}

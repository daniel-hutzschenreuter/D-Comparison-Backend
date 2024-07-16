package de.ptb.backend.model.dsi;

import de.ptb.backend.BERT.RunResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DccData {
    DccQuantity quantity;
    DccList list;

    public void addQuantity(DccQuantity quantity){
        this.list.quantities.add(quantity);
    }

    public DccData(DccQuantity quantity){
        this.quantity = quantity;
    }

    public DccData(DccList list){
        this.list = list;
    }

    public String toXMLString(){
        String quantityString = this.quantity != null ? this.quantity.toXMLString() : "";
        String listString = this.list != null ? this.list.toXMLString() : "";

        String XMLString = "<dcc:data>\n" +
                quantityString +
                listString +
                "</dcc:data>\n";

        return XMLString;
    }
}

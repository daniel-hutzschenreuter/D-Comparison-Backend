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
    DccList dccList;
    List<DccQuantity> quantityList = new ArrayList<>();


    public void addQuantity(DccQuantity quantity){
        this.quantityList.add(quantity);
    }

    public DccData(DccQuantity quantity){
        this.quantity = quantity;
    }

    public DccData(DccList list){
        this.dccList = list;
    }

    public String toXMLString(){
        String quantityString = this.quantity != null ? this.quantity.toXMLString() : "";
        String listString = this.dccList != null ? this.dccList.toXMLString() : "";

        StringBuilder quantitiesString = new StringBuilder();
        if(this.quantityList != null){
            for (DccQuantity quantity : this.quantityList){
                quantitiesString.append(quantity.toXMLString());
            }
        }

        String XMLString = "<dcc:data>\n" +
                quantityString +
                quantitiesString +
                listString +
                "</dcc:data>\n";

        return XMLString;
    }
}

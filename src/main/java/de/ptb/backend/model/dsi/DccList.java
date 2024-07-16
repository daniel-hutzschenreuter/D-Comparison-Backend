package de.ptb.backend.model.dsi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DccList {
    List<DccQuantity> quantities = new ArrayList<>();

    public void addQuantity(DccQuantity quantity){
        this.quantities.add(quantity);
    }

    public String toXMLString(){
        String XMLString = "";

        for (DccQuantity quantity : quantities){
            XMLString = XMLString + quantity.toXMLString();
        }

        return XMLString;
    }
}

package de.ptb.backend.model.formula;

import de.ptb.backend.model.FundamentalConstant;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EEqualsMC2 {
    FundamentalConstant c;
    List<Double> massValues = new ArrayList<>();

    public EEqualsMC2(FundamentalConstant speedOfLight, List<SiReal> siReals) {
        this.c = speedOfLight;
        for(SiReal sireal: siReals){
            if(sireal.getUnit().equals("\\one")){
                this.massValues.add(sireal.getValue());
            }
        }
    }
    public  EEqualsMC2(FundamentalConstant speedOfLight){
        this.c = speedOfLight;
    }

    public SiReal calculate(Double mass){
        Double value = mass * this.c.getValue()*this.c.getValue();
        String unit = "\\joule";
        String dateTime = LocalDateTime.now().toString();
        Double uncertainty = 0.0;
        int coverageFactor = 0;
        Double coverageProbability = 0.0;
        return new SiReal(value, unit, dateTime, new SiExpandedUnc(uncertainty, coverageFactor, coverageProbability));
    }

    public List<SiReal> calculate(){
        List<SiReal> newSiReals = new ArrayList<>();
        for (Double mass: this.massValues){
            newSiReals.add(this.calculate(mass));
        }
        return newSiReals;
    }
}

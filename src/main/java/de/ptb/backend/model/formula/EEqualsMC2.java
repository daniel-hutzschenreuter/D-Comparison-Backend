package de.ptb.backend.model.formula;

import de.ptb.backend.model.dsi.SiConstant;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EEqualsMC2 {
    SiConstant c;
    List<SiReal> massSiReal = new ArrayList<>();

    public EEqualsMC2(SiConstant speedOfLight, List<SiReal> siReals) {
        this.c = speedOfLight;
        for(SiReal sireal: siReals){
            if(sireal.getUnit().equals("\\kilogram")){
                this.massSiReal.add(sireal);
            }
        }
    }
    public  EEqualsMC2(SiConstant speedOfLight){
        this.c = speedOfLight;
    }

    public SiReal calculate(SiReal massSiReal){
        Double value = massSiReal.getValue() * this.c.getValue()*this.c.getValue();
        String unit = "\\joule";
        String dateTime = LocalDateTime.now().toString();
        Double uncertainty = massSiReal.getExpUnc().getUncertainty();
        int coverageFactor = massSiReal.getExpUnc().getCoverageFactor();
        Double coverageProbability = massSiReal.getExpUnc().getCoverageProbability();
        return new SiReal(value, unit, dateTime, new SiExpandedUnc(uncertainty, coverageFactor, coverageProbability));
    }

    public List<SiReal> calculate(){
        List<SiReal> newSiReals = new ArrayList<>();
        for (SiReal mass: this.massSiReal){
            newSiReals.add(this.calculate(mass));
        }
        return newSiReals;
    }
}

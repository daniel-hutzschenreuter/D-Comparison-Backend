package de.ptb.backend.services;

import de.ptb.backend.BERT.DIR;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SiRealDifferenceCalculator implements I_SiRealDifferenceCalculator {

    @Override
    public List<SiReal> calculateDifference(List<SiReal> siReal1, List<SiReal> siReal2){
        String unit = siReal1.get(0).getUnit();

        List<SiReal> results = new ArrayList<>();
        for (int i = 0; i < siReal1.size(); i++) {
            double val1 = siReal1.get(i).getValue();
            double val2 = siReal2.get(i).getValue();

            double uncVal1 = siReal1.get(i).getExpUnc().getUncertainty() / siReal1.get(i).getExpUnc().getCoverageFactor();
            double uncVal2 = siReal2.get(i).getExpUnc().getUncertainty() / siReal2.get(i).getExpUnc().getCoverageFactor();

            double diff = val1 - val2;
            double uncDiff = Math.sqrt(uncVal1*uncVal1 +uncVal2*uncVal2) * 2;

            SiExpandedUnc siUncDiff = new SiExpandedUnc(uncDiff, 2, 0.95);

            SiReal result = new SiReal("Difference", diff, unit, "no Date", siUncDiff);
            results.add(result);
        }
        return results;
    }
}

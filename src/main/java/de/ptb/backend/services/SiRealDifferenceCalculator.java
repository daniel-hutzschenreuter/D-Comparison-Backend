package de.ptb.backend.services;

import de.ptb.backend.BERT.DIR;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiExpandedUncXMLList;
import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.dsi.SiRealListXMLList;
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


    @Override
    public List<SiRealListXMLList> calculateDifferenceList(List<SiRealListXMLList> siRealLists1, List<SiRealListXMLList> siRealLists2){

        List<SiRealListXMLList> results = new ArrayList<>();
        for (int j = 0; j < siRealLists1.size(); j++){
            // get valueLists and Uncertainty List from SiRealXMLLists
            List<Double> values1 = siRealLists1.get(j).getValues();
            List<Double> values2 = siRealLists2.get(j).getValues();
            SiExpandedUncXMLList uncList1 = siRealLists1.get(j).getExpUncList();
            SiExpandedUncXMLList uncList2 = siRealLists2.get(j).getExpUncList();

            List<Double> differences = new ArrayList<>();
            List<Double> uncertainties = new ArrayList<>();
            for (int i = 0; i < values1.size(); i++) {
                double val1 = values1.get(i);
                double val2 = values2.get(i);

                double uncVal1 = uncList1.getUncertaintyList().get(i) / uncList1.getCoverageFactorList().get(i);
                double uncVal2 = uncList2.getUncertaintyList().get(i) / uncList2.getCoverageFactorList().get(i);

                double diff = val1 - val2;
                differences.add(diff);

                double uncDiff = Math.sqrt(uncVal1*uncVal1 +uncVal2*uncVal2) * 2;
                uncertainties.add(uncDiff);

            }
            // build new ExpandedUncertaintyXMLList and SiRealXMLList
            String name = siRealLists1.get(j).getName();
            String unit = siRealLists1.get(j).getUnit();
            List<Integer> covFactors = uncList1.getCoverageFactorList();
            List<Double> covProbability = uncList1.getCoverageProbabilityList();
            List<String> distributions = uncList1.getDistributionList();

            SiExpandedUncXMLList diffExpUncXMLList = new SiExpandedUncXMLList(uncertainties, covFactors, covProbability, distributions);
            SiRealListXMLList diffSiRealXMLList = new SiRealListXMLList(name, differences, unit, diffExpUncXMLList);

            results.add(diffSiRealXMLList);
        }
        return results;
    }
}

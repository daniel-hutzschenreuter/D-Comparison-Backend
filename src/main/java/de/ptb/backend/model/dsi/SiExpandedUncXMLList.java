package de.ptb.backend.model.dsi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class SiExpandedUncXMLList {
    Integer lengthList;
    List<Double> uncertaintyXMLList;
    List<Integer> coverageFactorXMLList;
    List<Double> coverageProbabilityXMLList;
    List<String> distributionXMLList;

    public SiExpandedUncXMLList(List<Double> uncertaintyList, List<Integer> coverageFactorList,
                                List<Double> coverageProbabilityList, List<String> distributionList) {
        this.uncertaintyXMLList = uncertaintyList;
        this.lengthList = this.uncertaintyXMLList.size();
        this.coverageFactorXMLList = coverageFactorList;
        this.coverageProbabilityXMLList = coverageProbabilityList;
        this.distributionXMLList = distributionList;
    }

    public SiExpandedUncXMLList(List<Double> uncertaintyList, Integer coverageFactor,
                                Double coverageProbability, String distribution) {
        this.uncertaintyXMLList = uncertaintyList;
        this.lengthList = this.uncertaintyXMLList.size();
        this.coverageFactorXMLList = convertSingleIntegerToList(coverageFactor);
        this.coverageProbabilityXMLList = convertSingleDoubleToList(coverageProbability);
        this.distributionXMLList = convertSingleStringToList(distribution);
    }

    public SiExpandedUncXMLList(Double uncertainty, Integer coverageFactor,
                                Double coverageProbability, String distribution, Integer lengthList) {
        this.lengthList = lengthList;
        this.uncertaintyXMLList = convertSingleDoubleToList(uncertainty);
        this.coverageFactorXMLList = convertSingleIntegerToList(coverageFactor);
        this.coverageProbabilityXMLList = convertSingleDoubleToList(coverageProbability);
        this.distributionXMLList = convertSingleStringToList(distribution);
    }


    private List<Double> convertSingleDoubleToList(Double value) {
        return new ArrayList<Double>(Collections.nCopies(this.lengthList, value));
    }

    private List<Integer> convertSingleIntegerToList(Integer value) {
        return new ArrayList<Integer>(Collections.nCopies(this.lengthList, value));
    }

    private List<String> convertSingleStringToList(String value) {
        return new ArrayList<String>(Collections.nCopies(this.lengthList, value));
    }
}

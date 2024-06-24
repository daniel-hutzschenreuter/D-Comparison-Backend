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
    List<Double> uncertaintyList;
    List<Integer> coverageFactorList;
    List<Double> coverageProbabilityList;
    List<String> distributionList;

    public SiExpandedUncXMLList(List<Double> uncertaintyList, List<Integer> coverageFactorList,
                                List<Double> coverageProbabilityList, List<String> distributionList) {
        this.uncertaintyList = uncertaintyList;
        this.lengthList = this.uncertaintyList.size();
        this.coverageFactorList = coverageFactorList;
        this.coverageProbabilityList = coverageProbabilityList;
        this.distributionList = distributionList;
    }

    public SiExpandedUncXMLList(List<Double> uncertaintyList, Integer coverageFactor,
                                Double coverageProbability, String distribution) {
        this.uncertaintyList = uncertaintyList;
        this.lengthList = this.uncertaintyList.size();
        this.coverageFactorList = convertSingleIntegerToList(coverageFactor);
        this.coverageProbabilityList = convertSingleDoubleToList(coverageProbability);
        this.distributionList = convertSingleStringToList(distribution);
    }

    public SiExpandedUncXMLList(Double uncertainty, Integer coverageFactor,
                                Double coverageProbability, String distribution, Integer lengthList) {
        this.lengthList = lengthList;
        this.uncertaintyList = convertSingleDoubleToList(uncertainty);
        this.coverageFactorList = convertSingleIntegerToList(coverageFactor);
        this.coverageProbabilityList = convertSingleDoubleToList(coverageProbability);
        this.distributionList = convertSingleStringToList(distribution);
    }


    private List<Double> convertSingleDoubleToList(Double value) {
        return new ArrayList<>(Collections.nCopies(this.lengthList, value));
    }

    private List<Integer> convertSingleIntegerToList(Integer value) {
        return new ArrayList<>(Collections.nCopies(this.lengthList, value));
    }

    private List<String> convertSingleStringToList(String value) {
        return new ArrayList<>(Collections.nCopies(this.lengthList, value));
    }
}

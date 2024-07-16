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

    private String covFactorsToString(){
        boolean allEqual = this.coverageFactorList.stream().distinct().count() <= 1;

        String stringOutput;
        if (allEqual) {
            stringOutput = this.coverageFactorList.get(0).toString();
        } else {
            stringOutput = this.coverageFactorList.toString().replaceAll("[,]","");
            stringOutput = stringOutput.substring(1, stringOutput.length()-1);
        }
        return stringOutput;
    }

    private String covProbabilitiesToString(){
        boolean allEqual = this.coverageProbabilityList.stream().distinct().count() <= 1;

        String stringOutput;
        if (allEqual) {
            stringOutput = this.coverageProbabilityList.get(0).toString();
        } else {
            stringOutput = this.coverageProbabilityList.toString().replaceAll("[,]","");
            stringOutput = stringOutput.substring(1, stringOutput.length()-1);
        }
        return stringOutput;
    }

    private String distributionsToString(){
        boolean allEqual = this.distributionList.stream().distinct().count() <= 1;

        String stringOutput;
        if (allEqual) {
            stringOutput = this.distributionList.get(0);
        } else {
            stringOutput = this.distributionList.toString().replaceAll("[,]","");
            stringOutput = stringOutput.substring(1, stringOutput.length()-1);
        }
        return stringOutput;
    }

    private String uncValuesToString(){
        String stringOutput = this.uncertaintyList.toString().replaceAll("[,]","");
        stringOutput = stringOutput.substring(1, stringOutput.length()-1);
        return stringOutput;
    }

    public String toXMLString(){
        // check if values are available
        String uncValueString = this.uncValuesToString();
        String covFactorString = this.covFactorsToString();
        String covProbabilityString = this.covProbabilitiesToString();
        String distributionString = this.distributionsToString();
        String XMLstring =
                "<si:expandedUncXMLList>\n" +
                "<si:uncertaintyXMLList>" + uncValueString + "</si:uncertaintyXMLList>\n" +
                "<si:coverageFactorXMLList>" + covFactorString +"</si:coverageFactorXMLList>\n" +
                "<si:coverageProbabilityXMLList>"+ covProbabilityString +"</si:coverageProbabilityXMLList>\n" +
                "<si:distributionXMLList>" + distributionString + "</si:distributionXMLList>\n" +
                "</si:expandedUncXMLList>\n";
        return XMLstring;
    }

}

package de.ptb.backend.services;

import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiExpandedUncXMLList;
import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.dsi.SiRealListXMLList;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class SiRealTemperaturFromResistanceCalculator implements I_SiRealTemperaturFromResistanceCalculator {

//    // Fitparameter for NTC Thermistor Calibration function
//    private double aNtc = 0.000928692;
//    private double bNtc = 0.000222219;
//    private double cNtc = 0.000000123621;

    // Fitparameter for pt100 Calibration function
    private double aPt100 = -0.0000640851395582478;
    private double bPt100 = 0.391809225298376;
    private double cPt100 = 100.036630814515;

//    @Override
//    public List<SiReal> calculatePt100Temperature(List<SiReal> resistance) {
//
//        List<SiReal> results = new ArrayList<>();
//        for (SiReal siReal : resistance) {
//            // calculate
//            double r = siReal.getValue();
//            double uncR = siReal.getExpUnc().getUncertainty() / siReal.getExpUnc().getCoverageFactor();
//            double temperature = (-bPt100 + Math.sqrt(Math.pow(bPt100, 2) - 4 * aPt100 * cPt100 + 4 * aPt100 * r)) / (2 * aPt100);
//
//            // preliminary Uncertainty calculation: will change in the future!
//            double tempUp = Math.abs((-bPt100 + Math.sqrt(Math.pow(bPt100, 2) - 4 * aPt100 * cPt100 + 4 * aPt100 * (r - uncR))) / (2 * aPt100) - temperature);
//            double tempDown = Math.abs((-bPt100 + Math.sqrt(Math.pow(bPt100, 2) - 4 * aPt100 * cPt100 + 4 * aPt100 * (r - uncR))) / (2 * aPt100) - temperature);
//            double uncTemperature = 2 * (tempUp + tempDown) / 2;
//            SiExpandedUnc siUncTemperature = new SiExpandedUnc(uncTemperature, 2, 0.95);
//
//            SiReal result = new SiReal("temperature_pt100", temperature, "\\degreecelsius", "no Date", siUncTemperature);
//            results.add(result);
//        }
//        return results;
//
//    }

//    @Override
//    public List<SiReal> calculateNTCTemperature(List<SiReal> resistance) {
//
//        List<SiReal> results = new ArrayList<>();
//        for (SiReal siReal : resistance) {
//            double r = siReal.getValue();
//            double uncR = siReal.getExpUnc().getUncertainty() / siReal.getExpUnc().getCoverageFactor();
//            double temperature = 1 / (aNtc + bNtc * Math.log(r) + cNtc * Math.pow(Math.log(r), 3)) - 273.15;
//
//            // preliminary Uncertainty calculation: will change in the future!
//            double tempUp = Math.abs(1 / (aNtc + bNtc * Math.log(r + uncR) + cNtc * Math.pow(Math.log(r + uncR), 3)) - 273.15 - temperature);
//            double tempDown = Math.abs(1 / (aNtc + bNtc * Math.log(r - uncR) + cNtc * Math.pow(Math.log(r - uncR), 3)) - 273.15 - temperature);
//            double uncTemperature = 2 * (tempUp + tempDown) / 2;
//            SiExpandedUnc siUncTemperature = new SiExpandedUnc(uncTemperature, 2, 0.95);
//
//            SiReal result = new SiReal("temperature_NTC", temperature, "\\degreecelsius", "no Date", siUncTemperature);
//            results.add(result);
//        }
//        return results;
//    }


    @Override
    public List<SiRealListXMLList> calculatePt100TemperatureList(List<SiRealListXMLList> resistanceXMLLists) {

        List<SiRealListXMLList> results = new ArrayList<>();
        for (SiRealListXMLList SiRealList : resistanceXMLLists) {
            List<Double> rValues = SiRealList.getValues();
            SiExpandedUncXMLList expUncList = SiRealList.getExpUncList();

            List<Double> temperatures = new ArrayList<>();
            List<Double> uncertainties = new ArrayList<>();
            for (int i = 0; i < SiRealList.getValues().size(); i++) {
                // get Values form SiRealXMLList
                double r = rValues.get(i);
                double expUnc = expUncList.getUncertaintyList().get(i);
                int covFactor = expUncList.getCoverageFactorList().get(i);

                //calculate Temperature
                double temperature = (-bPt100 + Math.sqrt(Math.pow(bPt100, 2) - 4 * aPt100 * cPt100 + 4 * aPt100 * r)) / (2 * aPt100);
                temperatures.add(temperature);

                //Uncertainty calculation:
                double uncMKT50 = 1.417744687875780; //Uncertainty of bath (nonlinearity,...)
                double uncKalPt100 = 3; //Uncertainty of pt100 calibration generalized
                double uncR = expUnc / covFactor;
                double uncRad = uncR/r/(0.014388/0.00001/Math.pow((273.15+temperature),2)); // from comparison with radiation thermometer
                double uncTemperature = Math.sqrt(uncMKT50*uncMKT50 + uncKalPt100*uncKalPt100 + uncRad*uncRad) * 2/1000;
                uncertainties.add(uncTemperature);
            }

            // build new ExpandedUncertaintyXMLList and SiRealXMLList
            String name = SiRealList.getName();
            String unit = "\\degreecelsius";
            List<Integer> covFactors = expUncList.getCoverageFactorList();
            List<Double> covProbability = expUncList.getCoverageProbabilityList();
            List<String> distributions = expUncList.getDistributionList();

            SiExpandedUncXMLList temperatureExpUncXMLList = new SiExpandedUncXMLList(uncertainties, covFactors, covProbability, distributions);
            SiRealListXMLList temperatureSiRealXMLList = new SiRealListXMLList(name, temperatures, unit, temperatureExpUncXMLList);
            results.add(temperatureSiRealXMLList);
        }
        return results;
    }

//    @Override
//    public List<SiRealListXMLList> calculateNTCTemperatureList(List<SiRealListXMLList> resistanceXMLLists) {
//
//        List<SiRealListXMLList> results = new ArrayList<>();
//        for (SiRealListXMLList SiRealList : resistanceXMLLists) {
//            List<Double> rValues = SiRealList.getValues();
//            SiExpandedUncXMLList expUncList = SiRealList.getExpUncList();
//
//            List<Double> temperatures = new ArrayList<>();
//            List<Double> uncertainties = new ArrayList<>();
//            for (int i = 0; i < SiRealList.getValues().size(); i++) {
//                // get Values form SiRealXMLList
//                double r = rValues.get(i);
//                double expUnc = expUncList.getUncertaintyList().get(i);
//                int covFactor = expUncList.getCoverageFactorList().get(i);
//
//                //calculate Temperature
//                double uncR = expUnc / covFactor;
//                double temperature = 1 / (aNtc + bNtc * Math.log(r) + cNtc * Math.pow(Math.log(r), 3)) - 273.15;
//                temperatures.add(temperature);
//
//                // preliminary Uncertainty calculation: will change in the future!
//                double tempUp = Math.abs(1 / (aNtc + bNtc * Math.log(r + uncR) + cNtc * Math.pow(Math.log(r + uncR), 3)) - 273.15 - temperature);
//                double tempDown = Math.abs(1 / (aNtc + bNtc * Math.log(r - uncR) + cNtc * Math.pow(Math.log(r - uncR), 3)) - 273.15 - temperature);
//                double uncTemperature = 2 * (tempUp + tempDown) / 2;
//                uncertainties.add(uncTemperature);
//            }
//            // build new ExpandedUncertaintyXMLList and SiRealXMLList
//            String name = SiRealList.getName();
//            String unit = SiRealList.getUnit();
//            List<Integer> covFactors = expUncList.getCoverageFactorList();
//            List<Double> covProbability = expUncList.getCoverageProbabilityList();
//            List<String> distributions = expUncList.getDistributionList();
//
//            SiExpandedUncXMLList temperatureExpUncXMLList = new SiExpandedUncXMLList(uncertainties, covFactors, covProbability, distributions);
//            SiRealListXMLList temperatureSiRealXMLList = new SiRealListXMLList(name, temperatures, unit, temperatureExpUncXMLList);
//            results.add(temperatureSiRealXMLList);
//        }
//        return results;
//    }
}

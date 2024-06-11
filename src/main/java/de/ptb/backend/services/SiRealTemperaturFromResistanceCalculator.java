package de.ptb.backend.services;

import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class SiRealTemperaturFromResistanceCalculator implements I_SiRealTemperaturFromResistanceCalculator{

    // Fitparameter for NTC Thermistor Calibration function
    private double aNtc = 0.000928692;
    private double bNtc = 0.000222219;
    private double cNtc = 0.000000123621;

    // Fitparameter for pt100 Calibration function
    private double aPt100 = -0.0000640851395582478;
    private double bPt100 = 0.391809225298376;
    private double cPt100 = 100.036630814515;

    @Override
    public List<SiReal> calculatePt100Temperature(List<SiReal> resistance) {

        List<SiReal> results = new ArrayList<>();
        for (SiReal siReal : resistance) {
            // calculate
            double r = siReal.getValue();
            double uncR = siReal.getExpUnc().getUncertainty() / siReal.getExpUnc().getCoverageFactor();
            double temperature = (-bPt100 + Math.sqrt(Math.pow(bPt100,2) - 4 * aPt100 * cPt100 + 4 * aPt100 * r))/(2 * aPt100);

            // preliminary Uncertainty calculation: will change in the future!
            double tempUp = Math.abs((-bPt100 + Math.sqrt(Math.pow(bPt100,2) - 4 * aPt100 * cPt100 + 4 * aPt100 * (r-uncR)))/(2 * aPt100) - temperature);
            double tempDown = Math.abs((-bPt100 + Math.sqrt(Math.pow(bPt100,2) - 4 * aPt100 * cPt100 + 4 * aPt100 * (r-uncR)))/(2 * aPt100) - temperature);
            double uncTemperature = 2 * (tempUp + tempDown) / 2;
            SiExpandedUnc siUncTemperature = new SiExpandedUnc(uncTemperature, 2, 0.95);

            SiReal result = new SiReal("temperature_pt100", temperature, "\\degreecelsius", "no Date", siUncTemperature);
            results.add(result);
        }
        return results;

    }

    @Override
    public List<SiReal> calculateNTCTemperature(List<SiReal> resistance) {

        List<SiReal> results = new ArrayList<>();
        for (SiReal siReal : resistance) {
            double r = siReal.getValue();
            double uncR = siReal.getExpUnc().getUncertainty() / siReal.getExpUnc().getCoverageFactor();
            double temperature = 1 / (aNtc + bNtc * Math.log(r) + cNtc * Math.pow(Math.log(r), 3)) - 273.15;

            // preliminary Uncertainty calculation: will change in the future!
            double tempUp = Math.abs(1 / (aNtc + bNtc * Math.log(r + uncR) + cNtc * Math.pow(Math.log(r + uncR), 3)) - 273.15 - temperature);
            double tempDown = Math.abs(1 / (aNtc + bNtc * Math.log(r - uncR) + cNtc * Math.pow(Math.log(r - uncR), 3)) - 273.15 - temperature);
            double uncTemperature = 2 * (tempUp + tempDown) / 2;
            SiExpandedUnc siUncTemperature = new SiExpandedUnc(uncTemperature, 2, 0.95);

            SiReal result = new SiReal("temperature_NTC", temperature, "\\degreecelsius", "no Date", siUncTemperature);
            results.add(result);
        }
        return results;
    }
}

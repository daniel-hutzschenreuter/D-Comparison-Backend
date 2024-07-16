package de.ptb.backend.services;

import de.ptb.backend.BERT.RunResult;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DsiConverter {

    public DccResult bilateralEnValuesToDccResult(List<Participant> participantList, double[][] bilateralEnValues, Double nominalTemeprature) {
        // get labelListString
        StringBuilder labelString = new StringBuilder();
        for (Participant participant : participantList) {
            labelString.append(participant.getName()).append(" ");
        }

        // get rows of bilateral En matrix as individual DccQuantity
        DccList bilateralEnValuesDccList = new DccList();
        for (int j = 0; j < bilateralEnValues.length; j++) {
            String name = "Bilateral en Matrix row " + j + ": " + participantList.get(j).getName();
            Double[] valuesArray = ArrayUtils.toObject(bilateralEnValues[j]);
            List<Double> values = Arrays.asList(valuesArray);
            String unit = "\\one";

            DccQuantity bilateralEnRow = new DccQuantity(
                    "comparison_equivalenceValueEnCriterion",
                    new DccName(name, "en"),
                    new SiRealListXMLList(values, labelString.toString(), unit));

            bilateralEnValuesDccList.addQuantity(bilateralEnRow);
        }

        return new DccResult(
                "comparison_bilateralEquivalenceValue",
                new DccName(
                        "Bilateral equivalence values for nominal temperature of " + nominalTemeprature + "°C",
                        "en"
                ),
                new DccData(bilateralEnValuesDccList)
        );
    }

    public DccMeasurementResults readValuesToDccMeasurementResults(List<Participant> participantList,
                                                                   List<SiRealListXMLList> nominalTempSiRealXMLLists,
                                                                   List<SiRealListXMLList> sensor1SiRealXMLLists,
                                                                   List<SiRealListXMLList> sensor2SiRealXMLLists,
                                                                   List<SiRealListXMLList> indicatedTempSiRealXMLLists,
                                                                   List<SiRealListXMLList> radianceTempSiRealXMLLists,
                                                                   List<SiRealListXMLList> referenceTempSiRealXMLLists,
                                                                   List<SiRealListXMLList> tempDifferenceSiRealXMLLists,
                                                                   List<SiRealListXMLList> enCriterionSiRealXMLLists) {

        DccMeasurementResults measurementResults = new DccMeasurementResults();
        for (int i = 0; i < participantList.size(); i++) {
            // collect all the relevant dcc input data from Participant Dccs
            DccData participantDccData = new DccData();

            participantDccData.addQuantity(
                    new DccQuantity(
                            "basic_nominalValue",
                            new DccName(
                                    "Set temperature",
                                    "en"
                            ),
                            nominalTempSiRealXMLLists.get(i)
                    )
            );

            participantDccData.addQuantity(
                    new DccQuantity(
                            "basic_measuredValueSensor1",
                            new DccName(
                                    "Resistance values of monitor PRT",
                                    "en"
                            ),
                            sensor1SiRealXMLLists.get(i)
                    )
            );

            participantDccData.addQuantity(
                    new DccQuantity(
                            "basic_measuredValueSensor2",
                            new DccName(
                                    "Resistance values of monitor NTC Thermistor",
                                    "en"
                            ),
                            sensor2SiRealXMLLists.get(i)
                    )
            );

            participantDccData.addQuantity(
                    new DccQuantity(
                            "basic_indicatedValue",
                            new DccName(
                                    "Indicated temperatures",
                                    "en"
                            ),
                            indicatedTempSiRealXMLLists.get(i)
                    )
            );

            participantDccData.addQuantity(
                    new DccQuantity(
                            "basic_measuredValue basic_arithmenticMean temperature_ITS-90",
                            new DccName(
                                    "Radiance temperatures",
                                    "en"
                            ),
                            radianceTempSiRealXMLLists.get(i)
                    )
            );

            participantDccData.addQuantity(
                    new DccQuantity(
                            "temperature_referenceValue",
                            new DccName(
                                    "Temperature reference calculated from pt-100 resistance",
                                    "en"
                            ),
                            referenceTempSiRealXMLLists.get(i)
                    )
            );

            participantDccData.addQuantity(
                    new DccQuantity(
                            "temperature_differenceValue",
                            new DccName(
                                    "Temperature differences between reference temperatures and radiance temperatures",
                                    "en"
                            ),
                            tempDifferenceSiRealXMLLists.get(i)
                    )
            );

            DccResult participantDccResult = new DccResult(
                    "temperature_radianceTemperature",
                    new DccName(
                            "Measurement data and derived values",
                            "en"),
                    participantDccData);

            // collect info from En Values for individual Participants
            DccData EnValueDccData = new DccData(
                new DccQuantity(
                    "comparison_equivalenceValueEnCriterion",
                    new DccName(
                            "En criterion values for temperature differences",
                            "en"
                    ),
                    enCriterionSiRealXMLLists.get(i)
                )
            );
            DccResult enValueDccResult = new DccResult(
                    "comparison_equivalenceValue",
                    new DccName(
                            "Equivalence values",
                            "en"),
                    EnValueDccData);


            DccResults dccResults = new DccResults();
            dccResults.addresult(participantDccResult);
            dccResults.addresult(enValueDccResult);

            // get pid without http reference
            String urlPid = participantList.get(i).getDccPid();
            int lastIndex = urlPid.lastIndexOf("/");
            String pid = lastIndex != -1 ? urlPid.substring(lastIndex+1) : urlPid;

            DccMeasurementResult dccMeasurementResult = new DccMeasurementResult(
                    pid,
                    new DccName(
                            "Comparison data of participant laboratory: " + nominalTempSiRealXMLLists.get(i).getName(),
                            "en"
                    ),
                    dccResults
            );

            measurementResults.addMeasurementResult(dccMeasurementResult);
        }
        return measurementResults;
    }

    public DccResult enRefValToDccResult(Double nominalTemperature, SiReal enCriterionRefeVal){
        return new DccResult(
                "temperature_radianceTemperature",
                new DccName("Temperature reference value at nominal temperature of "
                        + nominalTemperature + " °C", "en"),
                new DccData(new DccQuantity(
                        "comparison_referenceValueEnCriterion",
                        new DccName("Comparison Reference Value (En Criterion)", "en"),
                        enCriterionRefeVal
                )));
    }

    public List<SiRealListXMLList> EnCriterionsToSiRealXMLLists(List<RunResult> enCriterionResults){

        List<SiRealListXMLList> enCriterionSiRealXMLLists = new ArrayList<>();

        // create Lists to rearange the Runresult values
        List<List<Double>> newEnCriterionValueLists = new ArrayList<>();

        // initialize List of Lists
        for (int j = 0; j < enCriterionResults.get(0).getEOResults().size(); j++) {
            newEnCriterionValueLists.add(new ArrayList<>());
        }

        for (int i = 0; i < enCriterionResults.size(); i++){
            // one temperature per runResult
            RunResult runResult = enCriterionResults.get(i);
            for (int j = 0; j < runResult.getEOResults().size(); j++) {
                // one participant per E0
                Double enValue = runResult.getEOResults().get(j).getEquivalenceValue();
                newEnCriterionValueLists.get(j).add(enValue);
            }
        }

        // create SiRealListXMLList for En Value
        for (List<Double> enValues: newEnCriterionValueLists) {
            SiRealListXMLList enCriterionSiRealXMLList = new SiRealListXMLList("Equivalence Value", enValues, "\\one");
            enCriterionSiRealXMLLists.add(enCriterionSiRealXMLList);
        }
        return enCriterionSiRealXMLLists;
    }
}

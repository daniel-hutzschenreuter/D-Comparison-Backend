/*
 * Copyright (c) 2022 - 2023 Physikalisch-Technische Bundesanstalt (PTB), all rights reserved.
 * This source code and software is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, version 3 of the License.
 * The software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this XSD.  If not, see http://www.gnu.org/licenses.
 * CONTACT: 		info@ptb.de
 * DEVELOPMENT:	https://d-si.ptb.de
 * AUTHORS:		Wafa El Jaoua, Tobias Hoffmann, Clifford Brown, Daniel Hutzschenreuter
 * LAST MODIFIED:	15.08.23, 15:41
 */

package de.ptb.backend.model.dsi;

import de.ptb.backend.BERT.EO;
import de.ptb.backend.BERT.GEO;
import de.ptb.backend.BERT.RunResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Data
@NoArgsConstructor
public class TempMeasurementResult {
    List<String> pids;
    List<SiRealListXMLList> radianceTempSiRealXMLLists;
    List<SiRealListXMLList> nominalTempSiRealXMLLists;
    List<SiRealListXMLList> sensor1SiRealXMLLists;
    List<SiRealListXMLList> sensor2SiRealXMLLists;
    List<SiRealListXMLList> indicatedTempSiRealXMLLists;
    List<SiRealListXMLList> referenceTempSiRealXMLLists;
    List<SiRealListXMLList> tempDifferenceSiRealXMLLists;
    List<RunResult> enCriterionLists = new ArrayList<>();
    List<SiRealListXMLList> enCriterionSiRealXMLLists = new ArrayList<>();
    List<SiReal> enRefValues = new ArrayList<>();
    List<DccResult> bilateralEnValuesDccResult = new ArrayList<>();
//    String participantResultsXMLString;
//    String comparisonResultsXMLString;
//    List<String> measurementResultXMLStrings;
    String result = "";

    public void setParticpantValues(List<String> pid, List<SiRealListXMLList> radTempValue, List<SiRealListXMLList> nominalTempValue,
                                    List<SiRealListXMLList> sensor1Value, List<SiRealListXMLList> sensor2Value,
                                    List<SiRealListXMLList> indicatedTempValue, List<SiRealListXMLList> referenceTempValue,
                                    List<SiRealListXMLList> tempDifferences) {
        this.pids= pid;
        this.radianceTempSiRealXMLLists = radTempValue;
        this.nominalTempSiRealXMLLists = nominalTempValue;
        this.sensor1SiRealXMLLists = sensor1Value;
        this.sensor2SiRealXMLLists = sensor2Value;
        this.indicatedTempSiRealXMLLists = indicatedTempValue;
        this.referenceTempSiRealXMLLists = referenceTempValue;
        this.tempDifferenceSiRealXMLLists = tempDifferences;
    }

    public void addEnReferenceValue(SiReal enRefValue){
        this.enRefValues.add(enRefValue);
    }

    public void addbilateralEnValuesDccResult(DccResult bilateralEnValuesDccResult){
        this.bilateralEnValuesDccResult.add(bilateralEnValuesDccResult);
    }

    public void addEnCriterionValues(RunResult enCriterionResult){
        this.enCriterionLists.add(enCriterionResult);
    }

    public void generateEnCriterionSiRealXMLList(){
        // create Lists to rearange the Runresult values
        List<List<Double>> newEnCriterionValueLists = new ArrayList<>();

        // initialize List of Lists
        for (int j = 0; j < enCriterionLists.get(0).getEOResults().size(); j++) {
            newEnCriterionValueLists.add(new ArrayList<>());
        }


        for (int i = 0; i < enCriterionLists.size(); i++){
            // one temperature per runResult
            RunResult runResult = enCriterionLists.get(i);
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

    }

    public void generateParticipantMeasurementResultsXML() {
        for (int i = 0; i < this.pids.size(); i++){

            // extract pid name from url
            String urlPid = this.pids.get(i);
            int lastIndex = urlPid.lastIndexOf("/");
            String pid = lastIndex != -1 ? urlPid.substring(lastIndex+1) : urlPid;
            this.result +=
                    "\t\t<dcc:measurementResult id=\"" + pid + "\">\n" +
                    "\t\t\t<dcc:name>\n" + //TODO hier id dayX hinzufügen?
                    "\t\t\t\t<dcc:content lang=\"en\">Comparison data of participant laboratory: " + this.radianceTempSiRealXMLLists.get(i).getName() + "</dcc:content>\n" +
                    "\t\t\t</dcc:name>\n" +
                    "\t\t\t<dcc:results>\n" +
                    "\t\t\t\t<dcc:result refType=\"temperature_radianceTemperature\">\n" +
                    "\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t<dcc:content lang=\"en\">Measurement data and derived values</dcc:content>\n" +
                    "\t\t\t\t\t</dcc:name>\n" +
                    "\t\t\t\t\t<dcc:data>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"basic_nominalValue\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Set temperature</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                                   nominalTempSiRealXMLLists.get(i).toXMLString() +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"basic_measuredValueSensor1\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Resistance values of monitor PRT</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                                   sensor1SiRealXMLLists.get(i).toXMLString() +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"basic_measuredValueSensor2\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Resistance values of monitor NTC Thermistor</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                                   sensor2SiRealXMLLists.get(i).toXMLString() +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"temperature_indicatedValue\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Indicated temperatures</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                                   indicatedTempSiRealXMLLists.get(i).toXMLString() +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"temperature_referenceValue\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Temperature reference calculated from pt-100 resistance</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                                   referenceTempSiRealXMLLists.get(i).toXMLString() +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"basic_measuredValue basic_arithmenticMean temperature_ITS-90\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Radiance temperatures</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                                   radianceTempSiRealXMLLists.get(i).toXMLString() +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"temperature_differenceValue\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Temperature differences between reference temperatures and radiance temperatures</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                                   tempDifferenceSiRealXMLLists.get(i).toXMLString() +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t</dcc:data>\n" +
                    "\t\t\t\t</dcc:result>\n" +
                    "\t\t\t\t<dcc:result refType=\"comparison_equivalenceValue\">\n" +
                    "\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t<dcc:content lang=\"en\">Equivalence values</dcc:content>\n" +
                    "\t\t\t\t\t</dcc:name>\n" +
                    "\t\t\t\t\t<dcc:data>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"comparison_equivalenceValueEnCriterion\">\n" + //TODO hier ggf. id hinzufügen mit verweis auf temperature difference
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">En criterion values for temperature differences</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                                   enCriterionSiRealXMLLists.get(i).toXMLString() +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t</dcc:data>\n" +
                    "\t\t\t\t</dcc:result>\n" +
                    "\t\t\t</dcc:results>\n" +
                    "\t\t</dcc:measurementResult>\n ";
        }
    }

//    private void generateEquivalenceValueResultXML(){
//        this.comparisonResultsXMLString = "        <dcc:result refType=\"comparison_equivalenceValues\">\n" +
//                "          <dcc:name>\n" +
//                "            <dcc:content lang=\"en\">Equivalence reference values for each nominal temperature</dcc:content>\n" +
//                "          </dcc:name>\n" +
//                "          <dcc:data>\n";
//
//        List<Double> nominalTemperatures = this.nominalTempSiRealXMLLists.get(0).getValues();
//        for (int i=0; i < enRefValues.size(); i++) {
//            this.comparisonResultsXMLString +=
//                    "            <dcc:quantity refType=\"comparison_equivalenceValueEnCriterion\">\n" +
//                    "              <dcc:name>\n" +
//                    "                <dcc:content lang=\"en\">En criterion value for " + nominalTemperatures.get(i) + " °C</dcc:content>\n" +
//                    "              </dcc:name>\n" +
//                    "              <si:real>\n" +
//                    "                <si:value>" + this.enRefValues.get(i).getValue() + "</si:value>\n" +
//                    "                <si:unit>" + this.enRefValues.get(i).getUnit() + "</si:unit>\n" +
//                    "              </si:real>\n" +
//                    "            </dcc:quantity>\n";
//        }
//        this.comparisonResultsXMLString +=
//                "          </dcc:data>\n" +
//                "        </dcc:result>\n";
//    }

    public void generateComparisonMeasurementResultXML(){
        this.result =
                "\t\t<dcc:measurementResult refType=\"comparison_referenceValues\">\n" +
                "\t\t\t<dcc:name>\n" +
                "\t\t\t\t<dcc:content lang=\"en\">Comparison reference values for each nominal temperature </dcc:content>\n" +
                "\t\t\t</dcc:name>\n" +
                "\t\t\t<dcc:results>\n";

        List<Double> nominalTemperatures = this.nominalTempSiRealXMLLists.get(0).getValues();
        for (int i=0; i < enRefValues.size(); i++) {
            this.result +=
                "\t\t\t\t<dcc:result refType=\"temperature_radianceTemperature\">\n" + //Todo reftype anpassen
                "\t\t\t\t\t<dcc:name>\n" +
                "\t\t\t\t\t\t<dcc:content lang=\"en\"> Temperature reference value at nominal temperature of " + nominalTemperatures.get(i) + " °C</dcc:content>\n" +
                "\t\t\t\t\t</dcc:name>\n" +
                "\t\t\t\t\t<dcc:data>\n" +
                "\t\t\t\t\t\t<dcc:quantity refType=\"comparison_referenceValueEnCriterion\">\n" +
                "\t\t\t\t\t\t\t<dcc:name>\n" +
                "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Comparison Reference Value (En Criterion)</dcc:content>\n" +
                "\t\t\t\t\t\t\t</dcc:name>\n" +
                "\t\t\t\t\t\t\t<si:real>\n" +
                "\t\t\t\t\t\t\t\t<si:value>" + this.enRefValues.get(i).getValue() + "</si:value>\n" + //TODO vereinfachen mit SiReal method toXMLString
                "\t\t\t\t\t\t\t\t<si:unit>" + this.enRefValues.get(i).getUnit() + "</si:unit>\n" +
                "\t\t\t\t\t\t\t\t<si:expandedUnc>\n" +
                "\t\t\t\t\t\t\t\t\t<si:uncertainty>" + this.enRefValues.get(i).getExpUnc().getUncertainty() + "</si:uncertainty>\n" +
                "\t\t\t\t\t\t\t\t\t<si:coverageFactor>" + this.enRefValues.get(i).getExpUnc().getCoverageFactor() + "</si:coverageFactor>\n" +
                "\t\t\t\t\t\t\t\t\t<si:coverageProbability>" + this.enRefValues.get(i).getExpUnc().getCoverageProbability() + "</si:coverageProbability>\n" +
                "\t\t\t\t\t\t\t\t\t<si:distribution>normal</si:distribution>\n" +
                "\t\t\t\t\t\t\t\t</si:expandedUnc>\n" +
                "\t\t\t\t\t\t\t</si:real>\n" +
                "\t\t\t\t\t\t</dcc:quantity>\n" +
                "\t\t\t\t\t</dcc:data>\n" +
                "\t\t\t\t</dcc:result>\n" +
                this.bilateralEnValuesDccResult.get(i).toXMLString();
        }
        this.result +=
                "\t\t\t</dcc:results>\n" +
                "\t\t</dcc:measurementResult>\n";
    }

//    public void generateMeasurementResultsXMLString(){
//        this.generateEnCriterionSiRealXMLList();
//        this.generateParticipantMeasurementResultsXML();
//        this.generateComparisonMeasurementResultXML();
//        this.result = this.participantResultsXMLString + this.comparisonResultsXMLString;
//    }
    /**
     * This function generates for the parameter result the content of the last measurement result entry of the new DCC file
     */
//    public void generateTempKCMeasurement(String nominalTemperature, SiReal enRefValue){
//            this.result="<dcc:measurementResult refType=\"comparison_referenceValues\">\n" +
//                    "\t\t\t<dcc:name>\n" +
//                    "\t\t\t\t<dcc:content lang=\"en\">Mean deviation from reference Temperature at nominal Temperture of " + nominalTemperature + " °C</dcc:content>\n" +
//                    "\t\t\t</dcc:name>\n" +
//                    "\t\t\t<dcc:results>\n" +
//                    "\t\t\t\t<dcc:result refType=\"temperature_radianceTemperature\">\n" +
//                    "\t\t\t\t\t<dcc:name>\n" +
//                    "\t\t\t\t\t\t<dcc:content lang=\"en\"> Temperatur Reference Value</dcc:content>\n" +
//                    "\t\t\t\t\t</dcc:name>\n" +
//                    "\t\t\t\t\t<dcc:data>\n" +
//                    "\t\t\t\t\t\t<dcc:quantity refType=\"comparison_referenceValueEnCriterion\">\n" +
//                    "\t\t\t\t\t\t\t<dcc:name>\n" +
//                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Comparison Reference Value En Criterion</dcc:content>\n" +
//                    "\t\t\t\t\t\t\t</dcc:name>\n" +
//                    "\t\t\t\t\t\t\t<si:real>\n" +
//                    "\t\t\t\t\t\t\t\t<si:label>Temperatur Reference Value</si:label>\n" +
//                    "\t\t\t\t\t\t\t\t<si:value>"+enRefValue.getValue()+"</si:value>\n" +
//                    "\t\t\t\t\t\t\t\t<si:unit>"+enRefValue.getUnit()+"</si:unit>\n" +
//                    "\t\t\t\t\t\t\t\t<si:expandedUnc>\n" +
//                    "\t\t\t\t\t\t\t\t\t<si:uncertainty>"+enRefValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
//                    "\t\t\t\t\t\t\t\t\t<si:coverageFactor>"+enRefValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
//                    "\t\t\t\t\t\t\t\t\t<si:coverageProbability>"+enRefValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
//                    "\t\t\t\t\t\t\t\t\t<si:distribution>normal</si:distribution>\n" +
//                    "\t\t\t\t\t\t\t\t</si:expandedUnc>\n" +
//                    "\t\t\t\t\t\t\t</si:real>\n" +
//                    "\t\t\t\t\t\t</dcc:quantity>\n" +
//                    "\t\t\t\t\t</dcc:data>\n" +
//                    "\t\t\t\t</dcc:result>\n" +
//                    "\t\t\t</dcc:results>\n" +
//                    "\t\t</dcc:measurementResult>";
//    }


    @Override
    public String toString() {
        return this.result;
    }
}
//
///*
// * Copyright (c) 2022 - 2023 Physikalisch-Technische Bundesanstalt (PTB), all rights reserved.
// * This source code and software is free software: you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public License as published
// * by the Free Software Foundation, version 3 of the License.
// * The software is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU Lesser General Public License for more details.
// * You should have received a copy of the GNU Lesser General Public License
// * along with this XSD.  If not, see http://www.gnu.org/licenses.
// * CONTACT: 		info@ptb.de
// * DEVELOPMENT:	https://d-si.ptb.de
// * AUTHORS:		Wafa El Jaoua, Tobias Hoffmann, Clifford Brown, Daniel Hutzschenreuter
// * LAST MODIFIED:	15.08.23, 15:41
// */
//
//package de.ptb.backend.model.dsi;
//
//import de.ptb.backend.BERT.GEO;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//public class TempMeasurementResult {
//    String pidParticipant;
//    SiReal radTempValue;
//    SiReal nomTempValue;
//    SiReal sensor1Value;
//    SiReal sensor2Value;
//    SiReal indicatedTempValue;
//    SiReal referenceTempValue;
//    SiReal enValueEnCriterion;
//    SiReal enValueGrubbsTest;
//    SiReal refValueEnCriterion;
//    SiReal refValueGrubbsTest;
//    Double tempDifference;
//    String nominalTemperature;
//    //    Double kcTempValue;
////    SiReal kcValue = null;
////    Double enValue;
////    SiReal energyValue;
////    GEO grubsValue;
////    Double grubsUncertainty;
////    Double grubsComparisonValue;
//    String result = "";
//
////    /**
////     * This constructor is for every normal measurement result entry in the new DCC.
////     * @param radTempValue SiReal
////     * @param kcTempValue Double
////     * @param kcValue SiReal
////     * @param enValue Double
////     * @param energyValue SiReal
////     * @param grubsValue GEO
////     */
////    public TempMeasurementResult(SiReal radTempValue, Double kcTempValue, SiReal kcValue, Double enValue, SiReal energyValue, GEO grubsValue, String pid) {
////
////        this.radTempValue = radTempValue;
////        this.kcTempValue = kcTempValue;
////        this.kcValue = kcValue;
////        this.enValue = enValue;
////        this.energyValue = energyValue;
////        this.grubsValue = grubsValue;
////        this.pidParticipant= pid;
////        generateTempMeasurementResult();
////    }
////
////    public TempMeasurementResult(SiReal radTempValue, Double kcTempValue, Double enValue, SiReal kcValue){
////        this.radTempValue = radTempValue;
////        this.kcTempValue = kcTempValue;
////        this.kcValue = kcValue;
////        this.enValue = enValue;
////        //Change Function when not having grubsvalues
////        generateTempMeasurementResult();
////
////    }
////    /**
////     * This constructor is for the last measurement result entry in the new DCC.
////     * @param tempDifference Double which has the same value of the manipulator parameter of the function manipulateMassValues in BackendController.java
////     * @param kcTempValue Double
////     * @param kcValue Double
////     * @param grubsValue Double
////     * @param grubsUncertainty Double
////     */
////    public TempMeasurementResult(Double tempDifference, Double kcTempValue, SiReal kcValue, Double grubsValue, Double grubsUncertainty){
//////          public MeasurementResult(SiReal massDifference,SiReal energyValue,  Double kcMassValue, SiReal kcValue, Double grubsValue, Double grubsUncertainty){
////        this.kcValue = kcValue;
////        this.kcTempValue = kcTempValue;
////        this.tempDifference = tempDifference;
//////        this.energyValue= energyValue;
////        this.grubsComparisonValue = grubsValue;
////        this.grubsUncertainty = grubsUncertainty;
////
////        generateTempKCMeasurement();
////    }
////    public TempMeasurementResult(Double tempDifference, Double kcTempValue, SiReal kcValue){
////        this.kcValue = kcValue;
////        this.kcTempValue = kcTempValue;
////        this.tempDifference = tempDifference;
////        generateTempKCMeasurement();
////    }
////
////    public TempMeasurementResult(SiReal radTempValue, Double kcTempValue, Double enValue, SiReal kcValue, GEO grubsValue) {
////        this.radTempValue = radTempValue;
////        this.kcTempValue = kcTempValue;
////        this.enValue = enValue;
////        this.kcValue = kcValue;
////        this.grubsValue = grubsValue;
////        generateTempMeasurementResult();
////    }
//    /**
//     *
//     */
//    public void setParticpantTemp(SiReal radTempValue, SiReal enValueEnCriterion, SiReal enValueGrubbsTest, String pid,
//                                  SiReal nominalTempValue, SiReal sensor1Value, SiReal sensor2Value, SiReal indicatedTempValue,
//                                  SiReal referenceTempValue){
//        this.radTempValue = radTempValue;
//        this.enValueEnCriterion= enValueEnCriterion;
//        this.enValueGrubbsTest = enValueGrubbsTest;
//        this.pidParticipant= pid;
//        this.nomTempValue = nominalTempValue;
//        this.sensor1Value = sensor1Value;
//        this.sensor2Value = sensor2Value;
//        this.indicatedTempValue = indicatedTempValue;
//        this.referenceTempValue = referenceTempValue;
//        this.generateTempMeasurementResult();
//
//    }
//
//    public void setReferenceValues( SiReal refValueEnCriterion,SiReal refValueGrubbsTest){
//        this.refValueEnCriterion= refValueEnCriterion;
//        this.refValueGrubbsTest= refValueGrubbsTest;
////        this.energyValue= null;
////        this.grubsValue = new GEO();
//        this.generateTempKCMeasurement();
//    }
//
//    public void setEnReferenceValue( String nominalTemperature, SiReal refValueEnCriterion){
//        this.refValueEnCriterion= refValueEnCriterion;
//        this.nominalTemperature = nominalTemperature;
//        this.generateTempKCMeasurement();
//    }
//
//    /**
//     * This function generates for the participants results the content of one measurement result entry of the new DCC file
//     */
//    private void generateTempMeasurementResult() {
//
//        String urlPid = this.pidParticipant;
//        int lastIndex = urlPid.lastIndexOf("/");
//        if (lastIndex != -1) {
//            String extracted = urlPid.substring(lastIndex+1);
//            if (extracted.length() > 1){
//                String pid= extracted; //.substring(0,extracted.length() -1);
//                System.out.println("pidNew: " + pid);
//
//                this.result = "<dcc:measurementResult id=\"" + pid + "\">\n" + //TODO Hier klären ob refId oder id. refID gibt Fehler bei XML validierung
//                        "      <dcc:name>\n" +
//                        "        <dcc:content lang=\"en\">Comparison results of participant laboratory: " + this.radTempValue.getName() + "</dcc:content>\n" +
//                        "      </dcc:name>\n" +
//                        "      <dcc:results>\n" +
//                        "        <dcc:result refType=\"temperature_radianceTemperature\">\n" +
//                        "          <dcc:name>\n" +
//                        "            <dcc:content lang=\"en\">Deviation from reference temperature</dcc:content>\n" +
//                        "          </dcc:name>\n" +
//                        "          <dcc:data>\n" +
//                        "            <dcc:quantity refType=\"basic_nominalValue\">\n" +
//                        "              <dcc:name>\n" +
//                        "                <dcc:content lang=\"en\">Set temperature</dcc:content>\n" +
//                        "              </dcc:name>\n" +
//                        "              <si:real>\n" +
//                        "                <si:label>Temperatur</si:label>\n" +
//                        "                <si:value>" + this.nomTempValue.getValue() + "</si:value>\n" +
//                        "                <si:unit>" + this.nomTempValue.getUnit() + "</si:unit>\n" +
//                        "              </si:real>\n" +
//                        "            </dcc:quantity>\n" +
//                        "            <dcc:quantity refType=\"basic_measuredValueSensor1\">\n" +
//                        "              <dcc:name>\n" +
//                        "                <dcc:content lang=\"en\">average resistance of monitor PRT</dcc:content>\n" +
//                        "              </dcc:name>\n" +
//                        "              <si:real>\n" +
//                        "                <si:label>Temperatur</si:label>\n" +
//                        "                <si:value>" + this.sensor1Value.getValue() + "</si:value>\n" +
//                        "                <si:unit>" + this.sensor1Value.getUnit() + "</si:unit>\n" +
//                        "                <si:expandedUnc>\n" +
//                        "                   <si:uncertainty>" + this.sensor1Value.getExpUnc().getUncertainty() + "</si:uncertainty>\n" +
//                        "                   <si:coverageFactor>" + this.sensor1Value.getExpUnc().getCoverageFactor() + "</si:coverageFactor>\n" +
//                        "                   <si:coverageProbability>" + this.sensor1Value.getExpUnc().getCoverageProbability() + "</si:coverageProbability>\n" +
//                        "                   <si:distribution>normal</si:distribution>\n" +
//                        "                </si:expandedUnc>\n" +
//                        "              </si:real>\n" +
//                        "            </dcc:quantity>\n" +
//                        "            <dcc:quantity refType=\"basic_measuredValueSensor2\">\n" +
//                        "              <dcc:name>\n" +
//                        "                <dcc:content lang=\"en\">average resistance of monitor NTC Thermistor</dcc:content>\n" +
//                        "              </dcc:name>\n" +
//                        "              <si:real>\n" +
//                        "                <si:label>Temperatur</si:label>\n" +
//                        "                <si:value>" + this.sensor2Value.getValue() + "</si:value>\n" +
//                        "                <si:unit>" + this.sensor2Value.getUnit() + "</si:unit>\n" +
//                        "                <si:expandedUnc>\n" +
//                        "                   <si:uncertainty>" + this.sensor2Value.getExpUnc().getUncertainty() + "</si:uncertainty>\n" +
//                        "                   <si:coverageFactor>" + this.sensor2Value.getExpUnc().getCoverageFactor() + "</si:coverageFactor>\n" +
//                        "                   <si:coverageProbability>" + this.sensor2Value.getExpUnc().getCoverageProbability() + "</si:coverageProbability>\n" +
//                        "                   <si:distribution>normal</si:distribution>\n" +
//                        "                </si:expandedUnc>\n" +
//                        "              </si:real>\n" +
//                        "            </dcc:quantity>\n" +
//                        "            <dcc:quantity refType=\"temperature_indicatedValue\">\n" +
//                        "              <dcc:name>\n" +
//                        "                <dcc:content lang=\"en\">average indicated temperature</dcc:content>\n" +
//                        "              </dcc:name>\n" +
//                        "              <si:real>\n" +
//                        "                <si:label>Temperatur</si:label>\n" +
//                        "                <si:value>" + this.indicatedTempValue.getValue() + "</si:value>\n" +
//                        "                <si:unit>" + this.indicatedTempValue.getUnit() + "</si:unit>\n" +
//                        "              </si:real>\n" +
//                        "            </dcc:quantity>\n" +
//                        "            <dcc:quantity refType=\"temperature_referenceValue\">\n" +
//                        "              <dcc:name>\n" +
//                        "                <dcc:content lang=\"en\">Temperature Reference calculated from pt-100 Resistance</dcc:content>\n" +
//                        "              </dcc:name>\n" +
//                        "              <si:real>\n" +
//                        "                <si:label>Temperatur</si:label>\n" +
//                        "                <si:value>" + this.referenceTempValue.getValue() + "</si:value>\n" +
//                        "                <si:unit>" + this.referenceTempValue.getUnit() + "</si:unit>\n" +
//                        "                <si:expandedUnc>\n" +
//                        "                   <si:uncertainty>" + this.referenceTempValue.getExpUnc().getUncertainty() + "</si:uncertainty>\n" +
//                        "                   <si:coverageFactor>" + this.referenceTempValue.getExpUnc().getCoverageFactor() + "</si:coverageFactor>\n" +
//                        "                   <si:coverageProbability>" + this.referenceTempValue.getExpUnc().getCoverageProbability() + "</si:coverageProbability>\n" +
//                        "                   <si:distribution>normal</si:distribution>\n" +
//                        "                </si:expandedUnc>\n" +
//                        "              </si:real>\n" +
//                        "            </dcc:quantity>\n" +
//                        "            <dcc:quantity refType=\"basic_measuredValue basic_arithmenticMean temperature_ITS-90\">\n" +
//                        "              <dcc:name>\n" +
//                        "                <dcc:content lang=\"en\">average radiance temperature</dcc:content>\n" +
//                        "              </dcc:name>\n" +
//                        "              <si:real>\n" +
//                        "                <si:label>Temperatur</si:label>\n" +
//                        "                <si:value>" + this.radTempValue.getValue() + "</si:value>\n" +
//                        "                <si:unit>" + this.radTempValue.getUnit() + "</si:unit>\n" +
//                        "                <si:expandedUnc>\n" +
//                        "                   <si:uncertainty>" + this.radTempValue.getExpUnc().getUncertainty() + "</si:uncertainty>\n" +
//                        "                   <si:coverageFactor>" + this.radTempValue.getExpUnc().getCoverageFactor() + "</si:coverageFactor>\n" +
//                        "                   <si:coverageProbability>" + this.radTempValue.getExpUnc().getCoverageProbability() + "</si:coverageProbability>\n" +
//                        "                   <si:distribution>normal</si:distribution>\n" +
//                        "                </si:expandedUnc>\n" +
//                        "              </si:real>\n" +
//                        "            </dcc:quantity>\n" +
//                        "          </dcc:data>\n" +
//                        "        </dcc:result>\n" +
//                        "        <dcc:result refType=\"comparison_equivalenceValue\">\n" +
//                        "          <dcc:name>\n" +
//                        "            <dcc:content lang=\"en\">Equivalence value</dcc:content>\n" +
//                        "          </dcc:name>\n" +
//                        "          <dcc:data>\n" +
//                        "            <dcc:quantity refType=\"comparison_equivalenceValueEnCriterion\">\n" +
//                        "              <dcc:name>\n" +
//                        "                <dcc:content lang=\"en\">En criterion value</dcc:content>\n" +
//                        "              </dcc:name>\n" +
//                        "              <si:real>\n" +
//                        "                <si:value>" + this.enValueEnCriterion.getValue() + "</si:value>\n" +
//                        "                <si:unit>" + this.enValueEnCriterion.getUnit() + "</si:unit>\n" +
//                        "              </si:real>\n" +
//                        "            </dcc:quantity>\n" +
//                        "          </dcc:data>\n" +
//                        "        </dcc:result>\n" +
//                        "      </dcc:results>\n" +
//                        "    </dcc:measurementResult>\n ";
//
//            }
//        }
//    }
//    /**
//     * This function generates for the parameter result the content of the last measurement result entry of the new DCC file
//     */
//    public void generateTempKCMeasurement(){
//        this.result="<dcc:measurementResult refType=\"comparison_referenceValues\">\n" +
//                "\t\t\t<dcc:name>\n" +
//                "\t\t\t\t<dcc:content lang=\"en\">Mean deviation from reference Temperature at nominal Temperture of " + this.nominalTemperature + " °C</dcc:content>\n" +
//                "\t\t\t</dcc:name>\n" +
//                "\t\t\t<dcc:results>\n" +
//                "\t\t\t\t<dcc:result refType=\"temperature_radianceTemperature\">\n" +
//                "\t\t\t\t\t<dcc:name>\n" +
//                "\t\t\t\t\t\t<dcc:content lang=\"en\"> Temperatur Reference Value</dcc:content>\n" +
//                "\t\t\t\t\t</dcc:name>\n" +
//                "\t\t\t\t\t<dcc:data>\n" +
//                "\t\t\t\t\t\t<dcc:quantity refType=\"comparison_referenceValueEnCriterion\">\n" +
//                "\t\t\t\t\t\t\t<dcc:name>\n" +
//                "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Comparison Reference Value En Criterion</dcc:content>\n" +
//                "\t\t\t\t\t\t\t</dcc:name>\n" +
//                "\t\t\t\t\t\t\t<si:real>\n" +
//                "\t\t\t\t\t\t\t\t<si:label>Temperatur Reference Value</si:label>\n" +
//                "\t\t\t\t\t\t\t\t<si:value>"+this.refValueEnCriterion.getValue()+"</si:value>\n" +
//                "\t\t\t\t\t\t\t\t<si:unit>"+this.refValueEnCriterion.getUnit()+"</si:unit>\n" +
//                "\t\t\t\t\t\t\t\t<si:expandedUnc>\n" +
//                "\t\t\t\t\t\t\t\t\t<si:uncertainty>"+this.refValueEnCriterion.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
//                "\t\t\t\t\t\t\t\t\t<si:coverageFactor>"+this.refValueEnCriterion.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
//                "\t\t\t\t\t\t\t\t\t<si:coverageProbability>"+this.refValueEnCriterion.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
//                "\t\t\t\t\t\t\t\t\t<si:distribution>normal</si:distribution>\n" +
//                "\t\t\t\t\t\t\t\t</si:expandedUnc>\n" +
//                "\t\t\t\t\t\t\t</si:real>\n" +
//                "\t\t\t\t\t\t</dcc:quantity>\n" +
//                "\t\t\t\t\t</dcc:data>\n" +
//                "\t\t\t\t</dcc:result>\n" +
//                "\t\t\t</dcc:results>\n" +
//                "\t\t</dcc:measurementResult>";
////        }
////        else{
////            this.result = "         <dcc:measurementResult refId=\"comparison_referenceValues\">\n" +
////                    "       <dcc:name>\n" +
////                    "           <dcc:content lang=\"en\">Temperatur of Output Reference Values</dcc:content>\n" +
////                    "       </dcc:name>\n" +
////                    "       <dcc:results>\n" +
////                    "           <dcc:result refType=\"RefMassUnc\">\n" +
////                    "           <dcc:name>\n" +
////                    "               <dcc:content lang=\"en\">Reference Output Values</dcc:content>\n" +
////                    "           </dcc:name>\n" +
////                    "           <dcc:data>\n" +
////                    "               <dcc:quantity>\n" +
////                    "                   <si:real>\n" +
////                    "                       <si:value>"+this.kcTempValue +"</si:value>\n" +
////                    "                       <si:unit>/gram</si:unit>\n" +
////                    "                       <si:expandedUnc>\n" +
////                    "                           <si:uncertainty>0.00002535</si:uncertainty>\n" +
////                    "                           <si:coverageFactor>2</si:coverageFactor>\n" +
////                    "                           <si:coverageProbability>0.95</si:coverageProbability>\n" +
////                    "                           <si:distribution>normal</si:distribution>\n" +
////                    "                       </si:expandedUnc>\n" +
////                    "                   </si:real>\n" +
////                    "               </dcc:quantity>\n" +
////                    "           </dcc:data>\n" +
////                    "           </dcc:result>\n" +
////                    "           <dcc:result refType=\"Energy\">\n" +
////                    "               <dcc:name>\n" +
////                    "                   <dcc:content lang=\"en\">Energy</dcc:content>\n" +
////                    "               </dcc:name>\n" +
////                    "               <dcc:data>\n" +
////                    "                   <dcc:quantity refType=\"Energy\">\n" +
////                    "                   <dcc:name>\n" +
////                    "                       <dcc:content lang=\"en\">Energy</dcc:content>\n" +
////                    "                   </dcc:name>\n" +
////                    "                       <si:real>\n" +
////                    "                           <si:label>Energy</si:label>\n" +
////                    "                           <si:value>"+this.kcValue.getValue()+"</si:value>\n" +
////                    "                           <si:unit>"+this.kcValue.getUnit()+"</si:unit>\n" +
////                    "                           <si:expandedUnc>\n" +
////                    "                               <si:uncertainty>"+this.kcValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
////                    "                               <si:coverageFactor>"+this.kcValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
////                    "                               <si:coverageProbability>"+this.kcValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
////                    "                               <si:distribution>normal</si:distribution>\n" +
////                    "                           </si:expandedUnc>\n" +
////                    "                       </si:real>\n" +
////                    "                   </dcc:quantity>\n" +
////                    "               </dcc:data>\n" +
////                    "           </dcc:result>\n" +
////                    "           <dcc:result refType=\"Energy (Grubs)\">\n" +
////                    "               <dcc:name>\n" +
////                    "                   <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
////                    "               </dcc:name>\n" +
////                    "               <dcc:data>\n" +
////                    "                   <dcc:quantity refType=\"Energy (Grubs)\">\n" +
////                    "                       <dcc:name>\n" +
////                    "                           <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
////                    "                       </dcc:name>\n" +
////                    "                       <si:real>\n" +
////                    "                           <si:label>Energy (Grubs)</si:label>\n" +
////                    "                           <si:value>"+this.grubsComparisonValue+"</si:value>\n" +
////                    "                           <si:unit>"+this.kcValue.getUnit()+"</si:unit>\n" +
////                    "                           <si:expandedUnc>\n" +
////                    "                               <si:uncertainty>"+this.grubsUncertainty+"</si:uncertainty>\n" +
////                    "                               <si:coverageFactor>"+this.kcValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
////                    "                               <si:coverageProbability>"+this.kcValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
////                    "                               <si:distribution>normal</si:distribution>\n" +
////                    "                           </si:expandedUnc>\n" +
////                    "                       </si:real>\n" +
////                    "                   </dcc:quantity>\n" +
////                    "                </dcc:data>\n" +
////                    "           </dcc:result>\n" +
////                    "       </dcc:results>\n" +
////                    "    </dcc:measurementResult>\n";
////        }
//    }
//
//
//    @Override
//    public String toString() {
//        return result;
//    }
//}

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

import de.ptb.backend.BERT.GEO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TempMeasurementResult {
    String pidParticipant;
    SiReal tempValue;
//    TODO massDifference ablöst
//  SiReal massDifference;
    SiReal enValueEnCriterion;
    SiReal enValueGrubbsTest;
    SiReal refValueEnCriterion;
    SiReal refValueGrubbsTest;
    Double tempDifference;
    Double kcTempValue;
    SiReal kcValue = null;
    Double enValue;
    SiReal energyValue;
    GEO grubsValue;
    Double grubsUncertainty;
    Double grubsComparisonValue;
    String result = "";

    /**
     * This constructor is for every normal measurement result entry in the new DCC.
     * @param tempValue SiReal
     * @param kcTempValue Double
     * @param kcValue SiReal
     * @param enValue Double
     * @param energyValue SiReal
     * @param grubsValue GEO
     */
    public TempMeasurementResult(SiReal tempValue, Double kcTempValue, SiReal kcValue, Double enValue, SiReal energyValue, GEO grubsValue, String pid) {

        this.tempValue = tempValue;
        this.kcTempValue = kcTempValue;
        this.kcValue = kcValue;
        this.enValue = enValue;
        this.energyValue = energyValue;
        this.grubsValue = grubsValue;
        this.pidParticipant= pid;
        generateTempMeasurementResult();
    }

    public TempMeasurementResult(SiReal tempValue, Double kcTempValue, Double enValue, SiReal kcValue){
        this.tempValue = tempValue;
        this.kcTempValue = kcTempValue;
        this.kcValue = kcValue;
        this.enValue = enValue;
        //Change Function when not having grubsvalues
        generateTempMeasurementResult();

    }
    /**
     * This constructor is for the last measurement result entry in the new DCC.
     * @param tempDifference Double which has the same value of the manipulator parameter of the function manipulateMassValues in BackendController.java
     * @param kcTempValue Double
     * @param kcValue Double
     * @param grubsValue Double
     * @param grubsUncertainty Double
     */
    public TempMeasurementResult(Double tempDifference, Double kcTempValue, SiReal kcValue, Double grubsValue, Double grubsUncertainty){
//          public MeasurementResult(SiReal massDifference,SiReal energyValue,  Double kcMassValue, SiReal kcValue, Double grubsValue, Double grubsUncertainty){
        this.kcValue = kcValue;
        this.kcTempValue = kcTempValue;
        this.tempDifference = tempDifference;
//        this.energyValue= energyValue;
        this.grubsComparisonValue = grubsValue;
        this.grubsUncertainty = grubsUncertainty;

        generateTempKCMeasurement();
    }
    public TempMeasurementResult(Double tempDifference, Double kcTempValue, SiReal kcValue){
        this.kcValue = kcValue;
        this.kcTempValue = kcTempValue;
        this.tempDifference = tempDifference;
        generateTempKCMeasurement();
    }

    public TempMeasurementResult(SiReal tempValue, Double kcTempValue, Double enValue, SiReal kcValue, GEO grubsValue) {
        this.tempValue = tempValue;
        this.kcTempValue = kcTempValue;
        this.enValue = enValue;
        this.kcValue = kcValue;
        this.grubsValue = grubsValue;
        generateTempMeasurementResult();
    }
    /**
     *
     */
    public void setParticpantMass(SiReal tempValue, SiReal enValueEnCriterion,SiReal enValueGrubbsTest, String pid){
        this.tempValue = tempValue;
        this.enValueEnCriterion= enValueEnCriterion;
        this.enValueGrubbsTest=enValueGrubbsTest;
        this.energyValue= null;
        this.grubsValue = new GEO();
        this.pidParticipant= pid;
        this.generateTempMeasurementResult();

    }
    public void setReferenceValues( SiReal refValueEnCriterion,SiReal refValueGrubbsTest){
        this.refValueEnCriterion= refValueEnCriterion;
        this.refValueGrubbsTest= refValueGrubbsTest;
        this.energyValue= null;
        this.grubsValue = new GEO();
        this.generateTempKCMeasurement();
    }
    /**
     * This function generates for the participants results the content of one measurement result entry of the new DCC file
     */
    private void generateTempMeasurementResult() {

        String urlPid = this.pidParticipant;
        int lastIndex = urlPid.lastIndexOf("/");
        if (lastIndex != -1) {
            String extracted = urlPid.substring(lastIndex+1);
            if (extracted.length() > 1){
                String pid= extracted.substring(0,extracted.length() -1);
                System.out.println("pidNew: " + pid);

                this.result = "<dcc:measurementResult refId=\"" + pid + "\">\n" +
                        "      <dcc:name>\n" +
                        "        <dcc:content lang=\"en\">Comparison results of participant laboratory: " + this.tempValue.getName() + "</dcc:content>\n" +
                        "      </dcc:name>\n" +
                        "      <dcc:results>\n" +
                        "        <dcc:result refType=\"temperature_radianceTemperature\">\n" +
                        "          <dcc:name>\n" +
                        "            <dcc:content lang=\"en\">Temperatur of .....</dcc:content>\n" +
                        "          </dcc:name>\n" +
                        "          <dcc:data>\n" +
                        "            <dcc:quantity refType=\"basic_measuredValue\">\n" +
                        "              <dcc:name>\n" +
                        "                <dcc:content lang=\"en\">Measured value</dcc:content>\n" +
                        "              </dcc:name>\n" +
                        "              <si:real>\n" +
                        "                <si:label>Temperatur</si:label>\n" +
                        "                <si:value>" + this.tempValue.getValue() + "</si:value>\n" +
                        "                <si:unit>" + this.tempValue.getUnit() + "</si:unit>\n" +
                        "                <si:expandedUnc>\n" +
                        "                   <si:uncertainty>" + this.tempValue.getExpUnc().getUncertainty() + "</si:uncertainty>\n" +
                        "                   <si:coverageFactor>" + this.tempValue.getExpUnc().getCoverageFactor() + "</si:coverageFactor>\n" +
                        "                   <si:coverageProbability>" + this.tempValue.getExpUnc().getCoverageProbability() + "</si:coverageProbability>\n" +
                        "                   <si:distribution>normal</si:distribution>\n" +
                        "                </si:expandedUnc>\n" +
                        "              </si:real>\n" +
                        "            </dcc:quantity>\n" +
                        "          </dcc:data>\n" +
                        "        </dcc:result>\n" +
                        "        <dcc:result refType=\"comparison_equivalenceValue\">\n" +
                        "          <dcc:name>\n" +
                        "            <dcc:content lang=\"en\">Equivalence value</dcc:content>\n" +
                        "          </dcc:name>\n" +
                        "          <dcc:data>\n" +
                        "            <dcc:quantity refType=\"comparison_equivalenceValueEnCriterion\">\n" +
                        "              <dcc:name>\n" +
                        "                <dcc:content lang=\"en\">En criterion value</dcc:content>\n" +
                        "              </dcc:name>\n" +
                        "              <si:real>\n" +
                        "                <si:value>" + this.enValue + "</si:value>\n" +
                        "                <si:unit>\\one</si:unit>\n" +
                        "              </si:real>\n" +
                        "            </dcc:quantity>\n" +
                        "          </dcc:data>\n" +
                        "        </dcc:result>\n" +
                        "      </dcc:results>\n" +
                        "    </dcc:measurementResult>\n ";

            }
        }
    }
    /**
     * This function generates for the parameter result the content of the last measurement result entry of the new DCC file
     */
    public void generateTempKCMeasurement(){
        if(this.grubsValue== null){
            this.result = "         <dcc:measurementResult refId=\"comparison_referenceValues\">\n" +
                    "       <dcc:name>\n" +
                    "           <dcc:content lang=\"en\"> Temperatur of ......Output Reference Value</dcc:content>\n" +
                    "       </dcc:name>\n" +
                    "       <dcc:results>\n" +
                    "           <dcc:result refType=\"RefMassUnc\">\n" +
                    "           <dcc:name>\n" +
                    "               <dcc:content lang=\"en\">Reference Output Values</dcc:content>\n" +
                    "           </dcc:name>\n" +
                    "           <dcc:data>\n" +
                    "               <dcc:quantity>\n" +
                    "                   <si:real>\n" +
                    "                       <si:value>"+this.kcTempValue +"</si:value>\n" +
                    "                       <si:unit>/gram</si:unit>\n" +
                    "                       <si:expandedUnc>\n" +
                    "                           <si:uncertainty>0.00002535</si:uncertainty>\n" +
                    "                           <si:coverageFactor>2</si:coverageFactor>\n" +
                    "                           <si:coverageProbability>0.95</si:coverageProbability>\n" +
                    "                           <si:distribution>normal</si:distribution>\n" +
                    "                       </si:expandedUnc>\n" +
                    "                   </si:real>\n" +
                    "               </dcc:quantity>\n" +
                    "           </dcc:data>\n" +
                    "           </dcc:result>\n" +
                    "       </dcc:results>\n" +
                    "    </dcc:measurementResult>\n";
        }else if (this.energyValue == null) {
            this.result="<dcc:measurementResult refId=\"comparison_referenceValues\">\n" +
                    "\t\t\t<dcc:name>\n" +
                    "\t\t\t\t<dcc:content lang=\"en\">Temperatur of ... Output Reference Values</dcc:content>\n" +
                    "\t\t\t</dcc:name>\n" +
                    "\t\t\t<dcc:results>\n" +
                    "\t\t\t\t<dcc:result refType=\"temperature_radianceTemperature\">\n" +
                    "\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t<dcc:content lang=\"en\"> Temperatur Reference Value</dcc:content>\n" +
                    "\t\t\t\t\t</dcc:name>\n" +
                    "\t\t\t\t\t<dcc:data>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"comparison_referenceValueEnCriterion\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Comparison Reference Value En Criterion</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                    "\t\t\t\t\t\t\t<si:real>\n" +
                    "\t\t\t\t\t\t\t\t<si:label>Temperatur Reference Value</si:label>\n" +
                    "\t\t\t\t\t\t\t\t<si:value>"+this.refValueEnCriterion.getValue()+"</si:value>\n" +
                    "\t\t\t\t\t\t\t\t<si:unit>"+this.refValueEnCriterion.getUnit()+"</si:unit>\n" +
                    "\t\t\t\t\t\t\t\t<si:expandedUnc>\n" +
                    "\t\t\t\t\t\t\t\t\t<si:uncertainty>"+this.refValueEnCriterion.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                    "\t\t\t\t\t\t\t\t\t<si:coverageFactor>"+this.refValueEnCriterion.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                    "\t\t\t\t\t\t\t\t\t<si:coverageProbability>"+this.refValueEnCriterion.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                    "\t\t\t\t\t\t\t\t\t<si:distribution>normal</si:distribution>\n" +
                    "\t\t\t\t\t\t\t\t</si:expandedUnc>\n" +
                    "\t\t\t\t\t\t\t</si:real>\n" +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t</dcc:data>\n" +
                    "\t\t\t\t</dcc:result>\n" +
//                    "\t\t\t\t<dcc:result refType=\"mass_mass\">\n" +
//                    "\t\t\t\t\t<dcc:name>\n" +
//                    "\t\t\t\t\t\t<dcc:content lang=\"en\">Mass Reference Value</dcc:content>\n" +
//                    "\t\t\t\t\t</dcc:name>\n" +
//                    "\t\t\t\t\t<dcc:data>\n" +
//                    "\t\t\t\t\t\t<dcc:quantity refType=\"comparison_referenceValueGrubbsTest\">\n" +
//                    "\t\t\t\t\t\t\t<dcc:name>\n" +
//                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Comparison Reference Value Grubbs</dcc:content>\n" +
//                    "\t\t\t\t\t\t\t</dcc:name>\n" +
//                    "\t\t\t\t\t\t\t<si:real>\n" +
//                    "\t\t\t\t\t\t\t\t<si:label>Mass Reference Value</si:label>\n" +
//                    "\t\t\t\t\t\t\t\t<si:value>"+this.refValueGrubbsTest.getValue()+"</si:value>\n" +
//                    "\t\t\t\t\t\t\t\t<si:unit>"+this.refValueGrubbsTest.getUnit()+"</si:unit>\n" +
//                    "\t\t\t\t\t\t\t\t<si:expandedUnc>\n" +
//                    "\t\t\t\t\t\t\t\t\t<si:uncertainty>"+this.refValueGrubbsTest.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
//                    "\t\t\t\t\t\t\t\t\t<si:coverageFactor>"+this.refValueGrubbsTest.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
//                    "\t\t\t\t\t\t\t\t\t<si:coverageProbability>"+this.refValueGrubbsTest.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
//                    "\t\t\t\t\t\t\t\t\t<si:distribution>normal</si:distribution>\n" +
//                    "\t\t\t\t\t\t\t\t</si:expandedUnc>\n" +
//                    "\t\t\t\t\t\t\t</si:real>\n" +
//                    "\t\t\t\t\t\t</dcc:quantity>\n" +
//                    "\t\t\t\t\t</dcc:data>\n" +
//                    "\t\t\t\t</dcc:result>\n" +
                    "\t\t\t</dcc:results>\n" +
                    "\t\t</dcc:measurementResult>";
        }
        else{
            this.result = "         <dcc:measurementResult refId=\"comparison_referenceValues\">\n" +
                    "       <dcc:name>\n" +
                    "           <dcc:content lang=\"en\">Temperatur of Output Reference Values</dcc:content>\n" +
                    "       </dcc:name>\n" +
                    "       <dcc:results>\n" +
                    "           <dcc:result refType=\"RefMassUnc\">\n" +
                    "           <dcc:name>\n" +
                    "               <dcc:content lang=\"en\">Reference Output Values</dcc:content>\n" +
                    "           </dcc:name>\n" +
                    "           <dcc:data>\n" +
                    "               <dcc:quantity>\n" +
                    "                   <si:real>\n" +
                    "                       <si:value>"+this.kcTempValue +"</si:value>\n" +
                    "                       <si:unit>/gram</si:unit>\n" +
                    "                       <si:expandedUnc>\n" +
                    "                           <si:uncertainty>0.00002535</si:uncertainty>\n" +
                    "                           <si:coverageFactor>2</si:coverageFactor>\n" +
                    "                           <si:coverageProbability>0.95</si:coverageProbability>\n" +
                    "                           <si:distribution>normal</si:distribution>\n" +
                    "                       </si:expandedUnc>\n" +
                    "                   </si:real>\n" +
                    "               </dcc:quantity>\n" +
                    "           </dcc:data>\n" +
                    "           </dcc:result>\n" +
                    "           <dcc:result refType=\"Energy\">\n" +
                    "               <dcc:name>\n" +
                    "                   <dcc:content lang=\"en\">Energy</dcc:content>\n" +
                    "               </dcc:name>\n" +
                    "               <dcc:data>\n" +
                    "                   <dcc:quantity refType=\"Energy\">\n" +
                    "                   <dcc:name>\n" +
                    "                       <dcc:content lang=\"en\">Energy</dcc:content>\n" +
                    "                   </dcc:name>\n" +
                    "                       <si:real>\n" +
                    "                           <si:label>Energy</si:label>\n" +
                    "                           <si:value>"+this.kcValue.getValue()+"</si:value>\n" +
                    "                           <si:unit>"+this.kcValue.getUnit()+"</si:unit>\n" +
                    "                           <si:expandedUnc>\n" +
                    "                               <si:uncertainty>"+this.kcValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                    "                               <si:coverageFactor>"+this.kcValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                    "                               <si:coverageProbability>"+this.kcValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                    "                               <si:distribution>normal</si:distribution>\n" +
                    "                           </si:expandedUnc>\n" +
                    "                       </si:real>\n" +
                    "                   </dcc:quantity>\n" +
                    "               </dcc:data>\n" +
                    "           </dcc:result>\n" +
                    "           <dcc:result refType=\"Energy (Grubs)\">\n" +
                    "               <dcc:name>\n" +
                    "                   <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
                    "               </dcc:name>\n" +
                    "               <dcc:data>\n" +
                    "                   <dcc:quantity refType=\"Energy (Grubs)\">\n" +
                    "                       <dcc:name>\n" +
                    "                           <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
                    "                       </dcc:name>\n" +
                    "                       <si:real>\n" +
                    "                           <si:label>Energy (Grubs)</si:label>\n" +
                    "                           <si:value>"+this.grubsComparisonValue+"</si:value>\n" +
                    "                           <si:unit>"+this.kcValue.getUnit()+"</si:unit>\n" +
                    "                           <si:expandedUnc>\n" +
                    "                               <si:uncertainty>"+this.grubsUncertainty+"</si:uncertainty>\n" +
                    "                               <si:coverageFactor>"+this.kcValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                    "                               <si:coverageProbability>"+this.kcValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                    "                               <si:distribution>normal</si:distribution>\n" +
                    "                           </si:expandedUnc>\n" +
                    "                       </si:real>\n" +
                    "                   </dcc:quantity>\n" +
                    "                </dcc:data>\n" +
                    "           </dcc:result>\n" +
                    "       </dcc:results>\n" +
                    "    </dcc:measurementResult>\n";
        }
    }


    @Override
    public String toString() {
        return result;
    }
}

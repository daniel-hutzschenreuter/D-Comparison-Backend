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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
public class MeasurementResult {
    String pidParticipant;
    SiReal massValue;
    SiReal enValueEnCriterion;
    SiReal enValueGrubbsTest;
    SiReal refValueEnCriterion;
    SiReal refValueGrubbsTest;
    Double massDifference;
    Double kcMassValue;
    SiReal kcValue = null;
    Double enValue;
    SiReal energyValue;
    GEO grubsValue;
    Double grubsUncertainty;
    Double grubsComparisonValue;
    String result = "";

    /**
     * This constructor is for every normal measurement result entry in the new DCC.
     * @param massValue SiReal
     * @param kcMassValue Double
     * @param kcValue SiReal
     * @param enValue Double
     * @param energyValue SiReal
     * @param grubsValue GEO
     */
    public MeasurementResult(SiReal massValue, Double kcMassValue, SiReal kcValue, Double enValue, SiReal energyValue, GEO grubsValue) {
        this.massValue = massValue;
        this.kcMassValue = kcMassValue;
        this.kcValue = kcValue;
        this.enValue = enValue;
        this.energyValue = energyValue;
        this.grubsValue = grubsValue;
        generateMeasurementResult();
    }

    public MeasurementResult(SiReal massValue, Double kcMassValue, Double enValue, SiReal kcValue){
        this.massValue = massValue;
        this.kcMassValue = kcMassValue;
        this.kcValue = kcValue;
        this.enValue = enValue;
        //Change Fuction when not having grubsvalues
        generateMeasurementResult();

    }
    /**
     * This constructor is for the last measurement result entry in the new DCC.
     * @param massDifference Double which has the same value of the manipulator parameter of the function manipulateMassValues in BackendController.java
     * @param kcMassValue Double
     * @param kcValue Double
     * @param grubsValue Double
     * @param grubsUncertainty Double
     */
    public MeasurementResult(Double massDifference, Double kcMassValue, SiReal kcValue, Double grubsValue, Double grubsUncertainty){
        this.kcValue = kcValue;
        this.kcMassValue = kcMassValue;
        this.massDifference = massDifference;
        this.grubsComparisonValue = grubsValue;
        this.grubsUncertainty = grubsUncertainty;
        generateKCMeasurement();
    }
    public MeasurementResult(Double massDifference, Double kcMassValue, SiReal kcValue){
        this.kcValue = kcValue;
        this.kcMassValue = kcMassValue;
        this.massDifference = massDifference;
        generateKCMeasurement();
    }

    public MeasurementResult(SiReal massValue, Double kcMassValue, Double enValue, SiReal kcValue, GEO grubsValue) {
        this.massValue = massValue;
        this.kcMassValue = kcMassValue;
        this.enValue = enValue;
        this.kcValue = kcValue;
        this.grubsValue = grubsValue;
        generateMeasurementResult();
    }
    /**
     *
     */
    public void setParticpantMass(SiReal massValue, SiReal enValueEnCriterion,SiReal enValueGrubbsTest, String pid){
        this.massValue= massValue;
        this.enValueEnCriterion= enValueEnCriterion;
        this.enValueGrubbsTest=enValueGrubbsTest;
        this.energyValue= null;
        this.grubsValue = new GEO();
        this.pidParticipant= pid;
        this.generateMeasurementResult();

    }
    public void setReferenceValues( SiReal refValueEnCriterion,SiReal refValueGrubbsTest){
        this.refValueEnCriterion= refValueEnCriterion;
        this.refValueGrubbsTest= refValueGrubbsTest;
        this.energyValue= null;
        this.grubsValue = new GEO();
        this.generateKCMeasurement();
    }
    /**
     * This function generates for the participants results the content of one measurement result entry of the new DCC file
     */
    private void generateMeasurementResult() {
//        int start = this.massValue.getName().indexOf('(');
//        int end = this.massValue.getName().lastIndexOf(')');
//        if (start != -1 && end != -1 && start < end) {
//            String labName = this.massValue.getName().substring(start + 1, end);
//            System.out.println("labName: " + labName);
            String urlPid = this.pidParticipant;
            int lastIndex = urlPid.lastIndexOf("/");
            if (lastIndex != -1) {
            String extracted = urlPid.substring(lastIndex+1);
            if (extracted.length() > 1){
            String pid= extracted.substring(0,extracted.length() -1);
            System.out.println("pidNew: " + pid);
//            System.out.println("pid: "+ this.pidParticipant.substring(this.pidParticipant.lastIndexOf("/")+1,this.pidParticipant.lastIndexOf(this.pidParticipant.length()+1)));

//            System.out.println("pid: "+ this.pidParticipant.substring(this.pidParticipant.lastIndexOf("/")+1,this.pidParticipant.lastIndexOf(this.pidParticipant.length()+1)));
            if (this.grubsValue == null) {
                this.result = "<dcc:measurementResult refId=\"" + pid + "\">\n" +
                        "      <dcc:name>\n" +
                        "        <dcc:content lang=\"en\">Comparison results of participant laboratory: " + this.massValue.getName() + "</dcc:content>\n" +
                        "      </dcc:name>\n" +
                        "      <dcc:results>\n" +
                        "        <dcc:result refType=\"mass_mass\">\n" +
                        "          <dcc:name>\n" +
                        "            <dcc:content lang=\"en\">Mass of stainless steel cylinder</dcc:content>\n" +
                        "          </dcc:name>\n" +
                        "          <dcc:data>\n" +
                        "            <dcc:quantity refType=\"basic_measuredValue\">\n" +
                        "              <dcc:name>\n" +
                        "                <dcc:content lang=\"en\">Measured value</dcc:content>\n" +
                        "              </dcc:name>\n" +
                        "              <si:real>\n" +
                        "                <si:label>Mass</si:label>\n" +
                        "                <si:value>" + this.massValue.getValue() + "</si:value>\n" +
                        "                <si:unit>" + this.massValue.getUnit() + "</si:unit>\n" +
                        "                <si:expandedUnc>\n" +
                        "                   <si:uncertainty>" + this.massValue.getExpUnc().getUncertainty() + "</si:uncertainty>\n" +
                        "                   <si:coverageFactor>" + this.massValue.getExpUnc().getCoverageFactor() + "</si:coverageFactor>\n" +
                        "                   <si:coverageProbability>" + this.massValue.getExpUnc().getCoverageProbability() + "</si:coverageProbability>\n" +
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
            } else if (this.energyValue == null) {

                this.result = "<dcc:measurementResult refId=\"" + pid + "\">\n" +
                        "      <dcc:name>\n" +
                        "        <dcc:content lang=\"en\">Comparison results of participant laboratory: " + this.massValue.getName() + "</dcc:content>\n" +
                        "      </dcc:name>\n" +
                        "      <dcc:results>\n" +
                        "        <dcc:result refType=\"mass_mass\">\n" +
                        "          <dcc:name>\n" +
                        "            <dcc:content lang=\"en\">Mass of stainless steel cylinder</dcc:content>\n" +
                        "          </dcc:name>\n" +
                        "          <dcc:data>\n" +
                        "            <dcc:quantity refType=\"basic_measuredValue\">\n" +
                        "              <dcc:name>\n" +
                        "                <dcc:content lang=\"en\">Measured value</dcc:content>\n" +
                        "              </dcc:name>\n" +
                        "              <si:real>\n" +
                        "                <si:label>Mass</si:label>\n" +
                        "                <si:value>" + this.massValue.getValue() + "</si:value>\n" +
                        "                <si:unit>" + this.massValue.getUnit() + "</si:unit>\n" +
                        "                <si:expandedUnc>\n" +
                        "                   <si:uncertainty>" + this.massValue.getExpUnc().getUncertainty() + "</si:uncertainty>\n" +
                        "                   <si:coverageFactor>" + this.massValue.getExpUnc().getCoverageFactor() + "</si:coverageFactor>\n" +
                        "                   <si:coverageProbability>" + this.massValue.getExpUnc().getCoverageProbability() + "</si:coverageProbability>\n" +
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
                        "                <si:value>" + this.enValueEnCriterion.getValue() + "</si:value>\n" +
                        "                <si:unit>" + this.enValueEnCriterion.getUnit() + "</si:unit>\n" +
                        "              </si:real>\n" +
                        "            </dcc:quantity>\n" +
                        "          </dcc:data>\n" +
                        "        </dcc:result>\n" +
                        "        <dcc:result refType=\"comparison_equivalenceValue\">\n" +
                        "          <dcc:name>\n" +
                        "            <dcc:content lang=\"en\">Equivalence value</dcc:content>\n" +
                        "          </dcc:name>\n" +
                        "          <dcc:data>\n" +
                        "            <dcc:quantity refType=\"comparison_equivalenceValueGrubbsTest\">\n" +
                        "              <dcc:name>\n" +
                        "                <dcc:content lang=\"en\">Grubbs test value</dcc:content>\n" +
                        "              </dcc:name>\n" +
                        "              <si:real>\n" +
                        "                <si:value>" + this.enValueGrubbsTest.getValue() + "</si:value>\n" +
                        "                <si:unit>" + this.enValueGrubbsTest.getUnit() + "</si:unit>\n" +
                        "              </si:real>\n" +
                        "            </dcc:quantity>\n" +
                        "          </dcc:data>\n" +
                        "        </dcc:result>\n" +
                        "      </dcc:results>\n" +
                        "    </dcc:measurementResult>\n ";

            } else {
                this.result = "<dcc:measurementResult refId=\"" + pid + "\">\n" +
                        "      <dcc:name>\n" +
                        "        <dcc:content lang=\"en\">Comparison results of participant laboratory: " + this.massValue.getName() + "</dcc:content>\n" +
                        "      </dcc:name>\n" +
                        "      <dcc:results>\n" +
                        "        <dcc:result refType=\"mass_mass\">\n" +
                        "          <dcc:name>\n" +
                        "            <dcc:content lang=\"en\">Mass of stainless steel cylinder</dcc:content>\n" +
                        "          </dcc:name>\n" +
                        "          <dcc:data>\n" +
                        "            <dcc:quantity refType=\"basic_measuredValue\">\n" +
                        "              <dcc:name>\n" +
                        "                <dcc:content lang=\"en\">Measured value</dcc:content>\n" +
                        "              </dcc:name>\n" +
                        "              <si:real>\n" +
                        "                <si:label>Mass</si:label>\n" +
                        "                <si:value>" + this.massValue.getValue() + "</si:value>\n" +
                        "                <si:unit>" + this.massValue.getUnit() + "</si:unit>\n" +
                        "                <si:expandedUnc>\n" +
                        "                   <si:uncertainty>" + this.massValue.getExpUnc().getUncertainty() + "</si:uncertainty>\n" +
                        "                   <si:coverageFactor>" + this.massValue.getExpUnc().getCoverageFactor() + "</si:coverageFactor>\n" +
                        "                   <si:coverageProbability>" + this.massValue.getExpUnc().getCoverageProbability() + "</si:coverageProbability>\n" +
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
                        "        <dcc:result refType=\"comparison_equivalenceValue\">\n" +
                        "          <dcc:name>\n" +
                        "            <dcc:content lang=\"en\">Equivalence value</dcc:content>\n" +
                        "          </dcc:name>\n" +
                        "          <dcc:data>\n" +
                        "            <dcc:quantity refType=\"comparison_equivalenceValueGrubbsTest\">\n" +
                        "              <dcc:name>\n" +
                        "                <dcc:content lang=\"en\">Grubbs test value</dcc:content>\n" +
                        "              </dcc:name>\n" +
                        "              <si:real>\n" +
                        "                <si:value>" + this.grubsValue.getEquivalenceValue() + "</si:value>\n" +
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
    }
    /**
     * This function generates for the parameter result the content of the last measurement result entry of the new DCC file
     */
    public void generateKCMeasurement(){
        if(this.grubsValue== null){
            this.result = "         <dcc:measurementResult refId=\"comparison_referenceValues\">\n" +
                    "       <dcc:name>\n" +
                    "           <dcc:content lang=\"en\"> Mass of stainless steel cylinder Output Reference Value</dcc:content>\n" +
                    "       </dcc:name>\n" +
                    "       <dcc:results>\n" +
                    "           <dcc:result refType=\"RefMassUnc\">\n" +
                    "           <dcc:name>\n" +
                    "               <dcc:content lang=\"en\">Reference Output Values</dcc:content>\n" +
                    "           </dcc:name>\n" +
                    "           <dcc:data>\n" +
                    "               <dcc:quantity>\n" +
                    "                   <si:real>\n" +
                    "                       <si:value>"+this.kcMassValue+"</si:value>\n" +
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
                    "\t\t\t\t<dcc:content lang=\"en\">Mass of stainless steel cylinder Output Reference Values</dcc:content>\n" +
                    "\t\t\t</dcc:name>\n" +
                    "\t\t\t<dcc:results>\n" +
                    "\t\t\t\t<dcc:result refType=\"mass_mass\">\n" +
                    "\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t<dcc:content lang=\"en\">Mass Reference Value</dcc:content>\n" +
                    "\t\t\t\t\t</dcc:name>\n" +
                    "\t\t\t\t\t<dcc:data>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"comparison_referenceValueEnCriterion\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Comparison Reference Value En Criterion</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                    "\t\t\t\t\t\t\t<si:real>\n" +
                    "\t\t\t\t\t\t\t\t<si:label>Mass Reference Value</si:label>\n" +
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
                    "\t\t\t\t<dcc:result refType=\"mass_mass\">\n" +
                    "\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t<dcc:content lang=\"en\">Mass Reference Value</dcc:content>\n" +
                    "\t\t\t\t\t</dcc:name>\n" +
                    "\t\t\t\t\t<dcc:data>\n" +
                    "\t\t\t\t\t\t<dcc:quantity refType=\"comparison_referenceValueGrubbsTest\">\n" +
                    "\t\t\t\t\t\t\t<dcc:name>\n" +
                    "\t\t\t\t\t\t\t\t<dcc:content lang=\"en\">Comparison Reference Value Grubbs</dcc:content>\n" +
                    "\t\t\t\t\t\t\t</dcc:name>\n" +
                    "\t\t\t\t\t\t\t<si:real>\n" +
                    "\t\t\t\t\t\t\t\t<si:label>Mass Reference Value</si:label>\n" +
                    "\t\t\t\t\t\t\t\t<si:value>"+this.refValueGrubbsTest.getValue()+"</si:value>\n" +
                    "\t\t\t\t\t\t\t\t<si:unit>"+this.refValueGrubbsTest.getUnit()+"</si:unit>\n" +
                    "\t\t\t\t\t\t\t\t<si:expandedUnc>\n" +
                    "\t\t\t\t\t\t\t\t\t<si:uncertainty>"+this.refValueGrubbsTest.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                    "\t\t\t\t\t\t\t\t\t<si:coverageFactor>"+this.refValueGrubbsTest.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                    "\t\t\t\t\t\t\t\t\t<si:coverageProbability>"+this.refValueGrubbsTest.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                    "\t\t\t\t\t\t\t\t\t<si:distribution>normal</si:distribution>\n" +
                    "\t\t\t\t\t\t\t\t</si:expandedUnc>\n" +
                    "\t\t\t\t\t\t\t</si:real>\n" +
                    "\t\t\t\t\t\t</dcc:quantity>\n" +
                    "\t\t\t\t\t</dcc:data>\n" +
                    "\t\t\t\t</dcc:result>\n" +
                    "\t\t\t</dcc:results>\n" +
                    "\t\t</dcc:measurementResult>";
        }
        else{
            this.result = "         <dcc:measurementResult refId=\"comparison_referenceValues\">\n" +
                    "       <dcc:name>\n" +
                    "           <dcc:content lang=\"en\">Mass of Silicon Sphere Output Reference Values</dcc:content>\n" +
                    "       </dcc:name>\n" +
                    "       <dcc:results>\n" +
                    "           <dcc:result refType=\"RefMassUnc\">\n" +
                    "           <dcc:name>\n" +
                    "               <dcc:content lang=\"en\">Reference Output Values</dcc:content>\n" +
                    "           </dcc:name>\n" +
                    "           <dcc:data>\n" +
                    "               <dcc:quantity>\n" +
                    "                   <si:real>\n" +
                    "                       <si:value>"+this.kcMassValue+"</si:value>\n" +
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

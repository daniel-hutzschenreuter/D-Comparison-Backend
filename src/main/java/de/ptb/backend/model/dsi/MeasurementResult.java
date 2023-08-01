package de.ptb.backend.model.dsi;

import de.ptb.backend.BERT.GEO;

public class MeasurementResult {
    String name = "";
    SiReal massValue;
    Double massDifference;
    Double kcMassValue;
    SiReal kcValue;
    Double enValue;
    SiReal energyValue;
    GEO grubsValue;
    Double grubsUncertainty;
    Double grubsComparisonValue;
    String result = "";
    public MeasurementResult(SiReal massValue, Double kcMassValue, SiReal kcValue, Double enValue, SiReal energyValue, GEO grubsValue) {
        this.massValue = massValue;
        this.kcMassValue = kcMassValue;
        this.kcValue = kcValue;
        this.enValue = enValue;
        this.energyValue = energyValue;
        this.grubsValue = grubsValue;
        generateMeasurementResult();
    }
    public MeasurementResult(String name, SiReal massValue, Double kcMassValue, SiReal kcValue, Double enValue, SiReal energyValue){
        this.name = name;
        this.massValue = massValue;
        this.kcMassValue = kcMassValue;
        this.kcValue = kcValue;
        this.enValue = enValue;
        this.energyValue = energyValue;
        generateMeasurementResult();
    }

    public MeasurementResult(Double massDifference, Double kcMassValue, SiReal kcValue, Double grubsValue, Double grubsUncertainty){
        this.kcValue = kcValue;
        this.kcMassValue = kcMassValue;
        this.massDifference = massDifference;
        this.grubsComparisonValue = grubsValue;
        this.grubsUncertainty = grubsUncertainty;
        generateKCMeasurement();
    }
    private void generateMeasurementResult(){
        this.result = "         <dcc:measurementResult refId=\"NMIJ-1\">\n" +
                "       <dcc:name>\n" +
                "           <dcc:content lang=\"en\">"+this.name+"</dcc:content>\n" +
                "       </dcc:name>\n" +
                "       <dcc:results>\n" +
                "           <dcc:result refType=\"Mass\">\n" +
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Mass of Silicon Sphere</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity reftype=\"Kilogram\">\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">Kilogram</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:label>Mass</si:label>\n" +
                "                           <si:value>"+this.massValue.getValue()+"</si:value>\n" +
                "                           <si:unit>"+this.massValue.getUnit()+"</si:unit>\n" +
                "                           <si:expandedUnc>\n" +
                "                               <si:uncertainty>"+this.massValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                "                               <si:coverageFactor>"+this.massValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                "                               <si:coverageProbability>"+this.massValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                "                               <si:distribution>Normal</si:distribution>\n" +
                "                           </si:expandedUnc>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "                   <dcc:quantity refType=\"En\">\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">En</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:value>"+this.enValue+"</si:value>\n" +
                "                           <si:unit>\\one</si:unit>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "               </dcc:data>\n" +
                "           </dcc:result>\n" +
                "           <dcc:result refType=\"Energy (Grubs)\">\n" +
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity>\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:label>Energy</si:label>\n" +
                "                           <si:value>"+this.energyValue.getValue()+"</si:value>\n" +
                "                           <si:unit>"+this.energyValue.getUnit()+"</si:unit>\n" +
                "                           <si:expandedUnc>\n" +
                "                               <si:uncertainty>"+this.energyValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                "                               <si:coverageFactor>"+this.energyValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                "                               <si:coverageProbability>"+this.energyValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                "                           </si:expandedUnc>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "                </dcc:data>\n" +
                "           </dcc:result>\n" +
                "           <dcc:result refType=\"Energy\">\n"+
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Energy</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity>\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">Energy</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:label>Energy</si:label>\n" +
                "                           <si:value>"+this.energyValue.getValue()+"</si:value>\n" +
                "                           <si:unit>"+this.energyValue.getUnit()+"</si:unit>\n" +
                "                           <si:expandedUnc>\n" +
                "                               <si:uncertainty>"+this.energyValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                "                               <si:coverageFactor>"+this.energyValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                "                               <si:coverageProbability>"+this.energyValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                "                           </si:expandedUnc>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "               </dcc:data>\n" +
                "           </dcc:result>\n" +
                "           <dcc:result refType=\"participant_delta_mass\">\n" +
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Delta Mass</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity refType=\"participant_delta_mass\">\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">Delta Mass</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:value>"+this.massValue.getMassDifference()+"</si:value>\n" +
                "                           <si:unit>"+this.massValue.getUnit()+"</si:unit>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "               </dcc:data>\n" +
                "           </dcc:result>\n" +
                "       </dcc:results>\n" +
                "   </dcc:measurementResult>\n";
    }

    public void generateKCMeasurement(){
        this.result = "         <dcc:measurementResult refId=\"RefVals\">\n" +
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
                "                       <si:difference>"+this.massDifference+"</si:difference>\n" +
                "                       <si:expandedUnc>\n" +
                "                           <si:uncertainty>0.00002535</si:uncertainty>\n" +
                "                           <si:coverageFactor>2</si:coverageFactor>\n" +
                "                           <si:coverageProbability>0.95</si:coverageProbability>\n" +
                "                           <si:distribution>Normal</si:distribution>\n" +
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
                "                               <si:distribution>Normal</si:distribution>\n" +
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
                "                               <si:distribution>Normal</si:distribution>\n" +
                "                           </si:expandedUnc>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "                </dcc:data>\n" +
                "       </dcc:result>\n" +
                "       </dcc:results>\n" +
                "    </dcc:measurementResult>\n";
    }

    @Override
    public String toString() {
        return result;
    }
}

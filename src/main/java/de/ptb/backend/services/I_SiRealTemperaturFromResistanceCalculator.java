package de.ptb.backend.services;

import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.dsi.SiRealListXMLList;

import java.util.List;

public interface I_SiRealTemperaturFromResistanceCalculator {

//    List<SiReal> calculatePt100Temperature(List<SiReal> resistancePt100);
//    List<SiReal> calculateNTCTemperature(List<SiReal> resistanceNTC);
    List<SiRealListXMLList> calculatePt100TemperatureList(List<SiRealListXMLList> resistancePt100List);
//    List<SiRealListXMLList> calculateNTCTemperatureList(List<SiRealListXMLList> resistanceNTCList);

}

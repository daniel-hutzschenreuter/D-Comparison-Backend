package de.ptb.backend.services;

import de.ptb.backend.model.dsi.SiReal;

import java.util.List;

public interface I_SiRealTemperaturFromResistanceCalculator {

    List<SiReal> calculatePt100Temperature(List<SiReal> resistancePt100);
    List<SiReal> calculateNTCTemperature(List<SiReal> resistanceNTC);

}

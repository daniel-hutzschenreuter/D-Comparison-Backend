package de.ptb.backend.services;

import de.ptb.backend.model.dsi.SiReal;

import java.util.List;

public interface I_SiRealDifferenceCalculator {

    List<SiReal> calculateDifference(List<SiReal> siReal1, List<SiReal> siReal2);

}

package de.ptb.backend.services;

import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.dsi.SiRealListXMLList;

import java.util.List;

public interface I_SiRealDifferenceCalculator {

    List<SiReal> calculateDifference(List<SiReal> siReal1, List<SiReal> siReal2);
    List<SiRealListXMLList> calculateDifferenceList(List<SiRealListXMLList> siRealList1, List<SiRealListXMLList> siRealList2);
}

package de.ptb.backend.test;

import de.ptb.backend.BERT.*;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class EnKcCalculationTest {
    List<Double> enValues = new ArrayList<>(Arrays.asList(5.169930,5.169860,5.169886,5.169979,5.169950,5.169875,5.169900,5.170060,5.170660,5.169600,5.170025,5.169950));
    List<Double> uncertainties = new ArrayList<>(Arrays.asList(0.000088,0.000086,0.000100,0.000062,0.000108,0.000100,0.000400,0.000160,0.000200,0.000260,0.000120,0.000200));
    List<Double> enCriterion = new ArrayList<>(Arrays.asList(-0.09,-0.97,-0.54,0.79,0.12,-0.66,-0.09,0.78,3.57,-1.29,0.76,0.06));
    @Test
    public void demoTestMethod(){
        assertTrue(true);
    }
    @Test
    public void checkKcCalc() throws IOException {
        List<SiReal> enSiReals = new ArrayList<>();
        for(int i = 0; i < this.enValues.size(); i++){
            enSiReals.add(new SiReal(this.enValues.get(i), "/joule", "",new SiExpandedUnc(this.uncertainties.get(i),0,0.0)));
        }
        fDKCR fdkcr = new fDKCR();
        RunfDKCR objRunfDKCR = new RunfDKCR();
        Vector<DIR> inputs = new Vector<>();
        for (SiReal siReal : enSiReals) {
            DIR sirealAsDIR = new DIR(siReal.getValue(), siReal.getExpUnc().getUncertainty());
            inputs.add(sirealAsDIR);
        }
        objRunfDKCR.ReadData();
        objRunfDKCR.ReadDKRCContributions();
        fdkcr.setData(objRunfDKCR.getDKCRTitle(), objRunfDKCR.getDKCRID(), objRunfDKCR.getNTotalContributions(), objRunfDKCR.getPilotOrganisationID(),objRunfDKCR.getDKCRDimension(), objRunfDKCR.getDKCRUnit(), enSiReals.size(), inputs, objRunfDKCR.getRunResults());
        objRunfDKCR.setNr(fdkcr.processDKCR());
        Vector<RunResult> Results = objRunfDKCR.getRunResults();
        List<Double>enValuesResult = new ArrayList<>();
        for(EO result: Results.get(0).getEOResults()){
            enValuesResult.add(result.getEquivalenceValue());
        }
        assertSame(enValuesResult, this.enCriterion);
    }
}

package de.ptb.backend.BERT;
import java.util.Vector;

public class fDKCR {
    String DKCRTitle;	// The title for the Digital Key Comparison Report (DKCR)
    String DKCRID;		// BIPM allocated ID for DKCR
    Integer NTotalContributions;			// The total number of contributions to the DKCR
    String PilotOrganisationID;			// ID of Pilot Organisation for DKCR
    String DKCRDimension;				// Measurement dimension
    String DKCRUnit;					// Unit of dimension
    Integer Nc = 0;			// The current (temporal) number of contributions (so far!)
    Integer Nr = 0;			// The number of Runs required for DKCR completion
    Vector<DIR> DirInputs;
    Vector<RunResult> RunResults;
    public void setData(String DKCRTitle,
                        String DKCRID,
                        Integer NTotalContributions,
                        String PilotOrganisationID,
                        String DKCRDimension,
                        String DKCRUnit,
                        Integer Nc,
                        Vector<DIR> DirInputs,
                        Vector<RunResult> RunResults
    )
    {
        this.DKCRTitle = DKCRTitle;
        this.DKCRID = DKCRID;
        this.NTotalContributions = NTotalContributions;
        this.PilotOrganisationID = PilotOrganisationID;
        this.DKCRDimension = DKCRDimension;
        this.DKCRUnit = DKCRUnit;
        this.Nc = Nc;
        this.DirInputs = DirInputs;
        this.RunResults = RunResults;
    }
    public int processDKCR()
    {
        int NOutlierFlags = 0;		// The number of outlier flags from current Run
        int OldNOutlierFlags = 0;	// The number of outlier flags from the previous Run
        int NRun = 1;				// The number of the current Run
        Double dxi = 0.0;
        Double dUi = 0.0;			//
        Double soousq = 0.0;		// Sum of 1 / dUi**2


        // Note Only data with a ResultPresentFlag = true will be processed. All data for false cases will be ignored.
        // However the data structures used will assume that all data is present.

        // While this is the first run, or a new Outlier Flag has been added in the previous run process again.
        while(NRun == 1 || (NOutlierFlags != OldNOutlierFlags))
        {

            // Set up Outflier flag count for the next Run
            //OldNOutlierFlags = NOutlierFlags;		// NOutliferFlags maybe incremented in this Run

            // Set up a new set of empty RunResults
            RunResult a = new RunResult(NTotalContributions);		// NTotalContributions is the total number of contributions
            // Add this to the RunResults
            RunResults.add(a);

            // if this is not the first run, copy the outlier flags from the previous run into the current run's data structure.
            if(NRun != 1)
            {
                // Zero Number of outlierflag counts
                OldNOutlierFlags = 0;
                NOutlierFlags = 0;

                // Copy Outlier information from previous run
                RunResult q = RunResults.get(NRun - 2);  // SHould be NRun - 2
                // TODO copy the outlier information from Previous run into this new one
                for(int i = 0; i < DirInputs.size(); i++)
                {
                    EO eot = a.EOResults.get(i);
                    EO eop = q.EOResults.get(i);

                    // Is the previous run for this contribution already an outlier?
                    if(eop.OutlierFlag)
                    {
                        // Increment outlierflag counts
                        OldNOutlierFlags++;
                        NOutlierFlags++;
                        // Set the outlierflag in this run to still be an outlier
                        eot.OutlierFlag = true;
                    }

                }

                // Determine NOutlierFlags and OldNOutlierFlags (the same value at the start of Run)

            }
            else
            {
                // Yes this is the first Run so need to reset the NOutlierFlags and OldNOutlierFlags (if not already!)
                NOutlierFlags = 0;
                OldNOutlierFlags = 0;
            }

            //// Do 1st Stage DKCR Processing

            // Initialise the sum
            Double G = 0.0;

            // Loop through the contributions to calculate G (ignore any outliers, flagged and cases where ResultPresentFlag = false)
            for(int i = 0; i < DirInputs.size(); i++)
            {
                // Get the contribution data
                DIR o = DirInputs.get(i);

                // Process only if ResultPresent = true
                if(o.ResultPresentFlag == true)
                {
                    // Check that its not already an outlier
                    EO p = a.EOResults.get(i);
                    if(p.OutlierFlag == false)
                    {
                        G = G + 1 / (o.Ui * o.Ui);
                    }
                }
            }

            Double H = 1 / G;

            Double K = Math.sqrt(G);

            Double URef = 1 / K;

            Double xRef = 0.0;

            // Now calculate xRef
            // Loop through the contributions to calculate G (ignore any outliers, flagged and cases where ResultPresentFlag = false)
            for(int i = 0; i < DirInputs.size(); i++)
            {
                // Get the contribution data
                DIR o = DirInputs.get(i);

                // Process only if ResultPresent = true
                if(o.ResultPresentFlag == true)
                {
                    // Check that its not already an outlier
                    EO p = a.EOResults.get(i);
                    if(p.OutlierFlag == false)
                    {
                        xRef = xRef + H * (o.xi / (o.Ui * o.Ui));
                    }
                }
            }

            // Store the relevant xRef URef values in the data structure
            a.URef = URef;
            a.xRef = xRef;

            //// Do 2nd Stage DKCR Processing

            Double En95 = 0.0;

            for(int i = 0; i < DirInputs.size(); i++)
            {
                // Get the contribution data
                DIR o = DirInputs.get(i);

                // Process only if ResultPresent = true
                if(o.ResultPresentFlag == true)
                {
                    // Check that its not already an outlier
                    EO p = a.EOResults.get(i);
                    if(p.OutlierFlag == false)
                    {
                        // Calc. En95 for each of the contributions
                        En95 = Math.abs((o.xi - a.xRef) / Math.sqrt(o.Ui*o.Ui - a.URef*a.URef));

                        // Get the relevant EO object
                        EO eo = a.EOResults.get(i);

                        // Store value in data structure
                        eo.EquivalenceValue = En95;

                        // Calculate the Rounded Value and store in EquivalenceValueRounded
                        eo.RoundEquivalenceValue();

                        // Is it a (new) outlier?
                        //TODO
                        // Daniel: here is bug
                        if(En95 > 1.0)
                        {
                            // Yes its an new Outlier
                            eo.OutlierFlag = true;

                            // Increment the Outlier Count NOutlierFlags
                            NOutlierFlags++;
                        }
                    }
                    // wafa  and Daniel
                    else if (p.OutlierFlag== true){

                        En95 = Math.abs((o.xi - a.xRef) / Math.sqrt(o.Ui*o.Ui + a.URef*a.URef));

                        // Get the relevant EO object
                        EO eo = a.EOResults.get(i);

                        // Store value in data structure
                        eo.EquivalenceValue = En95;

                        // Calculate the Rounded Value and store in EquivalenceValueRounded
                        eo.RoundEquivalenceValue();

                    }
                }
            }

            // Increment the Run Number
            NRun++;
        }

        Nr = NRun - 1;

        return Nr;  // Because NRun - 1 is the number of active Runs

    }
}


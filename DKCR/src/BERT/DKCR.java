package BERT;

import java.util.Iterator;
import java.util.Vector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DKCR {
	
	
	String DKCRTitle;	// The title for the Digital Key Comparison Report (DKCR) 
	String DKCRID;		// BIPM allocated ID for DKCR
	Integer NTotalContributions;			// The total number of contributions to the DKCR
	
	String PilotOrganisationID;			// ID of Pilot Organisation for DKCR
	
	String DKCRDimension;				// Measurement dimension
	String DKCRUnit;					// Unit of dimension
	
	Integer Nc = 0;			// The current (temporal) number of contributions (so far!)
	
	String DKRSetUpfilename = "DKCR.txt";	// File containing setup data for the one and only (atomic) DKCR
	String DKCRContributions = "DKCRContributions.txt";			// File containing current actual contributions
	
	String pathname = "TestFiles";	// The pathname of the folder containing DKCR data
	
	// Vector<DIR> RunResults = new Vector<DIR>(50, 10);
	Vector<DIR> DirInputs = new Vector<DIR>();				// A vector of N pairs of (xi, Ui) 
	
	Vector<RunResult> RunResults = new Vector<RunResult>();	// A vector of Nr RunResult(s) (Ei, OutlierFlagi)
	
	public DKCR() throws IOException
	{
		// So far empty constructor
		
		// Read in the set up information for the DKCR from the folder "TestFiles"
		int result = ReadData();
		
	}
	
	public int ReadData() throws IOException
	{
		// Read data from DKCR.txt file which contains the set up information for the DKCR
		
		try
		{
		    BufferedReader in = new BufferedReader(new FileReader(pathname + "//" + DKRSetUpfilename)); 
		    String line;
		    int ln = 0;
		    
		    // The DKCR.txt file will contain the total number of contributions NTotalContributions
		    
		    while  (( line = in.readLine()) != null )
		    {
		    	
		    	// Do something with the line
		    	System.out.println(line);		// Debug shows file being read
		        String[] current_Record = line.split(",");
		    	
		    	if(ln == 0)
		    	{   
			        // Read info into the one and only (atomic) DKCR attributes
			        this.DKCRTitle = current_Record[0];
			        this.DKCRID = current_Record[1];
			        this.NTotalContributions = Integer.parseInt(current_Record[2]);
			        this.PilotOrganisationID = current_Record[3];
			        this.DKCRDimension = current_Record[4];
			        this.DKCRUnit = current_Record[5];
		    	}
		    	else
		    	{
		    		// Read info into a DIR new object
		    		DIR a = new DIR();
		    		a.DKCRCONID = current_Record[0];
		    		a.NMIID = current_Record[1];
		    		a.ResultPresentFlag = false;			// No data has been included yet!
		    		a.xi = 0.0;								// So force to 0.0
		    		a.Ui = 0.0;								// So force to 0.0
		    		this.DirInputs.add(a);
		    	}
		    	
		        ln++;
				        
		    }
		    in.close();
		}
		    
		catch( IOException ioException ) 
		{
		    System.out.println("Exception: "+ioException);
		}
		    
		return 0;
	}
	
	public int ReadDKRCContributions() throws IOException
	{
		
		// Read data from DKCRContributions.txt file which contains the contribution data so far
		// Note the number of entries will be less than NTotalContributions if not all contributions
		// have been received.
		
		try
		{
		    BufferedReader in = new BufferedReader(new FileReader(pathname + "//" + DKCRContributions)); 
		    String line;
		    int ln = 0;
		    
		    while  (( line = in.readLine()) != null )
		    {
		    	
		    	// Do something with the line
		    	System.out.println(line);		// Debug shows file being read
		        String[] current_Record = line.split(",");
		    	

	    		// Get Data from line
		        String ID = current_Record[0];
		        Double xi = Double.parseDouble(current_Record[1]);
		        Double Ui = Double.parseDouble(current_Record[2]);
		        
		        AddContributions(ID, xi, Ui);
		        
		        ln++;
		        
		        // Increment the number of contributions counter
		        Nc++;
				        
		    }
		    in.close();
		}
		    
		catch( IOException ioException ) 
		{
		    System.out.println("Exception: "+ioException);
		}
		
		
		return 0;
		
	}
	
	public int AddContributions(String sID, Double dxi, Double dUi)
	{
		// The size of DirInputs will always be NTotalContributions
		// but the number of contributions so far may not equal NTotalContributions
		// Therefore count number of contributions and set this to Nc
		
		
		// Search through DirInputs for contribution ID and add results
	    for(int i = 0; i < DirInputs.size(); i++)
	    {
	    	DIR o = DirInputs.get(i);
	    	if(o.DKCRCONID.equals(sID))
	    	{
	    		// Add the results to this object
	    		o.xi = dxi;
	    		o.Ui = dUi;
	    		// Set the data present flag from false to true
	    		o.ResultPresentFlag = true;

	    	}
	    	
	    }
		
		return 0;
	}
	
	public int ProcessDKCR()
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
		    	RunResult q = RunResults.get(NRun - 2);  // SHould this be NRun - 2  ???
		    	// TODO copy the outlier information from Previous run into this new one
			    for(int i = 0; i < DirInputs.size(); i++)
			    {
			    	EO eot = a.EOResults.get(i);
			    	EO eop = q.EOResults.get(i);
			    	
			    	// Is the previous run for this contribution already an outlier?
			    	if(eop.OutlierFlag == true)
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
		    			if(En95 > 1.0)
		    			{
		    				// Yes its an new Outlier
		    				eo.OutlierFlag = true;
		    				
		    				// Increment the Outlier Count NOutlierFlags
		    				NOutlierFlags++;
		    			}	
		    		}
		    	}		    	
		    }		    
		    
		    // Increment the Run Number
		    NRun++;
		}
		
		return 0;
	}
	
	public int PresentResults()
	{

		RunResults.forEach(RunResult->
		{
			System.out.println(" ");
			System.out.println("URef="+(RunResult.URef).toString()+" "+"xRef="+(RunResult.xRef).toString());
			RunResult.EOResults.forEach(EO->
			{
				System.out.println(EO.EquivalenceValue+ " ("+EO.EquivalenceValueRounded + ") " + EO.OutlierFlag);
			});
		});

		
		return 0;
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		// Create the NMIS object that will contain a list of BIPM registered NMIs
		NMIS objNMIS = new NMIS();
		
		// Create a new top level (atomic) DKCR object and read in setup data for this DKCR
		DKCR objDKCR = new DKCR();
	
		// Read in actual DKCR contributions from DKCRContribtions.txt and update the DirInputs Vector
		objDKCR.ReadDKRCContributions();
		
		// Perform the Processing of the DKRC
		objDKCR.ProcessDKCR();
		
		// Present Results
		objDKCR.PresentResults();	// Basic output to console window
		
	    System.out.println("DKCR Completed!");
		
		
		int x = 1;
		
		
	}

}

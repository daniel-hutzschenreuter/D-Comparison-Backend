package BERT;

import java.util.Vector;

public class RunResult {

	Double xRef;									// xRef value for a Run
	Double URef;									// URef value for a Run
	Vector<EO> EOResults = new Vector<EO>();		// A vector of length N (pairs of Equivalence and Outlier flags)
	
	public RunResult(int N)							// N is the number of contributions to the DKCR
	{
		// Initialise xRef and URef
		xRef = 0.0;
		URef = 0.0;
		// Add a set of N EO objects into the EOResults vector
		for(int i = 0; i < N; i++)
		{
			EO a = new EO();
			// Initialise the object a
			a.EquivalenceValue = 0.0;
			a.EquivalenceValueRounded = 0.0;
			a.OutlierFlag = false;
			EOResults.add(a);
		}
		
	}
}

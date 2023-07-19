package de.ptb.backend.BERT;

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
			EO objEO = new EO();
			// Initialise the object a
			objEO.EquivalenceValue = 0.0;
			objEO.EquivalenceValueRounded = 0.0;
			objEO.OutlierFlag = false;
			EOResults.add(objEO);
		}

	}
	@Override
	public String toString() {
		return "RunResult{" +
				"xRef=" + xRef +
				", URef=" + URef +
				", EOResults=" + EOResults.toString() +
				'}';
	}

	public Double getxRef() {
		return xRef;
	}

	public Double getURef() {
		return URef;
	}

	public Vector<EO> getEOResults() {
		return EOResults;
	}
}

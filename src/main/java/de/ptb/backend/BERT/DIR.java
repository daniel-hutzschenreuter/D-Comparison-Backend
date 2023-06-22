package de.ptb.backend.BERT;

import java.util.*;		// For Vector collection

public class DIR {
	String DKCRCONID;							// DKCR Contribution ID
	String NMIID;								// The ID of the Metrology Institute
	Double xi;									// The xi value for an indiviual contribution
	Double Ui;									// The Ui value for an individual contribution
	Boolean ResultPresentFlag;					// Indicates that a valid Result is Present
	//   (Note:- this is not the same as the Outlier Flag)

	String filename;

	// Vector<RunResult> RunResults = new Vector<RunResult>();	// A vector of Nr RunResult(s)
	public DIR() {
	}

	public DIR(Double xi, Double ui) {
		this.xi = xi;
		this.Ui = ui;
		this.ResultPresentFlag=true;
	}

	@Override
	public String toString() {
		return "DIR{" +
				"DKCRCONID='" + DKCRCONID + '\'' +
				", NMIID='" + NMIID + '\'' +
				", xi=" + xi +
				", Ui=" + Ui +
				", ResultPresentFlag=" + ResultPresentFlag +
				", filename='" + filename + '\'' +
				'}';
	}
}


package de.ptb.backend.BERT;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class RunfDKCR {
    String DKCRTitle;    // The title for the Digital Key Comparison Report (DKCR)
    String DKCRID;        // BIPM allocated ID for DKCR
    Integer NTotalContributions;            // The total number of contributions to the DKCR
    String PilotOrganisationID;            // ID of Pilot Organisation for DKCR
    String DKCRDimension;                // Measurement dimension
    String DKCRUnit;                    // Unit of dimension
    Integer Nc = 0;            // The current (temporal) number of contributions (so far!)
    Integer Nr = 0;            // The number of Runs required for DKCR completion
    String DKRSetUpfilename = "DKCR.txt";    // File containing setup data for the one and only (atomic) DKCR
    String DKCRContributions = "DKCRContributions.txt";            // File containing current actual contributions
    String pathname = "src/main/resources/TestFiles";    // The pathname of the folder containing DKCR data
    // Vector<DIR> RunResults = new Vector<DIR>(50, 10);
    Vector<DIR> DirInputs = new Vector<>();                // A vector of Nc pairs of (xi, Ui
    Vector<RunResult> RunResults = new Vector<>();    // A vector of Nr RunResult(s) Nc(Ei, OutlierFlagi)

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        String DKRSetUpfilename = "DKCR.txt";    // File containing setup data for the one and only (atomic) DKCR
        String DKCRContributions = "DKCRContributions.txt";            // File containing current actual contributions
        String pathname = "TestFiles";    // The pathname of the folder containing DKCR data
        // Create a new fDKCR object

        fDKCR objfDKCR = new fDKCR();


        RunfDKCR objRunfDKCR = new RunfDKCR();


        //// Read in data from txt files

        // Read data from setup file

        objRunfDKCR.ReadData();

        // Read data from contributions file

        objRunfDKCR.ReadDKRCContributions();


        // Set the data for fDKCR

        objfDKCR.setData(objRunfDKCR.DKCRTitle,

                objRunfDKCR.DKCRID,

                objRunfDKCR.NTotalContributions,

                objRunfDKCR.PilotOrganisationID,

                objRunfDKCR.DKCRDimension,

                objRunfDKCR.DKCRUnit,

                objRunfDKCR.Nc,

                objRunfDKCR.DirInputs,

                objRunfDKCR.RunResults);


        // Process the DKCR

        objRunfDKCR.Nr = objfDKCR.processDKCR();


        // Get Results from the DKCR

        //objfDKCR.getResults(objRunfDKCR.Nr);


        int z = 1;

    }


    public void ReadData() throws IOException {

        // Read data from DKCR.txt file which contains the set up information for the DKCR


        try {

            BufferedReader in = new BufferedReader(new FileReader(pathname + "/" + DKRSetUpfilename));

            String line;

            int ln = 0;


            // The DKCR.txt file will contain the total number of contributions NTotalContributions


            while ((line = in.readLine()) != null) {


                // Do something with the line

                //System.out.println(line);        // Debug shows file being read

                String[] current_Record = line.split(",");


                if (ln == 0) {

                    // Read info into the one and only (atomic) DKCR attributes

                    this.DKCRTitle = current_Record[0];

                    this.DKCRID = current_Record[1];

                    this.NTotalContributions = Integer.parseInt(current_Record[2]);

                    this.PilotOrganisationID = current_Record[3];

                    this.DKCRDimension = current_Record[4];

                    this.DKCRUnit = current_Record[5];

                } else {

                    // Read info into a DIR new object

                    DIR a = new DIR();

                    a.DKCRCONID = current_Record[0];

                    a.NMIID = current_Record[1];

                    a.ResultPresentFlag = false;            // No data has been included yet!

                    a.xi = 0.0;                                // So force to 0.0

                    a.Ui = 0.0;                                // So force to 0.0

                    this.DirInputs.add(a);

                }


                ln++;


            }

            in.close();

        } catch (IOException ioException) {

            System.out.println("Exception: " + ioException);

        }


    }

    public void ReadDKRCContributions() throws IOException {


        // Read data from DKCRContributions.txt file which contains the contribution data so far

        // Note the number of entries will be less than NTotalContributions if not all contributions

        // have been received.


        // The ResultPresentFlag (boolean) will indicate if data has been received.


        try {

            BufferedReader in = new BufferedReader(new FileReader(pathname + "//" + DKCRContributions));

            String line;

            int ln = 0;


            while ((line = in.readLine()) != null) {


                // Process the line

             //   System.out.println(line);        // Debug shows file being read

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

        } catch (IOException ioException) {

            System.out.println("Exception: " + ioException);

        }

    }


    public int AddContributions(String sID, Double dxi, Double dUi) {

        // The size of DirInputs will always be NTotalContributions

        // but the number of contributions so far may not equal NTotalContributions

        // Therefore count number of contributions and set this to Nc


        // Search through DirInputs for contribution ID and add results

        for (int i = 0; i < DirInputs.size(); i++) {
            DIR o = DirInputs.get(i);
            if (o.DKCRCONID.equals(sID)) {
                // Add the results to this object

                o.xi = dxi;

                o.Ui = dUi;

                // Set the data present flag from false to true

                o.ResultPresentFlag = true;
            }
        }
        return 0;
    }

    public String getDKCRTitle() {
        return DKCRTitle;
    }

    public String getDKCRID() {
        return DKCRID;
    }

    public Integer getNTotalContributions() {
        return NTotalContributions;
    }

    public String getPilotOrganisationID() {
        return PilotOrganisationID;
    }

    public String getDKCRDimension() {
        return DKCRDimension;
    }

    public String getDKCRUnit() {
        return DKCRUnit;
    }

    public Integer getNc() {
        return Nc;
    }

    public Integer getNr() {
        return Nr;
    }

    public String getDKRSetUpfilename() {
        return DKRSetUpfilename;
    }

    public String getDKCRContributions() {
        return DKCRContributions;
    }

    public String getPathname() {
        return pathname;
    }

    public Vector<RunResult> getRunResults() {
        return RunResults;
    }

    public void setNr(int nr) {
        this.Nr = nr;
    }
}
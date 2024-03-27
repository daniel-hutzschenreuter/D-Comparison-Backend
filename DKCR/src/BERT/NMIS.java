package BERT;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class NMIS 
{
	
	Vector<DKCRContributor> nmis = new Vector<DKCRContributor>();
	
	public NMIS() throws IOException
	{
		// Empty Constructor
		int result = ReadData();
	}
	
	public int ReadData() throws IOException
	{
		// Read the NMI data from standard filename "NMIS.txt" into Vector "NMIS"

		try
		{
		    BufferedReader in = new BufferedReader(new FileReader("TestFiles//NMIS.txt")); 
		    String line;
		    
		    while  (( line = in.readLine()) != null )
		    {
		    	// Do something with the line
		    	System.out.println(line);		// Debug shows file being read
		    	
		        String[] current_Record = line.split(",");
		        
		        // Create a new DKCRContributor
		        DKCRContributor a = new DKCRContributor();
		        a.MetrologyID = current_Record[0];
		        a.Name = current_Record[1];
		        a.Country = current_Record[2];
		        
		        // Add the new DKCRContributor to the nmis Vector
		        nmis.add(a);
		        
		    }
		    
		    in.close();
		}
		
		catch( IOException ioException ) 
		{
		    System.out.println("Exception: "+ioException);
		}

		
		return 0;
	}

}

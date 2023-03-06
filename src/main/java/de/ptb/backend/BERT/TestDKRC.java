package de.ptb.backend.BERT;


import java.util.*;

public class TestDKRC {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");
		
		Vector<EO> testeo = new Vector<EO>();
		
		int x = 1;
		
		EO a = new EO();
		
		testeo.add(a);
		
		testeo.add(new EO());
		
		a.OutlierFlag = false;
		a.EquivalenceValue = 1.0;
		
		EO b = (EO)testeo.get(1);
		
		x=2;
		
		for(int k = 0; k < 10; k++)
		{
			testeo.add(new EO());			
		}
		
		x = 3;
	}

}

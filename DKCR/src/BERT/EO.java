package BERT;

public class EO {

	Boolean OutlierFlag;
	Double EquivalenceValue;
	Double EquivalenceValueRounded;
	
	public int RoundEquivalenceValue()
	{
		
		Double x = EquivalenceValue;
		
		Double s = 0.0;
		Double a = 0.0;
		Double b = 0.0;
		Double c = 0.0;
		
		s = Math.signum(x);
		a = 100 * x;
		b = a + s/2;
		c = Math.signum(b) * Math.floor(Math.abs(b));
		
		Boolean c1;
		if( a - Math.floor(a) == 0.5 )
			c1 = true;
		else
			c1 = false;
		
		Boolean c2;
		if( c != (2 * Math.floor(c / 2)) )
			c2 = true;
		else
			c2 = false;
		
		if(c1 && c2)
		{
			c = c - s;
		}
		
		EquivalenceValueRounded = c / 100.0;
		
		// System.out.println("Finished x = " + x.toString() + " rounded = " + xbr.toString());
		
		int y = 1;
		return 0;
	}
	
}

package BERT;


public class Testtdp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Double x = -1.555999;
		
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
		
		Double xbr = c / 100.0;
		
		System.out.println("Finished x = " + x.toString() + " rounded = " + xbr.toString());
		
		int y = 1;
		
				
	}

}

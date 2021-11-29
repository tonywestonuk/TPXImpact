package tpximpact.techassignment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import tpximpact.techassignment.exceptions.ATMError;


public class IntegrationTest {
	
	public final String testInput = 
			"8000\n"
			+ "\n"
			+ "12345678 1234 1234\n"
			+ "500 100\n"
			+ "B\n"
			+ "W 100\n"
			+ "\n"
			+ "87654321 4321 4321\n"
			+ "100 0\n"
			+ "W 10\n"
			+ "\n"
			+ "87654321 4321 4321\n"
			+ "0 0\n"
			+ "W 10\n"
			+ "B";
	
	@Test
	public void integrationTest() throws ATMError {
		
		AtmImpl atm = new AtmImpl();
		StringReader rdr = new StringReader(testInput);
		StringWriter wtr= new StringWriter();
		
		AtmLineInputReader atmRdr = new AtmLineInputReader(atm, rdr, wtr);
		atmRdr.run();
		
		assertEquals( "500\n"
					+ "400\n"
					+ "90\n"
					+ "FUNDS_ERR\n"
					+ "0\n", 
					
					wtr.toString());
		
		System.out.println("Integeration Test");
		System.out.println("-----------------");
		System.out.println();
		System.out.println("Input Data");
		System.out.println(testInput);
		System.out.println();
		System.out.println("Output Data");
		System.out.println(wtr);
	}
}

package tpximpact.techassignment;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Launcher {

	public static void main(String[] args) {
		
		
		AtmImpl atm = new AtmImpl();
		AtmLineInputReader lineInput = new AtmLineInputReader(atm, new InputStreamReader(System.in), new OutputStreamWriter(System.out));
	
		lineInput.run();
	}
}

package tpximpact.techassignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import tpximpact.techassignment.exceptions.ATMError;
import tpximpact.techassignment.exceptions.ATMLineReaderException;
import tpximpact.techassignment.exceptions.ATMError.Errors;


public class TestLineInputReader {
	
	
	@Test
	public void testLIR_checkHopperInitalisedOK()  {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n");
		new AtmLineInputReader(mockAtm,rdr , new StringWriter()).run();
		verify(mockAtm).setCashHopper(4124);
	}
	
	@Test
	public void testLIR_errorThrownWhenBadInitData1() {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\nThisshouldnBeHere\n");
		
		assertThrows(ATMLineReaderException.class, () -> {
			new AtmLineInputReader(mockAtm,rdr , new StringWriter()).run();
	    });	
	}
	
	@Test
	public void testLIR_errorThrownWhenBadInitData2() {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124 1422\n\n");
		
		assertThrows(ATMLineReaderException.class, () -> {
			new AtmLineInputReader(mockAtm,rdr , new StringWriter()).run();
	    });	
	}
	
	@Test
	public void testLIR_CheckCustomerAcctPin()  {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n12345678 3232 4343");	
		new AtmLineInputReader(mockAtm,rdr , new StringWriter()).run();
		verify(mockAtm).setAccountAndPin(12345678, 3232, 4343);
	}
	
	@Test
	public void testLIR_CheckCustomerAcctPinErr() {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n12345678 3232");	
		assertThrows(ATMLineReaderException.class, () -> {
			new AtmLineInputReader(mockAtm,rdr , new StringWriter()).run();
	    });	
	}
	
	@Test
	public void testLIR_CheckCustomerBalance()  {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n12345678 3232 4343\n5000 6000");	
		new AtmLineInputReader(mockAtm,rdr , new StringWriter()).run();
		verify(mockAtm).setBalanceOverdraft(5000, 6000);
	}
	
	
	@Test
	public void testLIR_CheckCustomerBalanceErr() {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n12345678 3232\n5000 6000 1234");	
		assertThrows(ATMLineReaderException.class, () -> {
			new AtmLineInputReader(mockAtm,rdr , new StringWriter()).run();
	    });	
	}
	
	@Test
	public void testLIR_CheckCustomerReturnBalance() throws ATMError {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n12345678 3232 5727\n5000 6000\nB");	
		StringWriter wtr = new StringWriter();
		
		when(mockAtm.queryBalance()).thenReturn(14256l);
		
		new AtmLineInputReader(mockAtm,rdr , wtr).run();
		
		assertEquals("14256\n", wtr.toString());  
	}
	
	@Test
	public void testLIR_CheckCustomerWithdraw() throws ATMError {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n12345678 3232 5727\n5000 6000\nW 123");	
		StringWriter wtr = new StringWriter();
		
		when(mockAtm.withdraw(123)).thenReturn(456l);
		
		new AtmLineInputReader(mockAtm,rdr , wtr).run();
		
		assertEquals("456\n", wtr.toString());  
	}
	
	@Test
	public void testLIR_CheckCustomerWithdrawError() throws ATMError {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n12345678 3232 5727\n5000 6000\nW 123");	
		StringWriter wtr = new StringWriter();
		
		when(mockAtm.withdraw(123)).thenThrow(new ATMError(Errors.ACCOUNT_ERR));
		
		new AtmLineInputReader(mockAtm,rdr , wtr).run();
		
		assertEquals("ACCOUNT_ERR\n", wtr.toString());  
	}
	
	@Test
	public void testLIR_CheckCustomerBalanceError() throws ATMError {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n12345678 3232 5727\n5000 6000\nB");	
		StringWriter wtr = new StringWriter();
		
		when(mockAtm.queryBalance()).thenThrow(new ATMError(Errors.ACCOUNT_ERR));
		
		new AtmLineInputReader(mockAtm,rdr , wtr).run();
		
		assertEquals("ACCOUNT_ERR\n", wtr.toString());  
	}
	
	@Test
	public void testLIR_endOfSession() throws ATMError {
		Atm mockAtm = mock(Atm.class);
		StringReader rdr = new StringReader("4124\n\n12345678 3232 5727\n5000 6000\n\n");	
		StringWriter wtr = new StringWriter();
		
		new AtmLineInputReader(mockAtm,rdr , wtr).run();
		
		verify(mockAtm).endCustomerSession();
	}
	
	
	


}

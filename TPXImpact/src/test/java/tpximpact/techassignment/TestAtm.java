package tpximpact.techassignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


import org.junit.jupiter.api.Test;

import tpximpact.techassignment.exceptions.ATMError;


public class TestAtm {
	
	
	@Test
	public void testATM_queryBalance() throws ATMError {
		AtmImpl atm = new AtmImpl();
		atm.setBalanceOverdraft(576, 123);
		atm.setAccountAndPin(12345, 1001, 1001);
		assertEquals(576, atm.queryBalance());
	}
	
	@Test
	public void testATM_queryBalanceWrongPin()  {
		AtmImpl atm = new AtmImpl();
		atm.setBalanceOverdraft(576, 123);
		atm.setAccountAndPin(12345, 1001, 1002);
		
		try {
			atm.queryBalance();
			fail("queryBalance() did not throw exception");
		} catch (ATMError e) {
			assertEquals( "ACCOUNT_ERR", e.getMessage());
		}
		
	}
	
	@Test
	public void testATM_withdrawWrongPin()  {
		AtmImpl atm = new AtmImpl();
		atm.setCashHopper(1000);
		atm.setBalanceOverdraft(576, 123);
		atm.setAccountAndPin(12345, 1001, 1002);
		
		try {
			atm.withdraw(100);
			fail("withdraw() did not throw exception");
		} catch (ATMError e) {
			assertEquals("ACCOUNT_ERR",e.getMessage() );
		}
	}
	
	@Test
	public void testATM_withdrawATMOutOfMoney()  {
		AtmImpl atm = new AtmImpl();
		atm.setCashHopper(100);
		atm.setBalanceOverdraft(576, 123);
		atm.setAccountAndPin(12345, 1001, 1001);
		
		try {
			atm.withdraw(101);
			fail("withdraw() did not throw exception");
		} catch (ATMError e) {
			assertEquals("ATM_ERR",e.getMessage() );
		}	
	}
	
	@Test
	public void testATM_withdrawExactATMMoney() throws ATMError  {
		AtmImpl atm = new AtmImpl();
		atm.setCashHopper(100);
		atm.setBalanceOverdraft(576, 123);
		atm.setAccountAndPin(12345, 1001, 1001);
			
		long balance = atm.withdraw(100);
		assertEquals(476, balance);
	}

	@Test
	public void testATM_withdrawATMInsufficientFunds()  {
		AtmImpl atm = new AtmImpl();
		atm.setCashHopper(100);
		atm.setBalanceOverdraft(200, 200);
		atm.setAccountAndPin(12345, 1001, 1001);
		
		try {
			atm.withdraw(401);
			fail("withdraw() did not throw exception");
		} catch (ATMError e) {
			assertEquals("FUNDS_ERR",e.getMessage() );
		}	
	}
	
	@Test
	public void testATM_withdrawATMSuccess() throws ATMError  {
		AtmImpl atm = new AtmImpl();
		atm.setCashHopper(500);
		atm.setBalanceOverdraft(200, 200);
		atm.setAccountAndPin(12345, 1001, 1001);

		long balance = atm.withdraw(400);
		assertEquals(-200, balance);
		
	}
	
	@Test
	public void testATM_getBalanceAfterEndSession() throws ATMError  {
		AtmImpl atm = new AtmImpl();
		atm.setCashHopper(500);
		atm.setBalanceOverdraft(200, 200);
		atm.setAccountAndPin(12345, 1001, 1001);
		
		atm.endCustomerSession();
		
		try {
			atm.queryBalance();
			fail("queryBalance() did not throw exception");
		} catch (ATMError e) {
			assertEquals( "ACCOUNT_ERR", e.getMessage());
		}
	}
}

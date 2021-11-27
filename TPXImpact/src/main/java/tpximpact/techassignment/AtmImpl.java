package tpximpact.techassignment;

import tpximpact.techassignment.exceptions.ATMError;
import tpximpact.techassignment.exceptions.ATMError.Errors;

public class AtmImpl implements Atm {

	private long cashHopper;
	private long balance;
	private long overdraft;
	private int acctNbr;
	
	private boolean correctPinEntered = false;
	
	@Override
	public void setCashHopper(long amount) {
		cashHopper = amount;
	}

	@Override
	public void setAccountAndPin(int acctNbr, int correctPin, int enteredPin) {
		this.acctNbr = acctNbr;
		
		correctPinEntered = (correctPin == enteredPin);	
	}

	@Override
	public void setBalanceOverdraft(long balance, long overdraft) {
		this.balance = balance;
		this.overdraft = overdraft;	
	}

	@Override
	public long queryBalance() throws ATMError {
		if (!correctPinEntered) {
			throw new ATMError(Errors.ACCOUNT_ERR);
		}
		
		return balance;
	}

	@Override
	public long withdraw(long withdrawAmount) throws ATMError {
		if (!correctPinEntered) {
			throw new ATMError(Errors.ACCOUNT_ERR);
		}
		
		if (balance+overdraft < withdrawAmount) {
			throw new ATMError(Errors.FUNDS_ERR);
		}
		
		if (cashHopper < withdrawAmount) {
			throw new ATMError(Errors.ATM_ERR);
		}
		
		
		balance -= withdrawAmount;
		cashHopper -=withdrawAmount;
		
		return balance;
		
	}

	@Override
	public void endCustomerSession() {
		correctPinEntered = false;
		acctNbr = 0;
		balance = 0;
		overdraft = 0;
	}


	
}

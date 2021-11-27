package tpximpact.techassignment;

import tpximpact.techassignment.exceptions.ATMError;

public interface Atm {

	void setCashHopper(long amount);

	void setAccountAndPin(int acctNbr, int correctPin, int enteredPin);

	void setBalanceOverdraft(long balance, long overdraft);

	long queryBalance() throws ATMError;

	long withdraw(long withdrawAmount) throws ATMError;

	void endCustomerSession();
}

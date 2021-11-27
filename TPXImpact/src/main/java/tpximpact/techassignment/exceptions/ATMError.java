package tpximpact.techassignment.exceptions;

public class ATMError extends Exception {
	
	public static enum Errors {
		ACCOUNT_ERR,
		FUNDS_ERR,
		ATM_ERR
	}

	public ATMError(Errors err) {
		super (err.toString());
	}

}

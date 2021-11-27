package tpximpact.techassignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import tpximpact.techassignment.exceptions.ATMError;
import tpximpact.techassignment.exceptions.ATMLineReaderException;

public class AtmLineInputReader {

	private BufferedReader input;
	private PrintWriter output;
	
	private enum State {
		INIT,
		WAITING_FOR_CUSTOMER,
		WAITING_FOR_BALANCE,
		SERVICE_CUSTOMER_REQUEST;
	}
	
	private State currentState = State.INIT;
	
	private Atm atm;

	public AtmLineInputReader(Atm atm, Reader inp, Writer opt) {
		this.input = new BufferedReader(inp);
		this.output = new PrintWriter(opt);
		this.atm = atm;
	}
	
	
	
	public void run() {
		try {
		 String lineIn = input.readLine();
		 while (lineIn!=null) {
			 String[] splitLineBySpace = lineIn.split(" ");
			 
			 switch (currentState) {
			 case INIT:
				 initAtm(splitLineBySpace);
				 lineIn = input.readLine();
				 if (!lineIn.equals("")) {
					 throw new ATMLineReaderException("Malformed ATM Startup - second line is not blank");
				 }
				 
				 currentState = State.WAITING_FOR_CUSTOMER;
				 break;
				 
			 case WAITING_FOR_CUSTOMER: 
				 accountPinMessage(splitLineBySpace);
				 currentState = State.WAITING_FOR_BALANCE;
				 break;
				 
			 case WAITING_FOR_BALANCE: 
				 balanceOverdraftMessage(splitLineBySpace);
				 currentState = State.SERVICE_CUSTOMER_REQUEST;
				 break;
				 
			 case SERVICE_CUSTOMER_REQUEST:
				 if (lineIn.equals("")) {
					 currentState = State.WAITING_FOR_CUSTOMER;
					 atm.endCustomerSession();
				 } else {
					 serviceCustomerRequest(splitLineBySpace);
				 }
			}
			 
			 output.flush();
			 
			 lineIn = input.readLine();
		 }

		} catch(IOException err) {
			err.printStackTrace();
		}
	}
	
	private void initAtm(String[] message) {
		if (message.length!=1) throw new ATMLineReaderException("Malformed initATM Message ");
		atm.setCashHopper(Long.parseLong(message[0]));	
	}
	
	private void accountPinMessage(String[] message) {
		if (message.length!=3) throw new ATMLineReaderException("Malformed accountPinMessage ");
		
		try {
			int acctNbr = Integer.parseInt(message[0]);
			int correctPin = Integer.parseInt(message[1]);
			int enteredPin = Integer.parseInt(message[2]);	
			
			if (acctNbr > 99999999 || acctNbr<0)  new ATMLineReaderException("account number invalid ");
			if (correctPin > 9999 || correctPin<0)  new ATMLineReaderException("correct pin number invalid ");
			if (enteredPin > 9999 || enteredPin<0)  new ATMLineReaderException("entered pin number invalid ");

			
			atm.setAccountAndPin(acctNbr,correctPin,enteredPin);
	
		} catch (NumberFormatException e) {
			throw new ATMLineReaderException("Malformed accountPinMessage ");
		}	
	}
	
	private void balanceOverdraftMessage(String[] message) {
		if (message.length!=2) throw new RuntimeException("Malformed balanceOverdraftMessage ");
		
		try {
			long balance = Long.parseLong(message[0]);
			long overdraft = Long.parseLong(message[1]);		
			atm.setBalanceOverdraft(balance, overdraft);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Malformed accountPinMessage ");
		}	
	}
	
	private void serviceCustomerRequest(String[] message) {
		try {			
			if (message[0].equals("B")) {
				long balance = atm.queryBalance();
				output.println(balance);
		
			} else if (message[0].equals("W")) {
				long withdrawAmount = Long.parseLong(message[1]);
				long balance =  atm.withdraw(withdrawAmount);
				output.println(balance);
			}
		} catch (ATMError err) {
			output.println(err.getMessage());
		}
	}
}

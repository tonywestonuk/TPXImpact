## TPXImpact - Technical Test.

ATM simulator.

To build:

	mvn package

To run, cd to the target directory and then:
	
	java -jar atm.jar < input.txt 
	
Where input.txt is line input list of commands to send to the atm. There is a copy of input.txt in src/main/resources

Atm.jar will also run interactively without specifying an input file:
	
	java -jar atm.jar
	8000
	12345678 1234 1234
	500 100
	B
	 {Responds with '500'}
	W 100
	 {Responds with '400'}
	W 1000
	 {Responds with 'FUNDS_ERR'}
	B
	 {Responds with '400'}
	 
	CTRL-C to exit
	
## Approach

From the spec, I noticed that there was two main parts to the system:
 - The actual ATM logic which would handle balances, withdrawals, pin validation.
 - A line input processor, which would take input line by line, parsing each line while keeping track of where it was, and then forwarding the requests to the ATM. Any responses received from the Atm, are appended to the output. 

I decided to structure the code around two classes to do this:
 - AtmImpl.java is the ATM business logic
 - AtmLineInputReader.java is what handles the input/output 
 
The two are built to be independent/ decoupled from each other.  If, for example a future development required a HTTP REST type interface, Atm.java would
not need to change.  Just a new ATMRestProcessor to handle the new interface.

Long Intergers have been used to hold the balance.  Though this works great and fits the spec,  integers can not hold fractions. If this was a real ATM I would either make each integer hold a monetary amount in pennies, not pounds (100 = £1) ,  dividing by 100 when the value needs to be shown, and multiplying by 100 when a value is entered.  


This, or use BigDecimal. 
 
**AtmLineInputReader.java**   
Reads each line and validates it based on the current state of reading line input, one of INIT, WAITING_FOR_CUSTOMER, WAITING_FOR_BALANCE, SERVICE_CUSTOMER_REQUEST.  Once parsed, the information is sent to the Atm in the form of method calls.  Any responses from Atm are appended to the output stream writer.
 	 
**AtmImpl.java**   
Handles the actual business logic of the Atm, keeping track of the customer balance, the cash hopper, and if the customer has correctly entered their pin.
 
 The three tests classes I have included:
  - TestATM.java  tests the business logic of AtmImpl.java
  - TestLineInputReader.java tests the logic of the line reader, by ensuring that the processed lines correctly call the correct methods on an Atm mock object.
  - IntegrationTest.java which checks the whole system, by testing that the input provided in the specification, generates the expected output.
  


  

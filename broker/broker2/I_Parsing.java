

public interface I_Parsing {
	//function: finds given string in file: nasdaq
	//input: String to lookup
	//output: (long) quote of input from file on sucess, 0 on error
	public long FIND(String args);
	
	
	//function: appends to the end of file: nasdaq 
	//input: String to append
	//output: -1 on error , 1 on success - prints done to stdout
	public int ADD(String args);
	
	
	//function: updates quote matching symbol in file: nasdaq
	//input: String of symbol, (long) quote to enter
	//output: returns (long) quote on success, -1 on error
	public long UPDATE(String args, long quote);
	
}

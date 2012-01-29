import java.io.*;


public class Parsing implements I_Parsing {
	File file = new File("nasdaq");
	
	//function: appends to the end of file: nasdaq 
	//input: String to append
	//output: -1 on error , 1 on success - prints done to stdout
	public int ADD(String args){
		
		try{		
			
			//check if the given input already exists in the file
			long check = FIND(args);
			
			//if it does not exist: go ahead and append
			//if it does exist : TODO return whatever error code
			if(check != 0){
				//send error packet
				System.out.println(args + " is already in the file: " + file.getName());
				return -1;
			}
		
			String newline = "\n";
	
			FileWriter fileWritter = new FileWriter(file.getName(),true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        
	        bufferWritter.write(newline);
	        bufferWritter.write(args);
	        bufferWritter.close();
			
	        System.out.println("Done");
	    
		}
	   
		catch(Exception e){
			e.printStackTrace();
		}
		 return 1;
	}
	//function: finds given string in file: nasdaq
	//input: String to lookup
	//output: (long) quote of input from file on sucess, 0 on error
	public long FIND(String args){
		try{
			FileReader fileReader = new FileReader(file.getName());
			BufferedReader bufferReadder = new BufferedReader(fileReader);
	        String f_line; 
	        //call reader and check if args (user input) already exists in the database
	        do{
	        	
	        	f_line = bufferReadder.readLine();
	        	
	        	if((f_line != null) && (f_line.contains(args))){
	        		//TODO: tell exchange world already exists
	        		String regex = " ";
	        		String [] out = f_line.split(regex);
	        		//System.out.println(out[1]);
	        		long output = Long.parseLong(out[1]);
	        		return output;
	        	}
	        	
	        }while(f_line != null);
		bufferReadder.close();
		}
	  catch(Exception e){
		  e.printStackTrace();
	  }
		  return 0;
	}
	//function: updates quote matching symbol in file: nasdaq
	//input: String of symbol, (long) quote to enter
	//output: returns (long) quote on success
	public long UPDATE(String args, long quote){
	
		//1) find symbol
		long found = FIND(args);
		//if found continue else return error
		
		if(found != 0){		
		try{
			//2) update number associated with symbol
			FileReader fileReader = new FileReader(file.getName());
			BufferedReader bufferReadder = new BufferedReader(fileReader);
			String[] f_line = new String[5000];
			int count = 0;
			for(int i=0; i<5000; i++){
				f_line[i] = bufferReadder.readLine();
				if(f_line[i] == null){
					fileReader.close();
					break;
				}
			
				if(f_line[i].contains(args) == true){
					//System.out.println("FOUND: " + args + " at " + i);
					String replacement = args + " " + Long.toString(quote);
					f_line[i] = replacement;
					//System.out.println("NEW LINE: " + f_line[i]);
				}
	
				System.out.println("READING:  " + f_line[i]);
				count ++;
				}
			
			//This clears the file - since append is set to false.
				FileWriter fileWritter = new FileWriter(file.getName(), false);
		        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.close();
				
			//fill in with the modified string array
			for(int k = 0; k<count; k++){
				FileWriter fileWritter1 = new FileWriter(file.getName(), true);
		        BufferedWriter bufferWritter1 = new BufferedWriter(fileWritter1);
		      //  System.out.println("SAVED:  " + f_line[k]);
		        bufferWritter1.write(f_line[k]);
		        if(k<count -1)
		        	bufferWritter1.newLine();
		        bufferWritter1.close();
			}
		}

		catch(Exception e){
			e.printStackTrace();
		}
		return quote;
		}
		//not found
		if(found == 0)
			return -1;
		//should not come here
		return 0;
	}
}
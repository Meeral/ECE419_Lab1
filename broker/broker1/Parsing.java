

import java.io.*;

public class Parsing implements I_Parsing {
	File file = new File("nasdaq");
	
	public void APPEND(String args){
		
		try{		
			
			//check if the given input already exists in the file
			long check = FIND(args);
			
			//if it does not exist: go ahead and append
			//if it does exist : TODO return whatever error code
			if(check != 0){
				//send error packet
				System.out.println(args + " is already in the file: " + file.getName());
				return;
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
	}
	
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
}
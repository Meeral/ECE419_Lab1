import java.io.*;
import java.net.*;
	
	public class BrokerClient {
		public static void main(String[] args) throws IOException,
		ClassNotFoundException {
	
	Socket echoSocket = null;
	ObjectOutputStream out = null;
	ObjectInputStream in = null;
	
	try {
		/* variables for hostname/port */
		String hostname = "localhost"; // We need to change this so that we can connect to any computer
		int port = 8888; //we need to change this so we can conenct to any port, technically any 'unused' non dedicated ports
		
		if(args.length == 2 ) {
			hostname = args[0];
			System.out.println(hostname);
			port = Integer.parseInt(args[1]);
		} else {
			System.err.println("ERROR: Invalid arguments!");
			System.exit(-1);
		}
		echoSocket = new Socket(hostname, port); //creates a new Socket object and names it echoSocket. The Socket constructor used here requires the 		name of the machine and the port number to which you want to connect.
	
		out = new ObjectOutputStream(echoSocket.getOutputStream());
		in = new ObjectInputStream(echoSocket.getInputStream());
	
	}
	catch (UnknownHostException e) {
		e.printStackTrace();
		System.err.println("ERROR: Don't know where to connect!!");
		System.exit(1);
	} 
	catch (IOException e) {
		e.printStackTrace();
		System.err.println("ERROR: Couldn't get I/O for the connection.");
		System.exit(1);
	}
	
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	String userInput;
	
	System.out.print("> ");
	while (((userInput = stdIn.readLine()) != null) && (userInput.toLowerCase().indexOf("x") == -1)) {
		/* make a new request packet */
		BrokerPacket packetToServer = new BrokerPacket();
		packetToServer.type = BrokerPacket.BROKER_REQUEST;
		packetToServer.symbol = userInput;
		out.writeObject(packetToServer);
	
		/* print server reply */
		BrokerPacket packetFromServer = new BrokerPacket();
		
		packetFromServer = (BrokerPacket) in.readObject();
		
		if(packetFromServer !=null){
	    if(packetFromServer.type == BrokerPacket.BROKER_QUOTE)
	    {
		System.out.println("Quote from broker:"  + packetFromServer.quote);
	    }
	    else if(packetFromServer.type == BrokerPacket.BROKER_ERROR){
	    	System.out.println("does not exits");
	    }
	    //----------EXPECT EXCHANGE_REPLY---------------//
		/* re-print console prompt */
		System.out.print("> ");
	
	    }
		}
	
	
	/* tell server that i'm quitting */
	BrokerPacket packetToServer = new BrokerPacket();
	packetToServer.type = BrokerPacket.BROKER_BYE;
	//packetToServer.message = "Bye!";
	out.writeObject(packetToServer);
	
	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();
	}
	}
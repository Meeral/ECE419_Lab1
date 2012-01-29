import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class OnlineBrokerThread extends Thread{
	
		Socket socket = null;
	OnlineBrokerThread(Socket serversocket){
		//create new thread
		super("OnlineBrokerThread");
		//set socket
		this.socket = serversocket;	
		//print thread creating message
		System.out.println("Created new thread to handle client requests");
	}
	
	//read incoming client packet - using Brokerpacket.java parameters
	public void run(){
		
		try{
			//1. Create inputstream and output stream variables to read and send
			ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
			
			//Create packet to save input and output messages of type BrokerPacket
			BrokerPacket packetfromclient;
		//	BrokerPacket packettoclient = new BrokerPacket();
			
			//read packet -- process message
			while((packetfromclient = (BrokerPacket) fromClient.readObject())!= null){
				
				//client should send packet with a type BROKER_REQUEST if quote is needed
				if(packetfromclient.type == BrokerPacket.BROKER_REQUEST){
					//read symbol
					//note: symbol here should be a valid string 
					String symbollookup1 = packetfromclient.symbol;
					String symbollookup = symbollookup1.toLowerCase();
					System.out.println(symbollookup);
					//find symbol in nasdaq text file and return associated number
					//every symbol should have an associated number
					Parsing finding;
					finding = new Parsing();
					long output;
					output = finding.FIND(symbollookup);
					long dne = 0;
					if(output == 0){
						//return error
						BrokerPacket packettoclient = new BrokerPacket();
						packettoclient.type = BrokerPacket.BROKER_QUOTE;
						packettoclient.quote = dne;
						toClient.writeObject(packettoclient);
						continue;
					}
					else if (output != 0){
						BrokerPacket packettoclient = new BrokerPacket();
						packettoclient.type = BrokerPacket.BROKER_QUOTE;
						
						packettoclient.quote = output;
						//System.out.println(packettoclient.quote);
						toClient.writeObject(packettoclient);
						continue;
					}
					
				}
				
				//if leaving
				if(packetfromclient.type == BrokerPacket.BROKER_BYE){
					fromClient.close();
					toClient.close();
					socket.close();
					break;
				}
				
				System.err.println("ERROR: UNKNOWN ERROR");
				System.exit(-1);
			}
			
			fromClient.close();
			toClient.close();
			socket.close();
			
			
		}
		catch (IOException e){
			e.printStackTrace();
		}
		catch (ClassNotFoundException c){
			c.printStackTrace();
		}
	
	}
	
}

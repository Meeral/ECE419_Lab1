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
			//BrokerPacket packettoclient = new BrokerPacket();
			
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
				
				//handle the exchange client -- ADDING
				if(packetfromclient.type == BrokerPacket.EXCHANGE_ADD){
					String symbollookup1 = packetfromclient.symbol;
					String symbollookup = symbollookup1.toLowerCase();
					BrokerPacket packettoclient = new BrokerPacket();
					packettoclient.type = BrokerPacket.EXCHANGE_REPLY;
					System.out.println(symbollookup);
					//add symbol at the end of file
					Parsing finding;
					finding = new Parsing();
					int err; 
					err = finding.ADD(symbollookup);
					
					if(err == -1){
						//return string that says : [input symbol] exists. 
						String exists = symbollookup + " exists.";
						packettoclient.symbol = exists;
						toClient.writeObject(packettoclient);
						continue;
					}
						//else return confirmation string 
						String confirmation = symbollookup + " added.";
						packettoclient.symbol = confirmation;
						toClient.writeObject(packettoclient);
						continue;
				}
				//handle the exchange client -- UPDATE
				if(packetfromclient.type == BrokerPacket.EXCHANGE_UPDATE){
					String symbollookup1 = packetfromclient.symbol;
					long quote = packetfromclient.quote;
					String symbollookup = symbollookup1.toLowerCase();
					BrokerPacket packettoclient = new BrokerPacket();
					packettoclient.type = BrokerPacket.EXCHANGE_REPLY;
					System.out.println(symbollookup);
					//update symbol only if it exists
					Parsing finding;
					finding = new Parsing();
					long err; 
					err = finding.UPDATE(symbollookup, quote);
					if(err == -1){
						//symbol not found - does not exist
						//return string: [input symbol] invalid. 
						String invalid = symbollookup + " invalid.";
						packettoclient.symbol = invalid;
						toClient.writeObject(packettoclient);
						continue;
					}
					if(err == 0){
						//should never return 0 - problem
						//exit
						System.out.println("SOMETHING BAD HAPPENED WHILE UPDATING FILE- IGNORING PACKET");
						continue;
					}
					//have found the symbol to update in the file and it has been updated
					String confirmation = symbollookup + " updated to " + Long.toString(quote); 
					packettoclient.symbol = confirmation; 
					toClient.writeObject(packettoclient);
					continue;
				}
				if(packetfromclient.type == BrokerPacket.EXCHANGE_REMOVE){
					
				}
				//if leaving
				if(packetfromclient.type == BrokerPacket.BROKER_BYE){
	
					fromClient.close();
					toClient.close();
					//socket.close();
					break;
				}
				
				System.err.println("ERROR: UNKNOWN ERROR");
				System.exit(-1);
			}
			
			fromClient.close();
			toClient.close();
			socket.close();
			return;
			
		}
		catch (IOException e){
			e.printStackTrace();
		}
		catch (ClassNotFoundException c){
			c.printStackTrace();
		}
	
	}
	
}

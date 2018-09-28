package scrabble;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Quite a lot of this code was produced by 'Mordechai' from 
 * https://stackoverflow.com/questions/13115784/sending-a-message-to-all-clients-client-server-communication
 */

public class Server {
	
	private int turn=0;
	private int numberOfPlayers;
	private ArrayList<ConnectionToClient> clients;
	private ServerSocket serverSocket;
	private LinkedBlockingQueue<Object> messages;
	
	public Server(int port, int numberOfPlayers) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.numberOfPlayers = numberOfPlayers;
		
		Thread accept = new Thread() {
            public void run(){
                while(true){
                    try{
                    	if (clients.size() < numberOfPlayers) {
                    		Socket s = serverSocket.accept();
                        	clients.add(new ConnectionToClient(s, clients.size()));
                    	}
                    }
                    catch(IOException e){ 
                    	e.printStackTrace(); 
                    }
                }
            }
        };
        accept.setDaemon(true);
        accept.start();
        
        Thread messageHandling = new Thread() {
            public void run(){
                while(true){
                    try{
                        Object message = messages.take();
                        // Do some handling here...
                    }
                    catch(InterruptedException e){ }
                }
            }
        };

        messageHandling.setDaemon(true);
        messageHandling.start();
	}
	
	public void nextTurn() {
		turn = (turn+1)%numberOfPlayers;
	}
	
	private class ConnectionToClient {
		ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket;
        int index;
        
		ConnectionToClient(Socket socket, int index) throws IOException {
			this.socket = socket;
			this.in = new ObjectInputStream(socket.getInputStream());
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.index = index;
			
			Thread read = new Thread(){
                public void run(){
                    while(true){
                        try{
                            Object obj = in.readObject();
                            if (index == turn) {
                            	messages.put(obj);
                            	nextTurn();
                            }
                        }
                        catch(Exception e){ 
                        	e.printStackTrace(); 
                        }
                    }
                }
            };

            read.setDaemon(true); // terminate when main ends
            read.start();
		}
		
		public void write(Object obj) {
            try{
                out.writeObject(obj);
            }
            catch(IOException e){ 
            	e.printStackTrace(); 
            }
		}
	}

}
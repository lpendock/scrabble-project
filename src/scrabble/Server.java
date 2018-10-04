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
	private LinkedBlockingQueue<String> messages;
	private Main main;

	public void runServer() {

		Thread accept = new Thread() {
			public void run(){
			while(true){
				System.out.println("Listening...");
				try{
					int size = clients.size();
					if (size < numberOfPlayers) {
						Socket s = serverSocket.accept();
						s.setKeepAlive(true);
						System.out.println("Connected to client " + size);
						clients.add(new ConnectionToClient(s, size));

					}
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			}
		};
		accept.start();

		Thread messageHandling = new Thread() {
			public void run(){
				//System.out.println("Checking messages...");
				while(true){
					try{
						String message = messages.take();
						// Do some handling here...
						main.parseCommand(message);
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		};

		messageHandling.setDaemon(true);
		messageHandling.start();
//		while(true);
	}

	public Server(int port, int numberOfPlayers, Main main) throws IOException {
		this.main = main;
		this.serverSocket = new ServerSocket(port);
		this.numberOfPlayers = numberOfPlayers;
		this.clients = new ArrayList<ConnectionToClient>();
		this.messages = new LinkedBlockingQueue<String>();
	}

	
	public void sendMessageToAll(String message) {
		for (ConnectionToClient c: clients) {
			c.write(message);
		}
	}


	public void nextTurn() {
		turn = (turn+1)%numberOfPlayers;
	}
	
	private class ConnectionToClient {
		InputStream in;
        OutputStream out;
        DataInputStream dis;
    	DataOutputStream dos;
        Socket socket;
        int index;
        boolean active = true;
        
		ConnectionToClient(Socket socket, int index) throws IOException {
			this.socket = socket;
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();
			this.index = index;
			this.dis = new DataInputStream(in);
			this.dos = new DataOutputStream(out);
			
			Thread read = new Thread(){
                public void run(){
				while(active) {
				   try {
						String message = dis.readUTF();
						messages.put(message);
				   }
				   catch (Exception e) {
						e.printStackTrace();
						active=false;
				   }
				}
                }
            };

            read.start();
		}
		
		
		public void write(String str) {
            try{
            	if (active) {
					dos.writeUTF(str);
				}
            }
            catch(IOException e){
            	active=false;
            	e.printStackTrace();
            	//lost connection with server
            	
            }
		}
	}

}
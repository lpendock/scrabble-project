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
	
	public static void main(String[] args) {
		try {
			Server s = new Server(8080, 4);
			s.runServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int turn=0;
	private int numberOfPlayers;
	private ArrayList<ConnectionToClient> clients;
	private ServerSocket serverSocket;
	private LinkedBlockingQueue<String> messages;

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
						System.out.println("adding a client, now clients size is: " + clients.size());
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
						System.out.println(message);
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

	public Server(int port, int numberOfPlayers) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.numberOfPlayers = numberOfPlayers;
		this.clients = new ArrayList<ConnectionToClient>();
		this.messages = new LinkedBlockingQueue<String>();
	}
	
	public void sendMessage(String message, int index) {
		clients.get(index).write(message);
	}
	
	public void sendMessageToAll(String message) {
		System.out.println("I was out side");
		System.out.println("clients size: " + clients.size());
		for (ConnectionToClient c: clients) {
			System.out.println("I was above.");
			c.write(message);
			System.out.println("I was here.");
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

//            read.setDaemon(true); // terminate when main ends
            read.start();
		}
		
		
		public void write(String str) {
            try{
            	if (active) {
					dos.writeUTF(str);

					System.out.println("wrote something");
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
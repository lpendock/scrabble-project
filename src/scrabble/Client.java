package scrabble;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * Handles connection and network logic if client.
 */

public class Client {

	private Socket socket;
	private LinkedBlockingQueue<String> messages;
	private ConnectionToServer server;
	private Main main;
	private boolean active = true;
	
	/**
	 * Creates a client class 
	 * @param port Number needed to connect to correct Server port
	 * @param address The ip address of the server
	 * @param main 	The main class of the scrabble program
	 * @throws IOException
	 */
	public Client(int port, String address, Main main) throws IOException {
		this.main = main;
		this.socket = new Socket(address,port);
		System.out.println("client initiated");
		this.messages = new LinkedBlockingQueue<String>();

	}


	/**
	 * starts the process of connecting to the server and message handling 
	 */
	public void runClient () {
		//attempts to connect to the server
		Thread connectToServer = new Thread() {
			@Override
			public void run() {
				System.out.println("run client..");
				try{
					server = new ConnectionToServer(socket);
					System.out.println("Connected to server ");
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		};
		connectToServer.start();
		
		//attempts to start message handling
		Thread messageHandling = new Thread() {
			public void run(){
				
				while(true){
					try{
						if (!messages.isEmpty()) {
							String message = messages.take();
							System.out.println(message);
							main.parseCommand(message);
						}
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		};
		messageHandling.start();
	}

	public void sendToServer(String message) {
		while (this.server == null) {
			System.out.print(".");
		}
		System.out.println("\nClient sends message: " + message);
		this.server.write(message);
	}

	private class ConnectionToServer {
		InputStream in;
		OutputStream out;
		DataInputStream dis;
		DataOutputStream dos;
		Socket socket;
		boolean active = true;

		ConnectionToServer(Socket socket) throws IOException {
			this.socket = socket;
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();
			this.dis = new DataInputStream(in);
			this.dos = new DataOutputStream(out);

			Thread read = new Thread(){
				public void run(){
					while(active) {
						try {
							String message = dis.readUTF();
							messages.put(message);
						} catch (Exception e) {
							active=false;
							System.out.println("I'm inactive now.");
							main.initCheckWinner();
							main.initDisconnected();
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

package scrabble;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
	
	public static void main(String[] args) {
		try {
			Client c = new Client(8080, "127.0.0.1");
			c.runClient();
			c.sendToServer("I heard you !!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Socket socket;
	private LinkedBlockingQueue<String> messages;
	private ConnectionToServer server;

	private boolean active = true;
	
	public Client(int port, String address) throws IOException {
		this.socket = new Socket(address,port);
		System.out.println("client initiated");
		this.messages = new LinkedBlockingQueue<String>();

	}

	public void sendToServer(String message) {
		while (this.server == null) {
			System.out.println("connecting...");
		}
		this.server.write(message);
	}

	public void runClient () {
		Thread connectToServer = new Thread() {
			@Override
			public void run() {
				System.out.println("Listening...");
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

		Thread messageHandling = new Thread() {
			public void run(){
				//System.out.println("Checking messages...");
				while(true){
					try{
						if (!messages.isEmpty()) {
							String message = messages.take();
							System.out.println(message);
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


	private class ConnectionToServer {
		InputStream in;
		OutputStream out;
		DataInputStream dis;
		DataOutputStream dos;
		Socket socket;
		int index;
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
							System.out.println("I heard something");
							messages.put(message);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							active=false;
							System.out.println("I'm inactive now.");
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

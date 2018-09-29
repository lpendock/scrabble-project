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
	
	public Server(int port, int numberOfPlayers) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.numberOfPlayers = numberOfPlayers;
		this.clients = new ArrayList<ConnectionToClient>();
		this.messages = new LinkedBlockingQueue<String>();
		Thread threads[] = new Thread[numberOfPlayers];
		
		Thread accept = new Thread() {
            public void run(){
                while(true){
                	System.out.println("Listening...");
                    try{
                    	int size = clients.size();
                    	if (size < numberOfPlayers) {
                    		Socket s = serverSocket.accept();
                    		System.out.println("Connected to client " + size);
                    		threads[size] = new Thread() {
                    			public void run(){
                    				try {
										clients.add(new ConnectionToClient(s, size));
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                    			}
                    		};
                    		threads[size].setDaemon(true);
                    		threads[size].start();
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
            	//System.out.println("Checking messages...");
                while(true){
                    try{
                        String message = messages.take();
                        // Do some handling here...
                        System.out.println(message);
                    }
                    catch(InterruptedException e){ }
                }
            }
        };

        messageHandling.setDaemon(true);
        messageHandling.start();
        while(true);
        
	}
	
	public void sendMessage(String message, int index) {
		clients.get(index).write(message);
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
						String message = new String(dis.readUTF());
						if (!message.equals("Are you still there?"))
							messages.put(message);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							active=false;
						}                       
                    }
                }
            };

            read.setDaemon(true); // terminate when main ends
            read.start();
            
            Thread checkConnection = new Thread(){
                public void run(){
                    while(active){
							//Communicate with client
						try {
							sleep(1000);
							write("Are you still there?");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							active=false;
							break;
						}
                        
                    }
                }
            };
            checkConnection.setDaemon(true);
    		checkConnection.start();
    		
    		while(true);
		}
		
		
		public void write(String str) {
            try{
            	if (active)
            		dos.writeUTF(str);
            }
            catch(IOException e){
            	active=false;
            	e.printStackTrace();
            	//lost connection with server
            	
            }
		}
	}

}
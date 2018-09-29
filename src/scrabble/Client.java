package scrabble;

import java.io.*;
import java.net.*;

public class Client {
	
	public static void main(String[] args) {
		try {
			Client c = new Client(8080, "127.0.0.1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public Client(int port, String address) throws IOException {
		this.socket = new Socket(address,port);
		System.out.println("Connected...");
		this.in = socket.getInputStream();
		this.out = socket.getOutputStream();
		this.dis = new DataInputStream(in);
		this.dos = new DataOutputStream(out);
		
		Thread checkConnection = new Thread(){
            public void run(){
                while(true){
                    try {
                    	sleep(1000);
                    	String st = new String(dis.readUTF());
                    	dos.writeUTF("Are you still there?");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
                    
                }
            }
        };
        checkConnection.setDaemon(true);
		checkConnection.start();
		
		//For testing
		Thread sendMessages = new Thread(){
            public void run(){
                while(true){
                    try {
                    	sleep(1000);
                    	dos.writeUTF("Yee");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
                    
                }
            }
        };
        sendMessages.setDaemon(true);
        sendMessages.start();
		
		while(true);
	}

}

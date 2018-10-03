package scrabble;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.concurrent.ConcurrentLinkedQueue;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean host = false;
//	private ConcurrentLinkedQueue<String> members;
	private ArrayList<String> members;
	private String currentPlayer;
	public Server server;
	public Client client;
	private Game game;

	public boolean isHost() {
		return host;
	}

	/**
	 * Set the player of the current game
	 * @param playerName name of the player
	 */
	public void setPlayer(String playerName) {
		this.currentPlayer = playerName;
		members.add(this.currentPlayer);
	}


	/**
	 * Return the player of current game
	 * @return player name
	 */
	public String getPlayer() {
		return this.currentPlayer;
	}

	public void startGame() {

		if (host) {
			game.setPlayerTurn(true);
			this.server.sendMessageToAll("startGame");
		}
		// show game
		game.setTitle("Scrabble -- " + currentPlayer);
		game.setVisible(true);


		// when game started, the member list should be fixed, so
		// we filter out any possible null values.
		ArrayList<String> finalMembers = new ArrayList<>();
		for (String member : members) {
			if (member != null) {
				finalMembers.add(member);
			}
		}
		this.members = finalMembers;

		game.initInfoBoard();
		// hide the main GUI
		this.setVisible(false);
		validate();
	}


	public ArrayList<String> getMemberList() {
		return members;
	}


	/**
	 * Join one more player to the game
	 * @param name
	 */
	public void addMember(String name) {
		members.add(name);
	}


	public void notifyClientsMemberChanges() {

		if (this.isHost()) {
			String command = "memberUpdated";
			for (String name : members) {
				if (name!=null) {
					command = command + "#" + name;
				}
			}
			this.server.sendMessageToAll(command);
		}
	}

	public void parseCommand(String command) {
		System.out.println("command received is: " + command);
		String[] commands = command.split("#");
		if (commands.length < 1) System.out.println("commands not valid");

		if (commands[0].equals("updateBoard") && commands.length == 4) {
			this.game.getGameBoard().updateBoard(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]), commands[3]);
			if (this.isHost()) {
				this.game.notifyBoardChanges(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]), commands[3]);
			}
			return;
		}

		if (commands[0].equals("memberAdded")) {
			addMember(commands[1]);
			notifyClientsMemberChanges();
			initMemberMenu();
			return;
		}

		if (commands[0].equals("memberUpdated")) {
			this.members = new ArrayList<>();
			for (int i = 1; i < commands.length; i++) {
				addMember(commands[i]);
			}
			initMemberMenu();
			return;
		}


		// should only be received by clients
		if (commands[0].equals("startGame")) {
			startGame();
			return;
		}

		// should only be received by host
		if (commands[0].equals("finishedTurn")) {
			String nextPlayer = this.game.getPlayerNextTurn(commands[1]);
			if (nextPlayer.equals(this.currentPlayer)) {
				this.game.setPlayerTurn(true);
			} else {
				this.game.notifyNextPlayer(nextPlayer);
			}
			return;
		}

		// should only be received by clients
		if (commands[0].equals("nextPlayer")) {
			if (commands[1].equals(this.currentPlayer)) {
				this.game.setPlayerTurn(true);
			}
			return;
		}
	}

	public void notifyServerJoiningGame() {
		String command = "memberAdded#" + this.currentPlayer;
		this.client.sendToServer(command);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				initMemberMenu();
			}
		});

	}




	public void initMemberMenu() {
		this.setContentPane(new MembersMenu(this));
		this.validate();
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
					frame.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(java.awt.event.WindowEvent windowEvent) {
							if (JOptionPane.showConfirmDialog(frame,
									"Are you sure you want to close this window?", "Close Window?",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
								//can send info to whatever before exit
								int exiting = 1;
								System.out.println("exiting");
								System.exit(0);
							}
						}
					});
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		setBounds(100, 100, 1000, 800);
		setTitle("Scrabble");
		this.members = new ArrayList<>();

		this.game = new Game(this);

		int n = JOptionPane.showConfirmDialog(
				this,
				"Do you want to host a game?",
				"new game",
				JOptionPane.YES_NO_OPTION);

		if (n == JOptionPane.YES_OPTION) {
			initServer();
			Thread runServer = new Thread() {
				public void run() {
					server.runServer();
				}
			};
			runServer.start();
		} else {
			initClient();
			Thread runClient = new Thread() {
				public void run() {
					client.runClient();
					client.sendToServer("client is here !!!!");
				}
			};
			runClient.start();
		}

		Login login = new Login(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(login);
	}


	private void initServer() {
		try {
			this.host = true;
			server = new Server(8080, 4, this);
			System.out.println("init server!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private void initClient() {
		try {
			client = new Client(8080, "127.0.0.1", this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

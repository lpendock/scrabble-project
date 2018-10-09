package scrabble;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean host = false;
	private ArrayList<String> members;
	private String currentPlayer;
	private boolean isGameRunning = false;
	public int passCount = 0;

	public scrabble.Server server;
	public scrabble.Client client;
	public scrabble.MembersMenu membersMenu;


	private int port = 8080;
	//just to check if client is connected to a server;
	private int clientConnected = 0;
	//for name checking(or anyother form of checking)
	private boolean check;
	private Game game;
	//Default port and IP, will be changed when user enters it in login.
	private String IPaddress = "127.0.0.1";


	public void startGame() {

		this.isGameRunning = true;

		//the first player in the list will start the game first
		if (this.game.playerList.get(0).equals(getPlayer())) {
			game.setPlayerTurn(true);
			game.getGameBoard().setFirstHand(true);
		}

		// show game
		game.setTitle("Scrabble -- " + currentPlayer);
		game.setVisible(true);


		game.initInfoBoard();
		// hide the main GUI
		this.setVisible(false);
		validate();
	}


	public void parseCommand(String command) {
		System.out.println("command received is: " + command);
		String[] commands = command.split("#");
		if (commands.length < 1) System.out.println("commands not valid");

		switch (commands[0]) {
			case "updateBoard":
				this.game.getGameBoard().updateBoard(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]), commands[3]);
				if (this.isHost()) {
					this.game.notifyBoardChanges(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]), commands[3]);
				}
				break;

			case "memberAdded":
				System.out.println("got name from client!");
				addMember(commands[1]);
				// only host can send this message
				notifyClientsMemberChanges();
				initMemberMenu();
				break;

			case "memberUpdated":
				this.members = new ArrayList<>();
				for (int i = 1; i < commands.length; i++) {
					addMember(commands[i]);
				}
				System.out.println("updated members list,refreshing memberMenu");
				initMemberMenu();
				break;

			// all players can send this message to invite other
			// if host is not invited, host have to broadcast this message
			case "invite":
				String inviter = commands[1];
				String invitee = commands[2];
				if (getPlayer().equals(invitee) && !this.membersMenu.isInvited()) {
					this.membersMenu.initInvitationDialog(inviter);

				} else if (isHost()) {
					// resend this message to all clients
					this.server.sendMessageToAll("invite#" + inviter + "#" + invitee);
				}
				break;

			// if host is the inviter, host have to broadcast this message
			case "acceptInvitation":
				if (commands[1].equals(getPlayer())) {
//					EventQueue.invokeLater(new Runnable() {
//						@Override
//						public void run() {
//							initMemberMenu();
//						}
//					});
					this.membersMenu.addInvitee(commands[2]);
					return;
				} else if (isHost()) {
					this.server.sendMessageToAll("acceptInvitation#" + commands[1] + "#" + commands[2]);
				}
				break;


			//Very similar to logic of memberUpdated and all that(didnt want to
			//touch it due to it being used in other places)
			case "getMemberList":
				System.out.println("sending list");
				 notifyClientsMemberList();
				break;

			case "updateMemberList":
				this.members = new ArrayList<>();
				for (int i = 1; i < commands.length; i++) {
					addMember(commands[i]);
				}
				break;


			case "startGame":

				if (isHost()) {
					//notify all clients to start the game
					this.server.sendMessageToAll(command);
				}

				//update player list to play list: those who play the game
				this.game.playerList = new ArrayList<>();
				for (int j = 1; j < commands.length; j++) {
					this.game.playerList.add(commands[j]);
				}

				for (int i = 1; i < commands.length; i ++) {
					// if this player is invited to the game
					if (commands[i].equals(getPlayer())) {

						startGame();
						return;
					}
				}


				break;

			// should only be received by host
			case "finishedTurn":
				String nextPlayer = this.game.getPlayerNextTurn(commands[1]);
				// if received "true", it means play pass the round
				// then add the counter
				if (commands[2].equals("true")) {
					game.addPass();
				} else {
					// once it's not pass, reset the count to zero;
					passCount = 0;
				}
				if (nextPlayer.equals(this.currentPlayer)) {
					this.game.setPlayerTurn(true);
				} else {
					this.game.notifyNextPlayer(nextPlayer);
				}
				break;

			// should only be received by clients
			case "nextPlayer":
				if (commands[1].equals(this.currentPlayer)) {
					this.game.setPlayerTurn(true);
				}
				break;

			case "attemptedWord":
				if (isHost()) {
					this.game.notifyWordAttempted(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]), Integer.parseInt(commands[3]), Integer.parseInt(commands[4]));
				}
				this.game.getGameBoard().highlightAttemptedWord(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]), Integer.parseInt(commands[3]), Integer.parseInt(commands[4]));
				break;

			case "initVote":
				// todo: host received word index

				if (isHost()) {
					this.game.initVote(commands[1], commands[2]);
					String index = commands[3] + "#" + commands[4] + "#" + commands[5] + "#" + commands[6];
					this.game.getVote().setIndex(index);
				}

				// all players except the initiator should vote
				if (!this.currentPlayer.equals(commands[1]) && isGameRunning()) {
					this.game.displayVote();
				}
				break;

			// only host should receive this command
			case "voteOpinion":
				this.game.updateVote(commands[1]);
				break;

			// clients update their scores
			case "updateScore":
				this.game.getGameInfoBoard().updateScore(commands[1], Integer.parseInt(commands[2]));
				break;

			case "completedWord":
				if (commands[1].equals("true")) {
					this.game.getGameBoard().highlightCompletedWord(Integer.parseInt(commands[2]),
							Integer.parseInt(commands[3]), Integer.parseInt(commands[4]), Integer.parseInt(commands[5]));
				} else {
					this.game.getGameBoard().setBackAttemptedWord(Integer.parseInt(commands[2]),
							Integer.parseInt(commands[3]), Integer.parseInt(commands[4]), Integer.parseInt(commands[5]));
				}
				break;

			// should only be received by host
			case "exit":
				if (!isHost()) return;
				notifyClientsEndGame();
				initCheckWinner();
				break;

			// should only be received by clients not host
			case "endGame":
				initCheckWinner();
				break;

			case "logout":
				memberLogout(commands[1]);
				notifyClientsMemberChanges();
				initMemberMenu();
				break;
		}

	}

	/**
	 * --------------------------------------Notification-------------------------------------------------
	 */


	// notify host or clients the game start with certain members
	public void notifyGameStart() {
		String command = "startGame";
		for (String member : membersMenu.getInviteeList() ){
			if (member != null) {
				command = command + "#" + member;
			}
		}
		if (isHost()) {
			this.server.sendMessageToAll(command);
			this.game.playerList = membersMenu.getInviteeList();
			startGame();
		} else {
			this.client.sendToServer(command);
		}
	}


	// should only be called by server
	// every player has to end the game
	public void notifyClientsEndGame() {
		String command = "endGame#";
		this.server.sendMessageToAll(command);

	}

	// tell server that client has closed;
	// then all player end game
	public void notifyExitGame() {
		String command = "exit#" + getPlayer();
		this.client.sendToServer(command);
	}

	// client notify server before close
	public void notifyLogout() {
		String command = "logout#" + getPlayer();
		this.client.sendToServer(command);
	}


	public void notifyClientsMemberChanges() {

		if (this.isHost()) {
			String command = "memberUpdated";
			for (String name : members) {
				if (name!=null) {
					command = command + "#" + name;
				}
			}
			System.out.println("sending names to clients! ");
			this.server.sendMessageToAll(command);
		}
	}


	//Update everyone due the way our architecture works i cant set to the one who requested it.
	//I dont think it would cause any problems due to the logic of our program(will test more).

	public void notifyClientsMemberList() {
		if (this.isHost()) {

			String command = "updateMemberList";
			for (String name : members) {
				if (name!=null) {
					command = command + "#" + name;
				}
			}

			this.server.sendMessageToAll(command);
		}
	}

	// any user send out invitation to other members
	public void notifyInvitation(String invitee) {
		String command = "invite#" + getPlayer() + "#" + invitee;
		if (isHost()) {
			this.server.sendMessageToAll(command);
		} else {
			this.client.sendToServer(command);
		}
	}

	// notify inviter acceptance after player click accept
	public void notifyAcceptInvitation(String inviter) {
		String command = "acceptInvitation#" + inviter + "#" + getPlayer();
		this.membersMenu.setInvited();
		this.membersMenu.banInvitationFunction();
		if (isHost()) {
			this.server.sendMessageToAll(command);
		} else {
			this.client.sendToServer(command);
		}
	}


	public void notifyServerJoiningGame() {
		String command = "memberAdded#" + this.currentPlayer;
		System.out.println("sending name to server!");
		this.client.sendToServer(command);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				initMemberMenu();
			}
		});

	}

	/**
	 * --------------------------------------Init Components-------------------------------------------------
	 */


	//Simple winner calculator
	public void initCheckWinner() {
		if (!isGameRunning()) return;
		ArrayList<String> winners = this.game.getGameInfoBoard().checkWinner();
		JOptionPane.showMessageDialog(this, "Winners is/are "+ winners);

		System.exit(0);
	}


	public void initMemberMenu() {
		this.membersMenu = new MembersMenu(this);
		this.setContentPane(membersMenu);
		this.validate();
	}
	
	
	public void initLogin() {
		this.setContentPane(new Login(this));
		this.validate();
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


		// handle closing operations
		this.game.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(game,
						"Are you sure you want to close this window?", "Close Window?",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){

					// all player end game
					if(isHost()) {
						notifyClientsEndGame();
						initCheckWinner();
					} else {
						notifyExitGame();
						notifyLogout();
					}

					System.out.println("exiting");
				}
			}
		});
		this.game.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		int n = JOptionPane.showConfirmDialog(
				this,
				"Do you want to host a game?",
				"new game",
				JOptionPane.YES_NO_OPTION);

		if (n == JOptionPane.YES_OPTION) {

			//sets that this specific program is a host for login.
			this.host = true;
			
		} else {
			//set not host let login start client stuff.
			this.host = false;

		}

		//start at connectionMenu first
		connectionMenu con = new connectionMenu(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(con);
	}


	/**
	 * --------------------------------------InitServer-------------------------------------------------
	 */


	//start server starting process
	public void startServerProcess() {
		initServer();
		Thread runServer = new Thread() {
			public void run() {
				server.runServer();
			}
		};
		runServer.start();
	}
	
	public void startClientProcess() {

		Thread runClient = new Thread() {
			public void run() {
				client.runClient();
				client.sendToServer("client is here !!!!");
			}
		};
		runClient.start();
		//might not need this anymore
		clientConnected = 1;
		
	}
	private void initServer() {
		try {
			this.host = true;
			server = new Server(port, 4, this);
			System.out.println("init server!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public boolean initClient() {
		try {
			client = new Client(port, IPaddress, this);
			return true;
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(this,
					"There is no host on this address. Please check your IP address and Port number"
					,"Host not found",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			return false;
		}
	}

	/**
	 * --------------------------------------MISC-------------------------------------------------
	 */

	public boolean isGameRunning() {
		return isGameRunning;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public int getClientConnected() {
		return clientConnected;
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

	public void memberLogout(String member) {
		members.remove(member);
		members.removeAll(Collections.singleton(null));
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

	public void setIPaddress(String iPaddress) {
		IPaddress = iPaddress;
		System.out.println("ip set to: "+IPaddress);
	}

	//checks name (will remove value setcheck and just return)
	public boolean checkNameTaken(String name) {
		System.out.println(members);
		if(members.contains(name)) {
			System.out.println("set true");
			setCheck(true);
		}else {
			System.out.println("set false");
			setCheck(false);
		}
		return check;
	}

	public void setPort(int port) {

		this.port = port;
		System.out.println("Port set to: "+port);
	}

	public boolean isHost() {
		return host;
	}


	/**
	 * --------------------------------------Main Method-------------------------------------------------
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

								// server tell all clients to shut down
								if (frame.isHost() ) {

									if (!frame.getMemberList().isEmpty()) {
										frame.notifyClientsEndGame();
									}
									System.exit(0);
								} else {

									if (frame.isGameRunning()) {
										// all player end game
										frame.notifyExitGame();
									}else {
										// if logged in
										if (!frame.getMemberList().isEmpty()) {
											frame.notifyLogout();
										}
											// non-login just exit
										System.exit(0);


									}
								}
								System.out.println("exiting");
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

}

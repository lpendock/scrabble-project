package scrabble;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	int players = 1;
	String player= "";
	private boolean host = false;
	public Server server;

	public boolean isHost() {
		return host;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
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
		//Toolkit tk = Toolkit.getDefaultToolkit();
		//Dimension screenSize = tk.getScreenSize();
		//setLocation(screenSize.width / 2, screenSize.height / 2);
		this.getContentPane().setLayout(new BorderLayout());
		setBounds(100, 100, 1000, 800);
		setTitle("Scrabble");


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
//			SwingUtilities.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					initServer();
//				}
//			});


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
			server = new Server(8080, 4);
			System.out.println("init server!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void changePanel(int num) {
		//TEMP solution(probably) decide which panel do display based on pre-defined numbers
		// 2 = board
		// 3 = membersMenu
		if (num == 2) {
			Game game = new Game(this);
			// show game
			game.setTitle("Scrabble");
			game.setVisible(true);
			game.addMember(player);
			game.initInfoBoard();
			// hide the main GUI
			this.setVisible(false);
			validate();
		}
		else if(num == 3) {
			MembersMenu menu = new MembersMenu(this);
			setContentPane(menu);
			validate();
		}
	}

}

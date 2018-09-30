package scrabble;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GUI_main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	int players = 1;
	String player= "";

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
					GUI_main frame = new GUI_main();
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
	public GUI_main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Toolkit tk = Toolkit.getDefaultToolkit();
		//Dimension screenSize = tk.getScreenSize();
		//setLocation(screenSize.width / 2, screenSize.height / 2);
		this.getContentPane().setLayout(new BorderLayout());
		setBounds(100, 100, 1000, 800);
		setTitle("Scrabble");
		Login login = new Login(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(login);
	}
	
	public void changePanel(int num) {
		//TEMP solution(probably) decide which panel do display based on pre-defined numbers
		// 2 = board
		// 3 = membersMenu
		if (num == 2) {
			GameView gameView = new GameView();
			// show gameView
			gameView.setTitle("Scrabble");
			gameView.setVisible(true);
			gameView.addMember(player);
			gameView.initInfoBoard();
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

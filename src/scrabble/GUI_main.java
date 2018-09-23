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
	int players = 2;

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
		setBounds(100, 100, 1000, 800);
		Login login = new Login(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(login);
	}
	
	public void changePanel(int num) {
		if (num == 2) {
			board board = new board(players);
			setContentPane(board);
			validate();
		}
	}

}

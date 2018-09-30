package scrabble;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class board extends JPanel {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SQUARE_SIZE = 10;
	int players = 2;

	JPanel scorePanel = new JPanel();
	JLabel[] scores = null;

	JButton rackButton1 = new JButton();
	JButton rackButton2 = new JButton();
	JPanel gridPanel = new JPanel();
	JButton[][] grid = new JButton[20][20];
	JPanel[][] squares = new JPanel[20][20];
	String setLetter = "";


	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 800;
	private AlphabetPanel alphabetPanel;

	// Button to initiate vote
	private final JButton voteButton = new JButton("vote");



	private int playerNums;


	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_WIDTH, DEFAULT_WIDTH);
	}
	
	/**
	 * Create the panel.
	 */
	public board(AlphabetPanel alphabetPanel) {
		this.alphabetPanel = alphabetPanel;
		setLayout(new BorderLayout());
		this.players = players;
		this.playerNums = 4;

		//create the bag and draw first hand.
		//this can be changed where a client has to send a request or something


		scores = new JLabel[players];
		initGUI();
		this.setBorder(new LineBorder(Color.BLACK));
		setVisible(true);
	
	}

	
	private void initGUI() {
		initGrid();
//		initScorePanel();
		add(gridPanel);
		add(scorePanel, BorderLayout.NORTH);

		
	}

//	private void initScorePanel() {
//		scorePanel.setLayout(new GridLayout(1, players));
//		for (int row = 0; row < players; row++) {
//			//Can use arraylist or whatever to save all player names
//			//For now just uses one player
//			JLabel label = new JLabel(gui.getPlayer()+":");
//			scores[row] =label;
//			scorePanel.add(label);
//		}
//
//	}



	private void initGrid() {
		gridPanel.setLayout(new GridLayout(20, 20));



		for (int row = 0; row < 20; row++) {
			for (int column = 0; column < 20; column++) {
				//Square square = board.getSquare(row, column);
				JPanel panel = new JPanel();
				JButton button = new JButton();

				button.setFont(new Font("Arial", Font.BOLD, 23));

				//Row and column number can be sent to other players when it updates
				final int rownum = row;
				final int columnnum = column;
				button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	JOptionPane.showMessageDialog(panel,
	            		    "grid location:"+rownum+columnnum);

					if (alphabetPanel.getTheChosenTile() != null) {
						setLetter = alphabetPanel.getTheChosenTile().getText();
					}

	            	if(button.getText().equals("") && !setLetter.equals("")) {
	            		button.setText(setLetter);
						System.out.println("button recieved: " + alphabetPanel.getTheChosenTile().getText());
	            		setLetter = "";
	            		alphabetPanel.setNewTile();

	            		checkScore(rownum,columnnum);
	            	}
	            }
				});
				button.setBackground(Color.white);
				button.setMargin( new Insets(5, 5, 5, 5) );
				panel.setBackground(Color.white);
				panel.setSize(50, 50);
				panel.setLayout(new BorderLayout(0, 0));
				panel.setBorder(BorderFactory.createEtchedBorder());
				panel.add(button);
				squares[row][column] = panel;
				grid[row][column] = button;
				gridPanel.add(panel);
			}
		}
	}

	


	//Can be used to update board when we implement that
	public void updateBoard() {
		for (int row = 0; row < 20; row++) {
			for (int column = 0; column < 20; column++) {
				//JLabel label = grid[row][column];
			
		}
}
	}


	public void checkScore(int rownum,int columnnum) {    
		String word1 = "";
		String word2 = "";
		for (int row = 0; row < 20; row++) {
			if (grid[row][columnnum].getText() != "" ) {
				word1 = word1 + grid[row][columnnum].getText();
			}
	}
		for (int column = 0; column < 20; column++) {
			if (grid[rownum][column].getText() != "" ) {
				word2 = word2 + grid[rownum][column].getText();
			}
	}
		String[] words = {word1,word2};
		String s = (String)JOptionPane.showInputDialog(
                this,
                "Words found are:\n"
               ,
                "Customized Dialog",
                JOptionPane.PLAIN_MESSAGE,
                null, words,
                word1);

		
		//TEMP trying out of voting
		//default icon, custom title
		Vote vote = new Vote(playerNums);
		int n = JOptionPane.showConfirmDialog(
		    this,
		    "Is this a Word?",
		    "Voting Process",
		    JOptionPane.YES_NO_OPTION);
		System.out.println(n);
		if(n == 1) {
			vote.voteNo();
		}else {
			vote.voteYes();
		}
		vote.votingCompleted();
		boolean check = vote.getResult();
		if (check == true) {
			int score = s.length();
//			scores[0].setText(gui.getPlayer()+":"+score);
			System.out.println(score);
		}else {
			JOptionPane.showMessageDialog(this, "Error", "Vote failed", n);
		}
		
	}
}

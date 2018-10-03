package scrabble;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;

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
	private GameView gameView;
	// Button to initiate vote
	private final JButton voteButton = new JButton("vote");



	private int playerNums;


	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * Create the panel.
	 */
	public board(GameView gameView) {
		this.gameView = gameView;
		this.alphabetPanel = gameView.getAlphabetPanel();
		setLayout(new BorderLayout());
		this.players = players;
		this.playerNums = 4;

		//create the bag and draw first hand.
		//this can be changed where a client has to send a request or something


		scores = new JLabel[players];
		initGUI();

		// set some padding between other components
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

				button.setFont(new Font("Arial", Font.BOLD, 20));

				//Row and column number can be sent to other players when it updates
				final int rownum = row;
				final int columnnum = column;
				button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {

					if (alphabetPanel.getTheChosenTile() != null) {
						setLetter = alphabetPanel.getTheChosenTile().getText();
					}

	            	if(button.getText().equals("") && !setLetter.equals("")) {
	            		button.setText(setLetter);
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

	/**
	 * we check characters that adjacent to the the chosen tile from
	 * four directions: up, down, left, right.
	 * @param rowNum
	 * @param columnNum
	 */
	public void checkScore(int rowNum,int columnNum) {

		int voteOrNot = JOptionPane.showConfirmDialog(
				this,
				"Is this a word?",
				"confirm",
				JOptionPane.YES_NO_OPTION);

		if (voteOrNot == JOptionPane.NO_OPTION) return;

		// The chosen tile is the starting point.
		// There is only two cases: the word may be in the same row
		// or in the same column.
		String columnWord = grid[rowNum][columnNum].getText();
		String rowWord = grid[rowNum][columnNum].getText();

		// Select characters below the target tile
		for (int row = rowNum + 1; row < 20; row++) {
			if (!grid[row][columnNum].getText().equals("") ) {
				columnWord = columnWord + grid[row][columnNum].getText();
			} else {
				break;
			}
		}

		// Select characters above the target tile
		for (int row = rowNum - 1; row > 0; row--) {
			if (!grid[row][columnNum].getText().equals("") ) {
				columnWord = grid[row][columnNum].getText() + columnWord;
			} else {
				break;
			}
		}

		// Select characters on the right of the target tile
		for (int column = columnNum + 1; column < 20; column++) {
			if (!grid[rowNum][column].getText().equals("") ) {
				rowWord = rowWord + grid[rowNum][column].getText();
			} else {
				break;
			}
		}

		// Select characters on the left of the target tile
		for (int column = columnNum - 1; column > 0; column--) {
			if (!grid[rowNum][column].getText().equals("") ) {
				rowWord = grid[rowNum][column].getText() + rowWord;
			} else {
				break;
			}
		}


		String[] words = {rowWord,columnWord};

		String s = (String)JOptionPane.showInputDialog(
                this,
                "Words found are:\n"
               ,
                "Customized Dialog",
                JOptionPane.PLAIN_MESSAGE,
                null, words,
				words[0]);

		
		//TEMP trying out of voting
		//default icon, custom title


		// Single player should not vote in reality
		// Just do this for testing
		Vote vote = new Vote(2);
		int n = JOptionPane.showConfirmDialog(
		    this,
		    "Do you accept this Word?",
		    "Voting Process",
		    JOptionPane.YES_NO_OPTION);
		System.out.println(n);
		if(n == 1) {
			vote.voteNo();
		}else {
			vote.voteYes();
		}
		if (vote.votingCompleted()) {

			if (vote.getResult() == true){
				int score = s.length();
				gameView.getGameInfoBoard().updateScore(gameView.getCurrentPlayer(), score);
//			scores[0].setText(gui.getPlayer()+":"+score);
				System.out.println(score);
			}else{
				JOptionPane.showMessageDialog(this, "Error", "Vote failed", n);
			}
		}
		
	}
}

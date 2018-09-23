package scrabble;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JTabbedPane;

public class board extends JPanel {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SQUARE_SIZE = 10;
	int players = 2;
	String setLetter = "";
	JPanel scorePanel = new JPanel();
	JLabel[] scores = null;
	JPanel rackPanel = new JPanel();
	JButton rackButton1 = new JButton();
	JButton rackButton2 = new JButton();
	JPanel gridPanel = new JPanel();
	JButton[][] grid = new JButton[20][20];
	JPanel[][] squares = new JPanel[20][20];
	private final JButton btnNewButton = new JButton("a");
	private final JButton btnNewButton_1 = new JButton("b");
	private final JButton btnNewButton_2 = new JButton("c");
	private final JButton btnNewButton_3 = new JButton("d");
	private final JButton btnNewButton_4 = new JButton("e");
	private final JButton btnNewButton_5 = new JButton("f");
	private final JButton btnNewButton_6 = new JButton("g");
	
	/**
	 * Create the panel.
	 */
	public board(int players) {
		setLayout(new BorderLayout());
		this.players = players;
		scores = new JLabel[players];
		initGUI();
		setVisible(true);
	
	}
	private void initGUI() {
		initGrid();
		initScorePanel();
		initRackPanel();
		add(gridPanel);
		add(scorePanel, BorderLayout.NORTH);
		add(rackPanel, BorderLayout.SOUTH);
		
	}

	private void initScorePanel() {
		scorePanel.setLayout(new GridLayout(1, players));
		for (int row = 0; row < players; row++) {	
			JLabel label = new JLabel("Player "+row+":");
			scores[row] =label;
			scorePanel.add(label);
		}
		
	}
	private void initRackPanel() {
		rackPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		rackPanel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setLetter = btnNewButton.getText();
            }
			});
		
		rackPanel.add(btnNewButton_1);
		
		rackPanel.add(btnNewButton_2);
		
		rackPanel.add(btnNewButton_3);
		
		rackPanel.add(btnNewButton_4);
		
		rackPanel.add(btnNewButton_6);
		
		rackPanel.add(btnNewButton_5);
	
	}

	private void initGrid() {
		gridPanel.setLayout(new GridLayout(20, 20));
		for (int row = 0; row < 20; row++) {
			for (int column = 0; column < 20; column++) {
				//Square square = board.getSquare(row, column);
				JPanel panel = new JPanel();
				JButton button = new JButton();
				final int rownum = row;
				final int columnnum = column;
				button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	JOptionPane.showMessageDialog(panel,
	            		    "grid location:"+rownum+columnnum);
	            	if(button.getText() == "" && setLetter != "") {
	            		button.setText(setLetter);
	            		setLetter = "";
	            		checkScore(rownum,columnnum);
	            	}
	            }
				});
				button.setBackground(Color.white);
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

		//If a string was returned, say so.
		int score = s.length();
		scores[0].setText("player 0:"+score);
		System.out.println(score);
	}
}

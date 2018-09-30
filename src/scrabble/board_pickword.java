package scrabble;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class board extends JPanel {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SQUARE_SIZE = 10;
	int players = 2;
	
	boolean startpick = false;
	boolean endpick = false;
	int pickx;
	int picky;
	int startx;
	int starty;
	int endx;
	int endy;
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
	AlphabetBag bag;
	ArrayList<String> hand = new ArrayList<String>();
	JButton tileChosen;
	private GUI_main gui;

	
	/**
	 * Create the panel.
	 * @param gui_main 
	 */
	public board(int players, GUI_main gui_main) {
		setLayout(new BorderLayout());
		this.players = players;
		gui = gui_main;
		//create the bag and draw first hand.
		//this can be changed where a client has to send a request or something
		this.bag = new AlphabetBag();
		this.hand = bag.firstDraw();
		initHand();
		scores = new JLabel[players];
		initGUI();
		setVisible(true);
	
	}
	//start first hand(display correct letters)
	private void initHand() {
		btnNewButton.setText(hand.get(0));
		btnNewButton_1.setText(hand.get(1));
		btnNewButton_2.setText(hand.get(2));
		btnNewButton_3.setText(hand.get(3));
		btnNewButton_4.setText(hand.get(4));
		btnNewButton_5.setText(hand.get(5));
		btnNewButton_6.setText(hand.get(6));
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
			//Can use arraylist or whatever to save all player names
			//For now just uses one player
			JLabel label = new JLabel(gui.getPlayer()+":");
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
            	tileChosen = btnNewButton;
            }
			});
		
		rackPanel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setLetter = btnNewButton_1.getText();
            	tileChosen = btnNewButton_1;
            }
			});
		
		rackPanel.add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setLetter = btnNewButton_2.getText();
            	tileChosen = btnNewButton_2;
            }
			});
		
		rackPanel.add(btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setLetter = btnNewButton_3.getText();
            	tileChosen = btnNewButton_3;
            }
			});
		
		rackPanel.add(btnNewButton_4);
		btnNewButton_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setLetter = btnNewButton_4.getText();
            	tileChosen = btnNewButton_4;
            }
			});
		
		rackPanel.add(btnNewButton_5);
		btnNewButton_5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setLetter = btnNewButton_5.getText();
            	tileChosen = btnNewButton_5;
            }
			});
		
		rackPanel.add(btnNewButton_6);
		btnNewButton_6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setLetter = btnNewButton_6.getText();
            	tileChosen = btnNewButton_6;
            }
			});
	
	}

	private void initGrid() {
		gridPanel.setLayout(new GridLayout(20, 20));
		for (int row = 0; row < 20; row++) {
			for (int column = 0; column < 20; column++) {
				//Square square = board.getSquare(row, column);
				JPanel panel = new JPanel();
				JButton button = new JButton();
				//Row and column number can be sent to other players when it updates
				final int rownum = row;
				final int columnnum = column;
				button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	JOptionPane.showMessageDialog(panel,
	            		    "grid location:"+rownum+" "+columnnum);
	            	
	            	if(startpick) {

	            		findStartPoint(rownum,columnnum,panel);
        				
            		}else if(endpick) {
            			findEndPoint(rownum,columnnum,panel);
            		}
            		else if(startpick == false && endpick == false && button.getText() == "" && setLetter != "") {
	            		button.setText(setLetter);
	            		setLetter = "";
	            		newTile(tileChosen);
            			pickx = rownum;
            			picky = columnnum;

            			startpick = true;
        				JOptionPane.showMessageDialog(panel,
    	            		    "pick a startpoint of your word "+pickx+" "+picky);
	            		//pickWord(rownum,columnnum);
	            		//checkScore(rownum,columnnum);
	            	}else {
        				JOptionPane.showMessageDialog(panel,
    	            		    "wrong!!!");
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

	
	public void newTile(JButton tile) {
		String letter = bag.getLetter();
		tile.setText(letter);
	}

	//Can be used to update board when we implement that
	public void updateBoard() {
		for (int row = 0; row < 20; row++) {
			for (int column = 0; column < 20; column++) {
				//JLabel label = grid[row][column];
			
		}
}
	}


	public void checkScore() {    
		//Find the word.
		String s = "";

		if(startx == endx) {
			System.out.printf("yo");
			for(int j = starty ;j <= endy ; j ++) {
				s += grid[startx][j].getText();
			}
			
		}else {
			for(int i = startx ;i <= endx ; i ++) {
				s += grid[i][starty].getText();
			}
		}
		JPanel panel = new JPanel();
		JOptionPane.showMessageDialog(panel,
    		    "The word is "+s);

		Vote vote = new Vote(gui.getPlayers());
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
			scores[0].setText(gui.getPlayer()+":"+score);
			System.out.println(score);
		}else {
			JOptionPane.showMessageDialog(this, "Error", "Vote failed", n);
		}

		//
		//TEMP trying out of voting
		//default icon, custom title

		
	}
	public void findStartPoint(int rownum, int columnnum,JPanel panel) {
		if(rownum == pickx && columnnum == picky) { //startpoint or endpoint is the same as pickpoint
			startx = rownum;
			starty = columnnum;
			startpick = false;
			endpick = true;
		
		}else if(rownum == pickx && columnnum < picky && checkEmpty(rownum,columnnum) ) {
			startx = rownum;
			starty = columnnum;
			startpick = false;
			endpick = true;
		}else if(rownum < pickx && columnnum == picky && checkEmpty(rownum,columnnum)) {
			startx = rownum;
			starty = columnnum; 
			startpick = false;
			endpick = true;
		}else {
			JOptionPane.showMessageDialog(panel,
        		    "Wrong startpoint!!!");
		}
		if(startpick == false) {
			JOptionPane.showMessageDialog(panel,
        		    "pick a endpoint of your word from start point "+startx+" "+starty);
		}
	
	}
	public void findEndPoint(int rownum, int columnnum,JPanel panel) {
		if(rownum == pickx && columnnum == picky) { //startpoint or endpoint is the same as pickpoint
			endx = rownum;
			endy = columnnum;
			endpick = false;
		
		}else if(rownum == pickx && columnnum > picky && checkEmpty(rownum,columnnum) ) {
			endx = rownum;
			endy= columnnum;
			endpick = false;
		}else if(rownum > pickx && columnnum == picky && checkEmpty(rownum,columnnum) ) {
			endx= rownum;
			endy= columnnum; 
			endpick = false;
		}else {
			JOptionPane.showMessageDialog(panel,
        		    "Wrong endpoint!!!");
		}
		if(endpick == false) {
			JOptionPane.showMessageDialog(panel,
        		    "Startpoint:"+startx+" "+starty+", endpoint:"+endx+" "+endy);
			checkScore();
		}
	
	}
	public boolean checkEmpty(int rownum, int columnnum) {

		if(columnnum == picky) {
			if(rownum < pickx ) {//startpoint
				for(int i = rownum; i < pickx;i++) {
					if (grid[i][columnnum].getText() == "") {
						return false;
					}
				}	
			}else {//endpoint
				for(int i = rownum; i > pickx;i--) {
					if (grid[i][columnnum].getText() == "") {
						return false;
					}
				}
			}
			
		}else if(rownum == pickx){
			if(columnnum < picky) {//startpoint
				for(int j = columnnum; j < picky; j++) {
					if (grid[rownum][j].getText() == "") {
						return false;
					}
				}
			}else {//endpoint
				for(int j = columnnum; j > picky; j--) {
					if (grid[rownum][j].getText() == "") {
						return false;
					}
				}
			}

		}
		return true;
	}
}

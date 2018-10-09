package scrabble;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameInfoBoard extends JPanel{


    private JPanel controlPanel = new JPanel();
    private JPanel playerListPanel = new JPanel();
    private JPanel scoreListPanel = new JPanel();
    private JPanel statusPanel = new JPanel();
    private JLabel statusLabel = new JLabel();
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 400;

    private Game game;
    private ArrayList<String> members;
    private HashMap<String, JLabel> playerScoreMap;

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }


    public GameInfoBoard (Game game) {
        setLayout(new BorderLayout());

        playerListPanel.setLayout( new GridLayout(8,1));
        scoreListPanel.setLayout( new GridLayout(8,1));

        this.add(playerListPanel, BorderLayout.WEST);
        this.add(scoreListPanel, BorderLayout.EAST);

        JLabel playerName;
        JLabel score;

        playerName = new JLabel("Players");
        score = new JLabel("Score");

        playerName.setFont(new Font("Courier New", Font.BOLD, 18));
        score.setFont(new Font("Courier New", Font.BOLD, 18));
        playerListPanel.add(playerName);
        scoreListPanel.add(score);

        playerListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 0));
        scoreListPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 30));

        statusLabel = new JLabel();
        changeStatus(false);
        statusPanel.add(statusLabel);
        statusPanel.setBackground(Color.lightGray);
        this.add(statusPanel, BorderLayout.NORTH);

        initControlButton();
        this.playerScoreMap = new HashMap<>();
        this.game = game;

    }

    public void initPlayerInfo() {
        this.members = game.playerList;
        for (int i = 0; i < members.size(); i ++) {

            JLabel memberName = new JLabel(members.get(i));
            memberName.setFont(new Font("Arial", Font.BOLD, 15));
            playerListPanel.add(memberName);

            JLabel score = new JLabel("0");
            score.setFont(new Font("Arial", Font.BOLD, 15));
            scoreListPanel.add(score);

            playerScoreMap.put(members.get(i), score);
        }
    }

    //check who has highest score(can return multiple people who share highest score)
    public ArrayList<String> checkWinner() {
    	ArrayList<String> winners = new ArrayList<String>();
    	int highscore = 0;

    	for (Map.Entry<String, JLabel> entry : playerScoreMap.entrySet())
    	{
    	    int score = Integer.parseInt(entry.getValue().getText());
    		
    	    if (score > highscore)
    	    {
    	    	highscore = score;
    	    	winners.clear();
    	        winners.add(entry.getKey());
    	    }else if(score == highscore) {
    	    	winners.add(entry.getKey());
    	    }
    	   
    	}
    	return winners;
    }

    public void updateScore(String playerName, int points) {
        if (!game.isGameRunning()) return;
        int currentScore = Integer.parseInt(playerScoreMap.get(playerName).getText());
        playerScoreMap.get(playerName).setText("" + (currentScore + points));
    }


    // init vote button and pass button
    private void initControlButton() {

        JButton voteBtn = new JButton("vote");
        voteBtn.setFont(new Font("Arial", Font.BOLD, 16));
        voteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!game.isPlayerTurn()) {
                    JOptionPane.showConfirmDialog(
                            game.getGameInfoBoard(),
                            "Please wait for your turn",
                            "confirm",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }

                if (!game.getGameBoard().isLetterPlaced()) {
                    JOptionPane.showConfirmDialog(
                            game.getGameInfoBoard(),
                            "Please place one tile",
                            "confirm",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }

                int row = game.getGameBoard().getSelectedRow();
                int column = game.getGameBoard().getSelectedColumn();
                game.getGameBoard().getWord(row,column);
                // if there are multiple players, they have to play by turn
                if (game.playerList.size() != 1) {

                    game.setPlayerTurn(false);

                    if (game.isHost()) {
                        game.notifyNextPlayer(game.getPlayerNextTurn(game.getCurrentPlayer()));
                    } else {
                        game.notifyCompletedTurn(false);
                    }

                }
                game.getGameBoard().setLetterPlaced(false);
            }

        });


        JButton passBtn = new JButton("pass");
        passBtn.setFont(new Font("Arial", Font.BOLD, 16));
        passBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!game.isPlayerTurn()) {
                    JOptionPane.showConfirmDialog(
                            game.getGameInfoBoard(),
                            "Please wait for your turn",
                            "confirm",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }

                if (game.playerList.size() != 1) {

                    game.setPlayerTurn(false);

                    if (game.isHost()) {
                        game.addPass();
                        game.notifyNextPlayer(game.getPlayerNextTurn(game.getCurrentPlayer()));
                    } else {
                        // if player didn't place any letter
                        if (!game.getGameBoard().isLetterPlaced()) {
                            game.notifyCompletedTurn(true);
                        } else {
                            game.notifyCompletedTurn(false);
                        }
                    }
                }
                // reset status
                game.getGameBoard().setLetterPlaced(false);
            }

        });


        controlPanel.add(voteBtn);
        // just add some space here
        controlPanel.add(Box.createRigidArea(new Dimension(5,0)));
        controlPanel.add(passBtn);
        this.add(controlPanel, BorderLayout.SOUTH);
    }

    // use boolean value to represent whether it is current player's turn
    public void changeStatus(boolean turn) {
        if (turn) {
            statusLabel.setText("Your Turn");
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 26));
            statusLabel.setForeground(Color.blue);
        } else {
            statusLabel.setText("Wait");
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 26));
            statusLabel.setForeground(Color.RED);
        }
        statusPanel.revalidate();
    }
}

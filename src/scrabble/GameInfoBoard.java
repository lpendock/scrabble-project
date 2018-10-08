package scrabble;


import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameInfoBoard extends JPanel{


    private JPanel controlPanel = new JPanel();
    private JPanel playerListPanel = new JPanel();
    private JPanel scoreListPanel = new JPanel();

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

//        setBorder(BorderFactory.createEmptyBorder(10, 10, 200, 30));
        playerListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 0));
        scoreListPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 30));

        initControlButton();
        this.playerScoreMap = new HashMap<>();
        this.game = game;

    }

    public void initPlayerInfo() {
        this.members = game.getMembersList();
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
        int currentScore = Integer.parseInt(playerScoreMap.get(playerName).getText());
        playerScoreMap.get(playerName).setText("" + (currentScore + points));
    }

    private void initControlButton() {

        JButton voteBtn = new JButton("vote");
        voteBtn.setFont(new Font("Arial", Font.BOLD, 16));

        JButton passBtn = new JButton("pass");
        passBtn.setFont(new Font("Arial", Font.BOLD, 16));


        controlPanel.add(voteBtn);
        // just add some space here
        controlPanel.add(Box.createRigidArea(new Dimension(5,0)));
        controlPanel.add(passBtn);
        this.add(controlPanel, BorderLayout.SOUTH);
    }
}

package scrabble;


import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameInfoBoard extends JPanel{

    private JLabel playerName;
    private JLabel score;
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

        add(playerListPanel, BorderLayout.WEST);
        add(scoreListPanel, BorderLayout.EAST);


        playerName = new JLabel("Players");
        score = new JLabel("Score");

        playerName.setFont(new Font("Courier New", Font.BOLD, 18));
        score.setFont(new Font("Courier New", Font.BOLD, 18));
        playerListPanel.add(playerName);
        scoreListPanel.add(score);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 200, 30));

        this.playerScoreMap = new HashMap<>();
        this.game = game;

    }

    public void initPlayerInfo() {
        this.members = game.getMembersList();
        for (int i = 0; i < members.size(); i ++) {

            JLabel memberName = new JLabel(members.get(i));
            memberName.setFont(new Font("Courier New", Font.BOLD, 15));
            playerListPanel.add(memberName);

            JLabel score = new JLabel("0");
            score.setFont(new Font("Courier New", Font.BOLD, 15));
            scoreListPanel.add(score);

            playerScoreMap.put(members.get(i), score);
        }
    }


    public void updateScore(String playerName, int points) {
        int currentScore = Integer.parseInt(this.playerScoreMap.get(playerName).getText());
        this.playerScoreMap.get(playerName).setText("" + (currentScore + points));
    }
}

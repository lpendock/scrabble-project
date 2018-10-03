package scrabble;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameInfoBoard extends JPanel{

    JLabel playerName;
    JLabel score;
    JPanel panel = new JPanel();
    JPanel playerListPanel = new JPanel();
    JPanel scoreListPanel = new JPanel();

    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 400;

    private ArrayList<String> members;
    private HashMap<String, JLabel> playerScoreMap;

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }


    GameInfoBoard (GameView gameView) {
        setLayout(new BorderLayout());

        playerListPanel.setLayout( new GridLayout(8,1));
        scoreListPanel.setLayout( new GridLayout(8,1));

        add(playerListPanel, BorderLayout.WEST);
        add(scoreListPanel, BorderLayout.EAST);


        playerName = new JLabel("Players");
        score = new JLabel("Score");

        playerListPanel.add(playerName);
        scoreListPanel.add(score);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 200, 30));

        this.playerScoreMap = new HashMap<>();
        this.members = gameView.getMembers();

    }

    public void initPlayerInfo() {
        for (int i = 0; i < members.size(); i ++) {
            playerListPanel.add(new JLabel(members.get(i)));
            JLabel score = new JLabel("0");
            scoreListPanel.add(score);

            playerScoreMap.put(members.get(i), score);
        }
    }


    public void updateScore(String playerName, int points) {
        int currentScore = Integer.parseInt(this.playerScoreMap.get(playerName).getText());
        this.playerScoreMap.get(playerName).setText("" + (currentScore + points));
    }
}
